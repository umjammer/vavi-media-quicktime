package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointer;

/**
 * This class provides a representation the native QTSSourcerTimingParams structure
 * that provides sourcer timing services for the Sourcer
 */
public final class SourcerTimingParams extends QTPointer {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 60;

    SourcerTimingParams() throws QTException {
        super(60, true);
    }

    SourcerTimingParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    /**
     * Constructs an SourcerTimingParams version 1 object based on the native QTSSourcerTimingParams structure
     * @param flags flags of the sourcer timing params
     * @param timeScale time scale of the sourcer
     * @param presStartTime presentation start time
     * @param presEndTime presentation end time
     * @param presCurrTime presentation current time
     * @param localStartTime local start time
     * @param localEndTime local end time
     * @param localCurrTime local current time
     */
    public SourcerTimingParams(int flags, int timeScale, long presStartTime, long presEndTime, long presCurrTime, long localStartTime, long localEndTime, long localCurrTime) throws QTException {
        this(1, flags, timeScale, presStartTime, presEndTime, presCurrTime, localStartTime, localEndTime, localCurrTime);
    }

    /**
     * Constructs an SourcerTimingParams object based on the native QTSSourcerTimingParams structure
     * @param vers version of the params structure
     * @param flags flags of the sourcer timing params
     * @param timeScale time scale of the sourcer
     * @param presStartTime presentation start time
     * @param presEndTime presentation end time
     * @param presCurrTime presentation current time
     * @param localStartTime local start time
     * @param localEndTime local end time
     * @param localCurrTime local current time
     */
    public SourcerTimingParams(int vers, int flags, int timeScale, long presStartTime, long presEndTime, long presCurrTime, long localStartTime, long localEndTime, long localCurrTime) throws QTException {
        super(60, true);
        setVersion(vers);
        setFlags(flags);
        setTimeScale(timeScale);
        setPresentationStartTime(presStartTime);
        setPresentationEndTime(presEndTime);
        setPresentationCurrentTime(presCurrTime);
        setLocalStartTime(localStartTime);
        setLocalEndTime(localEndTime);
        setLocalCurrentTime(localCurrTime);
    }

    /**
     * Sets the version of the sourcer timing params object
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
    }

    /**
     * Gets the version of the sourcer timing params object
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the flags of the sourcer timing object
     * @param flags sourcer timing flags
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
     * Sets the time scale of the sourcer timing object
     * @param timeScale time scale
     */
    public void setTimeScale(int timeScale) {
        setIntAt(8, timeScale);
    }

    /**
     * Gets the time scale
     */
    public int getTimeScale() {
        return getIntAt(8);
    }

    /**
     * Sets the presentation start time
     * @param startTime presentation start time
     */
    public void setPresentationStartTime(long startTime) {
        setLongAt(12, startTime);
    }

    /**
     * Gets the presentation start time
     */
    public long getPresentationStartTime() {
        return getLongAt(12);
    }

    /**
     * Sets the presentation end time
     * @param endTime presentation end time
     */
    public void setPresentationEndTime(long endTime) {
        setLongAt(20, endTime);
    }

    /**
     * Gets the presentation end time
     */
    public long getPresentationEndTime() {
        return getLongAt(20);
    }

    /**
     * Sets the presentation current time
     * @param currTime presentation current time
     */
    public void setPresentationCurrentTime(long currTime) {
        setLongAt(28, currTime);
    }

    /**
     * Gets the presentation current time
     */
    public long getPresentationCurrentTime() {
        return getLongAt(28);
    }

    /**
     * Sets the local start time
     * @param localStartTime local start time
     */
    public void setLocalStartTime(long localStartTime) {
        setLongAt(36, localStartTime);
    }

    /**
     * Gets the local start time
     */
    public long getLocalStartTime() {
        return getLongAt(36);
    }

    /**
     * Sets the local end time
     * @param localEndTime local end time
     */
    public void setLocalEndTime(long localEndTime) {
        setLongAt(44, localEndTime);
    }

    /**
     * Gets the local end time
     */
    public long getLocalEndTime() {
        return getLongAt(44);
    }

    /**
     * Sets the local current time
     * @param localCurrTime local current time
     */
    public void setLocalCurrentTime(long localCurrTime) {
        setLongAt(52, localCurrTime);
    }

    /**
     * Gets the local end time
     */
    public long getLocalCurrentTime() {
        return getLongAt(52);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",timeScale=" + getTimeScale() + ",presentationStartTime" + getPresentationStartTime() + ",presentationEndTime " + getPresentationEndTime() + ",presentationCurrTime," + getPresentationCurrentTime() + ",localStartTime," + getLocalStartTime() + ",localEndtime," + getLocalEndTime() + "localCurrTime," + getLocalCurrentTime() + "]";
    }
}
