
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;


/**
 * @deprecated
 */
public final class SetGWorld implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SetGWorld.class);
                    }
                });
            }
        }.establish();
    }

    public SetGWorld(QDGraphics paramQDGraphics) throws QDException {
        this(paramQDGraphics, null);
    }

    public SetGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws QDException {
        int i = QTObject.ID(paramQDGraphics);
        if (i == 0)
            throw new QDException(-50);
        if (i == -1)
            return;
        GetGWorld(this.savedPort, this.savedDevice);
        SetGWorld(i, QTObject.ID(paramGDevice));
    }

    private int[] savedPort = {
        0
    };

    private int[] savedDevice = {
        0
    };

    public final void reset() {
        if (this.savedPort[0] != -1) {
            SetGWorld(this.savedPort[0], this.savedDevice[0]);
            this.savedPort[0] = -1;
        }
    }

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);
}
