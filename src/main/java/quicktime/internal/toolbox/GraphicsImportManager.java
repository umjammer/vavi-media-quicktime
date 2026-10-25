/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Graphics importer components ('grip') in Java, reading image files with {@link ImageFormats}.
 */
public final class GraphicsImportManager {

    private static final Logger logger = Logger.getLogger(GraphicsImportManager.class.getName());

    private GraphicsImportManager() {
    }

    static final int grip = Bytes.fourCC("grip");

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short fnfErr = -43;
    static final short ioErr = -36;
    static final short userCanceledErr = -128;
    static final short badComponentInstance = (short) 0x8001;
    static final short cantFindHandler = -2003;
    static final short codecBadDataErr = -8969;
    static final short noCodecErr = -8961;
    static final short invalidDataRef = -2014;

    // DoesDrawAllPixels
    static final short graphicsImporterDrawsAllPixels = 0;
    static final short graphicsImporterDoesntDrawAllPixels = 1;

    static void register() {
        for (ImageFormats.Format f : ImageFormats.formats.values()) {
            ComponentManager.register(grip, f.fileType(), 0, f.name(), f.name() + " graphics importer", c -> new Importer(f.fileType()));
        }
    }

    /** a graphics importer instance */
    static final class Importer {
        int format;
        // the source
        String path;
        int handle;
        int dataRef;
        int dataRefType;
        long offset;
        long limit = -1;
        byte[] cached;
        // the image
        int imageIndex = 1;
        BufferedImage image;
        // drawing
        Rectangle srcRect;
        int[] matrix;
        int clip;
        int mode = Imaging.ditherCopy;
        byte[] opColor = {(byte) 0x80, 0, (byte) 0x80, 0, (byte) 0x80, 0};
        int quality = Codecs.codecLosslessQuality;
        int flags;
        int port;
        int gdh;
        int exportSettings;

        Importer(int format) {
            this.format = format;
            int[] g = QuickDrawManager.currentGWorld();
            port = g[0];
            gdh = g[1];
        }

        void reset() {
            cached = null;
            image = null;
            imageIndex = 1;
            srcRect = null;
            matrix = null;
        }

        /** @return the bytes of the source */
        byte[] bytes() throws IOException {
            if (cached != null) {
                return cached;
            }
            byte[] all;
            if (path != null) {
                MdatWriter.flush(path);
                all = Files.readAllBytes(new File(path).toPath());
            } else if (handle != 0) {
                all = Heap.handleBytes(handle);
            } else if (dataRef != 0) {
                all = dataRefBytes(dataRef, dataRefType);
            } else {
                throw new IOException("no data source");
            }
            int from = (int) Math.min(all.length, Math.max(0, offset));
            int to = limit < 0 ? all.length : (int) Math.min(all.length, limit);
            cached = from == 0 && to == all.length ? all : Arrays.copyOfRange(all, from, Math.max(from, to));
            return cached;
        }

        BufferedImage image() throws IOException {
            if (image == null) {
                if (format == 0) {
                    format = ImageFormats.detect(bytes(), path);
                }
                image = ImageFormats.decode(format, bytes(), imageIndex);
            }
            return image;
        }

        Rectangle natural() throws IOException {
            BufferedImage img = image();
            return new Rectangle(0, 0, img.getWidth(), img.getHeight());
        }

        Rectangle source() throws IOException {
            return srcRect != null ? srcRect : natural();
        }

        int[] matrix() {
            return matrix != null ? matrix : MatrixManager.identity();
        }

        /** @return the bounds of the source rect through the matrix */
        Rectangle bounds() throws IOException {
            return Imaging.transform(matrix()).createTransformedShape(source()).getBounds();
        }
    }

    /** @return the bytes of the whole data of a data reference */
    static byte[] dataRefBytes(int dataRef, int dataRefType) throws IOException {
        DataRefRec d = MovieToolbox.dataRef(dataRef, dataRefType);
        if (d == null) {
            throw new IOException("no data reference");
        }
        if (d.type == DataRefRec.hndl) {
            return Heap.handleBytes(d.handle());
        }
        String p = d.path();
        if (p != null) {
            MdatWriter.flush(p);
            return Files.readAllBytes(new File(p).toPath());
        }
        if (d.type == DataRefRec.url) {
            try (InputStream is = URI.create(d.cString()).toURL().openStream()) {
                return is.readAllBytes();
            }
        }
        throw new IOException("unsupported data reference: " + Bytes.fourCC(d.type));
    }

