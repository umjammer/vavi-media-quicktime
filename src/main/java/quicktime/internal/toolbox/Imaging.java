/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


/**
 * Pixel maps, image data arguments and drawing for the image functions.
 * <p>
 * Image data arguments come as Java byte or int arrays (memory in big endian order) or as pointers into the heap.
 */
final class Imaging {

    private Imaging() {
    }

    // graphics modes
    static final int ditherCopy = 64;
    static final int blend = 32;
    static final int transparent = 36;
    static final int graphicsModeStraightAlpha = 256;
    static final int graphicsModePreWhiteAlpha = 257;
    static final int graphicsModePreBlackAlpha = 258;
    static final int graphicsModeComposition = 259;
    static final int graphicsModeStraightAlphaBlend = 260;
    static final int graphicsModePreMulColorAlpha = 261;
    static final int graphicsModePerComponentAlpha = 272;

    // ---- data arguments

    /** @return the number of bytes the data argument can hold */
    static int capacity(Object data) {
        return switch (data) {
            case byte[] b -> b.length;
            case int[] a -> a.length * 4;
            case short[] a -> a.length * 2;
            case Integer p -> p == 0 ? 0 : Heap.remaining(p);
            default -> 0;
        };
    }

    /** @return the bytes of the data argument, all of them if size is not positive */
    static byte[] read(Object data, int size) {
        int n = capacity(data);
        if (size > 0) {
            n = Math.min(n, size);
        }
        byte[] b = new byte[n];
        if (data instanceof Integer p) {
            Heap.read(p, b, 0, n);
        } else {
            Bytes.fromArray(data, 0, b, 0, n);
        }
        return b;
    }

    /** @return false if the data argument is too small */
    static boolean write(Object data, byte[] b) {
        if (capacity(data) < b.length) {
            return false;
        }
        if (data instanceof Integer p) {
            Heap.write(p, b, 0, b.length);
        } else {
            Bytes.toArray(b, 0, data, 0, b.length);
        }
        return true;
    }

    // ---- rects and matrices

    /** @return a QuickDraw rect top left bottom right as a rectangle */
    static Rectangle rect(byte[] r) {
        int top = Bytes.getShort(r, 0), left = Bytes.getShort(r, 2), bottom = Bytes.getShort(r, 4), right = Bytes.getShort(r, 6);
        return new Rectangle(left, top, right - left, bottom - top);
    }

    static void putRect(byte[] r, int o, Rectangle rc) {
        Bytes.putShort(r, o, rc.y);
        Bytes.putShort(r, o + 2, rc.x);
        Bytes.putShort(r, o + 4, rc.y + rc.height);
        Bytes.putShort(r, o + 6, rc.x + rc.width);
    }

    static byte[] rectBytes(Rectangle rc) {
        byte[] r = new byte[8];
        putRect(r, 0, rc);
        return r;
    }

    /** @return the 36 byte matrix as a transform */
    static AffineTransform transform(byte[] matrix) {
        if (matrix == null) {
            return new AffineTransform();
        }
        return transform(MatrixManager.read(matrix));
    }

    static AffineTransform transform(int[] m) {
        double[][] d = MatrixManager.toDoubles(m);
        return new AffineTransform(d[0][0], d[0][1], d[1][0], d[1][1], d[2][0], d[2][1]);
    }

    /** @return the transform mapping the source rect onto the destination rect */
    static AffineTransform mapRect(Rectangle src, Rectangle dst) {
        AffineTransform at = new AffineTransform();
        at.translate(dst.x, dst.y);
        at.scale(src.width == 0 ? 1 : dst.width / (double) src.width, src.height == 0 ? 1 : dst.height / (double) src.height);
        at.translate(-src.x, -src.y);
        return at;
    }

    static byte[] matrixBytes(AffineTransform at) {
        double[][] d = {{at.getScaleX(), at.getShearY(), 0}, {at.getShearX(), at.getScaleY(), 0}, {at.getTranslateX(), at.getTranslateY(), 1}};
        int[] m = MatrixManager.fromDoubles(d);
        byte[] b = new byte[36];
        for (int i = 0; i < 9; i++) {
            Bytes.putInt(b, i * 4, m[i]);
        }
        return b;
    }

