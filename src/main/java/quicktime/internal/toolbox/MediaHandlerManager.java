/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * MediaHandlerManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class MediaHandlerManager {

    private MediaHandlerManager() {
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaFrameLabelToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FlashMediaFrameLabelToMovieTime");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaFrameNumberToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FlashMediaFrameNumberToMovieTime");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaGetDisplayedFrameNumber(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FlashMediaGetDisplayedFrameNumber");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaGetFlashVariable(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FlashMediaGetFlashVariable");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaGetRefConBounds(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        throw new UnsupportedToolboxException("FlashMediaGetRefConBounds");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaGetRefConID(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FlashMediaGetRefConID");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaGetSupportedSwfVersion(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("FlashMediaGetSupportedSwfVersion");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaIDToRefCon(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("FlashMediaIDToRefCon");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaSetFlashVariable(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte paramByte) {
        throw new UnsupportedToolboxException("FlashMediaSetFlashVariable");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaSetPan(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("FlashMediaSetPan");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaSetZoom(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("FlashMediaSetZoom");
    }

    /** used by FlashMediaHandler */
    public static int FlashMediaSetZoomRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("FlashMediaSetZoomRect");
    }

    /** used by AtomContainer */
    public static int Media3DGetNamedObjectList(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("Media3DGetNamedObjectList");
    }

    /** used by AtomContainer */
    public static int Media3DGetRendererList(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("Media3DGetRendererList");
    }

    /** used by UserData, VisualMediaHandler */
    public static int MediaGetGraphicsMode(int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaGetGraphicsMode");
    }

    /** used by VisualMediaHandler */
    public static int MediaGetPublicInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MediaGetPublicInfo");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaGetSoundBalance(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("MediaGetSoundBalance");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaGetSoundBassAndTreble(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2) {
        throw new UnsupportedToolboxException("MediaGetSoundBassAndTreble");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaGetSoundEqualizerBandLevels(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaGetSoundEqualizerBandLevels");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaGetSoundEqualizerBands(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaGetSoundEqualizerBands");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaGetSoundLevelMeterInfo(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaGetSoundLevelMeterInfo");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaGetSoundLevelMeteringEnabled(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaGetSoundLevelMeteringEnabled");
    }

    /** used by UserData, VisualMediaHandler */
    public static int MediaGetTrackOpaque(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaGetTrackOpaque");
    }

    /** used by VisualMediaHandler */
    public static int MediaHitTestForTargetRefCon(int paramInt1, long paramLong, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MediaHitTestForTargetRefCon");
    }

    /** used by VisualMediaHandler */
    public static int MediaHitTestTargetRefCon(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaHitTestTargetRefCon");
    }

    /** used by UserData, VisualMediaHandler */
    public static int MediaSetGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaSetGraphicsMode");
    }

    /** used by VisualMediaHandler */
    public static int MediaSetPublicInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("MediaSetPublicInfo");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaSetSoundBalance(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("MediaSetSoundBalance");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaSetSoundBassAndTreble(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("MediaSetSoundBassAndTreble");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaSetSoundEqualizerBands(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MediaSetSoundEqualizerBands");
    }

    /** used by MPEGMediaHandler, SoundMediaHandler, StreamMediaHandler */
    public static int MediaSetSoundLevelMeteringEnabled(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("MediaSetSoundLevelMeteringEnabled");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaGetChildMovieDataReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("MovieMediaGetChildMovieDataReference");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentMovieProperty");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentMovieProperty");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentMovieProperty");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaGetCurrentTrackProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MovieMediaGetCurrentTrackProperty");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaLoadChildMovieFromDataReference(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MovieMediaLoadChildMovieFromDataReference");
    }

    /** used by MovieMediaHandler */
    public static int MovieMediaSetChildMovieDataReference(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MovieMediaSetChildMovieDataReference");
    }

    /** used by TextMediaHandler */
    public static int TextMediaAddHiliteSample(int paramInt1, short paramShort1, short paramShort2, byte[] paramArrayOfByte, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TextMediaAddHiliteSample");
    }

    /** used by TextMediaHandler */
    public static int TextMediaAddTextSample(int paramInt1, int paramInt2, int paramInt3, short paramShort1, short paramShort2, short paramShort3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort4, byte[] paramArrayOfByte3, int paramInt4, int paramInt5, short paramShort5, short paramShort6, byte[] paramArrayOfByte4, int paramInt6, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("TextMediaAddTextSample");
    }

    /** used by TextMediaHandler */
    public static int TextMediaFindNextText(int paramInt1, int paramInt2, int paramInt3, short paramShort, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("TextMediaFindNextText");
    }

    /** used by TextMediaHandler */
    public static int TextMediaHiliteTextSample(int paramInt1, int paramInt2, short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("TextMediaHiliteTextSample");
    }

    /** used by TextMediaHandler */
    public static int TextMediaSetTextSampleData(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        throw new UnsupportedToolboxException("TextMediaSetTextSampleData");
    }

    /** used by VideoMediaHandler */
    public static int VideoMediaGetStallCount(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("VideoMediaGetStallCount");
    }

    /** used by VideoMediaHandler */
    public static int VideoMediaGetStatistics(int paramInt) {
        throw new UnsupportedToolboxException("VideoMediaGetStatistics");
    }

    /** used by VideoMediaHandler */
    public static int VideoMediaResetStatistics(int paramInt) {
        throw new UnsupportedToolboxException("VideoMediaResetStatistics");
    }

}
