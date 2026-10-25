/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


/**
 * Reads and writes the movie atom ('moov') of QuickTime and ISO base media (MPEG-4, 3GPP) files.
 */
final class MovieAtoms {

    private MovieAtoms() {
    }

    static int t(String s) {
        return Bytes.fourCC(s);
    }

    static final int moov = t("moov"), cmov = t("cmov"), dcom = t("dcom"), cmvd = t("cmvd"), mvhd = t("mvhd"), trak = t("trak"),
            tkhd = t("tkhd"), edts = t("edts"), elst = t("elst"), mdia = t("mdia"), mdhd = t("mdhd"), hdlr = t("hdlr"),
            minf = t("minf"), vmhd = t("vmhd"), smhd = t("smhd"), gmhd = t("gmhd"), dinf = t("dinf"), dref = t("dref"),
            stbl = t("stbl"), stsd = t("stsd"), stts = t("stts"), ctts = t("ctts"), stss = t("stss"), stsc = t("stsc"),
            stsz = t("stsz"), stz2 = t("stz2"), stco = t("stco"), co64 = t("co64"), udta = t("udta"), tref = t("tref"),
            load = t("load"), mdat = t("mdat"), ftyp = t("ftyp"), free = t("free"), skip = t("skip"), wide = t("wide"),
            mhlr = t("mhlr"), dhlr = t("dhlr"), soun = t("soun"), vide = t("vide"), gmin = t("gmin"), zlib = t("zlib");

    // ---- atom parsing

    /** an atom in a byte array */
    record Atom(int type, int offset, int headerSize, int size) {
        int data() {
            return offset + headerSize;
        }

        int end() {
            return offset + size;
        }
    }

    /** @return the atoms in the range */
    static List<Atom> atoms(byte[] b, int off, int end) {
        List<Atom> list = new ArrayList<>();
        int p = off;
        while (p + 8 <= end) {
            long size = Bytes.getInt(b, p) & 0xffff_ffffL;
            int type = Bytes.getInt(b, p + 4);
            int header = 8;
            if (size == 1) {
                size = Bytes.getLong(b, p + 8);
                header = 16;
            } else if (size == 0) {
                size = end - p;
            }
            if (size < header || p + size > end) {
                if (size == 4 || (size < 8 && list.size() > 0)) {
                    // terminating 32 bit 0 of 'udta' and such
                    break;
                }
                break;
            }
            list.add(new Atom(type, p, header, (int) size));
            p += (int) size;
        }
        return list;
    }

    static Atom child(byte[] b, Atom parent, int type) {
        for (Atom a : atoms(b, parent.data(), parent.end())) {
            if (a.type == type) {
                return a;
            }
        }
        return null;
    }

    static List<Atom> children(byte[] b, Atom parent, int type) {
        List<Atom> list = new ArrayList<>();
        for (Atom a : atoms(b, parent.data(), parent.end())) {
            if (a.type == type) {
                list.add(a);
            }
        }
        return list;
    }

    // ---- reading

    /** @return the 'moov' atom bytes (with header) of the file and its offset, null if none */
    static long[] findMoov(RandomAccessFile f) throws IOException {
        long p = 0;
        long length = f.length();
        byte[] h = new byte[16];
        while (p + 8 <= length) {
            f.seek(p);
            f.readFully(h, 0, (int) Math.min(16, length - p));
            long size = Bytes.getInt(h, 0) & 0xffff_ffffL;
            int type = Bytes.getInt(h, 4);
            if (size == 1) {
                size = Bytes.getLong(h, 8);
            } else if (size == 0) {
                size = length - p;
            }
            if (size < 8) {
                break;
            }
            if (type == moov) {
                return new long[] {p, size};
            }
            p += size;
        }
        return null;
    }

    /** @return true if the file looks like a QuickTime or ISO media file */
    static boolean isMovieFile(String path) {
        try (RandomAccessFile f = new RandomAccessFile(path, "r")) {
            return findMoov(f) != null;
        } catch (IOException e) {
            return false;
        }
    }

    /** reads the movie of the file */
    static MovieRec readFile(String path) throws IOException {
        try (RandomAccessFile f = new RandomAccessFile(path, "r")) {
            long[] m = findMoov(f);
            if (m == null) {
                return null;
            }
            byte[] b = new byte[(int) m[1]];
            f.seek(m[0]);
            f.readFully(b);
            MovieRec movie = read(b, path);
            movie.path = path;
            movie.moovOffset = m[0];
            movie.moovSize = m[1];
            return movie;
        }
    }

