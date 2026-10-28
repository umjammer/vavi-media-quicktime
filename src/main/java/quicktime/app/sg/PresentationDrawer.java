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

    private static final TaskThread idleTasker = new TaskThread("Default Pres Tasker", 20);

    private final Presentation pres;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This constructor creates an PresentationDrawer object from a Presentation component.
     * @param pres the Presentation object to present
     */
    public PresentationDrawer(Presentation pres) throws QTException {
        this.pres = pres;
        setDefaultTasker(idleTasker);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This returns the PresentationDrawer's Presentation that is displayed by this drawable.
     * @return an Presentation
     */
    public Presentation getPresentation() {
        return this.pres;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @throws quicktime.QTException
     */
    @Override
    public QDGraphics getGWorld() throws QTException {
        return this.pres.getGWorld();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param gWorld a QDGraphics object
     * @throws quicktime.QTException
     */
    @Override
    public synchronized void setGWorld(QDGraphics gWorld) throws QTException {
        this.pres.setGWorld(gWorld);
    }

    /**
     * @deprecated
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
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the intial size of the Presentation
     * @throws quicktime.QTException
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        QDDimension localQDDimension = this.pres.getDimensions(null);
        Matrix localMatrix = this.pres.getMatrix(null);
        return new QDDimension(localQDDimension.getWidth() / localMatrix.getSx(), localQDDimension.getHeight() / localMatrix.getSy());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @throws quicktime.QTException
     */
    @Override
    public Matrix getMatrix() throws QTException {
        return this.pres.getMatrix(null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     * @param matrix the new display Matrix
     * @throws quicktime.QTException
     */
    @Override
    public void setMatrix(Matrix matrix) throws QTException {
        this.pres.setMatrix(null, matrix);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the display bounds for the Presentation
     * @param bounds the new size and location.
     * @throws quicktime.QTException
     */
    @Override
    public synchronized void setDisplayBounds(QDRect bounds) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(bounds.getX());
        localMatrix.setTy(bounds.getY());
        localMatrix.setSx(bounds.getWidth() / localQDRect.getWidth());
        localMatrix.setSy(bounds.getHeight() / localQDRect.getHeight());
        setMatrix(localMatrix);
        this.pres.setDimensions(null, bounds.getWidth(), bounds.getHeight());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @throws quicktime.QTException
     */
    @Override
    public void setLocation(int x, int y) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(x);
        localQDRect.setY(y);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current display location and size of the Presentation.
     *
     * @return the boundary of the client
     * @throws quicktime.QTException
     */
    @Override
    public QDRect getDisplayBounds() throws QTException {
        return new QDRect(this.pres.getDimensions(null));
    }

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
    @Override
    public final synchronized void redraw(Region invalidRgn) throws QTException {
        this.pres.invalidateRegion(invalidRgn);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @throws quicktime.QTException
     */
    @Override
    public Region getClip() throws QTException {
        return this.pres.getClip(null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @throws quicktime.QTException
     */
    @Override
    public void setClip(Region reg) throws QTException {
        this.pres.setClip(null, reg);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method performs idle processing for the Presentation and will be automatically
     * called if this object is added to the TaskThread object.
     * @see quicktime.util.TaskThread
     */
    @Override
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
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void addedTo(Object interest) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void removedFrom(Object interest) {
    }
}

