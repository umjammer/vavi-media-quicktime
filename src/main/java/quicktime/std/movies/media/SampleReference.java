
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.QTPointerRef;


public final class SampleReference extends QTPointerRef {
    public static final int kNativeSize = 18;

    public SampleReference() throws QTException {
        super(18, true);
    }

    public SampleReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws QTException {
        this();
        setDataOffset(paramInt1);
        setDataSize(paramInt2);
        setDurationPerSample(paramInt3);
        setNumberOfSamples(paramInt4);
        setFlags(paramInt5);
    }

    public int getDataOffset() {
        return getIntAt(0);
    }

    public void setDataOffset(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getDataSize() {
        return getIntAt(4);
    }

    public void setDataSize(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getDurationPerSample() {
        return getIntAt(8);
    }

    public void setDurationPerSample(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getNumberOfSamples() {
        return getIntAt(12);
    }

    public void setNumberOfSamples(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getFlags() {
        return getShortAt(16);
    }

    public void setFlags(int paramInt) {
        setShortAt(16, (short) paramInt);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SampleReference.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
