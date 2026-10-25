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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * QuickDraw pictures (PICT, version 2).
 * <p>
 * Pictures are written as one uncompressed 32 bit DirectBitsRect. Reading understands the bitmap opcodes
 * (DirectBitsRect/Rgn, PackBitsRect/Rgn, BitsRect/Rgn) and QuickTime compressed pictures with JPEG, PNG
 * or other codecs ImageIO can read. Vector opcodes are skipped.
 */
final class PictCodec {

    private static final Logger logger = Logger.getLogger(PictCodec.class.getName());

    private PictCodec() {
    }

    /** @return the bytes of a PicHandle (without the 512 byte file header) */
    static byte[] encode(BufferedImage image, int left, int top) {
        int w = image.getWidth();
        int h = image.getHeight();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Out o = new Out(os);
        o.s(0); // size, patched
        o.rect(top, left, top + h, left + w);
        o.s(0x0011).s(0x02ff);
        o.s(0x0c00).s(-2).s(0).i(72 << 16).i(72 << 16).rect(top, left, top + h, left + w).i(0);
        o.s(0x001e);
        o.s(0x0001).s(10).rect(top, left, top + h, left + w);
        o.s(0x009a);
        o.i(0x0000_00ff);
        int rowBytes = w * 4;
        o.s(rowBytes | 0x8000).rect(top, left, top + h, left + w);
        o.s(0).s(1).i(0).i(72 << 16).i(72 << 16).s(16).s(32).s(3).s(8).i(0).i(0).i(0);
        o.rect(top, left, top + h, left + w).rect(top, left, top + h, left + w).s(0);
        int[] row = new int[w];
        for (int y = 0; y < h; y++) {
            image.getRGB(0, y, w, 1, row, 0, w);
            for (int x = 0; x < w; x++) {
                o.i(row[x] | 0xff00_0000);
            }
        }
        if ((os.size() & 1) != 0) {
            os.write(0);
        }
        o.s(0x00ff);
        byte[] b = os.toByteArray();
        Bytes.putShort(b, 0, b.length & 0xffff);
        return b;
    }

    private static final class Out {
        final ByteArrayOutputStream os;

        Out(ByteArrayOutputStream os) {
            this.os = os;
        }

        Out s(int v) {
            os.write(v >> 8);
            os.write(v);
            return this;
        }

        Out i(int v) {
            s(v >> 16);
            return s(v);
        }

        Out rect(int top, int left, int bottom, int right) {
            return s(top).s(left).s(bottom).s(right);
        }
    }

    /** @return the picture frame, left top right bottom */
    static int[] frame(byte[] b) {
        int p = b.length >= 512 + 10 && isPictAt(b, 512) && !isPictAt(b, 0) ? 512 : 0;
        return new int[] {Bytes.getShort(b, p + 4), Bytes.getShort(b, p + 2), Bytes.getShort(b, p + 8), Bytes.getShort(b, p + 6)};
    }

    private static boolean isPictAt(byte[] b, int p) {
        return p + 14 <= b.length && ((Bytes.getShort(b, p + 10) == 0x0011 && Bytes.getShort(b, p + 12) == 0x02ff) || (b[p + 10] == 0x11 && b[p + 11] == 0x01));
    }

    /** @return the image of the picture, null if nothing can be decoded */
    static synchronized BufferedImage decode(byte[] b) {
        try {
            int base = b.length >= 512 + 14 && !isPictAt(b, 0) && isPictAt(b, 512) ? 512 : 0;
            int top = Bytes.getShort(b, base + 2), left = Bytes.getShort(b, base + 4), bottom = Bytes.getShort(b, base + 6), right = Bytes.getShort(b, base + 8);
            int w = right - left, h = bottom - top;
            if (w <= 0 || h <= 0) {
                return null;
            }
            BufferedImage canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = canvas.createGraphics();
            g.setColor(java.awt.Color.white);
            g.fillRect(0, 0, w, h);
            boolean drawn = false;
            int p = base + 10;
            if (b[p] == 0x11 && b[p + 1] == 0x01) {
                // version 1 pictures have byte opcodes, only skip to the bitmaps is not worth it
                g.dispose();
                return null;
            }
            while (p + 2 <= b.length) {
                int op = Bytes.getShort(b, p) & 0xffff;
                p += 2;
                switch (op) {
                    case 0x0000, 0x001e -> {
                        // nop, def hilite
                    }
                    case 0x0011 -> p += 2;
                    case 0x0c00 -> p += 24;
                    case 0x0001 -> p += Bytes.getShort(b, p);
                    case 0x0098, 0x0099, 0x0090, 0x0091, 0x009a, 0x009b -> {
                        int[] r = new int[1];
                        // the destination rect is recorded by bits()
                        BufferedImage img = bits(b, p, op, r);
                        if (img != null) {
                            g.drawImage(img, lastDst[1] - left, lastDst[0] - top, lastDst[3] - lastDst[1], lastDst[2] - lastDst[0], null);
                            drawn = true;
                        }
                        p = r[0];
                    }
                    case 0x8200, 0x8201 -> {
                        int size = Bytes.getInt(b, p);
                        BufferedImage img = compressed(b, p + 4, size);
                        if (img != null) {
                            g.drawImage(img, lastDst[1] - left, lastDst[0] - top, lastDst[3] - lastDst[1], lastDst[2] - lastDst[0], null);
                            drawn = true;
                        }
                        p += 4 + size;
                    }
                    case 0x00ff -> p = b.length;
                    case 0x00a0 -> p += 2;
                    case 0x00a1 -> p += 2 + Bytes.getShort(b, p + 2);
                    default -> {
                        int skip = skip(b, p, op);
                        if (skip < 0) {
                            p = b.length;
                        } else {
                            p += skip;
                        }
                    }
                }
                if ((p & 1) != 0) {
                    p++;
                }
            }
            g.dispose();
            return drawn ? canvas : null;
        } catch (RuntimeException e) {
            logger.log(Level.FINE, "broken picture", e);
            return null;
        }
    }