    /** reads a movie from a 'moov' atom (with header) */
    static MovieRec read(byte[] b, String selfPath) throws IOException {
        List<Atom> top = atoms(b, 0, b.length);
        Atom moovAtom = null;
        for (Atom a : top) {
            if (a.type == moov) {
                moovAtom = a;
                break;
            }
        }
        if (moovAtom == null) {
            throw new IOException("no movie atom");
        }
        Atom cm = child(b, moovAtom, cmov);
        if (cm != null) {
            byte[] u = decompress(b, cm);
            return read(u, selfPath);
        }
        MovieRec movie = new MovieRec();
        try {
            for (Atom a : atoms(b, moovAtom.data(), moovAtom.end())) {
                if (a.type == mvhd) {
                    readMvhd(b, a, movie);
                } else if (a.type == udta) {
                    movie.userData.dispose();
                    movie.userData = UserDataRec.fromBytes(b, a.data(), a.end());
                    movie.userData.owned = true;
                }
            }
            for (Atom a : children(b, moovAtom, trak)) {
                readTrak(b, a, movie, selfPath);
            }
        } catch (RuntimeException e) {
            movie.dispose();
            throw new IOException("broken movie atom", e);
        }
        movie.timeBase.scale = movie.timeScale;
        movie.timeBase.setTime(0, movie.timeScale);
        movie.updateTimeBase();
        movie.changed = false;
        return movie;
    }

    private static byte[] decompress(byte[] b, Atom cm) throws IOException {
        Atom d = child(b, cm, dcom);
        Atom v = child(b, cm, cmvd);
        if (d == null || v == null || Bytes.getInt(b, d.data()) != zlib) {
            throw new IOException("unsupported compressed movie");
        }
        int size = Bytes.getInt(b, v.data());
        Inflater inflater = new Inflater();
        inflater.setInput(b, v.data() + 4, v.end() - v.data() - 4);
        byte[] u = new byte[size];
        try {
            int n = 0;
            while (n < size && !inflater.finished()) {
                int r = inflater.inflate(u, n, size - n);
                if (r == 0 && inflater.needsInput()) {
                    break;
                }
                n += r;
            }
        } catch (DataFormatException e) {
            throw new IOException(e);
        } finally {
            inflater.end();
        }
        return u;
    }

    private static void readMvhd(byte[] b, Atom a, MovieRec m) {
        int p = a.data();
        int version = b[p] & 0xff;
        p += 4;
        if (version == 1) {
            m.creationTime = (int) Bytes.getLong(b, p);
            m.modificationTime = (int) Bytes.getLong(b, p + 8);
            m.timeScale = Bytes.getInt(b, p + 16);
            p += 28;
        } else {
            m.creationTime = Bytes.getInt(b, p);
            m.modificationTime = Bytes.getInt(b, p + 4);
            m.timeScale = Bytes.getInt(b, p + 8);
            p += 16;
        }
        if (m.timeScale <= 0) {
            m.timeScale = 600;
        }
        m.preferredRate = Bytes.getInt(b, p);
        m.preferredVolume = Bytes.getShort(b, p + 4);
        m.volume = m.preferredVolume;
        p += 16;
        for (int i = 0; i < 9; i++) {
            m.matrix[i] = Bytes.getInt(b, p + i * 4);
        }
        p += 36;
        if (p + 28 <= a.end()) {
            m.previewTime = Bytes.getInt(b, p);
            m.previewDuration = Bytes.getInt(b, p + 4);
            m.posterTime = Bytes.getInt(b, p + 8);
            m.selectionTime = Bytes.getInt(b, p + 12);
            m.selectionDuration = Bytes.getInt(b, p + 16);
            m.nextTrackID = Math.max(1, Bytes.getInt(b, p + 24));
        }
    }

