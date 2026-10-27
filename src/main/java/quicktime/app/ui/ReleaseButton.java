package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;

/**
 * @deprecated
 * This class represents a normal button which fires the action event
 * when the button is released.
 */
public class ReleaseButton extends QTButton {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * sets the current image of the QTButton and creates a TwoDSprite button
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     */
    public ReleaseButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, null);
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
    public ReleaseButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3, ImageSpec paramImageSpec4) throws QTException {
        super(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is invoked by the button responder when the mouse is released
     */
    @Override
    public void released() throws QTException {
        super.released();
        if (isMouseInButton()) {
            fireAction();
        }
    }
}

