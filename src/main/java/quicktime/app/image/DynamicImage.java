package quicktime.app.image;

import quicktime.QTException;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;

/**
 * This class enables members of SWCompositors that present an image
 * which changes to explicitly invalidate the TwoDSprite that is their
 * presenter in the SWCompositor. This is a requirement of the SWCompositor
 * due to efficiencies in the composite cycle.
 * @see "quicktime.app.display.SWCompositor"
 * @deprecated
 */
public interface DynamicImage extends Compositable {

    /**
     * Returns an object that invalidates a sprite. When a DynamicImage
     * is a member of a SWompositor and the DynamicImage presents a
     * changing image (for example a movie), the TwoDSprite that presents
     * that image in the SWCompositor must be invalidated so that it will
     * redraw the Sprites image in the next composite cycle. The Invalidator
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
     * @throws quicktime.QTException
     * @see "quicktime.app.display.SWCompositor"
     * @deprecated
     * @since QTJava 6.1
     */
    Invalidator addedToCompositor(SWCompositor c, TwoDSprite s) throws QTException;

    /**
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    void removedFromCompositor(SWCompositor c) throws QTException;
}
