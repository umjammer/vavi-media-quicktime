
package quicktime.app.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.PixelGrabber;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.QTAppException;
import quicktime.app.actions.Invalidator;
import quicktime.app.actions.Notifier;
import quicktime.app.actions.NotifyListener;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.QTDrawable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class QTImageDrawer implements Notifier, QTDrawable, DynamicImage, Redrawable {
    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static final boolean isWin = QTSession.isCurrentOS(2);

    /**
     * @deprecated
     */
    public static final int kMultiFrameRetain = 6;

    private ImagePresenter image;

    private Dimension mSize;

    private Image offscreenImage;

    private Image jPainterImage;

    private Graphics offscreenGraphics;

    private Paintable painter;

    /**
     * @deprecated
     */
    public QTImageDrawer(Paintable paramPaintable, Dimension paramDimension, int paramInt) {
        this.mSize = paramDimension;
        this.painter = paramPaintable;
        this.image = new ImagePresenter(new QDRect(this.mSize));
        this.singleFrameFlag = paramInt;
        this.mRedrawFlag = (paramInt != 1);
    }

    /**
     * @deprecated
     */
    public QTImageDrawer(Image paramImage) throws QTAppException {
        int i = paramImage.getWidth(null);
        if (i == -1)
            throw new QTAppException("Image size must be known");
        int j = paramImage.getHeight(null);

        this.mSize = new Dimension(i, j);
        this.image = new ImagePresenter(new QDRect(this.mSize));
        this.painter = new JImagePainter(paramImage);
        this.singleFrameFlag = 1;
        this.mRedrawFlag = false;
    }

    /**
     * @deprecated
     */
    public QTImageDrawer(Image paramImage, Dimension paramDimension) {
        this(new JImagePainter(paramImage), paramDimension, 1);
    }

    private boolean interruptedGrab = false;

    private Component canv;

    private boolean mRedrawFlag = false;

    private NotifyListener nl;

    private int singleFrameFlag;

    private RawEncodedImage rei;

    private IntEncodedImage iData;

    private TwoDSprite sprite;

    /**
     * @deprecated
     */
    public Paintable getPaintable() {
        return this.painter;
    }

    /**
     * @deprecated
     */
    public int export(GraphicsExporter paramGraphicsExporter) throws QTException {
        QDGraphics localQDGraphics = new QDGraphics(getDescription(), 0);

        return export(paramGraphicsExporter, localQDGraphics);
    }

    /**
     * @deprecated
     */
    public int export(GraphicsExporter paramGraphicsExporter, QDGraphics paramQDGraphics) throws QTException {
        if (this.canv == null) {
            throw new QTAppException("QTImageDrawer must be added to a QTCanvas before it can export");
        }
        if (!paramQDGraphics.isOffscreen()) {
            throw new QTAppException("QTImageDrawer.export must be supplied an offscreen QDGraphics");
        }

        QDGraphics localQDGraphics = getGWorld();

        setGWorld(paramQDGraphics);
        redraw(null);

        setGWorld(localQDGraphics);

        paramGraphicsExporter.setInputGWorld(paramQDGraphics);

        int i = paramGraphicsExporter.doExport();

        paramQDGraphics = null;
        return i;
    }

    /**
     * @deprecated
     */
    private synchronized void setSize(Dimension paramDimension) {
        this.mSize = paramDimension;
        if (this.canv == null) {
            return;
        }

        this.offscreenImage = this.canv.createImage(this.mSize.width, this.mSize.height);

        if (this.offscreenImage == null) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException("must have a valid offscreen image"),
                                             this.canv,
                                             "createImage");
            return;
        }
        if (this.singleFrameFlag == 6) {
            this.offscreenGraphics = this.offscreenImage.getGraphics();
        }
        try {
            ImageDescription localImageDescription = ImageDescription.getJavaDefaultPixelDescription(this.mSize.width,
                                                                                                     this.mSize.height);
            this.iData = new IntEncodedImage(this.mSize.width * this.mSize.height);

            localImageDescription.setDataSize(this.iData.getSize());

            this.image.setImageData(this.iData, localImageDescription);
            this.painter.newSizeNotified(this, this.mSize);
            if ((this.painter instanceof JImagePainter))
                ((JImagePainter) this.painter).prepareImage(this.canv);
            prepaint();
            if ((!this.interruptedGrab) && (isSingleFrame())) {
                doSingleFrame();
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "addedTo");
        }
    }

    private void doSingleFrame() throws QTException {
        this.rei = new RawEncodedImage(this.iData.getInts());
        this.image.setImageData(this.rei);
        this.iData = null;
        this.offscreenImage.flush();
        this.offscreenImage = null;
    }

    /**
     * @deprecated
     */
    public Dimension getSize() {
        return new Dimension(this.mSize.width, this.mSize.height);
    }

    /**
     * @deprecated
     */
    public final Region getClip() throws QTException {
        return this.image.getClip();
    }

    /**
     * @deprecated
     */
    public final void setClip(Region paramRegion) throws StdQTException {
        this.image.setClip(paramRegion);
    }

    /**
     * @deprecated
     */
    public ImagePresenter toImagePresenter() throws QTException {
        if (this.iData == null) {
            if (this.rei == null)
                return null;
            if (isSingleFrame()) {
                return ImagePresenter.fromQTImage(this.rei, (ImageDescription) this.image.getDescription().clone());
            }
            return ImagePresenter.fromQTImage((EncodedImage) this.rei.clone(),
                                              (ImageDescription) this.image.getDescription().clone());
        }

        return ImagePresenter.fromQTImage(new RawEncodedImage(this.iData.getInts()),
                                          (ImageDescription) this.image.getDescription().clone());
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return this.image.getDescription();
    }

    /**
     * @deprecated
     */
    public synchronized EncodedImage getImage() throws QTException {
        if (this.iData == null)
            return this.rei;
        return this.iData;
    }

    /**
     * @deprecated
     */
    public final Matrix getMatrix() throws StdQTException {
        return this.image.getMatrix();
    }

    /**
     * @deprecated
     */
    public final void setMatrix(Matrix paramMatrix) throws QTException {
        this.image.setMatrix(paramMatrix);
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        return this.image.getInitialSize();
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return this.image.getOriginalSize();
    }

    /**
     * @deprecated
     */
    public final QDGraphics getGWorld() {
        return this.image.getGWorld();
    }

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.image.setGWorld(paramQDGraphics);
    }

    /**
     * @deprecated
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        this.image.setDisplayBounds(paramQDRect);
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() {
        return this.image.getDisplayBounds();
    }

    /**
     * @deprecated
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if ((this.iData == null) && (this.rei == null))
            return;
        Region localRegion;
        if (this.interruptedGrab) {
            localRegion = prepaint();
            if ((localRegion != null) && (paramRegion != null))
                paramRegion = localRegion.union(paramRegion);
            if ((!this.interruptedGrab) && (isSingleFrame())) {
                doSingleFrame();
            }
        } else if ((!isSingleFrame()) && (isRedrawing())) {
            localRegion = prepaint();
            if ((localRegion != null) && (paramRegion != null))
                paramRegion = localRegion.union(paramRegion);
        }
        this.image.redraw(paramRegion);
    }

    /**
     * @deprecated
     */
    public void setRedrawing(boolean paramBoolean) {
        this.mRedrawFlag = paramBoolean;
    }

    /**
     * @deprecated
     */
    public boolean isSingleFrame() {
        return this.singleFrameFlag == 1;
    }

    /**
     * @deprecated
     */
    public boolean isRedrawing() {
        return this.mRedrawFlag;
    }

    /**
     * @deprecated
     */
    public Region prepaint() throws QTException {
        if (this.offscreenImage == null) {
            return null;
        }
        Region localRegion = null;

        try {
            if (this.offscreenGraphics == null) {
                this.offscreenGraphics = this.offscreenImage.getGraphics();
            }
            Rectangle[] arrayOfRectangle = this.painter.paint(this.offscreenGraphics);
            if (arrayOfRectangle != null) {
                for (int i = 0; i < arrayOfRectangle.length; i++) {
                    if (arrayOfRectangle[i] == null)
                        break;
                    int j = arrayOfRectangle[i].x;
                    int k = arrayOfRectangle[i].y;
                    int m = arrayOfRectangle[i].width > this.mSize.width ? this.mSize.width : arrayOfRectangle[i].width;
                    int n = arrayOfRectangle[i].height > this.mSize.height ? this.mSize.height : arrayOfRectangle[i].height;
                    PixelGrabber localPixelGrabber = new PixelGrabber(this.offscreenImage,
                                                                      j,
                                                                      k,
                                                                      m,
                                                                      n,
                                                                      this.iData.getInts(),
                                                                      k * this.mSize.width + j,
                                                                      this.mSize.width);
                    localPixelGrabber.grabPixels();
                    if (localRegion == null) {
                        localRegion = new Region(new QDRect(arrayOfRectangle[i].x,
                                                            arrayOfRectangle[i].y,
                                                            arrayOfRectangle[i].width,
                                                            arrayOfRectangle[i].height));
                    } else
                        localRegion = localRegion.union(new QDRect(arrayOfRectangle[i].x,
                                                                   arrayOfRectangle[i].y,
                                                                   arrayOfRectangle[i].width,
                                                                   arrayOfRectangle[i].height));
                }
            }
        } catch (InterruptedException localInterruptedException) {
            this.interruptedGrab = true;
            if (this.singleFrameFlag == 2)
                this.offscreenGraphics = null;
            return null;
        }
        this.interruptedGrab = false;
        if ((this.sprite != null) && (!isSingleFrame()) && (this.sprite.isValid()))
            this.sprite.invalidate();
        if (this.singleFrameFlag == 2)
            this.offscreenGraphics = null;
        return localRegion;
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        this.image.setGraphicsMode(paramGraphicsMode);
    }

    /**
     * @deprecated
     */
    public GraphicsMode getGraphicsMode() {
        return this.image.getGraphicsMode();
    }

    class RedrawInvalidator extends Invalidator {
        RedrawInvalidator(TwoDSprite paramTwoDSprite) {
            super(paramTwoDSprite);
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (QTImageDrawer.this.iData != null) {
                if (QTImageDrawer.this.isRedrawing()) {
                    QTImageDrawer.this.redraw(null);
                } else
                    this.s.setImage(QTImageDrawer.this.iData);
            }
            return true;
        }
    }

    /**
     * @deprecated
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        this.sprite = paramTwoDSprite;
        return isSingleFrame() ? null : new RedrawInvalidator(paramTwoDSprite);
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
    public synchronized void addedTo(Object paramObject) {
        if ((paramObject instanceof Component)) {
            if (this.canv != null)
                removedFrom(this.canv);
            this.canv = ((Component) paramObject);

            setSize(this.mSize);

            if (this.nl != null) {
                this.nl.notifyComplete();
            }
        }
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
        if ((paramObject instanceof Component)) {
            this.canv = null;
            this.offscreenImage = null;
        }
    }

    /**
     * @deprecated
     */
    public boolean addNotifyListener(NotifyListener paramNotifyListener) {
        this.nl = paramNotifyListener;
        if (!paramNotifyListener.setNotifier(this)) {
            paramNotifyListener = null;
            return false;
        }
        if (this.canv != null)
            paramNotifyListener.notifyComplete();
        return true;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTImageDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
