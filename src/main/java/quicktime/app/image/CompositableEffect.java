
package quicktime.app.image;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;


/**
 * @deprecated
 */
public class CompositableEffect extends QTEffect implements DynamicImage, Redrawable {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public CompositableEffect() throws QTException {
        this(2);
    }

    /**
     * @deprecated
     */
    public CompositableEffect(int paramInt) throws QTException {
        super(paramInt);
    }

    class CEInvalidator extends Invalidator {
        CEInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (CompositableEffect.this.isRedrawing()) {
                CompositableEffect.this.redraw(null);
                this.s.invalidate();
            }
            return true;
        }
    }

    /**
     * @deprecated
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        return isSingleFrame() ? null : new CEInvalidator(paramTwoDSprite);
    }

    /**
     * @deprecated
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) {
    }
}
