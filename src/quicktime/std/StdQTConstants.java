
package quicktime.std;

public abstract interface StdQTConstants {
    public static final int codecCanScale = 1;

    public static final int codecCanMask = 2;

    public static final int codecCanMatte = 4;

    public static final int codecCanTransform = 8;

    public static final int codecCanTransferMode = 16;

    public static final int codecCanCopyPrev = 32;

    public static final int codecCanSpool = 64;

    public static final int codecCanClipVertical = 128;

    public static final int codecCanClipRectangular = 256;

    public static final int codecCanRemapColor = 512;

    public static final int codecCanFastDither = 1024;

    public static final int codecCanSrcExtract = 2048;

    public static final int codecCanCopyPrevComp = 4096;

    public static final int codecCanAsync = 8192;

    public static final int codecCanMakeMask = 16384;

    public static final int codecCanShift = 32768;

    public static final int codecCanAsyncWhen = 65536;

    public static final int codecCanShieldCursor = 131072;

    public static final int codecCanManagePrevBuffer = 262144;

    public static final int codecHasVolatileBuffer = 524288;

    public static final int codecWantsRegionMask = 1048576;

    public static final int codecImageBufferIsOnScreen = 2097152;

    public static final int codecWantsDestinationPixels = 4194304;

    public static final int codecWantsSpecialScaling = 8388608;

    public static final int codecHandlesInputs = 16777216;

    public static final int codecCanDoIndirectSurface = 33554432;

    public static final int codecIsSequenceSensitive = 67108864;

    public static final int codecRequiresOffscreen = 134217728;

    public static final int codecRequiresMaskBits = 268435456;

    public static final int codecCanRemapResolution = 536870912;

    public static final int codecIsDirectToScreenOnly = 1073741824;

    public static final int codecCanLockSurface = Integer.MIN_VALUE;

    public static final int codecConditionFirstBand = 1;

    public static final int codecConditionLastBand = 2;

    public static final int codecConditionFirstFrame = 4;

    public static final int codecConditionNewDepth = 8;

    public static final int codecConditionNewTransform = 16;

    public static final int codecConditionNewSrcRect = 32;

    public static final int codecConditionNewMask = 64;

    public static final int codecConditionNewMatte = 128;

    public static final int codecConditionNewTransferMode = 256;

    public static final int codecConditionNewClut = 512;

    public static final int codecConditionNewAccuracy = 1024;

    public static final int codecConditionNewDestination = 2048;

    public static final int codecConditionFirstScreen = 4096;

    public static final int codecConditionDoCursor = 8192;

    public static final int codecConditionCatchUpDiff = 16384;

    public static final int codecConditionMaskMayBeChanged = 32768;

    public static final int codecConditionToBuffer = 65536;

    public static final int codecConditionCodecChangedMask = Integer.MIN_VALUE;

    public static final int codecInfoResourceType = 1667523433;

    public static final int codecInterfaceVersion = 2;

    public static final int matrixFlagScale2x = 128;

    public static final int matrixFlagScale1x = 64;

    public static final int matrixFlagScaleHalf = 32;

    public static final int kScreenFloodMethodNone = 0;

    public static final int kScreenFloodMethodKeyColor = 1;

    public static final int kScreenFloodMethodAlpha = 2;

    public static final int kFlushLastQueuedFrame = 0;

    public static final int kFlushFirstQueuedFrame = 1;

    public static final int kNewImageGWorldErase = 1;

    public static final int kCodecFrameTypeUnknown = 0;

    public static final int kCodecFrameTypeKey = 1;

    public static final int kCodecFrameTypeDifference = 2;

    public static final int kCodecFrameTypeDroppableDifference = 3;

    public static final int kParameterTitleName = 1851878757;

    public static final int kParameterTitleID = 1;

    public static final int kParameterWhatName = 2003329396;

    public static final int kParameterWhatID = 1;

    public static final int kParameterVersionName = 1986359923;

    public static final int kParameterVersionID = 1;

    public static final int kParameterRepeatableName = 1885697125;

    public static final int kParameterRepeatableID = 1;

    public static final int kParameterRepeatableTrue = 1;

    public static final int kParameterRepeatableFalse = 0;

    public static final int kParameterAlternateCodecName = 1937072755;

    public static final int kParameterAlternateCodecID = 1;

    public static final int kParameterSourceCountName = 1936876403;

    public static final int kParameterSourceCountID = 1;

    public static final int kParameterDependencyName = 1684366704;

    public static final int kParameterDependencyID = 1;

    public static final int kParameterListDependsUponColorProfiles = 1886547814;

    public static final int kParameterListDependsUponFonts = 1718578804;

    public static final int kParameterEnumList = 1701737837;

    public static final int kParameterAtomTypeAndID = 1954115685;

    public static final int kNoAtom = 1852796517;

    public static final int kAtomNoFlags = 0;

    public static final int kAtomNotInterpolated = 1;

    public static final int kAtomInterpolateIsOptional = 2;

    public static final int kParameterDataType = 1684108385;

    public static final int kParameterTypeDataText = 1952807028;

    public static final int kParameterTypeDataEnum = 1701737837;

    public static final int kParameterTypeDataBitField = 1651470188;

    public static final int kParameterTypeDataImage = 1768776039;

    public static final int kParameterAlternateDataType = 1634497585;

    public static final int kParameterTypeDataColorValue = 1668115570;

    public static final int kParameterTypeDataCubic = 1668637289;

    public static final int kParameterTypeDataNURB = 1853190754;

    public static final int kParameterDataRange = 1918987879;

    public static final int kNoMinimumLongFixed = Integer.MAX_VALUE;

    public static final int kNoMaximumLongFixed = Integer.MIN_VALUE;

    public static final int kNoScaleLongFixed = 0;

    public static final int kNoPrecision = -1;

    public static final int kParameterImageNoFlags = 0;

    public static final int kParameterDataBehavior = 1684632684;

    public static final int kParameterItemEditText = 1701079412;

    public static final int kParameterItemEditLong = 1819242087;

    public static final int kParameterItemEditFixed = 1718188132;

    public static final int kParameterItemEditDouble = 1685026146;

    public static final int kParameterItemPopUp = 1886351477;

    public static final int kParameterItemRadioCluster = 1918985321;

    public static final int kParameterItemCheckBox = 1667786104;

    public static final int kParameterItemControl = 1668183148;

    public static final int kParameterItemLine = 1818848869;

    public static final int kParameterItemColorPicker = 1885954923;

    public static final int kParameterItemGroupDivider = 1684633193;

    public static final int kParameterItemStaticText = 1937006964;

    public static final int kParameterItemDragImage = 1768776039;

    public static final int kGraphicsNoFlags = 0;

    public static final int kGraphicsFlagsGray = 1;

    public static final int kGroupNoFlags = 0;

    public static final int kGroupAlignText = 65536;

    public static final int kGroupSurroundBox = 131072;

    public static final int kGroupMatrix = 262144;

    public static final int kGroupNoName = 524288;

    public static final int kDisableControl = 1;

    public static final int kDisableWhenNotEqual = 1;

    public static final int kDisableWhenEqual = 17;

    public static final int kDisableWhenLessThan = 33;

    public static final int kDisableWhenGreaterThan = 49;

    public static final int kPopupStoreAsString = 65536;

    public static final int kParameterDataUsage = 1970496800;

    public static final int kParameterUsagePixels = 1885960300;

    public static final int kParameterUsageRectangle = 1919247220;

    public static final int kParameterUsagePoint = 2021204000;

    public static final int kParameterUsage3DPoint = 2021227040;

    public static final int kParameterUsageDegrees = 1684367218;

    public static final int kParameterUsageRadians = 1918985331;

    public static final int kParameterUsagePercent = 1885564532;

    public static final int kParameterUsageSeconds = 1936024435;

    public static final int kParameterUsageMilliseconds = 1836279139;

    public static final int kParameterUsageMicroseconds = -1250728605;

    public static final int kParameterUsage3by3Matrix = 862091571;

    public static final int kParameterDataDefaultItem = 1684434036;

    public static final int kParameterInfoLongName = -1452383891;

    public static final int kParameterInfoCopyright = -1453100935;

    public static final int kParameterInfoDescription = -1452708250;

    public static final int kParameterInfoWindowTitle = -1451790732;

    public static final int kParameterInfoPicture = -1452250760;

    public static final int kParameterInfoManufacturer = -1452449426;

    public static final int kParameterInfoIDs = 1;

    public static final int kParameterValidationNoFlags = 0;

    public static final int kParameterValidationFinalValidation = 1;

    public static final int kImageCodecSettingsFieldCount = 1718183276;

    public static final int kImageCodecSettingsFieldOrdering = 1717858157;

    public static final int kImageCodecSettingsFieldOrderingF1F2 = 1;

    public static final int kImageCodecSettingsFieldOrderingF2F1 = 2;

    public static final int kMotionJPEGTag = 1835692135;

    public static final int kJPEGQuantizationTablesImageDescriptionExtension = 1835692404;

    public static final int kJPEGHuffmanTablesImageDescriptionExtension = 1835690100;

    public static final int kFieldInfoImageDescriptionExtension = 1718183276;

    public static final int kFieldOrderUnknown = 0;

    public static final int kFieldsStoredF1F2DisplayedF1F2 = 1;

    public static final int kFieldsStoredF1F2DisplayedF2F1 = 2;

    public static final int kFieldsStoredF2F1DisplayedF1F2 = 5;

    public static final int kFieldsStoredF2F1DisplayedF2F1 = 6;

    public static final int kEffectSourceName = 1936876320;

    public static final int kEffectDataSourceType = 1685353332;

    public static final int kEffectRawSource = 0;

    public static final int kEffectGenericType = 1734698598;

    public static final int kLinearGradient = 0;

    public static final int kCircularGradient = 1;

    public static final int kCurveGetLengthSelect = 256;

    public static final int kCurveLengthToPointSelect = 257;

    public static final int kCurveNewPathSelect = 258;

    public static final int kCurveCountPointsInPathSelect = 259;

    public static final int kCurveGetPathPointSelect = 260;

    public static final int kCurveInsertPointIntoPathSelect = 261;

    public static final int kCurveSetPathPointSelect = 262;

    public static final int kCurveGetNearestPathPointSelect = 263;

    public static final int kCurvePathPointToLengthSelect = 264;

    public static final int kCurveCreateVectorStreamSelect = 265;

    public static final int kCurveAddAtomToVectorStreamSelect = 266;

    public static final int kCurveAddPathAtomToVectorStreamSelect = 267;

    public static final int kCurveAddZeroAtomToVectorStreamSelect = 268;

    public static final int kCurveGetAtomDataFromVectorStreamSelect = 269;

    public static final int gxNoFill = 0;

    public static final int gxOpenFrameFill = 1;

    public static final int gxFrameFill = 1;

    public static final int gxClosedFrameFill = 2;

    public static final int gxHollowFill = 2;

    public static final int gxEvenOddFill = 3;

    public static final int gxSolidFill = 3;

    public static final int gxWindingFill = 4;

    public static final int gxInverseEvenOddFill = 5;

    public static final int gxInverseSolidFill = 5;

    public static final int gxInverseFill = 5;

    public static final int gxInverseWindingFill = 6;

    public static final int kDontUseValidateToFindGraphicsImporter = 1;

    public static final int identityMatrixType = 0;

    public static final int translateMatrixType = 1;

    public static final int scaleMatrixType = 2;

    public static final int scaleTranslateMatrixType = 3;

    public static final int linearMatrixType = 4;

    public static final int linearTranslateMatrixType = 5;

    public static final int perspectiveMatrixType = 6;

    public static final int graphicsImporterUsesImageDecompressor = 8388608;

    public static final int quickTimeImageFileImageDescriptionAtom = 1768190819;

    public static final int quickTimeImageFileImageDataAtom = 1768186228;

    public static final int quickTimeImageFileMetaDataAtom = 1835365473;

    public static final int kGraphicsExportGroup = 1702391919;

    public static final int kGraphicsExportFileType = 1718909296;

    public static final int kGraphicsExportMIMEType = 1835625829;

    public static final int kGraphicsExportExtension = 1702392864;

    public static final int kGraphicsExportDescription = 1684370275;

    public static final int imageTranscodererComponentType = 1768780899;

    public static final int handlerHasSpatial = 1;

    public static final int handlerCanClip = 2;

    public static final int handlerCanMatte = 4;

    public static final int handlerCanTransferMode = 8;

    public static final int handlerNeedsBuffer = 16;

    public static final int handlerNoIdle = 32;

    public static final int handlerNoScheduler = 64;

    public static final int handlerWantsTime = 128;

    public static final int handlerCGrafPortOnly = 256;

    public static final int handlerCanSend = 512;

    public static final int handlerCanHandleComplexMatrix = 1024;

