
package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.util.QTPointerRef;


public final class PresIdleParams extends QTPointerRef {
    public static final int kNativeSize = 20;

    private Stream stream;

    public PresIdleParams() throws QTException {
        super(20, true);
    }

    public PresIdleParams(Stream paramStream, long paramLong, int paramInt) throws QTException {
        this();
        setStream(paramStream);
        setMovieTimeToDisplay(paramLong);
        setFlagsIn(paramInt);
    }

    public void setStream(Stream paramStream) throws QTException {
        setIntAt(0, QTObject.ID(paramStream));
        this.stream = paramStream;
    }

    public Stream getStream() throws QTException {
        return this.stream;
    }

    public void setMovieTimeToDisplay(long paramLong) throws QTException {
        setLongAt(4, paramLong);
    }

    public void setFlagsIn(int paramInt) throws QTException {
        setIntAt(12, paramInt);
    }

    public int getFlagsOut() throws QTException {
        return getIntAt(16);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /PresIdleParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
