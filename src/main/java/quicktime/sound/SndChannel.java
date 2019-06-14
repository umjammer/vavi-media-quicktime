package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.SoundLib;
import quicktime.std.clocks.Clock;
import quicktime.std.movies.media.LevelMeterInfo;
import quicktime.std.movies.media.MediaEQSpectrumBands;
import quicktime.util.QTUtils;

/**
 * Sound Channels are used to playback sound.
 */
public final class SndChannel extends QTObject implements InterfaceLib, SoundLib {

    private static Object linkage;

    private static SoundRunner tempUPP;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SndChannel.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     * QuickTime::SndNewChannel
     */
    public SndChannel() throws QTException {
        this(5, 192);
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     * QuickTime::SndNewChannel
     * @param synth the sound data type you intend to play on this channel
     * @param init the desired initialization parameters for the channel.
     */
    public SndChannel(int paramInt1, int paramInt2) throws QTException {
        super(allocate((short) paramInt1, paramInt2));
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     * QuickTime::SndNewChannel
     * @param callback the (optional) callback that can be called when a sound has completed.
     */
    public SndChannel(SoundCallBack paramSoundCallBack) throws QTException {
        this(5, 192, paramSoundCallBack);
    }

    /**
     * Opens a new SndChannel and allocates memory to store the commands that will control
     * the operation of the sound channel.
     * QuickTime::SndNewChannel
     * @param synth the sound data type you intend to play on this channel
     * @param init the desired initialization parameters for the channel.
     * @param callback the (optional) callback that can be called when a sound has completed.
     */
    public SndChannel(int paramInt1, int paramInt2, SoundCallBack paramSoundCallBack) throws QTException {
        super(allocate((short) paramInt1, paramInt2, paramSoundCallBack));
        if (paramSoundCallBack != null) {
            this.sndUPP = tempUPP;
            this.sndUPP.setSndChannel(this);
        }
        tempUPP = null;
    }

    private static int allocate(short paramShort, int paramInt, SoundCallBack paramSoundCallBack) throws QTException {
        int[] arrayOfInt = { 0 };
        if (paramSoundCallBack != null) {
            tempUPP = new SoundRunner(paramSoundCallBack);
            SoundException.checkError(SndNewChannel(arrayOfInt, paramShort, paramInt, tempUPP.getClosure()));
        } else {
            SoundException.checkError(SndNewChannel(arrayOfInt, paramShort, paramInt, 0));
        }
        return arrayOfInt[0];
    }

    private static int allocate(short paramShort, int paramInt) throws SoundException {
        int[] arrayOfInt = { 0 };
        SoundException.checkError(SndNewChannel(arrayOfInt, paramShort, paramInt, 0));
        return arrayOfInt[0];
    }

    private boolean quietNow = false;

    private byte[] scratchArray;

    private SoundRunner sndUPP;

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
    public void setQuietNow(boolean paramBoolean) {
        this.quietNow = paramBoolean;
    }

    /**
     * Plays a sound asynchronously from memory.
     * QuickTime::SndPlay
     * @param sndHdl the handle that has the sound data in it
     * @param {@link quicktime.sound.SoundException}
     */
    public void play(SndHandle paramSndHandle) throws SoundException {
        SoundException.checkError(SndPlay(_ID(), QTObject.ID(paramSndHandle), (byte) 1));
    }

    /**
     * Returns true if the channel is both NOT busy and NOT paused - ie. there are
     * no commands to waiting to be processed for the channel and it is not paused.
     * QuickTime::SndChannelStatus
     * @return a boolean
     * @return {@link quicktime.sound.SoundException}
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
     * QuickTime::SndChannelStatus
     * @return a boolean
     * @return {@link quicktime.sound.SoundException}
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
     * QuickTime::SndChannelStatus
     * @return SCStatus
     * @return {@link quicktime.sound.SoundException}
     */
    public SCStatus status() throws SoundException {
        SCStatus localSCStatus = new SCStatus();
        int i = SndChannelStatus(_ID(), (short) 24, localSCStatus.getBytes());
        SoundException.checkError(i);
        return localSCStatus;
    }

    /**
     * The SndDoCommand function sends the sound command specified in the cmd parameter to the end of the command queue of the channel specified in the chan parameter. The noWait is set to true
     * QuickTime::SndDoCommand
     * @param cmd A sound command to be sent to the channel
     * @param {@link quicktime.sound.SoundException}
     */
    public void doCommand(SndCommand paramSndCommand) throws SoundException {
        doCommand(paramSndCommand, true);
    }

    /**
     * The SndDoCommand function sends the sound command specified in the cmd parameter to the end of the command queue of the channel specified in the chan parameter.
     * QuickTime::SndDoCommand
     * @param cmd A sound command to be sent to the channel
     * @param noWait noWait	A flag indicating whether the Sound Manager should wait for a free space in a full queue (FALSE) or whether it should return immediately with a queueFull result code if the queue is full (TRUE).
     * @param {@link quicktime.sound.SoundException}
     */
    public void doCommand(SndCommand paramSndCommand, boolean paramBoolean) throws SoundException {
        SoundException.checkError(SndDoCommand(_ID(), QTObject.ID(paramSndCommand), (byte) (paramBoolean ? 1 : 0)));
    }

    /**
     * The SndDoImmediate function operates much like SndDoCommand, except that it bypasses the existing command queue of the sound channel and sends the specified command directly to the Sound Manager for immediate processing.
     * QuickTime::SndDoImmediate
     * @param cmd A sound command to be sent to the channel
     * @param {@link quicktime.sound.SoundException}
     */
    public void doImmediate(SndCommand paramSndCommand) throws SoundException {
        SoundException.checkError(SndDoImmediate(_ID(), QTObject.ID(paramSndCommand)));
    }

    /**
     * Gets Level Meter state.
     * QuickTime::SndGetInfo
     * @return the balance value
     * @return {@link quicktime.sound.SoundException}
     */
    public boolean getLevelMeterOnOff() throws SoundException {
        byte[] arrayOfByte = { 0 };
        SoundException.checkError(SndGetInfo(_ID(), 1819108724, arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * Sets Level Meter state.
     * QuickTime::SndSetInfo
     * @param enabled true if enabled
     * @param {@link quicktime.sound.SoundException}
     */
    public void setLevelMeterOnOff(boolean paramBoolean) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1819108724, (byte) (paramBoolean ? 1 : 0)));
    }

    /**
     * Returns the current clock of the SoundClock.
     * QuickTime::SndSetInfo
     * @return the clock
     * @return {@link quicktime.sound.SoundException}
     */
    public void enableClock(boolean paramBoolean) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1935895659, (byte) (paramBoolean ? 1 : 0)));
    }

    /**
     * Returns the current clock of the SoundClock.
     * QuickTime::SndGetInfo
     * @return the clock
     * @return {@link quicktime.sound.SoundException}
     */
    public Clock getClock() throws SoundException {
        return Clock.fromSndChannel(this);
    }

    /**
     * Gets the level values.
     * QuickTime::SndGetInfo
     * @return LevelMeterInfo object
     * @return {@link quicktime.sound.SoundException}
     */
    public LevelMeterInfo getActiveLevels() throws SoundException {
        LevelMeterInfo localLevelMeterInfo = new LevelMeterInfo();
        int i = SndGetInfo(_ID(), 1819107683, localLevelMeterInfo.getBytes());
        return localLevelMeterInfo;
    }

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with their frequncy limits.
     * QuickTime::SndGetInfo
     * @return MediaEQSpectrumBandsRecord object
     * @return {@link quicktime.sound.SoundException}
     */
    public MediaEQSpectrumBands getEQSpectrumBands(int paramInt) throws SoundException {
        MediaEQSpectrumBands localMediaEQSpectrumBands = new MediaEQSpectrumBands(paramInt);
        SoundException.checkError(SndGetInfo(_ID(), 1701933922, localMediaEQSpectrumBands.getBytes()));
        return localMediaEQSpectrumBands;
    }

    /**
     * Set the array of fequencies that are used to calculate spectral information.
     * The frequencies are the upper limits for each band, thus the first frequency specified (say 100) will
     * set that first band from 0 to 100Hz, the second value (say 200) sets the second band from 100 to 200Hz.
     * QuickTime::SndSetInfo
     * @param spectrumInfo MediaEQSpectrumBandsRecord object
     * @param {@link quicktime.sound.SoundException}
     */
    public void setEQSpectrumBands(MediaEQSpectrumBands paramMediaEQSpectrumBands) throws SoundException {
        SoundException.checkError(SndSetInfo(_ID(), 1701933922, paramMediaEQSpectrumBands.getBytes()));
    }

    /**
     * Gets the Equalizer Band Levels. The levels are equal to supplied the bands count.
     * QuickTime::SndGetInfo
     * @return bandLevels
     * @return {@link quicktime.sound.SoundException}
     */
    public int[] getEQSpectrumLevels(int paramInt) throws SoundException {
        byte[] arrayOfByte = new byte[paramInt];
        SoundException.checkError(SndGetInfo(_ID(), 1701932150, arrayOfByte));
        int[] arrayOfInt = new int[paramInt];
        for (int i = 0; i < arrayOfInt.length; i++) {
            arrayOfInt[i] = QTUtils.UByte2Int(arrayOfByte[i]);
        }
        return arrayOfInt;
    }

    /**
     * Returns true if the tone control (bass and treble) are enabled.
     * QuickTime::SndGetInfo
     * @return a boolean.
     * @return {@link quicktime.sound.SoundException}
     */
    public boolean getEQToneControlOnOff() throws SoundException {
        byte[] arrayOfByte = { 0 };
        SoundException.checkError(SndGetInfo(_ID(), 1701934179, arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * If true enables the tone control (bass and treble).
     * QuickTime::SndSetInfo
     * @param flag a boolean.
     * @param {@link quicktime.sound.SoundException}
     */
    public void setEQToneControlOnOff(boolean paramBoolean) throws SoundException {
        byte[] arrayOfByte = { 0 };
        SoundException.checkError(SndSetInfo(_ID(), 1701934179, (byte) (paramBoolean ? 1 : 0)));
    }

    /**
     * Sets the values of the bass and treble tone control gain values.
     * Range is 0 to 255 unity gain. You can overdrive these values...
     * QuickTime::SndSetInfo
     * @param bass the sound bass
     * @param treble the sound treble
     * @param {@link quicktime.sound.SoundException}
     */
    public void setEQToneControlGain(int paramInt1, int paramInt2) throws SoundException {
        int i = QTUtils.X2ShortFix(paramInt1);
        int j = QTUtils.X2ShortFix(paramInt2);
        int k = j << 16 | i & 0xFFFF;
        SoundException.checkError(SndSetInfo(_ID(), 1701934183, k));
    }

    /**
     * Gets the values of the bass and treble tone control gain values.
     * Range is 0 to 255 unity gain. You can overdrive these values...
     * QuickTime::SndGetInfo
     * @return an array on length 2, containing 1st element as bass and 2nd element as treble.
     * @return {@link quicktime.sound.SoundException}
     */
    public int[] getEQToneControlGain() throws SoundException {
        int[] arrayOfInt = new int[2];
        SoundException.checkError(SndGetInfo(_ID(), 1701934183, arrayOfInt));
        int i = arrayOfInt[0];
        arrayOfInt[0] = (i | 0xFFFF);
        arrayOfInt[1] = (i >>> 16);
        return arrayOfInt;
    }

    private static native short SndNewChannel(int[] paramArrayOfInt, short paramShort, int paramInt1, int paramInt2);

    private static native short SndPlay(int paramInt1, int paramInt2, byte paramByte);

    private static native short SndChannelStatus(int paramInt, short paramShort, byte[] paramArrayOfByte);

    private static native short SndDoCommand(int paramInt1, int paramInt2, byte paramByte);

    private static native short SndDoImmediate(int paramInt1, int paramInt2);

    private static native short SndSetInfo(int paramInt1, int paramInt2, byte paramByte);

    private static native short SndSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SndSetInfo(int paramInt1, int paramInt2, int paramInt3);

    private static native short SndGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SndGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SndChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
