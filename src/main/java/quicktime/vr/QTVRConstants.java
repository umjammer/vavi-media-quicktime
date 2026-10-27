package quicktime.vr;

public interface QTVRConstants {

    int kQTVRSetPanAngleSelector = 8192;

    int kQTVRSetTiltAngleSelector = 8193;

    int kQTVRSetFieldOfViewSelector = 8194;

    int kQTVRSetViewCenterSelector = 8195;

    int kQTVRMouseEnterSelector = 8196;

    int kQTVRMouseWithinSelector = 8197;

    int kQTVRMouseLeaveSelector = 8198;

    int kQTVRMouseDownSelector = 8199;

    int kQTVRMouseStillDownSelector = 8200;

    int kQTVRMouseUpSelector = 8201;

    int kQTVRTriggerHotSpotSelector = 8202;

    int kQTVRGetHotSpotTypeSelector = 8203;

    int kQTVRControllerSubType = 1668577648;

    int kQTVRQTVRType = 1903457906;

    int kQTVRPanoramaType = 1885433455;

    int kQTVRObjectType = 1868720741;

    int kQTVROldPanoType = 1398042734;

    int kQTVROldObjectType = 1937010273;

    /**
     * QTVR hot spot types
     */
    int kQTVRHotSpotLinkType = 1818848875;

    /**
     * QTVR hot spot types
     */
    int kQTVRHotSpotURLType = 1970433056;

    /**
     * QTVR hot spot types
     */
    int kQTVRHotSpotUndefinedType = 1970168934;

    /**
     * Special Values for nodeID in QTVRGoToNodeID
     */
    int kQTVRCurrentNode = 0;

    /**
     * Special Values for nodeID in QTVRGoToNodeID
     */
    int kQTVRPreviousNode = Integer.MIN_VALUE;

    /**
     * Special Values for nodeID in QTVRGoToNodeID
     */
    int kQTVRDefaultNode = -2147483647;

    /**
     * Panorama correction modes used for the kQTVRImagingCorrection imaging property
     */
    int kQTVRNoCorrection = 0;

    /**
     * Panorama correction modes used for the kQTVRImagingCorrection imaging property
     */
    int kQTVRPartialCorrection = 1;

    /**
     * Panorama correction modes used for the kQTVRImagingCorrection imaging property
     */
    int kQTVRFullCorrection = 2;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    int kQTVRStatic = 1;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    int kQTVRMotion = 2;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    int kQTVRCurrentMode = 0;

    /**
     * Imaging Modes used by QTVRSetImagingProperty, QTVRGetImagingProperty, QTVRUpdate, QTVRBeginUpdate
     */
    int kQTVRAllModes = 100;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    int kQTVRImagingCorrection = 1;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    int kQTVRImagingQuality = 2;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    int kQTVRImagingDirectDraw = 3;

    /**
     * Imaging Properties used by QTVRSetImagingProperty, QTVRGetImagingProperty
     */
    int kQTVRImagingCurrentMode = 100;

    /**
     * OR the above with kImagingDefaultValue to get/set the default value
     */
    int kImagingDefaultValue = Integer.MIN_VALUE;

    /**
     * Transition Types used by QTVRSetTransitionProperty, QTVREnableTransition
     */
    int kQTVRTransitionSwing = 1;

    /**
     * Transition Properties QTVRSetTransitionProperty
     */
    int kQTVRTransitionSpeed = 1;

    /**
     * Transition Properties QTVRSetTransitionProperty
     */
    int kQTVRTransitionDirection = 2;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRUnconstrained = 0;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantPanLeft = 1;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantPanRight = 2;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantPanUp = 4;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantPanDown = 8;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantZoomIn = 16;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantZoomOut = 32;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantTranslateLeft = 64;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantTranslateRight = 128;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantTranslateUp = 256;

    /**
     * Constraint values used to construct value returned by GetConstraintStatus
     */
    int kQTVRCantTranslateDown = 512;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    int kQTVRPanning = 1;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    int kQTVRTranslating = 2;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    int kQTVRZooming = 4;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    int kQTVRScrolling = 8;

    /**
     * Object-only mouse mode values used to construct value returned by QTVRGetCurrentMouseMode
     */
    int kQTVRSelecting = 16;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionMouseClickHysteresis = 1;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionMouseClickTimeout = 2;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionPanTiltSpeed = 3;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionZoomSpeed = 4;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionTranslateOnMouseDown = 101;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionMouseMotionScale = 102;

