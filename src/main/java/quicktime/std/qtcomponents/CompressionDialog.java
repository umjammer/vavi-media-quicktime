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

/**
 * This class represents the standard compression dialog.
 */
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

    /**
     * Displays the standard dialog box to the user for a single image or for sound.
     * QuickTime::SCRequestImageSettings()
     */
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

    /**
     * Retrieves all of the settings for a dialog.
     * QuickTime::SCGetInfo()
     */
    public QTHandleRef getInfoState() throws QTException {
        return QTHandleRef.fromCompressionDialogState(this);
    }

    /**
     * Sets all of the settings for a dialog.
     * QuickTime::SCSetInfo()
     */
    public void setInfoState(QTHandleRef paramQTHandleRef) throws StdQTException {
        int[] arrayOfInt = { QTObject.ID(paramQTHandleRef) };
        StdQTException.checkError(SCSetInfo(_ID(), 1936946273, arrayOfInt));
    }

    /**
     * Sets the current preferences for a dialog.
     * QuickTime::SCSetInfo()
     * @param flags the preference flags
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoPreferences(int paramInt) throws StdQTException {
        int[] arrayOfInt = { paramInt };
        StdQTException.checkError(SCSetInfo(_ID(), 1886545254, arrayOfInt));
    }

    /**
     * Retrieves the current preferences for a dialog.
     * QuickTime::SCGetInfo()
     * @return the preference flags
     * @return {@link quicktime.std.StdQTException}
     */
    public int getInfoPreferences() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1886545254, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Retrieves the current configuration from the dialog
     * QuickTime__SCGetSettingsFromAtomContainer()
     * @return the settings
     * @return {@link quicktime.std.StdQTException}
     */
    public AtomContainer getSettings() throws StdQTException {
        return AtomContainer.fromCompressionDialog(this);
    }

    /**
     * Deprecated.
     * <I/>
     * Sets the current configuration for a dialog.
     * QuickTime::SCSetSettingsFromAtomContainer()
     * @see setSettings
     */
    @Deprecated
    public void setInfoState(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SCSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer)));
    }

    /**
     * Sets the current configuration for a dialog.
     * QuickTime::SCSetSettingsFromAtomContainer()
     * @param settings -
     * @param {@link quicktime.std.StdQTException}
     */
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