    public static final int handlerWantsDestinationPixels = 2048;

    public static final int handlerCanSendImageData = 4096;

    public static final int mMustDraw = 8;

    public static final int mAtEnd = 16;

    public static final int mPreflightDraw = 32;

    public static final int mSyncDrawing = 64;

    public static final int mDidDraw = 1;

    public static final int mNeedsToDraw = 4;

    public static final int mDrawAgain = 8;

    public static final int mPartialDraw = 16;

    public static final int forceUpdateRedraw = 1;

    public static final int forceUpdateNewBuffer = 2;

    public static final int mHitTestBounds = 1;

    public static final int mHitTestImage = 2;

    public static final int mHitTestInvisible = 4;

    public static final int mHitTestIsClick = 8;

    public static final int mOpaque = 1;

    public static final int mInvisible = 2;

    public static final int kMediaVideoParamBrightness = 1;

    public static final int kMediaVideoParamContrast = 2;

    public static final int kMediaVideoParamHue = 3;

    public static final int kMediaVideoParamSharpness = 4;

    public static final int kMediaVideoParamSaturation = 5;

    public static final int kMediaVideoParamBlackLevel = 6;

    public static final int kMediaVideoParamWhiteLevel = 7;

    public static final int standardCompressionType = 1935893609;

    public static final int standardCompressionSubType = 1768776039;

    public static final int standardCompressionSubTypeSound = 1936684398;

    public static final int scListEveryCodec = 2;

    public static final int scAllowZeroFrameRate = 4;

    public static final int scAllowZeroKeyFrameRate = 8;

    public static final int scShowBestDepth = 16;

    public static final int scUseMovableModal = 32;

    public static final int scDisableFrameRateItem = 64;

    public static final int scPreferCropping = 1;

    public static final int scPreferScaling = 2;

    public static final int scPreferScalingAndCropping = 3;

    public static final int scDontDetermineSettingsFromTestImage = 4;

    public static final int scTestImageWidth = 80;

    public static final int scTestImageHeight = 80;

    public static final int scOKItem = 1;

    public static final int scCancelItem = 2;

    public static final int scCustomItem = 3;

    public static final int scUserCancelled = 1;

    public static final int scSpatialSettingsType = 1936749676;

    public static final int scTemporalSettingsType = 1953526380;

    public static final int scDataRateSettingsType = 1685217652;

    public static final int scColorTableType = 1668052340;

    public static final int scProgressProcType = 1886547815;

    public static final int scExtendedProcsType = 2020635235;

    public static final int scPreferenceFlagsType = 1886545254;

    public static final int scSettingsStateType = 1936946273;

    public static final int scSequenceIDType = 1936028021;

    public static final int scWindowPositionType = 2003723383;

    public static final int scCodecFlagsType = 1667656807;

    public static final int scCodecSettingsType = 1667523939;

    public static final int scForceKeyValueType = 1802725741;

    public static final int scSoundSampleRateType = 1936945780;

    public static final int scSoundSampleSizeType = 1936946035;

    public static final int scSoundChannelCountType = 1936941923;

    public static final int scSoundCompressionType = 1936941940;

    public static final int scCompressionListType = 1668577644;

    public static final int scCodecManufacturerType = 1668114034;

    public static final int scGetCompression = 1;

    public static final int scShowMotionSettings = 1;

    public static final int scSettingsChangedItem = -1;

    public static final int scCompressFlagIgnoreIdenticalFrames = 1;

    public static final int kQTSettingsVideo = 1986618469;

    public static final int kQTSettingsSound = 1936684398;

    public static final int tcSourceRefNameType = 1851878757;

    public static final int tctNegFlag = 128;

    public static final int movieExportWidth = 2003072104;

    public static final int movieExportHeight = 1751476084;

    public static final int movieExportDuration = 1685418593;

    public static final int movieExportVideoFilter = 1768320116;

    public static final int pnotComponentWantsEvents = 1;

    public static final int pnotComponentNeedsNoCache = 2;

    public static final int showFilePreviewComponentType = 1886285684;

    public static final int createFilePreviewComponentType = 1886216555;

    public static final int dataCompressorComponentType = 1684238189;

    public static final int dataDecompressorComponentType = 1684301155;

    public static final int appleDataCompressorSubType = 1633969507;

    public static final int zlibDataCompressorSubType = 2053925218;

    public static final int kDataHExtendedSchedule = 2020896356;

    public static final int grabPictOffScreen = 1;

    public static final int grabPictIgnoreClip = 2;

    public static final int grabPictCurrentImage = 4;

    public static final int sgFlagControlledGrab = 1;

    public static final int sgDeviceNameFlagDeviceUnavailable = 1;

    public static final int sgPanelFlagForPanel = 1;

    public static final int seqGrabSettingsPreviewOnly = 1;

    public static final int qtVideoOutputComponentType = 1987016052;

    public static final int qtVideoOutputComponentBaseSubType = 1650553701;

    public static final int kQTVideoOutputDontDisplayToUser = 1;

    public static final int kBackgroundSpriteLayerNum = 32767;

    public static final int kSpritePropertyMatrix = 1;

    public static final int kSpritePropertyImageDescription = 2;

    public static final int kSpritePropertyImageDataPtr = 3;

    public static final int kSpritePropertyVisible = 4;

    public static final int kSpritePropertyLayer = 5;

    public static final int kSpritePropertyGraphicsMode = 6;

    public static final int kSpritePropertyImageDataSize = 7;

    public static final int kOnlyDrawToSpriteWorld = 1;

    public static final int kSpriteWorldPreflight = 2;

    public static final int kSpriteWorldDidDraw = 1;

    public static final int kSpriteWorldNeedsToDraw = 2;

    public static final int kScaleSpritesToScaleWorld = 2;

    public static final int kSpriteWorldHighQuality = 4;

    public static final int kSpriteWorldDontAutoInvalidate = 8;

    public static final int kSpriteWorldInvisible = 16;

    public static final int spriteHitTestBounds = 1;

    public static final int spriteHitTestImage = 2;

    public static final int spriteHitTestInvisibleSprites = 4;

    public static final int spriteHitTestIsClick = 8;

    public static final int spriteHitTestLocInDisplayCoordinates = 16;

    public static final int qtcbNeedsRateChanges = 1;

    public static final int qtcbNeedsTimeChanges = 2;

    public static final int qtcbNeedsStartStopChanges = 4;

    public static final int clockComponentType = 1668050795;

    public static final int systemTickClock = 1953063787;

    public static final int systemSecondClock = 1936024431;

    public static final int systemMillisecondClock = 1835625580;

    public static final int systemMicrosecondClock = 1835623282;

    public static final int kClockRateIsLinear = 1;

    public static final int kClockImplementsCallBacks = 2;

    public static final int kClockCanHandleIntermittentSound = 4;

    public static final int loopTimeBase = 1;

    public static final int palindromeLoopTimeBase = 2;

    public static final int maintainTimeBaseZero = 4;

    public static final int triggerAtStart = 1;

    public static final int triggerAtStop = 2;

    public static final int triggerRateLT = 4;

    public static final int triggerRateGT = 8;

    public static final int triggerRateEqual = 16;

    public static final int triggerRateLTE = 20;

    public static final int triggerRateGTE = 24;

    public static final int triggerRateNotEqual = 28;

    public static final int triggerRateChange = 0;

    public static final int timeBaseBeforeStartTime = 1;

    public static final int timeBaseAfterStopTime = 2;

    public static final int triggerTimeFwd = 1;

    public static final int triggerTimeBwd = 2;

    public static final int triggerTimeEither = 3;

    public static final int kAnyType = 0;

    public static final int kAnySubType = 0;

    public static final int kSorensonCodecType = 1398165809;

    public static final int kRawCodecType = 1918990112;

    public static final int kCinepakCodecType = 1668704612;

    public static final int kGraphicsCodecType = 1936548640;

    public static final int kAnimationCodecType = 1919706400;

    public static final int kVideoCodecType = 1919973985;

    public static final int kComponentVideoCodecType = 2037741106;

    public static final int kJPEGCodecType = 1785750887;

    public static final int kMotionJPEGACodecType = 1835692129;

    public static final int kMotionJPEGBCodecType = 1835692130;

    public static final int kSGICodecType = 777209673;

    public static final int kPlanarRGBCodecType = 943870035;

    public static final int kMacPaintCodecType = 1346589767;

    public static final int kGIFCodecType = 1734960672;

    public static final int kPhotoCDCodecType = 1802527588;

    public static final int kQuickDrawGXCodecType = 1902405496;

    public static final int kAVRJPEGCodecType = 1635152416;

    public static final int kOpenDMLJPEGCodecType = 1684890161;

    public static final int kBMPCodecType = 1465011269;

    public static final int kWindowsRawCodecType = 1465008471;

    public static final int kVectorCodecType = 1885434984;

    public static final int kQuickDrawCodecType = 1902408311;

    public static final int kWaterRippleCodecType = 1919512684;

    public static final int kFireCodecType = 1718186597;

    public static final int kCloudCodecType = 1668050805;

    public static final int kH261CodecType = 1748121137;

    public static final int kH263CodecType = 1748121139;

    public static final int kDVCNTSCCodecType = 1685480224;

    public static final int kDVCPALCodecType = 1685480304;

    public static final int kDVCProNTSCCodecType = 1685483630;

    public static final int kDVCProPALCodecType = 1685483632;

    public static final int kBaseCodecType = 1650553701;

    public static final int kFLCCodecType = 1718380899;

    public static final int kTargaCodecType = 1952932128;

    public static final int kComponentVideoSigned = 2037741173;

    public static final int kComponentVideoUnsigned = 2037741171;

    public static final int kCMYKCodecType = 1668118891;

    public static final int kMicrosoftVideo1CodecType = 1836283491;

    public static final int kBlurImageFilterType = 1651275122;

    public static final int kSharpenImageFilterType = 1936224880;

    public static final int kEdgeDetectImageFilterType = 1701078885;

    public static final int kEmbossImageFilterType = 1701667443;

    public static final int kConvolveImageFilterType = 1734700651;

    public static final int kAlphaGainImageFilterType = 1734437230;

    public static final int kRGBColorBalanceImageFilterType = 1919378018;

    public static final int kHSLColorBalanceImageFilterType = 1752394850;

    public static final int kColorSyncImageFilterType = 1937337955;

    public static final int kFilmNoiseImageFilterType = 1718447731;

    public static final int kSolarizeImageFilterType = 1936682098;

    public static final int kColorTintImageFilterType = 1953066612;

    public static final int kAlphaCompositorTransitionType = 1651273316;

    public static final int kCrossFadeTransitionType = 1685286006;

    public static final int kChromaKeyTransitionType = 1667982713;

    public static final int kImplodeTransitionType = 1836084335;

    public static final int kExplodeTransitionType = 2020633711;

    public static final int kGradientTransitionType = 1835103348;

    public static final int kPushTransitionType = 1886745448;

    public static final int kSlideTransitionType = 1936484708;

    public static final int kWipeTransitionType = 1936552052;

    public static final int kIrisTransitionType = 1936551986;

    public static final int kRadialTransitionType = 1936551987;

    public static final int kMatrixTransitionType = 1936551988;

    public static final int kRandomWipeTransitionType = 409;

    public static final int kCurvePathAtom = 1885434984;

    public static final int kCurveEndAtom = 2053468783;

    public static final int kCurveAntialiasControlAtom = 1634628713;

    public static final int kCurveAntialiasOff = 0;

    public static final int kCurveAntialiasOn = -1;

    public static final int kCurveFillTypeAtom = 1718185068;

    public static final int kCurvePenThicknessAtom = 1885695604;

    public static final int kCurveMiterLimitAtom = 1835627634;

    public static final int kCurveJoinAttributesAtom = 1785686382;

    public static final int kCurveMinimumDepthAtom = 1835626084;

    public static final int kCurveDepthAlwaysOffscreenMask = Integer.MIN_VALUE;

    public static final int kCurveGradientAngleAtom = 1634625388;

    public static final int kCurveGradientRadiusAtom = 1918985321;

    public static final int kCurveGradientOffsetAtom = 1667591796;

    public static final int kCurveARGBColorAtom = 1634887522;

    public static final int kCurveGradientTypeAtom = 1735550068;

    public static final int kCurveGradientRecordAtom = 1735549284;

    public static final int codecInfoDoes1 = 1;

    public static final int codecInfoDoes2 = 2;

    public static final int codecInfoDoes4 = 4;

    public static final int codecInfoDoes8 = 8;

    public static final int codecInfoDoes16 = 16;

    public static final int codecInfoDoes32 = 32;

    public static final int codecInfoDoesDither = 64;

    public static final int codecInfoDoesStretch = 128;

    public static final int codecInfoDoesShrink = 256;

    public static final int codecInfoDoesMask = 512;

    public static final int codecInfoDoesTemporal = 1024;

