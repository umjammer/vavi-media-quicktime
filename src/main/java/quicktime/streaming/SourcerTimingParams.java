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
    public SourcerTimingParams(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6) throws QTException {
        this(1, paramInt1, paramInt2, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5, paramLong6);
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
    public SourcerTimingParams(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6) throws QTException {
        super(60, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setTimeScale(paramInt3);
        setPresentationStartTime(paramLong1);
        setPresentationEndTime(paramLong2);
        setPresentationCurrentTime(paramLong3);
        setLocalStartTime(paramLong4);
        setLocalEndTime(paramLong5);
        setLocalCurrentTime(paramLong6);
    }

    /**
     * Sets the version of the sourcer timing params object
     * @param vers version
     */
    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
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
    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
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
    public void setTimeScale(int paramInt) {
        setIntAt(8, paramInt);
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
    public void setPresentationStartTime(long paramLong) {
        setLongAt(12, paramLong);
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
    public void setPresentationEndTime(long paramLong) {
        setLongAt(20, paramLong);
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
    public void setPresentationCurrentTime(long paramLong) {
        setLongAt(28, paramLong);
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
    public void setLocalStartTime(long paramLong) {
        setLongAt(36, paramLong);
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
    public void setLocalEndTime(long paramLong) {
        setLongAt(44, paramLong);
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
    public void setLocalCurrentTime(long paramLong) {
        setLongAt(52, paramLong);
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
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerTimingParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
