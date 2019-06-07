package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(FlashMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    FlashMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Pan by a percentage.Negative xPercent pans left, positive xPercent pans right.
     * Negative yPercent pans to bottom, positive yPercent pans to top.
     * The Zoom Rectangle must be less than the full size of the Flash track for panning to be possible.
     * QuickTime::FlashMediaSetPan()
     * @param xPercent left / right pan percent
     * @param yPercent top / bottom pan percent
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPan(short paramShort1, short paramShort2) throws StdQTException {
        int i = FlashMediaSetPan(_ID(), paramShort1, paramShort2);
        StdQTException.checkError(i);
    }

    /**
     * Zoom in or out by a percentage. Use a number less than 100 to zoom in, and greater than 100 to zoom out.
     * QuickTime::FlashMediaSetZoom()
     * @param factor the zoom percentage factor.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setZoom(short paramShort) throws StdQTException {
        int i = FlashMediaSetZoom(_ID(), paramShort);
        StdQTException.checkError(i);
    }

    /**
     * Zoom in on a specified sub rectangle, this becomes the "Zoom Rectangle".
     * QuickTime::FlashMediaSetZoomRect()
     * @param left the left corner
     * @param top the top corner
     * @param right the right corner
     * @param bottom the bottom corner
     * @param {@link quicktime.std.StdQTException}
     */
    public void setZoomRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws StdQTException {
        int i = FlashMediaSetZoomRect(_ID(), paramInt1, paramInt2, paramInt3, paramInt4);
        StdQTException.checkError(i);
    }

    /**
     * You can use mediaHitTestForTargetRefCon() to hit test a flash track and get a refCon for a button or symbol that was hit.
     * A RefCon represents a flash button or symbol at runtime.
     * QuickTime::FlashMediaGetRefConBounds()
     * @param refCon the runtime symbol.
     * @return the bounding rectangle for the given refCon
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getRefConBounds(int paramInt) throws StdQTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        int[] arrayOfInt4 = { 0 };
        int i = FlashMediaGetRefConBounds(_ID(), paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4);
        StdQTException.checkError(i);
        return new QDRect(arrayOfInt1[0], arrayOfInt2[0], arrayOfInt3[0] - arrayOfInt1[0], arrayOfInt4[0] - arrayOfInt2[0]);
    }

    /**
     * A RefCon represents a flash button or symbol at runtime. The ID represents the button or symbols ID in the .swf file.
     * Converts a Refcon to an ID.
     * QuickTime::FlashMediaGetRefConID()
     * @param refCon the runtime symbol.
     * @return refConID the symbols ID.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getRefConID(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaGetRefConID(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * A RefCon represents a flash button or symbol at runtime. The ID represents the button or symbols ID in the .swf file.
     * Convert an ID to a RefCon.
     * QuickTime::FlashMediaIDToRefCon()
     * @param refConID the symbols ID.
     * @return refCon the symbol.
     * @return {@link quicktime.std.StdQTException}
     */
    public int idToRefCon(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaIDToRefCon(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Get the currently displayed Flash Frame number.
     * QuickTime::FlashMediaGetDisplayedFrameNumber()
     * @return flashFrameNumber the flash frame number.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getDisplayedFrameNumber() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaGetDisplayedFrameNumber(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Given a Flash Frame Number, get the corresponding movieTime.
     * QuickTime::FlashMediaFrameNumberToMovieTime()
     * @param flashFrameNumber the flash frame number.
     * @return movieTime the corresponding movieTime.
     * @return {@link quicktime.std.StdQTException}
     */
    public int frameNumberToMovieTime(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaFrameNumberToMovieTime(_ID(), paramInt, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Given a Flash frame's label, get the corresponding movieTime.
     * QuickTime::FlashMediaFrameLabelToMovieTime()
     * @param theLabel the flash frame's label.
     * @return movieTime the corresponding movie time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int frameLabelToMovieTime(QTPointerRef paramQTPointerRef) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaFrameLabelToMovieTime(_ID(), QTObject.ID(paramQTPointerRef), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Gets the value of a specified Flash action variable
     * QuickTime::FlashMediaGetFlashVariable()
     * @param path Specifies the path to the flash button to which the variable is attached
     * @param name Specifies the name of the flash variable
     * @return the vallue of the action variable
     * @return {@link quicktime.std.StdQTException}
     */
    public String getFlashVariable(String paramString1, String paramString2) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = FlashMediaGetFlashVariable(_ID(), QTUtils.String2CString(paramString1), QTUtils.String2CString(paramString2), arrayOfInt);
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
     * QuickTime::FlashMediaGetSupportedSwfVersion()
     * @return the supported version
     * @return {@link quicktime.std.StdQTException}
     */
    public String getSupportedSwfVersion() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        int i = FlashMediaGetSupportedSwfVersion(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        if (arrayOfByte.length == 1) {
            return new Byte(arrayOfByte[0]).toString();
        }
        return QTUtils.CString2String(arrayOfByte, 0);
    }

    /**
     * Sets the specified Flash action variable to a value
     * QuickTime::FlashMediaSetFlashVariable()
     * @param path Specifies the path to the flash button to which the variable is attached
     * @param name Specifies the name of the flash variable
     * @param value Specifies the new value of the flash variable
     * @param updateFocus Pass TRUE if the focus is to be changed
     * @param {@link quicktime.std.StdQTException}
     */
    public void setFlashVariable(String paramString1, String paramString2, String paramString3, boolean paramBoolean) throws StdQTException {
        int i = FlashMediaSetFlashVariable(_ID(), QTUtils.String2CString(paramString1), QTUtils.String2CString(paramString2), QTUtils.String2CString(paramString3), (byte) (paramBoolean == true ? 1 : 0));
        StdQTException.checkError(i);
    }

    private static native int FlashMediaSetPan(int paramInt, short paramShort1, short paramShort2);

    class myStringHandle extends StringHandle {

        protected myStringHandle(int paramInt, Object paramObject) {
            super(paramInt, paramObject, 1);
        }
    }

    private static native int FlashMediaSetZoom(int paramInt, short paramShort);

    private static native int FlashMediaSetZoomRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native int FlashMediaGetRefConBounds(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4);

    private static native int FlashMediaGetRefConID(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaIDToRefCon(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaGetDisplayedFrameNumber(int paramInt, int[] paramArrayOfInt);

    private static native int FlashMediaFrameNumberToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaFrameLabelToMovieTime(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int FlashMediaGetFlashVariable(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt);

    private static native int FlashMediaSetFlashVariable(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte paramByte);

    private static native int FlashMediaGetSupportedSwfVersion(int paramInt, byte[] paramArrayOfByte);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/FlashMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
