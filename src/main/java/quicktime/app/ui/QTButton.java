
package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.QTAppException;
import quicktime.app.event.MouseTargetListener;
import quicktime.app.event.QTActionEvent;
import quicktime.app.event.QTActionListener;
import quicktime.app.event.QTEventCaster;
import quicktime.app.event.QTMouseEvent;
import quicktime.app.image.ImageSpec;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.Matrix;


/**
 * @deprecated
 */
public abstract class QTButton extends UIElement {
    /**
     * @deprecated
     */
    protected ImageSpec releasedImage;

    protected ImageSpec rolloverImage;

    protected ImageSpec pressedImage;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected QTButton(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            ImageSpec paramImageSpec3,
            ImageSpec paramImageSpec4) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4, new Matrix(), 1, null);
    }

    /**
     * @deprecated
     */
    protected QTButton(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            ImageSpec paramImageSpec3,
            ImageSpec paramImageSpec4,
            Matrix paramMatrix,
            int paramInt,
            GraphicsMode paramGraphicsMode) throws QTException {
        super(paramImageSpec1, paramImageSpec3, paramMatrix, paramInt, paramGraphicsMode);

        if (paramImageSpec2 == null) {
            throw new QTAppException("QTButton:released and pressed images must be specified");
        }
        this.releasedImage = paramImageSpec1;
        this.pressedImage = paramImageSpec2;
        this.rolloverImage = paramImageSpec4;
    }

    protected QTActionListener actionListener = null;

    private boolean pressedFlag;

    private boolean outOfBounds;

    private MouseTargetListener mouseTargetListener;

    /**
     * @deprecated
     */
    public void released() throws QTException {
        this.pressedFlag = false;
        setImageOnRelease();
    }

    private void setImageOnRelease() throws QTException {
        if (this.outOfBounds) {
            setCurrentImage(this.releasedImage);
        } else if (this.rolloverImage != null) {
            setCurrentImage(this.rolloverImage);
        } else {
            setCurrentImage(this.releasedImage);
        }
    }

    /**
     * @deprecated
     */
    public void setReleasedImage(ImageSpec paramImageSpec) throws QTException {
        if (paramImageSpec == null) {
            throw new QTAppException("QTButton:released image cannot be null");
        }
        this.releasedImage = paramImageSpec;
        setImageOnRelease();
    }

    /**
     * @deprecated
     */
    public ImageSpec getReleasedImage() {
        return this.releasedImage;
    }

    /**
     * @deprecated
     */
    public void setRolloverImage(ImageSpec paramImageSpec) throws QTException {
        this.rolloverImage = paramImageSpec;
        if ((!this.pressedFlag) && (!this.outOfBounds) && (this.rolloverImage != null)) {
            setCurrentImage(this.rolloverImage);
        }
    }

    /**
     * @deprecated
     */
    public ImageSpec getRolloverImage() {
        return this.rolloverImage;
    }

    /**
     * @deprecated
     */
    public void setPressedImage(ImageSpec paramImageSpec) throws QTException {
        if (paramImageSpec == null) {
            throw new QTAppException("QTButton:pressed image cannot be null");
        }
        this.pressedImage = paramImageSpec;
        if ((this.pressedFlag) && (!this.outOfBounds)) {
            setCurrentImage(this.pressedImage);
        }
    }

    /**
     * @deprecated
     */
    public ImageSpec getPressedImage() {
        return this.pressedImage;
    }

    /**
     * @deprecated
     */
    public void setDeactiveImage(ImageSpec paramImageSpec) throws QTException {
        this.deactiveImage = paramImageSpec;
        if ((this.deactiveImage != null) && (!isActive())) {
            setCurrentImage(this.deactiveImage);
        }
    }

    /**
     * @deprecated
     */
    public ImageSpec getDeactiveImage() {
        return this.deactiveImage;
    }

    /**
     * @deprecated
     */
    public void pressed() throws QTException {
        this.pressedFlag = true;
        setCurrentImage(this.pressedImage);
    }

    /**
     * @deprecated
     */
    public boolean isPressed() {
        return this.pressedFlag;
    }

    /**
     * @deprecated
     */
    public boolean isMouseInButton() {
        return !this.outOfBounds;
    }

    /**
     * @deprecated
     */
    public void addActionListener(QTActionListener paramQTActionListener) {
        this.actionListener = QTEventCaster.add(this.actionListener, paramQTActionListener);
    }

    /**
     * @deprecated
     */
    public void removeActionListener(QTActionListener paramQTActionListener) {
        this.actionListener = QTEventCaster.remove(this.actionListener, paramQTActionListener);
    }

    /**
     * @deprecated
     */
    public void addMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        this.mouseTargetListener = QTEventCaster.add(this.mouseTargetListener, paramMouseTargetListener);
    }

    /**
     * @deprecated
     */
    public void removeMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        this.mouseTargetListener = QTEventCaster.remove(this.mouseTargetListener, paramMouseTargetListener);
    }

    /**
     * @deprecated
     */
    protected void entered(QTMouseEvent paramQTMouseEvent) throws QTException {
        this.outOfBounds = false;
        if (this.pressedFlag) {
            setCurrentImage(this.pressedImage);
        } else if (this.rolloverImage != null)
            setCurrentImage(this.rolloverImage);
        if (this.mouseTargetListener != null) {
            this.mouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void exited(QTMouseEvent paramQTMouseEvent) throws QTException {
        this.outOfBounds = true;
        setCurrentImage(this.releasedImage);
        if (this.mouseTargetListener != null) {
            this.mouseTargetListener.mouseTargetExited(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void fireAction() {
        if (this.actionListener != null) {
            this.actionListener.actionPerformed(new QTActionEvent(this, this.label));
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/ui/
 * QTButton.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
