package quicktime.app.image;

import quicktime.QTException;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;

/**
 * Deprecated.
 * This class enables members of SWCompositors that present an image
 * which changes to explicitly invalidate the TwoDSprite that is their
 * presenter in the SWCompositor. This is a requirement of the SWCompositor
 * due to efficiencies in the composit cycle.
 * @see quicktime.app.display.SWCompositor
 */
public abstract interface DynamicImage extends Compositable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     * @return {@link quicktime.QTException}
     * @see quicktime.app.display.SWCompositor
     */
    public abstract Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     * @param {@link quicktime.QTException}
     */
    public abstract void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /DynamicImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
