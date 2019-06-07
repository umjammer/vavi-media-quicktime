package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.util.QTPointerRef;

/**
 * A class that represents an presentation idle parameters
 */
public final class PresIdleParams extends QTPointerRef {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    private Stream stream;

    /**
     * Create a PresIdleParams object
     */
    public PresIdleParams() throws QTException {
        super(20, true);
    }

    /**
     * This constructor creates an PresIdleParams Object
     * @param s the stream of the presentation
     */
    public PresIdleParams(Stream paramStream, long paramLong, int paramInt) throws QTException {
        this();
        setStream(paramStream);
        setMovieTimeToDisplay(paramLong);
        setFlagsIn(paramInt);
    }

    /**
     * This method sets the stream of the params object
     * @param s the stream of the params
     * @param {@link quicktime.QTException}
     */
    public void setStream(Stream paramStream) throws QTException {
        setIntAt(0, QTObject.ID(paramStream));
        this.stream = paramStream;
    }

    /**
     * @return the stream of the params
     * @return {@link quicktime.QTException}
     */
    public Stream getStream() throws QTException {
        return this.stream;
    }

    /**
     * This method sets the movie time to display of the params object
     * @param time the time of the params
     * @param {@link quicktime.QTException}
     */
    public void setMovieTimeToDisplay(long paramLong) throws QTException {
        setLongAt(4, paramLong);
    }

    /**
     * This method sets the input flags of the prarms object
     * @param flags input flags
     * @param {@link quicktime.QTException}
     */
    public void setFlagsIn(int paramInt) throws QTException {
        setIntAt(12, paramInt);
    }

    /**
     * This method gets the output flags of the params object
     * @return flags
     * @return {@link quicktime.QTException}
     */
    public int getFlagsOut() throws QTException {
        return getIntAt(16);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /PresIdleParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
