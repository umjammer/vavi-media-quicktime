package quicktime;

public interface Errors {

    /**
     * no error
     */
    int noErr = 0;

    /**
     * Unspecified error code.
     */
    int kUnspecifiedErr = -1;

    /**
     * Instrument number was incorrect
     */
    int kIllegalInstrumentErr = -2079;

    /**
     * An incorrect part number was supplied
     */
    int kInvalidPartNumberErr = -2075;

    int pictIsOpenErr = -9;

    int controlErr = -17;

    int statusErr = -18;

    int readErr = -19;

    int writErr = -20;

    int badUnitErr = -21;

    int unitEmptyErr = -22;

    int openErr = -23;

    int closErr = -24;

    int dRemovErr = -25;

    int dInstErr = -26;

    int abortErr = -27;

    int dirFulErr = -33;

    int dskFulErr = -34;

    int nsvErr = -35;

    int ioErr = -36;

    int bdNamErr = -37;

    int eofErr = -39;

    int fnfErr = -43;

    int wPrErr = -44;

    int vLckdErr = -46;

    int fBsyErr = -47;

    int dupFNErr = -48;

    int paramErr = -50;

    int permErr = -54;

    int memFullErr = -108;

    int dirNFErr = -120;

    int userCanceledErr = -128;

    int cDepthErrInvalid = -157;

    /**
     * Resource errors
     */
    int rmvResFailed = -196;

    /**
     * Resource errors
     */
    int addResFailed = -194;

    /**
     * Resource errors
     */
    int resFNotFound = -193;

    /**
     * Resource errors
     */
    int resNotFound = -192;

    /**
     * Sound manager errors
     */
    int notEnoughHardwareErr = -201;

    /**
     * Sound manager errors
     */
    int queueFull = -203;

    /**
     * Sound manager errors
     */
    int badChannel = -205;

    /**
     * Sound manager errors
     */
    int resProblem = -204;

    /**
     * Sound manager errors
     */
    int badFormat = -206;

    /**
     * Sound manager errors
     */
    int notEnoughBufferSpace = -207;

    /**
     * Sound manager errors
     */
    int badFileFormat = -208;

    /**
     * Sound manager errors
     */
    int channelBusy = -209;

    /**
     * Sound manager errors
     */
    int buffersTooSmall = -210;

    /**
     * Sound manager errors
     */
    int channelNotBusy = -211;

    /**
     * Sound manager errors
     */
    int noMoreRealTime = -212;

    /**
     * Sound manager errors
     */
    int siNoSoundInHardware = -220;

    /**
     * Sound manager errors
     */
    int siBadSoundInDevice = -221;

    /**
     * Sound manager errors
     */
    int siNoBufferSpecified = -222;

    /**
     * Sound manager errors
     */
    int siInvalidCompression = -223;

    /**
     * Sound manager errors
     */
    int siHardDriveTooSlow = -224;

    /**
     * Sound manager errors
     */
    int siInvalidSampleRate = -225;

    /**
     * Sound manager errors
     */
    int siInvalidSampleSize = -226;

    /**
     * Sound manager errors
     */
    int siDeviceBusyErr = -227;

    /**
     * Sound manager errors
     */
    int siBadDeviceName = -228;

    /**
     * Sound manager errors
     */
    int siBadRefNum = -229;

    /**
     * Sound manager errors
     */
    int siInputDeviceErr = -230;

    /**
     * Sound manager errors
     */
    int siUnknownInfoType = -231;

    /**
     * Sound manager errors
     */
    int siUnknownQuality = -232;

    /**
     * Sound manager errors
     */
    int rgnTooBigErr = -500;

    /**
     * Port related errors
     */
    int noPortErr = -903;

    /**
     * Port related errors
     */
    int noGlobalsErr = -904;

    /**
     * Port related errors
     */
    int destPortErr = -906;

    int duplicateHandlerErr = -1855;

    int handlerNotFoundErr = -1856;

    /**
     * Movie Toolbox errors
     */
    int couldNotResolveDataRef = -2000;

