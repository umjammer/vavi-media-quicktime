package quicktime.std;

/**
 * This class represents all of the new constants in the QuickTime interface for version 5.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public interface StdQTConstants5 {

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextAlbum = -1453233054;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextArtist = -1455336876;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextComposer = -1453101203;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextEncodedBy = -1452970397;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextGenre = -1452841618;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextOriginalArtist = -1452314523;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextTrack = -1451986325;

    /**
     * QuickTime 5 user data types
     */
    int kUserDataTextURLLink = -1451920788;

    /**
     * QuickTime 5 datahandler type
     */
    int pointerDataHandlerSubType = 1886679584;

    /**
     * QuickTime 5 datahandler type
     */
    int nullDataHandlerSubType = 1853189228;

    int handlerCanPicSave = 8192;

    int videoFlagDontLeanAhead = 1;

    /**
     * QuickTime 5 media task flag
     */
    int mPrecompositeOnly = 512;

    /**
     * QuickTime 5 media task flag
     */
    int mSoundOnly = 1024;

    /**
     * QuickTime 5 media task flag
     */
    int mDoIdleActionsBeforeDraws = 2048;

    /**
     * QuickTime 5 media task flag
     */
    int mDisableIdleActions = 4096;

    /**
     * QuickTime 5 media task flag
     */
    int mIgnorePortOrigin = 8192;

    /**
     * QuickTime 5 media task flag
     */
    int mWantIdleActions = 32;

    /**
     * QuickTime 5 for MediaGetInfo() and MediaSetInfo()
     */
    int kMHInfoEncodedFrameRate = 1869767028;

    /**
     * QuickTime 5 text media constants (displayFlags for TextProc)
     */
    int txtProcDefaultDisplay = 0;

    /**
     * QuickTime 5 text media constants (displayFlags for TextProc)
     */
    int txtProcDontDisplay = 1;

    /**
     * QuickTime 5 text media constants (displayFlags for TextProc)
     */
    int txtProcDoDisplay = 2;

    /**
     * QuickTime 5 network status
     */
    int kQTNetworkStatusNoNetwork = -2;

    /**
     * QuickTime 5 network status
     */
    int kQTNetworkStatusUncertain = -1;

    /**
     * QuickTime 5 network status
     */
    int kQTNetworkStatusNotConnected = 0;

    /**
     * QuickTime 5 network status
     */
    int kQTNetworkStatusConnected = 1;

    int kTextSelection = 4;

    int kTextRelativeScroll = 15;

    int kTextHyperTextFace = 16;

    int kTextHyperTextColor = 17;

    int kTextKeyEntry = 18;

    int kTextMouseDown = 19;

    int kTextTextBox = 20;

    int kTextEditState = 21;

    int kTextLength = 22;

    int kTextStartSelection = 22;

    int kTextEndSelection = 23;

    int kTextTextBoxLeft = 24;

    int kTextTextBoxTop = 25;

    int kTextTextBoxRight = 26;

    int kTextTextBoxBottom = 27;

    /**
     * QuickTime 5 interactive sprite support QTEventRecord flag
     */
    int kQTEventPayloadIsQTList = 1;

    /**
     * QuickTime 5 Track action constant
     */
    int kActionTrackSetIdleFrequency = 2056;

    /**
     * QuickTime 5 Track action constant
     */
    int kActionTrackSetBassTreble = 2057;

    /**
     * QuickTime 5 Sprite action constant
     */
    int kActionSpriteExchangeLists = 3084;

    /**
     * QuickTime 5 QTVR action constant
     */
    int kActionQTVREnableHotSpot = 4101;

    /**
     * QuickTime 5 QTVR action constant
     */
    int kActionQTVRShowHotSpots = 4102;

    /**
     * QuickTime 5 QTVR action constant
     */
    int kActionQTVRTranslateObject = 4103;

    /**
     * QuickTime 5 System action constant
     */
    int kActionDoCompressedActions = 6159;

    /**
     * QuickTime 5 System action constant
     */
    int kActionSendAppMessage = 6160;

    /**
     * QuickTime 5 System action constant
     */
    int kActionLoadComponent = 6161;

    /**
     * QuickTime 5 System action constant
     */
    int kActionSetFocus = 6162;

    /**
     * QuickTime 5 System action constant
     */
    int kActionDontPassKeyEvent = 6163;

    /**
     * QuickTime 5 FlashTrack action constant
     */
    int kActionFlashTrackSetFlashVariable = 10245;

    /**
     * QuickTime 5 FlashTrack action constant
     */
    int kActionFlashTrackDoButtonActions = 10246;

    /**
     * QuickTime 5 MovieTrack action constant
     */
    int kActionMovieTrackLoadChildMovieWithQTListParams = 11266;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackPasteText = 12288;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetTextBox = 12291;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetTextStyle = 12292;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetSelection = 12293;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetBackgroundColor = 12294;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetForegroundColor = 12295;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetFace = 12296;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetFont = 12297;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetSize = 12298;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetAlignment = 12299;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetHilite = 12300;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetDropShadow = 12301;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetDisplayFlags = 12302;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetScroll = 12303;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackRelativeScroll = 12304;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackFindText = 12305;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetHyperTextFace = 12306;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetHyperTextColor = 12307;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackKeyEntry = 12308;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackMouseDown = 12309;

    /**
     * QuickTime 5 TextTrack action constant
     */
    int kActionTextTrackSetEditable = 12310;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListAddElement = 13312;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListRemoveElements = 13313;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListSetElementValue = 13314;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListPasteFromXML = 13315;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListSetMatchingFromXML = 13316;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListSetFromURL = 13317;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListExchangeLists = 13318;

    /**
     * QuickTime 5 MultiTarget action constant
     */
    int kActionListServerQuery = 13319;

    /**
     * QuickTime 5 action range
     */
    int kFirstTextTrackAction = 12288;

    /**
     * QuickTime 5 action range
     */
    int kLastTextTrackAction = 12310;

    /**
     * QuickTime 5 action range
     */
    int kFirstMultiTargetAction = 13312;

    /**
     * QuickTime 5 action range
     */
    int kLastMultiTargetAction = 13319;

    /**
     * QuickTime 5 action range
     */
    int kLastAction5 = 13319;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandEventParameter = 26;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandFreeMemory = 27;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandNetworkStatus = 28;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandQuickTimeVersionRegistered = 29;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandSystemVersion = 30;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieDuration = 1029;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieTimeScale = 1030;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieWidth = 1031;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieHeight = 1032;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieLoadState = 1033;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieTrackCount = 1034;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieIsActive = 1035;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieName = 1036;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandMovieID = 1037;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTrackDuration = 2054;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTrackName = 2055;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTrackID = 2056;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTrackIdleFrequency = 2057;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTrackBass = 2058;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTrackTreble = 2059;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandSpriteName = 3095;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandQTVRHotSpotsVisible = 4100;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandQTVRViewCenterH = 4101;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandQTVRViewCenterV = 4102;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandCanHaveFocus = 5124;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandHasFocus = 5125;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandSpriteTrackSpriteIDAtPoint = 3094;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackEditable = 6144;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackCopyText = 6145;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackStartSelection = 6146;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackEndSelection = 6147;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackTextBoxLeft = 6148;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackTextBoxTop = 6149;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackTextBoxRight = 6150;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackTextBoxBottom = 6151;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTextTrackTextLength = 6152;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandListCountElements = 7168;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandListGetElementPathByIndex = 7169;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandListGetElementValue = 7170;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandListCopyToXML = 7171;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandSin = 8192;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandCos = 8193;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandTan = 8194;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandATan = 8195;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandATan2 = 8196;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandDegreesToRadians = 8197;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandRadiansToDegrees = 8198;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandSquareRoot = 8199;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandExponent = 8200;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandLog = 8201;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandFlashTrackVariable = 9216;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandStringLength = 10240;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandStringCompare = 10241;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandStringSubString = 10242;

    /**
     * QuickTime 5 operand constant
     */
    int kOperandStringConcat = 10243;

    /**
     * QuickTime 5 target atom type
     */
    int kTargetCurrentQTEventParams = 1702260833;

    /**
     * QuickTime 5 QTEvent type
     */
    int kQTEventListReceived = 1818850164;

    /**
     * QuickTime 5 flag for scriptTypeFlags field of the QTDoScriptRecord
     */
    int kScriptIsAppleScript = 32;

    int kMovieAnchorDataRefIsDefault = 1;

    int hintsNoRenderingTimeOut = 2097152;

    /**
     * flags for GetMovieImporterForDataRef
     */
    int kGetMovieImporterAutoImportOnly = 1024;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTRealTimeSharedPriority = 25;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTBandwidthNotifyNeedToStop = 1;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTBandwidthNotifyGoodToGo = 2;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTBandwidthChangeRequest = 4;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTBandwidthQueueRequest = 8;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTBandwidthScheduledRequest = 16;

    /**
     * QuickTime 5 bandwidth
     */
    int kQTBandwidthVoluntaryRelease = 32;

    int kQTCloneShareSamples = 1;

    int kQTCloneDontCopyEdits = 2;

    /**
     * QuickTime 5 movie load state
     */
    int kMovieLoadStateLoading = 1000;

    /**
     * QuickTime 5 movie load state
     */
    int kMovieLoadStatePlayable = 10000;

    /**
     * QuickTime 5 movie load state
     */
    int kMovieLoadStatePlaythroughOK = 20000;

    /**
     * QuickTime 5 movie load state
     */
    int kMovieLoadStateComplete = 100000;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    int kQTDontUseDataToFindImporter = 1;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    int kQTDontLookForMovieImporterIfGraphicsImporterFound = 2;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    int kQTAllowOpeningStillImagesAsMovies = 4;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    int kQTAllowImportersThatWouldCreateNewFile = 8;

    /**
     * QuickTime 5 CanQuickTimeOpenFile/DataRef input flag
     */
    int kQTAllowAggressiveImporters = 16;

    /**
     * QuickTime 5 QTList element type
     */
    int kQTListElementTypeEmpty = 1701671028;

    /**
     * QuickTime 5 QTList element type
     */
    int kQTListElementTypeFloat = 1718382433;

    /**
     * QuickTime 5 QTList element type
     */
    int kQTListElementTypeString = 1937011232;

    /**
     * QuickTime 5 QTList element type
     */
    int kQTListElementTypeList = 1818850164;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    int kQTParseTextHREFText = 1952807028;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    int kQTParseTextHREFBaseURL = 1651864172;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    int kQTParseTextHREFClickPoint = 1668049259;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    int kQTParseTextHREFUseAltDelim = 1634497636;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    int kQTParseTextHREFDelimiter = 1684368493;

    /**
     * QuickTime 5 QTParseTextHREF inContainer atom
     */
    int kQTParseTextHREFRecomposeHREF = 1919447654;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFURL = 1970433056;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFTarget = 1952543335;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFChapter = 1667785072;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFIsAutoHREF = 1635087471;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFIsServerMap = 1936548208;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFHREF = 1752327526;

    /**
     * QuickTime 5 QTParseTextHREF outContainer atom
     */
    int kQTParseTextHREFEMBEDArgs = 1835165028;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionSetVolumeStep = 82;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionAutoPlay = 83;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionPauseToBuffer = 84;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionAppMessageReceived = 85;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionMovieFinished = 86;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionEvaluateExpressionWithType = 89;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionGetMovieName = 90;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionGetMovieID = 91;

    /**
     * QuickTime 5 mcAction
     */
    short mcActionGetMovieActive = 92;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    int kQTAppMessageSoftwareChanged = 1;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    int kQTAppMessageWindowCloseRequested = 3;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    int kQTAppMessageExitFullScreenRequested = 4;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    int kQTAppMessageDisplayChannels = 5;

    /**
     * QuickTime 5 message to the application via mcActionAppMessageReceived
     */
    int kQTAppMessageEnterFullScreenRequested = 6;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsBooleanPtr = 1;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsShortPtr = 2;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsLongPtr = 3;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsMatrixRecordPtr = 4;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsModifierTrackGraphicsModeRecord = 5;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsHandle = 6;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsStr255 = 7;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsFloatPtr = 8;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsPointPtr = 9;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsNewAtomContainer = 10;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsQTEventRecordPtr = 11;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsFixedPtr = 12;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsSetControllerValuePtr = 13;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsRgnHandle = 14;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsComponentDescriptionPtr = 15;

    /**
     * QuickTime 5 value for paramType field of QTFetchParameterAsRecord
     */
    int kFetchAsCString = 16;

    /**
     * QuickTime 5 sample format atom
     */
    int kMovieMediaPrerollTime = 1886545266;

    int kICMSequenceTaskWeight = 1953981801;

    int kICMSequenceTaskName = 1953390957;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    int kQTPhotoshopLayerMode = 1819111268;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    int kQTPhotoshopLayerOpacity = 1819242593;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    int kQTPhotoshopLayerClipping = 1818455152;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    int kQTPhotoshopLayerFlags = 1818651751;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    int kQTPhotoshopLayerName = -1452511635;

    /**
     * QuickTime 5 User data type for layers of Photoshop files
     */
    int kQTPhotoshopLayerUnicodeName = 1819635305;

    /**
     * QuickTime 5 XML Parser component
     */
    int xmlParseComponentType = 1885434483;

    /**
     * QuickTime 5 XML Parser component
     */
    int xmlParseComponentSubType = 2020437024;

    /**
     * QuickTime 5 XML namespace
     */
    int nameSpaceIDNone = 0;

    /**
     * QuickTime 5 XML identifier
     */
    int xmlIdentifierInvalid = 0;

    /**
     * QuickTime 5 XML identifier
     */
    int xmlIdentifierUnrecognized = -1;

    /**
     * QuickTime 5 XML element flag
     */
    int elementFlagAlwaysSelfContained = 1;

    /**
     * QuickTime 5 XML element flag
     */
    int elementFlagPreserveWhiteSpace = 2;

    /**
     * QuickTime 5 XML parse flag
     */
    int xmlParseFlagAllowUppercase = 1;

    /**
     * QuickTime 5 XML parse flag
     */
    int xmlParseFlagAllowUnquotedAttributeValues = 2;

    /**
     * QuickTime 5 XML parse flag
     */
    int xmlParseFlagEventParseOnly = 4;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindCharString = 0;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindInteger = 1;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindPercent = 2;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindBoolean = 4;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindOnOff = 8;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindColor = 16;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindEnum = 32;

    /**
     * QuickTime 5 XML attribute kind
     */
    int attributeValueKindCaseSensEnum = 64;

    /**
     * QuickTime 5 XML content kind
     */
    int xmlContentTypeInvalid = 0;

    /**
     * QuickTime 5 XML content kind
     */
    int xmlContentTypeElement = 1;

    /**
     * QuickTime 5 XML content kind
     */
    int xmlContentTypeCharData = 2;

    int kQTGetMIMETypeInfoIsQuickTimeMovieType = 1836019574;

    int kQTGetMIMETypeInfoIsUnhelpfulType = 1685417314;

    int kTextMediaGetTextPropertySelect = 269;

    int kFlashMediaGetFlashVariableSelect = 266;

    int kFlashMediaSetFlashVariableSelect = 267;

    int kFlashMediaDoButtonActionsSelect = 268;

    int kFlashMediaGetSupportedSwfVersionSelect = 269;

    int kMCAddMovieSegmentSelect = 51;

    int kMCTrimMovieSegmentSelect = 52;

    int codecUsesOverlaySurface = 1;

    int codecImageBufferIsOverlaySurface = 2;

    int codecSrcMustBeImageBuffer = 4;

    int codecImageBufferIsInAGPMemory = 16;

    int codecImageBufferIsInPCIMemory = 32;

    /**
     * QuickTime 5 higher level purpose of a parameter or set of parameters
     */
    int kParameterUsageCircularDegrees = 1684367203;

    /**
     * QuickTime 5 higher level purpose of a parameter or set of parameters
     */
    int kParameterUsageCircularRadians = 1918985315;

    int kMediaQTIdleFrequencySelector = 1768187505;
}

