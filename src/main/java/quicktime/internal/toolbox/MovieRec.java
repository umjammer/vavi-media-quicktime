/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.List;


/**
 * A movie: tracks in a common time scale, played by a time base.
 */
final class MovieRec {

    /** seconds between 1904-01-01 and 1970-01-01 */
    static final long MAC_EPOCH_OFFSET = 2_082_844_800L;

    /** @return the current time in seconds since 1904, as QuickTime stores it */
    static int now() {
        return (int) (System.currentTimeMillis() / 1000 + MAC_EPOCH_OFFSET);
    }

    final int ref;
    int timeScale = 600;
    final List<TrackRec> tracks = new ArrayList<>();
    int nextTrackID = 1;
    int creationTime;
    int modificationTime;
    /** 16.16 fixed */
    int preferredRate = 0x10000;
    /** 8.8 fixed */
    short preferredVolume = 0x100;
    short volume = 0x100;
    int[] matrix = MatrixManager.identity();
    int posterTime;
    int previewTime;
    int previewDuration;
    int selectionTime;
    int selectionDuration;
    UserDataRec userData;
    final ClockManager.TimeBaseRec timeBase;
    /** default data reference for new media, null for none */
    DataRefRec defaultDataRef;
    DataRefRec anchorDataRef;
    boolean active = true;
    boolean changed;
    boolean previewMode;
    int language;
    int playHints;
    long activeSegmentStart = -1;
    long activeSegmentDuration = -1;
    int gworld;
    int gdevice;
    /** the movie box, top left bottom right */
    short[] box;
    int clipRgn;
    int displayClipRgn;
    int colorTable;
    int progressProc;
    int progressRefCon;
    int drawingCompleteProc;
    int drawingCompleteRefCon;
    int drawingCompleteFlags;
    boolean autoTrackAlternates = true;
    /** the file the movie was read from or is written to, null if none */
    String path;
    /** file offset of the 'moov' atom in {@link #path}, -1 if none */
    long moovOffset = -1;
    long moovSize;
    /** playback of the media, null when not playing */
    Object player;
    /** the movie time last drawn into the gworld, -1 if it needs drawing */
    long drawnTime = -1;

    MovieRec() {
        this.ref = Heap.newObject(this, 4);
        this.creationTime = now();
        this.modificationTime = creationTime;
        this.userData = new UserDataRec();
        this.userData.owned = true;
        this.timeBase = ClockManager.newTimeBase(this);
        this.timeBase.scale = timeScale;
    }

    void dispose() {
        for (TrackRec t : tracks) {
            t.dispose();
        }
        tracks.clear();
        userData.dispose();
        ClockManager.disposeTimeBase(timeBase);
        Heap.free(ref);
    }

    long duration() {
        long d = 0;
        for (TrackRec t : tracks) {
            d = Math.max(d, t.duration());
        }
        return d;
    }

    TrackRec track(int id) {
        for (TrackRec t : tracks) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

    TrackRec newTrack() {
        TrackRec t = new TrackRec(this, nextTrackID++);
        tracks.add(t);
        changed = true;
        return t;
    }

    void removeTrack(TrackRec t) {
        tracks.remove(t);
        for (TrackRec o : tracks) {
            for (List<Integer> ids : o.references.values()) {
                // references to a removed track become 0, as on the real toolbox
                ids.replaceAll(id -> id == t.id ? 0 : id);
            }
        }
        t.dispose();
        changed = true;
    }

    /** updates the stop time of the time base after the duration changed */
    void updateTimeBase() {
        ClockManager.TimeBaseRec tb = timeBase;
        tb.anchor();
        synchronized (tb) {
            tb.start = activeSegmentStart >= 0 ? ClockManager.convert(activeSegmentStart, timeScale, tb.scale) : 0;
            tb.stop = activeSegmentStart >= 0 ? ClockManager.convert(activeSegmentStart + activeSegmentDuration, timeScale, tb.scale)
                    : ClockManager.convert(duration(), timeScale, tb.scale);
        }
    }

    /** @return the current movie time */
    long time() {
        return timeBase.time(timeScale);
    }

    void setTimeScale(int scale) {
        if (scale <= 0 || scale == timeScale) {
            return;
        }
        long t = time();
        for (TrackRec tr : tracks) {
            tr.rescale(timeScale, scale);
        }
        posterTime = (int) ClockManager.convert(posterTime, timeScale, scale);
        previewTime = (int) ClockManager.convert(previewTime, timeScale, scale);
        previewDuration = (int) ClockManager.convert(previewDuration, timeScale, scale);
        selectionTime = (int) ClockManager.convert(selectionTime, timeScale, scale);
        selectionDuration = (int) ClockManager.convert(selectionDuration, timeScale, scale);
        if (activeSegmentStart >= 0) {
            activeSegmentStart = ClockManager.convert(activeSegmentStart, timeScale, scale);
            activeSegmentDuration = ClockManager.convert(activeSegmentDuration, timeScale, scale);
        }
        long time = ClockManager.convert(t, timeScale, scale);
        timeScale = scale;
        timeBase.setTime(time, scale);
        updateTimeBase();
        changed = true;
    }

    /** @return the natural bounds of the visual tracks, left top right bottom in pixels */
    double[] naturalBounds() {
        double l = 0, t = 0, r = 0, b = 0;
        boolean any = false;
        double[][] mm = MatrixManager.toDoubles(matrix);
        for (TrackRec tr : tracks) {
            if (!tr.enabled || tr.width == 0 || tr.height == 0) {
                continue;
            }
            double[][] m = MatrixManager.multiply(MatrixManager.toDoubles(tr.matrix), mm);
            double w = tr.width / 65536.0;
            double h = tr.height / 65536.0;
            for (double[] p : new double[][] {{0, 0}, {w, 0}, {w, h}, {0, h}}) {
                double[] q = MatrixManager.transform(m, p[0], p[1]);
                if (!any) {
                    l = r = q[0];
                    t = b = q[1];
                    any = true;
                } else {
                    l = Math.min(l, q[0]);
                    r = Math.max(r, q[0]);
                    t = Math.min(t, q[1]);
                    b = Math.max(b, q[1]);
                }
            }
        }
        return new double[] {l, t, r, b};
    }
}
