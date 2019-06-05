
package quicktime;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import quicktime.app.time.TaskThread;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.QuickTimeVRLib;
import quicktime.jdirect.SoundLib;
import quicktime.qd.GDevice;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd3d.QD3DException;
import quicktime.util.QTBuild;
import quicktime.util.QTUtils;
import sun.awt.DrawingSurface;
import sun.awt.DrawingSurfaceInfo;
import sun.awt.Win32DrawingSurface;


public final class QTSession implements QuickTimeLib, QuickTimeVRLib, QuickDraw3DLib {
    private static Object linkage;

    static {

        if ((isCurrentOS(4)) && (getJavaVersion() >= 65540)) {
            Toolkit.getDefaultToolkit();
        }

        if (isCurrentOS(2)) {
            if (QuickTimeLib.libraryInstance == null) {
                throw new UnsatisfiedLinkError("QuickTimeLib");
            }
            if (InterfaceLib.libraryInstance == null) {
                throw new UnsatisfiedLinkError("InterfaceLib");
            }
            if (PrimitivesLib.libraryInstance == null) {
                throw new UnsatisfiedLinkError("PrimitivesLib");
            }
            if (SoundLib.libraryInstance == null) {
                throw new UnsatisfiedLinkError("SoundLib");
            }

            if (System.getSecurityManager() != null) {
                if (getJavaVersion() == 65537) {
                    if (WinJNINative.DoSecurityInit() != 0) {
                        throw new SecurityException("quicktime.QTSession");

                    }

                } else {
                    class PrivelegedAction {
                        void establish() {
                            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                                public Void run() {
                                    System.setProperty("restrict.package.definition.quicktime", "true");
                                    return null;
                                }
                            });
                        }
                    }
                    new PrivelegedAction().establish();
                }
            }
        }

        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTSession.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    private static void doVMCheck() {
        if (!doneVMCheck) {
            String str1 = System.getProperty("java.version");
            int i = 0;
            while (!Character.isDigit(str1.charAt(i))) {
                i++;
            }
            int j = Character.getNumericValue(str1.charAt(i));
            jVersion = j << 16;

            int k = 0;

            if (str1.length() > i + 1) {
                i++;

                while ((str1.length() > i) && (!Character.isDigit(str1.charAt(i)))) {
                    i++;
                }
                if (str1.length() > i) {
                    k = Character.getNumericValue(str1.charAt(i));
                }
            }

            jVersion |= k;
            if ((j < 1) || ((j == 1) && (k < 1))) {
                throw new QTRuntimeException("QTJava requires Java Version 1.1 or greater");
            }

            String str2 = System.getProperty("java.vendor");
            String str3 = System.getProperty("os.name");
            if (isCurrentOS(2)) {
                if ((str2.startsWith("Netscape")) || (str2.startsWith("Microsoft"))) {
                    throw new QTRuntimeException("QTJava requires Sun Java VM 1.1 or greater");
                }
            } else if (!str2.startsWith("Apple ")) {
                throw new QTRuntimeException("QTJava requires Apple Java VM 1.1 or greater");
            }

            doneVMCheck = true;
        }
    }

    private static boolean doneVMCheck = false;

    private static boolean qtPresent = false;

    private static boolean gestaltDone = false;

    private static boolean vrinitialized = false;

    private static int initDone = 0;

    private static boolean threeDInitialised = false;

    private static boolean enterCalled = false;

    private static int version;

    private static int osConstant = 0;

    private static int jVersion;

    private static Class theClass = null;

    public static final int kMacOS = 1;

    public static final int kWin32 = 2;

    public static final int kMacOSX = 4;

    public static final int kInitVR = 1;

    public static final int kInit3D = 2;

    public static int getJavaVersion() {
        if (jVersion == 0)
            doVMCheck();
        return jVersion;
    }

    public static boolean isClassSigned() {
        if (theClass != null) {
            Object[] arrayOfObject = theClass.getSigners();
            return arrayOfObject != null;
        }
        return false;
    }

    public static boolean hasSecurityRestrictions() {
        SecurityManager localSecurityManager = null;
        if ((localSecurityManager = System.getSecurityManager()) != null) {
            try {
                localSecurityManager.checkExit(0);
            } catch (SecurityException localSecurityException) {
                return !isClassSigned();
            }
        }
        return false;
    }

    public static boolean canDoFullScreen() {
        if (System.getSecurityManager() != null) {
            return (isClassSigned()) || (System.getSecurityManager().checkTopLevelWindow(new Frame()));
        }
        return true;
    }

    public static boolean isCurrentOS(int paramInt) {
        if (osConstant <= 0) {
            if (osConstant == -1) {
                return false;
            }
            String str = System.getProperty("os.name");

            if ((str.regionMatches(false, 0, "Darwin", 0, 6)) || (str.regionMatches(false, 0, "Mac OS X", 0, 8))) {
                osConstant = 4;
            } else if (str.regionMatches(false, 0, "Mac", 0, 3)) {
                osConstant = 1;
            } else if (str.regionMatches(false, 0, "Win", 0, 3)) {
                osConstant = 2;
            } else {
                osConstant = -1;
            }
        }
        return (paramInt & osConstant) != 0;
    }

    public static boolean isInitialized() {
        return initDone != 0;
    }

    public static boolean isVRInitialized() {
        return vrinitialized;
    }

    public static boolean is3DInitialized() {
        return threeDInitialised;
    }

    public static void initialize(int paramInt) throws QTException {
        initialize(paramInt, null);
    }

    public static void initialize(int paramInt, Class paramClass) throws QTException {

        synchronized (terminationLock()) {
            if (initDone == 0) {
                if (paramClass != null) {
                    theClass = paramClass;
                    if (!isClassSigned())
                        throw new SecurityException(paramClass + ",is NOT SIGNED");
                }
                if (isCurrentOS(2)) {
                    int i = InitializeQTML(paramInt);
                    QTException.checkError(i);

                    try {
                        Class.forName("java.awt.Frame");
                        if (dispatchFrame == null) {
                            QTFrame localQTFrame = new QTFrame();
                            localQTFrame.setLayout(null);
                            localQTFrame.pack();
                            dispatchFrame = localQTFrame;
                        }
                    } catch (ClassNotFoundException localClassNotFoundException) {
                    }
                }

                QTObject.qtIsNOTInitialized = false;
                initDone = 1;
                sessionID += 1;

                if (!firstTime) {
                    QDGraphics.scratch = new QDGraphics(QDGraphics.kDefaultPixelFormat,
                                                        new QDRect(1, 1),
                                                        null,
                                                        GDevice.getMain(),
                                                        2);
                    QDGraphics.validScratch = new QDGraphics(QDGraphics.kDefaultPixelFormat,
                                                             new QDRect(1, 1),
                                                             null,
                                                             GDevice.getMain(),
                                                             2);
                }

                if ((isCurrentOS(1)) && (macQuitter == null))
                    macQuitter = new MacDoQuit();
            } else {
                initDone += 1;
            }
        }
    }

    static class QTFrame extends Frame {
        int HWND = 0;

        public void addNotify() {
            super.addNotify();
            Object localObject;
            if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 65540)) {
                localObject = new WinJNIDrawingSurface(this);
                ((WinJNIDrawingSurface) localObject).lock();
                this.HWND = ((WinJNIDrawingSurface) localObject).getHWnd();
                ((WinJNIDrawingSurface) localObject).unlock();
                QTSession.CreatePortAssociation(this.HWND, 0, 258);
            } else if ((getPeer() instanceof DrawingSurface)) {
                localObject = ((DrawingSurface) getPeer()).getDrawingSurfaceInfo();
                ((DrawingSurfaceInfo) localObject).lock();
                this.HWND = ((Win32DrawingSurface) ((DrawingSurfaceInfo) localObject).getSurface()).getHWnd();
                ((DrawingSurfaceInfo) localObject).unlock();

                QTSession.CreatePortAssociation(this.HWND, 0, 258);
            }
        }
    }

    private static boolean firstTime = true;

    private static boolean qtjwiredDebug = false;

    private static void JQTinitializeVR() throws QTException {
        if ((!isCurrentOS(4)) && (QuickTimeVRLib.libraryInstance == null)) {
            throw new UnsatisfiedLinkError("QuickTimeVRLib");
        }
        vrinitialized = true;
    }

    private static void JQTinitialize() throws QTException {

        synchronized (terminationLock()) {
            if (qtjwiredDebug)
                System.out.println("JQTinitialize...");
            if (initDone == 0) {
                if (isCurrentOS(2)) {
                    hWnd = WinJNINative.GetHWND();
                }

                int i = gestalt(1903454573);
                qtPresent = true;
                version = i;
                if ((getMajorVersion() < 4) || ((getMajorVersion() == 4) && (getMinorVersion() < 1))) {
                    if (qtjwiredDebug)
                        System.out.println("Version of QuickTime not found...");
                    JQTterminate();
                    throw new QTException("Version of QuickTime not found");
                }

                QTObject.qtIsNOTInitialized = false;
                initDone = 1;
                sessionID += 1;
                if (qtjwiredDebug) {
                    System.out.println("scratch depth 0x" + Integer.toHexString(QDGraphics.kDefaultPixelFormat));
                }
                if (!firstTime) {
                    QDGraphics.scratch = new QDGraphics(QDGraphics.kDefaultPixelFormat,
                                                        new QDRect(1, 1),
                                                        null,
                                                        GDevice.getMain(),
                                                        2);
                    if (qtjwiredDebug)
                        System.out.println("validScratch depth 0x" + Integer.toHexString(QDGraphics.kDefaultPixelFormat));
                    QDGraphics.validScratch = new QDGraphics(QDGraphics.kDefaultPixelFormat,
                                                             new QDRect(1, 1),
                                                             null,
                                                             GDevice.getMain(),
                                                             2);
                }
                if (qtjwiredDebug)
                    System.out.println("JQTinitialized");
            } else {
                initDone += 1;
            }
        }
    }

    private static int wPort = 0;

    private static int hWnd = 0;

    private static Object dispatchFrame;

    private static Object macQuitter;

    static int sessionID;

    public static void initializeVR() throws QTException {
        if (!isInitialized())
            initialize(0);
        if (!vrinitialized) {
            JQTinitializeVR();

            if (isCurrentOS(2)) {
                int i = InitializeQTVR();
                QTException.checkError(i);
            }
        }
    }

    public static void initialize3D() throws QTException {
        if (!isInitialized())
            initialize(0);
        if (!threeDInitialised) {
            if (isCurrentOS(1)) {
                gestalt(1902392164);
            } else if (isCurrentOS(2)) {
                if (QuickDraw3DLib.libraryInstance == null) {
                    throw new UnsatisfiedLinkError("QuickDraw3DLib");
                }
            } else {
                throw new QTRuntimeException("Unsupported OS");
            }
            int i = Q3Initialize();
            if (i == 0)
                throw new QD3DException(37049);
        }
        threeDInitialised = true;
    }

    public static void open() throws QTException {
        open(3, 0, 0);
    }

    public static void open(int paramInt) throws QTException {
        open(3, 0, paramInt);
    }

    public static void open(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        initialize(0);
        int i = gestalt(1903454573);
        qtPresent = true;
        version = i;
        if ((getMajorVersion() < paramInt1) || ((getMajorVersion() == paramInt1) && (getMinorVersion() < paramInt2))) {

            close();
            throw new QTException("Version of QuickTime not found");
        }

        enterMovies();
        enterCalled = true;
        if ((0x1 & paramInt3) != 0)
            initializeVR();
        if (((0x2 & paramInt3) != 0) && (!isCurrentOS(4))) {
            initialize3D();
        }
    }

    public static void open(Class paramClass) throws QTException {
        open(3, 0, 0, paramClass);
    }

    public static void open(int paramInt, Class paramClass) throws QTException {
        open(3, 0, paramInt, paramClass);
    }

    public static void open(int paramInt1, int paramInt2, int paramInt3, Class paramClass) throws QTException {
        initialize(0, paramClass);
        int i = gestalt(1903454573);
        qtPresent = true;
        version = i;
        if ((getMajorVersion() < paramInt1) || ((getMajorVersion() == paramInt1) && (getMinorVersion() < paramInt2))) {

            close();
            throw new QTException("Version of QuickTime not found");
        }
        enterMovies();
        enterCalled = true;
        if ((0x1 & paramInt3) != 0)
            initializeVR();
        if (((0x2 & paramInt3) != 0) && (!isCurrentOS(4))) {
            initialize3D();
        }
    }

    public static void terminate() {
        if (initDone > 1) {
            initDone -= 1;
        } else if (initDone == 1) {
            removeAllCanvases();
            TaskThread.killAllThreads();
            QTUtils.reclaimMemory();

            synchronized (terminationLock()) {

                QTObject.qtIsNOTInitialized = true;
                initDone = 0;
                synchronized (QTNative.globalsLock) {
                    QTObjectManagement.removeAll();
                }

                terminateVR();
                terminate3D();
                if (enterCalled) {
                    exitMovies();
                    enterCalled = false;
                }

                if (!firstTime) {
                    QDGraphics.scratch = null;
                    QDGraphics.validScratch = null;
                } else {
                    firstTime = false;
                }
                if (isCurrentOS(2)) {
                    WinJNINative.DestroyPortAssociations();
                    if ((dispatchFrame != null) && (wPort != 0)) {
                        DestroyPortAssociation(wPort);
                        wPort = 0;
                        ((Frame) dispatchFrame).dispose();
                        dispatchFrame = null;
                    }

                    TerminateQTML();
                }

                theClass = null;
            }
        }
    }

    private static void JQTterminate() {
        if (initDone > 1) {
            initDone -= 1;
        } else if (initDone == 1) {
            if (qtjwiredDebug)
                System.out.println("Doing Terminate");
            removeAllCanvases();
            TaskThread.killAllThreads();
            QTUtils.reclaimMemory();

            synchronized (terminationLock()) {
                QTObject.qtIsNOTInitialized = true;
                initDone = 0;

                QTObjectManagement.removeAll();
                if (qtjwiredDebug) {
                    System.out.println("After RemoveAll");
                }
                if (!firstTime) {
                    QDGraphics.scratch = null;
                    QDGraphics.validScratch = null;
                } else {
                    firstTime = false;
                }
                if (isCurrentOS(2)) {
                    WinJNINative.DestroyPortAssociations();
                    if (hWnd != 0) {
                        WinJNINative.DestroyHWnd(hWnd);
                        hWnd = 0;
                    }
                }

                theClass = null;
                vrinitialized = false;
            }

            if (qtjwiredDebug) {
                System.out.println(" JQTterminated");
            }
        }
    }

    public static Object terminationLock() {
        return termLock;
    }

    private static final Object termLock = new Object();

    public static void terminateVR() {
        if (vrinitialized) {
            QTUtils.reclaimMemory();
            if (isCurrentOS(2))
                TerminateQTVR();
            vrinitialized = false;
        }
    }

    public static void terminate3D() {
        if (threeDInitialised) {
            QTUtils.reclaimMemory();
            int i = Q3Exit();
            threeDInitialised = false;
        }
    }

    public static boolean isQD3DAvailable() throws QTException {
        if (!isCurrentOS(4)) {
            try {
                int i = gestalt(1902392164);
                return i == 1;
            } catch (QTException localQTException) {
                return false;
            }
        }
        return false;
    }

    public static boolean isQTVRAvailable() throws QTException {
        try {
            int i = gestalt(1903457906);
            int j = i & 0x1;
            return j != 0;
        } catch (QTException localQTException) {
        }
        return false;
    }

    public static final int gestalt(int paramInt) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = Gestalt(paramInt, arrayOfInt);
        QTException.checkError(i);
        return arrayOfInt[0];
    }

    public static final void enterMovies() throws QTException {

        synchronized (QTNative.globalsLock) {
            if ((isCurrentOS(4)) && (getQTVersion() >= 104857600)) {
                setJavaThreadHook();
            }
            QTException.checkError(EnterMovies());
        }
    }

    public static final int getMajorVersion() {
        return (version & 0xF000000) >> 24;
    }

    public static final int getMinorVersion() {
        return (version & 0xF00000) >> 20;
    }

    public static final int getBugFixVersion() {
        return (version & 0xF0000) >> 16;
    }

    public static final int getDeveloperVersion() {
        return version & 0xFFFF;
    }

    public static int getQTVersion() {
        return version;
    }

    public static int getQTMajorVersion() {
        return (version & 0xFF000000) >>> 24;
    }

    public static int getQTMinorVersion() {
        return (version & 0xFF0000) >>> 16;
    }

    private static Vector<Object> canvases = new Vector<>();

    static boolean isQTCanvas(Object paramObject) {
        Class<?> localClass = paramObject.getClass();
        while ((localClass != null) && ((localClass.getName().equals("quicktime.app.view.QTJavaAWTCanvas"))
                                        || (localClass.getName().equals("quicktime.app.view.QTJavaCocoaCanvas")))) {
            localClass = localClass.getSuperclass();
        }

        return localClass != null;
    }

    public static void addActiveCanvas(Object paramObject) {
        if (!isQTCanvas(paramObject))
            return;
        if (canvases.contains(paramObject))
            return;
        canvases.addElement(paramObject);
    }

    public static void removeActiveCanvas(Object paramObject) {
        canvases.removeElement(paramObject);
    }

    private static void removeAllCanvases() {
        while (canvases.size() > 0) {
            Component localComponent = (Component) canvases.elementAt(0);

            removeActiveCanvas(localComponent);
        }
    }

    public static void close() {
    }

    public static final void exitMovies() {
    }

    static void setJavaThreadHook() {
    }

    private static native void DestroyPortAssociation(int paramInt);

    private static native int CreatePortAssociation(int paramInt1, int paramInt2, int paramInt3);

    private static native short InitializeQTML(int paramInt);

    private static native void TerminateQTML();

    private static native short EnterMovies();

    private static native void ExitMovies();

    private static native short Gestalt(int paramInt, int[] paramArrayOfInt);

    private static native short InitializeQTVR();

    private static native short TerminateQTVR();

    private static native int Q3Initialize();

    private static native int Q3Exit();

    private static native void SetJavaThreadCallbackProc();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/QTSession
 * .class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
