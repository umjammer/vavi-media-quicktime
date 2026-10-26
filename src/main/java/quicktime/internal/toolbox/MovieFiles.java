/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.Deflater;


/**
 * Movie files: reading and writing the movie resource ('moov') and flattening.
 */
final class MovieFiles {

    private MovieFiles() {
    }

    static final int flattenAddMovieToDataFork = 1;
    static final int flattenActiveTracksOnly = 4;
    static final int flattenDontInterleaveFlatten = 8;
    static final int flattenCompressMovieResource = 32;
    static final int flattenForceMovieResourceBeforeMovieData = 64;

    /** progress callback, return non 0 to abort */
    interface Progress {
        int progress(int message, int operation, int percentFixed);
    }

    static final Progress NO_PROGRESS = (m, o, p) -> 0;

    /** @return a progress reporter calling the movie's progress proc */
    static Progress progressOf(MovieRec m) {
        if (m.progressProc == 0 || m.progressProc == -1) {
            return NO_PROGRESS;
        }
        return (message, operation, percent) -> Closures.callInt(m.progressProc, m.ref, message, operation, percent, m.progressRefCon);
    }

    /** writes the movie atom into the movie file, an existing one is replaced */
    static void writeMovieResource(MovieRec m, String path) throws IOException {
        MdatWriter.close(path);
        DataRefRec.closeReader(path);
        // sample data written into the movie file are self references now
        for (TrackRec t : m.tracks) {
            if (t.media != null) {
                for (DataRefRec d : t.media.dataRefs) {
                    if (d.type == DataRefRec.alis && (d.flags & DataRefRec.selfReference) == 0 && path.equals(d.path())) {
                        d.flags |= DataRefRec.selfReference;
                        d.selfPath = path;
                    }
                }
            }
        }
        byte[] moov = MovieAtoms.write(m, false, path);
        try (RandomAccessFile f = new RandomAccessFile(path, "rw")) {
            if (m.path != null && m.path.equals(path) && m.moovOffset >= 0 && m.moovOffset + m.moovSize <= f.length()) {
                long free = m.moovSize - moov.length;
                if (free == 0 || free >= 8) {
                    f.seek(m.moovOffset);
                    f.write(moov);
                    if (free >= 8) {
                        byte[] h = new byte[8];
                        Bytes.putInt(h, 0, (int) free);
                        Bytes.putInt(h, 4, MovieAtoms.free);
                        f.write(h);
                    }
                    m.moovSize = moov.length;
                    return;
                }
                // the old one becomes free space
                f.seek(m.moovOffset + 4);
                f.writeInt(MovieAtoms.free);
            }
            long at = f.length();
            f.seek(at);
            f.write(moov);
            m.moovOffset = at;
            m.moovSize = moov.length;
            m.path = path;
        }
        m.changed = false;
    }

    /** @return a 'cmov' compressed movie atom */
    static byte[] compress(byte[] moov) {
        Deflater deflater = new Deflater();
        deflater.setInput(moov);
        deflater.finish();
        byte[] buf = new byte[moov.length + 64];
        int n = deflater.deflate(buf);
        deflater.end();
        MovieAtoms.Out o = new MovieAtoms.Out();
        o.begin(MovieAtoms.moov).begin(MovieAtoms.cmov);
        o.begin(MovieAtoms.dcom).i32(MovieAtoms.zlib).end();
        o.begin(MovieAtoms.cmvd).i32(moov.length).bytes(java.util.Arrays.copyOf(buf, n)).end();
        o.end().end();
        return o.toByteArray();
    }

    // ---- flatten

    /** a piece of media data to copy */
    private record Chunk(MediaRec media, MediaRec.SampleRun run, double time, long sourceOffset) {
    }

