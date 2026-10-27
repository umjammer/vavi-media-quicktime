/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * QuickDrawManager.
 * <p>
 * Offscreen QuickDraw: graphics worlds, pix maps, graphics devices, regions, pictures and drawing with Java 2D.
 * Structures are laid out as on the real toolbox so that QuickTime for Java can read them:
 * <ul>
 * <li>a GWorld is a 108 byte CGrafPort: portPixMap at 2, portVersion (0xC001 for offscreen) at 6,
 *     portRect at 16, visRgn at 24, clipRgn at 28, colors at 36/42, pen at 48..66, text at 68..76</li>
 * <li>a PixMap handle holds the 50 byte PixMap: baseAddr, rowBytes, bounds, ..., pixelSize at 32, pixelFormat at 38</li>
 * <li>a GDevice handle holds the 62 byte GDevice: gdFlags at 20, gdPMap at 22, gdRect at 34</li>
 * </ul>
 * The pixels of a GWorld live in the emulated memory and are shared with a {@link BufferedImage} for drawing.
 */
public final class QuickDrawManager {

    private QuickDrawManager() {
    }

    // pixel formats
    static final int k1MonochromePixelFormat = 1;
    static final int k8IndexedPixelFormat = 8;
    static final int k16BE555PixelFormat = 16;
    static final int k24RGBPixelFormat = 24;
    static final int k32ARGBPixelFormat = 32;
    static final int k32BGRAPixelFormat = Bytes.fourCC("BGRA");
    static final int k32ABGRPixelFormat = Bytes.fourCC("ABGR");
    static final int k32RGBAPixelFormat = Bytes.fourCC("RGBA");
    static final int k8GrayPixelFormat = 40;

    // transfer modes
    static final int srcCopy = 0;
    static final int srcOr = 1;
    static final int srcXor = 2;
    static final int srcBic = 3;
    static final int patCopy = 8;
    static final int patOr = 9;
    static final int patXor = 10;

    static final int CGRAFPORT_SIZE = 108;
    static final int PIXMAP_SIZE = 50;
    static final int GDEVICE_SIZE = 62;

    /** a graphics port, onscreen ports are offscreen too since there is no screen to draw on */
    static final class Port {
        final int ref;
        int pixMap;
        int pixels;
        int width;
        int height;
        int rowBytes;
        int pixelFormat;
        int depth;
        int left;
        int top;
        int device;
        BufferedImage image;
        Shape recording;

        Port(int ref) {
            this.ref = ref;
        }

        int shortAt(int off) {
            return Heap.getShort(ref + off);
        }

        void putShort(int off, int v) {
            Heap.putShort(ref + off, (short) v);
        }

        int[] rgb(int off) {
            return new int[] {Heap.getShort(ref + off) & 0xffff, Heap.getShort(ref + off + 2) & 0xffff, Heap.getShort(ref + off + 4) & 0xffff};
        }

        Color color(int off) {
            int[] c = rgb(off);
            return new Color(c[0] >> 8, c[1] >> 8, c[2] >> 8);
        }

        int clipRgn() {
            return Heap.getInt(ref + 28);
        }

        int visRgn() {
            return Heap.getInt(ref + 24);
        }

        /** @return graphics drawing into the port, clipped */
        Graphics2D graphics() {
            if (image == null) {
                return null;
            }
            Graphics2D g = image.createGraphics();
            g.translate(-left, -top);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            Area clip = Regions.read(clipRgn());
            int vis = visRgn();
            if (vis != 0) {
                clip.intersect(Regions.read(vis));
            }
            g.setClip(clip);
            return g;
        }

        /** the alpha channel is opaque for drawing */
        void setColor(Graphics2D g, Color c, int mode) {
            switch (mode & 0x3f) {
                case srcXor, patXor -> {
                    g.setXORMode(Color.white);
                    g.setColor(Color.black);
                }
                case srcBic -> g.setColor(color(42));
                default -> g.setColor(c);
            }
            g.setComposite(AlphaComposite.Src);
        }
    }

    private static final Map<Integer, Port> ports = new HashMap<>();

    static synchronized Port port(int ref) {
        Port p = ports.get(ref);
        if (p == null) {
            throw new IllegalArgumentException("not a port: 0x" + Integer.toHexString(ref));
        }
        return p;
    }

    static synchronized Port portOrNull(int ref) {
        return ports.get(ref);
    }

    /** @return the port owning the PixMap handle, null if none */
    static synchronized Port portOfPixMap(int pixMap) {
        for (Port p : ports.values()) {
            if (p.pixMap == pixMap) {
                return p;
            }
        }
        return null;
    }

    private static int currentPort;
    private static int currentDevice;
    private static short qdError;

    // ---- pixel memory

    static int depthOf(int pixelFormat) {
        if (pixelFormat == k32BGRAPixelFormat || pixelFormat == k32ABGRPixelFormat || pixelFormat == k32RGBAPixelFormat) {
            return 32;
        }
        if (pixelFormat == k8GrayPixelFormat) {
            return 8;
        }
        return switch (pixelFormat) {
            case 1, 2, 4, 8, 16, 24, 32 -> pixelFormat;
            case 33, 34, 36, 40 -> pixelFormat - 32;
            default -> 32;
        };
    }

