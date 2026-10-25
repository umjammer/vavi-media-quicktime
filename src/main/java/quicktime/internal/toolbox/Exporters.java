/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Movie export components in Java.
 * <ul>
 * <li>QuickTime movies are flattened.</li>
 * <li>MPEG-4 and 3GPP files are remuxed when the codecs fit the format, the platform encodes otherwise.</li>
 * <li>AIFF and WAVE files are rendered from PCM sound tracks.</li>
 * <li>Text tracks are exported as QuickTime text descriptors.</li>
 * </ul>
 */
final class Exporters {

    private Exporters() {
    }

    static final int MooV = Bytes.fourCC("MooV");
    static final int mpg4 = Bytes.fourCC("mpg4");
    static final int M4V = Bytes.fourCC("M4V ");
    static final int M4A = Bytes.fourCC("M4A ");
    static final int _3gpp = Bytes.fourCC("3gpp");
    static final int _3gp2 = Bytes.fourCC("3gp2");
    static final int AIFF = Bytes.fourCC("AIFF");
    static final int WAVE = Bytes.fourCC("WAVE");
    static final int TEXT = Bytes.fourCC("TEXT");

    /** codecs allowed in ISO files */
    private static final Set<Integer> ISO_VIDEO = Set.of(Bytes.fourCC("avc1"), Bytes.fourCC("avc3"), Bytes.fourCC("hvc1"), Bytes.fourCC("hev1"),
            Bytes.fourCC("mp4v"), Bytes.fourCC("s263"), Bytes.fourCC("h263"), Bytes.fourCC("jpeg"));
    private static final Set<Integer> ISO_AUDIO = Set.of(Bytes.fourCC("mp4a"), Bytes.fourCC("samr"), Bytes.fourCC("sawb"), Bytes.fourCC("sevc"),
            Bytes.fourCC("alac"), Bytes.fourCC("ac-3"), Bytes.fourCC("ec-3"));

    /** @return the 'ftyp' brands of the file type, null for QuickTime */
    static int[] brands(int fileType) {
        if (fileType == mpg4) {
            return new int[] {Bytes.fourCC("mp42"), Bytes.fourCC("isom")};
        }
        if (fileType == M4V) {
            return new int[] {Bytes.fourCC("M4V "), Bytes.fourCC("M4A "), Bytes.fourCC("mp42"), Bytes.fourCC("isom")};
        }
        if (fileType == M4A) {
            return new int[] {Bytes.fourCC("M4A "), Bytes.fourCC("mp42"), Bytes.fourCC("isom")};
        }
        if (fileType == _3gpp) {
            return new int[] {Bytes.fourCC("3gp4"), Bytes.fourCC("3gp5"), Bytes.fourCC("isom")};
        }
        if (fileType == _3gp2) {
            return new int[] {Bytes.fourCC("3g2a"), Bytes.fourCC("isom")};
        }
        return null;
    }

    /** @return the file name extension of the file type */
    static String extension(int fileType) {
        if (fileType == mpg4) {
            return "mp4";
        }
        if (fileType == M4V) {
            return "m4v";
        }
        if (fileType == M4A) {
            return "m4a";
        }
        if (fileType == _3gpp) {
            return "3gp";
        }
        if (fileType == _3gp2) {
            return "3g2";
        }
        if (fileType == AIFF) {
            return "aif";
        }
        if (fileType == WAVE) {
            return "wav";
        }
        if (fileType == TEXT) {
            return "txt";
        }
        return "mov";
    }

    /** @return the movie to export: the time range and only track applied */
    static MovieRec part(MovieRec m, TrackRec only, long start, long duration) {
        MovieRec part = MovieEdits.copyRange(m, start, duration > 0 ? duration : Math.max(1, m.duration() - start));
        if (only != null) {
            for (TrackRec t : new ArrayList<>(part.tracks)) {
                if (t.id != only.id) {
                    part.removeTrack(t);
                }
            }
        }
        return part;
    }

