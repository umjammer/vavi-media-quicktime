package quicktime.app.anim;

import java.awt.Dimension;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.QTAppException;
import quicktime.app.actions.Notifier;
import quicktime.app.actions.NotifyListener;
import quicktime.app.display.Layerable;
import quicktime.app.image.Compositable;
import quicktime.app.image.ImageDataSequence;
import quicktime.app.image.ImageSettable;
import quicktime.app.image.ImageSpec;
import quicktime.app.image.Transformable;
import quicktime.app.time.Timer;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.std.anim.Sprite;
import quicktime.std.anim.SpriteWorld;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;

/**
 * A TwoDSprite is a specialized presenter for image data within the context of a SpriteWorld.
 * It contains information also about the layer within the SpriteWorld that the image presented by the Sprite
 * is drawn. It also contains information about visibility and Matrix transformations and GraphicsMode
 * drawing transformations that are applied to the image data when the Sprite is drawn in the SpriteWorld.
 *
 * Typically, an application will provide the image data and presentation characteristics and the SWCompositor
 * assumes the responsibility of creating the actual TwoDSprite's Sprite that is used to present this data in the internal
 * SpriteWorld that the SWCompositor uses to render and construct its image.
 * The TwoDSprite uses the quicktime.std.anim.Sprite class to draw the image in a quicktime.std.anim.SpriteWorld.
 *
 * @see "quicktime.app.display.Compositor"
 * @see quicktime.app.anim.TwoDSpriteInfo
 * @deprecated
 */
public class TwoDSprite implements NotifyListener, Transformable, Layerable, ImageSettable, Compositable {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static EncodedImage ei = null;

    private static EncodedImage getDummyEI() throws QTException {
        if (ei == null)
            ei = new RawEncodedImage(4, true);
        return ei;
    }

    private static ImageDescription id = null;

    private static ImageDescription getDummyID() throws QTException {
        if (id == null) {
            if (QTSession.isCurrentOS(2)) {
                id = new ImageDescription(32);
                id.setDepth(0);
            } else {
                id = new ImageDescription(1918990112);
                id.setDepth(32);
            }
            id.setSpatialQuality(512);
            id.setWidth(1);
            id.setHeight(1);
            id.setHRes(72.0F);
            id.setVRes(72.0F);
            id.setDataSize(4);
            id.setFrameCount(1);
            id.setClutID(-1);
        }
        return id;
    }

    /**
     * Create a sprite when the final destination of the sprite is not known. This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param spriteInfo the image and layout information for the sprite when it is created.
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite(TwoDSpriteInfo spriteInfo) throws QTException {
        this.info = spriteInfo;
    }

    /**
     * Create a sprite that will use a hidden image, visibility set to false and an Identity matrix.
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite() throws QTException {
        this(new Matrix(), 1, null);
    }

    /**
     * Create a sprite that will use a hidden image and visibility set to false. This constructor
     * is used when the TwoDSprite is being used as a NotifyListener, expecting the image
     * data from a Notifier to arrive at some indeterminate time in the future. The arguments
     * supplied here will be used when the image data is supplied by the Notifier.
     * @param matrix the display matrix of the sprite
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @param graphicsMode the GraphicsMode that is applied to the rendering of the
     *  image data, this can be null
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite(Matrix matrix, int layer, GraphicsMode graphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(getDummyEI(), getDummyID(), matrix, false, layer, graphicsMode);
    }

    /**
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param image the image data and description that describes the image of the sprite
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite(ImageSpec image, Matrix matrix, boolean visibility, int layer) throws QTException {
        this.info = new TwoDSpriteInfo(image.getImage(), image.getDescription(), matrix, visibility, layer);
    }

    private transient Sprite sprite;

    /**
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param image the image data and description that describes the image of the sprite
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @param graphicsMode the GraphicsMode that is applied to the rendering of the
     *  image data, this can be null
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite(ImageSpec image, Matrix matrix, boolean visibility, int layer, GraphicsMode graphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(image.getImage(), image.getDescription(), matrix, visibility, layer, graphicsMode);
    }

    /**
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param images the image data and description that describes the images of the sprite
     * @param frame the frame number within the images that will be used as the current image for
     *  the sprite when it is created
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite(ImageDataSequence images, int frame, Matrix matrix, boolean visibility, int layer) throws QTException {
        this.info = new TwoDSpriteInfo(images.getImage(frame), images.getDescription(), matrix, visibility, layer);
    }

    private final TwoDSpriteInfo info;

    private transient ImageSpec is;

    /**
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param images the image data and description that describes the images of the sprite
     * @param frame the frame number within the images that will be used as the current image for
     *  the sprite when it is created
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @param graphicsMode the GraphicsMode that is applied to the rendering of the
     *  image data, this can be null
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSprite(ImageDataSequence images, int frame, Matrix matrix, boolean visibility, int layer, GraphicsMode graphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(images.getImage(frame), images.getDescription(), matrix, visibility, layer, graphicsMode);
    }

    /**
     * @deprecated
     */
    TwoDSprite(Sprite paramSprite) throws QTException {
        this.sprite = paramSprite;
        this.info = new TwoDSpriteInfo(this.sprite.getImageData(), this.sprite.getImageDescription(), this.sprite.getMatrix(), this.sprite.getVisible(), this.sprite.getLayer());
    }

