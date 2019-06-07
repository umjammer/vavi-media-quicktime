package quicktime.streaming;

/**
 * This class represents all of the new constants in the QuickTime interface for version 5.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public final class QTSConstants {

    static final int kQTSInfiniteDuration = Integer.MAX_VALUE;

    static final int kQTSUnknownDuration = 0;

    static final int kQTSNormalForwardRate = 65536;

    static final int kQTSStoppedRate = 0;

    static final int kQTSTargetBufferDurationInfo = 1651861106;

    static final int kQTSDurationInfo = 1685418593;

    static final int kQTSSoundLevelMeteringEnabledInfo = 1836348014;

    static final int kQTSSoundLevelMeterInfo = 1818588781;

    static final int kQTSSourceTrackIDInfo = 1869900132;

    static final int kQTSSourceLayerInfo = 1869379954;

    static final int kQTSSourceLanguageInfo = 1869377127;

    static final int kQTSSourceTrackFlagsInfo = 1869899372;

    static final int kQTSSourceDimensionsInfo = 1868851565;

    static final int kQTSSourceVolumesInfo = 1870032748;

    static final int kQTSSourceMatrixInfo = 1869439348;

    static final int kQTSSourceClipRectInfo = 1868786800;

    static final int kQTSSourceGraphicsModeInfo = 1869050477;

    static final int kQTSSourceScaleInfo = 1869833068;

    static final int kQTSSourceBoundingRectInfo = 1869767540;

    static final int kQTSSourceUserDataInfo = 1869964404;

    static final int kQTSSourceInputMapInfo = 1869180272;

    static final int kQTSInfo_DataProc = 1684108400;

    static final int kQTSInfo_SendDataExtras = 1684371572;

    static final int kQTSInfo_HintTrackID = 1752459620;

    static final int kQTSStatisticsInfo = 1937006964;

    static final int kQTSMinStatusDimensionsInfo = 1836282980;

    static final int kQTSNormalStatusDimensionsInfo = 1853060196;

    static final int kQTSNetworkIDStringInfo = 1852400755;

    static final int kQTSTotalDataRateInfo = 1685222516;

    static final int kQTSTotalDataRateInInfo = 1685222505;

    static final int kQTSTotalDataRateOutInfo = 1685222511;

    static final int kQTSLostPercentInfo = 1819304820;

    static final int kQTSNumViewersInfo = 1853254007;

    static final int kQTSMediaTypeInfo = 1836349808;

    static final int kQTSNameInfo = 1851878757;

    static final int kQTSCanHandleSendDataType = 1667789668;

    static final int kQTSAnnotationsInfo = 1835365473;

    static final int kQTSRemainingBufferTimeInfo = 1651797363;

    static final int kQTSInfo_SettingsText = 1937011832;

    static final int kQTSTargetBufferDurationTimeScale = 1000;

    static final int kQTSSampleDataProcParamsVersion1 = 1;

    static final int kQTSMustDraw = 8;

    static final int kQTSAtEnd = 16;

    static final int kQTSPreflightDraw = 32;

    static final int kQTSSyncDrawing = 64;

    static final int kQTSSupportsPerStreamControlCharacteristic = 1886610292;

    static final int kQTSDidDraw = 1;

    static final int kQTSNeedsToDraw = 4;

    static final int kQTSDrawAgain = 8;

    static final int kQTSPartialDraw = 16;

    public static final int kQTSNullNotification = 1853189228;

    public static final int kQTSErrorNotification = 1701999136;

    public static final int kQTSNewPresDetectedNotification = 1852143472;

    public static final int kQTSPresBeginChangingNotification = 1886544738;

    public static final int kQTSPresDoneChangingNotification = 1886544740;

    public static final int kQTSPresentationChangedNotification = 1886544744;

    public static final int kQTSNewStreamNotification = 1937010295;

    public static final int kQTSStreamBeginChangingNotification = 1937007458;

    public static final int kQTSStreamDoneChangingNotification = 1937007460;

    public static final int kQTSStreamChangedNotification = 1937007464;

    public static final int kQTSStreamGoneNotification = 1937008494;

    public static final int kQTSPreviewAckNotification = 1886806379;

    public static final int kQTSPrerollAckNotification = 1885430635;

    public static final int kQTSStartAckNotification = 1935762283;

    public static final int kQTSStopAckNotification = 2019648363;

    public static final int kQTSStatusNotification = 1937006964;

    public static final int kQTSURLNotification = 1970433056;

    public static final int kQTSDurationNotification = 1685418593;

    public static final int kQTSNewPresentationNotification = 1852863091;

    public static final int kQTSPresentationGoneNotification = 2020635251;

    public static final int kQTSPresentationDoneNotification = 1885630318;

    public static final int kQTSBandwidthAlertNotification = 1651990892;

    public static final int kQTSAnnotationsChangedNotification = 1835365473;

    public static final int kQTSInvalidPresentation = 0;

    public static final int kQTSAllPresentations = 0;

    public static final int kQTSInvalidStream = 0;

    public static final int kQTSAllStreams = 0;

    public static final int kQTSFatalErrorFlag = 1;

    public static final int kQTSAutoModeFlag = 1;

    public static final int kQTSDontShowStatusFlag = 8;

    public static final int kQTSSendMediaFlag = 65536;

    public static final int kQTSReceiveMediaFlag = 131072;

    static final int kQTSExportFlag_ShowDialog = 1;

    static final int kQTSPresParamsVersion1 = 1;

    static final int kQTSExportParamsVersion1 = 1;

    public static final int kQTSAllStatisticsType = 1634495520;

    public static final int kQTSShortStatisticsType = 1936224884;

    public static final int kQTSSummaryStatisticsType = 1937075565;

    public static final int kQTSGetNameStatisticsFlag = 1;

    public static final int kQTSDontGetDataStatisticsFlag = 2;

    public static final int kQTSUpdateAtomsStatisticsFlag = 4;

    public static final int kQTSGetUnitsStatisticsFlag = 8;

    public static final int kQTSUpdateAllIfNecessaryStatisticsFlag = 65536;

    public static final int kQTSStatisticsStreamAtomType = 1937011309;

    public static final int kQTSStatisticsNameAtomType = 1851878757;

    public static final int kQTSStatisticsDataFormatAtomType = 1718775156;

    public static final int kQTSStatisticsDataAtomType = 1684108385;

    public static final int kQTSStatisticsUnitsAtomType = 1970170228;

    public static final int kQTSStatisticsUnitsNameAtomType = 1970170222;

    public static final int kQTSStatisticsSInt32DataFormat = 1936274226;

    public static final int kQTSStatisticsUInt32DataFormat = 1969828658;

    public static final int kQTSStatisticsSInt16DataFormat = 1936273718;

    public static final int kQTSStatisticsUInt16DataFormat = 1969828150;

    public static final int kQTSStatisticsFixedDataFormat = 1718188132;

    public static final int kQTSStatisticsUnsignedFixedDataFormat = 1969645944;

    public static final int kQTSStatisticsStringDataFormat = 1937011303;

    public static final int kQTSStatisticsOSTypeDataFormat = 1869837424;

    public static final int kQTSStatisticsRectDataFormat = 1919247220;

    public static final int kQTSStatisticsPointDataFormat = 1886350964;

    public static final int kQTSStatisticsNoUnitsType = 0;

    public static final int kQTSStatisticsPercentUnitsType = 1885564532;

    public static final int kQTSStatisticsBitsPerSecUnitsType = 1651536672;

    public static final int kQTSStatisticsFramesPerSecUnitsType = 1718645536;

    public static final int kQTSTotalDataRateStat = 1685222516;

    public static final int kQTSTotalDataRateInStat = 1685222505;

    public static final int kQTSTotalDataRateOutStat = 1685222511;

    public static final int kQTSNetworkIDStringStat = 1852400755;

    public static final int kQTSAtomType_Presentation = 1886545267;

    public static final int kQTSAtomType_PresentationHeader = 1885889650;

    public static final int kQTSAtomType_MediaStream = 1836282994;

    public static final int kQTSAtomType_MediaStreamHeader = 1836279908;

    public static final int kQTSAtomType_MediaDescriptionText = 1835296115;

    public static final int kQTSAtomType_ClipRect = 1668049264;

    public static final int kQTSAtomType_Duration = 1685418593;

    public static final int kQTSAtomType_BufferTime = 1651861106;

    public static final int kQTSAtomType_Settings = 1936028788;

    public static final int kQTSAtomType_Bandwidth = 1651991668;

    public static final int kQTSAtomType_Attribute = 1635021938;

    public static final int kQTSAtomType_AttributeName = 1635021422;

    public static final int kQTSAtomType_AttributeValue = 1635021430;

    public static final int kQTSAtomType_TransportSettings = 1953653102;

    public static final int kQTSAtomType_Sourcer = 1936876402;

    public static final int kQTSAtomType_SourcerType = 1936876404;

    public static final int kQTSSourcerType = 1936876402;

    public static final int kQTSSGChannelSourcerType = 1936155496;

    public static final int kQTSMovieTrackSourcerType = 1953653099;

    public static final int kQTSPushDataSourcerType = 1886745448;

    public static final int kQTSSourcerInitParamsVersion1 = 1;

    public static final int kQTSLoopParamsVersion1 = 1;

    public static final int kQTSTrackParamsVersion1 = 1;

    public static final int kQTSSourcerTimingParamsVersion1 = 1;

    public static final int kQTSPushDataParamsVersion1 = 1;

    public static final int kQTSSourcerCallbackProcParamsVersion1 = 1;

    public static final int kQTSInfo_Track = 1953653099;

    public static final int kQTSInfo_Loop = 1819242352;

    public static final int kQTSInfo_SourcerTiming = 1937009005;

    public static final int kQTSInfo_TargetFrameRate = 1952870515;

    public static final int kQTSInfo_PushData = 1886745448;

    public static final int kQTSInfo_SourcerCallbackProc = 1935893104;

    public static final int kQTSInfo_TargetDataRate = 1952739956;

    public static final int kQTSInfo_AudioAutoGainOnOff = 1634165536;

    public static final int kQTSInfo_AudioGain = 1734437230;

    public static final int kQTSInfo_CroppedInputRect = 1668444274;

    public static final int kQTSInfo_SpatialSettings = 1936749676;

    public static final int kQTSInfo_TemporalSettings = 1953526380;

    public static final int kQTSInfo_DataRateSettings = 1685217652;

    public static final int kQTSInfo_CodecFlags = 1667656807;

    public static final int kQTSInfo_CodecSettings = 1667523939;

    public static final int kQTSInfo_ForceKeyValue = 1802725741;

    public static final int kQTSInfo_SoundSampleRate = 1936945780;

    public static final int kQTSInfo_SoundSampleSize = 1936946035;

    public static final int kQTSInfo_SoundChannelCount = 1936941923;

    public static final int kQTSInfo_SoundCompression = 1936941940;

    public static final int kQTSInfo_CompressionList = 1668577644;

    public static final int kQTSInfo_VideoHue = 1752524064;

    public static final int kQTSInfo_VideoSaturation = 1935766642;

    public static final int kQTSInfo_VideoContrast = 1953657716;

    public static final int kQTSInfo_VideoBrightness = 1651665268;

    public static final int kQTSInfo_VideoSharpness = 1936224880;

    public static final int kQTSInfo_TimeScale = 1935892844;

    public static final int kQTSInfo_SGChannel = 1936155496;

    public static final int kQTSInfo_InputDeviceName = 1768844909;

    public static final int kQTSInfo_InputSourceName = 1936879213;

    public static final int kQTSInfo_FullInputRect = 1718971506;

    public static final int kQTSLoopFlag_Loop = 1;

    public static final int kQTSSourcerDataFlag_SyncSample = 1;

    public static final int kQTSPushDataFlag_SampleTimeIsValid = 1;

    public static final int kQTSPushDataFlag_DurationIsValid = 2;

    public static final int kQTSSourcerCallback_Done = 1685024357;

    public static final int kQTSPushDataSourcerCallback_HasCharacteristic = 1293;

    public static final int kQTSPushDataSourcerCallback_SetInfo = 1287;

    public static final int kQTSPushDataSourcerCallback_GetInfo = 1288;

    public static final int kQTSSourcerInitializeSelect = 1280;

    public static final int kQTSSourcerSetEnableSelect = 1283;

    public static final int kQTSSourcerGetEnableSelect = 1284;

    public static final int kQTSSourcerSetInfoSelect = 1287;

    public static final int kQTSSourcerGetInfoSelect = 1288;

    public static final int kQTSSourcerSetTimeScaleSelect = 1294;

    public static final int kQTSSourcerGetTimeScaleSelect = 1295;

    public static final int kQTSSourcerIdleSelect = 1302;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /QTSConstants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