    static Importer importer(int gi) {
        ComponentManager.InstanceRec i = ComponentManager.instance(gi);
        return i != null && i.impl instanceof Importer g ? g : null;
    }

    private static int open(int format) {
        ComponentManager.ComponentRec c = ComponentManager.find(grip, format);
        return c != null ? ComponentManager.open(c) : 0;
    }

    /** @return the format of the data, 0 if no importer can read it */
    private static int detect(byte[] b, String name) {
        int f = ImageFormats.detect(b, name);
        return f != 0 && ComponentManager.find(grip, f) != null ? f : 0;
    }

    private static byte[] head(String path) throws IOException {
        try (InputStream is = Files.newInputStream(new File(path).toPath())) {
            return is.readNBytes(1024);
        }
    }

    public static short GetGraphicsImporterForFile(byte[] fsspec, int[] gi) {
        return GetGraphicsImporterForFileWithFlags(fsspec, gi, 0);
    }

    public static short GetGraphicsImporterForFileWithFlags(byte[] fsspec, int[] gi, int flags) {
        String path = FileManager.specToPath(fsspec);
        gi[0] = 0;
        if (path == null || !new File(path).exists()) {
            return fnfErr;
        }
        try {
            int f = detect(head(path), path);
            if (f == 0) {
                return cantFindHandler;
            }
            gi[0] = open(f);
            Importer g = importer(gi[0]);
            g.path = path;
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static short GetGraphicsImporterForDataRef(int dataRef, int dataRefType, int[] gi) {
        return GetGraphicsImporterForDataRefWithFlags(dataRef, dataRefType, gi, 0);
    }

    public static short GetGraphicsImporterForDataRefWithFlags(int dataRef, int dataRefType, int[] gi, int flags) {
        gi[0] = 0;
        try {
            byte[] b = dataRefBytes(dataRef, dataRefType);
            DataRefRec d = MovieToolbox.dataRef(dataRef, dataRefType);
            String name = d.path() != null ? d.path() : d.type == DataRefRec.url ? d.cString() : null;
            int f = detect(b, name);
            if (f == 0) {
                return cantFindHandler;
            }
            gi[0] = open(f);
            Importer g = importer(gi[0]);
            g.dataRef = Heap.newHandle(Heap.handleBytes(dataRef));
            g.dataRefType = dataRefType;
            g.cached = b;
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "importer for data ref", e);
            return invalidDataRef;
        }
    }

    // ---- data source

    public static int GraphicsImportSetDataFile(int gi, byte[] fsspec) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.reset();
        g.path = FileManager.specToPath(fsspec);
        g.handle = 0;
        g.dataRef = 0;
        return g.path != null ? noErr : paramErr;
    }

