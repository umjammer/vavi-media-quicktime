package quicktime.std;

/**
 * This class represents all of the new constants in the QuickTime interface for version 5.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public abstract interface StdQTConstants5 {

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextAlbum = -1453233054;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextArtist = -1455336876;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextComposer = -1453101203;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextEncodedBy = -1452970397;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextGenre = -1452841618;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextOriginalArtist = -1452314523;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextTrack = -1451986325;

    /**
     * QuickTime 5 user data types
     */
    public static final int kUserDataTextURLLink = -1451920788;

    /**
     * QuickTime 5 datahandler type
     */
    public static final int pointerDataHandlerSubType = 1886679584;

    /**
     * QuickTime 5 datahandler type
     */
    public static final int nullDataHandlerSubType = 1853189228;

    public static final int handlerCanPicSave = 8192;

    public static final int videoFlagDontLeanAhead = 1;

    /**
     * QuickTime 5 media task flag
     */
    public static final int mPrecompositeOnly = 512;

    /**
     * QuickTime 5 media task flag
     */
    public static final int mSoundOnly = 1024;

    /**
     * QuickTime 5 media task flag
     */
    public static final int mDoIdleActionsBeforeDraws = 2048;

    /**
     * QuickTime 5 media task flag
     */
    public static final int mDisableIdleActions = 4096;

    /**
     * QuickTime 5 media task flag
     */
    public static final int mIgnorePortOrigin = 8192;

    /**
     * QuickTime 5 media task flag
     */
    public static final int mWantIdleActions = 32;

    /**
     * QuickTime 5 for MediaGetInfo() and MediaSetInfo()
     */
    public static final int kMHInfoEncodedFrameRate = 1869767028;

    /**
     * QuickTime 5 text media constants (displayFlags for TextProc)
     */
    public static final int txtProcDefaultDisplay = 0;

    /**
     * QuickTime 5 text media constants (displayFlags for TextProc)
     */
    public static final int txtProcDontDisplay = 1;

    /**
     * QuickTime 5 text media constants (displayFlags for TextProc)
     */
    public static final int txtProcDoDisplay = 2;

    /**
     * QuickTime 5 network status
     */
    public static final int kQTNetworkStatusNoNetwork = -2;

    /**
     * QuickTime 5 network status
     */
    public static final int kQTNetworkStatusUncertain = -1;

    /**
     * QuickTime 5 network status
     */
    public static final int kQTNetworkStatusNotConnected = 0;

    /**
     * QuickTime 5 network status
     */
    public static final int kQTNetworkStatusConnected = 1;

    public static final int kTextSelection = 4;

    public static final int kTextRelativeScroll = 15;

    public static final int kTextHyperTextFace = 16;

    public static final int kTextHyperTextColor = 17;

    public static final int kTextKeyEntry = 18;

    public static final int kTextMouseDown = 19;

    public static final int kTextTextBox = 20;

    public static final int kTextEditState = 21;

    public static final int kTextLength = 22;

    public static final int kTextStartSelection = 22;

    public static final int kTextEndSelection = 23;

    public static final int kTextTextBoxLeft = 24;

    public static final int kTextTextBoxTop = 25;

    public static final int kTextTextBoxRight = 26;

    public static final int kTextTextBoxBottom = 27;

    /**
     * QuickTime 5 interactive sprite support QTEventRecord flag
     */
    public static final int kQTEventPayloadIsQTList = 1;

    /**
     * QuickTime 5 Track action constant
     */
    public static final int kActionTrackSetIdleFrequency = 2056;

    /**
     * QuickTime 5 Track action constant
     */
    public static final int kActionTrackSetBassTreble = 2057;

    /**
     * QuickTime 5 Sprite action constant
     */
    public static final int kActionSpriteExchangeLists = 3084;

    /**
     * QuickTime 5 QTVR action constant
     */
    public static final int kActionQTVREnableHotSpot = 4101;

    /**
     * QuickTime 5 QTVR action constant
     */
    public static final int kActionQTVRShowHotSpots = 4102;

    /**
     * QuickTime 5 QTVR action constant
     */
    public static final int kActionQTVRTranslateObject = 4103;

    /**
     * QuickTime 5 System action constant
     */
    public static final int kActionDoCompressedActions = 6159;

    /**
     * QuickTime 5 System action constant
     */
    public static final int kActionSendAppMessage = 6160;

    /**
     * QuickTime 5 System action constant
     */
    public static final int kActionLoadComponent = 6161;

    /**
     * QuickTime 5 System action constant
     */
    public static final int kActionSetFocus = 6162;

    /**
     * QuickTime 5 System action constant
     */
    public static final int kActionDontPassKeyEvent = 6163;

    /**
     * QuickTime 5 FlashTrack action constant
     */
    public static final int kActionFlashTrackSetFlashVariable = 10245;

    /**
     * QuickTime 5 FlashTrack action constant
     */
    public static final int kActionFlashTrackDoButtonActions = 10246;

    /**
     * QuickTime 5 MovieTrack action constant
     */
    public static final int kActionMovieTrackLoadChildMovieWithQTListParams = 11266;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackPasteText = 12288;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetTextBox = 12291;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetTextStyle = 12292;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetSelection = 12293;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetBackgroundColor = 12294;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetForegroundColor = 12295;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetFace = 12296;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetFont = 12297;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetSize = 12298;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetAlignment = 12299;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetHilite = 12300;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetDropShadow = 12301;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetDisplayFlags = 12302;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetScroll = 12303;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackRelativeScroll = 12304;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackFindText = 12305;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetHyperTextFace = 12306;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetHyperTextColor = 12307;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackKeyEntry = 12308;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackMouseDown = 12309;

    /**
     * QuickTime 5 TextTrack action constant
     */
    public static final int kActionTextTrackSetEditable = 12310;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListAddElement = 13312;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListRemoveElements = 13313;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListSetElementValue = 13314;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListPasteFromXML = 13315;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListSetMatchingFromXML = 13316;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListSetFromURL = 13317;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListExchangeLists = 13318;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    public static final int kActionListServerQuery = 13319;

    /**
     * QuickTime 5 action range
     */
    public static final int kFirstTextTrackAction = 12288;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastTextTrackAction = 12310;

    /**
     * QuickTime 5 action range
     */
    public static final int kFirstMultiTargetAction = 13312;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastMultiTargetAction = 13319;

    /**
     * QuickTime 5 action range
     */
    public static final int kLastAction5 = 13319;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandEventParameter = 26;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandFreeMemory = 27;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandNetworkStatus = 28;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandQuickTimeVersionRegistered = 29;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandSystemVersion = 30;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieDuration = 1029;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieTimeScale = 1030;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieWidth = 1031;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieHeight = 1032;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieLoadState = 1033;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieTrackCount = 1034;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieIsActive = 1035;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieName = 1036;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandMovieID = 1037;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTrackDuration = 2054;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTrackName = 2055;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTrackID = 2056;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTrackIdleFrequency = 2057;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTrackBass = 2058;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTrackTreble = 2059;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandSpriteName = 3095;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandQTVRHotSpotsVisible = 4100;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandQTVRViewCenterH = 4101;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandQTVRViewCenterV = 4102;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandCanHaveFocus = 5124;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandHasFocus = 5125;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandSpriteTrackSpriteIDAtPoint = 3094;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackEditable = 6144;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackCopyText = 6145;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackStartSelection = 6146;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackEndSelection = 6147;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackTextBoxLeft = 6148;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackTextBoxTop = 6149;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackTextBoxRight = 6150;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackTextBoxBottom = 6151;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTextTrackTextLength = 6152;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandListCountElements = 7168;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandListGetElementPathByIndex = 7169;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandListGetElementValue = 7170;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandListCopyToXML = 7171;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandSin = 8192;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandCos = 8193;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandTan = 8194;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandATan = 8195;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandATan2 = 8196;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandDegreesToRadians = 8197;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandRadiansToDegrees = 8198;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandSquareRoot = 8199;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandExponent = 8200;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandLog = 8201;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandFlashTrackVariable = 9216;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandStringLength = 10240;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandStringCompare = 10241;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandStringSubString = 10242;

    /**
     * QuickTime 5 operand constant
     */
    public static final int kOperandStringConcat = 10243;

    /**
     * QuickTime 5 target atom type
     */
    public static final int kTargetCurrentQTEventParams = 1702260833;

    /**
     * QuickTime 5 QTEvent type
     */
    public static final int kQTEventListReceived = 1818850164;

    /**
     * QuickTime 5 flag for scriptTypeFlags field of the QTDoScriptRecord
     */
    public static final int kScriptIsAppleScript = 32;

    public static final int kMovieAnchorDataRefIsDefault = 1;

    public static final int hintsNoRenderingTimeOut = 2097152;

    /**
     * flags for GetMovieImporterForDataRef
     */
    public static final int kGetMovieImporterAutoImportOnly = 1024;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTRealTimeSharedPriority = 25;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTBandwidthNotifyNeedToStop = 1;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTBandwidthNotifyGoodToGo = 2;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTBandwidthChangeRequest = 4;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTBandwidthQueueRequest = 8;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTBandwidthScheduledRequest = 16;

    /**
     * QuickTime 5 bandwidth
     */
    public static final int kQTBandwidthVoluntaryRelease = 32;

    public static final int kQTCloneShareSamples = 1;

    public static final int kQTCloneDontCopyEdits = 2;

    /**
     * QuickTime 5 movie load state
     */
    public static final int kMovieLoadStateLoading = 1000;

    /**
     * QuickTime 5 movie load state
     */
    public static final int kMovieLoadStatePlayable = 10000;

    /**
     * QuickTime 5 movie load state
     */
    public static final int kMovieLoadStatePlaythroughOK = 20000;

    /**
     * QuickTime 5 movie load state
     */
    public static final int kMovieLoadStateComplete = 100000;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    public static final int kQTDontUseDataToFindImporter = 1;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    public static final int kQTDontLookForMovieImporterIfGraphicsImporterFound = 2;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    public static final int kQTAllowOpeningStillImagesAsMovies = 4;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    public static final int kQTAllowImportersThatWouldCreateNewFile = 8;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    public static final int kQTAllowAggressiveImporters = 16;

    /**
     * QuickTime 5 QTList element type
     */
    public static final int kQTListElementTypeEmpty = 1701671028;

    /**
     * QuickTime 5 QTList element type
     */
    public static final int kQTListElementTypeFloat = 1718382433;

    /**
     * QuickTime 5 QTList element type
     */
    public static final int kQTListElementTypeString = 1937011232;

    /**
     * QuickTime 5 QTList element type
     */
    public static final int kQTListElementTypeList = 1818850164;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    public static final int kQTParseTextHREFText = 1952807028;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    public static final int kQTParseTextHREFBaseURL = 1651864172;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    public static final int kQTParseTextHREFClickPoint = 1668049259;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    public static final int kQTParseTextHREFUseAltDelim = 1634497636;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    public static final int kQTParseTextHREFDelimiter = 1684368493;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    public static final int kQTParseTextHREFRecomposeHREF = 1919447654;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFURL = 1970433056;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFTarget = 1952543335;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFChapter = 1667785072;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFIsAutoHREF = 1635087471;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFIsServerMap = 1936548208;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFHREF = 1752327526;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    public static final int kQTParseTextHREFEMBEDArgs = 1835165028;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionSetVolumeStep = 82;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionAutoPlay = 83;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionPauseToBuffer = 84;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionAppMessageReceived = 85;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionMovieFinished = 86;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionEvaluateExpressionWithType = 89;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionGetMovieName = 90;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionGetMovieID = 91;

    /**
     * QuickTime 5 mcAction
     */
    public static final short mcActionGetMovieActive = 92;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    public static final int kQTAppMessageSoftwareChanged = 1;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    public static final int kQTAppMessageWindowCloseRequested = 3;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    public static final int kQTAppMessageExitFullScreenRequested = 4;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    public static final int kQTAppMessageDisplayChannels = 5;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    public static final int kQTAppMessageEnterFullScreenRequested = 6;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsBooleanPtr = 1;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsShortPtr = 2;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsLongPtr = 3;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsMatrixRecordPtr = 4;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsModifierTrackGraphicsModeRecord = 5;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsHandle = 6;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsStr255 = 7;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsFloatPtr = 8;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsPointPtr = 9;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsNewAtomContainer = 10;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsQTEventRecordPtr = 11;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsFixedPtr = 12;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsSetControllerValuePtr = 13;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsRgnHandle = 14;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsComponentDescriptionPtr = 15;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    public static final int kFetchAsCString = 16;

    /**
     * QuickTime 5 sample format atom
     */
    public static final int kMovieMediaPrerollTime = 1886545266;

    public static final int kICMSequenceTaskWeight = 1953981801;

    public static final int kICMSequenceTaskName = 1953390957;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    public static final int kQTPhotoshopLayerMode = 1819111268;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    public static final int kQTPhotoshopLayerOpacity = 1819242593;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    public static final int kQTPhotoshopLayerClipping = 1818455152;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    public static final int kQTPhotoshopLayerFlags = 1818651751;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    public static final int kQTPhotoshopLayerName = -1452511635;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    public static final int kQTPhotoshopLayerUnicodeName = 1819635305;

    /**
     * QuickTime 5 XML Parser component
     */
    public static final int xmlParseComponentType = 1885434483;

    /**
     * QuickTime 5 XML Parser component
     */
    public static final int xmlParseComponentSubType = 2020437024;

    /**
     * QuickTime 5 XML namespace
     */
    public static final int nameSpaceIDNone = 0;

    /**
     * QuickTime 5 XML identifier
     */
    public static final int xmlIdentifierInvalid = 0;

    /**
     * QuickTime 5 XML identifier
     */
    public static final int xmlIdentifierUnrecognized = -1;

    /**
     * QuickTime 5 XML element flag
     */
    public static final int elementFlagAlwaysSelfContained = 1;

    /**
     * QuickTime 5 XML element flag
     */
    public static final int elementFlagPreserveWhiteSpace = 2;

    /**
     * QuickTime 5 XML parse flag
     */
    public static final int xmlParseFlagAllowUppercase = 1;

    /**
     * QuickTime 5 XML parse flag
     */
    public static final int xmlParseFlagAllowUnquotedAttributeValues = 2;

    /**
     * QuickTime 5 XML parse flag
     */
    public static final int xmlParseFlagEventParseOnly = 4;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindCharString = 0;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindInteger = 1;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindPercent = 2;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindBoolean = 4;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindOnOff = 8;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindColor = 16;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindEnum = 32;

    /**
     * QuickTime 5 XML attribute kind
     */
    public static final int attributeValueKindCaseSensEnum = 64;

    /**
     * QuickTime 5 XML content kind
     */
    public static final int xmlContentTypeInvalid = 0;

    /**
     * QuickTime 5 XML content kind
     */
    public static final int xmlContentTypeElement = 1;

    /**
     * QuickTime 5 XML content kind
     */
    public static final int xmlContentTypeCharData = 2;

    public static final int kQTGetMIMETypeInfoIsQuickTimeMovieType = 1836019574;

    public static final int kQTGetMIMETypeInfoIsUnhelpfulType = 1685417314;

    public static final int kTextMediaGetTextPropertySelect = 269;

    public static final int kFlashMediaGetFlashVariableSelect = 266;

    public static final int kFlashMediaSetFlashVariableSelect = 267;

    public static final int kFlashMediaDoButtonActionsSelect = 268;

    public static final int kFlashMediaGetSupportedSwfVersionSelect = 269;

    public static final int kMCAddMovieSegmentSelect = 51;

    public static final int kMCTrimMovieSegmentSelect = 52;

    public static final int codecUsesOverlaySurface = 1;

    public static final int codecImageBufferIsOverlaySurface = 2;

    public static final int codecSrcMustBeImageBuffer = 4;

    public static final int codecImageBufferIsInAGPMemory = 16;

    public static final int codecImageBufferIsInPCIMemory = 32;

    /**
     * QuickTime 5 higher level purpose of a parameter or set of parameters
     */
    public static final int kParameterUsageCircularDegrees = 1684367203;

    /**
     * QuickTime 5 higher level purpose of a parameter or set of parameters
     */
    public static final int kParameterUsageCircularRadians = 1918985315;

    public static final int kMediaQTIdleFrequencySelector = 1768187505;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * StdQTConstants5.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
