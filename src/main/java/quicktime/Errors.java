package quicktime;

public abstract interface Errors {

    /**
     * no error
     */
    public static final int noErr = 0;

    /**
     * Unspecified error code.
     */
    public static final int kUnspecifiedErr = -1;

    /**
     * Instrument number was incorrect
     */
    public static final int kIllegalInstrumentErr = -2079;

    /**
     * An incorrect part number was supplied
     */
    public static final int kInvalidPartNumberErr = -2075;

    public static final int pictIsOpenErr = -9;

    public static final int controlErr = -17;

    public static final int statusErr = -18;

    public static final int readErr = -19;

    public static final int writErr = -20;

    public static final int badUnitErr = -21;

    public static final int unitEmptyErr = -22;

    public static final int openErr = -23;

    public static final int closErr = -24;

    public static final int dRemovErr = -25;

    public static final int dInstErr = -26;

    public static final int abortErr = -27;

    public static final int dirFulErr = -33;

    public static final int dskFulErr = -34;

    public static final int nsvErr = -35;

    public static final int ioErr = -36;

    public static final int bdNamErr = -37;

    public static final int eofErr = -39;

    public static final int fnfErr = -43;

    public static final int wPrErr = -44;

    public static final int vLckdErr = -46;

    public static final int fBsyErr = -47;

    public static final int dupFNErr = -48;

    public static final int paramErr = -50;

    public static final int permErr = -54;

    public static final int memFullErr = -108;

    public static final int dirNFErr = -120;

    public static final int userCanceledErr = -128;

    public static final int cDepthErrInvalid = -157;

    /**
     * Resource errors
     */
    public static final int rmvResFailed = -196;

    /**
     * Resource errors
     */
    public static final int addResFailed = -194;

    /**
     * Resource errors
     */
    public static final int resFNotFound = -193;

    /**
     * Resource errors
     */
    public static final int resNotFound = -192;

    /**
     * Sound manager errors
     */
    public static final int notEnoughHardwareErr = -201;

    /**
     * Sound manager errors
     */
    public static final int queueFull = -203;

    /**
     * Sound manager errors
     */
    public static final int badChannel = -205;

    /**
     * Sound manager errors
     */
    public static final int resProblem = -204;

    /**
     * Sound manager errors
     */
    public static final int badFormat = -206;

    /**
     * Sound manager errors
     */
    public static final int notEnoughBufferSpace = -207;

    /**
     * Sound manager errors
     */
    public static final int badFileFormat = -208;

    /**
     * Sound manager errors
     */
    public static final int channelBusy = -209;

    /**
     * Sound manager errors
     */
    public static final int buffersTooSmall = -210;

    /**
     * Sound manager errors
     */
    public static final int channelNotBusy = -211;

    /**
     * Sound manager errors
     */
    public static final int noMoreRealTime = -212;

    /**
     * Sound manager errors
     */
    public static final int siNoSoundInHardware = -220;

    /**
     * Sound manager errors
     */
    public static final int siBadSoundInDevice = -221;

    /**
     * Sound manager errors
     */
    public static final int siNoBufferSpecified = -222;

    /**
     * Sound manager errors
     */
    public static final int siInvalidCompression = -223;

    /**
     * Sound manager errors
     */
    public static final int siHardDriveTooSlow = -224;

    /**
     * Sound manager errors
     */
    public static final int siInvalidSampleRate = -225;

    /**
     * Sound manager errors
     */
    public static final int siInvalidSampleSize = -226;

    /**
     * Sound manager errors
     */
    public static final int siDeviceBusyErr = -227;

    /**
     * Sound manager errors
     */
    public static final int siBadDeviceName = -228;

    /**
     * Sound manager errors
     */
    public static final int siBadRefNum = -229;

    /**
     * Sound manager errors
     */
    public static final int siInputDeviceErr = -230;

    /**
     * Sound manager errors
     */
    public static final int siUnknownInfoType = -231;

    /**
     * Sound manager errors
     */
    public static final int siUnknownQuality = -232;

    /**
     * Sound manager errors
     */
    public static final int rgnTooBigErr = -500;

    /**
     * Port related errors
     */
    public static final int noPortErr = -903;

    /**
     * Port related errors
     */
    public static final int noGlobalsErr = -904;

    /**
     * Port related errors
     */
    public static final int destPortErr = -906;

