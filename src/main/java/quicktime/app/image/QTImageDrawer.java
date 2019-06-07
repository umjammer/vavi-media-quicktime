package quicktime.app.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.PixelGrabber;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.QTAppException;
import quicktime.app.actions.Invalidator;
import quicktime.app.actions.Notifier;
import quicktime.app.actions.NotifyListener;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.QTDrawable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;

/**
 * Deprecated.
 * Enables standard java drawing commands and graphics objects to have their content
 * rendered by QuickTime within a QuickTime graphics space. The QTImageDrawer is a parent
 * container for an object that implements the Paintable interface - which is the object that
 * actually does the drawing using standard java.awt paint commands and graphics objects.
 * The QTImageDrawer implements the DynamicImage interface so can be added to the
 * quicktime.app.display.Compositor object and thus used to draw into the same display space as other
 * QuickTime generated content, or can be a direct client of a QTCanvas.
 *
 * The isSingleFrame flag that is specified in the constructor allows optimisations to be made
 * by the QTImageDrawer. If this flag is set to Redrawable.kSingleFrame then the QTImageDrawer
 * will only call its Paintable object once to capture the image it Paintable draws. It will
 * then discard all of the objects it has acquired to allow the paint call to the Paintable.
 * If kMultiFrame is specified then on each redraw call the Paintable's paint call is called
 * and the result is redrawn. If the QTImageDrawer is a member of the SWCompositor a similar
 * efficiency can be achieved - if kSingleFrame is specified then when the drawer is added
 * to the Compositor it does not require invalidation so does not create an invalidator as its
 * image data is static. If kMultiFrame is specified then an invalidator is created.
 *
 * An application can explicitly call prepaint - which is the method that calls the Paintable's
 * paint method at any time. If the application is itself controlling the prepaint call then it
 * will probably want to set and reset the isRedrawing flag as the redraw method determines
 * whether to call prepaint or not based on the value of this flag. Once image data has been captured
 * using the prepaint call to see the results in destination QDGraphics redraw must be called. Thus
 * if an application is calling prepaint itself it will likely want to then setRedrawing to false and
 * would only require redrawing to be set to true if it requires any consequent calls of the redraw
 * method to re-capture image data from a call to prepaint.
 *
 * The Invalidator determines if the QTImageDrawer requires redrawing and invalidation based on
 * the value of the isRedrawing flag - it redraws and invalidates if isRedrawing returns true.
 * If the application is controlling the prepaint calls (and thus would typically set redrawing to false)
 * then it should invalidate the sprite presenter of the QTImageDrawer.
 * @see quicktime.app.display.Compositor,
 * quicktime.app.display.Paintable
 */
public class QTImageDrawer implements Notifier, QTDrawable, DynamicImage, Redrawable {

    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static final boolean isWin = QTSession.isCurrentOS(2);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This flag modifies the behaviour of the multiFrame flag in that
     * it will retain resources that are used between invocations of the
     * attached Paintables paint method. This setting is useful when
     * using the QTImageDrawer frequently to produce of modify pixels, however if
     * this production is only done spasmodically then the kMultiFrame
     * flag is better used to ensure that objects are not held for no good reason.
     */
    public static final int kMultiFrameRetain = 6;

    private ImagePresenter image;

    private Dimension mSize;

    private Image offscreenImage;

    private Image jPainterImage;

    private Graphics offscreenGraphics;

