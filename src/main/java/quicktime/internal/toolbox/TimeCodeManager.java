/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * The timecode media handler ('tmcd') in Java.
 * <p>
 * TimeCodeDef: flags(4) fTimeScale(4) frameDuration(4) numFrames(1) padding(1).
 * TimeCodeRecord: hours(1, bit 7 negative) minutes(1) seconds(1) frames(1), or a counter(4).
 * TimeCodeDescription: the sample description header(16) reserved(4) TimeCodeDef(14) then the source reference as user data atoms.
 * The samples are big endian frame numbers (or counters).
 */
public final class TimeCodeManager {

    private TimeCodeManager() {
    }

    static final int noErr = 0;
    static final int paramErr = -50;
    static final int invalidTime = -2015;
    static final int badComponentInstance = 0x8001;

    // TimeCodeDef flags
    static final int tcDropFrame = 1;
    static final int tc24HourMax = 2;
    static final int tcNegTimesOK = 4;
    static final int tcCounter = 8;

    static final int tctNegFlag = 0x80;

    static final int TIME_CODE_DEF = 20;
    static final int SOURCE_REF = 34;

    /** the state of a timecode media handler */
    static final class State {
        int flags;
        final byte[] displayOptions = new byte[20];

        State() {
            Bytes.putShort(displayOptions, 4, 12);
            Arrays.fill(displayOptions, 14, 20, (byte) 0xff);
        }
    }

    private static final Map<MediaRec, State> states = new WeakHashMap<>();

    private static synchronized State state(MediaRec m) {
        return states.computeIfAbsent(m, k -> new State());
    }

    /** a timecode definition */
    record Def(int flags, int timeScale, int frameDuration, int numFrames) {
        static Def of(byte[] b, int o) {
            return new Def(Bytes.getInt(b, o), Bytes.getInt(b, o + 4), Bytes.getInt(b, o + 8), b[o + 12] & 0xff);
        }

        void put(byte[] b, int o) {
            Bytes.putInt(b, o, flags);
            Bytes.putInt(b, o + 4, timeScale);
            Bytes.putInt(b, o + 8, frameDuration);
            b[o + 12] = (byte) numFrames;
        }

        boolean drop() {
            return (flags & tcDropFrame) != 0;
        }

        boolean counter() {
            return (flags & tcCounter) != 0;
        }
    }

    /** frames dropped every minute but every tenth: 2 at 30, 4 at 60 */
    private static int dropFrames(Def d) {
        return d.drop() ? Math.max(2, Math.round(d.numFrames() / 15f)) : 0;
    }

    /** @return the hours, minutes, seconds and frames of a frame number */
    static int[] toTimeCode(Def d, long frame) {
        int fps = Math.max(1, d.numFrames());
        boolean negative = frame < 0;
        long f = Math.abs(frame);
        int drop = dropFrames(d);
        if (drop > 0) {
            // add back the dropped frame numbers
            long perMinute = fps * 60L - drop;
            long perTen = perMinute * 10 + drop;
            long tens = f / perTen;
            long rest = f % perTen;
            f += drop * 9L * tens;
            if (rest > drop) {
                f += drop * ((rest - drop) / perMinute);
            }
        }
        long frames = f % fps;
        long seconds = f / fps % 60;
        long minutes = f / fps / 60 % 60;
        long hours = f / fps / 3600;
        if ((d.flags() & tc24HourMax) != 0) {
            hours %= 24;
        }
        return new int[] {(int) hours, (int) minutes, (int) seconds, (int) frames, negative ? 1 : 0};
    }

    /** @return the frame number of a time code */
    static long toFrame(Def d, int hours, int minutes, int seconds, int frames, boolean negative) {
        int fps = Math.max(1, d.numFrames());
        long totalMinutes = hours * 60L + minutes;
        long f = (totalMinutes * 60 + seconds) * fps + frames;
        int drop = dropFrames(d);
        if (drop > 0) {
            f -= drop * (totalMinutes - totalMinutes / 10);
        }
        return negative ? -f : f;
    }

    static void putRecord(byte[] r, int[] tc) {
        r[0] = (byte) (tc[0] | (tc[4] != 0 ? tctNegFlag : 0));
        r[1] = (byte) tc[1];
        r[2] = (byte) tc[2];
        r[3] = (byte) tc[3];
    }

    static long recordToFrame(Def d, byte[] r) {
        if (d.counter()) {
            return Bytes.getInt(r, 0);
        }
        return toFrame(d, r[0] & 0x7f, r[1] & 0xff, r[2] & 0xff, r[3] & 0xff, (r[0] & tctNegFlag) != 0);
    }

