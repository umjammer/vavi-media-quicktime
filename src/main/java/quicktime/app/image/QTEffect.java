
package quicktime.app.image;

import java.awt.Dimension;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeBase;
import quicktime.std.comp.ComponentDescription;
import quicktime.std.comp.ComponentIdentifier;
import quicktime.std.image.DSequence;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ICMFrameTime;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.movies.Atom;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.AtomData;
import quicktime.util.EncodedImage;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class QTEffect implements QTDrawable, Redrawable {
    ImageDescription effectDesc;

    QDRect mDisplayBounds;

    QDDimension initSize;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public QTEffect() throws QTException {
        this(2, new QDDimension());
    }

    /**
     * @deprecated
     */
    public QTEffect(Dimension paramDimension) throws QTException {
        this(2, new QDDimension(paramDimension.width, paramDimension.height));
    }

    /**
     * @deprecated
     */
    public QTEffect(int paramInt) throws QTException {
        this(paramInt, new QDDimension());
    }

    /**
     * @deprecated
     */
    public QTEffect(int paramInt, Dimension paramDimension) throws QTException {
        this(paramInt, new QDDimension(paramDimension.width, paramDimension.height));
    }

    /**
     * @deprecated
     */
    public QTEffect(int paramInt, QDDimension paramQDDimension) throws QTException {
        this.mRedrawFlag = (paramInt == 2);
        this.singleFrameFlag = paramInt;

        this.effectDesc = new ImageDescription(0);

        this.effectDesc.setTemporalQuality(512);
        this.effectDesc.setSpatialQuality(512);

        this.effectDesc.setHRes(72.0F);
        this.effectDesc.setVRes(72.0F);

        this.effectDesc.setDepth(24);

        this.effectDesc.setFrameCount(1);

        this.effectDesc.setClutID(-1);

        this.mat = new Matrix();
        if ((paramQDDimension != null) && (paramQDDimension.getWidth() != 0) && (paramQDDimension.getHeight() != 0)) {
            setDisplayBounds(new QDRect(paramQDDimension));
        }
        this.initSize = paramQDDimension;
    }

    private QDGraphics mDestPort = QDGraphics.scratch;

    int mNumberOfFrames = 1;

    AtomContainer effectSample;

    RawEncodedImage effectSamplePtr;

    private Region clip;

    TimeBase tb;

    DSequence effectSequence;

    Matrix mat;

    GraphicsMode gMode;

    private quicktime.app.time.TaskThread tt;

    ICMFrameTime ft = new ICMFrameTime();

    int currentFrame = 1;

    private boolean mRedrawFlag;

    private int singleFrameFlag;

    boolean needsRedraw = false;

    /**
     * @deprecated
     */
    public void setRedrawing(boolean paramBoolean) {
        this.mRedrawFlag = paramBoolean;
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
    public boolean isSingleFrame() {
        return this.singleFrameFlag == 1;
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.effectSequence != null)
            this.effectSequence.setGraphicsMode(paramGraphicsMode);
        this.gMode = paramGraphicsMode;
    }

    /**
     * @deprecated
     */
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws StdQTException {
        this.clip = paramRegion;
        if (this.effectSequence != null)
            this.effectSequence.setMask(this.clip);
    }

    public Region getClip() {
        return this.clip;
    }

    /**
     * @deprecated
     */
    public AtomContainer getEffect() throws StdQTException {
        return this.effectSample.copyAtom(Atom.kParentIsContainer);
    }

    /**
     * @deprecated
     */
    public void setEffect(AtomContainer paramAtomContainer) throws QTException {
        if (paramAtomContainer == null) {
            this.effectSamplePtr = null;
            if (this.effectSample != null)
                this.effectSample.unlock();
            this.effectSample = null;
            return;
        }

        this.effectSample = paramAtomContainer;
        Atom localAtom = this.effectSample.findChildByID_Atom(Atom.kParentIsContainer, 2003329396, 1);
        this.effectSample.lock();
        AtomData localAtomData = this.effectSample.getAtomData(localAtom);
        this.effectDesc.setCType(EndianOrder.flipBigEndianToNative32(localAtomData.getInt(0)));
        this.effectSamplePtr = RawEncodedImage.fromQTPointer(this.effectSample.toQTPointer());

        setUpSequence();
    }

    /**
     * @deprecated
     */
    protected synchronized boolean setUpSequence() throws QTException {
        QDRect localQDRect = getDisplayBounds();
        if ((QDGraphics.scratch.equals(getGWorld())) || (this.effectSamplePtr == null) || (localQDRect == null)) {
            return false;
        }
        this.mat.setTx(localQDRect.getX());
        this.mat.setTy(localQDRect.getY());
        this.effectSequence = new DSequence(this.effectDesc,
                                            this.effectSamplePtr,
                                            getGWorld(),
                                            null,
                                            this.mat,
                                            getClip(),
                                            0,
                                            512,
                                            null);

        if (this.gMode != null) {
            this.effectSequence.setGraphicsMode(this.gMode);
        }

        this.tb = new TimeBase();
        this.tb.setRate(0.0F);
        this.effectSequence.setTimeBase(this.tb);
        return true;
    }

    public int getFrames() {
        return this.mNumberOfFrames;
    }

    /**
     * @deprecated
     */
    public int getCurrentFrame() {
        return this.currentFrame;
    }

    /**
     * @deprecated
     */
    public void checkForEffect(int paramInt) throws QTException {
        ComponentDescription localComponentDescription = new ComponentDescription();
        localComponentDescription.setType(QTUtils.toOSType("imdc"));
        localComponentDescription.setSubType(paramInt);
        ComponentIdentifier localComponentIdentifier = ComponentIdentifier.find(localComponentDescription);
        if (localComponentIdentifier == null) {
            throw new StdQTException(-50);
        }
    }

    /**
     * @deprecated
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.mDestPort = paramQDGraphics;
        if (QDGraphics.scratch.equals(paramQDGraphics)) {
            this.effectSequence = null;
        } else {
            setUpSequence();
        }
    }

    /**
     * @deprecated
     */
    public synchronized QDGraphics getGWorld() throws QTException {
        return this.mDestPort;
    }

    /**
     * @deprecated
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws QTException {
        this.mDisplayBounds = paramQDRect;
        this.effectDesc.setHeight(paramQDRect.getHeight());
        this.effectDesc.setWidth(paramQDRect.getWidth());
        this.mat.setTx(paramQDRect.getX());
        this.mat.setTy(paramQDRect.getY());
        setUpSequence();
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
    public QDRect getDisplayBounds() throws QTException {
        return this.mDisplayBounds == null ? new QDRect(this.initSize.getWidth(), this.initSize.getHeight())
                                           : this.mDisplayBounds;
    }

    /**
     * @deprecated
     */
    public DSequence getDSequence() {
        return this.effectSequence;
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() {
        return this.effectSamplePtr;
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return this.effectDesc;
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        QDDimension localQDDimension = getOriginalSize();
        return new Dimension(localQDDimension.getWidth(), localQDDimension.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return this.initSize.copy();
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws QTException {
        return this.mat.copy();
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        this.mat = paramMatrix.copy();
        if (this.effectSequence != null) {
            this.effectSequence.setMatrix(this.mat);
        }
    }

    /**
     * @deprecated
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if (this.effectSequence != null) {
            this.tb.setValue(this.currentFrame, this.mNumberOfFrames);
            this.ft.setValue(this.currentFrame);
            if (paramRegion != null) {
                if (getClip() == null) {
                    this.effectSequence.setMask(paramRegion);
                } else
                    this.effectSequence.invalidate(paramRegion);
            }
            this.effectSequence.decompressFrameWhen(this.effectSamplePtr, 0, this.ft);
            if ((paramRegion != null) && (getClip() == null)) {
                this.effectSequence.setMask(getClip());
            }
        }
    }

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /QTEffect.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
