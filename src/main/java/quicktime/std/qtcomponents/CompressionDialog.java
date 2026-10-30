package quicktime.std.qtcomponents;

import com.apple.mrj.macos.carbon.CarbonAccess;
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

    CompressionDialog(int subType) throws QTException {
        super(1935893609, subType);
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
    public void setInfoState(QTHandleRef infoState) throws StdQTException {
        int[] info = { QTObject.ID(infoState) };
        StdQTException.checkError(SCSetInfo(_ID(), 1936946273, info));
    }

    /**
     * Sets the current preferences for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param flags the preference flags
     * @throws quicktime.std.StdQTException
     */
    public void setInfoPreferences(int flags) throws StdQTException {
        int[] info = { flags };
        StdQTException.checkError(SCSetInfo(_ID(), 1886545254, info));
    }

    /**
     * Retrieves the current preferences for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the preference flags
     * @throws quicktime.std.StdQTException
     */
    public int getInfoPreferences() throws StdQTException {
        int[] info = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1886545254, info));
        return info[0];
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
    public void setInfoState(AtomContainer infoState) throws StdQTException {
        StdQTException.checkError(SCSetSettingsFromAtomContainer(_ID(), QTObject.ID(infoState)));
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

    private static int SCRequestImageSettings(int ci) {
        return ImageCompressionManager.SCRequestImageSettings(ci);
    }

    private static int SCGetInfo(int ci, int type, int[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, int info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, int[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCGetInfo(int ci, int type, short[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, short[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCGetInfo(int ci, int type, byte[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, byte[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCSetSettingsFromAtomContainer(int ci, int settings) {
        return ImageCompressionManager.SCSetSettingsFromAtomContainer(ci, settings);
    }
}