    /**
     * Movie Toolbox errors
     */
    int badImageDescription = -2001;

    /**
     * Movie Toolbox errors
     */
    int badPublicMovieAtom = -2002;

    /**
     * Movie Toolbox errors
     */
    int cantFindHandler = -2003;

    /**
     * Movie Toolbox errors
     */
    int cantOpenHandler = -2004;

    /**
     * Movie Toolbox errors
     */
    int badComponentType = -2005;

    /**
     * Movie Toolbox errors
     */
    int noMediaHandler = -2006;

    /**
     * Movie Toolbox errors
     */
    int noDataHandler = -2007;

    /**
     * Movie Toolbox errors
     */
    int invalidMedia = -2008;

    /**
     * Movie Toolbox errors
     */
    int invalidTrack = -2009;

    /**
     * Movie Toolbox errors
     */
    int invalidMovie = -2010;

    /**
     * Movie Toolbox errors
     */
    int invalidSampleTable = -2011;

    /**
     * Movie Toolbox errors
     */
    int invalidDataRef = -2012;

    /**
     * Movie Toolbox errors
     */
    int invalidHandler = -2013;

    /**
     * Movie Toolbox errors
     */
    int invalidDuration = -2014;

    /**
     * Movie Toolbox errors
     */
    int invalidTime = -2015;

    /**
     * Movie Toolbox errors
     */
    int cantPutPublicMovieAtom = -2016;

    /**
     * Movie Toolbox errors
     */
    int badEditList = -2017;

    /**
     * Movie Toolbox errors
     */
    int mediaTypesDontMatch = -2018;

    /**
     * Movie Toolbox errors
     */
    int progressProcAborted = -2019;

    /**
     * Movie Toolbox errors
     */
    int movieToolboxUninitialized = -2020;

    /**
     * Movie Toolbox errors
     */
    int noRecordOfApp = -2020;

    /**
     * Movie Toolbox errors
     */
    int wfFileNotFound = -2021;

    /**
     * Movie Toolbox errors
     */
    int cantCreateSingleForkFile = -2022;

    /**
     * Movie Toolbox errors
     */
    int invalidEditState = -2023;

    /**
     * Movie Toolbox errors
     */
    int nonMatchingEditState = -2024;

    /**
     * Movie Toolbox errors
     */
    int staleEditState = -2025;

    /**
     * Movie Toolbox errors
     */
    int userDataItemNotFound = -2026;

    /**
     * Movie Toolbox errors
     */
    int maxSizeToGrowTooSmall = -2027;

    /**
     * Movie Toolbox errors
     */
    int badTrackIndex = -2028;

    /**
     * Movie Toolbox errors
     */
    int trackIDNotFound = -2029;

    /**
     * Movie Toolbox errors
     */
    int trackNotInMovie = -2030;

    /**
     * Movie Toolbox errors
     */
    int timeNotInTrack = -2031;

    /**
     * Movie Toolbox errors
     */
    int timeNotInMedia = -2032;

    /**
     * Movie Toolbox errors
     */
    int badEditIndex = -2033;

    /**
     * Movie Toolbox errors
     */
    int internalQuickTimeError = -2034;

    /**
     * Movie Toolbox errors
     */
    int cantEnableTrack = -2035;

    /**
     * Movie Toolbox errors
     */
    int invalidRect = -2036;

    /**
     * Movie Toolbox errors
     */
    int invalidSampleNum = -2037;

    /**
     * Movie Toolbox errors
     */
    int invalidChunkNum = -2038;

    /**
     * Movie Toolbox errors
     */
    int invalidSampleDescIndex = -2039;

    /**
     * Movie Toolbox errors
     */
    int invalidChunkCache = -2040;

    /**
     * Movie Toolbox errors
     */
    int invalidSampleDescription = -2041;

    /**
     * Movie Toolbox errors
     */
    int dataNotOpenForRead = -2042;

    /**
     * Movie Toolbox errors
     */
    int dataNotOpenForWrite = -2043;

