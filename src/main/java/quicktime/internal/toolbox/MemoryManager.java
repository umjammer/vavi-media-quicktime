/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * MemoryManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class MemoryManager {

    private MemoryManager() {
    }

    /** used by ExtScheduledSoundHeader, MusicDescription, QTHandleRef, QTPointer, QTPointerRef, QTUtils, QTVector, SndHandle */
    public static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("BlockMove");
    }

    /** used by ComponentIdentifier, QTObjectManagement, QTUtils, QTVRInstance, SPBDevice, SoundCompressionDialog, UserData */
    public static void DisposeHandle(int paramInt) {
        throw new UnsupportedToolboxException("DisposeHandle");
    }

    /** used by MediaEQSpectrumBands, QTFile, QTObjectManagement, SPBDevice */
    public static void DisposePtr(int paramInt) {
        throw new UnsupportedToolboxException("DisposePtr");
    }

    /** used by ComponentIdentifier, QTHandleRef, SoundCompressionDialog, UserData */
    public static int GetHandleSize(int paramInt) {
        throw new UnsupportedToolboxException("GetHandleSize");
    }

    /** used by QTHandleRef, QTVRInstance, UserData */
    public static void HLock(int paramInt) {
        throw new UnsupportedToolboxException("HLock");
    }

    /** used by QTHandleRef */
    public static void HLockHi(int paramInt) {
        throw new UnsupportedToolboxException("HLockHi");
    }

    /** used by QTHandleRef */
    public static void HUnlock(int paramInt) {
        throw new UnsupportedToolboxException("HUnlock");
    }

    /** used by QTHandle */
    public static short HandAndHand(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("HandAndHand");
    }

    /** used by QTHandle, QTHandleRef, QTPointer, QTPointerRef, Region, SampleDescription, TimeBase */
    public static short MemError() {
        throw new UnsupportedToolboxException("MemError");
    }

    /** used by QTHandleRef */
    public static void MoveHHi(int paramInt) {
        throw new UnsupportedToolboxException("MoveHHi");
    }

    /** used by ComponentIdentifier */
    public static int NewEmptyHandle() {
        throw new UnsupportedToolboxException("NewEmptyHandle");
    }

    /** used by ComponentIdentifier, QTHandleRef, QTUtils, QTVRInstance, SoundCompressionDialog, UserData */
    public static int NewHandle(int paramInt) {
        throw new UnsupportedToolboxException("NewHandle");
    }

    /** used by QTHandle, QTHandleRef */
    public static int NewHandleClear(int paramInt) {
        throw new UnsupportedToolboxException("NewHandleClear");
    }

    /** used by QTPointerRef, SPBDevice */
    public static int NewPtr(int paramInt) {
        throw new UnsupportedToolboxException("NewPtr");
    }

    /** used by MediaEQSpectrumBands, QTFile, QTPointerRef */
    public static int NewPtrClear(int paramInt) {
        throw new UnsupportedToolboxException("NewPtrClear");
    }

    /** used by MoviesDispatcher, QTHandle */
    public static short PtrToHand(int paramInt1, int[] paramArrayOfInt, int paramInt2) {
        throw new UnsupportedToolboxException("PtrToHand");
    }

    /** used by QTHandle */
    public static void ReserveMem(int paramInt) {
        throw new UnsupportedToolboxException("ReserveMem");
    }

    /** used by QTHandle, SampleDescription */
    public static void SetHandleSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetHandleSize");
    }

    /** used by QTPointer */
    public static void SetPtrSize(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetPtrSize");
    }

    /** used by QTFile, QTHandleRef, QTPointerRef, TimeCodeDescription, UserData */
    public static void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyArrayToPointer(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyArrayToPointer(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyArrayToPointer(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by MovieMediaHandler, QTHandleRef, QTPointerRef, RawEncodedImage, SoundCompressionDialog */
    public static void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyArrayToPointer(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyArrayToPointer(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyArrayToPointer");
    }

    /** used by MoviesDispatcher, QTFile, QTGetExternalMovieRecord, QTHandleRef, QTPointerRef, SPBDevice, StatusParams, StreamDispatcher, TimeCodeDef, UserData, XMLAttribute, XMLContent, XMLDispatcher, XMLElement */
    public static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyPointerToArray(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyPointerToArray(int paramInt1, int paramInt2, double[] paramArrayOfDouble, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyPointerToArray(int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by QTHandleRef, QTPointerRef, SoundCompressionDialog */
    public static void copyPointerToArray(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyPointerToArray(int paramInt1, int paramInt2, long[] paramArrayOfLong, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by QTHandleRef, QTPointerRef */
    public static void copyPointerToArray(int paramInt1, int paramInt2, short[] paramArrayOfShort, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("copyPointerToArray");
    }

    /** used by SCStatus */
    public static byte getByteFromArray(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("getByteFromArray");
    }

    /** used by IntEncodedImage */
    public static byte getByteFromArray(int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("getByteFromArray");
    }

    /** used by ComponentIdentifier, QTHandleRef, SPBDevice */
    public static byte getByteFromHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getByteFromHandle");
    }

    /** used by MoviesDispatcher, QTFile, QTGetExternalMovieRecord, QTPointerRef, StatusParams, StreamDispatcher, XMLAttribute, XMLContent, XMLDispatcher, XMLElement */
    public static byte getByteFromPointer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getByteFromPointer");
    }

    /** used by QTByteObject */
    public static double getDoubleFromArray(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("getDoubleFromArray");
    }

    /** used by QTHandleRef */
    public static double getDoubleFromHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getDoubleFromHandle");
    }

    /** used by QTPointerRef */
    public static double getDoubleFromPointer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getDoubleFromPointer");
    }

    /** used by QDColor, QDPoint, QTByteObject */
    public static float getFloatFromArray(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("getFloatFromArray");
    }

    /** used by QTHandleRef */
    public static float getFloatFromHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getFloatFromHandle");
    }

    /** used by QTPointerRef, QTVRInterceptRecord */
    public static float getFloatFromPointer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getFloatFromPointer");
    }

    /** used by CameraData, CameraPlacement, CodecComponent, CodecInfo, ComponentDescription, DataRateParams, DataRateSettings, DigitizerInfo, GraphicsMode, ICMFrameTime, ICMPixelFormatInfo, Matrix, MovieMediaHandler, MusicMIDIPacket, NoteRequestInfo, OpenCPicParams, OrthographicCameraData, QDColor, QDDimension, QDPoint, QDRect, QTByteObject, QTFile, QTPointer, QTUtils, SCStatus, SPBDevice, ScrpSTElement, SoundComponentData, SpatialSettings, SynthesizerConnections, SynthesizerDescription, TemporalSettings, TimeBase, TimeCodeDef, TuneStatus, VectorStream, ViewAngleAspectCameraData, ViewPlaneCameraData */
    public static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("getIntFromArray");
    }

    /** used by QTUtils */
    public static int getIntFromArray(float[] paramArrayOfFloat, int paramInt) {
        throw new UnsupportedToolboxException("getIntFromArray");
    }

    /** used by IntEncodedImage, QTPointer, QTUtils */
    public static int getIntFromArray(int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("getIntFromArray");
    }

    /** used by ColorTable, CursorRecord, PixMap, QTHandleRef, RawEncodedImage, SPBDevice */
    public static int getIntFromHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getIntFromHandle");
    }

    /** used by MediaEQSpectrumBands, MoviesDispatcher, MusicDescription, QDGraphics, QTHandleRef, QTPointerRef, QTUtils, QTVRInstance, QTVRInterceptRecord, Region, SGDispatcher, SPB, SPBDevice, SndCommand, SoundCompressionDialog, StreamDispatcher, StringHandle, TimeBase, TunePlayer, UserData, VRDispatcher, XMLAttribute, XMLContent, XMLDispatcher, XMLDoc, XMLElement */
    public static int getIntFromPointer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getIntFromPointer");
    }

    /** used by ICMFrameTime, QTByteObject, QTUtils, SGOutput */
    public static long getLongFromArray(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("getLongFromArray");
    }

    /** used by QTUtils */
    public static long getLongFromArray(float[] paramArrayOfFloat, int paramInt) {
        throw new UnsupportedToolboxException("getLongFromArray");
    }

    /** used by QTUtils */
    public static long getLongFromArray(int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("getLongFromArray");
    }

    /** used by QTHandleRef */
    public static long getLongFromHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getLongFromHandle");
    }

    /** used by QTPointerRef, QTUtils, StreamDispatcher */
    public static long getLongFromPointer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getLongFromPointer");
    }

    /** used by CodecInfo, DigitizerInfo, GraphicsMode, ICMPixelFormatInfo, MusicMIDIPacket, NoteRequestInfo, OpenCPicParams, Pict, QDColor, QDRect, QTByteObject, QTFile, QTUtils, SPBDevice, ScrpSTElement, SoundComponentData, SpatialSettings, SynthesizerConnections, SynthesizerDescription, TCTextOptions, TuneStatus */
    public static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("getShortFromArray");
    }

    /** used by QTUtils */
    public static short getShortFromArray(float[] paramArrayOfFloat, int paramInt) {
        throw new UnsupportedToolboxException("getShortFromArray");
    }

    /** used by IntEncodedImage, QTUtils */
    public static short getShortFromArray(int[] paramArrayOfInt, int paramInt) {
        throw new UnsupportedToolboxException("getShortFromArray");
    }

    /** used by QTHandleRef, RawEncodedImage, SPBDevice, Sprite */
    public static short getShortFromHandle(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getShortFromHandle");
    }

    /** used by MoviesDispatcher, QDGraphics, QTPointerRef, QTUtils, SPB, SPBDevice, SndCommand, XMLAttribute */
    public static short getShortFromPointer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("getShortFromPointer");
    }

    /** used by QTHandleRef */
    public static void setByteInHandle(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("setByteInHandle");
    }

    /** used by MoviesDispatcher, QTFile, QTPointerRef, VRDispatcher */
    public static void setByteInPointer(int paramInt1, int paramInt2, byte paramByte) {
        throw new UnsupportedToolboxException("setByteInPointer");
    }

    /** used by QTByteObject */
    public static void setDoubleInArray(byte[] paramArrayOfByte, int paramInt, double paramDouble) {
        throw new UnsupportedToolboxException("setDoubleInArray");
    }

    /** used by QTHandleRef */
    public static void setDoubleInHandle(int paramInt1, int paramInt2, double paramDouble) {
        throw new UnsupportedToolboxException("setDoubleInHandle");
    }

    /** used by QTPointerRef */
    public static void setDoubleInPointer(int paramInt1, int paramInt2, double paramDouble) {
        throw new UnsupportedToolboxException("setDoubleInPointer");
    }

    /** used by QDColor, QDPoint, QTByteObject */
    public static void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat) {
        throw new UnsupportedToolboxException("setFloatInArray");
    }

    /** used by QTHandleRef */
    public static void setFloatInHandle(int paramInt1, int paramInt2, float paramFloat) {
        throw new UnsupportedToolboxException("setFloatInHandle");
    }

    /** used by QTPointerRef, QTVRInterceptRecord */
    public static void setFloatInPointer(int paramInt1, int paramInt2, float paramFloat) {
        throw new UnsupportedToolboxException("setFloatInPointer");
    }

    /** used by CameraData, CameraPlacement, CodecInfo, ComponentDescription, DataRateParams, DataRateSettings, DigitizerInfo, GraphicsMode, ICMFrameTime, ICMPixelFormatInfo, Matrix, Movie, MusicMIDIPacket, NoteRequestInfo, OpenCPicParams, OrthographicCameraData, QDColor, QDDimension, QDPoint, QDRect, QTByteObject, QTUtils, SCStatus, ScrpSTElement, SoundComponentData, SpatialSettings, SynthesizerConnections, SynthesizerDescription, TemporalSettings, TimeCodeDef, TuneStatus, VectorStream, ViewAngleAspectCameraData, ViewPlaneCameraData */
    public static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("setIntInArray");
    }

    /** used by QTUtils */
    public static void setIntInArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("setIntInArray");
    }

    /** used by QTUtils */
    public static void setIntInArray(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("setIntInArray");
    }

    /** used by CursorRecord, QTHandleRef */
    public static void setIntInHandle(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("setIntInHandle");
    }

    /** used by MediaEQSpectrumBands, MoviesDispatcher, QDGraphics, QTPointerRef, QTUtils, QTVRInterceptRecord, SGDispatcher, SPB */
    public static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("setIntInPointer");
    }

    /** used by ICMFrameTime, QTByteObject, QTUtils, SGOutput, SequenceGrabber */
    public static void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong) {
        throw new UnsupportedToolboxException("setLongInArray");
    }

    /** used by QTUtils */
    public static void setLongInArray(float[] paramArrayOfFloat, int paramInt, long paramLong) {
        throw new UnsupportedToolboxException("setLongInArray");
    }

    /** used by QTUtils */
    public static void setLongInArray(int[] paramArrayOfInt, int paramInt, long paramLong) {
        throw new UnsupportedToolboxException("setLongInArray");
    }

    /** used by QTHandleRef */
    public static void setLongInHandle(int paramInt1, int paramInt2, long paramLong) {
        throw new UnsupportedToolboxException("setLongInHandle");
    }

    /** used by QTPointerRef, QTUtils */
    public static void setLongInPointer(int paramInt1, int paramInt2, long paramLong) {
        throw new UnsupportedToolboxException("setLongInPointer");
    }

    /** used by CodecInfo, DigitizerInfo, GraphicsMode, ICMPixelFormatInfo, MusicMIDIPacket, NoteRequestInfo, OpenCPicParams, Pict, QDColor, QDRect, QTByteObject, QTUtils, ScrpSTElement, SoundComponentData, SpatialSettings, SynthesizerConnections, SynthesizerDescription, TCTextOptions, TuneStatus */
    public static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("setShortInArray");
    }

    /** used by QTUtils */
    public static void setShortInArray(float[] paramArrayOfFloat, int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("setShortInArray");
    }

    /** used by QTUtils */
    public static void setShortInArray(int[] paramArrayOfInt, int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("setShortInArray");
    }

    /** used by QTHandleRef */
    public static void setShortInHandle(int paramInt1, int paramInt2, short paramShort) {
        throw new UnsupportedToolboxException("setShortInHandle");
    }

    /** used by MoviesDispatcher, QTPointerRef, QTUtils, SPB */
    public static void setShortInPointer(int paramInt1, int paramInt2, short paramShort) {
        throw new UnsupportedToolboxException("setShortInPointer");
    }

}
