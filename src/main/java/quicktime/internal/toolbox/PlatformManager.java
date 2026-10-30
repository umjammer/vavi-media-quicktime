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
    public static short BeginFullScreen(int[] restoreState, int whichGD, short[] desiredWidth, short[] desiredHeight, int newWindow, int eraseColor, int flags) {
        throw new UnsupportedToolboxException("BeginFullScreen");
    }

    /** used by QTSession, WinNativeGraphics */
    public static int CreatePortAssociation(int theWnd, int storage, int flags) {
        throw new UnsupportedToolboxException("CreatePortAssociation");
    }

    /** used by WinJNINative */
    public static void DestroyHWnd(int hWnd) {
        throw new UnsupportedToolboxException("DestroyHWnd");
    }

    /** used by QTSession, WinNativeGraphics */
    public static void DestroyPortAssociation(int cgp) {
        throw new UnsupportedToolboxException("DestroyPortAssociation");
    }

    /** used by WinJNINative */
    public static void DestroyPortAssociations() {

    }

    /** used by MethodClosure */
    public static void DisposeMethodClosure(int proc) {
        Closures.dispose(proc);
    }

    /** used by WinJNINative */
    public static int DoSecurityInit() {
        return 0;
    }

    /** used by FullScreenWindow */
    public static void DrawMenuBar() {

    }

    /** used by FullScreen */
    public static short EndFullScreen(int fullState, int flags) {
        throw new UnsupportedToolboxException("EndFullScreen");
    }

    /** used by QTSession */
    public static short Gestalt(int selector, int[] response) {
        return Gestalts.gestalt(selector, response);
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
    public static int GetNativeWindowPort(int nativeWindow) {
        throw new UnsupportedToolboxException("GetNativeWindowPort");
    }

    /** used by ParameterDialog */
    public static boolean GetNextEvent(short eventMask, byte[] theEvent) {
        return false;
    }

    /** used by QTPreferences */
    public static short GetQuickTimePreference(int type, int[] container) {
        return Gestalts.getPreference(type, container);
    }

    /** used by HIToolBoxFunctions */
    public static int InstallEventLoopTimer(int inEventLoop, double delay, double interval, int proc, int userData, int[] outTimer) {
        return EventLoop.install(delay, interval, proc, userData, outTimer);
    }

    /** used by MethodClosure */
    public static int NewMethodClosure(Object target, String name, String descriptor) {
        return Closures.newClosure(target, name, descriptor);
    }

    /** used by QTPreferences */
    public static short QTGetAccessKeys(byte[] accessKeyType, int flags, int[] keys) {
        throw new UnsupportedToolboxException("QTGetAccessKeys");
    }

    /** used by RestrictionSet */
    public static short QTGetMovieRestrictions(int movie, int[] outRestrictionSet, int[] outSeed) {
        throw new UnsupportedToolboxException("QTGetMovieRestrictions");
    }

    /** used by RestrictionSet */
    public static short QTGetSupportedRestrictions(int inRestrictionClass, int[] outRestrictionIDs) {
        throw new UnsupportedToolboxException("QTGetSupportedRestrictions");
    }

    /** used by QTPreferences */
    public static short QTRegisterAccessKey(byte[] accessKeyType, int flags, int accessKey) {
        throw new UnsupportedToolboxException("QTRegisterAccessKey");
    }

    /** used by RestrictionSet */
    public static short QTRestrictionsGetInfo(int inRestrictionSet, int[] outRestrictionClassCount, int[] outSeed) {
        throw new UnsupportedToolboxException("QTRestrictionsGetInfo");
    }

    /** used by RestrictionSet */
    public static short QTRestrictionsGetItem(int inRestrictionSet, int inRestrictionClass, int[] outRestrictions) {
        throw new UnsupportedToolboxException("QTRestrictionsGetItem");
    }

    /** used by QTPreferences */
    public static short QTUnregisterAccessKey(byte[] accessKeyType, int flags, int accessKey) {
        throw new UnsupportedToolboxException("QTUnregisterAccessKey");
    }

    /** used by QTCanvas */
    public static void RemoveContext(Object context) {

    }

    /** used by HIToolBoxFunctions */
    public static int RemoveEventLoopTimer(int ref) {
        return EventLoop.remove(ref);
    }

    /** used by QTJavaJNIAWTCanvas */
    public static void RemoveNewContext(Object context) {

    }

    /** used by HIToolBoxFunctions */
    public static int SetEventLoopTimerNextFireTime(int ref, double delay) {
        return EventLoop.setNextFireTime(ref, delay);
    }

    /** used by QTSession */
    public static void SetJavaThreadCallbackProc() {

    }

    /** used by QTPreferences */
    public static short SetQuickTimePreference(int type, int container) {
        return Gestalts.setPreference(type, container);
    }

    /** used by HIToolBoxFunctions */
    public static void _UseMainRunLoopForCurrentThread() {

    }

    /** used by QTJavaCocoaCanvas */
    public static int createImageNSViewNative(int width, int height, Object callback) {
        throw new UnsupportedToolboxException("createImageNSViewNative");
    }

    /** used by QTJavaCocoaCanvas */
    public static int createNSViewNative(float width, float height, Object movieView, Object callback) {
        throw new UnsupportedToolboxException("createNSViewNative");
    }

    /** used by QTJavaCocoaCanvas */
    public static int createSequenceGrabberNSView(int width, int height, Object callback) {
        throw new UnsupportedToolboxException("createSequenceGrabberNSView");
    }

    /** used by WinNativeHelper */
    public static void freeDrawingSurface(Component component, int ds) {
        throw new UnsupportedToolboxException("freeDrawingSurface");
    }

    /** used by WinNativeHelper */
    public static void getBounds(Component component, int ds, int[] bounds) {
        throw new UnsupportedToolboxException("getBounds");
    }

    /** used by WinNativeHelper */
    public static int getCompHWnd(Component component) {
        throw new UnsupportedToolboxException("getCompHWnd");
    }

    /** used by WinNativeHelper */
    public static int getDrawingSurface(Component component) {
        throw new UnsupportedToolboxException("getDrawingSurface");
    }

    /** used by WinNativeHelper */
    public static int getDrawingSurfaceHWnd(Component component, int ds) {
        throw new UnsupportedToolboxException("getDrawingSurfaceHWnd");
    }

    /** used by QTJavaCocoaCanvas */
    public static Object getMovieController(int movie) {
        throw new UnsupportedToolboxException("getMovieController");
    }

    /** used by QTJavaCocoaCanvas */
    public static void initGlobalLock(Object lock) {

    }

    /** used by QTJavaCocoaCanvas */
    public static void initGlobalOffScreen(Object offScreen) {
        throw new UnsupportedToolboxException("initGlobalOffScreen");
    }

    /** used by JDirectLinker */
    public static int loadNativeLibrary(String libName) {
        throw new UnsupportedToolboxException("loadNativeLibrary");
    }

    /** used by WinNativeHelper */
    public static int lockDrawingSurface(Component component, int ds) {
        throw new UnsupportedToolboxException("lockDrawingSurface");
    }

    /** used by QTJavaCocoaCanvas */
    public static void removeMovie(int movie) {
        throw new UnsupportedToolboxException("removeMovie");
    }

    /** used by QTJavaCocoaCanvas */
    public static void setFullScreen(byte fullScreen) {
        throw new UnsupportedToolboxException("setFullScreen");
    }

    /** used by QTJavaCocoaCanvas */
    public static void setMovie(int movie, Object movieObject) {
        throw new UnsupportedToolboxException("setMovie");
    }

    /** used by QTJavaCocoaCanvas */
    public static void setMovieController(int controller, Object controllerObject) {
        throw new UnsupportedToolboxException("setMovieController");
    }

    /** used by JDirectLinker */
    public static void unloadNativeLibrary(int library) {
        throw new UnsupportedToolboxException("unloadNativeLibrary");
    }

    /** used by WinNativeHelper */
    public static void unlockDrawingSurface(Component component, int ds) {
        throw new UnsupportedToolboxException("unlockDrawingSurface");
    }

}
