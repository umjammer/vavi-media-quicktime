/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * QuickDrawManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class QuickDrawManager {

    private QuickDrawManager() {
    }

    /** used by ParameterDialog */
    public static void BeginUpdate(int paramInt) {
        throw new UnsupportedToolboxException("BeginUpdate");
    }

    /** used by Region */
    public static short BitMapToRegion(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("BitMapToRegion");
    }

    /** used by QDGraphics */
    public static short CharWidth(short paramShort) {
        throw new UnsupportedToolboxException("CharWidth");
    }

    /** used by QDGraphics */
    public static void ClipRect(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ClipRect");
    }

    /** used by QDGraphics */
    public static void ClipRect(int paramInt) {
        throw new UnsupportedToolboxException("ClipRect");
    }

    /** used by Pict */
    public static void ClosePicture() {
        throw new UnsupportedToolboxException("ClosePicture");
    }

    /** used by Polygon */
    public static void ClosePoly() {
        throw new UnsupportedToolboxException("ClosePoly");
    }

    /** used by Region */
    public static void CloseRgn(int paramInt) {
        throw new UnsupportedToolboxException("CloseRgn");
    }

    /** used by QDGraphics, Region */
    public static void CopyRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("CopyRgn");
    }

    /** used by Region */
    public static void DiffRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("DiffRgn");
    }

    /** used by QTObjectManagement */
    public static void DisposeCTable(int paramInt) {
        throw new UnsupportedToolboxException("DisposeCTable");
    }

    /** used by QTObjectManagement */
    public static void DisposeGWorld(int paramInt) {
        throw new UnsupportedToolboxException("DisposeGWorld");
    }

    /** used by QTObjectManagement */
    public static void DisposeMatte(int paramInt) {
        throw new UnsupportedToolboxException("DisposeMatte");
    }

    /** used by QTObjectManagement, Region */
    public static void DisposeRgn(int paramInt) {
        throw new UnsupportedToolboxException("DisposeRgn");
    }

    /** used by QDGraphics */
    public static void DrawChar(short paramShort) {
        throw new UnsupportedToolboxException("DrawChar");
    }

    /** used by Pict */
    public static void DrawPicture(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("DrawPicture");
    }

    /** used by QDGraphics */
    public static void DrawText(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("DrawText");
    }

    /** used by Pict */
    public static short DrawTrimmedPicture(int paramInt1, byte[] paramArrayOfByte, int paramInt2, short paramShort, int paramInt3) {
        throw new UnsupportedToolboxException("DrawTrimmedPicture");
    }

    /** used by Region */
    public static byte EmptyRgn(int paramInt) {
        throw new UnsupportedToolboxException("EmptyRgn");
    }

    /** used by ParameterDialog */
    public static void EndUpdate(int paramInt) {
        throw new UnsupportedToolboxException("EndUpdate");
    }

    /** used by QDGraphics, Region */
    public static byte EqualRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("EqualRgn");
    }

    /** used by QDGraphics */
    public static void EraseArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("EraseArc");
    }

    /** used by QDGraphics */
    public static void EraseOval(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("EraseOval");
    }

    /** used by QDGraphics */
    public static void ErasePoly(int paramInt) {
        throw new UnsupportedToolboxException("ErasePoly");
    }

    /** used by QDGraphics */
    public static void EraseRect(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("EraseRect");
    }

    /** used by QDGraphics */
    public static void EraseRgn(int paramInt) {
        throw new UnsupportedToolboxException("EraseRgn");
    }

    /** used by QDGraphics */
    public static void EraseRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("EraseRoundRect");
    }

    /** used by QDGraphics */
    public static void FrameArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("FrameArc");
    }

    /** used by QDGraphics */
    public static void FrameOval(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("FrameOval");
    }

    /** used by QDGraphics */
    public static void FramePoly(int paramInt) {
        throw new UnsupportedToolboxException("FramePoly");
    }

    /** used by QDGraphics */
    public static void FrameRect(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("FrameRect");
    }

    /** used by QDGraphics */
    public static void FrameRgn(int paramInt) {
        throw new UnsupportedToolboxException("FrameRgn");
    }

    /** used by QDGraphics */
    public static void FrameRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("FrameRoundRect");
    }

    /** used by GDevice */
    public static short GDGetScale(int paramInt, int[] paramArrayOfInt, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GDGetScale");
    }

    /** used by GDevice */
    public static short GDHasScale(int paramInt, short paramShort, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GDHasScale");
    }

    /** used by GDevice */
    public static short GDSetScale(int paramInt1, int paramInt2, short paramShort) {
        throw new UnsupportedToolboxException("GDSetScale");
    }

    /** used by QDGraphics */
    public static void GetBackColor(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetBackColor");
    }

    /** used by QDGraphics */
    public static void GetCPixel(short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetCPixel");
    }

    /** used by QDGraphics */
    public static void GetClip(int paramInt) {
        throw new UnsupportedToolboxException("GetClip");
    }

    /** used by GDevice */
    public static int GetDeviceList() {
        throw new UnsupportedToolboxException("GetDeviceList");
    }

    /** used by QDFont */
    public static void GetFNum(byte[] paramArrayOfByte, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GetFNum");
    }

    /** used by QDGraphics */
    public static void GetFontInfo(short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("GetFontInfo");
    }

    /** used by QDFont */
    public static void GetFontName(short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetFontName");
    }

    /** used by QDGraphics */
    public static void GetForeColor(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetForeColor");
    }

    /** used by GDevice */
    public static int GetGDevice() {
        throw new UnsupportedToolboxException("GetGDevice");
    }

    /** used by CSequence, DSequence, GraphicsExporter, GraphicsImporter, ImageDescription, Movie, MovieController, ParameterDialog, Pict, Polygon, QDGraphics, QTHandleRef, QTImage, Region, SCSequence, SequenceGrabber, SetGWorld, TextMediaHandler */
    public static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("GetGWorld");
    }

    /** used by Pict, PixMap, QDGraphics */
    public static int GetGWorldPixMap(int paramInt) {
        throw new UnsupportedToolboxException("GetGWorldPixMap");
    }

    /** used by GDevice */
    public static int GetMainDevice() {
        throw new UnsupportedToolboxException("GetMainDevice");
    }

    /** used by GDevice */
    public static int GetMaxDevice(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetMaxDevice");
    }

    /** used by GDevice */
    public static int GetNextDevice(int paramInt) {
        throw new UnsupportedToolboxException("GetNextDevice");
    }

    /** used by RawEncodedImage */
    public static int GetPixBaseAddr(int paramInt) {
        throw new UnsupportedToolboxException("GetPixBaseAddr");
    }

    /** used by PixMap */
    public static int GetPixBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetPixBounds");
    }

    /** used by QDGraphics */
    public static void GetPort(int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("GetPort");
    }

    /** used by QDGraphics */
    public static int GetPortBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetPortBounds");
    }

    /** used by QDGraphics */
    public static int GetPortClipRegion(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetPortClipRegion");
    }

    /** used by QDGraphics */
    public static int GetPortVisibleRegion(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("GetPortVisibleRegion");
    }

    /** used by Region */
    public static byte GetRegionBounds(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("GetRegionBounds");
    }

    /** used by AtomContainer */
    public static short ITextAddString(int paramInt1, int paramInt2, short paramShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ITextAddString");
    }

    /** used by AtomContainer */
    public static short ITextGetString(int paramInt1, int paramInt2, short paramShort, short[] paramArrayOfShort, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("ITextGetString");
    }

    /** used by AtomContainer */
    public static short ITextRemoveString(int paramInt1, int paramInt2, short paramShort, int paramInt3) {
        throw new UnsupportedToolboxException("ITextRemoveString");
    }

    /** used by GDevice */
    public static void InitGDevice(short paramShort, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("InitGDevice");
    }

    /** used by Region */
    public static void InsetRgn(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("InsetRgn");
    }

    /** used by ParameterDialog */
    public static void InvalRect(short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("InvalRect");
    }

    /** used by QDGraphics */
    public static void InvertArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("InvertArc");
    }

    /** used by QDGraphics */
    public static void InvertOval(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("InvertOval");
    }

    /** used by QDGraphics */
    public static void InvertPoly(int paramInt) {
        throw new UnsupportedToolboxException("InvertPoly");
    }

    /** used by QDGraphics */
    public static void InvertRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("InvertRoundRect");
    }

    /** used by QDGraphics */
    public static byte IsPortOffscreen(int paramInt) {
        throw new UnsupportedToolboxException("IsPortOffscreen");
    }

    /** used by Region */
    public static byte IsRegionRectangular(int paramInt) {
        throw new UnsupportedToolboxException("IsRegionRectangular");
    }

    /** used by QTObjectManagement */
    public static void KillPicture(int paramInt) {
        throw new UnsupportedToolboxException("KillPicture");
    }

    /** used by QTObjectManagement */
    public static void KillPoly(int paramInt) {
        throw new UnsupportedToolboxException("KillPoly");
    }

    /** used by QDGraphics */
    public static void Line(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("Line");
    }

    /** used by QDGraphics */
    public static void LineTo(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("LineTo");
    }

    /** used by Pict, PixMap, QDGraphics */
    public static byte LockPixels(int paramInt) {
        throw new UnsupportedToolboxException("LockPixels");
    }

    /** used by Region */
    public static void MacCopyRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MacCopyRgn");
    }

    /** used by QDGraphics */
    public static void MacDrawText(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("MacDrawText");
    }

    /** used by Region */
    public static byte MacEqualRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("MacEqualRgn");
    }

    /** used by QDGraphics */
    public static void MacFrameRect(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("MacFrameRect");
    }

    /** used by QDGraphics */
    public static void MacFrameRgn(int paramInt) {
        throw new UnsupportedToolboxException("MacFrameRgn");
    }

    /** used by QDGraphics */
    public static void MacLineTo(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("MacLineTo");
    }

    /** used by Region */
    public static void MacOffsetRgn(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("MacOffsetRgn");
    }

    /** used by QDGraphics */
    public static void MacPaintRgn(int paramInt) {
        throw new UnsupportedToolboxException("MacPaintRgn");
    }

    /** used by ParameterDialog */
    public static void MacSetPort(int paramInt) {
        throw new UnsupportedToolboxException("MacSetPort");
    }

    /** used by Region */
    public static void MacSetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
        throw new UnsupportedToolboxException("MacSetRectRgn");
    }

    /** used by Region */
    public static void MacUnionRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MacUnionRgn");
    }

    /** used by Region */
    public static void MacXorRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("MacXorRgn");
    }

    /** used by Region */
    public static void MapRgn(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        throw new UnsupportedToolboxException("MapRgn");
    }

    /** used by QDGraphics */
    public static void MeasureText(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("MeasureText");
    }

    /** used by QDGraphics */
    public static void Move(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("Move");
    }

    /** used by QDGraphics */
    public static void MoveTo(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("MoveTo");
    }

    /** used by QDGraphics */
    public static short NewImageGWorld(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("NewImageGWorld");
    }

    /** used by Region */
    public static int NewRgn() {
        throw new UnsupportedToolboxException("NewRgn");
    }

    /** used by Polygon */
    public static void OffsetPoly(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("OffsetPoly");
    }

    /** used by QDGraphics, Region */
    public static void OffsetRgn(int paramInt, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("OffsetRgn");
    }

    /** used by Pict */
    public static int OpenCPicture(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("OpenCPicture");
    }

    /** used by Polygon */
    public static int OpenPoly() {
        throw new UnsupportedToolboxException("OpenPoly");
    }

    /** used by Region */
    public static void OpenRgn() {
        throw new UnsupportedToolboxException("OpenRgn");
    }

    /** used by QDGraphics */
    public static void PaintArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("PaintArc");
    }

    /** used by QDGraphics */
    public static void PaintOval(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("PaintOval");
    }

    /** used by QDGraphics */
    public static void PaintPoly(int paramInt) {
        throw new UnsupportedToolboxException("PaintPoly");
    }

    /** used by QDGraphics */
    public static void PaintRect(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("PaintRect");
    }

    /** used by QDGraphics, Region */
    public static void PaintRgn(int paramInt) {
        throw new UnsupportedToolboxException("PaintRgn");
    }

    /** used by QDGraphics */
    public static void PaintRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("PaintRoundRect");
    }

    /** used by QDGraphics */
    public static void PenNormal() {
        throw new UnsupportedToolboxException("PenNormal");
    }

    /** used by QDGraphics */
    public static void PenSize(short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("PenSize");
    }

    /** used by Region */
    public static byte PtInRgn(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("PtInRgn");
    }

    /** used by Pict */
    public static short QDError() {
        throw new UnsupportedToolboxException("QDError");
    }

    /** used by MovieController */
    public static void QDFlushPortBuffer(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QDFlushPortBuffer");
    }

    /** used by QDGraphics */
    public static short QTNewGWorld(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("QTNewGWorld");
    }

    /** used by QDGraphics */
    public static void RGBBackColor(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("RGBBackColor");
    }

    /** used by QDGraphics */
    public static void RGBForeColor(byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("RGBForeColor");
    }

    /** used by Region */
    public static byte RectInRgn(byte[] paramArrayOfByte, int paramInt) {
        throw new UnsupportedToolboxException("RectInRgn");
    }

    /** used by Region */
    public static void RectRgn(int paramInt, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("RectRgn");
    }

    /** used by QDGraphics, Region */
    public static void SectRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("SectRgn");
    }

    /** used by QDGraphics */
    public static void SetCPixel(short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        throw new UnsupportedToolboxException("SetCPixel");
    }

    /** used by QDGraphics */
    public static void SetClip(int paramInt) {
        throw new UnsupportedToolboxException("SetClip");
    }

    /** used by GDevice */
    public static void SetDeviceAttribute(int paramInt, short paramShort, byte paramByte) {
        throw new UnsupportedToolboxException("SetDeviceAttribute");
    }

    /** used by Region */
    public static void SetEmptyRgn(int paramInt) {
        throw new UnsupportedToolboxException("SetEmptyRgn");
    }

    /** used by GDevice */
    public static void SetGDevice(int paramInt) {
        throw new UnsupportedToolboxException("SetGDevice");
    }

    /** used by CSequence, DSequence, GraphicsExporter, GraphicsImporter, ImageDescription, Movie, MovieController, ParameterDialog, Pict, Polygon, QDGraphics, QTHandleRef, QTImage, Region, SCSequence, SequenceGrabber, SetGWorld, TextMediaHandler */
    public static void SetGWorld(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("SetGWorld");
    }

    /** used by ParameterDialog */
    public static void SetPort(int paramInt) {
        throw new UnsupportedToolboxException("SetPort");
    }

    /** used by QDGraphics, Region */
    public static void SetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
        throw new UnsupportedToolboxException("SetRectRgn");
    }

    /** used by QDGraphics */
    public static void StdText(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("StdText");
    }

    /** used by QDGraphics */
    public static short StdTxMeas(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, short[] paramArrayOfShort) {
        throw new UnsupportedToolboxException("StdTxMeas");
    }

    /** used by GDevice */
    public static int TestDeviceAttribute(int paramInt, short paramShort) {
        throw new UnsupportedToolboxException("TestDeviceAttribute");
    }

    /** used by QDGraphics */
    public static void TextFace(short paramShort) {
        throw new UnsupportedToolboxException("TextFace");
    }

    /** used by QDGraphics */
    public static void TextFont(short paramShort) {
        throw new UnsupportedToolboxException("TextFont");
    }

    /** used by QDGraphics */
    public static void TextMode(short paramShort) {
        throw new UnsupportedToolboxException("TextMode");
    }

    /** used by QDGraphics */
    public static void TextSize(short paramShort) {
        throw new UnsupportedToolboxException("TextSize");
    }

    /** used by QDGraphics */
    public static short TextWidth(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        throw new UnsupportedToolboxException("TextWidth");
    }

    /** used by QDGraphics, Region */
    public static void UnionRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("UnionRgn");
    }

    /** used by Pict, PixMap */
    public static void UnlockPixels(int paramInt) {
        throw new UnsupportedToolboxException("UnlockPixels");
    }

    /** used by QDGraphics, Region */
    public static void XorRgn(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("XorRgn");
    }

}
