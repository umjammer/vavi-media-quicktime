
package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.util.QTPointerRef;


public final class QTGetExternalMovieRecord extends QTPointerRef implements quicktime.jdirect.PrimitivesLib {
    private static Object linkage;

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

    public int getTargetType() {
        return getIntAt(0);
    }

    public String getMovieName() {
        int i = getIntAt(4);

        int j = getByteFromPointer(i, 0);

        byte[] arrayOfByte = new byte[j];
        copyPointerToArray(i, 1, arrayOfByte, 0, j);

        return new String(arrayOfByte);
    }

    public int getMovieID() {
        return getIntAt(8);
    }

    public Movie getMovie() {
        return new Movie(getIntAt(12), this);
    }

    public MovieController getMovieController() {
        return new MovieController(getIntAt(16), this);
    }

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native byte getByteFromPointer(int paramInt1, int paramInt2);
}
