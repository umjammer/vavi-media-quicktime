
package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointer;


public final class SourcerTimingParams extends QTPointer {
    public static final int kNativeSize = 60;

    SourcerTimingParams() throws QTException {
        super(60, true);
    }

    SourcerTimingParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    public SourcerTimingParams(int paramInt1,
            int paramInt2,
            long paramLong1,
            long paramLong2,
            long paramLong3,
            long paramLong4,
            long paramLong5,
            long paramLong6) throws QTException {
        this(1, paramInt1, paramInt2, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5, paramLong6);
    }

    public SourcerTimingParams(int paramInt1,
            int paramInt2,
            int paramInt3,
            long paramLong1,
            long paramLong2,
            long paramLong3,
            long paramLong4,
            long paramLong5,
            long paramLong6) throws QTException {
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

    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getVersion() {
        return getIntAt(0);
    }

    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getFlags() {
        return getIntAt(4);
    }

    public void setTimeScale(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getTimeScale() {
        return getIntAt(8);
    }

    public void setPresentationStartTime(long paramLong) {
        setLongAt(12, paramLong);
    }

    public long getPresentationStartTime() {
        return getLongAt(12);
    }

    public void setPresentationEndTime(long paramLong) {
        setLongAt(20, paramLong);
    }

    public long getPresentationEndTime() {
        return getLongAt(20);
    }

    public void setPresentationCurrentTime(long paramLong) {
        setLongAt(28, paramLong);
    }

    public long getPresentationCurrentTime() {
        return getLongAt(28);
    }

    public void setLocalStartTime(long paramLong) {
        setLongAt(36, paramLong);
    }

    public long getLocalStartTime() {
        return getLongAt(36);
    }

    public void setLocalEndTime(long paramLong) {
        setLongAt(44, paramLong);
    }

    public long getLocalEndTime() {
        return getLongAt(44);
    }

    public void setLocalCurrentTime(long paramLong) {
        setLongAt(52, paramLong);
    }

    public long getLocalCurrentTime() {
        return getLongAt(52);
    }

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",timeScale=" + getTimeScale()
               + ",presentationStartTime" + getPresentationStartTime() + ",presentationEndTime " + getPresentationEndTime()
               + ",presentationCurrTime," + getPresentationCurrentTime() + ",localStartTime," + getLocalStartTime()
               + ",localEndtime," + getLocalEndTime() + "localCurrTime," + getLocalCurrentTime() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerTimingParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
