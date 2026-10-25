/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Editing between tracks and movies: segments, selections, the scrap and edit states.
 * Media samples are never copied, a destination media gets references to the source data.
 */
final class MovieEdits {

    private MovieEdits() {
    }

    /**
     * Appends the samples of the source media covering the media time range to the destination media.
     * @return the destination media time corresponding to srcStart
     */
    static long appendMediaRange(MediaRec dst, MediaRec src, long srcStart, long srcDuration) {
        long first = src.sampleAt(Math.max(0, srcStart));
        if (first == 0) {
            return dst.duration();
        }
        long last = srcDuration > 0 ? src.sampleAt(Math.min(src.duration() - 1, srcStart + srcDuration - 1)) : first;
        if (last == 0) {
            last = src.sampleCount();
        }
        // video needs the preceding sync sample to decode
        if (src.isVisual()) {
            long sync = src.syncSampleAtOrBefore(first);
            if (sync > 0) {
                first = sync;
            }
        }
        long firstTime = src.timeOfSample(first);
        long dstStart = dst.duration();
        Map<Integer, Integer> drefs = new HashMap<>();
        Map<Integer, Integer> descs = new HashMap<>();
        boolean sameScale = dst.timeScale == src.timeScale;
        for (long s = first; s <= last; ) {
            int r = src.runOfSample(s);
            MediaRec.SampleRun run = src.runs.get(r);
            long k = s - src.firstSampleOf(r);
            long n = Math.min(run.count - k, last - s + 1);
            int dref = drefs.computeIfAbsent(run.dataRefIndex, i -> dst.dataRefIndex(src.dataRef(i).copy()));
            int desc = descs.computeIfAbsent(run.descIndex, i -> {
                byte[] d = src.descriptions.get(i - 1).clone();
                Bytes.putShort(d, 14, dref);
                return dst.descriptionIndex(d);
            });
            int duration = sameScale ? run.duration : (int) ClockManager.convert(run.duration, src.timeScale, dst.timeScale);
            dst.addSamples(dref, run.offsetOf(k), run.size, run.bytesOf(k, n), duration, (int) n, desc, run.sync, run.ctsOffset);
            s += n;
        }
        long offset = srcStart - firstTime;
        return dstStart + (sameScale ? offset : ClockManager.convert(offset, src.timeScale, dst.timeScale));
    }

    /** InsertTrackSegment */
    static void insertTrackSegment(TrackRec src, TrackRec dst, long srcIn, long srcDuration, long dstIn) {
        List<TrackRec.Edit> edits = src.copyEdits(srcIn, srcDuration);
        int srcScale = src.movieScale();
        int dstScale = dst.movieScale();
        List<TrackRec.Edit> inserted = new ArrayList<>();
        for (TrackRec.Edit e : edits) {
            long duration = ClockManager.convert(e.duration, srcScale, dstScale);
            if (e.isEmpty() || src.media == null) {
                inserted.add(new TrackRec.Edit(duration, -1, 0x10000));
            } else if (dst.media == src.media) {
                inserted.add(new TrackRec.Edit(duration, e.mediaTime, e.rate));
            } else {
                if (dst.media == null) {
                    dst.media = new MediaRec(dst, src.media.type, src.media.timeScale);
                    dst.media.language = src.media.language;
                    dst.media.handlerName = src.media.handlerName;
                    dst.media.handlerManufacturer = src.media.handlerManufacturer;
                }
                long mediaDuration = src.mediaDuration(e.duration, e.rate);
                long mt = appendMediaRange(dst.media, src.media, e.mediaTime, mediaDuration);
                inserted.add(new TrackRec.Edit(duration, mt, e.rate));
            }
        }
        if (dst.width == 0 && dst.height == 0) {
            dst.width = src.width;
            dst.height = src.height;
        }
        dst.insertEdits(dstIn, inserted);
    }