    public static int TCFrameNumberToTimeCode(int mh, int frameNumber, byte[] tcdef, byte[] tcrec) {
        Def d = Def.of(tcdef, 0);
        if (d.counter()) {
            Bytes.putInt(tcrec, 0, frameNumber);
            return noErr;
        }
        if (frameNumber < 0 && (d.flags() & tcNegTimesOK) == 0) {
            return invalidTime;
        }
        putRecord(tcrec, toTimeCode(d, frameNumber));
        return noErr;
    }

    public static int TCTimeCodeToFrameNumber(int mh, byte[] tcdef, byte[] tcrec, int[] frameNumber) {
        Def d = Def.of(tcdef, 0);
        if (!d.counter() && ((tcrec[1] & 0xff) > 59 || (tcrec[2] & 0xff) > 59 || (tcrec[3] & 0xff) >= Math.max(1, d.numFrames()))) {
            return invalidTime;
        }
        frameNumber[0] = (int) recordToFrame(d, tcrec);
        return noErr;
    }

    /** HH:MM:SS:FF, HH:MM:SS;FF for drop frame, the number for counters */
    static String toString(Def d, long frameOrCounter, byte[] tcrec) {
        if (d.counter()) {
            return String.valueOf(frameOrCounter);
        }
        int h = tcrec[0] & 0x7f, m = tcrec[1] & 0xff, s = tcrec[2] & 0xff, f = tcrec[3] & 0xff;
        String sign = (tcrec[0] & tctNegFlag) != 0 ? "-" : "";
        return String.format("%s%02d:%02d:%02d%s%02d", sign, h, m, s, d.drop() ? ";" : ":", f);
    }

    public static int TCTimeCodeToString(int mh, byte[] tcdef, byte[] tcrec, byte[] str) {
        Def d = Def.of(tcdef, 0);
        Bytes.putPString(str, 0, toString(d, d.counter() ? Bytes.getInt(tcrec, 0) : 0, tcrec), Math.min(256, str.length));
        return noErr;
    }

    /** the record is a counter */
    public static int TCTimeCodeToString(int mh, byte[] tcdef, int[] counter, byte[] str) {
        Def d = Def.of(tcdef, 0);
        byte[] r = new byte[4];
        if (d.counter()) {
            Bytes.putInt(r, 0, counter[0]);
        } else {
            putRecord(r, toTimeCode(d, counter[0]));
        }
        Bytes.putPString(str, 0, toString(d, counter[0], r), Math.min(256, str.length));
        return noErr;
    }

    /** @return the description and the frame at the media time, or null */
    private static int timeCodeAt(MediaRec m, long mediaTime, int[] frameNumber, byte[] tcdef, byte[] tcrec, int[] srcRef) {
        long s = m.sampleAt(mediaTime);
        if (s == 0) {
            return invalidTime;
        }
        int r = m.runOfSample(s);
        MediaRec.SampleRun run = m.runs.get(r);
        byte[] desc = m.descriptions.get(run.descIndex - 1);
        if (desc.length < SOURCE_REF) {
            return paramErr;
        }
        Def d = Def.of(desc, TIME_CODE_DEF);
        long k = s - m.firstSampleOf(r);
        byte[] data;
        try {
            data = m.dataRef(run.dataRefIndex).read(run.offsetOf(k), 4);
        } catch (IOException e) {
            return -36;
        }
        long value = Bytes.getInt(data, 0);
        long into = mediaTime - m.timeOfSample(s);
        // the time into the sample in frames, the media time scale may differ from the definition's
        double frameSeconds = d.frameDuration() / (double) Math.max(1, d.timeScale());
        long frames = frameSeconds > 0 ? (long) Math.floor(into / (double) m.timeScale / frameSeconds + 1e-9) : 0;
        long frame = value + frames;
        if (frameNumber != null && frameNumber.length > 0) {
            frameNumber[0] = (int) frame;
        }
        if (tcdef != null) {
            System.arraycopy(desc, TIME_CODE_DEF, tcdef, 0, Math.min(14, tcdef.length));
        }
        if (tcrec != null) {
            if (d.counter()) {
                Bytes.putInt(tcrec, 0, (int) frame);
            } else {
                putRecord(tcrec, toTimeCode(d, frame));
            }
        }
        if (srcRef != null && srcRef.length > 0) {
            srcRef[0] = sourceRef(desc);
        }
        return noErr;
    }

