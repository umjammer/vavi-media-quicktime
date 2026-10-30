package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.QTPointer;


/**
 * This class provides a representation the native QTSPushDataParams structure
 * that provides settings for pushing data from the Sourcer
 * <p>
 * When configuring the Sourcer for use with Push Data, you may send arbitrary
 * data. You need to provide a sample descripion and a data pointer. The time
 * stamp is optional. If you do not provide this time stamp, the current time
 * will be used.
 */
public final class PushDataParams extends QTPointer {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 40;

    SampleDescription theSampleDescription = null;

    QTPointer theData = null;

    PushDataParams() throws QTException {
        super(40, true);
    }

    PushDataParams(byte[] byteArray) throws QTException {
        super(byteArray);
    }

    /**
     * Constructs an PushDataParams object based on the native QTSPushDataParams structure
     *
     * @param vers version of the params structure
     */
    public PushDataParams(int vers, SampleDescription sampleDesc, int seed, long time, long duration, QTPointer data) throws QTException {
        super(40, true);
        setVersion(vers);
        setSampleDescription(sampleDesc);
        setSampleDescSeed(seed);
        setSampleTime(time);
        setDuration(duration);
        setDataPtr(data);
    }

    /**
     * Constructs an PushDataParams object based on the native QTSPushDataParams structure
     *
     * @param vers version of the params structure
     */
    public PushDataParams(int vers, SampleDescription sampleDesc, int seed, QTPointer data) throws QTException {
        this(vers, sampleDesc, seed, 0L, 0L, data);
        setFlags(0);
    }

    /**
     * Sets the version of the push data params object
     *
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
    }

    /**
     * Gets the version of the push data params object
     *
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the flags of the push data object
     *
     * @param flags sourcer callback flags
     */
    public void setFlags(int flags) {
        setIntAt(4, flags);
    }

    /**
     * Gets the flags
     */
    public int getFlags() {
        return getIntAt(4);
    }

    /**
     * Set the sample description of the push data
     *
     * @param sampleDesc sample description
     */
    public void setSampleDescription(SampleDescription sampleDesc) {
        this.theSampleDescription = sampleDesc;
        setIntAt(8, QTObject.ID(sampleDesc));
    }

    /**
     * Get the sample description of the push data
     *
     * @return sample description
     */
    public SampleDescription getSampleDescription() {
        return this.theSampleDescription;
    }

    /**
     * Set sample description seed of the push data
     *
     * @param seed sample description seed
     */
    public void setSampleDescSeed(int seed) {
        setIntAt(12, seed);
    }

    /**
     * Get sample description seed of the push data
     *
     * @return sample description seed
     */
    public int getSampleDescSeed() {
        return getIntAt(12);
    }

    /**
     * Set sample time of the push data
     *
     * @param time sample time
     *             NOTE: This function automatically adds kQTSPushDataFlag_SampleTimeIsValid to the flags
     */
    public void setSampleTime(long time) {
        setLongAt(16, time);
        setFlags(getFlags() & 0x1);
    }

    /**
     * Get sample time of the push data
     *
     * @return sample time
     */
    public long getSampleTime() {
        return getLongAt(16);
    }

    /**
     * Set duration of the push data
     *
     * @param duration duration
     *                 NOTE: This function automatically adds kQTSPushDataFlag_DurationIsValid to the flags
     */
    public void setDuration(long duration) {
        setLongAt(24, duration);
        setFlags(getFlags() & 0x2);
    }

    /**
     * Get duration of the push data
     *
     * @return duration
     */
    public long getDuration() {
        return getLongAt(24);
    }

    /**
     * Sets the push data
     *
     * @param data push data
     */
    public void setDataPtr(QTPointer data) {
        this.theData = data;
        setIntAt(32, data.getSize());
        setIntAt(36, QTObject.ID(data));
    }

    /**
     * Gets the push data
     *
     * @return data
     */
    public QTPointer getDataPtr() {
        return this.theData;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a String
     */
    public String toString() {
        return getClass().getName() +
                "[version=" + getVersion() +
                ",flags=" + getFlags() +
                ",sampleDescription=" + getSampleDescription() +
                ",sampleDescriptionSeed=" + getSampleDescSeed() +
                ",sampleTime=" + getSampleTime() +
                ",duration=" + getDuration() +
                ",dataPtr=" + getDataPtr() +
                "]";
    }
}