    private static void readTrak(byte[] b, Atom trakAtom, MovieRec movie, String selfPath) {
        Atom th = child(b, trakAtom, tkhd);
        if (th == null) {
            return;
        }
        TrackRec t = new TrackRec(movie, 0);
        int p = th.data();
        int version = b[p] & 0xff;
        int flags = Bytes.getInt(b, p) & 0xffffff;
        t.enabled = (flags & TrackRec.trackEnabled) != 0;
        t.usage = flags & (TrackRec.trackUsageInMovie | TrackRec.trackUsageInPreview | TrackRec.trackUsageInPoster);
        p += 4;
        long tkhdDuration;
        if (version == 1) {
            t.creationTime = (int) Bytes.getLong(b, p);
            t.modificationTime = (int) Bytes.getLong(b, p + 8);
            t.id = Bytes.getInt(b, p + 16);
            tkhdDuration = Bytes.getLong(b, p + 24);
            p += 32;
        } else {
            t.creationTime = Bytes.getInt(b, p);
            t.modificationTime = Bytes.getInt(b, p + 4);
            t.id = Bytes.getInt(b, p + 8);
            tkhdDuration = Bytes.getInt(b, p + 16) & 0xffff_ffffL;
            p += 20;
        }
        p += 8;
        t.layer = Bytes.getShort(b, p);
        t.alternateGroup = Bytes.getShort(b, p + 2);
        t.volume = Bytes.getShort(b, p + 4);
        p += 8;
        for (int i = 0; i < 9; i++) {
            t.matrix[i] = Bytes.getInt(b, p + i * 4);
        }
        p += 36;
        t.width = Bytes.getInt(b, p);
        t.height = Bytes.getInt(b, p + 4);
        movie.tracks.add(t);
        movie.nextTrackID = Math.max(movie.nextTrackID, t.id + 1);

        Atom md = child(b, trakAtom, mdia);
        if (md != null) {
            readMdia(b, md, t, selfPath);
        }
        Atom ed = child(b, trakAtom, edts);
        Atom el = ed != null ? child(b, ed, elst) : null;
        if (el != null) {
            int q = el.data();
            int v = b[q] & 0xff;
            int n = Bytes.getInt(b, q + 4);
            q += 8;
            for (int i = 0; i < n; i++) {
                long dur, mt;
                if (v == 1) {
                    dur = Bytes.getLong(b, q);
                    mt = Bytes.getLong(b, q + 8);
                    q += 16;
                } else {
                    dur = Bytes.getInt(b, q) & 0xffff_ffffL;
                    mt = Bytes.getInt(b, q + 4);
                    q += 8;
                }
                int rate = Bytes.getInt(b, q);
                q += 4;
                if (dur > 0 || n == 1) {
                    t.edits.add(new TrackRec.Edit(dur == 0 && t.media != null ? t.trackDuration(t.media.duration() - Math.max(0, mt), rate) : dur, mt, rate == 0 ? 0x10000 : rate));
                }
            }
        } else if (t.media != null && t.media.duration() > 0) {
            long d = tkhdDuration > 0 ? tkhdDuration : t.trackDuration(t.media.duration(), 0x10000);
            t.edits.add(new TrackRec.Edit(d, 0, 0x10000));
        }
        Atom tr = child(b, trakAtom, tref);
        if (tr != null) {
            for (Atom r : atoms(b, tr.data(), tr.end())) {
                List<Integer> ids = new ArrayList<>();
                for (int q = r.data(); q + 4 <= r.end(); q += 4) {
                    ids.add(Bytes.getInt(b, q));
                }
                t.references.put(r.type, ids);
            }
        }
        Atom ud = child(b, trakAtom, udta);
        if (ud != null) {
            t.userData.dispose();
            t.userData = UserDataRec.fromBytes(b, ud.data(), ud.end());
            t.userData.owned = true;
        }
        Atom ld = child(b, trakAtom, load);
        if (ld != null && ld.end() - ld.data() >= 16) {
            t.preloadTime = Bytes.getInt(b, ld.data());
            t.preloadDuration = Bytes.getInt(b, ld.data() + 4);
            t.preloadFlags = Bytes.getInt(b, ld.data() + 8);
            t.defaultHints = Bytes.getInt(b, ld.data() + 12);
        }
    }

