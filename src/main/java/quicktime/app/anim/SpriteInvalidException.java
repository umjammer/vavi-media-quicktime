package quicktime.app.anim;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * Certain actions like invalidate can only be performed on a TwoDSprite
 * when the Sprite that it uses is an active member of its SpriteWorld.
 * If a method is called on the TwoDSprite when its Sprite is invalid
 * this runtime exception is thrown.
 */
public class SpriteInvalidException extends QTRuntimeException {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates the exception.
     * @param s a message
     */
    public SpriteInvalidException(String paramString) {
        super(paramString);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * SpriteInvalidException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
