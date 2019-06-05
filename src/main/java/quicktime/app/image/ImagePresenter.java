
package quicktime.app.image;

import java.awt.Dimension;
import java.io.IOException;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.io.QTFile;
import quicktime.qd.Pict;
import quicktime.qd.PixMap;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.qd.SetGWorld;
import quicktime.std.StdQTException;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.DSequence;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ICMPixelFormatInfo;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.std.image.QTImage;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;


/**
 * @deprecated
 */
public class ImagePresenter implements QTDrawable, Compositable, ImageSettable {
    private static final boolean doDrawProfile = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromFile(QTFile paramQTFile) throws IOException, QTException {
        return fromGraphicsImporterDrawer(new GraphicsImporterDrawer(paramQTFile));
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws IOException, QTException {
        return fromGraphicsImporterDrawer(new GraphicsImporterDrawer(paramGraphicsImporter));
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromGraphicsImporterDrawer(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws IOException,
                                                                                                                QTException {
        QDRect localQDRect = new QDRect(paramGraphicsImporterDrawer.getDisplayBounds().getWidth(),
                                        paramGraphicsImporterDrawer.getDisplayBounds().getHeight());
        QDGraphics localQDGraphics = new QDGraphics(localQDRect);
        paramGraphicsImporterDrawer.setDisplayBounds(localQDRect);
        paramGraphicsImporterDrawer.setGWorld(localQDGraphics);
        paramGraphicsImporterDrawer.redraw(null);
        return fromGWorld(localQDGraphics);
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromImageSpec(ImageSpec paramImageSpec) throws QTException {
        return fromQTImage(paramImageSpec.getImage(), paramImageSpec.getDescription());
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromImageSequence(ImageDataSequence paramImageDataSequence, int paramInt) throws QTException {
        return fromQTImage(paramImageDataSequence.getImage(paramInt), paramImageDataSequence.getDescription());
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromGWorld(QDGraphics paramQDGraphics) throws QTException {
        PixMap localPixMap = paramQDGraphics.getPixMap();
        return new ImagePresenter(localPixMap.getPixelData(), new ImageDescription(localPixMap));
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromGWorld(QDGraphics paramQDGraphics,
                                            QDRect paramQDRect,
                                            int paramInt1,
                                            int paramInt2,
                                            int paramInt3,
                                            CodecComponent paramCodecComponent) throws QTException {
        int i = QTImage.getMaxCompressionSize(paramQDGraphics,
                                              paramQDRect,
                                              paramInt1,
                                              paramInt2,
                                              paramInt3,
                                              paramCodecComponent);
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(i, true);
        ImageDescription localImageDescription = QTImage.compress(paramQDGraphics,
                                                                  paramQDRect,
                                                                  paramInt2,
                                                                  paramInt3,
                                                                  localRawEncodedImage);
        localRawEncodedImage.setSize(localImageDescription.getDataSize());
        ImagePresenter localImagePresenter = new ImagePresenter(paramQDRect);
        localImagePresenter.optimiseRedraw = false;
        localImagePresenter.setImageData(localRawEncodedImage, localImageDescription);
        return localImagePresenter;
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromPict(Pict paramPict) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(1902408311);
        QDRect localQDRect = paramPict.getPictFrame();
        localImageDescription.setVersion(-2);
        localImageDescription.setVendor(1634758764);
        localImageDescription.setSpatialQuality(1024);
        localImageDescription.setWidth(localQDRect.getWidth());
        localImageDescription.setHeight(localQDRect.getHeight());
        localImageDescription.setHRes(72.0F);
        localImageDescription.setVRes(72.0F);
        localImageDescription.setDataSize(paramPict.getSize());
        localImageDescription.setFrameCount(1);
        localImageDescription.setDepth(24);
        localImageDescription.setClutID(-1);
        return new ImagePresenter(paramPict.toEncodedImage(), localImageDescription);
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromQTImage(EncodedImage paramEncodedImage,
                                             ImageDescription paramImageDescription) throws QTException {
        return new ImagePresenter(paramEncodedImage, paramImageDescription);
    }

    /**
     * @deprecated
     */
    public ImagePresenter(QDRect paramQDRect) {
        this.mDisplayBounds = paramQDRect;
        this.origWidth = paramQDRect.getWidth();
        this.origHeight = paramQDRect.getHeight();
    }

    /**
     * @deprecated
     */
    private ImagePresenter(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        setImageData(paramEncodedImage, paramImageDescription);
    }

    private ImageDescription mImageDescription = null;

    private DSequence mImageSeq = null;

    private QDRect mDisplayBounds = null;

    private ByteEncodedImage mDataB;

    private RawEncodedImage mDataR;

    private IntEncodedImage mDataI;

    private EncodedImage mData;

    private QDGraphics port = QDGraphics.scratch;

    private int origWidth;

    private int origHeight;

    private Matrix mat = new Matrix();

    private Region clip;

    private GraphicsMode gMode = new GraphicsMode();

    private boolean optimiseRedraw = true;

    /**
     * @deprecated
     */
    public void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        int i = paramImageDescription.getCType();
        this.mImageDescription = paramImageDescription;
        if ((paramEncodedImage != null) && (this.optimiseRedraw))
            if (((i == 1919706400) || (i == 1918990112) || (i == 1902408311) || (i == 1885434984)
                 || (ICMPixelFormatInfo.isValidPixelFormat(i)) ? 1 : 0) == 0) {

                Object localObject1 = null;
                try {
                    QDGraphics localQDGraphics = new QDGraphics(new QDRect(paramImageDescription.getWidth(),
                                                                           paramImageDescription.getHeight()));
                    DSequence localDSequence = new DSequence(paramImageDescription,
                                                             localQDGraphics,
                                                             null,
                                                             null,
                                                             null,
                                                             0,
                                                             768,
                                                             CodecComponent.anyCodec);

                    localDSequence.decompressFrameS(paramEncodedImage, 0);
                    PixMap localPixMap = localQDGraphics.getPixMap();
                    this.mImageDescription = new ImageDescription(localPixMap);
                    paramEncodedImage = localPixMap.getPixelData();
                } catch (QTException localQTException) {
                    this.mImageDescription = paramImageDescription;
                } finally {
                    if (localObject1 != null) {
                        ((SetGWorld) localObject1).reset();
                    }
                }
            }
        this.mImageDescription.setFrameCount(1);

        this.origWidth = paramImageDescription.getWidth();
        this.origHeight = paramImageDescription.getHeight();
        if (this.mDisplayBounds == null)
            this.mDisplayBounds = new QDRect(paramImageDescription.getWidth(), paramImageDescription.getHeight());
        setDisplayBounds(this.mDisplayBounds);

        setImageData(paramEncodedImage);
    }

    /**
     * @deprecated
     */
    public void setImageData(EncodedImage paramEncodedImage) throws QTException {
        this.mData = paramEncodedImage;
        if ((this.mData instanceof ByteEncodedImage)) {
            this.mDataB = ((ByteEncodedImage) this.mData);
            this.mDataI = null;
            this.mDataR = null;
        } else if ((this.mData instanceof IntEncodedImage)) {
            this.mDataI = ((IntEncodedImage) this.mData);
            this.mDataB = null;
            this.mDataR = null;
        } else if ((this.mData instanceof RawEncodedImage)) {
            this.mDataR = ((RawEncodedImage) this.mData);
            this.mDataI = null;
            this.mDataB = null;
        } else {
            this.mDataR = null;
            this.mDataI = null;
            this.mDataB = null;
        }
        if (this.mData != null) {
            this.mImageDescription.setDataSize(this.mData.getSize());
            if (this.mImageSeq == null) {
                setGWorld(this.port);
            }
        }
    }

    /**
     * @deprecated
     */
    public void setRedrawOptimised(boolean paramBoolean) {
        this.optimiseRedraw = paramBoolean;
    }

    /**
     * @deprecated
     */
    public boolean isRedrawOptimised() {
        return this.optimiseRedraw;
    }

    /**
     * @deprecated
     */
    public void removeImageData() throws QTException {
        setGWorld(QDGraphics.scratch);
        this.mData = null;
        this.mDataI = null;
        this.mDataB = null;
        this.mDataR = null;
        this.mImageDescription = null;
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() {
        return this.mImageDescription;
    }

    /**
     * @deprecated
     */
    public DSequence getDSequence() {
        return this.mImageSeq;
    }

    /**
     * @deprecated
     */
    public Region getClip() throws QTException {
        return this.clip;
    }

    /**
     * @deprecated
     */
    public synchronized void setClip(Region paramRegion) throws StdQTException {
        if (this.mImageSeq != null)
            this.mImageSeq.setMask(paramRegion);
        this.clip = paramRegion;
    }

    /**
     * @deprecated
     */
    public Matrix getMatrix() throws StdQTException {
        return this.mImageSeq != null ? this.mImageSeq.getMatrix() : this.mat;
    }

    /**
     * @deprecated
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        if (this.mImageSeq != null) {
            this.mImageSeq.setMatrix(paramMatrix);
        }
        this.mDisplayBounds.setX(paramMatrix.getTx() + 0.5F);
        this.mDisplayBounds.setY(paramMatrix.getTy() + 0.5F);
        this.mDisplayBounds.setWidth(this.origWidth * paramMatrix.getSx() + 0.5F);
        this.mDisplayBounds.setHeight(this.origHeight * paramMatrix.getSy() + 0.5F);
        this.mat = paramMatrix;
    }

    /**
     * @deprecated
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.origWidth, this.origHeight);
    }

    /**
     * @deprecated
     */
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.origWidth, this.origHeight);
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() {
        return this.mData;
    }

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
        if ((this.mData != null) && (this.mImageDescription != null)) {
            if (QDGraphics.scratch.equals(paramQDGraphics)) {
                this.mImageSeq = null;
            } else {
                this.mImageSeq = new DSequence(this.mImageDescription,
                                               paramQDGraphics,
                                               null,
                                               getMatrix(),
                                               getClip(),
                                               0,
                                               768,
                                               CodecComponent.bestFidelityCodec);

                setGraphicsMode(this.gMode);
            }
        }
        this.port = paramQDGraphics;
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        Matrix localMatrix = new Matrix();

        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / this.origWidth);
        localMatrix.setSy(paramQDRect.getHeight() / this.origHeight);

        if (this.mImageSeq != null)
            this.mImageSeq.setMatrix(localMatrix);
        this.mat = localMatrix;
        this.mDisplayBounds = paramQDRect;
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
        return this.mDisplayBounds;
    }

    /**
     * @deprecated
     */
    public synchronized void redraw(Region paramRegion) throws QTException {
        if (this.mImageSeq == null) {
            return;
        }
        long l = 0L;

        if (paramRegion != null) {
            if (this.clip == null) {
                this.mImageSeq.setMask(paramRegion);
            } else
                this.mImageSeq.invalidate(paramRegion);
            doDraw();
            if (this.clip == null)
                this.mImageSeq.setMask(this.clip);
        } else {
            doDraw();
        }
    }

    /**
     * @deprecated
     */
    protected final void doDraw() throws QTException {
        if (this.mDataR != null) {
            this.mImageSeq.decompressFrameS(this.mDataR, 0);
        } else if (this.mDataI != null) {
            this.mImageSeq.decompressFrameS(this.mDataI, 0);
        } else if (this.mDataB != null) {
            this.mImageSeq.decompressFrameS(this.mDataB, 0);
        }
    }

    /**
     * @deprecated
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.mImageSeq != null)
            this.mImageSeq.setGraphicsMode(paramGraphicsMode);
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
    public String toString() {
        return getClass().getName() + "[" + this.mData + "," + this.mImageDescription + "]";
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
 * /ImagePresenter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