    /**
     * Exports the movie part, which is consumed.
     * @return an OSErr
     */
    static int export(MovieRec part, String path, int fileType, MovieFiles.Progress progress) throws IOException {
        try {
            if (fileType == MooV || fileType == 0) {
                MovieFiles.flatten(part, path, MovieFiles.flattenForceMovieResourceBeforeMovieData, null, false, progress).dispose();
                part = null;
                return 0;
            }
            int[] brands = brands(fileType);
            if (brands != null) {
                if (toIso(part, fileType == M4A)) {
                    MovieFiles.flatten(part, path, MovieFiles.flattenForceMovieResourceBeforeMovieData, brands, true, progress).dispose();
                    part = null;
                    return 0;
                }
            } else if (fileType == AIFF || fileType == WAVE) {
                if (writePcm(part, path, fileType == AIFF)) {
                    return 0;
                }
            } else if (fileType == TEXT) {
                return writeText(part, path);
            }
            return MediaEngine.get().export(part, path, fileType, progress);
        } finally {
            if (part != null) {
                part.dispose();
            }
        }
    }

    // ---- ISO

    /** converts the movie for an ISO file, @return false if a codec does not fit */
    static boolean toIso(MovieRec m, boolean audioOnly) {
        for (TrackRec t : new ArrayList<>(m.tracks)) {
            if (t.media == null || !t.enabled) {
                m.removeTrack(t);
                continue;
            }
            MediaRec md = t.media;
            boolean video = md.type == Bytes.fourCC("vide");
            boolean sound = md.type == Bytes.fourCC("soun");
            if (!video && !sound) {
                // text, time code, ... are not carried
                m.removeTrack(t);
                continue;
            }
            if (audioOnly && video) {
                m.removeTrack(t);
                continue;
            }
            for (int i = 0; i < md.descriptions.size(); i++) {
                byte[] d = md.descriptions.get(i);
                int format = Bytes.getInt(d, 4);
                if (video && !ISO_VIDEO.contains(format)) {
                    return false;
                }
                if (sound) {
                    if (!ISO_AUDIO.contains(format)) {
                        return false;
                    }
                    md.descriptions.set(i, isoSoundDescription(d));
                }
            }
            if (sound) {
                md.handlerName = "SoundHandler";
            } else {
                md.handlerName = "VideoHandler";
            }
        }
        return !m.tracks.isEmpty();
    }

    /** @return a version 0 sound sample entry, the decoder configuration moved out of a QuickTime 'wave' atom */
    static byte[] isoSoundDescription(byte[] d) {
        int version = Bytes.getShort(d, 16);
        if (version == 0) {
            return d;
        }
        int fixed = version == 1 ? 52 : version == 2 ? 72 : 36;
        int channels = Bytes.getShort(d, 24);
        int bits = Bytes.getShort(d, 26);
        int rate = Bytes.getInt(d, 32);
        if (version == 2) {
            double r = Double.longBitsToDouble(Bytes.getLong(d, 40));
            channels = Bytes.getInt(d, 48);
            rate = r < 65536 ? (int) Math.round(r * 65536) : 0;
            bits = 16;
        }
        List<byte[]> children = new ArrayList<>();
        for (MovieAtoms.Atom a : MovieAtoms.atoms(d, Math.min(fixed, d.length), d.length)) {
            if (a.type() == Bytes.fourCC("wave")) {
                for (MovieAtoms.Atom w : MovieAtoms.atoms(d, a.data(), a.end())) {
                    if (w.type() == Bytes.fourCC("esds") || w.type() == Bytes.fourCC("damr") || w.type() == Bytes.fourCC("dawp")
                            || w.type() == Bytes.fourCC("alac") || w.type() == Bytes.fourCC("dac3") || w.type() == Bytes.fourCC("dec3")) {
                        children.add(java.util.Arrays.copyOfRange(d, w.offset(), w.end()));
                    }
                }
            } else if (a.type() != Bytes.fourCC("chan")) {
                children.add(java.util.Arrays.copyOfRange(d, a.offset(), a.end()));
            }
        }
        int size = 36;
        for (byte[] c : children) {
            size += c.length;
        }
        byte[] n = new byte[size];
        Bytes.putInt(n, 0, size);
        Bytes.putInt(n, 4, Bytes.getInt(d, 4));
        Bytes.putShort(n, 14, Bytes.getShort(d, 14));
        Bytes.putShort(n, 24, channels);
        Bytes.putShort(n, 26, bits == 0 ? 16 : bits);
        Bytes.putInt(n, 32, rate);
        int p = 36;
        for (byte[] c : children) {
            System.arraycopy(c, 0, n, p, c.length);
            p += c.length;
        }
        return n;
    }

