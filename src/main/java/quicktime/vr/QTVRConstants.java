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

    /**
     * QTVR hot spot types
     */
    public static final int kQTVRHotSpotLinkType = 1818848875;

    /**
     * QTVR hot spot types
     */
    public static final int kQTVRHotSpotURLType = 1970433056;

    /**
     * QTVR hot spot types
     */
    public static final int kQTVRHotSpotUndefinedType = 1970168934;

    /**
     * Special Values for nodeID in QTVRGoToNodeID
     */
    public static final int kQTVRCurrentNode = 0;

    /**
     * Special Values for nodeID in QTVRGoToNodeID
     */
    public static final int kQTVRPreviousNode = Integer.MIN_VALUE;

    /**
     * Special Values for nodeID in QTVRGoToNodeID
     */
    public static final int kQTVRDefaultNode = -2147483647;

    /**
     * Panorama correction modes used for the kQTVRImagingCorrection imaging property
     */
    public static final int kQTVRNoCorrection = 0;

    /**
     * Panorama correction modes used for the kQTVRImagingCorrection imaging property
     */
    public static final int kQTVRPartialCorrection = 1;

    /**
     * Panorama correction modes used for the kQTVRImagingCorrection imaging property
     */
    public static final int kQTVRFullCorrection = 2;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    public static final int kQTVRStatic = 1;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    public static final int kQTVRMotion = 2;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    public static final int kQTVRCurrentMode = 0;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    public static final int kQTVRAllModes = 100;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    public static final int kQTVRImagingCorrection = 1;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    public static final int kQTVRImagingQuality = 2;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    public static final int kQTVRImagingDirectDraw = 3;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    public static final int kQTVRImagingCurrentMode = 100;

    /**
     * OR the above with kImagingDefaultValue to get/set the default value
     */
    public static final int kImagingDefaultValue = Integer.MIN_VALUE;

    /**
     * Transition Types used by QTVRSetTransitionProperty, QTVREnableTransition
     */
    public static final int kQTVRTransitionSwing = 1;

    /**
     * Transition Properties QTVRSetTransitionProperty
     */
    public static final int kQTVRTransitionSpeed = 1;

    /**
     * Transition Properties QTVRSetTransitionProperty
     */
    public static final int kQTVRTransitionDirection = 2;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRUnconstrained = 0;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantPanLeft = 1;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantPanRight = 2;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantPanUp = 4;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantPanDown = 8;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantZoomIn = 16;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantZoomOut = 32;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantTranslateLeft = 64;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantTranslateRight = 128;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantTranslateUp = 256;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    public static final int kQTVRCantTranslateDown = 512;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    public static final int kQTVRPanning = 1;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    public static final int kQTVRTranslating = 2;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    public static final int kQTVRZooming = 4;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    public static final int kQTVRScrolling = 8;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    public static final int kQTVRSelecting = 16;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionMouseClickHysteresis = 1;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionMouseClickTimeout = 2;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionPanTiltSpeed = 3;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionZoomSpeed = 4;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionTranslateOnMouseDown = 101;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionMouseMotionScale = 102;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    public static final int kQTVRInteractionNudgeMode = 103;

    /**
     * OR the above with kQTVRInteractionDefaultValue to get/set the default value
     */
    public static final int kQTVRInteractionDefaultValue = Integer.MIN_VALUE;

    /**
     * Angular units used by QTVRSetAngularUnits
     */
    public static final int kQTVRDegrees = 0;

    /**
     * Angular units used by QTVRSetAngularUnits
     */
    public static final int kQTVRRadians = 1;

    /**
     * Values for enableFlag parameter in QTVREnableHotSpot
     */
    public static final int kQTVRHotSpotID = 0;

    /**
     * Values for enableFlag parameter in QTVREnableHotSpot
     */
    public static final int kQTVRHotSpotType = 1;

    /**
     * Values for enableFlag parameter in QTVREnableHotSpot
     */
    public static final int kQTVRAllHotSpots = 2;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    public static final int kQTVRPan = 0;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    public static final int kQTVRTilt = 1;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    public static final int kQTVRFieldOfView = 2;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    public static final int kQTVRViewCenterH = 4;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    public static final int kQTVRViewCenterV = 5;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    public static final int kQTVRPalindromeViewFrames = 1;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    public static final int kQTVRStartFirstViewFrame = 2;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    public static final int kQTVRDontLoopViewFrames = 3;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    public static final int kQTVRPlayEveryViewFrame = 4;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    public static final int kQTVRSyncViewToFrameRate = 16;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    public static final int kQTVRPalindromeViews = 17;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
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

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRSuppressBackBtn = 65536;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRSuppressZoomBtns = 131072;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRSuppressHotSpotBtn = 262144;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRSuppressTranslateBtn = 524288;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRSuppressHelpText = 1048576;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRSuppressHotSpotNames = 2097152;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    public static final int mcFlagQTVRExplicitFlagSet = Integer.MIN_VALUE;

    /**
     * Cursor types used in type field of QTVRCursorRecord
     */
    public static final int kQTVRUseDefaultCursor = 0;

    /**
     * Cursor types used in type field of QTVRCursorRecord
     */
    public static final int kQTVRStdCursorType = 1;

    /**
     * Cursor types used in type field of QTVRCursorRecord
     */
    public static final int kQTVRColorCursorType = 2;

    /**
     * Values for flags parameter in QTVRMouseOverHotSpot callback
     */
    public static final int kQTVRHotSpotEnter = 0;

    /**
     * Values for flags parameter in QTVRMouseOverHotSpot callback
     */
    public static final int kQTVRHotSpotWithin = 1;

    /**
     * Values for flags parameter in QTVRMouseOverHotSpot callback
     */
    public static final int kQTVRHotSpotLeave = 2;

    /**
     * Values for flags parameter in QTVRSetPrescreenImagingCompleteProc
     */
    public static final int kQTVRPreScreenEveryIdle = 1;

    public static final int kQTControllerType = 1668577648;

    public static final int kQTControllerID = 1;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRWorldHeaderAtomType = 1987212131;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRImagingParentAtomType = 1768777584;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRPanoImagingAtomType = 1768779886;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRObjectImagingAtomType = 1768779618;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRNodeParentAtomType = 1987210864;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRNodeIDAtomType = 1987210857;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRNodeLocationAtomType = 1852600163;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRCursorParentAtomType = 1987208048;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRCursorAtomType = 1129665107;

    /**
     * VRWorld atom types
     */
    public static final int kQTVRColorCursorAtomType = 1668445042;

    /**
     * NodeInfo atom types
     */
    public static final int kQTVRNodeHeaderAtomType = 1852074084;

    /**
     * NodeInfo atom types
     */
    public static final int kQTVRHotSpotParentAtomType = 1752395873;

    /**
     * NodeInfo atom types
     */
    public static final int kQTVRHotSpotAtomType = 1752134771;

    /**
     * NodeInfo atom types
     */
    public static final int kQTVRHotSpotInfoAtomType = 1752394094;

    /**
     * NodeInfo atom types
     */
    public static final int kQTVRLinkInfoAtomType = 1818848875;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRStringAtomType = 1987212135;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRStringEncodingAtomType = 1987212133;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRPanoSampleDataAtomType = 1885626740;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRObjectInfoAtomType = 1868720745;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRImageTrackRefAtomType = 1768780914;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRHotSpotTrackRefAtomType = 1752396914;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRAngleRangeAtomType = 1634889319;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRTrackRefArrayAtomType = 1953654118;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRPanConstraintAtomType = 1885564782;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRTiltConstraintAtomType = 1952673646;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRFOVConstraintAtomType = 1717792622;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRCubicViewAtomType = 1668642423;

    /**
     * Miscellaneous atom types
     */
    public static final int kQTVRCubicFaceDataAtomType = 1668638305;

    public static final int kQTVRObjectInfoAtomID = 1;

    public static final int kQTVRObjectImageTrackRefAtomID = 1;

    public static final int kQTVRObjectHotSpotTrackRefAtomID = 1;

    /**
     * Track reference types
     */
    public static final int kQTVRImageTrackRefType = 1768777588;

    /**
     * Track reference types
     */
    public static final int kQTVRHotSpotTrackRefType = 1752134772;

    /**
     * Old hot spot types
     */
    public static final int kQTVRHotSpotNavigableType = 1851881063;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    public static final int kQTVRValidPan = 1;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    public static final int kQTVRValidTilt = 2;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    public static final int kQTVRValidFOV = 4;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    public static final int kQTVRValidViewCenter = 8;

    /**
     * Values for flags field in QTVRPanoSampleAtom
     */
    public static final int kQTVRPanoFlagHorizontal = 1;

    /**
     * Values for flags field in QTVRPanoSampleAtom
     */
    public static final int kQTVRPanoFlagLast = Integer.MIN_VALUE;

    /**
     * Values for locationFlags field in QTVRNodeLocationAtom
     */
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

