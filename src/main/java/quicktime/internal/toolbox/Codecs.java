/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


/**
 * Image codecs of the Image Compression Manager in Java.
 * <p>
 * Images are ARGB {@link BufferedImage}s, compressed data is described by an 86 byte image description
 * (optionally followed by a color table and extension atoms).
 */
final class Codecs {

    private Codecs() {
    }

    static final int codecMinQuality = 0x000;
    static final int codecLowQuality = 0x100;
    static final int codecNormalQuality = 0x200;
    static final int codecHighQuality = 0x300;
    static final int codecMaxQuality = 0x3ff;
    static final int codecLosslessQuality = 0x400;

    // CodecInfo flags
    static final int codecInfoDoes1 = 1;
    static final int codecInfoDoes2 = 1 << 1;
    static final int codecInfoDoes4 = 1 << 2;
    static final int codecInfoDoes8 = 1 << 3;
    static final int codecInfoDoes16 = 1 << 4;
    static final int codecInfoDoes32 = 1 << 5;
    static final int codecInfoDoesDither = 1 << 6;
    static final int codecInfoDoesStretch = 1 << 7;
    static final int codecInfoDoesShrink = 1 << 8;
    static final int codecInfoDoesMask = 1 << 9;
    static final int codecInfoDoesTemporal = 1 << 10;
    static final int codecInfoDoesSpool = 1 << 15;

    static final int codecInfoDepth1 = 1;
    static final int codecInfoDepth2 = 1 << 1;
    static final int codecInfoDepth4 = 1 << 2;
    static final int codecInfoDepth8 = 1 << 3;
    static final int codecInfoDepth16 = 1 << 4;
    static final int codecInfoDepth32 = 1 << 5;
    static final int codecInfoDepth24 = 1 << 6;
    static final int codecInfoDepth33 = 1 << 7;
    static final int codecInfoDepth34 = 1 << 8;
    static final int codecInfoDepth36 = 1 << 9;
    static final int codecInfoDepth40 = 1 << 10;
    static final int codecInfoStoresClut = 1 << 11;
    static final int codecInfoDoesLossless = 1 << 12;
    static final int codecInfoSequenceSensitive = 1 << 13;

    static final int DESCRIPTION_SIZE = 86;

    /** an image description */
    static final class Desc {
        final byte[] d;
        /** decoder state of a sequence using this description */
        Object state;

        Desc(byte[] d) {
            this.d = d;
        }

        int size() {
            return Bytes.getInt(d, 0);
        }

        int codec() {
            return Bytes.getInt(d, 4);
        }

        int width() {
            return Bytes.getShort(d, 32) & 0xffff;
        }

        int height() {
            return Bytes.getShort(d, 34) & 0xffff;
        }

        int dataSize() {
            return Bytes.getInt(d, 44);
        }

        int depth() {
            return Bytes.getShort(d, 82);
        }

        int clutId() {
            return Bytes.getShort(d, 84);
        }

        int spatialQuality() {
            return Bytes.getInt(d, 28);
        }

        String name() {
            return Bytes.getPString(d, 50);
        }

        /** @return the bits per pixel of the data, gray depths are 33 - 40 */
        int bits() {
            int depth = depth();
            return depth > 32 ? depth - 32 : depth;
        }

        /** @return the ARGB palette for indexed depths */
        int[] palette() {
            int depth = depth();
            if (depth > 32) {
                return grayPalette(depth - 32);
            }
            if (depth > 8) {
                return null;
            }
            if (clutId() == 0 && d.length >= DESCRIPTION_SIZE + 8 && size() > DESCRIPTION_SIZE) {
                return readColorTable(d, DESCRIPTION_SIZE, 1 << depth);
            }
            return defaultPalette(depth);
        }
    }

    /** @return a new image description */
    static byte[] description(int codec, int width, int height, int depth, String name, int dataSize, int quality) {
        byte[] d = new byte[DESCRIPTION_SIZE];
        Bytes.putInt(d, 0, DESCRIPTION_SIZE);
        Bytes.putInt(d, 4, codec);
        Bytes.putShort(d, 14, 1); // dataRefIndex
        Bytes.putShort(d, 16, 1); // version
        Bytes.putShort(d, 18, 1); // revision
        Bytes.putInt(d, 20, Bytes.fourCC("appl"));
        Bytes.putInt(d, 24, Math.min(quality, codecMaxQuality));
        Bytes.putInt(d, 28, quality);
        Bytes.putShort(d, 32, width);
        Bytes.putShort(d, 34, height);
        Bytes.putInt(d, 36, 72 << 16);
        Bytes.putInt(d, 40, 72 << 16);
        Bytes.putInt(d, 44, dataSize);
        Bytes.putShort(d, 48, 1);
        Bytes.putPString(d, 50, name, 32);
        Bytes.putShort(d, 82, depth);
        Bytes.putShort(d, 84, -1);
        return d;
    }

    // ---- color tables

    /** @return the palette of a ColorTable: seed(4) flags(2) size(2) {value(2) r g b(2)}... */
    static int[] readColorTable(byte[] b, int o, int count) {
        int n = (Bytes.getShort(b, o + 6) & 0xffff) + 1;
        int[] p = defaultPalette(Integer.numberOfTrailingZeros(Math.max(2, count)));
        boolean device = (Bytes.getShort(b, o + 4) & 0x8000) != 0;
        for (int i = 0; i < n && o + 8 + i * 8 + 8 <= b.length; i++) {
            int e = o + 8 + i * 8;
            int index = device ? i : Bytes.getShort(b, e) & 0xffff;
            if (index < p.length) {
                p[index] = 0xff00_0000 | ((b[e + 2] & 0xff) << 16) | ((b[e + 4] & 0xff) << 8) | (b[e + 6] & 0xff);
            }
        }
        return p;
    }

    /** @return the bytes of a ColorTable */
    static byte[] colorTable(int[] palette) {
        byte[] b = new byte[8 + palette.length * 8];
        Bytes.putShort(b, 6, palette.length - 1);
        for (int i = 0; i < palette.length; i++) {
            int e = 8 + i * 8;
            Bytes.putShort(b, e, i);
            int c = palette[i];
            Bytes.putShort(b, e + 2, ((c >> 16) & 0xff) * 0x101);
            Bytes.putShort(b, e + 4, ((c >> 8) & 0xff) * 0x101);
            Bytes.putShort(b, e + 6, (c & 0xff) * 0x101);
        }
        return b;
    }