    /**
     * Movie Toolbox errors
     */
    int dataAlreadyOpenForWrite = -2044;

    /**
     * Movie Toolbox errors
     */
    int dataAlreadyClosed = -2045;

    /**
     * Movie Toolbox errors
     */
    int endOfDataReached = -2046;

    /**
     * Movie Toolbox errors
     */
    int dataNoDataRef = -2047;

    /**
     * Movie Toolbox errors
     */
    int noMovieFound = -2048;

    /**
     * Movie Toolbox errors
     */
    int invalidDataRefContainer = -2049;

    /**
     * Movie Toolbox errors
     */
    int badDataRefIndex = -2050;

    /**
     * Movie Toolbox errors
     */
    int noDefaultDataRef = -2051;

    /**
     * Movie Toolbox errors
     */
    int couldNotUseAnExistingSample = -2052;

    /**
     * Movie Toolbox errors
     */
    int featureUnsupported = -2053;

    /**
     * Movie Toolbox errors
     */
    int noVideoTrackInMovieErr = -2054;

    /**
     * Movie Toolbox errors
     */
    int noSoundTrackInMovieErr = -2055;

    /**
     * Movie Toolbox errors
     */
    int soundSupportNotAvailableErr = -2056;

    /**
     * Movie Toolbox errors
     */
    int unsupportedAuxiliaryImportData = -2057;

    /**
     * Movie Toolbox errors
     */
    int auxiliaryExportDataUnavailable = -2058;

    /**
     * Movie Toolbox errors
     */
    int samplesAlreadyInMediaErr = -2059;

    /**
     * Movie Toolbox errors
     */
    int noSourceTreeFoundErr = -2060;

    /**
     * Movie Toolbox errors
     */
    int sourceNotFoundErr = -2061;

    /**
     * Movie Toolbox errors
     */
    int movieTextNotFoundErr = -2062;

    /**
     * Movie Toolbox errors
     */
    int missingRequiredParameterErr = -2063;

    /**
     * Movie Toolbox errors
     */
    int invalidSpriteWorldPropertyErr = -2064;

    /**
     * Movie Toolbox errors
     */
    int invalidSpritePropertyErr = -2065;

    /**
     * Movie Toolbox errors
     */
    int gWorldsNotSameDepthAndSizeErr = -2066;

    /**
     * Movie Toolbox errors
     */
    int invalidSpriteIndexErr = -2067;

    /**
     * Movie Toolbox errors
     */
    int invalidImageIndexErr = -2068;

    /**
     * Movie Toolbox errors
     */
    int invalidSpriteIDErr = -2069;

    int internalComponentErr = -2070;

    int notImplementedMusicOSErr = -2071;

    int cantSendToSynthesizerOSErr = -2072;

    int cantReceiveFromSynthesizerOSErr = -2073;

    int illegalVoiceAllocationOSErr = -2074;

    int illegalPartOSErr = -2075;

    int illegalChannelOSErr = -2076;

    int illegalKnobOSErr = -2077;

    int illegalKnobValueOSErr = -2078;

    int illegalInstrumentOSErr = -2079;

    int illegalControllerOSErr = -2080;

    int midiManagerAbsentOSErr = -2081;

    int synthesizerNotRespondingOSErr = -2082;

    int synthesizerOSErr = -2083;

    int illegalNoteChannelOSErr = -2084;

    int noteChannelNotAllocatedOSErr = -2085;

    int tunePlayerFullOSErr = -2086;

    int tuneParseOSErr = -2087;

    int noExportProcAvailableErr = -2089;

    int videoOutputInUseErr = -2090;

    int componentDllLoadErr = -2091;

    int componentDllEntryNotFoundErr = -2092;

    int qtmlDllLoadErr = -2093;

    int qtmlDllEntryNotFoundErr = -2094;

    int qtmlUninitialized = -2095;

    int unsupportedOSErr = -2096;

    int unsupportedProcessorErr = -2097;

    int cannotFindAtomErr = -2101;

    int notLeafAtomErr = -2102;

    int atomsNotOfSameTypeErr = -2103;

