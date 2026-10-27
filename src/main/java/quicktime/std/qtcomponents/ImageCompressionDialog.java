package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.io.OpenFile;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.Pict;
import quicktime.qd.PixMap;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.QTImage;
import quicktime.util.QTHandle;

/**
 * This class represents the standard compression dialog for images.
 */
public final class ImageCompressionDialog extends CompressionDialog implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(ImageCompressionDialog.class));
            }
        }.establish();
    }

    /**
     * Opens the CompressionDialog component for an Image compression dialog.
     */
    public ImageCompressionDialog() throws QTException {
        super(1768776039);
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a pixel map.
     * @see "QuickTime::SCDefaultPixMapSettings()"
     * @param src the pix map to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @throws quicktime.std.StdQTException
     */
    public void defaultPixMapSettings(PixMap paramPixMap, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPixMapSettings(_ID(), QTObject.ID(paramPixMap), (short) (paramBoolean ? 1 : 0)));
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a Pict.
     * @see "QuickTime::SCDefaultPictHandleSettings()"
     * @param src the Pict to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @throws quicktime.std.StdQTException
     */
    public void defaultPictSettings(Pict paramPict, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPictHandleSettings(_ID(), QTObject.ID(paramPict), (short) (paramBoolean ? 1 : 0)));
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a Pict File.
     * @see "QuickTime::SCDefaultPictFileSettings()"
     * @param src the Pict file to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @throws quicktime.std.StdQTException
     */
    public void defaultPictFileSettings(OpenFile paramOpenFile, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPictFileSettings(_ID(), (short) QTObject.ID(paramOpenFile), (short) (paramBoolean ? 1 : 0)));
    }

    /**
     * Displays the standard dialog box to the user for a single image or for sound.
     * @see "QuickTime::SCRequestImageSettings()"
     */
    public void requestImageSettings() throws StdQTException {
        requestSettings();
    }

    /**
     * Displays the standard dialog box to the user for a sequence of images.
     * @see "QuickTime::SCRequestSequenceSettings()"
     */
    public void requestSequenceSettings() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SCRequestSequenceSettings(_ID());
            if (i == 1)
                throw new StdQTException(-128);
            StdQTException.checkError(i);
        }
    }

    /**
     * Compresses an image that is sourced by a PixMap from the supplied QDGraphics, returning
     * the compressed data and an image description.
     * @see "QuickTime::SCCompressImage()"
     * @param src the source PixMap is retrieved from the supplied QDGraphics.
     * @param srcRect you can specify to compress just a part of the pix map by specifying
     *  a rectangle - or pass in null to compress the entire pix map.
     * @return an image data object.
     * @throws quicktime.QTException
     */
    public QTImage compressImage(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return ImageDescription.fromImageCompressionDialog(this, paramQDGraphics, paramQDRect);
    }

    /**
     * Compresses a Pict returning a new Pict.
     * @see "QuickTime::SCCompressPicture()"
     * @param src the source Pict.
     * @return a Pict
     * @throws quicktime.QTException
     */
    public Pict compressPicture(Pict paramPict) throws QTException {
        return Pict.fromImageCompressionDialog(this, paramPict);
    }

    /**
     * Compresses a Pict contained in the src open Pict file into the destination open Pict file.
     * @see "QuickTime::SCCompressPictureFile()"
     * @param src the source Pict File.
     * @param dest the destination Pict File.
     * @throws quicktime.QTException
     */
    public void compressPictureFile(OpenFile paramOpenFile1, OpenFile paramOpenFile2) throws QTException {
        int i = SCCompressPictureFile(_ID(), (short) QTObject.ID(paramOpenFile1), (short) QTObject.ID(paramOpenFile2));
        StdQTException.checkError(i);
    }

    /**
     * Creates a SCSequence object to begin compression of a sequence of images.
     * @see "QuickTime::SCCompressSequenceBegin()"
     * @param src the source image
     * @param srcRect which part of the source to compress or null for the entire image.
     * @return a SCSequence object
     * @throws quicktime.QTException
     */
    public SCSequence compressSequenceBegin(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return new SCSequence(this, paramQDGraphics, paramQDRect);
    }

    /**
     * Sets the current spatial settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param settings the new spatial settings
     * @throws quicktime.std.StdQTException
     */
    public void setInfoSpatialSettings(SpatialSettings paramSpatialSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1936749676, paramSpatialSettings.getBytes()));
    }

    /**
     * Gets the current spatial settings for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the new spatial settings
     * @throws quicktime.std.StdQTException
     */
    public SpatialSettings getInfoSpatialSettings() throws StdQTException {
        SpatialSettings localSpatialSettings = new SpatialSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1936749676, localSpatialSettings.getBytes()));
        return localSpatialSettings;
    }

    /**
     * Sets the current temporal settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param settings the new temporal settings
     * @throws quicktime.std.StdQTException
     */
    public void setInfoTemporalSettings(TemporalSettings paramTemporalSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1953526380, paramTemporalSettings.getBytes()));
    }

    /**
     * Gets the current temporal settings for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the new temporal settings
     * @throws quicktime.std.StdQTException
     */
    public TemporalSettings getInfoTemporalSettings() throws StdQTException {
        TemporalSettings localTemporalSettings = new TemporalSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1953526380, localTemporalSettings.getBytes()));
        return localTemporalSettings;
    }

    /**
     * Sets the current data rate settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param settings the new data rate settings
     * @throws quicktime.std.StdQTException
     */
    public void setInfoDataRateSettings(DataRateSettings paramDataRateSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1685217652, paramDataRateSettings.getBytes()));
    }

    /**
     * Gets the current data rate settings for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the new data rate settings
     * @throws quicktime.std.StdQTException
     */
    public DataRateSettings getInfoDataRateSettings() throws StdQTException {
        DataRateSettings localDataRateSettings = new DataRateSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1685217652, localDataRateSettings.getBytes()));
        return localDataRateSettings;
    }

    /**
     * Sets the current color table or null to clear the current color table for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param ctab the new color table
     * @throws quicktime.std.StdQTException
     */
    public void setInfoColorTable(ColorTable paramColorTable) throws StdQTException {
        int[] arrayOfInt = { QTObject.ID(paramColorTable) };
        StdQTException.checkError(SCSetInfo(_ID(), 1668052340, arrayOfInt));
    }

    /**
     * Gets the current color table or null to clear the current color table for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the current color table or null if no color table being used.
     * @throws quicktime.QTException
     */
    public ColorTable getInfoColorTable() throws QTException {
        return ColorTable.fromCompressionDialog(this);
    }

    /**
     * Sets the dialog box's test image from a Pict.
     * @see "QuickTime::SCSetTestImagePictHandle()"
     * @param testPict the new test image
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @throws quicktime.std.StdQTException
     */
    public void setTestImagePict(Pict paramPict, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictHandle(_ID(), QTObject.ID(paramPict), paramQDRect != null ? paramQDRect.getRect() : null, (short) paramInt));
    }

    /**
     * Sets the dialog box's test image from a Pict file.
     * @see "QuickTime::SCSetTestImagePictFile()"
     * @param testPict the new test image sourced from an open Pict file.
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @throws quicktime.std.StdQTException
     */
    public void setTestImagePictFile(OpenFile paramOpenFile, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictFile(_ID(), (short) QTObject.ID(paramOpenFile), paramQDRect != null ? paramQDRect.getRect() : null, (short) paramInt));
    }

    /**
     * Sets the dialog box's test image from a PixMap.
     * @see "QuickTime::SCSetTestImagePixMap()"
     * @param testPixMap the new test image
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @throws quicktime.std.StdQTException
     */
    public void setTestImagePixMap(PixMap paramPixMap, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePixMap(_ID(), QTObject.ID(paramPixMap), paramQDRect != null ? paramQDRect.getRect() : null, (short) paramInt));
    }

    /**
     * Creates an offscreen QDGraphics based on the current compression settings.
     * @see "QuickTime::SCNewGWorld()"
     * @param rp the boundaries of the graphics world.
     * @param flags flags as specified to NewGWorld.
     * @return a QDGraphics object
     * @throws quicktime.QTException
     */
    public QDGraphics newGWorld(QDRect paramQDRect, int paramInt) throws QTException {
        return new QDGraphics(this, paramQDRect, paramInt);
    }

    /**
     * Sets the compression flags.
     * @see "QuickTime::SCSetCompressFlags()"
     * @param flags the new flags
     * @throws quicktime.std.StdQTException
     */
    public void setCompressFlags(int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetCompressFlags(_ID(), paramInt));
    }

    /**
     * Gets the compression flags.
     * @see "QuickTime::SCGetCompressFlags()"
     */
    public int getCompressFlags() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetCompressFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * returns the current sequence identifier .
     * @see "QuickTime::SCGetInfo(scSequenceIDType)"
     */
    public int getSequenceIDType() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936028021, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * returns the imagecompressions control flags .
     * @see "QuickTime::SCGetInfo(scCodecFlagsType)"
     */
    public short getCodecFlagsType() throws StdQTException {
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1667656807, arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * Sets the imagecompressions control flags .
     * @see "QuickTime::SCSetInfo(scCodecFlagsType)"
     */
    public void setCodecFlagsType(int paramInt) throws StdQTException {
        short[] arrayOfShort = { (short) paramInt };
        StdQTException.checkError(SCSetInfo(_ID(), 1667656807, arrayOfShort));
    }

    /**
     * returns the imagecompressions control flags .
     * @see "QuickTime::SCGetInfo(scCodecSettingsType)"
     */
    public QTHandle getCodecSettingsType() throws StdQTException {
        return QTHandle.fromImageCompressionDialog(this);
    }

    /**
     * returns the Codec ManufacturerType  .
     * @see "QuickTime::SCGetInfo(scCodecFlagsType)"
     */
    public int getCodecManufacturerType() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1668114034, arrayOfInt));
        return arrayOfInt[0];
    }

    private static int SCDefaultPixMapSettings(int paramInt1, int paramInt2, short paramShort) {
        return ImageCompressionManager.SCDefaultPixMapSettings(paramInt1, paramInt2, paramShort);
    }

    private static int SCDefaultPictHandleSettings(int paramInt1, int paramInt2, short paramShort) {
        return ImageCompressionManager.SCDefaultPictHandleSettings(paramInt1, paramInt2, paramShort);
    }

    private static int SCDefaultPictFileSettings(int paramInt, short paramShort1, short paramShort2) {
        return ImageCompressionManager.SCDefaultPictFileSettings(paramInt, paramShort1, paramShort2);
    }

    private static int SCRequestSequenceSettings(int paramInt) {
        return ImageCompressionManager.SCRequestSequenceSettings(paramInt);
    }

    private static int SCCompressPictureFile(int paramInt, short paramShort1, short paramShort2) {
        return ImageCompressionManager.SCCompressPictureFile(paramInt, paramShort1, paramShort2);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, int paramInt3) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramInt3);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfShort);
    }

    private static int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SCGetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SCSetInfo(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SCSetTestImagePictHandle(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort) {
        return ImageCompressionManager.SCSetTestImagePictHandle(paramInt1, paramInt2, paramArrayOfByte, paramShort);
    }

    private static int SCSetTestImagePictFile(int paramInt, short paramShort1, byte[] paramArrayOfByte, short paramShort2) {
        return ImageCompressionManager.SCSetTestImagePictFile(paramInt, paramShort1, paramArrayOfByte, paramShort2);
    }

    private static int SCSetTestImagePixMap(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort) {
        return ImageCompressionManager.SCSetTestImagePixMap(paramInt1, paramInt2, paramArrayOfByte, paramShort);
    }

    private static int SCSetCompressFlags(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SCSetCompressFlags(paramInt1, paramInt2);
    }

    private static int SCGetCompressFlags(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.SCGetCompressFlags(paramInt, paramArrayOfInt);
    }
}

