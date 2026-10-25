package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SpriteManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.anim.Sprite;
import quicktime.std.movies.AtomContainer;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;
import quicktime.util.UtilException;

/**
 * This class corresponds to the image description handle.
 * refer to ImageDescription in QuickTime
 */
public class ImageDescription extends SampleDescription implements QuickTimeLib {

    private static Object linkage;

    private static EndianDescriptor ed;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 86;

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    protected static EndianDescriptor makeED() {
        EndianDescriptor endiandescriptor = SampleDescription.makeED();
        EndianFlipSpec endianflipspec = new EndianFlipSpec(16, 2, 2);
        endiandescriptor.addFlipSpec(endianflipspec);
        EndianFlipSpec endianflipspec1 = new EndianFlipSpec(20, 4, 3);
        endiandescriptor.addFlipSpec(endianflipspec1);
        EndianFlipSpec endianflipspec2 = new EndianFlipSpec(32, 2, 2);
        endiandescriptor.addFlipSpec(endianflipspec2);
        EndianFlipSpec endianflipspec3 = new EndianFlipSpec(36, 4, 2);
        endiandescriptor.addFlipSpec(endianflipspec3);
        EndianFlipSpec endianflipspec4 = new EndianFlipSpec(44, 4, 1);
        endiandescriptor.addFlipSpec(endianflipspec4);
        EndianFlipSpec endianflipspec5 = new EndianFlipSpec(48, 2, 1);
        endiandescriptor.addFlipSpec(endianflipspec5);
        EndianFlipSpec endianflipspec6 = new EndianFlipSpec(82, 2, 2);
        endiandescriptor.addFlipSpec(endianflipspec6);
        return endiandescriptor;
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * Determines the image description information.
     * QuickTime::GraphicsExportGetInputImageDescription
     * @see {@link quicktime.std.image.GraphicsExporter#getInputImageDescription()}
     */
    public static ImageDescription fromGraphicsExporter(GraphicsExporter graphicsexporter) throws StdQTException {
        int[] ai = new int[1];
        int i = GraphicsExportGetInputImageDescription(QTObject.ID(graphicsexporter), ai);
        StdQTException.checkError(i);
        return new ImageDescription(ai[0], null);
    }

    /**
     * Returns the image description information.
     * QuickTime::GraphicsExportDoUseCompressor
     * @see {@link quicktime.std.image.GraphicsExporter#doUseCompressor(quicktime.std.movies.AtomContainer)}
     */
    public static ImageDescription fromGraphicsExporterCompressor(GraphicsExporter graphicsexporter, AtomContainer atomcontainer) throws StdQTException {
        int[] ai = { 0 };
        int i = GraphicsExportDoUseCompressor(QTObject.ID(graphicsexporter), QTObject.ID(atomcontainer), ai);
        StdQTException.checkError(i);
        return new ImageDescription(ai[0], graphicsexporter);
    }

    /**
     * Determines the image description information.
     * QuickTime::GraphicsImportGetImageDescription
     * @see {@link quicktime.std.image.GraphicsImporter#getImageDescription()}
     */
    public static ImageDescription fromGraphicsImporter(GraphicsImporter graphicsimporter) throws StdQTException {
        int[] ai = new int[1];
        int i = GraphicsImportGetImageDescription(QTObject.ID(graphicsimporter), ai);
        StdQTException.checkError(i);
        return new ImageDescription(ai[0], null);
    }

    /**
     * This method gets the image description property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the image description.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.anim.Sprite#getImageDescription()}
     */
    public static ImageDescription fromSprite(Sprite sprite) throws StdQTException {
        int[] ai = { 0 };
        short word0 = GetSpriteProperty(QTObject.ID(sprite), 2, ai);
        StdQTException.checkError(word0);
        return new ImageDescription(ai[0], sprite);
    }

    /**
     * Compresses a frame that is sourced by a PixMap from the supplied QDGraphics, returning
     * the compressed data and an image description.
     * QuickTime::SCCompressImage()
     * @param src the source PixMap.
     * @param srcRect you can specify to compress just a part of the pix map by specifying
     *  a rectangle - or pass in null to compress the entire pix map.
     * @return an image data object.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.qtcomponents.ImageCompressionDialog#compressImage(quicktime.qd.QDGraphics, quicktime.qd.QDRect)}
     */
    public static QTImage fromImageCompressionDialog(ImageCompressionDialog imagecompressiondialog, QDGraphics qdgraphics, QDRect qdrect) throws QTException {
        byte[] abyte0 = qdrect == null ? null : qdrect.getRect();
        int[] ai = { 0 };
        int[] ai1 = { 0 };
        if (qdgraphics == null)
            throw new StdQTException(-50);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if (QTSession.isCurrentOS(1) || QTSession.isCurrentOS(4))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(qdgraphics), 0);
            i = SCCompressImage(QTObject.ID(imagecompressiondialog), QTObject.ID(qdgraphics.getPixMap()), abyte0, ai, ai1);
            if (QTSession.isCurrentOS(1) || QTSession.isCurrentOS(4))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        if (i == 1) {
            throw new StdQTException(-128);
        } else {
            StdQTException.checkError(i);
            class _cls1ImageHdl extends QTHandleRef {

                _cls1ImageHdl(int i, Object obj) {
                    super(i, obj, false);
                }
            }
            _cls1ImageHdl _lcls1imagehdl = new _cls1ImageHdl(ai1[0], null);
            _lcls1imagehdl.lockHigh();
            return new QTImage(new ImageDescription(ai[0], null), RawEncodedImage.fromQTHandle(_lcls1imagehdl));
        }
    }

    /**
     * Create an ImageDescription structure that describes
     * the default Java pixel format.
     * The Application must later set the data size that describes the image
     * represented by this image description. This is typically the size returned from one of
     * the Encoded image formats.
     * @param width width of the image
     * @param height height of image
     * @param {@link quicktime.QTException}
     */
    public static ImageDescription getJavaDefaultPixelDescription(int i, int j) throws QTException {
        ImageDescription imagedescription = null;
        if (QTSession.isCurrentOS(2))
            imagedescription = new ImageDescription(QDGraphics.kDefaultPixelFormat);
        else
            imagedescription = new ImageDescription(0x72617720);
        imagedescription.setDepth(32);
        imagedescription.setSpatialQuality(512);
        imagedescription.setWidth(i);
        imagedescription.setHeight(j);
        imagedescription.setHRes(72F);
        imagedescription.setVRes(72F);
        imagedescription.setFrameCount(1);
        imagedescription.setClutID(-1);
        return (ImageDescription) imagedescription.clone();
    }

    /**
     * Create an ImageDescription for an Effect Type.
     * QuickTime::MakeImageDescriptionForEffect
     * @param effectType the effectType for which the imageDescription will be made
     * @return the ImageDescription for the given Effect type.
     * @return {@link quicktime.QTException}
     */
    public static ImageDescription forEffect(int i) throws QTException {
        return new ImageDescription(allocate(i), null);
    }

    /**
     * Create an ImageDescription structure
     * @param format the dataFormat
     */
    public ImageDescription(int i) throws QTException {
        super(86, true, i);
    }

    /**
     * Create an ImageDescription from a PixMap.
     * QuickTime::MakeImageDescriptionForPixMap
     * @param pixmap the PixMap from which the imageDescription will be made
     */
    public ImageDescription(PixMap pixmap) throws QTException {
        this(allocate(pixmap), null);
    }

    protected ImageDescription(int i, Object obj) {
        super(i, obj, false);
    }

    private static int allocate(PixMap pixmap) throws QTException {
        int[] ai = { 0 };
        StdQTException.checkError(MakeImageDescriptionForPixMap(QTObject.ID(pixmap), ai));
        return ai[0];
    }

    private static int allocate(int i) throws QTException {
        int[] ai = { 0 };
        StdQTException.checkError(MakeImageDescriptionForEffect(i, ai));
        return ai[0];
    }

    /**
     * Implementation of Cloneable Interface.
     */
    public Object clone() {
        return new ImageDescription(makeAndCopyHandle(), null);
    }

    /**
     * Update the custom color table for an image.
     * QuickTime::SetImageDescriptionCTable
     * @param cTable a color table.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setCTable(ColorTable colortable) throws StdQTException {
        StdQTException.checkError(SetImageDescriptionCTable(_ID(), QTObject.ID(colortable)));
    }

    /**
     * Get the custom color table for an image. If there is no CTable this returns null.
     * QuickTime::GetImageDescriptionCTable
     * @return the ColorTable
     * @return {@link quicktime.QTException}
     */
    public ColorTable getCTable() throws QTException {
        return ColorTable.fromImageDescription(this);
    }

    /**
     * Counts the number of extensions of a given type.
     * QuickTime::CountImageDescriptionExtensionType
     * @param idType A four-byte signature indentifying the type of data being counted from the ImageDescription.
     * @return the number of extensions of the specified type
     * @return {@link quicktime.std.StdQTException}
     */
    public int countExtensionType(int i) throws StdQTException {
        int[] ai = new int[1];
        StdQTException.checkError(CountImageDescriptionExtensionType(_ID(), i, ai));
        return ai[0];
    }

    /**
     * Returns the next extension type.
     * QuickTime::GetNextImageDescriptionExtensionType
     * @return on entry, contains the starting point for the search. On return, will contain the next extension type found
     * @return {@link quicktime.std.StdQTException}
     */
    public int getNextExtensionType() throws StdQTException {
        int[] ai = new int[1];
        StdQTException.checkError(GetNextImageDescriptionExtensionType(_ID(), ai));
        return ai[0];
    }

    /**
     * Add the extended data of the image.
     * QuickTime::AddImageDescriptionExtension
     * @param idType A four-byte signature indentifying the type of data being added to the ImageDescription.
     * @param extension The handle containing the extension data.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addExtension(int i, QTHandle qthandle) throws StdQTException {
        StdQTException.checkError(AddImageDescriptionExtension(_ID(), QTObject.ID(qthandle), i));
    }

    /**
     * Removes a specified extension from an ImageDescription.
     * QuickTime::RemoveImageDescriptionExtension
     * @param idType A four-byte signature indentifying the type of data being removed from the ImageDescription.
     * @param index The index (from 1 to the count as returned by countExtensionType) of the extension to remove..
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeExtension(int i, int j) throws StdQTException {
        StdQTException.checkError(RemoveImageDescriptionExtension(_ID(), i, j));
    }

    /**
     * Obtain the extended data for an image.
     * QuickTime::GetImageDescriptionExtension
     * @param idType specifies the extension's type value.
     * @param index specifies the extension's index value.
     * @return a handle to the returned data.
     * @return {@link quicktime.std.StdQTException}
     */
    @Deprecated
    public QTHandle getExtension(int i, int j) throws StdQTException {
        return QTHandle.fromImageDescription(this, i, j);
    }

    /**
     * Creates an offscreen QDGraphics object from the width, height, depth, and color table
     * of a specified image description structure.
     * QuickTime::NewImageGWorld
     * @param flags holding graphics world flags.
     * @return a QDGraphics.
     * @return {@link quicktime.QTException}
     */
    public QDGraphics newGWorld(int i) throws QTException {
        return new QDGraphics(this, i);
    }

    /**
     * Return the total size of this structure.
     */
    public int getIdSize() {
        return getIntAt(0);
    }

    /**
     * Return the compressor creator type.
     */
    public int getCType() {
        return getIntAt(4);
    }

    /**
     * Return the version of compressed data.
     */
    public int getVersion() {
        return getShortAt(16);
    }

    /**
     * Return the revision level.
     */
    public int getRevisionLevel() {
        return getShortAt(18);
    }

    /**
     * Return the vendor.
     */
    public int getVendor() {
        return getIntAt(20);
    }

    /**
     * Return the temporal Quality.
     */
    public int getTemporalQuality() {
        return getIntAt(24);
    }

    /**
     * Return the Spatial Quality.
     */
    public int getSpatialQuality() {
        return getIntAt(28);
    }

    /**
     * Return the width of the source image.
     */
    public int getWidth() {
        return getShortAt(32);
    }

    /**
     * Return the height of the source image.
     */
    public int getHeight() {
        return getShortAt(34);
    }

    /**
     * Return a QDRect that describes the width and height of the Image the description describes.
     */
    public QDRect getBounds() {
        return new QDRect(0, 0, getShortAt(32), getShortAt(34));
    }

    /**
     * Return the horizontal resolution of the source image.
     */
    public float getHRes() {
        return QTUtils.Fix2X(getIntAt(36));
    }

    /**
     * Return the vertical resolution of the source image.
     */
    public float getVRes() {
        return QTUtils.Fix2X(getIntAt(40));
    }

    /**
     * Return the size of the compressed image.
     */
    public int getDataSize() {
        return getIntAt(44);
    }

    /**
     * Return number of frames in image data.
     */
    public int getFrameCount() {
        return getShortAt(48);
    }

    /**
     * Return name of the compression algorithm.
     */
    public String getName() throws UtilException {
        return getPStringAt(50);
    }

    /**
     * Return pixel depth of source image.
     */
    public int getDepth() {
        return getShortAt(82);
    }

    /**
     * Return ID number of the color table for image.
     */
    public int getClutID() {
        return getShortAt(84);
    }

    /**
     * Set the compressor creator type.
     * @param cType a compressor type.
     */
    public void setCType(int i) {
        setIntAt(4, i);
    }

    /**
     * Set the version.
     * @param version a version number.
     */
    public void setVersion(int i) {
        setShortAt(16, (short) i);
    }

    /**
     * Set the revision level.
     * @param revision a revision number.
     */
    public void setRevisionLevel(int i) {
        setShortAt(18, (short) i);
    }

    /**
     * Set the vendor.
     * @param vendor a developer of the compressor.
     */
    public void setVendor(int i) {
        setIntAt(20, i);
    }

    /**
     * Set temporal quality.
     * @param temporalQuality a degree of temporal compression.
     */
    public void setTemporalQuality(int i) {
        setIntAt(24, i);
    }

    /**
     * Set spatial quality.
     */
    public void setSpatialQuality(int i) {
        setIntAt(28, i);
    }

    /**
     * Set width of source image in pixels.
     * @param width the width of the source image in pixels.
     */
    public void setWidth(int i) {
        setShortAt(32, (short) i);
    }

    /**
     * Set height of source image in pixels.
     * @param height the height of the source image in pixels.
     */
    public void setHeight(int i) {
        setShortAt(34, (short) i);
    }

    /**
     * Set horizontal resolution.
     * @param hRes the horizontal resolution of the source image.
     */
    public void setHRes(float f) {
        setIntAt(36, QTUtils.X2Fix(f));
    }

    /**
     * Set vertical resolution.
     * @param vRes the vertical resolution of the source image.
     */
    public void setVRes(float f) {
        setIntAt(40, QTUtils.X2Fix(f));
    }

    /**
     * Set the data size.
     * @param dataSize the size of the compressed image.
     */
    public void setDataSize(int i) {
        setIntAt(44, i);
    }

    /**
     * Set the frame count.
     * @param frameCount the number of frames in the image data.
     */
    public void setFrameCount(int i) {
        setShortAt(48, (short) i);
    }

    /**
     * Set the name of the compression algorithm.
     * @param name indicates the compression algorithm.
     * @param {@link quicktime.util.UtilException}
     */
    public void setName(String s) throws UtilException {
        setPStringAt(50, 31, s);
    }

    /**
     * Set the pixel depth.
     * @param depth is the pixel depth specified for the compressed image.
     */
    public void setDepth(int i) {
        setShortAt(82, (short) i);
    }

    /**
     * Set the id number of the color table.
     * @param clutID contains the ID of the color table for the compressed image.
     */
    public void setClutID(int i) {
        setShortAt(84, (short) i);
    }

    /**
     * Returns the String representation of this Image Description.
     * @return a string representation of this object.
     */
    public String toString() {
        String s = "";
        try {
            s = getName();
        } catch (QTException qtexception) {
        }
        return getClass().getName() + "[cType=" + QTUtils.fromOSType(getCType()) + ",temporalQuality=" + getTemporalQuality() + ",spatialQulity=" + getSpatialQuality() + ",width=" + getWidth() + ",height=" + getHeight() + ",dataSize=" + getDataSize() + ",frameCount=" + getFrameCount() + ",name=" + s + ",depth=" + getDepth() + "]";
    }

    private static int GraphicsExportDoUseCompressor(int i, int j, int[] ai) {
        return ImageCompressionManager.GraphicsExportDoUseCompressor(i, j, ai);
    }

    private static int GraphicsExportGetInputImageDescription(int i, int[] ai) {
        return ImageCompressionManager.GraphicsExportGetInputImageDescription(i, ai);
    }

    private static int GraphicsImportGetImageDescription(int i, int[] ai) {
        return ImageCompressionManager.GraphicsImportGetImageDescription(i, ai);
    }

    private static short GetSpriteProperty(int i, int j, int[] ai) {
        return SpriteManager.GetSpriteProperty(i, j, ai);
    }

    private static int SCCompressImage(int i, int j, byte[] abyte0, int[] ai, int[] ai1) {
        return ImageCompressionManager.SCCompressImage(i, j, abyte0, ai, ai1);
    }

    private static short MakeImageDescriptionForPixMap(int i, int[] ai) {
        return ImageCompressionManager.MakeImageDescriptionForPixMap(i, ai);
    }

    private static short SetImageDescriptionCTable(int i, int j) {
        return ImageCompressionManager.SetImageDescriptionCTable(i, j);
    }

    private static short GetImageDescriptionExtension(int i, int[] ai, int j, int k) {
        return ImageCompressionManager.GetImageDescriptionExtension(i, ai, j, k);
    }

    private static short AddImageDescriptionExtension(int i, int j, int k) {
        return ImageCompressionManager.AddImageDescriptionExtension(i, j, k);
    }

    private static short RemoveImageDescriptionExtension(int i, int j, int k) {
        return ImageCompressionManager.RemoveImageDescriptionExtension(i, j, k);
    }

    private static short CountImageDescriptionExtensionType(int i, int j, int[] ai) {
        return ImageCompressionManager.CountImageDescriptionExtensionType(i, j, ai);
    }

    private static short GetNextImageDescriptionExtensionType(int i, int[] ai) {
        return ImageCompressionManager.GetNextImageDescriptionExtensionType(i, ai);
    }

    private static short MakeImageDescriptionForEffect(int i, int[] ai) {
        return ImageCompressionManager.MakeImageDescriptionForEffect(i, ai);
    }

    private static void SetGWorld(int i, int j) {
        QuickDrawManager.SetGWorld(i, j);
    }

    private static void GetGWorld(int[] ai, int[] ai1) {
        QuickDrawManager.GetGWorld(ai, ai1);
    }

    static {
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(ImageDescription.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }
}
