/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Graphics exporter components ('grex') in Java, writing image files with {@link ImageFormats}.
 */
public final class GraphicsExportManager {

    private static final Logger logger = Logger.getLogger(GraphicsExportManager.class.getName());

    private GraphicsExportManager() {
    }

    static final int grex = Bytes.fourCC("grex");

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short ioErr = -36;
    static final short badComponentInstance = (short) 0x8001;
    static final short codecErr = -8960;
    static final short codecBadDataErr = -8969;
    static final short invalidDataRef = -2014;
    static final short dataTooBigErr = -2090;

    static void register() {
        for (ImageFormats.Format f : ImageFormats.formats.values()) {
            if (f.canExport()) {
                ComponentManager.register(grex, f.fileType(), 0, f.name(), f.name() + " graphics exporter", c -> new Exporter(f.fileType()));
            }
        }
    }

    /** a graphics exporter instance */
    static final class Exporter {
        final int format;
        // input, one of them
        int importer;
        int picture;
        int gworld;
        int pixMap;
        String inFile;
        int inHandle;
        int inPtr;
        int inPtrSize;
        int inDataRef;
        int inDataRefType;
        int inDesc;
        long inOffset;
        long inLimit = -1;
        // output, one of them
        String outFile;
        int outHandle;
        int outDataRef;
        int outDataRefType;
        long outOffset;
        long outMaxSize;
        boolean truncate = true;
        long written;
        int fileType;
        int creator = Bytes.fourCC("ogle");
        // settings
        final ImageFormats.Settings settings = new ImageFormats.Settings();
        int targetDataSize;
        boolean dontRecompress;
        boolean exif;
        boolean thumbnail;
        int thumbnailWidth;
        int thumbnailHeight;
        int metaData;

        Exporter(int format) {
            this.format = format;
            this.fileType = format;
            if (format == ImageFormats.qtif) {
                settings.codec = Bytes.fourCC("png ");
            }
        }

        void clearInput() {
            importer = picture = gworld = pixMap = inHandle = inPtr = inPtrSize = inDataRef = inDataRefType = inDesc = 0;
            inFile = null;
            inOffset = 0;
            inLimit = -1;
        }

        void clearOutput() {
            outFile = null;
            outHandle = outDataRef = outDataRefType = 0;
            outOffset = 0;
            outMaxSize = 0;
            written = 0;
        }

        boolean compressedInput() {
            return inFile != null || inHandle != 0 || inPtr != 0 || inDataRef != 0 || importer != 0;
        }

        /** @return the compressed input data */
        byte[] inputData() throws IOException {
            byte[] all;
            if (importer != 0) {
                GraphicsImportManager.Importer g = GraphicsImportManager.importer(importer);
                return g.bytes();
            } else if (inFile != null) {
                all = Files.readAllBytes(new File(inFile).toPath());
            } else if (inHandle != 0) {
                all = Heap.handleBytes(inHandle);
            } else if (inPtr != 0) {
                all = Heap.read(inPtr, inPtrSize);
            } else if (inDataRef != 0) {
                all = GraphicsImportManager.dataRefBytes(inDataRef, inDataRefType);
            } else {
                throw new IOException("no compressed input");
            }
            int from = (int) Math.min(all.length, inOffset);
            int to = inLimit < 0 ? all.length : (int) Math.min(all.length, inLimit);
            return from == 0 && to == all.length ? all : Arrays.copyOfRange(all, from, Math.max(from, to));
        }

        /** @return the format of the compressed input, 0 if it is described by an image description */
        int inputFormat() throws IOException {
            if (importer != 0) {
                return GraphicsImportManager.importer(importer).format;
            }
            if (inDesc != 0) {
                return 0;
            }
            return ImageFormats.detect(inputData(), inFile);
        }

