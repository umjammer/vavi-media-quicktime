package quicktime.std;

/**
 * This class represents all of the constants in the QuickTime interface.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public interface StdQTConstants {

    int codecCanScale = 1;

    int codecCanMask = 2;

    int codecCanMatte = 4;

    int codecCanTransform = 8;

    int codecCanTransferMode = 16;

    int codecCanCopyPrev = 32;

    int codecCanSpool = 64;

    int codecCanClipVertical = 128;

    int codecCanClipRectangular = 256;

    int codecCanRemapColor = 512;

    int codecCanFastDither = 1024;

    int codecCanSrcExtract = 2048;

    int codecCanCopyPrevComp = 4096;

    int codecCanAsync = 8192;

    int codecCanMakeMask = 16384;

    int codecCanShift = 32768;

    int codecCanAsyncWhen = 65536;

    int codecCanShieldCursor = 131072;

    int codecCanManagePrevBuffer = 262144;

    int codecHasVolatileBuffer = 524288;

    int codecWantsRegionMask = 1048576;

    int codecImageBufferIsOnScreen = 2097152;

    int codecWantsDestinationPixels = 4194304;

    int codecWantsSpecialScaling = 8388608;

    int codecHandlesInputs = 16777216;

    int codecCanDoIndirectSurface = 33554432;

    int codecIsSequenceSensitive = 67108864;

    int codecRequiresOffscreen = 134217728;

    int codecRequiresMaskBits = 268435456;

    int codecCanRemapResolution = 536870912;

    int codecIsDirectToScreenOnly = 1073741824;

    int codecCanLockSurface = Integer.MIN_VALUE;

    int codecConditionFirstBand = 1;

    int codecConditionLastBand = 2;

    int codecConditionFirstFrame = 4;

    int codecConditionNewDepth = 8;

    int codecConditionNewTransform = 16;

    int codecConditionNewSrcRect = 32;

    int codecConditionNewMask = 64;

    int codecConditionNewMatte = 128;

    int codecConditionNewTransferMode = 256;

    int codecConditionNewClut = 512;

    int codecConditionNewAccuracy = 1024;

    int codecConditionNewDestination = 2048;

    int codecConditionFirstScreen = 4096;

    int codecConditionDoCursor = 8192;

    int codecConditionCatchUpDiff = 16384;

    int codecConditionMaskMayBeChanged = 32768;

    int codecConditionToBuffer = 65536;

    int codecConditionCodecChangedMask = Integer.MIN_VALUE;

    int codecInfoResourceType = 1667523433;

    int codecInterfaceVersion = 2;

    int matrixFlagScale2x = 128;

    int matrixFlagScale1x = 64;

    int matrixFlagScaleHalf = 32;

    int kScreenFloodMethodNone = 0;

    int kScreenFloodMethodKeyColor = 1;

    int kScreenFloodMethodAlpha = 2;

    int kFlushLastQueuedFrame = 0;

    int kFlushFirstQueuedFrame = 1;

    int kNewImageGWorldErase = 1;

    int kCodecFrameTypeUnknown = 0;

    int kCodecFrameTypeKey = 1;

    int kCodecFrameTypeDifference = 2;

    int kCodecFrameTypeDroppableDifference = 3;

    int kParameterTitleName = 1851878757;

    int kParameterTitleID = 1;

    int kParameterWhatName = 2003329396;

    int kParameterWhatID = 1;

    int kParameterVersionName = 1986359923;

    int kParameterVersionID = 1;

    int kParameterRepeatableName = 1885697125;

    int kParameterRepeatableID = 1;

    int kParameterRepeatableTrue = 1;

    int kParameterRepeatableFalse = 0;

    int kParameterAlternateCodecName = 1937072755;

    int kParameterAlternateCodecID = 1;

    int kParameterSourceCountName = 1936876403;

    int kParameterSourceCountID = 1;

    int kParameterDependencyName = 1684366704;

    int kParameterDependencyID = 1;

    int kParameterListDependsUponColorProfiles = 1886547814;

    int kParameterListDependsUponFonts = 1718578804;

    int kParameterEnumList = 1701737837;

    int kParameterAtomTypeAndID = 1954115685;

    int kNoAtom = 1852796517;

    int kAtomNoFlags = 0;

    int kAtomNotInterpolated = 1;

    int kAtomInterpolateIsOptional = 2;

    int kParameterDataType = 1684108385;

    int kParameterTypeDataText = 1952807028;

    int kParameterTypeDataEnum = 1701737837;

    int kParameterTypeDataBitField = 1651470188;

    int kParameterTypeDataImage = 1768776039;

    int kParameterAlternateDataType = 1634497585;

    int kParameterTypeDataColorValue = 1668115570;

    int kParameterTypeDataCubic = 1668637289;

    int kParameterTypeDataNURB = 1853190754;

    int kParameterDataRange = 1918987879;

    int kNoMinimumLongFixed = Integer.MAX_VALUE;

    int kNoMaximumLongFixed = Integer.MIN_VALUE;

    int kNoScaleLongFixed = 0;

    int kNoPrecision = -1;

    int kParameterImageNoFlags = 0;

    int kParameterDataBehavior = 1684632684;

    int kParameterItemEditText = 1701079412;

    int kParameterItemEditLong = 1819242087;

    int kParameterItemEditFixed = 1718188132;

    int kParameterItemEditDouble = 1685026146;

    int kParameterItemPopUp = 1886351477;

    int kParameterItemRadioCluster = 1918985321;

    int kParameterItemCheckBox = 1667786104;

    int kParameterItemControl = 1668183148;

    int kParameterItemLine = 1818848869;

    int kParameterItemColorPicker = 1885954923;

    int kParameterItemGroupDivider = 1684633193;

    int kParameterItemStaticText = 1937006964;

    int kParameterItemDragImage = 1768776039;

    int kGraphicsNoFlags = 0;

    int kGraphicsFlagsGray = 1;

    int kGroupNoFlags = 0;

    int kGroupAlignText = 65536;

    int kGroupSurroundBox = 131072;

    int kGroupMatrix = 262144;

    int kGroupNoName = 524288;

    int kDisableControl = 1;

    int kDisableWhenNotEqual = 1;

    int kDisableWhenEqual = 17;

    int kDisableWhenLessThan = 33;

    int kDisableWhenGreaterThan = 49;

    int kPopupStoreAsString = 65536;

    int kParameterDataUsage = 1970496800;

    int kParameterUsagePixels = 1885960300;

    int kParameterUsageRectangle = 1919247220;

    int kParameterUsagePoint = 2021204000;

    int kParameterUsage3DPoint = 2021227040;

    int kParameterUsageDegrees = 1684367218;

    int kParameterUsageRadians = 1918985331;

    int kParameterUsagePercent = 1885564532;

    int kParameterUsageSeconds = 1936024435;

    int kParameterUsageMilliseconds = 1836279139;

    int kParameterUsageMicroseconds = -1250728605;

    int kParameterUsage3by3Matrix = 862091571;

    int kParameterDataDefaultItem = 1684434036;

    int kParameterInfoLongName = -1452383891;

    int kParameterInfoCopyright = -1453100935;

    int kParameterInfoDescription = -1452708250;

    int kParameterInfoWindowTitle = -1451790732;

    int kParameterInfoPicture = -1452250760;

    int kParameterInfoManufacturer = -1452449426;

    int kParameterInfoIDs = 1;

    int kParameterValidationNoFlags = 0;

    int kParameterValidationFinalValidation = 1;

    int kImageCodecSettingsFieldCount = 1718183276;

    int kImageCodecSettingsFieldOrdering = 1717858157;

    int kImageCodecSettingsFieldOrderingF1F2 = 1;

    int kImageCodecSettingsFieldOrderingF2F1 = 2;

    int kMotionJPEGTag = 1835692135;

    int kJPEGQuantizationTablesImageDescriptionExtension = 1835692404;

    int kJPEGHuffmanTablesImageDescriptionExtension = 1835690100;

    int kFieldInfoImageDescriptionExtension = 1718183276;

    int kFieldOrderUnknown = 0;

    int kFieldsStoredF1F2DisplayedF1F2 = 1;

    int kFieldsStoredF1F2DisplayedF2F1 = 2;

    int kFieldsStoredF2F1DisplayedF1F2 = 5;

    int kFieldsStoredF2F1DisplayedF2F1 = 6;

    int kEffectSourceName = 1936876320;

    int kEffectDataSourceType = 1685353332;

    int kEffectRawSource = 0;

    int kEffectGenericType = 1734698598;

    int kLinearGradient = 0;

    int kCircularGradient = 1;

    int kCurveGetLengthSelect = 256;

    int kCurveLengthToPointSelect = 257;

    int kCurveNewPathSelect = 258;

    int kCurveCountPointsInPathSelect = 259;

    int kCurveGetPathPointSelect = 260;

    int kCurveInsertPointIntoPathSelect = 261;

    int kCurveSetPathPointSelect = 262;

    int kCurveGetNearestPathPointSelect = 263;

    int kCurvePathPointToLengthSelect = 264;

    int kCurveCreateVectorStreamSelect = 265;

    int kCurveAddAtomToVectorStreamSelect = 266;

    int kCurveAddPathAtomToVectorStreamSelect = 267;

    int kCurveAddZeroAtomToVectorStreamSelect = 268;

    int kCurveGetAtomDataFromVectorStreamSelect = 269;

    int gxNoFill = 0;

    int gxOpenFrameFill = 1;

    int gxFrameFill = 1;

    int gxClosedFrameFill = 2;

    int gxHollowFill = 2;

    int gxEvenOddFill = 3;

    int gxSolidFill = 3;

    int gxWindingFill = 4;

    int gxInverseEvenOddFill = 5;

    int gxInverseSolidFill = 5;

    int gxInverseFill = 5;

    int gxInverseWindingFill = 6;

    int kDontUseValidateToFindGraphicsImporter = 1;

    int identityMatrixType = 0;

    int translateMatrixType = 1;

    int scaleMatrixType = 2;

    int scaleTranslateMatrixType = 3;

    int linearMatrixType = 4;

    int linearTranslateMatrixType = 5;

    int perspectiveMatrixType = 6;

    int graphicsImporterUsesImageDecompressor = 8388608;

    int quickTimeImageFileImageDescriptionAtom = 1768190819;

    int quickTimeImageFileImageDataAtom = 1768186228;

    int quickTimeImageFileMetaDataAtom = 1835365473;

    int kGraphicsExportGroup = 1702391919;

    int kGraphicsExportFileType = 1718909296;

    int kGraphicsExportMIMEType = 1835625829;

    int kGraphicsExportExtension = 1702392864;

    int kGraphicsExportDescription = 1684370275;

    int imageTranscodererComponentType = 1768780899;

    int handlerHasSpatial = 1;

    int handlerCanClip = 2;

    int handlerCanMatte = 4;

    int handlerCanTransferMode = 8;

    int handlerNeedsBuffer = 16;

    int handlerNoIdle = 32;

    int handlerNoScheduler = 64;

    int handlerWantsTime = 128;

    int handlerCGrafPortOnly = 256;

    int handlerCanSend = 512;

    int handlerCanHandleComplexMatrix = 1024;

    int handlerWantsDestinationPixels = 2048;

    int handlerCanSendImageData = 4096;

    int mMustDraw = 8;

    int mAtEnd = 16;

    int mPreflightDraw = 32;

    int mSyncDrawing = 64;

    int mDidDraw = 1;

    int mNeedsToDraw = 4;

    int mDrawAgain = 8;

    int mPartialDraw = 16;

    int forceUpdateRedraw = 1;

    int forceUpdateNewBuffer = 2;

    int mHitTestBounds = 1;

    int mHitTestImage = 2;

    int mHitTestInvisible = 4;

    int mHitTestIsClick = 8;

    int mOpaque = 1;

    int mInvisible = 2;

    int kMediaVideoParamBrightness = 1;

    int kMediaVideoParamContrast = 2;

    int kMediaVideoParamHue = 3;

    int kMediaVideoParamSharpness = 4;

    int kMediaVideoParamSaturation = 5;

    int kMediaVideoParamBlackLevel = 6;

    int kMediaVideoParamWhiteLevel = 7;

    int standardCompressionType = 1935893609;

    int standardCompressionSubType = 1768776039;

    int standardCompressionSubTypeSound = 1936684398;

    int scListEveryCodec = 2;

    int scAllowZeroFrameRate = 4;

    int scAllowZeroKeyFrameRate = 8;

    int scShowBestDepth = 16;

    int scUseMovableModal = 32;

    int scDisableFrameRateItem = 64;

    int scPreferCropping = 1;

    int scPreferScaling = 2;

    int scPreferScalingAndCropping = 3;

    int scDontDetermineSettingsFromTestImage = 4;

    int scTestImageWidth = 80;

    int scTestImageHeight = 80;

    int scOKItem = 1;

    int scCancelItem = 2;

    int scCustomItem = 3;

    int scUserCancelled = 1;

    int scSpatialSettingsType = 1936749676;

    int scTemporalSettingsType = 1953526380;

    int scDataRateSettingsType = 1685217652;

    int scColorTableType = 1668052340;

    int scProgressProcType = 1886547815;

    int scExtendedProcsType = 2020635235;

    int scPreferenceFlagsType = 1886545254;

    int scSettingsStateType = 1936946273;

    int scSequenceIDType = 1936028021;

    int scWindowPositionType = 2003723383;

    int scCodecFlagsType = 1667656807;

    int scCodecSettingsType = 1667523939;

    int scForceKeyValueType = 1802725741;

    int scSoundSampleRateType = 1936945780;

    int scSoundSampleSizeType = 1936946035;

    int scSoundChannelCountType = 1936941923;

    int scSoundCompressionType = 1936941940;

    int scCompressionListType = 1668577644;

    int scCodecManufacturerType = 1668114034;

    int scGetCompression = 1;

    int scShowMotionSettings = 1;

    int scSettingsChangedItem = -1;

    int scCompressFlagIgnoreIdenticalFrames = 1;

    int kQTSettingsVideo = 1986618469;

    int kQTSettingsSound = 1936684398;

    int tcSourceRefNameType = 1851878757;

    int tctNegFlag = 128;

    /**
     * negative bit is in minutes
     */
    int movieExportWidth = 2003072104;

    /**
     * negative bit is in minutes
     */
    int movieExportHeight = 1751476084;

    /**
     * negative bit is in minutes
     */
    int movieExportDuration = 1685418593;

    /**
     * negative bit is in minutes
     */
    int movieExportVideoFilter = 1768320116;

    /**
     * File Preview Components
     */
    int pnotComponentWantsEvents = 1;

    /**
     * File Preview Components
     */
    int pnotComponentNeedsNoCache = 2;

    int showFilePreviewComponentType = 1886285684;

    int createFilePreviewComponentType = 1886216555;

    int dataCompressorComponentType = 1684238189;

    int dataDecompressorComponentType = 1684301155;

    int appleDataCompressorSubType = 1633969507;

    int zlibDataCompressorSubType = 2053925218;

    int kDataHExtendedSchedule = 2020896356;

    int grabPictOffScreen = 1;

    int grabPictIgnoreClip = 2;

    int grabPictCurrentImage = 4;

    int sgFlagControlledGrab = 1;

    int sgDeviceNameFlagDeviceUnavailable = 1;

    int sgPanelFlagForPanel = 1;

    int seqGrabSettingsPreviewOnly = 1;

    int qtVideoOutputComponentType = 1987016052;

    int qtVideoOutputComponentBaseSubType = 1650553701;

    int kQTVideoOutputDontDisplayToUser = 1;

    int kBackgroundSpriteLayerNum = 32767;

    int kSpritePropertyMatrix = 1;

    int kSpritePropertyImageDescription = 2;

    int kSpritePropertyImageDataPtr = 3;

    int kSpritePropertyVisible = 4;

    int kSpritePropertyLayer = 5;

    int kSpritePropertyGraphicsMode = 6;

    int kSpritePropertyImageDataSize = 7;

    int kOnlyDrawToSpriteWorld = 1;

    int kSpriteWorldPreflight = 2;

    /**
     * idle out-flags
     */
    int kSpriteWorldDidDraw = 1;

    /**
     * idle out-flags
     */
    int kSpriteWorldNeedsToDraw = 2;

    /**
     * sprite world flags
     */
    int kScaleSpritesToScaleWorld = 2;

    /**
     * sprite world flags
     */
    int kSpriteWorldHighQuality = 4;

    /**
     * sprite world flags
     */
    int kSpriteWorldDontAutoInvalidate = 8;

    /**
     * sprite world flags
     */
    int kSpriteWorldInvisible = 16;

    int spriteHitTestBounds = 1;

    int spriteHitTestImage = 2;

    int spriteHitTestInvisibleSprites = 4;

    int spriteHitTestIsClick = 8;

    int spriteHitTestLocInDisplayCoordinates = 16;

    /**
     * callback equates--values for the parameter param1 of
     * the callMeWhen method that indicate when the
     * callback method should be called
     */
    int qtcbNeedsRateChanges = 1;

    /**
     * callback equates--values for the parameter param1 of
     * the callMeWhen method that indicate when the
     * callback method should be called
     */
    int qtcbNeedsTimeChanges = 2;

    /**
     * callback equates--values for the parameter param1 of
     * the callMeWhen method that indicate when the
     * callback method should be called
     */
    int qtcbNeedsStartStopChanges = 4;

    int clockComponentType = 1668050795;

    int systemTickClock = 1953063787;

    int systemSecondClock = 1936024431;

    int systemMillisecondClock = 1835625580;

    int systemMicrosecondClock = 1835623282;

    int kClockRateIsLinear = 1;

    int kClockImplementsCallBacks = 2;

    int kClockCanHandleIntermittentSound = 4;

    /**
     * Time base flags
     */
    int loopTimeBase = 1;

    /**
     * Time base flags
     */
    int palindromeLoopTimeBase = 2;

    /**
     * Time base flags
     */
    int maintainTimeBaseZero = 4;

    /**
     * ExtremesCallback
     */
    int triggerAtStart = 1;

    /**
     * ExtremesCallback
     */
    int triggerAtStop = 2;

    /**
     * RateCallback
     */
    int triggerRateLT = 4;

    /**
     * RateCallback
     */
    int triggerRateGT = 8;

    /**
     * RateCallback
     */
    int triggerRateEqual = 16;

    /**
     * RateCallback
     */
    int triggerRateLTE = 20;

    /**
     * RateCallback
     */
    int triggerRateGTE = 24;

    /**
     * RateCallback
     */
    int triggerRateNotEqual = 28;

    /**
     * RateCallback
     */
    int triggerRateChange = 0;

    int timeBaseBeforeStartTime = 1;

    int timeBaseAfterStopTime = 2;

    /**
     * TimeCallback
     */
    int triggerTimeFwd = 1;

    /**
     * TimeCallback
     */
    int triggerTimeBwd = 2;

    /**
     * TimeCallback
     */
    int triggerTimeEither = 3;

    int kAnyType = 0;

    int kAnySubType = 0;

    int kSorensonCodecType = 1398165809;

    int kRawCodecType = 1918990112;

    int kCinepakCodecType = 1668704612;

    int kGraphicsCodecType = 1936548640;

    int kAnimationCodecType = 1919706400;

    int kVideoCodecType = 1919973985;

    int kComponentVideoCodecType = 2037741106;

    int kJPEGCodecType = 1785750887;

    int kMotionJPEGACodecType = 1835692129;

    int kMotionJPEGBCodecType = 1835692130;

    int kSGICodecType = 777209673;

    int kPlanarRGBCodecType = 943870035;

    int kMacPaintCodecType = 1347310663;

    int kGIFCodecType = 1734960672;

    int kPhotoCDCodecType = 1802527588;

    int kQuickDrawGXCodecType = 1902405496;

    int kAVRJPEGCodecType = 1635152416;

    int kOpenDMLJPEGCodecType = 1684890161;

    int kBMPCodecType = 1465011269;

    int kWindowsRawCodecType = 1465008471;

    int kVectorCodecType = 1885434984;

    int kQuickDrawCodecType = 1902408311;

    int kWaterRippleCodecType = 1919512684;

    int kFireCodecType = 1718186597;

    int kCloudCodecType = 1668050805;

    int kH261CodecType = 1748121137;

    int kH263CodecType = 1748121139;

    int kDVCNTSCCodecType = 1685480224;

    int kDVCPALCodecType = 1685480304;

    int kDVCProNTSCCodecType = 1685483630;

    int kDVCProPALCodecType = 1685483632;

    int kBaseCodecType = 1650553701;

    int kFLCCodecType = 1718380899;

    int kTargaCodecType = 1952932128;

    int kComponentVideoSigned = 2037741173;

    int kComponentVideoUnsigned = 2037741171;

    int kCMYKCodecType = 1668118891;

    int kMicrosoftVideo1CodecType = 1836283491;

    /**
     * One Source Effects
     */
    int kBlurImageFilterType = 1651275122;

    /**
     * One Source Effects
     */
    int kSharpenImageFilterType = 1936224880;

    /**
     * One Source Effects
     */
    int kEdgeDetectImageFilterType = 1701078885;

    /**
     * One Source Effects
     */
    int kEmbossImageFilterType = 1701667443;

    /**
     * One Source Effects
     */
    int kConvolveImageFilterType = 1734700651;

    /**
     * One Source Effects
     */
    int kAlphaGainImageFilterType = 1734437230;

    /**
     * One Source Effects
     */
    int kRGBColorBalanceImageFilterType = 1919378018;

    /**
     * One Source Effects
     */
    int kHSLColorBalanceImageFilterType = 1752394850;

    /**
     * One Source Effects
     */
    int kColorSyncImageFilterType = 1937337955;

    /**
     * One Source Effects
     */
    int kFilmNoiseImageFilterType = 1718447731;

    /**
     * One Source Effects
     */
    int kSolarizeImageFilterType = 1936682098;

    /**
     * One Source Effects
     */
    int kColorTintImageFilterType = 1953066612;

    /**
     * Two source effects
     */
    int kAlphaCompositorTransitionType = 1651273316;

    /**
     * Two source effects
     */
    int kCrossFadeTransitionType = 1685286006;

    /**
     * Two source effects
     */
    int kChromaKeyTransitionType = 1667982713;

    /**
     * Two source effects
     */
    int kImplodeTransitionType = 1836084335;

    /**
     * Two source effects
     */
    int kExplodeTransitionType = 2020633711;

    /**
     * Two source effects
     */
    int kGradientTransitionType = 1835103348;

    /**
     * Two source effects
     */
    int kPushTransitionType = 1886745448;

    /**
     * Two source effects
     */
    int kSlideTransitionType = 1936484708;

    /**
     * Two source effects
     */
    int kWipeTransitionType = 1936552052;

    /**
     * Two source effects
     */
    int kIrisTransitionType = 1936551986;

    /**
     * Two source effects
     */
    int kRadialTransitionType = 1936551987;

    /**
     * Two source effects
     */
    int kMatrixTransitionType = 1936551988;

    /**
     * Two source effects
     */
    int kRandomWipeTransitionType = 409;

    int kCurvePathAtom = 1885434984;

    int kCurveEndAtom = 2053468783;

    int kCurveAntialiasControlAtom = 1634628713;

    int kCurveAntialiasOff = 0;

    int kCurveAntialiasOn = -1;

    int kCurveFillTypeAtom = 1718185068;

    int kCurvePenThicknessAtom = 1885695604;

    int kCurveMiterLimitAtom = 1835627634;

    int kCurveJoinAttributesAtom = 1785686382;

    int kCurveMinimumDepthAtom = 1835626084;

    int kCurveDepthAlwaysOffscreenMask = Integer.MIN_VALUE;

    int kCurveGradientAngleAtom = 1634625388;

    int kCurveGradientRadiusAtom = 1918985321;

    int kCurveGradientOffsetAtom = 1667591796;

    int kCurveARGBColorAtom = 1634887522;

    int kCurveGradientTypeAtom = 1735550068;

    int kCurveGradientRecordAtom = 1735549284;

    int codecInfoDoes1 = 1;

    int codecInfoDoes2 = 2;

    int codecInfoDoes4 = 4;

    int codecInfoDoes8 = 8;

    int codecInfoDoes16 = 16;

    int codecInfoDoes32 = 32;

    int codecInfoDoesDither = 64;

    int codecInfoDoesStretch = 128;

    int codecInfoDoesShrink = 256;

    int codecInfoDoesMask = 512;

    int codecInfoDoesTemporal = 1024;

    int codecInfoDoesDouble = 2048;

    int codecInfoDoesQuad = 4096;

    int codecInfoDoesHalf = 8192;

    int codecInfoDoesQuarter = 16384;

    int codecInfoDoesRotate = 32768;

    int codecInfoDoesHorizFlip = 65536;

    int codecInfoDoesVertFlip = 131072;

    int codecInfoHasEffectParameterList = 262144;

    int codecInfoDoesBlend = 524288;

    int codecInfoDoesWarp = 1048576;

    int codecInfoDoesRecompress = 2097152;

    int codecInfoDoesSpool = 4194304;

    int codecInfoDoesRateConstrain = 8388608;

    int codecInfoDepth1 = 1;

    int codecInfoDepth2 = 2;

    int codecInfoDepth4 = 4;

    int codecInfoDepth8 = 8;

    int codecInfoDepth16 = 16;

    int codecInfoDepth32 = 32;

    int codecInfoDepth24 = 64;

    int codecInfoDepth33 = 128;

    int codecInfoDepth34 = 256;

    int codecInfoDepth36 = 512;

    int codecInfoDepth40 = 1024;

    int codecInfoStoresClut = 2048;

    int codecInfoDoesLossless = 4096;

    int codecInfoSequenceSensitive = 8192;

    /**
     * the type for "Components" which compress images.
     */
    int compressorComponentType = 1768776559;

    /**
     * the type for "Components" which decompress images.
     */
    int decompressorComponentType = 1768776803;

    int defaultDither = 0;

    int forceDither = 1;

    int suppressDither = 2;

    int useColorMatching = 4;

    int callStdBits = 1;

    int callOldBits = 2;

    int noDefaultOpcodes = 4;

    int graphicsModeStraightAlpha = 256;

    int graphicsModePreWhiteAlpha = 257;

    int graphicsModePreBlackAlpha = 258;

    int graphicsModeComposition = 259;

    int graphicsModeStraightAlphaBlend = 260;

    int graphicsModePreMulColorAlpha = 261;

    int evenField1ToEvenFieldOut = 1;

    int evenField1ToOddFieldOut = 2;

    int oddField1ToEvenFieldOut = 4;

    int oddField1ToOddFieldOut = 8;

    int evenField2ToEvenFieldOut = 16;

    int evenField2ToOddFieldOut = 32;

    int oddField2ToEvenFieldOut = 64;

    int oddField2ToOddFieldOut = 128;

    int icmFrameTimeHasVirtualStartTimeAndDuration = 1;

    int elOptionsIncludeNoneInList = 1;

    int pdOptionsCollectOneValue = 1;

    int pdOptionsAllowOptionalInterpolations = 2;

    int pdOptionsModalDialogBox = 4;

    int effectIsRealtime = 0;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectPercentage = 1885564532;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectWipe = 2003847492;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectSoftBorder = 1936680564;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectBorderWidth = 2003395700;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectBorderColor = 1650682994;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectHorizontalRepeat = 1752134249;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectVerticalRepeat = 1986359924;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectBlendMode = 1649241956;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectPreMultiplyColor = 1835232370;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectStraightAlpha = 1;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectPreMultiplyAlpha = 2;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectReverseAlpha = 3;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectGainValue = 1734437230;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectOffsetValue = 1868981875;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectTopAlphaPin = 1885957716;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectBottomAlphaPin = 1885957698;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectFromDirection = 1718775661;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectDirectionTop = 0;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectDirectionLeft = 1;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectDirectionBottom = 2;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectDirectionRight = 3;

    /**
     * These constants are paremeteres that control the operation of the effects.
     */
    int kEffectSlideAngle = 1634625388;

    /**
     * These constants are used in the building of the effects atom container.
     */
    int kEffectWhatAtom = 2003329396;

    /**
     * These constants are used in the building of the effects atom container.
     */
    int kEffectSrcAtom = 1936876320;

    /**
     * These constants are used in the building of the effects atom container.
     */
    int kEffectNameAtom = 1851878757;

    /**
     * These constants are used in the building of the effects atom container.
     */
    int kEffectTypeAtom = 1954115685;

    /**
     * These constants are used in the building of the effects atom container.
     */
    int kEffectManufacturerAtom = 1835101813;

    int pdActionConfirmDialog = 1;

    int pdActionSetAppleMenu = 2;

    int pdActionSetEditMenu = 3;

    int pdActionGetDialogValues = 4;

    int pdActionSetPreviewUserItem = 5;

    int pdActionSetPreviewPicture = 6;

    int pdActionSetColorPickerEventProc = 7;

    int pdActionSetDialogTitle = 8;

    int pdActionGetSubPanelMenu = 9;

    int pdActionActivateSubPanel = 10;

    int pdActionConductStopAlert = 11;

    int pdActionModelessCallback = 12;

    /**
     * THe constants that are used for different vendors
     */
    int kVendorApple = 1634758764;

    int graphicsExporterComponentType = 1735550328;

    int kBaseGraphicsExporterSubType = 1650553701;

    int graphicsExporterIsBaseExporter = 1;

    int graphicsExporterCanTranscode = 2;

    int graphicsExporterUsesImageCompressor = 4;

    int kQTResolutionSettings = 1919251311;

    int kQTTargetDataSize = 1684108154;

    int kQTDontRecompress = 1684960370;

    int kQTInterlaceStyle = 6909027;

    int kQTColorSyncProfile = 1768121197;

    int kQTTIFFCompressionMethod = 1953064547;

    int kQTTIFFCompression_None = 1;

    int kQTTIFFLittleEndian = 1953064549;

    int kQTPNGFilterPreference = 1835952486;

    int kQTPNGFilterBestForColorType = 1650879604;

    int kQTPNGFilterNone = 0;

    int kQTPNGFilterSub = 1;

    int kQTPNGFilterUp = 2;

    int kQTPNGFilterAverage = 3;

    int kQTPNGFilterPaeth = 4;

    int kQTPNGFilterAdaptivePerRow = 1634102388;

    int kQTPNGInterlaceStyle = 6909027;

    int kQTPNGInterlaceNone = 0;

    int kQTPNGInterlaceAdam7 = 1;

    /**
     * values for the Graphics Import component type and sub type.
     */
    int graphicsImporterComponentType = 1735551344;

    /**
     * Flags that are returned by the doesDrawAllPixels call
     */
    int graphicsImporterDrawsAllPixels = 0;

    /**
     * Flags that are returned by the doesDrawAllPixels call
     */
    int graphicsImporterDoesntDrawAllPixels = 1;

    /**
     * Flags that are returned by the doesDrawAllPixels call
     */
    int graphicsImporterDontKnowIfDrawAllPixels = 2;

    /**
     * Access Keys
     */
    int kAccessKeyAtomType = 1633905529;

    /**
     * Access Keys
     */
    int kAccessKeySystemFlag = 1;

    /**
     * track reference types
     */
    int kTrackReferenceChapterList = 1667785072;

    /**
     * track reference types
     */
    int kTrackReferenceTimeCode = 1953325924;

    /**
     * track reference types
     */
    int kTrackReferenceModifier = 1936945763;

    int codecLosslessQuality = 1024;

    int codecMaxQuality = 1023;

    int codecMinQuality = 0;

    int codecLowQuality = 256;

    int codecNormalQuality = 512;

    int codecHighQuality = 768;

    int codecLockBitsShieldCursor = 1;

    int codecCompletionSource = 1;

    int codecCompletionDest = 2;

    int codecCompletionDontUnshield = 4;

    int codecCompletionWentOffscreen = 8;

    int codecCompletionUnlockBits = 16;

    int codecCompletionForceChainFlush = 32;

    int codecCompletionDropped = 64;

    int codecProgressOpen = 0;

    int codecProgressUpdatePercent = 1;

    int codecProgressClose = 2;

    /**
     * input sequence flags
     */
    int codecFlagUseImageBuffer = 1;

    /**
     * input sequence flags
     */
    int codecFlagUseScreenBuffer = 2;

    /**
     * input sequence flags
     */
    int codecFlagUpdatePrevious = 4;

    /**
     * input sequence flags
     */
    int codecFlagNoScreenUpdate = 8;

    /**
     * input sequence flags
     */
    int codecFlagWasCompressed = 16;

    /**
     * input sequence flags
     */
    int codecFlagDontOffscreen = 32;

    /**
     * input sequence flags
     */
    int codecFlagUpdatePreviousComp = 64;

    /**
     * input sequence flags
     */
    int codecFlagForceKeyFrame = 128;

    /**
     * input sequence flags
     */
    int codecFlagOnlyScreenUpdate = 256;

    /**
     * input sequence flags
     */
    int codecFlagLiveGrab = 512;

    /**
     * input sequence flags
     */
    int codecFlagDontUseNewImageBuffer = 1024;

    /**
     * input sequence flags
     */
    int codecFlagInterlaceUpdate = 2048;

    /**
     * input sequence flags
     */
    int codecFlagCatchUpDiff = 4096;

    /**
     * input sequence flags
     */
    int codecFlagSupportDisable = 8192;

    /**
     * input sequence flags
     */
    int codecFlagReenable = 16384;

    /**
     * output sequence flags
     */
    int codecFlagOutUpdateOnDataSourceChange = 1024;

    /**
     * output sequence flags
     */
    int codecFlagSequenceSensitive = 2048;

    /**
     * output sequence flags
     */
    int codecFlagOutUpdateOnTimeChange = 4096;

    /**
     * output sequence flags
     */
    int codecFlagImageBufferNotSourceImage = 8192;

    /**
     * output sequence flags
     */
    int codecFlagUsedNewImageBuffer = 16384;

    /**
     * output sequence flags
     */
    int codecFlagUsedImageBuffer = 32768;

    int codecMinimumDataSize = 32768;

    /**
     * Used to control some behaviour when creating a QTFile object.
     */
    int kDeleteCurFile = Integer.MIN_VALUE;

    /**
     * Use this constant when you want the movie toolbox to display the default progress function.
     */
    int defaultProgressFuntion = -1;

    int kMimeInfoMimeTypeTag = 1835625829;

    int kMimeInfoFileExtensionTag = 1702392864;

    int kMimeInfoFileTypeTag = 1718909296;

    int kMimeInfoDescriptionTag = 1684370275;

    int kMimeInfoGroupTag = 1735552880;

    int kMimeInfoDoNotOverrideExistingFileTypeAssociation = 1852794465;

    int kDataRefMimeTypeTag = 1835625829;

    int kDataRefFileExtensionTag = 1702392864;

    int kDataRefQTFileTypeTag = 1718909296;

    int kDataRefExtensionChokeSpeed = 1667788651;

    int kDataRefExtensionMIMEType = 1835625829;

    int kDataRefExtensionMacOSFileType = 1718909296;

    int kDataRefExtensionInitializationData = 1684108385;

    int kQTFileTypeAIFF = 1095321158;

    int kQTFileTypeAIFC = 1095321155;

    int kQTFileTypeDVC = 1685480225;

    int kQTFileTypeMIDI = 1298752617;

    int kQTFileTypePicture = 1346978644;

    int kQTFileTypeMovie = 1299148630;

    int kQTFileTypeText = 1413830740;

    int kQTFileTypeWave = 1463899717;

    int kQTFileTypeSystemSevenSound = 1936091500;

    int kQTFileTypeMuLaw = 1431060823;

    int kQTFileTypeAVI = 1449547552;

    int kQTFileTypeSoundDesignerII = 1399075430;

    int kQTFileTypeAudioCDTrack = 1953653099;

    int kQTFileTypePICS = 1346978643;

    int kQTFileTypeGIF = 1195984486;

    int kQTFileTypePhotoShop = 943870035;

    int kQTFileTypeSGIImage = 777209673;

    int kQTFileTypeBMP = 1112363110;

    int kQTFileTypeJPEG = 1246774599;

    int kQTFileTypeJFIF = 1246774599;

    int kQTFileTypeMacPaint = 1347310663;

    int kQTFileTypeTargaImage = 1414547779;

    int kQTFileTypeQuickDrawGXPicture = 1902405496;

    int kQTFileTypeQuickTimeImage = 1903454566;

    int kQTFileType3DMF = 860114246;

    int kMoviePlayer = 1414942532;

    int kMShp = 1297311856;

    int kVis3 = 1449751347;

    int kPhotoshop = 943868237;

    int kClarisWorks = 1112490575;

    int kTeachText = 1953785204;

    int kPictureCompressor = 1886419049;

    int kGKON = 1196117838;

    int createMovieFileDeleteCurFile = Integer.MIN_VALUE;

    int createMovieFileDontCreateMovie = 1073741824;

    int createMovieFileDontOpenFile = 536870912;

    int createMovieFileDontCreateResFile = 268435456;

    int sfpItemPreviewAreaUser = 11;

    int sfpItemPreviewStaticText = 12;

    int sfpItemPreviewDividerUser = 13;

    int sfpItemCreatePreviewButton = 14;

    int sfpItemShowPreviewButton = 15;

    int kMovieVersion = 0;

    /**
     * version number of the format here described
     */
    int kVersionCheckMin = 0;

    /**
     * version number of the format here described
     */
    int kVersionCheckMask = 1;

    int kDataRate144ModemRate = 1400;

    int kDataRate288ModemRate = 2800;

    int kDataRateISDNRate = 5600;

    int kDataRateDualISDNRate = 11200;

    int kDataRateT1Rate = 150000;

    int kDataRateInfiniteRate = Integer.MAX_VALUE;

    /**
     * Track Structures
     */
    int trackEnable = 1;

    /**
     * Track Structures
     */
    int trackInMovie = 2;

    /**
     * Track Structures
     */
    int trackInPreview = 4;

    /**
     * Track Structures
     */
    int trackInPoster = 8;

    /**
     * some system defined format IDs
     */
    int movieFileType = 1299148630;

    /**
     * some system defined format IDs
     */
    int movieScrapType = 1836019574;

    /**
     * some system defined format IDs
     */
    int movieResourceType = 1836019574;

    /**
     * some system defined format IDs
     */
    int movieForwardPointerResourceType = 1718579813;

    /**
     * some system defined format IDs
     */
    int movieBackwardPointerResourceType = 1650549611;

    /**
     * some system defined format IDs
     */
    int movieResourceAtomType = 1836019574;

    /**
     * some system defined format IDs
     */
    int movieDataAtomType = 1835295092;

    /**
     * some system defined format IDs
     */
    int movieAID = 1836019574;

    /**
     * some system defined format IDs
     */
    int movieHeaderAID = 1836476516;

    /**
     * some system defined format IDs
     */
    int clipAID = 1668049264;

    /**
     * some system defined format IDs
     */
    int rgnClipAID = 1668441966;

    /**
     * some system defined format IDs
     */
    int matteAID = 1835103348;

    /**
     * some system defined format IDs
     */
    int matteCompAID = 1802330484;

    /**
     * some system defined format IDs
     */
    int trackAID = 1953653099;

    /**
     * some system defined format IDs
     */
    int userDataAID = 1969517665;

    /**
     * some system defined format IDs
     */
    int trackHeaderAID = 1953196132;

    /**
     * some system defined format IDs
     */
    int editsAID = 1701082227;

    /**
     * some system defined format IDs
     */
    int editListAID = 1701606260;

    /**
     * some system defined format IDs
     */
    int mediaAID = 1835297121;

    /**
     * some system defined format IDs
     */
    int mediaHeaderAID = 1835296868;

    /**
     * some system defined format IDs
     */
    int mediaInfoAID = 1835626086;

    /**
     * some system defined format IDs
     */
    int videoMediaInfoHeaderAID = 1986881636;

    /**
     * some system defined format IDs
     */
    int soundMediaInfoHeaderAID = 1936549988;

    /**
     * some system defined format IDs
     */
    int genericMediaInfoHeaderAID = 1735223396;

    /**
     * some system defined format IDs
     */
    int genericMediaInfoAID = 1735223662;

    /**
     * some system defined format IDs
     */
    int dataInfoAID = 1684631142;

    /**
     * some system defined format IDs
     */
    int dataRefAID = 1685218662;

    /**
     * some system defined format IDs
     */
    int sampleTableAID = 1937007212;

    /**
     * some system defined format IDs
     */
    int sTSampleDescAID = 1937011556;

    /**
     * some system defined format IDs
     */
    int sTTimeToSampAID = 1937011827;

    /**
     * some system defined format IDs
     */
    int sTSyncSampleAID = 1937011571;

    /**
     * some system defined format IDs
     */
    int sTSampleToChunkAID = 1937011555;

    /**
     * some system defined format IDs
     */
    int sTShadowSyncAID = 1937011560;

    /**
     * some system defined format IDs
     */
    int handlerAID = 1751411826;

    /**
     * some system defined format IDs
     */
    int sTSampleSizeAID = 1937011578;

    /**
     * some system defined format IDs
     */
    int sTChunkOffsetAID = 1937007471;

    /**
     * some system defined format IDs
     */
    int sTChunkOffset64AID = 1668232756;

    /**
     * some system defined format IDs
     */
    int sTSampleIDAID = 1937008996;

    /**
     * some system defined format IDs
     */
    int dataRefContainerAID = 1685218915;

    /**
     * some system defined format IDs
     */
    int trackReferenceAID = 1953654118;

    /**
     * some system defined format IDs
     */
    int colorTableAID = 1668571490;

    /**
     * some system defined format IDs
     */
    int loadSettingsAID = 1819238756;

    /**
     * some system defined format IDs
     */
    int propertyAtomAID = 1668244581;

    /**
     * some system defined format IDs
     */
    int inputMapAID = 1768776048;

    /**
     * some system defined format IDs
     */
    int movieBufferHintsAID = 1835165288;

    /**
     * some system defined format IDs
     */
    int movieDataRefAliasAID = 1835299425;

    /**
     * some system defined format IDs
     */
    int soundLocalizationAID = 1936486243;

    /**
     * some system defined format IDs
     */
    int compressedMovieAID = 1668116342;

    /**
     * some system defined format IDs
     */
    int compressedMovieDataAID = 1668118116;

    /**
     * some system defined format IDs
     */
    int dataCompressionAtomAID = 1684238189;

    /**
     * some system defined format IDs
     */
    int referenceMovieRecordAID = 1919775329;

    /**
     * some system defined format IDs
     */
    int referenceMovieDescriptorAID = 1919771745;

    /**
     * some system defined format IDs
     */
    int referenceMovieDataRefAID = 1919185510;

    /**
     * some system defined format IDs
     */
    int referenceMovieVersionCheckAID = 1919776355;

    /**
     * some system defined format IDs
     */
    int referenceMovieDataRateAID = 1919771762;

    /**
     * some system defined format IDs
     */
    int referenceMovieComponentCheckAID = 1919771492;

    /**
     * some system defined format IDs
     */
    int referenceMovieQualityAID = 1919775093;

    int kDataRefIsSelfContained = 1;

    int kParentAtomIsContainer = 0;

    int kITextRemoveEverythingBut = 0;

    int kITextRemoveLeaveSuggestedAlternate = 2;

    int kITextAtomType = 1769240692;

    int kITextStringAtomType = 1952807028;

    int fullScreenHideCursor = 1;

    int fullScreenAllowEvents = 2;

    int fullScreenDontChangeMenuBar = 4;

    int fullScreenPreflightSize = 8;

    int newMovieActive = 1;

    int newMovieDontResolveDataRefs = 2;

    int newMovieDontAskUnresolvedDataRefs = 4;

    int newMovieDontAutoAlternates = 8;

    int newMovieDontUpdateForeBackPointers = 16;

    int keepInRam = 1;

    int unkeepInRam = 2;

    int flushFromRam = 4;

    int loadForwardTrackEdits = 8;

    int loadBackwardTrackEdits = 16;

    int visualMediaCharacteristic = 1702454643;

    int audioMediaCharacteristic = 1700885107;

    int kCharacteristicCanSendVideo = 1987276388;

    int kCharacteristicProvidesURLs = 1970433132;

    int kCharacteristicNonLinear = 1852796524;

    int kCharacteristicProvidesActions = 1633907822;

    int kUserDataMovieControllerType = 1668577648;

    int kUserDataName = 1851878757;

    int kUserDataTextFullName = -1452383891;

    int kUserDataTextCopyright = -1453100935;

    int kUserDataTextInformation = -1452708250;

    int searchTextDontGoToFoundTime = 65536;

    int searchTextDontHiliteFoundText = 131072;

    int searchTextOneTrackOnly = 262144;

    int searchTextEnabledTracksOnly = 524288;

    int pasteInParallel = 1;

    int showUserSettingsDialog = 2;

    int movieToFileOnlyExport = 4;

    int movieFileSpecValid = 8;

    /**
     * magic res ID for flattened movies
     */
    int movieInDataForkResID = -1;

    int flattenAddMovieToDataFork = 1;

    int flattenActiveTracksOnly = 4;

    int flattenDontInterleaveFlatten = 8;

    int flattenFSSpecPtrIsDataRefRecordPtr = 16;

    int flattenCompressMovieResource = 32;

    int flattenForceMovieResourceBeforeMovieData = 64;

    int nextTimeMediaSample = 1;

    int nextTimeMediaEdit = 2;

    int nextTimeTrackEdit = 4;

    int nextTimeSyncSample = 8;

    int nextTimeStep = 16;

    int nextTimeEdgeOK = 16384;

    int nextTimeIgnoreActiveSegment = 32768;

    int movieScrapDontZeroScrap = 1;

    int movieScrapOnlyPutMovie = 2;

    int hintsScrubMode = 1;

    int hintsLoop = 2;

    int hintsDontPurge = 4;

    int hintsUseScreenBuffer = 32;

    int hintsAllowInterlace = 64;

    int hintsUseSoundInterp = 128;

    int hintsHighQuality = 256;

    int hintsPalindrome = 512;

    int hintsInactive = 2048;

    int hintsOffscreen = 4096;

    int hintsDontDraw = 8192;

    int hintsAllowBlacklining = 16384;

    int hintsDontUseVideoOverlaySurface = 65536;

    int hintsIgnoreBandwidthRestrictions = 131072;

    int hintsPlayingEveryFrame = 262144;

    int hintsAllowDynamicResize = 524288;

    /**
     * Used when setting movie drawing complete proc
     */
    int movieDrawingCallWhenChanged = 0;

    /**
     * Used when setting movie drawing complete proc
     */
    int movieDrawingCallAlways = 1;

    int movieTrackMediaType = 1;

    int movieTrackCharacteristic = 2;

    int movieTrackEnabledOnly = 4;

    int movieControllerComponentType = 1886151033;

    int mcTopLeftMovie = 1;

    int mcScaleMovieToFit = 2;

    int mcWithBadge = 4;

    int mcNotVisible = 8;

    int mcWithFrame = 16;

    int mcFlagSuppressMovieFrame = 1;

    int mcFlagSuppressStepButtons = 2;

    int mcFlagSuppressSpeakerButton = 4;

    int mcFlagsUseWindowPalette = 8;

    int mcFlagsDontInvalidate = 16;

    int mcFlagsUseCustomButton = 32;

    int mcPositionDontInvalidate = 32;

    int kMCIEEnabledButtonPicture = 1;

    int kMCIEDisabledButtonPicture = 2;

    int kMCIEDepressedButtonPicture = 3;

    int kMCIEEnabledSizeBoxPicture = 4;

    int kMCIEDisabledSizeBoxPicture = 5;

    int kMCIEEnabledUnavailableButtonPicture = 6;

    int kMCIEDisabledUnavailableButtonPicture = 7;

    int kMCIESoundSlider = 128;

    int kMCIESoundThumb = 129;

    int kMCIEColorTable = 256;

    int kMCIEIsFlatAppearance = 257;

    int kMCIEDoButtonIconsDropOnDepress = 258;

    int mcInfoUndoAvailable = 1;

    int mcInfoCutAvailable = 2;

    int mcInfoCopyAvailable = 4;

    int mcInfoPasteAvailable = 8;

    int mcInfoClearAvailable = 16;

    int mcInfoHasSound = 32;

    int mcInfoIsPlaying = 64;

    int mcInfoIsLooping = 128;

    int mcInfoIsInPalindrome = 256;

    int mcInfoEditingEnabled = 512;

    int mcInfoMovieIsInteractive = 1024;

    int mcMenuUndo = 1;

    int mcMenuCut = 3;

    int mcMenuCopy = 4;

    int mcMenuPaste = 5;

    int mcMenuClear = 6;

    int kControllerHeight = 16;

    short mcActionIdle = 1;

    short mcActionDraw = 2;

    short mcActionActivate = 3;

    short mcActionDeactivate = 4;

    short mcActionMouseDown = 5;

    short mcActionKey = 6;

    short mcActionPlay = 8;

    short mcActionGoToTime = 12;

    short mcActionSetVolume = 14;

    short mcActionGetVolume = 15;

    short mcActionStep = 18;

    short mcActionSetLooping = 21;

    short mcActionGetLooping = 22;

    short mcActionSetLoopIsPalindrome = 23;

    short mcActionGetLoopIsPalindrome = 24;

    short mcActionSetGrowBoxBounds = 25;

    short mcActionControllerSizeChanged = 26;

    short mcActionSetSelectionBegin = 29;

    short mcActionSetSelectionDuration = 30;

    short mcActionSetKeysEnabled = 32;

    short mcActionGetKeysEnabled = 33;

    short mcActionSetPlaySelection = 34;

    short mcActionGetPlaySelection = 35;

    short mcActionSetUseBadge = 36;

    short mcActionGetUseBadge = 37;

    short mcActionSetFlags = 38;

    short mcActionGetFlags = 39;

    short mcActionSetPlayEveryFrame = 40;

    short mcActionGetPlayEveryFrame = 41;

    short mcActionGetPlayRate = 42;

    short mcActionShowBalloon = 43;

    short mcActionBadgeClick = 44;

    short mcActionMovieClick = 45;

    short mcActionSuspend = 46;

    short mcActionResume = 47;

    short mcActionSetControllerKeysEnabled = 48;

    short mcActionGetTimeSliderRect = 49;

    short mcActionMovieEdited = 50;

    short mcActionGetDragEnabled = 51;

    short mcActionSetDragEnabled = 52;

    short mcActionGetSelectionBegin = 53;

    short mcActionGetSelectionDuration = 54;

    short mcActionPrerollAndPlay = 55;

    short mcActionGetCursorSettingEnabled = 56;

    short mcActionSetCursorSettingEnabled = 57;

    short mcActionSetColorTable = 58;

    short mcActionLinkToURL = 59;

    short mcActionCustomButtonClick = 60;

    short mcActionForceTimeTableUpdate = 61;

    short mcActionSetControllerTimeLimits = 62;

    short mcActionExecuteAllActionsForQTEvent = 63;

    short mcActionExecuteOneActionForQTEvent = 64;

    short mcActionAdjustCursor = 65;

    short mcActionUseTrackForTimeTable = 66;

    short mcActionClickAndHoldPoint = 67;

    short mcActionShowMessageString = 68;

    short mcActionShowStatusString = 69;

    short mcActionGetExternalMovie = 70;

    short mcActionGetChapterTime = 71;

    short mcActionPerformActionList = 72;

    short mcActionEvaluateExpression = 73;

    short mcActionFetchParameterAs = 74;

    short mcActionGetCursorByID = 75;

    short mcActionGetNextURL = 76;

    int kActionMovieSetVolume = 1024;

    int kActionMovieSetRate = 1025;

    int kActionMovieSetLoopingFlags = 1026;

    int kActionMovieGoToTime = 1027;

    int kActionMovieGoToTimeByName = 1028;

    int kActionMovieGoToBeginning = 1029;

    int kActionMovieGoToEnd = 1030;

    int kActionMovieStepForward = 1031;

    int kActionMovieStepBackward = 1032;

    int kActionMovieSetSelection = 1033;

    int kActionMovieSetSelectionByName = 1034;

    int kActionMoviePlaySelection = 1035;

    int kActionMovieSetLanguage = 1036;

    int kActionMovieChanged = 1037;

    int kActionTrackSetVolume = 2048;

    int kActionTrackSetBalance = 2049;

    int kActionTrackSetEnabled = 2050;

    int kActionTrackSetMatrix = 2051;

    int kActionTrackSetLayer = 2052;

    int kActionTrackSetClip = 2053;

    int kActionSpriteSetMatrix = 3072;

    int kActionSpriteSetImageIndex = 3073;

    int kActionSpriteSetVisible = 3074;

    int kActionSpriteSetLayer = 3075;

    int kActionSpriteSetGraphicsMode = 3076;

    int kActionSpritePassMouseToCodec = 3078;

    int kActionSpriteClickOnCodec = 3079;

    int kActionSpriteTranslate = 3080;

    int kActionSpriteScale = 3081;

    int kActionSpriteRotate = 3082;

    int kActionSpriteStretch = 3083;

    int kActionQTVRSetPanAngle = 4096;

    int kActionQTVRSetTiltAngle = 4097;

    int kActionQTVRSetFieldOfView = 4098;

    int kActionQTVRShowDefaultView = 4099;

    int kActionQTVRGoToNodeID = 4100;

    int kActionMusicPlayNote = 5120;

    int kActionMusicSetController = 5121;

    int kActionCase = 6144;

    int kActionWhile = 6145;

    int kActionGoToURL = 6146;

    int kActionSendQTEventToSprite = 6147;

    int kActionDebugStr = 6148;

    int kActionPushCurrentTime = 6149;

    int kActionPushCurrentTimeWithLabel = 6150;

    int kActionPopAndGotoTopTime = 6151;

    int kActionPopAndGotoLabeledTime = 6152;

    int kOperandExpression = 1;

    int kOperandConstant = 2;

    int kOperandMovieVolume = 1024;

    int kOperandMovieRate = 1025;

    int kOperandMovieIsLooping = 1026;

    int kOperandMovieLoopIsPalindrome = 1027;

    int kOperandMovieTime = 1028;

    int kOperandTrackVolume = 2048;

    int kOperandTrackBalance = 2049;

    int kOperandTrackEnabled = 2050;

    int kOperandTrackLayer = 2051;

    int kOperandTrackWidth = 2052;

    int kOperandTrackHeight = 2053;

    int kOperandSpriteBoundsLeft = 3072;

    int kOperandSpriteBoundsTop = 3073;

    int kOperandSpriteBoundsRight = 3074;

    int kOperandSpriteBoundsBottom = 3075;

    int kOperandSpriteImageIndex = 3076;

    int kOperandSpriteVisible = 3077;

    int kOperandSpriteLayer = 3078;

    int kOperandSpriteTrackVariable = 3079;

    int kOperandSpriteTrackNumSprites = 3080;

    int kOperandSpriteTrackNumImages = 3081;

    int kOperandSpriteID = 3082;

    int kOperandSpriteIndex = 3083;

    int kOperandSpriteFirstCornerX = 3084;

    int kOperandSpriteFirstCornerY = 3085;

    int kOperandSpriteSecondCornerX = 3086;

    int kOperandSpriteSecondCornerY = 3087;

    int kOperandSpriteThirdCornerX = 3088;

    int kOperandSpriteThirdCornerY = 3089;

    int kOperandSpriteFourthCornerX = 3090;

    int kOperandSpriteFourthCornerY = 3091;

    int kOperandSpriteImageRegistrationPointX = 3092;

    int kOperandSpriteImageRegistrationPointY = 3093;

    int kOperandQTVRPanAngle = 4096;

    int kOperandQTVRTiltAngle = 4097;

    int kOperandQTVRFieldOfView = 4098;

    int kOperandQTVRNodeID = 4099;

    int kOperandMouseLocalHLoc = 5120;

    int kOperandMouseLocalVLoc = 5121;

    int kOperandKeyIsDown = 5122;

    int kOperandRandom = 5123;

    int kTargetMovie = 1836019574;

    int kTargetTrackName = 1953656417;

    int kTargetTrackID = 1953655140;

    int kTargetTrackType = 1953657977;

    int kTargetTrackIndex = 1953655150;

    int kTargetSpriteName = 1936748129;

    int kTargetSpriteID = 1936746852;

    int kTargetSpriteIndex = 1936746862;

    int kQTEventType = 1702260340;

    int kAction = 1633907822;

    int kWhichAction = 2003331427;

    int kActionParameter = 1885434477;

    int kActionTarget = 1952543335;

    int kActionFlags = 1718378855;

    int kActionParameterMinValue = 1835626102;

    int kActionParameterMaxValue = 1835104374;

    int kActionListAtomType = 1818850164;

    int kExpressionContainerAtomType = 1702391922;

    int kConditionalAtomType = 1952805748;

    int kOperatorAtomType = 1869636978;

    int kOperandAtomType = 1869640302;

    int kCommentAtomType = 2003335456;

    int kQTEventMouseClick = 1668049259;

    int kQTEventMouseClickEnd = 1667591780;

    int kQTEventMouseClickEndTriggerButton = 1953655143;

    int kQTEventMouseEnter = 1701737586;

    int kQTEventMouseExit = 1702390132;

    int kQTEventFrameLoaded = 1718772077;

    int kQTEventIdle = 1768189029;

    int kQTEventRequestToModifyMovie = 1919250797;

    int kActionFlagActionIsDelta = 2;

    int kActionFlagParameterWrapsAround = 4;

    int kActionFlagActionIsToggle = 8;

    int kOperatorAdd = 1633969184;

    int kOperatorSubtract = 1937072672;

    int kOperatorMultiply = 1836412020;

    int kOperatorDivide = 1684633120;

    int kOperatorOr = 1869750304;

    int kOperatorAnd = 1634624544;

    int kOperatorNot = 1852797984;

    int kOperatorLessThan = 1008738336;

    int kOperatorLessThanEqualTo = 1010638880;

    int kOperatorEqualTo = 1025515552;

    int kOperatorNotEqualTo = 557654048;

    int kOperatorGreaterThan = 1042292768;

    int kOperatorGreaterThanEqualTo = 1044193312;

    int kOperatorModulo = 1836016672;

    int kOperatorIntegerDivide = 1768188278;

    int kOperatorAbsoluteValue = 1633841952;

    int kOperatorNegate = 1852139296;

    int kMediaPropertyNonLinearAtomType = 1852796524;

    int kMediaPropertyHasActions = 105;

    int preloadAlways = 1;

    int preloadOnlyIfEnabled = 2;

    int trackUsageInMovie = 2;

    int trackUsageInPreview = 4;

    int trackUsageInPoster = 8;

    int kTrackModifierInput = 26990;

    int kTrackModifierType = 29817;

    int kTrackModifierReference = 1936945763;

    int kTrackModifierObjectID = 1868720484;

    int kTrackModifierInputName = 1851878757;

    int kTrackModifierTypeMatrix = 1;

    int kTrackModifierTypeClip = 2;

    int kTrackModifierTypeGraphicsMode = 5;

    int kTrackModifierTypeVolume = 3;

    int kTrackModifierTypeBalance = 4;

    int kTrackModifierTypeImage = 1986618469;

    int kTrackModifierObjectMatrix = 6;

    int kTrackModifierObjectGraphicsMode = 7;

    int kTrackModifierType3d4x4Matrix = 8;

    int kTrackModifierCameraData = 9;

    int kTrackModifierSoundLocalizationData = 10;

    int kTrackModifierObjectImageIndex = 11;

    int kTrackModifierObjectLayer = 12;

    int kTrackModifierObjectVisible = 13;

    int kTrackModifierPanAngle = 1885433376;

    int kTrackModifierTiltAngle = 1953066100;

    int kTrackModifierVerticalFieldOfViewAngle = 1718580768;

    int kTrackModifierObjectQTEventSend = 1702260340;

    int videoMediaType = 1986618469;

    int soundMediaType = 1936684398;

    int textMediaType = 1952807028;

    int baseMediaType = 1735291491;

    int MPEGMediaType = 1297106247;

    int musicMediaType = 1836413801;

    int timeCodeMediaType = 1953325924;

    int spriteMediaType = 1936749172;

    int tweenMediaType = 1953981806;

    int threeDMediaType = 1902392164;

    int threeDeeMediaType = 1902392164;

    int DVCMediaType = 1685480224;

    int handleDataHandlerSubType = 1752065132;

    int resourceDataHandlerSubType = 1920168547;

    int aliasDataHandlerSubType = 1634494835;

    int urlDataHandlerSubType = 1970433056;

    int mediaSampleNotSync = 1;

    int mediaSampleShadowSync = 2;

    int dataRefSelfReference = 1;

    int dataRefWasNotResolved = 2;

    int dataHandlerType = 1684565106;

    int kDataHCanRead = 1;

    int kDataHSpecialRead = 2;

    int kDataHSpecialReadFile = 4;

    int kDataHCanWrite = 8;

    int kDataHSpecialWrite = 16;

    int kDataHSpecialWriteFile = 32;

    int kDataHCanStreamingWrite = 64;

    int kDataHMustCheckDataRef = 128;

    int kGetMovieImporterValidateToFind = 1;

    int kGetMovieImporterAllowNewFile = 2;

    int kGetMovieImporterDontConsiderGraphicsImporters = 4;

    int mediaHandlerType = 1835560050;

    int kSpritePropertyImageIndex = 100;

    int kSpriteTrackPropertyBackgroundColor = 101;

    int kSpriteTrackPropertyOffscreenBitDepth = 102;

    int kSpriteTrackPropertySampleFormat = 103;

    int kSpriteTrackPropertyScaleSpritesToScaleWorld = 104;

    int kSpriteTrackPropertyHasActions = 105;

    int kSpriteTrackPropertyVisible = 106;

    int kSpriteTrackPropertyQTIdleEventsFrequency = 107;

    int kSpriteImagePropertyRegistrationPoint = 1000;

    int kSpriteImagePropertyGroupID = 1001;

    int kNoQTIdleEvents = -1;

    int kKeyFrameAndSingleOverride = 2;

    int kKeyFrameAndAllOverrides = 4;

    int kSpriteAtomType = 1936749172;

    int kSpriteImagesContainerAtomType = 1768776564;

    int kSpriteImageAtomType = 1768776039;

    int kSpriteImageDataAtomType = 1768776801;

    int kSpriteImageGroupIDAtomType = 1768777586;

    int kSpriteImageRegistrationAtomType = 1768780391;

    int kSpriteSharedDataAtomType = 1684434036;

    int kSpriteNameAtomType = 1851878757;

    int kSpriteImageNameAtomType = 1851878757;

    int kSpriteUsesImageIDsAtomType = 1970496883;

    int findTextEdgeOK = 1;

    int findTextCaseSensitive = 2;

    int findTextReverseSearch = 4;

    int findTextWrapAround = 8;

    int findTextUseOffset = 16;

    int dropShadowOffsetType = 1685221487;

    int dropShadowTranslucencyType = 1685221492;

    int dfDontDisplay = 1;

    int dfDontAutoScale = 2;

    int dfClipToTextBox = 4;

    int dfUseMovieBGColor = 8;

    int dfShrinkTextBoxToFit = 16;

    int dfScrollIn = 32;

    int dfScrollOut = 64;

    int dfHorizScroll = 128;

    int dfReverseScroll = 256;

    int dfContinuousScroll = 512;

    int dfFlowHoriz = 1024;

    int dfContinuousKaraoke = 2048;

    int dfDropShadow = 4096;

    int dfAntiAlias = 8192;

    int dfKeyedText = 16384;

    int dfInverseHilite = 32768;

    int dfTextColorHilite = 65536;

    int k3DMediaRendererEntry = 1919250020;

    int k3DMediaRendererName = 1851878757;

    int k3DMediaRendererCode = 1919119204;

    int kGetAtomicInstNoExpandedSamples = 1;

    int kGetAtomicInstNoOriginalSamples = 2;

    int kGetAtomicInstNoSamples = 3;

    int kGetAtomicInstNoKnobList = 4;

    int kGetAtomicInstNoInstrumentInfo = 8;

    int kGetAtomicInstOriginalKnobList = 16;

    int kGetAtomicInstAllKnobs = 32;

    int kSetAtomicInstKeepOriginalInstrument = 1;

    int kSetAtomicInstShareAcrossParts = 2;

    int kSetAtomicInstCallerTosses = 4;

    int kSetAtomicInstCallerGuarantees = 8;

    int kSetAtomicInstInterruptSafe = 16;

    int kSetAtomicInstDontPreprocess = 128;

    int kInstrumentExactMatch = 2;

    int kInstrumentRecommendedSubstitute = 1;

    int kInstrumentRoland8BitQuality = 5;

    int kGetInstrumentInfoNoBuiltIn = 1;

    int kGetInstrumentInfoMidiUserInst = 2;

    int kGetInstrumentInfoNoIText = 4;

    int kBaseInstrumentSubType = 1650553701;

    int kInstrumentComponentType = 1768846196;

    int kMusicComponentType = 1836413801;

    int kSoftSynthComponentSubType = 1936924704;

    int kGMSynthComponentSubType = 1735204896;

    int kaiToneDescType = 1953459813;

    int kaiNoteRequestInfoType = 1853125233;

    int kaiKnobListType = 1802396268;

    int kaiKeyRangeInfoType = 1936289382;

    int kaiSampleDescType = 1935962979;

    int kaiSampleInfoType = 1936550254;

    int kaiSampleDataType = 1935958388;

    int kaiSampleDataQUIDType = 1903520100;

    int kaiInstInfoType = 1768517222;

    int kaiPictType = 1885954932;

    int kaiWriterType = 1081569908;

    int kaiCopyrightType = 1080258681;

    int kaiOtherStrType = 1937011232;

    int kaiInstrumentRefType = 1769104742;

    int kaiInstGMQualityType = 1903518060;

    int kaiLibraryInfoType = 1818848870;

    int kaiLibraryDescType = 1818522467;

    int kInstKnobMissingUnknown = 0;

    int kInstKnobMissingDefault = 1;

    int kMusicLoopTypeNormal = 0;

    int kMusicLoopTypePalindrome = 1;

    int instSamplePreProcessFlag = 1;

    int kQTMIDIComponentType = 1835623529;

    int kOMSComponentSubType = 1330467616;

    int kFMSComponentSubType = 1179472672;

    int kMIDIManagerComponentSubType = 1835886450;

    int kAppleSysexID = 17;

    int kAppleSysexCmdSampleSize = 1;

    int kAppleSysexCmdSampleBreak = 2;

    int kAppleSysexCmdAtomicInstrument = 16;

    int kAppleSysexCmdDeveloper = 32512;

    int kMusicPacketPortLost = 1;

    int kMusicPacketPortFound = 2;

    int kMusicPacketTimeGap = 3;

    int kNoteAllocatorComponentType = 1852798049;

    int kPickDontMix = 1;

    int kPickSameSynth = 2;

    int kPickUserInsts = 4;

    int kPickEditAllowEdit = 8;

    int kPickEditAllowPick = 16;

    int kPickEditSynthGlobal = 32;

    int kPickEditControllers = 64;

    int kNoteRequestNoGM = 1;

    int kNoteRequestNoSynthType = 2;

    int kNoteRequestSynthMustMatch = 4;

    int kVoiceCountDynamic = -1;

    int kGeneralEventNoteRequest = 1;

    int kGeneralEventPartKey = 4;

    int kGeneralEventTuneDifference = 5;

    int kGeneralEventAtomicInstrument = 6;

    int kGeneralEventKnob = 7;

    int kGeneralEventMIDIChannel = 8;

    int kGeneralEventPartChange = 9;

    int kGeneralEventNoOp = 10;

    int kGeneralEventUsedNotes = 11;

    int kGeneralEventPartMix = 12;

    /**
     * Marker Event Defined Types  // marker is 60 ee vv vv in hex, where e = event type, and v = value
     */
    int kMarkerEventEnd = 0;

    /**
     * Marker Event Defined Types  // marker is 60 ee vv vv in hex, where e = event type, and v = value
     */
    int kMarkerEventBeat = 1;

    /**
     * Marker Event Defined Types  // marker is 60 ee vv vv in hex, where e = event type, and v = value
     */
    int kMarkerEventTempo = 2;

    int kMIDINoteOn = 144;

    int kMIDINoteOff = 128;

    int kMIDIPolyPressure = 160;

    int kMIDIControlChange = 176;

    int kMIDIProgramChange = 192;

    int kMIDIAftertouch = 208;

    int kMIDIChannelPressure = 208;

    int kMIDIPitchBend = 224;

    int kMIDIBeginSystemExclusive = 240;

    int kMIDIEndSystemExclusive = 247;

    int kMIDIChannel1 = 0;

    int kMIDIChannel2 = 1;

    int kMIDIChannel3 = 2;

    int kMIDIChannel4 = 3;

    int kMIDIChannel5 = 4;

    int kMIDIChannel6 = 5;

    int kMIDIChannel7 = 6;

    int kMIDIChannel8 = 7;

    int kMIDIChannel9 = 8;

    int kMIDIChannel10 = 9;

    int kMIDIChannel11 = 10;

    int kMIDIChannel12 = 11;

    int kMIDIChannel13 = 12;

    int kMIDIChannel14 = 13;

    int kMIDIChannel15 = 14;

    int kMIDIChannel16 = 15;

    int kControllerModulationWheel = 1;

    int kControllerBreath = 2;

    int kControllerFoot = 4;

    int kControllerPortamentoTime = 5;

    int kControllerVolume = 7;

    int kControllerBalance = 8;

    int kControllerPan = 10;

    int kControllerExpression = 11;

    int kControllerLever1 = 16;

    int kControllerLever2 = 17;

    int kControllerLever3 = 18;

    int kControllerLever4 = 19;

    int kControllerLever5 = 80;

    int kControllerLever6 = 81;

    int kControllerLever7 = 82;

    int kControllerLever8 = 83;

    int kControllerPitchBend = 32;

    int kControllerAfterTouch = 33;

    int kControllerPartTranspose = 40;

    int kControllerTuneTranspose = 41;

    int kControllerPartVolume = 42;

    int kControllerTuneVolume = 43;

    int kControllerSustain = 64;

    int kControllerPortamento = 65;

    int kControllerSostenuto = 66;

    int kControllerSoftPedal = 67;

    int kControllerReverb = 91;

    int kControllerTremolo = 92;

    int kControllerChorus = 93;

    int kControllerCeleste = 94;

    int kControllerPhaser = 95;

    int kControllerEditPart = 113;

    int kControllerMasterTune = 114;

    int kControllerMasterTranspose = 114;

    int kControllerMasterVolume = 115;

    int kControllerMasterCPULoad = 116;

    int kControllerMasterPolyphony = 117;

    int kControllerMasterFeatures = 118;

    int kQTMSKnobStartID = 33554432;

    int kQTMSKnobVolumeAttackTimeID = 33554433;

    int kQTMSKnobVolumeDecayTimeID = 33554434;

    int kQTMSKnobVolumeSustainLevelID = 33554435;

    int kQTMSKnobVolumeRelease1RateID = 33554436;

    int kQTMSKnobVolumeDecayKeyScalingID = 33554437;

    int kQTMSKnobVolumeReleaseTimeID = 33554438;

    int kQTMSKnobVolumeLFODelayID = 33554439;

    int kQTMSKnobVolumeLFORampTimeID = 33554440;

    int kQTMSKnobVolumeLFOPeriodID = 33554441;

    int kQTMSKnobVolumeLFOShapeID = 33554442;

    int kQTMSKnobVolumeLFODepthID = 33554443;

    int kQTMSKnobVolumeOverallID = 33554444;

    int kQTMSKnobVolumeVelocity127ID = 33554445;

    int kQTMSKnobVolumeVelocity96ID = 33554446;

    int kQTMSKnobVolumeVelocity64ID = 33554447;

    int kQTMSKnobVolumeVelocity32ID = 33554448;

    int kQTMSKnobVolumeVelocity16ID = 33554449;

    int kQTMSKnobPitchTransposeID = 33554450;

    int kQTMSKnobPitchLFODelayID = 33554451;

    int kQTMSKnobPitchLFORampTimeID = 33554452;

    int kQTMSKnobPitchLFOPeriodID = 33554453;

    int kQTMSKnobPitchLFOShapeID = 33554454;

    int kQTMSKnobPitchLFODepthID = 33554455;

    int kQTMSKnobPitchLFOQuantizeID = 33554456;

    int kQTMSKnobStereoDefaultPanID = 33554457;

    int kQTMSKnobStereoPositionKeyScalingID = 33554458;

    int kQTMSKnobPitchLFOOffsetID = 33554459;

    int kQTMSKnobExclusionGroupID = 33554460;

    int kQTMSKnobSustainTimeID = 33554461;

    int kQTMSKnobSustainInfiniteID = 33554462;

    int kQTMSKnobVolumeLFOStereoID = 33554463;

    int kQTMSKnobVelocityLowID = 33554464;

    int kQTMSKnobVelocityHighID = 33554465;

    int kQTMSKnobVelocitySensitivityID = 33554466;

    int kQTMSKnobPitchSensitivityID = 33554467;

    int kQTMSKnobVolumeLFODepthFromWheelID = 33554468;

    int kQTMSKnobPitchLFODepthFromWheelID = 33554469;

    int kQTMSKnobVolumeExpOptionsID = 33554470;

    int kQTMSKnobEnv1AttackTimeID = 33554471;

    int kQTMSKnobEnv1DecayTimeID = 33554472;

    int kQTMSKnobEnv1SustainLevelID = 33554473;

    int kQTMSKnobEnv1SustainTimeID = 33554474;

    int kQTMSKnobEnv1SustainInfiniteID = 33554475;

    int kQTMSKnobEnv1ReleaseTimeID = 33554476;

    int kQTMSKnobEnv1ExpOptionsID = 33554477;

    int kQTMSKnobEnv2AttackTimeID = 33554478;

    int kQTMSKnobEnv2DecayTimeID = 33554479;

    int kQTMSKnobEnv2SustainLevelID = 33554480;

    int kQTMSKnobEnv2SustainTimeID = 33554481;

    int kQTMSKnobEnv2SustainInfiniteID = 33554482;

    int kQTMSKnobEnv2ReleaseTimeID = 33554483;

    int kQTMSKnobEnv2ExpOptionsID = 33554484;

    int kQTMSKnobPitchEnvelopeID = 33554485;

    int kQTMSKnobPitchEnvelopeDepthID = 33554486;

    int kQTMSKnobFilterKeyFollowID = 33554487;

    int kQTMSKnobFilterTransposeID = 33554488;

    int kQTMSKnobFilterQID = 33554489;

    int kQTMSKnobFilterFrequencyEnvelopeID = 33554490;

    int kQTMSKnobFilterFrequencyEnvelopeDepthID = 33554491;

    int kQTMSKnobFilterQEnvelopeID = 33554492;

    int kQTMSKnobFilterQEnvelopeDepthID = 33554493;

    int kQTMSKnobReverbThresholdID = 33554494;

    int kQTMSKnobVolumeAttackVelScalingID = 33554495;

    int kQTMSKnobLastIDPlus1 = 33554496;

    int kFirstGMInstrument = 1;

    int kLastGMInstrument = 128;

    int kFirstGSInstrument = 129;

    int kLastGSInstrument = 16383;

    int kFirstROMInstrument = 32768;

    int kLastROMInstrument = 65535;

    int kFirstUserInstrument = 65536;

    int kLastUserInstrument = 131071;

    int kInstrumentMatchSynthesizerType = 1;

    int kInstrumentMatchSynthesizerName = 2;

    int kInstrumentMatchName = 4;

    int kInstrumentMatchNumber = 8;

    int kInstrumentMatchGMNumber = 16;

    int kInstrumentMatchGSNumber = 32;

    int kKnobBasic = 8;

    int kKnobReadOnly = 16;

    int kKnobInterruptUnsafe = 32;

    int kKnobKeyrangeOverride = 64;

    int kKnobGroupStart = 128;

    int kKnobFixedPoint8 = 1024;

    int kKnobFixedPoint16 = 2048;

    int kKnobTypeNumber = 0;

    int kKnobTypeGroupName = 4096;

    int kKnobTypeBoolean = 8192;

    int kKnobTypeNote = 12288;

    int kKnobTypePan = 16384;

    int kKnobTypeInstrument = 20480;

    int kKnobTypeSetting = 24576;

    int kKnobTypeMilliseconds = 28672;

    int kKnobTypePercentage = 32768;

    int kKnobTypeHertz = 36864;

    int kKnobTypeButton = 40960;

    int kUnknownKnobValue = Integer.MAX_VALUE;

    int kDefaultKnobValue = 2147483646;

    short kControllerMaximum = 32767;

    short kControllerMinimum = -32768;

    int kNoDrumKit = 16384;

    int kFirstDrumkit = 16385;

    int kLastDrumkit = 16512;

    int kSynthesizerConnectionMono = 1;

    int kSynthesizerConnectionMMgr = 2;

    int kSynthesizerConnectionOMS = 4;

    int kSynthesizerConnectionQT = 8;

    int kSynthesizerConnectionUnavailable = 256;

    int kSynthesizerConnectionFMS = 16;

    int kSynthesizerDynamicVoice = 1;

    int kSynthesizerUsesMIDIPort = 2;

    int kSynthesizerMicrotone = 4;

    int kSynthesizerHasSamples = 8;

    int kSynthesizerMixedDrums = 16;

    int kSynthesizerSoftware = 32;

    int kSynthesizerHardware = 64;

    int kSynthesizerDynamicChannel = 128;

    int kSynthesizerHogsSystemChannel = 256;

    int kSynthesizerHasSystemChannel = 512;

    int kSynthesizerSlowSetPart = 1024;

    int kSynthesizerOffline = 4096;

    int kSynthesizerGM = 16384;

    int kSynthesizerDLS = 32768;

    int kSynthesizerSoundLocalization = 65536;

    /**
     * TunePlayer component type
     */
    int kTunePlayerComponentType = 1953853029;

    /**
     * Values for when to start.
     */
    int kTuneStartNow = 1;

    /**
     * Values for when to start.
     */
    int kTuneDontClipNotes = 2;

    /**
     * Values for when to start.
     */
    int kTuneExcludeEdgeNotes = 4;

    /**
     * Values for when to start.
     */
    int kTuneQuickStart = 8;

    /**
     * Values for when to start.
     */
    int kTuneLoopUntil = 16;

    /**
     * Values for when to start.
     */
    int kTunePlayDifference = 32;

    /**
     * Values for when to start.
     */
    int kTunePlayConcurrent = 64;

    /**
     * Values for when to start.
     */
    int kTuneStartNewMaster = 16384;

    /**
     * Values for stopping.
     */
    int kTuneStopFade = 1;

    /**
     * Values for stopping.
     */
    int kTuneStopSustain = 2;

    /**
     * Values for stopping.
     */
    int kTuneStopInstant = 4;

    /**
     * Values for stopping.
     */
    int kTuneStopReleaseChannels = 8;

    /**
     * flags for part mix.
     */
    int kTuneMixMute = 1;

    /**
     * flags for part mix.
     */
    int kTuneMixSolo = 2;

    int kRestEventType = 0;

    int kNoteEventType = 1;

    int kControlEventType = 2;

    int kMarkerEventType = 3;

    int kUndefined1EventType = 8;

    int kXNoteEventType = 9;

    int kXControlEventType = 10;

    int kKnobEventType = 11;

    int kUndefined2EventType = 12;

    int kUndefined3EventType = 13;

    int kUndefined4EventType = 14;

    int kGeneralEventType = 15;

    int kXEventLengthBits = 2;

    int kGeneralEventLengthBits = 3;

    int kEventLen = 1;

    int kXEventLen = 2;

    int kRestEventLen = 1;

    int kNoteEventLen = 1;

    int kControlEventLen = 1;

    int kMarkerEventLen = 1;

    int kXNoteEventLen = 2;

    int kXControlEventLen = 2;

    int kGeneralEventLen = 2;

    /**
     * Universal Event Defines
     */
    int kEventLengthFieldPos = 30;

    /**
     * Universal Event Defines
     */
    int kEventLengthFieldWidth = 2;

    /**
     * Universal Event Defines
     */
    int kEventTypeFieldPos = 29;

    /**
     * Universal Event Defines
     */
    int kEventTypeFieldWidth = 3;

    /**
     * Universal Event Defines
     */
    int kXEventTypeFieldPos = 28;

    /**
     * Universal Event Defines
     */
    int kXEventTypeFieldWidth = 4;

    /**
     * Universal Event Defines
     */
    int kEventPartFieldPos = 24;

    /**
     * Universal Event Defines
     */
    int kEventPartFieldWidth = 5;

    /**
     * Universal Event Defines
     */
    int kXEventPartFieldPos = 16;

    /**
     * Universal Event Defines
     */
    int kXEventPartFieldWidth = 12;

    /**
     * Universal Event Defines
     */
    int kRestEventDurationFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kRestEventDurationFieldWidth = 24;

    /**
     * Universal Event Defines
     */
    int kRestEventDurationMax = 16777215;

    /**
     * Universal Event Defines
     */
    int kNoteEventPitchFieldPos = 18;

    /**
     * Universal Event Defines
     */
    int kNoteEventPitchFieldWidth = 6;

    /**
     * Universal Event Defines
     */
    int kNoteEventPitchOffset = 32;

    /**
     * Universal Event Defines
     */
    int kNoteEventVolumeFieldPos = 11;

    /**
     * Universal Event Defines
     */
    int kNoteEventVolumeFieldWidth = 7;

    /**
     * Universal Event Defines
     */
    int kNoteEventVolumeOffset = 0;

    /**
     * Universal Event Defines
     */
    int kNoteEventDurationFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kNoteEventDurationFieldWidth = 11;

    /**
     * Universal Event Defines
     */
    int kNoteEventDurationMax = 2047;

    /**
     * Universal Event Defines
     */
    int kXNoteEventPitchFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kXNoteEventPitchFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kXNoteEventDurationFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kXNoteEventDurationFieldWidth = 22;

    /**
     * Universal Event Defines
     */
    int kXNoteEventDurationMax = 4194303;

    /**
     * Universal Event Defines
     */
    int kXNoteEventVolumeFieldPos = 22;

    /**
     * Universal Event Defines
     */
    int kXNoteEventVolumeFieldWidth = 7;

    /**
     * Universal Event Defines
     */
    int kControlEventControllerFieldPos = 16;

    /**
     * Universal Event Defines
     */
    int kControlEventControllerFieldWidth = 8;

    /**
     * Universal Event Defines
     */
    int kControlEventValueFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kControlEventValueFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kXControlEventControllerFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kXControlEventControllerFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kXControlEventValueFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kXControlEventValueFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kKnobEventValueHighFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kKnobEventValueHighFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kKnobEventKnobFieldPos = 16;

    /**
     * Universal Event Defines
     */
    int kKnobEventKnobFieldWidth = 14;

    /**
     * Universal Event Defines
     */
    int kKnobEventValueLowFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kKnobEventValueLowFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kMarkerEventSubtypeFieldPos = 16;

    /**
     * Universal Event Defines
     */
    int kMarkerEventSubtypeFieldWidth = 8;

    /**
     * Universal Event Defines
     */
    int kMarkerEventValueFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kMarkerEventValueFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kGeneralEventSubtypeFieldPos = 16;

    /**
     * Universal Event Defines
     */
    int kGeneralEventSubtypeFieldWidth = 14;

    /**
     * Universal Event Defines
     */
    int kGeneralEventLengthFieldPos = 0;

    /**
     * Universal Event Defines
     */
    int kGeneralEventLengthFieldWidth = 16;

    /**
     * Universal Event Defines
     */
    int kCurrentlyNativeEndian = 1;

    /**
     * Universal Event Defines
     */
    int kCurrentlyNotNativeEndian = 2;

    int movieExportType = 1936746868;

    int kMovieExportTextOnly = 0;

    int kMovieExportAbsoluteTime = 1;

    int kMovieExportRelativeTime = 2;

    int movieImportType = 1700885536;

    int movieImportCreateTrack = 1;

    int movieImportInParallel = 2;

    int movieImportMustUseTrack = 4;

    int movieImportResultUsedMultipleTracks = 8;

    int kMIDIImportSilenceBefore = 1;

    int kMIDIImportSilenceAfter = 2;

    int kMIDIImport20Playable = 4;

    int kMIDIImportWantLyrics = 8;

    int kQTSettingsEffect = 1701209701;

    int kQTSettingsMIDI = 1296647241;

    int kQTSettingsMIDISettingFlags = 1937011815;

    int kQTSettingsText = 1952807028;

    int kQTSettingsTextDescription = 1684370275;

    int kQTSettingsTextSize = 1936292453;

    int kQTSettingsTextSettingFlags = 1937011815;

    int kQTSettingsTextTimeFraction = 1953066342;

    int kQTSettingsTime = 1953066341;

    int kQTSettingsAudioCDTrack = 1953653099;

    int kQTSettingsAudioCDTrackRateShift = 1920165990;

    int tcDropFrame = 1;

    int tc24HourMax = 2;

    int tcNegTimesOK = 4;

    int tcCounter = 8;

    int tcdfShowTimeCode = 1;

    int tweenComponentType = 1953981806;

    int kTweenEntry = 1953981806;

    int kTweenData = 1684108385;

    int kTweenType = 1953984116;

    int kTweenStartOffset = 1953985396;

    int kTweenDuration = 1953981557;

    int kTweenFlags = 1718378855;

    int kTweenOutputMin = 1869441390;

    int kTweenOutputMax = 1869439352;

    int kTweenSequenceElement = 1936028005;

    int kTween3dInitialCondition = 1768124004;

    int kTweenInterpolationID = 1768846450;

    int kTweenInterpolateStyle = 1769174137;

    int kInputMapSubInputID = 1937072745;

    int kTweenRegionData = 1902408295;

    int kTweenPictureData = 1346978644;

    int kListElementType = 1954115685;

    int kListElementDataType = 1684108409;

    int kNameAtom = 1851878757;

    int kInitialRotationAtom = 1768845935;

    int kNonLinearTweenHeader = 1852601448;

    int kTweenReturnDelta = 1;

    int kTweenTypeShort = 1;

    int kTweenTypeLong = 2;

    int kTweenTypeFixed = 3;

    int kTweenTypeFloat = 3;

    int kTweenTypePoint = 4;

    int kTweenTypeQDRect = 5;

    int kTweenTypeQDRegion = 6;

    int kTweenTypeMatrix = 7;

    int kTweenTypeRGBColor = 8;

    int kTweenTypeGraphicsModeWithRGBColor = 9;

    int kTweenTypeQTFloatSingle = 10;

    int kTweenTypeQTFloatDouble = 11;

    int kTweenTypeFixedPoint = 12;

    int kTweenType3dScale = 863200097;

    int kTweenType3dTranslate = 863269473;

    int kTweenType3dRotate = 863137652;

    int kTweenType3dRotateAboutPoint = 863134064;

    int kTweenType3dRotateAboutAxis = 863134072;

    int kTweenType3dRotateAboutVector = 863139427;

    int kTweenType3dQuaternion = 863073633;

    int kTweenType3dMatrix = 862806388;

    int kTweenType3dCameraData = 862151021;

    int kTweenType3dSoundLocalizationData = 863202403;

    int kTweenTypePathToMatrixTranslation = 1735945588;

    int kTweenTypePathToMatrixRotation = 1735946354;

    int kTweenTypePathToMatrixTranslationAndRotation = 1735945586;

    int kTweenTypePathToFixedPoint = 1735943792;

    int kTweenTypePathXtoY = 1735948409;

    int kTweenTypePathYtoX = 1735948664;

    int kTweenTypeAtomList = 1635020653;

    int kTweenTypePolygon = 1886350457;

    int kTweenTypeMultiMatrix = 1836412013;

    int kTweenTypeSpin = 1936746862;

    int kTweenType3dMatrixNonLinear = 862874738;

    int kTweenType3dVRObject = 863400559;

    int kParameterTypeDataLong = 2;

    int kParameterTypeDataFixed = 3;

    int kParameterTypeDataRGBValue = 8;

    int kParameterTypeDataDouble = 11;

    int seqGrabComponentType = 1650553447;

    int seqGrabChannelType = 1936155496;

    int seqGrabPanelType = 1936158830;

    int seqGrabCompressionPanelType = 1668116594;

    int seqGrabSourcePanelType = 1936684402;

    int seqGrabUnpause = 0;

    int seqGrabPause = 1;

    int seqGrabPauseForMenu = 3;

    int seqGrabToDisk = 1;

    int seqGrabToMemory = 2;

    int seqGrabDontUseTempMemory = 4;

    int seqGrabAppendToFile = 8;

    int seqGrabDontAddMovieResource = 16;

    int seqGrabDontMakeMovie = 32;

    int seqGrabPreExtendFile = 64;

    int seqGrabDataProcIsInterruptSafe = 128;

    int seqGrabDataProcDoesOverlappingReads = 256;

    int seqGrabRecord = 1;

    int seqGrabPreview = 2;

    int seqGrabPlayDuringRecord = 4;

    int seqGrabHasBounds = 1;

    int seqGrabHasVolume = 2;

    int seqGrabHasDiscreteSamples = 4;

    int sgDeviceListWithIcons = 1;

    int sgDeviceListDontCheckAvailability = 2;

    int seqGrabWriteAppend = 0;

    int seqGrabWriteReserve = 1;

    int seqGrabWriteFill = 2;

    int channelPlayNormal = 0;

    int channelPlayFast = 1;

    int channelPlayHighQuality = 2;

    int channelPlayAllData = 4;

    int channelFlagDontOpenResFile = 2;

    int channelFlagHasDependency = 4;

    int videoDigitizerComponentType = 1986618471;

    int vdigInterfaceRev = 2;

    /**
     * Input Format Standards
     */
    int ntscIn = 0;

    /**
     * Input Format Standards
     */
    int currentIn = 0;

    /**
     * Input Format Standards
     */
    int palIn = 1;

    /**
     * Input Format Standards
     */
    int secamIn = 2;

    /**
     * Input Format Standards
     */
    int ntscReallyIn = 3;

    /**
     * Input Formats
     */
    int compositeIn = 0;

    /**
     * Input Formats
     */
    int sVideoIn = 1;

    /**
     * Input Formats
     */
    int rgbComponentIn = 2;

    /**
     * Input Formats
     */
    int rgbComponentSyncIn = 3;

    /**
     * Input Formats
     */
    int yuvComponentIn = 4;

    /**
     * Input Formats
     */
    int yuvComponentSyncIn = 5;

    /**
     * Input Formats
     */
    int tvTunerIn = 6;

    /**
     * Input Formats
     */
    int sdiIn = 7;

    /**
     * Video Digitizer PlayThru States
     */
    int vdPlayThruOff = 0;

    /**
     * Video Digitizer PlayThru States
     */
    int vdPlayThruOn = 1;

    /**
     * Input Color Space Modes
     */
    int vdDigitizerBW = 0;

    /**
     * Input Color Space Modes
     */
    int vdDigitizerRGB = 1;

    /**
     * Phase Lock Loop Modes
     */
    int vdBroadcastMode = 0;

    /**
     * Phase Lock Loop Modes
     */
    int vdVTRMode = 1;

    /**
     * Field Select Options
     */
    int vdUseAnyField = 0;

    /**
     * Field Select Options
     */
    int vdUseOddField = 1;

    /**
     * Field Select Options
     */
    int vdUseEvenField = 2;

    /**
     * vdig types
     */
    int vdTypeBasic = 0;

    /**
     * vdig types
     */
    int vdTypeAlpha = 1;

    /**
     * vdig types
     */
    int vdTypeMask = 2;

    /**
     * vdig types
     */
    int vdTypeKey = 3;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesNTSC = 1;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesPAL = 2;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesSECAM = 4;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesGenLock = 128;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesComposite = 256;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesSVideo = 512;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesComponent = 1024;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInVTR_Broadcast = 2048;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesColor = 4096;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInDoesBW = 8192;

    /**
     * Digitizer Input Capability/Current Flags
     */
    int digiInSignalLock = Integer.MIN_VALUE;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoes1 = 1;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoes2 = 2;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoes4 = 4;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoes8 = 8;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoes16 = 16;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoes32 = 32;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesDither = 64;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesStretch = 128;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesShrink = 256;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesMask = 512;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesDouble = 2048;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesQuad = 4096;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesQuarter = 8192;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesSixteenth = 16384;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesRotate = 32768;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesHorizFlip = 65536;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesVertFlip = 131072;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesSkew = 262144;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesBlend = 524288;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesWarp = 1048576;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesHW_DMA = 2097152;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesHWPlayThru = 4194304;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesILUT = 8388608;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesKeyColor = 16777216;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesAsyncGrabs = 33554432;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesUnreadableScreenBits = 67108864;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesCompress = 134217728;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesCompressOnly = 268435456;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesPlayThruDuringCompress = 536870912;

    /**
     * Digitizer Output Capability/Current Flags
     */
    int digiOutDoesCompressPartiallyVisible = 1073741824;

    int dmaDepth1 = 1;

    int dmaDepth2 = 2;

    int dmaDepth4 = 4;

    int dmaDepth8 = 8;

    int dmaDepth16 = 16;

    int dmaDepth32 = 32;

    int dmaDepth2Gray = 64;

    int dmaDepth4Gray = 128;

    int dmaDepth8Gray = 256;

    int kVDIGControlledFrameRate = -1;

    int sgChannelAtom = 1667785070;

    int sgChannelSettingsAtom = 1668575085;

    int sgChannelDescription = 1667527523;

    int sgChannelSettings = 1668506996;

    int sgDeviceNameType = 1851878757;

    int sgUsageType = 1970496800;

    int sgPlayFlagsType = 1886157158;

    int sgClipType = 1668049264;

    int sgMatrixType = 1836348024;

    int sgVolumeType = 1987013749;

    int sgPanelSettingsAtom = 1886678893;

    int sgPanelDescription = 1885631331;

    int sgPanelSettings = 1886610804;

    int sgcSoundCompressionType = 1935895920;

    int sgcSoundSampleRateType = 1936875892;

    int sgcSoundChannelCountType = 1935894638;

    int sgcSoundSampleSizeType = 1936943482;

    int sgcSoundInputType = 1936289392;

    int sgcSoundGainType = 1734437230;

    int sgcVideoHueType = 1752524064;

    int sgcVideoSaturationType = 1935766642;

    int sgcVideoContrastType = 1953657716;

    int sgcVideoSharpnessType = 1936224880;

    int sgcVideoBrigtnessType = 1651665268;

    int sgcVideoBlackLevelType = 1651272556;

    int sgcVideoWhiteLevelType = 2003334252;

    int sgcVideoInputType = 1986621040;

    int sgcVideoFormatType = 1987277924;

    int sgcVideoFilterType = 1986423924;

    int sgcVideoRectType = 1987208052;

    int sgcVideoDigitizerType = 1986292071;

    /**
     * Display mode atom types
     */
    int kQTVODisplayModeItem = 1902407017;

    /**
     * Display mode atom types
     */
    int kQTVODimensions = 1684630894;

    /**
     * Display mode atom types
     */
    int kQTVOResolution = 1919251308;

    /**
     * Display mode atom types
     */
    int kQTVORefreshRate = 1919247986;

    /**
     * Display mode atom types
     */
    int kQTVOPixelType = 1885960300;

    /**
     * Display mode atom types
     */
    int kQTVOName = 1851878757;

    /**
     * Display mode atom types
     */
    int kQTVODecompressors = 1684366191;

    /**
     * Display mode atom types
     */
    int kQTVODecompressorType = 1684370553;

    /**
     * Display mode atom types
     */
    int kQTVODecompressorContinuous = 1668247156;

    /**
     * Display mode atom types
     */
    int kQTVODecompressorComponent = 1668116596;

    int canMovieImportHandles = 1;

    int canMovieImportFiles = 2;

    int hasMovieImportUserInterface = 4;

    int canMovieExportHandles = 8;

    int canMovieExportFiles = 16;

    int hasMovieExportUserInterface = 32;

    int dontAutoFileMovieImport = 64;

    int canMovieExportAuxDataHandle = 128;

    int canMovieImportValidateHandles = 256;

    int canMovieImportValidateFile = 512;

    int dontRegisterWithEasyOpen = 1024;

    int canMovieImportInPlace = 2048;

    int movieImportSubTypeIsFileExtension = 4096;

    int canMovieImportPartial = 8192;

    int hasMovieImportMIMEList = 16384;

    int canMovieExportFromProcedures = 32768;

    int canMovieExportValidateMovie = 65536;

    int movieExportNeedsResourceFork = 131072;

    int canMovieImportDataReferences = 262144;

    int movieExportMustGetSourceMediaType = 524288;

    int reservedForUseByGraphicsImporters = 8388608;

    int scPositionRect = 2;

    int scPositionDialog = 3;

    int scSetTestImagePictHandle = 4;

    int scSetTestImagePictFile = 5;

    int scSetTestImagePixMap = 6;

    int scGetBestDeviceRect = 7;

    int scRequestImageSettings = 10;

    int scCompressImage = 11;

    int scCompressPicture = 12;

    int scCompressPictureFile = 13;

    int scRequestSequenceSettings = 14;

    int scCompressSequenceBegin = 15;

    int scCompressSequenceFrame = 16;

    int scCompressSequenceEnd = 17;

    int scDefaultPictHandleSettings = 18;

    int scDefaultPictFileSettings = 19;

    int scDefaultPixMapSettings = 20;

    int scGetInfo = 21;

    int scSetInfo = 22;

    int scNewGWorld = 23;
}
