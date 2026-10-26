
package quicktime.app.view;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTSession;
import quicktime.internal.toolbox.PlatformManager;


final class QTJavaJNIAWTCanvas {
    static void RemoveNewContext(Object paramObject) {
        PlatformManager.RemoveNewContext(paramObject);
    }

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        try {
                            System.loadLibrary("QTJava");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
            }
        }

        if (QTSession.isCurrentOS(2)) {
            if (QTSession.getJavaVersion() == 65537) {
                System.loadLibrary("QTJava");
            } else {
                new PrivelegedAction().establish();
            }
        }
    }
}