    // ---- PCM

    /** PCM sample format */
    record Pcm(int channels, int bits, double rate, boolean bigEndian, boolean isFloat, boolean signed) {
        int frameBytes() {
            return channels * ((bits + 7) / 8);
        }
    }

    /** @return the PCM format of a sound description, null if compressed */
    static Pcm pcm(byte[] d) {
        int format = Bytes.getInt(d, 4);
        int version = Bytes.getShort(d, 16);
        int channels = Bytes.getShort(d, 24);
        int bits = Bytes.getShort(d, 26);
        double rate = (Bytes.getInt(d, 32) & 0xffff_ffffL) / 65536.0;
        if (version == 2 && d.length >= 72) {
            rate = Double.longBitsToDouble(Bytes.getLong(d, 40));
            channels = Bytes.getInt(d, 48);
            bits = Bytes.getInt(d, 56);
            int flags = Bytes.getInt(d, 60);
            if (format == Bytes.fourCC("lpcm")) {
                return new Pcm(channels, bits, rate, (flags & 2) != 0, (flags & 1) != 0, (flags & 4) != 0 || (flags & 1) != 0);
            }
        }
        if (format == Bytes.fourCC("twos")) {
            return new Pcm(channels, bits, rate, true, false, true);
        }
        if (format == Bytes.fourCC("sowt")) {
            return new Pcm(channels, bits, rate, false, false, true);
        }
        if (format == Bytes.fourCC("raw ") || format == Bytes.fourCC("NONE")) {
            return new Pcm(channels, bits, rate, true, false, bits > 8);
        }
        if (format == Bytes.fourCC("in24") || format == Bytes.fourCC("in32") || format == Bytes.fourCC("fl32") || format == Bytes.fourCC("fl64")) {
            boolean little = false;
            for (MovieAtoms.Atom a : MovieAtoms.atoms(d, Math.min(version == 1 ? 52 : 36, d.length), d.length)) {
                if (a.type() == Bytes.fourCC("wave")) {
                    for (MovieAtoms.Atom w : MovieAtoms.atoms(d, a.data(), a.end())) {
                        if (w.type() == Bytes.fourCC("enda")) {
                            little = Bytes.getShort(d, w.data()) != 0;
                        }
                    }
                }
            }
            int b = format == Bytes.fourCC("in24") ? 24 : format == Bytes.fourCC("fl64") ? 64 : 32;
            boolean f = format == Bytes.fourCC("fl32") || format == Bytes.fourCC("fl64");
            return new Pcm(channels, b, rate, !little, f, true);
        }
        return null;
    }

    /** @return a sample as a double in [-1, 1) */
    private static double sample(byte[] b, int o, Pcm f) {
        int bytes = (f.bits + 7) / 8;
        long v = 0;
        for (int i = 0; i < bytes; i++) {
            int x = b[o + (f.bigEndian ? i : bytes - 1 - i)] & 0xff;
            v = (v << 8) | x;
        }
        if (f.isFloat) {
            return bytes == 8 ? Double.longBitsToDouble(v) : Float.intBitsToFloat((int) v);
        }
        if (f.signed) {
            v = (v << (64 - f.bits)) >> (64 - f.bits);
        } else {
            v -= 1L << (f.bits - 1);
        }
        return v / (double) (1L << (f.bits - 1));
    }