    public static final int codecInfoDoesDouble = 2048;

    public static final int codecInfoDoesQuad = 4096;

    public static final int codecInfoDoesHalf = 8192;

    public static final int codecInfoDoesQuarter = 16384;

    public static final int codecInfoDoesRotate = 32768;

    public static final int codecInfoDoesHorizFlip = 65536;

    public static final int codecInfoDoesVertFlip = 131072;

    public static final int codecInfoHasEffectParameterList = 262144;

    public static final int codecInfoDoesBlend = 524288;

    public static final int codecInfoDoesWarp = 1048576;

    public static final int codecInfoDoesRecompress = 2097152;

    public static final int codecInfoDoesSpool = 4194304;

    public static final int codecInfoDoesRateConstrain = 8388608;

    public static final int codecInfoDepth1 = 1;

    public static final int codecInfoDepth2 = 2;

    public static final int codecInfoDepth4 = 4;

    public static final int codecInfoDepth8 = 8;

    public static final int codecInfoDepth16 = 16;

    public static final int codecInfoDepth32 = 32;

    public static final int codecInfoDepth24 = 64;

    public static final int codecInfoDepth33 = 128;

    public static final int codecInfoDepth34 = 256;

    public static final int codecInfoDepth36 = 512;

    public static final int codecInfoDepth40 = 1024;

    public static final int codecInfoStoresClut = 2048;

    public static final int codecInfoDoesLossless = 4096;

    public static final int codecInfoSequenceSensitive = 8192;

    public static final int compressorComponentType = 1768776559;

    public static final int decompressorComponentType = 1768776803;

    public static final int defaultDither = 0;

    public static final int forceDither = 1;

    public static final int suppressDither = 2;

    public static final int useColorMatching = 4;

    public static final int callStdBits = 1;

    public static final int callOldBits = 2;

    public static final int noDefaultOpcodes = 4;

    public static final int graphicsModeStraightAlpha = 256;

    public static final int graphicsModePreWhiteAlpha = 257;

    public static final int graphicsModePreBlackAlpha = 258;

    public static final int graphicsModeComposition = 259;

    public static final int graphicsModeStraightAlphaBlend = 260;

    public static final int graphicsModePreMulColorAlpha = 261;

    public static final int evenField1ToEvenFieldOut = 1;

    public static final int evenField1ToOddFieldOut = 2;

    public static final int oddField1ToEvenFieldOut = 4;

    public static final int oddField1ToOddFieldOut = 8;

    public static final int evenField2ToEvenFieldOut = 16;

    public static final int evenField2ToOddFieldOut = 32;

    public static final int oddField2ToEvenFieldOut = 64;

    public static final int oddField2ToOddFieldOut = 128;

    public static final int icmFrameTimeHasVirtualStartTimeAndDuration = 1;

    public static final int elOptionsIncludeNoneInList = 1;

    public static final int pdOptionsCollectOneValue = 1;

    public static final int pdOptionsAllowOptionalInterpolations = 2;

    public static final int pdOptionsModalDialogBox = 4;

    public static final int effectIsRealtime = 0;

    public static final int kEffectPercentage = 1885564532;

    public static final int kEffectWipe = 2003847492;

    public static final int kEffectSoftBorder = 1936680564;

    public static final int kEffectBorderWidth = 2003395700;

    public static final int kEffectBorderColor = 1650682994;

    public static final int kEffectHorizontalRepeat = 1752134249;

    public static final int kEffectVerticalRepeat = 1986359924;

    public static final int kEffectBlendMode = 1649241956;

    public static final int kEffectPreMultiplyColor = 1835232370;

    public static final int kEffectStraightAlpha = 1;

    public static final int kEffectPreMultiplyAlpha = 2;

    public static final int kEffectReverseAlpha = 3;

    public static final int kEffectGainValue = 1734437230;

    public static final int kEffectOffsetValue = 1868981875;

    public static final int kEffectTopAlphaPin = 1885957716;

    public static final int kEffectBottomAlphaPin = 1885957698;

    public static final int kEffectFromDirection = 1718775661;

    public static final int kEffectDirectionTop = 0;

    public static final int kEffectDirectionLeft = 1;

    public static final int kEffectDirectionBottom = 2;

    public static final int kEffectDirectionRight = 3;

    public static final int kEffectSlideAngle = 1634625388;

    public static final int kEffectWhatAtom = 2003329396;

    public static final int kEffectSrcAtom = 1936876320;

    public static final int kEffectNameAtom = 1851878757;

    public static final int kEffectTypeAtom = 1954115685;

    public static final int kEffectManufacturerAtom = 1835101813;

    public static final int pdActionConfirmDialog = 1;

    public static final int pdActionSetAppleMenu = 2;

    public static final int pdActionSetEditMenu = 3;

    public static final int pdActionGetDialogValues = 4;

    public static final int pdActionSetPreviewUserItem = 5;

    public static final int pdActionSetPreviewPicture = 6;

    public static final int pdActionSetColorPickerEventProc = 7;

    public static final int pdActionSetDialogTitle = 8;

    public static final int pdActionGetSubPanelMenu = 9;

    public static final int pdActionActivateSubPanel = 10;

    public static final int pdActionConductStopAlert = 11;

    public static final int pdActionModelessCallback = 12;

    public static final int kVendorApple = 1634758764;

    public static final int graphicsExporterComponentType = 1735550328;

    public static final int kBaseGraphicsExporterSubType = 1650553701;

    public static final int graphicsExporterIsBaseExporter = 1;

    public static final int graphicsExporterCanTranscode = 2;

    public static final int graphicsExporterUsesImageCompressor = 4;

    public static final int kQTResolutionSettings = 1919251311;

    public static final int kQTTargetDataSize = 1684108154;

    public static final int kQTDontRecompress = 1684960370;

    public static final int kQTInterlaceStyle = 6909027;

    public static final int kQTColorSyncProfile = 1768121197;

    public static final int kQTTIFFCompressionMethod = 1953064547;

    public static final int kQTTIFFCompression_None = 1;

    public static final int kQTTIFFLittleEndian = 1953064549;

    public static final int kQTPNGFilterPreference = 1835952486;

    public static final int kQTPNGFilterBestForColorType = 1650879604;

    public static final int kQTPNGFilterNone = 0;

    public static final int kQTPNGFilterSub = 1;

    public static final int kQTPNGFilterUp = 2;

    public static final int kQTPNGFilterAverage = 3;

    public static final int kQTPNGFilterPaeth = 4;

    public static final int kQTPNGFilterAdaptivePerRow = 1634102388;

    public static final int kQTPNGInterlaceStyle = 6909027;

    public static final int kQTPNGInterlaceNone = 0;

    public static final int kQTPNGInterlaceAdam7 = 1;

    public static final int graphicsImporterComponentType = 1735551344;

    public static final int graphicsImporterDrawsAllPixels = 0;

    public static final int graphicsImporterDoesntDrawAllPixels = 1;

    public static final int graphicsImporterDontKnowIfDrawAllPixels = 2;

    public static final int kAccessKeyAtomType = 1633905529;

    public static final int kAccessKeySystemFlag = 1;

    public static final int kTrackReferenceChapterList = 1667785072;

    public static final int kTrackReferenceTimeCode = 1953325924;

    public static final int kTrackReferenceModifier = 1936945763;

    public static final int codecLosslessQuality = 1024;

    public static final int codecMaxQuality = 1023;

    public static final int codecMinQuality = 0;

    public static final int codecLowQuality = 256;

    public static final int codecNormalQuality = 512;

    public static final int codecHighQuality = 768;

    public static final int codecLockBitsShieldCursor = 1;

    public static final int codecCompletionSource = 1;

    public static final int codecCompletionDest = 2;

    public static final int codecCompletionDontUnshield = 4;

    public static final int codecCompletionWentOffscreen = 8;

    public static final int codecCompletionUnlockBits = 16;

    public static final int codecCompletionForceChainFlush = 32;

    public static final int codecCompletionDropped = 64;

    public static final int codecProgressOpen = 0;

    public static final int codecProgressUpdatePercent = 1;

    public static final int codecProgressClose = 2;

    public static final int codecFlagUseImageBuffer = 1;

    public static final int codecFlagUseScreenBuffer = 2;

    public static final int codecFlagUpdatePrevious = 4;

    public static final int codecFlagNoScreenUpdate = 8;

    public static final int codecFlagWasCompressed = 16;

    public static final int codecFlagDontOffscreen = 32;

    public static final int codecFlagUpdatePreviousComp = 64;

    public static final int codecFlagForceKeyFrame = 128;

    public static final int codecFlagOnlyScreenUpdate = 256;

    public static final int codecFlagLiveGrab = 512;

    public static final int codecFlagDontUseNewImageBuffer = 1024;

    public static final int codecFlagInterlaceUpdate = 2048;

    public static final int codecFlagCatchUpDiff = 4096;

    public static final int codecFlagSupportDisable = 8192;

    public static final int codecFlagReenable = 16384;

    public static final int codecFlagOutUpdateOnDataSourceChange = 1024;

    public static final int codecFlagSequenceSensitive = 2048;

    public static final int codecFlagOutUpdateOnTimeChange = 4096;

    public static final int codecFlagImageBufferNotSourceImage = 8192;

    public static final int codecFlagUsedNewImageBuffer = 16384;

    public static final int codecFlagUsedImageBuffer = 32768;

    public static final int codecMinimumDataSize = 32768;

    public static final int kDeleteCurFile = Integer.MIN_VALUE;

    public static final int defaultProgressFuntion = -1;

    public static final int kMimeInfoMimeTypeTag = 1835625829;

    public static final int kMimeInfoFileExtensionTag = 1702392864;

    public static final int kMimeInfoFileTypeTag = 1718909296;

    public static final int kMimeInfoDescriptionTag = 1684370275;

    public static final int kMimeInfoGroupTag = 1735552880;

    public static final int kMimeInfoDoNotOverrideExistingFileTypeAssociation = 1852794465;

    public static final int kDataRefMimeTypeTag = 1835625829;

    public static final int kDataRefFileExtensionTag = 1702392864;

    public static final int kDataRefQTFileTypeTag = 1718909296;

    public static final int kDataRefExtensionChokeSpeed = 1667788651;

    public static final int kDataRefExtensionMIMEType = 1835625829;

    public static final int kDataRefExtensionMacOSFileType = 1718909296;

    public static final int kDataRefExtensionInitializationData = 1684108385;

    public static final int kQTFileTypeAIFF = 1095321158;

    public static final int kQTFileTypeAIFC = 1095321155;

    public static final int kQTFileTypeDVC = 1685480225;

    public static final int kQTFileTypeMIDI = 1298752617;

    public static final int kQTFileTypePicture = 1346978644;

    public static final int kQTFileTypeMovie = 1299148630;

    public static final int kQTFileTypeText = 1413830740;

    public static final int kQTFileTypeWave = 1463899717;

    public static final int kQTFileTypeSystemSevenSound = 1936091500;

    public static final int kQTFileTypeMuLaw = 1431060823;

    public static final int kQTFileTypeAVI = 1449547552;

    public static final int kQTFileTypeSoundDesignerII = 1399075430;

    public static final int kQTFileTypeAudioCDTrack = 1953653099;

    public static final int kQTFileTypePICS = 1346978643;

    public static final int kQTFileTypeGIF = 1195984486;

    public static final int kQTFileTypePhotoShop = 943870035;

    public static final int kQTFileTypeSGIImage = 777209673;

    public static final int kQTFileTypeBMP = 1112363110;

    public static final int kQTFileTypeJPEG = 1246774599;

    public static final int kQTFileTypeJFIF = 1246774599;

    public static final int kQTFileTypeMacPaint = 1347310663;

    public static final int kQTFileTypeTargaImage = 1414547779;

    public static final int kQTFileTypeQuickDrawGXPicture = 1902405496;

    public static final int kQTFileTypeQuickTimeImage = 1903454566;

    public static final int kQTFileType3DMF = 860114246;

    public static final int kMoviePlayer = 1414942532;

    public static final int kMShp = 1297311856;

    public static final int kVis3 = 1449751347;

    public static final int kPhotoshop = 943868237;

    public static final int kClarisWorks = 1112490575;

    public static final int kTeachText = 1953785204;

    public static final int kPictureCompressor = 1886419049;

    public static final int kGKON = 1196117838;

    public static final int createMovieFileDeleteCurFile = Integer.MIN_VALUE;

    public static final int createMovieFileDontCreateMovie = 1073741824;

    public static final int createMovieFileDontOpenFile = 536870912;

    public static final int createMovieFileDontCreateResFile = 268435456;

    public static final int sfpItemPreviewAreaUser = 11;

    public static final int sfpItemPreviewStaticText = 12;

    public static final int sfpItemPreviewDividerUser = 13;