    public static final int duplicateHandlerErr = -1855;

    public static final int handlerNotFoundErr = -1856;

    /**
     * Movie Toolbox errors
     */
    public static final int couldNotResolveDataRef = -2000;

    /**
     * Movie Toolbox errors
     */
    public static final int badImageDescription = -2001;

    /**
     * Movie Toolbox errors
     */
    public static final int badPublicMovieAtom = -2002;

    /**
     * Movie Toolbox errors
     */
    public static final int cantFindHandler = -2003;

    /**
     * Movie Toolbox errors
     */
    public static final int cantOpenHandler = -2004;

    /**
     * Movie Toolbox errors
     */
    public static final int badComponentType = -2005;

    /**
     * Movie Toolbox errors
     */
    public static final int noMediaHandler = -2006;

    /**
     * Movie Toolbox errors
     */
    public static final int noDataHandler = -2007;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidMedia = -2008;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidTrack = -2009;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidMovie = -2010;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSampleTable = -2011;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidDataRef = -2012;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidHandler = -2013;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidDuration = -2014;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidTime = -2015;

    /**
     * Movie Toolbox errors
     */
    public static final int cantPutPublicMovieAtom = -2016;

    /**
     * Movie Toolbox errors
     */
    public static final int badEditList = -2017;

    /**
     * Movie Toolbox errors
     */
    public static final int mediaTypesDontMatch = -2018;

    /**
     * Movie Toolbox errors
     */
    public static final int progressProcAborted = -2019;

    /**
     * Movie Toolbox errors
     */
    public static final int movieToolboxUninitialized = -2020;

    /**
     * Movie Toolbox errors
     */
    public static final int noRecordOfApp = -2020;

    /**
     * Movie Toolbox errors
     */
    public static final int wfFileNotFound = -2021;

    /**
     * Movie Toolbox errors
     */
    public static final int cantCreateSingleForkFile = -2022;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidEditState = -2023;

    /**
     * Movie Toolbox errors
     */
    public static final int nonMatchingEditState = -2024;

    /**
     * Movie Toolbox errors
     */
    public static final int staleEditState = -2025;

    /**
     * Movie Toolbox errors
     */
    public static final int userDataItemNotFound = -2026;

    /**
     * Movie Toolbox errors
     */
    public static final int maxSizeToGrowTooSmall = -2027;

    /**
     * Movie Toolbox errors
     */
    public static final int badTrackIndex = -2028;

    /**
     * Movie Toolbox errors
     */
    public static final int trackIDNotFound = -2029;

    /**
     * Movie Toolbox errors
     */
    public static final int trackNotInMovie = -2030;

    /**
     * Movie Toolbox errors
     */
    public static final int timeNotInTrack = -2031;

    /**
     * Movie Toolbox errors
     */
    public static final int timeNotInMedia = -2032;

    /**
     * Movie Toolbox errors
     */
    public static final int badEditIndex = -2033;

    /**
     * Movie Toolbox errors
     */
    public static final int internalQuickTimeError = -2034;

    /**
     * Movie Toolbox errors
     */
    public static final int cantEnableTrack = -2035;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidRect = -2036;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSampleNum = -2037;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidChunkNum = -2038;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSampleDescIndex = -2039;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidChunkCache = -2040;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSampleDescription = -2041;

    /**
     * Movie Toolbox errors
     */
    public static final int dataNotOpenForRead = -2042;

    /**
     * Movie Toolbox errors
     */
    public static final int dataNotOpenForWrite = -2043;

    /**
     * Movie Toolbox errors
     */
    public static final int dataAlreadyOpenForWrite = -2044;

    /**
     * Movie Toolbox errors
     */
    public static final int dataAlreadyClosed = -2045;

    /**
     * Movie Toolbox errors
     */
    public static final int endOfDataReached = -2046;

    /**
     * Movie Toolbox errors
     */
    public static final int dataNoDataRef = -2047;

    /**
     * Movie Toolbox errors
     */
    public static final int noMovieFound = -2048;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidDataRefContainer = -2049;

    /**
     * Movie Toolbox errors
     */
    public static final int badDataRefIndex = -2050;

    /**
     * Movie Toolbox errors
     */
    public static final int noDefaultDataRef = -2051;