    private static void readMdia(byte[] b, Atom mdiaAtom, TrackRec t, String selfPath) {
        Atom mh = child(b, mdiaAtom, mdhd);
        Atom hd = child(b, mdiaAtom, hdlr);
        int type = hd != null ? Bytes.getInt(b, hd.data() + 8) : 0;
        int scale = 600;
        MediaRec m;
        if (mh != null) {
            int p = mh.data();
            int version = b[p] & 0xff;
            p += 4;
            int creation, modification;
            if (version == 1) {
                creation = (int) Bytes.getLong(b, p);
                modification = (int) Bytes.getLong(b, p + 8);
                scale = Bytes.getInt(b, p + 16);
                p += 28;
            } else {
                creation = Bytes.getInt(b, p);
                modification = Bytes.getInt(b, p + 4);
                scale = Bytes.getInt(b, p + 8);
                p += 16;
            }
            m = new MediaRec(t, type, scale <= 0 ? 600 : scale);
            m.creationTime = creation;
            m.modificationTime = modification;
            m.language = Bytes.getShort(b, p);
            m.quality = Bytes.getShort(b, p + 2);
        } else {
            m = new MediaRec(t, type, scale);
        }
        if (hd != null) {
            m.handlerManufacturer = Bytes.getInt(b, hd.data() + 12);
            m.handlerName = handlerName(b, hd);
        }
        t.media = m;
        Atom mi = child(b, mdiaAtom, minf);
        if (mi == null) {
            return;
        }
        for (Atom a : atoms(b, mi.data(), mi.end())) {
            if (a.type == vmhd && a.end() - a.data() >= 12) {
                m.graphicsMode = Bytes.getShort(b, a.data() + 4);
                for (int i = 0; i < 3; i++) {
                    m.opColor[i] = Bytes.getShort(b, a.data() + 6 + i * 2);
                }
            } else if (a.type == smhd && a.end() - a.data() >= 6) {
                m.balance = Bytes.getShort(b, a.data() + 4);
            } else if (a.type == gmhd) {
                m.baseMediaInfo = Arrays.copyOfRange(b, a.data(), a.end());
            } else if (a.type == hdlr) {
                m.dataHandlerType = Bytes.getInt(b, a.data() + 8);
            }
        }
        Atom di = child(b, mi, dinf);
        Atom dr = di != null ? child(b, di, dref) : null;
        if (dr != null) {
            int n = Bytes.getInt(b, dr.data() + 4);
            List<Atom> entries = atoms(b, dr.data() + 8, dr.end());
            for (int i = 0; i < n && i < entries.size(); i++) {
                Atom e = entries.get(i);
                int flags = Bytes.getInt(b, e.data()) & 0xffffff;
                DataRefRec d = new DataRefRec(e.type, flags, Arrays.copyOfRange(b, e.data() + 4, e.end()));
                d.selfPath = selfPath;
                m.dataRefs.add(d);
            }
        }
        if (m.dataRefs.isEmpty()) {
            m.dataRefs.add(DataRefRec.self(selfPath));
        }
        Atom st = child(b, mi, stbl);
        int modification = m.modificationTime;
        if (st != null) {
            readStbl(b, st, m);
        }
        m.changed();
        m.modificationTime = modification;
    }

    private static String handlerName(byte[] b, Atom hd) {
        int p = hd.data() + 24;
        int end = hd.end();
        if (p >= end) {
            return "";
        }
        int len = b[p] & 0xff;
        if (len > 0 && p + 1 + len == end) {
            // QuickTime, pascal string
            return new String(b, p + 1, len, Bytes.MAC_ROMAN);
        }
        int e = p;
        while (e < end && b[e] != 0) {
            e++;
        }
        return new String(b, p, e - p, StandardCharsets.UTF_8);
    }

    /** @return the bytes of a QuickTime sound sample as numerator/denominator, null if not a frame based sound description */
    static long[] soundBytesPerSample(byte[] d) {
        if (d.length < 36) {
            return null;
        }
        int version = Bytes.getShort(d, 16);
        int channels = Bytes.getShort(d, 24) & 0xffff;
        int bits = Bytes.getShort(d, 26) & 0xffff;
        if (version == 1 && d.length >= 52) {
            long samplesPerPacket = Bytes.getInt(d, 36) & 0xffff_ffffL;
            long bytesPerFrame = Bytes.getInt(d, 44) & 0xffff_ffffL;
            if (samplesPerPacket > 0 && bytesPerFrame > 0) {
                return new long[] {bytesPerFrame, samplesPerPacket};
            }
        } else if (version == 2 && d.length >= 72) {
            long bytesPerPacket = Bytes.getInt(d, 64) & 0xffff_ffffL;
            long framesPerPacket = Bytes.getInt(d, 68) & 0xffff_ffffL;
            if (bytesPerPacket > 0 && framesPerPacket > 0) {
                return new long[] {bytesPerPacket, framesPerPacket};
            }
            return null;
        }
        if (Bytes.getInt(d, 4) == t("ima4")) {
            return new long[] {34L * channels, 64};
        }
        if (channels > 0 && bits > 0) {
            return new long[] {(long) channels * ((bits + 7) / 8), 1};
        }
        return null;
    }

