
package quicktime.app.image;

import java.awt.Dimension;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.QTDrawable;
import quicktime.app.spaces.Sequencer;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.DSequence;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class DSequenceFromMemory extends Sequencer implements QTDrawable, DynamicImage {
    private DSequence seq;

    private Vector frames;

    private int initialNumElements;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static RawEncodedImage privateGetData(GraphicsImporter paramGraphicsImporter) throws QTException {
        int i = paramGraphicsImporter.getDataSize();
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(i, true);
        paramGraphicsImporter.readData(localRawEncodedImage, paramGraphicsImporter.getDataOffset(), i);
        return localRawEncodedImage;
    }

    /**
     * @deprecated
     */
    public DSequenceFromMemory(GraphicsImporter paramGraphicsImporter) throws QTException {
        this(privateGetData(paramGraphicsImporter), paramGraphicsImporter.getImageDescription());
    }

    /**
     * @deprecated
     */
    public DSequenceFromMemory(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        this.initialNumElements = 1;
        this.frames = new Vector(this.initialNumElements, this.initialNumElements);
        this.frames.addElement(paramEncodedImage);
        this.mDesc = paramImageDescription;
        this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * @deprecated
     */
    public DSequenceFromMemory(CSequenceToMemory paramCSequenceToMemory) throws QTException {
        this.mDesc = paramCSequenceToMemory.getDescription();
        this.frames = paramCSequenceToMemory.getFrames();
        this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * @deprecated
     */
    public DSequenceFromMemory(ImageSpec paramImageSpec) throws QTException {
        if ((paramImageSpec instanceof ImageDataSequence)) {
            ImageDataSequence localImageDataSequence = (ImageDataSequence) paramImageSpec;
            this.initialNumElements = localImageDataSequence.size();
            this.frames = new Vector(this.initialNumElements, this.initialNumElements);
            for (int i = 1; i <= localImageDataSequence.size(); i++) {
                this.frames.addElement(localImageDataSequence.getImage(i));
            }
            this.mDesc = localImageDataSequence.getDescription();
            this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
        } else {
            this.initialNumElements = 1;
            this.frames = new Vector();
            this.frames.addElement(paramImageSpec.getImage());
            this.mDesc = paramImageSpec.getDescription();
            this.bnds = new QDRect(this.mDesc.getWidth(), this.mDesc.getHeight());
        }
    }

    private int currentFrame = 1;

    private QDGraphics port = QDGraphics.scratch;

    private int mWidth;

    private int mHeight;

    private ImageDescription mDesc;

    private Matrix mat = new Matrix();

    private QDRect bnds;

    private Region clip = null;

    private GraphicsMode gMode;

    public int flags = 0;

    /**
     * @deprecated
     */
    public QDGraphics getGWorld() {
        return this.port;
    }

    /**
     * @deprecated
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        this.port = paramQDGraphics;
        if (!QDGraphics.scratch.equals(paramQDGraphics)) {
            this.seq = new DSequence(this.mDesc,
                                     this.port,
                                     null,
                                     getMatrix(),
                                     null,
                                     this.flags,
                                     768,
                                     CodecComponent.bestSpeedCodec);

            if (this.gMode != null)
                setGraphicsMode(this.gMode);
            if (this.clip != null)
                setClip(this.clip);
        } else {
            this.seq = null;
        }
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        if (this.seq != null)
            this.seq.setMatrix(paramMatrix);
        this.mat = paramMatrix;
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.seq != null ? this.seq.getMatrix() : this.mat;
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.mDesc.getWidth(), this.mDesc.getHeight());
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / this.mDesc.getWidth());
        localMatrix.setSy(paramQDRect.getHeight() / this.mDesc.getHeight());
        this.mWidth = paramQDRect.getWidth();
        this.mHeight = paramQDRect.getHeight();

        if (this.seq != null)
            this.seq.setMatrix(localMatrix);
        this.bnds = paramQDRect;
    }

    /**
     * @deprecated
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        this.bnds.setX(paramInt1);
        this.bnds.setY(paramInt2);
        setDisplayBounds(this.bnds);
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() {
        return this.bnds;
    }

    /**
     * @deprecated
     */
    public final synchronized void redraw(Region paramRegion) throws StdQTException {
        if (this.seq == null)
            return;
        if (paramRegion != null) {
            this.seq.setMask(paramRegion);
            drawCurrentFrame(this.flags);
            this.seq.setMask(this.clip);
        } else {
            drawCurrentFrame(this.flags);
        }
    }

    /**
     * @deprecated
     */
    public Region getClip() throws StdQTException {
        return this.clip;
    }

    /**
     * @deprecated
     */
    public void setClip(Region paramRegion) throws StdQTException {
        if (this.seq != null)
            this.seq.setMask(paramRegion);
        this.clip = paramRegion;
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return this.mDesc;
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() throws StdQTException {
        if (this.frames != null) {
            return (EncodedImage) this.frames.elementAt(getCurrentFrame() - 1);
        }
        return null;
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.frames != null ? this.frames.size() : 0;
    }

    /**
     * @deprecated
     */
    public int drawCurrentFrame(int paramInt) throws StdQTException {
        if (this.seq == null)
            return 0;
        EncodedImage localEncodedImage = getImage();
        return this.seq.decompressFrameS(localEncodedImage, paramInt);
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.seq != null)
            this.seq.setGraphicsMode(paramGraphicsMode);
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
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) {
        return new SeqInvalidator(paramTwoDSprite, getCurrentFrame());
    }

    class SeqInvalidator extends Invalidator {
        private int lastFrame;

        SeqInvalidator(TwoDSprite paramTwoDSprite, int paramInt) {
            super(paramTwoDSprite);
            this.lastFrame = paramInt;
        }

        public boolean tickle(float paramFloat, int paramInt) throws QTException {
            if (this.lastFrame != DSequenceFromMemory.this.getCurrentFrame()) {
                DSequenceFromMemory.this.redraw(null);
                this.s.invalidate();
                this.lastFrame = DSequenceFromMemory.this.getCurrentFrame();
            }
            return true;
        }
    }

    /**
     * @deprecated
     */
    public DSequence getSequence() {
        return this.seq;
    }

    /**
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "[size=" + size() + "]";
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

    /**
     * @deprecated
     */
    public void removedFromCompositor(SWCompositor paramSWCompositor) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /DSequenceFromMemory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
