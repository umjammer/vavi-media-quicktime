package quicktime;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import quicktime.app.time.TaskThread;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.internal.toolbox.VRManager;
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

/**
 * This class will check if QT is present and provides calls for enterMovies.
 * This essentially does any initialisation that QT requires.
 *
 * QTSession.open will intialize QuickTime, do a gestalt check and call enterMovies. Calls
 * to the initialize routine are accumulative - thus if your code calls QTSession.open (which calls
 * QTSession.initialize) and/or QTSession.initialize, the current session of QuickTime will only
 * be closed down when it gets the same number of calls to QTSession.close (which calls QTSession.terminate)
 * and/or QTSession.terminate.
 *
 * A typical usage of QTSession calls by an application, applet or bean is: (this gives the applet version)
 *
 * public class MyApplet extends Applet {
 * public void init () {
 * try {
 * QTSession.open();
 * ...
 * } catch (QTException e) {
 * }
 * }
 *
 * public void destroy () {
 * QTSession.close();
 * }
 * }
 *
 * Thus the applet initializes the QTSession in its init method and closes it down in its destroy method.
 * This ensures that if you have other applets (or beans, etc) that are running in the same process that
 * the QuickTime session will only be terminated when all of the current Java QTSessions in that process
 * have been destroyed.
 */
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
            Runtime.Version version = Runtime.version();
            int j = version.feature();
            int k = version.interim();
            jVersion = (j << 16) | k;
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

    /**
     * The list of currently supported operating systems for QTJava.
     */
    public static final int kMacOS = 1;

    /**
     * The list of currently supported operating systems for QTJava.
     */
    public static final int kWin32 = 2;

    /**
     * The list of currently supported operating systems for QTJava.
     */
    public static final int kMacOSX = 4;

    /**
     * Flags that can be passed in to the open call to also initialise these environments.
     */
    public static final int kInitVR = 1;

    /**
     * Flags that can be passed in to the open call to also initialise these environments.
     */
    public static final int kInit3D = 2;

    /**
     * Return the Java version in the format of:0xVVVVSSSS.
     * @return an int
     */
    public static int getJavaVersion() {
        if (jVersion == 0)
            doVMCheck();
        return jVersion;
    }

    /**
     * To check if the class has been registered and is signed.
     * @return true if applet is signed and registered.
     *  Returns false if a signedClass has not been registered with the QTSession.initialize or open methods.
     */
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

    /**
     * @return true if program is able to run in FullScreen mode.
     */
    public static boolean canDoFullScreen() {
        return !hasSecurityRestrictions() || isClassSigned();
    }

    /**
     * This method returns true if the passed in OS name is the OS upon which the current session of QuickTime
     * is running.
     * @param osName the osName of the system that you want to find out if you are running on.
     * @return true if the current OS is the one as passed in or false if not.
     */
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

    /**
     * This call will return true if the QuickTime Session has been initialized, otherwise false.
     * An application should acquire the termination lock if it is possible that the QTSession has
     * been terminated or could be terminated whilst this query is being processed.
     *
     * synchronized (QTSession.terminationLock()) {
     * if (QTSession.isInitialized()) {
     * //...
     * }
     * }
     *
     * @return true if still initialized
     */
    public static boolean isInitialized() {
        return initDone != 0;
    }

    /**
     * This call will return true if the QuickTime Session has been initialized, otherwise false.
     */
    public static boolean isVRInitialized() {
        return vrinitialized;
    }

    /**
     * This call will return true if the QuickTime Session has been initialized, otherwise false.
     */
    public static boolean is3DInitialized() {
        return threeDInitialised;
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QT environment.
     * If you use this call (and not the open calls) then you should also ensure that you check
     * that QT is present through the gestalt call and call enterMovies to initialize the
     * movie toolbox.
     * QuickTime::InitializeQTML()
     * @param flag flag that are valid for this call.
     * @param {@link quicktime.QTException}
     */
    public static void initialize(int paramInt) throws QTException {
        initialize(paramInt, null);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QT environment.
     * If you use this call (and not the open calls) then you should also ensure that you check
     * that QT is present through the gestalt call and call enterMovies to initialize the
     * movie toolbox.
     * QuickTime::InitializeQTML()
     * @param flag flag that are valid for this call.
     * @param signedClass the Class object which is signed
     * @param {@link quicktime.QTException}
     */
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
                    QDGraphics.scratch = new QDGraphics(QDGraphics.kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
                    QDGraphics.validScratch = new QDGraphics(QDGraphics.kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
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
            } else {
                java.awt.peer.ComponentPeer peer = sun.awt.AWTAccessor.getComponentAccessor().getPeer(this);
                if (peer instanceof DrawingSurface) {
                    localObject = ((DrawingSurface) peer).getDrawingSurfaceInfo();
                    ((DrawingSurfaceInfo) localObject).lock();
                    this.HWND = ((Win32DrawingSurface) ((DrawingSurfaceInfo) localObject).getSurface()).getHWnd();
                    ((DrawingSurfaceInfo) localObject).unlock();
                    QTSession.CreatePortAssociation(this.HWND, 0, 258);
                }
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
                    QDGraphics.scratch = new QDGraphics(QDGraphics.kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
                    if (qtjwiredDebug)
                        System.out.println("validScratch depth 0x" + Integer.toHexString(QDGraphics.kDefaultPixelFormat));
                    QDGraphics.validScratch = new QDGraphics(QDGraphics.kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
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

    /**
     * This call will initialize QTVR. It must be called before any of the QTVR API calls are made.
     * QuickTime::InitializeQTVR()
     */
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

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * This will ensure that any required initialisations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3.
     * This will also call EnterMovies.
     * QuickTime::InitializeQTML()
     * QuickTime::Gestalt()
     * QuickTime::EnterMovies()
     */
    public static void open() throws QTException {
        open(3, 0, 0);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * This will ensure that any required initialisations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3. You can set the initialisation options
     * to also have this call initialise QuickTimeVR and/or QuickDraw3D.
     * @param initFlag can also initialise VR or 3D.
     *  This will also call EnterMovies.
     *  QuickTime::InitializeQTML()
     *  QuickTime::Gestalt()
     *  QuickTime::EnterMovies()
     * @param {@link quicktime.QTException}
     */
    public static void open(int paramInt) throws QTException {
        open(3, 0, paramInt);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * You can optionally open a session where the specified minimal
     * version and minorVersion of QT must be present or an exception will be
     * thrown.
     * This will also call EnterMovies for you.
     * QuickTime::InitializeQTML()
     * QuickTime::Gestalt()
     * QuickTime::EnterMovies()
     * @param minorVersion the minimal minorVersion of QT that must be present
     * @param initFlag can also initialise VR or 3D.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * This will ensure that any required initialisations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3.
     * This will also call EnterMovies.
     * QuickTime::InitializeQTML()
     * QuickTime::Gestalt()
     * QuickTime::EnterMovies()
     */
    public static void open(Class paramClass) throws QTException {
        open(3, 0, 0, paramClass);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * This will ensure that any required initialisations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3. You can set the initialisation options
     * to also have this call initialise QuickTimeVR and/or QuickDraw3D.
     * @param initFlag can also initialise VR or 3D.
     * @param {@link quicktime.QTException}
     */
    public static void open(int paramInt, Class paramClass) throws QTException {
        open(3, 0, paramInt, paramClass);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * You can optionally open a session where the specified minimal
     * version and minorVersion of QT must be present or an exception will be
     * thrown.
     * This will also call EnterMovies for you.
     * QuickTime::InitializeQTML()
     * QuickTime::Gestalt()
     * QuickTime::EnterMovies()
     * @param minorVersion the minimal minorVersion of QT that must be present
     * @param initFlag can also initialise VR or 3D.
     * @param signedClass the Class object which is signed
     * @param {@link quicktime.QTException}
     */
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

    /**
     * This call will close down the QTML environment, VR and QD3D. This
     * will mean that the QTML environment will need to be reinitialised before it can be
     * used again.
     * This call should not be called from one of the quicktime.app.time.TaskThread objects
     * as a side effect of this call is stopping all thread that belong to the TaskThread group.
     * QuickTime::TerminateQTML()
     */
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

    /**
     * This returns an object that can be synchronized against for termination checking.
     * @see isInitialised
     */
    public static Object terminationLock() {
        return termLock;
    }

    private static final Object termLock = new Object();

    /**
     * This call will terminate QTVR. You cannot call QTVR API's after you make this call unless you
     * reinitialize QTVR first. QTSession.close will terminate VR if it has been initialized.
     * QuickTime::TerminateQTVR()
     */
    public static void terminateVR() {
        if (vrinitialized) {
            QTUtils.reclaimMemory();
            if (isCurrentOS(2))
                TerminateQTVR();
            vrinitialized = false;
        }
    }

    /**
     * This call will terminate QuickDraw3D. You cannot call QD3D API's after you make this call unless you
     * reinitialize QD3D first. QTSession.close will terminate 3D if it has been initialized.
     * QuickTime::Q3Exit()
     */
    public static void terminate3D() {
        if (threeDInitialised) {
            QTUtils.reclaimMemory();
            int i = Q3Exit();
            threeDInitialised = false;
        }
    }

    /**
     * Determine whether it is safe to call a QuickDraw 3D API.
     * QuickTime::Gestalt(gestaltQD3D,result)
     */
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

    /**
     * Determine whether it is safe to call a QuickTime VR API.
     * QuickTime::Gestalt(gestaltQTVRMgrAttr,result)
     */
    public static boolean isQTVRAvailable() throws QTException {
        try {
            int i = gestalt(1903457906);
            int j = i & 0x1;
            return j != 0;
        } catch (QTException localQTException) {
        }
        return false;
    }

    /**
     * This call will do a gestalt check. If you are on a non-Macintosh machine this
     * call will also initialise QTML if that has not already been done.
     * It will return the result value if the gestalt check is successful,
     * QuickTime::Gestalt()
     * @param selector the selector code for the information you need.
     * @param {@link quicktime.QTException}
     */
    public static final int gestalt(int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = Gestalt(paramInt, arrayOfInt);
        QTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Before you call any Movie Toolbox functions, you must initialize the toolbox
     * QuickTime::EnterMovies()
     */
    public static final void enterMovies() throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((isCurrentOS(4)) && (getQTVersion() >= 104857600)) {
                setJavaThreadHook();
            }
            QTException.checkError(EnterMovies());
        }
    }

    /**
     * This contains the version of QuickTime that is currently present
     */
    public static final int getMajorVersion() {
        return (version & 0xF000000) >> 24;
    }

    /**
     * This returns the sub version of QuickTime that is currently present
     */
    public static final int getMinorVersion() {
        return (version & 0xF00000) >> 20;
    }

    /**
     * This returns the sub version of QuickTime that is currently present
     */
    public static final int getBugFixVersion() {
        return (version & 0xF0000) >> 16;
    }

    /**
     * This returns the sub version of QuickTime that is currently present
     */
    public static final int getDeveloperVersion() {
        return version & 0xFFFF;
    }

    /**
     * This returns the complete version number of QuickTime
     */
    public static int getQTVersion() {
        return version;
    }

    /**
     * This returns the major version of QuickTime
     */
    public static int getQTMajorVersion() {
        return (version & 0xFF000000) >>> 24;
    }

    /**
     * This returns the minor version of QuickTime
     */
    public static int getQTMinorVersion() {
        return (version & 0xFF0000) >>> 16;
    }

    private static Vector<Object> canvases = new Vector<>();

    static boolean isQTCanvas(Object paramObject) {
        Class<?> localClass = paramObject.getClass();
        while ((localClass != null) && ((localClass.getName().equals("quicktime.app.view.QTJavaAWTCanvas")) || (localClass.getName().equals("quicktime.app.view.QTJavaCocoaCanvas")))) {
            localClass = localClass.getSuperclass();
        }
        return localClass != null;
    }

    /**
     * Adds a canvas to a registry of active canvases. You should not need to
     * call this method.
     */
    public static void addActiveCanvas(Object paramObject) {
        if (!isQTCanvas(paramObject))
            return;
        if (canvases.contains(paramObject))
            return;
        canvases.addElement(paramObject);
    }

    /**
     * Removes a canvas from a registry of active canvases. You should not need to
     * call this method.
     */
    public static void removeActiveCanvas(Object paramObject) {
        canvases.removeElement(paramObject);
    }

    private static void removeAllCanvases() {
        while (canvases.size() > 0) {
            Component localComponent = (Component) canvases.elementAt(0);
            removeActiveCanvas(localComponent);
        }
    }

    /**
     * This call will close down the QTML environment.
     * This call should not be called from one of the quicktime.app.time.TaskThread objects
     * as a side effect of this call is stopping all thread that belong to the TaskThread group.
     * @see terminate
     */
    public static void close() {
    }

    /**
     * QuickTime calls the ExitMovies function automatically when your application quits -
     * you only need to call thi function if you finish with the Movie Toolbox long
     * before your application is ready to quit.
     * QuickTime::ExitMovies()
     */
    public static final void exitMovies() {
    }

    static void setJavaThreadHook() {
    }

    private static void DestroyPortAssociation(int paramInt) {
        PlatformManager.DestroyPortAssociation(paramInt);
    }

    private static int CreatePortAssociation(int paramInt1, int paramInt2, int paramInt3) {
        return PlatformManager.CreatePortAssociation(paramInt1, paramInt2, paramInt3);
    }

    private static short InitializeQTML(int paramInt) {
        return 0;
    }

    private static void TerminateQTML() {
    }

    private static short EnterMovies() {
        return 0;
    }

    private static void ExitMovies() {
    }

    private static short Gestalt(int paramInt, int[] paramArrayOfInt) {
        return PlatformManager.Gestalt(paramInt, paramArrayOfInt);
    }

    private static short InitializeQTVR() {
        return VRManager.InitializeQTVR();
    }

    private static short TerminateQTVR() {
        return VRManager.TerminateQTVR();
    }

    private static int Q3Initialize() {
        return QD3DManager.Q3Initialize();
    }

    private static int Q3Exit() {
        return QD3DManager.Q3Exit();
    }

    private static void SetJavaThreadCallbackProc() {
        PlatformManager.SetJavaThreadCallbackProc();
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/QTSession
 * .class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