    public static final int sfpItemCreatePreviewButton = 14;

    public static final int sfpItemShowPreviewButton = 15;

    public static final int kMovieVersion = 0;

    public static final int kVersionCheckMin = 0;

    public static final int kVersionCheckMask = 1;

    public static final int kDataRate144ModemRate = 1400;

    public static final int kDataRate288ModemRate = 2800;

    public static final int kDataRateISDNRate = 5600;

    public static final int kDataRateDualISDNRate = 11200;

    public static final int kDataRateT1Rate = 150000;

    public static final int kDataRateInfiniteRate = Integer.MAX_VALUE;

    public static final int trackEnable = 1;

    public static final int trackInMovie = 2;

    public static final int trackInPreview = 4;

    public static final int trackInPoster = 8;

    public static final int movieFileType = 1299148630;

    public static final int movieScrapType = 1836019574;

    public static final int movieResourceType = 1836019574;

    public static final int movieForwardPointerResourceType = 1718579813;

    public static final int movieBackwardPointerResourceType = 1650549611;

    public static final int movieResourceAtomType = 1836019574;

    public static final int movieDataAtomType = 1835295092;

    public static final int movieAID = 1836019574;

    public static final int movieHeaderAID = 1836476516;

    public static final int clipAID = 1668049264;

    public static final int rgnClipAID = 1668441966;

    public static final int matteAID = 1835103348;

    public static final int matteCompAID = 1802330484;

    public static final int trackAID = 1953653099;

    public static final int userDataAID = 1969517665;

    public static final int trackHeaderAID = 1953196132;

    public static final int editsAID = 1701082227;

    public static final int editListAID = 1701606260;

    public static final int mediaAID = 1835297121;

    public static final int mediaHeaderAID = 1835296868;

    public static final int mediaInfoAID = 1835626086;

    public static final int videoMediaInfoHeaderAID = 1986881636;

    public static final int soundMediaInfoHeaderAID = 1936549988;

    public static final int genericMediaInfoHeaderAID = 1735223396;

    public static final int genericMediaInfoAID = 1735223662;

    public static final int dataInfoAID = 1684631142;

    public static final int dataRefAID = 1685218662;

    public static final int sampleTableAID = 1937007212;

    public static final int sTSampleDescAID = 1937011556;

    public static final int sTTimeToSampAID = 1937011827;

    public static final int sTSyncSampleAID = 1937011571;

    public static final int sTSampleToChunkAID = 1937011555;

    public static final int sTShadowSyncAID = 1937011560;

    public static final int handlerAID = 1751411826;

    public static final int sTSampleSizeAID = 1937011578;

    public static final int sTChunkOffsetAID = 1937007471;

    public static final int sTChunkOffset64AID = 1668232756;

    public static final int sTSampleIDAID = 1937008996;

    public static final int dataRefContainerAID = 1685218915;

    public static final int trackReferenceAID = 1953654118;

    public static final int colorTableAID = 1668571490;

    public static final int loadSettingsAID = 1819238756;

    public static final int propertyAtomAID = 1668244581;

    public static final int inputMapAID = 1768776048;

    public static final int movieBufferHintsAID = 1835165288;

    public static final int movieDataRefAliasAID = 1835299425;

    public static final int soundLocalizationAID = 1936486243;

    public static final int compressedMovieAID = 1668116342;

    public static final int compressedMovieDataAID = 1668118116;

    public static final int dataCompressionAtomAID = 1684238189;

    public static final int referenceMovieRecordAID = 1919775329;

    public static final int referenceMovieDescriptorAID = 1919771745;

    public static final int referenceMovieDataRefAID = 1919185510;

    public static final int referenceMovieVersionCheckAID = 1919776355;

    public static final int referenceMovieDataRateAID = 1919771762;

    public static final int referenceMovieComponentCheckAID = 1919771492;

    public static final int referenceMovieQualityAID = 1919775093;

    public static final int kDataRefIsSelfContained = 1;

    public static final int kParentAtomIsContainer = 0;

    public static final int kITextRemoveEverythingBut = 0;

    public static final int kITextRemoveLeaveSuggestedAlternate = 2;

    public static final int kITextAtomType = 1769240692;

    public static final int kITextStringAtomType = 1952807028;

    public static final int fullScreenHideCursor = 1;

    public static final int fullScreenAllowEvents = 2;

    public static final int fullScreenDontChangeMenuBar = 4;

    public static final int fullScreenPreflightSize = 8;

    public static final int newMovieActive = 1;

    public static final int newMovieDontResolveDataRefs = 2;

    public static final int newMovieDontAskUnresolvedDataRefs = 4;

    public static final int newMovieDontAutoAlternates = 8;

    public static final int newMovieDontUpdateForeBackPointers = 16;

    public static final int keepInRam = 1;

    public static final int unkeepInRam = 2;

    public static final int flushFromRam = 4;

    public static final int loadForwardTrackEdits = 8;

    public static final int loadBackwardTrackEdits = 16;

    public static final int visualMediaCharacteristic = 1702454643;

    public static final int audioMediaCharacteristic = 1700885107;

    public static final int kCharacteristicCanSendVideo = 1987276388;

    public static final int kCharacteristicProvidesURLs = 1970433132;

    public static final int kCharacteristicNonLinear = 1852796524;

    public static final int kCharacteristicProvidesActions = 1633907822;

    public static final int kUserDataMovieControllerType = 1668577648;

    public static final int kUserDataName = 1851878757;

    public static final int kUserDataTextFullName = -1452383891;

    public static final int kUserDataTextCopyright = -1453100935;

    public static final int kUserDataTextInformation = -1452708250;

    public static final int searchTextDontGoToFoundTime = 65536;

    public static final int searchTextDontHiliteFoundText = 131072;

    public static final int searchTextOneTrackOnly = 262144;

    public static final int searchTextEnabledTracksOnly = 524288;

    public static final int pasteInParallel = 1;

    public static final int showUserSettingsDialog = 2;

    public static final int movieToFileOnlyExport = 4;

    public static final int movieFileSpecValid = 8;

    public static final int movieInDataForkResID = -1;

    public static final int flattenAddMovieToDataFork = 1;

    public static final int flattenActiveTracksOnly = 4;

    public static final int flattenDontInterleaveFlatten = 8;

    public static final int flattenFSSpecPtrIsDataRefRecordPtr = 16;

    public static final int flattenCompressMovieResource = 32;

    public static final int flattenForceMovieResourceBeforeMovieData = 64;

    public static final int nextTimeMediaSample = 1;

    public static final int nextTimeMediaEdit = 2;

    public static final int nextTimeTrackEdit = 4;

    public static final int nextTimeSyncSample = 8;

    public static final int nextTimeStep = 16;

    public static final int nextTimeEdgeOK = 16384;

    public static final int nextTimeIgnoreActiveSegment = 32768;

    public static final int movieScrapDontZeroScrap = 1;

    public static final int movieScrapOnlyPutMovie = 2;

    public static final int hintsScrubMode = 1;

    public static final int hintsLoop = 2;

    public static final int hintsDontPurge = 4;

    public static final int hintsUseScreenBuffer = 32;

    public static final int hintsAllowInterlace = 64;

    public static final int hintsUseSoundInterp = 128;

    public static final int hintsHighQuality = 256;

    public static final int hintsPalindrome = 512;

    public static final int hintsInactive = 2048;

    public static final int hintsOffscreen = 4096;

    public static final int hintsDontDraw = 8192;

    public static final int hintsAllowBlacklining = 16384;

    public static final int hintsDontUseVideoOverlaySurface = 65536;

    public static final int hintsIgnoreBandwidthRestrictions = 131072;

    public static final int hintsPlayingEveryFrame = 262144;

    public static final int hintsAllowDynamicResize = 524288;

    public static final int movieDrawingCallWhenChanged = 0;

    public static final int movieDrawingCallAlways = 1;

    public static final int movieTrackMediaType = 1;

    public static final int movieTrackCharacteristic = 2;

    public static final int movieTrackEnabledOnly = 4;

    public static final int movieControllerComponentType = 1886151033;

    public static final int mcTopLeftMovie = 1;

    public static final int mcScaleMovieToFit = 2;

    public static final int mcWithBadge = 4;

    public static final int mcNotVisible = 8;

    public static final int mcWithFrame = 16;

    public static final int mcFlagSuppressMovieFrame = 1;

    public static final int mcFlagSuppressStepButtons = 2;

    public static final int mcFlagSuppressSpeakerButton = 4;

    public static final int mcFlagsUseWindowPalette = 8;

    public static final int mcFlagsDontInvalidate = 16;

    public static final int mcFlagsUseCustomButton = 32;

    public static final int mcPositionDontInvalidate = 32;

    public static final int kMCIEEnabledButtonPicture = 1;

    public static final int kMCIEDisabledButtonPicture = 2;

    public static final int kMCIEDepressedButtonPicture = 3;

    public static final int kMCIEEnabledSizeBoxPicture = 4;

    public static final int kMCIEDisabledSizeBoxPicture = 5;

    public static final int kMCIEEnabledUnavailableButtonPicture = 6;

    public static final int kMCIEDisabledUnavailableButtonPicture = 7;

    public static final int kMCIESoundSlider = 128;

    public static final int kMCIESoundThumb = 129;

    public static final int kMCIEColorTable = 256;

    public static final int kMCIEIsFlatAppearance = 257;

    public static final int kMCIEDoButtonIconsDropOnDepress = 258;

    public static final int mcInfoUndoAvailable = 1;

    public static final int mcInfoCutAvailable = 2;

    public static final int mcInfoCopyAvailable = 4;

    public static final int mcInfoPasteAvailable = 8;

    public static final int mcInfoClearAvailable = 16;

    public static final int mcInfoHasSound = 32;

    public static final int mcInfoIsPlaying = 64;

    public static final int mcInfoIsLooping = 128;

    public static final int mcInfoIsInPalindrome = 256;

    public static final int mcInfoEditingEnabled = 512;

    public static final int mcInfoMovieIsInteractive = 1024;

    public static final int mcMenuUndo = 1;

    public static final int mcMenuCut = 3;

    public static final int mcMenuCopy = 4;

    public static final int mcMenuPaste = 5;

    public static final int mcMenuClear = 6;

    public static final int kControllerHeight = 16;

    public static final short mcActionIdle = 1;

    public static final short mcActionDraw = 2;

    public static final short mcActionActivate = 3;

    public static final short mcActionDeactivate = 4;

    public static final short mcActionMouseDown = 5;

    public static final short mcActionKey = 6;

    public static final short mcActionPlay = 8;

    public static final short mcActionGoToTime = 12;

    public static final short mcActionSetVolume = 14;

    public static final short mcActionGetVolume = 15;

    public static final short mcActionStep = 18;

    public static final short mcActionSetLooping = 21;

    public static final short mcActionGetLooping = 22;

    public static final short mcActionSetLoopIsPalindrome = 23;

    public static final short mcActionGetLoopIsPalindrome = 24;

    public static final short mcActionSetGrowBoxBounds = 25;

    public static final short mcActionControllerSizeChanged = 26;

    public static final short mcActionSetSelectionBegin = 29;

    public static final short mcActionSetSelectionDuration = 30;

    public static final short mcActionSetKeysEnabled = 32;

    public static final short mcActionGetKeysEnabled = 33;

    public static final short mcActionSetPlaySelection = 34;

    public static final short mcActionGetPlaySelection = 35;

    public static final short mcActionSetUseBadge = 36;

    public static final short mcActionGetUseBadge = 37;

    public static final short mcActionSetFlags = 38;

    public static final short mcActionGetFlags = 39;

    public static final short mcActionSetPlayEveryFrame = 40;

    public static final short mcActionGetPlayEveryFrame = 41;

    public static final short mcActionGetPlayRate = 42;

    public static final short mcActionShowBalloon = 43;

    public static final short mcActionBadgeClick = 44;

    public static final short mcActionMovieClick = 45;

    public static final short mcActionSuspend = 46;

    public static final short mcActionResume = 47;

    public static final short mcActionSetControllerKeysEnabled = 48;

    public static final short mcActionGetTimeSliderRect = 49;

    public static final short mcActionMovieEdited = 50;

    public static final short mcActionGetDragEnabled = 51;

    public static final short mcActionSetDragEnabled = 52;

    public static final short mcActionGetSelectionBegin = 53;

    public static final short mcActionGetSelectionDuration = 54;

    public static final short mcActionPrerollAndPlay = 55;

    public static final short mcActionGetCursorSettingEnabled = 56;

    public static final short mcActionSetCursorSettingEnabled = 57;

    public static final short mcActionSetColorTable = 58;

    public static final short mcActionLinkToURL = 59;

    public static final short mcActionCustomButtonClick = 60;

    public static final short mcActionForceTimeTableUpdate = 61;

    public static final short mcActionSetControllerTimeLimits = 62;