    /** compacts the media of each track to the samples its edits use, returns the chunks to copy */
    private static List<Chunk> plan(MovieRec out, boolean interleave) {
        List<Chunk> chunks = new ArrayList<>();
        for (TrackRec t : out.tracks) {
            MediaRec m = t.media;
            if (m == null) {
                continue;
            }
            // used sample ranges, in media time
            List<long[]> used = new ArrayList<>();
            for (TrackRec.Edit e : t.edits) {
                if (!e.isEmpty()) {
                    used.add(new long[] {e.mediaTime, e.mediaTime + Math.max(1, t.mediaDuration(e.duration, e.rate))});
                }
            }
            MediaRec n = new MediaRec(t, m.type, m.timeScale);
            n.userData.dispose();
            n.userData = m.userData.copy();
            n.language = m.language;
            n.quality = m.quality;
            n.handlerManufacturer = m.handlerManufacturer;
            n.handlerName = m.handlerName;
            n.graphicsMode = m.graphicsMode;
            n.opColor = m.opColor;
            n.balance = m.balance;
            n.baseMediaInfo = m.baseMediaInfo;
            n.inputMap = m.inputMap;
            n.propertyAtom = m.propertyAtom;
            n.creationTime = m.creationTime;
            used.sort(Comparator.comparingLong(a -> a[0]));
            // map edits to the new media
            List<long[]> mapping = new ArrayList<>(); // old start, new start, length
            long lastEnd = Long.MIN_VALUE;
            for (long[] u : used) {
                if (u[0] < lastEnd) {
                    // overlapping ranges, copy the rest only
                    if (u[1] <= lastEnd) {
                        continue;
                    }
                    u[0] = lastEnd;
                }
                long newStart = MovieEdits.appendMediaRange(n, m, u[0], u[1] - u[0]);
                mapping.add(new long[] {u[0], newStart, u[1] - u[0]});
                lastEnd = u[1];
            }
            for (TrackRec.Edit e : t.edits) {
                if (e.isEmpty()) {
                    continue;
                }
                for (long[] mp : mapping) {
                    if (e.mediaTime >= mp[0] && e.mediaTime < mp[0] + Math.max(1, mp[2])) {
                        e.mediaTime = mp[1] + (e.mediaTime - mp[0]);
                        break;
                    }
                }
            }
            t.media = n;
            m.track = null;
            m.dispose();
            // split runs into chunks of about half a second for interleaving
            List<MediaRec.SampleRun> split = new ArrayList<>();
            long maxDur = interleave ? Math.max(1, n.timeScale / 2) : Long.MAX_VALUE;
            for (MediaRec.SampleRun r : n.runs) {
                long per = r.duration == 0 ? r.count : Math.max(1, maxDur / Math.max(1, r.duration));
                for (long k = 0; k < r.count; k += per) {
                    int c = (int) Math.min(per, r.count - k);
                    split.add(new MediaRec.SampleRun(r.dataRefIndex, r.offsetOf(k), r.size, r.bytesOf(k, c), r.duration, c, r.descIndex, r.sync, r.ctsOffset));
                }
            }
            n.runs.clear();
            n.runs.addAll(split);
            n.changed();
            for (int i = 0; i < n.runs.size(); i++) {
                MediaRec.SampleRun r = n.runs.get(i);
                double time = n.firstTimeOf(i) / (double) n.timeScale;
                chunks.add(new Chunk(n, r, interleave ? time : t.id * 1e12 + time, r.offset));
            }
        }
        chunks.sort(Comparator.comparingDouble(Chunk::time));
        return chunks;
    }

    /**
     * Flattens the movie into a self contained file.
     * @param fileType brands for the 'ftyp' atom (major first), null for a QuickTime movie without one
     * @return the flattened movie, which references the new file
     */
    static MovieRec flatten(MovieRec src, String path, int flags, int[] fileType, Progress progress) throws IOException {
        return flatten(MovieEdits.copy(src), path, flags, fileType, false, progress);
    }