    int atomIndexInvalidErr = -2104;

    int duplicateAtomTypeAndIDErr = -2105;

    int invalidAtomErr = -2106;

    int invalidAtomContainerErr = -2107;

    int invalidAtomTypeErr = -2108;

    int cannotBeLeafAtomErr = -2109;

    /**
     * Path errors
     */
    int pathTooLongErr = -2110;

    /**
     * Path errors
     */
    int emptyPathErr = -2111;

    /**
     * Path errors
     */
    int noPathMappingErr = -2112;

    /**
     * Path errors
     */
    int pathNotVerifiedErr = -2113;

    /**
     * Path errors
     */
    int unknownFormatErr = -2114;

    /**
     * Path errors
     */
    int wackBadFileErr = -2115;

    /**
     * Path errors
     */
    int wackForkNotFoundErr = -2116;

    /**
     * Path errors
     */
    int wackBadMetaDataErr = -2117;

    /**
     * Path errors
     */
    int qfcbNotFoundErr = -2118;

    /**
     * Path errors
     */
    int qfcbNotCreatedErr = -2119;

    /**
     * Path errors
     */
    int AAPNotCreatedErr = -2120;

    /**
     * Path errors
     */
    int AAPNotFoundErr = -2121;

    /**
     * Path errors
     */
    int ASDBadHeaderErr = -2122;

    /**
     * Path errors
     */
    int ASDBadForkErr = -2123;

    /**
     * Path errors
     */
    int ASDEntryNotFoundErr = -2124;

    /**
     * Path errors
     */
    int fileOffsetTooBigErr = -2125;

    /**
     * Path errors
     */
    int notAllowedToSaveMovieErr = -2126;

    /**
     * Path errors
     */
    int qtNetworkAlreadyAllocatedErr = -2127;

    /**
     * Path errors
     */
    int urlDataHHTTPProtocolErr = -2129;

    /**
     * Path errors
     */
    int urlDataHHTTPNoNetDriverErr = -2130;

    /**
     * Path errors
     */
    int urlDataHHTTPURLErr = -2131;

    /**
     * Path errors
     */
    int urlDataHHTTPRedirectErr = -2132;

    /**
     * Path errors
     */
    int urlDataHFTPProtocolErr = -2133;

    /**
     * Path errors
     */
    int urlDataHFTPShutdownErr = -2134;

    /**
     * Path errors
     */
    int urlDataHFTPBadUserErr = -2135;

    /**
     * Path errors
     */
    int urlDataHFTPBadPasswordErr = -2136;

    /**
     * Path errors
     */
    int urlDataHFTPServerErr = -2137;

    /**
     * Path errors
     */
    int urlDataHFTPDataConnectionErr = -2138;

    /**
     * Path errors
     */
    int urlDataHFTPNoDirectoryErr = -2139;

    /**
     * Path errors
     */
    int urlDataHFTPQuotaErr = -2140;

    /**
     * Path errors
     */
    int urlDataHFTPPermissionsErr = -2141;

    /**
     * Path errors
     */
    int urlDataHFTPFilenameErr = -2142;

    /**
     * Path errors
     */
    int urlDataHFTPNoNetDriverErr = -2143;

    /**
     * Path errors
     */
    int urlDataHFTPBadNameListErr = -2144;

    /**
     * Path errors
     */
    int urlDataHFTPNeedPasswordErr = -2145;

    /**
     * Path errors
     */
    int urlDataHFTPNoPasswordErr = -2146;

    /**
     * Path errors
     */
    int urlDataHFTPServerDisconnectedErr = -2147;

    /**
     * Path errors
     */
    int urlDataHFTPURLErr = -2148;

    /**
     * Path errors
     */
    int notEnoughDataErr = -2149;

    /**
     * Path errors
     */
    int qtActionNotHandledErr = -2157;

    int digiUnimpErr = -2201;

    int qtParamErr = -2202;

    int matrixErr = -2203;

    int notExactMatrixErr = -2204;

    int noMoreKeyColorsErr = -2205;

