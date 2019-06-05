
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.time.Ticklish;


/**
 * @deprecated
 */
public class Invalidator implements Ticklish {
    protected TwoDSprite s;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public Invalidator(TwoDSprite paramTwoDSprite) {
        this.s = paramTwoDSprite;
    }

    /**
     * @deprecated
     */
    public TwoDSprite getTarget() {
        return this.s;
    }

    /**
     * @deprecated
     */
    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        this.s.invalidate();
        return true;
    }

    /**
     * @deprecated
     */
    public void timeChanged(int paramInt) throws QTException {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/Invalidator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
