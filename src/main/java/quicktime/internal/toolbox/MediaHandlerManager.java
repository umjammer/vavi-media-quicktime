/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * MediaHandlerManager.
 * <p>
 * The media handler functions of video, sound and text media. A media handler instance belongs to one media
 * ({@link MovieToolbox#GetMediaHandler}). Flash, 3D and movie media have no implementation.
 */
public final class MediaHandlerManager {

    private MediaHandlerManager() {
    }

    static final int noErr = 0;
    static final int badComponentSelector = 0x8002;
    static final int movieTextNotFoundErr = -2062;

    /** settings of a media handler that the media itself does not keep */
    static final class HandlerState {
        short bass;
        short treble;
        boolean levelMetering;
        int[] bands = {32, 64, 125, 250, 500, 1000, 2000, 4000, 8000, 16000};
        int textSampleData;
        int textSampleDataType;
    }

    private static final java.util.Map<MediaRec, HandlerState> states = new java.util.WeakHashMap<>();

    private static synchronized HandlerState state(MediaRec m) {
        return states.computeIfAbsent(m, k -> new HandlerState());
    }

    static MediaRec media(int mh) {
        MediaRec m = ComponentManager.mediaOfHandler(mh);
        if (m == null) {
            throw new IllegalArgumentException("not a media handler: 0x" + Integer.toHexString(mh));
        }
        return m;
    }

    // ---- generic

    /** rgb color is 3 shorts */
    public static int MediaGetGraphicsMode(int mh, int[] mode, byte[] opColor) {
        MediaRec m = media(mh);
        if (mode != null && mode.length > 0) {
            mode[0] = m.graphicsMode;
        }
        if (opColor != null) {
            for (int i = 0; i < 3; i++) {
                Bytes.putShort(opColor, i * 2, m.opColor[i]);
            }
        }
        return noErr;
    }

    public static int MediaSetGraphicsMode(int mh, int mode, byte[] opColor) {
        MediaRec m = media(mh);
        m.graphicsMode = (short) mode;
        if (opColor != null) {
            for (int i = 0; i < 3; i++) {
                m.opColor[i] = Bytes.getShort(opColor, i * 2);
            }
        }
        m.changed();
        return noErr;
    }

    public static int MediaGetTrackOpaque(int mh, byte[] opaque) {
        MediaRec m = media(mh);
        if (opaque != null && opaque.length > 0) {
            // copy mode draws every pixel
            opaque[0] = (byte) (m.isVisual() && (m.graphicsMode & 0xff) == 0x40 || m.graphicsMode == 0 ? 1 : 0);
        }
        return noErr;
    }

    public static int MediaGetPublicInfo(int mh, int infoSelector, byte[] info, int[] size) {
        return badComponentSelector;
    }

    public static int MediaSetPublicInfo(int mh, int infoSelector, int info, int size) {
        return badComponentSelector;
    }

    /** sprites and flash only */
    public static int MediaHitTestForTargetRefCon(int mh, long flags, int loc, int[] targetRefCon) {
        if (targetRefCon != null && targetRefCon.length > 0) {
            targetRefCon[0] = 0;
        }
        return noErr;
    }

    public static int MediaHitTestTargetRefCon(int mh, int targetRefCon, int flags, int loc, byte[] wasHit) {
        if (wasHit != null && wasHit.length > 0) {
            wasHit[0] = 0;
        }
        return noErr;
    }

    // ---- sound

    public static int MediaGetSoundBalance(int mh, short[] balance) {
        balance[0] = media(mh).balance;
        return noErr;
    }

    public static int MediaSetSoundBalance(int mh, short balance) {
        MediaRec m = media(mh);
        m.balance = balance;
        m.changed();
        return noErr;
    }

    public static int MediaGetSoundBassAndTreble(int mh, short[] bass, short[] treble) {
        HandlerState s = state(media(mh));
        bass[0] = s.bass;
        treble[0] = s.treble;
        return noErr;
    }

    public static int MediaSetSoundBassAndTreble(int mh, short bass, short treble) {
        HandlerState s = state(media(mh));
        s.bass = bass;
        s.treble = treble;
        return noErr;
    }

    /** bands: count(2) frequencies pointer(4), the frequencies are unsigned fixed */
    public static int MediaGetSoundEqualizerBands(int mh, byte[] bands) {
        HandlerState s = state(media(mh));
        int count = Bytes.getShort(bands, 0);
        int ptr = Bytes.getInt(bands, 2);
        int n = count <= 0 ? s.bands.length : Math.min(count, s.bands.length);
        Bytes.putShort(bands, 0, n);
        if (ptr != 0) {
            for (int i = 0; i < n; i++) {
                Heap.putInt(ptr + i * 4, s.bands[i] << 16);
            }
        }
        return noErr;
    }

    public static int MediaSetSoundEqualizerBands(int mh, byte[] bands) {
        HandlerState s = state(media(mh));
        int count = Bytes.getShort(bands, 0);
        int ptr = Bytes.getInt(bands, 2);
        if (count > 0 && ptr != 0) {
            int[] b = new int[count];
            for (int i = 0; i < count; i++) {
                b[i] = Heap.getInt(ptr + i * 4) >>> 16;
            }
            s.bands = b;
        }
        return noErr;
    }

    public static int MediaGetSoundLevelMeteringEnabled(int mh, byte[] enabled) {
        enabled[0] = (byte) (state(media(mh)).levelMetering ? 1 : 0);
        return noErr;
    }

    public static int MediaSetSoundLevelMeteringEnabled(int mh, byte enabled) {
        state(media(mh)).levelMetering = enabled != 0;
        return noErr;
    }

    /** @return the peak levels (0 - 255) of each channel around the current movie time, null if it cannot be decoded */
    private static int[] peaks(MediaRec m, int channelsWanted) {
        if (m.track == null || m.track.movie == null || m.descriptions.isEmpty()) {
            return null;
        }
        Audio.Decoder dec = Audio.decoder(m.descriptions.getFirst());
        if (dec == null) {
            return null;
        }
        int channels = Math.max(1, dec.channels);
        int[] peaks = new int[channels];
        long mt = m.track.trackTimeToMediaTime(m.track.movie.time());
        if (mt < 0) {
            return peaks;
        }
        int frames = (int) Math.max(1, m.timeScale / 30);
        try {
            float[] pcm = Audio.read(m, mt, frames, channels);
            if (pcm == null) {
                return null;
            }
            for (int i = 0; i < pcm.length; i++) {
                int c = i % channels;
                peaks[c] = Math.max(peaks[c], (int) Math.min(255, Math.abs(pcm[i]) * 255));
            }
        } catch (IOException e) {
            return peaks;
        }
        return peaks;
    }

    /** level meter info: numChannels(2) leftMeter(1) rightMeter(1) */
    public static int MediaGetSoundLevelMeterInfo(int mh, byte[] info) {
        MediaRec m = media(mh);
        int[] p = peaks(m, 2);
        int channels = p == null ? 0 : p.length;
        Bytes.putShort(info, 0, channels);
        info[2] = (byte) (p == null ? 0 : p[0]);
        info[3] = (byte) (p == null ? 0 : p.length > 1 ? p[1] : p[0]);
        return noErr;
    }

    /** one level byte per band, the overall level as there is no spectrum analysis */
    public static int MediaGetSoundEqualizerBandLevels(int mh, byte[] levels) {
        MediaRec m = media(mh);
        int[] p = peaks(m, 2);
        int level = 0;
        if (p != null) {
            for (int v : p) {
                level = Math.max(level, v);
            }
        }
        java.util.Arrays.fill(levels, (byte) level);
        return noErr;
    }

    // ---- video

    public static int VideoMediaGetStallCount(int mh, int[] stalls) {
        stalls[0] = 0;
        return noErr;
    }

    /** @return the frame rate as fixed, from the sample durations */
    public static int VideoMediaGetStatistics(int mh) {
        MediaRec m = media(mh);
        long count = m.sampleCount();
        long duration = m.duration();
        return duration == 0 ? 0 : (int) (count * m.timeScale * 65536L / duration);
    }

    public static int VideoMediaResetStatistics(int mh) {
        return noErr;
    }

    // ---- text

    /** @return a text sample description */
    static byte[] textDescription(int displayFlags, int justification, byte[] backColor, byte[] textBox, int fontNumber, int fontFace, byte[] foreColor, String fontName) {
        byte[] name = fontName.getBytes(Bytes.MAC_ROMAN);
        int size = 60 + name.length;
        byte[] d = new byte[size];
        Bytes.putInt(d, 0, size);
        Bytes.putInt(d, 4, Bytes.fourCC("text"));
        Bytes.putShort(d, 14, 1);
        Bytes.putInt(d, 16, displayFlags);
        Bytes.putInt(d, 20, justification);
        if (backColor != null) {
            System.arraycopy(backColor, 0, d, 24, 6);
        } else {
            java.util.Arrays.fill(d, 24, 30, (byte) 0xff);
        }
        if (textBox != null) {
            System.arraycopy(textBox, 0, d, 30, 8);
        }
        Bytes.putShort(d, 46, fontNumber);
        Bytes.putShort(d, 48, fontFace);
        if (foreColor != null) {
            System.arraycopy(foreColor, 0, d, 53, 6);
        }
        d[59] = (byte) name.length;
        System.arraycopy(name, 0, d, 60, name.length);
        return d;
    }

    /** @return a text sample: length(2) text 'styl' 'hlit' 'hclr' */
    static byte[] textSample(byte[] text, int fontNumber, int fontSize, int face, byte[] color, int hiliteStart, int hiliteEnd, byte[] hiliteColor) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(text.length >> 8);
        os.write(text.length);
        os.write(text, 0, text.length);
        MovieAtoms.Out o = new MovieAtoms.Out();
        o.begin(Bytes.fourCC("styl")).i16(1).i32(0).i16(fontSize + fontSize / 4).i16(fontSize).i16(fontNumber).i8(face).i8(0).i16(fontSize);
        if (color != null) {
            o.bytes(java.util.Arrays.copyOf(color, 6));
        } else {
            o.zeros(6);
        }
        o.end();
        if (hiliteEnd > hiliteStart) {
            o.begin(Bytes.fourCC("hlit")).i32(hiliteStart).i32(hiliteEnd).end();
            if (hiliteColor != null) {
                o.begin(Bytes.fourCC("hclr")).bytes(java.util.Arrays.copyOf(hiliteColor, 6)).end();
            }
        }
        os.writeBytes(o.toByteArray());
        return os.toByteArray();
    }

    /** adds a sample to the media, @return the media time of the sample */
    private static int addSample(MediaRec m, byte[] sample, byte[] desc, int duration, int[] sampleTime) {
        int h = Heap.newHandle(sample);
        int dh = Heap.newHandle(desc);
        try {
            return MovieToolbox.AddMediaSample(m.ref, h, 0, sample.length, duration, dh, 1, (short) 0, sampleTime);
        } finally {
            Heap.disposeHandle(h);
            Heap.disposeHandle(dh);
        }
    }

    public static int TextMediaAddTextSample(int mh, int text, int size, short fontNumber, short fontSize, short textFace, byte[] textColor,
                                             byte[] backColor, short textJustification, byte[] textBox, int displayFlags, int scrollDelay,
                                             short hiliteStart, short hiliteEnd, byte[] hiliteColor, int duration, int[] sampleTime) {
        MediaRec m = media(mh);
        byte[] t = size == 0 ? new byte[0] : Heap.read(text, size);
        byte[] desc = textDescription(displayFlags, textJustification, backColor, textBox, fontNumber, textFace, textColor,
                QuickDrawManager.fontName(fontNumber));
        return addSample(m, textSample(t, fontNumber, fontSize, textFace, textColor, hiliteStart, hiliteEnd, hiliteColor), desc, duration, sampleTime);
    }

    public static int TextMediaAddHiliteSample(int mh, short hiliteStart, short hiliteEnd, byte[] hiliteColor, int duration, int[] sampleTime) {
        MediaRec m = media(mh);
        byte[] desc = m.descriptions.isEmpty() ? textDescription(0, 0, null, null, 0, 0, null, "") : m.descriptions.getLast();
        return addSample(m, textSample(new byte[0], 0, 12, 0, null, hiliteStart, hiliteEnd, hiliteColor), desc, duration, sampleTime);
    }

    /** the highlight is drawn by the media handler, there is no drawing here */
    public static int TextMediaHiliteTextSample(int mh, int sampleTime, short hiliteStart, short hiliteEnd, byte[] hiliteColor) {
        return noErr;
    }

    public static int TextMediaSetTextSampleData(int mh, int[] data, int dataType) {
        HandlerState s = state(media(mh));
        s.textSampleData = data != null && data.length > 0 ? data[0] : 0;
        s.textSampleDataType = dataType;
        return noErr;
    }

    public static int TextMediaFindNextText(int mh, int text, int size, short findFlags, int startTime, int[] foundTime, int[] foundDuration, int[] offset) {
        MediaRec m = media(mh);
        boolean caseSensitive = (findFlags & MovieToolbox.findTextCaseSensitive) != 0;
        boolean reverse = (findFlags & MovieToolbox.findTextReverseSearch) != 0;
        boolean wrap = (findFlags & MovieToolbox.findTextWrapAround) != 0;
        String needle = new String(Heap.read(text, size), Bytes.MAC_ROMAN);
        if (!caseSensitive) {
            needle = needle.toLowerCase();
        }
        long count = m.sampleCount();
        if (count == 0) {
            return movieTextNotFoundErr;
        }
        long first = Math.max(1, m.sampleAt(Math.max(0, startTime)));
        for (long i = 0; i < count; i++) {
            long s = reverse ? first - i : first + i;
            if (s < 1 || s > count) {
                if (!wrap) {
                    break;
                }
                s = reverse ? s + count : s - count;
            }
            String str = MovieToolbox.textSample(m, s);
            if (str == null) {
                continue;
            }
            int at = caseSensitive ? str.indexOf(needle) : str.toLowerCase().indexOf(needle);
            if (at >= 0) {
                foundTime[0] = (int) m.timeOfSample(s);
                foundDuration[0] = m.runs.get(m.runOfSample(s)).duration;
                offset[0] = at;
                return noErr;
            }
        }
        return movieTextNotFoundErr;
    }

    // ---- flash, 3D, movie media

    public static int FlashMediaFrameLabelToMovieTime(int mh, int label, int[] time) {
        throw new UnsupportedToolboxException("FlashMediaFrameLabelToMovieTime", "flash media is not supported");
    }

    public static int FlashMediaFrameNumberToMovieTime(int mh, int frame, int[] time) {
        throw new UnsupportedToolboxException("FlashMediaFrameNumberToMovieTime", "flash media is not supported");
    }

    public static int FlashMediaGetDisplayedFrameNumber(int mh, int[] frame) {
        throw new UnsupportedToolboxException("FlashMediaGetDisplayedFrameNumber", "flash media is not supported");
    }

    public static int FlashMediaGetFlashVariable(int mh, byte[] path, byte[] name, int[] value) {
        throw new UnsupportedToolboxException("FlashMediaGetFlashVariable", "flash media is not supported");
    }

    public static int FlashMediaGetRefConBounds(int mh, int refCon, int[] left, int[] top, int[] right, int[] bottom) {
        throw new UnsupportedToolboxException("FlashMediaGetRefConBounds", "flash media is not supported");
    }

    public static int FlashMediaGetRefConID(int mh, int refCon, int[] id) {
        throw new UnsupportedToolboxException("FlashMediaGetRefConID", "flash media is not supported");
    }

    public static int FlashMediaGetSupportedSwfVersion(int mh, byte[] version) {
        throw new UnsupportedToolboxException("FlashMediaGetSupportedSwfVersion", "flash media is not supported");
    }

    public static int FlashMediaIDToRefCon(int mh, int id, int[] refCon) {
        throw new UnsupportedToolboxException("FlashMediaIDToRefCon", "flash media is not supported");
    }

    public static int FlashMediaSetFlashVariable(int mh, byte[] path, byte[] name, byte[] value, byte updateFocus) {
        throw new UnsupportedToolboxException("FlashMediaSetFlashVariable", "flash media is not supported");
    }

    public static int FlashMediaSetPan(int mh, short x, short y) {
        throw new UnsupportedToolboxException("FlashMediaSetPan", "flash media is not supported");
    }

    public static int FlashMediaSetZoom(int mh, short factor) {
        throw new UnsupportedToolboxException("FlashMediaSetZoom", "flash media is not supported");
    }

    public static int FlashMediaSetZoomRect(int mh, int left, int top, int right, int bottom) {
        throw new UnsupportedToolboxException("FlashMediaSetZoomRect", "flash media is not supported");
    }

    public static int Media3DGetNamedObjectList(int mh, int[] list) {
        throw new UnsupportedToolboxException("Media3DGetNamedObjectList", "QuickDraw 3D is not supported");
    }

    public static int Media3DGetRendererList(int mh, int[] list) {
        throw new UnsupportedToolboxException("Media3DGetRendererList", "QuickDraw 3D is not supported");
    }

    public static int MovieMediaGetChildMovieDataReference(int mh, int id, int index, int dataRefType, byte[] dataRef, int dataRefIndex, int flags) {
        throw new UnsupportedToolboxException("MovieMediaGetChildMovieDataReference", "movie media is not supported");
    }

    public static int MovieMediaGetCurrentMovieProperty(int mh, int whichProperty, byte[] value) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentMovieProperty", "movie media is not supported");
    }

    public static int MovieMediaGetCurrentMovieProperty(int mh, int whichProperty, int value) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentMovieProperty", "movie media is not supported");
    }

    public static int MovieMediaGetCurrentMovieProperty(int mh, int whichProperty, int[] value) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentMovieProperty", "movie media is not supported");
    }

    public static int MovieMediaGetCurrentTrackProperty(int mh, int trackID, int whichProperty, int[] value) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentTrackProperty", "movie media is not supported");
    }

    public static int MovieMediaLoadChildMovieFromDataReference(int mh, int id) {
        throw new UnsupportedToolboxException("MovieMediaLoadChildMovieFromDataReference", "movie media is not supported");
    }

    public static int MovieMediaSetChildMovieDataReference(int mh, int id, int dataRefType, byte[] dataRef) {
        throw new UnsupportedToolboxException("MovieMediaSetChildMovieDataReference", "movie media is not supported");
    }
}
