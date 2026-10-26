/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A media: the samples of a track, their descriptions and data references.
 * <p>
 * The sample table is a list of runs of samples that have the same size, duration, description,
 * sync flag and are contiguous in the same data reference, as {@code AddMediaSample} adds them.
 */
final class MediaRec {

    /** contiguous samples sharing their properties */
    static final class SampleRun {
        /** 1 based */
        int dataRefIndex;
        long offset;
        /** nominal size of each sample as in 'stsz', 1 for frames of QuickTime sound */
        int size;
        /** size of the run in bytes */
        long bytes;
        /** duration of each sample */
        int duration;
        int count;
        /** 1 based */
        int descIndex;
        boolean sync;
        /** composition time offset of each sample */
        int ctsOffset;

        SampleRun(int dataRefIndex, long offset, int size, long bytes, int duration, int count, int descIndex, boolean sync, int ctsOffset) {
            this.dataRefIndex = dataRefIndex;
            this.offset = offset;
            this.size = size;
            this.bytes = bytes;
            this.duration = duration;
            this.count = count;
            this.descIndex = descIndex;
            this.sync = sync;
            this.ctsOffset = ctsOffset;
        }

        SampleRun copy() {
            return new SampleRun(dataRefIndex, offset, size, bytes, duration, count, descIndex, sync, ctsOffset);
        }

        /** @return true if the run can take the samples at its end */
        boolean extendable(int dataRefIndex, long offset, int size, long bytes, int count, int duration, int descIndex, boolean sync, int ctsOffset) {
            return this.dataRefIndex == dataRefIndex && this.offset + this.bytes == offset && this.size == size
                    && this.bytes * count == bytes * this.count
                    && this.duration == duration && this.descIndex == descIndex && this.sync == sync && this.ctsOffset == ctsOffset;
        }

        /** @return the file offset of the 0 based sample in the run */
        long offsetOf(long k) {
            return offset + bytes * k / count;
        }

        /** @return the byte size of n samples from the 0 based sample k */
        long bytesOf(long k, long n) {
            return offsetOf(k + n) - offsetOf(k);
        }
    }

    // media sample flags
    static final int mediaSampleNotSync = 1;
    static final int mediaSampleShadowSync = 2;

    final int ref;
    TrackRec track;
    /** media type, the handler subtype ('vide', 'soun', ...) */
    int type;
    int timeScale;
    short language;
    short quality;
    int creationTime;
    int modificationTime;
    /** handler manufacturer and name, from 'hdlr' */
    int handlerManufacturer;
    String handlerName = "";
    /** data handler subtype of the minf 'hdlr' */
    int dataHandlerType = DataRefRec.alis;
    /** graphics mode and op color of 'vmhd', balance of 'smhd' */
    short graphicsMode = 0x40;
    short[] opColor = {(short) 0x8000, (short) 0x8000, (short) 0x8000};
    short balance;
    /** media information atom of other media types ('gmhd' contents) */
    byte[] baseMediaInfo;

    final List<SampleRun> runs = new ArrayList<>();
    final List<byte[]> descriptions = new ArrayList<>();
    final List<DataRefRec> dataRefs = new ArrayList<>();
    int defaultDataRefIndex = 1;
    UserDataRec userData;
    byte[] inputMap;
    byte[] propertyAtom;
    int playHints;
    int preferredChunkSize;
    boolean editing;
    /** the media handler instance, created on demand */
    int handler;

    // derived, see index()
    private long[] firstSample;
    private long[] firstTime;
    private long sampleCount;
    private long duration;

    MediaRec(TrackRec track, int type, int timeScale) {
        this.ref = Heap.newObject(this, 4);
        this.track = track;
        this.type = type;
        this.timeScale = timeScale;
        this.creationTime = MovieRec.now();
        this.modificationTime = creationTime;
        this.userData = new UserDataRec();
        this.userData.owned = true;
    }

    void dispose() {
        userData.dispose();
        if (handler != 0) {
            ComponentManager.CloseComponent(handler);
        }
        Heap.free(ref);
    }

    /** @return a copy of the media for another track, sample data stays where it is */
    MediaRec copy(TrackRec newTrack) {
        MediaRec m = new MediaRec(newTrack, type, timeScale);
        m.language = language;
        m.quality = quality;
        m.handlerManufacturer = handlerManufacturer;
        m.handlerName = handlerName;
        m.dataHandlerType = dataHandlerType;
        m.graphicsMode = graphicsMode;
        m.opColor = opColor.clone();
        m.balance = balance;
        m.baseMediaInfo = baseMediaInfo == null ? null : baseMediaInfo.clone();
        for (SampleRun r : runs) {
            m.runs.add(r.copy());
        }
        for (byte[] d : descriptions) {
            m.descriptions.add(d.clone());
        }
        for (DataRefRec d : dataRefs) {
            m.dataRefs.add(d.copy());
        }
        m.defaultDataRefIndex = defaultDataRefIndex;
        m.userData.dispose();
        m.userData = userData.copy();
        m.userData.owned = true;
        m.inputMap = inputMap == null ? null : inputMap.clone();
        m.propertyAtom = propertyAtom == null ? null : propertyAtom.clone();
        m.playHints = playHints;
        m.preferredChunkSize = preferredChunkSize;
        return m;
    }

    void changed() {
        firstSample = null;
        modificationTime = MovieRec.now();
        if (track != null && track.movie != null) {
            track.movie.changed = true;
        }
    }