    /** the destination rect of the last bitmap, top left bottom right */
    private static final int[] lastDst = new int[4];

    /** @return the size of the data of an opcode that is not drawn, -1 if unknown */
    private static int skip(byte[] b, int p, int op) {
        if (op >= 0x0100 && op <= 0x7fff) {
            return (op >> 8) * 2;
        }
        if (op >= 0x8000 && op <= 0x80ff) {
            return 0;
        }
        if (op >= 0x8100) {
            return 4 + Bytes.getInt(b, p);
        }
        return switch (op) {
            case 0x0003, 0x0004, 0x0005, 0x0008, 0x000d, 0x0015, 0x0016, 0x0023 -> 2;
            case 0x0006, 0x0007, 0x000b, 0x000c, 0x000e, 0x000f, 0x0021 -> 4;
            case 0x001a, 0x001b, 0x001d, 0x001f -> 6;
            case 0x0009, 0x000a, 0x0002, 0x0010, 0x0020, 0x0030, 0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037 -> 8;
            case 0x0022 -> 6;
            case 0x0038, 0x0039, 0x003a, 0x003b, 0x003c, 0x003d, 0x003e, 0x003f, 0x0048, 0x0049, 0x004a, 0x004b, 0x004c, 0x004d, 0x004e,
                 0x004f, 0x0058, 0x0059, 0x005a, 0x005b, 0x005c, 0x005d, 0x005e, 0x005f, 0x0068, 0x0069, 0x006a, 0x006b, 0x006c, 0x006d,
                 0x006e, 0x006f, 0x0078, 0x0079, 0x007a, 0x007b, 0x007c, 0x007d, 0x007e, 0x007f, 0x0088, 0x0089, 0x008a, 0x008b, 0x008c,
                 0x008d, 0x008e, 0x008f -> 0;
            case 0x0040, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056,
                 0x0057 -> 8;
            case 0x0060, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067 -> 12;
            case 0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077, 0x0080, 0x0081, 0x0082, 0x0083, 0x0084, 0x0085, 0x0086,
                 0x0087 -> Bytes.getShort(b, p) & 0xffff;
            case 0x0028 -> 5 + (b[p + 4] & 0xff);
            case 0x0029, 0x002a -> 2 + (b[p + 1] & 0xff);
            case 0x002b -> 3 + (b[p + 2] & 0xff);
            case 0x002c, 0x002d, 0x002e, 0x002f -> 2 + (Bytes.getShort(b, p) & 0xffff);
            case 0x0012, 0x0013, 0x0014 -> -1;
            default -> -1;
        };
    }

