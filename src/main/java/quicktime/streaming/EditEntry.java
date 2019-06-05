
package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class EditEntry extends QTPointerRef {
    public static final int kNativeSize = 20;

    public EditEntry() throws QTException {
        super(20, true);
    }

    public EditEntry(long paramLong1, long paramLong2, float paramFloat) throws QTException {
        this();
        setPresentationDuration(paramLong1);
        setStreamStartTime(paramLong2);
        setStreamRate(paramFloat);
    }

    public long getPresentationDuration() {
        return getLongAt(0);
    }

    public long getStreamStartTime() {
        return getLongAt(8);
    }

    public float getStreamRate() {
        return QTUtils.Fix2X(getIntAt(16));
    }

    public void setPresentationDuration(long paramLong) {
        setLongAt(0, paramLong);
    }

    public void setStreamStartTime(long paramLong) {
        setLongAt(8, paramLong);
    }

    public void setStreamRate(float paramFloat) {
        setIntAt(16, QTUtils.X2Fix(paramFloat));
    }

    public String toString() {
        return getClass().getName() + "[duration=" + getPresentationDuration() + ",startTime=" + getStreamStartTime() + ",rate"
               + getStreamRate() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /EditEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
