
package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Movie;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.util.QTHandleRef;


public final class ColorTable extends QTHandleRef implements QuickTimeLib {
    private static Object linkage;

    public static final int kNativeSize = 16;

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ColorTable.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static ColorTable fromImageDescription(ImageDescription paramImageDescription) throws QTException {
        int[] arrayOfInt = new int[1];
        if ((paramImageDescription.getClutID() == -1) || (paramImageDescription.getClutID() == 0))
            return null;
        StdQTException.checkError(GetImageDescriptionCTable(QTObject.ID(paramImageDescription), arrayOfInt));
        return (arrayOfInt[0] == -1) || (arrayOfInt[0] == 0) ? null : new ColorTable(arrayOfInt[0], false);
    }

    public static ColorTable fromMovie(Movie paramMovie) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GetMovieColorTable(QTObject.ID(paramMovie), arrayOfInt);
        StdQTException.checkError(i);

        return arrayOfInt[0] == -1 ? null : new ColorTable(arrayOfInt[0], false);
    }

    public static ColorTable fromPixMap(PixMap paramPixMap) {
        return new ColorTable(getIntFromHandle(QTObject.ID(paramPixMap), 42), paramPixMap, false);
    }

    public static ColorTable fromCompressionDialog(ImageCompressionDialog paramImageCompressionDialog) throws QTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetInfo(QTObject.ID(paramImageCompressionDialog), 1668052340, arrayOfInt));

        return arrayOfInt[0] == 0 ? null : new ColorTable(arrayOfInt[0], false);
    }

    private ColorTable(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, null, paramBoolean);
    }

    private ColorTable(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, paramBoolean);
    }

    public int getCtSeed() {
        return getIntAt(0);
    }

    public int getCtSize() {
        return getShortAt(6);
    }

    public String toString() {
        return super.toString() + "[ctSeed=" + getCtSeed() + ",ctFlags=0x" + Integer.toHexString(getShortAt(4)) + ",numEntries="
               + getCtSize() + "]";
    }

    private static native short GetImageDescriptionCTable(int paramInt, int[] paramArrayOfInt);

    private static native short GetMovieColorTable(int paramInt, int[] paramArrayOfInt);

    private static native int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int getIntFromHandle(int paramInt1, int paramInt2);
}
