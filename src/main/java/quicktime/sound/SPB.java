package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QTNative;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointerRef;

/**
 * The SPB class is used in conjunction with a SPBDevice to record sound.
 * It contains fields that describe how much sound the device should record
 * and a buffer of memory where the recorded sound is recorded.
 */
public final class SPB extends QTByteObject implements quicktime.jdirect.QuickTimeLib, quicktime.jdirect.PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 38;

    private final SPBDevice device;

    private QTPointerRef buffer;

    private SoundRunner runner;

    private final quicktime.util.QTPointer realObject;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SPB.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Create a SPB.
     * @param device the SPBDevice that is to be used for recording the sound
     * @param count how many bytes of data to record
     * @param milliseconds On input, the number of milliseconds to record. If this field indicates a longer recording time than the count field, then the count field is ignored.
     * @param buffer A QTPointer to the buffer for the sampled-sound data. If this buffer length is too small to contain the amount of sampled-sound data specified in the count and milliseconds fields, then recording time is truncated so that the sampled-sound data fits in the buffer.
     */
    public SPB(SPBDevice device, int count, int milliseconds, QTPointerRef buffer) {
        super(38);
        try {
            this.realObject = new quicktime.util.QTPointer(38, true);
        } catch (quicktime.QTException localQTException) {
            throw new quicktime.QTRuntimeException(localQTException);
        }
        setIntAt(0, QTObject.ID(device));
        setIntInPointer(QTObject.ID(this.realObject), 0, QTObject.ID(device));
        setIntAt(12, buffer.getSize());
        setIntInPointer(QTObject.ID(this.realObject), 12, buffer.getSize());
        this.device = device;
        setCount(count);
        setMilliseconds(milliseconds);
        setBuffer(buffer);
    }

    public void setCompletionProc(SICompletion paramSICompletion) {
        if (this.runner != null)
            this.runner.cleanup();
        if (paramSICompletion == null) {
            setIntAt(20, 0);
            setIntInPointer(QTObject.ID(this.realObject), 20, 0);
            this.runner = null;
        } else {
            this.runner = new SoundRunner(this, paramSICompletion);
            int i = this.runner.getClosure();
            setIntAt(20, i);
            setIntInPointer(QTObject.ID(this.realObject), 20, i);
        }
    }

    public void removeCompletionProc() {
        setCompletionProc(null);
    }

    /**
     * Get the number of bytes that are or were recorded.
     * @return number of bytes
     */
    public int getCount() {
        return getIntFromPointer(QTObject.ID(this.realObject), 4);
    }

    /**
     * Sets the number of bytes that are to be recorded.
     * @param count number of bytes
     */
    public void setCount(int count) {
        setIntAt(4, count);
        setIntInPointer(QTObject.ID(this.realObject), 4, count);
    }

    /**
     * Get the number of bytes that are or were recorded.
     * @return number of bytes
     */
    public int getBufferLength() {
        return getIntFromPointer(QTObject.ID(this.realObject), 12);
    }

    /**
     * Get the the number of milliseconds that are or were recorded.
     * @return number of milliseconds
     */
    public int getMilliseconds() {
        return getIntFromPointer(QTObject.ID(this.realObject), 8);
    }

    /**
     * Sets the the number of milliseconds that are to be recorded.
     * @param msecs milliseconds
     */
    public void setMilliseconds(int msecs) {
        setIntAt(8, msecs);
        setIntInPointer(QTObject.ID(this.realObject), 8, msecs);
    }

    /**
     * Gets the device that is attached to this object.
     * @return the SPBDevice
     */
    public SPBDevice getDevice() {
        return this.device;
    }

    /**
     * Sets the buffer that the device will record into.
     * @param buffer the record buffer
     */
    public void setBuffer(QTPointerRef buffer) {
        setIntAt(16, QTObject.ID(buffer));
        setIntInPointer(QTObject.ID(this.realObject), 16, QTObject.ID(buffer));
        setIntAt(12, buffer.getSize());
        setIntInPointer(QTObject.ID(this.realObject), 12, buffer.getSize());
        this.buffer = buffer;
    }

    /**
     * Sets the buffer that the device will record into.
     */
    public QTPointerRef getBuffer() {
        return this.buffer;
    }

    /**
     * You can call this to check the value of the error code.
     * An error code > 0 means that an Async recording is still occuring.
     * An error code == 0 means no recording and no error.
     * An error code < 0 means that an error has occured and recording is terminated.
     */
    public short getError() {
        return getShortFromPointer(QTObject.ID(this.realObject), 32);
    }

    /**
     * Record audio data into memory, either synchronously or asynchronously.
     * The sound is recorded into the specified buffer. If it is recorded
     * synchronously then the error code will be checked and an exception thrown
     * if required. If recorded asynchronously then the application will need to
     * check for errors after the recording has completed with the checkError code.
     * After recording the buffer contains the recorded bytes, the count how many
     * bytes were recorded, the milliseconds how many milliseconds of sound were
     * recorded based on the current settings of the device.
     * @see "QuickTime::SPBRecord"
     * @param flag if true record asynchronously, if false records synchronously.
     * @throws quicktime.sound.SoundException
     */
    public void record(boolean flag) throws SoundException {
        int i = 0;
        byte b = (byte) (flag ? 1 : 0);
        setShortAt(32, (short) 0);
        setShortInPointer(QTObject.ID(this.realObject), 32, (short) 0);
        synchronized (quicktime.jdirect.QTNative.globalsLock) {
            i = SPBRecord(QTObject.ID(this.realObject), b);
        }
        SoundException.checkError(i);
    }

    /**
     * Pause recording from a sound input device.
     * @see "QuickTime::SPBPauseRecording"
     */
    public void pauseRecording() throws SoundException {
        SoundException.checkError(SPBPauseRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    /**
     * Pause recording from a sound input device.
     * @see "QuickTime::SPBResumeRecording"
     */
    public void resumeRecording() throws SoundException {
        SoundException.checkError(SPBResumeRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    /**
     * End a recording from a sound input device. If you call the checkError
     * instance method you will get an abortErr exception which indicates
     * that the recording was stopped by calling this method.
     * @see "QuickTime::SPBStopRecording"
     */
    public void stopRecording() throws SoundException {
        SoundException.checkError(SPBStopRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    /**
     * The status of the recording.
     * While the input device is recording, this call returns true.
     * When a recording terminates without an error, this call false.
     * When an error occurs during recording or the recording has been terminated
     * by a call to the SPBStopRecording function (abortErr), this call throws an exception.
     * @see "QuickTime::SPBGetRecordingStatus"
     */
    public boolean isRecording() throws SoundException {
        getRecordingStatus();
        if (this.recordingStatus[0] > 0)
            return true;
        if (this.recordingStatus[0] == 0)
            return false;
        throw new SoundException(this.recordingStatus[0]);
    }

    /**
     * The level of the meter.
     * While the input device is recording, this call returns the level.
     * @see "QuickTime::SPBGetRecordingStatus"
     * @return the current meter level
     * @throws quicktime.sound.SoundException
     */
    public short meterLevel() throws SoundException {
        getRecordingStatus();
        return this.meterLevelValue[0];
    }

    /**
     * The total number of samples to record.
     * @see "QuickTime::SPBGetRecordingStatus"
     * @return the total number of samples to record
     * @throws quicktime.sound.SoundException
     */
    public int totalSamplesToRecord() throws SoundException {
        getRecordingStatus();
        return this.totalSamplesToRecordVal[0];
    }

    /**
     * The total number of samples recorded.
     * @see "QuickTime::SPBGetRecordingStatus"
     * @return the total number of samples recorded
     * @throws quicktime.sound.SoundException
     */
    public int numberOfSamplesRecorded() throws SoundException {
        getRecordingStatus();
        return this.numberOfSamplesRecordedVal[0];
    }

    /**
     * The total number of msecs to record.
     * @see "QuickTime::SPBGetRecordingStatus"
     * @return the total number of msecs to record
     * @throws quicktime.sound.SoundException
     */
    public int totalMsecsToRecord() throws SoundException {
        getRecordingStatus();
        return this.totalMsecsToRecordVal[0];
    }

    /**
     * The total number of msecs recorded.
     * @see "QuickTime::SPBGetRecordingStatus"
     * @return the total number of msecs recorded
     * @throws quicktime.sound.SoundException
     */
    public int numberOfMsecsRecorded() throws SoundException {
        getRecordingStatus();
        return this.numberOfMsecsRecordedVal[0];
    }

    private final short[] recordingStatus = { 0 };

    private final short[] meterLevelValue = { 0 };

    private final int[] totalSamplesToRecordVal = { 0 };

    private final int[] numberOfSamplesRecordedVal = { 0 };

    private final int[] totalMsecsToRecordVal = { 0 };

    private final int[] numberOfMsecsRecordedVal = { 0 };

    private void getRecordingStatus() throws SoundException {
        int i = SPBGetRecordingStatus(getIntFromPointer(QTObject.ID(this.realObject), 0), this.recordingStatus, this.meterLevelValue, this.totalSamplesToRecordVal, this.numberOfSamplesRecordedVal, this.totalMsecsToRecordVal, this.numberOfMsecsRecordedVal);
        SoundException.checkError(i);
    }

    private static short getShortFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getShortFromPointer(paramInt1, paramInt2);
    }

    private static int getIntFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getIntFromPointer(paramInt1, paramInt2);
    }

    private static void setIntInPointer(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.setIntInPointer(paramInt1, paramInt2, paramInt3);
    }

    private static void setShortInPointer(int paramInt1, int paramInt2, short paramShort) {
        MemoryManager.setShortInPointer(paramInt1, paramInt2, paramShort);
    }

    private static short SPBRecord(int paramInt, byte paramByte) {
        return SoundManager.SPBRecord(paramInt, paramByte);
    }

    private static short SPBPauseRecording(int paramInt) {
        return SoundManager.SPBPauseRecording(paramInt);
    }

    private static short SPBResumeRecording(int paramInt) {
        return SoundManager.SPBResumeRecording(paramInt);
    }

    private static short SPBStopRecording(int paramInt) {
        return SoundManager.SPBStopRecording(paramInt);
    }

    private static short SPBGetRecordingStatus(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        return SoundManager.SPBGetRecordingStatus(paramInt, paramArrayOfShort1, paramArrayOfShort2, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4);
    }
}