    private static void readStbl(byte[] b, Atom st, MediaRec m) {
        Atom sd = child(b, st, stsd);
        if (sd != null) {
            int n = Bytes.getInt(b, sd.data() + 4);
            List<Atom> entries = atoms(b, sd.data() + 8, sd.end());
            for (int i = 0; i < n && i < entries.size(); i++) {
                Atom e = entries.get(i);
                m.descriptions.add(Arrays.copyOfRange(b, e.offset(), e.end()));
            }
        }
        Atom ts = child(b, st, stts);
        Atom cs = child(b, st, ctts);
        Atom ss = child(b, st, stss);
        Atom sc = child(b, st, stsc);
        Atom sz = child(b, st, stsz);
        Atom z2 = child(b, st, stz2);
        Atom co = child(b, st, stco);
        Atom c6 = child(b, st, co64);
        if (ts == null || sc == null || (sz == null && z2 == null) || (co == null && c6 == null)) {
            return;
        }
        // chunk offsets
        int chunkCount = Bytes.getInt(b, (co != null ? co : c6).data() + 4);
        long[] chunkOffsets = new long[chunkCount];
        for (int i = 0; i < chunkCount; i++) {
            chunkOffsets[i] = co != null ? Bytes.getInt(b, co.data() + 8 + i * 4) & 0xffff_ffffL : Bytes.getLong(b, c6.data() + 8 + i * 8);
        }
        // sample sizes
        int constSize;
        int sampleCount;
        int[] sizes = null;
        if (sz != null) {
            constSize = Bytes.getInt(b, sz.data() + 4);
            sampleCount = Bytes.getInt(b, sz.data() + 8);
            if (constSize == 0) {
                sizes = new int[sampleCount];
                for (int i = 0; i < sampleCount; i++) {
                    sizes[i] = Bytes.getInt(b, sz.data() + 12 + i * 4);
                }
            }
        } else {
            constSize = 0;
            int field = b[z2.data() + 7] & 0xff;
            sampleCount = Bytes.getInt(b, z2.data() + 8);
            sizes = new int[sampleCount];
            for (int i = 0; i < sampleCount; i++) {
                sizes[i] = switch (field) {
                    case 4 -> ((b[z2.data() + 12 + i / 2] & 0xff) >> ((i & 1) == 0 ? 4 : 0)) & 0x0f;
                    case 8 -> b[z2.data() + 12 + i] & 0xff;
                    default -> Bytes.getShort(b, z2.data() + 12 + i * 2) & 0xffff;
                };
            }
        }
        // durations, as (count, duration) runs
        int sttsCount = Bytes.getInt(b, ts.data() + 4);
        int[] sttsN = new int[sttsCount];
        int[] sttsD = new int[sttsCount];
        for (int i = 0; i < sttsCount; i++) {
            sttsN[i] = Bytes.getInt(b, ts.data() + 8 + i * 8);
            sttsD[i] = Bytes.getInt(b, ts.data() + 12 + i * 8);
        }
        int cttsCount = cs != null ? Bytes.getInt(b, cs.data() + 4) : 0;
        // sync samples
        int[] syncs = null;
        if (ss != null) {
            int n = Bytes.getInt(b, ss.data() + 4);
            syncs = new int[n];
            for (int i = 0; i < n; i++) {
                syncs[i] = Bytes.getInt(b, ss.data() + 8 + i * 4);
            }
        }
        int stscCount = Bytes.getInt(b, sc.data() + 4);

        int sttsI = 0, sttsLeft = sttsCount > 0 ? sttsN[0] : 0;
        int cttsI = 0, cttsLeft = cttsCount > 0 ? Bytes.getInt(b, cs.data() + 8) : 0;
        int syncI = 0;
        int sample = 0;
        for (int e = 0; e < stscCount; e++) {
            int firstChunk = Bytes.getInt(b, sc.data() + 8 + e * 12);
            int perChunk = Bytes.getInt(b, sc.data() + 12 + e * 12);
            int desc = Bytes.getInt(b, sc.data() + 16 + e * 12);
            int lastChunk = e + 1 < stscCount ? Bytes.getInt(b, sc.data() + 8 + (e + 1) * 12) - 1 : chunkCount;
            byte[] d = desc >= 1 && desc <= m.descriptions.size() ? m.descriptions.get(desc - 1) : null;
            int dataRefIndex = d != null && d.length >= 16 ? Bytes.getShort(d, 14) : 1;
            long[] soundBytes = m.isSound() && constSize == 1 && d != null ? soundBytesPerSample(d) : null;
            for (int c = firstChunk; c <= lastChunk && c <= chunkCount; c++) {
                long offset = chunkOffsets[c - 1];
                for (int k = 0; k < perChunk && sample < sampleCount; k++, sample++) {
                    while (sttsLeft == 0 && sttsI + 1 < sttsCount) {
                        sttsLeft = sttsN[++sttsI];
                    }
                    int duration = sttsCount > 0 ? sttsD[sttsI] : 1;
                    sttsLeft--;
                    int cts = 0;
                    if (cttsCount > 0) {
                        while (cttsLeft == 0 && cttsI + 1 < cttsCount) {
                            cttsI++;
                            cttsLeft = Bytes.getInt(b, cs.data() + 8 + cttsI * 8);
                        }
                        cts = Bytes.getInt(b, cs.data() + 12 + cttsI * 8);
                        cttsLeft--;
                    }
                    boolean sync = true;
                    if (syncs != null) {
                        while (syncI < syncs.length && syncs[syncI] < sample + 1) {
                            syncI++;
                        }
                        sync = syncI < syncs.length && syncs[syncI] == sample + 1;
                    }
                    int size = sizes != null ? sizes[sample] : constSize;
                    if (soundBytes != null) {
                        // the rest of the chunk at once, QuickTime sound frames
                        int n = Math.min(perChunk - k, sampleCount - sample);
                        long bytes = n * soundBytes[0] / soundBytes[1];
                        m.addSamples(dataRefIndex, offset, size, bytes, duration, n, desc, sync, cts);
                        offset += bytes;
                        sample += n - 1;
                        sttsLeft -= n - 1;
                        // stts of sound is one entry in practice, keep it simple
                        while (sttsLeft < 0 && sttsI + 1 < sttsCount) {
                            sttsLeft += sttsN[++sttsI];
                        }
                        break;
                    }
                    m.addSamples(dataRefIndex, offset, size, size, duration, 1, desc, sync, cts);
                    offset += size;
                }
            }
        }
    }

