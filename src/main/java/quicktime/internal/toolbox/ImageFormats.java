/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;


/**
 * Image file formats of the graphics importers and exporters.
 */
final class ImageFormats {

    private ImageFormats() {
    }

    /** an image file format */
    record Format(String type, String extension, String mime, String name, String codec, boolean canExport) {

        int fileType() {
            return Bytes.fourCC(type);
        }
    }

    static final Map<Integer, Format> formats = new LinkedHashMap<>();

    private static void add(String type, String extension, String mime, String name, String codec, boolean canExport) {
        Format f = new Format(type, extension, mime, name, codec, canExport);
        formats.put(f.fileType(), f);
    }

    static {
        add("JPEG", "jpg", "image/jpeg", "JPEG", "jpeg", true);
        add("PNGf", "png", "image/png", "PNG", "png ", true);
        add("GIFf", "gif", "image/gif", "GIF", "gif ", true);
        add("BMPf", "bmp", "image/bmp", "BMP", "WRLE", true);
        add("TIFF", "tif", "image/tiff", "TIFF", "tiff", true);
        add("PICT", "pct", "image/x-pict", "PICT", "qdrw", true);
        add("qtif", "qtif", "image/x-quicktime", "QuickTime Image", "", true);
        add("8BPS", "psd", "image/vnd.adobe.photoshop", "Photoshop", "8BPS", true);
        add("PNTG", "pntg", "image/x-macpaint", "MacPaint", "PNTG", true);
    }

    static Format format(int type) {
        return formats.get(type);
    }

    static final int JPEG = Bytes.fourCC("JPEG");
    static final int PNGf = Bytes.fourCC("PNGf");
    static final int GIFf = Bytes.fourCC("GIFf");
    static final int BMPf = Bytes.fourCC("BMPf");
    static final int TIFF = Bytes.fourCC("TIFF");
    static final int PICT = Bytes.fourCC("PICT");
    static final int qtif = Bytes.fourCC("qtif");
    static final int _8BPS = Bytes.fourCC("8BPS");
    static final int PNTG = Bytes.fourCC("PNTG");

    /** @return the format of the data by its contents or the file name extension, 0 if unknown */
    static int detect(byte[] b, String name) {
        if (b.length >= 3 && (b[0] & 0xff) == 0xff && (b[1] & 0xff) == 0xd8) {
            return JPEG;
        }
        if (b.length >= 8 && (b[0] & 0xff) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G') {
            return PNGf;
        }
        if (b.length >= 4 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F') {
            return GIFf;
        }
        if (b.length >= 2 && b[0] == 'B' && b[1] == 'M') {
            return BMPf;
        }
        if (b.length >= 4 && ((b[0] == 'I' && b[1] == 'I' && b[2] == 42 && b[3] == 0) || (b[0] == 'M' && b[1] == 'M' && b[2] == 0 && b[3] == 42))) {
            return TIFF;
        }
        if (b.length >= 4 && b[0] == '8' && b[1] == 'B' && b[2] == 'P' && b[3] == 'S') {
            return _8BPS;
        }
        if (b.length >= 16 && (Bytes.getInt(b, 4) == Bytes.fourCC("idsc") || Bytes.getInt(b, 4) == Bytes.fourCC("idat") || Bytes.getInt(b, 4) == Bytes.fourCC("iicc"))) {
            return qtif;
        }
        String ext = name == null ? "" : name.substring(name.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        switch (ext) {
            case "pct", "pict", "pic" -> {
                return PICT;
            }
            case "pntg", "mac", "pnt" -> {
                return PNTG;
            }
            case "qtif", "qti" -> {
                return qtif;
            }
            default -> {
            }
        }
        if (isPict(b, 512) || isPict(b, 0)) {
            return PICT;
        }
        return 0;
    }

    private static boolean isPict(byte[] b, int p) {
        return p + 14 <= b.length && ((Bytes.getShort(b, p + 10) == 0x0011 && Bytes.getShort(b, p + 12) == 0x02ff) || (b[p + 10] == 0x11 && b[p + 11] == 0x01));
    }

    /** @return the picture without its file header */
    static byte[] pict(byte[] b) {
        return !isPict(b, 0) && isPict(b, 512) ? Arrays.copyOfRange(b, 512, b.length) : b;
    }

    // ---- qtif

    /** @return the image description and the data offset and size of a QuickTime image file */
    static Object[] qtifParts(byte[] b) {
        byte[] desc = null;
        int data = -1, size = 0;
        int p = 0;
        while (p + 8 <= b.length) {
            int s = Bytes.getInt(b, p);
            int t = Bytes.getInt(b, p + 4);
            int h = 8;
            if (s == 1 && p + 16 <= b.length) {
                s = (int) Bytes.getLong(b, p + 8);
                h = 16;
            } else if (s == 0) {
                s = b.length - p;
            }
            if (s < h || p + s > b.length) {
                break;
            }
            if (t == Bytes.fourCC("idsc")) {
                desc = Arrays.copyOfRange(b, p + h, p + s);
            } else if (t == Bytes.fourCC("idat")) {
                data = p + h;
                size = s - h;
            }
            p += s;
        }
        if (desc == null || data < 0) {
            return null;
        }
        return new Object[] {desc, data, size};
    }

    static byte[] qtif(byte[] desc, byte[] data) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.writeBytes(atomHeader(8 + desc.length, "idsc"));
        os.writeBytes(desc);
        os.writeBytes(atomHeader(8 + data.length, "idat"));
        os.writeBytes(data);
        return os.toByteArray();
    }