    /**
     * Movie Toolbox errors
     */
    public static final int couldNotUseAnExistingSample = -2052;

    /**
     * Movie Toolbox errors
     */
    public static final int featureUnsupported = -2053;

    /**
     * Movie Toolbox errors
     */
    public static final int noVideoTrackInMovieErr = -2054;

    /**
     * Movie Toolbox errors
     */
    public static final int noSoundTrackInMovieErr = -2055;

    /**
     * Movie Toolbox errors
     */
    public static final int soundSupportNotAvailableErr = -2056;

    /**
     * Movie Toolbox errors
     */
    public static final int unsupportedAuxiliaryImportData = -2057;

    /**
     * Movie Toolbox errors
     */
    public static final int auxiliaryExportDataUnavailable = -2058;

    /**
     * Movie Toolbox errors
     */
    public static final int samplesAlreadyInMediaErr = -2059;

    /**
     * Movie Toolbox errors
     */
    public static final int noSourceTreeFoundErr = -2060;

    /**
     * Movie Toolbox errors
     */
    public static final int sourceNotFoundErr = -2061;

    /**
     * Movie Toolbox errors
     */
    public static final int movieTextNotFoundErr = -2062;

    /**
     * Movie Toolbox errors
     */
    public static final int missingRequiredParameterErr = -2063;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSpriteWorldPropertyErr = -2064;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSpritePropertyErr = -2065;

    /**
     * Movie Toolbox errors
     */
    public static final int gWorldsNotSameDepthAndSizeErr = -2066;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSpriteIndexErr = -2067;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidImageIndexErr = -2068;

    /**
     * Movie Toolbox errors
     */
    public static final int invalidSpriteIDErr = -2069;

    public static final int internalComponentErr = -2070;

    public static final int notImplementedMusicOSErr = -2071;

    public static final int cantSendToSynthesizerOSErr = -2072;

    public static final int cantReceiveFromSynthesizerOSErr = -2073;

    public static final int illegalVoiceAllocationOSErr = -2074;

    public static final int illegalPartOSErr = -2075;

    public static final int illegalChannelOSErr = -2076;

    public static final int illegalKnobOSErr = -2077;

    public static final int illegalKnobValueOSErr = -2078;

    public static final int illegalInstrumentOSErr = -2079;

    public static final int illegalControllerOSErr = -2080;

    public static final int midiManagerAbsentOSErr = -2081;

    public static final int synthesizerNotRespondingOSErr = -2082;

    public static final int synthesizerOSErr = -2083;

    public static final int illegalNoteChannelOSErr = -2084;

    public static final int noteChannelNotAllocatedOSErr = -2085;

    public static final int tunePlayerFullOSErr = -2086;

    public static final int tuneParseOSErr = -2087;

    public static final int noExportProcAvailableErr = -2089;

    public static final int videoOutputInUseErr = -2090;

    public static final int componentDllLoadErr = -2091;

    public static final int componentDllEntryNotFoundErr = -2092;

    public static final int qtmlDllLoadErr = -2093;

    public static final int qtmlDllEntryNotFoundErr = -2094;

    public static final int qtmlUninitialized = -2095;

    public static final int unsupportedOSErr = -2096;

    public static final int unsupportedProcessorErr = -2097;

    public static final int cannotFindAtomErr = -2101;

    public static final int notLeafAtomErr = -2102;

    public static final int atomsNotOfSameTypeErr = -2103;

    public static final int atomIndexInvalidErr = -2104;

    public static final int duplicateAtomTypeAndIDErr = -2105;

    public static final int invalidAtomErr = -2106;

    public static final int invalidAtomContainerErr = -2107;

    public static final int invalidAtomTypeErr = -2108;

    public static final int cannotBeLeafAtomErr = -2109;

    /**
     * Path errors
     */
    public static final int pathTooLongErr = -2110;

    /**
     * Path errors
     */
    public static final int emptyPathErr = -2111;

    /**
     * Path errors
     */
    public static final int noPathMappingErr = -2112;

    /**
     * Path errors
     */
    public static final int pathNotVerifiedErr = -2113;

    /**
     * Path errors
     */
    public static final int unknownFormatErr = -2114;

    /**
     * Path errors
     */
    public static final int wackBadFileErr = -2115;

    /**
     * Path errors
     */
    public static final int wackForkNotFoundErr = -2116;

