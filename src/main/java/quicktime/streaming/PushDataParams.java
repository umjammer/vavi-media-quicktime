
package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.QTPointer;


public final class PushDataParams extends QTPointer {
    public static final int kNativeSize = 40;

    SampleDescription theSampleDescription = null;

    QTPointer theData = null;

    PushDataParams() throws QTException {
        super(40, true);
    }

    PushDataParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    public PushDataParams(int paramInt1,
            SampleDescription paramSampleDescription,
            int paramInt2,
            long paramLong1,
            long paramLong2,
            QTPointer paramQTPointer) throws QTException {
        super(40, true);
        setVersion(paramInt1);
        setSampleDescription(paramSampleDescription);
        setSampleDescSeed(paramInt2);
        setSampleTime(paramLong1);
        setDuration(paramLong2);
        setDataPtr(paramQTPointer);
    }

    public PushDataParams(int paramInt1,
            SampleDescription paramSampleDescription,
            int paramInt2,
            QTPointer paramQTPointer) throws QTException {
        this(paramInt1, paramSampleDescription, paramInt2, 0L, 0L, paramQTPointer);
        setFlags(0);
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

    public void setSampleDescription(SampleDescription paramSampleDescription) {
        this.theSampleDescription = paramSampleDescription;
        setIntAt(8, QTObject.ID(paramSampleDescription));
    }

    public SampleDescription getSampleDescription() {
        return this.theSampleDescription;
    }

    public void setSampleDescSeed(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getSampleDescSeed() {
        return getIntAt(12);
    }

    public void setSampleTime(long paramLong) {
        setLongAt(16, paramLong);
        setFlags(getFlags() & 0x1);
    }

    public long getSampleTime() {
        return getLongAt(16);
    }

    public void setDuration(long paramLong) {
        setLongAt(24, paramLong);
        setFlags(getFlags() & 0x2);
    }

    public long getDuration() {
        return getLongAt(24);
    }

    public void setDataPtr(QTPointer paramQTPointer) {
        this.theData = paramQTPointer;
        setIntAt(32, paramQTPointer.getSize());
        setIntAt(36, QTObject.ID(paramQTPointer));
    }

    public QTPointer getDataPtr() {
        return this.theData;
    }

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",sampleDescription="
               + getSampleDescription() + ",sampleDescriptionSeed=" + getSampleDescSeed() + ",sampleTime=" + getSampleTime()
               + ",duration=" + getDuration() + ",dataPtr=" + getDataPtr() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /PushDataParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