    // ---- pixel maps

    /** the fields of a PixMap */
    static final class PixMapInfo {
        int base;
        int rowBytes;
        Rectangle bounds;
        int depth;
        int pixelFormat;
        int ctab;

        int[] palette() {
            if (ctab != 0 && Heap.isValid(ctab)) {
                try {
                    return Codecs.readColorTable(Heap.handleBytes(ctab), 0, 1 << Math.min(8, depth));
                } catch (RuntimeException e) {
                    // fall through
                }
            }
            return depth <= 8 ? Codecs.defaultPalette(depth) : null;
        }
    }

    /** @return the PixMap of a port, or the argument if it is a PixMap handle */
    static int pixMapOf(int portOrPixMap) {
        QuickDrawManager.Port p = QuickDrawManager.portOrNull(portOrPixMap);
        return p != null ? p.pixMap : portOrPixMap;
    }

    static PixMapInfo info(int pixMap) {
        int p = Heap.deref(pixMap);
        PixMapInfo i = new PixMapInfo();
        i.base = Heap.getInt(p);
        i.rowBytes = Heap.getShort(p + 4) & 0x3fff;
        int top = Heap.getShort(p + 6), left = Heap.getShort(p + 8), bottom = Heap.getShort(p + 10), right = Heap.getShort(p + 12);
        i.bounds = new Rectangle(left, top, right - left, bottom - top);
        boolean isPixMap = (Heap.getShort(p + 4) & 0x8000) != 0;
        i.depth = isPixMap ? Heap.getShort(p + 32) : 1;
        int format = isPixMap && Heap.getShort(p + 14) == 4 ? Heap.getInt(p + 38) : 0;
        i.pixelFormat = format != 0 ? format : i.depth;
        i.ctab = isPixMap ? Heap.getInt(p + 42) : 0;
        return i;
    }

    /** @return the port drawing into the pixel map, null if it has none */
    static QuickDrawManager.Port portOf(int pixMap) {
        QuickDrawManager.Port p = QuickDrawManager.portOrNull(pixMap);
        return p != null ? p : QuickDrawManager.portOfPixMap(pixMap);
    }

    /** @return an ARGB copy of the rect (in pixel map coordinates) of the pixel map, all of it if the rect is null */
    static BufferedImage read(int portOrPixMap, Rectangle src) {
        int pixMap = pixMapOf(portOrPixMap);
        QuickDrawManager.Port port = portOf(pixMap);
        BufferedImage whole;
        Rectangle bounds;
        if (port != null && port.image != null) {
            whole = port.image;
            bounds = new Rectangle(port.left, port.top, port.width, port.height);
        } else {
            PixMapInfo i = info(pixMap);
            bounds = i.bounds;
            byte[] bytes = Heap.read(i.base, Math.min(Heap.remaining(i.base), i.rowBytes * i.bounds.height));
            whole = QuickDrawManager.image(bytes, bounds.width, bounds.height, i.rowBytes, i.pixelFormat);
            if (whole == null || i.depth <= 16 && i.pixelFormat != QuickDrawManager.k8GrayPixelFormat && i.depth != 1) {
                whole = Codecs.decodeRaw(bytes, bounds.width, bounds.height, i.depth, i.rowBytes, i.palette());
            }
        }
        Rectangle r = src == null ? bounds : src.intersection(bounds);
        BufferedImage img = Codecs.argb(Math.max(1, r.width), Math.max(1, r.height));
        if (!r.isEmpty()) {
            Graphics2D g = img.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(whole, bounds.x - r.x, bounds.y - r.y, null);
            g.dispose();
        }
        if (whole.getColorModel().hasAlpha() && pixelsHaveNoAlpha(img)) {
            // 32 bit pixel maps commonly leave alpha 0
            int[] px = Codecs.pixels(img);
            for (int k = 0; k < px.length; k++) {
                px[k] |= 0xff00_0000;
            }
            img.setRGB(0, 0, img.getWidth(), img.getHeight(), px, 0, img.getWidth());
        }
        return img;
    }

