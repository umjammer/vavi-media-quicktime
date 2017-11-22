
package quicktime;

import java.security.AccessController;
import java.security.PrivilegedAction;


final class WinJNINative {
    static native int DoSecurityInit();

    static native void DestroyPortAssociations();

    static native void DestroyHWnd(int paramInt);

    static native int GetHWND();

    static {
        if (QTSession.isCurrentOS(2)) {
            if (QTSession.getJavaVersion() == 65537) {
                System.loadLibrary("QTJava");

            } else {

                class PrivelegedAction {
                    void establish() {
                        AccessController.doPrivileged(new PrivilegedAction<Void>() {
                            public Void run() {
                                System.loadLibrary("QTJava");
                                return null;
                            }
                        });
                    }
                }
                new PrivelegedAction().establish();
            }
        }
    }
}