    public static final short mcActionExecuteAllActionsForQTEvent = 63;

    public static final short mcActionExecuteOneActionForQTEvent = 64;

    public static final short mcActionAdjustCursor = 65;

    public static final short mcActionUseTrackForTimeTable = 66;

    public static final short mcActionClickAndHoldPoint = 67;

    public static final short mcActionShowMessageString = 68;

    public static final short mcActionShowStatusString = 69;

    public static final short mcActionGetExternalMovie = 70;

    public static final short mcActionGetChapterTime = 71;

    public static final short mcActionPerformActionList = 72;

    public static final short mcActionEvaluateExpression = 73;

    public static final short mcActionFetchParameterAs = 74;

    public static final short mcActionGetCursorByID = 75;

    public static final short mcActionGetNextURL = 76;

    public static final int kActionMovieSetVolume = 1024;

    public static final int kActionMovieSetRate = 1025;

    public static final int kActionMovieSetLoopingFlags = 1026;

    public static final int kActionMovieGoToTime = 1027;

    public static final int kActionMovieGoToTimeByName = 1028;

    public static final int kActionMovieGoToBeginning = 1029;

    public static final int kActionMovieGoToEnd = 1030;

    public static final int kActionMovieStepForward = 1031;

    public static final int kActionMovieStepBackward = 1032;

    public static final int kActionMovieSetSelection = 1033;

    public static final int kActionMovieSetSelectionByName = 1034;

    public static final int kActionMoviePlaySelection = 1035;

    public static final int kActionMovieSetLanguage = 1036;

    public static final int kActionMovieChanged = 1037;

    public static final int kActionTrackSetVolume = 2048;

    public static final int kActionTrackSetBalance = 2049;

    public static final int kActionTrackSetEnabled = 2050;

    public static final int kActionTrackSetMatrix = 2051;

    public static final int kActionTrackSetLayer = 2052;

    public static final int kActionTrackSetClip = 2053;

    public static final int kActionSpriteSetMatrix = 3072;

    public static final int kActionSpriteSetImageIndex = 3073;

    public static final int kActionSpriteSetVisible = 3074;

    public static final int kActionSpriteSetLayer = 3075;

    public static final int kActionSpriteSetGraphicsMode = 3076;

    public static final int kActionSpritePassMouseToCodec = 3078;

    public static final int kActionSpriteClickOnCodec = 3079;

    public static final int kActionSpriteTranslate = 3080;

    public static final int kActionSpriteScale = 3081;

    public static final int kActionSpriteRotate = 3082;

    public static final int kActionSpriteStretch = 3083;

    public static final int kActionQTVRSetPanAngle = 4096;

    public static final int kActionQTVRSetTiltAngle = 4097;

    public static final int kActionQTVRSetFieldOfView = 4098;

    public static final int kActionQTVRShowDefaultView = 4099;

    public static final int kActionQTVRGoToNodeID = 4100;

    public static final int kActionMusicPlayNote = 5120;

    public static final int kActionMusicSetController = 5121;

    public static final int kActionCase = 6144;

    public static final int kActionWhile = 6145;

    public static final int kActionGoToURL = 6146;

    public static final int kActionSendQTEventToSprite = 6147;

    public static final int kActionDebugStr = 6148;

    public static final int kActionPushCurrentTime = 6149;

    public static final int kActionPushCurrentTimeWithLabel = 6150;

    public static final int kActionPopAndGotoTopTime = 6151;

    public static final int kActionPopAndGotoLabeledTime = 6152;

    public static final int kOperandExpression = 1;

    public static final int kOperandConstant = 2;

    public static final int kOperandMovieVolume = 1024;

    public static final int kOperandMovieRate = 1025;

    public static final int kOperandMovieIsLooping = 1026;

    public static final int kOperandMovieLoopIsPalindrome = 1027;

    public static final int kOperandMovieTime = 1028;

    public static final int kOperandTrackVolume = 2048;

    public static final int kOperandTrackBalance = 2049;

    public static final int kOperandTrackEnabled = 2050;

    public static final int kOperandTrackLayer = 2051;

    public static final int kOperandTrackWidth = 2052;

    public static final int kOperandTrackHeight = 2053;

    public static final int kOperandSpriteBoundsLeft = 3072;

    public static final int kOperandSpriteBoundsTop = 3073;

    public static final int kOperandSpriteBoundsRight = 3074;

    public static final int kOperandSpriteBoundsBottom = 3075;

    public static final int kOperandSpriteImageIndex = 3076;

    public static final int kOperandSpriteVisible = 3077;

    public static final int kOperandSpriteLayer = 3078;

    public static final int kOperandSpriteTrackVariable = 3079;

    public static final int kOperandSpriteTrackNumSprites = 3080;

    public static final int kOperandSpriteTrackNumImages = 3081;

    public static final int kOperandSpriteID = 3082;

    public static final int kOperandSpriteIndex = 3083;

    public static final int kOperandSpriteFirstCornerX = 3084;

    public static final int kOperandSpriteFirstCornerY = 3085;

    public static final int kOperandSpriteSecondCornerX = 3086;

    public static final int kOperandSpriteSecondCornerY = 3087;

    public static final int kOperandSpriteThirdCornerX = 3088;

    public static final int kOperandSpriteThirdCornerY = 3089;

    public static final int kOperandSpriteFourthCornerX = 3090;

    public static final int kOperandSpriteFourthCornerY = 3091;

    public static final int kOperandSpriteImageRegistrationPointX = 3092;

    public static final int kOperandSpriteImageRegistrationPointY = 3093;

    public static final int kOperandQTVRPanAngle = 4096;

    public static final int kOperandQTVRTiltAngle = 4097;

    public static final int kOperandQTVRFieldOfView = 4098;

    public static final int kOperandQTVRNodeID = 4099;

    public static final int kOperandMouseLocalHLoc = 5120;

    public static final int kOperandMouseLocalVLoc = 5121;

    public static final int kOperandKeyIsDown = 5122;

    public static final int kOperandRandom = 5123;

    public static final int kTargetMovie = 1836019574;

    public static final int kTargetTrackName = 1953656417;

    public static final int kTargetTrackID = 1953655140;

    public static final int kTargetTrackType = 1953657977;

    public static final int kTargetTrackIndex = 1953655150;

    public static final int kTargetSpriteName = 1936748129;

    public static final int kTargetSpriteID = 1936746852;

    public static final int kTargetSpriteIndex = 1936746862;

    public static final int kQTEventType = 1702260340;

    public static final int kAction = 1633907822;

    public static final int kWhichAction = 2003331427;

    public static final int kActionParameter = 1885434477;

    public static final int kActionTarget = 1952543335;

    public static final int kActionFlags = 1718378855;

    public static final int kActionParameterMinValue = 1835626102;

    public static final int kActionParameterMaxValue = 1835104374;

    public static final int kActionListAtomType = 1818850164;

    public static final int kExpressionContainerAtomType = 1702391922;

    public static final int kConditionalAtomType = 1952805748;

    public static final int kOperatorAtomType = 1869636978;

    public static final int kOperandAtomType = 1869640302;

    public static final int kCommentAtomType = 2003335456;

    public static final int kQTEventMouseClick = 1668049259;

    public static final int kQTEventMouseClickEnd = 1667591780;

    public static final int kQTEventMouseClickEndTriggerButton = 1953655143;

    public static final int kQTEventMouseEnter = 1701737586;

    public static final int kQTEventMouseExit = 1702390132;

    public static final int kQTEventFrameLoaded = 1718772077;

    public static final int kQTEventIdle = 1768189029;

    public static final int kQTEventRequestToModifyMovie = 1919250797;

    public static final int kActionFlagActionIsDelta = 2;

    public static final int kActionFlagParameterWrapsAround = 4;

    public static final int kActionFlagActionIsToggle = 8;

    public static final int kOperatorAdd = 1633969184;

    public static final int kOperatorSubtract = 1937072672;

    public static final int kOperatorMultiply = 1836412020;

    public static final int kOperatorDivide = 1684633120;

    public static final int kOperatorOr = 1869750304;

    public static final int kOperatorAnd = 1634624544;

    public static final int kOperatorNot = 1852797984;

    public static final int kOperatorLessThan = 1008738336;

    public static final int kOperatorLessThanEqualTo = 1010638880;

    public static final int kOperatorEqualTo = 1025515552;

    public static final int kOperatorNotEqualTo = 557654048;

    public static final int kOperatorGreaterThan = 1042292768;

    public static final int kOperatorGreaterThanEqualTo = 1044193312;

    public static final int kOperatorModulo = 1836016672;

    public static final int kOperatorIntegerDivide = 1768188278;

    public static final int kOperatorAbsoluteValue = 1633841952;

    public static final int kOperatorNegate = 1852139296;

    public static final int kMediaPropertyNonLinearAtomType = 1852796524;

    public static final int kMediaPropertyHasActions = 105;

    public static final int preloadAlways = 1;

    public static final int preloadOnlyIfEnabled = 2;

    public static final int trackUsageInMovie = 2;

    public static final int trackUsageInPreview = 4;

    public static final int trackUsageInPoster = 8;

    public static final int kTrackModifierInput = 26990;

    public static final int kTrackModifierType = 29817;

    public static final int kTrackModifierReference = 1936945763;

    public static final int kTrackModifierObjectID = 1868720484;

    public static final int kTrackModifierInputName = 1851878757;

    public static final int kTrackModifierTypeMatrix = 1;

    public static final int kTrackModifierTypeClip = 2;

    public static final int kTrackModifierTypeGraphicsMode = 5;

    public static final int kTrackModifierTypeVolume = 3;

    public static final int kTrackModifierTypeBalance = 4;

    public static final int kTrackModifierTypeImage = 1986618469;

    public static final int kTrackModifierObjectMatrix = 6;

    public static final int kTrackModifierObjectGraphicsMode = 7;

    public static final int kTrackModifierType3d4x4Matrix = 8;

    public static final int kTrackModifierCameraData = 9;

    public static final int kTrackModifierSoundLocalizationData = 10;

    public static final int kTrackModifierObjectImageIndex = 11;

    public static final int kTrackModifierObjectLayer = 12;

    public static final int kTrackModifierObjectVisible = 13;

    public static final int kTrackModifierPanAngle = 1885433376;

    public static final int kTrackModifierTiltAngle = 1953066100;

    public static final int kTrackModifierVerticalFieldOfViewAngle = 1718580768;

    public static final int kTrackModifierObjectQTEventSend = 1702260340;

    public static final int videoMediaType = 1986618469;

    public static final int soundMediaType = 1936684398;

    public static final int textMediaType = 1952807028;

    public static final int baseMediaType = 1735291491;

    public static final int MPEGMediaType = 1297106247;

    public static final int musicMediaType = 1836413801;

    public static final int timeCodeMediaType = 1953325924;

    public static final int spriteMediaType = 1936749172;

    public static final int tweenMediaType = 1953981806;

    public static final int threeDMediaType = 1902392164;

    public static final int threeDeeMediaType = 1902392164;

    public static final int DVCMediaType = 1685480224;

    public static final int handleDataHandlerSubType = 1752065132;

    public static final int resourceDataHandlerSubType = 1920168547;

    public static final int aliasDataHandlerSubType = 1634494835;

    public static final int urlDataHandlerSubType = 1970433056;

    public static final int mediaSampleNotSync = 1;

    public static final int mediaSampleShadowSync = 2;

    public static final int dataRefSelfReference = 1;

    public static final int dataRefWasNotResolved = 2;

    public static final int dataHandlerType = 1684565106;

    public static final int kDataHCanRead = 1;

    public static final int kDataHSpecialRead = 2;

    public static final int kDataHSpecialReadFile = 4;

    public static final int kDataHCanWrite = 8;

    public static final int kDataHSpecialWrite = 16;

    public static final int kDataHSpecialWriteFile = 32;

    public static final int kDataHCanStreamingWrite = 64;

    public static final int kDataHMustCheckDataRef = 128;

    public static final int kGetMovieImporterValidateToFind = 1;

    public static final int kGetMovieImporterAllowNewFile = 2;

    public static final int kGetMovieImporterDontConsiderGraphicsImporters = 4;

    public static final int mediaHandlerType = 1835560050;

    public static final int kSpritePropertyImageIndex = 100;

    public static final int kSpriteTrackPropertyBackgroundColor = 101;

    public static final int kSpriteTrackPropertyOffscreenBitDepth = 102;

    public static final int kSpriteTrackPropertySampleFormat = 103;

    public static final int kSpriteTrackPropertyScaleSpritesToScaleWorld = 104;

    public static final int kSpriteTrackPropertyHasActions = 105;

    public static final int kSpriteTrackPropertyVisible = 106;

    public static final int kSpriteTrackPropertyQTIdleEventsFrequency = 107;

