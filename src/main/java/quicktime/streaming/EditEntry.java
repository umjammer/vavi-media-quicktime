package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

/**
 * A class that represents an editable entry within a stream
 */
public final class EditEntry extends QTPointerRef {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    /**
     * Create an EditEntry object
     */
    public EditEntry() throws QTException {
        super(20, true);
    }

    /**
     * This constructor creates an EditEntry Object
     * @param duration the duration of the presentation
     * @param startTime the start time of the stream
     * @param rate the rate of the stream
     */
    public EditEntry(long duration, long startTime, float rate) throws QTException {
        this();
        setPresentationDuration(duration);
        setStreamStartTime(startTime);
        setStreamRate(rate);
    }

    /**
     * Retrieves the presentation duration of the entry.
     * @return duration of the presenter
     */
    public long getPresentationDuration() {
        return getLongAt(0);
    }

    /**
     * Retrieves the start time associated with this entries stream.
     * @return start time
     */
    public long getStreamStartTime() {
        return getLongAt(8);
    }

    /**
     * Retrieves the streaming rate associated with the entry.
     * @return stream rate
     */
    public float getStreamRate() {
        return QTUtils.Fix2X(getIntAt(16));
    }

    /**
     * Sets the presentation duration of the entry.
     * @param duration the presentation duration
     */
    public void setPresentationDuration(long duration) {
        setLongAt(0, duration);
    }

    /**
     * Sets the start time of the entry's stream.
     * @param startTime stream start time
     */
    public void setStreamStartTime(long startTime) {
        setLongAt(8, startTime);
    }

    /**
     * Sets the time scale of the Broadcaster params.
     */
    public void setStreamRate(float paramFloat) {
        setIntAt(16, QTUtils.X2Fix(paramFloat));
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[duration=" + getPresentationDuration() + ",startTime=" + getStreamStartTime() + ",rate" + getStreamRate() + "]";
    }
}
