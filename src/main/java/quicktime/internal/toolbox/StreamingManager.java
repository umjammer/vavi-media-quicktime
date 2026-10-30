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
    public static short QTSDisposePresentation(int presentation, int flags) {
        throw new UnsupportedToolboxException("QTSDisposePresentation");
    }

    /** used by QTObjectManagement */
    public static short QTSDisposeStream(int stream, int flags) {
        throw new UnsupportedToolboxException("QTSDisposeStream");
    }

    /** used by Stream */
    public static int QTSGetStreamPresentation(int stream) {
        throw new UnsupportedToolboxException("QTSGetStreamPresentation");
    }

    /** used by Presentation */
    public static short QTSNewPresentation(int newPresentationParams, int[] outPresentation) {
        throw new UnsupportedToolboxException("QTSNewPresentation");
    }

    /** used by Presentation */
    public static short QTSNewPresentationFromData(int dataType, int data, long[] dataLength, int presParams, int[] outPresentation) {
        throw new UnsupportedToolboxException("QTSNewPresentationFromData");
    }

    /** used by Presentation */
    public static short QTSNewPresentationFromDataRef(int dataRef, int dataRefType, int presParams, int[] outPresentation) {
        throw new UnsupportedToolboxException("QTSNewPresentationFromDataRef");
    }

    /** used by Presentation */
    public static short QTSNewPresentationFromFile(byte[] fileSpec, int presParams, int[] outPresentation) {
        throw new UnsupportedToolboxException("QTSNewPresentationFromFile");
    }

    /** used by Presentation */
    public static short QTSPresAddSourcer(int presentation, int stream, int sourcer, int flags) {
        throw new UnsupportedToolboxException("QTSPresAddSourcer");
    }

    /** used by Presentation */
    public static short QTSPresExport(int presentation, int stream, byte[] exportParams) {
        throw new UnsupportedToolboxException("QTSPresExport");
    }

    /** used by Presentation */
    public static short QTSPresGetActiveSegment(int presentation, int stream, long[] startTime, long[] duration) {
        throw new UnsupportedToolboxException("QTSPresGetActiveSegment");
    }

    /** used by Presentation, Region */
    public static short QTSPresGetClip(int presentation, int stream, int[] clip) {
        throw new UnsupportedToolboxException("QTSPresGetClip");
    }

    /** used by Presentation */
    public static short QTSPresGetDimensions(int presentation, int stream, int[] width, int[] height) {
        throw new UnsupportedToolboxException("QTSPresGetDimensions");
    }

    /** used by Presentation */
    public static short QTSPresGetEnable(int presentation, int stream, byte[] enableMode) {
        throw new UnsupportedToolboxException("QTSPresGetEnable");
    }

    /** used by Presentation */
    public static short QTSPresGetFlags(int presentation, int[] flags) {
        throw new UnsupportedToolboxException("QTSPresGetFlags");
    }

    /** used by Presentation, QDGraphics */
    public static short QTSPresGetGWorld(int presentation, int stream, int[] gWorld, int[] gdHandle) {
        throw new UnsupportedToolboxException("QTSPresGetGWorld");
    }

    /** used by Presentation */
    public static short QTSPresGetGraphicsMode(int presentation, int stream, short[] mode, byte[] opColor) {
        throw new UnsupportedToolboxException("QTSPresGetGraphicsMode");
    }

    /** used by Presentation */
    public static short QTSPresGetIndSourcer(int presentation, int stream, int index, int[] sourcer) {
        throw new UnsupportedToolboxException("QTSPresGetIndSourcer");
    }

    /** used by Presentation */
    public static int QTSPresGetIndStream(int presentation, int index) {
        throw new UnsupportedToolboxException("QTSPresGetIndStream");
    }

    /** used by Presentation */
    public static short QTSPresGetInfo(int presentation, int stream, int selector, byte[] param) {
        throw new UnsupportedToolboxException("QTSPresGetInfo");
    }

    /** used by Presentation */
    public static short QTSPresGetInfo(int presentation, int stream, int selector, int[] param) {
        throw new UnsupportedToolboxException("QTSPresGetInfo");
    }

    /** used by Presentation */
    public static short QTSPresGetNotificationProc(int presentation, int notificationProc, int[] refCon) {
        throw new UnsupportedToolboxException("QTSPresGetNotificationProc");
    }

    /** used by Presentation */
    public static short QTSPresGetNumSourcers(int presentation, int stream) {
        throw new UnsupportedToolboxException("QTSPresGetNumSourcers");
    }

    /** used by Presentation */
    public static int QTSPresGetNumStreams(int presentation) {
        throw new UnsupportedToolboxException("QTSPresGetNumStreams");
    }

    /** used by Pict */
    public static short QTSPresGetPicture(int presentation, int stream, int[] picture) {
        throw new UnsupportedToolboxException("QTSPresGetPicture");
    }

    /** used by Presentation */
    public static short QTSPresGetPlayHints(int presentation, int stream, int[] playHints) {
        throw new UnsupportedToolboxException("QTSPresGetPlayHints");
    }

    /** used by Presentation */
    public static short QTSPresGetPreferredRate(int presentation, int[] preferredRate) {
        throw new UnsupportedToolboxException("QTSPresGetPreferredRate");
    }

    /** used by Presentation */
    public static short QTSPresGetPresenting(int presentation, int stream, byte[] presenting) {
        throw new UnsupportedToolboxException("QTSPresGetPresenting");
    }

    /** used by TimeBase */
    public static short QTSPresGetTimeBase(int presentation, int[] timeBase) {
        throw new UnsupportedToolboxException("QTSPresGetTimeBase");
    }

    /** used by Presentation */
    public static short QTSPresGetTimeScale(int presentation, int[] timeScale) {
        throw new UnsupportedToolboxException("QTSPresGetTimeScale");
    }

    /** used by Presentation */
    public static short QTSPresGetVolumes(int presentation, int stream, short[] leftVolume, short[] rightVolume) {
        throw new UnsupportedToolboxException("QTSPresGetVolumes");
    }

    /** used by Presentation */
    public static short QTSPresHasCharacteristic(int presentation, int stream, int characteristic, byte[] hasIt) {
        throw new UnsupportedToolboxException("QTSPresHasCharacteristic");
    }

    /** used by Presentation */
    public static void QTSPresIdle(int presentation, int returnedEvent) {
        throw new UnsupportedToolboxException("QTSPresIdle");
    }

    /** used by Presentation */
    public static short QTSPresInvalidateRegion(int presentation, int region) {
        throw new UnsupportedToolboxException("QTSPresInvalidateRegion");
    }

    /** used by Stream */
    public static short QTSPresNewStream(int presentation, int dataType, int data, int dataLength, int flags, int outStream) {
        throw new UnsupportedToolboxException("QTSPresNewStream");
    }

    /** used by Presentation */
    public static short QTSPresPreroll(int presentation, int stream, int timeValue, int rate, int flags) {
        throw new UnsupportedToolboxException("QTSPresPreroll");
    }

    /** used by Presentation */
    public static short QTSPresPreroll64(int presentation, int stream, long[] preferredTime, int rate, int flags) {
        throw new UnsupportedToolboxException("QTSPresPreroll64");
    }

    /** used by Presentation */
    public static short QTSPresPreview(int presentation, int stream, long[] timeValue, int rate, int flags) {
        throw new UnsupportedToolboxException("QTSPresPreview");
    }

    /** used by Presentation */
    public static short QTSPresRemoveSourcer(int presentation, int stream, int sourcer, int flags) {
        throw new UnsupportedToolboxException("QTSPresRemoveSourcer");
    }

    /** used by Presentation */
    public static short QTSPresSetActiveSegment(int presentation, int stream, long[] startTime, long[] duration) {
        throw new UnsupportedToolboxException("QTSPresSetActiveSegment");
    }

    /** used by Presentation */
    public static short QTSPresSetClip(int presentation, int stream, int clip) {
        throw new UnsupportedToolboxException("QTSPresSetClip");
    }

    /** used by Presentation */
    public static short QTSPresSetDimensions(int presentation, int stream, int width, int height) {
        throw new UnsupportedToolboxException("QTSPresSetDimensions");
    }

    /** used by Presentation */
    public static short QTSPresSetEnable(int presentation, int stream, byte enableMode) {
        throw new UnsupportedToolboxException("QTSPresSetEnable");
    }

    /** used by Presentation */
    public static short QTSPresSetFlags(int presentation, int flags, int flagsMask) {
        throw new UnsupportedToolboxException("QTSPresSetFlags");
    }

    /** used by Presentation */
    public static short QTSPresSetGWorld(int presentation, int stream, int gWorld, int gdHandle) {
        throw new UnsupportedToolboxException("QTSPresSetGWorld");
    }

    /** used by Presentation */
    public static short QTSPresSetGraphicsMode(int presentation, int stream, short mode, byte[] opColor) {
        throw new UnsupportedToolboxException("QTSPresSetGraphicsMode");
    }

    /** used by Presentation */
    public static short QTSPresSetInfo(int presentation, int stream, int selector, byte[] param) {
        throw new UnsupportedToolboxException("QTSPresSetInfo");
    }

    /** used by Presentation */
    public static short QTSPresSetNotificationProc(int presentation, int notificationProc, int[] refCon) {
        throw new UnsupportedToolboxException("QTSPresSetNotificationProc");
    }

    /** used by Presentation */
    public static short QTSPresSetPlayHints(int presentation, int stream, int playHints, int playHintsMask) {
        throw new UnsupportedToolboxException("QTSPresSetPlayHints");
    }

    /** used by Presentation */
    public static short QTSPresSetPreferredRate(int presentation, int rate, int flags) {
        throw new UnsupportedToolboxException("QTSPresSetPreferredRate");
    }

    /** used by Presentation */
    public static short QTSPresSetPresenting(int presentation, int stream, byte presenting) {
        throw new UnsupportedToolboxException("QTSPresSetPresenting");
    }

    /** used by Presentation */
    public static short QTSPresSetVolumes(int presentation, int stream, short leftVolume, short rightVolume) {
        throw new UnsupportedToolboxException("QTSPresSetVolumes");
    }

    /** used by SettingsDialog */
    public static short QTSPresSettingsDialog(int presentation, int stream, int flags, int filterProc, int filterProcRefCon) {
        throw new UnsupportedToolboxException("QTSPresSettingsDialog");
    }

    /** used by Presentation */
    public static short QTSPresSkipTo(int presentation, int timeValue) {
        throw new UnsupportedToolboxException("QTSPresSkipTo");
    }

    /** used by Presentation */
    public static short QTSPresSkipTo64(int presentation, long[] timeValue) {
        throw new UnsupportedToolboxException("QTSPresSkipTo64");
    }

    /** used by Presentation */
    public static short QTSPresStart(int presentation, int stream, int flags) {
        throw new UnsupportedToolboxException("QTSPresStart");
    }

    /** used by Presentation */
    public static short QTSPresStop(int presentation, int stream, int flags) {
        throw new UnsupportedToolboxException("QTSPresStop");
    }

    /** used by Sourcer */
    public static int QTSSourcerGetEnable(int sourcer, byte[] enableMode, int flags) {
        throw new UnsupportedToolboxException("QTSSourcerGetEnable");
    }

    /** used by Sourcer */
    public static int QTSSourcerGetInfo(int sourcer, int selector, byte[] ioParams) {
        throw new UnsupportedToolboxException("QTSSourcerGetInfo");
    }

    /** used by Sourcer */
    public static int QTSSourcerGetTimeScale(int sourcer, int[] timeScale) {
        throw new UnsupportedToolboxException("QTSSourcerGetTimeScale");
    }

    /** used by Sourcer */
    public static int QTSSourcerIdle(int sourcer, long inTime, int inFlags, int[] outFlags) {
        throw new UnsupportedToolboxException("QTSSourcerIdle");
    }

    /** used by Sourcer */
    public static int QTSSourcerInitialize(int sourcer, int inInitParams) {
        throw new UnsupportedToolboxException("QTSSourcerInitialize");
    }

    /** used by Sourcer */
    public static int QTSSourcerSetEnable(int sourcer, byte enableMode, int flags) {
        throw new UnsupportedToolboxException("QTSSourcerSetEnable");
    }

    /** used by Sourcer */
    public static int QTSSourcerSetInfo(int sourcer, int selector, int ioParams) {
        throw new UnsupportedToolboxException("QTSSourcerSetInfo");
    }

    /** used by Sourcer */
    public static int QTSSourcerSetTimeScale(int sourcer, int timeScale) {
        throw new UnsupportedToolboxException("QTSSourcerSetTimeScale");
    }

}
