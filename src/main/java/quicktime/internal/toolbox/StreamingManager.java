/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;


/**
 * StreamingManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class StreamingManager {

    private StreamingManager() {
    }

    /** used by QTObjectManagement */
    public static short QTSDisposePresentation(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSDisposePresentation");
    }

    /** used by QTObjectManagement */
    public static short QTSDisposeStream(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSDisposeStream");
    }

    /** used by Stream */
    public static int QTSGetStreamPresentation(int paramInt) {
        throw new UnsupportedToolboxException("QTSGetStreamPresentation");
    }

    /** used by Presentation */
    public static short QTSNewPresentation(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSNewPresentation");
    }

    /** used by Presentation */
    public static short QTSNewPresentationFromData(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSNewPresentationFromData");
    }

    /** used by Presentation */
    public static short QTSNewPresentationFromDataRef(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSNewPresentationFromDataRef");
    }

    /** used by Presentation */
    public static short QTSNewPresentationFromFile(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSNewPresentationFromFile");
    }

    /** used by Presentation */
    public static short QTSPresAddSourcer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresAddSourcer");
    }

    /** used by Presentation */
    public static short QTSPresExport(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresExport");
    }

    /** used by Presentation */
    public static short QTSPresGetActiveSegment(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2) {
        throw new UnsupportedToolboxException("QTSPresGetActiveSegment");
    }

    /** used by Presentation, Region */
    public static short QTSPresGetClip(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetClip");
    }

    /** used by Presentation */
    public static short QTSPresGetDimensions(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTSPresGetDimensions");
    }

    /** used by Presentation */
    public static short QTSPresGetEnable(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresGetEnable");
    }

    /** used by Presentation */
    public static short QTSPresGetFlags(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetFlags");
    }

    /** used by Presentation, QDGraphics */
    public static short QTSPresGetGWorld(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTSPresGetGWorld");
    }

    /** used by Presentation */
    public static short QTSPresGetGraphicsMode(int paramInt1, int paramInt2, short[] paramArrayOfShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresGetGraphicsMode");
    }

    /** used by Presentation */
    public static short QTSPresGetIndSourcer(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetIndSourcer");
    }

    /** used by Presentation */
    public static int QTSPresGetIndStream(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSPresGetIndStream");
    }

    /** used by Presentation */
    public static short QTSPresGetInfo(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresGetInfo");
    }

    /** used by Presentation */
    public static short QTSPresGetInfo(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetInfo");
    }

    /** used by Presentation */
    public static short QTSPresGetNotificationProc(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetNotificationProc");
    }

    /** used by Presentation */
    public static short QTSPresGetNumSourcers(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSPresGetNumSourcers");
    }

    /** used by Presentation */
    public static int QTSPresGetNumStreams(int paramInt) {
        throw new UnsupportedToolboxException("QTSPresGetNumStreams");
    }

    /** used by Pict */
    public static short QTSPresGetPicture(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetPicture");
    }

    /** used by Presentation */
    public static short QTSPresGetPlayHints(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetPlayHints");
    }

    /** used by Presentation */
    public static short QTSPresGetPreferredRate(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetPreferredRate");
    }

    /** used by Presentation */
    public static short QTSPresGetPresenting(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresGetPresenting");
    }

    /** used by TimeBase */
    public static short QTSPresGetTimeBase(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetTimeBase");
    }

    /** used by Presentation */
    public static short QTSPresGetTimeScale(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresGetTimeScale");
    }

    /** used by Presentation */
    public static short QTSPresGetVolumes(int paramInt1, int paramInt2, short[] paramArrayOfShort1, short[] paramArrayOfShort2) {
        throw new UnsupportedToolboxException("QTSPresGetVolumes");
    }

    /** used by Presentation */
    public static short QTSPresHasCharacteristic(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresHasCharacteristic");
    }

    /** used by Presentation */
    public static void QTSPresIdle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSPresIdle");
    }

    /** used by Presentation */
    public static short QTSPresInvalidateRegion(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSPresInvalidateRegion");
    }

    /** used by Stream */
    public static short QTSPresNewStream(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        throw new UnsupportedToolboxException("QTSPresNewStream");
    }

    /** used by Presentation */
    public static short QTSPresPreroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("QTSPresPreroll");
    }

    /** used by Presentation */
    public static short QTSPresPreroll64(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresPreroll64");
    }

    /** used by Presentation */
    public static short QTSPresPreview(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresPreview");
    }

    /** used by Presentation */
    public static short QTSPresRemoveSourcer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresRemoveSourcer");
    }

    /** used by Presentation */
    public static short QTSPresSetActiveSegment(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2) {
        throw new UnsupportedToolboxException("QTSPresSetActiveSegment");
    }

    /** used by Presentation */
    public static short QTSPresSetClip(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTSPresSetClip");
    }

    /** used by Presentation */
    public static short QTSPresSetDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresSetDimensions");
    }

    /** used by Presentation */
    public static short QTSPresSetEnable(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("QTSPresSetEnable");
    }

    /** used by Presentation */
    public static short QTSPresSetFlags(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTSPresSetFlags");
    }

    /** used by Presentation */
    public static short QTSPresSetGWorld(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresSetGWorld");
    }

    /** used by Presentation */
    public static short QTSPresSetGraphicsMode(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresSetGraphicsMode");
    }

    /** used by Presentation */
    public static short QTSPresSetInfo(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSPresSetInfo");
    }

    /** used by Presentation */
    public static short QTSPresSetNotificationProc(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSPresSetNotificationProc");
    }

    /** used by Presentation */
    public static short QTSPresSetPlayHints(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTSPresSetPlayHints");
    }

    /** used by Presentation */
    public static short QTSPresSetPreferredRate(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTSPresSetPreferredRate");
    }

    /** used by Presentation */
    public static short QTSPresSetPresenting(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("QTSPresSetPresenting");
    }

    /** used by Presentation */
    public static short QTSPresSetVolumes(int paramInt1, int paramInt2, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("QTSPresSetVolumes");
    }

    /** used by SettingsDialog */
    public static short QTSPresSettingsDialog(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        throw new UnsupportedToolboxException("QTSPresSettingsDialog");
    }

    /** used by Presentation */
    public static short QTSPresSkipTo(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSPresSkipTo");
    }

    /** used by Presentation */
    public static short QTSPresSkipTo64(int paramInt, long[] paramArrayOfLong) {
        throw new UnsupportedToolboxException("QTSPresSkipTo64");
    }

    /** used by Presentation */
    public static short QTSPresStart(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTSPresStart");
    }

    /** used by Presentation */
    public static short QTSPresStop(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTSPresStop");
    }

    /** used by Sourcer */
    public static int QTSSourcerGetEnable(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        throw new UnsupportedToolboxException("QTSSourcerGetEnable");
    }

    /** used by Sourcer */
    public static int QTSSourcerGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("QTSSourcerGetInfo");
    }

    /** used by Sourcer */
    public static int QTSSourcerGetTimeScale(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSSourcerGetTimeScale");
    }

    /** used by Sourcer */
    public static int QTSSourcerIdle(int paramInt1, long paramLong, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTSSourcerIdle");
    }

    /** used by Sourcer */
    public static int QTSSourcerInitialize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSSourcerInitialize");
    }

    /** used by Sourcer */
    public static int QTSSourcerSetEnable(int paramInt1, byte paramByte, int paramInt2) {
        throw new UnsupportedToolboxException("QTSSourcerSetEnable");
    }

    /** used by Sourcer */
    public static int QTSSourcerSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("QTSSourcerSetInfo");
    }

    /** used by Sourcer */
    public static int QTSSourcerSetTimeScale(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTSSourcerSetTimeScale");
    }

}
