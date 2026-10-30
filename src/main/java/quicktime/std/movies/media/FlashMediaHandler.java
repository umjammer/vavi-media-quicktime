package quicktime.std.movies.media;

import quicktime.QTObject;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


/**
 * This media handler allows a SWF file to be treated as a track within a QuickTime movie. Because a QuickTime movie may contain any
 * number of tracks, multiple SWF tracks may be added to the same movie. The Flash Media Handler also provides support for an
 * optimized case using the alpha channel graphics mode, which allows a Flash track to be composited cleanly over other tracks.
 */
public final class FlashMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    FlashMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Pan by a percentage.Negative xPercent pans left, positive xPercent pans right.
     * Negative yPercent pans to bottom, positive yPercent pans to top.
     * The Zoom Rectangle must be less than the full size of the Flash track for panning to be possible.
     * @see "QuickTime::FlashMediaSetPan()"
     * @param xPercent left / right pan percent
     * @param yPercent top / bottom pan percent
     * @throws quicktime.std.StdQTException
     */
    public void setPan(short xPercent, short yPercent) throws StdQTException {
        int i = FlashMediaSetPan(_ID(), xPercent, yPercent);
        StdQTException.checkError(i);
    }

    /**
     * Zoom in or out by a percentage. Use a number less than 100 to zoom in, and greater than 100 to zoom out.
     * @see "QuickTime::FlashMediaSetZoom()"
     * @param factor the zoom percentage factor.
     * @throws quicktime.std.StdQTException
     */
    public void setZoom(short factor) throws StdQTException {
        int i = FlashMediaSetZoom(_ID(), factor);
        StdQTException.checkError(i);
    }

    /**
     * Zoom in on a specified sub rectangle, this becomes the "Zoom Rectangle".
     * @see "QuickTime::FlashMediaSetZoomRect()"
     * @param left the left corner
     * @param top the top corner
     * @param right the right corner
     * @param bottom the bottom corner
     * @throws quicktime.std.StdQTException
     */
    public void setZoomRect(int left, int top, int right, int bottom) throws StdQTException {
        int i = FlashMediaSetZoomRect(_ID(), left, top, right, bottom);
        StdQTException.checkError(i);
    }

    /**
     * You can use mediaHitTestForTargetRefCon() to hit test a flash track and get a refCon for a button or symbol that was hit.
     * A RefCon represents a flash button or symbol at runtime.
     * @see "QuickTime::FlashMediaGetRefConBounds()"
     * @param refCon the runtime symbol.
     * @return the bounding rectangle for the given refCon
     * @throws quicktime.std.StdQTException
     */
    public QDRect getRefConBounds(int refCon) throws StdQTException {
        int[] left = { 0 };
        int[] top = { 0 };
        int[] right = { 0 };
        int[] bottom = { 0 };
        int i = FlashMediaGetRefConBounds(_ID(), refCon, left, top, right, bottom);
        StdQTException.checkError(i);
        return new QDRect(left[0], top[0], right[0] - left[0], bottom[0] - top[0]);
    }

    /**
     * A RefCon represents a flash button or symbol at runtime. The ID represents the button or symbols ID in the .swf file.
     * Converts a Refcon to an ID.
     * @see "QuickTime::FlashMediaGetRefConID()"
     * @param refCon the runtime symbol.
     * @return refConID the symbols ID.
     * @throws quicktime.std.StdQTException
     */
    public int getRefConID(int refCon) throws StdQTException {
        int[] id = { 0 };
        int i = FlashMediaGetRefConID(_ID(), refCon, id);
        StdQTException.checkError(i);
        return id[0];
    }

    /**
     * A RefCon represents a flash button or symbol at runtime. The ID represents the button or symbols ID in the .swf file.
     * Convert an ID to a RefCon.
     * @see "QuickTime::FlashMediaIDToRefCon()"
     * @param refConID the symbols ID.
     * @return refCon the symbol.
     * @throws quicktime.std.StdQTException
     */
    public int idToRefCon(int refConID) throws StdQTException {
        int[] refCon = { 0 };
        int i = FlashMediaIDToRefCon(_ID(), refConID, refCon);
        StdQTException.checkError(i);
        return refCon[0];
    }

    /**
     * Get the currently displayed Flash Frame number.
     * @see "QuickTime::FlashMediaGetDisplayedFrameNumber()"
     * @return flashFrameNumber the flash frame number.
     * @throws quicktime.std.StdQTException
     */
    public int getDisplayedFrameNumber() throws StdQTException {
        int[] frame = { 0 };
        int i = FlashMediaGetDisplayedFrameNumber(_ID(), frame);
        StdQTException.checkError(i);
        return frame[0];
    }

    /**
     * Given a Flash Frame Number, get the corresponding movieTime.
     * @see "QuickTime::FlashMediaFrameNumberToMovieTime()"
     * @param flashFrameNumber the flash frame number.
     * @return movieTime the corresponding movieTime.
     * @throws quicktime.std.StdQTException
     */
    public int frameNumberToMovieTime(int flashFrameNumber) throws StdQTException {
        int[] time = { 0 };
        int i = FlashMediaFrameNumberToMovieTime(_ID(), flashFrameNumber, time);
        StdQTException.checkError(i);
        return time[0];
    }

    /**
     * Given a Flash frame's label, get the corresponding movieTime.
     * @see "QuickTime::FlashMediaFrameLabelToMovieTime()"
     * @param theLabel the flash frame's label.
     * @return movieTime the corresponding movie time.
     * @throws quicktime.std.StdQTException
     */
    public int frameLabelToMovieTime(QTPointerRef theLabel) throws StdQTException {
        int[] time = { 0 };
        int i = FlashMediaFrameLabelToMovieTime(_ID(), QTObject.ID(theLabel), time);
        StdQTException.checkError(i);
        return time[0];
    }

    /**
     * Gets the value of a specified Flash action variable
     * @see "QuickTime::FlashMediaGetFlashVariable()"
     * @param path Specifies the path to the flash button to which the variable is attached
     * @param name Specifies the name of the flash variable
     * @return the vallue of the action variable
     * @throws quicktime.std.StdQTException
     */
    public String getFlashVariable(String path, String name) throws StdQTException {
        int[] value = { 0 };
        int i = FlashMediaGetFlashVariable(_ID(), QTUtils.String2CString(path), QTUtils.String2CString(name), value);
        StdQTException.checkError(i);
        myStringHandle localmyStringHandle = new myStringHandle(value[0], this);
        try {
            return localmyStringHandle.toJavaString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the SwfVersion support by the Media handler
     * @see "QuickTime::FlashMediaGetSupportedSwfVersion()"
     * @return the supported version
     * @throws quicktime.std.StdQTException
     */
    public String getSupportedSwfVersion() throws StdQTException {
        byte[] version = { 0 };
        int i = FlashMediaGetSupportedSwfVersion(_ID(), version);
        StdQTException.checkError(i);
        if (version.length == 1) {
            return Byte.valueOf(version[0]).toString();
        }
        return QTUtils.CString2String(version, 0);
    }

    /**
     * Sets the specified Flash action variable to a value
     * @see "QuickTime::FlashMediaSetFlashVariable()"
     * @param path Specifies the path to the flash button to which the variable is attached
     * @param name Specifies the name of the flash variable
     * @param value Specifies the new value of the flash variable
     * @param updateFocus Pass TRUE if the focus is to be changed
     * @throws quicktime.std.StdQTException
     */
    public void setFlashVariable(String path, String name, String value, boolean updateFocus) throws StdQTException {
        int i = FlashMediaSetFlashVariable(_ID(), QTUtils.String2CString(path), QTUtils.String2CString(name), QTUtils.String2CString(value), (byte) (updateFocus == true ? 1 : 0));
        StdQTException.checkError(i);
    }

    private static int FlashMediaSetPan(int mh, short x, short y) {
        return MediaHandlerManager.FlashMediaSetPan(mh, x, y);
    }

    static class myStringHandle extends StringHandle {

        protected myStringHandle(int nativeRep, Object ref) {
            super(nativeRep, ref, 1);
        }
    }

    private static int FlashMediaSetZoom(int mh, short factor) {
        return MediaHandlerManager.FlashMediaSetZoom(mh, factor);
    }

    private static int FlashMediaSetZoomRect(int mh, int left, int top, int right, int bottom) {
        return MediaHandlerManager.FlashMediaSetZoomRect(mh, left, top, right, bottom);
    }

    private static int FlashMediaGetRefConBounds(int mh, int refCon, int[] left, int[] top, int[] right, int[] bottom) {
        return MediaHandlerManager.FlashMediaGetRefConBounds(mh, refCon, left, top, right, bottom);
    }

    private static int FlashMediaGetRefConID(int mh, int refCon, int[] id) {
        return MediaHandlerManager.FlashMediaGetRefConID(mh, refCon, id);
    }

    private static int FlashMediaIDToRefCon(int mh, int id, int[] refCon) {
        return MediaHandlerManager.FlashMediaIDToRefCon(mh, id, refCon);
    }

    private static int FlashMediaGetDisplayedFrameNumber(int mh, int[] frame) {
        return MediaHandlerManager.FlashMediaGetDisplayedFrameNumber(mh, frame);
    }

    private static int FlashMediaFrameNumberToMovieTime(int mh, int frame, int[] time) {
        return MediaHandlerManager.FlashMediaFrameNumberToMovieTime(mh, frame, time);
    }

    private static int FlashMediaFrameLabelToMovieTime(int mh, int label, int[] time) {
        return MediaHandlerManager.FlashMediaFrameLabelToMovieTime(mh, label, time);
    }

    private static int FlashMediaGetFlashVariable(int mh, byte[] path, byte[] name, int[] value) {
        return MediaHandlerManager.FlashMediaGetFlashVariable(mh, path, name, value);
    }

    private static int FlashMediaSetFlashVariable(int mh, byte[] path, byte[] name, byte[] value, byte updateFocus) {
        return MediaHandlerManager.FlashMediaSetFlashVariable(mh, path, name, value, updateFocus);
    }

    private static int FlashMediaGetSupportedSwfVersion(int mh, byte[] version) {
        return MediaHandlerManager.FlashMediaGetSupportedSwfVersion(mh, version);
    }
}