    /** @return the i-th (0 based) track of the media type in the movie */
    private static TrackRec nthOfType(MovieRec m, int type, int n, List<TrackRec> exclude) {
        int i = 0;
        for (TrackRec t : m.tracks) {
            if (t.media != null && t.media.type == type && !exclude.contains(t)) {
                if (i++ == n) {
                    return t;
                }
            }
        }
        return null;
    }

    /** InsertMovieSegment, all destination tracks get the time inserted */
    static void insertMovieSegment(MovieRec src, MovieRec dst, long srcIn, long srcDuration, long dstIn) {
        long dstDuration = ClockManager.convert(srcDuration, src.timeScale, dst.timeScale);
        List<TrackRec> used = new ArrayList<>();
        Map<Integer, Integer> perType = new HashMap<>();
        for (TrackRec s : new ArrayList<>(src.tracks)) {
            if (s.media == null) {
                continue;
            }
            int n = perType.merge(s.media.type, 1, Integer::sum) - 1;
            TrackRec d = nthOfType(dst, s.media.type, n, used);
            if (d == null) {
                d = dst.newTrack();
                s.copySettings(d);
                if (dstIn > 0) {
                    d.insertEmpty(0, dstIn);
                }
            }
            used.add(d);
            insertTrackSegment(s, d, srcIn, srcDuration, dstIn);
        }
        for (TrackRec d : dst.tracks) {
            if (!used.contains(d) && d.duration() > dstIn) {
                d.insertEmpty(dstIn, dstDuration);
            }
        }
        dst.changed = true;
        dst.updateTimeBase();
    }

    /** @return a new movie with the movie time range */
    static MovieRec copyRange(MovieRec m, long start, long duration) {
        MovieRec c = new MovieRec();
        copySettings(m, c);
        for (TrackRec t : m.tracks) {
            TrackRec n = c.newTrack();
            n.id = t.id;
            t.copySettings(n);
            if (t.media != null) {
                n.media = t.media.copy(n);
            }
            n.userData.dispose();
            n.userData = t.userData.copy();
            n.userData.owned = true;
            t.references.forEach((k, v) -> n.references.put(k, new ArrayList<>(v)));
            for (TrackRec.Edit e : t.copyEdits(start, duration)) {
                n.edits.add(e);
            }
            n.normalize();
            while (!n.edits.isEmpty() && n.edits.getLast().isEmpty()) {
                n.edits.removeLast();
            }
        }
        c.nextTrackID = m.nextTrackID;
        c.updateTimeBase();
        c.changed = false;
        return c;
    }

    /** @return a full copy of the movie */
    static MovieRec copy(MovieRec m) {
        MovieRec c = copyRange(m, 0, Math.max(m.duration(), 1));
        c.posterTime = m.posterTime;
        c.previewTime = m.previewTime;
        c.previewDuration = m.previewDuration;
        c.selectionTime = m.selectionTime;
        c.selectionDuration = m.selectionDuration;
        c.path = m.path;
        return c;
    }

    static void copySettings(MovieRec m, MovieRec c) {
        c.timeScale = m.timeScale;
        c.timeBase.scale = m.timeScale;
        c.preferredRate = m.preferredRate;
        c.preferredVolume = m.preferredVolume;
        c.volume = m.volume;
        c.matrix = m.matrix.clone();
        c.language = m.language;
        c.userData.dispose();
        c.userData = m.userData.copy();
        c.userData.owned = true;
        c.defaultDataRef = m.defaultDataRef == null ? null : m.defaultDataRef.copy();
    }

    static void deleteSegment(MovieRec m, long start, long duration) {
        for (TrackRec t : m.tracks) {
            t.delete(start, duration);
        }
        m.changed = true;
        m.updateTimeBase();
    }

    static void insertEmptySegment(MovieRec m, long at, long duration) {
        for (TrackRec t : m.tracks) {
            if (t.duration() > at) {
                t.insertEmpty(at, duration);
            }
        }
        m.changed = true;
        m.updateTimeBase();
    }

