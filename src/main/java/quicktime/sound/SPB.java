package quicktime.sound;

import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointerRef;


/**
 * The SPB class is used in conjunction with a SPBDevice to record sound.
 * It contains fields that describe how much sound the device should record
 * and a buffer of memory where the recorded sound is recorded.
 */
public final class SPB extends QTByteObject implements quicktime.jdirect.QuickTimeLib, quicktime.jdirect.PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 38;

    private final SPBDevice device;

    private QTPointerRef buffer;

    private SoundRunner runner;

    private final quicktime.util.QTPointer realObject;

    /**
     * Create a SPB.
     *
     * @param device       the SPBDevice that is to be used for recording the sound
     * @param count        how many bytes of data to record
     * @param milliseconds On input, the number of milliseconds to record. If this field indicates a longer recording time than the count field, then the count field is ignored.
     * @param buffer       A QTPointer to the buffer for the sampled-sound data. If this buffer length is too small to contain the amount of sampled-sound data specified in the count and milliseconds fields, then recording time is truncated so that the sampled-sound data fits in the buffer.
     */
    public SPB(SPBDevice device, int count, int milliseconds, QTPointerRef buffer) {
        super(kNativeSize);
        try {
            this.realObject = new quicktime.util.QTPointer(kNativeSize, true);
        } catch (quicktime.QTException e) {
            throw new quicktime.QTRuntimeException(e);
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

    public void setCompletionProc(SICompletion completionProc) {
        if (this.runner != null)
            this.runner.cleanup();
        if (completionProc == null) {
            setIntAt(20, 0);
            setIntInPointer(QTObject.ID(this.realObject), 20, 0);
            this.runner = null;
        } else {
            this.runner = new SoundRunner(this, completionProc);
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
     *
     * @return number of bytes
     */
    public int getCount() {
        return getIntFromPointer(QTObject.ID(this.realObject), 4);
    }

    /**
     * Sets the number of bytes that are to be recorded.
     *
     * @param count number of bytes
     */
    public void setCount(int count) {
        setIntAt(4, count);
        setIntInPointer(QTObject.ID(this.realObject), 4, count);
    }

    /**
     * Get the number of bytes that are or were recorded.
     *
     * @return number of bytes
     */
    public int getBufferLength() {
        return getIntFromPointer(QTObject.ID(this.realObject), 12);
    }

    /**
     * Get the the number of milliseconds that are or were recorded.
     *
     * @return number of milliseconds
     */
    public int getMilliseconds() {
        return getIntFromPointer(QTObject.ID(this.realObject), 8);
    }

    /**
     * Sets the the number of milliseconds that are to be recorded.
     *
     * @param msecs milliseconds
     */
    public void setMilliseconds(int msecs) {
        setIntAt(8, msecs);
        setIntInPointer(QTObject.ID(this.realObject), 8, msecs);
    }

    /**
     * Gets the device that is attached to this object.
     *
     * @return the SPBDevice
     */
    public SPBDevice getDevice() {
        return this.device;
    }

    /**
     * Sets the buffer that the device will record into.
     *
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
     *
     * @param flag if true record asynchronously, if false records synchronously.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBRecord"
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
     *
     * @see "QuickTime::SPBPauseRecording"
     */
    public void pauseRecording() throws SoundException {
        SoundException.checkError(SPBPauseRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    /**
     * Pause recording from a sound input device.
     *
     * @see "QuickTime::SPBResumeRecording"
     */
    public void resumeRecording() throws SoundException {
        SoundException.checkError(SPBResumeRecording(getIntFromPointer(QTObject.ID(this.realObject), 0)));
    }

    /**
     * End a recording from a sound input device. If you call the checkError
     * instance method you will get an abortErr exception which indicates
     * that the recording was stopped by calling this method.
     *
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
     *
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
     *
     * @return the current meter level
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetRecordingStatus"
     */
    public short meterLevel() throws SoundException {
        getRecordingStatus();
        return this.meterLevelValue[0];
    }

    /**
     * The total number of samples to record.
     *
     * @return the total number of samples to record
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetRecordingStatus"
     */
    public int totalSamplesToRecord() throws SoundException {
        getRecordingStatus();
        return this.totalSamplesToRecordVal[0];
    }

    /**
     * The total number of samples recorded.
     *
     * @return the total number of samples recorded
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetRecordingStatus"
     */
    public int numberOfSamplesRecorded() throws SoundException {
        getRecordingStatus();
        return this.numberOfSamplesRecordedVal[0];
    }

    /**
     * The total number of msecs to record.
     *
     * @return the total number of msecs to record
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetRecordingStatus"
     */
    public int totalMsecsToRecord() throws SoundException {
        getRecordingStatus();
        return this.totalMsecsToRecordVal[0];
    }

    /**
     * The total number of msecs recorded.
     *
     * @return the total number of msecs recorded
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SPBGetRecordingStatus"
     */
    public int numberOfMsecsRecorded() throws SoundException {
        getRecordingStatus();
        return this.numberOfMsecsRecordedVal[0];
    }

    private final short[] recordingStatus = {0};

    private final short[] meterLevelValue = {0};

    private final int[] totalSamplesToRecordVal = {0};

    private final int[] numberOfSamplesRecordedVal = {0};

    private final int[] totalMsecsToRecordVal = {0};

    private final int[] numberOfMsecsRecordedVal = {0};

    private void getRecordingStatus() throws SoundException {
        int i = SPBGetRecordingStatus(getIntFromPointer(QTObject.ID(this.realObject), 0), this.recordingStatus, this.meterLevelValue, this.totalSamplesToRecordVal, this.numberOfSamplesRecordedVal, this.totalMsecsToRecordVal, this.numberOfMsecsRecordedVal);
        SoundException.checkError(i);
    }

    private static short getShortFromPointer(int ptr, int offset) {
        return MemoryManager.getShortFromPointer(ptr, offset);
    }

    private static int getIntFromPointer(int ptr, int offset) {
        return MemoryManager.getIntFromPointer(ptr, offset);
    }

    private static void setIntInPointer(int ptr, int offset, int value) {
        MemoryManager.setIntInPointer(ptr, offset, value);
    }

    private static void setShortInPointer(int ptr, int offset, short value) {
        MemoryManager.setShortInPointer(ptr, offset, value);
    }

    private static short SPBRecord(int spb, byte async) {
        return SoundManager.SPBRecord(spb, async);
    }

    private static short SPBPauseRecording(int inRefNum) {
        return SoundManager.SPBPauseRecording(inRefNum);
    }

    private static short SPBResumeRecording(int inRefNum) {
        return SoundManager.SPBResumeRecording(inRefNum);
    }

    private static short SPBStopRecording(int inRefNum) {
        return SoundManager.SPBStopRecording(inRefNum);
    }

    private static short SPBGetRecordingStatus(int inRefNum, short[] recordingStatus, short[] meterLevel, int[] totalSamplesToRecord, int[] numberOfSamplesRecorded, int[] totalMsecsToRecord, int[] numberOfMsecsRecorded) {
        return SoundManager.SPBGetRecordingStatus(inRefNum, recordingStatus, meterLevel, totalSamplesToRecord, numberOfSamplesRecorded, totalMsecsToRecord, numberOfMsecsRecorded);
    }
}
