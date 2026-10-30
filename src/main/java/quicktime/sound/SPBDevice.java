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
        byte[] deviceName = new byte[256];
        int[] deviceIconHandle = {0};
        int i = SPBGetIndexedDevice((short) count, deviceName, deviceIconHandle);
        if (i == 65315)
            return null;
        DisposeHandle(deviceIconHandle[0]);
        SoundException.checkError(i);
        return QTUtils.PString2String(deviceName, 0);
    }

    /**
     * Get the Sound Input Driver
     *
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetSoundInputDriver"
     * @see quicktime.std.sg.SGSoundChannel#getInputDriver()
     */
    public static SPBDevice fromSoundChannel(SGSoundChannel ref) throws StdQTException {
        int i = SGGetSoundInputDriver(QTObject.ID(ref));
        return new SPBDevice(i, ref);
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

    private static int allocate(String str, int permission) throws SoundException {
        int[] inRefNum = {0};
        if ((permission == 1) && (QTSession.hasSecurityRestrictions())) {
            throw new SecurityException("Only able to record sound with security settings when class is signed");
        }
        SoundException.checkError(SPBOpenDevice(str == null ? null : QTUtils.String2PString(str, 255), (short) permission, inRefNum));
        return inRefNum[0];
    }

    private SPBDevice(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    private void getDeviceInfo(int selector, byte[] info) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), selector, info));
    }

    private void getDeviceInfo(int selector, int info) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), selector, info));
    }

    private void getDeviceInfo(int selector, short[] info) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), selector, info));
    }

    private void getDeviceInfo(int selector, int[] info) throws SoundException {
        SoundException.checkError(SPBGetDeviceInfo(_ID(), selector, info));
    }

    private void setDeviceInfo(int selector, short[] info) throws SoundException {
        SoundException.checkError(SPBSetDeviceInfo(_ID(), selector, info));
    }

    private void setDeviceInfo(int selector, int[] info) throws SoundException {
        SoundException.checkError(SPBSetDeviceInfo(_ID(), selector, info));
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
        int[] value = {msecs};
        SoundException.checkError(SPBMillisecondsToBytes(_ID(), value));
        return value[0];
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
        int[] value = {bytes};
        SoundException.checkError(SPBBytesToMilliseconds(_ID(), value));
        return value[0];
    }

    /**
     * Set the current state of the level meter.
     *
     * @param flag set to true to turn the channel on, false to turn it off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setLevelMeterOnOff(boolean flag) throws SoundException {
        short[] info = {(short) (flag ? 1 : 0)};
        setDeviceInfo(1819108724, info);
    }

    /**
     * Get the current state of the level meter.
     *
     * @return a boolean that determines whether the channel is on or off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean getLevelMeterOnOff() throws SoundException {
        short[] info = {0, 0};
        getDeviceInfo(1819108724, info);
        return info[0] != 0;
    }

    /**
     * Get the current level of the level meter.
     *
     * @return an integer that signals the current level of the channel.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getLevelMeterLevel() throws SoundException {
        short[] info = {0, 0};
        getDeviceInfo(1819108724, info);
        return info[1];
    }

    /**
     * Set the state of the automatic gain control.
     *
     * @param flag set to true to turn automatic gain on, false to turn it off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setAutomaticGainControl(boolean flag) throws SoundException {
        short[] info = {(short) (flag ? 1 : 0)};
        setDeviceInfo(1634165536, info);
    }

    /**
     * Get the state of the automatic gain control.
     *
     * @return a boolean that determines whether automatic gain is on or off.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean getAutomaticGainControl() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1634165536, info);
        return info[0] != 0;
    }

    /**
     * Set the input gain level.
     *
     * @param gain a float (fixed value) to set the gain to.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setInputGain(float gain) throws SoundException {
        int[] info = {QTUtils.X2Fix(gain)};
        setDeviceInfo(1734437230, info);
    }

    /**
     * Get the input gain level.
     *
     * @return a float (fixed) of the current level on the input gain.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getInputGain() throws SoundException {
        int[] info = {0};
        getDeviceInfo(1734437230, info);
        return QTUtils.Fix2X(info[0]);
    }

    /**
     * Get the input Source Index.
     *
     * @return an integr which is the index of the current sound input source
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getInputSource() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1936684402, info);
        return info[0];
    }

    /**
     * sets the input Source to the passed in (index) source.
     *
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setInputSource(int inputSource) throws SoundException {
        short[] info = {(short) inputSource};
        setDeviceInfo(1936684402, info);
    }

    /**
     * Get a list of the names of all the sound input sources
     * supported by the sound input device
     */
    public String[] getInputSourceNames() throws SoundException {
        byte[] info = new byte[4];
        getDeviceInfo(1936613741, info);
        int j = getIntFromArray(info, 0);
        int i = getShortFromHandle(j, 0);
        String[] inputSourceNames = new String[i];
        int k = 0;
        for (int m = 0; m < i; m++) {
            int n = getByteFromHandle(j, k + 2);
            byte[] pString = new byte[n + 1];
            copyPointerToArray(getIntFromPointer(j, 0), k + 2, pString, 0, n + 1);
            inputSourceNames[m] = QTUtils.PString2String(pString, 0);
            k = k + n + 1;
        }
        DisposeHandle(j);
        return inputSourceNames;
    }

    /**
     * Get the maximum number of channels this device can record.
     *
     * @return the maximum number of channels this device can record.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getChannelAvailable() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1667785078, info);
        return info[0];
    }

    /**
     * Returns the number of channels that this device is to record.
     *
     * @return channels number of channels
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getNumberChannels() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1667785070, info);
        return info[0];
    }

    /**
     * Sets the number of channels that this device is to record.
     *
     * @param channels number of channels
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setNumberChannels(int channels) throws SoundException {
        short[] info = {(short) channels};
        setDeviceInfo(1667785070, info);
    }

    /**
     * Get the PlayThru level, 0 means it's off.
     *
     * @return the current PlayThru level.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getPlayThruOnOff() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1886155880, info);
        return info[0];
    }

    /**
     * Set the PlayThru level, 0 means it's off.
     *
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setPlayThruOnOff(int playThruOnOff) throws SoundException {
        short[] info = {(short) playThruOnOff};
        setDeviceInfo(1886155880, info);
    }

    /**
     * Set the sample rate to be produced by this device.
     *
     * @param rate the rate of the device
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setSampleRate(float rate) throws SoundException {
        int[] info = {QTUtils.X2UFix(rate)};
        setDeviceInfo(1936875892, info);
    }

    /**
     * Gets the sample rate produced by this device.
     *
     * @return the rate of the device
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getSampleRate() throws SoundException {
        int[] info = {0};
        getDeviceInfo(1936875892, info);
        return QTUtils.UFix2X(info[0]);
    }

    /**
     * Returns a float list containing the sample rates available.
     *
     * @return a float array containing the list of sample rates.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float[] getSampleRateAvailable() throws SoundException {
        byte[] info = new byte[6];
        getDeviceInfo(1936941430, info);
        int i = getShortFromArray(info, 0);
        float[] sampleRateAvailable = null;
        if (i == 0) {
            sampleRateAvailable = new float[2];
        } else {
            sampleRateAvailable = new float[i];
        }
        for (int j = 0; j < sampleRateAvailable.length; j++) {
            int k = getIntFromHandle(getIntFromArray(info, 2), j * 4);
            sampleRateAvailable[j] = QTUtils.UFix2X(k);
        }
        DisposeHandle(getIntFromArray(info, 2));
        return sampleRateAvailable;
    }

    /**
     * Set the number of bits per sample.
     *
     * @param sampleSize is the number of bits in an individual sample.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setSampleSize(int sampleSize) throws SoundException {
        short[] info = {(short) sampleSize};
        setDeviceInfo(1936943482, info);
    }

    /**
     * Returns the number of bits per sample.
     *
     * @return number of bits in a sample.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getSampleSize() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1936943482, info);
        return info[0];
    }

    /**
     * Returns an integer list containing the sample sizes available.
     *
     * @return an integer array containing the list of samples.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int[] getSampleSizeAvailable() throws SoundException {
        byte[] info = new byte[6];
        getDeviceInfo(1936941430, info);
        int i = getShortFromArray(info, 0);
        int[] sampleSizeAvailable = new int[i];
        for (int j = 0; j < i; j++) sampleSizeAvailable[j] = getShortFromHandle(getIntFromArray(info, 2), j * 2);
        DisposeHandle(getIntFromArray(info, 2));
        return sampleSizeAvailable;
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
        int[] info = {QTUtils.X2Fix(leftChannel), QTUtils.X2Fix(rightChannel)};
        setDeviceInfo(1936154985, info);
    }

    /**
     * Returns the current gain setting for the left channel of a stereo device
     *
     * @return the gain setting
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getStereoInputGainLeft() throws SoundException {
        int[] info = {0, 0};
        getDeviceInfo(1936154985, info);
        return QTUtils.Fix2X(info[0]);
    }

    /**
     * Returns the current gain setting for the right channel of a stereo device
     *
     * @return the gain setting
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public float getStereoInputGainRight() throws SoundException {
        int[] info = {0, 0};
        getDeviceInfo(1936154985, info);
        return QTUtils.Fix2X(info[1]);
    }

    /**
     * Get the state of continuous recording from this device
     *
     * @return the state of continuous recording (false is off, true is on)
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean getContinuousRecording() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1668247156, info);
        return info[0] == 1;
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
        short[] info = {(short) (isContinuous ? 1 : 0)};
        setDeviceInfo(1668247156, info);
    }

    /**
     * Set the compression type.
     *
     * @param compType the compressor type.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void setCompressionType(int compType) throws SoundException {
        int[] info = {compType};
        setDeviceInfo(1668246896, info);
    }

    /**
     * Returns the current compression type.
     *
     * @return the compression type
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int getCompressionType() throws SoundException {
        int[] info = {0};
        getDeviceInfo(1668246896, info);
        return info[0];
    }

    /**
     * Returns an integer (OSTypes) list containing the available compression types.
     *
     * @return an integer array containing the list of compression types.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public int[] getCompressionAvailable() throws SoundException {
        byte[] info = new byte[6];
        getDeviceInfo(1668112758, info);
        int i = getShortFromArray(info, 0);
        int[] compressionAvailable = new int[i];
        for (int j = 0; j < i; j++) compressionAvailable[j] = getIntFromHandle(getIntFromArray(info, 2), j * 4);
        DisposeHandle(getIntFromArray(info, 2));
        return compressionAvailable;
    }

    /**
     * Returns true if the current device supports an options dialog box and false if not.
     *
     * @return a boolean
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public boolean hasOptionsDialog() throws SoundException {
        short[] info = {0};
        getDeviceInfo(1869640804, info);
        return info[0] != 0;
    }

    /**
     * Shows the options dialog if available.
     *
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public void showOptionsDialog() throws SoundException {
        int[] info = {0};
        setDeviceInfo(1869640804, info);
    }

    /**
     * Returns the name of this device.
     *
     * @return a String
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetDeviceInfo"
     */
    public String getName() throws SoundException {
        byte[] info = new byte[256];
        getDeviceInfo(1851878757, info);
        return QTUtils.PString2String(info, 0);
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
        int[] activeLevels = new int[i];
        for (int k = 0; k < i; k++) activeLevels[k] = getShortFromPointer(j, k * 2);
        DisposePtr(j);
        return activeLevels;
    }

    /**
     * A String representation of the class.
     *
     * @return a String representation of this object.
     */
    public String toString() {
        try {
            return getClass().getName() + "[name=" + getName() + "]";
        } catch (QTException e) {
        }
        return super.toString();
    }

    private static int SGGetSoundInputDriver(int ref) {
        return SequenceGrabberManager.SGGetSoundInputDriver(ref);
    }

    private static short SPBOpenDevice(byte[] deviceName, short permission, int[] inRefNum) {
        return SoundManager.SPBOpenDevice(deviceName, permission, inRefNum);
    }

    private static short SPBGetDeviceInfo(int inRefNum, int selector, int[] info) {
        return SoundManager.SPBGetDeviceInfo(inRefNum, selector, info);
    }

    private static short SPBSetDeviceInfo(int inRefNum, int selector, int[] info) {
        return SoundManager.SPBSetDeviceInfo(inRefNum, selector, info);
    }

    private static short SPBGetDeviceInfo(int inRefNum, int selector, short[] info) {
        return SoundManager.SPBGetDeviceInfo(inRefNum, selector, info);
    }

    private static short SPBSetDeviceInfo(int inRefNum, int selector, short[] info) {
        return SoundManager.SPBSetDeviceInfo(inRefNum, selector, info);
    }

    private static short SPBGetDeviceInfo(int inRefNum, int selector, byte[] info) {
        return SoundManager.SPBGetDeviceInfo(inRefNum, selector, info);
    }

    private static short SPBGetDeviceInfo(int inRefNum, int selector, int info) {
        return SoundManager.SPBGetDeviceInfo(inRefNum, selector, info);
    }

    private static short SPBMillisecondsToBytes(int inRefNum, int[] value) {
        return SoundManager.SPBMillisecondsToBytes(inRefNum, value);
    }

    private static short SPBBytesToMilliseconds(int inRefNum, int[] value) {
        return SoundManager.SPBBytesToMilliseconds(inRefNum, value);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static byte getByteFromHandle(int h, int offset) {
        return MemoryManager.getByteFromHandle(h, offset);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static short getShortFromPointer(int ptr, int offset) {
        return MemoryManager.getShortFromPointer(ptr, offset);
    }

    private static int getIntFromHandle(int h, int offset) {
        return MemoryManager.getIntFromHandle(h, offset);
    }

    private static void DisposeHandle(int h) {
        MemoryManager.DisposeHandle(h);
    }

    private static short getShortFromHandle(int h, int offset) {
        return MemoryManager.getShortFromHandle(h, offset);
    }

    private static short SPBGetIndexedDevice(short count, byte[] deviceName, int[] deviceIconHandle) {
        return SoundManager.SPBGetIndexedDevice(count, deviceName, deviceIconHandle);
    }

    private static int NewPtr(int size) {
        return MemoryManager.NewPtr(size);
    }

    private static void DisposePtr(int p) {
        MemoryManager.DisposePtr(p);
    }
}