    private Paintable painter;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates the QTImageDrawer object which is the parent container of the
     * Paintable object. You supply an initial size - which defines the drawing area
     * that the paintable object has to draw into. Once created a paintable object's
     * drawing area cannot be resized as far as its relationship to its parent container.
     * If the display area of the QTImageDrawer is changed then the iamge that the Paintable
     * object paints will be scaled accordingly.
     * @param painter the java object that will paint into the space reserved by the QTImageDrawer
     * @param initialSize the size (with origin of 0, 0) that defines the drawing size available to the painter.
     * @param isSingleFrame if the kSingleFrame flag is set the Redrawable will capture the
     *  the results of the Paintable's paint call and then discard all of the resources required
     *  to call its Paintable (and the Paintable itself). In this case the Paintable's paint method
     *  will only be called once. If kSingleFrame is not set the Paintable's paint call will be called
     *  whenever redraw() is called and the isRedrawing flag is true.
     */
    public QTImageDrawer(Paintable paramPaintable, Dimension paramDimension, int paramInt) {
        this.mSize = paramDimension;
        this.painter = paramPaintable;
        this.image = new ImagePresenter(new QDRect(this.mSize));
        this.singleFrameFlag = paramInt;
        this.mRedrawFlag = (paramInt != 1);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates a QTImageDrawer that will grab the pixel data that results from drawing
     * the specified image at its full size. Only single frame java.awt.Images are supported.
     * @param image the java.awt.Image to capture. The size of this image must be known before
     *  this constructor can be used.
     * {@link quicktime.app.image.JImagePainter}
     */
    public QTImageDrawer(Image paramImage) throws QTAppException {
        int i = paramImage.getWidth(null);
        if (i == -1)
            throw new QTAppException("Image size must be known");
        int j = paramImage.getHeight(null);
        this.mSize = new Dimension(i, j);
        this.image = new ImagePresenter(new QDRect(this.mSize));
        this.painter = new JImagePainter(paramImage);
        this.singleFrameFlag = 1;
        this.mRedrawFlag = false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates a QTImageDrawer that will grab the pixel data that results from a scaled drawing
     * of the specified image at the specified size. Only single frame java.awt.Images are supported.
     * @param image the java.awt.Image to capture.
     * @param initialSize the scaled drawing size of the image.
     * {@link quicktime.app.image.JImagePainter}
     */
    public QTImageDrawer(Image paramImage, Dimension paramDimension) {
        this(new JImagePainter(paramImage), paramDimension, 1);
    }

    private boolean interruptedGrab = false;

    private Component canv;

    private boolean mRedrawFlag = false;

    private NotifyListener nl;

    private int singleFrameFlag;

    private RawEncodedImage rei;

    private IntEncodedImage iData;

    private TwoDSprite sprite;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Paintable object that is the client of the QTImageDrawer.
     * This call may return null if the QTImageDrawer is a singleFrame based
     * capture and the single frame has already been captured.
     * @return a Paintable
     */
    public Paintable getPaintable() {
        return this.painter;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method uses the supplied GraphicsExporter to export
     * the current image that is being presented by the QTImageDrawer
     * and its Paintable client. The GraphicsExporter's output target
     * must be set before this call is made.
     * @param exporter the GraphicsExporter that will be used to export the pixel image
     * @return the number of bytes that were written to the destination of the GraphicsExporter
     * @return {@link quicktime.QTException}
     */
    public int export(GraphicsExporter paramGraphicsExporter) throws QTException {
        QDGraphics localQDGraphics = new QDGraphics(getDescription(), 0);
        return export(paramGraphicsExporter, localQDGraphics);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method uses the supplied GraphicsExporter to export
     * the current image that is being presented by the QTImageDrawer
     * and its Paintable client. The GraphicsExporter's output target
     * must be set before this call is made.
     * @param exporter the GraphicsExporter that will be used to export the pixel image
     * @param offscreenGWorld the destination QDGraphics that is used to export (ie. the pixel image).
     * @return the number of bytes that were written to the destination of the GraphicsExporter
     * @return {@link quicktime.QTException}
     */
    public int export(GraphicsExporter paramGraphicsExporter, QDGraphics paramQDGraphics) throws QTException {
        if (this.canv == null) {
            throw new QTAppException("QTImageDrawer must be added to a QTCanvas before it can export");
        }
        if (!paramQDGraphics.isOffscreen()) {
            throw new QTAppException("QTImageDrawer.export must be supplied an offscreen QDGraphics");
        }
        QDGraphics localQDGraphics = getGWorld();
        setGWorld(paramQDGraphics);
        redraw(null);
        setGWorld(localQDGraphics);
        paramGraphicsExporter.setInputGWorld(paramQDGraphics);
        int i = paramGraphicsExporter.doExport();
        paramQDGraphics = null;
        return i;
    }

    /**
     * @deprecated
     */
    private synchronized void setSize(Dimension paramDimension) {
        this.mSize = paramDimension;
        if (this.canv == null) {
            return;
        }
        this.offscreenImage = this.canv.createImage(this.mSize.width, this.mSize.height);
        if (this.offscreenImage == null) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException("must have a valid offscreen image"), this.canv, "createImage");
            return;
        }
        if (this.singleFrameFlag == 6) {
            this.offscreenGraphics = this.offscreenImage.getGraphics();
        }
        try {
            ImageDescription localImageDescription = ImageDescription.getJavaDefaultPixelDescription(this.mSize.width, this.mSize.height);
            this.iData = new IntEncodedImage(this.mSize.width * this.mSize.height);
            localImageDescription.setDataSize(this.iData.getSize());
            this.image.setImageData(this.iData, localImageDescription);
            this.painter.newSizeNotified(this, this.mSize);
            if ((this.painter instanceof JImagePainter))
                ((JImagePainter) this.painter).prepareImage(this.canv);
            prepaint();
            if ((!this.interruptedGrab) && (isSingleFrame())) {
                doSingleFrame();
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "addedTo");
        }
    }

    private void doSingleFrame() throws QTException {
        this.rei = new RawEncodedImage(this.iData.getInts());
        this.image.setImageData(this.rei);
        this.iData = null;
        this.offscreenImage.flush();
        this.offscreenImage = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current size of the offscreen Image that the Painter object
     * will draw into.
     * @return the size
     */
    public Dimension getSize() {
        return new Dimension(this.mSize.width, this.mSize.height);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public final Region getClip() throws QTException {
        return this.image.getClip();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setClip(Region paramRegion) throws StdQTException {
        this.image.setClip(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return an ImagePresenter object from the drawing that has been done
     * by the Paintable object. This will copy the image data that has been created.
     * You will have to have attached the QTImageDrawer
     * object to a canvas and called redraw() before this will contain a valid image
     * due to the restriction in java.awt 1.1 that offscreen java Images cannot be created
     * unless the java.awt.Component is attached to a container with an existing
     * native graphics environment.
     * @return an ImagePresenter object of the drawn java image.
     * @return {@link quicktime.QTException}
     */
    public ImagePresenter toImagePresenter() throws QTException {
        if (this.iData == null) {
            if (this.rei == null)
                return null;
            if (isSingleFrame()) {
                return ImagePresenter.fromQTImage(this.rei, (ImageDescription) this.image.getDescription().clone());
            }
            return ImagePresenter.fromQTImage((EncodedImage) this.rei.clone(), (ImageDescription) this.image.getDescription().clone());
        }
        return ImagePresenter.fromQTImage(new RawEncodedImage(this.iData.getInts()), (ImageDescription) this.image.getDescription().clone());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getDescription() throws QTException {
        return this.image.getDescription();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data.
     * @return the image data
     * @return {@link quicktime.QTException}
     */
    public synchronized EncodedImage getImage() throws QTException {
        if (this.iData == null)
            return this.rei;
        return this.iData;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public final Matrix getMatrix() throws StdQTException {
        return this.image.getMatrix();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method assigns a mapping matrix to the sequence.
     * QuickTime::SetDSequenceMatrix
     * @param matrix a Matrix object that specifies how to transform the image during
     *                 decompression
     * @param {@link quicktime.QTException}
     */
    public final void setMatrix(Matrix paramMatrix) throws QTException {
        this.image.setMatrix(paramMatrix);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        return this.image.getInitialSize();
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @return {@link quicktime.QTException}
     * @see ()
     */
    public QDDimension getOriginalSize() throws QTException {
        return this.image.getOriginalSize();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     */
    public final QDGraphics getGWorld() {
        return this.image.getGWorld();
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
        this.image.setGWorld(paramQDGraphics);
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
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.image.setDisplayBounds(paramQDRect);
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
        return this.image.getDisplayBounds();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     *
     * This call will just redraw the current image. If you want to
     * have the Paintable client repaint itself then you must set the
     * redrawing flag to true.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @param {@link quicktime.QTException}
     * @see {@link quicktime.app.image.QTImageDrawer#setRedrawing(boolean)}
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if ((this.iData == null) && (this.rei == null))
            return;
        Region localRegion;
        if (this.interruptedGrab) {
            localRegion = prepaint();
            if ((localRegion != null) && (paramRegion != null))
                paramRegion = localRegion.union(paramRegion);
            if ((!this.interruptedGrab) && (isSingleFrame())) {
                doSingleFrame();
            }
        } else if ((!isSingleFrame()) && (isRedrawing())) {
            localRegion = prepaint();
            if ((localRegion != null) && (paramRegion != null))
                paramRegion = localRegion.union(paramRegion);
        }
        this.image.redraw(paramRegion);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If true then the redraw method of the object will fetch new image data
     * from its image source.
     * @param redrawFlag boolean determining whether the current image is redrawn (false)
     *  of fresh or updated image data is first retrieved (true).
     */
    public void setRedrawing(boolean paramBoolean) {
        this.mRedrawFlag = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the Redrawable only has a single frame to render and thus
     * will never need to re-acquire or redraw its image. Certain Redrawable objects
     * are able to optimise their resources knowing this constraint.
     * @return a boolean
     */
    public boolean isSingleFrame() {
        return this.singleFrameFlag == 1;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the image data may be altered or different upon different calls
     * of the redraw method of the QTDrawable interface
     */
    public boolean isRedrawing() {
        return this.mRedrawFlag;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is used to paint the painter object's image and to grab
     * the resulting pixels into a format that the QTImageDrawer can then
     * draw using the quicktime drawing commands. It returns a Region
     * that defins the areas of the pixel data that were changed from
     * the call. It will call the Paintable's paint method.
     * @return a Region or null if the QTImageDrawer does not yet have an
     *  offscreen java.awt.Image object to paint too.
     * @return {@link quicktime.QTException}
     */
    public Region prepaint() throws QTException {
        if (this.offscreenImage == null) {
            return null;
        }
        Region localRegion = null;
        try {
            if (this.offscreenGraphics == null) {
                this.offscreenGraphics = this.offscreenImage.getGraphics();
            }
            Rectangle[] arrayOfRectangle = this.painter.paint(this.offscreenGraphics);
            if (arrayOfRectangle != null) {
                for (int i = 0; i < arrayOfRectangle.length; i++) {
                    if (arrayOfRectangle[i] == null)
                        break;
                    int j = arrayOfRectangle[i].x;
                    int k = arrayOfRectangle[i].y;
                    int m = arrayOfRectangle[i].width > this.mSize.width ? this.mSize.width : arrayOfRectangle[i].width;
                    int n = arrayOfRectangle[i].height > this.mSize.height ? this.mSize.height : arrayOfRectangle[i].height;
                    PixelGrabber localPixelGrabber = new PixelGrabber(this.offscreenImage, j, k, m, n, this.iData.getInts(), k * this.mSize.width + j, this.mSize.width);
                    localPixelGrabber.grabPixels();
                    if (localRegion == null) {
                        localRegion = new Region(new QDRect(arrayOfRectangle[i].x, arrayOfRectangle[i].y, arrayOfRectangle[i].width, arrayOfRectangle[i].height));
                    } else
                        localRegion = localRegion.union(new QDRect(arrayOfRectangle[i].x, arrayOfRectangle[i].y, arrayOfRectangle[i].width, arrayOfRectangle[i].height));
                }
            }
        } catch (InterruptedException localInterruptedException) {
            this.interruptedGrab = true;
            if (this.singleFrameFlag == 2)
                this.offscreenGraphics = null;
            return null;
        }
        this.interruptedGrab = false;
        if ((this.sprite != null) && (!isSingleFrame()) && (this.sprite.isValid()))
            this.sprite.invalidate();
        if (this.singleFrameFlag == 2)
            this.offscreenGraphics = null;
        return localRegion;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This sets the GraphicsMode for the Compositor when it draws its composited
     * contents to its destination QDGraphics (typically the QTCanvas).
     * @param mode Specifies the graphics mode.
     * @param {@link quicktime.QTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        this.image.setGraphicsMode(paramGraphicsMode);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This gets the current GraphicsMode for the compositor.
     * @return a Graphics mode object
     */
    public GraphicsMode getGraphicsMode() {
        return this.image.getGraphicsMode();
    }

    class RedrawInvalidator extends Invalidator {

        RedrawInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (QTImageDrawer.this.iData != null) {
                if (QTImageDrawer.this.isRedrawing()) {
                    QTImageDrawer.this.redraw(null);
                } else
                    this.s.setImage(QTImageDrawer.this.iData);
            }
            return true;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an object that invalidates a sprite. When a DynamicImage
     * is a member of a SWompositor and the DynamicImage presents a
     * changing image (for example a movie), the TwoDSprite that presents
     * that image in the SWCompositor must be invalidated so that it will
     * redraw the Sprites image in the next composit cycle. The Invalidator
     * object is used to perform this function by the SWCompositor if the object
     * itself cannot perform the invalidation.
     * Can return null if the DynamicImage does not require the SWCompositor
     * to invalidate it (or the object is in a state where its image won't change).
     *
     * The Invalidators provided with objects that implement the DynamicImage
     * interface will execute at the scale and period of the Compositor
     * when they are created.
     *
     * This call will not return an Invalidator if the QTImageDrawer is presenting
     * a single frame image as the assumption is made that the image data is not changing.
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *  the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @see quicktime.app.display.SWCompositor
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        this.sprite = paramTwoDSprite;
        return isSingleFrame() ? null : new RedrawInvalidator(paramTwoDSprite);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) {
        this.sprite = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. If the object is a kind of
     * java.awt.Component then an offscreen image is created from this Component
     * and this Image is used as the pixel resource for a java.awt.Graphics object
     * that is passed to the attached Paintable object. If not nothing is done.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public synchronized void addedTo(Object paramObject) {
        if ((paramObject instanceof Component)) {
            if (this.canv != null)
                removedFrom(this.canv);
            this.canv = ((Component) paramObject);
            setSize(this.mSize);
            if (this.nl != null) {
                this.nl.notifyComplete();
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest. If the object is a kind of
     * java.awt.Component then the cached java.awt.Image object that was
     * created in the addedTo method is disposed of.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
        if ((paramObject instanceof Component)) {
            this.canv = null;
            this.offscreenImage = null;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This registers the deferred listener.
     * @param nl the NotifyListener that is interested in something from this Notifier
     * @return true if the Notifer is an appropriate Notifier for the Listener
     */
    public boolean addNotifyListener(NotifyListener paramNotifyListener) {
        this.nl = paramNotifyListener;
        if (!paramNotifyListener.setNotifier(this)) {
            paramNotifyListener = null;
            return false;
        }
        if (this.canv != null)
            paramNotifyListener.notifyComplete();
        return true;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTImageDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
