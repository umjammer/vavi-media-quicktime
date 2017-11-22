
package quicktime.sound;

public abstract interface SoundConstants {
    public static final int gestaltSoundAttr = 1936614432;

    public static final int gestaltStereoCapability = 0;

    public static final int gestaltStereoMixing = 1;

    public static final int gestaltSoundIOMgrPresent = 3;

    public static final int gestaltBuiltInSoundInput = 4;

    public static final int gestaltHasSoundInputDevice = 5;

    public static final int gestaltPlayAndRecord = 6;

    public static final int gestalt16BitSoundIO = 7;

    public static final int gestaltStereoInput = 8;

    public static final int gestaltLineLevelInput = 9;

    public static final int gestaltSndPlayDoubleBuffer = 10;

    public static final int gestaltMultiChannels = 11;

    public static final int gestalt16BitAudioSupport = 12;

    /**
     * @deprecated
     */
    public static final int initCmd = 1;

    /**
     * @deprecated
     */
    public static final int freeCmd = 2;

    /**
     * @deprecated
     */
    public static final int totalLoadCmd = 26;

    /**
     * @deprecated
     */
    public static final int loadCmd = 27;

    /**
     * @deprecated
     */
    public static final int freqDurationCmd = 40;

    /**
     * @deprecated
     */
    public static final int restCmd = 41;

    /**
     * @deprecated
     */
    public static final int freqCmd = 42;

    /**
     * @deprecated
     */
    public static final int ampCmd = 43;

    /**
     * @deprecated
     */
    public static final int timbreCmd = 44;

    /**
     * @deprecated
     */
    public static final int getAmpCmd = 45;

    /**
     * @deprecated
     */
    public static final int waveTableCmd = 60;

    /**
     * @deprecated
     */
    public static final int phaseCmd = 61;

    /**
     * @deprecated
     */
    public static final int rateCmd = 82;

    /**
     * @deprecated
     */
    public static final int continueCmd = 83;

    /**
     * @deprecated
     */
    public static final int doubleBufferCmd = 84;

    /**
     * @deprecated
     */
    public static final int getRateCmd = 85;

    public static final int nullCmd = 0;

    public static final int quietCmd = 3;

    public static final int flushCmd = 4;

    public static final int reInitCmd = 5;

    public static final int waitCmd = 10;

    public static final int pauseCmd = 11;

    public static final int resumeCmd = 12;

    public static final int callBackCmd = 13;

    public static final int syncCmd = 14;

    public static final int availableCmd = 24;

    public static final int versionCmd = 25;

    public static final int volumeCmd = 46;

    public static final int getVolumeCmd = 47;

    public static final int clockComponentCmd = 50;

    public static final int getClockComponentCmd = 51;

    public static final int scheduledSoundCmd = 52;

    public static final int linkSoundComponentsCmd = 53;

    public static final int soundCmd = 80;

    public static final int bufferCmd = 81;

    public static final int rateMultiplierCmd = 86;

    public static final int getRateMultiplierCmd = 87;

    public static final int initChanLeft = 2;

    public static final int initChanRight = 3;

    public static final int initNoInterp = 4;

    public static final int initNoDrop = 8;

    public static final int initMono = 128;

    public static final int initStereo = 192;

    public static final int initMACE3 = 768;

    public static final int initMACE6 = 1024;

    public static final int initPanMask = 3;

    public static final int initSRateMask = 48;

    public static final int initStereoMask = 192;

    public static final int initCompMask = 65280;

    /**
     * @deprecated
     */
    public static final int squareWaveSynth = 1;

    /**
     * @deprecated
     */
    public static final int waveTableSynth = 3;

    /**
     * @deprecated
     */
    public static final int MACE3snthID = 11;

    /**
     * @deprecated
     */
    public static final int MACE6snthID = 13;

    /**
     * @deprecated
     */
    public static final int dbBufferReady = 1;

    /**
     * @deprecated
     */
    public static final int dbLastBuffer = 4;

    public static final int soundListRsrc = 1936614432;

    public static final int rate48khz = -1149239296;

    public static final int rate44khz = -1404829696;

    public static final int rate32khz = 2097152000;

    public static final int rate22050hz = 1445068800;

    public static final int rate22khz = 1458473891;

    public static final int rate16khz = 1048576000;

    public static final int rate11khz = 729236945;

    public static final int rate11025hz = 722534400;

    public static final int rate8khz = 524288000;

    public static final int sampledSynth = 5;

    public static final int kMiddleC = 60;

    public static final int kSimpleBeepID = 1;

    public static final int kFullVolume = 256;

    public static final int kNoVolume = 0;

    public static final int stdQLength = 128;

    public static final int dataOffsetFlag = 32768;

    public static final int kUseOptionalOutputDevice = -1;

    public static final int notCompressed = 0;

    public static final int fixedCompression = -1;

    public static final int variableCompression = -2;

    public static final int twoToOne = 1;

    public static final int eightToThree = 2;

    public static final int threeToOne = 3;

    public static final int sixToOne = 4;

    public static final int sixToOnePacketSize = 8;

    public static final int threeToOnePacketSize = 16;