    int notExactSizeErr = -2206;

    int badDepthErr = -2207;

    int noDMAErr = -2208;

    int badCallOrderErr = -2209;

    int invalidComponentID = -3000;

    int validInstancesExist = -3001;

    int componentNotCaptured = -3002;

    int componentDontRegister = -3003;

    int unresolvedComponentDLLErr = -3004;

    int afpAccessDenied = -5000;

    int afpObjectTypeErr = -5025;

    int codecErr = -8960;

    int noCodecErr = -8961;

    int codecUnimpErr = -8962;

    int codecSizeErr = -8963;

    int codecScreenBufErr = -8964;

    int codecImageBufErr = -8965;

    int codecSpoolErr = -8966;

    int codecAbortErr = -8967;

    int codecWouldOffscreenErr = -8968;

    int codecBadDataErr = -8969;

    int codecDataVersErr = -8970;

    int codecExtensionNotFoundErr = -8971;

    int scTypeNotFoundErr = -8971;

    int codecConditionErr = -8972;

    int codecOpenErr = -8973;

    int codecCantWhenErr = -8974;

    int codecCantQueueErr = -8975;

    int codecNothingToBlitErr = -8976;

    int codecNoMemoryPleaseWaitErr = -8977;

    int codecDisabledErr = -8978;

    int codecNeedToFlushChainErr = -8979;

    int lockPortBitsBadSurfaceErr = -8980;

    int lockPortBitsWindowMovedErr = -8981;

    int lockPortBitsWindowResizedErr = -8982;

    int lockPortBitsWindowClippedErr = -8983;

    int lockPortBitsBadPortErr = -8984;

    int lockPortBitsSurfaceLostErr = -8985;

    int codecParameterDialogConfirm = -8986;

    int codecNeedAccessKeyErr = -8987;

    int codecOffscreenFailedErr = -8988;

    int codecDroppedFrameErr = -8989;

    int noDeviceForChannel = -9400;

    int grabTimeComplete = -9401;

    int cantDoThatInCurrentMode = -9402;

    int notEnoughMemoryToGrab = -9403;

    int notEnoughDiskSpaceToGrab = -9404;

    int couldntGetRequiredComponent = -9405;

    int badSGChannel = -9406;

    int seqGrabInfoNotAvailable = -9407;

    int deviceCantMeetRequest = -9408;

    int badControllerHeight = -9994;

    int editingNotAllowed = -9995;

    int controllerBoundsNotExact = -9996;

    int cannotSetWidthOfAttachedController = -9997;

    int controllerHasFixedHeight = -9998;

    int cannotMoveAttachedController = -9999;

    int kQ3ErrorInternalError = -28500;

    int kQ3ErrorNoRecovery = -28499;

    int kQ3ErrorLastFatalError = -28498;

    int kQ3ErrorNotInitialized = -28490;

    int kQ3ErrorAlreadyInitialized = -28489;

    int kQ3ErrorUnimplemented = -28488;

    int kQ3ErrorRegistrationFailed = -28487;

    int kQ3ErrorUnixError = -28486;

    int kQ3ErrorMacintoshError = -28485;

    int kQ3ErrorX11Error = -28484;

    int kQ3ErrorMemoryLeak = -28483;

    int kQ3ErrorOutOfMemory = -28482;

    int kQ3ErrorNULLParameter = -28481;

    int kQ3ErrorParameterOutOfRange = -28480;

    int kQ3ErrorInvalidParameter = -28479;

    int kQ3ErrorInvalidData = -28478;

    int kQ3ErrorAcceleratorAlreadySet = -28477;

    int kQ3ErrorVector3DNotUnitLength = -28476;

    int kQ3ErrorVector3DZeroLength = -28475;

    int kQ3ErrorInvalidObject = -28474;

    int kQ3ErrorInvalidObjectClass = -28473;

    int kQ3ErrorInvalidObjectType = -28472;

    int kQ3ErrorInvalidObjectName = -28471;

    int kQ3ErrorObjectClassInUse = -28470;