    public static int GraphicsImportGetDataFile(int gi, byte[] fsspec) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        if (g.path == null) {
            return paramErr;
        }
        byte[] s = FileManager.pathToSpec(g.path);
        System.arraycopy(s, 0, fsspec, 0, Math.min(s.length, fsspec.length));
        return noErr;
    }

    public static int GraphicsImportSetDataHandle(int gi, int h) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.reset();
        g.handle = h;
        g.path = null;
        g.dataRef = 0;
        return noErr;
    }

    public static int GraphicsImportGetDataHandle(int gi, int[] h) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        h[0] = g.handle;
        return noErr;
    }

    public static int GraphicsImportSetDataReference(int gi, int dataRef, int dataRefType) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.reset();
        g.dataRef = Heap.newHandle(Heap.handleBytes(dataRef));
        g.dataRefType = dataRefType;
        g.path = null;
        g.handle = 0;
        return noErr;
    }

    /** files are referenced by an alias data reference */
    public static int GraphicsImportGetDataReference(int gi, int[] dataRef, int[] dataRefType) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        if (g.dataRef != 0) {
            dataRef[0] = Heap.newHandle(Heap.handleBytes(g.dataRef));
            dataRefType[0] = g.dataRefType;
        } else if (g.path != null) {
            dataRef[0] = Heap.newHandle(FileManager.newAliasRecord(g.path));
            dataRefType[0] = DataRefRec.alis;
        } else if (g.handle != 0) {
            byte[] b = new byte[4];
            Bytes.putInt(b, 0, g.handle);
            dataRef[0] = Heap.newHandle(b);
            dataRefType[0] = DataRefRec.hndl;
        } else {
            return paramErr;
        }
        return noErr;
    }

    public static int GraphicsImportGetAliasedDataReference(int gi, int[] dataRef, int[] dataRefType) {
        return GraphicsImportGetDataReference(gi, dataRef, dataRefType);
    }

    public static int GraphicsImportSetDataReferenceOffsetAndLimit(int gi, int offset, int limit) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.offset = offset & 0xffff_ffffL;
        g.limit = limit == -1 ? -1 : limit & 0xffff_ffffL;
        g.cached = null;
        g.image = null;
        return noErr;
    }

    public static int GraphicsImportGetDataReferenceOffsetAndLimit(int gi, int[] offset, int[] limit) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        offset[0] = (int) g.offset;
        limit[0] = (int) g.limit;
        return noErr;
    }

    /** the image data, for QuickTime image files the 'idat' contents */
    public static int GraphicsImportGetDataOffsetAndSize(int gi, int[] offset, int[] size) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            byte[] b = g.bytes();
            if (g.format == ImageFormats.qtif) {
                Object[] p = ImageFormats.qtifParts(b);
                if (p != null) {
                    offset[0] = (Integer) p[1];
                    size[0] = (Integer) p[2];
                    return noErr;
                }
            }
            offset[0] = 0;
            size[0] = b.length;
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    private static int readData(int gi, Object dst, int offset, int size) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            byte[] b = g.bytes();
            if (offset < 0 || offset + size > b.length) {
                return paramErr;
            }
            return Imaging.write(dst, Arrays.copyOfRange(b, offset, offset + size)) ? noErr : paramErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int GraphicsImportReadData(int gi, byte[] dst, int offset, int size) {
        return readData(gi, dst, offset, size);
    }

    public static int GraphicsImportReadData(int gi, int dst, int offset, int size) {
        return readData(gi, dst, offset, size);
    }

    public static int GraphicsImportReadData(int gi, int[] dst, int offset, int size) {
        return readData(gi, dst, offset, size);
    }

    // ---- the image

    public static int GraphicsImportValidate(int gi, int[] valid) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            g.image();
            valid[0] = 1;
        } catch (IOException | RuntimeException e) {
            valid[0] = 0;
        }
        return noErr;
    }

    public static int GraphicsImportGetNaturalBounds(int gi, byte[] rect) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            Imaging.putRect(rect, 0, g.natural());
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsImportGetImageDescription(int gi, int[] desc) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            desc[0] = Heap.newHandle(ImageFormats.description(g.format, g.bytes(), g.image()));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsImportGetImageCount(int gi, int[] count) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            count[0] = ImageFormats.count(g.format, g.bytes());
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int GraphicsImportSetImageIndex(int gi, int index) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        if (index < 1) {
            return paramErr;
        }
        g.imageIndex = index;
        g.image = null;
        return noErr;
    }

    public static int GraphicsImportGetImageIndex(int gi, int[] index) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        index[0] = g.imageIndex;
        return noErr;
    }

    public static int GraphicsImportDoesDrawAllPixels(int gi, short[] drawsAllPixels) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            drawsAllPixels[0] = g.image().getColorModel().hasAlpha() ? graphicsImporterDoesntDrawAllPixels : graphicsImporterDrawsAllPixels;
            return noErr;
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    /** no metadata is read from image files */
    public static int GraphicsImportGetMetaData(int gi, int userData) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        return noErr;
    }

    // ---- geometry

    public static int GraphicsImportSetSourceRect(int gi, byte[] rect) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.srcRect = rect != null ? Imaging.rect(rect) : null;
        return noErr;
    }

    public static int GraphicsImportGetSourceRect(int gi, byte[] rect) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            Imaging.putRect(rect, 0, g.source());
            return noErr;
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsImportGetDefaultSourceRect(int gi, byte[] rect) {
        return GraphicsImportGetNaturalBounds(gi, rect);
    }

    public static int GraphicsImportSetMatrix(int gi, byte[] matrix) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.matrix = matrix != null ? MatrixManager.read(matrix) : null;
        return noErr;
    }

    public static int GraphicsImportGetMatrix(int gi, byte[] matrix) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        int[] m = g.matrix();
        for (int i = 0; i < 9; i++) {
            Bytes.putInt(matrix, i * 4, m[i]);
        }
        return noErr;
    }

    public static int GraphicsImportGetDefaultMatrix(int gi, byte[] matrix) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        int[] m = MatrixManager.identity();
        for (int i = 0; i < 9; i++) {
            Bytes.putInt(matrix, i * 4, m[i]);
        }
        return noErr;
    }

    /** the matrix maps the source rect onto the rect */
    public static int GraphicsImportSetBoundsRect(int gi, byte[] rect) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            AffineTransform at = Imaging.mapRect(g.source(), Imaging.rect(rect));
            g.matrix = MatrixManager.read(Imaging.matrixBytes(at));
            return noErr;
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsImportGetBoundsRect(int gi, byte[] rect) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            Imaging.putRect(rect, 0, g.bounds());
            return noErr;
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsImportSetDestRect(int gi, byte[] rect) {
        return GraphicsImportSetBoundsRect(gi, rect);
    }

    public static int GraphicsImportGetDestRect(int gi, byte[] rect) {
        return GraphicsImportGetBoundsRect(gi, rect);
    }

    public static int GraphicsImportSetClip(int gi, int clip) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        if (g.clip != 0) {
            Heap.disposeHandle(g.clip);
        }
        g.clip = clip != 0 ? QuickDrawManager.copyRegion(clip) : 0;
        return noErr;
    }

    public static int GraphicsImportGetClip(int gi, int[] clip) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        clip[0] = g.clip;
        return noErr;
    }

    public static int GraphicsImportGetDefaultClip(int gi, int[] clip) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        clip[0] = 0;
        return noErr;
    }

    public static int GraphicsImportSetGraphicsMode(int gi, int mode, byte[] opColor) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.mode = mode;
        if (opColor != null) {
            g.opColor = opColor.clone();
        }
        return noErr;
    }

    public static int GraphicsImportGetGraphicsMode(int gi, int[] mode, byte[] opColor) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        mode[0] = g.mode;
        if (opColor != null) {
            System.arraycopy(g.opColor, 0, opColor, 0, Math.min(6, opColor.length));
        }
        return noErr;
    }

    /** images with alpha are drawn with straight alpha */
    public static int GraphicsImportGetDefaultGraphicsMode(int gi, int[] mode, byte[] opColor) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            mode[0] = g.image().getColorModel().hasAlpha() ? Imaging.graphicsModeStraightAlpha : Imaging.ditherCopy;
        } catch (IOException e) {
            mode[0] = Imaging.ditherCopy;
        }
        if (opColor != null) {
            Arrays.fill(opColor, 0, Math.min(6, opColor.length), (byte) 0x80);
        }
        return noErr;
    }

    public static int GraphicsImportSetQuality(int gi, int quality) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.quality = quality;
        return noErr;
    }

    public static int GraphicsImportGetQuality(int gi, int[] quality) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        quality[0] = g.quality;
        return noErr;
    }

    public static int GraphicsImportSetFlags(int gi, int flags) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.flags = flags;
        return noErr;
    }

    public static int GraphicsImportGetFlags(int gi, int[] flags) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        flags[0] = g.flags;
        return noErr;
    }

    public static int GraphicsImportSetGWorld(int gi, int port, int gdh) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        int[] cur = QuickDrawManager.currentGWorld();
        g.port = port != 0 ? port : cur[0];
        g.gdh = gdh != 0 ? gdh : cur[1];
        return noErr;
    }

    public static int GraphicsImportGetGWorld(int gi, int[] port, int[] gdh) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        port[0] = g.port;
        if (gdh != null && gdh.length > 0) {
            gdh[0] = g.gdh;
        }
        return noErr;
    }

    /** @return the image cropped to the source rect */
    static BufferedImage sourceImage(Importer g) throws IOException {
        BufferedImage img = g.image();
        Rectangle src = g.source().intersection(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
        return src.width == img.getWidth() && src.height == img.getHeight() ? img : img.getSubimage(src.x, src.y, src.width, src.height);
    }

    public static int GraphicsImportDraw(int gi) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            BufferedImage img = sourceImage(g);
            Rectangle src = g.source();
            AffineTransform at = Imaging.transform(g.matrix());
            at.translate(Math.max(0, src.x), Math.max(0, src.y));
            Imaging.draw(g.port, img, at, g.mode, g.opColor, g.clip);
            return noErr;
        } catch (IOException e) {
            logger.log(Level.FINE, "draw", e);
            return codecBadDataErr;
        } catch (RuntimeException e) {
            logger.log(Level.FINE, "draw", e);
            return paramErr;
        }
    }

    // ---- pictures and files

    /** @return the image drawn through the matrix into its bounds rect */
    static BufferedImage rendered(Importer g) throws IOException {
        BufferedImage img = sourceImage(g);
        if (g.matrix == null || Imaging.transform(g.matrix).isIdentity()) {
            return img;
        }
        Rectangle b = g.bounds();
        BufferedImage t = Codecs.argb(b.width, b.height);
        java.awt.Graphics2D gr = t.createGraphics();
        gr.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        AffineTransform at = new AffineTransform();
        at.translate(-b.x, -b.y);
        at.concatenate(Imaging.transform(g.matrix));
        at.translate(Math.max(0, g.source().x), Math.max(0, g.source().y));
        gr.drawImage(img, at, null);
        gr.dispose();
        return t;
    }

    /** the picture of the image drawn into its bounds rect */
    static byte[] picture(Importer g) throws IOException {
        Rectangle b = g.bounds();
        return PictCodec.encode(rendered(g), b.x, b.y);
    }

    public static int GraphicsImportGetAsPicture(int gi, int[] pict) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            pict[0] = Heap.newHandle(picture(g));
            return noErr;
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    private static int write(byte[] fsspec, byte[] b) {
        String p = FileManager.specToPath(fsspec);
        if (p == null) {
            return paramErr;
        }
        try {
            DataRefRec.closeReader(p);
            Files.write(new File(p).toPath(), b);
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int GraphicsImportSaveAsPicture(int gi, byte[] fsspec, int scriptTag) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            byte[] pict = picture(g);
            byte[] file = new byte[512 + pict.length];
            System.arraycopy(pict, 0, file, 512, pict.length);
            return write(fsspec, file);
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    /** the data is kept as it is if a codec can read it, else it is compressed again as PNG */
    public static int GraphicsImportSaveAsQuickTimeImageFile(int gi, byte[] fsspec, int scriptTag) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        try {
            byte[] b = g.bytes();
            BufferedImage img = g.image();
            byte[] desc = ImageFormats.description(g.format, b, img);
            byte[] data = b;
            if (g.format == ImageFormats.qtif) {
                Object[] p = ImageFormats.qtifParts(b);
                data = Arrays.copyOfRange(b, (Integer) p[1], (Integer) p[1] + (Integer) p[2]);
            } else if (Codecs.get(Bytes.getInt(desc, 4)) == null || g.format == ImageFormats.PICT || g.format == ImageFormats._8BPS || g.format == ImageFormats.PNTG) {
                byte[][] out = ImageCompressionManager.compress(img, null, Codecs.get(Bytes.fourCC("png ")), ImageFormats.depthOf(img), Codecs.codecLosslessQuality);
                desc = out[0];
                data = out[1];
            }
            return write(fsspec, ImageFormats.qtif(desc, data));
        } catch (IOException e) {
            return codecBadDataErr;
        }
    }

    public static short GraphicsImportExportImageFile(int gi, int fileType, int fileCreator, byte[] fsspec, short scriptTag) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        int[] ge = new int[1];
        short e = ComponentManager.OpenADefaultComponent(GraphicsExportManager.grex, fileType, ge);
        if (e != noErr) {
            return e;
        }
        try {
            int r = GraphicsExportManager.GraphicsExportSetInputGraphicsImporter(ge[0], gi);
            if (r == noErr) {
                r = GraphicsExportManager.GraphicsExportSetOutputFile(ge[0], fsspec);
            }
            if (r == noErr) {
                r = GraphicsExportManager.GraphicsExportDoExport(ge[0], new int[1]);
            }
            return (short) r;
        } finally {
            ComponentManager.CloseComponent(ge[0]);
        }
    }

    /** the export types as 'expo' atoms with 'ftyp', 'mime', 'ext ' and 'desc' children */
    public static int GraphicsImportGetExportImageTypeList(int gi, int[] container) {
        if (importer(gi) == null) {
            return badComponentInstance;
        }
        int[] ac = new int[1];
        AtomManager.QTNewAtomContainer(ac);
        int id = 1;
        for (ImageFormats.Format f : ImageFormats.formats.values()) {
            if (!f.canExport()) {
                continue;
            }
            int[] parent = new int[1];
            AtomManager.QTInsertChild(ac[0], 0, Bytes.fourCC("expo"), id++, (short) 0, 0, (byte[]) null, parent);
            AtomManager.QTInsertChild(ac[0], parent[0], Bytes.fourCC("ftyp"), 1, (short) 0, 4, ImageCompressionManager.int4(f.fileType()), new int[1]);
            mimeAtoms(ac[0], parent[0], f, 1);
        }
        container[0] = ac[0];
        return noErr;
    }

    static void mimeAtoms(int ac, int parent, ImageFormats.Format f, int id) {
        byte[] mime = f.mime().getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        byte[] ext = f.extension().getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        byte[] desc = f.name().getBytes(java.nio.charset.StandardCharsets.US_ASCII);
        AtomManager.QTInsertChild(ac, parent, Bytes.fourCC("mime"), id, (short) 0, mime.length, mime, new int[1]);
        AtomManager.QTInsertChild(ac, parent, Bytes.fourCC("ext "), id, (short) 0, ext.length, ext, new int[1]);
        AtomManager.QTInsertChild(ac, parent, Bytes.fourCC("desc"), id, (short) 0, desc.length, desc, new int[1]);
    }

    public static int GraphicsImportGetMIMETypeList(int gi, int[] container) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        int[] ac = new int[1];
        AtomManager.QTNewAtomContainer(ac);
        ImageFormats.Format f = ImageFormats.format(g.format);
        if (f != null) {
            mimeAtoms(ac[0], 0, f, 1);
        }
        container[0] = ac[0];
        return noErr;
    }

    public static int GraphicsImportGetExportSettingsAsAtomContainer(int gi, int[] container) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        if (g.exportSettings != 0) {
            container[0] = Heap.newHandle(Heap.handleBytes(g.exportSettings));
        } else {
            AtomManager.QTNewAtomContainer(container);
        }
        return noErr;
    }

    public static int GraphicsImportSetExportSettingsFromAtomContainer(int gi, int[] container) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        g.exportSettings = container[0] != 0 ? Heap.newHandle(Heap.handleBytes(container[0])) : 0;
        return noErr;
    }

    /** asks for a file and a type with a Swing file chooser, the image is exported to it */
    public static int GraphicsImportDoExportImageFileDialog(int gi, byte[] defaultSpec, byte[] prompt, int filterProc, int[] exportedType, byte[] exportedSpec,
                                                           short[] scriptTag) {
        Importer g = importer(gi);
        if (g == null) {
            return badComponentInstance;
        }
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            return userCanceledErr;
        }
        String def = defaultSpec != null ? FileManager.specToPath(defaultSpec) : null;
        String title = prompt != null && prompt.length > 0 ? Bytes.getPString(prompt, 0) : "Export Image";
        Object[] r = ExportDialog.ask(def, title);
        if (r == null) {
            return userCanceledErr;
        }
        int type = (Integer) r[0];
        byte[] spec = FileManager.pathToSpec((String) r[1]);
        short e = GraphicsImportExportImageFile(gi, type, 0, spec, (short) 0);
        if (e != noErr) {
            return e;
        }
        exportedType[0] = type;
        System.arraycopy(spec, 0, exportedSpec, 0, Math.min(spec.length, exportedSpec.length));
        if (scriptTag != null && scriptTag.length > 0) {
            scriptTag[0] = 0;
        }
        return noErr;
    }
}