    public static final int stateBlockSize = 64;

    public static final int leftOverBlockSize = 32;

    public static final int firstSoundFormat = 1;

    public static final int secondSoundFormat = 2;

    public static final int sysBeepDisable = 0;

    public static final int sysBeepEnable = 1;

    public static final int sysBeepSynchronous = 2;

    public static final int unitTypeNoSelection = 65535;

    public static final int unitTypeSeconds = 0;

    public static final int stdSH = 0;

    public static final int extSH = 255;

    public static final int cmpSH = 254;

    public static final int kSoundNotCompressed = 1313820229;

    public static final int k8BitOffsetBinaryFormat = 1918990112;

    public static final int k16BitBigEndianFormat = 1953984371;

    public static final int k16BitLittleEndianFormat = 1936684916;

    public static final int kFloat32Format = 1718367026;

    public static final int kFloat64Format = 1718367796;

    public static final int k24BitFormat = 1768829492;

    public static final int k32BitFormat = 1768829746;

    public static final int k32BitLittleEndianFormat = 842231401;

    public static final int kMACE3Compression = 1296122675;

    public static final int kMACE6Compression = 1296122678;

    public static final int kCDXA4Compression = 1667528756;

    public static final int kCDXA2Compression = 1667528754;

    public static final int kIMACompression = 1768775988;

    public static final int kULawCompression = 1970037111;

    public static final int kALawCompression = 1634492791;

    public static final int kMicrosoftADPCMFormat = 1836253186;

    public static final int kDVIIntelIMAFormat = 1836253201;

    public static final int kDVAudioFormat = 1685480289;

    public static final int kOffsetBinary = 1918990112;

    public static final int kTwosComplement = 1953984371;

    public static final int kLittleEndianFormat = 1936684916;

    public static final int kMPEGLayer3Format = 1836253269;

    public static final int kFullMPEGLay3Format = 778924083;

    public static final int siCDQuality = 1667506208;

    public static final int siBestQuality = 1650815860;

    public static final int siBetterQuality = 1650816114;

    public static final int siGoodQuality = 1735356260;

    public static final int siNoneQuality = 1852796517;

    public static final int siReadPermission = 0;

    public static final int siWritePermission = 1;

    public static final int siDeviceIsConnected = 1;

    public static final int siDeviceNotConnected = 0;

    public static final int siDontKnowIfConnected = -1;

    public static final int k8BitRawIn = 1;

    public static final int k8BitTwosIn = 2;

    public static final int k16BitIn = 4;

    public static final int kStereoIn = 8;

    public static final int k8BitRawOut = 256;

    public static final int k8BitTwosOut = 512;

    public static final int k16BitOut = 1024;

    public static final int kStereoOut = 2048;

    public static final int kReverse = 65536;

    public static final int kRateConvert = 131072;

    public static final int kCreateSoundSource = 262144;

    public static final int kVMAwareness = 2097152;

    public static final int kHighQuality = 4194304;

    public static final int kNonRealTime = 8388608;

    public static final int kSourcePaused = 1;

    public static final int kPassThrough = 65536;

    public static final int kNoSoundComponentChain = 131072;

    public static final int kNoMixing = 1;

    public static final int kNoSampleRateConversion = 2;

    public static final int kNoSampleSizeConversion = 4;

    public static final int kNoSampleFormatConversion = 8;

    public static final int kNoChannelConversion = 16;

    public static final int kNoDecompression = 32;

    public static final int kNoVolumeConversion = 64;

    public static final int kNoRealtimeProcessing = 128;

    public static final int kScheduledSource = 256;

    public static final int kNonInterleavedBuffer = 512;

    public static final int kNonPagingMixer = 1024;

    public static final int kSoundConverterMixer = 2048;

    public static final int kPagingMixer = 4096;

    public static final int kVMAwareMixer = 8192;

    public static final int kExtendedSoundData = 16384;

    public static final int kBestQuality = 1;

    public static final int kInputMask = 255;

    public static final int kOutputMask = 65280;

    public static final int kOutputShift = 8;

    public static final int kActionMask = 16711680;

    public static final int kSoundComponentBits = 16777215;

    public static final int kAudioFormatAtomType = 1718775137;

    public static final int kAudioEndianAtomType = 1701733473;

    public static final int kAudioVBRAtomType = 1986163297;

    public static final int kAudioTerminatorAtomType = 0;

    public static final int kSoundConverterDidntFillBuffer = 1;

    public static final int kSoundConverterHasLeftOverData = 2;

    public static final int kExtendedSoundSampleCountNotValid = 1;

    public static final int kExtendedSoundBufferSizeValid = 2;

    public static final int kScheduledSoundDoScheduled = 1;

    public static final int kScheduledSoundDoCallBack = 2;

    public static final int kScheduledSoundExtendedHdr = 4;

    public static final int kInvalidSource = -1;

    public static final int kNoSource = 1852796517;

    public static final int kCDSource = 1667506208;

    public static final int kExtMicSource = 1701669219;

    public static final int kSoundInSource = 1936289386;

    public static final int kRCAInSource = 1769104225;

