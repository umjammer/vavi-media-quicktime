
package quicktime;

import java.awt.Component;
import java.security.AccessController;
import java.security.PrivilegedAction;


final class WinNativeHelper {
    static native int getCompHWnd(Component paramComponent);

    static native int getDrawingSurfaceHWnd(Component paramComponent, int paramInt);

    static native int getDrawingSurface(Component paramComponent);

    static native int lockDrawingSurface(Component paramComponent, int paramInt);

    static native void unlockDrawingSurface(Component paramComponent, int paramInt);

    static native void getBounds(Component paramComponent, int paramInt, int[] paramArrayOfInt);

    static native void freeDrawingSurface(Component paramComponent, int paramInt);

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