    int kQ3ErrorAccessRestricted = -28469;

    int kQ3ErrorMetaHandlerRequired = -28468;

    int kQ3ErrorNeedRequiredMethods = -28467;

    int kQ3ErrorNoSubClassType = -28466;

    int kQ3ErrorUnknownElementType = -28465;

    int kQ3ErrorNotSupported = -28464;

    int kQ3ErrorNoExtensionsFolder = -28463;

    int kQ3ErrorExtensionError = -28462;

    int kQ3ErrorPrivateExtensionError = -28461;

    int kQ3ErrorDegenerateGeometry = -28460;

    int kQ3ErrorGeometryInsufficientNumberOfPoints = -28459;

    int kQ3ErrorNoStorageSetForFile = -28458;

    int kQ3ErrorEndOfFile = -28457;

    int kQ3ErrorFileCancelled = -28456;

    int kQ3ErrorInvalidMetafile = -28455;

    int kQ3ErrorInvalidMetafilePrimitive = -28454;

    int kQ3ErrorInvalidMetafileLabel = -28453;

    int kQ3ErrorInvalidMetafileObject = -28452;

    int kQ3ErrorInvalidMetafileSubObject = -28451;

    int kQ3ErrorInvalidSubObjectForObject = -28450;

    int kQ3ErrorUnresolvableReference = -28449;

    int kQ3ErrorUnknownObject = -28448;

    int kQ3ErrorStorageInUse = -28447;

    int kQ3ErrorStorageAlreadyOpen = -28446;

    int kQ3ErrorStorageNotOpen = -28445;

    int kQ3ErrorStorageIsOpen = -28444;

    int kQ3ErrorFileAlreadyOpen = -28443;

    int kQ3ErrorFileNotOpen = -28442;

    int kQ3ErrorFileIsOpen = -28441;

    int kQ3ErrorBeginWriteAlreadyCalled = -28440;

    int kQ3ErrorBeginWriteNotCalled = -28439;

    int kQ3ErrorEndWriteNotCalled = -28438;

    int kQ3ErrorReadStateInactive = -28437;

    int kQ3ErrorStateUnavailable = -28436;

    int kQ3ErrorWriteStateInactive = -28435;

    int kQ3ErrorSizeNotLongAligned = -28434;

    int kQ3ErrorFileModeRestriction = -28433;

    int kQ3ErrorInvalidHexString = -28432;

    int kQ3ErrorWroteMoreThanSize = -28431;

    int kQ3ErrorWroteLessThanSize = -28430;

    int kQ3ErrorReadLessThanSize = -28429;

    int kQ3ErrorReadMoreThanSize = -28428;

    int kQ3ErrorNoBeginGroup = -28427;

    int kQ3ErrorSizeMismatch = -28426;

    int kQ3ErrorStringExceedsMaximumLength = -28425;

    int kQ3ErrorValueExceedsMaximumSize = -28424;

    int kQ3ErrorNonUniqueLabel = -28423;

    int kQ3ErrorEndOfContainer = -28422;

    int kQ3ErrorUnmatchedEndGroup = -28421;

    int kQ3ErrorFileVersionExists = -28420;

    int kQ3ErrorViewNotStarted = -28419;

    int kQ3ErrorViewIsStarted = -28418;

    int kQ3ErrorRendererNotSet = -28417;

    int kQ3ErrorRenderingIsActive = -28416;

    int kQ3ErrorImmediateModeUnderflow = -28415;

    int kQ3ErrorDisplayNotSet = -28414;

    int kQ3ErrorCameraNotSet = -28413;

    int kQ3ErrorDrawContextNotSet = -28412;

    int kQ3ErrorNonInvertibleMatrix = -28411;

    int kQ3ErrorRenderingNotStarted = -28410;

    int kQ3ErrorPickingNotStarted = -28409;

    int kQ3ErrorBoundsNotStarted = -28408;

    int kQ3ErrorDataNotAvailable = -28407;

    int kQ3ErrorNothingToPop = -28406;

    int kQ3ErrorUnknownStudioType = -28405;

