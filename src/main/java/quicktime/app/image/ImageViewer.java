package quicktime.app.image;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;

/**
 * Deprecated.
 * This class presents a sequence of Images that a user can view
 * by simply clicking the mouse, or option clicking to view backwards.
 * It takes an ImageSequencer object as its source images, and uses
 * an ImagePresenter object internally to present the current image.
 */
public abstract class ImageViewer implements QTDrawable, ImageSettable {

    private ImageSequencer images;

    /**
     * Deprecated.
     * This is the ImagePresenter object that presents the current image.* @deprecated since QTJava 6.1
     */
    protected ImagePresenter imagePresenter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * Creates a viewer for a sequence of images.* @deprecated since QTJava 6.1
     */
    public ImageViewer(ImageSequencer paramImageSequencer) throws QTException {
        setImages(paramImageSequencer);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current collection of images that the Viewer is presenting
     */
    public ImageSequencer getImages() {
        return this.images;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This can be used by subclasses to change the Image that the viewer is presenting.
     * @param imageData the new image data that the object should make its current image.
     * @param desc this is the description that describes the image data
     * @param {@link quicktime.QTException}
     * @param {@link quicktime.std.StdQTException}
     */
    public void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException, StdQTException {
        if (this.imagePresenter == null) {
            this.imagePresenter = new ImagePresenter(new QDRect(paramImageDescription.getWidth(), paramImageDescription.getHeight()));
        }
        this.imagePresenter.setImageData(paramEncodedImage, paramImageDescription);
        this.imagePresenter.redraw(null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a new sequence of images for the viewer to present. The viewer will always
     * display the currentImage, so if you want to have a particular setting for this
     * a subclass can overide this method, set the currentImage on the incoming images
     * and then call super.setImages(images).
     */
    public void setImages(ImageSequencer paramImageSequencer) throws QTException {
        this.images = paramImageSequencer;
        setImageData(paramImageSequencer.getImage(), paramImageSequencer.getDescription());
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
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.imagePresenter.setGWorld(paramQDGraphics);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     */
    public QDGraphics getGWorld() {
        return this.imagePresenter.getGWorld();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.imagePresenter.setDisplayBounds(paramQDRect);
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
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the boundary of the client
     */
    public QDRect getDisplayBounds() {
        return this.imagePresenter.getDisplayBounds();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to set a QTDrawable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @param {@link quicktime.QTException}
     */
    public void setClip(Region paramRegion) throws QTException {
        this.imagePresenter.setClip(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows you to get the current clipped Region of the QTDrawable.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.imagePresenter.getClip();
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        return this.imagePresenter.getInitialSize();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size of the ImageViewer's image data
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        return this.imagePresenter.getOriginalSize();
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
        return this.imagePresenter.getMatrix();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     * @param matrix the new matrix
     * @param {@link quicktime.QTException}
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.imagePresenter.setMatrix(paramMatrix);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This sets the GraphicsMode for the Compositor when it draws its composited
     * contents to its destination QDGraphics (typically the QTCanvas).
     * @param mode the Graphics mode for the Compositor
     * @param {@link quicktime.QTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        this.imagePresenter.setGraphicsMode(paramGraphicsMode);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This gets the current GraphicsMode for the compositor.
     * @return current GraphicsMode for the compositor
     */
    public GraphicsMode getGraphicsMode() {
        return this.imagePresenter.getGraphicsMode();
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
        this.imagePresenter.redraw(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. In this class it does nothing.
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
     * that is the source of the interest. In this class it does nothing.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageViewer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
