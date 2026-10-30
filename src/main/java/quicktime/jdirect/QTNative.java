package quicktime.jdirect;

import quicktime.QTSession;

public final class QTNative {

    public static Object linkNativeMethods(Class<?> clazz) {
        if (QTSession.isCurrentOS(2)) {
            JDirectLinker.linkNativeMethods(clazz);
            return null;
        }
        if (QTSession.isCurrentOS(4)) {
            if (QTSession.getJavaVersion() >= 65540) {
                return new quicktime.internal.jdirect.Linker(clazz);
            }
            return new com.apple.mrj.jdirect.Linker(clazz);
        }
        return null;
    }

    static Object loadQT() {
        if (QTSession.isCurrentOS(2))
            return JDirectLinker.loadLibrary("QTJavaNative");
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("QuickTimeLib");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    static Object loadQTS() {
        if (QTSession.isCurrentOS(2)) {
            return null;
        }
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("QTStreamLib");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    static Object loadIL() {
        if (QTSession.isCurrentOS(2))
            return JDirectLinker.loadLibrary("QTJavaNative");
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("InterfaceLib");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    static Object loadPL() {
        if (QTSession.isCurrentOS(2))
            return JDirectLinker.loadLibrary("QTJava");
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("AppleJava");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    static Object loadVR() {
        if (QTSession.isCurrentOS(2))
            return JDirectLinker.loadLibrary("QTJava");
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("QuickTimeVRLib");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    static Object load3D() {
        if (QTSession.isCurrentOS(2))
            return JDirectLinker.loadLibrary("QTJava");
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("QuickDraw™ 3D");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    static Object loadSL() {
        if (QTSession.isCurrentOS(2))
            return JDirectLinker.loadLibrary("QTJava");
        if (QTSession.isCurrentOS(1))
            return com.apple.mrj.jdirect.JDirectLinker.loadLibrary("SoundLib");
        if (QTSession.isCurrentOS(4)) {
            return null;
        }
        throw new UnsatisfiedLinkError("Unknown OS");
    }

    /**
     * This object is a global lock that must be grabbed through a synchronized (..) call
     * before any global value in the Sytsem (such as the current graf port) is set.
     * This ensures that global variables can only be written to (and read) by a single
     * thread of execution at one time.
     */
    public static final Object globalsLock = new Object();
}