    /**
     * Path errors
     */
    public static final int wackBadMetaDataErr = -2117;

    /**
     * Path errors
     */
    public static final int qfcbNotFoundErr = -2118;

    /**
     * Path errors
     */
    public static final int qfcbNotCreatedErr = -2119;

    /**
     * Path errors
     */
    public static final int AAPNotCreatedErr = -2120;

    /**
     * Path errors
     */
    public static final int AAPNotFoundErr = -2121;

    /**
     * Path errors
     */
    public static final int ASDBadHeaderErr = -2122;

    /**
     * Path errors
     */
    public static final int ASDBadForkErr = -2123;

    /**
     * Path errors
     */
    public static final int ASDEntryNotFoundErr = -2124;

    /**
     * Path errors
     */
    public static final int fileOffsetTooBigErr = -2125;

    /**
     * Path errors
     */
    public static final int notAllowedToSaveMovieErr = -2126;

    /**
     * Path errors
     */
    public static final int qtNetworkAlreadyAllocatedErr = -2127;

    /**
     * Path errors
     */
    public static final int urlDataHHTTPProtocolErr = -2129;

    /**
     * Path errors
     */
    public static final int urlDataHHTTPNoNetDriverErr = -2130;

    /**
     * Path errors
     */
    public static final int urlDataHHTTPURLErr = -2131;

    /**
     * Path errors
     */
    public static final int urlDataHHTTPRedirectErr = -2132;

    /**
     * Path errors
     */
    public static final int urlDataHFTPProtocolErr = -2133;

    /**
     * Path errors
     */
    public static final int urlDataHFTPShutdownErr = -2134;

    /**
     * Path errors
     */
    public static final int urlDataHFTPBadUserErr = -2135;

    /**
     * Path errors
     */
    public static final int urlDataHFTPBadPasswordErr = -2136;

    /**
     * Path errors
     */
    public static final int urlDataHFTPServerErr = -2137;

    /**
     * Path errors
     */
    public static final int urlDataHFTPDataConnectionErr = -2138;

    /**
     * Path errors
     */
    public static final int urlDataHFTPNoDirectoryErr = -2139;

    /**
     * Path errors
     */
    public static final int urlDataHFTPQuotaErr = -2140;

    /**
     * Path errors
     */
    public static final int urlDataHFTPPermissionsErr = -2141;

    /**
     * Path errors
     */
    public static final int urlDataHFTPFilenameErr = -2142;

    /**
     * Path errors
     */
    public static final int urlDataHFTPNoNetDriverErr = -2143;

    /**
     * Path errors
     */
    public static final int urlDataHFTPBadNameListErr = -2144;

    /**
     * Path errors
     */
    public static final int urlDataHFTPNeedPasswordErr = -2145;

    /**
     * Path errors
     */
    public static final int urlDataHFTPNoPasswordErr = -2146;

    /**
     * Path errors
     */
    public static final int urlDataHFTPServerDisconnectedErr = -2147;

    /**
     * Path errors
     */
    public static final int urlDataHFTPURLErr = -2148;

    /**
     * Path errors
     */
    public static final int notEnoughDataErr = -2149;

    /**
     * Path errors
     */
    public static final int qtActionNotHandledErr = -2157;

    public static final int digiUnimpErr = -2201;

    public static final int qtParamErr = -2202;

    public static final int matrixErr = -2203;

    public static final int notExactMatrixErr = -2204;

    public static final int noMoreKeyColorsErr = -2205;

    public static final int notExactSizeErr = -2206;

    public static final int badDepthErr = -2207;

    public static final int noDMAErr = -2208;

    public static final int badCallOrderErr = -2209;

    public static final int invalidComponentID = -3000;

    public static final int validInstancesExist = -3001;

    public static final int componentNotCaptured = -3002;

    public static final int componentDontRegister = -3003;

    public static final int unresolvedComponentDLLErr = -3004;

    public static final int afpAccessDenied = -5000;

    public static final int afpObjectTypeErr = -5025;

    public static final int codecErr = -8960;

    public static final int noCodecErr = -8961;

    public static final int codecUnimpErr = -8962;

    public static final int codecSizeErr = -8963;

    public static final int codecScreenBufErr = -8964;

    public static final int codecImageBufErr = -8965;

    public static final int codecSpoolErr = -8966;

    public static final int codecAbortErr = -8967;

