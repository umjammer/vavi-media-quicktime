package quicktime.sound;

/**
 * This class represents all of the constants in the Sound Manager interface.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public interface SoundConstants {

    int gestaltSoundAttr = 1936614432;

    int gestaltStereoCapability = 0;

    int gestaltStereoMixing = 1;

    int gestaltSoundIOMgrPresent = 3;

    int gestaltBuiltInSoundInput = 4;

    int gestaltHasSoundInputDevice = 5;

    int gestaltPlayAndRecord = 6;

    int gestalt16BitSoundIO = 7;

    int gestaltStereoInput = 8;

    int gestaltLineLevelInput = 9;

    int gestaltSndPlayDoubleBuffer = 10;

    int gestaltMultiChannels = 11;

    int gestalt16BitAudioSupport = 12;

    /**
     * @deprecated
     * <I/>
     */
    int initCmd = 1;

    /**
     * @deprecated
     * <I/>
     */
    int freeCmd = 2;

    /**
     * @deprecated
     * <I/>
     */
    int totalLoadCmd = 26;

    /**
     * @deprecated
     * <I/>
     */
    int loadCmd = 27;

    /**
     * @deprecated
     * <I/>
     */
    int freqDurationCmd = 40;

    /**
     * @deprecated
     * <I/>
     */
    int restCmd = 41;

    /**
     * @deprecated
     * <I/>
     */
    int freqCmd = 42;

    /**
     * @deprecated
     * <I/>
     */
    int ampCmd = 43;

    /**
     * @deprecated
     * <I/>
     */
    int timbreCmd = 44;

    /**
     * @deprecated
     * <I/>
     */
    int getAmpCmd = 45;

    /**
     * @deprecated
     * <I/>
     */
    int waveTableCmd = 60;

    /**
     * @deprecated
     * <I/>
     */
    int phaseCmd = 61;

    /**
     * @deprecated
     * <I/>
     */
    int rateCmd = 82;

    /**
     * @deprecated
     * <I/>
     */
    int continueCmd = 83;

    /**
     * @deprecated
     * <I/>
     */
    int doubleBufferCmd = 84;

    /**
     * @deprecated
     * <I/>
     */
    int getRateCmd = 85;

    int nullCmd = 0;

    int quietCmd = 3;

    int flushCmd = 4;

    int reInitCmd = 5;

    int waitCmd = 10;

    int pauseCmd = 11;

    int resumeCmd = 12;

    int callBackCmd = 13;

    int syncCmd = 14;

    int availableCmd = 24;

    int versionCmd = 25;

    int volumeCmd = 46;

    int getVolumeCmd = 47;

    int clockComponentCmd = 50;

    int getClockComponentCmd = 51;

    int scheduledSoundCmd = 52;

    int linkSoundComponentsCmd = 53;

    int soundCmd = 80;

    int bufferCmd = 81;

    int rateMultiplierCmd = 86;

    int getRateMultiplierCmd = 87;

    int initChanLeft = 2;

    int initChanRight = 3;

    int initNoInterp = 4;

    int initNoDrop = 8;

    int initMono = 128;

    int initStereo = 192;

    int initMACE3 = 768;

    int initMACE6 = 1024;

    int initPanMask = 3;

    int initSRateMask = 48;

    int initStereoMask = 192;

    int initCompMask = 65280;

    /**
     * @deprecated
     * <I/>
     */
    int squareWaveSynth = 1;

    /**
     * @deprecated
     * <I/>
     */
    int waveTableSynth = 3;

    /**
     * @deprecated
     * <I/>
     */
    int MACE3snthID = 11;

    /**
     * @deprecated
     * <I/>
     */
    int MACE6snthID = 13;

    /**
     * @deprecated
     * <I/>
     */
    int dbBufferReady = 1;

    /**
     * @deprecated
     * <I/>
     */
    int dbLastBuffer = 4;

    int soundListRsrc = 1936614432;

    int rate48khz = -1149239296;

    int rate44khz = -1404829696;

    int rate32khz = 2097152000;

    int rate22050hz = 1445068800;

    int rate22khz = 1458473891;

    int rate16khz = 1048576000;

    int rate11khz = 729236945;

    int rate11025hz = 722534400;

    int rate8khz = 524288000;

    int sampledSynth = 5;

    int kMiddleC = 60;

    int kSimpleBeepID = 1;

    int kFullVolume = 256;

    int kNoVolume = 0;

    int stdQLength = 128;

    int dataOffsetFlag = 32768;

    int kUseOptionalOutputDevice = -1;

    int notCompressed = 0;

    int fixedCompression = -1;

    int variableCompression = -2;

    int twoToOne = 1;

    int eightToThree = 2;

    int threeToOne = 3;

    int sixToOne = 4;

    int sixToOnePacketSize = 8;

    int threeToOnePacketSize = 16;

    int stateBlockSize = 64;

    int leftOverBlockSize = 32;

    int firstSoundFormat = 1;

    int secondSoundFormat = 2;

    int sysBeepDisable = 0;

    int sysBeepEnable = 1;

    int sysBeepSynchronous = 2;

    int unitTypeNoSelection = 65535;

    int unitTypeSeconds = 0;

    int stdSH = 0;

    int extSH = 255;

    int cmpSH = 254;

    int kSoundNotCompressed = 1313820229;

    int k8BitOffsetBinaryFormat = 1918990112;

    int k16BitBigEndianFormat = 1953984371;

    int k16BitLittleEndianFormat = 1936684916;

    int kFloat32Format = 1718367026;

    int kFloat64Format = 1718367796;

    int k24BitFormat = 1768829492;

    int k32BitFormat = 1768829746;

    int k32BitLittleEndianFormat = 842231401;

    int kMACE3Compression = 1296122675;

    int kMACE6Compression = 1296122678;

    int kCDXA4Compression = 1667528756;

    int kCDXA2Compression = 1667528754;

    int kIMACompression = 1768775988;

    int kULawCompression = 1970037111;

    int kALawCompression = 1634492791;

    int kMicrosoftADPCMFormat = 1836253186;

    int kDVIIntelIMAFormat = 1836253201;

    int kDVAudioFormat = 1685480289;

    int kOffsetBinary = 1918990112;

    int kTwosComplement = 1953984371;

    int kLittleEndianFormat = 1936684916;

    int kMPEGLayer3Format = 1836253269;

    int kFullMPEGLay3Format = 778924083;

    int siCDQuality = 1667506208;

    int siBestQuality = 1650815860;

    int siBetterQuality = 1650816114;

    int siGoodQuality = 1735356260;

    int siNoneQuality = 1852796517;

    int siReadPermission = 0;

    int siWritePermission = 1;

    int siDeviceIsConnected = 1;

    int siDeviceNotConnected = 0;

    int siDontKnowIfConnected = -1;

    int k8BitRawIn = 1;

    int k8BitTwosIn = 2;

    int k16BitIn = 4;

    int kStereoIn = 8;

    int k8BitRawOut = 256;

    int k8BitTwosOut = 512;

    int k16BitOut = 1024;

    int kStereoOut = 2048;

    int kReverse = 65536;

    int kRateConvert = 131072;

    int kCreateSoundSource = 262144;

    int kVMAwareness = 2097152;

    int kHighQuality = 4194304;

    int kNonRealTime = 8388608;

    int kSourcePaused = 1;

    int kPassThrough = 65536;

    int kNoSoundComponentChain = 131072;

    int kNoMixing = 1;

    int kNoSampleRateConversion = 2;

    int kNoSampleSizeConversion = 4;

    int kNoSampleFormatConversion = 8;

    int kNoChannelConversion = 16;

    int kNoDecompression = 32;

    int kNoVolumeConversion = 64;

    int kNoRealtimeProcessing = 128;

    int kScheduledSource = 256;

    int kNonInterleavedBuffer = 512;

    int kNonPagingMixer = 1024;

    int kSoundConverterMixer = 2048;

    int kPagingMixer = 4096;

    int kVMAwareMixer = 8192;

    int kExtendedSoundData = 16384;

    int kBestQuality = 1;

    int kInputMask = 255;

    int kOutputMask = 65280;

    int kOutputShift = 8;

    int kActionMask = 16711680;

    int kSoundComponentBits = 16777215;

    int kAudioFormatAtomType = 1718775137;

    int kAudioEndianAtomType = 1701733473;

    int kAudioVBRAtomType = 1986163297;

    int kAudioTerminatorAtomType = 0;

    int kSoundConverterDidntFillBuffer = 1;

    int kSoundConverterHasLeftOverData = 2;

    int kExtendedSoundSampleCountNotValid = 1;

    int kExtendedSoundBufferSizeValid = 2;

    int kScheduledSoundDoScheduled = 1;

    int kScheduledSoundDoCallBack = 2;

    int kScheduledSoundExtendedHdr = 4;

    int kInvalidSource = -1;

    int kNoSource = 1852796517;

    int kCDSource = 1667506208;

    int kExtMicSource = 1701669219;

    int kSoundInSource = 1936289386;

    int kRCAInSource = 1769104225;

    int kTVFMTunerSource = 1953916525;

    int kDAVInSource = 1768186230;

    int kIntMicSource = 1768778083;

    int kMediaBaySource = 1835164025;

    int kModemSource = 1836016749;

    int kPCCardSource = 1885564192;

    int kZoomVideoSource = 2054582371;

    int kDVDSource = 1685480545;

    int siActiveChannels = 1667785059;

    int siActiveLevels = 1819107683;

    int siAGCOnOff = 1634165536;

    int siAsync = 1634957678;

    int siAVDisplayBehavior = 1635148898;

    int siChannelAvailable = 1667785078;

    int siCompressionAvailable = 1668112758;

    int siCompressionChannels = 1668309876;

    int siCompressionFactor = 1668114017;

    int siCompressionHeader = 1668114532;

    int siCompressionNames = 1668178285;

    int siCompressionParams = 1702257015;

    int siCompressionSampleRate = 1668313716;

    int siCompressionType = 1668246896;

    int siContinuous = 1668247156;

    int siDecompressionParams = 2002876005;

    int siDeviceBufferInfo = 1684171118;

    int siDeviceConnected = 1684238190;

    int siDeviceIcon = 1768124270;

    int siDeviceName = 1851878757;

    int siEQSpectrumBands = 1701933922;

    int siEQSpectrumLevels = 1701932150;

    int siEQSpectrumOnOff = 1701932143;

    int siEQToneControlGain = 1701934183;

    int siEQToneControlOnOff = 1701934179;

    int siHardwareBalance = 1751277932;

    int siHardwareBalanceSteps = 1751280755;

    int siHardwareBass = 1751277939;

    int siHardwareBassSteps = 1751282548;

    int siHardwareBusy = 1752654451;

    int siHardwareFormat = 1752655469;

    int siHardwareMute = 1752003956;

    int siHardwareMuteNoPrefs = 1752002160;

    int siHardwareTreble = 1752461922;

    int siHardwareTrebleSteps = 1752659059;

    int siHardwareVolume = 1752592236;

    int siHardwareVolumeSteps = 1752396912;

    int siHeadphoneMute = 1886221684;

    int siHeadphoneVolume = 1886809964;

    int siHeadphoneVolumeSteps = 1751413620;

    int siInputAvailable = 1768841590;

    int siInputGain = 1734437230;

    int siInputSource = 1936684402;

    int siInputSourceNames = 1936613741;

    int siLevelMeterOnOff = 1819108724;

    int siModemGain = 1835491689;

    int siMonitorAvailable = 1835950454;

    int siMonitorSource = 1836019315;

    int siNumberChannels = 1667785070;

    int siOptionsDialog = 1869640804;

    int siOSTypeInputSource = 1768845428;

    int siOSTypeInputAvailable = 1768841590;

    int siOutputDeviceName = 1869504877;

    int siPlayThruOnOff = 1886155880;

    int siPostMixerSoundComponent = 1886612856;

    int siPreMixerSoundComponent = 1886547320;

    int siQuality = 1903518060;

    int siRateMultiplier = 1919776108;

    int siRecordingQuality = 1903518060;

    int siSampleRate = 1936875892;

    int siSampleRateAvailable = 1936875894;

    int siSampleSize = 1936943482;

    int siSampleSizeAvailable = 1936941430;

    int siSetupCDAudio = 1937072996;

    int siSetupModemAudio = 1937075556;

    int siSlopeAndIntercept = 1718378864;

    int siSoundClock = 1935895659;

    int siUseThisSoundClock = 1935895651;

    int siSpeakerMute = 1936553332;

    int siSpeakerVolume = 1937141612;

    int siSSpCPULoadLimit = 862219372;

    int siSSpLocalization = 862218598;

    int siSSpSpeakerSetup = 862221172;

    int siStereoInputGain = 1936154985;

    int siSubwooferMute = 1651340660;

    int siTwosComplementOnOff = 1953984371;

    int siVolume = 1987013749;

    int siVoxRecordInfo = 1987016818;

    int siVoxStopInfo = 1987016819;

    int siWideStereo = 2003395685;

    int siSupportedExtendedFlags = 1702389356;

    /**
     * QT5 Constants
     */
    int siTerminalType = 1953790320;

    /**
     * QT5 Constants
     */
    int siVendorProduct = 1987080815;

    /**
     * QT5 Constants
     */
    int siRateConverterRollOffSlope = 1919116386;

    /**
     * QT5 Constants
     */
    int siOutputLatency = 1869378661;

    int kMicrophoneArray = 1835623265;
}

