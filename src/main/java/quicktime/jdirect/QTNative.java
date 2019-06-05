
package quicktime.jdirect;

import quicktime.QTSession;


public final class QTNative {
    public static Object linkNativeMethods(Class<?> paramClass) {
        if (QTSession.isCurrentOS(2)) {
            JDirectLinker.linkNativeMethods(paramClass);
            return null;
        }
        if (QTSession.isCurrentOS(4)) {
            if (QTSession.getJavaVersion() >= 65540) {
                return new quicktime.internal.jdirect.Linker(paramClass);
            }
            return new com.apple.mrj.jdirect.Linker(paramClass);
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

    public static final Object globalsLock = null;

    private static Object _lock;

    private static Object _getLock() {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() < 65540)) {
            _lock = com.apple.mrj.macos.carbon.CarbonLock.getInstance();
        } else if (QTSession.isCurrentOS(1)) {
            _lock = com.apple.mrj.macos.toolbox.Toolbox.LOCK;
        } else if (_lock == null) {
            _lock = new Object();
        }
        return _lock;
    }
}
