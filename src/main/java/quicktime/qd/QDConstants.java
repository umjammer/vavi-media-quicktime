package quicktime.qd;

/**
 * This class represents all of the constants in the QuickDraw interface.
 * ALL constants start with a small letter (not a capital one)
 * except for constants that start with an acronym, such as MPEG...
 */
public abstract interface QDConstants {

    public static final int teJustLeft = 0;

    public static final int teJustCenter = 1;

    public static final int teJustRight = -1;

    public static final int teForceLeft = -2;

    public static final int teFlushDefault = 0;

    public static final int teCenter = 1;

    public static final int teFlushRight = -1;

    public static final int teFlushLeft = -2;

    /**
     * MacOS font style constants
     */
    public static final int normal = 0;

    public static final int bold = 1;

    public static final int italic = 2;

    public static final int underlined = 4;

    public static final int picLParen = 0;

    public static final int picRParen = 1;

    public static final int clutType = 0;

    public static final int fixedType = 1;

    public static final int directType = 2;

    public static final int gdDevType = 0;

    public static final int burstDevice = 7;

    public static final int ext32Device = 8;

    public static final int ramInit = 10;

    public static final int mainScreen = 11;

    public static final int allInit = 12;

    public static final int screenDevice = 13;

    public static final int noDriver = 14;

    public static final int screenActive = 15;

    public static final int interlacedDevice = 2;

    public static final int roundedDevice = 5;

    public static final int hasAuxMenuBar = 6;

    public static final int hiliteBit = 7;

    public static final int pHiliteBit = 0;

    public static final int defQDColors = 127;

    public static final int RGBDirect = 16;

    public static final int baseAddr32 = 4;

    public static final int sysPatListID = 0;

    public static final int iBeamCursor = 1;

    public static final int crossCursor = 2;

    public static final int plusCursor = 3;

    public static final int watchCursor = 4;

    public static final int kQDGrafVerbFrame = 0;

    public static final int kQDGrafVerbPaint = 1;

    public static final int kQDGrafVerbErase = 2;

    public static final int kQDGrafVerbInvert = 3;

    public static final int kQDGrafVerbFill = 4;

    public static final int chunky = 0;

    public static final int chunkyPlanar = 1;

    public static final int planar = 2;

    public static final int kNoConstraint = 0;

    public static final int kVerticalConstraint = 1;

    public static final int kHorizontalConstraint = 2;

    public static final int kCursorImageMajorVersion = 1;

    public static final int kCursorImageMinorVersion = 0;

    public static final int singleDevicesBit = 0;

    public static final int dontMatchSeedsBit = 1;

    public static final int allDevicesBit = 2;

    public static final int singleDevices = 1;

    public static final int dontMatchSeeds = 2;

    public static final int allDevices = 4;

    public static final int pixPurgeBit = 0;

    public static final int noNewDeviceBit = 1;

    public static final int useTempMemBit = 2;

    public static final int keepLocalBit = 3;

    public static final int pixelsPurgeableBit = 6;

    public static final int pixelsLockedBit = 7;

    public static final int mapPixBit = 16;

    public static final int newDepthBit = 17;

    public static final int alignPixBit = 18;

    public static final int newRowBytesBit = 19;

    public static final int reallocPixBit = 20;

    public static final int clipPixBit = 28;

    public static final int stretchPixBit = 29;

    public static final int ditherPixBit = 30;

    public static final int gwFlagErrBit = 31;

    public static final int deviceIsIndirect = 1;

    public static final int deviceNeedsLock = 2;

    public static final int deviceIsStatic = 4;

    public static final int deviceIsExternalBuffer = 8;

    public static final int deviceIsDDSurface = 16;

    public static final int deviceIsDCISurface = 32;

    public static final int deviceIsGDISurface = 64;

    public static final int deviceIsAScreen = 128;

    public static final int deviceIsOverlaySurface = 256;

    public static final int pixPurge = 1;

    public static final int noNewDevice = 2;

    public static final int useTempMem = 4;

    public static final int keepLocal = 8;

    public static final int pixelsPurgeable = 64;

    public static final int pixelsLocked = 128;

    public static final int kAllocDirectDrawSurface = 16384;

    public static final int kICMTempThenAppMemory = 4096;

