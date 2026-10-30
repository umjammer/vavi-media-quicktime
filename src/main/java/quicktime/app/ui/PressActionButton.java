package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;

/**
 * @deprecated
 * This class represents a normal button which fires the action event
 * when the button is pressed. It keeps firing till the button is released
 */
public class PressActionButton extends QTButton {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the current image of the QTButton and creates a TwoDSprite button.
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     */
    public PressActionButton(ImageSpec releasedImage, ImageSpec pressedImage, ImageSpec deactiveImage) throws QTException {
        this(releasedImage, pressedImage, deactiveImage, null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the current image of the QTButton and creates a TwoDSprite button.
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     * @param rolloverImage the image to be displayed when the button is released and in the mouse.
     */
    public PressActionButton(ImageSpec releasedImage, ImageSpec pressedImage, ImageSpec deactiveImage, ImageSpec rolloverImage) throws QTException {
        super(releasedImage, pressedImage, deactiveImage, rolloverImage);
    }

    private boolean doContinuousPress = true;

    /**
     * @deprecated
     * @since QTJava 6.1
     * If this is set to true, the button will continue to fire whilst
     * the user presses the mouse and it is also in the button.
     * If this is false, the action will fire ONLY when the user first presses the button.
     */
    public void setContinuousPressAction(boolean continuousPressAction) {
        this.doContinuousPress = continuousPressAction;
    }

    /**
     * @deprecated
     * @return a boolean * @deprecated since QTJava 6.1
     */
    public boolean isContinuousPressAction() {
        return this.doContinuousPress;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method. is called when the mouse button is pressed over the QTButton
     * it keeps firing as long as the mouse is pressed. This method is invoked by
     * the ButtonActivator.
     */
    @Override
    public void pressed() throws QTException {
        super.pressed();
        if (this.doContinuousPress) {
            new Thread(() -> {
                while (PressActionButton.this.isPressed()) {
                    if (PressActionButton.this.isMouseInButton())
                        PressActionButton.this.fireAction();
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            fireAction();
        }
    }
}
