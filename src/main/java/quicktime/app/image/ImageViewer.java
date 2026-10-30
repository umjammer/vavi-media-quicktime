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
 * @deprecated
 * This class presents a sequence of Images that a user can view
 * by simply clicking the mouse, or option clicking to view backwards.
 * It takes an ImageSequencer object as its source images, and uses
 * an ImagePresenter object internally to present the current image.
 */
public abstract class ImageViewer implements QTDrawable, ImageSettable {

    private ImageSequencer images;

    /**
     * @deprecated
     * This is the ImagePresenter object that presents the current image.* @deprecated since QTJava 6.1
     */
    protected ImagePresenter imagePresenter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * Creates a viewer for a sequence of images.* @deprecated since QTJava 6.1
     */
    public ImageViewer(ImageSequencer images) throws QTException {
        setImages(images);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current collection of images that the Viewer is presenting
     */
    public ImageSequencer getImages() {
        return this.images;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This can be used by subclasses to change the Image that the viewer is presenting.
     * @param imageData the new image data that the object should make its current image.
     * @param desc this is the description that describes the image data
     * @throws quicktime.QTException
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setImageData(EncodedImage imageData, ImageDescription desc) throws QTException, StdQTException {
        if (this.imagePresenter == null) {
            this.imagePresenter = new ImagePresenter(new QDRect(desc.getWidth(), desc.getHeight()));
        }
        this.imagePresenter.setImageData(imageData, desc);
        this.imagePresenter.redraw(null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets a new sequence of images for the viewer to present. The viewer will always
     * display the currentImage, so if you want to have a particular setting for this
     * a subclass can overide this method, set the currentImage on the incoming images
     * and then call super.setImages(images).
     */
    public void setImages(ImageSequencer images) throws QTException {
        this.images = images;
        setImageData(images.getImage(), images.getDescription());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @throws quicktime.QTException
     */
    @Override
    public void setGWorld(QDGraphics cgp) throws QTException {
        this.imagePresenter.setGWorld(cgp);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     */
    @Override
    public QDGraphics getGWorld() {
        return this.imagePresenter.getGWorld();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setDisplayBounds(QDRect bounds) throws StdQTException {
        this.imagePresenter.setDisplayBounds(bounds);
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
        QDRect bounds = getDisplayBounds();
        bounds.setX(x);
        bounds.setY(y);
        setDisplayBounds(bounds);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the boundary of the client
     */
    @Override
    public QDRect getDisplayBounds() {
        return this.imagePresenter.getDisplayBounds();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method allows you to set a QTDrawable's clipping region.
     * @param theClip a Region that defines the new clipping region.
     * @throws quicktime.QTException
     */
    @Override
    public void setClip(Region theClip) throws QTException {
        this.imagePresenter.setClip(theClip);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method allows you to get the current clipped Region of the QTDrawable.
     * @return the clipping region
     * @throws quicktime.QTException
     */
    @Override
    public Region getClip() throws QTException {
        return this.imagePresenter.getClip();
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        return this.imagePresenter.getInitialSize();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size of the ImageViewer's image data
     * @throws quicktime.QTException
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        return this.imagePresenter.getOriginalSize();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @throws quicktime.std.StdQTException
     */
    @Override
    public Matrix getMatrix() throws StdQTException {
        return this.imagePresenter.getMatrix();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     * @param matrix the new matrix
     * @throws quicktime.QTException
     */
    @Override
    public void setMatrix(Matrix matrix) throws QTException {
        this.imagePresenter.setMatrix(matrix);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This sets the GraphicsMode for the Compositor when it draws its composited
     * contents to its destination QDGraphics (typically the QTCanvas).
     * @param mode the Graphics mode for the Compositor
     * @throws quicktime.QTException
     */
    public void setGraphicsMode(GraphicsMode mode) throws QTException {
        this.imagePresenter.setGraphicsMode(mode);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This gets the current GraphicsMode for the compositor.
     * @return current GraphicsMode for the compositor
     */
    public GraphicsMode getGraphicsMode() {
        return this.imagePresenter.getGraphicsMode();
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
        this.imagePresenter.redraw(invalidRgn);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. In this class it does nothing.
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
     * that is the source of the interest. In this class it does nothing.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void removedFrom(Object interest) {
    }
}
