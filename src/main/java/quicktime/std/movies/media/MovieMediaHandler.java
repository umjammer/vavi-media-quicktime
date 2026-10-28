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

    MovieMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * This method retrieves the duration of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the duration of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getCurrentMoviePropertyDuration() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1685418593, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method gets the time scale of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the time scale of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getCurrentMoviePropertyTimeScale() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953066355, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method gets the time record associated with the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the time of the movie as a TimeRecord
     * @throws quicktime.QTException
     */
    public TimeRecord getCurrentMoviePropertyTime() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953066358, QTObject.ID(localTimeRecord));
        StdQTException.checkError(i);
        return localTimeRecord;
    }

    /**
     * This method gets the natural bounds of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the natural bounds of the movie
     * @throws quicktime.std.StdQTException
     */
    public QDRect getCurrentMoviePropertyNaturalBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1851880546, localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * This method gets the matrix of the currently instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return the matrix of the movie
     * @throws quicktime.std.StdQTException
     */
    public Matrix getCurrentMoviePropertyMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1836348024, localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    /**
     * This method returns an array of all the track IDs associated with the currently
     * instantiated embedded movie
     * @see "QuickTime::MovieMediaGetCurrentMovieProperty()"
     * @return an array of track IDs of the movie
     * @throws quicktime.std.StdQTException
     */
    public int[] getCurrentMoviePropertyTrackList() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953264500, arrayOfInt);
        StdQTException.checkError(i);
        int j = arrayOfInt[0];
        return arrayOfInt;
    }

    /**
     * This method gets the media type of the currently instantiated embedded movie
     * @see "QuickTime::MovieGetCurrentTrackProperty()"
     * @param trackID the ID of the track to return the type of
     * @return the media type of the movie
     * @throws quicktime.std.StdQTException
     */
    public int getCurrentTrackPropertyMediaType(int trackID) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = MovieMediaGetCurrentTrackProperty(_ID(), 1836349808, trackID, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        byte[] arrayOfByte = { 0 };
        int j = MovieMediaGetChildMovieDataReference(_ID(), dataRefID, dataRefIndex, i, arrayOfByte, dataRefID, dataRefIndex);
        StdQTException.checkError(j);
        return new DataRef(new QTHandle(arrayOfByte));
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

    private static int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MediaHandlerManager.MovieMediaGetCurrentMovieProperty(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MovieMediaGetCurrentMovieProperty(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, int paramInt3) {
        return MediaHandlerManager.MovieMediaGetCurrentMovieProperty(paramInt1, paramInt2, paramInt3);
    }

    private static int MovieMediaGetCurrentTrackProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
        return MediaHandlerManager.MovieMediaGetCurrentTrackProperty(paramInt1, paramInt2, paramInt3, paramArrayOfInt);
    }

    private static int MovieMediaGetChildMovieDataReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6) {
        return MediaHandlerManager.MovieMediaGetChildMovieDataReference(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6);
    }

    private static int MovieMediaSetChildMovieDataReference(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MovieMediaSetChildMovieDataReference(paramInt1, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static int MovieMediaLoadChildMovieFromDataReference(int paramInt1, int paramInt2) {
        return MediaHandlerManager.MovieMediaLoadChildMovieFromDataReference(paramInt1, paramInt2);
    }

    private static void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }
}