    /**
     * Properties for use with QTVRSetInteractionProperty/GetInteractionProperty
     */
    int kQTVRInteractionNudgeMode = 103;

    /**
     * OR the above with kQTVRInteractionDefaultValue to get/set the default value
     */
    int kQTVRInteractionDefaultValue = Integer.MIN_VALUE;

    /**
     * Angular units used by QTVRSetAngularUnits
     */
    int kQTVRDegrees = 0;

    /**
     * Angular units used by QTVRSetAngularUnits
     */
    int kQTVRRadians = 1;

    /**
     * Values for enableFlag parameter in QTVREnableHotSpot
     */
    int kQTVRHotSpotID = 0;

    /**
     * Values for enableFlag parameter in QTVREnableHotSpot
     */
    int kQTVRHotSpotType = 1;

    /**
     * Values for enableFlag parameter in QTVREnableHotSpot
     */
    int kQTVRAllHotSpots = 2;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    int kQTVRPan = 0;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    int kQTVRTilt = 1;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    int kQTVRFieldOfView = 2;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    int kQTVRViewCenterH = 4;

    /**
     * Values for kind parameter in QTVRGet/SetConstraints, QTVRGetViewingLimits
     */
    int kQTVRViewCenterV = 5;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRPalindromeViewFrames = 1;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRStartFirstViewFrame = 2;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRDontLoopViewFrames = 3;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRPlayEveryViewFrame = 4;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRSyncViewToFrameRate = 16;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRPalindromeViews = 17;

    /**
     * Values for setting parameter in QTVRSetAnimationSetting, QTVRGetAnimationSetting
     * View Frame Animation Settings
     */
    int kQTVRPlayStreamingViews = 18;

    int kQTVRWrapPan = 1;

    int kQTVRWrapTilt = 2;

    int kQTVRCanZoom = 3;

    int kQTVRReverseHControl = 4;

    int kQTVRReverseVControl = 5;

    int kQTVRSwapHVControl = 6;

    int kQTVRTranslation = 7;

    int kQTVRDefault = 0;

    int kQTVRCurrent = 2;

    int kQTVRMouseDown = 3;

    int kQTVRRight = 0;

    int kQTVRUpRight = 45;

    int kQTVRUp = 90;

    int kQTVRUpLeft = 135;

    int kQTVRLeft = 180;

    int kQTVRDownLeft = 225;

    int kQTVRDown = 270;

    int kQTVRDownRight = 315;

    int kQTVRNudgeRotate = 0;

    int kQTVRNudgeTranslate = 1;

    int kQTVRNudgeSameAsMouse = 2;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRSuppressBackBtn = 65536;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRSuppressZoomBtns = 131072;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRSuppressHotSpotBtn = 262144;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRSuppressTranslateBtn = 524288;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRSuppressHelpText = 1048576;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRSuppressHotSpotNames = 2097152;

    /**
     * Flags to control elements of the QTVR control bar (set via mcActionSetFlags)
     */
    int mcFlagQTVRExplicitFlagSet = Integer.MIN_VALUE;

    /**
     * Cursor types used in type field of QTVRCursorRecord
     */
    int kQTVRUseDefaultCursor = 0;

    /**
     * Cursor types used in type field of QTVRCursorRecord
     */
    int kQTVRStdCursorType = 1;

    /**
     * Cursor types used in type field of QTVRCursorRecord
     */
    int kQTVRColorCursorType = 2;

    /**
     * Values for flags parameter in QTVRMouseOverHotSpot callback
     */
    int kQTVRHotSpotEnter = 0;

    /**
     * Values for flags parameter in QTVRMouseOverHotSpot callback
     */
    int kQTVRHotSpotWithin = 1;

    /**
     * Values for flags parameter in QTVRMouseOverHotSpot callback
     */
    int kQTVRHotSpotLeave = 2;

    /**
     * Values for flags parameter in QTVRSetPrescreenImagingCompleteProc
     */
    int kQTVRPreScreenEveryIdle = 1;

    int kQTControllerType = 1668577648;

    int kQTControllerID = 1;

    /**
     * VRWorld atom types
     */
    int kQTVRWorldHeaderAtomType = 1987212131;

    /**
     * VRWorld atom types
     */
    int kQTVRImagingParentAtomType = 1768777584;

    /**
     * VRWorld atom types
     */
    int kQTVRPanoImagingAtomType = 1768779886;

