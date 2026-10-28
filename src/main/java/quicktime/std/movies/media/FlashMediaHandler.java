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

    FlashMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
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
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        int[] arrayOfInt4 = { 0 };
        int i = FlashMediaGetRefConBounds(_ID(), refCon, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4);
        StdQTException.checkError(i);
        return new QDRect(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0] - arrayOfInt1[0], arrayOfInt4[0] - arrayOfInt2[0]);
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
        int[] arrayOfInt = { 0 };
        int i = FlashMediaGetRefConID(_ID(), refCon, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        int[] arrayOfInt = { 0 };
        int i = FlashMediaIDToRefCon(_ID(), refConID, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Get the currently displayed Flash Frame number.
     * @see "QuickTime::FlashMediaGetDisplayedFrameNumber()"
     * @return flashFrameNumber the flash frame number.
     * @throws quicktime.std.StdQTException
     */
    public int getDisplayedFrameNumber() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaGetDisplayedFrameNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Given a Flash Frame Number, get the corresponding movieTime.
     * @see "QuickTime::FlashMediaFrameNumberToMovieTime()"
     * @param flashFrameNumber the flash frame number.
     * @return movieTime the corresponding movieTime.
     * @throws quicktime.std.StdQTException
     */
    public int frameNumberToMovieTime(int flashFrameNumber) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaFrameNumberToMovieTime(_ID(), flashFrameNumber, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Given a Flash frame's label, get the corresponding movieTime.
     * @see "QuickTime::FlashMediaFrameLabelToMovieTime()"
     * @param theLabel the flash frame's label.
     * @return movieTime the corresponding movie time.
     * @throws quicktime.std.StdQTException
     */
    public int frameLabelToMovieTime(QTPointerRef theLabel) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaFrameLabelToMovieTime(_ID(), QTObject.ID(theLabel), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        int[] arrayOfInt = { 0 };
        int i = FlashMediaGetFlashVariable(_ID(), QTUtils.String2CString(path), QTUtils.String2CString(name), arrayOfInt);
        StdQTException.checkError(i);
        myStringHandle localmyStringHandle = new myStringHandle(arrayOfInt[0], this);
        try {
            return localmyStringHandle.toJavaString();
        } catch (Exception localException) {
            localException.printStackTrace();
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
        byte[] arrayOfByte = { 0 };
        int i = FlashMediaGetSupportedSwfVersion(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        if (arrayOfByte.length == 1) {
            return Byte.valueOf(arrayOfByte[0]).toString();
        }
        return QTUtils.CString2String(arrayOfByte, 0);
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

    private static int FlashMediaSetPan(int paramInt, short paramShort1, short paramShort2) {
        return MediaHandlerManager.FlashMediaSetPan(paramInt, paramShort1, paramShort2);
    }

    static class myStringHandle extends StringHandle {

        protected myStringHandle(int paramInt, Object paramObject) {
            super(paramInt, paramObject, 1);
        }
    }

    private static int FlashMediaSetZoom(int paramInt, short paramShort) {
        return MediaHandlerManager.FlashMediaSetZoom(paramInt, paramShort);
    }

    private static int FlashMediaSetZoomRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MediaHandlerManager.FlashMediaSetZoomRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static int FlashMediaGetRefConBounds(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4) {
        return MediaHandlerManager.FlashMediaGetRefConBounds(paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4);
    }

    private static int FlashMediaGetRefConID(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MediaHandlerManager.FlashMediaGetRefConID(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int FlashMediaIDToRefCon(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MediaHandlerManager.FlashMediaIDToRefCon(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int FlashMediaGetDisplayedFrameNumber(int paramInt, int[] paramArrayOfInt) {
        return MediaHandlerManager.FlashMediaGetDisplayedFrameNumber(paramInt, paramArrayOfInt);
    }

    private static int FlashMediaFrameNumberToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MediaHandlerManager.FlashMediaFrameNumberToMovieTime(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int FlashMediaFrameLabelToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return MediaHandlerManager.FlashMediaFrameLabelToMovieTime(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int FlashMediaGetFlashVariable(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt) {
        return MediaHandlerManager.FlashMediaGetFlashVariable(paramInt, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfInt);
    }

    private static int FlashMediaSetFlashVariable(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte paramByte) {
        return MediaHandlerManager.FlashMediaSetFlashVariable(paramInt, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramByte);
    }

    private static int FlashMediaGetSupportedSwfVersion(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.FlashMediaGetSupportedSwfVersion(paramInt, paramArrayOfByte);
    }
}