    public static final int kSpriteImagePropertyRegistrationPoint = 1000;

    public static final int kSpriteImagePropertyGroupID = 1001;

    public static final int kNoQTIdleEvents = -1;

    public static final int kKeyFrameAndSingleOverride = 2;

    public static final int kKeyFrameAndAllOverrides = 4;

    public static final int kSpriteAtomType = 1936749172;

    public static final int kSpriteImagesContainerAtomType = 1768776564;

    public static final int kSpriteImageAtomType = 1768776039;

    public static final int kSpriteImageDataAtomType = 1768776801;

    public static final int kSpriteImageGroupIDAtomType = 1768777586;

    public static final int kSpriteImageRegistrationAtomType = 1768780391;

    public static final int kSpriteSharedDataAtomType = 1684434036;

    public static final int kSpriteNameAtomType = 1851878757;

    public static final int kSpriteImageNameAtomType = 1851878757;

    public static final int kSpriteUsesImageIDsAtomType = 1970496883;

    public static final int findTextEdgeOK = 1;

    public static final int findTextCaseSensitive = 2;

    public static final int findTextReverseSearch = 4;

    public static final int findTextWrapAround = 8;

    public static final int findTextUseOffset = 16;

    public static final int dropShadowOffsetType = 1685221487;

    public static final int dropShadowTranslucencyType = 1685221492;

    public static final int dfDontDisplay = 1;

    public static final int dfDontAutoScale = 2;

    public static final int dfClipToTextBox = 4;

    public static final int dfUseMovieBGColor = 8;

    public static final int dfShrinkTextBoxToFit = 16;

    public static final int dfScrollIn = 32;

    public static final int dfScrollOut = 64;

    public static final int dfHorizScroll = 128;

    public static final int dfReverseScroll = 256;

    public static final int dfContinuousScroll = 512;

    public static final int dfFlowHoriz = 1024;

    public static final int dfContinuousKaraoke = 2048;

    public static final int dfDropShadow = 4096;

    public static final int dfAntiAlias = 8192;

    public static final int dfKeyedText = 16384;

    public static final int dfInverseHilite = 32768;

    public static final int dfTextColorHilite = 65536;

    public static final int k3DMediaRendererEntry = 1919250020;

    public static final int k3DMediaRendererName = 1851878757;

    public static final int k3DMediaRendererCode = 1919119204;

    public static final int kGetAtomicInstNoExpandedSamples = 1;

    public static final int kGetAtomicInstNoOriginalSamples = 2;

    public static final int kGetAtomicInstNoSamples = 3;

    public static final int kGetAtomicInstNoKnobList = 4;

    public static final int kGetAtomicInstNoInstrumentInfo = 8;

    public static final int kGetAtomicInstOriginalKnobList = 16;

    public static final int kGetAtomicInstAllKnobs = 32;

    public static final int kSetAtomicInstKeepOriginalInstrument = 1;

    public static final int kSetAtomicInstShareAcrossParts = 2;

    public static final int kSetAtomicInstCallerTosses = 4;

    public static final int kSetAtomicInstCallerGuarantees = 8;

    public static final int kSetAtomicInstInterruptSafe = 16;

    public static final int kSetAtomicInstDontPreprocess = 128;

    public static final int kInstrumentExactMatch = 2;

    public static final int kInstrumentRecommendedSubstitute = 1;

    public static final int kInstrumentRoland8BitQuality = 5;

    public static final int kGetInstrumentInfoNoBuiltIn = 1;

    public static final int kGetInstrumentInfoMidiUserInst = 2;

    public static final int kGetInstrumentInfoNoIText = 4;

    public static final int kBaseInstrumentSubType = 1650553701;

    public static final int kInstrumentComponentType = 1768846196;

    public static final int kMusicComponentType = 1836413801;

    public static final int kSoftSynthComponentSubType = 1936924704;

    public static final int kGMSynthComponentSubType = 1735204896;

    public static final int kaiToneDescType = 1953459813;

    public static final int kaiNoteRequestInfoType = 1853125233;

    public static final int kaiKnobListType = 1802396268;

    public static final int kaiKeyRangeInfoType = 1936289382;

    public static final int kaiSampleDescType = 1935962979;

    public static final int kaiSampleInfoType = 1936550254;

    public static final int kaiSampleDataType = 1935958388;

    public static final int kaiSampleDataQUIDType = 1903520100;

    public static final int kaiInstInfoType = 1768517222;

    public static final int kaiPictType = 1885954932;

    public static final int kaiWriterType = 1081569908;

    public static final int kaiCopyrightType = 1080258681;

    public static final int kaiOtherStrType = 1937011232;

    public static final int kaiInstrumentRefType = 1769104742;

    public static final int kaiInstGMQualityType = 1903518060;

    public static final int kaiLibraryInfoType = 1818848870;

    public static final int kaiLibraryDescType = 1818522467;

    public static final int kInstKnobMissingUnknown = 0;

    public static final int kInstKnobMissingDefault = 1;

    public static final int kMusicLoopTypeNormal = 0;

    public static final int kMusicLoopTypePalindrome = 1;

    public static final int instSamplePreProcessFlag = 1;

    public static final int kQTMIDIComponentType = 1835623529;

    public static final int kOMSComponentSubType = 1330467616;

    public static final int kFMSComponentSubType = 1179472672;

    public static final int kMIDIManagerComponentSubType = 1835886450;

    public static final int kAppleSysexID = 17;

    public static final int kAppleSysexCmdSampleSize = 1;

    public static final int kAppleSysexCmdSampleBreak = 2;

    public static final int kAppleSysexCmdAtomicInstrument = 16;

    public static final int kAppleSysexCmdDeveloper = 32512;

    public static final int kMusicPacketPortLost = 1;

    public static final int kMusicPacketPortFound = 2;

    public static final int kMusicPacketTimeGap = 3;

    public static final int kNoteAllocatorComponentType = 1852798049;

    public static final int kPickDontMix = 1;

    public static final int kPickSameSynth = 2;

    public static final int kPickUserInsts = 4;

    public static final int kPickEditAllowEdit = 8;

    public static final int kPickEditAllowPick = 16;

    public static final int kPickEditSynthGlobal = 32;

    public static final int kPickEditControllers = 64;

    public static final int kNoteRequestNoGM = 1;

    public static final int kNoteRequestNoSynthType = 2;

    public static final int kNoteRequestSynthMustMatch = 4;

    public static final int kVoiceCountDynamic = -1;

    public static final int kGeneralEventNoteRequest = 1;

    public static final int kGeneralEventPartKey = 4;

    public static final int kGeneralEventTuneDifference = 5;

    public static final int kGeneralEventAtomicInstrument = 6;

    public static final int kGeneralEventKnob = 7;

    public static final int kGeneralEventMIDIChannel = 8;

    public static final int kGeneralEventPartChange = 9;

    public static final int kGeneralEventNoOp = 10;

    public static final int kGeneralEventUsedNotes = 11;

    public static final int kGeneralEventPartMix = 12;

    public static final int kMarkerEventEnd = 0;

    public static final int kMarkerEventBeat = 1;

    public static final int kMarkerEventTempo = 2;

    public static final int kMIDINoteOn = 144;

    public static final int kMIDINoteOff = 128;

    public static final int kMIDIPolyPressure = 160;

    public static final int kMIDIControlChange = 176;

    public static final int kMIDIProgramChange = 192;

    public static final int kMIDIAftertouch = 208;

    public static final int kMIDIChannelPressure = 208;

    public static final int kMIDIPitchBend = 224;

    public static final int kMIDIBeginSystemExclusive = 240;

    public static final int kMIDIEndSystemExclusive = 247;

    public static final int kMIDIChannel1 = 0;

    public static final int kMIDIChannel2 = 1;

    public static final int kMIDIChannel3 = 2;

    public static final int kMIDIChannel4 = 3;

    public static final int kMIDIChannel5 = 4;

    public static final int kMIDIChannel6 = 5;

    public static final int kMIDIChannel7 = 6;

    public static final int kMIDIChannel8 = 7;

    public static final int kMIDIChannel9 = 8;

    public static final int kMIDIChannel10 = 9;

    public static final int kMIDIChannel11 = 10;

    public static final int kMIDIChannel12 = 11;

    public static final int kMIDIChannel13 = 12;

    public static final int kMIDIChannel14 = 13;

    public static final int kMIDIChannel15 = 14;

    public static final int kMIDIChannel16 = 15;

    public static final int kControllerModulationWheel = 1;

    public static final int kControllerBreath = 2;

    public static final int kControllerFoot = 4;

    public static final int kControllerPortamentoTime = 5;

    public static final int kControllerVolume = 7;

    public static final int kControllerBalance = 8;

    public static final int kControllerPan = 10;

    public static final int kControllerExpression = 11;

    public static final int kControllerLever1 = 16;

    public static final int kControllerLever2 = 17;

    public static final int kControllerLever3 = 18;

    public static final int kControllerLever4 = 19;

    public static final int kControllerLever5 = 80;

    public static final int kControllerLever6 = 81;

    public static final int kControllerLever7 = 82;

    public static final int kControllerLever8 = 83;

    public static final int kControllerPitchBend = 32;

    public static final int kControllerAfterTouch = 33;

    public static final int kControllerPartTranspose = 40;

    public static final int kControllerTuneTranspose = 41;

    public static final int kControllerPartVolume = 42;

    public static final int kControllerTuneVolume = 43;

    public static final int kControllerSustain = 64;

    public static final int kControllerPortamento = 65;

    public static final int kControllerSostenuto = 66;

    public static final int kControllerSoftPedal = 67;

    public static final int kControllerReverb = 91;

    public static final int kControllerTremolo = 92;

    public static final int kControllerChorus = 93;

    public static final int kControllerCeleste = 94;

    public static final int kControllerPhaser = 95;

    public static final int kControllerEditPart = 113;

    public static final int kControllerMasterTune = 114;

    public static final int kControllerMasterTranspose = 114;

    public static final int kControllerMasterVolume = 115;

    public static final int kControllerMasterCPULoad = 116;

    public static final int kControllerMasterPolyphony = 117;

    public static final int kControllerMasterFeatures = 118;

    public static final int kQTMSKnobStartID = 33554432;

    public static final int kQTMSKnobVolumeAttackTimeID = 33554433;

    public static final int kQTMSKnobVolumeDecayTimeID = 33554434;

    public static final int kQTMSKnobVolumeSustainLevelID = 33554435;

    public static final int kQTMSKnobVolumeRelease1RateID = 33554436;

    public static final int kQTMSKnobVolumeDecayKeyScalingID = 33554437;

    public static final int kQTMSKnobVolumeReleaseTimeID = 33554438;

    public static final int kQTMSKnobVolumeLFODelayID = 33554439;

    public static final int kQTMSKnobVolumeLFORampTimeID = 33554440;

    public static final int kQTMSKnobVolumeLFOPeriodID = 33554441;

    public static final int kQTMSKnobVolumeLFOShapeID = 33554442;

    public static final int kQTMSKnobVolumeLFODepthID = 33554443;

    public static final int kQTMSKnobVolumeOverallID = 33554444;

    public static final int kQTMSKnobVolumeVelocity127ID = 33554445;

    public static final int kQTMSKnobVolumeVelocity96ID = 33554446;

    public static final int kQTMSKnobVolumeVelocity64ID = 33554447;

    public static final int kQTMSKnobVolumeVelocity32ID = 33554448;

    public static final int kQTMSKnobVolumeVelocity16ID = 33554449;

    public static final int kQTMSKnobPitchTransposeID = 33554450;

    public static final int kQTMSKnobPitchLFODelayID = 33554451;

    public static final int kQTMSKnobPitchLFORampTimeID = 33554452;

    public static final int kQTMSKnobPitchLFOPeriodID = 33554453;

    public static final int kQTMSKnobPitchLFOShapeID = 33554454;

    public static final int kQTMSKnobPitchLFODepthID = 33554455;

    public static final int kQTMSKnobPitchLFOQuantizeID = 33554456;

    public static final int kQTMSKnobStereoDefaultPanID = 33554457;

    public static final int kQTMSKnobStereoPositionKeyScalingID = 33554458;

    public static final int kQTMSKnobPitchLFOOffsetID = 33554459;

    public static final int kQTMSKnobExclusionGroupID = 33554460;

    public static final int kQTMSKnobSustainTimeID = 33554461;

    public static final int kQTMSKnobSustainInfiniteID = 33554462;

    public static final int kQTMSKnobVolumeLFOStereoID = 33554463;

    public static final int kQTMSKnobVelocityLowID = 33554464;

    public static final int kQTMSKnobVelocityHighID = 33554465;

    public static final int kQTMSKnobVelocitySensitivityID = 33554466;

    public static final int kQTMSKnobPitchSensitivityID = 33554467;

    public static final int kQTMSKnobVolumeLFODepthFromWheelID = 33554468;

