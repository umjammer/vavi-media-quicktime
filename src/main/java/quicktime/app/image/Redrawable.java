package quicktime.app.image;

/**
 * Deprecated.
 * This interface is used to describe objects that have image data that should be
 * redrawn when a redraw call is issued. Typically if you have a static image then
 * there is no extra work that has to be done once you have the image data - it can
 * just be redrawn as is. However there are some image objects that may have image
 * data that could change and therefore those objects have potentially changing image data.
 * The two objects
 * that implement this interface currently are the QTEffect and the QTImageDrawer;
 * effects because they may or may not alter the pixel data depending on the type of
 * the effect and the QTImageDrawer depending on whether its Paintable client will
 * paint a changing image. This delineates the behaviour of the redraw method of
 * the Drawable interface.
 *
 * The set and is Redrawing methods are used to control the redrawing characteristics
 * of the Redrawable object. For instance you may have an object that is a QTTransition
 * and whilst you are transitioning this object you require that the redraw call redraws
 * the current frame of the transition. So in this case setRedrawing is set to true.
 * Once the transition is complete though and if the transition is now presenting a static
 * image there is not need to explicitly redraw the object.
 *
 * The isSingleFrame() method determines if an Invalidator is created when the Redrawable is added
 * to the Compositor - if the Redrawable is a single frame object then no Invalidator is created
 * as the Compositor assumes that the Redrawable is presenting a static image and thus
 * requires no explicit invalidation. The isSingleFrame() method however has no actual effect on whether
 * an object will redraw or not - just on the creation of the Invalidator.
 * @see {@link quicktime.app.image.QTEffect},
 * {@link quicktime.app.image.QTImageDrawer},
 * quicktime.app.display.SWCompositor
 */
public abstract interface Redrawable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * These flags are used in the construction of Redrawable to signify
     * that the redrawable is a single or multi-framed (dynamic) image source.
     * In some case certain optimizations are possible throguh knowing the Redrawable's
     * drawing requirements. These options are generally set at construction time
     * and the isSingleFrame method returns which option was set.
     */
    public static final int kSingleFrame = 1;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * These flags are used in the construction of Redrawable to signify
     * that the redrawable is a single or multi-framed (dynamic) image source.
     * In some case certain optimizations are possible throguh knowing the Redrawable's
     * drawing requirements. These options are generally set at construction time
     * and the isSingleFrame method returns which option was set.
     */
    public static final int kMultiFrame = 2;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If true then the redraw method of the object will fetch new image data
     * from its image source.
     * @param redrawFlag boolean determining whether the current image is redrawn (false)
     *  of fresh or updated image data is first retrieved (true).
     */
    public abstract void setRedrawing(boolean paramBoolean);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the image data may be altered or different upon different calls
     * of the redraw method of the QTDrawable interface.
     */
    public abstract boolean isRedrawing();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the Redrawable only has a single frame to render and thus
     * will never need to re-acquire or redraw its image. Certain Redrawable objects
     * are able to optimise their resources knowing this constraint.
     * @return a boolean
     */
    public abstract boolean isSingleFrame();
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Redrawable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
