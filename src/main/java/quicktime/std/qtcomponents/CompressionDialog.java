package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
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

    int res;

    CompressionDialog(int paramInt) throws QTException {
        super(1935893609, paramInt);
    }

    /**
     * Displays the standard dialog box to the user for a single image or for sound.
     * @see "QuickTime::SCRequestImageSettings()"
     */
    public void requestSettings() throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            this.res = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                synchronized (QTNative.globalsLock) {
                    CompressionDialog.this.res = CompressionDialog.SCRequestImageSettings(_ID());
                }
                return CompressionDialog.this.res;
            });
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
     * @see "QuickTime::SCGetInfo()"
     */
    public QTHandleRef getInfoState() throws QTException {
        return QTHandleRef.fromCompressionDialogState(this);
    }

    /**
     * Sets all of the settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     */
    public void setInfoState(QTHandleRef paramQTHandleRef) throws StdQTException {
        int[] arrayOfInt = { QTObject.ID(paramQTHandleRef) };
        StdQTException.checkError(SCSetInfo(_ID(), 1936946273, arrayOfInt));
    }

    /**
     * Sets the current preferences for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param flags the preference flags
     * @throws quicktime.std.StdQTException
     */
    public void setInfoPreferences(int flags) throws StdQTException {
        int[] arrayOfInt = { flags };
        StdQTException.checkError(SCSetInfo(_ID(), 1886545254, arrayOfInt));
    }

    /**
     * Retrieves the current preferences for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the preference flags
     * @throws quicktime.std.StdQTException
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
     * @throws quicktime.std.StdQTException
     */
    public AtomContainer getSettings() throws StdQTException {
        return AtomContainer.fromCompressionDialog(this);
    }

    /**
     * @deprecated
     * <I/>
     * Sets the current configuration for a dialog.
     * @see "QuickTime::SCSetSettingsFromAtomContainer()"
     * @see #setSettings
     */
    @Deprecated
    public void setInfoState(AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(SCSetSettingsFromAtomContainer(_ID(), QTObject.ID(paramAtomContainer)));
    }

    /**
     * Sets the current configuration for a dialog.
     * @see "QuickTime::SCSetSettingsFromAtomContainer()"
     * @param settings -
     * @throws quicktime.std.StdQTException
     */
    public void setSettings(AtomContainer settings) throws StdQTException {
        StdQTException.checkError(SCSetSettingsFromAtomContainer(_ID(), QTObject.ID(settings)));
    }

    private static int SCRequestImageSettings(int paramInt) {
        return ImageCompressionManager.SCRequestImageSettings(paramInt);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramInt3);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SCSetSettingsFromAtomContainer(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SCSetSettingsFromAtomContainer(paramInt1, paramInt2);
    }
}

