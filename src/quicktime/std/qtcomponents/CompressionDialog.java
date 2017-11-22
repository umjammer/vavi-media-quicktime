
package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.AtomContainer;
import quicktime.util.QTHandleRef;


public class CompressionDialog extends Component implements QuickTimeLib {
    private static Object linkage;

    int res;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(CompressionDialog.class);
                    }
                });
            }
        }.establish();
    }

    CompressionDialog(int paramInt) throws QTException {
        super(1935893609, paramInt);
    }

    public void requestSettings() throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            this.res = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        CompressionDialog.this.res = CompressionDialog.SCRequestImageSettings(_ID());
                    }
                    return CompressionDialog.this.res;
                }
            })).intValue();
            if (this.res == 1)
                throw new StdQTException(-128);
            StdQTException.checkError(this.res);
        } else {
            synchronized (QTNative.globalsLock) {
                this.res = SCRequestImageSettings(_ID());
                if (this.res == 1)
                    throw new StdQTException(-128);
                StdQTException.checkError(this.res);
            }
        }
    }

    public QTHandleRef getInfoState() throws QTException {
        return QTHandleRef.fromCompressionDialogState(this);
    }

    public void setInfoState(QTHandleRef paramQTHandleRef) throws StdQTException {
        int[] arrayOfInt = {
            QTObject.ID(paramQTHandleRef)
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1936946273, arrayOfInt));
    }

    public void setInfoPreferences(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            paramInt
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1886545254, arrayOfInt));
    }

    public int getInfoPreferences() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1886545254, arrayOfInt));

        return arrayOfInt[0];
    }

    public AtomContainer getSettings() throws StdQTException {
        return AtomContainer.fromCompressionDialog(this);
    }

    @Deprecated
    public void setInfoState(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SCSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer)));
    }

    public void setSettings(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SCSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer)));
    }

    private static native int SCRequestImageSettings(int paramInt);

    private static native int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SCSetInfo(int paramInt1, int paramInt2, int paramInt3);

    private static native int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SCSetSettingsFromAtomContainer(int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/CompressionDialog.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
