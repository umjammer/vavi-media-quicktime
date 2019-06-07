package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.util.QTPointerRef;

public final class QTGetExternalMovieRecord extends QTPointerRef implements quicktime.jdirect.PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 20;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QTGetExternalMovieRecord.class);
                    }
                });
            }
        }.establish();
    }

    QTGetExternalMovieRecord(int paramInt, Object paramObject) {
        super(paramInt, 20, paramObject);
    }

    /**
     * Return the target Type associated with the event
     */
    public int getTargetType() {
        return getIntAt(0);
    }

    /**
     * Return the movie name associated with the event
     */
    public String getMovieName() {
        int i = getIntAt(4);
        int j = getByteFromPointer(i, 0);
        byte[] arrayOfByte = new byte[j];
        copyPointerToArray(i, 1, arrayOfByte, 0, j);
        return new String(arrayOfByte);
    }

    /**
     * Return the movie ID
     */
    public int getMovieID() {
        return getIntAt(8);
    }

    /**
     * Return the movie associated with the event
     */
    public Movie getMovie() {
        return new Movie(getIntAt(12), this);
    }

    /**
     * Return the movie controller
     */
    public MovieController getMovieController() {
        return new MovieController(getIntAt(16), this);
    }

    private static native void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4);

    private static native byte getByteFromPointer(int paramInt1, int paramInt2);
}
