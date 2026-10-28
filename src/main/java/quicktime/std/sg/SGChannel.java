package quicktime.std.sg;

import com.apple.mrj.macos.carbon.CarbonAccess;
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

    private SequenceGrabber owner;

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
     *
     * @return a SampleDescription object.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetChannelSampleDescription()"
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
     *
     * @see "QuickTime::SGSettingsDialog"
     */
    public void settingsDialog() throws StdQTException {
        int[] arrayOfInt = null;
        int i = _ID();
        int j;
        if (QTSession.isCurrentOS(4)) {
            j = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner), i, (short) 0, arrayOfInt, 0, 0, 0);
                return l;
            });
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
     *
     * @param flags     The control flags for the current operation.
     * @param panelList An array of sequence grabber panel component identifiers.
     *                  Will only present these components in the panel component pop-up menu. Set to null to use all identifiers.
     *                  SGPanel[] panelList
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSettingsDialog"
     */
    public void settingsDialog(int flags, Component[] panelList) throws StdQTException {
        int i = 0;
        int j = i;
        int[] arrayOfInt1 = null;
        int[] arrayOfInt2 = arrayOfInt1;
        int k = flags;
        int m = 0;
        int n = _ID();
        if (panelList != null) {
            i = panelList.length;
            arrayOfInt1 = new int[i];
            for (int i1 = 0; i1 < i; i1++) {
                arrayOfInt1[i1] = QTObject.ID(panelList[i1]);
            }
        }
        if (QTSession.isCurrentOS(4)) {
            int o = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner), n, (short) j, arrayOfInt2, k, 0, 0);
                return l;
            });
            StdQTException.checkError(o);
        } else {
            synchronized (QTNative.globalsLock) {
                int o = SGSettingsDialog(QTObject.ID(this.owner), _ID(), (short) j, arrayOfInt1, flags, 0, 0);
                StdQTException.checkError(o);
            }
        }
    }

    /**
     * Set the source identification information associated with a sequence grabber channel.
     *
     * @param name      Source identification name.
     * @param scriptTag source informationÕs language code.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGChannelSetDataSourceName"
     */
    public void setDataSourceName(String name, int scriptTag) throws StdQTException {
        int i = SGChannelSetDataSourceName(_ID(), QTUtils.String2PString(name, 255), scriptTag);
        StdQTException.checkError(i);
    }

    /**
     * Get the source identification information associated with a sequence grabber channel.
     *
     * @see "QuickTime::SGChannelGetDataSourceName"
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
     *
     * @param ud Specifies a user data list with configuration information for the sequence grabber.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetChannelSettings"
     */
    public void setSettings(UserData ud) throws StdQTException {
        int i = SGSetChannelSettings(QTObject.ID(this.owner), _ID(), QTObject.ID(ud), 0);
        StdQTException.checkError(i);
    }

    /**
     * Retrieves the current settings of the SequenceGrabber channel.
     * The sequence grabber places this configuration information into a Movie Toolbox user data list.
     *
     * @see "QuickTime::SGGetChannelSettings"
     */
    public UserData getSettings() throws StdQTException {
        return UserData.fromSGChannel(this);
    }

    /**
     * Specifies how a channel is to be used by the SequenceGrabber component.
     *
     * @param usage contains flags that specify how the channel is to be used
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetChannelUsage"
     */
    public void setUsage(int usage) throws StdQTException {
        StdQTException.checkError(SGSetChannelUsage(_ID(), usage));
    }

    /**
     * Returns how the channel is being used by the SequenceGrabber component.
     *
     * @return current usage flags
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetChannelUsage"
     */
    public int getUsage() throws StdQTException {
        int[] arrayOfInt = {0};
        StdQTException.checkError(SGGetChannelUsage(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Allows you to influence the speed and quality with which the sequence grabber displays data
     * from a channel.
     *
     * @param playFlags the flags
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetChannelPlayFlags"
     */
    public void setPlayFlags(int playFlags) throws StdQTException {
        StdQTException.checkError(SGSetChannelPlayFlags(_ID(), playFlags));
    }

    /**
     * Allows you to get the speed and quality with which the sequence grabber displays data
     * from a channel.
     *
     * @return the flags
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetChannelPlayFlags"
     */
    public int getPlayFlags() throws StdQTException {
        int[] arrayOfInt = {0};
        StdQTException.checkError(SGGetChannelPlayFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * The method allows the sequence grabber to assign a device to your channel.
     *
     * @see "QuickTime::SGSetChannelDevice"
     */
    public void setDevice(String driverName) throws StdQTException {
        StdQTException.checkError(SGSetChannelDevice(_ID(), QTUtils.String2PString(driverName, 255)));
    }

    /**
     * Get the list of currently available Sound Input Driver names.
     *
     * @return the sound input names list.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetChannelDeviceList"
     * @see #setDevice(String driverName) to set the input driver.
     */
    public SGDeviceList getDeviceList(int paramInt) throws StdQTException {
        int[] arrayOfInt = {0};
        paramInt ^= 0x1;
        int i = SGGetChannelDeviceList(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return new SGDeviceList(arrayOfInt[0], null, this.owner);
    }

    /**
     * This method allows you to retrieve a channelÕs time scale.
     *
     * @return the time scale
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetChannelTimeScale"
     */
    public int getTimeScale() throws StdQTException {
        int[] arrayOfInt = {0};
        int i = SGGetChannelTimeScale(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * @see "QuickTime::SGSetChannelOutput"
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
