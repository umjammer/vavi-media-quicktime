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
 * Deprecated.
 * This class represents an image that is loaded into memory. Its focus is on performance
 * so if the image data that is presented to the ImagePresenter is in a format that is non-optimal
 * for drawing the data will generally be converted to a format that is optimal, at the expense of a greater usage
 * of memory (see setImageData methods). This process is controlled by the optimised redrawing flag, which if true
 * (the default), may convert the image to a better format for drawing performance.
 */
public class ImagePresenter implements QTDrawable, Compositable, ImageSettable {

    private static final boolean doDrawProfile = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from a file.
     * @param qtFile the QTFile.
     * @param java.io.IOException
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromFile(QTFile paramQTFile) throws IOException, QTException {
        return fromGraphicsImporterDrawer(new GraphicsImporterDrawer(paramQTFile));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from a GraphicsImporter.
     * @param importer the GraphicsImporter object.
     * @param java.io.IOException
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws IOException, QTException {
        return fromGraphicsImporterDrawer(new GraphicsImporterDrawer(paramGraphicsImporter));
    }

    /**
     * @deprecated
     */
    public static ImagePresenter fromGraphicsImporterDrawer(GraphicsImporterDrawer paramGraphicsImporterDrawer) throws IOException, QTException {
        QDRect localQDRect = new QDRect(paramGraphicsImporterDrawer.getDisplayBounds().getWidth(), paramGraphicsImporterDrawer.getDisplayBounds().getHeight());
        QDGraphics localQDGraphics = new QDGraphics(localQDRect);
        paramGraphicsImporterDrawer.setDisplayBounds(localQDRect);
        paramGraphicsImporterDrawer.setGWorld(localQDGraphics);
        paramGraphicsImporterDrawer.redraw(null);
        return fromGWorld(localQDGraphics);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from an ImageSpec object.
     * @param image the image
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromImageSpec(ImageSpec paramImageSpec) throws QTException {
        return fromQTImage(paramImageSpec.getImage(), paramImageSpec.getDescription());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from nth first image of any ImageDataSequence object
     * @param image the image
     * @param nth the index into the ImageDataSequence object of the image data that should be used
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromImageSequence(ImageDataSequence paramImageDataSequence, int paramInt) throws QTException {
        return fromQTImage(paramImageDataSequence.getImage(paramInt), paramImageDataSequence.getDescription());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from the PixMap of the QDGraphics object.
     * It will not be compressed.
     * @param grafPort the grafPort that will be used to create the ImagePresenter object from.
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromGWorld(QDGraphics paramQDGraphics) throws QTException {
        PixMap localPixMap = paramQDGraphics.getPixMap();
        return new ImagePresenter(localPixMap.getPixelData(), new ImageDescription(localPixMap));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from PixMap source, bounds QDRect, color depth, compression quality, codec type,
     * and codec component. This will circumvent the normal restrictions placed on an ImagePresenter in that the data
     * will be kept in the format you specify, even if that is not an optimal format for rendering.
     *
     * @param port a QDGraphics object from which the PixMap will be used.
     * @param rect the QDRect object - this may not be null
     * @param colorDepth the int value.
     * @param quality the int value.
     * @param codecType the int value.
     * @param codec the CodecComponent object.
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromGWorld(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, int paramInt3, CodecComponent paramCodecComponent) throws QTException {
        int i = QTImage.getMaxCompressionSize(paramQDGraphics, paramQDRect, paramInt1, paramInt2, paramInt3, paramCodecComponent);
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(i, true);
        ImageDescription localImageDescription = QTImage.compress(paramQDGraphics, paramQDRect, paramInt2, paramInt3, localRawEncodedImage);
        localRawEncodedImage.setSize(localImageDescription.getDataSize());
        ImagePresenter localImagePresenter = new ImagePresenter(paramQDRect);
        localImagePresenter.optimiseRedraw = false;
        localImagePresenter.setImageData(localRawEncodedImage, localImageDescription);
        return localImagePresenter;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from a Pict object that is stored in memory.
     * @param p the pict handle
     * @param {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This creates an ImagePresenter object from raw image data and ImageDescription.
     * @param data the image data.
     * @param desc the ImageDescription object.
     * @param {@link quicktime.QTException}
     */
    public static ImagePresenter fromQTImage(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        return new ImagePresenter(paramEncodedImage, paramImageDescription);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates an emtpy ImagePresenter object - you will need to set the image data and description
     * before this object is usable.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the current image data and description.
     * If the data is specified then it may be recompressed if the format of the data is
     * not optimal for redrawing and the redraw optimised flag is set to true. If you pass in null
     * for the EncodedImage argument then the application must call the setImageData (EncodedImage) method
     * after this to ensure that the ImagePresenter has both required data and description.
     * The data formats that are not recompressed are any known pixel format (which includes the rawCodecType),
     * animation, vector or QuickDraw formats.
     * @param data the new image data for display or null to allow the application to keep the data in its preferred format.
     * @param desc describes the format of the image data - this may NOT be null.
     * @param {@link quicktime.QTException}
     */
    public void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException {
        int i = paramImageDescription.getCType();
        this.mImageDescription = paramImageDescription;
        if ((paramEncodedImage != null) && (this.optimiseRedraw))
            if (((i == 1919706400) || (i == 1918990112) || (i == 1902408311) || (i == 1885434984) || (ICMPixelFormatInfo.isValidPixelFormat(i)) ? 1 : 0) == 0) {
                Object localObject1 = null;
                try {
                    QDGraphics localQDGraphics = new QDGraphics(new QDRect(paramImageDescription.getWidth(), paramImageDescription.getHeight()));
                    DSequence localDSequence = new DSequence(paramImageDescription, localQDGraphics, null, null, null, 0, 768, CodecComponent.anyCodec);
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If you have already set the image data and you have image data in the same
     * general parameters as previously set (ie. the same ImageDescription will
     * describe the data) then this call can be used to just replace the changed
     * image data.
     * @param ei the new encoded image data
     * @param {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the state of the optimisation for redraw flag. If true, then when image data is set
     * the ImagePresenter may decompress the image data to a format that is optimised for performance
     * at the expense of possibly greater memory usage. If false, then setting the image data will
     * not decompress the image data. The default is true. Changing this flag will not alter the current
     * image data format, it only effects consequent calls to the setImageData (EncodedImage, ImageDescription) calls.
     * @param flag the new setting.
     */
    public void setRedrawOptimised(boolean paramBoolean) {
        this.optimiseRedraw = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current state of the optimisation for drawing flag.
     * @return a boolean
     */
    public boolean isRedrawOptimised() {
        return this.optimiseRedraw;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method removes the previously set encoded image data and description
     * from the ImagePresenter and will remove it from any destination QDGraphics
     * it is currently drawing into.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     */
    public ImageDescription getDescription() {
        return this.mImageDescription;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the DSequence that is used by the ImagePresenter. This
     * object may be null if the ImagePresenter is not currently attached to
     * a valid destination QDGraphics. The application should not cache this
     * sequence object for any length of time - as if the destination QDGraphics
     * is changed the DSequence is thrown away and a new one is created.
     */
    public DSequence getDSequence() {
        return this.mImageSeq;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a region that defines the current clipping region for the object.
     * @return the clipping region
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return this.clip;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets a region that will define the clipping region for the object
     * @param reg the new clipping region
     * @param {@link quicktime.std.StdQTException}
     */
    public synchronized void setClip(Region paramRegion) throws StdQTException {
        if (this.mImageSeq != null)
            this.mImageSeq.setMask(paramRegion);
        this.clip = paramRegion;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        return this.mImageSeq != null ? this.mImageSeq.getMatrix() : this.mat;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method assigns a mapping matrix to the sequence.
     * QuickTime::SetDSequenceMatrix
     * @param matrix a Matrix object that specifies how to transform the image during
     *                 decompression
     * @param {@link quicktime.QTException}
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
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        return new Dimension(this.origWidth, this.origHeight);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size of the image data presented.
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        return new QDDimension(this.origWidth, this.origHeight);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data.
     * @return the image data
     */
    public EncodedImage getImage() {
        return this.mData;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     */
    public QDGraphics getGWorld() {
        return this.port;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @param {@link quicktime.QTException}
     */
    public void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        if ((this.mData != null) && (this.mImageDescription != null)) {
            if (QDGraphics.scratch.equals(paramQDGraphics)) {
                this.mImageSeq = null;
            } else {
                this.mImageSeq = new DSequence(this.mImageDescription, paramQDGraphics, null, getMatrix(), getClip(), 0, 768, CodecComponent.bestFidelityCodec);
                setGraphicsMode(this.gMode);
            }
        }
        this.port = paramQDGraphics;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @param {@link quicktime.std.StdQTException}
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @param {@link quicktime.QTException}
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the boundary of the client
     */
    public QDRect getDisplayBounds() {
        return this.mDisplayBounds;
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This is the method that blits the data directly to the destination QDGraphics.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This sets the GraphicsMode for the Compositor when it draws its composited
     * contents to its destination QDGraphics (typically the QTCanvas).
     * @param mode the new GraphicsMode for this presenter
     * @param {@link quicktime.QTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.mImageSeq != null)
            this.mImageSeq.setGraphicsMode(paramGraphicsMode);
        this.gMode = paramGraphicsMode;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This gets the current GraphicsMode for the compositor.
     * @return a GraphicsMode
     */
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a String representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[" + this.mData + "," + this.mImageDescription + "]";
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public void addedTo(Object paramObject) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImagePresenter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
