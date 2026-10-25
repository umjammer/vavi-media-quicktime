/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * The Image Compression Manager in Java: codecs of {@link Codecs}, image descriptions, compression sequences,
 * the standard compression component, data codecs and the curve utilities.
 */
public final class ImageCompressionManager {

    private static final Logger logger = Logger.getLogger(ImageCompressionManager.class.getName());

    private ImageCompressionManager() {
    }

    static final short noErr = 0;
    static final short paramErr = -50;
    static final short memFullErr = -108;
    static final short userCanceledErr = -128;
    static final short siInvalidCompression = -223;
    static final short codecErr = -8960;
    static final short noCodecErr = -8961;
    static final short codecUnimpErr = -8962;
    static final short codecSizeErr = -8963;
    static final short codecBadDataErr = -8969;
    static final short codecExtensionNotFoundErr = -8971;
    static final short scTypeNotFoundErr = codecExtensionNotFoundErr;
    static final short invalidComponentID = -3000;

    static final int imco = Bytes.fourCC("imco");
    static final int imdc = Bytes.fourCC("imdc");
    static final int scdi = Bytes.fourCC("scdi");
    static final int dcom = Bytes.fourCC("dcom");
    static final int ddec = Bytes.fourCC("ddec");
    static final int path = Bytes.fourCC("path");
    static final int zlib = Bytes.fourCC("zlib");
    static final int raw = Bytes.fourCC("raw ");

    // special codec components
    static final int anyCodec = 0;
    static final int bestSpeedCodec = -1;
    static final int bestFidelityCodec = -2;
    static final int bestCompressionCodec = -3;

    // codec flags
    static final int codecFlagUpdatePrevious = 1 << 2;
    static final int codecFlagWasCompressed = 1 << 4;
    static final int codecFlagUpdatePreviousComp = 1 << 6;
    static final int codecFlagForceKeyFrame = 1 << 7;
    static final int codecFlagDontOffscreen = 1 << 5;

    static void register() {
        for (Codecs.Codec c : Codecs.all()) {
            ComponentManager.register(imdc, c.type, c.decompressFlags, c.name, c.name + " decompressor", null);
            if (c.canCompress()) {
                ComponentManager.register(imco, c.type, c.compressFlags, c.name, c.name + " compressor", null);
            }
        }
        ComponentManager.register(imco, path, 0, "Curve", "Curve utilities", null);
        ComponentManager.register(scdi, Bytes.fourCC("imag"), 0, "Standard Image Compression", "Standard image compression dialog", c -> new SC(false));
        ComponentManager.register(scdi, Bytes.fourCC("soun"), 0, "Standard Sound Compression", "Standard sound compression dialog", c -> new SC(true));
        ComponentManager.register(dcom, zlib, 0, "zlib", "zlib data compressor", null);
        ComponentManager.register(ddec, zlib, 0, "zlib", "zlib data decompressor", null);
    }

    // ---- codecs

    /** @return the codec of a codec type and a codec component, null if none */
    static Codecs.Codec codec(int cType, int codecComponent) {
        if (codecComponent != 0 && codecComponent != bestSpeedCodec && codecComponent != bestFidelityCodec && codecComponent != bestCompressionCodec) {
            ComponentManager.ComponentRec c = ComponentManager.component(codecComponent);
            if (c != null && (c.type == imco || c.type == imdc)) {
                return Codecs.get(c.subType);
            }
        }
        return Codecs.get(cType);
    }

    /** @return the depth stored for a requested depth */
    static int depth(Codecs.Codec c, int requested, int sourceDepth) {
        if (requested == 0) {
            requested = sourceDepth == 32 ? 24 : sourceDepth;
        }
        return c.depth(requested);
    }

    /** @return compressed data of the image and its description */
    static byte[][] compress(BufferedImage image, BufferedImage previous, Codecs.Codec c, int depth, int quality) throws IOException {
        int d = c.depth(depth);
        byte[] data = c.encode(image, previous, d, quality);
        byte[] desc = Codecs.description(c.type, image.getWidth(), image.getHeight(), d, c.name, data.length, quality);
        return new byte[][] {desc, data};
    }

    public static short CodecManagerVersion(int[] version) {
        version[0] = 0x0004_0000;
        return noErr;
    }

    /** CodecNameSpecList: count(2) {codec(4) cType(4) typeName(32) name handle(4)}... */
    public static short GetCodecNameList(int[] list, short showAll) {
        List<ComponentManager.ComponentRec> cs = new ArrayList<>();
        for (Codecs.Codec c : Codecs.all()) {
            ComponentManager.ComponentRec r = ComponentManager.find(imco, c.type);
            if (r == null && showAll != 0) {
                r = ComponentManager.find(imdc, c.type);
            }
            if (r != null) {
                cs.add(r);
            }
        }
        int p = Heap.newPtr(2 + cs.size() * 44);
        Heap.putShort(p, (short) cs.size());
        for (int i = 0; i < cs.size(); i++) {
            ComponentManager.ComponentRec r = cs.get(i);
            int e = p + 2 + i * 44;
            Heap.putInt(e, r.ref);
            Heap.putInt(e + 4, r.subType);
            byte[] name = new byte[32];
            Bytes.putPString(name, 0, Codecs.get(r.subType).name, 32);
            Heap.write(e + 8, name, 0, 32);
            Heap.putInt(e + 40, Heap.newHandle(Arrays.copyOf(name, (name[0] & 0xff) + 1)));
        }
        list[0] = p;
        return noErr;
    }

    public static short DisposeCodecNameList(int list) {
        if (list == 0) {
            return noErr;
        }
        int n = Heap.getShort(list);
        for (int i = 0; i < n; i++) {
            Heap.disposeHandle(Heap.getInt(list + 2 + i * 44 + 40));
        }
        Heap.free(list);
        return noErr;
    }

