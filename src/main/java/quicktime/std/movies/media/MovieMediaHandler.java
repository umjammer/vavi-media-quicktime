package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.image.Matrix;
import quicktime.util.QTHandle;


public class MovieMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    MovieMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * This method retrieves the duration of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the duration of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getCurrentMoviePropertyDuration() throws StdQTException {
        int[] value = new int[1];
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1685418593, value);
        StdQTException.checkError(i);
        return value[0];
    }

    /**
     * This method gets the time scale of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the time scale of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getCurrentMoviePropertyTimeScale() throws StdQTException {
        int[] value = new int[1];
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953066355, value);
        StdQTException.checkError(i);
        return value[0];
    }

    /**
     * This method gets the time record associated with the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the time of the movie as a TimeRecord
     * @throws quicktime.QTException
     */
    public TimeRecord getCurrentMoviePropertyTime() throws QTException {
        TimeRecord value = new TimeRecord();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953066358, QTObject.ID(value));
        StdQTException.checkError(i);
        return value;
    }

    /**
     * This method gets the natural bounds of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the natural bounds of the movie
     * @throws quicktime.std.StdQTException
     */
    public QDRect getCurrentMoviePropertyNaturalBounds() throws StdQTException {
        QDRect currentMoviePropertyNaturalBounds = new QDRect();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1851880546, currentMoviePropertyNaturalBounds.getRect());
        StdQTException.checkError(i);
        return currentMoviePropertyNaturalBounds;
    }

    /**
     * This method gets the matrix of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the matrix of the movie
     * @throws quicktime.std.StdQTException
     */
    public Matrix getCurrentMoviePropertyMatrix() throws StdQTException {
        Matrix currentMoviePropertyMatrix = new Matrix();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1836348024, currentMoviePropertyMatrix.getBytes());
        StdQTException.checkError(i);
        return currentMoviePropertyMatrix;
    }

    /**
     * This method returns an array of all the track IDs associated with the currently
     * instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return an array of track IDs of the movie
     * @throws quicktime.std.StdQTException
     */
    public int[] getCurrentMoviePropertyTrackList() throws StdQTException {
        int[] value = { 0 };
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953264500, value);
        StdQTException.checkError(i);
        int j = value[0];
        return value;
    }

    /**
     * This method gets the media type of the currently instantiated embedded movie
     * @see "QuickTime::MovieGetCurrentTrackProperty()"
     * @param trackID the ID of the track to return the type of
     * @return the media type of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getCurrentTrackPropertyMediaType(int trackID) throws StdQTException {
        int[] value = { 0 };
        int i = MovieMediaGetCurrentTrackProperty(_ID(), 1836349808, trackID, value);
        StdQTException.checkError(i);
        return value[0];
    }

    /**
     * This method gets the data reference of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetChildMovieDataReference()"
     * @param dataRefID the ID of the data reference to retrieve
     * @param dataRefIndex the index of the data reference to retrieve
     * @return the DataRef of the movie
     * @throws quicktime.QTException
     */
    public DataRef getChildMovieDataReference(int dataRefID, int dataRefIndex) throws QTException {
        int i = 0;
        byte[] dataRef = { 0 };
        int j = MovieMediaGetChildMovieDataReference(_ID(), dataRefID, dataRefIndex, i, dataRef, dataRefID, dataRefIndex);
        StdQTException.checkError(j);
        return new DataRef(new QTHandle(dataRef));
    }

    /**
     * This method sets the data reference of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaSetChildMovieDataReference()"
     * @param dataRefID the ID of the data ref to set
     * @param dataRefType the type of the data ref to set
     * @param dataRef the data ref to set
     * @throws quicktime.std.StdQTException
     */
    public void setChildMovieDataReference(int dataRefID, int dataRefType, DataRef dataRef) throws StdQTException {
        int i = MovieMediaSetChildMovieDataReference(_ID(), dataRefID, dataRefType, dataRef.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method loads the movie associated with a specific data reference
     * @see "QuickTime::MovieMediaLoadChildMovieFromDataReference()"
     * @param dataRefID the dataReference ID to be used
     * @throws quicktime.std.StdQTException
     */
    public void loadChildMovieFromDataReference(int dataRefID) throws StdQTException {
        int i = MovieMediaLoadChildMovieFromDataReference(_ID(), dataRefID);
        StdQTException.checkError(i);
    }

    private static int MovieMediaGetCurrentMovieProperty(int mh, int whichProperty, int[] value) {
        return MediaHandlerManager.MovieMediaGetCurrentMovieProperty(mh, whichProperty, value);
    }

    private static int MovieMediaGetCurrentMovieProperty(int mh, int whichProperty, byte[] value) {
        return MediaHandlerManager.MovieMediaGetCurrentMovieProperty(mh, whichProperty, value);
    }

    private static int MovieMediaGetCurrentMovieProperty(int mh, int whichProperty, int value) {
        return MediaHandlerManager.MovieMediaGetCurrentMovieProperty(mh, whichProperty, value);
    }

    private static int MovieMediaGetCurrentTrackProperty(int mh, int trackID, int whichProperty, int[] value) {
        return MediaHandlerManager.MovieMediaGetCurrentTrackProperty(mh, trackID, whichProperty, value);
    }

    private static int MovieMediaGetChildMovieDataReference(int mh, int id, int index, int dataRefType, byte[] dataRef, int dataRefIndex, int flags) {
        return MediaHandlerManager.MovieMediaGetChildMovieDataReference(mh, id, index, dataRefType, dataRef, dataRefIndex, flags);
    }

    private static int MovieMediaSetChildMovieDataReference(int mh, int id, int dataRefType, byte[] dataRef) {
        return MediaHandlerManager.MovieMediaSetChildMovieDataReference(mh, id, dataRefType, dataRef);
    }

    private static int MovieMediaLoadChildMovieFromDataReference(int mh, int id) {
        return MediaHandlerManager.MovieMediaLoadChildMovieFromDataReference(mh, id);
    }

    private static void copyArrayToPointer(int[] array, int arrayOffset, int ptr, int ptrOffset, int size) {
        MemoryManager.copyArrayToPointer(array, arrayOffset, ptr, ptrOffset, size);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }
}
