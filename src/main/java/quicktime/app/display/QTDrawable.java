package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.image.Transformable;
import quicktime.qd.QDGraphics;
import quicktime.qd.Region;

/**
 * Deprecated.
 * This interface is designed to work hand-in-hand with a QTCanvas object. Therefore,
 * all classes which want to interact with a QTCanvas object, referred to as 'clients'
 * must implement this interface.
 * The class that implements this interface must draw into the QDGraphics that is presented
 * to the client - therefore this is not an appropriate interface for objects that are not
 * QuickTime based drawing objects.
 * Note: A Java applet or application never directly calls any of these
 * Drawable methods -- the QTCanvas object associated with this
 * Drawable object will call these methods as needed.
 * QTExceptions can be thrown by any of these methods and would indicate that either the
 * graphics environment has changed in some unexpected way or that the media object itself
 * is in some unexpected state.
 */
public abstract interface QTDrawable extends Drawable, Transformable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @param {@link quicktime.QTException}
     */
    public abstract void setLocation(int paramInt1, int paramInt2) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @param {@link quicktime.QTException}
     */
    public abstract void setGWorld(QDGraphics paramQDGraphics) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @return {@link quicktime.QTException}
     */
    public abstract QDGraphics getGWorld() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to set a Transformable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @param {@link quicktime.QTException}
     */
    public abstract void setClip(Region paramRegion) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to get the current clipped Region of the Transformable.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public abstract Region getClip() throws QTException;
}

