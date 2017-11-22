
package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;


/**
 * @deprecated
 */
public class PressActionButton extends QTButton {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public PressActionButton(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            ImageSpec paramImageSpec3) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, null);
    }

    /**
     * @deprecated
     */
    public PressActionButton(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            ImageSpec paramImageSpec3,
            ImageSpec paramImageSpec4) throws QTException {
        super(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4);
    }

    private boolean doContinuousPress = true;

    /**
     * @deprecated
     */
    public void setContinuousPressAction(boolean paramBoolean) {
        this.doContinuousPress = paramBoolean;
    }

    public boolean isContinuousPressAction() {
        return this.doContinuousPress;
    }

    /**
     * @deprecated
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
