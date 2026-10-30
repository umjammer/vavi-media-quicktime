package quicktime.sound;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.SoundLib;
import quicktime.std.clocks.Clock;
import quicktime.std.movies.media.LevelMeterInfo;
import quicktime.std.movies.media.MediaEQSpectrumBands;
import quicktime.util.QTUtils;


/**
 * Sound Channels are used to playback sound.
 */
public final class SndChannel extends QTObject implements InterfaceLib, SoundLib {

    private static SoundRunner tempUPP;

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     *
     * @see "QuickTime::SndNewChannel"
     */
    public SndChannel() throws QTException {
        this(5, 192);
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     *
     * @param synth the sound data type you intend to play on this channel
     * @param init  the desired initialization parameters for the channel.
     * @see "QuickTime::SndNewChannel"
     */
    public SndChannel(int synth, int init) throws QTException {
        super(allocate((short) synth, init));
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     *
     * @param callback the (optional) callback that can be called when a sound has completed.
     * @see "QuickTime::SndNewChannel"
     */
    public SndChannel(SoundCallBack callback) throws QTException {
        this(5, 192, callback);
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     *
     * @param synth    the sound data type you intend to play on this channel
     * @param init     the desired initialization parameters for the channel.
     * @param callback the (optional) callback that can be called when a sound has completed.
     * @see "QuickTime::SndNewChannel"
     */
    public SndChannel(int synth, int init, SoundCallBack callback) throws QTException {
        super(allocate((short) synth, init, callback));
        if (callback != null) {
            this.sndUPP = tempUPP;
            this.sndUPP.setSndChannel(this);
        }
        tempUPP = null;
    }

    private static int allocate(short synth, int init, SoundCallBack callback) throws QTException {
        int[] chan = {0};
        if (callback != null) {
            tempUPP = new SoundRunner(callback);
            SoundException.checkError(SndNewChannel(chan, synth, init, tempUPP.getClosure()));
        } else {
            SoundException.checkError(SndNewChannel(chan, synth, init, 0));
        }
        return chan[0];
    }

    private static int allocate(short synth, int init) throws SoundException {
        int[] chan = {0};
        SoundException.checkError(SndNewChannel(chan, synth, init, 0));
        return chan[0];
    }

    private boolean quietNow = false;

    private byte[] scratchArray;

    private SoundRunner sndUPP;

    @Override
    protected void _dispose() throws QTException {
        if (this.sndUPP != null) {
            this.sndUPP.cleanup();
            this.sndUPP = null;
        }
    }

    /**
     * Returns the current setting of the quietNow flag.
     */
    public boolean isQuietNow() {
        return this.quietNow;
    }

    /**
     * This flag controls whether a sound channel is silent when it is disposed of.
     * If this is false, then when the SndChannel is diposed, the disposal call becomes
     * synchronous and waits until the the sound is actually finished. If this is true,
     * the SndChannel's queue is flushed immediately, which may silent a sound if it is playing.
     * The default setting is to allow the sound to finish playing (the flag is set to false).
     * If you wish to explicitly stop a sound when you dispose the channel then set this to true.
     */
    public void setQuietNow(boolean quietNow) {
        this.quietNow = quietNow;
    }

    /**
     * Plays a sound asynchronously from memory.
     *
     * @param sndHdl the handle that has the sound data in it
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndPlay"
     */
    public void play(SndHandle sndHdl) throws SoundException {
        SoundException.checkError(SndPlay(_ID(), QTObject.ID(sndHdl), (byte) 1));
    }

    /**
     * Returns true if the channel is both NOT busy and NOT paused - ie. there are
     * no commands to waiting to be processed for the channel and it is not paused.
     *
     * @return a boolean
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndChannelStatus"
     */
    public boolean isFinished() throws SoundException {
        if (this.scratchArray == null)
            this.scratchArray = new byte[24];
        int i = SndChannelStatus(_ID(), (short) 24, this.scratchArray);
        SoundException.checkError(i);
        return (this.scratchArray[12] == 0) && (this.scratchArray[14] == 0);
    }

    /**
     * Returns true if the channel is currently paused.
     *
     * @return a boolean
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndChannelStatus"
     */
    public boolean isPaused() throws SoundException {
        if (this.scratchArray == null)
            this.scratchArray = new byte[24];
        int i = SndChannelStatus(_ID(), (short) 24, this.scratchArray);
        SoundException.checkError(i);
        return this.scratchArray[14] != 0;
    }

    /**
     * The fields of the record returned by this call accurately
     * describe the sound channel specified.
     *
     * @return SCStatus
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndChannelStatus"
     */
    public SCStatus status() throws SoundException {
        SCStatus scStatus = new SCStatus();
        int i = SndChannelStatus(_ID(), (short) 24, scStatus.getBytes());
        SoundException.checkError(i);
        return scStatus;
    }

    /**
     * The SndDoCommand function sends the sound command specified in the cmd parameter to the end of the command queue of the channel specified in the chan parameter. The noWait is set to true
     *
     * @param cmd A sound command to be sent to the channel
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndDoCommand"
     */
    public void doCommand(SndCommand cmd) throws SoundException {
        doCommand(cmd, true);
    }

    /**
     * The SndDoCommand function sends the sound command specified in the cmd parameter to the end of the command queue of the channel specified in the chan parameter.
     *
     * @param cmd    A sound command to be sent to the channel
     * @param noWait noWait A flag indicating whether the Sound Manager should wait for a free space in a full queue (FALSE) or whether it should return immediately with a queueFull result code if the queue is full (TRUE).
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndDoCommand"
     */
    public void doCommand(SndCommand cmd, boolean noWait) throws SoundException {
        SoundException.checkError(SndDoCommand(_ID(), QTObject.ID(cmd), (byte) (noWait ? 1 : 0)));
    }

    /**
     * The SndDoImmediate function operates much like SndDoCommand, except that it bypasses the existing command queue of the sound channel and sends the specified command directly to the Sound Manager for immediate processing.
     *
     * @param cmd A sound command to be sent to the channel
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndDoImmediate"
     */
    public void doImmediate(SndCommand cmd) throws SoundException {
        SoundException.checkError(SndDoImmediate(_ID(), QTObject.ID(cmd)));
    }

    /**
     * Gets Level Meter state.
     *
     * @return the balance value
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public boolean getLevelMeterOnOff() throws SoundException {
        byte[] info = {0};
        SoundException.checkError(SndGetInfo(_ID(), 1819108724, info));
        return info[0] != 0;
    }

    /**
     * Sets Level Meter state.
     *
     * @param enabled true if enabled
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndSetInfo"
     */
    public void setLevelMeterOnOff(boolean enabled) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1819108724, (byte) (enabled ? 1 : 0)));
    }

