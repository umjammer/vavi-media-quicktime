package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.sg.SGSoundChannel;
import quicktime.util.QTUtils;

public final class SPBDevice extends QTObject implements QuickTimeLib {

    private static Object linkage;

    static {
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SPBDevice.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * You can use the SPBGetIndexedDevice function to help generate a list of sound input devices.
     * QuickTime::SPBGetIndexedDevice
     * @param count The index number of the sound input device you wish to obtain information about.
     *  2return the name of the sound input device specified by the count parameter or null if there isn't a device at that index.
     * @param {@link quicktime.sound.SoundException}
     */
    public static String getIndexedDevice(int paramInt) throws SoundException {
        byte[] arrayOfByte = new byte[256];
        int[] arrayOfInt = { 0 };
        int i = SPBGetIndexedDevice((short) paramInt, arrayOfByte, arrayOfInt);
        if (i == 65315)
            return null;
        DisposeHandle(arrayOfInt[0]);
        SoundException.checkError(i);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Get the Sound Input Driver
     * QuickTime::SGGetSoundInputDriver
     * @return the Sound Input driver in use by this channel.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.sg.SGSoundChannel#getInputDriver()}
     */
    public static SPBDevice fromSoundChannel(SGSoundChannel paramSGSoundChannel) throws StdQTException {
        int i = SGGetSoundInputDriver(QTObject.ID(paramSGSoundChannel));
        return new SPBDevice(i, paramSGSoundChannel);
    }

    /**
     * The SPBOpenDevice function attempts to open a sound input device having the name indicated by the deviceName parameter.
     * QuickTime::SPBOpenDevice
     * @param deviceName The name of the sound input device to open, or the empty string if the default sound input device is to be opened.
     * @param permission A flag that indicates whether subsequent operations with that device are to be read/write or read-only.
     */
    public SPBDevice(String paramString, int paramInt) throws SoundException {
        super(allocate(paramString, paramInt), null);
    }

    private static int allocate(String paramString, int paramInt) throws SoundException {
        int[] arrayOfInt = { 0 };
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
     * QuickTime::SPBMillisecondsToBytes
     * @param msecs how many milliseconds you wish to record
     * @return how many bytes you would require to record the specifiec milliseconds.
     * @return {@link quicktime.sound.SoundException}
     */
    public int millisecondsToBytes(int paramInt) throws SoundException {
        int[] arrayOfInt = { paramInt };
        SoundException.checkError(SPBMillisecondsToBytes(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Reports how many milliseconds are required to record the specified bytes amount of sound data, given the input deviceÕs current sample rate, sample size, number of channels, and compression factor.
     * QuickTime::SPBBytesToMilliseconds
     * @param bytes how many bytes you wish to record
     * @return how many milliseconds.
     * @return {@link quicktime.sound.SoundException}
     */
    public int bytesToMilliseconds(int paramInt) throws SoundException {
        int[] arrayOfInt = { paramInt };
        SoundException.checkError(SPBBytesToMilliseconds(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Set the current state of the level meter.
     * QuickTime::SPBGetDeviceInfo
     * @param flag set to true to turn the channel on, false to turn it off.
     * @param {@link quicktime.sound.SoundException}
     */
    public void setLevelMeterOnOff(boolean paramBoolean) throws SoundException {
        short[] arrayOfShort = { (short) (paramBoolean ? 1 : 0) };
        setDeviceInfo(1819108724, arrayOfShort);
    }

    /**
     * Get the current state of the level meter.
     * QuickTime::SPBGetDeviceInfo
     * @return a boolean that determines whether the channel is on or off.
     * @return {@link quicktime.sound.SoundException}
     */
    public boolean getLevelMeterOnOff() throws SoundException {
        short[] arrayOfShort = { 0, 0 };
        getDeviceInfo(1819108724, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    /**
     * Get the current level of the level meter.
     * QuickTime::SPBGetDeviceInfo
     * @return an integer that signals the current level of the channel.
     * @return {@link quicktime.sound.SoundException}
     */
    public int getLevelMeterLevel() throws SoundException {
        short[] arrayOfShort = { 0, 0 };
        getDeviceInfo(1819108724, arrayOfShort);
        return arrayOfShort[1];
    }

    /**
     * Set the state of the automatic gain control.
     * QuickTime::SPBGetDeviceInfo
     * @param flag set to true to turn automatic gain on, false to turn it off.
     * @param {@link quicktime.sound.SoundException}
     */
    public void setAutomaticGainControl(boolean paramBoolean) throws SoundException {
        short[] arrayOfShort = { (short) (paramBoolean ? 1 : 0) };
        setDeviceInfo(1634165536, arrayOfShort);
    }

    /**
     * Get the state of the automatic gain control.
     * QuickTime::SPBGetDeviceInfo
     * @return a boolean that determines whether automatic gain is on or off.
     * @return {@link quicktime.sound.SoundException}
     */
    public boolean getAutomaticGainControl() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1634165536, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    /**
     * Set the input gain level.
     * QuickTime::SPBGetDeviceInfo
     * @param gain a float (fixed value) to set the gain to.
     * @param {@link quicktime.sound.SoundException}
     */
    public void setInputGain(float paramFloat) throws SoundException {
        int[] arrayOfInt = { QTUtils.X2Fix(paramFloat) };
        setDeviceInfo(1734437230, arrayOfInt);
    }

    /**
     * Get the input gain level.
     * QuickTime::SPBGetDeviceInfo
     * @return a float (fixed) of the current level on the input gain.
     * @return {@link quicktime.sound.SoundException}
     */
    public float getInputGain() throws SoundException {
        int[] arrayOfInt = { 0 };
        getDeviceInfo(1734437230, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Get the input Source Index.
     * QuickTime::SPBGetDeviceInfo
     * @return an integr which is the index of the current sound input source
     * @return {@link quicktime.sound.SoundException}
     */
    public int getInputSource() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1936684402, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * sets the input Source to the passed in (index) source.
     * QuickTime::SPBGetDeviceInfo
     */
    public void setInputSource(int paramInt) throws SoundException {
        short[] arrayOfShort = { (short) paramInt };
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
     * QuickTime::SPBGetDeviceInfo
     * @return the maximum number of channels this device can record.
     * @return {@link quicktime.sound.SoundException}
     */
    public int getChannelAvailable() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1667785078, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Returns the number of channels that this device is to record.
     * QuickTime::SPBGetDeviceInfo
     * @return channels number of channels
     * @return {@link quicktime.sound.SoundException}
     */
    public int getNumberChannels() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1667785070, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Sets the number of channels that this device is to record.
     * QuickTime::SPBGetDeviceInfo
     * @param channels number of channels
     * @param {@link quicktime.sound.SoundException}
     */
    public void setNumberChannels(int paramInt) throws SoundException {
        short[] arrayOfShort = { (short) paramInt };
        setDeviceInfo(1667785070, arrayOfShort);
    }

    /**
     * Get the PlayThru level, 0 means it's off.
     * QuickTime::SPBGetDeviceInfo
     * @return the current PlayThru level.
     * @return {@link quicktime.sound.SoundException}
     */
    public int getPlayThruOnOff() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1886155880, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Set the PlayThru level, 0 means it's off.
     * QuickTime::SPBGetDeviceInfo
     */
    public void setPlayThruOnOff(int paramInt) throws SoundException {
        short[] arrayOfShort = { (short) paramInt };
        setDeviceInfo(1886155880, arrayOfShort);
    }

    /**
     * Set the sample rate to be produced by this device.
     * QuickTime::SPBGetDeviceInfo
     * @param rate the rate of the device
     * @param {@link quicktime.sound.SoundException}
     */
    public void setSampleRate(float paramFloat) throws SoundException {
        int[] arrayOfInt = { QTUtils.X2UFix(paramFloat) };
        setDeviceInfo(1936875892, arrayOfInt);
    }

    /**
     * Gets the sample rate produced by this device.
     * QuickTime::SPBGetDeviceInfo
     * @return the rate of the device
     * @return {@link quicktime.sound.SoundException}
     */
    public float getSampleRate() throws SoundException {
        int[] arrayOfInt = { 0 };
        getDeviceInfo(1936875892, arrayOfInt);
        return QTUtils.UFix2X(arrayOfInt[0]);
    }

    /**
     * Returns a float list containing the sample rates available.
     * QuickTime::SPBGetDeviceInfo
     * @return a float array containing the list of sample rates.
     * @return {@link quicktime.sound.SoundException}
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
     * QuickTime::SPBGetDeviceInfo
     * @param sampleSize is the number of bits in an individual sample.
     * @param {@link quicktime.sound.SoundException}
     */
    public void setSampleSize(int paramInt) throws SoundException {
        short[] arrayOfShort = { (short) paramInt };
        setDeviceInfo(1936943482, arrayOfShort);
    }

    /**
     * Returns the number of bits per sample.
     * QuickTime::SPBGetDeviceInfo
     * @return number of bits in a sample.
     * @return {@link quicktime.sound.SoundException}
     */
    public int getSampleSize() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1936943482, arrayOfShort);
        return arrayOfShort[0];
    }

    /**
     * Returns an integer list containing the sample sizes available.
     * QuickTime::SPBGetDeviceInfo
     * @return an integer array containing the list of samples.
     * @return {@link quicktime.sound.SoundException}
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
     * QuickTime::SPBGetDeviceInfo
     * @param leftChannel the gain for the left channel (range 0.5 to 1.5)
     * @param rightChannel the gain for the right channel (range 0.5 to 1.5)
     * @param {@link quicktime.sound.SoundException}
     */
    public void setStereoInputGain(float paramFloat1, float paramFloat2) throws SoundException {
        int[] arrayOfInt = { QTUtils.X2Fix(paramFloat1), QTUtils.X2Fix(paramFloat2) };
        setDeviceInfo(1936154985, arrayOfInt);
    }

    /**
     * Returns the current gain setting for the left channel of a stereo device
     * QuickTime::SPBGetDeviceInfo
     * @return the gain setting
     * @return {@link quicktime.sound.SoundException}
     */
    public float getStereoInputGainLeft() throws SoundException {
        int[] arrayOfInt = { 0, 0 };
        getDeviceInfo(1936154985, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Returns the current gain setting for the right channel of a stereo device
     * QuickTime::SPBGetDeviceInfo
     * @return the gain setting
     * @return {@link quicktime.sound.SoundException}
     */
    public float getStereoInputGainRight() throws SoundException {
        int[] arrayOfInt = { 0, 0 };
        getDeviceInfo(1936154985, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[1]);
    }

    /**
     * Get the state of continuous recording from this device
     * QuickTime::SPBGetDeviceInfo
     * @return the state of continuous recording (false is off, true is on)
     * @return {@link quicktime.sound.SoundException}
     */
    public boolean getContinuousRecording() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1668247156, arrayOfShort);
        return arrayOfShort[0] == 1;
    }

    /**
     * Set the state of continuous recording from this device.
     * Only sound input device drivers that support asynchronous recording support continuous recording.
     * QuickTime::SPBGetDeviceInfo
     * @param isContinuous the state of continuous recording (false is off, true is on)
     * @param {@link quicktime.sound.SoundException}
     */
    public void setContinuousRecording(boolean paramBoolean) throws SoundException {
        short[] arrayOfShort = { (short) (paramBoolean ? 1 : 0) };
        setDeviceInfo(1668247156, arrayOfShort);
    }

    /**
     * Set the compression type.
     * QuickTime::SPBGetDeviceInfo
     * @param compType the compressor type.
     * @param {@link quicktime.sound.SoundException}
     */
    public void setCompressionType(int paramInt) throws SoundException {
        int[] arrayOfInt = { paramInt };
        setDeviceInfo(1668246896, arrayOfInt);
    }

    /**
     * Returns the current compression type.
     * QuickTime::SPBGetDeviceInfo
     * @return the compression type
     * @return {@link quicktime.sound.SoundException}
     */
    public int getCompressionType() throws SoundException {
        int[] arrayOfInt = { 0 };
        getDeviceInfo(1668246896, arrayOfInt);
        return arrayOfInt[0];
    }

    /**
     * Returns an integer (OSTypes) list containing the available compression types.
     * QuickTime::SPBGetDeviceInfo
     * @return an integer array containing the list of compression types.
     * @return {@link quicktime.sound.SoundException}
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
     * QuickTime::SPBGetDeviceInfo
     * @return a boolean
     * @return {@link quicktime.sound.SoundException}
     */
    public boolean hasOptionsDialog() throws SoundException {
        short[] arrayOfShort = { 0 };
        getDeviceInfo(1869640804, arrayOfShort);
        return arrayOfShort[0] != 0;
    }

    /**
     * Shows the options dialog if available.
     * QuickTime::SPBGetDeviceInfo
     */
    public void showOptionsDialog() throws SoundException {
        int[] arrayOfInt = { 0 };
        setDeviceInfo(1869640804, arrayOfInt);
    }

    /**
     * Returns the name of this device.
     * QuickTime::SPBGetDeviceInfo
     * @return a String
     * @return {@link quicktime.sound.SoundException}
     */
    public String getName() throws SoundException {
        byte[] arrayOfByte = new byte[256];
        getDeviceInfo(1851878757, arrayOfByte);
        return QTUtils.PString2String(arrayOfByte, 0);
    }

    /**
     * Get the current signal level for each active channel.
     * @return an short[] of the current signal level
     * @return {@link quicktime.sound.SoundException}
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

