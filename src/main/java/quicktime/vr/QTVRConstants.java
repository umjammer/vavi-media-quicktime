
package quicktime.vr;

public abstract interface QTVRConstants {
    public static final int kQTVRSetPanAngleSelector = 8192;

    public static final int kQTVRSetTiltAngleSelector = 8193;

    public static final int kQTVRSetFieldOfViewSelector = 8194;

    public static final int kQTVRSetViewCenterSelector = 8195;

    public static final int kQTVRMouseEnterSelector = 8196;

    public static final int kQTVRMouseWithinSelector = 8197;

    public static final int kQTVRMouseLeaveSelector = 8198;

    public static final int kQTVRMouseDownSelector = 8199;

    public static final int kQTVRMouseStillDownSelector = 8200;

    public static final int kQTVRMouseUpSelector = 8201;

    public static final int kQTVRTriggerHotSpotSelector = 8202;

    public static final int kQTVRGetHotSpotTypeSelector = 8203;

    public static final int kQTVRControllerSubType = 1668577648;

    public static final int kQTVRQTVRType = 1903457906;

    public static final int kQTVRPanoramaType = 1885433455;

    public static final int kQTVRObjectType = 1868720741;

    public static final int kQTVROldPanoType = 1398042734;

    public static final int kQTVROldObjectType = 1937010273;

    public static final int kQTVRHotSpotLinkType = 1818848875;

    public static final int kQTVRHotSpotURLType = 1970433056;

    public static final int kQTVRHotSpotUndefinedType = 1970168934;

    public static final int kQTVRCurrentNode = 0;

    public static final int kQTVRPreviousNode = Integer.MIN_VALUE;

    public static final int kQTVRDefaultNode = -2147483647;

    public static final int kQTVRNoCorrection = 0;

    public static final int kQTVRPartialCorrection = 1;

    public static final int kQTVRFullCorrection = 2;

    public static final int kQTVRStatic = 1;

    public static final int kQTVRMotion = 2;

    public static final int kQTVRCurrentMode = 0;

    public static final int kQTVRAllModes = 100;

    public static final int kQTVRImagingCorrection = 1;

    public static final int kQTVRImagingQuality = 2;

    public static final int kQTVRImagingDirectDraw = 3;

    public static final int kQTVRImagingCurrentMode = 100;

    public static final int kImagingDefaultValue = Integer.MIN_VALUE;

    public static final int kQTVRTransitionSwing = 1;

    public static final int kQTVRTransitionSpeed = 1;

    public static final int kQTVRTransitionDirection = 2;

    public static final int kQTVRUnconstrained = 0;

    public static final int kQTVRCantPanLeft = 1;

    public static final int kQTVRCantPanRight = 2;

    public static final int kQTVRCantPanUp = 4;

    public static final int kQTVRCantPanDown = 8;

    public static final int kQTVRCantZoomIn = 16;

    public static final int kQTVRCantZoomOut = 32;

    public static final int kQTVRCantTranslateLeft = 64;

    public static final int kQTVRCantTranslateRight = 128;

    public static final int kQTVRCantTranslateUp = 256;

    public static final int kQTVRCantTranslateDown = 512;

    public static final int kQTVRPanning = 1;

    public static final int kQTVRTranslating = 2;

    public static final int kQTVRZooming = 4;

    public static final int kQTVRScrolling = 8;

    public static final int kQTVRSelecting = 16;

    public static final int kQTVRInteractionMouseClickHysteresis = 1;

    public static final int kQTVRInteractionMouseClickTimeout = 2;

    public static final int kQTVRInteractionPanTiltSpeed = 3;

    public static final int kQTVRInteractionZoomSpeed = 4;

    public static final int kQTVRInteractionTranslateOnMouseDown = 101;

    public static final int kQTVRInteractionMouseMotionScale = 102;

    public static final int kQTVRInteractionNudgeMode = 103;

    public static final int kQTVRInteractionDefaultValue = Integer.MIN_VALUE;

    public static final int kQTVRDegrees = 0;

    public static final int kQTVRRadians = 1;

    public static final int kQTVRHotSpotID = 0;

    public static final int kQTVRHotSpotType = 1;

    public static final int kQTVRAllHotSpots = 2;

    public static final int kQTVRPan = 0;

    public static final int kQTVRTilt = 1;

    public static final int kQTVRFieldOfView = 2;

    public static final int kQTVRViewCenterH = 4;

    public static final int kQTVRViewCenterV = 5;

    public static final int kQTVRPalindromeViewFrames = 1;

    public static final int kQTVRStartFirstViewFrame = 2;

    public static final int kQTVRDontLoopViewFrames = 3;

    public static final int kQTVRPlayEveryViewFrame = 4;

    public static final int kQTVRSyncViewToFrameRate = 16;

    public static final int kQTVRPalindromeViews = 17;

    public static final int kQTVRPlayStreamingViews = 18;

    public static final int kQTVRWrapPan = 1;

    public static final int kQTVRWrapTilt = 2;

    public static final int kQTVRCanZoom = 3;

    public static final int kQTVRReverseHControl = 4;

    public static final int kQTVRReverseVControl = 5;

    public static final int kQTVRSwapHVControl = 6;