    private static void putSample(byte[] b, int o, double s, int bits, boolean bigEndian, boolean signed) {
        int bytes = bits / 8;
        long max = (1L << (bits - 1)) - 1;
        long v = Math.max(-max - 1, Math.min(max, Math.round(s * (max + 1))));
        if (!signed) {
            v += max + 1;
        }
        for (int i = 0; i < bytes; i++) {
            b[o + (bigEndian ? bytes - 1 - i : i)] = (byte) (v >> (8 * i));
        }
    }

    /** writes the first enabled PCM sound track, @return false if there is none */
    static boolean writePcm(MovieRec m, String path, boolean aiff) throws IOException {
        TrackRec sound = null;
        int soundTracks = 0;
        for (TrackRec t : m.tracks) {
            if (t.enabled && t.media != null && t.media.isSound()) {
                soundTracks++;
                sound = sound == null ? t : sound;
            }
        }
        if (sound == null || (soundTracks > 1 && MediaEngine.get().isAvailable())) {
            return false;
        }
        MediaRec md = sound.media;
        Pcm in = null;
        for (byte[] d : md.descriptions) {
            Pcm p = pcm(d);
            if (p == null || (in != null && !in.equals(p))) {
                return false;
            }
            in = p;
        }
        if (in == null) {
            return false;
        }
        for (TrackRec.Edit e : sound.edits) {
            if (!e.isEmpty() && e.rate != 0x10000) {
                return false;
            }
        }
        int outBits = in.isFloat ? 16 : Math.min(32, Math.max(8, in.bits));
        int outFrame = in.channels * outBits / 8;
        // frames in the media time scale, which is the sample rate for sound
        long frames = 0;
        for (TrackRec.Edit e : sound.edits) {
            frames += sound.mediaDuration(e.duration, 0x10000);
        }
        DataRefRec.closeReader(path);
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(path))) {
            byte[] header = aiff ? aiffHeader(in, outBits, frames) : waveHeader(in, outBits, frames);
            os.write(header);
            byte[] silence = new byte[outFrame];
            if (!aiff && outBits == 8) {
                java.util.Arrays.fill(silence, (byte) 0x80);
            }
            for (TrackRec.Edit e : sound.edits) {
                long n = sound.mediaDuration(e.duration, 0x10000);
                if (e.isEmpty()) {
                    for (long i = 0; i < n; i++) {
                        os.write(silence);
                    }
                    continue;
                }
                long t = e.mediaTime;
                long left = n;
                while (left > 0) {
                    long s = md.sampleAt(t);
                    if (s == 0) {
                        for (long i = 0; i < left; i++) {
                            os.write(silence);
                        }
                        break;
                    }
                    int r = md.runOfSample(s);
                    MediaRec.SampleRun run = md.runs.get(r);
                    long k = s - md.firstSampleOf(r);
                    long count = Math.min(run.count - k, Math.max(1, left / Math.max(1, run.duration)));
                    count = Math.min(count, 65536);
                    byte[] data = md.dataRef(run.dataRefIndex).read(run.offsetOf(k), (int) run.bytesOf(k, count));
                    long framesHere = Math.min(left, count * run.duration);
                    int inFrame = in.frameBytes();
                    byte[] out = new byte[(int) framesHere * outFrame];
                    for (int i = 0; i < framesHere && (long) (i + 1) * inFrame <= data.length; i++) {
                        for (int c = 0; c < in.channels; c++) {
                            double v = sample(data, i * inFrame + c * ((in.bits + 7) / 8), in);
                            putSample(out, i * outFrame + c * (outBits / 8), v, outBits, aiff, aiff || outBits > 8);
                        }
                    }
                    os.write(out);
                    left -= framesHere;
                    t += framesHere;
                }
            }
            if ((frames * outFrame & 1) != 0) {
                os.write(0);
            }
        }
        return true;
    }

    /** 80 bit IEEE extended */
    static byte[] extended(double v) {
        byte[] b = new byte[10];
        if (v == 0) {
            return b;
        }
        int exp = Math.getExponent(v);
        long mant = (long) (v / Math.pow(2, exp) * (1L << 62)) << 1;
        Bytes.putShort(b, 0, exp + 16383);
        Bytes.putLong(b, 2, mant | Long.MIN_VALUE);
        return b;
    }

    private static byte[] aiffHeader(Pcm in, int bits, long frames) {
        long data = frames * in.channels * bits / 8;
        MovieAtoms.Out o = new MovieAtoms.Out();
        o.i32(Bytes.fourCC("FORM")).i32((int) (4 + 26 + 16 + data + (data & 1))).i32(Bytes.fourCC("AIFF"));
        o.i32(Bytes.fourCC("COMM")).i32(18).i16(in.channels).i32((int) frames).i16(bits).bytes(extended(in.rate));
        o.i32(Bytes.fourCC("SSND")).i32((int) (8 + data)).i32(0).i32(0);
        return o.toByteArray();
    }

    private static byte[] waveHeader(Pcm in, int bits, long frames) {
        long data = frames * in.channels * bits / 8;
        byte[] b = new byte[44];
        System.arraycopy("RIFF".getBytes(StandardCharsets.US_ASCII), 0, b, 0, 4);
        le32(b, 4, (int) (36 + data + (data & 1)));
        System.arraycopy("WAVEfmt ".getBytes(StandardCharsets.US_ASCII), 0, b, 8, 8);
        le32(b, 16, 16);
        le16(b, 20, 1);
        le16(b, 22, in.channels);
        le32(b, 24, (int) Math.round(in.rate));
        le32(b, 28, (int) Math.round(in.rate) * in.channels * bits / 8);
        le16(b, 32, in.channels * bits / 8);
        le16(b, 34, bits);
        System.arraycopy("data".getBytes(StandardCharsets.US_ASCII), 0, b, 36, 4);
        le32(b, 40, (int) data);
        return b;
    }

    private static void le16(byte[] b, int o, int v) {
        b[o] = (byte) v;
        b[o + 1] = (byte) (v >> 8);
    }

    private static void le32(byte[] b, int o, int v) {
        le16(b, o, v);
        le16(b, o + 2, v >> 16);
    }

    // ---- text

    /** writes the text tracks as a QuickTime text descriptor file */
    static int writeText(MovieRec m, String path) throws IOException {
        TrackRec text = null;
        for (TrackRec t : m.tracks) {
            if (t.media != null && t.media.type == Bytes.fourCC("text")) {
                text = t;
                break;
            }
        }
        if (text == null) {
            return MovieToolbox.featureUnsupported;
        }
        StringBuilder sb = new StringBuilder("{QTtext}{timeScale:" + m.timeScale + "}\n");
        long s = 0;
        for (TrackRec.Edit e : text.edits) {
            if (!e.isEmpty()) {
                MediaRec md = text.media;
                long mt = e.mediaTime;
                long end = e.mediaTime + text.mediaDuration(e.duration, e.rate);
                long sample = md.sampleAt(mt);
                while (sample > 0 && sample <= md.sampleCount() && md.timeOfSample(sample) < end) {
                    long t = s + text.trackDuration(Math.max(0, md.timeOfSample(sample) - mt), e.rate);
                    sb.append('[').append(timeStamp(t, m.timeScale)).append("]\n");
                    String str = MovieToolbox.textSample(md, sample);
                    sb.append(str == null ? "" : str.replace("\r", "\n")).append('\n');
                    sample++;
                }
            }
            s += e.duration;
        }
        sb.append('[').append(timeStamp(s, m.timeScale)).append("]\n");
        DataRefRec.closeReader(path);
        try (RandomAccessFile f = new RandomAccessFile(path, "rw")) {
            f.setLength(0);
            f.write(sb.toString().getBytes(Bytes.MAC_ROMAN));
        }
        return 0;
    }

    private static String timeStamp(long t, int scale) {
        long total = t / scale;
        long frac = t % scale;
        return String.format("%02d:%02d:%02d.%02d", total / 3600, total / 60 % 60, total % 60, frac * 100 / scale);
    }
}