    /** @return the gray palette of the depth, 0 is white as in the Mac gray color tables */
    static int[] grayPalette(int bits) {
        int n = 1 << bits;
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            int v = 255 - i * 255 / (n - 1);
            p[i] = 0xff00_0000 | v << 16 | v << 8 | v;
        }
        return p;
    }

    private static final int[] PALETTE_4 = {
            0xffffff, 0xfcf305, 0xff6402, 0xdd0806, 0xf20884, 0x4600a5, 0x0000d4, 0x02abea,
            0x1fb714, 0x006411, 0x562c05, 0x90713a, 0xc0c0c0, 0x808080, 0x404040, 0x000000,
    };

    /** @return the standard Mac color table of the depth */
    static int[] defaultPalette(int depth) {
        int[] p;
        switch (depth) {
            case 1 -> p = new int[] {0xffffff, 0x000000};
            case 2 -> p = new int[] {0xffffff, 0xacacac, 0x555555, 0x000000};
            case 4 -> p = PALETTE_4.clone();
            default -> {
                p = new int[256];
                int[] cube = {0xff, 0xcc, 0x99, 0x66, 0x33, 0x00};
                for (int i = 0; i < 215; i++) {
                    p[i] = cube[i / 36] << 16 | cube[(i / 6) % 6] << 8 | cube[i % 6];
                }
                int[] ramp = {0xee, 0xdd, 0xbb, 0xaa, 0x88, 0x77, 0x55, 0x44, 0x22, 0x11};
                for (int i = 0; i < 10; i++) {
                    p[215 + i] = ramp[i] << 16;
                    p[225 + i] = ramp[i] << 8;
                    p[235 + i] = ramp[i];
                    p[245 + i] = ramp[i] * 0x010101;
                }
                p[255] = 0;
            }
        }
        for (int i = 0; i < p.length; i++) {
            p[i] |= 0xff00_0000;
        }
        return p;
    }

    /** @return the index of the closest palette entry */
    static int nearest(int[] palette, int argb) {
        int best = 0;
        int bestD = Integer.MAX_VALUE;
        int r = (argb >> 16) & 0xff, g = (argb >> 8) & 0xff, b = argb & 0xff;
        for (int i = 0; i < palette.length; i++) {
            int c = palette[i];
            int dr = ((c >> 16) & 0xff) - r, dg = ((c >> 8) & 0xff) - g, db = (c & 0xff) - b;
            int d = dr * dr * 3 + dg * dg * 4 + db * db * 2;
            if (d < bestD) {
                bestD = d;
                best = i;
            }
        }
        return best;
    }

    // ---- codecs

    /** an image codec */
    abstract static class Codec {
        final int type;
        final String name;
        /** codecInfoDoes* of the decompressor */
        final int decompressFlags;
        /** codecInfoDoes* of the compressor, 0 if it cannot compress */
        final int compressFlags;
        /** codecInfoDepth* */
        final int formatFlags;

        Codec(String type, String name, int decompressFlags, int compressFlags, int formatFlags) {
            this.type = Bytes.fourCC(type);
            this.name = name;
            this.decompressFlags = decompressFlags | codecInfoDoesStretch | codecInfoDoesShrink | codecInfoDoesDither | codecInfoDoesMask;
            this.compressFlags = compressFlags;
            this.formatFlags = formatFlags;
        }

        boolean canCompress() {
            return compressFlags != 0;
        }

        boolean isTemporal() {
            return (compressFlags & codecInfoDoesTemporal) != 0 || (decompressFlags & codecInfoDoesTemporal) != 0;
        }

        /** @return the depth the codec stores for a requested depth */
        int depth(int requested) {
            return requested == 0 ? 24 : requested;
        }

        /** @return the image, the previous frame is given for temporal codecs */
        abstract BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) throws IOException;

        /** @return compressed data, a difference to the previous image if not null */
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) throws IOException {
            throw new IOException("no compressor: " + name);
        }

        /** @return the worst case size of compressed data */
        int maxSize(int width, int height, int depth) {
            return width * height * 4 + height * 16 + 1024;
        }
    }

    private static final Map<Integer, Codec> codecs = new LinkedHashMap<>();

    private static void add(Codec c) {
        codecs.put(c.type, c);
    }

    static {
        add(new Raw());
        add(new ImageIOCodec("jpeg", "Photo - JPEG", "jpeg", codecInfoDepth24 | codecInfoDepth40, false));
        add(new ImageIOCodec("png ", "PNG", "png", codecInfoDepth24 | codecInfoDepth32 | codecInfoDepth8 | codecInfoDepth40 | codecInfoDoesLossless, true));
        add(new ImageIOCodec("tiff", "TIFF", "tiff", codecInfoDepth24 | codecInfoDepth32 | codecInfoDoesLossless, true));
        add(new ImageIOCodec("WRLE", "BMP", "bmp", codecInfoDepth24 | codecInfoDoesLossless, false));
        add(new ImageIOCodec("gif ", "GIF", "gif", codecInfoDepth8, true));
        add(new Pict());
        add(new Animation());
        add(new Yuv("2vuy", "Component Video - CCIR-601 uyvy", false));
        add(new Yuv("yuv2", "Component Video", true));
        add(new Planar());
        add(new RoadPizza());
        add(new Graphics());
        add(new Cinepak());
    }

    /** @return the codec, null if there is no Java implementation */
    static Codec get(int type) {
        return codecs.get(type);
    }

    static List<Codec> all() {
        return new ArrayList<>(codecs.values());
    }

    /** @return the image of the compressed data */
    static BufferedImage decode(byte[] desc, byte[] data, BufferedImage previous) throws IOException {
        Desc d = new Desc(desc);
        Codec c = get(d.codec());
        if (c == null) {
            return null;
        }
        return c.decode(data, d, previous);
    }

    // ---- helpers

    static BufferedImage argb(int width, int height) {
        return new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_ARGB);
    }

    /** @return the previous frame or a new image for temporal decoding */
    static BufferedImage canvas(BufferedImage previous, int width, int height) {
        BufferedImage img = argb(width, height);
        if (previous != null) {
            Graphics2D g = img.createGraphics();
            g.setComposite(java.awt.AlphaComposite.Src);
            g.drawImage(previous, 0, 0, null);
            g.dispose();
        } else {
            int[] px = new int[img.getWidth() * img.getHeight()];
            java.util.Arrays.fill(px, 0xff00_0000);
            img.setRGB(0, 0, img.getWidth(), img.getHeight(), px, 0, img.getWidth());
        }
        return img;
    }

    static int[] pixels(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        return img.getRGB(0, 0, w, h, null, 0, w);
    }

    static BufferedImage image(int[] argb, int width, int height) {
        BufferedImage img = argb(width, height);
        img.setRGB(0, 0, width, height, argb, 0, width);
        return img;
    }

    static int rgb555(int v) {
        int r = (v >> 10) & 0x1f, g = (v >> 5) & 0x1f, b = v & 0x1f;
        return 0xff00_0000 | (r << 3 | r >> 2) << 16 | (g << 3 | g >> 2) << 8 | (b << 3 | b >> 2);
    }

    static int to555(int argb) {
        return ((argb >> 19) & 0x1f) << 10 | ((argb >> 11) & 0x1f) << 5 | ((argb >> 3) & 0x1f);
    }

    /** @return argb of a gray depth pixel value, 0 is white */
    static int gray(int v, int bits) {
        int max = (1 << bits) - 1;
        int g = 255 - v * 255 / max;
        return 0xff00_0000 | g << 16 | g << 8 | g;
    }

    static int luma(int argb) {
        return (((argb >> 16) & 0xff) * 299 + ((argb >> 8) & 0xff) * 587 + (argb & 0xff) * 114) / 1000;
    }

    /** @return the row bytes of uncompressed data of the description, rows are 2 byte aligned or as many as the data has */
    static int rowBytes(int width, int bits, int height, int dataLength) {
        int min = (width * bits + 7) / 8;
        if (height > 0 && dataLength / height > min) {
            return dataLength / height;
        }
        return min;
    }

    /** reads a packed pixel of 1, 2, 4 or 8 bits */
    static int packed(byte[] b, int rowOffset, int x, int bits) {
        int bit = x * bits;
        int o = rowOffset + bit / 8;
        if (o >= b.length) {
            return 0;
        }
        int shift = 8 - bits - (bit % 8);
        return ((b[o] & 0xff) >> shift) & ((1 << bits) - 1);
    }

    // ---- raw

    /** uncompressed pixels: 1, 2, 4, 8 bit indexed or gray, 16 bit 555, 24 bit RGB, 32 bit ARGB */
    static final class Raw extends Codec {
        Raw() {
            super("raw ", "None", codecInfoDoes1 | codecInfoDoes2 | codecInfoDoes4 | codecInfoDoes8 | codecInfoDoes16 | codecInfoDoes32,
                    codecInfoDoes1 | codecInfoDoes8 | codecInfoDoes16 | codecInfoDoes32,
                    codecInfoDepth1 | codecInfoDepth2 | codecInfoDepth4 | codecInfoDepth8 | codecInfoDepth16 | codecInfoDepth24 | codecInfoDepth32
                            | codecInfoDepth34 | codecInfoDepth36 | codecInfoDepth40 | codecInfoStoresClut | codecInfoDoesLossless);
        }

        @Override
        int depth(int requested) {
            return switch (requested) {
                case 1, 2, 4, 8, 16, 24, 32, 34, 36, 40 -> requested;
                case 33 -> 1;
                default -> 24;
            };
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) {
            return decodeRaw(data, desc.width(), desc.height(), desc.depth(), desc.palette());
        }

        @Override
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) {
            return encodeRaw(image, depth(depth));
        }

        @Override
        int maxSize(int width, int height, int depth) {
            return rowBytesOf(width, depth(depth)) * height;
        }
    }

    static int rowBytesOf(int width, int depth) {
        int bits = depth > 32 ? depth - 32 : depth;
        int rb = (width * bits + 7) / 8;
        return (rb + 1) & ~1;
    }

    static BufferedImage decodeRaw(byte[] data, int width, int height, int depth, int[] palette) {
        return decodeRaw(data, width, height, depth, -1, palette);
    }

    /** @param rowBytes the bytes per row, -1 to take them from the data length */
    static BufferedImage decodeRaw(byte[] data, int width, int height, int depth, int rowBytes, int[] palette) {
        int bits = depth > 32 ? depth - 32 : depth;
        if (rowBytes < 0) {
            rowBytes = rowBytes(width, bits, height, data.length);
        }
        int[] px = new int[width * height];
        for (int y = 0; y < height; y++) {
            int r = y * rowBytes;
            for (int x = 0; x < width; x++) {
                int v;
                switch (bits) {
                    case 32 -> {
                        int o = r + x * 4;
                        v = o + 4 <= data.length ? 0xff00_0000 | (data[o + 1] & 0xff) << 16 | (data[o + 2] & 0xff) << 8 | (data[o + 3] & 0xff) : 0xff00_0000;
                    }
                    case 24 -> {
                        int o = r + x * 3;
                        v = o + 3 <= data.length ? 0xff00_0000 | (data[o] & 0xff) << 16 | (data[o + 1] & 0xff) << 8 | (data[o + 2] & 0xff) : 0xff00_0000;
                    }
                    case 16 -> {
                        int o = r + x * 2;
                        v = o + 2 <= data.length ? rgb555(Bytes.getShort(data, o)) : 0xff00_0000;
                    }
                    default -> {
                        int i = packed(data, r, x, bits);
                        v = palette != null && i < palette.length ? palette[i] : gray(i, bits);
                    }
                }
                px[y * width + x] = v;
            }
        }
        return image(px, width, height);
    }

    static byte[] encodeRaw(BufferedImage image, int depth) {
        return encodeRaw(image, depth, -1, null);
    }

    /** @param rowBytes the bytes per row, -1 for the minimum */
    static byte[] encodeRaw(BufferedImage image, int depth, int rowBytes, int[] palette) {
        int w = image.getWidth(), h = image.getHeight();
        int bits = depth > 32 ? depth - 32 : depth;
        if (rowBytes < 0) {
            rowBytes = rowBytesOf(w, depth);
        }
        byte[] out = new byte[rowBytes * h];
        int[] px = pixels(image);
        if (palette == null) {
            palette = depth > 32 ? grayPalette(bits) : bits <= 8 ? defaultPalette(bits) : null;
        }
        for (int y = 0; y < h; y++) {
            int r = y * rowBytes;
            for (int x = 0; x < w; x++) {
                int c = px[y * w + x];
                switch (bits) {
                    case 32 -> {
                        int o = r + x * 4;
                        out[o] = (byte) (c >>> 24);
                        out[o + 1] = (byte) (c >> 16);
                        out[o + 2] = (byte) (c >> 8);
                        out[o + 3] = (byte) c;
                    }
                    case 24 -> {
                        int o = r + x * 3;
                        out[o] = (byte) (c >> 16);
                        out[o + 1] = (byte) (c >> 8);
                        out[o + 2] = (byte) c;
                    }
                    case 16 -> Bytes.putShort(out, r + x * 2, to555(c));
                    default -> {
                        int i = depth > 32 ? (255 - luma(c)) >> (8 - bits) : nearest(palette, c);
                        int bit = x * bits;
                        if (r + bit / 8 < out.length) {
                            out[r + bit / 8] |= (byte) (i << (8 - bits - bit % 8));
                        }
                    }
                }
            }
        }
        return out;
    }

    // ---- Java image I/O

    /** still image formats Java image I/O reads and writes */
    static final class ImageIOCodec extends Codec {
        final String format;
        final boolean alpha;

        ImageIOCodec(String type, String name, String format, int formatFlags, boolean alpha) {
            super(type, name, codecInfoDoes32, ImageIO.getImageWritersByFormatName(format).hasNext() ? codecInfoDoes32 : 0, formatFlags);
            this.format = format;
            this.alpha = alpha;
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) throws IOException {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
            if (img == null) {
                throw new IOException("cannot decode " + name);
            }
            return img;
        }

        @Override
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) throws IOException {
            return write(image, format, depth == 32 && alpha, quality);
        }

        @Override
        int depth(int requested) {
            return requested == 32 && alpha ? 32 : requested == 40 && (formatFlags & codecInfoDepth40) != 0 ? 40 : 24;
        }
    }

    /** @return the image converted to RGB, gray or ARGB */
    static BufferedImage convert(BufferedImage image, int type) {
        if (image.getType() == type) {
            return image;
        }
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), type);
        Graphics2D g = img.createGraphics();
        if (type != BufferedImage.TYPE_INT_ARGB) {
            g.setColor(java.awt.Color.white);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
        }
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return img;
    }

    /** @return the image written in the format, quality 0 - 0x400 for lossy formats */
    static byte[] write(BufferedImage image, String format, boolean alpha, int quality) throws IOException {
        BufferedImage img = convert(image, alpha ? BufferedImage.TYPE_INT_ARGB : format.equals("gif") ? BufferedImage.TYPE_BYTE_INDEXED : BufferedImage.TYPE_INT_RGB);
        Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName(format);
        if (!i.hasNext()) {
            throw new IOException("no writer: " + format);
        }
        ImageWriter w = i.next();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (ImageOutputStream out = ImageIO.createImageOutputStream(os)) {
            w.setOutput(out);
            ImageWriteParam p = w.getDefaultWriteParam();
            if (format.equals("jpeg")) {
                p.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                p.setCompressionQuality(Math.clamp(quality / (float) codecMaxQuality, 0.05f, 1f));
            } else if (format.equals("tiff") && p.canWriteCompressed()) {
                p.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                p.setCompressionType("Deflate");
            }
            w.write(null, new IIOImage(img, null, null), p);
        } finally {
            w.dispose();
        }
        return os.toByteArray();
    }

    // ---- QuickDraw picture

    static final class Pict extends Codec {
        Pict() {
            super("qdrw", "Graphics - PICT", codecInfoDoes32, codecInfoDoes32, codecInfoDepth32 | codecInfoDepth24);
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) throws IOException {
            BufferedImage img = PictCodec.decode(data);
            if (img == null) {
                throw new IOException("cannot decode picture");
            }
            return img;
        }

        @Override
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) {
            return PictCodec.encode(image, 0, 0);
        }
    }

    // ---- Animation

    /** the Animation codec, run length encoded lines with skips over unchanged pixels */
    static final class Animation extends Codec {
        Animation() {
            super("rle ", "Animation", codecInfoDoes32 | codecInfoDoesTemporal, codecInfoDoes32 | codecInfoDoesTemporal,
                    codecInfoDepth8 | codecInfoDepth16 | codecInfoDepth24 | codecInfoDepth32 | codecInfoDepth40 | codecInfoDoesLossless
                            | codecInfoSequenceSensitive);
        }

        @Override
        int depth(int requested) {
            return requested == 16 || requested == 32 ? requested : 24;
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) throws IOException {
            int w = desc.width(), h = desc.height();
            BufferedImage img = canvas(previous, w, h);
            int[] px = pixels(img);
            int bits = desc.bits();
            int[] palette = desc.palette();
            if (data.length >= 8) {
                int p = 4;
                int header = Bytes.getShort(data, p) & 0xffff;
                p += 2;
                int start = 0, lines = h;
                if ((header & 0x0008) != 0) {
                    start = Bytes.getShort(data, p) & 0xffff;
                    lines = Bytes.getShort(data, p + 4) & 0xffff;
                    p += 8;
                }
                // pixels per unit of skips and codes
                int ppu = bits >= 16 ? 1 : 32 / bits;
                for (int y = start; y < start + lines && y < h && p < data.length; y++) {
                    int x = ((data[p++] & 0xff) - 1) * ppu;
                    while (p < data.length) {
                        int code = data[p++];
                        if (code == -1) {
                            break;
                        }
                        if (code == 0) {
                            x += ((data[p++] & 0xff) - 1) * ppu;
                        } else if (code < 0) {
                            int n = -code;
                            int[] unit = new int[ppu];
                            p = unit(data, p, bits, palette, unit);
                            for (int i = 0; i < n; i++) {
                                for (int u : unit) {
                                    set(px, w, x++, y, u);
                                }
                            }
                        } else {
                            int[] unit = new int[ppu];
                            for (int i = 0; i < code; i++) {
                                p = unit(data, p, bits, palette, unit);
                                for (int u : unit) {
                                    set(px, w, x++, y, u);
                                }
                            }
                        }
                    }
                }
            }
            img.setRGB(0, 0, w, h, px, 0, w);
            return img;
        }

        private static void set(int[] px, int w, int x, int y, int v) {
            if (x >= 0 && x < w) {
                px[y * w + x] = v;
            }
        }

        private static int unit(byte[] b, int p, int bits, int[] palette, int[] out) {
            switch (bits) {
                case 32 -> {
                    out[0] = 0xff00_0000 | (b[p + 1] & 0xff) << 16 | (b[p + 2] & 0xff) << 8 | (b[p + 3] & 0xff);
                    return p + 4;
                }
                case 24 -> {
                    out[0] = 0xff00_0000 | (b[p] & 0xff) << 16 | (b[p + 1] & 0xff) << 8 | (b[p + 2] & 0xff);
                    return p + 3;
                }
                case 16 -> {
                    out[0] = rgb555(Bytes.getShort(b, p));
                    return p + 2;
                }
                default -> {
                    for (int i = 0; i < out.length; i++) {
                        int v = packed(b, p, i, bits);
                        out[i] = palette != null && v < palette.length ? palette[v] : gray(v, bits);
                    }
                    return p + 4;
                }
            }
        }

        @Override
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) {
            int w = image.getWidth(), h = image.getHeight();
            int d = depth(depth);
            int[] px = pixels(image);
            int[] prev = previous != null && previous.getWidth() == w && previous.getHeight() == h ? pixels(previous) : null;
            int first = 0, last = h - 1;
            if (prev != null) {
                while (first < h && same(px, prev, first * w, w)) {
                    first++;
                }
                while (last > first && same(px, prev, last * w, w)) {
                    last--;
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(0);
            os.write(0);
            os.write(0);
            os.write(0);
            if (prev == null) {
                os.write(0);
                os.write(0);
            } else {
                os.write(0);
                os.write(8);
                int lines = first >= h ? 0 : last - first + 1;
                writeShort(os, first >= h ? 0 : first);
                writeShort(os, 0);
                writeShort(os, lines);
                writeShort(os, 0);
            }
            for (int y = first; y <= last && y < h; y++) {
                line(os, px, prev, y * w, w, d);
            }
            byte[] b = os.toByteArray();
            Bytes.putInt(b, 0, b.length);
            return b;
        }

        private static boolean same(int[] a, int[] b, int o, int n) {
            for (int i = 0; i < n; i++) {
                if (((a[o + i] ^ b[o + i]) & 0x00ff_ffff) != 0) {
                    return false;
                }
            }
            return true;
        }

        private static void writeShort(ByteArrayOutputStream os, int v) {
            os.write(v >> 8);
            os.write(v);
        }

        private static void pixel(ByteArrayOutputStream os, int c, int depth) {
            switch (depth) {
                case 32 -> {
                    os.write(0xff);
                    os.write(c >> 16);
                    os.write(c >> 8);
                    os.write(c);
                }
                case 16 -> writeShort(os, to555(c));
                default -> {
                    os.write(c >> 16);
                    os.write(c >> 8);
                    os.write(c);
                }
            }
        }

        private static int key(int depth, int c) {
            return depth == 16 ? to555(c) : c & 0x00ff_ffff;
        }

        private static void line(ByteArrayOutputStream os, int[] px, int[] prev, int o, int w, int depth) {
            int x = 0;
            // leading skip
            if (prev != null) {
                while (x < w && x < 254 && key(depth, px[o + x]) == key(depth, prev[o + x])) {
                    x++;
                }
            }
            os.write(x + 1);
            while (x < w) {
                // skip unchanged pixels
                if (prev != null) {
                    int s = 0;
                    while (x + s < w && s < 254 && key(depth, px[o + x + s]) == key(depth, prev[o + x + s])) {
                        s++;
                    }
                    if (x + s >= w) {
                        break;
                    }
                    if (s >= 2) {
                        os.write(0);
                        os.write(s + 1);
                        x += s;
                        continue;
                    }
                }
                // repeat run
                int run = 1;
                while (x + run < w && run < 127 && key(depth, px[o + x + run]) == key(depth, px[o + x])) {
                    run++;
                }
                if (run >= 2) {
                    os.write(-run);
                    pixel(os, px[o + x], depth);
                    x += run;
                    continue;
                }
                // literals until a run or a skip starts
                int n = 0;
                while (x + n < w && n < 127) {
                    if (x + n + 1 < w && key(depth, px[o + x + n]) == key(depth, px[o + x + n + 1])) {
                        break;
                    }
                    if (prev != null && n > 0 && x + n + 1 < w && key(depth, px[o + x + n]) == key(depth, prev[o + x + n])
                            && key(depth, px[o + x + n + 1]) == key(depth, prev[o + x + n + 1])) {
                        break;
                    }
                    n++;
                }
                if (n == 0) {
                    n = 1;
                }
                os.write(n);
                for (int i = 0; i < n; i++) {
                    pixel(os, px[o + x + i], depth);
                }
                x += n;
            }
            os.write(0xff);
        }

        @Override
        int maxSize(int width, int height, int depth) {
            return 14 + height * (2 + width * 4 + (width / 127 + 1) * 2);
        }
    }

    // ---- component video

    /** 4:2:2 Y'CbCr, '2vuy' is Cb Y0 Cr Y1, 'yuv2' is Y0 Cb Y1 Cr with signed chroma */
    static final class Yuv extends Codec {
        final boolean yuv2;

        Yuv(String type, String name, boolean yuv2) {
            super(type, name, codecInfoDoes32, codecInfoDoes32, codecInfoDepth24);
            this.yuv2 = yuv2;
        }

        private static int clamp(int v) {
            return v < 0 ? 0 : Math.min(v, 255);
        }

        private static int rgb(int y, int cb, int cr) {
            int c = y - 16, d = cb - 128, e = cr - 128;
            int r = clamp((298 * c + 409 * e + 128) >> 8);
            int g = clamp((298 * c - 100 * d - 208 * e + 128) >> 8);
            int b = clamp((298 * c + 516 * d + 128) >> 8);
            return 0xff00_0000 | r << 16 | g << 8 | b;
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) {
            int w = desc.width(), h = desc.height();
            int rowBytes = rowBytes((w + 1) & ~1, 16, h, data.length);
            int[] px = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x += 2) {
                    int o = y * rowBytes + x * 2;
                    if (o + 4 > data.length) {
                        break;
                    }
                    int y0, y1, cb, cr;
                    if (yuv2) {
                        y0 = data[o] & 0xff;
                        cb = (data[o + 1] & 0xff) ^ 0x80;
                        y1 = data[o + 2] & 0xff;
                        cr = (data[o + 3] & 0xff) ^ 0x80;
                    } else {
                        cb = data[o] & 0xff;
                        y0 = data[o + 1] & 0xff;
                        cr = data[o + 2] & 0xff;
                        y1 = data[o + 3] & 0xff;
                    }
                    px[y * w + x] = rgb(y0, cb, cr);
                    if (x + 1 < w) {
                        px[y * w + x + 1] = rgb(y1, cb, cr);
                    }
                }
            }
            return image(px, w, h);
        }

        @Override
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) {
            int w = image.getWidth(), h = image.getHeight();
            int[] px = pixels(image);
            int rowBytes = ((w + 1) & ~1) * 2;
            byte[] out = new byte[rowBytes * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x += 2) {
                    int a = px[y * w + x], b = px[y * w + Math.min(x + 1, w - 1)];
                    int[] ya = ycc(a), yb = ycc(b);
                    int cb = (ya[1] + yb[1]) / 2, cr = (ya[2] + yb[2]) / 2;
                    int o = y * rowBytes + x * 2;
                    if (yuv2) {
                        out[o] = (byte) ya[0];
                        out[o + 1] = (byte) (cb ^ 0x80);
                        out[o + 2] = (byte) yb[0];
                        out[o + 3] = (byte) (cr ^ 0x80);
                    } else {
                        out[o] = (byte) cb;
                        out[o + 1] = (byte) ya[0];
                        out[o + 2] = (byte) cr;
                        out[o + 3] = (byte) yb[0];
                    }
                }
            }
            return out;
        }

        private static int[] ycc(int c) {
            int r = (c >> 16) & 0xff, g = (c >> 8) & 0xff, b = c & 0xff;
            int y = clamp(((66 * r + 129 * g + 25 * b + 128) >> 8) + 16);
            int cb = clamp(((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128);
            int cr = clamp(((112 * r - 94 * g - 18 * b + 128) >> 8) + 128);
            return new int[] {y, cb, cr};
        }

        @Override
        int maxSize(int width, int height, int depth) {
            return ((width + 1) & ~1) * 2 * height;
        }
    }

    // ---- planar RGB

    /** Planar RGB ('8BPS'), PackBits compressed planes */
    static final class Planar extends Codec {
        Planar() {
            super("8BPS", "Planar RGB", codecInfoDoes32, codecInfoDoes32, codecInfoDepth24 | codecInfoDepth32 | codecInfoDepth8 | codecInfoDoesLossless);
        }

        @Override
        int depth(int requested) {
            return requested == 32 ? 32 : 24;
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) throws IOException {
            int w = desc.width(), h = desc.height();
            int bits = desc.bits();
            int planes = bits == 32 ? 4 : bits == 24 ? 3 : 1;
            int[] palette = desc.palette();
            int[] px = new int[w * h];
            java.util.Arrays.fill(px, 0xff00_0000);
            int lengths = 0;
            int p = planes * h * 2;
            for (int c = 0; c < planes; c++) {
                for (int y = 0; y < h; y++) {
                    int len = Bytes.getShort(data, lengths) & 0xffff;
                    lengths += 2;
                    byte[] row = unpackBits(data, p, len, w);
                    p += len;
                    for (int x = 0; x < w; x++) {
                        int v = row[x] & 0xff;
                        int i = y * w + x;
                        if (planes == 1) {
                            px[i] = palette != null ? palette[v] : 0xff00_0000 | v * 0x010101;
                        } else if (c < 3) {
                            px[i] |= v << (16 - c * 8);
                        }
                    }
                }
            }
            return image(px, w, h);
        }

        @Override
        byte[] encode(BufferedImage image, BufferedImage previous, int depth, int quality) {
            int w = image.getWidth(), h = image.getHeight();
            int planes = depth(depth) == 32 ? 4 : 3;
            int[] px = pixels(image);
            ByteArrayOutputStream body = new ByteArrayOutputStream();
            byte[] lengths = new byte[planes * h * 2];
            byte[] row = new byte[w];
            for (int c = 0; c < planes; c++) {
                int shift = c < 3 ? 16 - c * 8 : 24;
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        row[x] = (byte) (px[y * w + x] >> shift);
                    }
                    byte[] packed = packBits(row);
                    Bytes.putShort(lengths, (c * h + y) * 2, packed.length);
                    body.writeBytes(packed);
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.writeBytes(lengths);
            os.writeBytes(body.toByteArray());
            return os.toByteArray();
        }
    }

    /** @return the unpacked bytes of PackBits data */
    static byte[] unpackBits(byte[] b, int p, int length, int count) {
        byte[] out = new byte[count];
        int o = 0;
        int end = Math.min(b.length, p + length);
        while (p < end && o < count) {
            int n = b[p++];
            if (n >= 0) {
                for (int i = 0; i <= n && p < end && o < count; i++) {
                    out[o++] = b[p++];
                }
            } else if (n != -128) {
                byte v = p < end ? b[p++] : 0;
                for (int i = 0; i < 1 - n && o < count; i++) {
                    out[o++] = v;
                }
            }
        }
        return out;
    }

    /** @return PackBits data of the bytes */
    static byte[] packBits(byte[] in) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int i = 0;
        while (i < in.length) {
            int run = 1;
            while (i + run < in.length && run < 128 && in[i + run] == in[i]) {
                run++;
            }
            if (run >= 2) {
                os.write(1 - run);
                os.write(in[i]);
                i += run;
                continue;
            }
            int n = 1;
            while (i + n < in.length && n < 128 && !(i + n + 1 < in.length && in[i + n] == in[i + n + 1])) {
                n++;
            }
            os.write(n - 1);
            os.write(in, i, n);
            i += n;
        }
        return os.toByteArray();
    }

    // ---- Road Pizza

    /** the Video codec ('rpza'), 4x4 blocks of 555 colors */
    static final class RoadPizza extends Codec {
        RoadPizza() {
            super("rpza", "Video", codecInfoDoes32 | codecInfoDoesTemporal, 0, codecInfoDepth16);
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) {
            int w = desc.width(), h = desc.height();
            BufferedImage img = canvas(previous, w, h);
            if (data.length < 4) {
                return img;
            }
            int[] px = pixels(img);
            int length = Math.clamp(Bytes.getInt(data, 0) & 0x00ff_ffff, 4, data.length);
            if (length < data.length && (data[0] & 0xff) != 0xe1) {
                length = data.length;
            }
            int p = 4;
            int blocksW = (w + 3) / 4;
            int total = blocksW * ((h + 3) / 4);
            int block = 0;
            int[] color4 = new int[4];
            loop:
            while (p < length && block < total) {
                int opcode = data[p++] & 0xff;
                int n = (opcode & 0x1f) + 1;
                int colorA = 0;
                if ((opcode & 0x80) == 0) {
                    if (p >= length) {
                        break;
                    }
                    colorA = (opcode << 8) | (data[p++] & 0xff);
                    opcode = 0;
                    if (p < length && (data[p] & 0x80) != 0) {
                        // a four color block with colorA
                        opcode = 0x20;
                        n = 1;
                    }
                }
                n = Math.min(n, total - block);
                switch (opcode & 0xe0) {
                    case 0x80 -> block += n;
                    case 0xa0 -> {
                        if (p + 2 > length) {
                            break loop;
                        }
                        int c = rgb555(Bytes.getShort(data, p));
                        p += 2;
                        for (int i = 0; i < n; i++) {
                            fill(px, w, h, block++, blocksW, c);
                        }
                    }
                    case 0xc0, 0x20 -> {
                        if ((opcode & 0xe0) == 0xc0) {
                            if (p + 2 > length) {
                                break loop;
                            }
                            colorA = Bytes.getShort(data, p) & 0xffff;
                            p += 2;
                        }
                        if (p + 2 > length) {
                            break loop;
                        }
                        int colorB = Bytes.getShort(data, p) & 0xffff;
                        p += 2;
                        int c1 = 0, c2 = 0;
                        for (int shift = 0; shift <= 10; shift += 5) {
                            int ta = (colorA >> shift) & 0x1f, tb = (colorB >> shift) & 0x1f;
                            c1 |= ((11 * ta + 21 * tb) >> 5) << shift;
                            c2 |= ((21 * ta + 11 * tb) >> 5) << shift;
                        }
                        color4[0] = rgb555(colorB);
                        color4[1] = rgb555(c1);
                        color4[2] = rgb555(c2);
                        color4[3] = rgb555(colorA);
                        for (int i = 0; i < n; i++) {
                            if (p + 4 > length) {
                                break loop;
                            }
                            for (int y = 0; y < 4; y++) {
                                int index = data[p++] & 0xff;
                                for (int x = 0; x < 4; x++) {
                                    put(px, w, h, block, blocksW, x, y, color4[(index >> (2 * (3 - x))) & 3]);
                                }
                            }
                            block++;
                        }
                    }
                    case 0x00 -> {
                        if (p + 30 > length) {
                            break loop;
                        }
                        for (int y = 0; y < 4; y++) {
                            for (int x = 0; x < 4; x++) {
                                if (y != 0 || x != 0) {
                                    colorA = Bytes.getShort(data, p) & 0xffff;
                                    p += 2;
                                }
                                put(px, w, h, block, blocksW, x, y, rgb555(colorA));
                            }
                        }
                        block++;
                    }
                    default -> {
                        break loop;
                    }
                }
            }
            img.setRGB(0, 0, w, h, px, 0, w);
            return img;
        }

        private static void put(int[] px, int w, int h, int block, int blocksW, int x, int y, int c) {
            int bx = (block % blocksW) * 4 + x, by = (block / blocksW) * 4 + y;
            if (bx < w && by < h) {
                px[by * w + bx] = c;
            }
        }

        private static void fill(int[] px, int w, int h, int block, int blocksW, int c) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    put(px, w, h, block, blocksW, x, y, c);
                }
            }
        }
    }

    // ---- Graphics

    /** the Graphics codec ('smc '), 4x4 blocks of 8 bit indexed colors with color caches */
    static final class Graphics extends Codec {
        Graphics() {
            super("smc ", "Graphics", codecInfoDoes32 | codecInfoDoesTemporal, 0, codecInfoDepth8);
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) {
            int w = desc.width(), h = desc.height();
            BufferedImage img = canvas(previous, w, h);
            int[] px = pixels(img);
            int[] palette = desc.palette();
            if (palette == null) {
                palette = defaultPalette(8);
            }
            int length = data.length >= 4 ? Math.min(data.length, Bytes.getInt(data, 0) & 0x00ff_ffff) : 0;
            int p = 4;
            int blocksW = (w + 3) / 4;
            int total = blocksW * ((h + 3) / 4);
            int block = 0;
            int[][] pairs = new int[256][2];
            int[][] quads = new int[256][4];
            int[][] octets = new int[256][8];
            int pi = 0, qi = 0, oi = 0;
            int[] cur = new int[16];
            try {
                while (p < length && block < total) {
                    int opcode = data[p++] & 0xff;
                    int type = opcode & 0xf0;
                    int n;
                    if (type < 0x80) {
                        n = (opcode & 0x10) != 0 ? (data[p++] & 0xff) + 1 : (opcode & 0x0f) + 1;
                    } else {
                        n = (opcode & 0x0f) + 1;
                    }
                    switch (type) {
                        case 0x00, 0x10 -> block += Math.min(n, total - block);
                        case 0x20, 0x30 -> {
                            for (int i = 0; i < n && block < total; i++) {
                                copy(px, w, h, blocksW, block - 1, block++);
                            }
                        }
                        case 0x40, 0x50 -> {
                            int a = block - 2, b = block - 1;
                            for (int i = 0; i < n * 2 && block < total; i++) {
                                copy(px, w, h, blocksW, (i & 1) == 0 ? a : b, block++);
                            }
                        }
                        case 0x60, 0x70 -> {
                            java.util.Arrays.fill(cur, palette[data[p++] & 0xff]);
                            for (int i = 0; i < n && block < total; i++) {
                                put(px, w, h, blocksW, block++, cur);
                            }
                        }
                        case 0x80, 0x90 -> {
                            int[] pair;
                            if (type == 0x80) {
                                pair = pairs[pi] = new int[] {data[p] & 0xff, data[p + 1] & 0xff};
                                p += 2;
                                pi = (pi + 1) & 0xff;
                            } else {
                                pair = pairs[data[p++] & 0xff];
                            }
                            for (int i = 0; i < n && block < total; i++) {
                                int flags = (data[p] & 0xff) << 8 | (data[p + 1] & 0xff);
                                p += 2;
                                for (int k = 0; k < 16; k++) {
                                    cur[k] = palette[pair[(flags >> (15 - k)) & 1]];
                                }
                                put(px, w, h, blocksW, block++, cur);
                            }
                        }
                        case 0xa0, 0xb0 -> {
                            int[] quad;
                            if (type == 0xa0) {
                                quad = quads[qi] = new int[] {data[p] & 0xff, data[p + 1] & 0xff, data[p + 2] & 0xff, data[p + 3] & 0xff};
                                p += 4;
                                qi = (qi + 1) & 0xff;
                            } else {
                                quad = quads[data[p++] & 0xff];
                            }
                            for (int i = 0; i < n && block < total; i++) {
                                for (int y = 0; y < 4; y++) {
                                    int bits = data[p++] & 0xff;
                                    for (int x = 0; x < 4; x++) {
                                        cur[y * 4 + x] = palette[quad[(bits >> (6 - x * 2)) & 3]];
                                    }
                                }
                                put(px, w, h, blocksW, block++, cur);
                            }
                        }
                        case 0xc0, 0xd0 -> {
                            int[] oct;
                            if (type == 0xc0) {
                                oct = octets[oi] = new int[8];
                                for (int k = 0; k < 8; k++) {
                                    oct[k] = data[p++] & 0xff;
                                }
                                oi = (oi + 1) & 0xff;
                            } else {
                                oct = octets[data[p++] & 0xff];
                            }
                            for (int i = 0; i < n && block < total; i++) {
                                // 01 23 45 67 89 AB -> a = 012456, b = 89A37B
                                int v1 = Bytes.getShort(data, p) & 0xffff;
                                int v2 = Bytes.getShort(data, p + 2) & 0xffff;
                                int v3 = Bytes.getShort(data, p + 4) & 0xffff;
                                p += 6;
                                int a = ((v1 & 0xfff0) << 8) | (v2 >> 4);
                                int b = ((v3 & 0xfff0) << 8) | ((v1 & 0x0f) << 8) | ((v2 & 0x0f) << 4) | (v3 & 0x0f);
                                for (int k = 0; k < 16; k++) {
                                    int flags = k < 8 ? a : b;
                                    cur[k] = palette[oct[(flags >> (21 - (k % 8) * 3)) & 7]];
                                }
                                put(px, w, h, blocksW, block++, cur);
                            }
                        }
                        case 0xe0 -> {
                            for (int i = 0; i < n && block < total; i++) {
                                for (int k = 0; k < 16; k++) {
                                    cur[k] = palette[data[p++] & 0xff];
                                }
                                put(px, w, h, blocksW, block++, cur);
                            }
                        }
                        default -> p = length;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // truncated data, keep what was decoded
            }
            img.setRGB(0, 0, w, h, px, 0, w);
            return img;
        }

        private static void put(int[] px, int w, int h, int blocksW, int block, int[] c) {
            int bx = (block % blocksW) * 4, by = (block / blocksW) * 4;
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    if (bx + x < w && by + y < h) {
                        px[(by + y) * w + bx + x] = c[y * 4 + x];
                    }
                }
            }
        }

        private static void copy(int[] px, int w, int h, int blocksW, int src, int dst) {
            if (src < 0) {
                return;
            }
            int sx = (src % blocksW) * 4, sy = (src / blocksW) * 4;
            int dx = (dst % blocksW) * 4, dy = (dst / blocksW) * 4;
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    if (sx + x < w && sy + y < h && dx + x < w && dy + y < h) {
                        px[(dy + y) * w + dx + x] = px[(sy + y) * w + sx + x];
                    }
                }
            }
        }
    }

    // ---- Cinepak

    /** Cinepak ('cvid'), vector quantized 4x4 blocks with strip codebooks kept in the description state */
    static final class Cinepak extends Codec {
        Cinepak() {
            super("cvid", "Cinepak", codecInfoDoes32 | codecInfoDoesTemporal, 0, codecInfoDepth24 | codecInfoDepth40);
        }

        /** 2x2 vectors as 4 argb pixels */
        private static final class Book {
            int[][] v1 = new int[256][4];
            int[][] v4 = new int[256][4];

            Book copy() {
                Book n = new Book();
                for (int i = 0; i < 256; i++) {
                    n.v1[i] = v1[i].clone();
                    n.v4[i] = v4[i].clone();
                }
                return n;
            }
        }

        private static int clamp(int v) {
            return v < 0 ? 0 : Math.min(v, 255);
        }

        private static void codebook(byte[] b, int p, int end, int[][] book, int id) {
            int n = (id & 0x04) != 0 ? 4 : 6;
            boolean selective = (id & 0x01) != 0;
            long flag = 0;
            long mask = 0;
            for (int i = 0; i < 256; i++) {
                if (selective && (mask >>>= 1) == 0) {
                    if (p + 4 > end) {
                        break;
                    }
                    flag = Bytes.getInt(b, p) & 0xffff_ffffL;
                    p += 4;
                    mask = 0x8000_0000L;
                }
                if (!selective || (flag & mask) != 0) {
                    if (p + n > end) {
                        break;
                    }
                    int u = n == 4 ? 0 : b[p + 4], v = n == 4 ? 0 : b[p + 5];
                    for (int k = 0; k < 4; k++) {
                        int y = b[p + k] & 0xff;
                        book[i][k] = 0xff00_0000 | clamp(y + v * 2) << 16 | clamp(y - (u / 2) - v) << 8 | clamp(y + u * 2);
                    }
                    p += n;
                }
            }
        }

        @Override
        BufferedImage decode(byte[] data, Desc desc, BufferedImage previous) {
            int w = desc.width(), h = desc.height();
            BufferedImage img = canvas(previous, w, h);
            if (data.length < 10) {
                return img;
            }
            int[] px = pixels(img);
            int frameFlags = data[0] & 0xff;
            int length = data.length;
            int strips = Bytes.getShort(data, 8) & 0xffff;
            Book[] bs = desc.state instanceof Book[] a ? a : new Book[0];
            if (bs.length < strips) {
                Book[] n = java.util.Arrays.copyOf(bs, strips);
                for (int i = bs.length; i < strips; i++) {
                    n[i] = new Book();
                }
                bs = n;
            }
            desc.state = bs;
            int p = 10;
            int y0 = 0;
            for (int s = 0; s < strips && p + 12 <= length; s++) {
                int stripSize = (Bytes.getInt(data, p) & 0x00ff_ffff) - 12;
                int y1 = Bytes.getShort(data, p + 4) & 0xffff;
                int x1 = Bytes.getShort(data, p + 6) & 0xffff;
                int y2 = Bytes.getShort(data, p + 8) & 0xffff;
                int x2 = Bytes.getShort(data, p + 10) & 0xffff;
                if (y1 == 0) {
                    y1 = y0;
                    y2 = y1 + y2;
                }
                p += 12;
                if (stripSize < 0) {
                    break;
                }
                int end = Math.min(length, p + stripSize);
                if (s > 0 && (frameFlags & 0x01) == 0) {
                    bs[s] = bs[s - 1].copy();
                }
                strip(data, p, end, bs[s], px, w, h, x1, y1, x2, y2);
                p = end;
                y0 = y2;
            }
            img.setRGB(0, 0, w, h, px, 0, w);
            return img;
        }

        private static void strip(byte[] b, int p, int end, Book book, int[] px, int w, int h, int x1, int y1, int x2, int y2) {
            while (p + 4 <= end) {
                int id = b[p] & 0xff;
                int size = (Bytes.getInt(b, p) & 0x00ff_ffff) - 4;
                p += 4;
                if (size < 0) {
                    return;
                }
                int cend = Math.min(end, p + size);
                switch (id) {
                    case 0x20, 0x21, 0x24, 0x25 -> codebook(b, p, cend, book.v4, id);
                    case 0x22, 0x23, 0x26, 0x27 -> codebook(b, p, cend, book.v1, id);
                    case 0x30, 0x31, 0x32 -> {
                        vectors(b, p, cend, id, book, px, w, h, x1, y1, x2, y2);
                        return;
                    }
                    default -> {
                    }
                }
                p = cend;
            }
        }

        private static void vectors(byte[] b, int p, int end, int id, Book book, int[] px, int w, int h, int x1, int y1, int x2, int y2) {
            long flag = 0;
            long mask = 0;
            for (int y = y1; y < y2; y += 4) {
                for (int x = x1; x < x2; x += 4) {
                    if ((id & 0x01) != 0 && (mask >>>= 1) == 0) {
                        if (p + 4 > end) {
                            return;
                        }
                        flag = Bytes.getInt(b, p) & 0xffff_ffffL;
                        p += 4;
                        mask = 0x8000_0000L;
                    }
                    if ((id & 0x01) != 0 && (flag & mask) == 0) {
                        continue;
                    }
                    if ((id & 0x02) == 0 && (mask >>>= 1) == 0) {
                        if (p + 4 > end) {
                            return;
                        }
                        flag = Bytes.getInt(b, p) & 0xffff_ffffL;
                        p += 4;
                        mask = 0x8000_0000L;
                    }
                    if ((id & 0x02) != 0 || (~flag & mask) != 0) {
                        if (p >= end) {
                            return;
                        }
                        int[] v = book.v1[b[p++] & 0xff];
                        for (int k = 0; k < 4; k++) {
                            int bx = x + (k & 1) * 2, by = y + (k >> 1) * 2;
                            put(px, w, h, bx, by, v[k]);
                            put(px, w, h, bx + 1, by, v[k]);
                            put(px, w, h, bx, by + 1, v[k]);
                            put(px, w, h, bx + 1, by + 1, v[k]);
                        }
                    } else {
                        if (p + 4 > end) {
                            return;
                        }
                        for (int k = 0; k < 4; k++) {
                            int[] v = book.v4[b[p + k] & 0xff];
                            int bx = x + (k & 1) * 2, by = y + (k >> 1) * 2;
                            put(px, w, h, bx, by, v[0]);
                            put(px, w, h, bx + 1, by, v[1]);
                            put(px, w, h, bx, by + 1, v[2]);
                            put(px, w, h, bx + 1, by + 1, v[3]);
                        }
                        p += 4;
                    }
                }
            }
        }

        private static void put(int[] px, int w, int h, int x, int y, int c) {
            if (x >= 0 && y >= 0 && x < w && y < h) {
                px[y * w + x] = c;
            }
        }
    }
}
