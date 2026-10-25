/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * SequenceGrabberManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class SequenceGrabberManager {

    private SequenceGrabberManager() {
    }

    /** used by SGChannel */
    public static int SGChannelGetDataSourceName(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGChannelGetDataSourceName");
    }

    /** used by SGChannel */
    public static int SGChannelSetDataSourceName(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("SGChannelSetDataSourceName");
    }

    /** used by SGChannel */
    public static int SGDisposeChannel(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGDisposeChannel");
    }

    /** used by QTObjectManagement */
    public static int SGDisposeDeviceList(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGDisposeDeviceList");
    }

    /** used by SGOutput */
    public static int SGDisposeOutput(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGDisposeOutput");
    }

    /** used by VisualChannel */
    public static int SGGetChannelBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetChannelBounds");
    }

    /** used by Region */
    public static int SGGetChannelClip(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetChannelClip");
    }

    /** used by SGChannel */
    public static int SGGetChannelDeviceList(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetChannelDeviceList");
    }

    /** used by SGChannel */
    public static int SGGetChannelPlayFlags(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetChannelPlayFlags");
    }

    /** used by SGChannel */
    public static int SGGetChannelSampleDescription(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGGetChannelSampleDescription");
    }

    /** used by UserData */
    public static int SGGetChannelSettings(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3) {
        throw new UnsupportedToolboxException("SGGetChannelSettings");
    }

    /** used by SGChannel */
    public static int SGGetChannelTimeScale(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetChannelTimeScale");
    }

    /** used by SGChannel */
    public static int SGGetChannelUsage(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetChannelUsage");
    }

    /** used by AudioChannel */
    public static int SGGetChannelVolume(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SGGetChannelVolume");
    }

    /** used by QTFile, SequenceGrabber */
    public static int SGGetDataOutput(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetDataOutput");
    }

    /** used by SGOutput */
    public static int SGGetDataOutputStorageSpaceRemaining(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetDataOutputStorageSpaceRemaining");
    }

    /** used by DataRef, SequenceGrabber */
    public static int SGGetDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        throw new UnsupportedToolboxException("SGGetDataRef");
    }

    /** used by SGVideoChannel */
    public static int SGGetFrameRate(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetFrameRate");
    }

    /** used by GDevice, QDGraphics, SequenceGrabber */
    public static int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("SGGetGWorld");
    }

    /** used by SequenceGrabber */
    public static int SGGetIndChannel(int paramInt1, short paramShort, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetIndChannel");
    }

    /** used by SequenceGrabber */
    public static int SGGetIndChannel(int paramInt, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("SGGetIndChannel");
    }

    /** used by SGMusicChannel */
    public static int SGGetInstrument(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetInstrument");
    }

    /** used by SequenceGrabber */
    public static int SGGetMaximumRecordTime(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetMaximumRecordTime");
    }

    /** used by SequenceGrabber */
    public static int SGGetMode(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("SGGetMode");
    }

    /** used by Movie */
    public static int SGGetMovie(int paramInt) {
        throw new UnsupportedToolboxException("SGGetMovie");
    }

    /** used by DataRef */
    public static int SGGetOutputDataReference(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("SGGetOutputDataReference");
    }

    /** used by SGOutput */
    public static int SGGetOutputMaximumOffset(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetOutputMaximumOffset");
    }

    /** used by SGOutput */
    public static int SGGetOutputNextOutput(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetOutputNextOutput");
    }

    /** used by SequenceGrabber */
    public static int SGGetPause(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetPause");
    }

    /** used by UserData */
    public static int SGGetSettings(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        throw new UnsupportedToolboxException("SGGetSettings");
    }

    /** used by SPBDevice */
    public static int SGGetSoundInputDriver(int paramInt) {
        throw new UnsupportedToolboxException("SGGetSoundInputDriver");
    }

    /** used by SGSoundChannel */
    public static int SGGetSoundInputParameters(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetSoundInputParameters");
    }

    /** used by SGSoundChannel */
    public static int SGGetSoundInputRate(int paramInt) {
        throw new UnsupportedToolboxException("SGGetSoundInputRate");
    }

    /** used by SGSoundChannel */
    public static int SGGetSoundRecordChunkSize(int paramInt) {
        throw new UnsupportedToolboxException("SGGetSoundRecordChunkSize");
    }

    /** used by SGVideoChannel */
    public static int SGGetSrcVideoBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetSrcVideoBounds");
    }

    /** used by SequenceGrabber */
    public static int SGGetStorageSpaceRemaining(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetStorageSpaceRemaining");
    }

    /** used by SGTextChannel */
    public static int SGGetTextReturnToSpaceValue(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("SGGetTextReturnToSpaceValue");
    }

    /** used by TimeBase */
    public static int SGGetTimeBase(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetTimeBase");
    }

    /** used by SequenceGrabber */
    public static int SGGetTimeRemaining(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetTimeRemaining");
    }

    /** used by Compressor */
    public static int SGGetVideoCompressor(int paramInt, short[] paramArrayOfShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        throw new UnsupportedToolboxException("SGGetVideoCompressor");
    }

    /** used by SGVideoChannel */
    public static int SGGetVideoCompressorType(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGGetVideoCompressorType");
    }

    /** used by SGVideoChannel */
    public static int SGGetVideoDigitizerComponent(int paramInt) {
        throw new UnsupportedToolboxException("SGGetVideoDigitizerComponent");
    }

    /** used by SGVideoChannel */
    public static int SGGetVideoRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGGetVideoRect");
    }

    /** used by Pict, SequenceGrabber */
    public static int SGGrabPict(int paramInt1, int[] paramArrayOfInt, byte[] paramArrayOfByte, short paramShort, int paramInt2) {
        throw new UnsupportedToolboxException("SGGrabPict");
    }

    /** used by SequenceGrabber */
    public static int SGIdle(int paramInt) {
        throw new UnsupportedToolboxException("SGIdle");
    }

    /** used by SGChannel */
    public static int SGInitChannel(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGInitChannel");
    }

    /** used by SequenceGrabber */
    public static int SGInitialize(int paramInt) {
        throw new UnsupportedToolboxException("SGInitialize");
    }

    /** used by SGChannel */
    public static int SGNewChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGNewChannel");
    }

    /** used by SGOutput */
    public static int SGNewOutput(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("SGNewOutput");
    }

    /** used by SequenceGrabber */
    public static int SGPause(int paramInt, byte paramByte) {
        throw new UnsupportedToolboxException("SGPause");
    }

    /** used by SequenceGrabber */
    public static int SGPrepare(int paramInt, byte paramByte1, byte paramByte2) {
        throw new UnsupportedToolboxException("SGPrepare");
    }

    /** used by SequenceGrabber */
    public static int SGRelease(int paramInt) {
        throw new UnsupportedToolboxException("SGRelease");
    }

    /** used by VisualChannel */
    public static int SGSetChannelBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetChannelBounds");
    }

    /** used by VisualChannel */
    public static int SGSetChannelClip(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetChannelClip");
    }

    /** used by SGChannel */
    public static int SGSetChannelDevice(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetChannelDevice");
    }

    /** used by SGOutput */
    public static int SGSetChannelOutput(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SGSetChannelOutput");
    }

    /** used by SGChannel */
    public static int SGSetChannelPlayFlags(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetChannelPlayFlags");
    }

    /** used by SGChannel */
    public static int SGSetChannelSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SGSetChannelSettings");
    }

    /** used by SGChannel */
    public static int SGSetChannelUsage(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetChannelUsage");
    }

    /** used by AudioChannel */
    public static int SGSetChannelVolume(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SGSetChannelVolume");
    }

    /** used by SequenceGrabber */
    public static int SGSetDataOutput(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetDataOutput");
    }

    /** used by SequenceGrabber */
    public static int SGSetDataProc(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SGSetDataProc");
    }

    /** used by SequenceGrabber */
    public static int SGSetDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("SGSetDataRef");
    }

    /** used by SGTextChannel */
    public static int SGSetFontName(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetFontName");
    }

    /** used by SGTextChannel */
    public static int SGSetFontSize(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SGSetFontSize");
    }

    /** used by SGVideoChannel */
    public static int SGSetFrameRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetFrameRate");
    }

    /** used by SequenceGrabber */
    public static int SGSetGWorld(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SGSetGWorld");
    }

    /** used by SGMusicChannel */
    public static int SGSetInstrument(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetInstrument");
    }

    /** used by SGTextChannel */
    public static int SGSetJustification(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SGSetJustification");
    }

    /** used by SequenceGrabber */
    public static int SGSetMaximumRecordTime(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetMaximumRecordTime");
    }

    /** used by SGOutput */
    public static int SGSetOutputFlags(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SGSetOutputFlags");
    }

    /** used by SGOutput */
    public static int SGSetOutputMaximumOffset(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetOutputMaximumOffset");
    }

    /** used by SGOutput */
    public static int SGSetOutputNextOutput(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SGSetOutputNextOutput");
    }

    /** used by SequenceGrabber */
    public static int SGSetSettings(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SGSetSettings");
    }

    /** used by SGSoundChannel */
    public static int SGSetSoundInputDriver(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetSoundInputDriver");
    }

    /** used by SGSoundChannel */
    public static int SGSetSoundInputParameters(int paramInt1, short paramShort1, short paramShort2, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetSoundInputParameters");
    }

    /** used by SGSoundChannel */
    public static int SGSetSoundInputRate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetSoundInputRate");
    }

    /** used by SGSoundChannel */
    public static int SGSetSoundRecordChunkSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetSoundRecordChunkSize");
    }

    /** used by SGTextChannel */
    public static int SGSetTextBackColor(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetTextBackColor");
    }

    /** used by SGTextChannel */
    public static int SGSetTextForeColor(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetTextForeColor");
    }

    /** used by SGTextChannel */
    public static int SGSetTextReturnToSpaceValue(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("SGSetTextReturnToSpaceValue");
    }

    /** used by SGVideoChannel */
    public static int SGSetVideoCompressor(int paramInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("SGSetVideoCompressor");
    }

    /** used by SGVideoChannel */
    public static int SGSetVideoCompressorType(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetVideoCompressorType");
    }

    /** used by SGVideoChannel */
    public static int SGSetVideoDigitizerComponent(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGSetVideoDigitizerComponent");
    }

    /** used by SGVideoChannel */
    public static int SGSetVideoRect(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SGSetVideoRect");
    }

    /** used by SGChannel */
    public static int SGSettingsDialog(int paramInt1, int paramInt2, short paramShort, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("SGSettingsDialog");
    }

    /** used by SGSoundChannel */
    public static int SGSoundInputDriverChanged(int paramInt) {
        throw new UnsupportedToolboxException("SGSoundInputDriverChanged");
    }

    /** used by SequenceGrabber */
    public static int SGStartPreview(int paramInt) {
        throw new UnsupportedToolboxException("SGStartPreview");
    }

    /** used by SequenceGrabber */
    public static int SGStartRecord(int paramInt) {
        throw new UnsupportedToolboxException("SGStartRecord");
    }

    /** used by SequenceGrabber */
    public static int SGStop(int paramInt) {
        throw new UnsupportedToolboxException("SGStop");
    }

    /** used by SequenceGrabber */
    public static int SGUpdate(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SGUpdate");
    }

    /** used by SGVideoChannel */
    public static int SGVideoDigitizerChanged(int paramInt) {
        throw new UnsupportedToolboxException("SGVideoDigitizerChanged");
    }

    /** used by SequenceGrabber */
    public static int SGWriteExtendedMovieData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5) {
        throw new UnsupportedToolboxException("SGWriteExtendedMovieData");
    }

    /** used by VideoDigitizer */
    public static int VDClearClipRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("VDClearClipRgn");
    }

    /** used by VideoDigitizer */
    public static int VDGetClipState(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("VDGetClipState");
    }

    /** used by VideoDigitizer */
    public static int VDGetDigitizerInfo(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("VDGetDigitizerInfo");
    }

    /** used by VideoDigitizer */
    public static int VDGetInput(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("VDGetInput");
    }

    /** used by VideoDigitizer */
    public static int VDGetInputFormat(int paramInt, short paramShort, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("VDGetInputFormat");
    }

    /** used by VideoDigitizer */
    public static int VDGetNumberOfInputs(int paramInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("VDGetNumberOfInputs");
    }

    /** used by VideoDigitizer */
    public static int VDSetClipRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("VDSetClipRgn");
    }

    /** used by VideoDigitizer */
    public static int VDSetClipState(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("VDSetClipState");
    }

    /** used by VideoDigitizer */
    public static int VDSetInput(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("VDSetInput");
    }

    /** used by VideoDigitizer */
    public static int VDSetInputStandard(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("VDSetInputStandard");
    }

}
