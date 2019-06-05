
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

    public ImageCompressionDialog() throws QTException {
        super(1768776039);
    }

    public void defaultPixMapSettings(PixMap paramPixMap, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPixMapSettings(_ID(), QTObject.ID(paramPixMap), (short) (paramBoolean ? 1 : 0)));
    }

    public void defaultPictSettings(Pict paramPict, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(SCDefaultPictHandleSettings(_ID(), QTObject.ID(paramPict), (short) (paramBoolean ? 1 : 0)));
    }

    public void defaultPictFileSettings(OpenFile paramOpenFile, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(SCDefaultPictFileSettings(_ID(),
                                                      (short) QTObject.ID(paramOpenFile),
                                                      (short) (paramBoolean ? 1 : 0)));
    }

    public void requestImageSettings() throws StdQTException {
        requestSettings();
    }

    public void requestSequenceSettings() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SCRequestSequenceSettings(_ID());
            if (i == 1)
                throw new StdQTException(-128);
            StdQTException.checkError(i);
        }
    }

    public QTImage compressImage(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return ImageDescription.fromImageCompressionDialog(this, paramQDGraphics, paramQDRect);
    }

    public Pict compressPicture(Pict paramPict) throws QTException {
        return Pict.fromImageCompressionDialog(this, paramPict);
    }

    public void compressPictureFile(OpenFile paramOpenFile1, OpenFile paramOpenFile2) throws QTException {
        int i = SCCompressPictureFile(_ID(), (short) QTObject.ID(paramOpenFile1), (short) QTObject.ID(paramOpenFile2));
        StdQTException.checkError(i);
    }

    public SCSequence compressSequenceBegin(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        return new SCSequence(this, paramQDGraphics, paramQDRect);
    }

    public void setInfoSpatialSettings(SpatialSettings paramSpatialSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1936749676, paramSpatialSettings.getBytes()));
    }

    public SpatialSettings getInfoSpatialSettings() throws StdQTException {
        SpatialSettings localSpatialSettings = new SpatialSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1936749676, localSpatialSettings.getBytes()));

        return localSpatialSettings;
    }

    public void setInfoTemporalSettings(TemporalSettings paramTemporalSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1953526380, paramTemporalSettings.getBytes()));
    }

    public TemporalSettings getInfoTemporalSettings() throws StdQTException {
        TemporalSettings localTemporalSettings = new TemporalSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1953526380, localTemporalSettings.getBytes()));

        return localTemporalSettings;
    }

    public void setInfoDataRateSettings(DataRateSettings paramDataRateSettings) throws StdQTException {
        StdQTException.checkError(SCSetInfo(_ID(), 1685217652, paramDataRateSettings.getBytes()));
    }

    public DataRateSettings getInfoDataRateSettings() throws StdQTException {
        DataRateSettings localDataRateSettings = new DataRateSettings();
        StdQTException.checkError(SCGetInfo(_ID(), 1685217652, localDataRateSettings.getBytes()));

        return localDataRateSettings;
    }

    public void setInfoColorTable(ColorTable paramColorTable) throws StdQTException {
        int[] arrayOfInt = {
            QTObject.ID(paramColorTable)
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1668052340, arrayOfInt));
    }

    public ColorTable getInfoColorTable() throws QTException {
        return ColorTable.fromCompressionDialog(this);
    }

    public void setTestImagePict(Pict paramPict, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException
                .checkError(SCSetTestImagePictHandle(_ID(),
                                                     QTObject.ID(paramPict),
                                                     paramQDRect != null ? paramQDRect.getRect() : null,
                                                     (short) paramInt));
    }

    public void setTestImagePictFile(OpenFile paramOpenFile, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePictFile(_ID(),
                                                         (short) QTObject.ID(paramOpenFile),
                                                         paramQDRect != null ? paramQDRect.getRect() : null,
                                                         (short) paramInt));
    }

    public void setTestImagePixMap(PixMap paramPixMap, QDRect paramQDRect, int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetTestImagePixMap(_ID(),
                                                       QTObject.ID(paramPixMap),
                                                       paramQDRect != null ? paramQDRect.getRect() : null,
                                                       (short) paramInt));
    }

    public QDGraphics newGWorld(QDRect paramQDRect, int paramInt) throws QTException {
        return new QDGraphics(this, paramQDRect, paramInt);
    }

    public void setCompressFlags(int paramInt) throws StdQTException {
        StdQTException.checkError(SCSetCompressFlags(_ID(), paramInt));
    }

    public int getCompressFlags() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetCompressFlags(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public int getSequenceIDType() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1936028021, arrayOfInt));

        return arrayOfInt[0];
    }

    public short getCodecFlagsType() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        StdQTException.checkError(SCGetInfo(_ID(), 1667656807, arrayOfShort));

        return arrayOfShort[0];
    }

    public void setCodecFlagsType(int paramInt) throws StdQTException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        StdQTException.checkError(SCSetInfo(_ID(), 1667656807, arrayOfShort));
    }

    public QTHandle getCodecSettingsType() throws StdQTException {
        return QTHandle.fromImageCompressionDialog(this);
    }

    public int getCodecManufacturerType() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
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

    private static native int SCSetTestImagePictFile(int paramInt,
                                                     short paramShort1,
                                                     byte[] paramArrayOfByte,
                                                     short paramShort2);

    private static native int SCSetTestImagePixMap(int paramInt1, int paramInt2, byte[] paramArrayOfByte, short paramShort);

    private static native int SCSetCompressFlags(int paramInt1, int paramInt2);

    private static native int SCGetCompressFlags(int paramInt, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/ImageCompressionDialog.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