    private void index() {
        if (firstSample != null) {
            return;
        }
        int n = runs.size();
        firstSample = new long[n];
        firstTime = new long[n];
        long s = 1;
        long t = 0;
        for (int i = 0; i < n; i++) {
            SampleRun r = runs.get(i);
            firstSample[i] = s;
            firstTime[i] = t;
            s += r.count;
            t += (long) r.duration * r.count;
        }
        sampleCount = s - 1;
        duration = t;
    }

    long duration() {
        index();
        return duration;
    }

    long sampleCount() {
        index();
        return sampleCount;
    }

    /** @return the index of the run holding the 1 based sample number, -1 if none */
    int runOfSample(long sample) {
        index();
        if (sample < 1 || sample > sampleCount) {
            return -1;
        }
        int i = Arrays.binarySearch(firstSample, sample);
        return i >= 0 ? i : -i - 2;
    }

    /** @return the index of the run playing at the media time, -1 if none */
    int runOfTime(long time) {
        index();
        if (time < 0 || time >= duration) {
            return -1;
        }
        int i = Arrays.binarySearch(firstTime, time);
        if (i < 0) {
            i = -i - 2;
        }
        // skip zero duration runs
        while (i + 1 < firstTime.length && firstTime[i + 1] == firstTime[i] && runs.get(i).duration == 0) {
            i++;
        }
        return i;
    }

    long firstSampleOf(int run) {
        index();
        return firstSample[run];
    }

    long firstTimeOf(int run) {
        index();
        return firstTime[run];
    }

    /** @return the 1 based sample number at the media time, 0 if none */
    long sampleAt(long time) {
        int r = runOfTime(time);
        if (r < 0) {
            return 0;
        }
        SampleRun run = runs.get(r);
        long k = run.duration == 0 ? 0 : (time - firstTime[r]) / run.duration;
        return firstSample[r] + Math.min(k, run.count - 1);
    }

    /** @return the media time of the 1 based sample number */
    long timeOfSample(long sample) {
        int r = runOfSample(sample);
        if (r < 0) {
            return -1;
        }
        return firstTime[r] + (sample - firstSample[r]) * runs.get(r).duration;
    }

    /** @return the 1 based number of the sync sample at or before the sample */
    long syncSampleAtOrBefore(long sample) {
        int r = runOfSample(sample);
        for (int i = r; i >= 0; i--) {
            SampleRun run = runs.get(i);
            if (run.sync) {
                return i == r ? sample : firstSample[i] + run.count - 1;
            }
        }
        return 0;
    }

    /** @return the 1 based number of the next sync sample after the sample, 0 if none */
    long syncSampleAfter(long sample) {
        int r = runOfSample(sample);
        if (r < 0) {
            return 0;
        }
        if (runs.get(r).sync && sample + 1 < firstSample[r] + runs.get(r).count) {
            return sample + 1;
        }
        for (int i = r + 1; i < runs.size(); i++) {
            if (runs.get(i).sync) {
                return firstSample[i];
            }
        }
        return 0;
    }

    long syncSampleCount() {
        long n = 0;
        for (SampleRun r : runs) {
            if (r.sync) {
                n += r.count;
            }
        }
        return n;
    }

    /** adds samples, the run is merged with the last run if possible */
    void addSamples(int dataRefIndex, long offset, int size, long bytes, int duration, int count, int descIndex, boolean sync, int ctsOffset) {
        if (count <= 0) {
            return;
        }
        if (!runs.isEmpty()) {
            SampleRun last = runs.getLast();
            if (last.extendable(dataRefIndex, offset, size, bytes, count, duration, descIndex, sync, ctsOffset)) {
                last.count += count;
                last.bytes += bytes;
                changed();
                return;
            }
        }
        runs.add(new SampleRun(dataRefIndex, offset, size, bytes, duration, count, descIndex, sync, ctsOffset));
        changed();
    }

    /** @return the 1 based index of the description, added if new */
    int descriptionIndex(byte[] desc) {
        for (int i = 0; i < descriptions.size(); i++) {
            if (Arrays.equals(descriptions.get(i), desc)) {
                return i + 1;
            }
        }
        descriptions.add(desc);
        return descriptions.size();
    }

    /** @return the 1 based index of the data reference, added if new */
    int dataRefIndex(DataRefRec d) {
        for (int i = 0; i < dataRefs.size(); i++) {
            if (dataRefs.get(i).sameAs(d)) {
                return i + 1;
            }
        }
        dataRefs.add(d);
        return dataRefs.size();
    }

    DataRefRec dataRef(int index) {
        return index >= 1 && index <= dataRefs.size() ? dataRefs.get(index - 1) : null;
    }

    /** @return the data size of the samples in the media time range */
    long dataSize(long start, long dur) {
        long size = 0;
        long end = start + dur;
        index();
        for (int i = 0; i < runs.size(); i++) {
            SampleRun r = runs.get(i);
            long t0 = firstTime[i];
            long t1 = t0 + (long) r.duration * r.count;
            if (t1 <= start || t0 >= end) {
                continue;
            }
            if (r.duration == 0) {
                size += r.bytes;
                continue;
            }
            long a = Math.max(0, (start - t0) / r.duration);
            long b = Math.min(r.count, (end - t0 + r.duration - 1) / r.duration);
            size += r.bytesOf(a, b - a);
        }
        return size;
    }

    boolean isVisual() {
        return type == Bytes.fourCC("vide") || type == Bytes.fourCC("text") || type == Bytes.fourCC("sprt")
                || type == Bytes.fourCC("qd3d") || type == Bytes.fourCC("tx3g") || type == Bytes.fourCC("sbtl");
    }

    boolean isSound() {
        return type == Bytes.fourCC("soun") || type == Bytes.fourCC("musi");
    }
}
