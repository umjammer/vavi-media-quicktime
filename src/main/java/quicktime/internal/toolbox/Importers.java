/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;


/**
 * Movie import components in Java. The media data is referenced in place, nothing is copied.
 * Files these cannot read go to the {@link MediaEngine}.
 */
final class Importers {

    private Importers() {
    }

    static final int MooV = Bytes.fourCC("MooV");
    static final int AIFF = Bytes.fourCC("AIFF");
    static final int AIFC = Bytes.fourCC("AIFC");
    static final int WAVE = Bytes.fourCC("WAVE");
    static final int JPEG = Bytes.fourCC("JPEG");
    static final int PNGf = Bytes.fourCC("PNGf");
    static final int GIFf = Bytes.fourCC("GIFf");
    static final int BMPf = Bytes.fourCC("BMPf");

    /** @return the import type of the file by its contents, 0 if unknown */
    static int detect(String path) {
        byte[] h = new byte[16];
        int n;
        try (RandomAccessFile f = new RandomAccessFile(path, "r")) {
            n = f.read(h);
        } catch (IOException e) {
            return 0;
        }
        if (n >= 12 && Bytes.getInt(h, 0) == Bytes.fourCC("FORM")) {
            int form = Bytes.getInt(h, 8);
            return form == AIFF || form == AIFC ? form : 0;
        }
        if (n >= 12 && Bytes.getInt(h, 0) == Bytes.fourCC("RIFF") && Bytes.getInt(h, 8) == WAVE) {
            return WAVE;
        }
        if (n >= 3 && (h[0] & 0xff) == 0xff && (h[1] & 0xff) == 0xd8) {
            return JPEG;
        }
        if (n >= 8 && (h[0] & 0xff) == 0x89 && h[1] == 'P' && h[2] == 'N' && h[3] == 'G') {
            return PNGf;
        }
        if (n >= 4 && h[0] == 'G' && h[1] == 'I' && h[2] == 'F') {
            return GIFf;
        }
        if (n >= 2 && h[0] == 'B' && h[1] == 'M') {
            return BMPf;
        }
        if (MovieAtoms.isMovieFile(path)) {
            return MooV;
        }
        return 0;
    }

    /** @return the import type of a file name extension, 0 if unknown */
    static int byExtension(String path) {
        String p = path.toLowerCase(Locale.ROOT);
        String ext = p.substring(p.lastIndexOf('.') + 1);
        return switch (ext) {
            case "mov", "qt", "mp4", "m4v", "m4a", "3gp", "3g2", "mqv" -> MooV;
            case "aif", "aiff" -> AIFF;
            case "aifc" -> AIFC;
            case "wav", "wave" -> WAVE;
            case "jpg", "jpeg" -> JPEG;
            case "png" -> PNGf;
            case "gif" -> GIFf;
            case "bmp" -> BMPf;
            default -> 0;
        };
    }

    /** @return the movie of the file, null if no Java importer can read it */
    static MovieRec importFile(String path) throws IOException {
        int type = detect(path);
        if (type == MooV) {
            return MovieAtoms.readFile(path);
        }
        if (type == AIFF || type == AIFC) {
            return aiff(path);
        }
        if (type == WAVE) {
            return wave(path);
        }
        if (type == JPEG || type == PNGf || type == GIFf || type == BMPf) {
            return image(path, type);
        }
        return null;
    }

    // ---- sound

    /** @return a QuickTime sound description */
    static byte[] soundDescription(int format, int channels, int bits, double sampleRate, boolean bigEndian, boolean isFloat, boolean signed) {
        boolean v0 = (format == Bytes.fourCC("twos") && (bits == 8 || bits == 16))
                || (format == Bytes.fourCC("sowt") && bits == 16)
                || (format == Bytes.fourCC("raw ") && bits == 8)
                || format == Bytes.fourCC("ulaw") || format == Bytes.fourCC("alaw");
        if (v0 && sampleRate < 65536) {
            byte[] d = new byte[36];
            Bytes.putInt(d, 0, 36);
            Bytes.putInt(d, 4, format);
            Bytes.putShort(d, 14, 1);
            Bytes.putShort(d, 24, channels);
            Bytes.putShort(d, 26, bits);
            Bytes.putInt(d, 32, (int) Math.round(sampleRate * 65536) & 0xffff_ffff);
            return d;
        }
        // version 2 'lpcm'
        byte[] d = new byte[72];
        Bytes.putInt(d, 0, 72);
        Bytes.putInt(d, 4, Bytes.fourCC("lpcm"));
        Bytes.putShort(d, 14, 1);
        Bytes.putShort(d, 16, 2);
        Bytes.putShort(d, 24, 3);
        Bytes.putShort(d, 26, 16);
        Bytes.putShort(d, 28, -2);
        Bytes.putInt(d, 32, 0x10000);
        Bytes.putInt(d, 36, 72);
        Bytes.putLong(d, 40, Double.doubleToLongBits(sampleRate));
        Bytes.putInt(d, 48, channels);
        Bytes.putInt(d, 52, 0x7f00_0000);
        Bytes.putInt(d, 56, bits);
        int flags = (isFloat ? 1 : 0) | (bigEndian ? 2 : 0) | (signed && !isFloat ? 4 : 0) | 8;
        Bytes.putInt(d, 60, flags);
        Bytes.putInt(d, 64, channels * ((bits + 7) / 8));
        Bytes.putInt(d, 68, 1);
        return d;
    }