    // ---- writing

    /** atom builder */
    static final class Out {
        private final ByteArrayOutputStream os = new ByteArrayOutputStream();
        private final List<Integer> starts = new ArrayList<>();
        private byte[] tmp = new byte[8];

        Out begin(int type) {
            starts.add(os.size());
            i32(0);
            i32(type);
            return this;
        }

        Out end() {
            int start = starts.removeLast();
            pending.add(new int[] {start, os.size() - start});
            return this;
        }

        private final List<int[]> pending = new ArrayList<>();

        Out fullBegin(int type, int version, int flags) {
            begin(type);
            i32((version << 24) | (flags & 0xffffff));
            return this;
        }

        Out i8(int v) {
            os.write(v);
            return this;
        }

        Out i16(int v) {
            os.write(v >> 8);
            os.write(v);
            return this;
        }

        Out i32(int v) {
            Bytes.putInt(tmp, 0, v);
            os.write(tmp, 0, 4);
            return this;
        }

        Out i64(long v) {
            Bytes.putLong(tmp, 0, v);
            os.write(tmp, 0, 8);
            return this;
        }

        Out bytes(byte[] b) {
            os.write(b, 0, b.length);
            return this;
        }

        Out zeros(int n) {
            for (int i = 0; i < n; i++) {
                os.write(0);
            }
            return this;
        }

        Out atom(int type, byte[] data) {
            return begin(type).bytes(data).end();
        }

        int size() {
            return os.size();
        }

        byte[] toByteArray() {
            byte[] b = os.toByteArray();
            for (int[] p : pending) {
                Bytes.putInt(b, p[0], p[1]);
            }
            return b;
        }
    }

    /** @return the 'moov' atom of the movie */
    static byte[] write(MovieRec movie) {
        return write(movie, false);
    }

    /**
     * @param iso true for the ISO base media file format (MPEG-4, 3GPP): handlers have no component type,
     *            names are C strings and self references are 'url ' entries
     * @return the 'moov' atom of the movie
     */
    static byte[] write(MovieRec movie, boolean iso) {
        Out o = new Out();
        o.begin(moov);
        writeMvhd(o, movie);
        for (TrackRec t : movie.tracks) {
            writeTrak(o, movie, t, iso);
        }
        if (!movie.userData.items.isEmpty()) {
            o.atom(udta, movie.userData.toBytes());
        }
        o.end();
        return o.toByteArray();
    }

    private static void writeMvhd(Out o, MovieRec m) {
        long duration = m.duration();
        boolean v1 = duration > 0xffff_ffffL;
        o.fullBegin(mvhd, v1 ? 1 : 0, 0);
        if (v1) {
            o.i64(m.creationTime & 0xffff_ffffL).i64(m.modificationTime & 0xffff_ffffL).i32(m.timeScale).i64(duration);
        } else {
            o.i32(m.creationTime).i32(m.modificationTime).i32(m.timeScale).i32((int) duration);
        }
        o.i32(m.preferredRate).i16(m.preferredVolume).zeros(10);
        for (int v : m.matrix) {
            o.i32(v);
        }
        o.i32(m.previewTime).i32(m.previewDuration).i32(m.posterTime).i32(m.selectionTime).i32(m.selectionDuration)
                .i32((int) m.time()).i32(m.nextTrackID);
        o.end();
    }

