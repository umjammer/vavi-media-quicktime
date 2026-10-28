package quicktime;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Vector;

import quicktime.app.time.TaskThread;
import quicktime.internal.awt.DrawingSurface;
import quicktime.internal.awt.DrawingSurfaceInfo;
import quicktime.internal.awt.Win32DrawingSurface;
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
import quicktime.util.QTUtils;


/**
 * This class will check if QT is present and provides calls for enterMovies.
 * This essentially does any initialisation that QT requires.
 * <p>
 * QTSession.open will intialize QuickTime, do a gestalt check and call enterMovies. Calls
 * to the initialize routine are accumulative - thus if your code calls QTSession.open (which calls
 * QTSession.initialize) and/or QTSession.initialize, the current session of QuickTime will only
 * be closed down when it gets the same number of calls to QTSession.close (which calls QTSession.terminate)
 * and/or QTSession.terminate.
 * <p>
 * A typical usage of QTSession calls by an application, applet or bean is: (this gives the applet version)
 * <p>
 * public class MyApplet extends Applet {
 * public void init () {
 * try {
 * QTSession.open();
 * ...
 * } catch (QTException e) {
 * }
 * }
 * <p>
 * public void destroy () {
 * QTSession.close();
 * }
 * }
 * <p>
 * Thus the applet initializes the QTSession in its init method and closes it down in its destroy method.
 * This ensures that if you have other applets (or beans, etc) that are running in the same process that
 * the QuickTime session will only be terminated when all of the current Java QTSessions in that process
 * have been destroyed.
 */
public final class QTSession implements QuickTimeLib, QuickTimeVRLib, QuickDraw3DLib {