    /** CodecInfo, 72 bytes */
    public static short GetCodecInfo(byte[] info, int cType, int codec) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null) {
            return noCodecErr;
        }
        Arrays.fill(info, (byte) 0);
        Bytes.putPString(info, 0, c.name, 32);
        Bytes.putShort(info, 32, 1);
        Bytes.putShort(info, 34, 1);
        Bytes.putInt(info, 36, Bytes.fourCC("appl"));
        Bytes.putInt(info, 40, c.decompressFlags);
        Bytes.putInt(info, 44, c.compressFlags);
        Bytes.putInt(info, 48, c.formatFlags);
        info[52] = (byte) ((c.formatFlags & Codecs.codecInfoDoesLossless) != 0 ? 100 : 80);
        info[53] = (byte) 90;
        Bytes.putShort(info, 54, 50);
        Bytes.putShort(info, 56, 50);
        info[58] = (byte) (c.type == raw ? 0 : 100);
        Bytes.putShort(info, 60, 1);
        Bytes.putShort(info, 62, 1);
        return noErr;
    }

    private static short findCodec(int cType, int specCodec, int[] compressor, int[] decompressor) {
        Codecs.Codec c = codec(cType, specCodec);
        if (c == null) {
            return noCodecErr;
        }
        if (compressor != null) {
            ComponentManager.ComponentRec r = ComponentManager.find(imco, c.type);
            if (r == null) {
                return noCodecErr;
            }
            compressor[0] = r.ref;
        }
        if (decompressor != null) {
            ComponentManager.ComponentRec r = ComponentManager.find(imdc, c.type);
            if (r == null) {
                return noCodecErr;
            }
            decompressor[0] = r.ref;
        }
        return noErr;
    }

    /** used by Decompressor */
    public static short FindCodec(int cType, int specCodec, int compressor, int[] decompressor) {
        return findCodec(cType, specCodec, null, decompressor);
    }

    /** used by Compressor */
    public static short FindCodec(int cType, int specCodec, int[] compressor, int decompressor) {
        return findCodec(cType, specCodec, compressor, null);
    }

    public static short GetMaxCompressionSize(int src, byte[] srcRect, short colorDepth, int quality, int cType, int codec, int[] size) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null || !c.canCompress()) {
            return noCodecErr;
        }
        Rectangle r = srcRect != null ? Imaging.rect(srcRect) : Imaging.bounds(src);
        size[0] = c.maxSize(r.width, r.height, depth(c, colorDepth, Imaging.depth(src)));
        return noErr;
    }

    public static short GetCompressionTime(int src, byte[] srcRect, short colorDepth, int cType, int codec, int spatialQuality, int temporalQuality, int[] time) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null || !c.canCompress()) {
            return noCodecErr;
        }
        if (spatialQuality != 0 && Heap.isValid(spatialQuality)) {
            Heap.putInt(spatialQuality, Math.min(Heap.getInt(spatialQuality), Codecs.codecLosslessQuality));
        }
        if (temporalQuality != 0 && Heap.isValid(temporalQuality)) {
            Heap.putInt(temporalQuality, c.isTemporal() ? Heap.getInt(temporalQuality) : 0);
        }
        time[0] = 0;
        return noErr;
    }

    // ---- still images

    private static short compressImage(int src, byte[] srcRect, int depth, int quality, int cType, int codec, int desc, Object data) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null || !c.canCompress()) {
            return noCodecErr;
        }
        try {
            Rectangle r = srcRect != null ? Imaging.rect(srcRect) : null;
            BufferedImage img = Imaging.read(src, r);
            byte[][] out = compress(img, null, c, depth(c, depth, Imaging.depth(src)), quality);
            if (!Imaging.write(data, out[1])) {
                return codecSizeErr;
            }
            Heap.setHandleBytes(desc, out[0]);
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "compress", e);
            return codecErr;
        }
    }

    public static short CompressImage(int src, byte[] srcRect, int quality, int cType, int desc, byte[] data) {
        return compressImage(src, srcRect, 0, quality, cType, 0, desc, data);
    }

    public static short CompressImage(int src, byte[] srcRect, int quality, int cType, int desc, int data) {
        return compressImage(src, srcRect, 0, quality, cType, 0, desc, data);
    }

    public static short CompressImage(int src, byte[] srcRect, int quality, int cType, int desc, int[] data) {
        return compressImage(src, srcRect, 0, quality, cType, 0, desc, data);
    }

    public static short FCompressImage(int src, byte[] srcRect, short colorDepth, int quality, int cType, int codec, int ctable, short flags, int bufferSize,
                                       int progressProc, int completionProc, int desc, byte[] data) {
        return compressImage(src, srcRect, colorDepth, quality, cType, codec, desc, data);
    }

    public static short FCompressImage(int src, byte[] srcRect, short colorDepth, int quality, int cType, int codec, int ctable, short flags, int bufferSize,
                                       int progressProc, int completionProc, int desc, int data) {
        return compressImage(src, srcRect, colorDepth, quality, cType, codec, desc, data);
    }

    public static short FCompressImage(int src, byte[] srcRect, short colorDepth, int quality, int cType, int codec, int ctable, short flags, int bufferSize,
                                       int progressProc, int completionProc, int desc, int[] data) {
        return compressImage(src, srcRect, colorDepth, quality, cType, codec, desc, data);
    }

    /** @return the decoded image of data with an image description handle */
    static BufferedImage decode(int desc, Object data, int size) throws IOException {
        byte[] d = Heap.handleBytes(desc);
        int dataSize = size > 0 ? size : Bytes.getInt(d, 44);
        BufferedImage img = Codecs.decode(d, Imaging.read(data, dataSize), null);
        if (img == null) {
            throw new IOException("no decompressor: " + Bytes.fourCC(Bytes.getInt(d, 4)));
        }
        return img;
    }

    private static short decompressImage(Object data, int desc, int dst, byte[] srcRect, AffineTransform at, int mode, int mask) {
        try {
            BufferedImage img = decode(desc, data, 0);
            if (srcRect != null) {
                Rectangle r = Imaging.rect(srcRect).intersection(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
                if (!r.isEmpty()) {
                    img = img.getSubimage(r.x, r.y, r.width, r.height);
                }
            }
            Imaging.draw(dst, img, at, mode, null, mask);
            return noErr;
        } catch (IOException e) {
            logger.log(Level.FINE, "decompress", e);
            return noCodecErr;
        } catch (RuntimeException e) {
            logger.log(Level.FINE, "decompress", e);
            return codecBadDataErr;
        }
    }

    private static short decompressImage(Object data, int desc, int dst, byte[] srcRect, byte[] dstRect, short mode, int mask) {
        byte[] d = Heap.handleBytes(desc);
        Rectangle src = srcRect != null ? Imaging.rect(srcRect) : new Rectangle(0, 0, Bytes.getShort(d, 32) & 0xffff, Bytes.getShort(d, 34) & 0xffff);
        Rectangle dr = dstRect != null ? Imaging.rect(dstRect) : src;
        AffineTransform at = Imaging.mapRect(new Rectangle(0, 0, src.width, src.height), dr);
        return decompressImage(data, desc, dst, srcRect, at, mode, mask);
    }

    public static short DecompressImage(byte[] data, int desc, int dst, byte[] srcRect, byte[] dstRect, short mode, int mask) {
        return decompressImage(data, desc, dst, srcRect, dstRect, mode, mask);
    }

    public static short DecompressImage(int data, int desc, int dst, byte[] srcRect, byte[] dstRect, short mode, int mask) {
        return decompressImage((Object) data, desc, dst, srcRect, dstRect, mode, mask);
    }

    public static short DecompressImage(int[] data, int desc, int dst, byte[] srcRect, byte[] dstRect, short mode, int mask) {
        return decompressImage(data, desc, dst, srcRect, dstRect, mode, mask);
    }

    public static short FDecompressImage(int data, int desc, int dst, byte[] srcRect, byte[] matrix, short mode, int mask, int matte, byte[] matteRect,
                                         int accuracy, int codec, int bufferSize, int dataProc, int progressProc) {
        AffineTransform at = Imaging.transform(matrix);
        return decompressImage((Object) data, desc, dst, srcRect, at, mode, mask);
    }

    private static short convertImage(int srcDD, Object srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, Object dstData) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null || !c.canCompress()) {
            return noCodecErr;
        }
        try {
            BufferedImage img = decode(srcDD, srcData, 0);
            int srcDepth = Bytes.getShort(Heap.handleBytes(srcDD), 82);
            byte[][] out = compress(img, null, c, depth(c, colorDepth, srcDepth), quality);
            if (!Imaging.write(dstData, out[1])) {
                return codecSizeErr;
            }
            Heap.setHandleBytes(dstDD, out[0]);
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "convert", e);
            return codecErr;
        }
    }

    public static short ConvertImage(int srcDD, byte[] srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, byte[] dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, byte[] srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, int dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, byte[] srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, int[] dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, int srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, byte[] dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, int srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, int dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, (Object) dstData);
    }

    public static short ConvertImage(int srcDD, int srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, int[] dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, int[] srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, byte[] dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, int[] srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, int dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    public static short ConvertImage(int srcDD, int[] srcData, short colorDepth, int ctable, int accuracy, int quality, int cType, int codec, int dstDD, int[] dstData) {
        return convertImage(srcDD, srcData, colorDepth, ctable, accuracy, quality, cType, codec, dstDD, dstData);
    }

    /** @return the size of the compressed data, from the data itself where the format tells it */
    private static short getCompressedImageSize(int desc, Object data, int[] size) {
        byte[] d = Heap.handleBytes(desc);
        int codec = Bytes.getInt(d, 4);
        int dataSize = Bytes.getInt(d, 44);
        byte[] b = Imaging.read(data, 0);
        int n = dataSize;
        String c = Bytes.fourCC(codec);
        if ((c.equals("rle ") || c.equals("smc ") || c.equals("rpza") || c.equals("cvid")) && b.length >= 4) {
            n = Bytes.getInt(b, 0) & (c.equals("rle ") ? 0x3fff_ffff : 0x00ff_ffff);
        } else if (c.equals("jpeg")) {
            for (int i = 2; i + 1 < b.length; i++) {
                if ((b[i] & 0xff) == 0xff && (b[i + 1] & 0xff) == 0xd9) {
                    n = i + 2;
                    break;
                }
            }
        } else if (c.equals("raw ")) {
            n = Codecs.rowBytesOf(Bytes.getShort(d, 32) & 0xffff, Bytes.getShort(d, 82)) * (Bytes.getShort(d, 34) & 0xffff);
        }
        size[0] = n > 0 ? n : b.length;
        return noErr;
    }

    public static short GetCompressedImageSize(int desc, byte[] data, int bufferSize, int dataProc, int[] size) {
        return getCompressedImageSize(desc, data, size);
    }

    public static short GetCompressedImageSize(int desc, int data, int bufferSize, int dataProc, int[] size) {
        return getCompressedImageSize(desc, data, size);
    }

    public static short GetCompressedImageSize(int desc, int[] data, int bufferSize, int dataProc, int[] size) {
        return getCompressedImageSize(desc, data, size);
    }

    /** similarity is a Fixed from 0 (different) to 1 (the same) */
    private static short getSimilarity(int src, byte[] srcRect, int desc, Object data, int[] similarity) {
        try {
            BufferedImage a = Imaging.read(src, srcRect != null ? Imaging.rect(srcRect) : null);
            BufferedImage b = decode(desc, data, 0);
            similarity[0] = Bytes.doubleToFixed(similarity(a, b));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecErr;
        }
    }

    static double similarity(BufferedImage a, BufferedImage b) {
        int w = Math.min(a.getWidth(), b.getWidth()), h = Math.min(a.getHeight(), b.getHeight());
        if (w == 0 || h == 0) {
            return 0;
        }
        long diff = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = a.getRGB(x, y), q = b.getRGB(x, y);
                diff += Math.abs(((p >> 16) & 0xff) - ((q >> 16) & 0xff)) + Math.abs(((p >> 8) & 0xff) - ((q >> 8) & 0xff)) + Math.abs((p & 0xff) - (q & 0xff));
            }
        }
        return 1 - diff / (w * (double) h * 3 * 255);
    }

    public static short GetSimilarity(int src, byte[] srcRect, int desc, byte[] data, int[] similarity) {
        return getSimilarity(src, srcRect, desc, data, similarity);
    }

    public static short GetSimilarity(int src, byte[] srcRect, int desc, int data, int[] similarity) {
        return getSimilarity(src, srcRect, desc, data, similarity);
    }

    public static short GetSimilarity(int src, byte[] srcRect, int desc, int[] data, int[] similarity) {
        return getSimilarity(src, srcRect, desc, data, similarity);
    }

    /** decodes, crops to the trim rect and compresses again with the same codec (or none if it cannot compress) */
    private static short trimImage(int desc, Object inData, Object outData, byte[] trimRect) {
        try {
            byte[] d = Heap.handleBytes(desc);
            BufferedImage img = decode(desc, inData, 0);
            Rectangle r = Imaging.rect(trimRect).intersection(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
            if (r.isEmpty()) {
                return paramErr;
            }
            BufferedImage part = Codecs.argb(r.width, r.height);
            Graphics2D g = part.createGraphics();
            g.drawImage(img, -r.x, -r.y, null);
            g.dispose();
            Codecs.Codec c = Codecs.get(Bytes.getInt(d, 4));
            if (c == null || !c.canCompress()) {
                c = Codecs.get(raw);
            }
            byte[][] out = compress(part, null, c, Bytes.getShort(d, 82), Bytes.getInt(d, 28));
            if (!Imaging.write(outData, out[1])) {
                return codecSizeErr;
            }
            byte[] nd = d.clone();
            Bytes.putInt(nd, 4, c.type);
            Bytes.putShort(nd, 32, r.width);
            Bytes.putShort(nd, 34, r.height);
            Bytes.putInt(nd, 44, out[1].length);
            Bytes.putShort(nd, 82, Bytes.getShort(out[0], 82));
            Heap.setHandleBytes(desc, nd);
            Imaging.putRect(trimRect, 0, new Rectangle(0, 0, r.width, r.height));
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "trim", e);
            return codecErr;
        }
    }

    public static short TrimImage(int desc, byte[] inData, int inBufferSize, int dataProc, byte[] outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, byte[] inData, int inBufferSize, int dataProc, int outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, byte[] inData, int inBufferSize, int dataProc, int[] outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, int inData, int inBufferSize, int dataProc, byte[] outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, int inData, int inBufferSize, int dataProc, int outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, (Object) outData, trimRect);
    }

    public static short TrimImage(int desc, int inData, int inBufferSize, int dataProc, int[] outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, int[] inData, int inBufferSize, int dataProc, byte[] outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, int[] inData, int inBufferSize, int dataProc, int outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    public static short TrimImage(int desc, int[] inData, int inBufferSize, int dataProc, int[] outData, int outBufferSize, int flushProc, byte[] trimRect, int progressProc) {
        return trimImage(desc, inData, outData, trimRect);
    }

    // ---- pictures

    /** @return the image of a PicHandle */
    static BufferedImage pictImage(int pict) throws IOException {
        BufferedImage img = PictCodec.decode(Heap.handleBytes(pict));
        if (img == null) {
            throw new IOException("cannot draw the picture");
        }
        return img;
    }

    /** @return a picture of compressed image data placed at the frame of the source picture */
    static byte[] compressedPicture(BufferedImage img, int[] frame, Codecs.Codec c, int depth, int quality) throws IOException {
        byte[][] out = compress(img, null, c, depth, quality);
        return PictCodec.encodeCompressed(out[0], out[1], frame[0], frame[1], frame[2], frame[3]);
    }

    public static short CompressPicture(int srcPicture, int dstPicture, int quality, int cType) {
        return FCompressPicture(srcPicture, dstPicture, (short) 0, 0, quality, (short) 0, (short) 0, 0, cType, 0);
    }

    public static short FCompressPicture(int srcPicture, int dstPicture, short colorDepth, int ctable, int quality, short doDither, short compressAgain,
                                         int progressProc, int cType, int codec) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null || !c.canCompress()) {
            return noCodecErr;
        }
        try {
            byte[] src = Heap.handleBytes(srcPicture);
            BufferedImage img = pictImage(srcPicture);
            Heap.setHandleBytes(dstPicture, compressedPicture(img, PictCodec.frame(src), c, depth(c, colorDepth, 32), quality));
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "compress picture", e);
            return codecErr;
        }
    }

    /** @return the image scaled to fit 80 x 80 */
    static BufferedImage thumbnail(BufferedImage img) {
        double s = Math.min(1.0, 80.0 / Math.max(img.getWidth(), img.getHeight()));
        int w = Math.max(1, (int) Math.round(img.getWidth() * s)), h = Math.max(1, (int) Math.round(img.getHeight() * s));
        BufferedImage t = Codecs.argb(w, h);
        Graphics2D g = t.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        return t;
    }

    public static short MakeThumbnailFromPicture(int picture, int colorDepth, int thumbnail, int progressProc) {
        try {
            Heap.setHandleBytes(thumbnail, PictCodec.encode(thumbnail(pictImage(picture)), 0, 0));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecErr;
        }
    }

    public static short MakeThumbnailFromPixMap(int src, byte[] srcRect, short colorDepth, int thumbnail, int progressProc) {
        try {
            BufferedImage img = Imaging.read(src, srcRect != null ? Imaging.rect(srcRect) : null);
            Heap.setHandleBytes(thumbnail, PictCodec.encode(thumbnail(img), 0, 0));
            return noErr;
        } catch (RuntimeException e) {
            return codecErr;
        }
    }

    // ---- image descriptions

    /** @return the offset of the extensions: after the base and its color table */
    static int extensionsStart(byte[] d) {
        int base = Codecs.DESCRIPTION_SIZE;
        if (Bytes.getShort(d, 84) == 0 && Bytes.getShort(d, 82) >= 1 && Bytes.getShort(d, 82) <= 8 && d.length >= base + 8) {
            base += 8 + ((Bytes.getShort(d, base + 6) & 0xffff) + 1) * 8;
        }
        return Math.min(base, d.length);
    }

    /** @return offsets of the extension atoms */
    static List<int[]> extensions(byte[] d) {
        List<int[]> l = new ArrayList<>();
        int end = Math.min(d.length, Math.max(Codecs.DESCRIPTION_SIZE, Bytes.getInt(d, 0)));
        int p = extensionsStart(d);
        while (p + 8 <= end) {
            int size = Bytes.getInt(d, p);
            if (size < 8 || p + size > end) {
                break;
            }
            l.add(new int[] {p, size, Bytes.getInt(d, p + 4)});
            p += size;
        }
        return l;
    }

    /** @return the data of the nth (1 based) extension of the type, null if there is none */
    static byte[] extension(byte[] d, int type, int index) {
        int n = 0;
        for (int[] e : extensions(d)) {
            if (e[2] == type && ++n == index) {
                return Arrays.copyOfRange(d, e[0] + 8, e[0] + e[1]);
            }
        }
        return null;
    }

    static byte[] addExtension(byte[] d, int type, byte[] data) {
        int size = Math.max(Codecs.DESCRIPTION_SIZE, Math.min(d.length, Bytes.getInt(d, 0)));
        byte[] n = Arrays.copyOf(d, size + 8 + data.length);
        Bytes.putInt(n, size, 8 + data.length);
        Bytes.putInt(n, size + 4, type);
        System.arraycopy(data, 0, n, size + 8, data.length);
        Bytes.putInt(n, 0, n.length);
        return n;
    }

    public static short GetImageDescriptionExtension(int desc, int[] extension, int idType, int index) {
        byte[] e = extension(Heap.handleBytes(desc), idType, Math.max(1, index));
        if (e == null) {
            extension[0] = 0;
            return codecExtensionNotFoundErr;
        }
        extension[0] = Heap.newHandle(e);
        return noErr;
    }

    public static short AddImageDescriptionExtension(int desc, int extension, int idType) {
        Heap.setHandleBytes(desc, addExtension(Heap.handleBytes(desc), idType, Heap.handleBytes(extension)));
        return noErr;
    }

    public static short RemoveImageDescriptionExtension(int desc, int idType, int index) {
        byte[] d = Heap.handleBytes(desc);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int start = extensionsStart(d);
        os.write(d, 0, start);
        int n = 0;
        boolean removed = false;
        for (int[] e : extensions(d)) {
            if (e[2] == idType && (index == 0 || ++n == index)) {
                removed = true;
                continue;
            }
            os.write(d, e[0], e[1]);
        }
        if (!removed) {
            return codecExtensionNotFoundErr;
        }
        byte[] b = os.toByteArray();
        Bytes.putInt(b, 0, b.length);
        Heap.setHandleBytes(desc, b);
        return noErr;
    }

    public static short CountImageDescriptionExtensionType(int desc, int idType, int[] count) {
        int n = 0;
        for (int[] e : extensions(Heap.handleBytes(desc))) {
            if (idType == 0 || e[2] == idType) {
                n++;
            }
        }
        count[0] = n;
        return noErr;
    }

    /** idType is the previous type on input, 0 to start, and the next type or 0 on output */
    public static short GetNextImageDescriptionExtensionType(int desc, int[] idType) {
        List<Integer> types = new ArrayList<>();
        for (int[] e : extensions(Heap.handleBytes(desc))) {
            if (!types.contains(e[2])) {
                types.add(e[2]);
            }
        }
        int i = idType[0] == 0 ? 0 : types.indexOf(idType[0]) + 1;
        idType[0] = i > 0 || idType[0] == 0 ? (i < types.size() ? types.get(i) : 0) : 0;
        return noErr;
    }

    public static short GetImageDescriptionCTable(int desc, int[] ctable) {
        byte[] d = Heap.handleBytes(desc);
        int depth = Bytes.getShort(d, 82);
        int[] palette = new Codecs.Desc(d).palette();
        if (palette == null) {
            palette = Codecs.defaultPalette(8);
        }
        byte[] ct = Codecs.colorTable(palette);
        if (Bytes.getShort(d, 84) == 0 && depth >= 1 && depth <= 8 && d.length >= Codecs.DESCRIPTION_SIZE + 8) {
            int n = ((Bytes.getShort(d, Codecs.DESCRIPTION_SIZE + 6) & 0xffff) + 1) * 8 + 8;
            ct = Arrays.copyOfRange(d, Codecs.DESCRIPTION_SIZE, Math.min(d.length, Codecs.DESCRIPTION_SIZE + n));
        }
        ctable[0] = Heap.newHandle(ct);
        return noErr;
    }

    public static short SetImageDescriptionCTable(int desc, int ctable) {
        byte[] d = Heap.handleBytes(desc);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(d, 0, Codecs.DESCRIPTION_SIZE);
        if (ctable != 0) {
            os.writeBytes(Heap.handleBytes(ctable));
        }
        for (int[] e : extensions(d)) {
            os.write(d, e[0], e[1]);
        }
        byte[] b = os.toByteArray();
        Bytes.putInt(b, 0, b.length);
        Bytes.putShort(b, 84, ctable != 0 ? 0 : -1);
        Heap.setHandleBytes(desc, b);
        return noErr;
    }

    public static short MakeImageDescriptionForPixMap(int pixMap, int[] desc) {
        Rectangle r = Imaging.bounds(pixMap);
        int depth = Imaging.depth(pixMap);
        desc[0] = Heap.newHandle(Codecs.description(raw, r.width, r.height, depth, "None", Codecs.rowBytesOf(r.width, depth) * r.height, Codecs.codecLosslessQuality));
        return noErr;
    }

    public static short MakeImageDescriptionForEffect(int effectType, int[] desc) {
        desc[0] = Heap.newHandle(Codecs.description(effectType, 0, 0, 24, "", 0, Codecs.codecNormalQuality));
        return noErr;
    }

    // ---- pixel formats

    private static final Map<Integer, byte[]> pixelFormats = new HashMap<>();

    static final int kICMPixelFormatIsIndexed = 1 << 4;
    static final int kICMPixelFormatIsSupportedByQD = 1 << 5;
    static final int kICMPixelFormatIsMonochrome = 1 << 6;
    static final int kICMPixelFormatHasAlphaChannel = 1 << 7;

    public static short QTGetPixelSize(int pixelFormat) {
        return (short) QuickDrawManager.depthOf(pixelFormat);
    }

    /** ICMPixelFormatInfo: size(4) formatFlags(4) bitsPerPixel[14] */
    public static short ICMGetPixelFormatInfo(int pixelFormat, byte[] info) {
        byte[] registered = pixelFormats.get(pixelFormat);
        if (registered != null) {
            System.arraycopy(registered, 0, info, 0, Math.min(info.length, registered.length));
            return noErr;
        }
        int bits;
        int flags = 0;
        switch (pixelFormat) {
            case 1, 2, 4, 8 -> {
                bits = pixelFormat;
                flags = kICMPixelFormatIsIndexed | kICMPixelFormatIsSupportedByQD | (pixelFormat == 1 ? kICMPixelFormatIsMonochrome : 0);
            }
            case 16, 24 -> {
                bits = pixelFormat;
                flags = kICMPixelFormatIsSupportedByQD;
            }
            case 32 -> {
                bits = 32;
                flags = kICMPixelFormatIsSupportedByQD | kICMPixelFormatHasAlphaChannel;
            }
            case 33, 34, 36, 40 -> {
                bits = pixelFormat - 32;
                flags = kICMPixelFormatIsIndexed | kICMPixelFormatIsSupportedByQD | kICMPixelFormatIsMonochrome;
            }
            default -> {
                String s = Bytes.fourCC(pixelFormat);
                switch (s) {
                    case "BGRA", "ABGR", "RGBA" -> {
                        bits = 32;
                        flags = kICMPixelFormatHasAlphaChannel;
                    }
                    case "2vuy", "yuvs", "yuv2", "b16g" -> bits = 16;
                    case "b32a" -> bits = 32;
                    case "b48r" -> bits = 48;
                    case "b64a" -> {
                        bits = 64;
                        flags = kICMPixelFormatHasAlphaChannel;
                    }
                    default -> {
                        return paramErr;
                    }
                }
            }
        }
        Arrays.fill(info, (byte) 0);
        Bytes.putInt(info, 0, 36);
        Bytes.putInt(info, 4, flags);
        Bytes.putShort(info, 8, bits);
        return noErr;
    }

    public static short ICMSetPixelFormatInfo(int pixelFormat, byte[] info) {
        pixelFormats.put(pixelFormat, info.clone());
        return noErr;
    }

    // ---- sound compression info

    /** CompressionInfo: recordSize(4) format(4) compressionVersion(2) samplesPerPacket(2) bytesPerPacket(2) bytesPerFrame(2) bytesPerSample(2) futureUse1(2) */
    public static short GetCompressionInfo(short compressionID, int format, short numChannels, short sampleSize, byte[] info) {
        int channels = Math.max(1, numChannels);
        int bytes = Math.max(1, sampleSize / 8);
        int spp, bpp, bps;
        String f = Bytes.fourCC(format);
        if (compressionID == -1 || compressionID == 0 || format == 0) {
            f = compressionID == 3 ? "MAC3" : compressionID == 4 ? "MAC6" : f.equals("\0\0\0\0") ? "raw " : f;
        }
        switch (f) {
            case "raw ", "twos", "sowt", "NONE", "in24", "in32", "fl32", "fl64", "lpcm" -> {
                if (f.equals("in24")) {
                    bytes = 3;
                } else if (f.equals("in32") || f.equals("fl32")) {
                    bytes = 4;
                } else if (f.equals("fl64")) {
                    bytes = 8;
                }
                spp = 1;
                bpp = bytes;
                bps = bytes;
            }
            case "ulaw", "alaw" -> {
                spp = 1;
                bpp = 1;
                bps = 2;
            }
            case "ima4" -> {
                spp = 64;
                bpp = 34;
                bps = 2;
            }
            case "MAC3" -> {
                spp = 6;
                bpp = 2;
                bps = 1;
            }
            case "MAC6" -> {
                spp = 6;
                bpp = 1;
                bps = 1;
            }
            default -> {
                return siInvalidCompression;
            }
        }
        Arrays.fill(info, (byte) 0);
        Bytes.putInt(info, 0, 20);
        Bytes.putInt(info, 4, Bytes.fourCC(f));
        Bytes.putShort(info, 8, 0);
        Bytes.putShort(info, 10, spp);
        Bytes.putShort(info, 12, bpp);
        Bytes.putShort(info, 14, bpp * channels);
        Bytes.putShort(info, 16, bps);
        return noErr;
    }

    // ---- compression sequences

    /** a compression sequence */
    static final class CSeq {
        final int ref;
        int pixMap;
        Rectangle srcRect;
        int depth;
        Codecs.Codec codec;
        int spatialQuality;
        int temporalQuality;
        int keyFrameRate;
        int ctable;
        int flags;
        int desc;
        int frameNumber;
        BufferedImage prev;
        int prevGWorld;
        byte[] dataRateParams = new byte[24];
        int preferredPacketSize;

        CSeq() {
            this.ref = Heap.newObject(this, 4);
        }
    }

    /** a decompression sequence */
    static final class DSeq {
        final int ref;
        Codecs.Desc desc;
        int port;
        Rectangle srcRect;
        int[] matrix;
        int mode;
        byte[] opColor;
        int mask;
        int flags;
        int accuracy;
        int matte;
        BufferedImage prev;
        int imageBuffer;
        byte[] timeCode;

        DSeq() {
            this.ref = Heap.newObject(this, 4);
        }
    }

    /** a data source of a sequence */
    static final class Source {
        final int ref;
        final Object seq;
        int sourceType;
        int inputNumber;
        int dataDescription;
        byte[] data;
        int transferProc;
        int refCon;
        boolean changed;

        Source(Object seq) {
            this.seq = seq;
            this.ref = Heap.newObject(this, 4);
        }
    }

    private static final List<Source> sources = new ArrayList<>();

    static CSeq cseq(int ref) {
        return Heap.peerOrNull(ref, CSeq.class);
    }

    static DSeq dseq(int ref) {
        return Heap.peerOrNull(ref, DSeq.class);
    }

    public static short CompressSequenceBegin(int[] seqID, int src, int prev, byte[] srcRect, byte[] prevRect, short colorDepth, int cType, int codec,
                                              int spatialQuality, int temporalQuality, int keyFrameRate, int ctable, short flags, int desc) {
        Codecs.Codec c = codec(cType, codec);
        if (c == null || !c.canCompress()) {
            return noCodecErr;
        }
        CSeq s = new CSeq();
        s.pixMap = src;
        s.srcRect = srcRect != null ? Imaging.rect(srcRect) : Imaging.bounds(src);
        s.codec = c;
        s.depth = depth(c, colorDepth, Imaging.depth(src));
        s.spatialQuality = spatialQuality;
        s.temporalQuality = temporalQuality;
        s.keyFrameRate = keyFrameRate;
        s.ctable = ctable;
        s.flags = flags;
        s.desc = desc;
        if (desc != 0) {
            Heap.setHandleBytes(desc, Codecs.description(c.type, s.srcRect.width, s.srcRect.height, s.depth, c.name, 0, spatialQuality));
        }
        if (prev != 0) {
            s.prev = Imaging.read(prev, prevRect != null ? Imaging.rect(prevRect) : null);
        }
        seqID[0] = s.ref;
        return noErr;
    }

    private static short compressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, Object data, int[] dataSize, byte[] similarity) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        try {
            BufferedImage img = Imaging.read(src != 0 ? src : s.pixMap, srcRect != null ? Imaging.rect(srcRect) : s.srcRect);
            boolean key = s.prev == null || !s.codec.isTemporal() || s.temporalQuality == 0 || (flags & codecFlagForceKeyFrame) != 0
                    || (s.keyFrameRate > 0 && s.frameNumber % s.keyFrameRate == 0);
            byte[] out = s.codec.encode(img, key ? null : s.prev, s.depth, s.spatialQuality);
            if (!Imaging.write(data, out)) {
                return codecSizeErr;
            }
            if (dataSize != null && dataSize.length > 0) {
                dataSize[0] = out.length;
            }
            if (similarity != null && similarity.length > 0) {
                similarity[0] = (byte) (key ? 0 : Math.max(1, (int) Math.round(similarity(img, s.prev) * 255)));
            }
            if (s.desc != 0) {
                byte[] d = Heap.handleBytes(s.desc);
                Bytes.putInt(d, 44, out.length);
                Heap.setHandleBytes(s.desc, d);
            }
            if ((flags & (codecFlagUpdatePrevious | codecFlagUpdatePreviousComp)) != 0 || s.codec.isTemporal()) {
                s.prev = (flags & codecFlagUpdatePreviousComp) != 0 ? Codecs.decode(Heap.handleBytes(s.desc), out, s.prev) : img;
            }
            s.frameNumber++;
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "compress frame", e);
            return codecErr;
        }
    }

    public static short CompressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, byte[] data, int[] dataSize, byte[] similarity, int asyncCompletion) {
        return compressSequenceFrame(seqID, src, srcRect, flags, data, dataSize, similarity);
    }

    public static short CompressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, int data, int[] dataSize, byte[] similarity, int asyncCompletion) {
        return compressSequenceFrame(seqID, src, srcRect, flags, data, dataSize, similarity);
    }

    public static short CompressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, int[] data, int[] dataSize, byte[] similarity, int asyncCompletion) {
        return compressSequenceFrame(seqID, src, srcRect, flags, data, dataSize, similarity);
    }

    public static short GetCSequenceDataRateParams(int seqID, byte[] params) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        System.arraycopy(s.dataRateParams, 0, params, 0, Math.min(params.length, s.dataRateParams.length));
        return noErr;
    }

    public static short SetCSequenceDataRateParams(int seqID, byte[] params) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        System.arraycopy(params, 0, s.dataRateParams, 0, Math.min(params.length, s.dataRateParams.length));
        return noErr;
    }

    public static short GetCSequenceFrameNumber(int seqID, int[] frameNumber) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        frameNumber[0] = s.frameNumber;
        return noErr;
    }

    public static short SetCSequenceFrameNumber(int seqID, int frameNumber) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.frameNumber = frameNumber;
        return noErr;
    }

    public static short GetCSequenceKeyFrameRate(int seqID, int[] keyFrameRate) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        keyFrameRate[0] = s.keyFrameRate;
        return noErr;
    }

    public static short SetCSequenceKeyFrameRate(int seqID, int keyFrameRate) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.keyFrameRate = keyFrameRate;
        return noErr;
    }

    public static short GetCSequenceMaxCompressionSize(int seqID, int src, int[] size) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        size[0] = s.codec.maxSize(s.srcRect.width, s.srcRect.height, s.depth);
        return noErr;
    }

    public static short SetCSequencePreferredPacketSize(int seqID, int size) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.preferredPacketSize = size;
        return noErr;
    }

    public static short SetCSequencePrev(int seqID, int prev, byte[] prevRect) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.prev = prev == 0 ? null : Imaging.read(prev, prevRect != null ? Imaging.rect(prevRect) : null);
        return noErr;
    }

    public static short SetCSequenceQuality(int seqID, int spatialQuality, int temporalQuality) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.spatialQuality = spatialQuality;
        s.temporalQuality = temporalQuality;
        return noErr;
    }

    /** @return a GWorld holding the image */
    static int gworld(BufferedImage img, int existing) {
        int g = existing;
        QuickDrawManager.Port p = g != 0 ? QuickDrawManager.portOrNull(g) : null;
        if (p == null || p.width != img.getWidth() || p.height != img.getHeight()) {
            if (p != null) {
                QuickDrawManager.disposePort(g);
            }
            g = QuickDrawManager.newPort(QuickDrawManager.k32ARGBPixelFormat, 0, 0, img.getWidth(), img.getHeight()).ref;
            p = QuickDrawManager.portOrNull(g);
        }
        Graphics2D gr = p.image.createGraphics();
        gr.setComposite(java.awt.AlphaComposite.Src);
        gr.drawImage(img, 0, 0, null);
        gr.dispose();
        return g;
    }

    public static short GetCSequencePrevBuffer(int seqID, int[] gworld) {
        CSeq s = cseq(seqID);
        if (s == null) {
            return paramErr;
        }
        if (s.prev == null) {
            gworld[0] = 0;
            return codecErr;
        }
        s.prevGWorld = gworld(s.prev, s.prevGWorld);
        gworld[0] = s.prevGWorld;
        return noErr;
    }

    public static short DecompressSequenceBegin(int[] seqID, int desc, int port, int gdh, byte[] srcRect, byte[] matrix, short mode, int mask, short flags,
                                                int accuracy, int codec) {
        byte[] d = Heap.handleBytes(desc);
        if (Codecs.get(Bytes.getInt(d, 4)) == null && (codec == 0 || codec(0, codec) == null)) {
            return noCodecErr;
        }
        DSeq s = new DSeq();
        s.desc = new Codecs.Desc(d);
        s.port = port != 0 ? port : QuickDrawManager.currentGWorld()[0];
        s.srcRect = srcRect != null ? Imaging.rect(srcRect) : null;
        s.matrix = matrix != null ? MatrixManager.read(matrix) : MatrixManager.identity();
        s.mode = mode;
        s.mask = mask != 0 ? QuickDrawManager.copyRegion(mask) : 0;
        s.flags = flags;
        s.accuracy = accuracy;
        seqID[0] = s.ref;
        return noErr;
    }

    public static short DecompressSequenceBeginS(int[] seqID, int desc, byte[] data, int dataSize, int port, int gdh, byte[] srcRect, byte[] matrix, short mode,
                                                 int mask, short flags, int accuracy, int codec) {
        return DecompressSequenceBegin(seqID, desc, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    public static short DecompressSequenceBeginS(int[] seqID, int desc, int data, int dataSize, int port, int gdh, byte[] srcRect, byte[] matrix, short mode,
                                                 int mask, short flags, int accuracy, int codec) {
        return DecompressSequenceBegin(seqID, desc, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    public static short DecompressSequenceBeginS(int[] seqID, int desc, int[] data, int dataSize, int port, int gdh, byte[] srcRect, byte[] matrix, short mode,
                                                 int mask, short flags, int accuracy, int codec) {
        return DecompressSequenceBegin(seqID, desc, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    /** @return the transform of the sequence from the image to the port */
    private static AffineTransform transform(DSeq s, BufferedImage img) {
        AffineTransform at = Imaging.transform(s.matrix);
        if (s.srcRect != null) {
            at.translate(-s.srcRect.x, -s.srcRect.y);
        }
        return at;
    }

    private static short decompressSequenceFrame(int seqID, Object data, int dataSize, short inFlags, short[] outFlags) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        try {
            byte[] b = Imaging.read(data, dataSize > 0 ? dataSize : s.desc.dataSize());
            Codecs.Codec c = Codecs.get(s.desc.codec());
            if (c == null) {
                return noCodecErr;
            }
            BufferedImage img = c.decode(b, s.desc, c.isTemporal() ? s.prev : null);
            s.prev = img;
            if (s.imageBuffer != 0) {
                s.imageBuffer = gworld(img, s.imageBuffer);
            }
            BufferedImage shown = img;
            if (s.srcRect != null) {
                Rectangle r = s.srcRect.intersection(new Rectangle(0, 0, img.getWidth(), img.getHeight()));
                if (!r.isEmpty()) {
                    shown = img.getSubimage(r.x, r.y, r.width, r.height);
                }
            }
            Imaging.draw(s.port, shown, Imaging.transform(s.matrix), s.mode, s.opColor, s.mask);
            if (outFlags != null && outFlags.length > 0) {
                outFlags[0] = (short) codecFlagWasCompressed;
            }
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "decompress frame", e);
            return codecBadDataErr;
        }
    }

    public static short DecompressSequenceFrame(int seqID, byte[] data, short inFlags, short[] outFlags, int asyncCompletion) {
        return decompressSequenceFrame(seqID, data, 0, inFlags, outFlags);
    }

    public static short DecompressSequenceFrame(int seqID, int data, short inFlags, short[] outFlags, int asyncCompletion) {
        return decompressSequenceFrame(seqID, data, 0, inFlags, outFlags);
    }

    public static short DecompressSequenceFrame(int seqID, int[] data, short inFlags, short[] outFlags, int asyncCompletion) {
        return decompressSequenceFrame(seqID, data, 0, inFlags, outFlags);
    }

    public static short DecompressSequenceFrameS(int seqID, byte[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion) {
        return decompressSequenceFrame(seqID, data, dataSize, inFlags, outFlags);
    }

    public static short DecompressSequenceFrameS(int seqID, int data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion) {
        return decompressSequenceFrame(seqID, data, dataSize, inFlags, outFlags);
    }

    public static short DecompressSequenceFrameS(int seqID, int[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion) {
        return decompressSequenceFrame(seqID, data, dataSize, inFlags, outFlags);
    }

    /** frames are decompressed at once, the frame time is not scheduled */
    public static short DecompressSequenceFrameWhen(int seqID, byte[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion, byte[] frameTime) {
        return decompressSequenceFrame(seqID, data, dataSize, inFlags, outFlags);
    }

    public static short DecompressSequenceFrameWhen(int seqID, int data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion, byte[] frameTime) {
        return decompressSequenceFrame(seqID, data, dataSize, inFlags, outFlags);
    }

    public static short DecompressSequenceFrameWhen(int seqID, int[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion, byte[] frameTime) {
        return decompressSequenceFrame(seqID, data, dataSize, inFlags, outFlags);
    }

    public static short SetDSequenceAccuracy(int seqID, int accuracy) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.accuracy = accuracy;
        return noErr;
    }

    public static short SetDSequenceFlags(int seqID, int flags, int flagsMask) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.flags = (s.flags & ~flagsMask) | (flags & flagsMask);
        return noErr;
    }

    public static short SetDSequenceMask(int seqID, int mask) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        if (s.mask != 0) {
            Heap.disposeHandle(s.mask);
        }
        s.mask = mask != 0 ? QuickDrawManager.copyRegion(mask) : 0;
        return noErr;
    }

    public static short SetDSequenceMatte(int seqID, int matte, byte[] matteRect) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.matte = matte;
        return noErr;
    }

    public static short SetDSequenceSrcRect(int seqID, byte[] srcRect) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.srcRect = srcRect != null ? Imaging.rect(srcRect) : null;
        return noErr;
    }

    public static short SetDSequenceTimeCode(int seqID, byte[] timeCodeFormat, byte[] timeCodeTime) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.timeCode = timeCodeTime != null ? timeCodeTime.clone() : null;
        return noErr;
    }

    public static short SetDSequenceTransferMode(int seqID, short mode, byte[] opColor) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.mode = mode;
        s.opColor = opColor != null ? opColor.clone() : null;
        return noErr;
    }

    public static short GetDSequenceMatrix(int seqID, byte[] matrix) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        for (int i = 0; i < 9; i++) {
            Bytes.putInt(matrix, i * 4, s.matrix[i]);
        }
        return noErr;
    }

    public static short SetDSequenceMatrix(int seqID, byte[] matrix) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        s.matrix = matrix != null ? MatrixManager.read(matrix) : MatrixManager.identity();
        return noErr;
    }

    public static short GetDSequenceImageBuffer(int seqID, int[] gworld) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        if (s.imageBuffer == 0) {
            BufferedImage img = s.prev != null ? s.prev : Codecs.argb(s.desc.width(), s.desc.height());
            s.imageBuffer = gworld(img, 0);
        }
        gworld[0] = s.imageBuffer;
        return noErr;
    }

    /** there is no separate screen buffer, the port is drawn into directly */
    public static short GetDSequenceScreenBuffer(int seqID, int[] gworld) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        gworld[0] = s.port;
        return noErr;
    }

    /** points are passed as v (high) h (low) */
    private static boolean hit(DSeq s, Object data, int dataSize, int where) throws IOException {
        Codecs.Codec c = Codecs.get(s.desc.codec());
        if (c == null) {
            throw new IOException("no codec");
        }
        BufferedImage img = s.prev;
        if (data != null && Imaging.capacity(data) > 0) {
            img = c.decode(Imaging.read(data, dataSize > 0 ? dataSize : s.desc.dataSize()), s.desc, c.isTemporal() ? s.prev : null);
        }
        if (img == null) {
            return false;
        }
        int v = where >> 16, h = (short) where;
        try {
            Point2D p = Imaging.transform(s.matrix).inverseTransform(new Point2D.Double(h, v), null);
            int x = (int) Math.floor(p.getX()) + (s.srcRect != null ? s.srcRect.x : 0);
            int y = (int) Math.floor(p.getY()) + (s.srcRect != null ? s.srcRect.y : 0);
            if (x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) {
                return false;
            }
            return (img.getRGB(x, y) >>> 24) != 0;
        } catch (NoninvertibleTransformException e) {
            return false;
        }
    }

    private static short hitTest(int seqID, Object data, int dataSize, int where, int[] hit) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        try {
            hit[0] = hit(s, data, dataSize, where) ? 1 : 0;
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecErr;
        }
    }

    public static short HitTestDSequenceData(int seqID, byte[] data, int dataSize, int where, int[] hit, int hitFlags) {
        return hitTest(seqID, data, dataSize, where, hit);
    }

    public static short HitTestDSequenceData(int seqID, int data, int dataSize, int where, int[] hit, int hitFlags) {
        return hitTest(seqID, data, dataSize, where, hit);
    }

    public static short HitTestDSequenceData(int seqID, int[] data, int dataSize, int where, int[] hit, int hitFlags) {
        return hitTest(seqID, data, dataSize, where, hit);
    }

    private static short ptIn(int seqID, Object data, int dataSize, int where, byte[] hit) {
        int[] h = new int[1];
        short e = hitTest(seqID, data, dataSize, where, h);
        hit[0] = (byte) h[0];
        return e;
    }

    public static short PtInDSequenceData(int seqID, byte[] data, int dataSize, int where, byte[] hit) {
        return ptIn(seqID, data, dataSize, where, hit);
    }

    public static short PtInDSequenceData(int seqID, int data, int dataSize, int where, byte[] hit) {
        return ptIn(seqID, data, dataSize, where, hit);
    }

    public static short PtInDSequenceData(int seqID, int[] data, int dataSize, int where, byte[] hit) {
        return ptIn(seqID, data, dataSize, where, hit);
    }

    public static short ICMShieldSequenceCursor(int seqID) {
        return noErr;
    }

    public static short CDSequenceBusy(int seqID) {
        return 0;
    }

    public static short CDSequenceEnd(int seqID) {
        CSeq c = cseq(seqID);
        if (c != null) {
            if (c.prevGWorld != 0) {
                QuickDrawManager.disposePort(c.prevGWorld);
            }
            Heap.free(seqID);
            return noErr;
        }
        DSeq d = dseq(seqID);
        if (d != null) {
            if (d.imageBuffer != 0) {
                QuickDrawManager.disposePort(d.imageBuffer);
            }
            if (d.mask != 0) {
                Heap.disposeHandle(d.mask);
            }
            synchronized (sources) {
                sources.removeIf(s -> s.seq == d);
            }
            Heap.free(seqID);
            return noErr;
        }
        return paramErr;
    }

    public static short CDSequenceEquivalentImageDescription(int seqID, int newDesc, byte[] equivalent) {
        DSeq s = dseq(seqID);
        CSeq c = s == null ? cseq(seqID) : null;
        byte[] cur = s != null ? s.desc.d : c != null && c.desc != 0 ? Heap.handleBytes(c.desc) : null;
        if (cur == null) {
            return paramErr;
        }
        byte[] n = Heap.handleBytes(newDesc);
        boolean same = Bytes.getInt(cur, 4) == Bytes.getInt(n, 4) && Bytes.getShort(cur, 32) == Bytes.getShort(n, 32)
                && Bytes.getShort(cur, 34) == Bytes.getShort(n, 34) && Bytes.getShort(cur, 82) == Bytes.getShort(n, 82)
                && Arrays.equals(Arrays.copyOfRange(cur, extensionsStart(cur), cur.length), Arrays.copyOfRange(n, extensionsStart(n), n.length));
        equivalent[0] = (byte) (same ? 1 : 0);
        return noErr;
    }

    public static short CDSequenceFlush(int seqID) {
        return dseq(seqID) != null || cseq(seqID) != null ? noErr : paramErr;
    }

    public static short CDSequenceInvalidate(int seqID, int invalRgn) {
        DSeq s = dseq(seqID);
        if (s == null) {
            return paramErr;
        }
        if (s.prev != null) {
            Imaging.draw(s.port, s.prev, transform(s, s.prev), s.mode, s.opColor, invalRgn);
        }
        return noErr;
    }

    public static short CDSequenceNewDataSource(int seqID, int[] source, int sourceType, int sourceInputNumber, int dataDescription, int transferProc, int refCon) {
        Object seq = dseq(seqID) != null ? dseq(seqID) : cseq(seqID);
        if (seq == null) {
            return paramErr;
        }
        Source s = new Source(seq);
        s.sourceType = sourceType;
        s.inputNumber = sourceInputNumber;
        s.dataDescription = dataDescription;
        s.transferProc = transferProc;
        s.refCon = refCon;
        synchronized (sources) {
            sources.add(s);
        }
        source[0] = s.ref;
        return noErr;
    }

    public static short CDSequenceGetDataSource(int seqID, int[] source, int sourceType, int sourceInputNumber) {
        Object seq = dseq(seqID) != null ? dseq(seqID) : cseq(seqID);
        synchronized (sources) {
            for (Source s : sources) {
                if (s.seq == seq && s.sourceType == sourceType && s.inputNumber == sourceInputNumber) {
                    source[0] = s.ref;
                    return noErr;
                }
            }
        }
        source[0] = 0;
        return paramErr;
    }

    private static short setSourceData(int source, Object data, int dataSize) {
        Source s = Heap.peerOrNull(source, Source.class);
        if (s == null) {
            return paramErr;
        }
        s.data = Imaging.read(data, dataSize);
        s.changed = true;
        return noErr;
    }

    public static short CDSequenceSetSourceData(int source, byte[] data, int dataSize) {
        return setSourceData(source, data, dataSize);
    }

    public static short CDSequenceSetSourceData(int source, int data, int dataSize) {
        return setSourceData(source, data, dataSize);
    }

    public static short CDSequenceSetSourceData(int source, int[] data, int dataSize) {
        return setSourceData(source, data, dataSize);
    }

    public static short CDSequenceChangedSourceData(int source) {
        Source s = Heap.peerOrNull(source, Source.class);
        if (s == null) {
            return paramErr;
        }
        s.changed = true;
        return noErr;
    }

    public static short CDSequenceDisposeDataSource(int source) {
        Source s = Heap.peerOrNull(source, Source.class);
        if (s == null) {
            return paramErr;
        }
        synchronized (sources) {
            sources.remove(s);
        }
        Heap.free(source);
        return noErr;
    }

    // ---- standard compression

    static final int scSpatialSettingsType = Bytes.fourCC("sptl");
    static final int scTemporalSettingsType = Bytes.fourCC("tprl");
    static final int scDataRateSettingsType = Bytes.fourCC("drat");
    static final int scColorTableType = Bytes.fourCC("clut");
    static final int scProgressProcType = Bytes.fourCC("prog");
    static final int scExtendedProcsType = Bytes.fourCC("xprc");
    static final int scPreferenceFlagsType = Bytes.fourCC("pref");
    static final int scSettingsStateType = Bytes.fourCC("ssta");
    static final int scSequenceIDType = Bytes.fourCC("sequ");
    static final int scWindowPositionType = Bytes.fourCC("wndw");
    static final int scCodecFlagsType = Bytes.fourCC("cflg");
    static final int scCodecSettingsType = Bytes.fourCC("cdec");
    static final int scForceKeyValueType = Bytes.fourCC("ksim");
    static final int scSoundSampleRateType = Bytes.fourCC("ssrt");
    static final int scSoundSampleSizeType = Bytes.fourCC("sssz");
    static final int scSoundChannelCountType = Bytes.fourCC("sscc");
    static final int scSoundCompressionType = Bytes.fourCC("ssct");
    static final int scCompressionListType = Bytes.fourCC("ctyl");
    static final int scCodecManufacturerType = Bytes.fourCC("cmfr");

    /** settings that are handles */
    private static boolean isHandleType(int type) {
        return type == scColorTableType || type == scSettingsStateType || type == scCodecSettingsType || type == scCompressionListType;
    }

    /** @return the size of a setting */
    private static int sizeOf(int type) {
        if (type == scSpatialSettingsType) {
            return 14;
        } else if (type == scTemporalSettingsType) {
            return 12;
        } else if (type == scDataRateSettingsType) {
            return 16;
        } else if (type == scExtendedProcsType) {
            return 48;
        } else if (type == scCodecFlagsType || type == scSoundSampleSizeType || type == scSoundChannelCountType) {
            return 2;
        }
        return 4;
    }

    /** the state of a standard compression component */
    static final class SC {
        final boolean sound;
        final Map<Integer, byte[]> info = new HashMap<>();
        int compressFlags;
        BufferedImage testImage;
        CSeq seq;
        int seqDesc;
        int seqData;

        SC(boolean sound) {
            this.sound = sound;
            byte[] sptl = new byte[14];
            Bytes.putInt(sptl, 0, Bytes.fourCC("jpeg"));
            Bytes.putShort(sptl, 8, 0);
            Bytes.putInt(sptl, 10, Codecs.codecNormalQuality);
            info.put(scSpatialSettingsType, sptl);
            byte[] tprl = new byte[12];
            Bytes.putInt(tprl, 4, 15 << 16);
            info.put(scTemporalSettingsType, tprl);
            info.put(scDataRateSettingsType, new byte[16]);
            byte[] pref = new byte[4];
            Bytes.putInt(pref, 0, 1 | 2 | 4); // show best depth, use movable modal, disallow zero frame rate
            info.put(scPreferenceFlagsType, pref);
            info.put(scSoundSampleRateType, int4(0xac44_0000));
            info.put(scSoundSampleSizeType, new byte[] {0, 16});
            info.put(scSoundChannelCountType, new byte[] {0, 2});
            info.put(scSoundCompressionType, int4(Bytes.fourCC("twos")));
            info.put(scCodecManufacturerType, int4(Bytes.fourCC("appl")));
            info.put(scCodecFlagsType, new byte[2]);
            info.put(scForceKeyValueType, int4(0));
        }

        int spatial(int off) {
            return off == 8 ? Bytes.getShort(info.get(scSpatialSettingsType), 8) : Bytes.getInt(info.get(scSpatialSettingsType), off);
        }

        int temporal(int off) {
            return Bytes.getInt(info.get(scTemporalSettingsType), off);
        }

        /** the state handle: count(4) {type(4) size(4) data}... */
        byte[] state() {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] n = int4(info.size());
            os.writeBytes(n);
            for (Map.Entry<Integer, byte[]> e : info.entrySet()) {
                os.writeBytes(int4(e.getKey()));
                os.writeBytes(int4(e.getValue().length));
                os.writeBytes(e.getValue());
            }
            return os.toByteArray();
        }

        void restore(byte[] b) {
            int n = Bytes.getInt(b, 0);
            int p = 4;
            for (int i = 0; i < n && p + 8 <= b.length; i++) {
                int type = Bytes.getInt(b, p);
                int size = Bytes.getInt(b, p + 4);
                info.put(type, Arrays.copyOfRange(b, p + 8, p + 8 + size));
                p += 8 + size;
            }
        }
    }

    static byte[] int4(int v) {
        byte[] b = new byte[4];
        Bytes.putInt(b, 0, v);
        return b;
    }

    static SC sc(int ci) {
        ComponentManager.InstanceRec i = ComponentManager.instance(ci);
        return i != null && i.impl instanceof SC s ? s : null;
    }

    private static int scGet(int ci, int type, Object out) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        if (type == scSettingsStateType) {
            int h = Heap.newHandle(s.state());
            ((int[]) out)[0] = h;
            return noErr;
        }
        if (type == scSequenceIDType) {
            ((int[]) out)[0] = s.seq != null ? s.seq.ref : 0;
            return noErr;
        }
        byte[] v = s.info.get(type);
        if (isHandleType(type)) {
            int[] a = (int[]) out;
            if (v == null) {
                a[0] = 0;
                return type == scColorTableType || type == scCodecSettingsType ? noErr : scTypeNotFoundErr;
            }
            if (a[0] != 0 && Heap.isValid(a[0]) && type == scCompressionListType) {
                // filled in place, the caller passed its own handle
                Heap.setHandleBytes(a[0], v);
            } else {
                a[0] = Heap.newHandle(v);
            }
            return noErr;
        }
        if (v == null) {
            return scTypeNotFoundErr;
        }
        Bytes.toArray(v, 0, out, 0, Math.min(v.length, Imaging.capacity(out)));
        return noErr;
    }

    private static int scSet(int ci, int type, Object in) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        if (isHandleType(type)) {
            int h = in == null ? 0 : in instanceof int[] a ? a[0] : (Integer) in;
            if (type == scSettingsStateType) {
                if (h != 0) {
                    s.restore(Heap.handleBytes(h));
                }
                return noErr;
            }
            if (h == 0) {
                s.info.remove(type);
            } else {
                s.info.put(type, Heap.handleBytes(h));
            }
            return noErr;
        }
        if (in == null) {
            s.info.remove(type);
            return noErr;
        }
        int size = sizeOf(type);
        byte[] v = in instanceof Integer p ? Heap.read(p, size) : Imaging.read(in, size);
        s.info.put(type, v);
        return noErr;
    }

    public static int SCGetInfo(int ci, int type, byte[] info) {
        return scGet(ci, type, info);
    }

    public static int SCGetInfo(int ci, int type, int[] info) {
        return scGet(ci, type, info);
    }

    public static int SCGetInfo(int ci, int type, short[] info) {
        return scGet(ci, type, info);
    }

    public static int SCSetInfo(int ci, int type, byte[] info) {
        return scSet(ci, type, info);
    }

    /** the value is a handle for handle settings, else a pointer to the setting */
    public static int SCSetInfo(int ci, int type, int info) {
        return scSet(ci, type, info);
    }

    public static int SCSetInfo(int ci, int type, int[] info) {
        return scSet(ci, type, info);
    }

    public static int SCSetInfo(int ci, int type, short[] info) {
        return scSet(ci, type, info);
    }

    public static int SCGetCompressFlags(int ci, int[] flags) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        flags[0] = s.compressFlags;
        return noErr;
    }

    public static int SCSetCompressFlags(int ci, int flags) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        s.compressFlags = flags;
        return noErr;
    }

    /** default settings for an image: lossless for indexed images, photo for deep ones */
    private static int scDefaults(int ci, BufferedImage img, int depth, boolean motion) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        byte[] sptl = s.info.get(scSpatialSettingsType).clone();
        Bytes.putInt(sptl, 0, depth <= 16 ? Bytes.fourCC("rle ") : Bytes.fourCC("jpeg"));
        Bytes.putInt(sptl, 4, 0);
        Bytes.putShort(sptl, 8, 0);
        Bytes.putInt(sptl, 10, Codecs.codecNormalQuality);
        s.info.put(scSpatialSettingsType, sptl);
        if (motion) {
            byte[] tprl = s.info.get(scTemporalSettingsType).clone();
            Bytes.putInt(tprl, 0, Codecs.codecNormalQuality);
            s.info.put(scTemporalSettingsType, tprl);
        }
        s.testImage = img;
        return noErr;
    }

    public static int SCDefaultPictFileSettings(int ci, short srcRef, short motion) {
        return scDefaults(ci, null, 32, motion != 0);
    }

    public static int SCDefaultPictHandleSettings(int ci, int srcPicture, short motion) {
        try {
            return scDefaults(ci, pictImage(srcPicture), 32, motion != 0);
        } catch (IOException e) {
            return scDefaults(ci, null, 32, motion != 0);
        }
    }

    public static int SCDefaultPixMapSettings(int ci, int src, short motion) {
        return scDefaults(ci, Imaging.read(src, null), Imaging.depth(src), motion != 0);
    }

    public static int SCSetTestImagePictFile(int ci, short testFileRef, byte[] testRect, short testFlags) {
        return sc(ci) != null ? noErr : invalidComponentID;
    }

    public static int SCSetTestImagePictHandle(int ci, int testPict, byte[] testRect, short testFlags) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        try {
            s.testImage = testPict != 0 ? pictImage(testPict) : null;
        } catch (IOException e) {
            s.testImage = null;
        }
        return noErr;
    }

    public static int SCSetTestImagePixMap(int ci, int testPixMap, byte[] testRect, short testFlags) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        s.testImage = testPixMap != 0 ? Imaging.read(testPixMap, testRect != null ? Imaging.rect(testRect) : null) : null;
        return noErr;
    }

    /** @return 0 if the settings were accepted, 1 if the dialog was canceled */
    private static int request(int ci, boolean sequence) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            // nobody to ask, the current settings stand
            return noErr;
        }
        return SCDialog.show(s, sequence) ? noErr : 1;
    }

    public static int SCRequestImageSettings(int ci) {
        return request(ci, false);
    }

    public static int SCRequestSequenceSettings(int ci) {
        return request(ci, true);
    }

    /** @return the codec of the spatial settings */
    private static Codecs.Codec scCodec(SC s) {
        Codecs.Codec c = codec(s.spatial(0), s.spatial(4));
        return c != null && c.canCompress() ? c : Codecs.get(raw);
    }

    public static int SCCompressImage(int ci, int src, byte[] srcRect, int[] desc, int[] data) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        try {
            Codecs.Codec c = scCodec(s);
            BufferedImage img = Imaging.read(src, srcRect != null ? Imaging.rect(srcRect) : null);
            byte[][] out = compress(img, null, c, depth(c, s.spatial(8), Imaging.depth(src)), s.spatial(10));
            desc[0] = Heap.newHandle(out[0]);
            data[0] = Heap.newHandle(out[1]);
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "SC compress", e);
            return codecErr;
        }
    }

    public static int SCCompressPicture(int ci, int srcPicture, int dstPicture) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        try {
            Codecs.Codec c = scCodec(s);
            byte[] b = Heap.handleBytes(srcPicture);
            Heap.setHandleBytes(dstPicture, compressedPicture(pictImage(srcPicture), PictCodec.frame(b), c, depth(c, s.spatial(8), 32), s.spatial(10)));
            return noErr;
        } catch (IOException | RuntimeException e) {
            return codecErr;
        }
    }

    /** PICT files have a 512 byte header */
    public static int SCCompressPictureFile(int ci, short srcRefNum, short dstRefNum) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        try {
            byte[] src = FileManager.readFork(srcRefNum);
            byte[] pict = src.length > 512 ? Arrays.copyOfRange(src, 512, src.length) : src;
            BufferedImage img = PictCodec.decode(pict);
            if (img == null) {
                return codecBadDataErr;
            }
            Codecs.Codec c = scCodec(s);
            byte[] out = compressedPicture(img, PictCodec.frame(pict), c, depth(c, s.spatial(8), 32), s.spatial(10));
            byte[] file = new byte[512 + out.length];
            System.arraycopy(out, 0, file, 512, out.length);
            FileManager.writeFork(dstRefNum, file);
            return noErr;
        } catch (IOException | RuntimeException e) {
            logger.log(Level.FINE, "SC compress picture file", e);
            return codecErr;
        }
    }

    public static int SCCompressSequenceBegin(int ci, int src, byte[] srcRect, int[] desc) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        Codecs.Codec c = scCodec(s);
        if (s.seq != null) {
            SCCompressSequenceEnd(ci);
        }
        s.seqDesc = Heap.newHandle(0);
        int[] id = new int[1];
        short e = CompressSequenceBegin(id, src, 0, srcRect, null, (short) s.spatial(8), c.type, 0, s.spatial(10), s.temporal(0), s.temporal(8), 0, (short) 0, s.seqDesc);
        if (e != noErr) {
            Heap.disposeHandle(s.seqDesc);
            s.seqDesc = 0;
            return e;
        }
        s.seq = cseq(id[0]);
        s.seqData = Heap.newHandle(0);
        if (desc != null && desc.length > 0) {
            desc[0] = s.seqDesc;
        }
        return noErr;
    }

    /** the data handle belongs to the component */
    public static int SCCompressSequenceFrame(int ci, int src, byte[] srcRect, int[] data, int[] dataSize, short[] notSyncFlag) {
        SC s = sc(ci);
        if (s == null || s.seq == null) {
            return s == null ? invalidComponentID : paramErr;
        }
        int max = s.seq.codec.maxSize(s.seq.srcRect.width, s.seq.srcRect.height, s.seq.depth);
        int buffer = Heap.newPtr(max);
        try {
            int[] size = new int[1];
            byte[] similarity = new byte[1];
            int ksim = s.info.containsKey(scForceKeyValueType) ? Bytes.getInt(s.info.get(scForceKeyValueType), 0) : 0;
            short e = compressSequenceFrame(s.seq.ref, src, srcRect, (short) 0, buffer, size, similarity);
            if (e != noErr) {
                return e;
            }
            if (ksim > 0 && similarity[0] != 0 && (similarity[0] & 0xff) < ksim) {
                // too different, make it a key frame
                s.seq.prev = null;
                s.seq.frameNumber--;
                e = compressSequenceFrame(s.seq.ref, src, srcRect, (short) codecFlagForceKeyFrame, buffer, size, similarity);
                if (e != noErr) {
                    return e;
                }
            }
            Heap.setHandleBytes(s.seqData, Heap.read(buffer, size[0]));
            data[0] = s.seqData;
            dataSize[0] = size[0];
            notSyncFlag[0] = (short) (similarity[0] != 0 ? 1 : 0);
            return noErr;
        } finally {
            Heap.free(buffer);
        }
    }

    public static int SCCompressSequenceEnd(int ci) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        if (s.seq != null) {
            CDSequenceEnd(s.seq.ref);
            s.seq = null;
        }
        if (s.seqData != 0) {
            Heap.disposeHandle(s.seqData);
            s.seqData = 0;
        }
        if (s.seqDesc != 0) {
            Heap.disposeHandle(s.seqDesc);
            s.seqDesc = 0;
        }
        return noErr;
    }

    /** the settings as atoms of their types, sound and image settings alike */
    public static int SCGetSettingsAsAtomContainer(int ci, int[] settings) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        int[] ac = new int[1];
        AtomManager.QTNewAtomContainer(ac);
        for (Map.Entry<Integer, byte[]> e : s.info.entrySet()) {
            int[] atom = new int[1];
            byte[] v = e.getValue();
            AtomManager.QTInsertChild(ac[0], 0, e.getKey(), 1, (short) 0, v.length, v, atom);
        }
        settings[0] = ac[0];
        return noErr;
    }

    public static int SCSetSettingsFromAtomContainer(int ci, int settings) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        if (settings == 0) {
            return paramErr;
        }
        for (Map.Entry<Integer, byte[]> e : AtomManager.children(settings, 0).entrySet()) {
            s.info.put(e.getKey(), e.getValue());
        }
        return noErr;
    }

    public static int SCNewGWorld(int ci, int[] gworld, byte[] rect, int flags) {
        SC s = sc(ci);
        if (s == null) {
            return invalidComponentID;
        }
        int depth = s.spatial(8);
        int format = depth == 0 || depth == 24 || depth == 32 ? QuickDrawManager.k32ARGBPixelFormat : depth;
        return QuickDrawManager.QTNewGWorld(gworld, format, rect, 0, 0, flags);
    }

    // ---- data codecs

    private static boolean isDataCodec(int dc, int type) {
        ComponentManager.ComponentRec c = ComponentManager.component(dc);
        return c != null && c.type == type;
    }

    public static int DataCodecGetCompressBufferSize(int dc, int srcSize, int[] dstSize) {
        if (!isDataCodec(dc, dcom)) {
            return invalidComponentID;
        }
        dstSize[0] = srcSize + srcSize / 1000 + 64;
        return noErr;
    }

    public static int DataCodecCompress(int dc, int srcData, int srcSize, int dstData, int dstBufferSize, int[] actualDstSize, int[] decompressSlop) {
        if (!isDataCodec(dc, dcom)) {
            return invalidComponentID;
        }
        Deflater d = new Deflater();
        try {
            d.setInput(Heap.read(srcData, srcSize));
            d.finish();
            byte[] out = new byte[Math.max(64, dstBufferSize)];
            int n = d.deflate(out);
            if (!d.finished() || n > dstBufferSize) {
                return codecSizeErr;
            }
            Heap.write(dstData, out, 0, n);
            actualDstSize[0] = n;
            if (decompressSlop != null && decompressSlop.length > 0) {
                decompressSlop[0] = 0;
            }
            return noErr;
        } finally {
            d.end();
        }
    }

    public static int DataCodecDecompress(int dc, int srcData, int srcSize, int dstData, int dstBufferSize) {
        if (!isDataCodec(dc, ddec)) {
            return invalidComponentID;
        }
        Inflater i = new Inflater();
        try {
            i.setInput(Heap.read(srcData, srcSize));
            byte[] out = new byte[dstBufferSize];
            int n = i.inflate(out);
            Heap.write(dstData, out, 0, n);
            return noErr;
        } catch (DataFormatException e) {
            return codecBadDataErr;
        } finally {
            i.end();
        }
    }

    // ---- curves

    public static int CurveCreateVectorStream(int effect, int[] stream) {
        stream[0] = Heap.newHandle(0);
        return noErr;
    }

    /** a vector stream is a list of atoms size(4) type(4) data; data size 0 and no data removes the atoms of the type */
    private static int curveAddAtom(int stream, int atomType, int dataSize, Object data) {
        byte[] s = Heap.handleBytes(stream);
        if (dataSize == 0 && (data == null || data instanceof Integer p && p == 0)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (int[] a : Curves.atoms(s)) {
                if (a[2] != atomType) {
                    os.write(s, a[0], a[1]);
                }
            }
            Heap.setHandleBytes(stream, os.toByteArray());
            return noErr;
        }
        byte[] b = Imaging.read(data, dataSize);
        byte[] n = Arrays.copyOf(s, s.length + 8 + b.length);
        Bytes.putInt(n, s.length, 8 + b.length);
        Bytes.putInt(n, s.length + 4, atomType);
        System.arraycopy(b, 0, n, s.length + 8, b.length);
        Heap.setHandleBytes(stream, n);
        return noErr;
    }

    public static int CurveAddAtomToVectorStream(int effect, int atomType, int dataSize, byte[] data, int stream) {
        return curveAddAtom(stream, atomType, dataSize, data);
    }

    public static int CurveAddAtomToVectorStream(int effect, int atomType, int dataSize, int data, int stream) {
        return curveAddAtom(stream, atomType, dataSize, data);
    }

    public static int CurveAddAtomToVectorStream(int effect, int atomType, int dataSize, int[] data, int stream) {
        return curveAddAtom(stream, atomType, dataSize, data);
    }

    public static int CurveAddPathAtomToVectorStream(int effect, int pathData, int stream) {
        byte[] p = Heap.handleBytes(pathData);
        return curveAddAtom(stream, path, p.length, p);
    }

    /** the zero atom ends the stream */
    public static int CurveAddZeroAtomToVectorStream(int effect, int stream) {
        byte[] s = Heap.handleBytes(stream);
        Heap.setHandleBytes(stream, Arrays.copyOf(s, s.length + 4));
        return noErr;
    }

    public static int CurveGetAtomDataFromVectorStream(int effect, int stream, int atomType, int[] dataSize, int[] dataPtr) {
        byte[] s = Heap.handleBytes(stream);
        for (int[] a : Curves.atoms(s)) {
            if (a[2] == atomType) {
                dataSize[0] = a[1] - 8;
                dataPtr[0] = Heap.deref(stream) + a[0] + 8;
                return noErr;
            }
        }
        return codecExtensionNotFoundErr;
    }

    public static int CurveNewPath(int effect, int[] pathHandle) {
        byte[] b = new byte[8];
        Bytes.putInt(b, 0, 1);
        pathHandle[0] = Heap.newHandle(b);
        return noErr;
    }

    public static int CurveCountPointsInPath(int effect, byte[] paths, int contourIndex, int[] count) {
        Curves.Contour c = Curves.contour(paths, contourIndex);
        if (c == null) {
            return paramErr;
        }
        count[0] = c.points.size();
        return noErr;
    }

    public static int CurveGetPathPoint(int effect, byte[] paths, int contourIndex, int pointIndex, byte[] point, byte[] isOnPath) {
        Curves.Contour c = Curves.contour(paths, contourIndex);
        if (c == null || pointIndex < 0 || pointIndex >= c.points.size()) {
            return paramErr;
        }
        double[] p = c.points.get(pointIndex);
        Bytes.putInt(point, 0, Bytes.doubleToFixed(p[0]));
        Bytes.putInt(point, 4, Bytes.doubleToFixed(p[1]));
        isOnPath[0] = (byte) (c.onCurve.get(pointIndex) ? 1 : 0);
        return noErr;
    }

    /** the paths are changed in place */
    public static int CurveSetPathPoint(int effect, byte[] paths, int contourIndex, int pointIndex, byte[] point, byte isOnPath) {
        Curves.Contour c = Curves.contour(paths, contourIndex);
        if (c == null || pointIndex < 0 || pointIndex >= c.points.size()) {
            return paramErr;
        }
        c.points.set(pointIndex, new double[] {Bytes.fixedToDouble(Bytes.getInt(point, 0)), Bytes.fixedToDouble(Bytes.getInt(point, 4))});
        c.onCurve.set(pointIndex, isOnPath != 0);
        byte[] n = Curves.replace(paths, contourIndex, c);
        if (n.length != paths.length) {
            return paramErr;
        }
        System.arraycopy(n, 0, paths, 0, n.length);
        return noErr;
    }

    public static int CurveInsertPointIntoPath(int effect, byte[] point, int pathHandle, int contourIndex, int pointIndex, byte isOnPath) {
        byte[] paths = Heap.handleBytes(pathHandle);
        Curves.Contour c = Curves.contour(paths, contourIndex);
        if (c == null) {
            return paramErr;
        }
        int i = Math.max(0, Math.min(pointIndex, c.points.size()));
        c.points.add(i, new double[] {Bytes.fixedToDouble(Bytes.getInt(point, 0)), Bytes.fixedToDouble(Bytes.getInt(point, 4))});
        c.onCurve.add(i, isOnPath != 0);
        Heap.setHandleBytes(pathHandle, Curves.replace(paths, contourIndex, c));
        return noErr;
    }

    /** the length is a wide holding a Fixed */
    public static int CurveGetLength(int effect, byte[] paths, int contourIndex, long[] length) {
        Curves.Contour c = Curves.contour(paths, contourIndex);
        if (c == null) {
            return paramErr;
        }
        length[0] = Math.round(Curves.length(c) * 65536);
        return noErr;
    }

    public static int CurveLengthToPoint(int effect, byte[] paths, int contourIndex, int length, byte[] point, byte[] tangent) {
        Curves.Contour c = Curves.contour(paths, contourIndex);
        if (c == null) {
            return paramErr;
        }
        double[][] pt = Curves.pointAt(c, Bytes.fixedToDouble(length));
        Bytes.putInt(point, 0, Bytes.doubleToFixed(pt[0][0]));
        Bytes.putInt(point, 4, Bytes.doubleToFixed(pt[0][1]));
        if (tangent != null && tangent.length >= 8) {
            Bytes.putInt(tangent, 0, Bytes.doubleToFixed(pt[1][0]));
            Bytes.putInt(tangent, 4, Bytes.doubleToFixed(pt[1][1]));
        }
        return noErr;
    }

    /** @param startDist the Fixed distance to start at, endDist where to stop, the length to the nearest point between them */
    public static int CurvePathPointToLength(int effect, byte[] paths, int startDist, int endDist, byte[] point, int[] length) {
        Curves.Contour c = Curves.contour(paths, 0);
        if (c == null) {
            return paramErr;
        }
        double x = Bytes.fixedToDouble(Bytes.getInt(point, 0)), y = Bytes.fixedToDouble(Bytes.getInt(point, 4));
        double start = Bytes.fixedToDouble(startDist), end = endDist == 0 ? Curves.length(c) : Bytes.fixedToDouble(endDist);
        length[0] = Bytes.doubleToFixed(Curves.nearestLength(c, x, y, start, end));
        return noErr;
    }

    public static int CurveGetNearestPathPoint(int effect, byte[] paths, byte[] point, int[] contourIndex, int[] pointIndex, int[] delta) {
        double x = Bytes.fixedToDouble(Bytes.getInt(point, 0)), y = Bytes.fixedToDouble(Bytes.getInt(point, 4));
        int contours = Bytes.getInt(paths, 0);
        double best = Double.MAX_VALUE;
        for (int k = 0; k < contours; k++) {
            Curves.Contour c = Curves.contour(paths, k);
            if (c == null) {
                break;
            }
            for (int i = 0; i < c.points.size(); i++) {
                double[] p = c.points.get(i);
                double d = Math.hypot(p[0] - x, p[1] - y);
                if (d < best) {
                    best = d;
                    contourIndex[0] = k;
                    pointIndex[0] = i;
                }
            }
        }
        if (best == Double.MAX_VALUE) {
            return paramErr;
        }
        delta[0] = Bytes.doubleToFixed(best);
        return noErr;
    }
}
