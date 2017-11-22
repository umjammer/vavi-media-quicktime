
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
 */
public class UIElement extends TwoDSprite implements DynamicImage {
    protected ImageSpec deactiveImage;

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
     */
    protected UIElement(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            Matrix paramMatrix,
            int paramInt,
            GraphicsMode paramGraphicsMode) throws QTException {
        super(paramMatrix, paramInt, paramGraphicsMode);
        this.deactiveImage = paramImageSpec2;
        if (paramImageSpec1 == null)
            throw new QTAppException("QTButton:released and pressed images must be specified");
        setCurrentImage(paramImageSpec1);
    }

    private boolean active = true;

    /**
     * @deprecated
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
     */
    public QDRect getBounds() throws QTException {
        Matrix localMatrix = getMatrix();
        QDDimension localQDDimension = getOriginalSize();
        return new QDRect((int) localMatrix.getTx(),
                          (int) localMatrix.getTy(),
                          (int) (localMatrix.getSx() * localQDDimension.getWidth()),
                          (int) (localMatrix.getSy() * localQDDimension.getHeight()));
    }

    /**
     * @deprecated
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
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @deprecated
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException {
        this.c = paramSWCompositor;
        setCurrentImage(this.currentImage);
        return new UIInvalidator(this);
    }

    /**
     * @deprecated
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException {
        this.c = null;
        this.currentInvalidator = null;
    }

    class UIInvalidator extends Invalidator {
        UIInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (UIElement.this.currentInvalidator != null)
                return UIElement.this.currentInvalidator.tickle(paramFloat, paramInt);
            return true;
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/ui/
 * UIElement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
