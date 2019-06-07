package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;

/**
 * Deprecated.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the current image of the QTButton and creates a TwoDSprite button.
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     */
    public PressActionButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the current image of the QTButton and creates a TwoDSprite button.
     * @param releasedImage the image to be displayed when the mouse is not pressed
     * @param pressedImage the image to be displayed when the mouse is pressed
     * @param deactiveImage the image to be displayed when the button is deactive
     * @param rolloverImage the image to be displayed when the button is released and in the mouse.
     */
    public PressActionButton(ImageSpec paramImageSpec1, ImageSpec paramImageSpec2, ImageSpec paramImageSpec3, ImageSpec paramImageSpec4) throws QTException {
        super(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4);
    }

    private boolean doContinuousPress = true;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If this is set to true, the button will continue to fire whilst
     * the user presses the mouse and it is also in the button.
     * If this is false, the action will fire ONLY when the user first presses the button.
     */
    public void setContinuousPressAction(boolean paramBoolean) {
        this.doContinuousPress = paramBoolean;
    }

    /**
     * Deprecated.
     * @return a boolean * @deprecated since QTJava 6.1
     */
    public boolean isContinuousPressAction() {
        return this.doContinuousPress;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method. is called when the mouse button is pressed over the QTButton
     * it keeps firing as long as the mouse is pressed. This method is invoked by
     * the ButtonActivator.
     */
    public void pressed() throws QTException {
        super.pressed();
        if (this.doContinuousPress) {
            new Thread(new Runnable() {

                public void run() {
                    while (PressActionButton.this.isPressed()) {
                        if (PressActionButton.this.isMouseInButton())
                            PressActionButton.this.fireAction();
                        try {
                            Thread.sleep(1L);
                        } catch (InterruptedException localInterruptedException) {
                            localInterruptedException.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            fireAction();
        }
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/ui/
 * PressActionButton.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