    private static byte[] atomHeader(int size, String type) {
        byte[] h = new byte[8];
        Bytes.putInt(h, 0, size);
        Bytes.putInt(h, 4, Bytes.fourCC(type));
        return h;
    }

    // ---- reading

    /** @return the number of images in the data */
    static int count(int type, byte[] b) {
        if (type == GIFf || type == TIFF) {
            try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(b))) {
                Iterator<ImageReader> i = ImageIO.getImageReaders(in);
                if (i.hasNext()) {
                    ImageReader r = i.next();
                    try {
                        r.setInput(in);
                        return Math.max(1, r.getNumImages(true));
                    } finally {
                        r.dispose();
                    }
                }
            } catch (IOException e) {
                return 1;
            }
        }
        return 1;
    }

    /** @return the image at the 1 based index */
    static BufferedImage decode(int type, byte[] b, int index) throws IOException {
        if (type == PICT) {
            BufferedImage img = PictCodec.decode(pict(b));
            if (img == null) {
                throw new IOException("cannot draw the picture");
            }
            return img;
        }
        if (type == qtif) {
            Object[] parts = qtifParts(b);
            if (parts == null) {
                throw new IOException("not a QuickTime image");
            }
            BufferedImage img = Codecs.decode((byte[]) parts[0], Arrays.copyOfRange(b, (Integer) parts[1], (Integer) parts[1] + (Integer) parts[2]), null);
            if (img == null) {
                throw new IOException("no decompressor: " + Bytes.fourCC(Bytes.getInt((byte[]) parts[0], 4)));
            }
            return img;
        }
        if (type == _8BPS) {
            return photoshop(b);
        }
        if (type == PNTG) {
            return macPaint(b);
        }
        try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(b))) {
            Iterator<ImageReader> i = ImageIO.getImageReaders(in);
            if (!i.hasNext()) {
                throw new IOException("no reader for " + Bytes.fourCC(type));
            }
            ImageReader r = i.next();
            try {
                r.setInput(in);
                return r.read(Math.max(0, index - 1));
            } finally {
                r.dispose();
            }
        }
    }

    /** a Photoshop file: the composite image of 8 or 16 bit gray, RGB or indexed */
    static BufferedImage photoshop(byte[] b) throws IOException {
        if (b.length < 26) {
            throw new IOException("short Photoshop file");
        }
        int channels = Bytes.getShort(b, 12) & 0xffff;
        int h = Bytes.getInt(b, 14), w = Bytes.getInt(b, 18);
        int depth = Bytes.getShort(b, 22);
        int mode = Bytes.getShort(b, 24);
        int p = 26;
        int colorLen = Bytes.getInt(b, p);
        byte[] palette = Arrays.copyOfRange(b, p + 4, p + 4 + colorLen);
        p += 4 + colorLen;
        p += 4 + Bytes.getInt(b, p);
        p += 4 + Bytes.getInt(b, p);
        int compression = Bytes.getShort(b, p);
        p += 2;
        int bytesPerSample = depth == 16 ? 2 : 1;
        int rowBytes = (w * depth + 7) / 8;
        byte[][] planes = new byte[channels][];
        if (compression == 1) {
            int counts = p;
            p += channels * h * 2;
            for (int c = 0; c < channels; c++) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                for (int y = 0; y < h; y++) {
                    int n = Bytes.getShort(b, counts + (c * h + y) * 2) & 0xffff;
                    os.writeBytes(Codecs.unpackBits(b, p, n, rowBytes));
                    p += n;
                }
                planes[c] = os.toByteArray();
            }
        } else if (compression == 0) {
            for (int c = 0; c < channels; c++) {
                planes[c] = Arrays.copyOfRange(b, p, p + rowBytes * h);
                p += rowBytes * h;
            }
        } else {
            throw new IOException("unsupported Photoshop compression: " + compression);
        }
        int[] px = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                int o = y * rowBytes + x * bytesPerSample;
                if (depth == 1) {
                    int v = (planes[0][y * rowBytes + x / 8] >> (7 - x % 8)) & 1;
                    px[i] = v != 0 ? 0xff00_0000 : 0xffff_ffff;
                    continue;
                }
                int c0 = planes[0][o] & 0xff;
                switch (mode) {
                    case 3 -> {
                        int g = channels > 1 ? planes[1][o] & 0xff : c0, bl = channels > 2 ? planes[2][o] & 0xff : c0;
                        int a = channels > 3 ? planes[3][o] & 0xff : 0xff;
                        px[i] = a << 24 | c0 << 16 | g << 8 | bl;
                    }
                    case 2 -> px[i] = 0xff00_0000 | (palette.length >= 768 ? (palette[c0] & 0xff) << 16 | (palette[256 + c0] & 0xff) << 8 | (palette[512 + c0] & 0xff) : c0 * 0x010101);
                    case 4 -> {
                        // CMYK, stored inverted
                        int cy = 255 - c0, m = 255 - (planes[1][o] & 0xff), ye = 255 - (planes[2][o] & 0xff), k = channels > 3 ? 255 - (planes[3][o] & 0xff) : 0;
                        int r = 255 - Math.min(255, cy + k), g = 255 - Math.min(255, m + k), bl = 255 - Math.min(255, ye + k);
                        px[i] = 0xff00_0000 | r << 16 | g << 8 | bl;
                    }
                    default -> {
                        int a = channels > 1 && mode == 1 ? planes[1][o] & 0xff : 0xff;
                        px[i] = a << 24 | c0 * 0x010101;
                    }
                }
            }
        }
        return Codecs.image(px, w, h);
    }

    /** a MacPaint file: 512 byte header, 720 PackBits rows of 72 bytes, set bits are black */
    static BufferedImage macPaint(byte[] b) {
        int w = 576, h = 720;
        int[] px = new int[w * h];
        int p = 512;
        for (int y = 0; y < h; y++) {
            byte[] row = new byte[72];
            int o = 0;
            while (o < 72 && p < b.length) {
                int n = b[p++];
                if (n >= 0) {
                    for (int i = 0; i <= n && o < 72 && p < b.length; i++) {
                        row[o++] = b[p++];
                    }
                } else if (n != -128) {
                    byte v = p < b.length ? b[p++] : 0;
                    for (int i = 0; i < 1 - n && o < 72; i++) {
                        row[o++] = v;
                    }
                }
            }
            for (int x = 0; x < w; x++) {
                px[y * w + x] = ((row[x / 8] >> (7 - x % 8)) & 1) != 0 ? 0xff00_0000 : 0xffff_ffff;
            }
        }
        return Codecs.image(px, w, h);
    }

    // ---- writing

    /** export settings */
    static final class Settings {
        int quality = Codecs.codecHighQuality;
        int depth;
        int compressionMethod;
        int interlaceStyle;
        int hRes = 72 << 16;
        int vRes = 72 << 16;
        int codec;
    }

    /** @return the image in the file format */
    static byte[] encode(int type, BufferedImage img, Settings s) throws IOException {
        if (type == PICT) {
            byte[] pict = PictCodec.encode(img, 0, 0);
            byte[] file = new byte[512 + pict.length];
            System.arraycopy(pict, 0, file, 512, pict.length);
            return file;
        }
        if (type == qtif) {
            Codecs.Codec c = s.codec != 0 && Codecs.get(s.codec) != null && Codecs.get(s.codec).canCompress() ? Codecs.get(s.codec) : Codecs.get(Bytes.fourCC("png "));
            byte[][] out = ImageCompressionManager.compress(img, null, c, s.depth != 0 ? s.depth : img.getColorModel().hasAlpha() ? 32 : 24, s.quality);
            return qtif(out[0], out[1]);
        }
        if (type == _8BPS) {
            return writePhotoshop(img, s.depth == 32 || (s.depth == 0 && img.getColorModel().hasAlpha()));
        }
        if (type == PNTG) {
            return writeMacPaint(img);
        }
        Format f = format(type);
        if (f == null) {
            throw new IOException("no exporter: " + Bytes.fourCC(type));
        }
        String format = switch (f.type()) {
            case "JPEG" -> "jpeg";
            case "PNGf" -> "png";
            case "GIFf" -> "gif";
            case "BMPf" -> "bmp";
            case "TIFF" -> "tiff";
            default -> throw new IOException("no exporter: " + f.type());
        };
        boolean alpha = (s.depth == 32 || s.depth == 0 && img.getColorModel().hasAlpha()) && (format.equals("png") || format.equals("tiff"));
        if (s.depth == 40 && !format.equals("gif")) {
            return writeGray(img, format);
        }
        return Codecs.write(img, format, alpha, s.quality);
    }

    private static byte[] writeGray(BufferedImage img, String format) throws IOException {
        BufferedImage g = Codecs.convert(img, BufferedImage.TYPE_BYTE_GRAY);
        Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName(format);
        if (!i.hasNext()) {
            throw new IOException("no writer: " + format);
        }
        ImageWriter w = i.next();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (ImageOutputStream out = ImageIO.createImageOutputStream(os)) {
            w.setOutput(out);
            w.write(new IIOImage(g, null, null));
        } finally {
            w.dispose();
        }
        return os.toByteArray();
    }

    static byte[] writePhotoshop(BufferedImage img, boolean alpha) {
        int w = img.getWidth(), h = img.getHeight();
        int channels = alpha ? 4 : 3;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] head = new byte[26];
        Bytes.putInt(head, 0, Bytes.fourCC("8BPS"));
        Bytes.putShort(head, 4, 1);
        Bytes.putShort(head, 12, channels);
        Bytes.putInt(head, 14, h);
        Bytes.putInt(head, 18, w);
        Bytes.putShort(head, 22, 8);
        Bytes.putShort(head, 24, 3);
        os.writeBytes(head);
        os.writeBytes(new byte[12]); // color mode data, image resources, layers
        int[] px = Codecs.pixels(img);
        byte[] counts = new byte[channels * h * 2];
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        byte[] row = new byte[w];
        for (int c = 0; c < channels; c++) {
            int shift = c < 3 ? 16 - c * 8 : 24;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    row[x] = (byte) (px[y * w + x] >> shift);
                }
                byte[] packed = Codecs.packBits(row);
                Bytes.putShort(counts, (c * h + y) * 2, packed.length);
                body.writeBytes(packed);
            }
        }
        os.write(0);
        os.write(1);
        os.writeBytes(counts);
        os.writeBytes(body.toByteArray());
        return os.toByteArray();
    }

    static byte[] writeMacPaint(BufferedImage img) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.writeBytes(new byte[512]);
        byte[] row = new byte[72];
        for (int y = 0; y < 720; y++) {
            Arrays.fill(row, (byte) 0);
            for (int x = 0; x < 576; x++) {
                if (x < img.getWidth() && y < img.getHeight()) {
                    int c = img.getRGB(x, y);
                    if ((c >>> 24) > 0x7f && Codecs.luma(c) < 128) {
                        row[x / 8] |= (byte) (0x80 >> (x % 8));
                    }
                }
            }
            os.writeBytes(Codecs.packBits(row));
        }
        return os.toByteArray();
    }

    // ---- descriptions

    /** @return an image description of the file data */
    static byte[] description(int type, byte[] b, BufferedImage img) {
        if (type == qtif) {
            Object[] parts = qtifParts(b);
            if (parts != null) {
                return (byte[]) parts[0];
            }
        }
        Format f = format(type);
        int codec = f != null && !f.codec().isEmpty() ? Bytes.fourCC(f.codec()) : type;
        int depth = depthOf(img);
        return Codecs.description(codec, img.getWidth(), img.getHeight(), depth, f != null ? f.name() : Bytes.fourCC(type), b.length, Codecs.codecHighQuality);
    }

    /** @return the QuickTime depth of an image: 32 with alpha, 40 for gray, 8 for indexed, 1 for bitmaps, else 24 */
    static int depthOf(BufferedImage img) {
        if (img.getColorModel().hasAlpha()) {
            return 32;
        }
        return switch (img.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY, BufferedImage.TYPE_USHORT_GRAY -> 40;
            case BufferedImage.TYPE_BYTE_BINARY -> img.getColorModel().getPixelSize() == 1 ? 1 : img.getColorModel().getPixelSize();
            case BufferedImage.TYPE_BYTE_INDEXED -> 8;
            default -> 24;
        };
    }
}