    private static final Logger logger = System.getLogger(QTSession.class.getName());

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
        }
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

    private static final boolean gestaltDone = false;

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
     *
     * @return an int
     */
    public static int getJavaVersion() {
        if (jVersion == 0)
            doVMCheck();
        return jVersion;
    }

    /**
     * To check if the class has been registered and is signed.
     *
     * @return true if applet is signed and registered.
     * Returns false if a signedClass has not been registered with the QTSession.initialize or open methods.
     */
    public static boolean isClassSigned() {
        if (theClass != null) {
            Object[] arrayOfObject = theClass.getSigners();
            return arrayOfObject != null;
        }
        return false;
    }

    public static boolean hasSecurityRestrictions() {
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
     *
     * @param osName the osName of the system that you want to find out if you are running on.
     * @return true if the current OS is the one as passed in or false if not.
     * Since the QuickTime toolbox is emulated, this is always {@link #kMacOSX}.
     */
    public static boolean isCurrentOS(int osName) {
        // the toolbox is emulated in Java (quicktime.internal.toolbox), every host behaves like Mac OS X
        if (osConstant == 0) {
            osConstant = kMacOSX;
        }
        return (osName & osConstant) != 0;
    }

    /**
     * This call will return true if the QuickTime Session has been initialized, otherwise false.
     * An application should acquire the termination lock if it is possible that the QTSession has
     * been terminated or could be terminated whilst this query is being processed.
     * <p>
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
     *
     * @param flag flag that are valid for this call.
     * @throws quicktime.QTException
     * @see "QuickTime::InitializeQTML()"
     */
    public static void initialize(int flag) throws QTException {
        initialize(flag, null);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QT environment.
     * If you use this call (and not the open calls) then you should also ensure that you check
     * that QT is present through the gestalt call and call enterMovies to initialize the
     * movie toolbox.
     *
     * @param flag        flag that are valid for this call.
     * @param signedClass the Class object which is signed
     * @throws quicktime.QTException
     * @see "QuickTime::InitializeQTML()"
     */
    public static void initialize(int flag, Class<?> signedClass) throws QTException {
        synchronized (terminationLock()) {
            if (initDone == 0) {
                if (signedClass != null) {
                    theClass = signedClass;
                    if (!isClassSigned())
                        throw new SecurityException(signedClass + ",is NOT SIGNED");
                }
                if (isCurrentOS(2)) {
                    int i = InitializeQTML(flag);
                    QTException.checkError(i);
                    try {
                        Class.forName("java.awt.Frame");
                        if (dispatchFrame == null) {
                            QTFrame localQTFrame = new QTFrame();
                            localQTFrame.setLayout(null);
                            localQTFrame.pack();
                            dispatchFrame = localQTFrame;
                        }
                    } catch (ClassNotFoundException _) {
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

        @Override
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

    private static final boolean qtjwiredDebug = false;

    private static void JQTinitializeVR() throws QTException {
        if ((!isCurrentOS(4)) && (QuickTimeVRLib.libraryInstance == null)) {
            throw new UnsatisfiedLinkError("QuickTimeVRLib");
        }
        vrinitialized = true;
    }

    private static void JQTinitialize() throws QTException {
        synchronized (terminationLock()) {
            if (qtjwiredDebug)
                logger.log(Level.DEBUG, "JQTinitialize...");
            if (initDone == 0) {
                if (isCurrentOS(2)) {
                    hWnd = WinJNINative.GetHWND();
                }
                int i = gestalt(1903454573);
                qtPresent = true;
                version = i;
                if ((getMajorVersion() < 4) || ((getMajorVersion() == 4) && (getMinorVersion() < 1))) {
                    if (qtjwiredDebug)
                        logger.log(Level.DEBUG, "Version of QuickTime not found...");
                    JQTterminate();
                    throw new QTException("Version of QuickTime not found");
                }
                QTObject.qtIsNOTInitialized = false;
                initDone = 1;
                sessionID += 1;
                if (qtjwiredDebug) {
                    logger.log(Level.DEBUG, "scratch depth 0x" + Integer.toHexString(QDGraphics.kDefaultPixelFormat));
                }
                if (!firstTime) {
                    QDGraphics.scratch = new QDGraphics(QDGraphics.kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
                    if (qtjwiredDebug)
                        logger.log(Level.DEBUG, "validScratch depth 0x" + Integer.toHexString(QDGraphics.kDefaultPixelFormat));
                    QDGraphics.validScratch = new QDGraphics(QDGraphics.kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
                }
                if (qtjwiredDebug)
                    logger.log(Level.DEBUG, "JQTinitialized");
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
     *
     * @see "QuickTime::InitializeQTVR()"
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
     *
     * @see "QuickTime::InitializeQTML()"
     * @see "QuickTime::Gestalt()"
     * @see "QuickTime::EnterMovies()"
     */
    public static void open() throws QTException {
        open(3, 0, 0);
    }

    /**
     * This call will open a QTSession and perform any initializations of the QTML environment.
     * This will ensure that any required initializations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3. You can set the initialisation options
     * to also have this call initialise QuickTimeVR and/or QuickDraw3D.
     *
     * @param initFlag can also initialize VR or 3D.
     *                 This will also call EnterMovies.
     * @throws quicktime.QTException
     * @see "QuickTime::InitializeQTML()"
     * @see "QuickTime::Gestalt()"
     * @see "QuickTime::EnterMovies()"
     */
    public static void open(int initFlag) throws QTException {
        open(3, 0, initFlag);
    }

    /**
     * This call will open a QTSession and perform any initializations of the QTML environment.
     * You can optionally open a session where the specified minimal
     * version and minorVersion of QT must be present or an exception will be
     * thrown.
     * This will also call EnterMovies for you.
     *
     * @param minorVersion the minimal minorVersion of QT that must be present
     * @param initFlag     can also initialise VR or 3D.
     * @throws quicktime.QTException
     * @see "QuickTime::InitializeQTML()"
     * @see "QuickTime::Gestalt()"
     * @see "QuickTime::EnterMovies()"
     */
    public static void open(int paramInt1, int minorVersion, int initFlag) throws QTException {
        initialize(0);
        int i = gestalt(1903454573);
        qtPresent = true;
        version = i;
        if ((getMajorVersion() < paramInt1) || ((getMajorVersion() == paramInt1) && (getMinorVersion() < minorVersion))) {
            close();
            throw new QTException("Version of QuickTime not found");
        }
        enterMovies();
        enterCalled = true;
        if ((0x1 & initFlag) != 0)
            initializeVR();
        if (((0x2 & initFlag) != 0) && (!isCurrentOS(4))) {
            initialize3D();
        }
    }

    /**
     * This call will open a QTSession and perform any initializations of the QTML environment.
     * This will ensure that any required initializations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3.
     * This will also call EnterMovies.
     *
     * @see "QuickTime::InitializeQTML()"
     * @see "QuickTime::Gestalt()"
     * @see "QuickTime::EnterMovies()"
     */
    public static void open(Class<?> paramClass) throws QTException {
        open(3, 0, 0, paramClass);
    }

    /**
     * This call will open a QTSession and perform any initializations of the QTML environment.
     * This will ensure that any required initializations are performed,
     * and that the required version of QuickTime is present. The minimal
     * version of QT that is required is version 3. You can set the initialization options
     * to also have this call initialise QuickTimeVR and/or QuickDraw3D.
     *
     * @param initFlag can also initialize VR or 3D.
     * @throws quicktime.QTException
     */
    public static void open(int initFlag, Class<?> paramClass) throws QTException {
        open(3, 0, initFlag, paramClass);
    }

    /**
     * This call will open a QTSession and perform any initialisations of the QTML environment.
     * You can optionally open a session where the specified minimal
     * version and minorVersion of QT must be present or an exception will be
     * thrown.
     * This will also call EnterMovies for you.
     *
     * @param minorVersion the minimal minorVersion of QT that must be present
     * @param initFlag     can also initialise VR or 3D.
     * @param signedClass  the Class object which is signed
     * @throws quicktime.QTException
     * @see "QuickTime::InitializeQTML()"
     * @see "QuickTime::Gestalt()"
     * @see "QuickTime::EnterMovies()"
     */
    public static void open(int paramInt1, int minorVersion, int initFlag, Class<?> signedClass) throws QTException {
        initialize(0, signedClass);
        int i = gestalt(1903454573);
        qtPresent = true;
        version = i;
        if ((getMajorVersion() < paramInt1) || ((getMajorVersion() == paramInt1) && (getMinorVersion() < minorVersion))) {
            close();
            throw new QTException("Version of QuickTime not found");
        }
        enterMovies();
        enterCalled = true;
        if ((0x1 & initFlag) != 0)
            initializeVR();
        if (((0x2 & initFlag) != 0) && (!isCurrentOS(4))) {
            initialize3D();
        }
    }

    /**
     * This call will close down the QTML environment, VR and QD3D. This
     * will mean that the QTML environment will need to be reinitialised before it can be
     * used again.
     * This call should not be called from one of the quicktime.app.time.TaskThread objects
     * as a side effect of this call is stopping all thread that belong to the TaskThread group.
     *
     * @see "QuickTime::TerminateQTML()"
     */
    public static void terminate() {
        if (initDone > 1) {
            initDone -= 1;
        } else if (initDone == 1) {
            removeAllCanvases();
            TaskThread.killAllThreads();
            quicktime.internal.toolbox.MovieAccess.terminate();
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
                logger.log(Level.DEBUG, "Doing Terminate");
            removeAllCanvases();
            TaskThread.killAllThreads();
            QTUtils.reclaimMemory();
            synchronized (terminationLock()) {
                QTObject.qtIsNOTInitialized = true;
                initDone = 0;
                QTObjectManagement.removeAll();
                if (qtjwiredDebug) {
                    logger.log(Level.DEBUG, "After RemoveAll");
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
                logger.log(Level.DEBUG, " JQTterminated");
            }
        }
    }

    /**
     * This returns an object that can be synchronized against for termination checking.
     *
     * @see "#isInitialised"
     */
    public static Object terminationLock() {
        return termLock;
    }

    private static final Object termLock = new Object();

    /**
     * This call will terminate QTVR. You cannot call QTVR API's after you make this call unless you
     * reinitialize QTVR first. QTSession.close will terminate VR if it has been initialized.
     *
     * @see "QuickTime::TerminateQTVR()"
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
     *
     * @see "QuickTime::Q3Exit()"
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
     *
     * @see "QuickTime::Gestalt(gestaltQD3D,result)"
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
     *
     * @see "QuickTime::Gestalt(gestaltQTVRMgrAttr,result)"
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
     *
     * @param selector the selector code for the information you need.
     * @throws quicktime.QTException
     * @see "QuickTime::Gestalt()"
     */
    public static int gestalt(int selector) throws QTException {
        int[] arrayOfInt = {0};
        int i = Gestalt(selector, arrayOfInt);
        QTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Before you call any Movie Toolbox functions, you must initialize the toolbox
     *
     * @see "QuickTime::EnterMovies()"
     */
    public static void enterMovies() throws QTException {
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
    public static int getMajorVersion() {
        return (version & 0xF000000) >> 24;
    }

    /**
     * This returns the sub version of QuickTime that is currently present
     */
    public static int getMinorVersion() {
        return (version & 0xF00000) >> 20;
    }

    /**
     * This returns the sub version of QuickTime that is currently present
     */
    public static int getBugFixVersion() {
        return (version & 0xF0000) >> 16;
    }

    /**
     * This returns the sub version of QuickTime that is currently present
     */
    public static int getDeveloperVersion() {
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

    private static final Vector<Object> canvases = new Vector<>();

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
        while (!canvases.isEmpty()) {
            Component localComponent = (Component) canvases.elementAt(0);
            removeActiveCanvas(localComponent);
        }
    }

    /**
     * This call will close down the QTML environment.
     * This call should not be called from one of the quicktime.app.time.TaskThread objects
     * as a side effect of this call is stopping all thread that belong to the TaskThread group.
     *
     * @see #terminate
     */
    public static void close() {
        terminate();
    }

    /**
     * QuickTime calls the ExitMovies function automatically when your application quits -
     * you only need to call thi function if you finish with the Movie Toolbox long
     * before your application is ready to quit.
     *
     * @see "QuickTime::ExitMovies()"
     */
    public static void exitMovies() {
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
