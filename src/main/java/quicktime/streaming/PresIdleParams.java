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
     *
     * @param s the stream of the presentation
     */
    public PresIdleParams(Stream s, long paramLong, int paramInt) throws QTException {
        this();
        setStream(s);
        setMovieTimeToDisplay(paramLong);
        setFlagsIn(paramInt);
    }

    /**
     * This method sets the stream of the params object
     *
     * @param s the stream of the params
     * @throws quicktime.QTException
     */
    public void setStream(Stream s) throws QTException {
        setIntAt(0, QTObject.ID(stream));
        this.stream = stream;
    }

    /**
     * @return the stream of the params
     * @throws quicktime.QTException
     */
    public Stream getStream() throws QTException {
        return this.stream;
    }

    /**
     * This method sets the movie time to display of the params object
     *
     * @param time the time of the params
     * @throws quicktime.QTException
     */
    public void setMovieTimeToDisplay(long time) throws QTException {
        setLongAt(4, time);
    }

    /**
     * This method sets the input flags of the prarms object
     *
     * @param flags input flags
     * @throws quicktime.QTException
     */
    public void setFlagsIn(int flags) throws QTException {
        setIntAt(12, flags);
    }

    /**
     * This method gets the output flags of the params object
     *
     * @return flags
     * @throws quicktime.QTException
     */
    public int getFlagsOut() throws QTException {
        return getIntAt(16);
    }
}