        BufferedImage inputImage() throws IOException {
            if (importer != 0) {
                GraphicsImportManager.Importer g = GraphicsImportManager.importer(importer);
                if (g == null) {
                    throw new IOException("the importer is closed");
                }
                return GraphicsImportManager.rendered(g);
            }
            if (picture != 0) {
                return ImageCompressionManager.pictImage(picture);
            }
            if (gworld != 0) {
                return Imaging.read(gworld, null);
            }
            if (pixMap != 0) {
                return Imaging.read(pixMap, null);
            }
            byte[] data = inputData();
            if (inDesc != 0) {
                BufferedImage img = Codecs.decode(Heap.handleBytes(inDesc), data, null);
                if (img == null) {
                    throw new IOException("no decompressor");
                }
                return img;
            }
            int f = ImageFormats.detect(data, inFile);
            if (f == 0) {
                throw new IOException("unknown input format");
            }
            return ImageFormats.decode(f, data, 1);
        }

        /** writes the bytes at the output offset plus what was written */
        void writeOutput(byte[] b) throws IOException {
            if (outMaxSize > 0 && written + b.length > outMaxSize) {
                throw new IOException("data too big");
            }
            long at = outOffset + written;
            if (outFile != null || outDataRef != 0 && outHandle == 0) {
                String p = outFile;
                int h = 0;
                if (p == null) {
                    DataRefRec d = MovieToolbox.dataRef(outDataRef, outDataRefType);
                    p = d.path();
                    if (d.type == DataRefRec.hndl) {
                        h = d.handle();
                    }
                }
                if (h != 0) {
                    writeHandle(h, at, b);
                } else if (p != null) {
                    DataRefRec.closeReader(p);
                    try (RandomAccessFile f = new RandomAccessFile(p, "rw")) {
                        if (written == 0 && truncate) {
                            f.setLength(at);
                        }
                        f.seek(at);
                        f.write(b);
                    }
                } else {
                    throw new IOException("the output data reference is not writable");
                }
            } else if (outHandle != 0) {
                writeHandle(outHandle, at, b);
            } else {
                throw new IOException("no output");
            }
            written += b.length;
        }

        private void writeHandle(int h, long at, byte[] b) {
            int size = Heap.handleSize(h);
            int end = (int) (at + b.length);
            if (written == 0 && truncate || end > size) {
                Heap.setHandleSize(h, written == 0 && truncate ? end : Math.max(size, end));
            }
            Heap.write(Heap.deref(h) + (int) at, b, 0, b.length);
        }

        byte[] readOutput(int offset, int size) throws IOException {
            if (outHandle != 0) {
                return Heap.read(Heap.deref(outHandle) + offset, size);
            }
            String p = outFile;
            if (p == null && outDataRef != 0) {
                p = MovieToolbox.dataRef(outDataRef, outDataRefType).path();
            }
            if (p == null) {
                throw new IOException("no output");
            }
            try (RandomAccessFile f = new RandomAccessFile(p, "r")) {
                byte[] b = new byte[size];
                f.seek(outOffset + offset);
                f.readFully(b);
                return b;
            }
        }
    }

    static Exporter exporter(int ge) {
        ComponentManager.InstanceRec i = ComponentManager.instance(ge);
        return i != null && i.impl instanceof Exporter x ? x : null;
    }

    // ---- input

