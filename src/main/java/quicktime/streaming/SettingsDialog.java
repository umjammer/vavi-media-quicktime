
package quicktime.streaming;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.std.StdQTException;


public class SettingsDialog implements QTStreamingLib {
    private static Object linkage;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SettingsDialog.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public SettingsDialog(Presentation paramPresentation) throws QTException {
        final Presentation localPresentation = paramPresentation;
        if (QTSession.isCurrentOS(4)) {
            StdQTException.checkError(((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    int i = SettingsDialog.QTSPresSettingsDialog(QTObject.ID(localPresentation), 0, 0, 0, 0);
                    return new Integer(i);
                }
            })).intValue());
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(QTSPresSettingsDialog(QTObject.ID(paramPresentation), 0, 0, 0, 0));
            }
        }
    }

    public SettingsDialog(Presentation paramPresentation, Stream paramStream) throws QTException {
        synchronized (QTNative.globalsLock) {
            QTException.checkError(QTSPresSettingsDialog(QTObject.ID(paramPresentation), QTObject.ID(paramStream), 0, 0, 0));
        }
    }

    private static native short QTSPresSettingsDialog(int paramInt1,
                                                      int paramInt2,
                                                      int paramInt3,
                                                      int paramInt4,
                                                      int paramInt5);
}
