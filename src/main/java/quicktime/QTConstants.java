package quicktime;

/**
 * This class represents all of the constants in the QT interfaces.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public abstract interface QTConstants {

    /**
     * Used to ascertain if QuickTime is present on user's system
     */
    public static final int gestaltQuickTimeVersion = 1903454573;

    /**
     * Used to ascertain if QuickTime3D is present on user's system
     */
    public static final int gestaltQD3D = 1902392164;

    /**
     * Used to ascertain if QuickTime3D is present on user's system
     */
    public static final int gestaltQD3DPresent = 1;

    /**
     * Used to ascertain if QuickTimeVR is present on user's system
     */
    public static final int gestaltQTVRMgrAttr = 1903457906;

    /**
     * Used to ascertain if QuickTimeVR is present on user's system
     */
    public static final int gestaltQTVRMgrPresent = 1;

    public static final int kInitializeQTMLNoSoundFlag = 1;

    public static final int kInitializeQTMLUseGDIFlag = 2;

    public static final int kQTMLHandlePortEvents = 1;

    public static final int kQTMLNoIdleEvents = 2;

    public static final int kInitializeQTMLDisableDirectSound = 4;

    /**
     * The Shift key was down when the event occurred.
     */
    public static final int kJavaShiftMask = 1;

    /**
     * The Control key was down when the event occurred.
     */
    public static final int kJavaControlMask = 2;

    /**
     * The Meta key was down when the event occurred.
     */
    public static final int kJavaMetaMask = 4;

    /**
     * The Alt or Option key was down when the event occurred.
     */
    public static final int kJavaAltMask = 8;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyEnter = 10;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyBackSpace = 8;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyTab = 9;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyCancel = 3;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyClear = 12;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyShift = 16;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyControl = 17;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyAlt = 18;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyPause = 19;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyCapsLock = 20;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyEscape = 27;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeySpace = 32;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyPageUp = 33;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyPageDown = 34;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyEnd = 35;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyHome = 36;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyLeft = 37;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyUp = 38;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyRight = 39;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyDown = 40;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyComma = 44;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeyPeriod = 46;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    public static final int kJavaVKeySlash = 47;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/
 * QTConstants.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
