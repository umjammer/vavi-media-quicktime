package quicktime.app.sg;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.Matrix;
import quicktime.streaming.Presentation;

public class PresentationDrawer extends Tasking implements QTDrawable {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static TaskThread idleTasker = new TaskThread("Default Pres Tasker", 20);

    private Presentation pres;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates an PresentationDrawer object from a Presentation component.
     * @param pres the Presentation object to present
     */
    public PresentationDrawer(Presentation paramPresentation) throws QTException {
        this.pres = paramPresentation;
        setDefaultTasker(idleTasker);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns the PresentationDrawer's Presentation that is displayed by this drawable.
     * @return an Presentation
     */
    public Presentation getPresentation() {
        return this.pres;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @return {@link quicktime.QTException}
     */
    public QDGraphics getGWorld() throws QTException {
        return this.pres.getGWorld();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param gWorld a QDGraphics object
     * @param {@link quicktime.QTException}
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.pres.setGWorld(paramQDGraphics);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension localQDDimension = this.pres.getDimensions(null);
        Matrix localMatrix = this.pres.getMatrix(null);
        return new Dimension((int) (localQDDimension.getWidth() / localMatrix.getSx()), (int) (localQDDimension.getHeight() / localMatrix.getSy()));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the intial size of the Presentation
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        QDDimension localQDDimension = this.pres.getDimensions(null);
        Matrix localMatrix = this.pres.getMatrix(null);
        return new QDDimension(localQDDimension.getWidth() / localMatrix.getSx(), localQDDimension.getHeight() / localMatrix.getSy());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @return {@link quicktime.QTException}
     */
    public Matrix getMatrix() throws QTException {
        return this.pres.getMatrix(null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     * @param matrix the new display Matrix
     * @param {@link quicktime.QTException}
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.pres.setMatrix(null, paramMatrix);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the display bounds for the Presentation
     * @param bounds the new size and location.
     * @param {@link quicktime.QTException}
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / localQDRect.getWidth());
        localMatrix.setSy(paramQDRect.getHeight() / localQDRect.getHeight());
        setMatrix(localMatrix);
        this.pres.setDimensions(null, paramQDRect.getWidth(), paramQDRect.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @param {@link quicktime.QTException}
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current display location and size of the Presentation.
     *
     * @return the boundary of the client
     * @return {@link quicktime.QTException}
     */
    public QDRect getDisplayBounds() throws QTException {
        return new QDRect(this.pres.getDimensions(null));
    }

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
    public final synchronized void redraw(Region paramRegion) throws QTException {
        this.pres.invalidateRegion(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.pres.getClip(null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @param {@link quicktime.QTException}
     */
    public void setClip(Region paramRegion) throws QTException {
        this.pres.setClip(null, paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method performs idle processing for the Presentation and will be automatically
     * called if this object is added to the TaskThread object.
     * @see quicktime.util.TaskThread
     */
    public final synchronized void task() throws QTException {
        this.pres.idle(null);
    }

    /**
     * Returns a String representation of this object * @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[" + this.pres + "]";
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/sg/
 * PresentationDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