    /** @return an image sharing the pixel memory, null for formats Java 2D cannot share */
    static BufferedImage image(byte[] data, int width, int height, int rowBytes, int pixelFormat) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        int depth = depthOf(pixelFormat);
        DataBufferByte buffer = new DataBufferByte(data, data.length);
        SampleModel sm;
        ColorModel cm;
        if (depth == 32) {
            int[] offsets;
            if (pixelFormat == k32BGRAPixelFormat) {
                offsets = new int[] {2, 1, 0, 3};
            } else if (pixelFormat == k32ABGRPixelFormat) {
                offsets = new int[] {3, 2, 1, 0};
            } else if (pixelFormat == k32RGBAPixelFormat) {
                offsets = new int[] {0, 1, 2, 3};
            } else {
                offsets = new int[] {1, 2, 3, 0};
            }
            sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4, rowBytes, offsets);
            cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false, ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        } else if (depth == 24) {
            sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3, rowBytes, new int[] {0, 1, 2});
            cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false, ColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
        } else if (depth == 8) {
            byte[] gray = new byte[256];
            for (int i = 0; i < 256; i++) {
                // classic Mac 8 bit gray: 0 is white
                gray[i] = (byte) (pixelFormat == k8GrayPixelFormat ? i : 255 - i);
            }
            sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1, rowBytes, new int[] {0});
            cm = new IndexColorModel(8, 256, gray, gray, gray);
        } else if (depth == 1) {
            sm = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, width, height, 1, rowBytes, 0);
            cm = new IndexColorModel(1, 2, new byte[] {-1, 0}, new byte[] {-1, 0}, new byte[] {-1, 0});
        } else {
            return null;
        }
        WritableRaster raster = Raster.createWritableRaster(sm, buffer, null);
        return new BufferedImage(cm, raster, false, null);
    }

    static int rowBytes(int width, int depth) {
        return ((width * depth + 7) / 8 + 15) & ~15;
    }

    /** @return a new PixMap handle for the pixels */
    static int newPixMap(int pixels, int rowBytes, int left, int top, int right, int bottom, int pixelFormat) {
        int depth = depthOf(pixelFormat);
        int h = Heap.newHandle(PIXMAP_SIZE);
        int p = Heap.deref(h);
        Heap.putInt(p, pixels);
        Heap.putShort(p + 4, (short) (rowBytes | 0x8000));
        Heap.putShort(p + 6, (short) top);
        Heap.putShort(p + 8, (short) left);
        Heap.putShort(p + 10, (short) bottom);
        Heap.putShort(p + 12, (short) right);
        Heap.putShort(p + 14, (short) 4); // pmVersion, baseAddr is 32 bit clean
        Heap.putInt(p + 22, 72 << 16);
        Heap.putInt(p + 26, 72 << 16);
        Heap.putShort(p + 30, (short) (depth >= 16 ? 16 : 0)); // RGBDirect
        Heap.putShort(p + 32, (short) depth);
        Heap.putShort(p + 34, (short) (depth == 32 ? 3 : depth >= 16 ? 3 : 1));
        Heap.putShort(p + 36, (short) (depth == 32 || depth == 24 ? 8 : depth == 16 ? 5 : depth));
        Heap.putInt(p + 38, pixelFormat);
        return h;
    }

    /** @return a new offscreen port */
    static synchronized Port newPort(int pixelFormat, int left, int top, int right, int bottom) {
        int ref = Heap.newObject(null, CGRAFPORT_SIZE);
        Port port = new Port(ref);
        int width = Math.max(0, right - left);
        int height = Math.max(0, bottom - top);
        port.depth = depthOf(pixelFormat);
        port.pixelFormat = pixelFormat;
        port.rowBytes = rowBytes(width, port.depth);
        port.width = width;
        port.height = height;
        port.left = left;
        port.top = top;
        port.pixels = Heap.newPtr(Math.max(1, port.rowBytes * height));
        port.image = image(Heap.backing(port.pixels), width, height, port.rowBytes, pixelFormat);
        port.pixMap = newPixMap(port.pixels, port.rowBytes, left, top, right, bottom, pixelFormat);
        port.device = mainDevice();
        Heap.putInt(ref + 2, port.pixMap);
        Heap.putShort(ref + 6, (short) 0xc001);
        Heap.putShort(ref + 16, (short) top);
        Heap.putShort(ref + 18, (short) left);
        Heap.putShort(ref + 20, (short) bottom);
        Heap.putShort(ref + 22, (short) right);
        Heap.putInt(ref + 24, newRectRegion(left, top, right, bottom));
        Heap.putInt(ref + 28, newRectRegion(-32767, -32767, 32767, 32767));
        // black on white
        Heap.putShort(ref + 42, (short) 0xffff);
        Heap.putShort(ref + 44, (short) 0xffff);
        Heap.putShort(ref + 46, (short) 0xffff);
        Heap.putShort(ref + 52, (short) 1);
        Heap.putShort(ref + 54, (short) 1);
        Heap.putShort(ref + 56, (short) 8); // patCopy
        Heap.putShort(ref + 72, (short) 1); // srcOr
        Heap.putShort(ref + 74, (short) 12);
        ports.put(ref, port);
        return port;
    }

    static synchronized void disposePort(int ref) {
        Port p = ports.remove(ref);
        if (p == null) {
            return;
        }
        Heap.disposeHandle(Heap.getInt(ref + 24));
        Heap.disposeHandle(Heap.getInt(ref + 28));
        Heap.disposeHandle(p.pixMap);
        Heap.free(p.pixels);
        Heap.free(ref);
        if (currentPort == ref) {
            currentPort = 0;
        }
    }

    /** @return the current port and device */
    static synchronized int[] currentGWorld() {
        if (currentPort == 0) {
            currentPort = scratch();
        }
        if (currentDevice == 0) {
            currentDevice = mainDevice();
        }
        return new int[] {currentPort, currentDevice};
    }

    private static int scratch;

    private static synchronized int scratch() {
        if (scratch == 0) {
            scratch = newPort(k32ARGBPixelFormat, 0, 0, 1, 1).ref;
        }
        return scratch;
    }

    private static Port current() {
        return port(currentGWorld()[0]);
    }

    // ---- devices

    private static int mainDevice;

    static synchronized int mainDevice() {
        if (mainDevice == 0) {
            int w = 1024, h = 768;
            if (!GraphicsEnvironment.isHeadless()) {
                try {
                    java.awt.Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    w = d.width;
                    h = d.height;
                } catch (RuntimeException ignored) {
                }
            }
            int gd = Heap.newHandle(GDEVICE_SIZE);
            int p = Heap.deref(gd);
            Heap.putShort(p + 4, (short) 2); // directType
            // screenDevice, noDriver, gdDevType, ramInit, mainScreen, screenActive
            Heap.putShort(p + 20, (short) ((1 << 13) | (1 << 14) | (1 << 0) | (1 << 10) | (1 << 11) | (1 << 15)));
            Heap.putInt(p + 22, newPixMap(0, rowBytes(w, 32), 0, 0, w, h, k32ARGBPixelFormat));
            Heap.putShort(p + 34, (short) 0);
            Heap.putShort(p + 36, (short) 0);
            Heap.putShort(p + 38, (short) h);
            Heap.putShort(p + 40, (short) w);
            mainDevice = gd;
        }
        return mainDevice;
    }

    public static int GetMainDevice() {
        return mainDevice();
    }

    public static int GetDeviceList() {
        return mainDevice();
    }

    public static int GetNextDevice(int gd) {
        return 0;
    }

    public static int GetMaxDevice(byte[] rect) {
        return mainDevice();
    }

    public static synchronized int GetGDevice() {
        return currentGWorld()[1];
    }

    public static synchronized void SetGDevice(int gd) {
        currentDevice = gd;
    }

    public static void InitGDevice(short refNum, int mode, int gd) {
    }

    public static int TestDeviceAttribute(int gd, short attribute) {
        return (Heap.getShort(Heap.deref(gd) + 20) >> attribute) & 1;
    }

    public static void SetDeviceAttribute(int gd, short attribute, byte value) {
        int p = Heap.deref(gd) + 20;
        int flags = Heap.getShort(p);
        flags = value != 0 ? flags | (1 << attribute) : flags & ~(1 << attribute);
        Heap.putShort(p, (short) flags);
    }

    public static short GDGetScale(int gd, int[] scale, short[] flags) {
        scale[0] = 0x10000;
        if (flags != null && flags.length > 0) {
            flags[0] = 0;
        }
        return 0;
    }

    public static short GDHasScale(int gd, short depth, int[] scale) {
        scale[0] = 0x10000;
        return 0;
    }

    public static short GDSetScale(int gd, int scale, short flags) {
        return scale == 0x10000 ? 0 : (short) -50;
    }

    // ---- gworlds

    /** rect is top left bottom right */
    public static short QTNewGWorld(int[] gworld, int pixelFormat, byte[] rect, int ctab, int gd, int flags) {
        int top = Bytes.getShort(rect, 0), left = Bytes.getShort(rect, 2), bottom = Bytes.getShort(rect, 4), right = Bytes.getShort(rect, 6);
        if (right < left || bottom < top) {
            return qdErr(-50);
        }
        gworld[0] = newPort(pixelFormat == 0 ? k32ARGBPixelFormat : pixelFormat, left, top, right, bottom).ref;
        return qdErr(0);
    }

    /** a GWorld for an image description handle: width at 32, height at 34, depth at 82 */
    public static short NewImageGWorld(int[] gworld, int imageDescription, int flags) {
        byte[] d = Heap.handleBytes(imageDescription);
        int width = Bytes.getShort(d, 32);
        int height = Bytes.getShort(d, 34);
        gworld[0] = newPort(k32ARGBPixelFormat, 0, 0, width, height).ref;
        return qdErr(0);
    }

    public static void DisposeGWorld(int gworld) {
        disposePort(gworld);
    }

    public static synchronized void GetGWorld(int[] port, int[] gdh) {
        int[] c = currentGWorld();
        port[0] = c[0];
        if (gdh != null && gdh.length > 0) {
            gdh[0] = c[1];
        }
    }

    public static synchronized void SetGWorld(int port, int gdh) {
        if (port != 0) {
            currentPort = port;
            Port p = portOrNull(port);
            currentDevice = gdh != 0 ? gdh : p != null ? p.device : currentDevice;
        }
    }

    public static synchronized void GetPort(int[] port) {
        port[0] = currentGWorld()[0];
    }

    public static synchronized void SetPort(int port) {
        if (port != 0) {
            currentPort = port;
        }
    }

    public static void MacSetPort(int port) {
        SetPort(port);
    }

    public static int GetGWorldPixMap(int gworld) {
        Port p = portOrNull(gworld);
        return p != null ? p.pixMap : Heap.getInt(gworld + 2);
    }

    public static byte LockPixels(int pixMap) {
        return 1;
    }

    public static void UnlockPixels(int pixMap) {
    }

    public static int GetPixBaseAddr(int pixMap) {
        return Heap.getInt(Heap.deref(pixMap));
    }

    public static int GetPixBounds(int pixMap, byte[] rect) {
        Heap.read(Heap.deref(pixMap) + 6, rect, 0, 8);
        return 0;
    }

    public static int GetPortBounds(int port, byte[] rect) {
        Heap.read(port + 16, rect, 0, 8);
        return 0;
    }

    public static byte IsPortOffscreen(int port) {
        return (byte) ((Heap.getShort(port + 6) & 0xc001) == 0xc001 ? 1 : 0);
    }

    public static int GetPortClipRegion(int port, int rgn) {
        CopyRgn(Heap.getInt(port + 28), rgn);
        return rgn;
    }

    public static int GetPortVisibleRegion(int port, int rgn) {
        CopyRgn(Heap.getInt(port + 24), rgn);
        return rgn;
    }

    public static void DisposeMatte(int pixMap) {
        Heap.disposeHandle(pixMap);
    }

    public static void DisposeCTable(int ctab) {
        if (ctab != 0) {
            Heap.disposeHandle(ctab);
        }
    }

    public static short QDError() {
        return qdError;
    }

    private static short qdErr(int e) {
        qdError = (short) e;
        return (short) e;
    }

    public static void QDFlushPortBuffer(int port, int rgn) {
    }

    public static void BeginUpdate(int window) {
    }

    public static void EndUpdate(int window) {
    }

    public static void InvalRect(short[] rect) {
    }

    /** draws an image into the port, scaled into the rect (left top right bottom) */
    static void drawImage(int port, BufferedImage image, int left, int top, int right, int bottom, AffineTransform transform) {
        Port p = portOrNull(port);
        if (p == null) {
            return;
        }
        Graphics2D g = p.graphics();
        if (g == null) {
            return;
        }
        try {
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (transform != null) {
                g.drawImage(image, transform, null);
            } else {
                g.drawImage(image, left, top, right - left, bottom - top, null);
            }
        } finally {
            g.dispose();
        }
    }

    /** @return the image sharing the pixels of the port, null if the pixel format has no image, for components to paint */
    public static BufferedImage portImage(int port) {
        Port p = portOrNull(port);
        return p == null ? null : p.image;
    }

    /** @return a copy of the pixels of the port as an ARGB image */
    static BufferedImage snapshot(int port) {
        Port p = port(port);
        BufferedImage img = new BufferedImage(Math.max(1, p.width), Math.max(1, p.height), BufferedImage.TYPE_INT_ARGB);
        if (p.image != null) {
            Graphics2D g = img.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(p.image, 0, 0, null);
            g.dispose();
        }
        return img;
    }

    // ---- colors and pen

    public static void RGBForeColor(byte[] rgb) {
        Heap.write(currentGWorld()[0] + 36, rgb, 0, 6);
    }

    public static void RGBBackColor(byte[] rgb) {
        Heap.write(currentGWorld()[0] + 42, rgb, 0, 6);
    }

    public static void GetForeColor(byte[] rgb) {
        Heap.read(currentGWorld()[0] + 36, rgb, 0, 6);
    }

    public static void GetBackColor(byte[] rgb) {
        Heap.read(currentGWorld()[0] + 42, rgb, 0, 6);
    }

    public static void PenNormal() {
        Port p = current();
        p.putShort(52, 1);
        p.putShort(54, 1);
        p.putShort(56, patCopy);
    }

    /** pen size is v, h at 52 */
    public static void PenSize(short width, short height) {
        Port p = current();
        p.putShort(52, height);
        p.putShort(54, width);
    }

    public static void MoveTo(short h, short v) {
        Port p = current();
        p.putShort(48, v);
        p.putShort(50, h);
    }

    public static void Move(short dh, short dv) {
        Port p = current();
        p.putShort(48, p.shortAt(48) + dv);
        p.putShort(50, p.shortAt(50) + dh);
    }

    public static void LineTo(short h, short v) {
        Port p = current();
        int x0 = p.shortAt(50), y0 = p.shortAt(48);
        int pw = p.shortAt(54), ph = p.shortAt(52);
        // the pen hangs below and to the right of the path
        Path2D path = new Path2D.Double();
        path.moveTo(x0, y0);
        path.lineTo(h, v);
        Area a = new Area(new BasicStroke(1).createStrokedShape(path));
        a.add(new Area(new Rectangle(x0, y0, Math.max(1, pw), Math.max(1, ph))));
        a.add(new Area(new Rectangle(h, v, Math.max(1, pw), Math.max(1, ph))));
        if (pw > 1 || ph > 1) {
            Path2D thick = new Path2D.Double();
            thick.moveTo(x0, y0);
            thick.lineTo(h, v);
            thick.lineTo(h + pw, v);
            thick.lineTo(h + pw, v + ph);
            thick.lineTo(x0 + pw, y0 + ph);
            thick.lineTo(x0, y0 + ph);
            thick.closePath();
            a.add(new Area(thick));
        }
        fill(p, a, p.color(36), p.shortAt(56));
        p.putShort(48, v);
        p.putShort(50, h);
    }

    public static void MacLineTo(short h, short v) {
        LineTo(h, v);
    }

    public static void Line(short dh, short dv) {
        Port p = current();
        LineTo((short) (p.shortAt(50) + dh), (short) (p.shortAt(48) + dv));
    }

    public static void GetCPixel(short h, short v, byte[] rgb) {
        Port p = current();
        if (p.image == null || h < p.left || v < p.top || h >= p.left + p.width || v >= p.top + p.height) {
            return;
        }
        int argb = p.image.getRGB(h - p.left, v - p.top);
        Bytes.putShort(rgb, 0, ((argb >> 16) & 0xff) * 0x101);
        Bytes.putShort(rgb, 2, ((argb >> 8) & 0xff) * 0x101);
        Bytes.putShort(rgb, 4, (argb & 0xff) * 0x101);
    }

    public static void SetCPixel(short h, short v, byte[] rgb) {
        Port p = current();
        if (p.image == null || h < p.left || v < p.top || h >= p.left + p.width || v >= p.top + p.height) {
            return;
        }
        int argb = 0xff00_0000 | ((Bytes.getShort(rgb, 0) >> 8 & 0xff) << 16) | ((Bytes.getShort(rgb, 2) >> 8 & 0xff) << 8) | (Bytes.getShort(rgb, 4) >> 8 & 0xff);
        p.image.setRGB(h - p.left, v - p.top, argb);
    }

    // ---- shapes

    /** @return a rect top left bottom right as a rectangle */
    static Rectangle rect(byte[] r) {
        int top = Bytes.getShort(r, 0), left = Bytes.getShort(r, 2), bottom = Bytes.getShort(r, 4), right = Bytes.getShort(r, 6);
        return new Rectangle(left, top, right - left, bottom - top);
    }

    private static void fill(Port p, Shape s, Color c, int mode) {
        synchronized (QuickDrawManager.class) {
            if (openRgnArea != null) {
                openRgnArea.add(new Area(s));
                return;
            }
        }
        if (p.recording != null) {
            return;
        }
        Graphics2D g = p.graphics();
        if (g == null) {
            return;
        }
        try {
            p.setColor(g, c, mode);
            g.fill(s);
        } finally {
            g.dispose();
        }
    }

    /** @return the frame of the shape with the pen, inside the shape */
    private static Shape frame(Port p, Shape s) {
        int pw = Math.max(1, p.shortAt(54));
        int ph = Math.max(1, p.shortAt(52));
        Rectangle b = s.getBounds();
        Area outer = new Area(s);
        AffineTransform t = new AffineTransform();
        double sx = b.width > 2 * pw ? (b.width - 2.0 * pw) / b.width : 0;
        double sy = b.height > 2 * ph ? (b.height - 2.0 * ph) / b.height : 0;
        t.translate(b.x + pw, b.y + ph);
        t.scale(sx, sy);
        t.translate(-b.x, -b.y);
        outer.subtract(new Area(t.createTransformedShape(s)));
        return outer;
    }

    private static void paint(Shape s) {
        Port p = current();
        fill(p, s, p.color(36), p.shortAt(56));
    }

    private static void erase(Shape s) {
        Port p = current();
        fill(p, s, p.color(42), patCopy);
    }

    private static void invert(Shape s) {
        Port p = current();
        fill(p, s, Color.black, patXor);
    }

    private static void frameShape(Shape s) {
        Port p = current();
        fill(p, frame(p, s), p.color(36), p.shortAt(56));
    }

    public static void PaintRect(byte[] r) {
        paint(rect(r));
    }

    public static void EraseRect(byte[] r) {
        erase(rect(r));
    }

    public static void FrameRect(byte[] r) {
        frameShape(rect(r));
    }

    public static void MacFrameRect(byte[] r) {
        FrameRect(r);
    }

    private static Shape oval(byte[] r) {
        Rectangle b = rect(r);
        return new Ellipse2D.Double(b.x, b.y, b.width, b.height);
    }

    public static void PaintOval(byte[] r) {
        paint(oval(r));
    }

    public static void EraseOval(byte[] r) {
        erase(oval(r));
    }

    public static void FrameOval(byte[] r) {
        frameShape(oval(r));
    }

    public static void InvertOval(byte[] r) {
        invert(oval(r));
    }

    private static Shape roundRect(byte[] r, short ovalWidth, short ovalHeight) {
        Rectangle b = rect(r);
        return new RoundRectangle2D.Double(b.x, b.y, b.width, b.height, ovalWidth, ovalHeight);
    }

    public static void PaintRoundRect(byte[] r, short ow, short oh) {
        paint(roundRect(r, ow, oh));
    }

    public static void EraseRoundRect(byte[] r, short ow, short oh) {
        erase(roundRect(r, ow, oh));
    }

    public static void FrameRoundRect(byte[] r, short ow, short oh) {
        frameShape(roundRect(r, ow, oh));
    }

    public static void InvertRoundRect(byte[] r, short ow, short oh) {
        invert(roundRect(r, ow, oh));
    }

    /** QuickDraw angles are clockwise from 12 o'clock */
    private static Shape arc(byte[] r, short startAngle, short arcAngle) {
        Rectangle b = rect(r);
        return new Arc2D.Double(b.x, b.y, b.width, b.height, 90 - startAngle, -arcAngle, Arc2D.PIE);
    }

    public static void PaintArc(byte[] r, short start, short angle) {
        paint(arc(r, start, angle));
    }

    public static void EraseArc(byte[] r, short start, short angle) {
        erase(arc(r, start, angle));
    }

    public static void InvertArc(byte[] r, short start, short angle) {
        invert(arc(r, start, angle));
    }

    public static void FrameArc(byte[] r, short start, short angle) {
        Rectangle b = rect(r);
        Port p = current();
        Graphics2D g = p.graphics();
        if (g == null) {
            return;
        }
        try {
            p.setColor(g, p.color(36), p.shortAt(56));
            g.setStroke(new BasicStroke(Math.max(1, p.shortAt(54))));
            g.draw(new Arc2D.Double(b.x, b.y, b.width - 1, b.height - 1, 90 - start, -angle, Arc2D.OPEN));
        } finally {
            g.dispose();
        }
    }

    // ---- polygons: polySize(2) polyBBox(8) points(v, h)...

    private static int openPoly;
    private static final List<int[]> polyPoints = new ArrayList<>();

    public static synchronized int OpenPoly() {
        openPoly = Heap.newHandle(10);
        polyPoints.clear();
        Port p = current();
        polyPoints.add(new int[] {p.shortAt(50), p.shortAt(48)});
        p.recording = new Rectangle();
        return openPoly;
    }

    public static synchronized void ClosePoly() {
        if (openPoly == 0) {
            return;
        }
        current().recording = null;
        writePoly(openPoly, polyPoints);
        openPoly = 0;
    }

    private static void writePoly(int h, List<int[]> pts) {
        int l = Integer.MAX_VALUE, t = Integer.MAX_VALUE, r = Integer.MIN_VALUE, b = Integer.MIN_VALUE;
        for (int[] pt : pts) {
            l = Math.min(l, pt[0]);
            r = Math.max(r, pt[0]);
            t = Math.min(t, pt[1]);
            b = Math.max(b, pt[1]);
        }
        byte[] d = new byte[10 + pts.size() * 4];
        Bytes.putShort(d, 0, d.length);
        Bytes.putShort(d, 2, pts.isEmpty() ? 0 : t);
        Bytes.putShort(d, 4, pts.isEmpty() ? 0 : l);
        Bytes.putShort(d, 6, pts.isEmpty() ? 0 : b);
        Bytes.putShort(d, 8, pts.isEmpty() ? 0 : r);
        for (int i = 0; i < pts.size(); i++) {
            Bytes.putShort(d, 10 + i * 4, pts.get(i)[1]);
            Bytes.putShort(d, 12 + i * 4, pts.get(i)[0]);
        }
        Heap.setHandleBytes(h, d);
    }

    private static List<int[]> readPoly(int h) {
        byte[] d = Heap.handleBytes(h);
        List<int[]> pts = new ArrayList<>();
        for (int i = 10; i + 4 <= d.length; i += 4) {
            pts.add(new int[] {Bytes.getShort(d, i + 2), Bytes.getShort(d, i)});
        }
        return pts;
    }

    private static Shape polygon(int h) {
        Path2D path = new Path2D.Double();
        List<int[]> pts = readPoly(h);
        for (int i = 0; i < pts.size(); i++) {
            if (i == 0) {
                path.moveTo(pts.get(i)[0], pts.get(i)[1]);
            } else {
                path.lineTo(pts.get(i)[0], pts.get(i)[1]);
            }
        }
        path.closePath();
        return path;
    }

    public static void KillPoly(int poly) {
        Heap.disposeHandle(poly);
    }

    public static void OffsetPoly(int poly, short dh, short dv) {
        List<int[]> pts = readPoly(poly);
        for (int[] pt : pts) {
            pt[0] += dh;
            pt[1] += dv;
        }
        writePoly(poly, pts);
    }

    public static void PaintPoly(int poly) {
        paint(polygon(poly));
    }

    public static void ErasePoly(int poly) {
        erase(polygon(poly));
    }

    public static void InvertPoly(int poly) {
        invert(polygon(poly));
    }

    public static void FramePoly(int poly) {
        Port p = current();
        Graphics2D g = p.graphics();
        if (g == null) {
            return;
        }
        try {
            p.setColor(g, p.color(36), p.shortAt(56));
            g.setStroke(new BasicStroke(Math.max(1, p.shortAt(54))));
            Path2D path = new Path2D.Double();
            List<int[]> pts = readPoly(poly);
            for (int i = 0; i < pts.size(); i++) {
                if (i == 0) {
                    path.moveTo(pts.get(i)[0], pts.get(i)[1]);
                } else {
                    path.lineTo(pts.get(i)[0], pts.get(i)[1]);
                }
            }
            g.draw(path);
        } finally {
            g.dispose();
        }
    }

    // ---- regions

    /** @return a new rectangular region handle */
    static int newRectRegion(int left, int top, int right, int bottom) {
        byte[] b = new byte[10];
        Bytes.putShort(b, 0, 10);
        if (right > left && bottom > top) {
            Bytes.putShort(b, 2, top);
            Bytes.putShort(b, 4, left);
            Bytes.putShort(b, 6, bottom);
            Bytes.putShort(b, 8, right);
        }
        return Heap.newHandle(b);
    }

    /** @return a copy of the region handle */
    static int copyRegion(int rgn) {
        return Heap.newHandle(Heap.handleBytes(rgn));
    }

    /** used by Matrix.transformRgn */
    static short transformRegion(int[] matrix, int rgn) {
        Area a = Regions.read(rgn);
        double[][] m = MatrixManager.toDoubles(matrix);
        if (m[0][2] != 0 || m[1][2] != 0) {
            return -50;
        }
        AffineTransform t = new AffineTransform(m[0][0], m[0][1], m[1][0], m[1][1], m[2][0], m[2][1]);
        Regions.write(rgn, a.createTransformedArea(t));
        return 0;
    }

    public static int NewRgn() {
        return newRectRegion(0, 0, 0, 0);
    }

    public static void DisposeRgn(int rgn) {
        if (rgn != 0) {
            Heap.disposeHandle(rgn);
        }
    }

    private static Area openRgnArea;

    /** shapes drawn until CloseRgn are added to the region */
    public static synchronized void OpenRgn() {
        openRgnArea = new Area();
    }

    public static synchronized void CloseRgn(int rgn) {
        Regions.write(rgn, openRgnArea == null ? new Area() : openRgnArea);
        openRgnArea = null;
    }

    public static void CopyRgn(int src, int dst) {
        if (src != dst) {
            Heap.setHandleBytes(dst, Heap.handleBytes(src));
        }
    }

    public static void MacCopyRgn(int src, int dst) {
        CopyRgn(src, dst);
    }

    public static void SetEmptyRgn(int rgn) {
        Heap.setHandleBytes(rgn, newRectBytes(0, 0, 0, 0));
    }

    private static byte[] newRectBytes(int left, int top, int right, int bottom) {
        byte[] b = new byte[10];
        Bytes.putShort(b, 0, 10);
        if (right > left && bottom > top) {
            Bytes.putShort(b, 2, top);
            Bytes.putShort(b, 4, left);
            Bytes.putShort(b, 6, bottom);
            Bytes.putShort(b, 8, right);
        }
        return b;
    }

    public static void SetRectRgn(int rgn, short left, short top, short right, short bottom) {
        Heap.setHandleBytes(rgn, newRectBytes(left, top, right, bottom));
    }

    public static void MacSetRectRgn(int rgn, short left, short top, short right, short bottom) {
        SetRectRgn(rgn, left, top, right, bottom);
    }

    public static void RectRgn(int rgn, byte[] r) {
        Rectangle b = rect(r);
        Heap.setHandleBytes(rgn, newRectBytes(b.x, b.y, b.x + b.width, b.y + b.height));
    }

    public static void OffsetRgn(int rgn, short dh, short dv) {
        byte[] b = Heap.handleBytes(rgn);
        if (b.length <= 10) {
            if (Bytes.getShort(b, 6) > Bytes.getShort(b, 2)) {
                Bytes.putShort(b, 2, Bytes.getShort(b, 2) + dv);
                Bytes.putShort(b, 4, Bytes.getShort(b, 4) + dh);
                Bytes.putShort(b, 6, Bytes.getShort(b, 6) + dv);
                Bytes.putShort(b, 8, Bytes.getShort(b, 8) + dh);
                Heap.setHandleBytes(rgn, b);
            }
            return;
        }
        Area a = Regions.read(rgn);
        a.transform(AffineTransform.getTranslateInstance(dh, dv));
        Regions.write(rgn, a);
    }

    public static void MacOffsetRgn(int rgn, short dh, short dv) {
        OffsetRgn(rgn, dh, dv);
    }

    public static void InsetRgn(int rgn, short dh, short dv) {
        Area a = Regions.read(rgn);
        if (Regions.isRectangular(rgn)) {
            Rectangle r = a.getBounds();
            r.grow(-dh, -dv);
            Heap.setHandleBytes(rgn, newRectBytes(r.x, r.y, r.x + Math.max(0, r.width), r.y + Math.max(0, r.height)));
            return;
        }
        Area shrunk = new Area(a);
        Rectangle b = a.getBounds();
        Area inv = new Area(new Rectangle(b.x - Math.abs(dh) - 1, b.y - Math.abs(dv) - 1, b.width + 2 * Math.abs(dh) + 2, b.height + 2 * Math.abs(dv) + 2));
        inv.subtract(a);
        if (dh > 0 || dv > 0) {
            for (Rectangle r : Regions.rectangles(inv)) {
                shrunk.subtract(new Area(new Rectangle(r.x - dh, r.y - dv, r.width + 2 * dh, r.height + 2 * dv)));
            }
        } else {
            for (Rectangle r : Regions.rectangles(a)) {
                shrunk.add(new Area(new Rectangle(r.x + dh, r.y + dv, r.width - 2 * dh, r.height - 2 * dv)));
            }
        }
        Regions.write(rgn, shrunk);
    }

    private static void op(int a, int b, int dst, java.util.function.BiConsumer<Area, Area> op) {
        Area x = Regions.read(a);
        op.accept(x, Regions.read(b));
        Regions.write(dst, x);
    }

    public static void SectRgn(int a, int b, int dst) {
        if (Regions.isRectangular(a) && Regions.isRectangular(b)) {
            int[] p = Regions.bounds(a), q = Regions.bounds(b);
            Heap.setHandleBytes(dst, newRectBytes(Math.max(p[0], q[0]), Math.max(p[1], q[1]), Math.min(p[2], q[2]), Math.min(p[3], q[3])));
            return;
        }
        op(a, b, dst, Area::intersect);
    }

    public static void UnionRgn(int a, int b, int dst) {
        op(a, b, dst, Area::add);
    }

    public static void MacUnionRgn(int a, int b, int dst) {
        UnionRgn(a, b, dst);
    }

    public static void DiffRgn(int a, int b, int dst) {
        op(a, b, dst, Area::subtract);
    }

    public static void XorRgn(int a, int b, int dst) {
        op(a, b, dst, Area::exclusiveOr);
    }

    public static void MacXorRgn(int a, int b, int dst) {
        XorRgn(a, b, dst);
    }

    public static void MapRgn(int rgn, byte[] src, byte[] dst) {
        Rectangle s = rect(src);
        Rectangle d = rect(dst);
        AffineTransform t = new AffineTransform();
        t.translate(d.x, d.y);
        t.scale(s.width == 0 ? 1 : d.width / (double) s.width, s.height == 0 ? 1 : d.height / (double) s.height);
        t.translate(-s.x, -s.y);
        Regions.write(rgn, Regions.read(rgn).createTransformedArea(t));
    }

    public static byte EmptyRgn(int rgn) {
        return (byte) (Regions.isEmpty(Regions.bounds(rgn)) ? 1 : 0);
    }

    public static byte EqualRgn(int a, int b) {
        return (byte) (java.util.Arrays.equals(Heap.handleBytes(a), Heap.handleBytes(b)) || Regions.read(a).equals(Regions.read(b)) ? 1 : 0);
    }

    public static byte MacEqualRgn(int a, int b) {
        return EqualRgn(a, b);
    }

    public static byte PtInRgn(int pt, int rgn) {
        int v = (short) (pt >> 16);
        int h = (short) pt;
        return (byte) (Regions.read(rgn).contains(h + 0.5, v + 0.5) ? 1 : 0);
    }

    public static byte RectInRgn(byte[] r, int rgn) {
        return (byte) (Regions.read(rgn).intersects(rect(r)) ? 1 : 0);
    }

    public static byte IsRegionRectangular(int rgn) {
        return (byte) (Regions.isRectangular(rgn) ? 1 : 0);
    }

    public static byte GetRegionBounds(int rgn, byte[] rect) {
        Heap.read(Heap.deref(rgn) + 2, rect, 0, 8);
        return 1;
    }

    /** bitmap: baseAddr(4) rowBytes(2) bounds(8), set bits (or opaque pixels of a pix map) make the region */
    public static short BitMapToRegion(int rgn, int bitmap) {
        int base = Heap.getInt(bitmap);
        int rowBytes = Heap.getShort(bitmap + 4) & 0x3fff;
        boolean pixMap = (Heap.getShort(bitmap + 4) & 0x8000) != 0;
        int top = Heap.getShort(bitmap + 6), left = Heap.getShort(bitmap + 8), bottom = Heap.getShort(bitmap + 10), right = Heap.getShort(bitmap + 12);
        int depth = pixMap ? Heap.getShort(bitmap + 32) : 1;
        Area a = new Area();
        for (int y = top; y < bottom; y++) {
            int start = -1;
            for (int x = left; x <= right; x++) {
                boolean set = false;
                if (x < right) {
                    int off = base + (y - top) * rowBytes;
                    if (depth == 1) {
                        set = (Heap.getByte(off + (x - left) / 8) & (0x80 >> ((x - left) % 8))) != 0;
                    } else if (depth == 32) {
                        set = (Heap.getInt(off + (x - left) * 4) & 0x00ff_ffff) != 0x00ff_ffff;
                    } else if (depth == 8) {
                        set = Heap.getByte(off + (x - left)) != 0;
                    }
                }
                if (set && start < 0) {
                    start = x;
                } else if (!set && start >= 0) {
                    a.add(new Area(new Rectangle(start, y, x - start, 1)));
                    start = -1;
                }
            }
        }
        Regions.write(rgn, a);
        return 0;
    }

    public static void PaintRgn(int rgn) {
        paint(Regions.read(rgn));
    }

    public static void MacPaintRgn(int rgn) {
        PaintRgn(rgn);
    }

    public static void EraseRgn(int rgn) {
        erase(Regions.read(rgn));
    }

    public static void FrameRgn(int rgn) {
        Port p = current();
        Area a = Regions.read(rgn);
        Area inner = new Area(a);
        int pw = Math.max(1, p.shortAt(54));
        int ph = Math.max(1, p.shortAt(52));
        Area outside = new Area(a.getBounds().getBounds2D());
        outside.add(new Area(new Rectangle(a.getBounds().x - pw, a.getBounds().y - ph, a.getBounds().width + 2 * pw, a.getBounds().height + 2 * ph)));
        outside.subtract(a);
        for (Rectangle r : Regions.rectangles(outside)) {
            inner.subtract(new Area(new Rectangle(r.x - pw + 1, r.y - ph + 1, r.width + 2 * pw - 2, r.height + 2 * ph - 2)));
        }
        Area frame = new Area(a);
        frame.subtract(inner);
        fill(p, frame, p.color(36), p.shortAt(56));
    }

    public static void MacFrameRgn(int rgn) {
        FrameRgn(rgn);
    }

    public static void GetClip(int rgn) {
        CopyRgn(current().clipRgn(), rgn);
    }

    public static void SetClip(int rgn) {
        CopyRgn(rgn, current().clipRgn());
    }

    public static void ClipRect(byte[] r) {
        Rectangle b = rect(r);
        Heap.setHandleBytes(current().clipRgn(), newRectBytes(b.x, b.y, b.x + b.width, b.y + b.height));
    }

    /** the rect is in memory */
    public static void ClipRect(int rectPtr) {
        ClipRect(Heap.read(rectPtr, 8));
    }

    // ---- text

    private static final String[] FONT_NAMES = {"Chicago", "Geneva", "New York", "Geneva", "Monaco", "Venice", "London", "Athens", "San Francisco", "Toronto"};

    private static final Map<String, Integer> fontIds = new HashMap<>();

    static {
        String[] classic = {"Chicago", "Geneva", "New York", "Geneva", "Monaco", "Venice", "London", "Athens", "San Francisco", "Toronto", "", "Cairo", "Los Angeles"};
        for (int i = 0; i < classic.length; i++) {
            if (!classic[i].isEmpty()) {
                fontIds.putIfAbsent(classic[i], i);
            }
        }
        fontIds.put("Times", 20);
        fontIds.put("Helvetica", 21);
        fontIds.put("Courier", 22);
        fontIds.put("Symbol", 23);
    }

    private static final Map<Integer, String> fontNames = new HashMap<>();

    static synchronized int fontId(String name) {
        Integer id = fontIds.computeIfAbsent(name, k -> 1024 + fontIds.size());
        fontNames.put(id, name);
        return id;
    }

    static synchronized String fontName(int id) {
        String n = fontNames.get(id);
        if (n != null) {
            return n;
        }
        for (Map.Entry<String, Integer> e : fontIds.entrySet()) {
            if (e.getValue() == id) {
                return e.getKey();
            }
        }
        return id >= 0 && id < FONT_NAMES.length ? FONT_NAMES[id] : "Dialog";
    }

    /** classic font names are mapped to logical fonts */
    private static Font font(Port p) {
        String name = fontName(p.shortAt(68));
        String family = switch (name) {
            case "Times", "New York" -> Font.SERIF;
            case "Courier", "Monaco" -> Font.MONOSPACED;
            case "Helvetica", "Geneva", "Chicago", "Dialog" -> Font.SANS_SERIF;
            default -> name;
        };
        int face = Heap.getByte(p.ref + 70);
        int style = ((face & 1) != 0 ? Font.BOLD : 0) | ((face & 2) != 0 ? Font.ITALIC : 0);
        int size = p.shortAt(74);
        return new Font(family, style, size <= 0 ? 12 : size);
    }

    private static FontMetrics metrics(Port p) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            return g.getFontMetrics(font(p));
        } finally {
            g.dispose();
        }
    }

    public static void TextFont(short font) {
        current().putShort(68, font);
    }

    public static void TextFace(short face) {
        Heap.putByte(current().ref + 70, (byte) face);
    }

    public static void TextMode(short mode) {
        current().putShort(72, mode);
    }

    public static void TextSize(short size) {
        current().putShort(74, size);
    }

    public static void GetFNum(byte[] name, short[] familyID) {
        familyID[0] = (short) fontId(Bytes.getPString(name, 0));
    }

    public static void GetFontName(short familyID, byte[] name) {
        Bytes.putPString(name, 0, fontName(familyID), name.length);
    }

    /** ascent, descent, widMax, leading */
    public static void GetFontInfo(short[] info) {
        FontMetrics m = metrics(current());
        info[0] = (short) m.getAscent();
        info[1] = (short) m.getDescent();
        info[2] = (short) m.getMaxAdvance();
        info[3] = (short) m.getLeading();
    }

    public static short TextWidth(byte[] text, short firstByte, short byteCount) {
        return (short) metrics(current()).stringWidth(new String(text, firstByte, byteCount, Bytes.MAC_ROMAN));
    }

    public static short CharWidth(short ch) {
        return (short) metrics(current()).charWidth(new String(new byte[] {(byte) ch}, Bytes.MAC_ROMAN).charAt(0));
    }

    /** the character locations are written as shorts into the int array memory */
    public static void MeasureText(short count, byte[] text, int[] locs) {
        FontMetrics m = metrics(current());
        String s = new String(text, 0, Math.min(count, text.length), Bytes.MAC_ROMAN);
        byte[] b = new byte[(count + 1) * 2];
        for (int i = 0; i <= count; i++) {
            Bytes.putShort(b, i * 2, m.stringWidth(s.substring(0, Math.min(i, s.length()))));
        }
        Bytes.toArray(b, 0, locs, 0, Math.min(b.length, locs.length * 4));
    }

    private static void drawString(String s) {
        Port p = current();
        Graphics2D g = p.graphics();
        if (g == null) {
            return;
        }
        try {
            g.setFont(font(p));
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int mode = p.shortAt(72);
            if (mode == srcCopy) {
                FontMetrics m = g.getFontMetrics();
                g.setColor(p.color(42));
                g.fillRect(p.shortAt(50), p.shortAt(48) - m.getAscent(), m.stringWidth(s), m.getAscent() + m.getDescent());
            }
            g.setColor(p.color(36));
            g.drawString(s, p.shortAt(50), p.shortAt(48));
            p.putShort(50, p.shortAt(50) + g.getFontMetrics().stringWidth(s));
        } finally {
            g.dispose();
        }
    }

    public static void DrawText(byte[] text, short firstByte, short byteCount) {
        drawString(new String(text, firstByte, byteCount, Bytes.MAC_ROMAN));
    }

    public static void MacDrawText(byte[] text, short firstByte, short byteCount) {
        DrawText(text, firstByte, byteCount);
    }

    public static void DrawChar(short ch) {
        drawString(new String(new byte[] {(byte) ch}, Bytes.MAC_ROMAN));
    }

    public static void StdText(short count, byte[] text, int numer, int denom) {
        DrawText(text, (short) 0, count);
    }

    /** @return the width, the font info is ascent, descent, widMax, leading */
    public static short StdTxMeas(short count, byte[] text, int[] numer, int[] denom, short[] info) {
        FontMetrics m = metrics(current());
        if (info != null && info.length >= 4) {
            info[0] = (short) m.getAscent();
            info[1] = (short) m.getDescent();
            info[2] = (short) m.getMaxAdvance();
            info[3] = (short) m.getLeading();
        }
        return (short) m.stringWidth(new String(text, 0, Math.min(count, text.length), Bytes.MAC_ROMAN));
    }

    public static short ITextAddString(int itext, int str, short regionTag, byte[] text) {
        throw new UnsupportedToolboxException("ITextAddString", "international text is not supported");
    }

    public static short ITextGetString(int itext, int str, short regionTag, short[] foundTag, byte[] text) {
        throw new UnsupportedToolboxException("ITextGetString", "international text is not supported");
    }

    public static short ITextRemoveString(int itext, int str, short regionTag, int flags) {
        throw new UnsupportedToolboxException("ITextRemoveString", "international text is not supported");
    }

    // ---- pictures

    /** @return a new PicHandle holding the image */
    static int newPict(BufferedImage image) {
        return Heap.newHandle(PictCodec.encode(image, 0, 0));
    }

    private static Port picturePort;
    private static int[] pictureFrame;
    private static int pictureSavedPort;
    private static int pictureHandle;

    /** recording draws into an offscreen port that becomes the picture */
    public static synchronized int OpenCPicture(byte[] params) {
        Rectangle r = rect(params);
        pictureSavedPort = currentGWorld()[0];
        picturePort = newPort(k32ARGBPixelFormat, r.x, r.y, r.x + r.width, r.y + r.height);
        Graphics2D g = picturePort.image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, r.width, r.height);
        g.dispose();
        pictureFrame = new int[] {r.x, r.y};
        currentPort = picturePort.ref;
        pictureHandle = Heap.newHandle(0);
        return pictureHandle;
    }

    public static synchronized void ClosePicture() {
        if (picturePort == null) {
            return;
        }
        BufferedImage img = snapshot(picturePort.ref);
        Heap.setHandleBytes(pictureHandle, PictCodec.encode(img, pictureFrame[0], pictureFrame[1]));
        disposePort(picturePort.ref);
        picturePort = null;
        currentPort = pictureSavedPort;
    }

    public static void KillPicture(int pict) {
        if (pict != 0) {
            Heap.disposeHandle(pict);
        }
    }

    public static void DrawPicture(int pict, byte[] dstRect) {
        BufferedImage img = PictCodec.decode(Heap.handleBytes(pict));
        if (img == null) {
            return;
        }
        Rectangle r = rect(dstRect);
        drawImage(currentGWorld()[0], img, r.x, r.y, r.x + r.width, r.y + r.height, null);
    }

    public static short DrawTrimmedPicture(int pict, byte[] frame, int trimMask, short doDither, int gEtc) {
        DrawPicture(pict, frame);
        return 0;
    }
}