    int kQ3ErrorAlreadyRendering = -28404;

    int kQ3ErrorStartGroupRange = -28403;

    int kQ3ErrorUnsupportedGeometryType = -28402;

    int kQ3ErrorInvalidGeometryType = -28401;

    int kQ3ErrorUnsupportedFunctionality = -28400;

    int kQ3ErrorInvalidPositionForGroup = -28399;

    int kQ3ErrorInvalidObjectForGroup = -28398;

    int kQ3ErrorInvalidObjectForPosition = -28397;

    int kQ3ErrorScaleOfZero = -28396;

    int kQ3ErrorBadStringType = -28395;

    int kQ3ErrorAttributeNotContained = -28394;

    int kQ3ErrorAttributeInvalidType = -28393;

    int kQ3ErrorInvalidCameraValues = -28392;

    int kQ3ErrorBadDrawContextType = -28391;

    int kQ3ErrorBadDrawContextFlag = -28390;

    int kQ3ErrorBadDrawContext = -28389;

    int kQ3ErrorUnsupportedPixelDepth = -28388;

    int kQ3ErrorController = -28387;

    int kQ3ErrorTracker = -28386;

    int kQ3ErrorWin32Error = -28385;

    int kQ3ErrorTypeAlreadyExistsAndHasSubclasses = -28384;

    int kQ3ErrorTypeAlreadyExistsAndOtherClassesDependOnIt = -28383;

    int kQ3ErrorTypeAlreadyExistsAndHasObjectInstances = -28382;

    int kQ3ErrorPickingLoopFailed = -28381;

    int kQ3ErrorRenderingLoopFailed = -28380;

    int kQ3ErrorWritingLoopFailed = -28379;

    int kQ3ErrorBoundingLoopFailed = -28378;

    int kQ3WarningNone = 0;

    int kQ3WarningInternalException = -28300;

    int kQ3WarningNoObjectSupportForDuplicateMethod = -28299;

    int kQ3WarningNoObjectSupportForDrawMethod = -28298;

    int kQ3WarningNoObjectSupportForWriteMethod = -28297;

    int kQ3WarningNoObjectSupportForReadMethod = -28296;

    int kQ3WarningUnknownElementType = -28295;

    int kQ3WarningTypeAndMethodAlreadyDefined = -28294;

    int kQ3WarningTypeIsOutOfRange = -28293;

    int kQ3WarningTypeHasNotBeenRegistered = -28292;

    int kQ3WarningVector3DNotUnitLength = -28291;

    int kQ3WarningInvalidSubObjectForObject = -28290;

    int kQ3WarningInvalidHexString = -28289;

    int kQ3WarningUnknownObject = -28288;

    int kQ3WarningInvalidMetafileObject = -28287;

    int kQ3WarningUnmatchedBeginGroup = -28286;

    int kQ3WarningUnmatchedEndGroup = -28285;

    int kQ3WarningInvalidTableOfContents = -28284;

    int kQ3WarningUnresolvableReference = -28283;

    int kQ3WarningNoAttachMethod = -28282;

    int kQ3WarningInconsistentData = -28281;

    int kQ3WarningReadLessThanSize = -28280;

    int kQ3WarningFilePointerResolutionFailed = -28279;

    int kQ3WarningFilePointerRedefined = -28278;

    int kQ3WarningStringExceedsMaximumLength = -28277;

    int kQ3WarningLowMemory = -28276;

    int kQ3WarningPossibleMemoryLeak = -28275;

    int kQ3WarningViewTraversalInProgress = -28274;

    int kQ3WarningNonInvertibleMatrix = -28273;

    int kQ3WarningQuaternionEntriesAreZero = -28272;

    int kQ3WarningFunctionalityNotSupported = -28271;

    int kQ3WarningInvalidPaneDimensions = -28270;

    int kQ3WarningPickParamOutside = -28269;

    int kQ3WarningScaleEntriesAllZero = -28268;

    int kQ3WarningScaleContainsNegativeEntries = -28267;

