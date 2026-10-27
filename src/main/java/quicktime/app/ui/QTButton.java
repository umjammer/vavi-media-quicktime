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
 * This abstarct class provides the basic functionality for a QTButton which takes
 * care of the event handling of the button. It also updates the button
 * images and sets the current image of the button as the mouse enters and
 * leaves the button. Other classes can extend this class to provide their
 * own button behaviour. The QTButton will fire action events as defined by these subclasses.
 * The label of the TwoDSprite will be passed on to the action listeners.
 */
public abstract class QTButton extends UIElement {

    /**
     * @deprecated
     * @since QTJava 6.1
     * The image presented by the QTButton when it is in a released state.
     * If a rollover image is specified, then this is presented when the mouse is
     * outside the button, and the rolloverImage is presented when the mouse is inside the button.
     */
    protected ImageSpec releasedImage;

    /**
     * @deprecated
     * If specified then the image presented by the QTButton when it is in a released state and the mouse is over the button.* @deprecated since QTJava 6.1
     */
    protected ImageSpec rolloverImage;

    /**
     * @deprecated
     * The image presented by the QTButton when it is in a pressed state.* @deprecated since QTJava 6.1
     */
    protected ImageSpec pressedImage;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the images of the UIElement as specified.
     * The default location is 0,0, the default layer is 1 and there is no
     * graphics mode assigned to the QTButton.
     * @param releasedImage will be the currentImage of the UIElement
     * @param pressedImage will be the image shown when the QTButton is pressed
     * @param deactiveImage will be the deactiveImage of the UIElement
     * @param rolloverImage will be the releasedImage of the UIElement when the mouse is over the button
     */
    protected QTButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3, ImageSpec paramImageSpec4) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4, new Matrix(), 1, null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the images and display characteristics of the UIElement as specified.
     * @param releasedImage will be the currentImage of the UIElement
     * @param pressedImage will be the image shown when the QTButton is pressed
     * @param deactiveImage will be the deactiveImage of the UIElement
     * @param rolloverImage will be the releasedImage of the UIElement when the mouse is over the button
     * @param matrix the display matrix properties of the UIElement
     * @param layer the layer in which the UIElement will be presented
     * @param mode the GraphicsMode of the UIElement.
     */
    protected QTButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3, ImageSpec paramImageSpec4, Matrix paramMatrix, int paramInt, GraphicsMode paramGraphicsMode) throws QTException {
        super(paramImageSpec1, paramImageSpec3, paramMatrix, paramInt, paramGraphicsMode);
        if (paramImageSpec2 == null) {
            throw new QTAppException("QTButton:released and pressed images must be specified");
        }
        this.releasedImage = paramImageSpec1;
        this.pressedImage = paramImageSpec2;
        this.rolloverImage = paramImageSpec4;
    }

    /**
     * @deprecated
     * The QTActionListeners that will be fired when the QTButton is activated.* @deprecated since QTJava 6.1
     */
    protected QTActionListener actionListener = null;

    private boolean pressedFlag;

    private boolean outOfBounds;

    private MouseTargetListener mouseTargetListener;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Subclass provides the implementation for this method, though the subclass should ensure
     * that super.released() is called. This method is called when the mouse button is
     * released on the button, or the mouse leaves a pressed button.
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
     * @since QTJava 6.1
     * Sets a new image for the released state of the button.
     * @param image the new released image
     * @throws quicktime.QTException
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
     * @since QTJava 6.1
     * @return the current image that is being used for the released state of the button
     */
    public ImageSpec getReleasedImage() {
        return this.releasedImage;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets a new image for the rollover state of the button.
     * @param image the new rollover image
     * @throws quicktime.QTException
     */
    public void setRolloverImage(ImageSpec paramImageSpec) throws QTException {
        this.rolloverImage = paramImageSpec;
        if ((!this.pressedFlag) && (!this.outOfBounds) && (this.rolloverImage != null)) {
            setCurrentImage(this.rolloverImage);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the current image that is being used for the rollover state of the button
     */
    public ImageSpec getRolloverImage() {
        return this.rolloverImage;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets a new image for the pressed state of the button.
     * @param image the new pressed image
     * @throws quicktime.QTException
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
     * @since QTJava 6.1
     * @return the current image that is being used for the pressed state of the button
     */
    public ImageSpec getPressedImage() {
        return this.pressedImage;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets a new image for the deactive state of the button.
     * @param image the new deactive image
     * @throws quicktime.QTException
     */
    public void setDeactiveImage(ImageSpec paramImageSpec) throws QTException {
        this.deactiveImage = paramImageSpec;
        if ((this.deactiveImage != null) && (!isActive())) {
            setCurrentImage(this.deactiveImage);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the current image that is being used for the deactive state of the button
     */
    public ImageSpec getDeactiveImage() {
        return this.deactiveImage;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Subclass provides the implementation for this method, though the subclass should ensure
     * that super.pressed() is called. This method is called when the mouse button is
     * pressed on the button and the mouse is in the buttton when pressed.
     */
    public void pressed() throws QTException {
        this.pressedFlag = true;
        setCurrentImage(this.pressedImage);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean isPressed() {
        return this.pressedFlag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean isMouseInButton() {
        return !this.outOfBounds;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Add an QTActionListeners to this button.
     * @param l a new ActionListener
     */
    public void addActionListener(QTActionListener paramQTActionListener) {
        this.actionListener = QTEventCaster.add(this.actionListener, paramQTActionListener);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove an ActionListener from this button.
     * @param l a new QTActionListeners
     */
    public void removeActionListener(QTActionListener paramQTActionListener) {
        this.actionListener = QTEventCaster.remove(this.actionListener, paramQTActionListener);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Add a MouseTargetListener for this QTButton. This listener
     * will fire when the mouse enters or exits the button.
     * @param l a new MouseTargetListener
     */
    public void addMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        this.mouseTargetListener = QTEventCaster.add(this.mouseTargetListener, paramMouseTargetListener);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove a MouseTargetListener for this QTButton.
     * @param l a MouseTargetListener to remove
     */
    public void removeMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        this.mouseTargetListener = QTEventCaster.remove(this.mouseTargetListener, paramMouseTargetListener);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the ButtonActivator when the mouse enters the button
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
     * @since QTJava 6.1
     * This method is called by the ButtonActivator when the mouse exits the button
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
     * @since QTJava 6.1
     * This method is invoked by the event handling code so that any QTActionListeners
     * actionPerformed methods are invoked.
     */
    protected void fireAction() {
        if (this.actionListener != null) {
            this.actionListener.actionPerformed(new QTActionEvent(this, this.label));
        }
    }
}