    private static void writeTrak(Out o, MovieRec movie, TrackRec t, boolean iso) {
        o.begin(trak);
        long duration = t.duration();
        boolean v1 = duration > 0xffff_ffffL;
        o.fullBegin(tkhd, v1 ? 1 : 0, (t.enabled ? TrackRec.trackEnabled : 0) | t.usage);
        if (v1) {
            o.i64(t.creationTime & 0xffff_ffffL).i64(t.modificationTime & 0xffff_ffffL).i32(t.id).i32(0).i64(duration);
        } else {
            o.i32(t.creationTime).i32(t.modificationTime).i32(t.id).i32(0).i32((int) duration);
        }
        short volume = iso && (t.media == null || !t.media.isSound()) ? 0 : t.volume;
        o.zeros(8).i16(t.layer).i16(t.alternateGroup).i16(volume).i16(0);
        for (int v : t.matrix) {
            o.i32(v);
        }
        o.i32(t.width).i32(t.height);
        o.end();
        if (!t.references.isEmpty()) {
            o.begin(tref);
            t.references.forEach((type, ids) -> {
                o.begin(type);
                for (int id : ids) {
                    o.i32(id);
                }
                o.end();
            });
            o.end();
        }
        if (!t.edits.isEmpty()) {
            boolean e1 = false;
            for (TrackRec.Edit e : t.edits) {
                e1 |= e.duration > 0xffff_ffffL || e.mediaTime > Integer.MAX_VALUE;
            }
            o.begin(edts).fullBegin(elst, e1 ? 1 : 0, 0).i32(t.edits.size());
            for (TrackRec.Edit e : t.edits) {
                if (e1) {
                    o.i64(e.duration).i64(e.mediaTime);
                } else {
                    o.i32((int) e.duration).i32((int) e.mediaTime);
                }
                o.i32(e.rate);
            }
            o.end().end();
        }
        if (t.preloadTime != -1 || t.preloadFlags != 0 || t.defaultHints != 0) {
            o.begin(load).i32(t.preloadTime).i32(t.preloadDuration).i32(t.preloadFlags).i32(t.defaultHints).end();
        }
        if (t.media != null) {
            writeMdia(o, t.media, iso);
        }
        if (!t.userData.items.isEmpty()) {
            o.atom(udta, t.userData.toBytes());
        }
        o.end();
    }

    private static void writeMdia(Out o, MediaRec m, boolean iso) {
        o.begin(mdia);
        long duration = m.duration();
        boolean v1 = duration > 0xffff_ffffL;
        o.fullBegin(mdhd, v1 ? 1 : 0, 0);
        if (v1) {
            o.i64(m.creationTime & 0xffff_ffffL).i64(m.modificationTime & 0xffff_ffffL).i32(m.timeScale).i64(duration);
        } else {
            o.i32(m.creationTime).i32(m.modificationTime).i32(m.timeScale).i32((int) duration);
        }
        o.i16(m.language).i16(m.quality).end();
        if (iso) {
            byte[] name = m.handlerName.getBytes(StandardCharsets.UTF_8);
            o.fullBegin(hdlr, 0, 0).i32(0).i32(m.type).i32(0).i32(0).i32(0).bytes(name).i8(0).end();
        } else {
            byte[] name = m.handlerName.getBytes(Bytes.MAC_ROMAN);
            o.fullBegin(hdlr, 0, 0).i32(mhlr).i32(m.type).i32(m.handlerManufacturer).i32(0).i32(0)
                    .i8(Math.min(255, name.length)).bytes(Arrays.copyOf(name, Math.min(255, name.length))).end();
        }
        o.begin(minf);
        if (m.type == vide) {
            o.fullBegin(vmhd, 0, 1).i16(m.graphicsMode).i16(m.opColor[0]).i16(m.opColor[1]).i16(m.opColor[2]).end();
        } else if (m.type == soun) {
            o.fullBegin(smhd, 0, 0).i16(m.balance).i16(0).end();
        } else if (m.baseMediaInfo != null) {
            o.atom(gmhd, m.baseMediaInfo);
        } else {
            o.begin(gmhd).fullBegin(gmin, 0, 0).i16(m.graphicsMode).i16(m.opColor[0]).i16(m.opColor[1]).i16(m.opColor[2])
                    .i16(m.balance).i16(0).end().end();
        }
        if (!iso) {
            o.fullBegin(hdlr, 0, 0).i32(dhlr).i32(m.dataHandlerType).i32(Bytes.fourCC("appl")).i32(0).i32(0)
                    .i8(0).end();
        }
        o.begin(dinf).fullBegin(dref, 0, 0).i32(m.dataRefs.size());
        for (DataRefRec d : m.dataRefs) {
            if (iso && (d.flags & DataRefRec.selfReference) != 0) {
                o.fullBegin(DataRefRec.url, 0, DataRefRec.selfReference).end();
            } else {
                o.fullBegin(d.type, 0, d.flags).bytes(d.entryData()).end();
            }
        }
        o.end().end();
        writeStbl(o, m);
        o.end();
        o.end();
    }

