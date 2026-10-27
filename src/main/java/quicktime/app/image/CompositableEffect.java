package quicktime.app.image;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;

/**
 * @deprecated
 * This class is used to directly add an effect to a SWCompositor. Only effects
 * of zero source (such as fire, ripple) can be added directly. Filters and Transitions
 * act on one or two sources and must be added to a SWCompositor using the
 * QTEffectPresenter.
 *
 * @see quicktime.app.image.QTEffectPresenter
 */
public class CompositableEffect extends QTEffect implements DynamicImage, Redrawable {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a CompositableEffect.
     */
    public CompositableEffect() throws QTException {
        this(2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates a CompositableEffect. The specified multiFrameFlag parameter should
     * be set to Redrawable.kMultiFrame as all of the existing QTEffects require
     * redrawing (and thus Invalidation) for their visual effect to be apparent.
     * @param multiFrameFlag is normally set to kMultiFrame
     */
    public CompositableEffect(int multiFrameFlag) throws QTException {
        super(multiFrameFlag);
    }

    class CEInvalidator extends Invalidator {

        CEInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        @Override
        public boolean tickle(float er, int t) throws QTException {
            if (CompositableEffect.this.isRedrawing()) {
                CompositableEffect.this.redraw(null);
                this.s.invalidate();
            }
            return true;
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns an object that invalidates a sprite. When a DynamicImage
     * is a member of a SWompositor and the DynamicImage presents a
     * changing image (for example a movie), the TwoDSprite that presents
     * that image in the SWCompositor must be invalidated so that it will
     * redraw the Sprites image in the next composit cycle. The Invalidator
     * object is used to perform this function by the SWCompositor if the object
     * itself cannot perform the invalidation.
     * Can return null if the DynamicImage does not require the SWCompositor
     * to invalidate it (or the object is in a state where its image won't change).
     *
     * The Invalidators provided with objects that implement the DynamicImage
     * interface will execute at the scale and period of the Compositor
     * when they are created.
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *  the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @see quicktime.app.display.SWCompositor
     */
    @Override
    public Invalidator addedToCompositor(SWCompositor c, TwoDSprite s) {
        return isSingleFrame() ? null : new CEInvalidator(s);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     */
    @Override
    public void removedFromCompositor(SWCompositor c) {
    }
}
