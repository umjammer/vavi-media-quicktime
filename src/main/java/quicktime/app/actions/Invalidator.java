package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.time.Ticklish;

/**
 * Deprecated.
 * The Invalidator class is used as a helper class when Compositing objects
 * that have a changing image source.
 *
 * The SWCompositor uses QuickTime's SpriteWorld to draw a member's image.
 * If the member's image data is changing but there are no changes in the
 * presentation of the member's Sprite (a QuickTime Sprite is used to represent
 * the member in the SpriteWorld of the SWCompositor) then the compositing
 * cycle of the SpriteWorld will not redraw the Sprite. Thus in order to
 * force the SpriteWorld to redraw a Sprite of this nature, the Sprite that
 * is presenting the member must be invalidated.
 *
 * Some classes explicitly use the Invalidator class in this
 * release, the CompositableEffect and QTImageDrawer. Implicit invalidation
 * is done in other classes (MoviePresenter, the SWCompositor itself if it a
 * member of a parent SWCompositor) as they possess their own sense of drawing.
 *
 * If an Invalidator is required it is returned by the addedToCompositor method
 * and the SWCompositor keeps a list of the Invalidators and invokes the invalidator
 * each time the SWCompositor itself is tickled. Subclasses of the Invalidator
 * class can override the tickle method and be more efficient based on the explicit conditions
 * under which the TwoDSprite is invalidated. For example if a QTImageDrawer is not
 * redrawing every time the TwoDSprite needs only to be invalidated when
 * the QTImageDrawer signifies that it is redrawing. Thus the Invalidator for
 * the QTImageDrawer checks isRedrawing() and only redraws and invalidates the
 * presenting Sprite if the QTImageDrawer returns true from the isRedrawing() call.
 * It will still ask the QTImageDrawer if it requires redrawing upon each composit loop
 * when the Invalidator itself is activated.
 *
 * @see quicktime.app.display.SWCompositor,
 * {@link quicktime.app.image.QTImageDrawer},
 * {@link quicktime.app.players.MoviePresenter}
 */
public class Invalidator implements Ticklish {

    /**
     * Deprecated.
     * This is the sprite that requires invalidation in the tickle call * @deprecated since QTJava 6.1
     */
    protected TwoDSprite s;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Constructs an Invalidator with the TwoDSprite that is presenting the Image
     * and would require invalidation.
     * @param s the TwoDSprite presenter
     */
    public Invalidator(TwoDSprite paramTwoDSprite) {
        this.s = paramTwoDSprite;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns the target TwoDSprite of the Invalidator.
     * @return a TwoDSprite object
     */
    public TwoDSprite getTarget() {
        return this.s;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Invalidators typically ignore time and rate values and invalidate
     * their sprite. The default implementation will always invalidate the
     * sprite - subclasses can refine the actual conditions under which
     * invalidation is appropriate.
     * However an Invalidator subclass could of course use the time and rate values.
     * @param er the effective rate of the Ticklish's Timer
     * @param t the time at which the Ticklish has been tickled.
     * @return true if the Ticklish object should receive a consequent tickle, or false
     *  if the Ticklish object should not be tickled again.
     * @return {@link quicktime.QTException}
     */
    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        this.s.invalidate();
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This call is ignored by the default Invalidator.
     * @param newTime the new time when the Ticklish's time line has changed
     * @param {@link quicktime.QTException}
     */
    public void timeChanged(int paramInt) throws QTException {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/Invalidator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
