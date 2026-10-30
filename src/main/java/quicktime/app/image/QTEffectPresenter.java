package quicktime.app.image;

import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.image.ImageDescription;


/**
 * The QTEffectPresenter is used to present typically a QTFilter or QTTransition
 * as a member of a SWCompositor. It will create an Invalidator based on whether
 * the effect is a single or multi-framed effect and the Invalidator will
 * redraw the effect based on the effect's current isRedrawing value. If the
 * effect is redrawn then the TwoDSprite that is presenting the effect in the
 * SWCompositor will be invalidated to update the image it is presenting.
 *
 * @see "quicktime.app.display.SWCompositor"
 * @deprecated
 */
public class QTEffectPresenter extends ImagePresenter implements DynamicImage, DrawingNotifier {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private final Vector<DrawingListener> listenerList = new Vector<>();

    private final QDGraphics g;

    private static QDRect moveToZero(QDRect rect) {
        rect.move(0, 0);
        return rect;
    }

    /**
     * Create a QTEffectPresenter from the supplied effect. The effect
     * will be rendered into a default offscreen QDGraphics and the
     * pixel data of the QDGraphics buffer becomes the image data
     * that this presenter presents.
     *
     * @param ef the effect that is rendered
     * @since QTJava 6.1
     * @deprecated
     */
    public QTEffectPresenter(QTEffect ef) throws QTException {
        this(ef, new QDGraphics(32, ef.getDisplayBounds(), 4096));
    }

    /**
     * Create a QTEffectPresenter from the supplied effect. The effect
     * will be rendered into the supplied offscreen QDGraphics and the
     * pixel data of the QDGraphics buffer becomes the image data
     * that this presenter presents.
     *
     * @param ef the effect that is rendered
     * @param g  the offscreen QDGraphics that the effect is rendered into.
     * @since QTJava 6.1
     * @deprecated
     */
    public QTEffectPresenter(QTEffect ef, QDGraphics g) throws QTException {
        super(moveToZero(ef.getDisplayBounds()));
        this.ef = ef;
        this.g = g;
        ef.setGWorld(g);
        ef.redraw(null);
        PixMap pixmap = g.getPixMap();
        setImageData(pixmap.getPixelData(), new ImageDescription(pixmap));
        if ((ef instanceof QTTransition)) {
            ((QTTransition) ef).thePresenter = this;
        }
    }

    /**
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     *
     * @param invalidRgn the invalidRgn that the client should redraw
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void redraw(Region invalidRgn) throws QTException {
        if (this.ef.isRedrawing())
            this.ef.redraw(invalidRgn);
        super.redraw(invalidRgn);
    }

    class EFInvalidator extends Invalidator {

        final QTEffectPresenter pres;

        EFInvalidator(TwoDSprite s, QTEffectPresenter pres) {
            super(s);
            this.pres = pres;
        }

        @Override
        public boolean tickle(float er, int t) throws QTException {
            if (QTEffectPresenter.this.ef.isRedrawing()) {
                QTEffectPresenter.this.ef.redraw(null);
                synchronized (QTEffectPresenter.this) {
                    for (int i = 0; i < QTEffectPresenter.this.listenerList.size(); i++)
                        QTEffectPresenter.this.listenerList.elementAt(i).drawingComplete(this.pres);
                }
                this.s.invalidate();
            }
            return true;
        }
    }

    /**
     * Returns the effect that the QTEffectPresenter is presenting.
     *
     * @return a QTEffect class
     * @since QTJava 6.1
     * @deprecated
     */
    public QTEffect getEffect() {
        return this.ef;
    }

    /**
     * If the effect being presented by this Presenter is a QTTransition then
     * this call will run the transition. If not then nothing will happen.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public void doTransition() throws QTException {
        if ((this.ef instanceof QTTransition)) {
            ((QTTransition) this.ef).doTransition();
        }
    }

    private final QTEffect ef;

    TwoDSprite sprite;

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
     * <p>
     * The Invalidators provided with objects that implement the DynamicImage
     * interface will execute at the scale and period of the Compositor
     * when they are created.
     *
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *          the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @see "quicktime.app.display.SWCompositor"
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public Invalidator addedToCompositor(SWCompositor c, TwoDSprite s) {
        this.sprite = s;
        return this.ef.isSingleFrame() ? null : new EFInvalidator(s, this);
    }

    /**
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @deprecated
     * @param c the SWCompositor it is being removed from
     * @since QTJava 6.1
     */
    @Override
    public void removedFromCompositor(SWCompositor c) {
        this.sprite = null;
    }

    /**
     * Registers an object as a listener of the Compositor. The object will receive notification when the Compositor draws a frame
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public synchronized void addDrawingListener(DrawingListener drawingListener) {
        if (this.listenerList.contains(drawingListener))
            return;
        this.listenerList.addElement(drawingListener);
    }

    /**
     * Removes an object that is a listener of the Compositor. The object will no longer receive notification when the Compositor draws a frame
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public synchronized void removeDrawingListener(DrawingListener drawingListener) {
        if (this.listenerList.contains(drawingListener)) {
            this.listenerList.removeElement(drawingListener);
        }
    }
}