    static void scaleSegment(MovieRec m, long start, long oldDuration, long newDuration) {
        for (TrackRec t : m.tracks) {
            t.scale(start, oldDuration, newDuration);
        }
        m.changed = true;
        m.updateTimeBase();
    }

    // ---- selection

    static MovieRec copySelection(MovieRec m) {
        return copyRange(m, m.selectionTime, m.selectionDuration);
    }

    static MovieRec cutSelection(MovieRec m) {
        MovieRec c = copySelection(m);
        clearSelection(m);
        return c;
    }

    static void clearSelection(MovieRec m) {
        deleteSegment(m, m.selectionTime, m.selectionDuration);
        m.selectionDuration = 0;
    }

    static void pasteSelection(MovieRec m, MovieRec src) {
        if (m.selectionDuration > 0) {
            deleteSegment(m, m.selectionTime, m.selectionDuration);
        }
        long d = src.duration();
        insertMovieSegment(src, m, 0, d, m.selectionTime);
        m.selectionDuration = (int) ClockManager.convert(d, src.timeScale, m.timeScale);
    }

    static void addSelection(MovieRec m, MovieRec src) {
        long srcDuration = src.duration();
        for (TrackRec s : src.tracks) {
            TrackRec d = m.newTrack();
            s.copySettings(d);
            if (m.selectionTime > 0) {
                d.insertEmpty(0, m.selectionTime);
            }
            insertTrackSegment(s, d, 0, srcDuration, m.selectionTime);
            if (m.selectionDuration > 0) {
                d.scale(m.selectionTime, ClockManager.convert(srcDuration, src.timeScale, m.timeScale), m.selectionDuration);
            }
        }
        m.changed = true;
        m.updateTimeBase();
    }

    // ---- scrap

    private static MovieRec scrap;

    static synchronized void putOnScrap(MovieRec m) {
        if (scrap != null) {
            scrap.dispose();
        }
        scrap = copy(m);
    }

    static synchronized MovieRec fromScrap() {
        return scrap == null ? null : copy(scrap);
    }

    static synchronized boolean hasScrap() {
        return scrap != null;
    }

    // ---- edit states

    /** a snapshot of the edits of a movie */
    static final class MovieEditState {
        final int ref;
        final MovieRec movie;
        final MovieRec snapshot;

        MovieEditState(MovieRec movie) {
            this.ref = Heap.newObject(this, 4);
            this.movie = movie;
            this.snapshot = copy(movie);
        }

        void dispose() {
            snapshot.dispose();
            Heap.free(ref);
        }
    }

    static void useMovieEditState(MovieRec m, MovieEditState state) {
        for (TrackRec t : new ArrayList<>(m.tracks)) {
            m.tracks.remove(t);
            t.dispose();
        }
        MovieRec s = copy(state.snapshot);
        for (TrackRec t : new ArrayList<>(s.tracks)) {
            s.tracks.remove(t);
            t.movie = m;
            m.tracks.add(t);
        }
        m.selectionTime = s.selectionTime;
        m.selectionDuration = s.selectionDuration;
        m.nextTrackID = Math.max(m.nextTrackID, s.nextTrackID);
        s.dispose();
        m.changed = true;
        m.updateTimeBase();
    }

    /** a snapshot of the edits of a track */
    static final class TrackEditState {
        final int ref;
        final TrackRec track;
        final List<TrackRec.Edit> edits = new ArrayList<>();

        TrackEditState(TrackRec track) {
            this.ref = Heap.newObject(this, 4);
            this.track = track;
            for (TrackRec.Edit e : track.edits) {
                edits.add(e.copy());
            }
        }

        void dispose() {
            Heap.free(ref);
        }
    }

    static void useTrackEditState(TrackRec t, TrackEditState state) {
        t.edits.clear();
        for (TrackRec.Edit e : state.edits) {
            t.edits.add(e.copy());
        }
        t.changed();
        t.movie.updateTimeBase();
    }
}
