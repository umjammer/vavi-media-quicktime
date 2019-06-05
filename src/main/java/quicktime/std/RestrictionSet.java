
package quicktime.std;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.movies.Movie;


public final class RestrictionSet extends QTObject implements QuickTimeLib {
    private static Object linkage;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(RestrictionSet.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    private RestrictionSet(int paramInt) {
        super(paramInt, null);
    }

    public static RestrictionSet fromMovie(Movie paramMovie) throws StdQTException {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        int i = QTGetMovieRestrictions(QTObject.ID(paramMovie), arrayOfInt1, arrayOfInt2);

        return new RestrictionSet(arrayOfInt1[0]);
    }

    public static int getSupportedRestrictions(int paramInt) {
        int[] arrayOfInt = {
            0
        };
        QTGetSupportedRestrictions(paramInt, arrayOfInt);
        return arrayOfInt[0];
    }

    public int getClassCount() {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        QTRestrictionsGetInfo(_ID(), arrayOfInt1, arrayOfInt2);
        return arrayOfInt1[0];
    }

    public int getSeed() {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

        QTRestrictionsGetInfo(_ID(), arrayOfInt1, arrayOfInt2);
        return arrayOfInt2[0];
    }

    public int getItem(int paramInt) {
        int[] arrayOfInt = {
            0
        };
        QTRestrictionsGetItem(_ID(), paramInt, arrayOfInt);
        return arrayOfInt[0];
    }

    private static native short QTGetMovieRestrictions(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short QTGetSupportedRestrictions(int paramInt, int[] paramArrayOfInt);

    private static native short QTRestrictionsGetInfo(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short QTRestrictionsGetItem(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}
