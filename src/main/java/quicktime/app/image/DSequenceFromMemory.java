package quicktime.app.image;

import java.awt.Dimension;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.QTDrawable;
import quicktime.app.spaces.Sequencer;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.DSequence;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;

/**
 * Deprecated.
 * This class provides a mechanism for decompressing a sequence of images
 * which were previously compressed using the CSequenceToMemory class.
 *
 * The decompressed sequence is done with a number of default settings:
 * it uses the port given to it by the displayables parent,
 * it uses no matrix and no mask region,
 * it uses ditherCopy as its transfer mode and
 * it does not use an offscreen buffer or image buffer but writes directly to
 * its destination.
 * @see quicktime.app.image.CSequenceFromMemory
 */
public class DSequenceFromMemory extends Sequencer implements QTDrawable, DynamicImage {

    private DSequence seq;

    private Vector frames;

    private int initialNumElements;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static RawEncodedImage privateGetData(GraphicsImporter paramGraphicsImporter) throws QTException {
        int i = paramGraphicsImporter.getDataSize();
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(i, true);
        paramGraphicsImporter.readData(localRawEncodedImage, paramGraphicsImporter.getDataOffset(), i);
        return localRawEncodedImage;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will create an DSequence object from a single piece of data and its description.
     * It will NOT copy the incoming image, but will merely add them to the internal data storage.
     */
    public DSequenceFromMemory(GraphicsImporter paramGraphicsImporter) throws QTException {
        this(privateGetData(paramGraphicsImporter), paramGraphicsImporter.getImageDescription());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will create an DSequence object from a single piece of data and its description.
     * It will NOT copy the incoming image, but will merely add them to the internal data storage.
     * @param data the image data object.
     * @param description the ImageDescription object.
     */
    public DSequenceFromMemory(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        this.initialNumElements = 1;
        this.frames = new Vector(this.initialNumElements, this.initialNumElements);
        this.frames.addElement(paramEncodedImage);
        this.mDesc = paramImageDescription;
        this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Movie Toolbox handles the details of decompressing image sequences in
     * QuickTime movies. If you need to decompress other sequences which were
     * compressed via the CSequenceToMemory class, your application can create
     * an instance of this DSequenceFromMemory class.
     *
     * @param sequenceInMemory a previously created CSequenceToMemory object
     */
    public DSequenceFromMemory(CSequenceToMemory paramCSequenceToMemory) throws QTException {
        this.mDesc = paramCSequenceToMemory.getDescription();
        this.frames = paramCSequenceToMemory.getFrames();
        this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will create an DSequence object from any object that implements the
     * ImageSpec interface. It will NOT copy the incoming image, but will merely
     * add it to the internal data storage.
     * @param spec the ImageSpec object
     */
    public DSequenceFromMemory(ImageSpec paramImageSpec) throws QTException {
        if ((paramImageSpec instanceof ImageDataSequence)) {
            ImageDataSequence localImageDataSequence = (ImageDataSequence) paramImageSpec;
            this.initialNumElements = localImageDataSequence.size();
            this.frames = new Vector(this.initialNumElements, this.initialNumElements);
            for (int i = 1; i <= localImageDataSequence.size(); i++) {
                this.frames.addElement(localImageDataSequence.getImage(i));
            }
            this.mDesc = localImageDataSequence.getDescription();
            this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
        } else {
            this.initialNumElements = 1;
            this.frames = new Vector();
            this.frames.addElement(paramImageSpec.getImage());
            this.mDesc = paramImageSpec.getDescription();
            this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
        }
    }

    private int currentFrame = 1;

    private QDGraphics port = QDGraphics.scratch;

    private int mWidth;

    private int mHeight;

    private ImageDescription mDesc;

    private Matrix mat = new Matrix();

    private QDRect bnds;

    private Region clip = null;

    private GraphicsMode gMode;

    /**
     * Deprecated.
     * The flags that are used in the redraw call. * @deprecated since QTJava 6.1
     */
    public int flags = 0;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     */
    public QDGraphics getGWorld() {
        return this.port;
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
        this.port = paramQDGraphics;
        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            this.seq = new DSequence(this.mDesc, this.port, null, getMatrix(), null, this.flags, 768, CodecComponent.bestSpeedCodec);
            if (this.gMode != null)
                setGraphicsMode(this.gMode);
            if (this.clip != null)
                setClip(this.clip);
        } else {
            this.seq = null;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will set the client's current Display Matrix
     * @param matrix the new Matrix
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        if (this.seq != null)
            this.seq.setMatrix(paramMatrix);
        this.mat = paramMatrix;
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
        return this.seq != null ? this.seq.getMatrix() : this.mat;
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the runtime type is a java.awt.Dimension - the return value can be
     *  cast to this Class - however getOriginalSize should be used.
     * @return {@link quicktime.QTException}
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.mDesc.getWidth(), this.mDesc.getHeight());
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
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / this.mDesc.getWidth());
        localMatrix.setSy(paramQDRect.getHeight() / this.mDesc.getHeight());
        this.mWidth = paramQDRect.getWidth();
        this.mHeight = paramQDRect.getHeight();
        if (this.seq != null)
            this.seq.setMatrix(localMatrix);
        this.bnds = paramQDRect;
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
        this.bnds.setX(paramInt1);
        this.bnds.setY(paramInt2);
        setDisplayBounds(this.bnds);
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
        return this.bnds;
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
        if (this.seq == null)
            return;
        if (paramRegion != null) {
            this.seq.setMask(paramRegion);
            drawCurrentFrame(this.flags);
            this.seq.setMask(this.clip);
        } else {
            drawCurrentFrame(this.flags);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @return {@link quicktime.std.StdQTException}
     */
    public Region getClip() throws StdQTException {
        return this.clip;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClip(Region paramRegion) throws StdQTException {
        if (this.seq != null)
            this.seq.setMask(paramRegion);
        this.clip = paramRegion;
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
        return this.mDesc;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data of the current frame
     * or null if there is no image data to return.
     * @return the image data
     * @return {@link quicktime.std.StdQTException}
     */
    public EncodedImage getImage() throws StdQTException {
        if (this.frames != null) {
            return (EncodedImage) this.frames.elementAt(getCurrentFrame() - 1);
        }
        return null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the number of image data objects (frames) that the object contains.
     * @return the number of frames if the image is the sequence of frames
     */
    public int size() {
        return this.frames != null ? this.frames.size() : 0;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Your application calls this method to decompress the current frame.
     *
     * Note: See the QTImage class and Inside Mac QuickTime documentation for
     * more information on valid flag values.
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @return {@link quicktime.std.StdQTException}
     */
    public int drawCurrentFrame(int paramInt) throws StdQTException {
        if (this.seq == null)
            return 0;
        EncodedImage localEncodedImage = getImage();
        return this.seq.decompressFrameS(localEncodedImage, paramInt);
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
        if (this.seq != null)
            this.seq.setGraphicsMode(paramGraphicsMode);
        this.gMode = paramGraphicsMode;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This gets the current GraphicsMode for the compositor.
     * @return a Graphics mode object
     */
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
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
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *  the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @see quicktime.app.display.SWCompositor
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        return new SeqInvalidator(paramTwoDSprite, getCurrentFrame());
    }

    class SeqInvalidator extends Invalidator {

        private int lastFrame;

        SeqInvalidator(TwoDSprite paramTwoDSprite, int paramInt) {
            super(paramTwoDSprite);
            this.lastFrame = paramInt;
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (this.lastFrame != DSequenceFromMemory.this.getCurrentFrame()) {
                DSequenceFromMemory.this.redraw(null);
                this.s.invalidate();
                this.lastFrame = DSequenceFromMemory.this.getCurrentFrame();
            }
            return true;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the CSequence object - this may return null if you haven't called
     * created the sequence.
     * @return the sequence
     */
    public DSequence getSequence() {
        return this.seq;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return a string representation of this object
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + "]";
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

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /DSequenceFromMemory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
