
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
 */
public abstract class ImageViewer implements QTDrawable, ImageSettable {
    private ImageSequencer images;

    protected ImagePresenter imagePresenter;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    public ImageViewer(ImageSequencer paramImageSequencer) throws QTException {
        setImages(paramImageSequencer);
    }

    /**
     * @deprecated
     */
    public ImageSequencer getImages() {
        return this.images;
    }

    /**
     * @deprecated
     */
    public void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException,
                                                                                                     StdQTException {
        if (this.imagePresenter == null) {
            this.imagePresenter = new ImagePresenter(new QDRect(paramImageDescription.getWidth(),
                                                                paramImageDescription.getHeight()));
        }
        this.imagePresenter.setImageData(paramEncodedImage, paramImageDescription);
        this.imagePresenter.redraw(null);
    }

    /**
     * @deprecated
     */
    public void setImages(ImageSequencer paramImageSequencer) throws QTException {
        this.images = paramImageSequencer;
        setImageData(paramImageSequencer.getImage(), paramImageSequencer.getDescription());
    }

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.imagePresenter.setGWorld(paramQDGraphics);
    }

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() {
        return this.imagePresenter.getGWorld();
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.imagePresenter.setDisplayBounds(paramQDRect);
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() {
        return this.imagePresenter.getDisplayBounds();
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws QTException {
        this.imagePresenter.setClip(paramRegion);
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.imagePresenter.getClip();
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        return this.imagePresenter.getInitialSize();
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return this.imagePresenter.getOriginalSize();
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.imagePresenter.getMatrix();
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.imagePresenter.setMatrix(paramMatrix);
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        this.imagePresenter.setGraphicsMode(paramGraphicsMode);
    }

    /**
     * @deprecated
     */
    public GraphicsMode getGraphicsMode() {
        return this.imagePresenter.getGraphicsMode();
    }

    /**
     * @deprecated
     */
    public final synchronized void redraw(Region paramRegion) throws QTException {
        this.imagePresenter.redraw(paramRegion);
    }

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageViewer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
