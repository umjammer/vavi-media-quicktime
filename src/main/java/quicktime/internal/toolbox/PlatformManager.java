/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package quicktime.internal.toolbox;

import java.awt.Component;


/**
 * PlatformManager.
 * <p>
 * Java replacement for the QuickTime native functions of this subsystem.
 * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.
 */
public final class PlatformManager {

    private PlatformManager() {
    }

    /** used by FullScreen */
    public static short BeginFullScreen(int[] paramArrayOfInt, int paramInt1, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int paramInt2, int paramInt3, int paramInt4) {
        throw new UnsupportedToolboxException("BeginFullScreen");
    }

    /** used by QTSession, WinNativeGraphics */
    public static int CreatePortAssociation(int paramInt1, int paramInt2, int paramInt3) {
        throw new UnsupportedToolboxException("CreatePortAssociation");
    }

    /** used by WinJNINative */
    public static void DestroyHWnd(int paramInt) {
        throw new UnsupportedToolboxException("DestroyHWnd");
    }

    /** used by QTSession, WinNativeGraphics */
    public static void DestroyPortAssociation(int paramInt) {
        throw new UnsupportedToolboxException("DestroyPortAssociation");
    }

    /** used by WinJNINative */
    public static void DestroyPortAssociations() {

    }

    /** used by MethodClosure */
    public static void DisposeMethodClosure(int paramInt) {
        Closures.dispose(paramInt);
    }

    /** used by WinJNINative */
    public static int DoSecurityInit() {
        return 0;
    }

    /** used by FullScreenWindow */
    public static void DrawMenuBar() {

    }

    /** used by FullScreen */
    public static short EndFullScreen(int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("EndFullScreen");
    }

    /** used by QTSession */
    public static short Gestalt(int paramInt, int[] paramArrayOfInt) {
        return Gestalts.gestalt(paramInt, paramArrayOfInt);
    }

    /** used by WinJNINative */
    public static int GetHWND() {
        throw new UnsupportedToolboxException("GetHWND");
    }

    /** used by HIToolBoxFunctions, ToneDescription */
    public static int GetMainEventLoop() {
        return EventLoop.MAIN;
    }

    /** used by QDGraphics, WinNativeGraphics */
    public static int GetNativeWindowPort(int paramInt) {
        throw new UnsupportedToolboxException("GetNativeWindowPort");
    }

    /** used by ParameterDialog */
    public static boolean GetNextEvent(short paramShort, byte[] paramArrayOfByte) {
        return false;
    }

    /** used by QTPreferences */
    public static short GetQuickTimePreference(int paramInt, int[] paramArrayOfInt) {
        return Gestalts.getPreference(paramInt, paramArrayOfInt);
    }

    /** used by HIToolBoxFunctions */
    public static int InstallEventLoopTimer(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return EventLoop.install(paramDouble1, paramDouble2, paramInt2, paramInt3, paramArrayOfInt);
    }

    /** used by MethodClosure */
    public static int NewMethodClosure(Object paramObject, String paramString1, String paramString2) {
        return Closures.newClosure(paramObject, paramString1, paramString2);
    }

    /** used by QTPreferences */
    public static short QTGetAccessKeys(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTGetAccessKeys");
    }

    /** used by RestrictionSet */
    public static short QTGetMovieRestrictions(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTGetMovieRestrictions");
    }

    /** used by RestrictionSet */
    public static short QTGetSupportedRestrictions(int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTGetSupportedRestrictions");
    }