    /**
     * Flattens a copy of a movie, the copy is consumed.
     * @param iso true to write the ISO base media file format
     */
    static MovieRec flatten(MovieRec out, String path, int flags, int[] fileType, boolean iso, Progress progress) throws IOException {
        if ((flags & flattenActiveTracksOnly) != 0) {
            for (TrackRec t : new ArrayList<>(out.tracks)) {
                if (!t.enabled) {
                    out.removeTrack(t);
                }
            }
        }
        progress.progress(0, 1, 0);
        try {
            List<Chunk> chunks = plan(out, (flags & flattenDontInterleaveFlatten) == 0);
            long dataSize = 0;
            for (Chunk c : chunks) {
                dataSize += c.run.bytes;
            }
            DataRefRec.closeReader(path);
            MdatWriter.close(path);
            File file = new File(path);
            if (file.exists() && !file.delete()) {
                throw new IOException("cannot replace " + path);
            }
            byte[] ftyp = fileType == null ? new byte[0] : ftyp(fileType);
            boolean moovFirst = (flags & flattenForceMovieResourceBeforeMovieData) != 0;
            // sample offsets relative to the data start
            long rel = 0;
            for (Chunk c : chunks) {
                c.run.offset = rel;
                rel += c.run.bytes;
            }
            boolean largeMdat = dataSize + 16 > 0xffff_ffffL;
            int mdatHeader = 16;
            long base;
            byte[] moov;
            // the data references to read from, the flattened media reference the new file only
            List<List<DataRefRec>> sources = new ArrayList<>();
            for (TrackRec t : out.tracks) {
                sources.add(t.media == null ? List.of() : new ArrayList<>(t.media.dataRefs));
                if (t.media != null) {
                    t.media.dataRefs.clear();
                    t.media.dataRefs.add(DataRefRec.self(path));
                    for (byte[] d : t.media.descriptions) {
                        Bytes.putShort(d, 14, 1);
                    }
                }
            }
            if (moovFirst) {
                // offsets do not change the size of the movie atom, except stco vs co64
                byte[] probe = moovBytes(out, flags, iso, path);
                base = ftyp.length + probe.length + mdatHeader;
                shift(chunks, base);
                moov = moovBytes(out, flags, iso, path);
                if (moov.length != probe.length) {
                    shift(chunks, -base);
                    base = ftyp.length + moov.length + mdatHeader;
                    shift(chunks, base);
                    moov = moovBytes(out, flags, iso, path);
                }
            } else {
                base = ftyp.length + mdatHeader;
                shift(chunks, base);
                moov = null;
            }
            try (RandomAccessFile f = new RandomAccessFile(path, "rw")) {
                f.write(ftyp);
                if (moovFirst) {
                    f.write(moov);
                }
                byte[] h = new byte[16];
                if (largeMdat) {
                    Bytes.putInt(h, 0, 1);
                    Bytes.putInt(h, 4, MovieAtoms.mdat);
                    Bytes.putLong(h, 8, dataSize + 16);
                } else {
                    Bytes.putInt(h, 0, 8);
                    Bytes.putInt(h, 4, MovieAtoms.wide);
                    Bytes.putInt(h, 8, (int) (dataSize + 8));
                    Bytes.putInt(h, 12, MovieAtoms.mdat);
                }
                f.write(h);
                long done = 0;
                byte[] buf = new byte[1 << 16];
                for (Chunk c : chunks) {
                    int ti = out.tracks.indexOf(c.media.track);
                    DataRefRec from = sources.get(ti).get(c.run.dataRefIndex - 1);
                    long remaining = c.run.bytes;
                    long so = c.sourceOffset;
                    while (remaining > 0) {
                        int n = (int) Math.min(buf.length, remaining);
                        from.read(so, buf, 0, n);
                        f.write(buf, 0, n);
                        so += n;
                        remaining -= n;
                    }
                    c.run.dataRefIndex = 1;
                    done += c.run.bytes;
                    if (progress.progress(1, 1, (int) (done * 0x10000L / Math.max(1, dataSize))) != 0) {
                        throw new IOException("aborted");
                    }
                }
                if (!moovFirst) {
                    moov = moovBytes(out, flags, iso, path);
                    f.write(moov);
                }
                out.moovOffset = moovFirst ? ftyp.length : f.length() - moov.length;
                out.moovSize = moov.length;
            }
            out.path = path;
            for (TrackRec t : out.tracks) {
                if (t.media != null) {
                    t.media.changed();
                }
            }
            out.changed = false;
            return out;
        } catch (IOException | RuntimeException e) {
            out.dispose();
            throw e;
        } finally {
            progress.progress(2, 1, 0x10000);
        }
    }

    private static byte[] moovBytes(MovieRec m, int flags, boolean iso, String path) {
        byte[] moov = MovieAtoms.write(m, iso, path);
        return (flags & flattenCompressMovieResource) != 0 ? compress(moov) : moov;
    }

    private static void shift(List<Chunk> chunks, long delta) {
        for (Chunk c : chunks) {
            c.run.offset += delta;
        }
    }

    /** @return an 'ftyp' atom, major brand first */
    static byte[] ftyp(int[] brands) {
        MovieAtoms.Out o = new MovieAtoms.Out();
        o.begin(MovieAtoms.ftyp).i32(brands[0]).i32(0);
        for (int b : brands) {
            o.i32(b);
        }
        o.end();
        return o.toByteArray();
    }
}