    /** @return a movie of one sound track whose frames are at the offset of the file */
    static MovieRec soundMovie(String path, byte[] desc, double sampleRate, long frames, long offset, long bytes) {
        MovieRec m = new MovieRec();
        int scale = (int) Math.max(1, Math.min(Integer.MAX_VALUE, Math.round(sampleRate)));
        m.setTimeScale(scale);
        TrackRec t = m.newTrack();
        t.volume = 0x100;
        MediaRec md = new MediaRec(t, Bytes.fourCC("soun"), scale);
        md.dataRefs.add(DataRefRec.forFile(path));
        md.handlerName = "Apple Sound Media Handler";
        int di = md.descriptionIndex(desc);
        if (frames > 0) {
            // runs are limited to int counts
            long done = 0;
            while (done < frames) {
                int n = (int) Math.min(Integer.MAX_VALUE / 2, frames - done);
                long b = bytes * (done + n) / frames - bytes * done / frames;
                md.addSamples(1, offset + bytes * done / frames, 1, b, 1, n, di, true, 0);
                done += n;
            }
        }
        t.media = md;
        if (frames > 0) {
            t.edits.add(new TrackRec.Edit(frames, 0, 0x10000));
        }
        m.updateTimeBase();
        m.changed = false;
        return m;
    }

    /** 80 bit IEEE extended to double */
    static double extended(byte[] b, int o) {
        int exp = ((b[o] & 0x7f) << 8) | (b[o + 1] & 0xff);
        long mant = Bytes.getLong(b, o + 2);
        if (exp == 0 && mant == 0) {
            return 0;
        }
        double v = (mant >>> 11) * Math.pow(2, exp - 16383 - 52);
        return (b[o] & 0x80) != 0 ? -v : v;
    }

