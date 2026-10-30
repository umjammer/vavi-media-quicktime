package quicktime.qd;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
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
 * @see "quicktime.std.qd.PixMap"
 */
public final class ColorTable extends QTHandleRef implements QuickTimeLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 16;

    /**
     * Get the custom color table for an image. If there is no CTable this returns null.
     * @see "QuickTime::GetImageDescriptionCTable"
     * @param id the ImageDescription
     * @throws quicktime.QTException
     * @see quicktime.std.image.ImageDescription#getCTable()
     */
    public static ColorTable fromImageDescription(ImageDescription id) throws QTException {
        int[] ctable = new int[1];
        if ((id.getClutID() == -1) || (id.getClutID() == 0))
            return null;
        StdQTException.checkError(GetImageDescriptionCTable(QTObject.ID(id), ctable));
        return (ctable[0] == -1) || (ctable[0] == 0) ? null : new ColorTable(ctable[0], false);
    }

    /**
     * This method allows your application to get the color table of the movie.
     * @see "QuickTime::GetMovieColorTable()"
     * @param m the movie to get the color table from
     * @return a ColorTable object specifiying the current color table.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getColorTable
     */
    public static ColorTable fromMovie(Movie m) throws QTException {
        int[] ctab = new int[1];
        int i = GetMovieColorTable(QTObject.ID(m), ctab);
        StdQTException.checkError(i);
        return ctab[0] == -1 ? null : new ColorTable(ctab[0], false);
    }

    /**
     * When creating a PixMap for a particular graphics device, Color QuickDraw
     * creates a ColorTable that defines the best colors available for the pixel
     * image on that particular graphics device.  Note that a reference to the
     * PixMap object will be maintained by the ColorTable object to prevent the
     * PixMap from being garbage collected before the ColorTable.
     * @return a ColorTable object
     * @see "quicktime.std.qd.PixMap"
     */
    public static ColorTable fromPixMap(PixMap pixMap) {
        return new ColorTable(getIntFromHandle(QTObject.ID(pixMap), 42), pixMap, false);
    }

    /**
     * Gets the current color table or null to clear the current color table for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the current color table or null if no color table being used.
     * @throws quicktime.QTException
     * @see quicktime.std.qtcomponents.ImageCompressionDialog#getInfoColorTable()
     */
    public static ColorTable fromCompressionDialog(ImageCompressionDialog ci) throws QTException {
        int[] destArray = { 0 };
        StdQTException.checkError(SCGetInfo(QTObject.ID(ci), 1668052340, destArray));
        return destArray[0] == 0 ? null : new ColorTable(destArray[0], false);
    }

    private ColorTable(int nativeRep, boolean locked) throws QTException {
        super(nativeRep, null, locked);
    }

    private ColorTable(int nativeRep, Object ref, boolean locked) {
        super(nativeRep, ref, locked);
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

    private static short GetImageDescriptionCTable(int desc, int[] ctable) {
        return ImageCompressionManager.GetImageDescriptionCTable(desc, ctable);
    }

    private static short GetMovieColorTable(int movie, int[] ctab) {
        return MovieToolbox.GetMovieColorTable(movie, ctab);
    }

    private static int SCGetInfo(int ci, int type, int[] destArray) {
        return ImageCompressionManager.SCGetInfo(ci, type, destArray);
    }

    private static int getIntFromHandle(int h, int offset) {
        return MemoryManager.getIntFromHandle(h, offset);
    }
}
