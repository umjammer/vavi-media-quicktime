/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A track: its edit list maps track time (in the movie time scale) to media time.
 */
final class TrackRec {

    /** an edit list entry */
    static final class Edit {
        /** in the movie time scale */
        long duration;
        /** -1 for an empty edit */
        long mediaTime;
        /** 16.16 fixed */
        int rate;

        Edit(long duration, long mediaTime, int rate) {
            this.duration = duration;
            this.mediaTime = mediaTime;
            this.rate = rate;
        }

        Edit copy() {
            return new Edit(duration, mediaTime, rate);
        }

        boolean isEmpty() {
            return mediaTime < 0;
        }
    }

    // track usage, the bits of the 'tkhd' flags
    static final int trackEnabled = 1;
    static final int trackUsageInMovie = 2;
    static final int trackUsageInPreview = 4;
    static final int trackUsageInPoster = 8;

    final int ref;
    MovieRec movie;
    int id;
    boolean enabled = true;
    int usage = trackUsageInMovie | trackUsageInPreview | trackUsageInPoster;
    final List<Edit> edits = new ArrayList<>();
    short layer;
    int alternateGroup;
    /** 8.8 fixed */
    short volume;
    int[] matrix = MatrixManager.identity();
    /** 16.16 fixed */
    int width;
    int height;
    int creationTime;
    int modificationTime;
    MediaRec media;
    UserDataRec userData;
    /** reference type to referenced track ids */
    final Map<Integer, List<Integer>> references = new LinkedHashMap<>();
    // load settings
    int preloadTime = -1;
    int preloadDuration;
    int preloadFlags;
    int defaultHints;
    /** a region handle, 0 for none */
    int clipRgn;
    /** a pix map, 0 for none */
    int matte;
    byte[] soundLocalization;
    int gworld;

    TrackRec(MovieRec movie, int id) {
        this.ref = Heap.newObject(this, 4);
        this.movie = movie;
        this.id = id;
        this.creationTime = MovieRec.now();
        this.modificationTime = creationTime;
        this.userData = new UserDataRec();
        this.userData.owned = true;
    }

    void dispose() {
        if (media != null) {
            media.dispose();
            media = null;
        }
        userData.dispose();
        Heap.free(ref);
    }

    int movieScale() {
        return movie.timeScale;
    }

    int mediaScale() {
        return media != null ? media.timeScale : movie.timeScale;
    }

    long duration() {
        long d = 0;
        for (Edit e : edits) {
            d += e.duration;
        }
        return d;
    }

    void changed() {
        modificationTime = MovieRec.now();
        if (movie != null) {
            movie.changed = true;
        }
    }

    /** @return the media duration played by a track duration at the rate */
    long mediaDuration(long trackDuration, int rate) {
        return Math.floorDiv(trackDuration * mediaScale() * (long) rate, (long) movieScale() << 16);
    }

    /** @return the track duration that plays the media duration at the rate */
    long trackDuration(long mediaDuration, int rate) {
        if (rate == 0) {
            return 0;
        }
        return Math.floorDiv(mediaDuration * movieScale() * 0x10000L + mediaScale() * (long) rate / 2, mediaScale() * (long) rate);
    }

    /** @return the media time playing at the track time, -1 in an empty edit or out of the track */
    long trackTimeToMediaTime(long time) {
        long s = 0;
        for (Edit e : edits) {
            if (time >= s && time < s + e.duration) {
                return e.isEmpty() ? -1 : e.mediaTime + mediaDuration(time - s, e.rate);
            }
            s += e.duration;
        }
        return -1;
    }

    /** @return the edit containing the track time and its start time, null if out of the track */
    Edit editAt(long time, long[] start) {
        long s = 0;
        for (Edit e : edits) {
            if (time >= s && time < s + e.duration) {
                if (start != null) {
                    start[0] = s;
                }
                return e;
            }
            s += e.duration;
        }
        return null;
    }

    /** makes an edit boundary at the time, pads the track with an empty edit if needed, @return the index of the edit starting there */
    int split(long time) {
        long s = 0;
        for (int i = 0; i < edits.size(); i++) {
            Edit e = edits.get(i);
            if (time == s) {
                return i;
            }
            if (time < s + e.duration) {
                long d = time - s;
                Edit second = new Edit(e.duration - d, e.isEmpty() ? -1 : e.mediaTime + mediaDuration(d, e.rate), e.rate);
                e.duration = d;
                edits.add(i + 1, second);
                return i + 1;
            }
            s += e.duration;
        }
        if (time > s) {
            edits.add(new Edit(time - s, -1, 0x10000));
        }
        return edits.size();
    }

    void insertEmpty(long at, long duration) {
        if (duration <= 0) {
            return;
        }
        int i = split(at);
        edits.add(i, new Edit(duration, -1, 0x10000));
        normalize();
        changed();
    }