    public static final int codecWouldOffscreenErr = -8968;

    public static final int codecBadDataErr = -8969;

    public static final int codecDataVersErr = -8970;

    public static final int codecExtensionNotFoundErr = -8971;

    public static final int scTypeNotFoundErr = -8971;

    public static final int codecConditionErr = -8972;

    public static final int codecOpenErr = -8973;

    public static final int codecCantWhenErr = -8974;

    public static final int codecCantQueueErr = -8975;

    public static final int codecNothingToBlitErr = -8976;

    public static final int codecNoMemoryPleaseWaitErr = -8977;

    public static final int codecDisabledErr = -8978;

    public static final int codecNeedToFlushChainErr = -8979;

    public static final int lockPortBitsBadSurfaceErr = -8980;

    public static final int lockPortBitsWindowMovedErr = -8981;

    public static final int lockPortBitsWindowResizedErr = -8982;

    public static final int lockPortBitsWindowClippedErr = -8983;

    public static final int lockPortBitsBadPortErr = -8984;

    public static final int lockPortBitsSurfaceLostErr = -8985;

    public static final int codecParameterDialogConfirm = -8986;

    public static final int codecNeedAccessKeyErr = -8987;

    public static final int codecOffscreenFailedErr = -8988;

    public static final int codecDroppedFrameErr = -8989;

    public static final int noDeviceForChannel = -9400;

    public static final int grabTimeComplete = -9401;

    public static final int cantDoThatInCurrentMode = -9402;

    public static final int notEnoughMemoryToGrab = -9403;

    public static final int notEnoughDiskSpaceToGrab = -9404;

    public static final int couldntGetRequiredComponent = -9405;

    public static final int badSGChannel = -9406;

    public static final int seqGrabInfoNotAvailable = -9407;

    public static final int deviceCantMeetRequest = -9408;

    public static final int badControllerHeight = -9994;

    public static final int editingNotAllowed = -9995;

    public static final int controllerBoundsNotExact = -9996;

    public static final int cannotSetWidthOfAttachedController = -9997;

    public static final int controllerHasFixedHeight = -9998;

    public static final int cannotMoveAttachedController = -9999;

    public static final int kQ3ErrorInternalError = -28500;

    public static final int kQ3ErrorNoRecovery = -28499;

    public static final int kQ3ErrorLastFatalError = -28498;

    public static final int kQ3ErrorNotInitialized = -28490;

    public static final int kQ3ErrorAlreadyInitialized = -28489;

    public static final int kQ3ErrorUnimplemented = -28488;

    public static final int kQ3ErrorRegistrationFailed = -28487;

    public static final int kQ3ErrorUnixError = -28486;

    public static final int kQ3ErrorMacintoshError = -28485;

    public static final int kQ3ErrorX11Error = -28484;

    public static final int kQ3ErrorMemoryLeak = -28483;

    public static final int kQ3ErrorOutOfMemory = -28482;

    public static final int kQ3ErrorNULLParameter = -28481;

    public static final int kQ3ErrorParameterOutOfRange = -28480;

    public static final int kQ3ErrorInvalidParameter = -28479;

    public static final int kQ3ErrorInvalidData = -28478;

    public static final int kQ3ErrorAcceleratorAlreadySet = -28477;

    public static final int kQ3ErrorVector3DNotUnitLength = -28476;

    public static final int kQ3ErrorVector3DZeroLength = -28475;

    public static final int kQ3ErrorInvalidObject = -28474;

    public static final int kQ3ErrorInvalidObjectClass = -28473;

    public static final int kQ3ErrorInvalidObjectType = -28472;

    public static final int kQ3ErrorInvalidObjectName = -28471;

    public static final int kQ3ErrorObjectClassInUse = -28470;

    public static final int kQ3ErrorAccessRestricted = -28469;

    public static final int kQ3ErrorMetaHandlerRequired = -28468;

    public static final int kQ3ErrorNeedRequiredMethods = -28467;

    public static final int kQ3ErrorNoSubClassType = -28466;

    public static final int kQ3ErrorUnknownElementType = -28465;

    public static final int kQ3ErrorNotSupported = -28464;

    public static final int kQ3ErrorNoExtensionsFolder = -28463;

    public static final int kQ3ErrorExtensionError = -28462;

