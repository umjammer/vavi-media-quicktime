package quicktime.app.sg;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.time.TaskThread;
import quicktime.app.time.Tasking;
import quicktime.jdirect.QTNative;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.Matrix;
import quicktime.std.sg.SGVideoChannel;
import quicktime.std.sg.SequenceGrabber;

/**
 * Deprecated.
 * This class presents the SequenceGrabber/SGChannel component as a Drawable object
 * so that it can be displayed within the confines of the QTCanvas.
 */
public class SGDrawer extends Tasking implements QTDrawable {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static TaskThread sgTasker = new TaskThread("Default SG Tasker", 20);

    private SGVideoChannel chan;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor creates an SGDrawer object from a SGChannel component.
     * If the channel is set to useVideo then it will throw a paramErr QTException.
     * @param sg the SequenceGrabber component channel to present.
     */
    public SGDrawer(SGVideoChannel paramSGVideoChannel) throws QTException {
        this.chan = paramSGVideoChannel;
        setDefaultTasker(sgTasker);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns the SequenceGrabber's VideoChannel that is presented by this drawable.
     * @return an SGVideoChannel
     */
    public SGVideoChannel getSGChannel() {
        return this.chan;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns the SequenceGrabber that is presented by this drawable.
     * @return an SequenceGrabber
     */
    public SequenceGrabber getSequenceGrabber() {
        return this.chan.getSequenceGrabber();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @return {@link quicktime.std.StdQTException}
     */
    public QDGraphics getGWorld() throws StdQTException {
        return this.chan.getSequenceGrabber().getGWorld();
    }

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
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.chan.getSequenceGrabber().setGWorld(paramQDGraphics, null);
        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            this.deferTasking = true;
        } else {
            stopTasking();
            this.chan.getSequenceGrabber().stop();
        }
    }

    private boolean deferTasking = false;

    private SGCaptureCallback stopTimeCallback;

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.chan.getBounds();
        Matrix localMatrix = this.chan.getMatrix();
        return new Dimension((int) (localQDRect.getWidth() / localMatrix.getSx()), (int) (localQDRect.getHeight() / localMatrix.getSy()));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the intial size of the SGDrawer
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.chan.getBounds();
        Matrix localMatrix = this.chan.getMatrix();
        return new QDDimension(localQDRect.getWidth() / localMatrix.getSx(), localQDRect.getHeight() / localMatrix.getSy());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        return this.chan.getMatrix();
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
        this.chan.setMatrix(paramMatrix);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the display bounds for the SGDrawer
     * @param bounds the new size and location.
     * @param {@link quicktime.std.StdQTException}
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.chan.setBounds(paramQDRect);
        if (this.deferTasking) {
            this.deferTasking = false;
            startTasking();
        }
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
     * Returns the current display location and size of the SGDrawer.
     *
     * @return the boundary of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getDisplayBounds() throws StdQTException {
        return this.chan.getBounds();
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
     * @param {@link quicktime.std.StdQTException}
     */
    public final synchronized void redraw(Region paramRegion) throws StdQTException {
        this.chan.getSequenceGrabber().update(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.chan.getClip();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClip(Region paramRegion) throws StdQTException {
        this.chan.setClip(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method performs idle processing for the SGDrawer and will be automatically
     * called if this object is added to the TaskThread object.
     * It the sequence grabber has completed its capture task this method
     * will stop tasking the SGDrawer, stop the sequence grabber and call
     * the SGCaptureCallback if registered to notify the application that Sequence grabbing
     * is complete.
     * @see quicktime.util.TaskThread
     */
    public final void task() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            if (!this.chan.getSequenceGrabber().idleMore()) {
                stopTasking();
                this.chan.getSequenceGrabber().stop();
                if (this.stopTimeCallback != null) {
                    new Thread(new Runnable() {

                        public void run() {
                            SGDrawer.this.stopTimeCallback.finish(SGDrawer.this);
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a SGCaptureCallback that will be used by the SGDrawer to
     * notify an application when a sequence grabbing operation
     * has completed.
     * @param callback can be null to de-register notification
     */
    public void setSGCaptureCallback(SGCaptureCallback paramSGCaptureCallback) {
        this.stopTimeCallback = paramSGCaptureCallback;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Gets a SGCaptureCallback that is used by the SGDrawer to
     * notify an application when a sequence grabbing operation
     * has completed.
     * @return the SGCaptureCallback object or null if none registered.
     */
    public SGCaptureCallback getSGCaptureCallback() {
        return this.stopTimeCallback;
    }

    /**
     * Deprecated.
     * Returns a String representation of this object.* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[" + this.chan + "]";
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
 * SGDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