    private static boolean pixelsHaveNoAlpha(BufferedImage img) {
        int[] px = Codecs.pixels(img);
        for (int p : px) {
            if ((p >>> 24) != 0) {
                return false;
            }
        }
        return true;
    }

    /** @return the bounds of the pixel map */
    static Rectangle bounds(int portOrPixMap) {
        int pixMap = pixMapOf(portOrPixMap);
        QuickDrawManager.Port port = portOf(pixMap);
        if (port != null) {
            return new Rectangle(port.left, port.top, port.width, port.height);
        }
        return info(pixMap).bounds;
    }

    /** @return the depth of the pixel map */
    static int depth(int portOrPixMap) {
        int pixMap = pixMapOf(portOrPixMap);
        QuickDrawManager.Port port = portOf(pixMap);
        return port != null ? port.depth : info(pixMap).depth;
    }

    /**
     * draws an image through a transform into a port or pixel map.
     * @param mode a graphics mode
     * @param opColor the 6 byte RGBColor for blend and transparent modes, may be null
     * @param mask a region clipping the drawing, may be 0
     */
    static void draw(int portOrPixMap, BufferedImage image, AffineTransform at, int mode, byte[] opColor, int mask) {
        int pixMap = pixMapOf(portOrPixMap);
        QuickDrawManager.Port port = portOf(pixMap);
        if (port != null && port.image != null) {
            Graphics2D g = port.graphics();
            try {
                if (mask != 0) {
                    Area clip = new Area(g.getClip());
                    clip.intersect(Regions.read(mask));
                    g.setClip(clip);
                }
                paint(g, image, at, mode, opColor);
            } finally {
                g.dispose();
            }
            return;
        }
        // a pixel map without a port: draw into a copy and write it back
        PixMapInfo i = info(pixMap);
        BufferedImage copy = read(pixMap, null);
        Graphics2D g = copy.createGraphics();
        try {
            g.translate(-i.bounds.x, -i.bounds.y);
            if (mask != 0) {
                g.setClip(Regions.read(mask));
            }
            paint(g, image, at, mode, opColor);
        } finally {
            g.dispose();
        }
        writeBack(i, copy);
    }

    /** writes an ARGB image into the pixel memory of the pixel map */
    private static void writeBack(PixMapInfo i, BufferedImage img) {
        int length = Math.min(Heap.remaining(i.base), i.rowBytes * i.bounds.height);
        byte[] bytes = Heap.read(i.base, length);
        BufferedImage view = i.depth >= 24 || i.pixelFormat == QuickDrawManager.k8GrayPixelFormat
                ? QuickDrawManager.image(bytes, i.bounds.width, i.bounds.height, i.rowBytes, i.pixelFormat) : null;
        if (view != null) {
            Graphics2D g = view.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.drawImage(img, 0, 0, null);
            g.dispose();
        } else {
            byte[] raw = Codecs.encodeRaw(img, i.depth, i.rowBytes, i.palette());
            System.arraycopy(raw, 0, bytes, 0, Math.min(raw.length, bytes.length));
        }
        Heap.write(i.base, bytes, 0, bytes.length);
    }