    transient SWCompositor spriteIsCompositor;

    private transient SWCompositor parentCompositor;

    private transient Timer parentsTimer;

    /**
     * The actionCommand string that is presented to the QTActionListeners of the QTButton.* @deprecated since QTJava 6.1
     * @deprecated
     */
    protected String label;

    /**
     * You can set the label string for this TwoDSprite.
     * @param label a string that is associated with this sprite.
     * @deprecated
     * @since QTJava 6.1
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the label that is associated with this TwoDSprite.
     * @deprecated
     * @since QTJava 6.1
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * This registers the deferred listener.
     * @param notifier the Notifier that will notify the listener when it
     *  has reached the state that the listener expects.
     * @return true if the Notifier is an instanceof ImageSpec and thus is a valid Notifier
     *  for the TwoDSprite. Returns false if the Notifier is not an instanceof ImageSpec.
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public boolean setNotifier(Notifier notifier) {
        if ((notifier instanceof ImageSpec)) {
            this.is = ((ImageSpec) notifier);
            return true;
        }
        this.is = null;
        return false;
    }

    /**
     * This method is called by the Notifier object when the image data that it is
     * supplying to the TwoDSprite is valid.
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void notifyComplete() {
        if (this.is == null)
            return;
        try {
            synchronized (this.is) {
                setImageData(this.is.getImage(), this.is.getDescription());
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "setImageData");
        }
    }

    /**
     * This method can be used to change the image that is being presented by the Sprite
     * and to ensure that the sprite is visible, if the specified image data is not null.
     * This method is also called internally when the
     * Notifier of this class notifies the Sprite that the image data is valid.
     * If valid image data is provided this method also requires an ImageDescription that
     * describes this image data.
     * @param im the EncodedImage image data
     * @param id the ImageDescription that describes the format of the EncodedImage data.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setImageData(EncodedImage im, ImageDescription id) throws QTException {
        im = im == null ? getDummyEI() : im;
        id = id == null ? getDummyID() : id;
        this.info.desc = id;
        if (this.sprite != null) {
            this.sprite.setImageDescription(id);
            if ((im != getDummyEI()) && (this.info.image == getDummyEI())) {
                this.sprite.setGraphicsMode(this.info.graphicsMode);
                this.sprite.setVisible(true);
            }
            setImage(im);
        } else {
            setImage(im);
        }
    }

    /**
     * This method should make the object's current image the specified image.
     * @deprecated
     * @since QTJava 6.1
     */
    public void setImage(EncodedImage im) throws QTException {
        im = im != null ? im : getDummyEI();
        if (this.sprite != null) {
            boolean bool = this.sprite.setImageDataIfDifferent(im);
            if (im == getDummyEI())
                this.sprite.setVisible(false);
            if (bool)
                doParentRedraw(true);
        }
        this.info.image = im;
    }

    private void doParentRedraw(boolean paramBoolean) throws QTException {
        if ((paramBoolean) && (this.parentsTimer != null) && (!this.parentsTimer.isTicking()) && (this.parentCompositor != null) && (this.parentCompositor.isAutoRedrawing())) {
            this.parentCompositor.idle();
        }
    }

