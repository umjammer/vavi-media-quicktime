package quicktime.std.sg;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.UserData;
import quicktime.util.QTUtils;

/**
 * Assists in importing sound and/or video into a movie.
 * The SGChannel implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * Refer to SGChannel in QuickTime Components
 */
public abstract class SGChannel extends Component implements QuickTimeLib {

    private static Object linkage;

    private SequenceGrabber owner;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SGChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
    }

    SGChannel(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        super(allocate(paramSequenceGrabber, paramInt), paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
    }

    private static int allocate(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = SGNewChannel(QTObject.ID(paramSequenceGrabber), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private void setOwner(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        if (paramSequenceGrabber == null)
            throw new StdQTException(-50);
        this.owner = paramSequenceGrabber;
    }

    void disposeChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        if (this.owner != paramSequenceGrabber) {
            throw new StdQTException(-50);
        }
        StdQTException.checkError(SGDisposeChannel(QTObject.ID(this.owner), _ID()));
        setNR(0);
        this.owner = null;
    }

    /**
     * This method allows you to retrieve a sample description from the SGChannel.
     * QuickTime::SGGetChannelSampleDescription()
     * @return a SampleDescription object.
     * @return {@link quicktime.QTException}
     */
    public final SampleDescription getSampleDescription() throws QTException {
        SampleDescription localSampleDescription = makeDescription();
        int i = SGGetChannelSampleDescription(_ID(), QTObject.ID(localSampleDescription));
        StdQTException.checkError(i);
        return localSampleDescription;
    }

    /**
     * Returns the SequenceGrabber that the channel is attached too.
     */
    public SequenceGrabber getSequenceGrabber() {
        return this.owner;
    }

    /**
     * Instructs the sequence grabber to display its settings dialog box to the user.
     * QuickTime::SGSettingsDialog
     */
    public void settingsDialog() throws StdQTException {
        final int[] arrayOfInt = null;
        final int i = _ID();
        int j;
        if (QTSession.isCurrentOS(4)) {
            j = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner), i, (short) 0, arrayOfInt, 0, 0, 0);
                    return l;
                }
            })).intValue();
            StdQTException.checkError(j);
        } else {
            synchronized (QTNative.globalsLock) {
                j = SGSettingsDialog(QTObject.ID(this.owner), _ID(), (short) 0, arrayOfInt, 0, 0, 0);
                StdQTException.checkError(j);
            }
        }
    }

    /**
     * Instructs the sequence grabber to display its settings dialog box to the user.
     * QuickTime::SGSettingsDialog
     * @param flags The control flags for the current operation.
     * @param panelList An array of sequence grabber panel component identifiers.
     *   Will only present these components in the panel component pop-up menu. Set to null to use all identifiers.
     *          SGPanel[] panelList
     * @param {@link quicktime.std.StdQTException}
     */
    public void settingsDialog(int paramInt, Component[] paramArrayOfComponent) throws StdQTException {
        int i = 0;
        final int j = i;
        int[] arrayOfInt1 = null;
        final int[] arrayOfInt2 = arrayOfInt1;
        final int k = paramInt;
        int m = 0;
        final int n = _ID();
        if (paramArrayOfComponent != null) {
            i = paramArrayOfComponent.length;
            arrayOfInt1 = new int[i];
            for (int i1 = 0; i1 < i; i1++) {
                arrayOfInt1[i1] = QTObject.ID(paramArrayOfComponent[i1]);
            }
        }
        if (QTSession.isCurrentOS(4)) {
            int o = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                public Object invoke() {
                    int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner), n, (short) j, arrayOfInt2, k, 0, 0);
                    return l;
                }
            })).intValue();
            StdQTException.checkError(o);
        } else {
            synchronized (QTNative.globalsLock) {
                int o = SGSettingsDialog(QTObject.ID(this.owner), _ID(), (short) j, arrayOfInt1, paramInt, 0, 0);
                StdQTException.checkError(o);
            }
        }
    }

    /**
     * Set the source identification information associated with a sequence grabber channel.
     * QuickTime::SGChannelSetDataSourceName
     * @param name Source identification name.
     * @param scriptTag source informationÕs language code.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDataSourceName(String paramString, int paramInt) throws StdQTException {
        int i = SGChannelSetDataSourceName(_ID(), QTUtils.String2PString(paramString, 255), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Get the source identification information associated with a sequence grabber channel.
     * QuickTime::SGChannelGetDataSourceName
     */
    public SGDataSourceInfo getDataSourceName() throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[256];
        int i = SGChannelGetDataSourceName(_ID(), arrayOfByte, arrayOfInt);
        StdQTException.checkError(i);
        return new SGDataSourceInfo(QTUtils.PString2String(arrayOfByte, 0), arrayOfInt[0]);
    }

    /**
     * Configure a SequenceGrabber channel based on the UserData
     * passed in. You typically get the UserData via a call to
     * NewUserDataFromHandle or from first calling SGGetChannelSettings.
     * QuickTime::SGSetChannelSettings
     * @param ud Specifies a user data list with configuration information for the sequence grabber.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSettings(UserData paramUserData) throws StdQTException {
        int i = SGSetChannelSettings(QTObject.ID(this.owner), _ID(), QTObject.ID(paramUserData), 0);
        StdQTException.checkError(i);
    }

    /**
     * Retrieves the current settings of the SequenceGrabber channel.
     * The sequence grabber places this configuration information into a Movie Toolbox user data list.
     * QuickTime::SGGetChannelSettings
     */
    public UserData getSettings() throws StdQTException {
        return UserData.fromSGChannel(this);
    }

    /**
     * Specifies how a channel is to be used by the SequenceGrabber component.
     * QuickTime::SGSetChannelUsage
     * @param usage contains flags that specify how the channel is to be used
     * @param {@link quicktime.std.StdQTException}
     */
    public void setUsage(int paramInt) throws StdQTException {
        StdQTException.checkError(SGSetChannelUsage(_ID(), paramInt));
    }

    /**
     * Returns how the channel is being used by the SequenceGrabber component.
     * QuickTime::SGGetChannelUsage
     * @return current usage flags
     * @return {@link quicktime.std.StdQTException}
     */
    public int getUsage() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SGGetChannelUsage(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Allows you to influence the speed and quality with which the sequence grabber displays data
     * from a channel.
     * QuickTime::SGSetChannelPlayFlags
     * @param playFlags the flags
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPlayFlags(int paramInt) throws StdQTException {
        StdQTException.checkError(SGSetChannelPlayFlags(_ID(), paramInt));
    }

    /**
     * Allows you to get the speed and quality with which the sequence grabber displays data
     * from a channel.
     * QuickTime::SGGetChannelPlayFlags
     * @return the flags
     * @return {@link quicktime.std.StdQTException}
     */
    public int getPlayFlags() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SGGetChannelPlayFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * The method allows the sequence grabber to assign a device to your channel.
     * QuickTime::SGSetChannelDevice
     */
    public void setDevice(String paramString) throws StdQTException {
        StdQTException.checkError(SGSetChannelDevice(_ID(), QTUtils.String2PString(paramString, 255)));
    }

    /**
     * Get the list of currently available Sound Input Driver names.
     * QuickTime::SGGetChannelDeviceList
     * @return the sound input names list.
     * @return {@link quicktime.std.StdQTException}
     * @see (String driverName) to set the input driver.
     */
    public SGDeviceList getDeviceList(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        paramInt ^= 0x1;
        int i = SGGetChannelDeviceList(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return new SGDeviceList(arrayOfInt[0], null, this.owner);
    }

    /**
     * This method allows you to retrieve a channelÕs time scale.
     * QuickTime::SGGetChannelTimeScale
     * @return the time scale
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTimeScale() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = SGGetChannelTimeScale(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * QuickTime::SGSetChannelOutput
     */
    public void setOutput(SGOutput paramSGOutput) throws StdQTException {
        paramSGOutput.setChannel(this);
    }

    abstract SampleDescription makeDescription() throws QTException;

    private static int SGSetChannelDevice(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetChannelDevice(paramInt, paramArrayOfByte);
    }

    private static int SGNewChannel(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGNewChannel(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SGSettingsDialog(int paramInt1, int paramInt2, short paramShort, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5) {
        return SequenceGrabberManager.SGSettingsDialog(paramInt1, paramInt2, paramShort, paramArrayOfInt, paramInt3, paramInt4, paramInt5);
    }

    private static int SGChannelSetDataSourceName(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        return SequenceGrabberManager.SGChannelSetDataSourceName(paramInt1, paramArrayOfByte, paramInt2);
    }

    private static int SGChannelGetDataSourceName(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGChannelGetDataSourceName(paramInt, paramArrayOfByte, paramArrayOfInt);
    }

    private static int SGSetChannelSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return SequenceGrabberManager.SGSetChannelSettings(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int SGInitChannel(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGInitChannel(paramInt1, paramInt2);
    }

    private static int SGSetChannelUsage(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGSetChannelUsage(paramInt1, paramInt2);
    }

    private static int SGGetChannelUsage(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetChannelUsage(paramInt, paramArrayOfInt);
    }

    private static int SGSetChannelPlayFlags(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGSetChannelPlayFlags(paramInt1, paramInt2);
    }

    private static int SGGetChannelPlayFlags(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetChannelPlayFlags(paramInt, paramArrayOfInt);
    }

    private static int SGGetChannelDeviceList(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetChannelDeviceList(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SGGetChannelSampleDescription(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGGetChannelSampleDescription(paramInt1, paramInt2);
    }

    private static int SGGetChannelTimeScale(int paramInt, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetChannelTimeScale(paramInt, paramArrayOfInt);
    }

    private static int SGDisposeChannel(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGDisposeChannel(paramInt1, paramInt2);
    }
}

