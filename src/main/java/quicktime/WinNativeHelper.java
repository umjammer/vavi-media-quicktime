
package quicktime;

import java.awt.Component;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.PlatformManager;


final class WinNativeHelper {
    static int getCompHWnd(Component paramComponent) {
        return PlatformManager.getCompHWnd(paramComponent);
    }

    static int getDrawingSurfaceHWnd(Component paramComponent, int paramInt) {
        return PlatformManager.getDrawingSurfaceHWnd(paramComponent, paramInt);
    }

    static int getDrawingSurface(Component paramComponent) {
        return PlatformManager.getDrawingSurface(paramComponent);
    }

    static int lockDrawingSurface(Component paramComponent, int paramInt) {
        return PlatformManager.lockDrawingSurface(paramComponent, paramInt);
    }

    static void unlockDrawingSurface(Component paramComponent, int paramInt) {
        PlatformManager.unlockDrawingSurface(paramComponent, paramInt);
    }

    static void getBounds(Component paramComponent, int paramInt, int[] paramArrayOfInt) {
        PlatformManager.getBounds(paramComponent, paramInt, paramArrayOfInt);
    }

    static void freeDrawingSurface(Component paramComponent, int paramInt) {
        PlatformManager.freeDrawingSurface(paramComponent, paramInt);
    }

    static {
        if ((QTSession.isCurrentOS(2)) && (QTSession.getJavaVersion() >= 65540)) {

            class PrivelegedAction {
                void establish() {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() {
                        public Void run() {
                            try {
                                System.loadLibrary("QTJava");
                            } catch (Exception localException) {
                                System.err.println("WinNativeHelper :System.loadLibrary failed ");
                                localException.printStackTrace();
                            }
                            return null;
                        }
                    });
                }
            }
            new PrivelegedAction().establish();
        }
    }
}
