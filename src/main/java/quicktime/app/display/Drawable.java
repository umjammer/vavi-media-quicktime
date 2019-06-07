package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.spaces.Listener;
import quicktime.qd.QDRect;
import quicktime.qd.Region;

/**
 * Deprecated.
 * This interface is designed to work hand-in-hand with a QTCanvas object. Therefore,
 * all classes which want to interact with a QTCanvas object, referred to as 'clients'
 * must implement this interface.
 * This interface is general enough that Java drawing objects can implement this interface and
 * therefore become a client of the QTCanvas.
 * Note: A Java applet or application never directly calls any of these
 * Drawable methods -- the QTCanvas object associated with this
 * Drawable object will call these methods as needed.
 * QTExceptions can be thrown by any of these methods and would indicate that either the
 * graphics environment has changed in some unexpected way or that the media object itself
 * is in some unexpected state.
 */
public abstract interface Drawable extends Listener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @param {@link quicktime.QTException}
     */
    public abstract void setDisplayBounds(QDRect paramQDRect) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the display boundary of the client
     * @return {@link quicktime.QTException}
     */
    public abstract QDRect getDisplayBounds() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @param {@link quicktime.QTException}
     */
    public abstract void redraw(Region paramRegion) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/Drawable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