    static MovieRec aiff(String path) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(path, "r")) {
            byte[] h = new byte[12];
            f.readFully(h);
            boolean aifc = Bytes.getInt(h, 8) == AIFC;
            int channels = 0, bits = 0;
            long frames = 0;
            double rate = 0;
            int compression = Bytes.fourCC("NONE");
            long dataOffset = -1, dataSize = 0;
            long p = 12;
            long end = Math.min(f.length(), 8 + (Bytes.getInt(h, 4) & 0xffff_ffffL));
            byte[] ch = new byte[8];
            while (p + 8 <= end) {
                f.seek(p);
                f.readFully(ch);
                int id = Bytes.getInt(ch, 0);
                long size = Bytes.getInt(ch, 4) & 0xffff_ffffL;
                if (id == Bytes.fourCC("COMM")) {
                    byte[] c = new byte[(int) Math.min(size, 64)];
                    f.readFully(c);
                    channels = Bytes.getShort(c, 0);
                    frames = Bytes.getInt(c, 2) & 0xffff_ffffL;
                    bits = Bytes.getShort(c, 6);
                    rate = extended(c, 8);
                    if (aifc && c.length >= 22) {
                        compression = Bytes.getInt(c, 18);
                    }
                } else if (id == Bytes.fourCC("SSND")) {
                    byte[] s = new byte[8];
                    f.readFully(s);
                    dataOffset = p + 16 + (Bytes.getInt(s, 0) & 0xffff_ffffL);
                    dataSize = size - 8;
                }
                p += 8 + size + (size & 1);
            }
            if (channels <= 0 || bits <= 0 || dataOffset < 0) {
                throw new IOException("broken AIFF: " + path);
            }
            byte[] desc;
            long bytes;
            if (compression == Bytes.fourCC("NONE") || compression == Bytes.fourCC("twos")) {
                desc = soundDescription(Bytes.fourCC("twos"), channels, bits, rate, true, false, true);
                bytes = frames * channels * ((bits + 7) / 8);
            } else if (compression == Bytes.fourCC("sowt")) {
                desc = soundDescription(Bytes.fourCC("sowt"), channels, bits, rate, false, false, true);
                bytes = frames * channels * ((bits + 7) / 8);
            } else if (compression == Bytes.fourCC("fl32") || compression == Bytes.fourCC("FL32") || compression == Bytes.fourCC("fl64")) {
                int b = compression == Bytes.fourCC("fl64") ? 64 : 32;
                desc = soundDescription(Bytes.fourCC("lpcm"), channels, b, rate, true, true, true);
                bytes = frames * channels * (b / 8);
            } else if (compression == Bytes.fourCC("ulaw") || compression == Bytes.fourCC("alaw")) {
                desc = soundDescription(compression, channels, 8, rate, true, false, true);
                bytes = frames * channels;
            } else {
                // compressed AIFC, the platform decodes it
                return null;
            }
            return soundMovie(path, desc, rate, frames, dataOffset, Math.min(bytes, dataSize));
        }
    }

    static MovieRec wave(String path) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(path, "r")) {
            long p = 12;
            long end = f.length();
            int format = 0, channels = 0, bits = 0;
            double rate = 0;
            long dataOffset = -1, dataSize = 0;
            byte[] ch = new byte[8];
            while (p + 8 <= end) {
                f.seek(p);
                f.readFully(ch);
                String id = new String(ch, 0, 4, StandardCharsets.US_ASCII);
                long size = (ch[4] & 0xff) | ((ch[5] & 0xff) << 8) | ((ch[6] & 0xff) << 16) | ((long) (ch[7] & 0xff) << 24);
                if (id.equals("fmt ")) {
                    byte[] c = new byte[(int) Math.min(size, 64)];
                    f.readFully(c);
                    format = le16(c, 0);
                    channels = le16(c, 2);
                    rate = le32(c, 4) & 0xffff_ffffL;
                    bits = le16(c, 14);
                    if (format == 0xfffe && c.length >= 26) {
                        format = le16(c, 24);
                    }
                } else if (id.equals("data")) {
                    dataOffset = p + 8;
                    dataSize = Math.min(size, end - dataOffset);
                    break;
                }
                p += 8 + size + (size & 1);
            }
            if (channels <= 0 || bits <= 0 || dataOffset < 0) {
                throw new IOException("broken WAVE: " + path);
            }
            int frameBytes = channels * ((bits + 7) / 8);
            long frames = dataSize / frameBytes;
            byte[] desc = switch (format) {
                case 1 -> bits == 8 ? soundDescription(Bytes.fourCC("raw "), channels, 8, rate, false, false, false)
                        : bits == 16 ? soundDescription(Bytes.fourCC("sowt"), channels, 16, rate, false, false, true)
                        : soundDescription(Bytes.fourCC("lpcm"), channels, bits, rate, false, false, true);
                case 3 -> soundDescription(Bytes.fourCC("lpcm"), channels, bits, rate, false, true, true);
                case 6 -> soundDescription(Bytes.fourCC("alaw"), channels, 8, rate, false, false, true);
                case 7 -> soundDescription(Bytes.fourCC("ulaw"), channels, 8, rate, false, false, true);
                default -> null;
            };
            if (desc == null) {
                return null;
            }
            return soundMovie(path, desc, rate, frames, dataOffset, frames * frameBytes);
        }
    }

    private static int le16(byte[] b, int o) {
        return (b[o] & 0xff) | ((b[o + 1] & 0xff) << 8);
    }

    private static int le32(byte[] b, int o) {
        return le16(b, o) | (le16(b, o + 2) << 16);
    }

    // ---- images

    /** @return an image description for a still image */
    static byte[] imageDescription(int codec, int width, int height, int depth, String name, int dataSize) {
        byte[] d = new byte[86];
        Bytes.putInt(d, 0, 86);
        Bytes.putInt(d, 4, codec);
        Bytes.putShort(d, 14, 1);
        Bytes.putShort(d, 16, 1);
        Bytes.putShort(d, 18, 1);
        Bytes.putInt(d, 20, Bytes.fourCC("appl"));
        Bytes.putInt(d, 28, 0x200);
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

    static MovieRec image(String path, int type) throws IOException {
        int width, height;
        try (ImageInputStream in = ImageIO.createImageInputStream(new java.io.File(path))) {
            var readers = ImageIO.getImageReaders(in);
            if (!readers.hasNext()) {
                return null;
            }
            ImageReader r = readers.next();
            r.setInput(in);
            width = r.getWidth(0);
            height = r.getHeight(0);
            r.dispose();
        }
        long size = new java.io.File(path).length();
        int codec = type == JPEG ? Bytes.fourCC("jpeg") : type == PNGf ? Bytes.fourCC("png ") : type == GIFf ? Bytes.fourCC("gif ") : Bytes.fourCC("WRLE");
        String name = type == JPEG ? "Photo - JPEG" : type == PNGf ? "PNG" : type == GIFf ? "GIF" : "BMP";
        MovieRec m = new MovieRec();
        TrackRec t = m.newTrack();
        t.width = width << 16;
        t.height = height << 16;
        MediaRec md = new MediaRec(t, Bytes.fourCC("vide"), 600);
        md.dataRefs.add(DataRefRec.forFile(path));
        md.handlerName = "Apple Video Media Handler";
        int di = md.descriptionIndex(imageDescription(codec, width, height, 24, name, (int) size));
        md.addSamples(1, 0, (int) size, size, 600, 1, di, true, 0);
        t.media = md;
        t.edits.add(new TrackRec.Edit(600, 0, 0x10000));
        m.updateTimeBase();
        m.changed = false;
        return m;
    }
}
