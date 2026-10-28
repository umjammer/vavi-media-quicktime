package quicktime;

/**
 * This class represents all of the constants in the QT interfaces.
 * ALL constants except for constants that start with an acronym, such as MPEG, ...
 * start with a small letter (not a capital one)
 */
public interface QTConstants {

    /**
     * Used to ascertain if QuickTime is present on user's system
     */
    int gestaltQuickTimeVersion = 1903454573;

    /**
     * Used to ascertain if QuickTime3D is present on user's system
     */
    int gestaltQD3D = 1902392164;

    /**
     * Used to ascertain if QuickTime3D is present on user's system
     */
    int gestaltQD3DPresent = 1;

    /**
     * Used to ascertain if QuickTimeVR is present on user's system
     */
    int gestaltQTVRMgrAttr = 1903457906;

    /**
     * Used to ascertain if QuickTimeVR is present on user's system
     */
    int gestaltQTVRMgrPresent = 1;

    int kInitializeQTMLNoSoundFlag = 1;

    int kInitializeQTMLUseGDIFlag = 2;

    int kQTMLHandlePortEvents = 1;

    int kQTMLNoIdleEvents = 2;

    int kInitializeQTMLDisableDirectSound = 4;

    /**
     * The Shift key was down when the event occurred.
     */
    int kJavaShiftMask = 1;

    /**
     * The Control key was down when the event occurred.
     */
    int kJavaControlMask = 2;

    /**
     * The Meta key was down when the event occurred.
     */
    int kJavaMetaMask = 4;

    /**
     * The Alt or Option key was down when the event occurred.
     */
    int kJavaAltMask = 8;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyEnter = 10;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyBackSpace = 8;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyTab = 9;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyCancel = 3;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyClear = 12;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyShift = 16;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyControl = 17;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyAlt = 18;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyPause = 19;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyCapsLock = 20;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyEscape = 27;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeySpace = 32;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyPageUp = 33;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyPageDown = 34;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyEnd = 35;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyHome = 36;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyLeft = 37;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyUp = 38;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyRight = 39;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyDown = 40;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyComma = 44;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeyPeriod = 46;

    /**
     * Some Java virtual key values - these are the same values as in KeyEvent.
     */
    int kJavaVKeySlash = 47;
}