    public static final int kICMAppThenTempMemory = 8192;

    public static final int mapPix = 65536;

    public static final int newDepth = 131072;

    public static final int alignPix = 262144;

    public static final int newRowBytes = 524288;

    public static final int reallocPix = 1048576;

    public static final int clipPix = 268435456;

    public static final int stretchPix = 536870912;

    public static final int ditherPix = 1073741824;

    public static final int gwFlagErr = Integer.MIN_VALUE;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k1MonochromePixelFormat = 1;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k2IndexedPixelFormat = 2;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k4IndexedPixelFormat = 4;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k8IndexedPixelFormat = 8;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k16BE555PixelFormat = 16;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k24RGBPixelFormat = 24;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k32ARGBPixelFormat = 32;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k1IndexedGrayPixelFormat = 33;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k2IndexedGrayPixelFormat = 34;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k4IndexedGrayPixelFormat = 36;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    public static final int k8IndexedGrayPixelFormat = 40;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k16LE555PixelFormat = 1278555445;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k16BE565PixelFormat = 1110783541;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k16LE565PixelFormat = 1278555701;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k24BGRPixelFormat = 842285639;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k32BGRAPixelFormat = 1111970369;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k32ABGRPixelFormat = 1094862674;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int k32RGBAPixelFormat = 1380401729;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kYUVSPixelFormat = 2037741171;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kYUVUPixelFormat = 2037741173;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kYVU9PixelFormat = 1498830137;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kYUV411PixelFormat = 1496592689;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kYVYU422PixelFormat = 1498830166;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kUYVY422PixelFormat = 1431918169;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    public static final int kYUV211PixelFormat = 1496461617;

    public static final int srcCopy = 0;

    public static final int srcOr = 1;

    public static final int srcXor = 2;

    public static final int srcBic = 3;

    public static final int notSrcCopy = 4;

    public static final int notSrcOr = 5;

    public static final int notSrcXor = 6;

    public static final int notSrcBic = 7;

    public static final int patCopy = 8;

    public static final int patOr = 9;

    public static final int patXor = 10;

    public static final int patBic = 11;

    public static final int notPatCopy = 12;

    public static final int notPatOr = 13;

    public static final int notPatXor = 14;

    public static final int notPatBic = 15;

    public static final int blend = 32;

    public static final int addPin = 33;

    public static final int addOver = 34;

    public static final int subPin = 35;

    public static final int transparent = 36;

    public static final int addMax = 37;

    public static final int adMax = 37;

    public static final int subOver = 38;

    public static final int adMin = 39;

    public static final int grayishTextOr = 49;

    public static final int hilitetransfermode = 50;

    public static final int hilite = 50;

    public static final int ditherCopy = 64;

    public static final int italicBit = 1;

    public static final int ulineBit = 2;

    public static final int outlineBit = 3;

    public static final int shadowBit = 4;

    public static final int condenseBit = 5;

    public static final int extendBit = 6;

    public static final int normalBit = 0;

    public static final int inverseBit = 1;

    public static final int redBit = 4;

    public static final int greenBit = 3;

    public static final int blueBit = 2;

    public static final int cyanBit = 8;

    public static final int magentaBit = 7;

    public static final int yellowBit = 6;

    public static final int blackBit = 5;

    public static final int blackColor = 33;

    public static final int whiteColor = 30;

    public static final int redColor = 205;

    public static final int greenColor = 341;

    public static final int blueColor = 409;

    public static final int cyanColor = 273;

    public static final int magentaColor = 137;

    public static final int yellowColor = 69;

    public static final int pmCourteous = 0;

    public static final int pmTolerant = 2;

    public static final int pmAnimated = 4;

    public static final int pmExplicit = 8;

    public static final int pmWhite = 16;

    public static final int pmBlack = 32;

    public static final int pmInhibitG2 = 256;

    public static final int pmInhibitC2 = 512;

    public static final int pmInhibitG4 = 1024;

    public static final int pmInhibitC4 = 2048;

    public static final int pmInhibitG8 = 4096;

    public static final int pmInhibitC8 = 8192;

    public static final int pmNoUpdates = 32768;

    public static final int pmBkUpdates = 40960;

    public static final int pmFgUpdates = 49152;

    public static final int pmAllUpdates = 57344;
}

