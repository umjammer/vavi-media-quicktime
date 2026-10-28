package quicktime.app.anim;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
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
     * @deprecated
     * @since QTJava 6.1
     * Creates the exception.
     * @param s a message
     */
    public SpriteInvalidException(String s) {
        super(s);
    }
}
