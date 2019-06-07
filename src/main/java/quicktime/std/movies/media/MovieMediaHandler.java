package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.image.Matrix;
import quicktime.util.QTHandle;

public class MovieMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(MovieMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    MovieMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * This method retrieves the duration of the currently instantiated embedded movie
     * QuickTime::MovieMediaGetCurrentMovieProperty()
     * @return the duration of the movie
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCurrentMoviePropertyDuration() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1685418593, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method gets the time scale of the currently instantiated embedded movie
     * QuickTime::MovieMediaGetCurrentMovieProperty()
     * @return the time scale of the movie
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCurrentMoviePropertyTimeScale() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953066355, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method gets the time record associated with the currently instantiated embedded movie
     * QuickTime::MovieMediaGetCurrentMovieProperty()
     * @return the time of the movie as a TimeRecord
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getCurrentMoviePropertyTime() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953066358, QTObject.ID(localTimeRecord));
        StdQTException.checkError(i);
        return localTimeRecord;
    }

    /**
     * This method gets the natural bounds of the currently instantiated embedded movie
     * QuickTime::MovieMediaGetCurrentMovieProperty()
     * @return the natural bounds of the movie
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getCurrentMoviePropertyNaturalBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1851880546, localQDRect.getRect());
        StdQTException.checkError(i);
        return localQDRect;
    }

    /**
     * This method gets the matrix of the currently instantiated embedded movie
     * QuickTime::MovieMediaGetCurrentMovieProperty()
     * @return the matrix of the movie
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getCurrentMoviePropertyMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1836348024, localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    public int[] getCurrentMoviePropertyTrackList() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = MovieMediaGetCurrentMovieProperty(_ID(), 1953264500, arrayOfInt);
        StdQTException.checkError(i);
        int j = arrayOfInt[0];
        return arrayOfInt;
    }

    /**
     * This method gets the media type of the currently instantiated embedded movie
     * QuickTime::MovieGetCurrentTrackProperty()
     * @param trackID the ID of the track to return the type of
     * @return the media type of the movie
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCurrentTrackPropertyMediaType(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = MovieMediaGetCurrentTrackProperty(_ID(), 1836349808, paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * This method gets the data reference of the currently instantiated embedded movie
     * QuickTime::MovieMediaGetChildMovieDataReference()
     * @param dataRefID the ID of the data reference to retrieve
     * @param dataRefIndex the index of the data reference to retrieve
     * @return the DataRef of the movie
     * @return {@link quicktime.QTException}
     */
    public DataRef getChildMovieDataReference(int paramInt1, int paramInt2) throws QTException {
        int i = 0;
        byte[] arrayOfByte = { 0 };
        int j = MovieMediaGetChildMovieDataReference(_ID(), paramInt1, paramInt2, i, arrayOfByte, paramInt1, paramInt2);
        StdQTException.checkError(j);
        return new DataRef(new QTHandle(arrayOfByte));
    }

    /**
     * This method sets the data reference of the currently instantiated embedded movie
     * QuickTime::MovieMediaSetChildMovieDataReference()
     * @param dataRefID the ID of the data ref to set
     * @param dataRefType the type of the data ref to set
     * @param dataRef the data ref to set
     * @param {@link quicktime.std.StdQTException}
     */
    public void setChildMovieDataReference(int paramInt1, int paramInt2, DataRef paramDataRef) throws StdQTException {
        int i = MovieMediaSetChildMovieDataReference(_ID(), paramInt1, paramInt2, paramDataRef.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * This method loads the movie associated with a specific data reference
     * QuickTime::MovieMediaLoadChildMovieFromDataReference()
     * @param dataRefID the dataReference ID to be used
     * @param {@link quicktime.std.StdQTException}
     */
    public void loadChildMovieFromDataReference(int paramInt) throws StdQTException {
        int i = MovieMediaLoadChildMovieFromDataReference(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    private static native int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MovieMediaGetCurrentMovieProperty(int paramInt1, int paramInt2, int paramInt3);

    private static native int MovieMediaGetCurrentTrackProperty(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native int MovieMediaGetChildMovieDataReference(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6);

    private static native int MovieMediaSetChildMovieDataReference(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native int MovieMediaLoadChildMovieFromDataReference(int paramInt1, int paramInt2);

    private static native void copyArrayToPointer(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MovieMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
