package quicktime.streaming;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.StreamingManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.std.StdQTException;

/**
 * This class represents a settings dialog used to configure a streaming session. The dialog allows the user to specify audio and video
 * source in addition to settings for compression and packetizer
 */
public class SettingsDialog implements QTStreamingLib {

    private static Object linkage;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SettingsDialog.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * This method creates and displays a new settings dialog for all active streams
     * @param p the presentation to configure
     * @see " QuickTime::QTSPresSettingsDialog"
     */
    public SettingsDialog(Presentation p) throws QTException {
        Presentation localPresentation = p;
        if (QTSession.isCurrentOS(4)) {
            StdQTException.checkError((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                @Override
                public Object invoke() {
                    int i = SettingsDialog.QTSPresSettingsDialog(QTObject.ID(localPresentation), 0, 0, 0, 0);
                    return i;
                }
            }));
        } else {
            synchronized (QTNative.globalsLock) {
                StdQTException.checkError(QTSPresSettingsDialog(QTObject.ID(p), 0, 0, 0, 0));
            }
        }
    }

    /**
     * This method creates and displays a new settings dialog for a specific stream
     * @param p the presentation to configure
     * @param s the stream to configure
     * @see " QuickTime::QTSPresSettingsDialog"
     */
    public SettingsDialog(Presentation p, Stream s) throws QTException {
        synchronized (QTNative.globalsLock) {
            QTException.checkError(QTSPresSettingsDialog(QTObject.ID(p), QTObject.ID(s), 0, 0, 0));
        }
    }

    private static short QTSPresSettingsDialog(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return StreamingManager.QTSPresSettingsDialog(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }
}
