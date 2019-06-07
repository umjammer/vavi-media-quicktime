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

/**
 * When creating a PixMap for a particular graphics device, Color QuickDraw creates
 * a ColorTable that defines the best colors available for the pixel image on that
 * particular graphics device.
 * refer to ColorTable in QuickDraw
 * @see quicktime.std.qd.PixMap
 */
public final class ColorTable extends QTHandleRef implements QuickTimeLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
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

    /**
     * Get the custom color table for an image. If there is no CTable this returns null.
     * QuickTime::GetImageDescriptionCTable
     * @param id the ImageDescription
     * @param {@link quicktime.QTException}
     * @see {@link quicktime.std.image.ImageDescription#getCTable()}
     */
    public static ColorTable fromImageDescription(ImageDescription paramImageDescription) throws QTException {
        int[] arrayOfInt = new int[1];
        if ((paramImageDescription.getClutID() == -1) || (paramImageDescription.getClutID() == 0))
            return null;
        StdQTException.checkError(GetImageDescriptionCTable(QTObject.ID(paramImageDescription), arrayOfInt));
        return (arrayOfInt[0] == -1) || (arrayOfInt[0] == 0) ? null : new ColorTable(arrayOfInt[0], false);
    }

    /**
     * This method allows your application to get the color table of the movie.
     * QuickTime::GetMovieColorTable()
     * @param m the movie to get the color table from
     * @return a ColorTable object specifiying the current color table.
     * @return {@link quicktime.QTException}
     * @see quicktime.std.movies.Movie#getCTable
     */
    public static ColorTable fromMovie(Movie paramMovie) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GetMovieColorTable(QTObject.ID(paramMovie), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == -1 ? null : new ColorTable(arrayOfInt[0], false);
    }

    /**
     * When creating a PixMap for a particular graphics device, Color QuickDraw
     * creates a ColorTable that defines the best colors available for the pixel
     * image on that particular graphics device.  Note that a reference to the
     * PixMap object will be maintained by the ColorTable object to prevent the
     * PixMap from being garbage collected before the ColorTable.
     * @return a ColorTable object
     * @see quicktime.std.qd.PixMap
     */
    public static ColorTable fromPixMap(PixMap paramPixMap) {
        return new ColorTable(getIntFromHandle(QTObject.ID(paramPixMap), 42), paramPixMap, false);
    }

    /**
     * Gets the current color table or null to clear the current color table for a dialog.
     * QuickTime::SCGetInfo()
     * @return the current color table or null if no color table being used.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.qtcomponents.ImageCompressionDialog#getInfoColorTable()}
     */
    public static ColorTable fromCompressionDialog(ImageCompressionDialog paramImageCompressionDialog) throws QTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(QTObject.ID(paramImageCompressionDialog), 1668052340, arrayOfInt));
        return arrayOfInt[0] == 0 ? null : new ColorTable(arrayOfInt[0], false);
    }

    private ColorTable(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, null, paramBoolean);
    }

    private ColorTable(int paramInt, Object paramObject, boolean paramBoolean) {
        super(paramInt, paramObject, paramBoolean);
    }

    /**
     * Identifies a particular instance of a color table.
     * @return an int
     */
    public int getCtSeed() {
        return getIntAt(0);
    }

    /**
     * Returns the number of colours in the colour table.
     */
    public int getCtSize() {
        return getShortAt(6);
    }

    /**
     * Print information about this object.
     * @return a string representing this ColorTable object
     */
    public String toString() {
        return super.toString() + "[ctSeed=" + getCtSeed() + ",ctFlags=0x" + Integer.toHexString(getShortAt(4)) + ",numEntries=" + getCtSize() + "]";
    }

    private static native short GetImageDescriptionCTable(int paramInt, int[] paramArrayOfInt);

    private static native short GetMovieColorTable(int paramInt, int[] paramArrayOfInt);

    private static native int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int getIntFromHandle(int paramInt1, int paramInt2);
}