    public static final int kQTMSKnobPitchLFODepthFromWheelID = 33554469;

    public static final int kQTMSKnobVolumeExpOptionsID = 33554470;

    public static final int kQTMSKnobEnv1AttackTimeID = 33554471;

    public static final int kQTMSKnobEnv1DecayTimeID = 33554472;

    public static final int kQTMSKnobEnv1SustainLevelID = 33554473;

    public static final int kQTMSKnobEnv1SustainTimeID = 33554474;

    public static final int kQTMSKnobEnv1SustainInfiniteID = 33554475;

    public static final int kQTMSKnobEnv1ReleaseTimeID = 33554476;

    public static final int kQTMSKnobEnv1ExpOptionsID = 33554477;

    public static final int kQTMSKnobEnv2AttackTimeID = 33554478;

    public static final int kQTMSKnobEnv2DecayTimeID = 33554479;

    public static final int kQTMSKnobEnv2SustainLevelID = 33554480;

    public static final int kQTMSKnobEnv2SustainTimeID = 33554481;

    public static final int kQTMSKnobEnv2SustainInfiniteID = 33554482;

    public static final int kQTMSKnobEnv2ReleaseTimeID = 33554483;

    public static final int kQTMSKnobEnv2ExpOptionsID = 33554484;

    public static final int kQTMSKnobPitchEnvelopeID = 33554485;

    public static final int kQTMSKnobPitchEnvelopeDepthID = 33554486;

    public static final int kQTMSKnobFilterKeyFollowID = 33554487;

    public static final int kQTMSKnobFilterTransposeID = 33554488;

    public static final int kQTMSKnobFilterQID = 33554489;

    public static final int kQTMSKnobFilterFrequencyEnvelopeID = 33554490;

    public static final int kQTMSKnobFilterFrequencyEnvelopeDepthID = 33554491;

    public static final int kQTMSKnobFilterQEnvelopeID = 33554492;

    public static final int kQTMSKnobFilterQEnvelopeDepthID = 33554493;

    public static final int kQTMSKnobReverbThresholdID = 33554494;

    public static final int kQTMSKnobVolumeAttackVelScalingID = 33554495;

    public static final int kQTMSKnobLastIDPlus1 = 33554496;

    public static final int kFirstGMInstrument = 1;

    public static final int kLastGMInstrument = 128;

    public static final int kFirstGSInstrument = 129;

    public static final int kLastGSInstrument = 16383;

    public static final int kFirstROMInstrument = 32768;

    public static final int kLastROMInstrument = 65535;

    public static final int kFirstUserInstrument = 65536;

    public static final int kLastUserInstrument = 131071;

    public static final int kInstrumentMatchSynthesizerType = 1;

    public static final int kInstrumentMatchSynthesizerName = 2;

    public static final int kInstrumentMatchName = 4;

    public static final int kInstrumentMatchNumber = 8;

    public static final int kInstrumentMatchGMNumber = 16;

    public static final int kInstrumentMatchGSNumber = 32;

    public static final int kKnobBasic = 8;

    public static final int kKnobReadOnly = 16;

    public static final int kKnobInterruptUnsafe = 32;

    public static final int kKnobKeyrangeOverride = 64;

    public static final int kKnobGroupStart = 128;

    public static final int kKnobFixedPoint8 = 1024;

    public static final int kKnobFixedPoint16 = 2048;

    public static final int kKnobTypeNumber = 0;

    public static final int kKnobTypeGroupName = 4096;

    public static final int kKnobTypeBoolean = 8192;

    public static final int kKnobTypeNote = 12288;

    public static final int kKnobTypePan = 16384;

    public static final int kKnobTypeInstrument = 20480;

    public static final int kKnobTypeSetting = 24576;

    public static final int kKnobTypeMilliseconds = 28672;

    public static final int kKnobTypePercentage = 32768;

    public static final int kKnobTypeHertz = 36864;

    public static final int kKnobTypeButton = 40960;

    public static final int kUnknownKnobValue = Integer.MAX_VALUE;

    public static final int kDefaultKnobValue = 2147483646;

    public static final short kControllerMaximum = 32767;

    public static final short kControllerMinimum = -32768;

    public static final int kNoDrumKit = 16384;

    public static final int kFirstDrumkit = 16385;

    public static final int kLastDrumkit = 16512;

    public static final int kSynthesizerConnectionMono = 1;

    public static final int kSynthesizerConnectionMMgr = 2;

    public static final int kSynthesizerConnectionOMS = 4;

    public static final int kSynthesizerConnectionQT = 8;

    public static final int kSynthesizerConnectionUnavailable = 256;

    public static final int kSynthesizerConnectionFMS = 16;

    public static final int kSynthesizerDynamicVoice = 1;

    public static final int kSynthesizerUsesMIDIPort = 2;

    public static final int kSynthesizerMicrotone = 4;

    public static final int kSynthesizerHasSamples = 8;

    public static final int kSynthesizerMixedDrums = 16;

    public static final int kSynthesizerSoftware = 32;

    public static final int kSynthesizerHardware = 64;

    public static final int kSynthesizerDynamicChannel = 128;

    public static final int kSynthesizerHogsSystemChannel = 256;

    public static final int kSynthesizerHasSystemChannel = 512;

    public static final int kSynthesizerSlowSetPart = 1024;

    public static final int kSynthesizerOffline = 4096;

    public static final int kSynthesizerGM = 16384;

    public static final int kSynthesizerDLS = 32768;

    public static final int kSynthesizerSoundLocalization = 65536;

    public static final int kTunePlayerComponentType = 1953853029;

    public static final int kTuneStartNow = 1;

    public static final int kTuneDontClipNotes = 2;

    public static final int kTuneExcludeEdgeNotes = 4;

    public static final int kTuneQuickStart = 8;

    public static final int kTuneLoopUntil = 16;

    public static final int kTunePlayDifference = 32;

    public static final int kTunePlayConcurrent = 64;

    public static final int kTuneStartNewMaster = 16384;

    public static final int kTuneStopFade = 1;

    public static final int kTuneStopSustain = 2;

    public static final int kTuneStopInstant = 4;

    public static final int kTuneStopReleaseChannels = 8;

    public static final int kTuneMixMute = 1;

    public static final int kTuneMixSolo = 2;

    public static final int kRestEventType = 0;

    public static final int kNoteEventType = 1;

    public static final int kControlEventType = 2;

    public static final int kMarkerEventType = 3;

    public static final int kUndefined1EventType = 8;

    public static final int kXNoteEventType = 9;

    public static final int kXControlEventType = 10;

    public static final int kKnobEventType = 11;

    public static final int kUndefined2EventType = 12;

    public static final int kUndefined3EventType = 13;

    public static final int kUndefined4EventType = 14;

    public static final int kGeneralEventType = 15;

    public static final int kXEventLengthBits = 2;

    public static final int kGeneralEventLengthBits = 3;

    public static final int kEventLen = 1;

    public static final int kXEventLen = 2;

    public static final int kRestEventLen = 1;

    public static final int kNoteEventLen = 1;

    public static final int kControlEventLen = 1;

    public static final int kMarkerEventLen = 1;

    public static final int kXNoteEventLen = 2;

    public static final int kXControlEventLen = 2;

    public static final int kGeneralEventLen = 2;

    public static final int kEventLengthFieldPos = 30;

    public static final int kEventLengthFieldWidth = 2;

    public static final int kEventTypeFieldPos = 29;

    public static final int kEventTypeFieldWidth = 3;

    public static final int kXEventTypeFieldPos = 28;

    public static final int kXEventTypeFieldWidth = 4;

    public static final int kEventPartFieldPos = 24;

    public static final int kEventPartFieldWidth = 5;

    public static final int kXEventPartFieldPos = 16;

    public static final int kXEventPartFieldWidth = 12;

    public static final int kRestEventDurationFieldPos = 0;

    public static final int kRestEventDurationFieldWidth = 24;

    public static final int kRestEventDurationMax = 16777215;

    public static final int kNoteEventPitchFieldPos = 18;

    public static final int kNoteEventPitchFieldWidth = 6;

    public static final int kNoteEventPitchOffset = 32;

    public static final int kNoteEventVolumeFieldPos = 11;

    public static final int kNoteEventVolumeFieldWidth = 7;

    public static final int kNoteEventVolumeOffset = 0;

    public static final int kNoteEventDurationFieldPos = 0;

    public static final int kNoteEventDurationFieldWidth = 11;

    public static final int kNoteEventDurationMax = 2047;

    public static final int kXNoteEventPitchFieldPos = 0;

    public static final int kXNoteEventPitchFieldWidth = 16;

    public static final int kXNoteEventDurationFieldPos = 0;

    public static final int kXNoteEventDurationFieldWidth = 22;

    public static final int kXNoteEventDurationMax = 4194303;

    public static final int kXNoteEventVolumeFieldPos = 22;

    public static final int kXNoteEventVolumeFieldWidth = 7;

    public static final int kControlEventControllerFieldPos = 16;

    public static final int kControlEventControllerFieldWidth = 8;

    public static final int kControlEventValueFieldPos = 0;

    public static final int kControlEventValueFieldWidth = 16;

    public static final int kXControlEventControllerFieldPos = 0;

    public static final int kXControlEventControllerFieldWidth = 16;

    public static final int kXControlEventValueFieldPos = 0;

    public static final int kXControlEventValueFieldWidth = 16;

    public static final int kKnobEventValueHighFieldPos = 0;

    public static final int kKnobEventValueHighFieldWidth = 16;

    public static final int kKnobEventKnobFieldPos = 16;

    public static final int kKnobEventKnobFieldWidth = 14;

    public static final int kKnobEventValueLowFieldPos = 0;

    public static final int kKnobEventValueLowFieldWidth = 16;

    public static final int kMarkerEventSubtypeFieldPos = 16;

    public static final int kMarkerEventSubtypeFieldWidth = 8;

    public static final int kMarkerEventValueFieldPos = 0;

    public static final int kMarkerEventValueFieldWidth = 16;

    public static final int kGeneralEventSubtypeFieldPos = 16;

    public static final int kGeneralEventSubtypeFieldWidth = 14;

    public static final int kGeneralEventLengthFieldPos = 0;

    public static final int kGeneralEventLengthFieldWidth = 16;

    public static final int kCurrentlyNativeEndian = 1;

    public static final int kCurrentlyNotNativeEndian = 2;

    public static final int movieExportType = 1936746868;

    public static final int kMovieExportTextOnly = 0;

    public static final int kMovieExportAbsoluteTime = 1;

    public static final int kMovieExportRelativeTime = 2;

    public static final int movieImportType = 1700885536;

    public static final int movieImportCreateTrack = 1;

    public static final int movieImportInParallel = 2;

    public static final int movieImportMustUseTrack = 4;

    public static final int movieImportResultUsedMultipleTracks = 8;

    public static final int kMIDIImportSilenceBefore = 1;

    public static final int kMIDIImportSilenceAfter = 2;

    public static final int kMIDIImport20Playable = 4;

    public static final int kMIDIImportWantLyrics = 8;

    public static final int kQTSettingsEffect = 1701209701;

    public static final int kQTSettingsMIDI = 1296647241;

    public static final int kQTSettingsMIDISettingFlags = 1937011815;

    public static final int kQTSettingsText = 1952807028;

    public static final int kQTSettingsTextDescription = 1684370275;

    public static final int kQTSettingsTextSize = 1936292453;

    public static final int kQTSettingsTextSettingFlags = 1937011815;

    public static final int kQTSettingsTextTimeFraction = 1953066342;

    public static final int kQTSettingsTime = 1953066341;

    public static final int kQTSettingsAudioCDTrack = 1953653099;

    public static final int kQTSettingsAudioCDTrackRateShift = 1920165990;

    public static final int tcDropFrame = 1;

    public static final int tc24HourMax = 2;

    public static final int tcNegTimesOK = 4;

    public static final int tcCounter = 8;

    public static final int tcdfShowTimeCode = 1;

    public static final int tweenComponentType = 1953981806;

    public static final int kTweenEntry = 1953981806;

    public static final int kTweenData = 1684108385;

    public static final int kTweenType = 1953984116;

    public static final int kTweenStartOffset = 1953985396;

    public static final int kTweenDuration = 1953981557;

    public static final int kTweenFlags = 1718378855;

    public static final int kTweenOutputMin = 1869441390;

    public static final int kTweenOutputMax = 1869439352;

    public static final int kTweenSequenceElement = 1936028005;

    public static final int kTween3dInitialCondition = 1768124004;

    public static final int kTweenInterpolationID = 1768846450;

    public static final int kTweenInterpolateStyle = 1769174137;

    public static final int kInputMapSubInputID = 1937072745;

    public static final int kTweenRegionData = 1902408295;

    public static final int kTweenPictureData = 1346978644;

