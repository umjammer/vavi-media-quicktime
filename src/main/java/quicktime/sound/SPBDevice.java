package quicktime.sound;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SGSoundChannel;
import quicktime.util.QTUtils;


public final class SPBDevice extends QTObject implements QuickTimeLib {

    /**
     * You can use the SPBGetIndexedDevice function to help generate a list of sound input devices.
     *
     * @param count The index number of the sound input device you wish to obtain information about.
     *              2return the name of the sound input device specified by the count parameter or null if there isn't a device at that index.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetIndexedDevice"
     */
    public static String getIndexedDevice(int count) throws SoundException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt = {0};
        int i = SPBGetIndexedDevice((short) count, arrayOfByte, arrayOfInt);
        if (i == 65315)
            return null;
        DisposeHandle(arrayOfInt[0]);
        SoundException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Get the Sound Input Driver
     *
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetSoundInputDriver"
     * @see quicktime.std.sg.SGSoundChannel#getInputDriver()
     */
    public static SPBDevice fromSoundChannel(SGSoundChannel paramSGSoundChannel) throws StdQTException {
        int i = SGGetSoundInputDriver(QTObject.ID(paramSGSoundChannel));
        return new SPBDevice(i, paramSGSoundChannel);
    }

    /**
     * The SPBOpenDevice function attempts to open a sound input device having the name indicated by the deviceName parameter.
     *
     * @param deviceName The name of the sound input device to open, or the empty string if the default sound input device is to be opened.
     * @param permission A flag that indicates whether subsequent operations with that device are to be read/write or read-only.
     * @see "QuickTime::SPBOpenDevice"
     */
    public SPBDevice(String deviceName, int permission) throws SoundException {
        super(allocate(deviceName, permission), null);
    }

    private static int allocate(String paramString, int paramInt) throws SoundException {
        int[] arrayOfInt = {0};
        if ((paramInt == 1) && (QTSession.hasSecurityRestrictions())) {
            throw new SecurityException("Only able to record sound with security settings when class is signed");
        }
        SoundException.checkError(SPBOpenDevice(paramString == null ? null : QTUtils.String2PString(paramString, 255), (short) paramInt, arrayOfInt));
        return arrayOfInt[0];
    }

