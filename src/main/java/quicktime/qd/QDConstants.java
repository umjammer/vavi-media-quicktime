package quicktime.qd;

/**
 * This class represents all of the constants in the QuickDraw interface.
 * ALL constants start with a small letter (not a capital one)
 * except for constants that start with an acronym, such as MPEG...
 */
public interface QDConstants {

    int teJustLeft = 0;

    int teJustCenter = 1;

    int teJustRight = -1;

    int teForceLeft = -2;

    int teFlushDefault = 0;

    int teCenter = 1;

    int teFlushRight = -1;

    int teFlushLeft = -2;

    /**
     * MacOS font style constants
     */
    int normal = 0;

    int bold = 1;

    int italic = 2;

    int underlined = 4;

    int picLParen = 0;

    int picRParen = 1;

    int clutType = 0;

    int fixedType = 1;

    int directType = 2;

    int gdDevType = 0;

    int burstDevice = 7;

    int ext32Device = 8;

    int ramInit = 10;

    int mainScreen = 11;

    int allInit = 12;

    int screenDevice = 13;

    int noDriver = 14;

    int screenActive = 15;

    int interlacedDevice = 2;

    int roundedDevice = 5;

    int hasAuxMenuBar = 6;

    int hiliteBit = 7;

    int pHiliteBit = 0;

    int defQDColors = 127;

    int RGBDirect = 16;

    int baseAddr32 = 4;

    int sysPatListID = 0;

    int iBeamCursor = 1;

    int crossCursor = 2;

    int plusCursor = 3;

    int watchCursor = 4;

    int kQDGrafVerbFrame = 0;

    int kQDGrafVerbPaint = 1;

    int kQDGrafVerbErase = 2;

    int kQDGrafVerbInvert = 3;

    int kQDGrafVerbFill = 4;

    int chunky = 0;

    int chunkyPlanar = 1;

    int planar = 2;

    int kNoConstraint = 0;

    int kVerticalConstraint = 1;

    int kHorizontalConstraint = 2;

    int kCursorImageMajorVersion = 1;

    int kCursorImageMinorVersion = 0;

    int singleDevicesBit = 0;

    int dontMatchSeedsBit = 1;

    int allDevicesBit = 2;

    int singleDevices = 1;

    int dontMatchSeeds = 2;

    int allDevices = 4;

    int pixPurgeBit = 0;

    int noNewDeviceBit = 1;

    int useTempMemBit = 2;

    int keepLocalBit = 3;

    int pixelsPurgeableBit = 6;

    int pixelsLockedBit = 7;

    int mapPixBit = 16;

    int newDepthBit = 17;

    int alignPixBit = 18;

    int newRowBytesBit = 19;

    int reallocPixBit = 20;

    int clipPixBit = 28;

    int stretchPixBit = 29;

    int ditherPixBit = 30;

    int gwFlagErrBit = 31;

    int deviceIsIndirect = 1;

    int deviceNeedsLock = 2;

    int deviceIsStatic = 4;

    int deviceIsExternalBuffer = 8;

    int deviceIsDDSurface = 16;

    int deviceIsDCISurface = 32;

    int deviceIsGDISurface = 64;

    int deviceIsAScreen = 128;

    int deviceIsOverlaySurface = 256;

    int pixPurge = 1;

    int noNewDevice = 2;

    int useTempMem = 4;

    int keepLocal = 8;

    int pixelsPurgeable = 64;

    int pixelsLocked = 128;

    int kAllocDirectDrawSurface = 16384;

    int kICMTempThenAppMemory = 4096;

    int kICMAppThenTempMemory = 8192;

    int mapPix = 65536;

    int newDepth = 131072;

    int alignPix = 262144;

    int newRowBytes = 524288;

    int reallocPix = 1048576;

    int clipPix = 268435456;

    int stretchPix = 536870912;

    int ditherPix = 1073741824;

    int gwFlagErr = Integer.MIN_VALUE;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k1MonochromePixelFormat = 1;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k2IndexedPixelFormat = 2;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k4IndexedPixelFormat = 4;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k8IndexedPixelFormat = 8;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k16BE555PixelFormat = 16;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k24RGBPixelFormat = 24;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k32ARGBPixelFormat = 32;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k1IndexedGrayPixelFormat = 33;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k2IndexedGrayPixelFormat = 34;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k4IndexedGrayPixelFormat = 36;

    /**
     * MacOS Pixel formats used in QTNewGWorld
     */
    int k8IndexedGrayPixelFormat = 40;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k16LE555PixelFormat = 1278555445;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k16BE565PixelFormat = 1110783541;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k16LE565PixelFormat = 1278555701;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k24BGRPixelFormat = 842285639;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k32BGRAPixelFormat = 1111970369;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k32ABGRPixelFormat = 1094862674;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int k32RGBAPixelFormat = 1380401729;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kYUVSPixelFormat = 2037741171;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kYUVUPixelFormat = 2037741173;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kYVU9PixelFormat = 1498830137;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kYUV411PixelFormat = 1496592689;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kYVYU422PixelFormat = 1498830166;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kUYVY422PixelFormat = 1431918169;

    /**
     * Win32 Pixel formats used in QTNewGWorld
     */
    int kYUV211PixelFormat = 1496461617;

    int srcCopy = 0;

    int srcOr = 1;

    int srcXor = 2;

    int srcBic = 3;

    int notSrcCopy = 4;

    int notSrcOr = 5;

    int notSrcXor = 6;

    int notSrcBic = 7;

    int patCopy = 8;

    int patOr = 9;

    int patXor = 10;

    int patBic = 11;

    int notPatCopy = 12;

    int notPatOr = 13;

    int notPatXor = 14;

    int notPatBic = 15;

    int blend = 32;

    int addPin = 33;

    int addOver = 34;

    int subPin = 35;

    int transparent = 36;

    int addMax = 37;

    int adMax = 37;

    int subOver = 38;

    int adMin = 39;

    int grayishTextOr = 49;

    int hilitetransfermode = 50;

    int hilite = 50;

    int ditherCopy = 64;

    int italicBit = 1;

    int ulineBit = 2;

    int outlineBit = 3;

    int shadowBit = 4;

    int condenseBit = 5;

    int extendBit = 6;

    int normalBit = 0;

    int inverseBit = 1;

    int redBit = 4;

    int greenBit = 3;

    int blueBit = 2;

    int cyanBit = 8;

    int magentaBit = 7;

    int yellowBit = 6;

    int blackBit = 5;

    int blackColor = 33;

    int whiteColor = 30;

    int redColor = 205;

    int greenColor = 341;

    int blueColor = 409;

    int cyanColor = 273;

    int magentaColor = 137;

    int yellowColor = 69;

    int pmCourteous = 0;

    int pmTolerant = 2;

    int pmAnimated = 4;

    int pmExplicit = 8;

    int pmWhite = 16;

    int pmBlack = 32;

    int pmInhibitG2 = 256;

    int pmInhibitC2 = 512;

    int pmInhibitG4 = 1024;

    int pmInhibitC4 = 2048;

    int pmInhibitG8 = 4096;

    int pmInhibitC8 = 8192;

    int pmNoUpdates = 32768;

    int pmBkUpdates = 40960;

    int pmFgUpdates = 49152;

    int pmAllUpdates = 57344;
}