    int kQ3WarningParameterOutOfRange = -28266;

    int kQ3WarningExtensionNotLoading = -28265;

    int kQ3WarningTypeAlreadyRegistered = -28264;

    int kQ3WarningTypeSameVersionAlreadyRegistered = -28263;

    int kQ3WarningTypeNewerVersionAlreadyRegistered = -28262;

    int kQ3WarningInvalidObjectInGroupMetafile = -28261;

    int kQ3NoticeDataAlreadyEmpty = -28100;

    int kQ3NoticeMethodNotSupported = -28099;

    int kQ3NoticeObjectAlreadySet = -28098;

    int kQ3NoticeParameterOutOfRange = -28097;

    int kQ3NoticeFileAliasWasChanged = -28096;

    int kQ3NoticeMeshVertexHasNoComponent = -28095;

    int kQ3NoticeMeshInvalidVertexFacePair = -28094;

    int kQ3NoticeMeshEdgeVertexDoNotCorrespond = -28093;

    int kQ3NoticeMeshEdgeIsNotBoundary = -28092;

    int kQ3NoticeDrawContextNotSetUsingInternalDefaults = -28091;

    int kQ3NoticeInvalidAttenuationTypeUsingInternalDefaults = -28090;

    int kQ3NoticeBrightnessGreaterThanOne = -28089;

    int kQ3NoticeScaleContainsZeroEntries = -28088;

    int kQ3NoticeSystemAlreadyInitialized = -28087;

    int kQ3NoticeViewSyncCalledAgain = -28086;

    int kQ3NoticeFileCancelled = -28085;

    int notAQTVRMovieErr = -30540;

    int constraintReachedErr = -30541;

    int callNotSupportedByNodeErr = -30542;

    int selectorNotSupportedByNodeErr = -30543;

    int invalidNodeIDErr = -30544;

    int invalidViewStateErr = -30545;

    int timeNotInViewErr = -30546;

    int propertyNotSupportedByNodeErr = -30547;

    int settingNotSupportedByNodeErr = -30548;

    int limitReachedErr = -30549;

    int invalidNodeFormatErr = -30550;

    int invalidHotSpotIDErr = -30551;

    int noMemoryNodeFailedInitialize = -30552;

    int streamingNodeNotReadyErr = -30553;

    int qtvrLibraryLoadErr = -30554;

    int qtvrUninitialized = -30555;

    /**
     * Component errors
     */
    int badComponentInstance = -2147450879;

    /**
     * Component errors
     */
    int badComponentSelector = -2147450878;

    /**
     * Music Errors
     */
    int notImplementedMusicErr = -2147420183;

    /**
     * Music Errors
     */
    int cantSendToSynthesizerErr = -2147420184;

    /**
     * Music Errors
     */
    int cantReceiveFromSynthesizerErr = -2147420185;

    /**
     * Music Errors
     */
    int illegalVoiceAllocationErr = -2147420186;

    /**
     * Music Errors
     */
    int illegalPartErr = -2147420187;

    /**
     * Music Errors
     */
    int illegalChannelErr = -2147420188;

    /**
     * Music Errors
     */
    int illegalKnobErr = -2147420189;

    /**
     * Music Errors
     */
    int illegalKnobValueErr = -2147420190;

    /**
     * Music Errors
     */
    int illegalInstrumentErr = -2147420191;

    /**
     * Music Errors
     */
    int illegalControllerErr = -2147420192;

    /**
     * Music Errors
     */
    int midiManagerAbsentErr = -2147420193;

    /**
     * Music Errors
     */
    int synthesizerNotRespondingErr = -2147420194;

    /**
     * Music Errors
     */
    int synthesizerErr = -2147420195;

    /**
     * Music Errors
     */
    int illegalNoteChannelErr = -2147420196;

    /**
     * Music Errors
     */
    int noteChannelNotAllocatedErr = -2147420197;

    /**
     * Music Errors
     */
    int tunePlayerFullErr = -2147420198;

    /**
     * Music Errors
     */
    int tuneParseErr = -2147420199;
}