    public static final int kQ3ErrorPrivateExtensionError = -28461;

    public static final int kQ3ErrorDegenerateGeometry = -28460;

    public static final int kQ3ErrorGeometryInsufficientNumberOfPoints = -28459;

    public static final int kQ3ErrorNoStorageSetForFile = -28458;

    public static final int kQ3ErrorEndOfFile = -28457;

    public static final int kQ3ErrorFileCancelled = -28456;

    public static final int kQ3ErrorInvalidMetafile = -28455;

    public static final int kQ3ErrorInvalidMetafilePrimitive = -28454;

    public static final int kQ3ErrorInvalidMetafileLabel = -28453;

    public static final int kQ3ErrorInvalidMetafileObject = -28452;

    public static final int kQ3ErrorInvalidMetafileSubObject = -28451;

    public static final int kQ3ErrorInvalidSubObjectForObject = -28450;

    public static final int kQ3ErrorUnresolvableReference = -28449;

    public static final int kQ3ErrorUnknownObject = -28448;

    public static final int kQ3ErrorStorageInUse = -28447;

    public static final int kQ3ErrorStorageAlreadyOpen = -28446;

    public static final int kQ3ErrorStorageNotOpen = -28445;

    public static final int kQ3ErrorStorageIsOpen = -28444;

    public static final int kQ3ErrorFileAlreadyOpen = -28443;

    public static final int kQ3ErrorFileNotOpen = -28442;

    public static final int kQ3ErrorFileIsOpen = -28441;

    public static final int kQ3ErrorBeginWriteAlreadyCalled = -28440;

    public static final int kQ3ErrorBeginWriteNotCalled = -28439;

    public static final int kQ3ErrorEndWriteNotCalled = -28438;

    public static final int kQ3ErrorReadStateInactive = -28437;

    public static final int kQ3ErrorStateUnavailable = -28436;

    public static final int kQ3ErrorWriteStateInactive = -28435;

    public static final int kQ3ErrorSizeNotLongAligned = -28434;

    public static final int kQ3ErrorFileModeRestriction = -28433;

    public static final int kQ3ErrorInvalidHexString = -28432;

    public static final int kQ3ErrorWroteMoreThanSize = -28431;

    public static final int kQ3ErrorWroteLessThanSize = -28430;

    public static final int kQ3ErrorReadLessThanSize = -28429;

    public static final int kQ3ErrorReadMoreThanSize = -28428;

    public static final int kQ3ErrorNoBeginGroup = -28427;

    public static final int kQ3ErrorSizeMismatch = -28426;

    public static final int kQ3ErrorStringExceedsMaximumLength = -28425;

    public static final int kQ3ErrorValueExceedsMaximumSize = -28424;

    public static final int kQ3ErrorNonUniqueLabel = -28423;

    public static final int kQ3ErrorEndOfContainer = -28422;

    public static final int kQ3ErrorUnmatchedEndGroup = -28421;

    public static final int kQ3ErrorFileVersionExists = -28420;

    public static final int kQ3ErrorViewNotStarted = -28419;

    public static final int kQ3ErrorViewIsStarted = -28418;

    public static final int kQ3ErrorRendererNotSet = -28417;

    public static final int kQ3ErrorRenderingIsActive = -28416;

    public static final int kQ3ErrorImmediateModeUnderflow = -28415;

    public static final int kQ3ErrorDisplayNotSet = -28414;

    public static final int kQ3ErrorCameraNotSet = -28413;

    public static final int kQ3ErrorDrawContextNotSet = -28412;

    public static final int kQ3ErrorNonInvertibleMatrix = -28411;

    public static final int kQ3ErrorRenderingNotStarted = -28410;

    public static final int kQ3ErrorPickingNotStarted = -28409;

    public static final int kQ3ErrorBoundsNotStarted = -28408;

    public static final int kQ3ErrorDataNotAvailable = -28407;

    public static final int kQ3ErrorNothingToPop = -28406;

    public static final int kQ3ErrorUnknownStudioType = -28405;

    public static final int kQ3ErrorAlreadyRendering = -28404;

    public static final int kQ3ErrorStartGroupRange = -28403;

    public static final int kQ3ErrorUnsupportedGeometryType = -28402;

    public static final int kQ3ErrorInvalidGeometryType = -28401;

    public static final int kQ3ErrorUnsupportedFunctionality = -28400;

