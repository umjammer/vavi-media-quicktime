
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
     * @deprecated
     */
    public TwoDSprite(TwoDSpriteInfo paramTwoDSpriteInfo) throws QTException {
        this.info = paramTwoDSpriteInfo;
    }

    /**
     * @deprecated
     */
    public TwoDSprite() throws QTException {
        this(new Matrix(), 1, null);
    }

    /**
     * @deprecated
     */
    public TwoDSprite(Matrix paramMatrix, int paramInt, GraphicsMode paramGraphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(getDummyEI(), getDummyID(), paramMatrix, false, paramInt, paramGraphicsMode);
    }

    /**
     * @deprecated
     */
    public TwoDSprite(ImageSpec paramImageSpec, Matrix paramMatrix, boolean paramBoolean, int paramInt) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageSpec.getImage(),
                                       paramImageSpec.getDescription(),
                                       paramMatrix,
                                       paramBoolean,
                                       paramInt);
    }

    private transient Sprite sprite;

    /**
     * @deprecated
     */
    public TwoDSprite(ImageSpec paramImageSpec,
            Matrix paramMatrix,
            boolean paramBoolean,
            int paramInt,
            GraphicsMode paramGraphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageSpec.getImage(),
                                       paramImageSpec.getDescription(),
                                       paramMatrix,
                                       paramBoolean,
                                       paramInt,
                                       paramGraphicsMode);
    }

    /**
     * @deprecated
     */
    public TwoDSprite(ImageDataSequence paramImageDataSequence,
            int paramInt1,
            Matrix paramMatrix,
            boolean paramBoolean,
            int paramInt2) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageDataSequence.getImage(paramInt1),
                                       paramImageDataSequence.getDescription(),
                                       paramMatrix,
                                       paramBoolean,
                                       paramInt2);
    }

    private TwoDSpriteInfo info;

    private transient ImageSpec is;

    /**
     * @deprecated
     */
    public TwoDSprite(ImageDataSequence paramImageDataSequence,
            int paramInt1,
            Matrix paramMatrix,
            boolean paramBoolean,
            int paramInt2,
            GraphicsMode paramGraphicsMode) throws QTException {
        this.info = new TwoDSpriteInfo(paramImageDataSequence.getImage(paramInt1),
                                       paramImageDataSequence.getDescription(),
                                       paramMatrix,
                                       paramBoolean,
                                       paramInt2,
                                       paramGraphicsMode);
    }

    /**
     * @deprecated
     */
    TwoDSprite(Sprite paramSprite) throws QTException {
        this.sprite = paramSprite;
        this.info = new TwoDSpriteInfo(this.sprite.getImageData(),
                                       this.sprite.getImageDescription(),
                                       this.sprite.getMatrix(),
                                       this.sprite.getVisible(),
                                       this.sprite.getLayer());
    }

    transient SWCompositor spriteIsCompositor;

    private transient SWCompositor parentCompositor;

    private transient Timer parentsTimer;

    protected String label;

    /**
     * @deprecated
     */
    public void setLabel(String paramString) {
        this.label = paramString;
    }

    /**
     * @deprecated
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @deprecated
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
     * @deprecated
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
     * @deprecated
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
     * @deprecated
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
        if ((paramBoolean) && (this.parentsTimer != null) && (!this.parentsTimer.isTicking()) && (this.parentCompositor != null)
            && (this.parentCompositor.isAutoRedrawing())) {

            this.parentCompositor.idle();
        }
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public boolean isValid() {
        return this.sprite != null;
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public TwoDSpriteInfo getSpriteInfo() throws QTException {
        return this.info;
    }

    /**
     * @deprecated
     */
    protected void removeNotify() throws QTException {
        if (this.spriteIsCompositor != null)
            this.spriteIsCompositor.setGWorld(QDGraphics.scratch);
        this.sprite = null;
    }

    /**
     * @deprecated
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
        this.sprite = new Sprite(paramSpriteWorld,
                                 this.info.desc,
                                 this.info.image,
                                 this.info.matrix,
                                 this.info.visible,
                                 this.info.layer);

        this.sprite.setGraphicsMode(this.info.graphicsMode);

        if (this.spriteIsCompositor != null) {
            this.spriteIsCompositor.setGWorld(QDGraphics.validScratch);
        }
        doParentRedraw(getVisible());
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws QTException {
        if (this.sprite != null)
            return this.sprite.getMatrix();
        return this.info.matrix;
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.info.desc.getWidth(), this.info.desc.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.info.desc.getWidth(), this.info.desc.getHeight());
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public boolean hitTest(int paramInt, QDPoint paramQDPoint) throws QTException {
        if (this.sprite == null) {
            QTRuntimeException.handleOrThrow(new SpriteInvalidException(toString()), this, "hitTest");
            return false;
        }
        return this.sprite.hitTest(paramInt, paramQDPoint);
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        if (this.sprite != null)
            return this.sprite.getImageDescription();
        return this.info.desc;
    }

    /**
     * @deprecated
     */
    public void setDescription(ImageDescription paramImageDescription) throws QTException {
        if (this.sprite != null)
            this.sprite.setImageDescription(paramImageDescription);
        this.info.desc = paramImageDescription;
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() throws QTException {
        if (this.sprite != null)
            return this.sprite.getImageData();
        return this.info.image;
    }

    /**
     * @deprecated
     */
    public boolean getVisible() throws QTException {
        if (this.sprite != null)
            return this.sprite.getVisible();
        return this.info.visible;
    }

    /**
     * @deprecated
     */
    public void setVisible(boolean paramBoolean) throws QTException {
        if (this.sprite != null) {
            this.sprite.setVisible(paramBoolean);
            doParentRedraw(paramBoolean);
        }
        this.info.visible = paramBoolean;
    }

    /**
     * @deprecated
     */
    public int getLayer() throws QTException {
        if (this.sprite != null)
            return this.sprite.getLayer();
        return this.info.layer;
    }

    /**
     * @deprecated
     */
    public void setLayer(int paramInt) throws QTException {
        if (this.sprite != null) {
            this.sprite.setLayer(paramInt);
            doParentRedraw(getVisible());
        }
        this.info.layer = paramInt;
    }

    /**
     * @deprecated
     */
    public GraphicsMode getGraphicsMode() throws QTException {
        if (this.sprite != null)
            return this.sprite.getGraphicsMode();
        return this.info.graphicsMode;
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.sprite != null) {
            this.sprite.setGraphicsMode(paramGraphicsMode);
            doParentRedraw(getVisible());
        }
        this.info.graphicsMode = paramGraphicsMode;
    }

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
     * @deprecated
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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * TwoDSprite.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