    /**
     * Return the sprite world that this sprite belongs too. If the TwoDSprite
     * is not currently attached to a SpriteWorld a SpriteInvalidException will
     * be thrown.
     * @return the current SpriteWorld of a valid TwoDSprite
     * @deprecated
     * @since QTJava 6.1
     */
    public SWCompositor getParent() {
        return this.parentCompositor;
    }

    void setParent(SWCompositor paramSWCompositor) {
        this.parentCompositor = paramSWCompositor;
        if (paramSWCompositor != null) {
            this.parentsTimer = paramSWCompositor.getTimer();
        } else {
            this.parentsTimer = null;
        }
    }

    /**
     * @deprecated
     */
    Sprite getSprite() {
        return this.sprite;
    }

    /**
     * Returns true if the Sprite being used by the TwoDSprite is valid and thus is constructed
     * and present in a SpriteWorld. Returns false if not.
     * @return a boolean
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean isValid() {
        return this.sprite != null;
    }

    /**
     * This will remove the sprite from the SpriteWorld it is contained in and destroy its
     * internal structures. The TwoDSprite must be reset after this call to recreate a
     * Sprite object to attach it to a sprite world.
     * @see #createSprite
     * @deprecated
     * @since QTJava 6.1
     */
    public void remove() throws QTException {
        if (this.sprite != null) {
            boolean bool = getVisible();
            this.sprite.remove();
            doParentRedraw(bool);
            this.sprite = null;
        }
    }

    /**
     * Returns all of the pertinent information about the current sprite's state to
     * recreate a sprite.
     * @return the TwoDSprite info object that contains all of the information that the Sprite
     *  is presenting.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public TwoDSpriteInfo getSpriteInfo() throws QTException {
        return this.info;
    }

    /**
     * This call should not be called by an application. It is used by the Compositor
     * to notify the Sprite that it is no longer valid - generally done when the Compositor
     * is recreating its internal SpriteWorld.
     * @deprecated
     * @since QTJava 6.1
     */
    protected void removeNotify() throws QTException {
        if (this.spriteIsCompositor != null)
            this.spriteIsCompositor.setGWorld(QDGraphics.scratch);
        this.sprite = null;
    }

    /**
     * This will create a sprite object that presents the specified image and display options
     * as specified in the contained info object.
     * @param sw the Sprite world that the Sprite will appear in
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public void createSprite(SpriteWorld sw) throws QTException {
        if (this.sprite != null)
            this.sprite.remove();
        if (this.info == null)
            throw new QTAppException("Can't create sprite with no information");
        if ((this.info.image == null) || (this.info.desc == null)) {
            this.info.image = getDummyEI();
            this.info.desc = getDummyID();
        }
        if ((this.info.image == getDummyEI()) || (this.info.desc == getDummyID()))
            this.info.visible = false;
        this.sprite = new Sprite(sw, this.info.desc, this.info.image, this.info.matrix, this.info.visible, this.info.layer);
        this.sprite.setGraphicsMode(this.info.graphicsMode);
        if (this.spriteIsCompositor != null) {
            this.spriteIsCompositor.setGWorld(QDGraphics.validScratch);
        }
        doParentRedraw(getVisible());
    }

    /**
     * This method returns the current matrix of the Transformable object.
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public Matrix getMatrix() throws QTException {
        if (this.sprite != null)
            return this.sprite.getMatrix();
        return this.info.matrix;
    }

    /**
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setMatrix(Matrix paramMatrix) throws QTException {
        if (this.sprite != null)
            this.sprite.setMatrix(paramMatrix);
        this.info.matrix = paramMatrix;
        if (this.spriteIsCompositor != null)
            this.spriteIsCompositor.setMatrix(paramMatrix);
        if (this.sprite != null) {
            doParentRedraw(getVisible());
        }
    }

    /**
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the runtime type is a java.awt.Dimension - the return value can be
     *  cast to this Class - however getOriginalSize should be used.
     * @throws quicktime.QTException
     * @see ()
     * @deprecated
     * @since QTJava 4.0
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.info.desc.getWidth(), this.info.desc.getHeight());
    }

    /**
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.info.desc.getWidth(), this.info.desc.getHeight());
    }

    /**
     * This method invalidates the portion of the sprite's sprite world which is occupied
     * by the sprite.
     * @deprecated
     * @since QTJava 6.1
     */
    public final void invalidate() throws QTException {
        if (this.sprite == null) {
            QTRuntimeException.handleOrThrow(new SpriteInvalidException(toString()), this, "invalidate");
            return;
        }
        this.sprite.invalidate();
        if (!(this.info.image instanceof RawEncodedImage))
            this.sprite.setImageData(this.info.image);
        doParentRedraw(getVisible());
    }