    public static final int kQ3ErrorInvalidPositionForGroup = -28399;

    public static final int kQ3ErrorInvalidObjectForGroup = -28398;

    public static final int kQ3ErrorInvalidObjectForPosition = -28397;

    public static final int kQ3ErrorScaleOfZero = -28396;

    public static final int kQ3ErrorBadStringType = -28395;

    public static final int kQ3ErrorAttributeNotContained = -28394;

    public static final int kQ3ErrorAttributeInvalidType = -28393;

    public static final int kQ3ErrorInvalidCameraValues = -28392;

    public static final int kQ3ErrorBadDrawContextType = -28391;

    public static final int kQ3ErrorBadDrawContextFlag = -28390;

    public static final int kQ3ErrorBadDrawContext = -28389;

    public static final int kQ3ErrorUnsupportedPixelDepth = -28388;

    public static final int kQ3ErrorController = -28387;

    public static final int kQ3ErrorTracker = -28386;

    public static final int kQ3ErrorWin32Error = -28385;

    public static final int kQ3ErrorTypeAlreadyExistsAndHasSubclasses = -28384;

    public static final int kQ3ErrorTypeAlreadyExistsAndOtherClassesDependOnIt = -28383;

    public static final int kQ3ErrorTypeAlreadyExistsAndHasObjectInstances = -28382;

    public static final int kQ3ErrorPickingLoopFailed = -28381;

    public static final int kQ3ErrorRenderingLoopFailed = -28380;

    public static final int kQ3ErrorWritingLoopFailed = -28379;

    public static final int kQ3ErrorBoundingLoopFailed = -28378;

    public static final int kQ3WarningNone = 0;

    public static final int kQ3WarningInternalException = -28300;

    public static final int kQ3WarningNoObjectSupportForDuplicateMethod = -28299;

    public static final int kQ3WarningNoObjectSupportForDrawMethod = -28298;

    public static final int kQ3WarningNoObjectSupportForWriteMethod = -28297;

    public static final int kQ3WarningNoObjectSupportForReadMethod = -28296;

    public static final int kQ3WarningUnknownElementType = -28295;

    public static final int kQ3WarningTypeAndMethodAlreadyDefined = -28294;

    public static final int kQ3WarningTypeIsOutOfRange = -28293;

    public static final int kQ3WarningTypeHasNotBeenRegistered = -28292;

    public static final int kQ3WarningVector3DNotUnitLength = -28291;

    public static final int kQ3WarningInvalidSubObjectForObject = -28290;

    public static final int kQ3WarningInvalidHexString = -28289;

    public static final int kQ3WarningUnknownObject = -28288;

    public static final int kQ3WarningInvalidMetafileObject = -28287;

    public static final int kQ3WarningUnmatchedBeginGroup = -28286;

    public static final int kQ3WarningUnmatchedEndGroup = -28285;

    public static final int kQ3WarningInvalidTableOfContents = -28284;

    public static final int kQ3WarningUnresolvableReference = -28283;

    public static final int kQ3WarningNoAttachMethod = -28282;

    public static final int kQ3WarningInconsistentData = -28281;

    public static final int kQ3WarningReadLessThanSize = -28280;

    public static final int kQ3WarningFilePointerResolutionFailed = -28279;

    public static final int kQ3WarningFilePointerRedefined = -28278;

    public static final int kQ3WarningStringExceedsMaximumLength = -28277;

    public static final int kQ3WarningLowMemory = -28276;

    public static final int kQ3WarningPossibleMemoryLeak = -28275;

    public static final int kQ3WarningViewTraversalInProgress = -28274;

    public static final int kQ3WarningNonInvertibleMatrix = -28273;

    public static final int kQ3WarningQuaternionEntriesAreZero = -28272;

    public static final int kQ3WarningFunctionalityNotSupported = -28271;

    public static final int kQ3WarningInvalidPaneDimensions = -28270;

    public static final int kQ3WarningPickParamOutside = -28269;

    public static final int kQ3WarningScaleEntriesAllZero = -28268;

    public static final int kQ3WarningScaleContainsNegativeEntries = -28267;

    public static final int kQ3WarningParameterOutOfRange = -28266;

    public static final int kQ3WarningExtensionNotLoading = -28265;

    public static final int kQ3WarningTypeAlreadyRegistered = -28264;