    /** @param at track time, -1 to append */
    void insertMedia(long at, long mediaTime, long mediaDuration, int rate) {
        if (at < 0) {
            at = duration();
        }
        int i = split(at);
        edits.add(i, new Edit(trackDuration(mediaDuration, rate), mediaTime, rate));
        normalize();
        changed();
    }

    void insertEdits(long at, List<Edit> inserted) {
        int i = split(at);
        for (Edit e : inserted) {
            edits.add(i++, e.copy());
        }
        normalize();
        changed();
    }

    void delete(long start, long duration) {
        if (duration <= 0) {
            return;
        }
        long d = duration();
        if (start >= d) {
            return;
        }
        int a = split(start);
        int b = split(Math.min(start + duration, d));
        edits.subList(a, b).clear();
        // trailing empty edits do not count
        while (!edits.isEmpty() && edits.getLast().isEmpty()) {
            edits.removeLast();
        }
        normalize();
        changed();
    }

    /** @return copies of the edits in the track time range */
    List<Edit> copyEdits(long start, long duration) {
        List<Edit> result = new ArrayList<>();
        long end = start + duration;
        long s = 0;
        for (Edit e : edits) {
            long es = s;
            long ee = s + e.duration;
            s = ee;
            if (ee <= start || es >= end) {
                continue;
            }
            long a = Math.max(es, start);
            long b = Math.min(ee, end);
            result.add(new Edit(b - a, e.isEmpty() ? -1 : e.mediaTime + mediaDuration(a - es, e.rate), e.rate));
        }
        if (s < end && duration > 0) {
            long a = Math.max(s, start);
            result.add(new Edit(end - a, -1, 0x10000));
        }
        return result;
    }

    void scale(long start, long oldDuration, long newDuration) {
        if (oldDuration <= 0 || newDuration < 0) {
            return;
        }
        int a = split(start);
        int b = split(start + oldDuration);
        long total = 0;
        for (int i = a; i < b; i++) {
            Edit e = edits.get(i);
            long nd = i == b - 1 ? newDuration - total : e.duration * newDuration / oldDuration;
            if (nd > 0) {
                e.rate = (int) (e.rate * (double) e.duration / nd);
            }
            e.duration = nd;
            total += nd;
        }
        edits.removeIf(e -> e.duration == 0);
        normalize();
        changed();
    }

    /** merges adjacent edits that continue each other */
    void normalize() {
        for (int i = edits.size() - 1; i > 0; i--) {
            Edit p = edits.get(i - 1);
            Edit e = edits.get(i);
            if (p.isEmpty() && e.isEmpty()) {
                p.duration += e.duration;
                edits.remove(i);
            } else if (!p.isEmpty() && !e.isEmpty() && p.rate == e.rate && p.mediaTime + mediaDuration(p.duration, p.rate) == e.mediaTime
                    && trackDuration(mediaDuration(p.duration + e.duration, p.rate), p.rate) == p.duration + e.duration) {
                p.duration += e.duration;
                edits.remove(i);
            }
        }
        edits.removeIf(e -> e.duration == 0);
    }

    /** @return the duration of the leading empty edit */
    long offset() {
        return !edits.isEmpty() && edits.getFirst().isEmpty() ? edits.getFirst().duration : 0;
    }

    void setOffset(long offset) {
        if (!edits.isEmpty() && edits.getFirst().isEmpty()) {
            edits.removeFirst();
        }
        if (offset > 0) {
            edits.addFirst(new Edit(offset, -1, 0x10000));
        }
        changed();
    }

    /** rescales the edit durations when the movie time scale changes */
    void rescale(int oldScale, int newScale) {
        for (Edit e : edits) {
            e.duration = ClockManager.convert(e.duration, oldScale, newScale);
        }
    }

    TrackRec copy(MovieRec newMovie, int newId) {
        TrackRec t = new TrackRec(newMovie, newId);
        copySettings(t);
        for (Edit e : edits) {
            t.edits.add(e.copy());
        }
        if (media != null) {
            t.media = media.copy(t);
        }
        t.userData.dispose();
        t.userData = userData.copy();
        t.userData.owned = true;
        references.forEach((k, v) -> t.references.put(k, new ArrayList<>(v)));
        return t;
    }

    void copySettings(TrackRec t) {
        t.enabled = enabled;
        t.usage = usage;
        t.layer = layer;
        t.alternateGroup = alternateGroup;
        t.volume = volume;
        t.matrix = matrix.clone();
        t.width = width;
        t.height = height;
        t.preloadTime = preloadTime;
        t.preloadDuration = preloadDuration;
        t.preloadFlags = preloadFlags;
        t.defaultHints = defaultHints;
        t.soundLocalization = soundLocalization == null ? null : soundLocalization.clone();
    }
}