    public static final int kListElementType = 1954115685;

    public static final int kListElementDataType = 1684108409;

    public static final int kNameAtom = 1851878757;

    public static final int kInitialRotationAtom = 1768845935;

    public static final int kNonLinearTweenHeader = 1852601448;

    public static final int kTweenReturnDelta = 1;

    public static final int kTweenTypeShort = 1;

    public static final int kTweenTypeLong = 2;

    public static final int kTweenTypeFixed = 3;

    public static final int kTweenTypeFloat = 3;

    public static final int kTweenTypePoint = 4;

    public static final int kTweenTypeQDRect = 5;

    public static final int kTweenTypeQDRegion = 6;

    public static final int kTweenTypeMatrix = 7;

    public static final int kTweenTypeRGBColor = 8;

    public static final int kTweenTypeGraphicsModeWithRGBColor = 9;

    public static final int kTweenTypeQTFloatSingle = 10;

    public static final int kTweenTypeQTFloatDouble = 11;

    public static final int kTweenTypeFixedPoint = 12;

    public static final int kTweenType3dScale = 863200097;

    public static final int kTweenType3dTranslate = 863269473;

    public static final int kTweenType3dRotate = 863137652;

    public static final int kTweenType3dRotateAboutPoint = 863134064;

    public static final int kTweenType3dRotateAboutAxis = 863134072;

    public static final int kTweenType3dRotateAboutVector = 863139427;

    public static final int kTweenType3dQuaternion = 863073633;

    public static final int kTweenType3dMatrix = 862806388;

    public static final int kTweenType3dCameraData = 862151021;

    public static final int kTweenType3dSoundLocalizationData = 863202403;

    public static final int kTweenTypePathToMatrixTranslation = 1735945588;

    public static final int kTweenTypePathToMatrixRotation = 1735946354;

    public static final int kTweenTypePathToMatrixTranslationAndRotation = 1735945586;

    public static final int kTweenTypePathToFixedPoint = 1735943792;

    public static final int kTweenTypePathXtoY = 1735948409;

    public static final int kTweenTypePathYtoX = 1735948664;

    public static final int kTweenTypeAtomList = 1635020653;

    public static final int kTweenTypePolygon = 1886350457;

    public static final int kTweenTypeMultiMatrix = 1836412013;

    public static final int kTweenTypeSpin = 1936746862;

    public static final int kTweenType3dMatrixNonLinear = 862874738;

    public static final int kTweenType3dVRObject = 863400559;

    public static final int kParameterTypeDataLong = 2;

    public static final int kParameterTypeDataFixed = 3;

    public static final int kParameterTypeDataRGBValue = 8;

    public static final int kParameterTypeDataDouble = 11;

    public static final int seqGrabComponentType = 1650553447;

    public static final int seqGrabChannelType = 1936155496;

    public static final int seqGrabPanelType = 1936158830;

    public static final int seqGrabCompressionPanelType = 1668116594;

    public static final int seqGrabSourcePanelType = 1936684402;

    public static final int seqGrabUnpause = 0;

    public static final int seqGrabPause = 1;

    public static final int seqGrabPauseForMenu = 3;

    public static final int seqGrabToDisk = 1;

    public static final int seqGrabToMemory = 2;

    public static final int seqGrabDontUseTempMemory = 4;

    public static final int seqGrabAppendToFile = 8;

    public static final int seqGrabDontAddMovieResource = 16;

    public static final int seqGrabDontMakeMovie = 32;

    public static final int seqGrabPreExtendFile = 64;

    public static final int seqGrabDataProcIsInterruptSafe = 128;

    public static final int seqGrabDataProcDoesOverlappingReads = 256;

    public static final int seqGrabRecord = 1;

    public static final int seqGrabPreview = 2;

    public static final int seqGrabPlayDuringRecord = 4;

    public static final int seqGrabHasBounds = 1;

    public static final int seqGrabHasVolume = 2;

    public static final int seqGrabHasDiscreteSamples = 4;

    public static final int sgDeviceListWithIcons = 1;

    public static final int sgDeviceListDontCheckAvailability = 2;

    public static final int seqGrabWriteAppend = 0;

    public static final int seqGrabWriteReserve = 1;

    public static final int seqGrabWriteFill = 2;

    public static final int channelPlayNormal = 0;

    public static final int channelPlayFast = 1;

    public static final int channelPlayHighQuality = 2;

    public static final int channelPlayAllData = 4;

    public static final int channelFlagDontOpenResFile = 2;

    public static final int channelFlagHasDependency = 4;

    public static final int videoDigitizerComponentType = 1986618471;

    public static final int vdigInterfaceRev = 2;

    public static final int ntscIn = 0;

    public static final int currentIn = 0;

    public static final int palIn = 1;

    public static final int secamIn = 2;

    public static final int ntscReallyIn = 3;

    public static final int compositeIn = 0;

    public static final int sVideoIn = 1;

    public static final int rgbComponentIn = 2;

    public static final int rgbComponentSyncIn = 3;

    public static final int yuvComponentIn = 4;

    public static final int yuvComponentSyncIn = 5;

    public static final int tvTunerIn = 6;

    public static final int sdiIn = 7;

    public static final int vdPlayThruOff = 0;

    public static final int vdPlayThruOn = 1;

    public static final int vdDigitizerBW = 0;

    public static final int vdDigitizerRGB = 1;

    public static final int vdBroadcastMode = 0;

    public static final int vdVTRMode = 1;

    public static final int vdUseAnyField = 0;

    public static final int vdUseOddField = 1;

    public static final int vdUseEvenField = 2;

    public static final int vdTypeBasic = 0;

    public static final int vdTypeAlpha = 1;

    public static final int vdTypeMask = 2;

    public static final int vdTypeKey = 3;

    public static final int digiInDoesNTSC = 1;

    public static final int digiInDoesPAL = 2;

    public static final int digiInDoesSECAM = 4;

    public static final int digiInDoesGenLock = 128;

    public static final int digiInDoesComposite = 256;

    public static final int digiInDoesSVideo = 512;

    public static final int digiInDoesComponent = 1024;

    public static final int digiInVTR_Broadcast = 2048;

    public static final int digiInDoesColor = 4096;

    public static final int digiInDoesBW = 8192;

    public static final int digiInSignalLock = Integer.MIN_VALUE;

    public static final int digiOutDoes1 = 1;

    public static final int digiOutDoes2 = 2;

    public static final int digiOutDoes4 = 4;

    public static final int digiOutDoes8 = 8;

    public static final int digiOutDoes16 = 16;

    public static final int digiOutDoes32 = 32;

    public static final int digiOutDoesDither = 64;

    public static final int digiOutDoesStretch = 128;

    public static final int digiOutDoesShrink = 256;

    public static final int digiOutDoesMask = 512;

    public static final int digiOutDoesDouble = 2048;

    public static final int digiOutDoesQuad = 4096;

    public static final int digiOutDoesQuarter = 8192;

    public static final int digiOutDoesSixteenth = 16384;

    public static final int digiOutDoesRotate = 32768;

    public static final int digiOutDoesHorizFlip = 65536;

    public static final int digiOutDoesVertFlip = 131072;

    public static final int digiOutDoesSkew = 262144;

    public static final int digiOutDoesBlend = 524288;

    public static final int digiOutDoesWarp = 1048576;

    public static final int digiOutDoesHW_DMA = 2097152;

    public static final int digiOutDoesHWPlayThru = 4194304;

    public static final int digiOutDoesILUT = 8388608;

    public static final int digiOutDoesKeyColor = 16777216;

    public static final int digiOutDoesAsyncGrabs = 33554432;

    public static final int digiOutDoesUnreadableScreenBits = 67108864;

    public static final int digiOutDoesCompress = 134217728;

    public static final int digiOutDoesCompressOnly = 268435456;

    public static final int digiOutDoesPlayThruDuringCompress = 536870912;

    public static final int digiOutDoesCompressPartiallyVisible = 1073741824;

    public static final int dmaDepth1 = 1;

    public static final int dmaDepth2 = 2;

    public static final int dmaDepth4 = 4;

    public static final int dmaDepth8 = 8;

    public static final int dmaDepth16 = 16;

    public static final int dmaDepth32 = 32;

    public static final int dmaDepth2Gray = 64;

    public static final int dmaDepth4Gray = 128;

    public static final int dmaDepth8Gray = 256;

    public static final int kVDIGControlledFrameRate = -1;

    public static final int sgChannelAtom = 1667785070;

    public static final int sgChannelSettingsAtom = 1668575085;

    public static final int sgChannelDescription = 1667527523;

    public static final int sgChannelSettings = 1668506996;

    public static final int sgDeviceNameType = 1851878757;

    public static final int sgUsageType = 1970496800;

    public static final int sgPlayFlagsType = 1886157158;

    public static final int sgClipType = 1668049264;

    public static final int sgMatrixType = 1836348024;

    public static final int sgVolumeType = 1987013749;

    public static final int sgPanelSettingsAtom = 1886678893;

    public static final int sgPanelDescription = 1885631331;

    public static final int sgPanelSettings = 1886610804;

    public static final int sgcSoundCompressionType = 1935895920;

    public static final int sgcSoundSampleRateType = 1936875892;

    public static final int sgcSoundChannelCountType = 1935894638;

    public static final int sgcSoundSampleSizeType = 1936943482;

    public static final int sgcSoundInputType = 1936289392;

    public static final int sgcSoundGainType = 1734437230;

    public static final int sgcVideoHueType = 1752524064;

    public static final int sgcVideoSaturationType = 1935766642;

    public static final int sgcVideoContrastType = 1953657716;

    public static final int sgcVideoSharpnessType = 1936224880;

    public static final int sgcVideoBrigtnessType = 1651665268;

    public static final int sgcVideoBlackLevelType = 1651272556;

    public static final int sgcVideoWhiteLevelType = 2003334252;

    public static final int sgcVideoInputType = 1986621040;

    public static final int sgcVideoFormatType = 1987277924;

    public static final int sgcVideoFilterType = 1986423924;

    public static final int sgcVideoRectType = 1987208052;

    public static final int sgcVideoDigitizerType = 1986292071;

    public static final int kQTVODisplayModeItem = 1902407017;

    public static final int kQTVODimensions = 1684630894;

    public static final int kQTVOResolution = 1919251308;

    public static final int kQTVORefreshRate = 1919247986;

    public static final int kQTVOPixelType = 1885960300;

    public static final int kQTVOName = 1851878757;

    public static final int kQTVODecompressors = 1684366191;

    public static final int kQTVODecompressorType = 1684370553;

    public static final int kQTVODecompressorContinuous = 1668247156;

    public static final int kQTVODecompressorComponent = 1668116596;

    public static final int canMovieImportHandles = 1;

    public static final int canMovieImportFiles = 2;

    public static final int hasMovieImportUserInterface = 4;

    public static final int canMovieExportHandles = 8;

    public static final int canMovieExportFiles = 16;

    public static final int hasMovieExportUserInterface = 32;

    public static final int dontAutoFileMovieImport = 64;

    public static final int canMovieExportAuxDataHandle = 128;

    public static final int canMovieImportValidateHandles = 256;

    public static final int canMovieImportValidateFile = 512;

    public static final int dontRegisterWithEasyOpen = 1024;

    public static final int canMovieImportInPlace = 2048;

    public static final int movieImportSubTypeIsFileExtension = 4096;

    public static final int canMovieImportPartial = 8192;

    public static final int hasMovieImportMIMEList = 16384;

    public static final int canMovieExportFromProcedures = 32768;

    public static final int canMovieExportValidateMovie = 65536;

    public static final int movieExportNeedsResourceFork = 131072;

    public static final int canMovieImportDataReferences = 262144;

    public static final int movieExportMustGetSourceMediaType = 524288;

    public static final int reservedForUseByGraphicsImporters = 8388608;

    public static final int scPositionRect = 2;

    public static final int scPositionDialog = 3;

    public static final int scSetTestImagePictHandle = 4;

    public static final int scSetTestImagePictFile = 5;

    public static final int scSetTestImagePixMap = 6;

    public static final int scGetBestDeviceRect = 7;

    public static final int scRequestImageSettings = 10;

    public static final int scCompressImage = 11;

    public static final int scCompressPicture = 12;

    public static final int scCompressPictureFile = 13;

    public static final int scRequestSequenceSettings = 14;

    public static final int scCompressSequenceBegin = 15;

    public static final int scCompressSequenceFrame = 16;

    public static final int scCompressSequenceEnd = 17;

    public static final int scDefaultPictHandleSettings = 18;

    public static final int scDefaultPictFileSettings = 19;

    public static final int scDefaultPixMapSettings = 20;

    public static final int scGetInfo = 21;

    public static final int scSetInfo = 22;

    public static final int scNewGWorld = 23;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * StdQTConstants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