    public static int GraphicsExportSetInputGraphicsImporter(int ge, int gi) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.importer = gi;
        return noErr;
    }

    public static int GraphicsExportGetInputGraphicsImporter(int ge, int[] gi) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        gi[0] = x.importer;
        return x.importer != 0 ? noErr : paramErr;
    }

    public static int GraphicsExportSetInputPicture(int ge, int picture) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.picture = picture;
        return noErr;
    }

    public static int GraphicsExportGetInputPicture(int ge, int[] picture) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        picture[0] = x.picture;
        return noErr;
    }

    public static int GraphicsExportSetInputGWorld(int ge, int gworld) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.gworld = gworld;
        return noErr;
    }

    public static int GraphicsExportGetInputGWorld(int ge, int[] gworld) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        gworld[0] = x.gworld;
        return noErr;
    }

    public static int GraphicsExportSetInputPixmap(int ge, int pixMap) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.pixMap = pixMap;
        return noErr;
    }

    public static int GraphicsExportGetInputPixmap(int ge, int[] pixMap) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        pixMap[0] = x.pixMap;
        return noErr;
    }

    public static int GraphicsExportSetInputFile(int ge, byte[] fsspec, int desc) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.inFile = FileManager.specToPath(fsspec);
        x.inDesc = desc != 0 ? Heap.newHandle(Heap.handleBytes(desc)) : 0;
        return x.inFile != null ? noErr : paramErr;
    }

    public static int GraphicsExportGetInputFile(int ge, byte[] fsspec) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        if (x.inFile == null) {
            return paramErr;
        }
        byte[] s = FileManager.pathToSpec(x.inFile);
        System.arraycopy(s, 0, fsspec, 0, Math.min(s.length, fsspec.length));
        return noErr;
    }

    public static int GraphicsExportSetInputHandle(int ge, int h, int desc) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.inHandle = h;
        x.inDesc = desc != 0 ? Heap.newHandle(Heap.handleBytes(desc)) : 0;
        return noErr;
    }

    public static int GraphicsExportGetInputHandle(int ge, int[] h) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        h[0] = x.inHandle;
        return noErr;
    }

    public static int GraphicsExportSetInputPtr(int ge, int p, int size, int desc) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.inPtr = p;
        x.inPtrSize = size;
        x.inDesc = desc != 0 ? Heap.newHandle(Heap.handleBytes(desc)) : 0;
        return noErr;
    }

    public static int GraphicsExportGetInputPtr(int ge, int[] p, int[] size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        p[0] = x.inPtr;
        size[0] = x.inPtrSize;
        return noErr;
    }

    public static int GraphicsExportSetInputDataReference(int ge, int dataRef, int dataRefType, int desc) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearInput();
        x.inDataRef = Heap.newHandle(Heap.handleBytes(dataRef));
        x.inDataRefType = dataRefType;
        x.inDesc = desc != 0 ? Heap.newHandle(Heap.handleBytes(desc)) : 0;
        return noErr;
    }

    public static int GraphicsExportGetInputDataReference(int ge, int[] dataRef, int[] dataRefType) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        dataRef[0] = x.inDataRef;
        dataRefType[0] = x.inDataRefType;
        return noErr;
    }

    public static int GraphicsExportSetInputOffsetAndLimit(int ge, int offset, int limit) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.inOffset = offset & 0xffff_ffffL;
        x.inLimit = limit == -1 ? -1 : limit & 0xffff_ffffL;
        return noErr;
    }

    public static int GraphicsExportGetInputOffsetAndLimit(int ge, int[] offset, int[] limit) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        offset[0] = (int) x.inOffset;
        limit[0] = (int) x.inLimit;
        return noErr;
    }

    public static int GraphicsExportGetInputDataSize(int ge, int[] size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            size[0] = x.compressedInput() ? x.inputData().length : 0;
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int GraphicsExportMayExporterReadInputData(int ge, byte[] mayReadInputData) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        mayReadInputData[0] = (byte) (x.compressedInput() ? 1 : 0);
        return noErr;
    }

    public static int GraphicsExportReadInputData(int ge, byte[] dst, int offset, int size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            byte[] b = x.inputData();
            if (offset < 0 || offset + size > b.length) {
                return paramErr;
            }
            System.arraycopy(b, offset, dst, 0, Math.min(size, dst.length));
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int GraphicsExportGetInputImageDescription(int ge, int[] desc) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            if (x.inDesc != 0) {
                desc[0] = Heap.newHandle(Heap.handleBytes(x.inDesc));
                return noErr;
            }
            BufferedImage img = x.inputImage();
            int f = x.compressedInput() ? x.inputFormat() : 0;
            byte[] d = f != 0 ? ImageFormats.description(f, x.inputData(), img)
                    : Codecs.description(ImageCompressionManager.raw, img.getWidth(), img.getHeight(), ImageFormats.depthOf(img), "None", 0, Codecs.codecLosslessQuality);
            desc[0] = Heap.newHandle(d);
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsExportGetInputImageDimensions(int ge, byte[] rect) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            BufferedImage img = x.inputImage();
            Imaging.putRect(rect, 0, new Rectangle(0, 0, img.getWidth(), img.getHeight()));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsExportGetInputImageDepth(int ge, int[] depth) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            if (x.gworld != 0 || x.pixMap != 0) {
                depth[0] = Imaging.depth(x.gworld != 0 ? x.gworld : x.pixMap);
            } else if (x.inDesc != 0) {
                depth[0] = Bytes.getShort(Heap.handleBytes(x.inDesc), 82);
            } else {
                depth[0] = ImageFormats.depthOf(x.inputImage());
            }
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecBadDataErr;
        }
    }

    public static int GraphicsExportDrawInputImage(int ge, int gworld, int gdh, byte[] srcRect, byte[] dstRect) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            BufferedImage img = x.inputImage();
            Rectangle src = srcRect != null ? Imaging.rect(srcRect) : new Rectangle(0, 0, img.getWidth(), img.getHeight());
            Rectangle dst = dstRect != null ? Imaging.rect(dstRect) : src;
            Imaging.draw(gworld != 0 ? gworld : QuickDrawManager.currentGWorld()[0], img, Imaging.mapRect(src, dst), Imaging.ditherCopy, null, 0);
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecBadDataErr;
        }
    }

    // ---- output

    public static int GraphicsExportSetOutputFile(int ge, byte[] fsspec) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearOutput();
        x.outFile = FileManager.specToPath(fsspec);
        return x.outFile != null ? noErr : paramErr;
    }

    public static int GraphicsExportGetOutputFile(int ge, byte[] fsspec) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        if (x.outFile == null) {
            return paramErr;
        }
        byte[] s = FileManager.pathToSpec(x.outFile);
        System.arraycopy(s, 0, fsspec, 0, Math.min(s.length, fsspec.length));
        return noErr;
    }

    public static int GraphicsExportSetOutputHandle(int ge, int h) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearOutput();
        x.outHandle = h;
        return noErr;
    }

    public static int GraphicsExportGetOutputHandle(int ge, int[] h) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        h[0] = x.outHandle;
        return noErr;
    }

    public static int GraphicsExportSetOutputDataReference(int ge, int dataRef, int dataRefType) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.clearOutput();
        x.outDataRef = Heap.newHandle(Heap.handleBytes(dataRef));
        x.outDataRefType = dataRefType;
        return noErr;
    }

    public static int GraphicsExportGetOutputDataReference(int ge, int[] dataRef, int[] dataRefType) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        dataRef[0] = x.outDataRef;
        dataRefType[0] = x.outDataRefType;
        return noErr;
    }

    public static int GraphicsExportSetOutputOffsetAndMaxSize(int ge, int offset, int maxSize, byte truncate) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.outOffset = offset & 0xffff_ffffL;
        x.outMaxSize = maxSize & 0xffff_ffffL;
        x.truncate = truncate != 0;
        return noErr;
    }

    public static int GraphicsExportGetOutputOffsetAndMaxSize(int ge, int[] offset, int[] maxSize, byte[] truncate) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        if (offset != null) {
            offset[0] = (int) x.outOffset;
        }
        if (maxSize != null) {
            maxSize[0] = (int) x.outMaxSize;
        }
        if (truncate != null) {
            truncate[0] = (byte) (x.truncate ? 1 : 0);
        }
        return noErr;
    }

    public static int GraphicsExportSetOutputFileTypeAndCreator(int ge, int fileType, int creator) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.fileType = fileType;
        x.creator = creator;
        return noErr;
    }

    public static int GraphicsExportGetOutputFileTypeAndCreator(int ge, int[] fileType, int[] creator) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        fileType[0] = x.fileType;
        creator[0] = x.creator;
        return noErr;
    }

    public static int GraphicsExportGetDefaultFileTypeAndCreator(int ge, int[] fileType, int[] creator) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        fileType[0] = x.format;
        creator[0] = Bytes.fourCC("ogle");
        return noErr;
    }

    /** the extension as a four character code padded with spaces */
    public static int GraphicsExportGetDefaultFileNameExtension(int ge, int[] extension) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        String e = (ImageFormats.format(x.format).extension() + "    ").substring(0, 4);
        extension[0] = Bytes.fourCC(e);
        return noErr;
    }

    public static int GraphicsExportGetMIMETypeList(int ge, int[] container) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        int[] ac = new int[1];
        AtomManager.QTNewAtomContainer(ac);
        GraphicsImportManager.mimeAtoms(ac[0], 0, ImageFormats.format(x.format), 1);
        container[0] = ac[0];
        return noErr;
    }

    public static int GraphicsExportWriteOutputData(int ge, byte[] data, int size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            x.writeOutput(Arrays.copyOf(data, size));
            return noErr;
        } catch (IOException e) {
            return ioErr;
        }
    }

    public static int GraphicsExportReadOutputData(int ge, byte[] dst, int offset, int size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            byte[] b = x.readOutput(offset, size);
            System.arraycopy(b, 0, dst, 0, Math.min(b.length, dst.length));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return ioErr;
        }
    }

    // ---- exporting

    /** @return true if the compressed input is in the output format already */
    static boolean canTranscode(Exporter x) {
        try {
            return x.compressedInput() && x.inDesc == 0 && x.inputFormat() == x.format && x.format != ImageFormats.PICT;
        } catch (IOException e) {
            return false;
        }
    }

    public static int GraphicsExportCanTranscode(int ge, byte[] canTranscode) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        canTranscode[0] = (byte) (canTranscode(x) ? 1 : 0);
        return noErr;
    }

    public static int GraphicsExportDoTranscode(int ge) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        if (!canTranscode(x)) {
            return paramErr;
        }
        try {
            x.written = 0;
            x.writeOutput(x.inputData());
            return noErr;
        } catch (IOException e) {
            logger.log(Level.FINE, "transcode", e);
            return ioErr;
        }
    }

    /** @return the encoded image, compressed down to the target data size for lossy formats if one is set */
    static byte[] encode(Exporter x, BufferedImage img) throws IOException {
        byte[] b = ImageFormats.encode(x.format, img, x.settings);
        if (x.targetDataSize > 0 && b.length > x.targetDataSize && (x.format == ImageFormats.JPEG || x.format == ImageFormats.qtif)) {
            int saved = x.settings.quality;
            try {
                for (int q = saved - 0x80; q > 0 && b.length > x.targetDataSize; q -= 0x80) {
                    x.settings.quality = q;
                    b = ImageFormats.encode(x.format, img, x.settings);
                }
            } finally {
                x.settings.quality = saved;
            }
        }
        return b;
    }

    public static int GraphicsExportDoExport(int ge, int[] actualSizeWritten) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        try {
            x.written = 0;
            if (x.dontRecompress && canTranscode(x)) {
                x.writeOutput(x.inputData());
            } else {
                x.writeOutput(encode(x, x.inputImage()));
            }
            if (actualSizeWritten != null && actualSizeWritten.length > 0) {
                actualSizeWritten[0] = (int) x.written;
            }
            return noErr;
        } catch (IOException e) {
            logger.log(Level.FINE, "export", e);
            return e.getMessage() != null && e.getMessage().equals("data too big") ? dataTooBigErr : codecErr;
        } catch (RuntimeException e) {
            logger.log(Level.FINE, "export", e);
            return codecBadDataErr;
        }
    }

    public static int GraphicsExportDoStandaloneExport(int ge) {
        return GraphicsExportDoExport(ge, null);
    }

    /** formats with a codec of the image compression manager */
    private static int codecOf(Exporter x) {
        ImageFormats.Format f = ImageFormats.format(x.format);
        if (x.format == ImageFormats.qtif) {
            return x.settings.codec;
        }
        return f.codec().isEmpty() ? 0 : Bytes.fourCC(f.codec());
    }

    public static int GraphicsExportCanUseCompressor(int ge, byte[] use, int[] codecSettings) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        int codec = codecOf(x);
        Codecs.Codec c = Codecs.get(codec);
        use[0] = (byte) (c != null && c.canCompress() ? 1 : 0);
        if (codecSettings != null && codecSettings.length > 0) {
            codecSettings[0] = settingsContainer(x);
        }
        return noErr;
    }

    /** compresses the input image with the codec of the settings, writes the data and returns its description */
    public static int GraphicsExportDoUseCompressor(int ge, int codecSettings, int[] desc) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        if (codecSettings != 0) {
            applySettings(x, AtomManager.children(codecSettings, 0));
        }
        Codecs.Codec c = Codecs.get(codecOf(x));
        if (c == null || !c.canCompress()) {
            return codecErr;
        }
        try {
            BufferedImage img = x.inputImage();
            byte[][] out = ImageCompressionManager.compress(img, null, c, x.settings.depth != 0 ? x.settings.depth : ImageFormats.depthOf(img), x.settings.quality);
            x.written = 0;
            x.writeOutput(out[1]);
            desc[0] = Heap.newHandle(out[0]);
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "use compressor", e);
            return codecErr;
        }
    }

    // ---- settings

    public static int GraphicsExportSetCompressionMethod(int ge, int method) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.settings.compressionMethod = method;
        if (x.format == ImageFormats.qtif && method != 0) {
            x.settings.codec = method;
        }
        return noErr;
    }

    public static int GraphicsExportGetCompressionMethod(int ge, int[] method) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        method[0] = x.format == ImageFormats.qtif ? x.settings.codec : x.settings.compressionMethod;
        return noErr;
    }

    public static int GraphicsExportSetCompressionQuality(int ge, int quality) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.settings.quality = quality;
        return noErr;
    }

    public static int GraphicsExportGetCompressionQuality(int ge, int[] quality) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        quality[0] = x.settings.quality;
        return noErr;
    }

    public static int GraphicsExportSetDepth(int ge, int depth) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.settings.depth = depth;
        return noErr;
    }

    public static int GraphicsExportGetDepth(int ge, int[] depth) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        depth[0] = x.settings.depth;
        return noErr;
    }

    public static int GraphicsExportSetResolution(int ge, int hRes, int vRes) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.settings.hRes = hRes;
        x.settings.vRes = vRes;
        return noErr;
    }

    public static int GraphicsExportGetResolution(int ge, int[] hRes, int[] vRes) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        hRes[0] = x.settings.hRes;
        vRes[0] = x.settings.vRes;
        return noErr;
    }

    public static int GraphicsExportSetInterlaceStyle(int ge, int style) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.settings.interlaceStyle = style;
        return noErr;
    }

    public static int GraphicsExportGetInterlaceStyle(int ge, int[] style) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        style[0] = x.settings.interlaceStyle;
        return noErr;
    }

    public static int GraphicsExportSetTargetDataSize(int ge, int size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.targetDataSize = size;
        return noErr;
    }

    public static int GraphicsExportGetTargetDataSize(int ge, int[] size) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        size[0] = x.targetDataSize;
        return noErr;
    }

    public static int GraphicsExportSetDontRecompress(int ge, byte dontRecompress) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.dontRecompress = dontRecompress != 0;
        return noErr;
    }

    public static int GraphicsExportGetDontRecompress(int ge, byte[] dontRecompress) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        dontRecompress[0] = (byte) (x.dontRecompress ? 1 : 0);
        return noErr;
    }

    public static int GraphicsExportSetExifEnabled(int ge, byte enable) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.exif = enable != 0;
        return noErr;
    }

    public static int GraphicsExportGetExifEnabled(int ge, byte[] enable) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        enable[0] = (byte) (x.exif ? 1 : 0);
        return noErr;
    }

    public static int GraphicsExportSetThumbnailEnabled(int ge, byte enable, int maxThumbnailWidth, int maxThumbnailHeight) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.thumbnail = enable != 0;
        x.thumbnailWidth = maxThumbnailWidth;
        x.thumbnailHeight = maxThumbnailHeight;
        return noErr;
    }

    public static int GraphicsExportGetThumbnailEnabled(int ge, byte[] enable, int[] maxThumbnailWidth, int[] maxThumbnailHeight) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        enable[0] = (byte) (x.thumbnail ? 1 : 0);
        maxThumbnailWidth[0] = x.thumbnailWidth;
        maxThumbnailHeight[0] = x.thumbnailHeight;
        return noErr;
    }

    public static int GraphicsExportSetMetaData(int ge, int userData) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        x.metaData = userData;
        return noErr;
    }

    public static int GraphicsExportGetMetaData(int ge, int[] userData) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        userData[0] = x.metaData;
        return noErr;
    }

    /** the settings as an 'sptl' atom (SCSpatialSettings) and a 'dpi ' atom of the resolutions */
    static int settingsContainer(Exporter x) {
        int[] ac = new int[1];
        AtomManager.QTNewAtomContainer(ac);
        byte[] sptl = new byte[14];
        int codec = codecOf(x);
        Bytes.putInt(sptl, 0, codec != 0 ? codec : x.format);
        Bytes.putShort(sptl, 8, x.settings.depth);
        Bytes.putInt(sptl, 10, x.settings.quality);
        AtomManager.QTInsertChild(ac[0], 0, ImageCompressionManager.scSpatialSettingsType, 1, (short) 0, sptl.length, sptl, new int[1]);
        byte[] dpi = new byte[8];
        Bytes.putInt(dpi, 0, x.settings.hRes);
        Bytes.putInt(dpi, 4, x.settings.vRes);
        AtomManager.QTInsertChild(ac[0], 0, Bytes.fourCC("dpi "), 1, (short) 0, dpi.length, dpi, new int[1]);
        return ac[0];
    }

    static void applySettings(Exporter x, Map<Integer, byte[]> atoms) {
        byte[] sptl = atoms.get(ImageCompressionManager.scSpatialSettingsType);
        if (sptl != null && sptl.length >= 14) {
            if (x.format == ImageFormats.qtif && Codecs.get(Bytes.getInt(sptl, 0)) != null) {
                x.settings.codec = Bytes.getInt(sptl, 0);
            }
            x.settings.depth = Bytes.getShort(sptl, 8);
            x.settings.quality = Bytes.getInt(sptl, 10);
        }
        byte[] dpi = atoms.get(Bytes.fourCC("dpi "));
        if (dpi != null && dpi.length >= 8) {
            x.settings.hRes = Bytes.getInt(dpi, 0);
            x.settings.vRes = Bytes.getInt(dpi, 4);
        }
    }

    public static int GraphicsExportGetSettingsAsAtomContainer(int ge, int[] container) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        container[0] = settingsContainer(x);
        return noErr;
    }

    /** the bytes of an atom container */
    public static int GraphicsExportSetSettingsFromAtomContainer(int ge, byte[] container) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        int h = Heap.newHandle(container);
        try {
            applySettings(x, AtomManager.children(h, 0));
            return noErr;
        } catch (RuntimeException e) {
            return paramErr;
        } finally {
            Heap.disposeHandle(h);
        }
    }

    public static int GraphicsExportGetSettingsAsText(int ge, int[] text) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ImageFormats.format(x.format).name()).append('\r');
        int codec = codecOf(x);
        if (codec != 0 && Codecs.get(codec) != null) {
            sb.append("Compressor: ").append(Codecs.get(codec).name).append('\r');
        }
        sb.append("Quality: ").append(x.settings.quality * 100 / Codecs.codecLosslessQuality).append("%\r");
        sb.append("Depth: ").append(x.settings.depth == 0 ? "Best" : String.valueOf(x.settings.depth)).append('\r');
        sb.append("Resolution: ").append(x.settings.hRes >> 16).append(" x ").append(x.settings.vRes >> 16).append(" dpi");
        text[0] = Heap.newHandle(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
        return noErr;
    }

    /** the depth and quality are asked with the standard compression dialog */
    public static int GraphicsExportRequestSettings(int ge, int filterProc, int refCon) {
        Exporter x = exporter(ge);
        if (x == null) {
            return badComponentInstance;
        }
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            return noErr;
        }
        ImageCompressionManager.SC s = new ImageCompressionManager.SC(false);
        byte[] sptl = new byte[14];
        int codec = codecOf(x);
        Bytes.putInt(sptl, 0, codec != 0 ? codec : Bytes.fourCC("raw "));
        Bytes.putShort(sptl, 8, x.settings.depth);
        Bytes.putInt(sptl, 10, x.settings.quality);
        s.info.put(ImageCompressionManager.scSpatialSettingsType, sptl);
        if (!SCDialog.show(s, false)) {
            return -128;
        }
        sptl = s.info.get(ImageCompressionManager.scSpatialSettingsType);
        if (x.format == ImageFormats.qtif) {
            x.settings.codec = Bytes.getInt(sptl, 0);
        }
        x.settings.depth = Bytes.getShort(sptl, 8);
        x.settings.quality = Bytes.getInt(sptl, 10);
        return noErr;
    }
}
