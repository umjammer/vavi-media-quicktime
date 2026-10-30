package quicktime.std.qtcomponents;

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
    public void defaultPixMapSettings(PixMap src, boolean motion) throws StdQTException {
        StdQTException.checkError(SCDefaultPixMapSettings(_ID(), QTObject.ID(src), (short) (motion ? 1 : 0)));
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a Pict.
     * @see "QuickTime::SCDefaultPictHandleSettings()"
     * @param src the Pict to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @throws quicktime.std.StdQTException
     */
    public void defaultPictSettings(Pict src, boolean motion) throws StdQTException {
        StdQTException.checkError(SCDefaultPictHandleSettings(_ID(), QTObject.ID(src), (short) (motion ? 1 : 0)));
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a Pict File.
     * @see "QuickTime::SCDefaultPictFileSettings()"
     * @param src the Pict file to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @throws quicktime.std.StdQTException
     */
    public void defaultPictFileSettings(OpenFile src, boolean motion) throws StdQTException {
        StdQTException.checkError(SCDefaultPictFileSettings(_ID(), (short) QTObject.ID(src), (short) (motion ? 1 : 0)));
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
    public QTImage compressImage(QDGraphics src, QDRect srcRect) throws QTException {
        return ImageDescription.fromImageCompressionDialog(this, src, srcRect);
    }

    /**
     * Compresses a Pict returning a new Pict.
     * @see "QuickTime::SCCompressPicture()"
     * @param src the source Pict.
     * @return a Pict
     * @throws quicktime.QTException
     */
    public Pict compressPicture(Pict src) throws QTException {
        return Pict.fromImageCompressionDialog(this, src);
    }

    /**
     * Compresses a Pict contained in the src open Pict file into the destination open Pict file.
     * @see "QuickTime::SCCompressPictureFile()"
     * @param src the source Pict File.
     * @param dest the destination Pict File.
     * @throws quicktime.QTException
     */
    public void compressPictureFile(OpenFile src, OpenFile dest) throws QTException {
        int i = SCCompressPictureFile(_ID(), (short) QTObject.ID(src), (short) QTObject.ID(dest));
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
    public SCSequence compressSequenceBegin(QDGraphics src, QDRect srcRect) throws QTException {
        return new SCSequence(this, src, srcRect);
    }

    /**
     * Sets the current spatial settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param settings the new spatial settings
     * @throws quicktime.std.StdQTException
     */
    public void setInfoSpatialSettings(SpatialSettings settings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1936749676, settings.getBytes()));
    }

    /**
     * Gets the current spatial settings for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the new spatial settings
     * @throws quicktime.std.StdQTException
     */
    public SpatialSettings getInfoSpatialSettings() throws StdQTException {
        SpatialSettings infoSpatialSettings = new SpatialSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1936749676, infoSpatialSettings.getBytes()));
        return infoSpatialSettings;
    }

    /**
     * Sets the current temporal settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param settings the new temporal settings
     * @throws quicktime.std.StdQTException
     */
    public void setInfoTemporalSettings(TemporalSettings settings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1953526380, settings.getBytes()));
    }

    /**
     * Gets the current temporal settings for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the new temporal settings
     * @throws quicktime.std.StdQTException
     */
    public TemporalSettings getInfoTemporalSettings() throws StdQTException {
        TemporalSettings infoTemporalSettings = new TemporalSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1953526380, infoTemporalSettings.getBytes()));
        return infoTemporalSettings;
    }

    /**
     * Sets the current data rate settings for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param settings the new data rate settings
     * @throws quicktime.std.StdQTException
     */
    public void setInfoDataRateSettings(DataRateSettings settings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1685217652, settings.getBytes()));
    }

    /**
     * Gets the current data rate settings for a dialog.
     * @see "QuickTime::SCGetInfo()"
     * @return the new data rate settings
     * @throws quicktime.std.StdQTException
     */
    public DataRateSettings getInfoDataRateSettings() throws StdQTException {
        DataRateSettings infoDataRateSettings = new DataRateSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1685217652, infoDataRateSettings.getBytes()));
        return infoDataRateSettings;
    }

    /**
     * Sets the current color table or null to clear the current color table for a dialog.
     * @see "QuickTime::SCSetInfo()"
     * @param ctab the new color table
     * @throws quicktime.std.StdQTException
     */
    public void setInfoColorTable(ColorTable ctab) throws StdQTException {
        int[] info = { QTObject.ID(ctab) };
        StdQTException.checkError(SCSetInfo(_ID(), 1668052340, info));
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
    public void setTestImagePict(Pict testPict, QDRect testRect, int testFlags) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictHandle(_ID(), QTObject.ID(testPict), testRect != null ? testRect.getRect() : null, (short) testFlags));
    }

    /**
     * Sets the dialog box's test image from a Pict file.
     * @see "QuickTime::SCSetTestImagePictFile()"
     * @param testPict the new test image sourced from an open Pict file.
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @throws quicktime.std.StdQTException
     */
    public void setTestImagePictFile(OpenFile testPict, QDRect testRect, int testFlags) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictFile(_ID(), (short) QTObject.ID(testPict), testRect != null ? testRect.getRect() : null, (short) testFlags));
    }

    /**
     * Sets the dialog box's test image from a PixMap.
     * @see "QuickTime::SCSetTestImagePixMap()"
     * @param testPixMap the new test image
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @throws quicktime.std.StdQTException
     */
    public void setTestImagePixMap(PixMap testPixMap, QDRect testRect, int testFlags) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePixMap(_ID(), QTObject.ID(testPixMap), testRect != null ? testRect.getRect() : null, (short) testFlags));
    }

    /**
     * Creates an offscreen QDGraphics based on the current compression settings.
     * @see "QuickTime::SCNewGWorld()"
     * @param rp the boundaries of the graphics world.
     * @param flags flags as specified to NewGWorld.
     * @return a QDGraphics object
     * @throws quicktime.QTException
     */
    public QDGraphics newGWorld(QDRect rp, int flags) throws QTException {
        return new QDGraphics(this, rp, flags);
    }

    /**
     * Sets the compression flags.
     * @see "QuickTime::SCSetCompressFlags()"
     * @param flags the new flags
     * @throws quicktime.std.StdQTException
     */
    public void setCompressFlags(int flags) throws StdQTException {
        StdQTException.checkError(SCSetCompressFlags(_ID(), flags));
    }

    /**
     * Gets the compression flags.
     * @see "QuickTime::SCGetCompressFlags()"
     */
    public int getCompressFlags() throws StdQTException {
        int[] flags = { 0 };
        StdQTException.checkError(SCGetCompressFlags(_ID(), flags));
        return flags[0];
    }

    /**
     * returns the current sequence identifier .
     * @see "QuickTime::SCGetInfo(scSequenceIDType)"
     */
    public int getSequenceIDType() throws StdQTException {
        int[] info = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936028021, info));
        return info[0];
    }

    /**
     * returns the imagecompressions control flags .
     * @see "QuickTime::SCGetInfo(scCodecFlagsType)"
     */
    public short getCodecFlagsType() throws StdQTException {
        short[] info = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1667656807, info));
        return info[0];
    }

    /**
     * Sets the imagecompressions control flags .
     * @see "QuickTime::SCSetInfo(scCodecFlagsType)"
     */
    public void setCodecFlagsType(int codecFlagsType) throws StdQTException {
        short[] info = { (short) codecFlagsType };
        StdQTException.checkError(SCSetInfo(_ID(), 1667656807, info));
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
        int[] info = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1668114034, info));
        return info[0];
    }

    private static int SCDefaultPixMapSettings(int ci, int src, short motion) {
        return ImageCompressionManager.SCDefaultPixMapSettings(ci, src, motion);
    }

    private static int SCDefaultPictHandleSettings(int ci, int srcPicture, short motion) {
        return ImageCompressionManager.SCDefaultPictHandleSettings(ci, srcPicture, motion);
    }

    private static int SCDefaultPictFileSettings(int ci, short srcRef, short motion) {
        return ImageCompressionManager.SCDefaultPictFileSettings(ci, srcRef, motion);
    }

    private static int SCRequestSequenceSettings(int ci) {
        return ImageCompressionManager.SCRequestSequenceSettings(ci);
    }

    private static int SCCompressPictureFile(int ci, short srcRefNum, short dstRefNum) {
        return ImageCompressionManager.SCCompressPictureFile(ci, srcRefNum, dstRefNum);
    }

    private static int SCGetInfo(int ci, int type, int[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, int info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, int[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCGetInfo(int ci, int type, short[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, short[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCGetInfo(int ci, int type, byte[] info) {
        return ImageCompressionManager.SCGetInfo(ci, type, info);
    }

    private static int SCSetInfo(int ci, int type, byte[] info) {
        return ImageCompressionManager.SCSetInfo(ci, type, info);
    }

    private static int SCSetTestImagePictHandle(int ci, int testPict, byte[] testRect, short testFlags) {
        return ImageCompressionManager.SCSetTestImagePictHandle(ci, testPict, testRect, testFlags);
    }

    private static int SCSetTestImagePictFile(int ci, short testFileRef, byte[] testRect, short testFlags) {
        return ImageCompressionManager.SCSetTestImagePictFile(ci, testFileRef, testRect, testFlags);
    }

    private static int SCSetTestImagePixMap(int ci, int testPixMap, byte[] testRect, short testFlags) {
        return ImageCompressionManager.SCSetTestImagePixMap(ci, testPixMap, testRect, testFlags);
    }

    private static int SCSetCompressFlags(int ci, int flags) {
        return ImageCompressionManager.SCSetCompressFlags(ci, flags);
    }

    private static int SCGetCompressFlags(int ci, int[] flags) {
        return ImageCompressionManager.SCGetCompressFlags(ci, flags);
    }
}
