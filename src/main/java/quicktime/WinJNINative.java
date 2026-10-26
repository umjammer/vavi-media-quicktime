
package quicktime;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.PlatformManager;


final class WinJNINative {
    static int DoSecurityInit() {
        return PlatformManager.DoSecurityInit();
    }

    static void DestroyPortAssociations() {
        PlatformManager.DestroyPortAssociations();
    }

    static void DestroyHWnd(int paramInt) {
        PlatformManager.DestroyHWnd(paramInt);
    }

    static int GetHWND() {
        return PlatformManager.GetHWND();
    }

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
