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
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.ComponentDescription;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.image.DSequence;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ICMFrameTime;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.EncodedImage;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;

/**
 * @deprecated
 * QTEffect forms the base class for visual effects that can be applied to images.
 * Depending on the effect itself, visual effects can be applied over a length of time, or once only. They can be
 * applied to no source images, a single image or multiple images.
 *
 * The QTEffect class itself forms a base class that given a rectangular region will apply some visual effect (like fire/ripples).
 * to its destination graphics.
 * There are two subclasses - QTFilter which is an effect that is applied to a single source image and QTTransition
 * which is an effect that is applied to two images, transitioning from the source image to the destination image.
 */
public class QTEffect implements QTDrawable, Redrawable {

    ImageDescription effectDesc;

    QDRect mDisplayBounds;

    QDDimension initSize;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTEffect with kMultiFrame set with a default initial size of 0, 0.
     */
    public QTEffect() throws QTException {
        this(2, new QDDimension());
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * Creates a QTEffect with kMultiFrame set and the specified initial size.
     * @param d the initialSize of the effect.
     */
    public QTEffect(Dimension d) throws QTException {
        this(2, new QDDimension(d.width, d.height));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTEffect with kMultiFrame set with a default initial size of 0, 0.
     * @param multiFrameFlag is normally set to kMultiFrame. The setting of this flag
     *  will also set the initial setting of the redrawing flag. QTEffects
     *  require redrawing so the default value is kMultiFrame. Typically the QTFilters do
     *  not require redrawing so their default value is kSingleFrame. QTTransitions are
     *  multiframes and so do require redrawing.
     */
    public QTEffect(int multiFrameFlag) throws QTException {
        this(multiFrameFlag, new QDDimension());
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * Creates a QTEffect. The specified multiFrameFlag parameter should
     * be set to Redrawable.kMultiFrame as all of the existing QTEffects require
     * redrawing for their visual effect to be apparent.
     * @param multiFrameFlag is normally set to kMultiFrame. The setting of this flag
     *  will also set the initial setting of the redrawing flag. QTEffects
     *  require redrawing so the default value is kMultiFrame. Typically the QTFilters do
     *  not require redrawing so their default value is kSingleFrame. QTTransitions are
     *  multiframes and so do require redrawing.
     * @param d the initialSize of the effect.
     * ()
     */
    public QTEffect(int multiFrameFlag, Dimension d) throws QTException {
        this(multiFrameFlag, new QDDimension(d.width, d.height));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a QTEffect. The specified multiFrameFlag parameter should
     * be set to Redrawable.kMultiFrame as all of the existing QTEffects require
     * redrawing for their visual effect to be apparent.
     * @param multiFrameFlag is normally set to kMultiFrame. The setting of this flag
     *  will also set the initial setting of the redrawing flag. QTEffects
     *  require redrawing so the default value is kMultiFrame. Typically the QTFilters do
     *  not require redrawing so their default value is kSingleFrame. QTTransitions are
     *  multiframes and so do require redrawing.
     * @param d the initialSize of the effect.
     */
    public QTEffect(int multiFrameFlag, QDDimension d) throws QTException {
        this.mRedrawFlag = (multiFrameFlag == 2);
        this.singleFrameFlag = multiFrameFlag;
        this.effectDesc = new ImageDescription(0);
        this.effectDesc.setTemporalQuality(512);
        this.effectDesc.setSpatialQuality(512);
        this.effectDesc.setHRes(72.0F);
        this.effectDesc.setVRes(72.0F);
        this.effectDesc.setDepth(24);
        this.effectDesc.setFrameCount(1);
        this.effectDesc.setClutID(-1);
        this.mat = new Matrix();
        if ((d != null) && (d.getWidth() != 0) && (d.getHeight() != 0)) {
            setDisplayBounds(new QDRect(d));
        }
        this.initSize = d;
    }

    private QDGraphics mDestPort = QDGraphics.scratch;

    int mNumberOfFrames = 1;

    AtomContainer effectSample;

    RawEncodedImage effectSamplePtr;

    private Region clip;

    TimeBase tb;

    DSequence effectSequence;

    Matrix mat;

    GraphicsMode gMode;

    private quicktime.app.time.TaskThread tt;

    final ICMFrameTime ft = new ICMFrameTime();

    int currentFrame = 1;

    private boolean mRedrawFlag;

    private final int singleFrameFlag;

    boolean needsRedraw = false;

    /**
     * @deprecated
     * @since QTJava 6.1
     * If true then the current frame of the effect is drawn on redraw.
     * If false then the effect is only applied when you explicitly apply it
     * For an effect or a filter the default setting for this flag is true - the effect
     * is redrawn on redraw. For a Transition the default setting for this flag
     * is false - the source of the effect only is redrawn.
     * @param redrawFlag boolean indicating redraw behaviour
     */
    @Override
    public void setRedrawing(boolean redrawFlag) {
        this.mRedrawFlag = redrawFlag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the state of the redraw flag
     * @return boolean
     */
    @Override
    public boolean isRedrawing() {
        return this.mRedrawFlag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the Redrawable only has a single frame to render and thus
     * will never need to re-acquire or redraw its image. Certain Redrawable objects
     * are able to optimise their resources knowing this constraint.
     * @return a boolean
     */
    @Override
    public boolean isSingleFrame() {
        return this.singleFrameFlag == 1;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This sets the GraphicsMode for the Compositor when it draws its composited
     * contents to its destination QDGraphics (typically the QTCanvas).
     */
    public void setGraphicsMode(GraphicsMode gMode) throws QTException {
        if (this.effectSequence != null)
            this.effectSequence.setGraphicsMode(gMode);
        this.gMode = gMode;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This gets the current GraphicsMode for the QTEffect.
     */
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will set the clipping of the effect - it defines the region of the source image
     * that the effect will be applied to.
     * @param r the clipping region
     * @throws quicktime.std.StdQTException
     */
    @Override
    public void setClip(Region r) throws StdQTException {
        this.clip = r;
        if (this.effectSequence != null)
            this.effectSequence.setMask(this.clip);
    }

    /**
     * @deprecated
     * Returns the current clipping region of the effect or null if the effect is not clipped * @deprecated since QTJava 6.1
     * @return the clipping region
     */
    @Override
    public Region getClip() {
        return this.clip;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will return a copy of the AtomContainer that contains the
     * current effect settings. You can alter the values of this container but you
     * must call setEffect for those new values to be used by the effect.
     */
    public AtomContainer getEffect() throws StdQTException {
        return this.effectSample.copyAtom(Atom.kParentIsContainer);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will take the incoming parameters and use this as the control parameters for the
     * effect. The container will be locked for the duration of the effect object's life or until
     * another effect is set.
     * @param effectParams the parameters that determine the effect that is run
     * @throws quicktime.QTException
     */
    public void setEffect(AtomContainer effectParams) throws QTException {
        if (effectParams == null) {
            this.effectSamplePtr = null;
            if (this.effectSample != null)
                this.effectSample.unlock();
            this.effectSample = null;
            return;
        }
        this.effectSample = effectParams;
        Atom localAtom = this.effectSample.findChildByID_Atom(Atom.kParentIsContainer, 2003329396, 1);
        this.effectSample.lock();
        AtomData localAtomData = this.effectSample.getAtomData(localAtom);
        this.effectDesc.setCType(EndianOrder.flipBigEndianToNative32(localAtomData.getInt(0)));
        this.effectSamplePtr = RawEncodedImage.fromQTPointer(this.effectSample.toQTPointer());
        setUpSequence();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by a number of other methods to create
     * the DSequence that is used to render the QTEffect.
     * When called the method will create a new DSequence if the required
     * state has been established to enable the creation of the DSequence.
     * @return true if a new DSequence was created, otherwise false.
     * @throws quicktime.QTException
     */
    protected synchronized boolean setUpSequence() throws QTException {
        QDRect localQDRect = getDisplayBounds();
        if ((QDGraphics.scratch.equals(getGWorld())) || (this.effectSamplePtr == null) || (localQDRect == null)) {
            return false;
        }
        this.mat.setTx(localQDRect.getX());
        this.mat.setTy(localQDRect.getY());
        this.effectSequence = new DSequence(this.effectDesc, this.effectSamplePtr, getGWorld(), null, this.mat, getClip(), 0, 512, null);
        if (this.gMode != null) {
            this.effectSequence.setGraphicsMode(this.gMode);
        }
        this.tb = new TimeBase();
        this.tb.setRate(0.0F);
        this.effectSequence.setTimeBase(this.tb);
        return true;
    }

    /**
     * @deprecated
     * Returns the current maximum number of frames that an effect will run for * @deprecated since QTJava 6.1
     */
    public int getFrames() {
        return this.mNumberOfFrames;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This returns the current frame that the effect is rendering.
     * @return the current frame
     */
    public int getCurrentFrame() {
        return this.currentFrame;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This will make sure the component (effect) is there and can set its parameter list for display.
     * If the effect is not present then an exception with a paramErr will be thrown
     * @param effectType the effect type to check for
     * @throws quicktime.QTException
     */
    public void checkForEffect(int effectType) throws QTException {
        ComponentDescription localComponentDescription = new ComponentDescription();
        localComponentDescription.setType(QTUtils.toOSType("imdc"));
        localComponentDescription.setSubType(effectType);
        ComponentIdentifier localComponentIdentifier = ComponentIdentifier.find(localComponentDescription);
        if (localComponentIdentifier == null) {
            throw new StdQTException(-50);
        }
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
    public synchronized void setGWorld(QDGraphics cgp) throws QTException {
        this.mDestPort = cgp;
        if (QDGraphics.scratch.equals(cgp)) {
            this.effectSequence = null;
        } else {
            setUpSequence();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     * @throws quicktime.QTException
     */
    @Override
    public synchronized QDGraphics getGWorld() throws QTException {
        return this.mDestPort;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @throws quicktime.QTException
     */
    @Override
    public synchronized void setDisplayBounds(QDRect bounds) throws QTException {
        this.mDisplayBounds = bounds;
        this.effectDesc.setHeight(bounds.getHeight());
        this.effectDesc.setWidth(bounds.getWidth());
        this.mat.setTx(bounds.getX());
        this.mat.setTy(bounds.getY());
        setUpSequence();
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
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the display boundary of the client
     * @throws quicktime.QTException
     */
    @Override
    public QDRect getDisplayBounds() throws QTException {
        return this.mDisplayBounds == null ? new QDRect(this.initSize.getWidth(), this.initSize.getHeight()) : this.mDisplayBounds;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the DSequence that is used by the QTEffect. This
     * object may be null if the QTEffect is not currently attached to
     * a valid destination QDGraphics. The application should not cache this
     * sequence object for any length of time - as if the destination QDGraphics
     * is changed the DSequence is thrown away and a new one is created.
     */
    public DSequence getDSequence() {
        return this.effectSequence;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the actual image data - in this case the effect's atom container.
     * @return the image data
     */
    public EncodedImage getImage() {
        return this.effectSamplePtr;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @throws quicktime.QTException
     */
    public ImageDescription getDescription() throws QTException {
        return this.effectDesc;
    }

    /**
     * @deprecated
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension localQDDimension = getOriginalSize();
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return a Dimension
     * @throws quicktime.QTException
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        return this.initSize.copy();
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
        return this.mat.copy();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    @Override
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mat = paramMatrix.copy();
        if (this.effectSequence != null) {
            this.effectSequence.setMatrix(this.mat);
        }
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
    public synchronized void redraw(Region invalidRgn) throws QTException {
        if (this.effectSequence != null) {
            this.tb.setValue(this.currentFrame, this.mNumberOfFrames);
            this.ft.setValue(this.currentFrame);
            if (invalidRgn != null) {
                if (getClip() == null) {
                    this.effectSequence.setMask(invalidRgn);
                } else
                    this.effectSequence.invalidate(invalidRgn);
            }
            this.effectSequence.decompressFrameWhen(this.effectSamplePtr, 0, this.ft);
            if ((invalidRgn != null) && (getClip() == null)) {
                this.effectSequence.setMask(getClip());
            }
        }
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