    /**
     * This method tests if a point is inside a sprite.
     * @param flags Specifies the control flags.
     * @param loc Specifies the test point.
     * @return true if the point is inside the sprite, false if not.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean hitTest(int flags, QDPoint loc) throws QTException {
        if (this.sprite == null) {
            QTRuntimeException.handleOrThrow(new SpriteInvalidException(toString()), this, "hitTest");
            return false;
        }
        return this.sprite.hitTest(flags, loc);
    }

    /**
     * This method gets the image description property of this sprite.
     * @return the image description.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public ImageDescription getDescription() throws QTException {
        if (this.sprite != null)
            return this.sprite.getImageDescription();
        return this.info.desc;
    }

    /**
     * This method sets the image description property of this sprite.
     * @param idh Specifies the image description.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public void setDescription(ImageDescription idh) throws QTException {
        if (this.sprite != null)
            this.sprite.setImageDescription(idh);
        this.info.desc = idh;
    }

    /**
     * This method returns the actual image data.
     * @return the image data
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public EncodedImage getImage() throws QTException {
        if (this.sprite != null)
            return this.sprite.getImageData();
        return this.info.image;
    }

    /**
     * This method gets the visibility property of this sprite.
     * @return the visibility.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean getVisible() throws QTException {
        if (this.sprite != null)
            return this.sprite.getVisible();
        return this.info.visible;
    }

    /**
     * This method sets the visibility property of this sprite.
     * @param visible Specifies the visibility.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    public void setVisible(boolean visible) throws QTException {
        if (this.sprite != null) {
            this.sprite.setVisible(visible);
            doParentRedraw(visible);
        }
        this.info.visible = visible;
    }

    /**
     * This method gets the layer property of this sprite.
     * @return the layer.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public int getLayer() throws QTException {
        if (this.sprite != null)
            return this.sprite.getLayer();
        return this.info.layer;
    }

    /**
     * This method sets the layer property of this sprite.
     * @param layer Specifies the layer.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setLayer(int layer) throws QTException {
        if (this.sprite != null) {
            this.sprite.setLayer(layer);
            doParentRedraw(getVisible());
        }
        this.info.layer = layer;
    }

    /**
     * This method gets the graphics mode property of this sprite.
     * @return the graphics mode.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public GraphicsMode getGraphicsMode() throws QTException {
        if (this.sprite != null)
            return this.sprite.getGraphicsMode();
        return this.info.graphicsMode;
    }

    /**
     * This method sets the graphics mode property of this sprite.
     * @param gMode Specifies the graphics mode.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void setGraphicsMode(GraphicsMode gMode) throws QTException {
        if (this.sprite != null) {
            this.sprite.setGraphicsMode(gMode);
            doParentRedraw(getVisible());
        }
        this.info.graphicsMode = gMode;
    }

    /**
     * Returns true if the TwoDSprite represents the same Sprite object
     * @since QTJava 6.1
     * @deprecated
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof TwoDSprite localTwoDSprite)) {
            if (localTwoDSprite.sprite == null) {
                return false;
            }
            return localTwoDSprite.sprite.equals(this.sprite);
        }
        return false;
    }

    /**
     * Returns a String representation of this class.
     * @return a String
     * @deprecated
     * @since QTJava 6.1
     */
    public String toString() {
        try {
            String str = "";
            if (this.label != null)
                str = "label=" + this.label;
            return getClass().getName() + "[" + str + "sprite= " + this.sprite + ",matrix=" + getMatrix() + "]";
        } catch (QTException _) {
        }
        return getClass().getName();
    }
}