    public static final int kQ3WarningTypeSameVersionAlreadyRegistered = -28263;

    public static final int kQ3WarningTypeNewerVersionAlreadyRegistered = -28262;

    public static final int kQ3WarningInvalidObjectInGroupMetafile = -28261;

    public static final int kQ3NoticeDataAlreadyEmpty = -28100;

    public static final int kQ3NoticeMethodNotSupported = -28099;

    public static final int kQ3NoticeObjectAlreadySet = -28098;

    public static final int kQ3NoticeParameterOutOfRange = -28097;

    public static final int kQ3NoticeFileAliasWasChanged = -28096;

    public static final int kQ3NoticeMeshVertexHasNoComponent = -28095;

    public static final int kQ3NoticeMeshInvalidVertexFacePair = -28094;

    public static final int kQ3NoticeMeshEdgeVertexDoNotCorrespond = -28093;

    public static final int kQ3NoticeMeshEdgeIsNotBoundary = -28092;

    public static final int kQ3NoticeDrawContextNotSetUsingInternalDefaults = -28091;

    public static final int kQ3NoticeInvalidAttenuationTypeUsingInternalDefaults = -28090;

    public static final int kQ3NoticeBrightnessGreaterThanOne = -28089;

    public static final int kQ3NoticeScaleContainsZeroEntries = -28088;

    public static final int kQ3NoticeSystemAlreadyInitialized = -28087;

    public static final int kQ3NoticeViewSyncCalledAgain = -28086;

    public static final int kQ3NoticeFileCancelled = -28085;

    public static final int notAQTVRMovieErr = -30540;

    public static final int constraintReachedErr = -30541;

    public static final int callNotSupportedByNodeErr = -30542;

    public static final int selectorNotSupportedByNodeErr = -30543;

    public static final int invalidNodeIDErr = -30544;

    public static final int invalidViewStateErr = -30545;

    public static final int timeNotInViewErr = -30546;

    public static final int propertyNotSupportedByNodeErr = -30547;

    public static final int settingNotSupportedByNodeErr = -30548;

    public static final int limitReachedErr = -30549;

    public static final int invalidNodeFormatErr = -30550;

    public static final int invalidHotSpotIDErr = -30551;

    public static final int noMemoryNodeFailedInitialize = -30552;

    public static final int streamingNodeNotReadyErr = -30553;

    public static final int qtvrLibraryLoadErr = -30554;

    public static final int qtvrUninitialized = -30555;

    /**
     * Component errors
     */
    public static final int badComponentInstance = -2147450879;

    /**
     * Component errors
     */
    public static final int badComponentSelector = -2147450878;

    /**
     * Music Errors
     */
    public static final int notImplementedMusicErr = -2147420183;

    /**
     * Music Errors
     */
    public static final int cantSendToSynthesizerErr = -2147420184;

    /**
     * Music Errors
     */
    public static final int cantReceiveFromSynthesizerErr = -2147420185;

    /**
     * Music Errors
     */
    public static final int illegalVoiceAllocationErr = -2147420186;

    /**
     * Music Errors
     */
    public static final int illegalPartErr = -2147420187;

    /**
     * Music Errors
     */
    public static final int illegalChannelErr = -2147420188;

    /**
     * Music Errors
     */
    public static final int illegalKnobErr = -2147420189;

    /**
     * Music Errors
     */
    public static final int illegalKnobValueErr = -2147420190;

    /**
     * Music Errors
     */
    public static final int illegalInstrumentErr = -2147420191;

    /**
     * Music Errors
     */
    public static final int illegalControllerErr = -2147420192;

    /**
     * Music Errors
     */
    public static final int midiManagerAbsentErr = -2147420193;

    /**
     * Music Errors
     */
    public static final int synthesizerNotRespondingErr = -2147420194;

    /**
     * Music Errors
     */
    public static final int synthesizerErr = -2147420195;

    /**
     * Music Errors
     */
    public static final int illegalNoteChannelErr = -2147420196;

    /**
     * Music Errors
     */
    public static final int noteChannelNotAllocatedErr = -2147420197;

    /**
     * Music Errors
     */
    public static final int tunePlayerFullErr = -2147420198;

    /**
     * Music Errors
     */
    public static final int tuneParseErr = -2147420199;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/Errors.
 * class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