    public static final int kQTVRTranslation = 7;

    public static final int kQTVRDefault = 0;

    public static final int kQTVRCurrent = 2;

    public static final int kQTVRMouseDown = 3;

    public static final int kQTVRRight = 0;

    public static final int kQTVRUpRight = 45;

    public static final int kQTVRUp = 90;

    public static final int kQTVRUpLeft = 135;

    public static final int kQTVRLeft = 180;

    public static final int kQTVRDownLeft = 225;

    public static final int kQTVRDown = 270;

    public static final int kQTVRDownRight = 315;

    public static final int kQTVRNudgeRotate = 0;

    public static final int kQTVRNudgeTranslate = 1;

    public static final int kQTVRNudgeSameAsMouse = 2;

    public static final int mcFlagQTVRSuppressBackBtn = 65536;

    public static final int mcFlagQTVRSuppressZoomBtns = 131072;

    public static final int mcFlagQTVRSuppressHotSpotBtn = 262144;

    public static final int mcFlagQTVRSuppressTranslateBtn = 524288;

    public static final int mcFlagQTVRSuppressHelpText = 1048576;

    public static final int mcFlagQTVRSuppressHotSpotNames = 2097152;

    public static final int mcFlagQTVRExplicitFlagSet = Integer.MIN_VALUE;

    public static final int kQTVRUseDefaultCursor = 0;

    public static final int kQTVRStdCursorType = 1;

    public static final int kQTVRColorCursorType = 2;

    public static final int kQTVRHotSpotEnter = 0;

    public static final int kQTVRHotSpotWithin = 1;

    public static final int kQTVRHotSpotLeave = 2;

    public static final int kQTVRPreScreenEveryIdle = 1;

    public static final int kQTControllerType = 1668577648;

    public static final int kQTControllerID = 1;

    public static final int kQTVRWorldHeaderAtomType = 1987212131;

    public static final int kQTVRImagingParentAtomType = 1768777584;

    public static final int kQTVRPanoImagingAtomType = 1768779886;

    public static final int kQTVRObjectImagingAtomType = 1768779618;

    public static final int kQTVRNodeParentAtomType = 1987210864;

    public static final int kQTVRNodeIDAtomType = 1987210857;

    public static final int kQTVRNodeLocationAtomType = 1852600163;

    public static final int kQTVRCursorParentAtomType = 1987208048;

    public static final int kQTVRCursorAtomType = 1129665107;

    public static final int kQTVRColorCursorAtomType = 1668445042;

    public static final int kQTVRNodeHeaderAtomType = 1852074084;

    public static final int kQTVRHotSpotParentAtomType = 1752395873;

    public static final int kQTVRHotSpotAtomType = 1752134771;

    public static final int kQTVRHotSpotInfoAtomType = 1752394094;

    public static final int kQTVRLinkInfoAtomType = 1818848875;

    public static final int kQTVRStringAtomType = 1987212135;

    public static final int kQTVRStringEncodingAtomType = 1987212133;

    public static final int kQTVRPanoSampleDataAtomType = 1885626740;

    public static final int kQTVRObjectInfoAtomType = 1868720745;

    public static final int kQTVRImageTrackRefAtomType = 1768780914;

    public static final int kQTVRHotSpotTrackRefAtomType = 1752396914;

    public static final int kQTVRAngleRangeAtomType = 1634889319;

    public static final int kQTVRTrackRefArrayAtomType = 1953654118;

    public static final int kQTVRPanConstraintAtomType = 1885564782;

    public static final int kQTVRTiltConstraintAtomType = 1952673646;

    public static final int kQTVRFOVConstraintAtomType = 1717792622;

    public static final int kQTVRCubicViewAtomType = 1668642423;

    public static final int kQTVRCubicFaceDataAtomType = 1668638305;

    public static final int kQTVRObjectInfoAtomID = 1;

    public static final int kQTVRObjectImageTrackRefAtomID = 1;

    public static final int kQTVRObjectHotSpotTrackRefAtomID = 1;

    public static final int kQTVRImageTrackRefType = 1768777588;

    public static final int kQTVRHotSpotTrackRefType = 1752134772;

    public static final int kQTVRHotSpotNavigableType = 1851881063;

    public static final int kQTVRValidPan = 1;

    public static final int kQTVRValidTilt = 2;

    public static final int kQTVRValidFOV = 4;

    public static final int kQTVRValidViewCenter = 8;

    public static final int kQTVRPanoFlagHorizontal = 1;

    public static final int kQTVRPanoFlagLast = Integer.MIN_VALUE;

    public static final int kQTVRSameFile = 0;

    public static final int kQTVRPanAngle = 256;

    public static final int kQTVRTiltAngle = 257;

    public static final int kQTVRFieldOfViewAngle = 259;

    public static final int kQTVRViewCenter = 260;

    public static final int kQTVRHotSpotsVisible = 512;

    public static final int kQTVRSetViewParameterSelector = 8204;

    public static final int kQTVRGetViewParameterSelector = 8205;

    public static final int kQTVRValueIsRelative = 1;

    public static final int kQTVRValueIsRate = 2;

    public static final int kQTVRValueIsUserPrefRelative = 4;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRConstants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
