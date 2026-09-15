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
 * Deprecated.
 * A TwoDSprite is a specialised presenter for image data within the context of a SpriteWorld.
 * It contains information also about the layer within the SpriteWorld that the image presented by the Sprite
 * is drawn. It also contains information about visibility and Matrix transformations and GraphicsMode
 * drawing transformations that are applied to the image data when the Sprite is drawn in the SpriteWorld.
 *
 * Typically an application will provide the image data and presentation characteristics and the SWCompositor
 * assumes the responsibility of creating the actual TwoDSprite's Sprite that is used to present this data in the internal
 * SpriteWorld that the SWCompositor uses to render and construct its image.
 * The TwoDSprite uses the quicktime.std.anim.Sprite class to draw the image in a quicktime.std.anim.SpriteWorld.
 *
 * @see quicktime.app.display.Compositor,
 * {@link quicktime.app.anim.TwoDSpriteInfo}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a sprite when the final destination of the sprite is not known. This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param spriteInfo the image and layout information for the sprite when it is created.
     */
    public TwoDSprite(TwoDSpriteInfo paramTwoDSpriteInfo) throws QTException {
        this.info = paramTwoDSpriteInfo;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a sprite that will use a hidden image, visibility set to false and an Identity matrix.
     */
    public TwoDSprite() throws QTException {
        this(new Matrix(), 1, null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a sprite that will use a hidden image and visibility set to false. This constructor
     * is used when the TwoDSprite is being used as a NotifyListener, expecting the image
     * data from a Notifier to arrive at some indeterminate time in the future. The arguments
     * supplied here will be used when the image data is supplied by the Notifier.
     * @param matrix the display matrix of the sprite
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @param graphicsMode the GraphicsMode that is applied to the rendering of the
     *  image data, this can be null
     */
    public TwoDSprite(Matrix paramMatrix, int paramInt, GraphicsMode paramGraphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(getDummyEI(), getDummyID(), paramMatrix, false, paramInt, paramGraphicsMode);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param image the image data and description that describes the image of the sprite
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     */
    public TwoDSprite(ImageSpec paramImageSpec, Matrix paramMatrix, boolean paramBoolean, int paramInt) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageSpec.getImage(), paramImageSpec.getDescription(), paramMatrix, paramBoolean, paramInt);
    }

    private transient Sprite sprite;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param image the image data and description that describes the image of the sprite
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     * @param graphicsMode the GraphicsMode that is applied to the rendering of the
     *  image data, this can be null
     */
    public TwoDSprite(ImageSpec paramImageSpec, Matrix paramMatrix, boolean paramBoolean, int paramInt, GraphicsMode paramGraphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageSpec.getImage(), paramImageSpec.getDescription(), paramMatrix, paramBoolean, paramInt, paramGraphicsMode);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This constructor
     * provides all of the state which will be used by the TwoDSprite object to create itself
     * when it is attached to a SpriteWorld.
     * @param images the image data and description that describes the images of the sprite
     * @param frame the frame number within the images that will be used as the current image for
     *  the sprite when it is created
     * @param matrix the display matrix of the sprite
     * @param visibility whether the sprite will be visible (true) or not (false) when created
     * @param layer the layer of the SpriteWorld where the Sprite will be drawn
     */
    public TwoDSprite(ImageDataSequence paramImageDataSequence, int paramInt1, Matrix paramMatrix, boolean paramBoolean, int paramInt2) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageDataSequence.getImage(paramInt1), paramImageDataSequence.getDescription(), paramMatrix, paramBoolean, paramInt2);
    }

    private TwoDSpriteInfo info;

    private transient ImageSpec is;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     */
    public TwoDSprite(ImageDataSequence paramImageDataSequence, int paramInt1, Matrix paramMatrix, boolean paramBoolean, int paramInt2, GraphicsMode paramGraphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageDataSequence.getImage(paramInt1), paramImageDataSequence.getDescription(), paramMatrix, paramBoolean, paramInt2, paramGraphicsMode);
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
     * Deprecated.
     * The actionCommand string that is presented to the QTActionListeners of the QTButton.* @deprecated since QTJava 6.1
     */
    protected String label;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * You can set the label string for this TwoDSprite.
     * @param label a string that is associated with this sprite.
     */
    public void setLabel(String paramString) {
        this.label = paramString;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the label that is associated with this TwoDSprite.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This registers the deferred listener.
     * @param notifier the Notifier that will notify the listener when it
     *  has reached the state that the listener expects.
     * @return true if the Notifier is an instanceof ImageSpec and thus is a valid Notifier
     *  for the TwoDSprite. Returns false if the Notifier is not an instanceof ImageSpec.
     */
    public boolean setNotifier(Notifier paramNotifier) {
        if ((paramNotifier instanceof ImageSpec)) {
            this.is = ((ImageSpec) paramNotifier);
            return true;
        }
        this.is = null;
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the Notifier object when the image data that it is
     * supplying to the TwoDSprite is valid.
     */
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method can be used to change the image that is being presented by the Sprite
     * and to ensure that the sprite is visible, if the specified image data is not null.
     * This method is also called internally when the
     * Notifier of this class notifies the Sprite that the image data is valid.
     * If valid image data is provided this method also requires an ImageDescription that
     * describes this image data.
     * @param im the EncodedImage image data
     * @param id the ImageDescription that describes the format of the EncodedImage data.
     * @param {@link quicktime.QTException}
     */
    public void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        paramEncodedImage = paramEncodedImage == null ? getDummyEI() : paramEncodedImage;
        paramImageDescription = paramImageDescription == null ? getDummyID() : paramImageDescription;
        this.info.desc = paramImageDescription;
        if (this.sprite != null) {
            this.sprite.setImageDescription(paramImageDescription);
            if ((paramEncodedImage != getDummyEI()) && (this.info.image == getDummyEI())) {
                this.sprite.setGraphicsMode(this.info.graphicsMode);
                this.sprite.setVisible(true);
            }
            setImage(paramEncodedImage);
        } else {
            setImage(paramEncodedImage);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should make the object's current image the specified image.
     */
    public void setImage(EncodedImage paramEncodedImage) throws QTException {
        paramEncodedImage = paramEncodedImage != null ? paramEncodedImage : getDummyEI();
        if (this.sprite != null) {
            boolean bool = this.sprite.setImageDataIfDifferent(paramEncodedImage);
            if (paramEncodedImage == getDummyEI())
                this.sprite.setVisible(false);
            if (bool)
                doParentRedraw(true);
        }
        this.info.image = paramEncodedImage;
    }

    private final void doParentRedraw(boolean paramBoolean) throws QTException {
        if ((paramBoolean) && (this.parentsTimer != null) && (!this.parentsTimer.isTicking()) && (this.parentCompositor != null) && (this.parentCompositor.isAutoRedrawing())) {
            this.parentCompositor.idle();
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the sprite world that this sprite belongs too. If the TwoDSprite
     * is not currently attached to a SpriteWorld a SpriteInvalidException will
     * be thrown.
     * @return the current SpriteWorld of a valid TwoDSprite
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the Sprite being used by the TwoDSprite is valid and thus is constructed
     * and present in a SpriteWorld. Returns false if not.
     * @return a boolean
     */
    public boolean isValid() {
        return this.sprite != null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will remove the sprite from the SpriteWorld it is contained in and destroy its
     * internal structures. The TwoDSprite must be reset after this call to recreate a
     * Sprite object to attach it to a sprite world.
     * @see createSprite
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns all of the pertinent information about the current sprite's state to
     * recreate a sprite.
     * @return the TwoDSprite info object that contains all of the information that the Sprite
     *  is presenting.
     * @return {@link quicktime.QTException}
     */
    public TwoDSpriteInfo getSpriteInfo() throws QTException {
        return this.info;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This call should not be called by an application. It is used by the Compositor
     * to notify the Sprite that it is no longer valid - generally done when the Compositor
     * is recreating its internal SpriteWorld.
     */
    protected void removeNotify() throws QTException {
        if (this.spriteIsCompositor != null)
            this.spriteIsCompositor.setGWorld(QDGraphics.scratch);
        this.sprite = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will create a sprite object that presents the specified image and display options
     * as specified in the contained info object.
     * @param sw the Sprite world that the Sprite will appear in
     * @param {@link quicktime.QTException}
     */
    public void createSprite(SpriteWorld paramSpriteWorld) throws QTException {
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
        this.sprite = new Sprite(paramSpriteWorld, this.info.desc, this.info.image, this.info.matrix, this.info.visible, this.info.layer);
        this.sprite.setGraphicsMode(this.info.graphicsMode);
        if (this.spriteIsCompositor != null) {
            this.spriteIsCompositor.setGWorld(QDGraphics.validScratch);
        }
        doParentRedraw(getVisible());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the current matrix of the Transformable object.
     */
    public Matrix getMatrix() throws QTException {
        if (this.sprite != null)
            return this.sprite.getMatrix();
        return this.info.matrix;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
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
        return new Dimension(this.info.desc.getWidth(), this.info.desc.getHeight());
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
        return new QDDimension(this.info.desc.getWidth(), this.info.desc.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method invalidates the portion of the sprite's sprite world which is occupied
     * by the sprite.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method tests if a point is inside a sprite.
     * @param flags Specifies the control flags.
     * @param loc Specifies the test point.
     * @return true if the point is inside the sprite, false if not.
     * @return {@link quicktime.QTException}
     */
    public boolean hitTest(int paramInt, QDPoint paramQDPoint) throws QTException {
        if (this.sprite == null) {
            QTRuntimeException.handleOrThrow(new SpriteInvalidException(toString()), this, "hitTest");
            return false;
        }
        return this.sprite.hitTest(paramInt, paramQDPoint);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the image description property of this sprite.
     * @return the image description.
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getDescription() throws QTException {
        if (this.sprite != null)
            return this.sprite.getImageDescription();
        return this.info.desc;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the image description property of this sprite.
     * @param idh Specifies the image description.
     * @param {@link quicktime.QTException}
     */
    public void setDescription(ImageDescription paramImageDescription) throws QTException {
        if (this.sprite != null)
            this.sprite.setImageDescription(paramImageDescription);
        this.info.desc = paramImageDescription;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data.
     * @return the image data
     * @return {@link quicktime.QTException}
     */
    public EncodedImage getImage() throws QTException {
        if (this.sprite != null)
            return this.sprite.getImageData();
        return this.info.image;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the visibility property of this sprite.
     * @return the visibility.
     * @return {@link quicktime.QTException}
     */
    public boolean getVisible() throws QTException {
        if (this.sprite != null)
            return this.sprite.getVisible();
        return this.info.visible;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the visibility property of this sprite.
     * @param visible Specifies the visibility.
     * @param {@link quicktime.QTException}
     */
    public void setVisible(boolean paramBoolean) throws QTException {
        if (this.sprite != null) {
            this.sprite.setVisible(paramBoolean);
            doParentRedraw(paramBoolean);
        }
        this.info.visible = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the layer property of this sprite.
     * @return the layer.
     * @return {@link quicktime.QTException}
     */
    public int getLayer() throws QTException {
        if (this.sprite != null)
            return this.sprite.getLayer();
        return this.info.layer;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the layer property of this sprite.
     * @param layer Specifies the layer.
     * @param {@link quicktime.QTException}
     */
    public void setLayer(int paramInt) throws QTException {
        if (this.sprite != null) {
            this.sprite.setLayer(paramInt);
            doParentRedraw(getVisible());
        }
        this.info.layer = paramInt;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method gets the graphics mode property of this sprite.
     * @return the graphics mode.
     * @return {@link quicktime.QTException}
     */
    public GraphicsMode getGraphicsMode() throws QTException {
        if (this.sprite != null)
            return this.sprite.getGraphicsMode();
        return this.info.graphicsMode;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the graphics mode property of this sprite.
     * @param gMode Specifies the graphics mode.
     * @param {@link quicktime.QTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.sprite != null) {
            this.sprite.setGraphicsMode(paramGraphicsMode);
            doParentRedraw(getVisible());
        }
        this.info.graphicsMode = paramGraphicsMode;
    }

    /**
     * Deprecated.
     * Returns true if the TwoDSprite represents the same Sprite object * @deprecated since QTJava 6.1
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof TwoDSprite)) {
            TwoDSprite localTwoDSprite = (TwoDSprite) paramObject;
            if (localTwoDSprite.sprite == null) {
                return false;
            }
            return localTwoDSprite.sprite.equals(this.sprite);
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a String representation of this class.
     * @return a String
     */
    public String toString() {
        try {
            String str = "";
            if (this.label != null)
                str = "label=" + this.label;
            return getClass().getName() + "[" + str + "sprite= " + this.sprite + ",matrix=" + getMatrix() + "]";
        } catch (QTException localQTException) {
        }
        return getClass().getName();
    }
}