    /** @return a new user data of the source reference atoms of the description, 0 if none */
    static int sourceRef(byte[] desc) {
        int end = Math.min(desc.length, Math.max(SOURCE_REF, Bytes.getInt(desc, 0)));
        if (end <= SOURCE_REF) {
            return 0;
        }
        UserDataRec u = UserDataRec.fromBytes(desc, SOURCE_REF, end);
        return u != null ? u.ref : 0;
    }

    public static int TCGetTimeCodeAtTime(int mh, int mediaTime, int[] frameNumber, byte[] tcdef, byte[] tcrec, int[] srcRef) {
        return timeCodeAt(MediaHandlerManager.media(mh), mediaTime, frameNumber, tcdef, tcrec, srcRef);
    }

    /** at the current time of the movie */
    public static int TCGetCurrentTimeCode(int mh, int[] frameNumber, byte[] tcdef, byte[] tcrec, int[] srcRef) {
        MediaRec m = MediaHandlerManager.media(mh);
        if (m.track == null || m.track.movie == null) {
            return invalidTime;
        }
        long mt = m.track.trackTimeToMediaTime(m.track.movie.time());
        if (mt < 0) {
            return invalidTime;
        }
        return timeCodeAt(m, mt, frameNumber, tcdef, tcrec, srcRef);
    }

    public static int TCGetSourceRef(int mh, int tcdH, int[] srefH) {
        byte[] d = Heap.handleBytes(tcdH);
        srefH[0] = sourceRef(d);
        return noErr;
    }

    /** the source reference is user data, or a handle of user data atoms */
    public static int TCSetSourceRef(int mh, int tcdH, int srefH) {
        byte[] d = Heap.handleBytes(tcdH);
        byte[] base = Arrays.copyOf(d, SOURCE_REF);
        byte[] ref;
        UserDataRec u = srefH != 0 ? Heap.peerOrNull(srefH, UserDataRec.class) : null;
        if (u != null) {
            ref = u.toBytes();
        } else if (srefH != 0) {
            ref = Heap.handleBytes(srefH);
        } else {
            ref = new byte[0];
        }
        // the user data atom list, without its 'udta' header
        if (ref.length >= 8 && Bytes.getInt(ref, 4) == Bytes.fourCC("udta")) {
            ref = Arrays.copyOfRange(ref, 8, Math.min(ref.length, Bytes.getInt(ref, 0)));
        }
        byte[] n = Arrays.copyOf(base, SOURCE_REF + ref.length);
        System.arraycopy(ref, 0, n, SOURCE_REF, ref.length);
        Bytes.putInt(n, 0, n.length);
        Heap.setHandleBytes(tcdH, n);
        return noErr;
    }

    public static int TCSetTimeCodeFlags(int mh, int flags, int flagsMask) {
        State s = state(MediaHandlerManager.media(mh));
        s.flags = (s.flags & ~flagsMask) | (flags & flagsMask);
        return noErr;
    }

    public static int TCGetTimeCodeFlags(int mh, int[] flags) {
        flags[0] = state(MediaHandlerManager.media(mh)).flags;
        return noErr;
    }

    /** TCTextOptions: txFont(2) txFace(2) txSize(2) pad(2) foreColor(6) backColor(6) */
    public static int TCSetDisplayOptions(int mh, byte[] options) {
        State s = state(MediaHandlerManager.media(mh));
        System.arraycopy(options, 0, s.displayOptions, 0, Math.min(20, options.length));
        return noErr;
    }

    public static int TCGetDisplayOptions(int mh, byte[] options) {
        State s = state(MediaHandlerManager.media(mh));
        System.arraycopy(s.displayOptions, 0, options, 0, Math.min(20, options.length));
        return noErr;
    }

    /** tcdfShowTimeCode */
    static boolean isShown(MediaRec m) {
        synchronized (TimeCodeManager.class) {
            State s = states.get(m);
            return s != null && (s.flags & 1) != 0;
        }
    }

    /** @return the text of the timecode at the media time, null if none */
    static String text(MediaRec m, long mediaTime) {
        byte[] def = new byte[14];
        byte[] rec = new byte[4];
        int[] frame = new int[1];
        if (timeCodeAt(m, mediaTime, frame, def, rec, null) != noErr) {
            return null;
        }
        Def d = Def.of(def, 0);
        return toString(d, frame[0], rec);
    }

    /** @return the display options of the media */
    static byte[] displayOptions(MediaRec m) {
        return state(m).displayOptions.clone();
    }
}
