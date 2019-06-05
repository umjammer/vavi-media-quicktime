
package quicktime.app.ui;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.image.ImageSpec;


/**
 * @deprecated
 */
public class PressReleaseButton extends PressActionButton {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public PressReleaseButton(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            ImageSpec paramImageSpec3) throws QTException {
        this(paramImageSpec1, paramImageSpec2, paramImageSpec3, null);
    }

    /**
     * @deprecated
     */
    public PressReleaseButton(ImageSpec paramImageSpec1,
            ImageSpec paramImageSpec2,
            ImageSpec paramImageSpec3,
            ImageSpec paramImageSpec4) throws QTException {
        super(paramImageSpec1, paramImageSpec2, paramImageSpec3, paramImageSpec4);
    }

    /**
     * @deprecated
     */
    public void released() throws QTException {
        super.released();
        if (isMouseInButton()) {
            fireAction();
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/ui/
 * PressReleaseButton.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
