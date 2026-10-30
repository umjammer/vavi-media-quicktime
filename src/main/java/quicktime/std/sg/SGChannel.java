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

    SGChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
        setOwner(owner);
    }

    SGChannel(SequenceGrabber owner, int channelType) throws StdQTException {
        super(allocate(owner, channelType), owner);
        setOwner(owner);
    }

    private static int allocate(SequenceGrabber sg, int channelType) throws StdQTException {
        int[] ref = new int[1];
        int i = SGNewChannel(QTObject.ID(sg), channelType, ref);
        StdQTException.checkError(i);
        return ref[0];
    }

    private void setOwner(SequenceGrabber owner) throws StdQTException {
        if (owner == null)
            throw new StdQTException(-50);
        this.owner = owner;
    }

    void disposeChannel(SequenceGrabber sequenceGrabber) throws StdQTException {
        if (this.owner != sequenceGrabber) {
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
        SampleDescription desc = makeDescription();
        int i = SGGetChannelSampleDescription(_ID(), QTObject.ID(desc));
        StdQTException.checkError(i);
        return desc;
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
        int[] panelList = null;
        int i = _ID();
        int j;
        if (QTSession.isCurrentOS(4)) {
            j = (Integer) CarbonAccess.invokeCarbonEventClient(() -> {
                int l = SGChannel.SGSettingsDialog(QTObject.ID(SGChannel.this.owner), i, (short) 0, panelList, 0, 0, 0);
                return l;
            });
            StdQTException.checkError(j);
        } else {
            synchronized (QTNative.globalsLock) {
                j = SGSettingsDialog(QTObject.ID(this.owner), _ID(), (short) 0, panelList, 0, 0, 0);
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
        int[] scriptTag = new int[1];
        byte[] name = new byte[256];
        int i = SGChannelGetDataSourceName(_ID(), name, scriptTag);
        StdQTException.checkError(i);
        return new SGDataSourceInfo(QTUtils.PString2String(name, 0), scriptTag[0]);
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
        int[] usage = {0};
        StdQTException.checkError(SGGetChannelUsage(_ID(), usage));
        return usage[0];
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
        int[] flags = {0};
        StdQTException.checkError(SGGetChannelPlayFlags(_ID(), flags));
        return flags[0];
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
    public SGDeviceList getDeviceList(int flags) throws StdQTException {
        int[] list = {0};
        flags ^= 0x1;
        int i = SGGetChannelDeviceList(_ID(), flags, list);
        StdQTException.checkError(i);
        return new SGDeviceList(list[0], null, this.owner);
    }

    /**
     * This method allows you to retrieve a channelÕs time scale.
     *
     * @return the time scale
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetChannelTimeScale"
     */
    public int getTimeScale() throws StdQTException {
        int[] scale = {0};
        int i = SGGetChannelTimeScale(_ID(), scale);
        StdQTException.checkError(i);
        return scale[0];
    }

    /**
     * @see "QuickTime::SGSetChannelOutput"
     */
    public void setOutput(SGOutput output) throws StdQTException {
        output.setChannel(this);
    }

    abstract SampleDescription makeDescription() throws QTException;

    private static int SGSetChannelDevice(int ref, byte[] name) {
        return SequenceGrabberManager.SGSetChannelDevice(ref, name);
    }

    private static int SGNewChannel(int sg, int channelType, int[] ref) {
        return SequenceGrabberManager.SGNewChannel(sg, channelType, ref);
    }

    private static int SGSettingsDialog(int sg, int ref, short numPanels, int[] panelList, int flags, int proc, int procRefNum) {
        return SequenceGrabberManager.SGSettingsDialog(sg, ref, numPanels, panelList, flags, proc, procRefNum);
    }

    private static int SGChannelSetDataSourceName(int ref, byte[] name, int scriptTag) {
        return SequenceGrabberManager.SGChannelSetDataSourceName(ref, name, scriptTag);
    }

    private static int SGChannelGetDataSourceName(int ref, byte[] name, int[] scriptTag) {
        return SequenceGrabberManager.SGChannelGetDataSourceName(ref, name, scriptTag);
    }

    private static int SGSetChannelSettings(int sg, int ref, int userData, int flags) {
        return SequenceGrabberManager.SGSetChannelSettings(sg, ref, userData, flags);
    }

    private static int SGInitChannel(int ref, int owner) {
        return SequenceGrabberManager.SGInitChannel(ref, owner);
    }

    private static int SGSetChannelUsage(int ref, int usage) {
        return SequenceGrabberManager.SGSetChannelUsage(ref, usage);
    }

    private static int SGGetChannelUsage(int ref, int[] usage) {
        return SequenceGrabberManager.SGGetChannelUsage(ref, usage);
    }

    private static int SGSetChannelPlayFlags(int ref, int flags) {
        return SequenceGrabberManager.SGSetChannelPlayFlags(ref, flags);
    }

    private static int SGGetChannelPlayFlags(int ref, int[] flags) {
        return SequenceGrabberManager.SGGetChannelPlayFlags(ref, flags);
    }

    private static int SGGetChannelDeviceList(int ref, int flags, int[] list) {
        return SequenceGrabberManager.SGGetChannelDeviceList(ref, flags, list);
    }

    private static int SGGetChannelSampleDescription(int ref, int desc) {
        return SequenceGrabberManager.SGGetChannelSampleDescription(ref, desc);
    }

    private static int SGGetChannelTimeScale(int ref, int[] scale) {
        return SequenceGrabberManager.SGGetChannelTimeScale(ref, scale);
    }

    private static int SGDisposeChannel(int sg, int ref) {
        return SequenceGrabberManager.SGDisposeChannel(sg, ref);
    }
}