    /**
     * Returns the current clock of the SoundClock.
     *
     * @return the clock
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndSetInfo"
     */
    public void enableClock(boolean enable) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1935895659, (byte) (enable ? 1 : 0)));
    }

    /**
     * Returns the current clock of the SoundClock.
     *
     * @return the clock
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public Clock getClock() throws SoundException {
        return Clock.fromSndChannel(this);
    }

    /**
     * Gets the level values.
     *
     * @return LevelMeterInfo object
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public LevelMeterInfo getActiveLevels() throws SoundException {
        LevelMeterInfo activeLevels = new LevelMeterInfo();
        int i = SndGetInfo(_ID(), 1819107683, activeLevels.getBytes());
        return activeLevels;
    }

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with their frequncy limits.
     *
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public MediaEQSpectrumBands getEQSpectrumBands(int count) throws SoundException {
        MediaEQSpectrumBands eqSpectrumBands = new MediaEQSpectrumBands(count);
        SoundException.checkError(SndGetInfo(_ID(), 1701933922, eqSpectrumBands.getBytes()));
        return eqSpectrumBands;
    }

    /**
     * Set the array of fequencies that are used to calculate spectral information.
     * The frequencies are the upper limits for each band, thus the first frequency specified (say 100) will
     * set that first band from 0 to 100Hz, the second value (say 200) sets the second band from 100 to 200Hz.
     *
     * @param spectrumInfo MediaEQSpectrumBandsRecord object
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndSetInfo"
     */
    public void setEQSpectrumBands(MediaEQSpectrumBands spectrumInfo) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1701933922, spectrumInfo.getBytes()));
    }

    /**
     * Gets the Equalizer Band Levels. The levels are equal to supplied the bands count.
     *
     * @return bandLevels
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public int[] getEQSpectrumLevels(int numBands) throws SoundException {
        byte[] info = new byte[numBands];
        SoundException.checkError(SndGetInfo(_ID(), 1701932150, info));
        int[] eqSpectrumLevels = new int[numBands];
        for (int i = 0; i < eqSpectrumLevels.length; i++) {
            eqSpectrumLevels[i] = QTUtils.UByte2Int(info[i]);
        }
        return eqSpectrumLevels;
    }

    /**
     * Returns true if the tone control (bass and treble) are enabled.
     *
     * @return a boolean.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public boolean getEQToneControlOnOff() throws SoundException {
        byte[] info = {0};
        SoundException.checkError(SndGetInfo(_ID(), 1701934179, info));
        return info[0] != 0;
    }

    /**
     * If true enables the tone control (bass and treble).
     *
     * @param flag a boolean.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndSetInfo"
     */
    public void setEQToneControlOnOff(boolean flag) throws SoundException {
        byte[] arrayOfByte = {0};
        SoundException.checkError(SndSetInfo(_ID(), 1701934179, (byte) (flag ? 1 : 0)));
    }

    /**
     * Sets the values of the bass and treble tone control gain values.
     * Range is 0 to 255 unity gain. You can overdrive these values...
     *
     * @param bass   the sound bass
     * @param treble the sound treble
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndSetInfo"
     */
    public void setEQToneControlGain(int bass, int treble) throws SoundException {
        int i = QTUtils.X2ShortFix(bass);
        int j = QTUtils.X2ShortFix(treble);
        int k = j << 16 | i & 0xFFFF;
        SoundException.checkError(SndSetInfo(_ID(), 1701934183, k));
    }

    /**
     * Gets the values of the bass and treble tone control gain values.
     * Range is 0 to 255 unity gain. You can overdrive these values...
     *
     * @return an array on length 2, containing 1st element as bass and 2nd element as treble.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SndGetInfo"
     */
    public int[] getEQToneControlGain() throws SoundException {
        int[] info = new int[2];
        SoundException.checkError(SndGetInfo(_ID(), 1701934183, info));
        int i = info[0];
        info[0] = (i | 0xFFFF);
        info[1] = (i >>> 16);
        return info;
    }

    private static short SndNewChannel(int[] chan, short synth, int init, int userRoutine) {
        return SoundManager.SndNewChannel(chan, synth, init, userRoutine);
    }

    private static short SndPlay(int chan, int sndHandle, byte async) {
        return SoundManager.SndPlay(chan, sndHandle, async);
    }

    private static short SndChannelStatus(int chan, short length, byte[] status) {
        return SoundManager.SndChannelStatus(chan, length, status);
    }

    private static short SndDoCommand(int chan, int cmd, byte noWait) {
        return SoundManager.SndDoCommand(chan, cmd, noWait);
    }

    private static short SndDoImmediate(int chan, int cmd) {
        return SoundManager.SndDoImmediate(chan, cmd);
    }

    private static short SndSetInfo(int chan, int selector, byte value) {
        return SoundManager.SndSetInfo(chan, selector, value);
    }

    private static short SndSetInfo(int chan, int selector, byte[] info) {
        return SoundManager.SndSetInfo(chan, selector, info);
    }

    private static short SndSetInfo(int chan, int selector, int value) {
        return SoundManager.SndSetInfo(chan, selector, value);
    }

    private static short SndGetInfo(int chan, int selector, byte[] info) {
        return SoundManager.SndGetInfo(chan, selector, info);
    }

    private static short SndGetInfo(int chan, int selector, int[] info) {
        return SoundManager.SndGetInfo(chan, selector, info);
    }
}