    /** paints the image with a graphics mode */
    static void paint(Graphics2D g, BufferedImage image, AffineTransform at, int mode, byte[] opColor) {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        BufferedImage src = image;
        switch (mode & 0xffff) {
            case graphicsModeStraightAlpha, graphicsModeStraightAlphaBlend, graphicsModeComposition, graphicsModePreMulColorAlpha,
                 graphicsModePerComponentAlpha -> {
                float a = mode == graphicsModeStraightAlphaBlend && opColor != null ? opWeight(opColor) : 1f;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            }
            case graphicsModePreWhiteAlpha, graphicsModePreBlackAlpha -> {
                src = unpremultiply(image, mode == graphicsModePreWhiteAlpha ? 0xff : 0);
                g.setComposite(AlphaComposite.SrcOver);
            }
            case blend -> {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opColor != null ? opWeight(opColor) : 0.5f));
                src = opaque(image);
            }
            case transparent -> {
                src = keyed(image, opColor != null ? rgb(opColor) : 0xffffff);
                g.setComposite(AlphaComposite.SrcOver);
            }
            case QuickDrawManager.srcOr -> {
                // black pixels of the source are drawn, white ones leave the destination
                src = keyed(opaque(image), 0xffffff);
                g.setComposite(AlphaComposite.SrcOver);
            }
            case QuickDrawManager.srcXor -> {
                g.setXORMode(Color.white);
                src = opaque(image);
            }
            case QuickDrawManager.srcBic -> {
                g.setComposite(new BicComposite());
                src = opaque(image);
            }
            default -> {
                g.setComposite(AlphaComposite.Src);
                src = opaque(image);
            }
        }
        g.drawImage(src, at, null);
    }

    private static float opWeight(byte[] opColor) {
        int r = Bytes.getShort(opColor, 0) & 0xffff, gr = Bytes.getShort(opColor, 2) & 0xffff, b = Bytes.getShort(opColor, 4) & 0xffff;
        return Math.max(0f, Math.min(1f, (r + gr + b) / (3f * 65535f)));
    }

    static int rgb(byte[] opColor) {
        return ((opColor[0] & 0xff) << 16) | ((opColor[2] & 0xff) << 8) | (opColor[4] & 0xff);
    }

    /** @return the image with alpha forced opaque, as copy modes ignore alpha */
    static BufferedImage opaque(BufferedImage image) {
        if (!image.getColorModel().hasAlpha()) {
            return image;
        }
        int[] px = Codecs.pixels(image);
        boolean all = true;
        for (int p : px) {
            if ((p >>> 24) != 0xff) {
                all = false;
                break;
            }
        }
        if (all) {
            return image;
        }
        for (int k = 0; k < px.length; k++) {
            px[k] |= 0xff00_0000;
        }
        return Codecs.image(px, image.getWidth(), image.getHeight());
    }

    private static BufferedImage keyed(BufferedImage image, int key) {
        int[] px = Codecs.pixels(image);
        for (int k = 0; k < px.length; k++) {
            if ((px[k] & 0x00ff_ffff) == key) {
                px[k] = 0;
            } else {
                px[k] |= 0xff00_0000;
            }
        }
        return Codecs.image(px, image.getWidth(), image.getHeight());
    }

    private static BufferedImage unpremultiply(BufferedImage image, int background) {
        int[] px = Codecs.pixels(image);
        for (int k = 0; k < px.length; k++) {
            int a = px[k] >>> 24;
            if (a == 0) {
                continue;
            }
            int c = px[k];
            int r = un((c >> 16) & 0xff, a, background), g = un((c >> 8) & 0xff, a, background), b = un(c & 0xff, a, background);
            px[k] = a << 24 | r << 16 | g << 8 | b;
        }
        return Codecs.image(px, image.getWidth(), image.getHeight());
    }

    private static int un(int c, int a, int bg) {
        int v = (c - bg * (255 - a) / 255) * 255 / a;
        return v < 0 ? 0 : Math.min(255, v);
    }

    /** srcBic: set source pixels clear the destination to the background (white) */
    private static final class BicComposite implements Composite {
        @Override
        public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
            return new CompositeContext() {
                @Override
                public void dispose() {
                }

                @Override
                public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
                    int w = Math.min(src.getWidth(), dstIn.getWidth()), h = Math.min(src.getHeight(), dstIn.getHeight());
                    Object sp = null, dp = null;
                    for (int y = 0; y < h; y++) {
                        for (int x = 0; x < w; x++) {
                            sp = src.getDataElements(x, y, sp);
                            dp = dstIn.getDataElements(x, y, dp);
                            int s = srcColorModel.getRGB(sp);
                            if ((s & 0x00ff_ffff) != 0x00ff_ffff && (s >>> 24) != 0) {
                                dp = dstColorModel.getDataElements(0xffff_ffff, dp);
                            }
                            dstOut.setDataElements(x, y, dp);
                        }
                    }
                }
            };
        }
    }
}