    /** used by QTPreferences */
    public static short QTRegisterAccessKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTRegisterAccessKey");
    }

    /** used by RestrictionSet */
    public static short QTRestrictionsGetInfo(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        throw new UnsupportedToolboxException("QTRestrictionsGetInfo");
    }

    /** used by RestrictionSet */
    public static short QTRestrictionsGetItem(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("QTRestrictionsGetItem");
    }

    /** used by QTPreferences */
    public static short QTUnregisterAccessKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        throw new UnsupportedToolboxException("QTUnregisterAccessKey");
    }

    /** used by QTCanvas */
    public static void RemoveContext(Object paramObject) {

    }

    /** used by HIToolBoxFunctions */
    public static int RemoveEventLoopTimer(int paramInt) {
        return EventLoop.remove(paramInt);
    }

    /** used by QTJavaJNIAWTCanvas */
    public static void RemoveNewContext(Object paramObject) {

    }

    /** used by HIToolBoxFunctions */
    public static int SetEventLoopTimerNextFireTime(int paramInt, double paramDouble) {
        return EventLoop.setNextFireTime(paramInt, paramDouble);
    }

    /** used by QTSession */
    public static void SetJavaThreadCallbackProc() {

    }

    /** used by QTPreferences */
    public static short SetQuickTimePreference(int paramInt1, int paramInt2) {
        return Gestalts.setPreference(paramInt1, paramInt2);
    }

    /** used by HIToolBoxFunctions */
    public static void _UseMainRunLoopForCurrentThread() {

    }

    /** used by QTJavaCocoaCanvas */
    public static int createImageNSViewNative(int paramInt1, int paramInt2, Object paramObject) {
        throw new UnsupportedToolboxException("createImageNSViewNative");
    }

    /** used by QTJavaCocoaCanvas */
    public static int createNSViewNative(float paramFloat1, float paramFloat2, Object paramObject1, Object paramObject2) {
        throw new UnsupportedToolboxException("createNSViewNative");
    }

    /** used by QTJavaCocoaCanvas */
    public static int createSequenceGrabberNSView(int paramInt1, int paramInt2, Object paramObject) {
        throw new UnsupportedToolboxException("createSequenceGrabberNSView");
    }

    /** used by WinNativeHelper */
    public static void freeDrawingSurface(Component paramComponent, int paramInt) {
        throw new UnsupportedToolboxException("freeDrawingSurface");
    }

    /** used by WinNativeHelper */
    public static void getBounds(Component paramComponent, int paramInt, int[] paramArrayOfInt) {
        throw new UnsupportedToolboxException("getBounds");
    }

    /** used by WinNativeHelper */
    public static int getCompHWnd(Component paramComponent) {
        throw new UnsupportedToolboxException("getCompHWnd");
    }

    /** used by WinNativeHelper */
    public static int getDrawingSurface(Component paramComponent) {
        throw new UnsupportedToolboxException("getDrawingSurface");
    }

    /** used by WinNativeHelper */
    public static int getDrawingSurfaceHWnd(Component paramComponent, int paramInt) {
        throw new UnsupportedToolboxException("getDrawingSurfaceHWnd");
    }

    /** used by QTJavaCocoaCanvas */
    public static Object getMovieController(int paramInt) {
        throw new UnsupportedToolboxException("getMovieController");
    }

    /** used by QTJavaCocoaCanvas */
    public static void initGlobalLock(Object paramObject) {

    }

    /** used by QTJavaCocoaCanvas */
    public static void initGlobalOffScreen(Object paramObject) {
        throw new UnsupportedToolboxException("initGlobalOffScreen");
    }

    /** used by JDirectLinker */
    public static int loadNativeLibrary(String paramString) {
        throw new UnsupportedToolboxException("loadNativeLibrary");
    }

    /** used by WinNativeHelper */
    public static int lockDrawingSurface(Component paramComponent, int paramInt) {
        throw new UnsupportedToolboxException("lockDrawingSurface");
    }

    /** used by QTJavaCocoaCanvas */
    public static void removeMovie(int paramInt) {
        throw new UnsupportedToolboxException("removeMovie");
    }

    /** used by QTJavaCocoaCanvas */
    public static void setFullScreen(byte paramByte) {
        throw new UnsupportedToolboxException("setFullScreen");
    }

    /** used by QTJavaCocoaCanvas */
    public static void setMovie(int paramInt, Object paramObject) {
        throw new UnsupportedToolboxException("setMovie");
    }

    /** used by QTJavaCocoaCanvas */
    public static void setMovieController(int paramInt, Object paramObject) {
        throw new UnsupportedToolboxException("setMovieController");
    }

    /** used by JDirectLinker */
    public static void unloadNativeLibrary(int paramInt) {
        throw new UnsupportedToolboxException("unloadNativeLibrary");
    }

    /** used by WinNativeHelper */
    public static void unlockDrawingSurface(Component paramComponent, int paramInt) {
        throw new UnsupportedToolboxException("unlockDrawingSurface");
    }

}