    public static final int kTVFMTunerSource = 1953916525;

    public static final int kDAVInSource = 1768186230;

    public static final int kIntMicSource = 1768778083;

    public static final int kMediaBaySource = 1835164025;

    public static final int kModemSource = 1836016749;

    public static final int kPCCardSource = 1885564192;

    public static final int kZoomVideoSource = 2054582371;

    public static final int kDVDSource = 1685480545;

    public static final int siActiveChannels = 1667785059;

    public static final int siActiveLevels = 1819107683;

    public static final int siAGCOnOff = 1634165536;

    public static final int siAsync = 1634957678;

    public static final int siAVDisplayBehavior = 1635148898;

    public static final int siChannelAvailable = 1667785078;

    public static final int siCompressionAvailable = 1668112758;

    public static final int siCompressionChannels = 1668309876;

    public static final int siCompressionFactor = 1668114017;

    public static final int siCompressionHeader = 1668114532;

    public static final int siCompressionNames = 1668178285;

    public static final int siCompressionParams = 1702257015;

    public static final int siCompressionSampleRate = 1668313716;

    public static final int siCompressionType = 1668246896;

    public static final int siContinuous = 1668247156;

    public static final int siDecompressionParams = 2002876005;

    public static final int siDeviceBufferInfo = 1684171118;

    public static final int siDeviceConnected = 1684238190;

    public static final int siDeviceIcon = 1768124270;

    public static final int siDeviceName = 1851878757;

    public static final int siEQSpectrumBands = 1701933922;

    public static final int siEQSpectrumLevels = 1701932150;

    public static final int siEQSpectrumOnOff = 1701932143;

    public static final int siEQToneControlGain = 1701934183;

    public static final int siEQToneControlOnOff = 1701934179;

    public static final int siHardwareBalance = 1751277932;

    public static final int siHardwareBalanceSteps = 1751280755;

    public static final int siHardwareBass = 1751277939;

    public static final int siHardwareBassSteps = 1751282548;

    public static final int siHardwareBusy = 1752654451;

    public static final int siHardwareFormat = 1752655469;

    public static final int siHardwareMute = 1752003956;

    public static final int siHardwareMuteNoPrefs = 1752002160;

    public static final int siHardwareTreble = 1752461922;

    public static final int siHardwareTrebleSteps = 1752659059;

    public static final int siHardwareVolume = 1752592236;

    public static final int siHardwareVolumeSteps = 1752396912;

    public static final int siHeadphoneMute = 1886221684;

    public static final int siHeadphoneVolume = 1886809964;

    public static final int siHeadphoneVolumeSteps = 1751413620;

    public static final int siInputAvailable = 1768841590;

    public static final int siInputGain = 1734437230;

    public static final int siInputSource = 1936684402;

    public static final int siInputSourceNames = 1936613741;

    public static final int siLevelMeterOnOff = 1819108724;

    public static final int siModemGain = 1835491689;

    public static final int siMonitorAvailable = 1835950454;

    public static final int siMonitorSource = 1836019315;

    public static final int siNumberChannels = 1667785070;

    public static final int siOptionsDialog = 1869640804;

    public static final int siOSTypeInputSource = 1768845428;

    public static final int siOSTypeInputAvailable = 1768841590;

    public static final int siOutputDeviceName = 1869504877;

    public static final int siPlayThruOnOff = 1886155880;

    public static final int siPostMixerSoundComponent = 1886612856;

    public static final int siPreMixerSoundComponent = 1886547320;

    public static final int siQuality = 1903518060;

    public static final int siRateMultiplier = 1919776108;

    public static final int siRecordingQuality = 1903518060;

    public static final int siSampleRate = 1936875892;

    public static final int siSampleRateAvailable = 1936875894;

    public static final int siSampleSize = 1936943482;

    public static final int siSampleSizeAvailable = 1936941430;

    public static final int siSetupCDAudio = 1937072996;

    public static final int siSetupModemAudio = 1937075556;

    public static final int siSlopeAndIntercept = 1718378864;

    public static final int siSoundClock = 1935895659;

    public static final int siUseThisSoundClock = 1935895651;

    public static final int siSpeakerMute = 1936553332;

    public static final int siSpeakerVolume = 1937141612;

    public static final int siSSpCPULoadLimit = 862219372;

    public static final int siSSpLocalization = 862218598;

    public static final int siSSpSpeakerSetup = 862221172;

    public static final int siStereoInputGain = 1936154985;

    public static final int siSubwooferMute = 1651340660;

    public static final int siTwosComplementOnOff = 1953984371;

    public static final int siVolume = 1987013749;

    public static final int siVoxRecordInfo = 1987016818;

    public static final int siVoxStopInfo = 1987016819;

    public static final int siWideStereo = 2003395685;

    public static final int siSupportedExtendedFlags = 1702389356;

    public static final int siTerminalType = 1953790320;

    public static final int siVendorProduct = 1987080815;

    public static final int siRateConverterRollOffSlope = 1919116386;

    public static final int siOutputLatency = 1869378661;

    public static final int kMicrophoneArray = 1835623265;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SoundConstants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
