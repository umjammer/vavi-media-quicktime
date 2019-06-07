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
 * Deprecated.
 * The QTEffectPresenter is used to present typically a QTFilter or QTTransition
 * as a member of a SWCompositor. It will create an Invalidator based on whether
 * the effect is a single or multi-framed effect and the Invalidator will
 * redraw the effect based on the effect's current isRedrawing value. If the
 * effect is redrawn then the TwoDSprite that is presenting the effect in the
 * SWCompositor will be invalidated to update the image it is presenting.
 * @see quicktime.app.display.SWCompositor
 */
public class QTEffectPresenter extends ImagePresenter implements DynamicImage, DrawingNotifier {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private Vector listenerList = new Vector();

    private QDGraphics g;

    private static QDRect moveToZero(QDRect paramQDRect) {
        paramQDRect.move(0, 0);
        return paramQDRect;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a QTEffectPresenter from the supplied effect. The effect
     * will be rendered into a default offscreen QDGraphics and the
     * pixel data of the QDGraphics buffer becomes the image data
     * that this presenter presents.
     * @param ef the effect that is rendered
     */
    public QTEffectPresenter(QTEffect paramQTEffect) throws QTException {
        this(paramQTEffect, new QDGraphics(32, paramQTEffect.getDisplayBounds(), 4096));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a QTEffectPresenter from the supplied effect. The effect
     * will be rendered into the supplied offscreen QDGraphics and the
     * pixel data of the QDGraphics buffer becomes the image data
     * that this presenter presents.
     * @param ef the effect that is rendered
     * @param g the offscreen QDGraphics that the effect is rendered into.
     */
    public QTEffectPresenter(QTEffect paramQTEffect, QDGraphics paramQDGraphics) throws QTException {
        super(moveToZero(paramQTEffect.getDisplayBounds()));
        this.ef = paramQTEffect;
        this.g = paramQDGraphics;
        paramQTEffect.setGWorld(paramQDGraphics);
        paramQTEffect.redraw(null);
        PixMap localPixMap = paramQDGraphics.getPixMap();
        setImageData(localPixMap.getPixelData(), new ImageDescription(localPixMap));
        if ((paramQTEffect instanceof QTTransition)) {
            ((QTTransition) paramQTEffect).thePresenter = this;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @param {@link quicktime.QTException}
     */
    public void redraw(Region paramRegion) throws QTException {
        if (this.ef.isRedrawing())
            this.ef.redraw(paramRegion);
        super.redraw(paramRegion);
    }

    class EFInvalidator extends Invalidator {

        QTEffectPresenter pres;

        EFInvalidator(TwoDSprite paramTwoDSprite, QTEffectPresenter paramQTEffectPresenter) {
            super(paramTwoDSprite);
            this.pres = paramQTEffectPresenter;
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (QTEffectPresenter.this.ef.isRedrawing()) {
                QTEffectPresenter.this.ef.redraw(null);
                synchronized (QTEffectPresenter.this) {
                    for (int i = 0; i < QTEffectPresenter.this.listenerList.size(); i++) ((DrawingListener) QTEffectPresenter.this.listenerList.elementAt(i)).drawingComplete(this.pres);
                }
                this.s.invalidate();
            }
            return true;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the effect that the QTEffectPresenter is presenting.
     * @return a QTEffect class
     */
    public QTEffect getEffect() {
        return this.ef;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the effect being presented by this Presenter is a QTTransition then
     * this call will run the transition. If not then nothing will happen.
     */
    public void doTransition() throws QTException {
        if ((this.ef instanceof QTTransition)) {
            ((QTTransition) this.ef).doTransition();
        }
    }

    private QTEffect ef;

    TwoDSprite sprite;

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
     * @see quicktime.app.display.SWCompositor
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        this.sprite = paramTwoDSprite;
        return this.ef.isSingleFrame() ? null : new EFInvalidator(paramTwoDSprite, this);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) {
        this.sprite = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Registers an object as a listener of the Compositor. The object will receive notification when the Compositor draws a frame
     */
    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList.contains(paramDrawingListener))
            return;
        this.listenerList.addElement(paramDrawingListener);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes an object that is a listener of the Compositor. The object will no longer receive notification when the Compositor draws a frame
     */
    public synchronized void removeDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList.contains(paramDrawingListener)) {
            this.listenerList.removeElement(paramDrawingListener);
        }
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTEffectPresenter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
