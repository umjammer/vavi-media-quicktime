package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
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
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(ImageCompressionDialog.class);
                    }
                });
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
     * QuickTime::SCDefaultPixMapSettings()
     * @param src the pix map to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @param {@link quicktime.std.StdQTException}
     */
    public void defaultPixMapSettings(PixMap paramPixMap, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPixMapSettings(_ID(), QTObject.ID(paramPixMap), (short) (paramBoolean ? 1 : 0)));
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a Pict.
     * QuickTime::SCDefaultPictHandleSettings()
     * @param src the Pict to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @param {@link quicktime.std.StdQTException}
     */
    public void defaultPictSettings(Pict paramPict, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPictHandleSettings(_ID(), QTObject.ID(paramPict), (short) (paramBoolean ? 1 : 0)));
    }

    /**
     * Allows you to derive default compression settings for an image that is stored in a Pict File.
     * QuickTime::SCDefaultPictFileSettings()
     * @param src the Pict file to be analyzed
     * @param motion set to true if the image is part of a sequence, false if a single image
     * @param {@link quicktime.std.StdQTException}
     */
    public void defaultPictFileSettings(OpenFile paramOpenFile, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPictFileSettings(_ID(), (short) QTObject.ID(paramOpenFile), (short) (paramBoolean ? 1 : 0)));
    }

    /**
     * Displays the standard dialog box to the user for a single image or for sound.
     * QuickTime::SCRequestImageSettings()
     */
    public void requestImageSettings() throws StdQTException {
        requestSettings();
    }

    /**
     * Displays the standard dialog box to the user for a sequence of images.
     * QuickTime::SCRequestSequenceSettings()
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
     * QuickTime::SCCompressImage()
     * @param src the source PixMap is retrieved from the supplied QDGraphics.
     * @param srcRect you can specify to compress just a part of the pix map by specifying
     *  a rectangle - or pass in null to compress the entire pix map.
     * @return an image data object.
     * @return {@link quicktime.QTException}
     */
    public QTImage compressImage(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return ImageDescription.fromImageCompressionDialog(this, paramQDGraphics, paramQDRect);
    }

    /**
     * Compresses a Pict returning a new Pict.
     * QuickTime::SCCompressPicture()
     * @param src the source Pict.
     * @return a Pict
     * @return {@link quicktime.QTException}
     */
    public Pict compressPicture(Pict paramPict) throws QTException {
        return Pict.fromImageCompressionDialog(this, paramPict);
    }

    /**
     * Compresses a Pict contained in the src open Pict file into the destination open Pict file.
     * QuickTime::SCCompressPictureFile()
     * @param src the source Pict File.
     * @param dest the destination Pict File.
     * @param {@link quicktime.QTException}
     */
    public void compressPictureFile(OpenFile paramOpenFile1, OpenFile paramOpenFile2) throws QTException {
        int i = SCCompressPictureFile(_ID(), (short) QTObject.ID(paramOpenFile1), (short) QTObject.ID(paramOpenFile2));
        StdQTException.checkError(i);
    }

    /**
     * Creates a SCSequence object to begin compression of a sequence of images.
     * QuickTime::SCCompressSequenceBegin()
     * @param src the source image
     * @param srcRect which part of the source to compress or null for the entire image.
     * @return a SCSequence object
     * @return {@link quicktime.QTException}
     */
    public SCSequence compressSequenceBegin(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return new SCSequence(this, paramQDGraphics, paramQDRect);
    }

    /**
     * Sets the current spatial settings for a dialog.
     * QuickTime::SCSetInfo()
     * @param settings the new spatial settings
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoSpatialSettings(SpatialSettings paramSpatialSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1936749676, paramSpatialSettings.getBytes()));
    }

    /**
     * Gets the current spatial settings for a dialog.
     * QuickTime::SCGetInfo()
     * @return the new spatial settings
     * @return {@link quicktime.std.StdQTException}
     */
    public SpatialSettings getInfoSpatialSettings() throws StdQTException {
        SpatialSettings localSpatialSettings = new SpatialSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1936749676, localSpatialSettings.getBytes()));
        return localSpatialSettings;
    }

    /**
     * Sets the current temporal settings for a dialog.
     * QuickTime::SCSetInfo()
     * @param settings the new temporal settings
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoTemporalSettings(TemporalSettings paramTemporalSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1953526380, paramTemporalSettings.getBytes()));
    }

    /**
     * Gets the current temporal settings for a dialog.
     * QuickTime::SCGetInfo()
     * @return the new temporal settings
     * @return {@link quicktime.std.StdQTException}
     */
    public TemporalSettings getInfoTemporalSettings() throws StdQTException {
        TemporalSettings localTemporalSettings = new TemporalSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1953526380, localTemporalSettings.getBytes()));
        return localTemporalSettings;
    }

    /**
     * Sets the current data rate settings for a dialog.
     * QuickTime::SCSetInfo()
     * @param settings the new data rate settings
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoDataRateSettings(DataRateSettings paramDataRateSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1685217652, paramDataRateSettings.getBytes()));
    }

    /**
     * Gets the current data rate settings for a dialog.
     * QuickTime::SCGetInfo()
     * @return the new data rate settings
     * @return {@link quicktime.std.StdQTException}
     */
    public DataRateSettings getInfoDataRateSettings() throws StdQTException {
        DataRateSettings localDataRateSettings = new DataRateSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1685217652, localDataRateSettings.getBytes()));
        return localDataRateSettings;
    }

    /**
     * Sets the current color table or null to clear the current color table for a dialog.
     * QuickTime::SCSetInfo()
     * @param ctab the new color table
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInfoColorTable(ColorTable paramColorTable) throws StdQTException {
        int[] arrayOfInt = { QTObject.ID(paramColorTable) };
        StdQTException.checkError(SCSetInfo(_ID(), 1668052340, arrayOfInt));
    }

    /**
     * Gets the current color table or null to clear the current color table for a dialog.
     * QuickTime::SCGetInfo()
     * @return the current color table or null if no color table being used.
     * @return {@link quicktime.QTException}
     */
    public ColorTable getInfoColorTable() throws QTException {
        return ColorTable.fromCompressionDialog(this);
    }

    /**
     * Sets the dialog box's test image from a Pict.
     * QuickTime::SCSetTestImagePictHandle()
     * @param testPict the new test image
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTestImagePict(Pict paramPict, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictHandle(_ID(), QTObject.ID(paramPict), paramQDRect != null ? paramQDRect.getRect() : null, (short) paramInt));
    }

    /**
     * Sets the dialog box's test image from a Pict file.
     * QuickTime::SCSetTestImagePictFile()
     * @param testPict the new test image sourced from an open Pict file.
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTestImagePictFile(OpenFile paramOpenFile, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictFile(_ID(), (short) QTObject.ID(paramOpenFile), paramQDRect != null ? paramQDRect.getRect() : null, (short) paramInt));
    }

    /**
     * Sets the dialog box's test image from a PixMap.
     * QuickTime::SCSetTestImagePixMap()
     * @param testPixMap the new test image
     * @param testRect the area of interest to display or null for the entire picture
     * @param testFlags controls how the dialog display's the image
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTestImagePixMap(PixMap paramPixMap, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePixMap(_ID(), QTObject.ID(paramPixMap), paramQDRect != null ? paramQDRect.getRect() : null, (short) paramInt));
    }

    /**
     * Creates an offscreen QDGraphics based on the current compression settings.
     * QuickTime::SCNewGWorld()
     * @param rp the boundaries of the graphics world.
     * @param flags flags as specified to NewGWorld.
     * @return a QDGraphics object
     * @return {@link quicktime.QTException}
     */
    public QDGraphics newGWorld(QDRect paramQDRect, int paramInt) throws QTException {
        return new QDGraphics(this, paramQDRect, paramInt);
    }

    /**
     * Sets the compression flags.
     * QuickTime::SCSetCompressFlags()
     * @param flags the new flags
     * @param {@link quicktime.std.StdQTException}
     */
    public void setCompressFlags(int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetCompressFlags(_ID(), paramInt));
    }

    /**
     * Gets the compression flags.
     * QuickTime::SCGetCompressFlags()
     */
    public int getCompressFlags() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetCompressFlags(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * returns the current sequence identifier .
     * QuickTime::SCGetInfo(scSequenceIDType)
     */
    public int getSequenceIDType() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1936028021, arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * returns the imagecompressions control flags .
     * QuickTime::SCGetInfo(scCodecFlagsType)
     */
    public short getCodecFlagsType() throws StdQTException {
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1667656807, arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * Sets the imagecompressions control flags .
     * QuickTime::SCSetInfo(scCodecFlagsType)
     */
    public void setCodecFlagsType(int paramInt) throws StdQTException {
        short[] arrayOfShort = { (short) paramInt };
        StdQTException.checkError(SCSetInfo(_ID(), 1667656807, arrayOfShort));
    }

    /**
     * returns the imagecompressions control flags .
     * QuickTime::SCGetInfo(scCodecSettingsType)
     */
    public QTHandle getCodecSettingsType() throws StdQTException {
        return QTHandle.fromImageCompressionDialog(this);
    }

    /**
     * returns the Codec ManufacturerType  .
     * QuickTime::SCGetInfo(scCodecFlagsType)
     */
    public int getCodecManufacturerType() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(SCGetInfo(_ID(), 1668114034, arrayOfInt));
        return arrayOfInt[0];
    }

    private static native int SCDefaultPixMapSettings(int paramInt1, int paramInt2, short paramShort);

    private static native int SCDefaultPictHandleSettings(int paramInt1, int paramInt2, short paramShort);

    private static native int SCDefaultPictFileSettings(int paramInt, short paramShort1, short paramShort2);

    private static native int SCRequestSequenceSettings(int paramInt);

    private static native int SCCompressPictureFile(int paramInt, short paramShort1, short paramShort2);

    private static native int SCGetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SCSetInfo(int paramInt1, int paramInt2, int paramInt3);

    private static native int SCSetInfo(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SCGetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SCSetInfo(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native int SCGetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SCSetInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SCSetTestImagePictHandle(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort);

    private static native int SCSetTestImagePictFile(int paramInt, short paramShort1, byte[] paramArrayOfByte, short paramShort2);

    private static native int SCSetTestImagePixMap(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort);

    private static native int SCSetCompressFlags(int paramInt1, int paramInt2);

    private static native int SCGetCompressFlags(int paramInt, int[] paramArrayOfInt);
}