    /**
     * VRWorld atom types
     */
    int kQTVRObjectImagingAtomType = 1768779618;

    /**
     * VRWorld atom types
     */
    int kQTVRNodeParentAtomType = 1987210864;

    /**
     * VRWorld atom types
     */
    int kQTVRNodeIDAtomType = 1987210857;

    /**
     * VRWorld atom types
     */
    int kQTVRNodeLocationAtomType = 1852600163;

    /**
     * VRWorld atom types
     */
    int kQTVRCursorParentAtomType = 1987208048;

    /**
     * VRWorld atom types
     */
    int kQTVRCursorAtomType = 1129665107;

    /**
     * VRWorld atom types
     */
    int kQTVRColorCursorAtomType = 1668445042;

    /**
     * NodeInfo atom types
     */
    int kQTVRNodeHeaderAtomType = 1852074084;

    /**
     * NodeInfo atom types
     */
    int kQTVRHotSpotParentAtomType = 1752395873;

    /**
     * NodeInfo atom types
     */
    int kQTVRHotSpotAtomType = 1752134771;

    /**
     * NodeInfo atom types
     */
    int kQTVRHotSpotInfoAtomType = 1752394094;

    /**
     * NodeInfo atom types
     */
    int kQTVRLinkInfoAtomType = 1818848875;

    /**
     * Miscellaneous atom types
     */
    int kQTVRStringAtomType = 1987212135;

    /**
     * Miscellaneous atom types
     */
    int kQTVRStringEncodingAtomType = 1987212133;

    /**
     * Miscellaneous atom types
     */
    int kQTVRPanoSampleDataAtomType = 1885626740;

    /**
     * Miscellaneous atom types
     */
    int kQTVRObjectInfoAtomType = 1868720745;

    /**
     * Miscellaneous atom types
     */
    int kQTVRImageTrackRefAtomType = 1768780914;

    /**
     * Miscellaneous atom types
     */
    int kQTVRHotSpotTrackRefAtomType = 1752396914;

    /**
     * Miscellaneous atom types
     */
    int kQTVRAngleRangeAtomType = 1634889319;

    /**
     * Miscellaneous atom types
     */
    int kQTVRTrackRefArrayAtomType = 1953654118;

    /**
     * Miscellaneous atom types
     */
    int kQTVRPanConstraintAtomType = 1885564782;

    /**
     * Miscellaneous atom types
     */
    int kQTVRTiltConstraintAtomType = 1952673646;

    /**
     * Miscellaneous atom types
     */
    int kQTVRFOVConstraintAtomType = 1717792622;

    /**
     * Miscellaneous atom types
     */
    int kQTVRCubicViewAtomType = 1668642423;

    /**
     * Miscellaneous atom types
     */
    int kQTVRCubicFaceDataAtomType = 1668638305;

    int kQTVRObjectInfoAtomID = 1;

    int kQTVRObjectImageTrackRefAtomID = 1;

    int kQTVRObjectHotSpotTrackRefAtomID = 1;

    /**
     * Track reference types
     */
    int kQTVRImageTrackRefType = 1768777588;

    /**
     * Track reference types
     */
    int kQTVRHotSpotTrackRefType = 1752134772;

    /**
     * Old hot spot types
     */
    int kQTVRHotSpotNavigableType = 1851881063;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    int kQTVRValidPan = 1;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    int kQTVRValidTilt = 2;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    int kQTVRValidFOV = 4;

    /**
     * Valid bits used in QTVRLinkHotSpotAtom
     */
    int kQTVRValidViewCenter = 8;

    /**
     * Values for flags field in QTVRPanoSampleAtom
     */
    int kQTVRPanoFlagHorizontal = 1;

    /**
     * Values for flags field in QTVRPanoSampleAtom
     */
    int kQTVRPanoFlagLast = Integer.MIN_VALUE;

    /**
     * Values for locationFlags field in QTVRNodeLocationAtom
     */
    int kQTVRSameFile = 0;

    int kQTVRPanAngle = 256;

    int kQTVRTiltAngle = 257;

    int kQTVRFieldOfViewAngle = 259;

    int kQTVRViewCenter = 260;

    int kQTVRHotSpotsVisible = 512;

    int kQTVRSetViewParameterSelector = 8204;

    int kQTVRGetViewParameterSelector = 8205;

    int kQTVRValueIsRelative = 1;

    int kQTVRValueIsRate = 2;

    int kQTVRValueIsUserPrefRelative = 4;
}

