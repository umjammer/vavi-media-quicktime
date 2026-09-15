package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;

/**
 * Deprecated.
 * This class represents a button which fires the action event
 * when the button is pressed and when it is released.
 * The user can specify optionally whether the button will also fire
 * the action whilst the button is still pressed.
 * Released actions will only be fired if the mouse is release whilst in the button.
 */
public class PressReleaseButton extends PressActionButton {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the current image of the QTButton and creates a TwoDSprite button
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     */
    public PressReleaseButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * sets the current image of the QTButton and creates a TwoDSprite button
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     * @param rolloverImage the image to be displayed when the button is released and in the mouse.
     */
    public PressReleaseButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3, ImageSpec paramImageSpec4) throws QTException {
        super(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is invoked by the button activator when the mouse is released
     */
    public void released() throws QTException {
        super.released();
        if (isMouseInButton()) {
            fireAction();
        }
    }
}

