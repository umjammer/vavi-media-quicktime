package quicktime.std.qtcomponents;

import quicktime.util.QTHandleRef;

/**
 * The TimeCodeInfo class represents time code as frame count or as HH:MM:SS:FF.
 * If frame count is used in construction, then TimeCodeTime time will be null.
 * If TimeCodeTime time is used in construction, then frame count will be 0.
 * It is an information class.
 */
public final class TimeCodeInfo {

    /**
     * Used to indicate the current frame number by the specified time and definition.
     */
    public int frameNumber;

    /**
     * Used to indicate the timecode formatting information.
     */
    public TimeCodeDef definition;

    /**
     * Represents time of the timecode as HH:MM:SS:FF.
     */
    public TimeCodeTime time;

    /**
     * Represents the total number of frames of the timecode.
     */
    public int counter;

    /**
     * Represents the timecode data.
     */
    public QTHandleRef userData;

    /**
     * Used when returning information from TimeCoder calls.
     */
    public TimeCodeInfo() {
        this.frameNumber = 0;
        this.definition = new TimeCodeDef();
        this.time = new TimeCodeTime();
        this.counter = 0;
        this.userData = null;
    }

    /**
     * Creates a TimeCodeDef object which has no initial settings.
     * The counter instance variable is 0.
     * @param frameNumber Current frame number.
     * @param definition Object with flags and other time code defining variables.
     * @param time TimeCode object using hours,minutes,seconds.
     * @param userData Handle to the data.
     */
    public TimeCodeInfo(int paramInt, TimeCodeDef paramTimeCodeDef, TimeCodeTime paramTimeCodeTime, QTHandleRef paramQTHandleRef) {
        this.frameNumber = paramInt;
        this.definition = paramTimeCodeDef;
        this.time = paramTimeCodeTime;
        this.counter = 0;
        this.userData = paramQTHandleRef;
    }

    /**
     * Creates a TimeCodeInfo object using frame count.
     * The time nstance variable is null.
     * @param frameNumber Current frame number.
     * @param definition Object with flags and other time code defining variables.
     * @param counter Number of frames
     * @param userData Handle to the data.
     */
    public TimeCodeInfo(int paramInt1, TimeCodeDef paramTimeCodeDef, int paramInt2, QTHandleRef paramQTHandleRef) {
        this.frameNumber = paramInt1;
        this.definition = paramTimeCodeDef;
        this.time = null;
        this.counter = paramInt2;
        this.userData = paramQTHandleRef;
    }

    /**
     * String representation of this class.
     */
    public String toString() {
        return getClass().getName() + "[" + "frameNumber=" + this.frameNumber + "definition=" + this.definition.toString() + "time=" + this.time.toString() + "counter=" + this.counter + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TimeCodeInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