    /** decodes a bitmap opcode, the end offset goes to end[0] */
    private static BufferedImage bits(byte[] b, int p, int op, int[] end) {
        boolean direct = op == 0x009a || op == 0x009b;
        boolean region = op == 0x0091 || op == 0x0099 || op == 0x009b;
        if (direct) {
            p += 4; // baseAddr
        }
        int rowBytes = Bytes.getShort(b, p) & 0x3fff;
        boolean pixMap = (Bytes.getShort(b, p) & 0x8000) != 0;
        int top = Bytes.getShort(b, p + 2), left = Bytes.getShort(b, p + 4), bottom = Bytes.getShort(b, p + 6), right = Bytes.getShort(b, p + 8);
        p += 10;
        int packType = 0, pixelSize = 1, cmpCount = 1;
        int[] palette = null;
        if (pixMap) {
            packType = Bytes.getShort(b, p + 2);
            pixelSize = Bytes.getShort(b, p + 18);
            cmpCount = Bytes.getShort(b, p + 20);
            p += 36;
            if (!direct) {
                // color table: seed(4) flags(2) size(2) entries(8 each)
                int n = (Bytes.getShort(b, p + 6) & 0xffff) + 1;
                palette = new int[256];
                for (int i = 0; i < n; i++) {
                    int e = p + 8 + i * 8;
                    int index = Bytes.getShort(b, e) & 0xff;
                    int idx = (Bytes.getShort(b, p + 4) & 0x8000) != 0 ? i : index;
                    palette[idx & 0xff] = 0xff00_0000 | ((b[e + 2] & 0xff) << 16) | ((b[e + 4] & 0xff) << 8) | (b[e + 6] & 0xff);
                }
                p += 8 + n * 8;
            }
        } else {
            palette = new int[] {0xffff_ffff, 0xff00_0000};
        }
        lastDst[0] = Bytes.getShort(b, p + 8);
        lastDst[1] = Bytes.getShort(b, p + 10);
        lastDst[2] = Bytes.getShort(b, p + 12);
        lastDst[3] = Bytes.getShort(b, p + 14);
        p += 16 + 2; // src rect, dst rect, mode
        if (region) {
            p += Bytes.getShort(b, p);
        }
        int w = right - left, h = bottom - top;
        BufferedImage img = new BufferedImage(Math.max(1, w), Math.max(1, h), BufferedImage.TYPE_INT_ARGB);
        int[] argb = new int[w];
        for (int y = 0; y < h; y++) {
            byte[] row;
            if (rowBytes < 8 || packType == 1 || (direct && packType == 2)) {
                int len = direct && packType == 2 ? w * 3 : rowBytes;
                row = java.util.Arrays.copyOfRange(b, p, p + len);
                p += len;
            } else {
                int count;
                if (rowBytes > 250) {
                    count = Bytes.getShort(b, p) & 0xffff;
                    p += 2;
                } else {
                    count = b[p] & 0xff;
                    p += 1;
                }
                row = unpack(b, p, count, rowBytes, direct && pixelSize == 16 && packType == 3);
                p += count;
            }
            for (int x = 0; x < w; x++) {
                if (direct) {
                    if (pixelSize == 32 && (packType == 4 || (packType == 0 && rowBytes >= 8))) {
                        // component planes, alpha first if 4 components
                        int o = cmpCount == 4 ? w : 0;
                        argb[x] = 0xff00_0000 | ((row[o + x] & 0xff) << 16) | ((row[o + w + x] & 0xff) << 8) | (row[o + 2 * w + x] & 0xff);
                    } else if (pixelSize == 32 && packType == 2) {
                        argb[x] = 0xff00_0000 | ((row[x * 3] & 0xff) << 16) | ((row[x * 3 + 1] & 0xff) << 8) | (row[x * 3 + 2] & 0xff);
                    } else if (pixelSize == 32) {
                        argb[x] = 0xff00_0000 | ((row[x * 4 + 1] & 0xff) << 16) | ((row[x * 4 + 2] & 0xff) << 8) | (row[x * 4 + 3] & 0xff);
                    } else {
                        int v = ((row[x * 2] & 0xff) << 8) | (row[x * 2 + 1] & 0xff);
                        argb[x] = 0xff00_0000 | (((v >> 10) & 0x1f) * 255 / 31 << 16) | (((v >> 5) & 0x1f) * 255 / 31 << 8) | ((v & 0x1f) * 255 / 31);
                    }
                } else {
                    int bit = x * pixelSize;
                    int v = (row[bit / 8] & 0xff) >> (8 - pixelSize - bit % 8) & ((1 << pixelSize) - 1);
                    argb[x] = palette[v & 0xff] == 0 && pixelSize == 1 ? (v == 0 ? 0xffff_ffff : 0xff00_0000) : palette[v & 0xff];
                }
            }
            img.setRGB(0, y, w, 1, argb, 0, w);
        }
        end[0] = p;
        return img;
    }

    /** PackBits, 16 bit words for 16 bit pixels */
    private static byte[] unpack(byte[] b, int p, int count, int rowBytes, boolean words) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(rowBytes);
        int end = p + count;
        int unit = words ? 2 : 1;
        while (p < end) {
            int n = b[p++];
            if (n >= 0) {
                int len = (n + 1) * unit;
                os.write(b, p, Math.min(len, end - p));
                p += len;
            } else if (n != -128) {
                for (int i = 0; i < -n + 1; i++) {
                    os.write(b, p, unit);
                }
                p += unit;
            }
        }
        byte[] r = os.toByteArray();
        return r.length >= rowBytes ? r : java.util.Arrays.copyOf(r, rowBytes);
    }

    /** QuickTime compressed picture: version(2) matrix(36) matteSize(4) matteRect(8) mode(2) srcRect(8) accuracy(4) maskSize(4), matte, mask, image description, data */
    private static BufferedImage compressed(byte[] b, int p, int size) {
        int matteSize = Bytes.getInt(b, p + 38);
        int maskSize = Bytes.getInt(b, p + 64);
        int src = p + 50;
        lastDst[0] = Bytes.getShort(b, src);
        lastDst[1] = Bytes.getShort(b, src + 2);
        lastDst[2] = Bytes.getShort(b, src + 4);
        lastDst[3] = Bytes.getShort(b, src + 6);
        int d = p + 68 + matteSize + maskSize;
        int descSize = Bytes.getInt(b, d);
        int dataSize = Bytes.getInt(b, d + 44);
        int data = d + descSize;
        try {
            return ImageIO.read(new ByteArrayInputStream(b, data, Math.min(dataSize > 0 ? dataSize : size, b.length - data)));
        } catch (IOException e) {
            return null;
        }
    }
}
