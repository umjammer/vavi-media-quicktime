package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.spaces.Listener;
import quicktime.qd.QDRect;
import quicktime.qd.Region;

/**
 * @deprecated
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
public interface Drawable extends Listener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @throws quicktime.QTException
     */
    void setDisplayBounds(QDRect bounds) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the display boundary of the client
     * @throws quicktime.QTException
     */
    QDRect getDisplayBounds() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @throws quicktime.QTException
     */
    void redraw(Region invalidRgn) throws QTException;
}