    private static void writeStbl(Out o, MediaRec m) {
        o.begin(stbl);
        o.fullBegin(stsd, 0, 0).i32(m.descriptions.size());
        for (byte[] d : m.descriptions) {
            o.bytes(d);
        }
        o.end();
        List<MediaRec.SampleRun> runs = new ArrayList<>();
        for (MediaRec.SampleRun r : m.runs) {
            if (r.count > 0) {
                runs.add(r);
            }
        }
        // stts
        List<long[]> tts = new ArrayList<>();
        for (MediaRec.SampleRun r : runs) {
            if (!tts.isEmpty() && tts.getLast()[1] == r.duration) {
                tts.getLast()[0] += r.count;
            } else {
                tts.add(new long[] {r.count, r.duration});
            }
        }
        o.fullBegin(stts, 0, 0).i32(tts.size());
        for (long[] e : tts) {
            o.i32((int) e[0]).i32((int) e[1]);
        }
        o.end();
        // ctts
        boolean anyCts = false;
        for (MediaRec.SampleRun r : runs) {
            anyCts |= r.ctsOffset != 0;
        }
        if (anyCts) {
            List<long[]> cts = new ArrayList<>();
            for (MediaRec.SampleRun r : runs) {
                if (!cts.isEmpty() && cts.getLast()[1] == r.ctsOffset) {
                    cts.getLast()[0] += r.count;
                } else {
                    cts.add(new long[] {r.count, r.ctsOffset});
                }
            }
            o.fullBegin(ctts, 0, 0).i32(cts.size());
            for (long[] e : cts) {
                o.i32((int) e[0]).i32((int) e[1]);
            }
            o.end();
        }
        // stss
        boolean allSync = true;
        for (MediaRec.SampleRun r : runs) {
            allSync &= r.sync;
        }
        if (!allSync) {
            List<Long> syncs = new ArrayList<>();
            long s = 1;
            for (MediaRec.SampleRun r : runs) {
                if (r.sync) {
                    for (int i = 0; i < r.count; i++) {
                        syncs.add(s + i);
                    }
                }
                s += r.count;
            }
            o.fullBegin(stss, 0, 0).i32(syncs.size());
            for (long v : syncs) {
                o.i32((int) v);
            }
            o.end();
        }
        // stsc, a chunk per run
        List<int[]> sc = new ArrayList<>();
        for (int i = 0; i < runs.size(); i++) {
            MediaRec.SampleRun r = runs.get(i);
            if (sc.isEmpty() || sc.getLast()[1] != r.count || sc.getLast()[2] != r.descIndex) {
                sc.add(new int[] {i + 1, r.count, r.descIndex});
            }
        }
        o.fullBegin(stsc, 0, 0).i32(sc.size());
        for (int[] e : sc) {
            o.i32(e[0]).i32(e[1]).i32(e[2]);
        }
        o.end();
        // stsz
        int constSize = runs.isEmpty() ? 0 : runs.getFirst().size;
        long total = 0;
        for (MediaRec.SampleRun r : runs) {
            if (r.size != constSize) {
                constSize = 0;
            }
            total += r.count;
        }
        o.fullBegin(stsz, 0, 0).i32(constSize).i32((int) total);
        if (constSize == 0) {
            for (MediaRec.SampleRun r : runs) {
                for (int i = 0; i < r.count; i++) {
                    o.i32(r.size);
                }
            }
        }
        o.end();
        // stco / co64
        boolean large = false;
        for (MediaRec.SampleRun r : runs) {
            large |= r.offset + r.bytes > 0xffff_ffffL;
        }
        o.fullBegin(large ? co64 : stco, 0, 0).i32(runs.size());
        for (MediaRec.SampleRun r : runs) {
            if (large) {
                o.i64(r.offset);
            } else {
                o.i32((int) r.offset);
            }
        }
        o.end();
        o.end();
    }
}
