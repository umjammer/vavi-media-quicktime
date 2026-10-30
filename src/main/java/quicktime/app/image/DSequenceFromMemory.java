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
 * This class provides a mechanism for decompressing a sequence of images
 * which were previously compressed using the CSequenceToMemory class.
 * <p>
 * The decompressed sequence is done with a number of default settings:
 * it uses the port given to it by the displayables parent,
 * it uses no matrix and no mask region,
 * it uses ditherCopy as its transfer mode and
 * it does not use an offscreen buffer or image buffer but writes directly to
 * its destination.
 *
 * @see "quicktime.app.image.CSequenceFromMemory"
 * @deprecated
 */
@Deprecated
public class DSequenceFromMemory extends Sequencer implements QTDrawable, DynamicImage {

    private DSequence seq;

    private final Vector<EncodedImage> frames;

    private int initialNumElements;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static RawEncodedImage privateGetData(GraphicsImporter graphicsImporter) throws QTException {
        int i = graphicsImporter.getDataSize();
        RawEncodedImage rawEncodedImage = new RawEncodedImage(i, true);
        graphicsImporter.readData(rawEncodedImage, graphicsImporter.getDataOffset(), i);
        return rawEncodedImage;
    }

    /**
     * This will create an DSequence object from a single piece of data and its description.
     * It will NOT copy the incoming image, but will merely add them to the internal data storage.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public DSequenceFromMemory(GraphicsImporter graphicsImporter) throws QTException {
        this(privateGetData(graphicsImporter), graphicsImporter.getImageDescription());
    }

    /**
     * This will create an DSequence object from a single piece of data and its description.
     * It will NOT copy the incoming image, but will merely add them to the internal data storage.
     *
     * @param data        the image data object.
     * @param description the ImageDescription object.
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public DSequenceFromMemory(EncodedImage data, ImageDescription description) throws QTException {
        this.initialNumElements = 1;
        this.frames = new Vector<>(this.initialNumElements, this.initialNumElements);
        this.frames.addElement(data);
        this.mDesc = description;
        this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * The Movie Toolbox handles the details of decompressing image sequences in
     * QuickTime movies. If you need to decompress other sequences which were
     * compressed via the CSequenceToMemory class, your application can create
     * an instance of this DSequenceFromMemory class.
     *
     * @param sequenceInMemory a previously created CSequenceToMemory object
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public DSequenceFromMemory(CSequenceToMemory sequenceInMemory) throws QTException {
        this.mDesc = sequenceInMemory.getDescription();
        this.frames = sequenceInMemory.getFrames();
        this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * This will create an DSequence object from any object that implements the
     * ImageSpec interface. It will NOT copy the incoming image, but will merely
     * add it to the internal data storage.
     *
     * @param spec the ImageSpec object
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public DSequenceFromMemory(ImageSpec spec) throws QTException {
        if ((spec instanceof ImageDataSequence imageDataSequence)) {
            this.initialNumElements = imageDataSequence.size();
            this.frames = new Vector<>(this.initialNumElements, this.initialNumElements);
            for (int i = 1; i <= imageDataSequence.size(); i++) {
                this.frames.addElement(imageDataSequence.getImage(i));
            }
            this.mDesc = imageDataSequence.getDescription();
            this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
        } else {
            this.initialNumElements = 1;
            this.frames = new Vector<>();
            this.frames.addElement(spec.getImage());
            this.mDesc = spec.getDescription();
            this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
        }
    }

    private final int currentFrame = 1;

    private QDGraphics port = QDGraphics.scratch;

    private int mWidth;

    private int mHeight;

    private final ImageDescription mDesc;

    private Matrix mat = new Matrix();

    private QDRect bnds;

    private Region clip = null;

    private GraphicsMode gMode;

    /**
     * The flags that are used in the redraw call. * @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public final int flags = 0;

    /**
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     *
     * @return a QDGraphics object or null
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public QDGraphics getGWorld() {
        return this.port;
    }

    /**
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void setGWorld(QDGraphics cgp) throws QTException {
        this.port = cgp;
        if (!QDGraphics.scratch.equals(cgp)) {
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
     * This will set the client's current Display Matrix
     *
     * @param matrix the new Matrix
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void setMatrix(Matrix matrix) throws StdQTException {
        if (this.seq != null)
            this.seq.setMatrix(matrix);
        this.mat = matrix;
    }

    /**
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public Matrix getMatrix() throws StdQTException {
        return this.seq != null ? this.seq.getMatrix() : this.mat;
    }

    /**
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     *
     * @return the runtime type is a java.awt.Dimension - the return value can be
     * cast to this Class - however getOriginalSize should be used.
     * @throws quicktime.QTException
     * @see ()
     * @since QTJava 4.0
     * @deprecated
     */
    @Deprecated
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     *
     * @return the original size
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void setDisplayBounds(QDRect bounds) throws StdQTException {
        Matrix matrix = new Matrix();
        matrix.setTx(bounds.getX());
        matrix.setTy(bounds.getY());
        matrix.setSx(bounds.getWidth() / this.mDesc.getWidth());
        matrix.setSy(bounds.getHeight() / this.mDesc.getHeight());
        this.mWidth = bounds.getWidth();
        this.mHeight = bounds.getHeight();
        if (this.seq != null)
            this.seq.setMatrix(matrix);
        this.bnds = bounds;
    }

