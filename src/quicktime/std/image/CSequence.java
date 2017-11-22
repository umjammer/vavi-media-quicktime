
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.StdQTException;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;


public final class CSequence extends CDSequence implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(CSequence.class);
                    }
                });
            }
        }.establish();
    }

    public CSequence(QDGraphics paramQDGraphics,
            QDRect paramQDRect,
            int paramInt1,
            CodecComponent paramCodecComponent,
            int paramInt2,
            int paramInt3,
            int paramInt4,
            int paramInt5) throws QTException {
        this(paramQDGraphics, paramQDRect, 0, paramInt1, paramCodecComponent, paramInt2, paramInt3, paramInt4, null, paramInt5);
    }

    public CSequence(QDGraphics paramQDGraphics,
            QDRect paramQDRect,
            int paramInt1,
            int paramInt2,
            CodecComponent paramCodecComponent,
            int paramInt3,
            int paramInt4,
            int paramInt5,
            ColorTable paramColorTable,
            int paramInt6) throws QTException {
        super(begin(paramQDGraphics,
                    paramQDRect,
                    paramInt1,
                    paramInt2,
                    paramCodecComponent,
                    paramInt3,
                    paramInt4,
                    paramInt5,
                    paramColorTable,
                    paramInt6),
              paramQDGraphics);
        this.mImageDescription = csImageDescription;
        csImageDescription = null;
    }

    private static int begin(QDGraphics paramQDGraphics,
                             QDRect paramQDRect,
                             int paramInt1,
                             int paramInt2,
                             CodecComponent paramCodecComponent,
                             int paramInt3,
                             int paramInt4,
                             int paramInt5,
                             ColorTable paramColorTable,
                             int paramInt6) throws QTException {
        int i = 0;
        int[] arrayOfInt = {
            0
        };

        if ((paramQDGraphics == null) || (paramQDRect == null))
            throw new QTException(-50);
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            csImageDescription = new ImageDescription(0);
            i = CompressSequenceBegin(arrayOfInt,
                                      QTObject.ID(paramQDGraphics.getPixMap()),
                                      0,
                                      paramQDRect.getRect(),
                                      null,
                                      (short) paramInt1,
                                      paramInt2,
                                      QTObject.ID(paramCodecComponent),
                                      paramInt3,
                                      paramInt4,
                                      paramInt5,
                                      QTObject.ID(paramColorTable),
                                      (short) paramInt6,
                                      QTObject.ID(csImageDescription));

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    private static ImageDescription csImageDescription;

    public CompressedFrameInfo compressFrame(QDGraphics paramQDGraphics,
                                             QDRect paramQDRect,
                                             int paramInt,
                                             EncodedImage paramEncodedImage) throws StdQTException, QDException {
        if ((paramEncodedImage instanceof RawEncodedImage))
            return compressFrame(paramQDGraphics, paramQDRect, paramInt, (RawEncodedImage) paramEncodedImage);
        if ((paramEncodedImage instanceof IntEncodedImage))
            return compressFrame(paramQDGraphics, paramQDRect, paramInt, (IntEncodedImage) paramEncodedImage);
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            return compressFrame(paramQDGraphics, paramQDRect, paramInt, (ByteEncodedImage) paramEncodedImage);
        }
        throw new StdQTException(-50);
    }

    public CompressedFrameInfo compressFrame(QDGraphics paramQDGraphics,
                                             QDRect paramQDRect,
                                             int paramInt,
                                             IntEncodedImage paramIntEncodedImage) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];

        int i = 0;
        if ((paramQDGraphics == null) || (paramQDRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = CompressSequenceFrame(_ID(),
                                      QTObject.ID(paramQDGraphics.getPixMap()),
                                      paramQDRect.getRect(),
                                      (short) paramInt,
                                      paramIntEncodedImage.getInts(),
                                      arrayOfInt,
                                      arrayOfByte,
                                      0);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);

        return new CompressedFrameInfo(arrayOfInt[0], arrayOfByte[0]);
    }

    public CompressedFrameInfo compressFrame(QDGraphics paramQDGraphics,
                                             QDRect paramQDRect,
                                             int paramInt,
                                             ByteEncodedImage paramByteEncodedImage) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];

        int i = 0;
        if ((paramQDGraphics == null) || (paramQDRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = CompressSequenceFrame(_ID(),
                                      QTObject.ID(paramQDGraphics.getPixMap()),
                                      paramQDRect.getRect(),
                                      (short) paramInt,
                                      paramByteEncodedImage.getBytes(),
                                      arrayOfInt,
                                      arrayOfByte,
                                      0);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);

        return new CompressedFrameInfo(arrayOfInt[0], arrayOfByte[0]);
    }

    public CompressedFrameInfo compressFrame(QDGraphics paramQDGraphics,
                                             QDRect paramQDRect,
                                             int paramInt,
                                             RawEncodedImage paramRawEncodedImage) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];
        int i = 0;

        if ((paramQDGraphics == null) || (paramQDRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = CompressSequenceFrame(_ID(),
                                      QTObject.ID(paramQDGraphics.getPixMap()),
                                      paramQDRect.getRect(),
                                      (short) paramInt,
                                      QTObject.ID(paramRawEncodedImage),
                                      arrayOfInt,
                                      arrayOfByte,
                                      0);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);

        return new CompressedFrameInfo(arrayOfInt[0], arrayOfByte[0]);
    }

    public int getMaxCompressionSize(QDGraphics paramQDGraphics) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = 0;

        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = GetCSequenceMaxCompressionSize(_ID(), QTObject.ID(paramQDGraphics.getPixMap()), arrayOfInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void setQuality(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetCSequenceQuality(_ID(), paramInt1, paramInt2));
    }

    public void setKeyFrameRate(int paramInt) throws StdQTException {
        StdQTException.checkError(SetCSequenceKeyFrameRate(_ID(), paramInt));
    }

    public int getKeyFrameRate() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequenceKeyFrameRate(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    public void setFrameNumber(int paramInt) throws StdQTException {
        StdQTException.checkError(SetCSequenceFrameNumber(_ID(), paramInt));
    }

    public int getFrameNumber() throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequenceFrameNumber(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    public void setPrev(QDGraphics paramQDGraphics, QDRect paramQDRect) throws StdQTException {
        int i = 0;
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = SetCSequencePrev(_ID(),
                                 QTObject.ID(paramQDGraphics.getPixMap()),
                                 paramQDRect != null ? paramQDRect.getRect() : null);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    public QDGraphics prevBuffer() throws StdQTException {
        return QDGraphics.fromCSequence(this);
    }

    public void setDataRateParams(DataRateParams paramDataRateParams) throws StdQTException {
        StdQTException.checkError(SetCSequenceDataRateParams(_ID(), paramDataRateParams.getBytes()));
    }

    public DataRateParams getDataRateParams() throws StdQTException {
        DataRateParams localDataRateParams = new DataRateParams();
        StdQTException.checkError(GetCSequenceDataRateParams(_ID(), localDataRateParams.getBytes()));

        return localDataRateParams;
    }

    public void setPreferredPacketSize(int paramInt) throws StdQTException {
        StdQTException.checkError(SetCSequencePreferredPacketSize(_ID(), paramInt));
    }

    private void setFlushProc(Object paramObject, int paramInt) {
    }

    private static native short CompressSequenceBegin(int[] paramArrayOfInt,
                                                      int paramInt1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte1,
                                                      byte[] paramArrayOfByte2,
                                                      short paramShort1,
                                                      int paramInt3,
                                                      int paramInt4,
                                                      int paramInt5,
                                                      int paramInt6,
                                                      int paramInt7,
                                                      int paramInt8,
                                                      short paramShort2,
                                                      int paramInt9);

    private static native short CompressSequenceFrame(int paramInt1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte1,
                                                      short paramShort,
                                                      byte[] paramArrayOfByte2,
                                                      int[] paramArrayOfInt,
                                                      byte[] paramArrayOfByte3,
                                                      int paramInt3);

    private static native short CompressSequenceFrame(int paramInt1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte1,
                                                      short paramShort,
                                                      int paramInt3,
                                                      int[] paramArrayOfInt,
                                                      byte[] paramArrayOfByte2,
                                                      int paramInt4);

    private static native short CompressSequenceFrame(int paramInt1,
                                                      int paramInt2,
                                                      byte[] paramArrayOfByte1,
                                                      short paramShort,
                                                      int[] paramArrayOfInt1,
                                                      int[] paramArrayOfInt2,
                                                      byte[] paramArrayOfByte2,
                                                      int paramInt3);

    private static native short GetCSequenceMaxCompressionSize(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short SetCSequenceQuality(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetCSequenceKeyFrameRate(int paramInt1, int paramInt2);

    private static native short GetCSequenceKeyFrameRate(int paramInt, int[] paramArrayOfInt);

    private static native short SetCSequenceFrameNumber(int paramInt1, int paramInt2);

    private static native short GetCSequenceFrameNumber(int paramInt, int[] paramArrayOfInt);

    private static native short SetCSequencePrev(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SetCSequenceDataRateParams(int paramInt, byte[] paramArrayOfByte);

    private static native short GetCSequenceDataRateParams(int paramInt, byte[] paramArrayOfByte);

    private static native short SetCSequencePreferredPacketSize(int paramInt1, int paramInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /CSequence.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
