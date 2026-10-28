package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;

/**
 * @deprecated
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
     * @deprecated
     * @since QTJava 6.1
     * Sets the current image of the QTButton and creates a TwoDSprite button
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     */
    public PressReleaseButton(ImageSpec releasedImage, ImageSpec pressedImage, ImageSpec deactiveImage) throws QTException {
        this(releasedImage, pressedImage, deactiveImage, null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * sets the current image of the QTButton and creates a TwoDSprite button
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     * @param rolloverImage the image to be displayed when the button is released and in the mouse.
     */
    public PressReleaseButton(ImageSpec releasedImage, ImageSpec pressedImage, ImageSpec deactiveImage, ImageSpec rolloverImage) throws QTException {
        super(releasedImage, pressedImage, deactiveImage, rolloverImage);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is invoked by the button activator when the mouse is released
     */
    @Override
    public void released() throws QTException {
        super.released();
        if (isMouseInButton()) {
            fireAction();
        }
    }
}