    /**
     * A convenience method to set the position of a QTDrawable object.
     *
     * @param x the new x location
     * @param y the new y location
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void setLocation(int x, int y) throws QTException {
        this.bnds.setX(x);
        this.bnds.setY(y);
        setDisplayBounds(this.bnds);
    }

    /**
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the boundary of the client
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public QDRect getDisplayBounds() {
        return this.bnds;
    }

    /**
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     *
     * @param invalidRgn the invalidRgn that the client should redraw
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public final synchronized void redraw(Region invalidRgn) throws StdQTException {
        if (this.seq == null)
            return;
        if (invalidRgn != null) {
            this.seq.setMask(invalidRgn);
            drawCurrentFrame(this.flags);
            this.seq.setMask(this.clip);
        } else {
            drawCurrentFrame(this.flags);
        }
    }

    /**
     * Returns a region that defines the current clipping region for the object.
     *
     * @return the clipping region
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public Region getClip() throws StdQTException {
        return this.clip;
    }

    /**
     * Sets a region that will define the clipping region for the object
     *
     * @param reg the new clipping region
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void setClip(Region reg) throws StdQTException {
        if (this.seq != null)
            this.seq.setMask(reg);
        this.clip = reg;
    }

    /**
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     *
     * @return the ImageDescription of an Image
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public ImageDescription getDescription() throws QTException {
        return this.mDesc;
    }

    /**
     * This method returns the actual image data of the current frame
     * or null if there is no image data to return.
     *
     * @return the image data
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public EncodedImage getImage() throws StdQTException {
        if (this.frames != null) {
            return this.frames.elementAt(getCurrentFrame() - 1);
        }
        return null;
    }

    /**
     * Return the number of image data objects (frames) that the object contains.
     *
     * @return the number of frames if the image is the sequence of frames
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public int size() {
        return this.frames != null ? this.frames.size() : 0;
    }

    /**
     * Your application calls this method to decompress the current frame.
     * <p>
     * Note: See the QTImage class and Inside Mac QuickTime documentation for
     * more information on valid flag values.
     *
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public int drawCurrentFrame(int inFlags) throws StdQTException {
        if (this.seq == null)
            return 0;
        EncodedImage data = getImage();
        return this.seq.decompressFrameS(data, inFlags);
    }

    /**
     * This sets the GraphicsMode for the Compositor when it draws its composited
     * contents to its destination QDGraphics (typically the QTCanvas).
     *
     * @param mode Specifies the graphics mode.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void setGraphicsMode(GraphicsMode mode) throws QTException {
        if (this.seq != null)
            this.seq.setGraphicsMode(mode);
        this.gMode = mode;
    }

    /**
     * This gets the current GraphicsMode for the compositor.
     *
     * @return a Graphics mode object
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
    }

    /**
     * Returns an object that invalidates a sprite. When a DynamicImage
     * is a member of a SWompositor and the DynamicImage presents a
     * changing image (for example a movie), the TwoDSprite that presents
     * that image in the SWCompositor must be invalidated so that it will
     * redraw the Sprites image in the next composit cycle. The Invalidator
     * object is used to perform this function by the SWCompositor if the object
     * itself cannot perform the invalidation.
     * Can return null if the DynamicImage does not require the SWCompositor
     * to invalidate it (or the object is in a state where its image won't change).
     * <p>
     * The Invalidators provided with objects that implement the DynamicImage
     * interface will execute at the scale and period of the Compositor
     * when they are created.
     *
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *          the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @see "quicktime.app.display.SWCompositor"
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public Invalidator addedToCompositor(SWCompositor c, TwoDSprite s) {
        return new SeqInvalidator(s, getCurrentFrame());
    }

    class SeqInvalidator extends Invalidator {

        private int lastFrame;

        SeqInvalidator(TwoDSprite s, int lastFrame) {
            super(s);
            this.lastFrame = lastFrame;
        }

        @Override
        public boolean tickle(float er, int t) throws QTException {
            if (this.lastFrame != DSequenceFromMemory.this.getCurrentFrame()) {
                DSequenceFromMemory.this.redraw(null);
                this.s.invalidate();
                this.lastFrame = DSequenceFromMemory.this.getCurrentFrame();
            }
            return true;
        }
    }

    /**
     * Returns the CSequence object - this may return null if you haven't called
     * created the sequence.
     *
     * @return the sequence
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public DSequence getSequence() {
        return this.seq;
    }

    /**
     * Return a string representation of this object
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public String toString() {
        return getClass().getName() + "[size=" + size() + "]";
    }

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     *
     * @param interest the object that is to be the source of interest for the
     *                 the object that implements this interface.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void addedTo(Object interest) {
    }

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     *
     * @param interest the object that was the source of interest for the
     *                 the object that implements this interface.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void removedFrom(Object interest) {
    }

    /**
     * @param c the SWCompositor it is being removed from
     * @since QTJava 6.1
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @deprecated
     */
    @Override
    @Deprecated
    public void removedFromCompositor(SWCompositor c) {
    }
}