    private SPBDevice(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    private void getDeviceInfo(int paramInt, byte[] paramArrayOfByte) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt, paramArrayOfByte));
    }

    private void getDeviceInfo(int paramInt1, int paramInt2) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt1, paramInt2));
    }

    private void getDeviceInfo(int paramInt, short[] paramArrayOfShort) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt, paramArrayOfShort));
    }

    private void getDeviceInfo(int paramInt, int[] paramArrayOfInt) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), paramInt, paramArrayOfInt));
    }

    private void setDeviceInfo(int paramInt, short[] paramArrayOfShort) throws SoundException {
        SoundException.checkError(SPBSetDeviceInfo(_ID(), paramInt, paramArrayOfShort));
    }

    private void setDeviceInfo(int paramInt, int[] paramArrayOfInt) throws SoundException {
        SoundException.checkError(SPBSetDeviceInfo(_ID(), paramInt, paramArrayOfInt));
    }

    /**
     * Reports how many bytes are required to store a recording of duration milliseconds, given the input deviceÕs current sample rate, sample size, number of channels, and compression factor.
     *
     * @param msecs how many milliseconds you wish to record
     * @return how many bytes you would require to record the specifiec milliseconds.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBMillisecondsToBytes"
     */
    public int millisecondsToBytes(int msecs) throws SoundException {
        int[] arrayOfInt = {msecs};
        SoundException.checkError(SPBMillisecondsToBytes(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Reports how many milliseconds are required to record the specified bytes amount of sound data, given the input deviceÕs current sample rate, sample size, number of channels, and compression factor.
     *
     * @param bytes how many bytes you wish to record
     * @return how many milliseconds.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBBytesToMilliseconds"
     */
    public int bytesToMilliseconds(int bytes) throws SoundException {
        int[] arrayOfInt = {bytes};
        SoundException.checkError(SPBBytesToMilliseconds(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Set the current state of the level meter.
     *
     * @param flag set to true to turn the channel on, false to turn it off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setLevelMeterOnOff(boolean flag) throws SoundException {
        short[] arrayOfShort = {(short) (flag ? 1 : 0)};
        setDeviceInfo(1819108724, arrayOfShort);
    }

    /**
     * Get the current state of the level meter.
     *
     * @return a boolean that determines whether the channel is on or off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean getLevelMeterOnOff() throws SoundException {
        short[] arrayOfShort = {0, 0};
        getDeviceInfo(1819108724, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    /**
     * Get the current level of the level meter.
     *
     * @return an integer that signals the current level of the channel.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getLevelMeterLevel() throws SoundException {
        short[] arrayOfShort = {0, 0};
        getDeviceInfo(1819108724, arrayOfShort);
        return arrayOfShort[1];
    }

    /**
     * Set the state of the automatic gain control.
     *
     * @param flag set to true to turn automatic gain on, false to turn it off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setAutomaticGainControl(boolean flag) throws SoundException {
        short[] arrayOfShort = {(short) (flag ? 1 : 0)};
        setDeviceInfo(1634165536, arrayOfShort);
    }

    /**
     * Get the state of the automatic gain control.
     *
     * @return a boolean that determines whether automatic gain is on or off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean getAutomaticGainControl() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1634165536, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    /**
     * Set the input gain level.
     *
     * @param gain a float (fixed value) to set the gain to.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setInputGain(float gain) throws SoundException {
        int[] arrayOfInt = {QTUtils.X2Fix(gain)};
        setDeviceInfo(1734437230, arrayOfInt);
    }

    /**
     * Get the input gain level.
     *
     * @return a float (fixed) of the current level on the input gain.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getInputGain() throws SoundException {
        int[] arrayOfInt = {0};
        getDeviceInfo(1734437230, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Get the input Source Index.
     *
     * @return an integr which is the index of the current sound input source
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getInputSource() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1936684402, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * sets the input Source to the passed in (index) source.
     *
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setInputSource(int paramInt) throws SoundException {
        short[] arrayOfShort = {(short) paramInt};
        setDeviceInfo(1936684402, arrayOfShort);
    }

    /**
     * Get a list of the names of all the sound input sources
     * supported by the sound input device
     */
    public String[] getInputSourceNames() throws SoundException {
        byte[] arrayOfByte1 = new byte[4];
        getDeviceInfo(1936613741, arrayOfByte1);
        int j = getIntFromArray(arrayOfByte1, 0);
        int i = getShortFromHandle(j, 0);
        String[] arrayOfString = new String[i];
        int k = 0;
        for (int m = 0; m < i; m++) {
            int n = getByteFromHandle(j, k + 2);
            byte[] arrayOfByte2 = new byte[n + 1];
            copyPointerToArray(getIntFromPointer(j, 0), k + 2, arrayOfByte2, 0, n + 1);
            arrayOfString[m] = QTUtils.PString2String(arrayOfByte2, 0);
            k = k + n + 1;
        }
        DisposeHandle(j);
        return arrayOfString;
    }

    /**
     * Get the maximum number of channels this device can record.
     *
     * @return the maximum number of channels this device can record.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getChannelAvailable() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1667785078, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Returns the number of channels that this device is to record.
     *
     * @return channels number of channels
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getNumberChannels() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1667785070, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Sets the number of channels that this device is to record.
     *
     * @param channels number of channels
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setNumberChannels(int channels) throws SoundException {
        short[] arrayOfShort = {(short) channels};
        setDeviceInfo(1667785070, arrayOfShort);
    }

    /**
     * Get the PlayThru level, 0 means it's off.
     *
     * @return the current PlayThru level.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getPlayThruOnOff() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1886155880, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Set the PlayThru level, 0 means it's off.
     *
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setPlayThruOnOff(int paramInt) throws SoundException {
        short[] arrayOfShort = {(short) paramInt};
        setDeviceInfo(1886155880, arrayOfShort);
    }

    /**
     * Set the sample rate to be produced by this device.
     *
     * @param rate the rate of the device
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setSampleRate(float rate) throws SoundException {
        int[] arrayOfInt = {QTUtils.X2UFix(rate)};
        setDeviceInfo(1936875892, arrayOfInt);
    }

    /**
     * Gets the sample rate produced by this device.
     *
     * @return the rate of the device
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getSampleRate() throws SoundException {
        int[] arrayOfInt = {0};
        getDeviceInfo(1936875892, arrayOfInt);
        return QTUtils.UFix2X(arrayOfInt[0]);
    }

    /**
     * Returns a float list containing the sample rates available.
     *
     * @return a float array containing the list of sample rates.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float[] getSampleRateAvailable() throws SoundException {
        byte[] arrayOfByte = new byte[6];
        getDeviceInfo(1936941430, arrayOfByte);
        int i = getShortFromArray(arrayOfByte, 0);
        float[] arrayOfFloat = null;
        if (i == 0) {
            arrayOfFloat = new float[2];
        } else {
            arrayOfFloat = new float[i];
        }
        for (int j = 0; j < arrayOfFloat.length; j++) {
            int k = getIntFromHandle(getIntFromArray(arrayOfByte, 2), j * 4);
            arrayOfFloat[j] = QTUtils.UFix2X(k);
        }
        DisposeHandle(getIntFromArray(arrayOfByte, 2));
        return arrayOfFloat;
    }

    /**
     * Set the number of bits per sample.
     *
     * @param sampleSize is the number of bits in an individual sample.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setSampleSize(int sampleSize) throws SoundException {
        short[] arrayOfShort = {(short) sampleSize};
        setDeviceInfo(1936943482, arrayOfShort);
    }

    /**
     * Returns the number of bits per sample.
     *
     * @return number of bits in a sample.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getSampleSize() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1936943482, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Returns an integer list containing the sample sizes available.
     *
     * @return an integer array containing the list of samples.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int[] getSampleSizeAvailable() throws SoundException {
        byte[] arrayOfByte = new byte[6];
        getDeviceInfo(1936941430, arrayOfByte);
        int i = getShortFromArray(arrayOfByte, 0);
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++) arrayOfInt[j] = getShortFromHandle(getIntFromArray(arrayOfByte, 2), j * 2);
        DisposeHandle(getIntFromArray(arrayOfByte, 2));
        return arrayOfInt;
    }

    /**
     * Set the the current stereo sound input gain.
     *
     * @param leftChannel  the gain for the left channel (range 0.5 to 1.5)
     * @param rightChannel the gain for the right channel (range 0.5 to 1.5)
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setStereoInputGain(float leftChannel, float rightChannel) throws SoundException {
        int[] arrayOfInt = {QTUtils.X2Fix(leftChannel), QTUtils.X2Fix(rightChannel)};
        setDeviceInfo(1936154985, arrayOfInt);
    }

    /**
     * Returns the current gain setting for the left channel of a stereo device
     *
     * @return the gain setting
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getStereoInputGainLeft() throws SoundException {
        int[] arrayOfInt = {0, 0};
        getDeviceInfo(1936154985, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Returns the current gain setting for the right channel of a stereo device
     *
     * @return the gain setting
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getStereoInputGainRight() throws SoundException {
        int[] arrayOfInt = {0, 0};
        getDeviceInfo(1936154985, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[1]);
    }

    /**
     * Get the state of continuous recording from this device
     *
     * @return the state of continuous recording (false is off, true is on)
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean getContinuousRecording() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1668247156, arrayOfShort);
        return arrayOfShort[0] == 1;
    }

    /**
     * Set the state of continuous recording from this device.
     * Only sound input device drivers that support asynchronous recording support continuous recording.
     *
     * @param isContinuous the state of continuous recording (false is off, true is on)
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setContinuousRecording(boolean isContinuous) throws SoundException {
        short[] arrayOfShort = {(short) (isContinuous ? 1 : 0)};
        setDeviceInfo(1668247156, arrayOfShort);
    }

    /**
     * Set the compression type.
     *
     * @param compType the compressor type.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setCompressionType(int compType) throws SoundException {
        int[] arrayOfInt = {compType};
        setDeviceInfo(1668246896, arrayOfInt);
    }

    /**
     * Returns the current compression type.
     *
     * @return the compression type
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getCompressionType() throws SoundException {
        int[] arrayOfInt = {0};
        getDeviceInfo(1668246896, arrayOfInt);
        return arrayOfInt[0];
    }

    /**
     * Returns an integer (OSTypes) list containing the available compression types.
     *
     * @return an integer array containing the list of compression types.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int[] getCompressionAvailable() throws SoundException {
        byte[] arrayOfByte = new byte[6];
        getDeviceInfo(1668112758, arrayOfByte);
        int i = getShortFromArray(arrayOfByte, 0);
        int[] arrayOfInt = new int[i];
        for (int j = 0; j < i; j++) arrayOfInt[j] = getIntFromHandle(getIntFromArray(arrayOfByte, 2), j * 4);
        DisposeHandle(getIntFromArray(arrayOfByte, 2));
        return arrayOfInt;
    }

    /**
     * Returns true if the current device supports an options dialog box and false if not.
     *
     * @return a boolean
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean hasOptionsDialog() throws SoundException {
        short[] arrayOfShort = {0};
        getDeviceInfo(1869640804, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    /**
     * Shows the options dialog if available.
     *
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void showOptionsDialog() throws SoundException {
        int[] arrayOfInt = {0};
        setDeviceInfo(1869640804, arrayOfInt);
    }

    /**
     * Returns the name of this device.
     *
     * @return a String
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public String getName() throws SoundException {
        byte[] arrayOfByte = new byte[256];
        getDeviceInfo(1851878757, arrayOfByte);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Get the current signal level for each active channel.
     *
     * @return an short[] of the current signal level
     * @throws quicktime.sound.SoundException
     */
    public int[] getActiveLevels() throws SoundException {
        int i = getNumberChannels();
        int j = NewPtr(i * 2);
        getDeviceInfo(1819107683, j);
        int[] arrayOfInt = new int[i];
        for (int k = 0; k < i; k++) arrayOfInt[k] = getShortFromPointer(j, k * 2);
        DisposePtr(j);
        return arrayOfInt;
    }

    /**
     * A String representation of the class.
     *
     * @return a String representation of this object.
     */
    public String toString() {
        try {
            return getClass().getName() + "[name=" + getName() + "]";
        } catch (QTException localQTException) {
        }
        return super.toString();
    }

    private static int SGGetSoundInputDriver(int paramInt) {
        return SequenceGrabberManager.SGGetSoundInputDriver(paramInt);
    }

    private static short SPBOpenDevice(byte[] paramArrayOfByte, short paramShort, int[] paramArrayOfInt) {
        return SoundManager.SPBOpenDevice(paramArrayOfByte, paramShort, paramArrayOfInt);
    }

    private static short SPBGetDeviceInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SoundManager.SPBGetDeviceInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short SPBSetDeviceInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SoundManager.SPBSetDeviceInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short SPBGetDeviceInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return SoundManager.SPBGetDeviceInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static short SPBSetDeviceInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return SoundManager.SPBSetDeviceInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static short SPBGetDeviceInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return SoundManager.SPBGetDeviceInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short SPBGetDeviceInfo(int paramInt1, int paramInt2, int paramInt3) {
        return SoundManager.SPBGetDeviceInfo(paramInt1, paramInt2, paramInt3);
    }

    private static short SPBMillisecondsToBytes(int paramInt, int[] paramArrayOfInt) {
        return SoundManager.SPBMillisecondsToBytes(paramInt, paramArrayOfInt);
    }

    private static short SPBBytesToMilliseconds(int paramInt, int[] paramArrayOfInt) {
        return SoundManager.SPBBytesToMilliseconds(paramInt, paramArrayOfInt);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static byte getByteFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromHandle(paramInt1, paramInt2);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static short getShortFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromPointer(paramInt1, paramInt2);
    }

    private static int getIntFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromHandle(paramInt1, paramInt2);
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }

    private static short getShortFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromHandle(paramInt1, paramInt2);
    }

    private static short SPBGetIndexedDevice(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        return SoundManager.SPBGetIndexedDevice(paramShort, paramArrayOfByte, paramArrayOfInt);
    }

    private static int NewPtr(int paramInt) {
        return MemoryManager.NewPtr(paramInt);
    }

    private static void DisposePtr(int paramInt) {
        MemoryManager.DisposePtr(paramInt);
    }
}
