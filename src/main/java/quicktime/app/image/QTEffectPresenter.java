
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
 * @deprecated
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
     * @deprecated
     */
    public QTEffectPresenter(QTEffect paramQTEffect) throws QTException {
        this(paramQTEffect, new QDGraphics(32, paramQTEffect.getDisplayBounds(), 4096));
    }

    /**
     * @deprecated
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
     * @deprecated
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
                    for (int i = 0; i < QTEffectPresenter.this.listenerList.size(); i++)
                        ((DrawingListener) QTEffectPresenter.this.listenerList.elementAt(i)).drawingComplete(this.pres);
                }
                this.s.invalidate();
            }
            return true;
        }
    }

    /**
     * @deprecated
     */
    public QTEffect getEffect() {
        return this.ef;
    }

    /**
     * @deprecated
     */
    public void doTransition() throws QTException {
        if ((this.ef instanceof QTTransition)) {
            ((QTTransition) this.ef).doTransition();
        }
    }

    private QTEffect ef;

    TwoDSprite sprite;

    /**
     * @deprecated
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        this.sprite = paramTwoDSprite;
        return this.ef.isSingleFrame() ? null : new EFInvalidator(paramTwoDSprite, this);
    }

    /**
     * @deprecated
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) {
        this.sprite = null;
    }

    /**
     * @deprecated
     */
    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList.contains(paramDrawingListener))
            return;
        this.listenerList.addElement(paramDrawingListener);
    }

    /**
     * @deprecated
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
