package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.QTAppException;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.image.DynamicImage;
import quicktime.app.image.ImageSpec;
import quicktime.qd.QDDimension;
import quicktime.qd.QDRect;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.Matrix;

/**
 * @deprecated
 * This class handles all the tasks of updating and redrawing the TwoDSprite. It takes care of
 * redrawing the Dynamic images that are in the sprite. This class also sets the size and location
 * of the TwoDSprite.
 */
public class UIElement extends TwoDSprite implements DynamicImage {

    /**
     * @deprecated
     * The image used when the UIElement is deactive* @deprecated since QTJava 6.1
     */
    protected ImageSpec deactiveImage;

    /**
     * @deprecated
     * The image used when the UIElement is active* @deprecated since QTJava 6.1
     */
    protected ImageSpec currentImage;

    SWCompositor c;

    Invalidator currentInvalidator;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructs a UIElement sprite.
     * @param currentImage this will be the currentImage that is presented by the UIElement. It may NOT be null
     * @param deactiveImage this is the image that will be used if the UIElement is deactiveated. It may be null
     * @param matrix describes the location and display characteristics of the UIElement
     * @param layer the back to front layer of the UIElement when added to the Compositor
     * @param mode if specified, a GraphicsMode that is used to render the UIElement
     */
    protected UIElement(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, Matrix paramMatrix, int paramInt, GraphicsMode paramGraphicsMode) throws QTException {
        super(paramMatrix, paramInt, paramGraphicsMode);
        this.deactiveImage = paramImageSpec2;
        if (paramImageSpec1 == null)
            throw new QTAppException("QTButton:released and pressed images must be specified");
        setCurrentImage(paramImageSpec1);
    }

    private boolean active = true;

    /**
     * @deprecated
     * @since QTJava 6.1
     * sets the x and y coordinates of the UIElement.
     * @param tx the x coordinate
     * @param ty the y coordinate
     * @throws quicktime.QTException
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        Matrix localMatrix = getMatrix();
        if (localMatrix != null) {
            localMatrix.setTx(paramInt1);
            localMatrix.setTy(paramInt2);
            setMatrix(localMatrix);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * sets the size of the UIElement
     * @param size the Dimension size in pixel values
     * @throws quicktime.QTException
     */
    public void setSize(QDDimension paramQDDimension) throws QTException {
        Matrix localMatrix1 = getMatrix();
        float f1 = localMatrix1.getTx();
        float f2 = localMatrix1.getTy();
        if (localMatrix1 != null) {
            Matrix localMatrix2 = new Matrix();
            QDDimension localQDDimension = getOriginalSize();
            localMatrix2.map(new QDRect(localQDDimension), new QDRect(paramQDDimension));
            localMatrix2.translate(f1, f2);
            setMatrix(localMatrix2);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the location and the size of the UIElement.
     * @throws quicktime.QTException
     */
    public QDRect getBounds() throws QTException {
        Matrix localMatrix = getMatrix();
        QDDimension localQDDimension = getOriginalSize();
        return new QDRect((int) localMatrix.getTx(), (int) localMatrix.getTy(), (int) (localMatrix.getSx() * localQDDimension.getWidth()), (int) (localMatrix.getSy() * localQDDimension.getHeight()));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the current image of the UIElement.
     * @param newCurrentImage the current image thats set to be displayed by the TwoDSprite. If null
     *  the UIElement is deactivated.
     * @throws quicktime.QTException
     */
    protected void setCurrentImage(ImageSpec paramImageSpec) throws QTException {
        if ((this.c != null) && ((this.currentImage instanceof DynamicImage))) {
            ((DynamicImage) this.currentImage).removedFromCompositor(this.c);
        }
        if (paramImageSpec == null) {
            setVisible(false);
        } else {
            setImageData(paramImageSpec.getImage(), paramImageSpec.getDescription());
            if ((this.c != null) && ((paramImageSpec instanceof DynamicImage)))
                this.currentInvalidator = ((DynamicImage) paramImageSpec).addedToCompositor(this.c, this);
            if (this.currentImage == null)
                setVisible(true);
        }
        this.currentImage = paramImageSpec;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the current state of the UIElement , it wont receive any events when set to false
     * @param active true will set the state to active
     * @throws quicktime.QTException
     */
    public void setActive(boolean paramBoolean) throws QTException {
        this.active = paramBoolean;
        if (paramBoolean) {
            setCurrentImage(this.currentImage);
        } else if (this.deactiveImage != null) {
            setCurrentImage(this.deactiveImage);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current state of the UIElement
     * @return true if actives
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
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
     * @throws quicktime.QTException
     * @see quicktime.app.anim.SWCompositor
     */
    @Override
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException {
        this.c = paramSWCompositor;
        setCurrentImage(this.currentImage);
        return new UIInvalidator(this);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     * @throws quicktime.QTException
     * @see quicktime.app.anim.SWCompositor
     */
    @Override
    public void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException {
        this.c = null;
        this.currentInvalidator = null;
    }

    class UIInvalidator extends Invalidator {

        UIInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        @Override
        public boolean tickle(float er, int t) throws QTException {
            if (UIElement.this.currentInvalidator != null)
                return UIElement.this.currentInvalidator.tickle(er, t);
            return true;
        }
    }
}

