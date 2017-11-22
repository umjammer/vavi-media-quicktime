
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.qd.PixMap;
import quicktime.qd.QDColor;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.qtcomponents.TimeCodeDef;
import quicktime.std.qtcomponents.TimeCodeTime;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;


public final class DSequence extends CDSequence {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DSequence.class);
                    }
                });
            }
        }.establish();
    }

    public DSequence(ImageDescription paramImageDescription,
            QDGraphics paramQDGraphics,
            QDRect paramQDRect,
            Matrix paramMatrix,
            Region paramRegion,
            int paramInt1,
            int paramInt2,
            CodecComponent paramCodecComponent) throws QTException {
        super(begin(paramImageDescription,
                    paramQDGraphics,
                    paramQDRect,
                    paramMatrix,
                    paramRegion,
                    paramInt1,
                    paramInt2,
                    paramCodecComponent),
              paramQDGraphics);
        this.mImageDescription = paramImageDescription;
    }

    public DSequence(ImageDescription paramImageDescription,
            EncodedImage paramEncodedImage,
            QDGraphics paramQDGraphics,
            QDRect paramQDRect,
            Matrix paramMatrix,
            Region paramRegion,
            int paramInt1,
            int paramInt2,
            CodecComponent paramCodecComponent) throws QTException {
        super(beginS(paramImageDescription,
                     paramEncodedImage,
                     paramQDGraphics,
                     paramQDRect,
                     paramMatrix,
                     paramRegion,
                     paramInt1,
                     paramInt2,
                     paramCodecComponent),
              paramQDGraphics);
        this.mImageDescription = paramImageDescription;
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    private static int codecDSeqFlushInstdOfDirtying = 256;

    private static int begin(ImageDescription paramImageDescription,
                             QDGraphics paramQDGraphics,
                             QDRect paramQDRect,
                             Matrix paramMatrix,
                             Region paramRegion,
                             int paramInt1,
                             int paramInt2,
                             CodecComponent paramCodecComponent) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = 0;

        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = DecompressSequenceBegin(arrayOfInt,
                                        QTObject.ID(paramImageDescription),
                                        QTObject.ID(paramQDGraphics),
                                        0,
                                        paramQDRect == null ? null : paramQDRect.getRect(),
                                        paramMatrix == null ? null : paramMatrix.getBytes(),
                                        (short) 0,
                                        QTObject.ID(paramRegion),
                                        (short) paramInt1,
                                        paramInt2,
                                        QTObject.ID(paramCodecComponent));

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);

        if (QTSession.isCurrentOS(4)) {
            StdQTException
                    .checkError(SetDSequenceFlags(arrayOfInt[0], codecDSeqFlushInstdOfDirtying, codecDSeqFlushInstdOfDirtying));
        }
        return arrayOfInt[0];
    }

    private static int beginS(ImageDescription paramImageDescription,
                              EncodedImage paramEncodedImage,
                              QDGraphics paramQDGraphics,
                              QDRect paramQDRect,
                              Matrix paramMatrix,
                              Region paramRegion,
                              int paramInt1,
                              int paramInt2,
                              CodecComponent paramCodecComponent) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = -50;

        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);

            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = DecompressSequenceBeginS(arrayOfInt,
                                             QTObject.ID(paramImageDescription),
                                             ((ByteEncodedImage) paramEncodedImage).getBytes(),
                                             paramEncodedImage.getSize(),
                                             QTObject.ID(paramQDGraphics),
                                             0,
                                             paramQDRect == null ? null : paramQDRect.getRect(),
                                             paramMatrix == null ? null : paramMatrix.getBytes(),
                                             (short) 0,
                                             QTObject.ID(paramRegion),
                                             (short) paramInt1,
                                             paramInt2,
                                             QTObject.ID(paramCodecComponent));

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = DecompressSequenceBeginS(arrayOfInt,
                                             QTObject.ID(paramImageDescription),
                                             ((IntEncodedImage) paramEncodedImage).getInts(),
                                             paramEncodedImage.getSize(),
                                             QTObject.ID(paramQDGraphics),
                                             0,
                                             paramQDRect == null ? null : paramQDRect.getRect(),
                                             paramMatrix == null ? null : paramMatrix.getBytes(),
                                             (short) 0,
                                             QTObject.ID(paramRegion),
                                             (short) paramInt1,
                                             paramInt2,
                                             QTObject.ID(paramCodecComponent));

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = DecompressSequenceBeginS(arrayOfInt,
                                             QTObject.ID(paramImageDescription),
                                             QTObject.ID((RawEncodedImage) paramEncodedImage),
                                             paramEncodedImage.getSize(),
                                             QTObject.ID(paramQDGraphics),
                                             0,
                                             paramQDRect == null ? null : paramQDRect.getRect(),
                                             paramMatrix == null ? null : paramMatrix.getBytes(),
                                             (short) 0,
                                             QTObject.ID(paramRegion),
                                             (short) paramInt1,
                                             paramInt2,
                                             QTObject.ID(paramCodecComponent));
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);

        if (QTSession.isCurrentOS(4))
            StdQTException
                    .checkError(SetDSequenceFlags(arrayOfInt[0], codecDSeqFlushInstdOfDirtying, codecDSeqFlushInstdOfDirtying));
        return arrayOfInt[0];
    }

    public int decompressFrameS(EncodedImage paramEncodedImage, int paramInt) throws StdQTException {
        if ((paramEncodedImage instanceof RawEncodedImage))
            return decompressFrameS((RawEncodedImage) paramEncodedImage, paramInt);
        if ((paramEncodedImage instanceof IntEncodedImage))
            return decompressFrameS((IntEncodedImage) paramEncodedImage, paramInt);
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            return decompressFrameS((ByteEncodedImage) paramEncodedImage, paramInt);
        }
        throw new StdQTException(-50);
    }

    public int decompressFrameS(ByteEncodedImage paramByteEncodedImage, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameS(_ID(),
                                         paramByteEncodedImage.getBytes(),
                                         paramByteEncodedImage.getSize(),
                                         (short) paramInt,
                                         arrayOfShort,
                                         0);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrameS(IntEncodedImage paramIntEncodedImage, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameS(_ID(),
                                         paramIntEncodedImage.getInts(),
                                         paramIntEncodedImage.getSize(),
                                         (short) paramInt,
                                         arrayOfShort,
                                         0);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrameS(RawEncodedImage paramRawEncodedImage, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameS(_ID(),
                                         QTObject.ID(paramRawEncodedImage),
                                         paramRawEncodedImage.getSize(),
                                         (short) paramInt,
                                         arrayOfShort,
                                         0);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrame(EncodedImage paramEncodedImage, int paramInt) throws StdQTException {
        if ((paramEncodedImage instanceof RawEncodedImage))
            return decompressFrame((RawEncodedImage) paramEncodedImage, paramInt);
        if ((paramEncodedImage instanceof IntEncodedImage))
            return decompressFrame((IntEncodedImage) paramEncodedImage, paramInt);
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            return decompressFrame((ByteEncodedImage) paramEncodedImage, paramInt);
        }
        throw new StdQTException(-50);
    }

    public int decompressFrame(ByteEncodedImage paramByteEncodedImage, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrame(_ID(), paramByteEncodedImage.getBytes(), (short) paramInt, arrayOfShort, 0);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrame(IntEncodedImage paramIntEncodedImage, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrame(_ID(), paramIntEncodedImage.getInts(), (short) paramInt, arrayOfShort, 0);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrame(RawEncodedImage paramRawEncodedImage, int paramInt) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrame(_ID(), QTObject.ID(paramRawEncodedImage), (short) paramInt, arrayOfShort, 0);

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrameWhen(ByteEncodedImage paramByteEncodedImage,
                                   int paramInt,
                                   ICMFrameTime paramICMFrameTime) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(),
                                            paramByteEncodedImage.getBytes(),
                                            paramByteEncodedImage.getSize(),
                                            (short) paramInt,
                                            arrayOfShort,
                                            0,
                                            paramICMFrameTime.getBytes());

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrameWhen(IntEncodedImage paramIntEncodedImage,
                                   int paramInt,
                                   ICMFrameTime paramICMFrameTime) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(),
                                            paramIntEncodedImage.getInts(),
                                            paramIntEncodedImage.getSize(),
                                            (short) paramInt,
                                            arrayOfShort,
                                            0,
                                            paramICMFrameTime.getBytes());

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrameWhen(RawEncodedImage paramRawEncodedImage,
                                   int paramInt,
                                   ICMFrameTime paramICMFrameTime) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(),
                                            QTObject.ID(paramRawEncodedImage),
                                            paramRawEncodedImage.getSize(),
                                            (short) paramInt,
                                            arrayOfShort,
                                            0,
                                            paramICMFrameTime.getBytes());

        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public int decompressFrameWhen(EncodedImage paramEncodedImage,
                                   int paramInt,
                                   ICMFrameTime paramICMFrameTime) throws StdQTException {
        if ((paramEncodedImage instanceof RawEncodedImage))
            return decompressFrameWhen((RawEncodedImage) paramEncodedImage, paramInt, paramICMFrameTime);
        if ((paramEncodedImage instanceof IntEncodedImage))
            return decompressFrameWhen((IntEncodedImage) paramEncodedImage, paramInt, paramICMFrameTime);
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            return decompressFrameWhen((ByteEncodedImage) paramEncodedImage, paramInt, paramICMFrameTime);
        }
        throw new StdQTException(-50);
    }

    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        StdQTException.checkError(SetDSequenceTransferMode(_ID(),
                                                           (short) paramGraphicsMode.getGraphicsMode(),
                                                           paramGraphicsMode.getColor().getRGBColor()));
    }

    public void setTransferMode(int paramInt, QDColor paramQDColor) throws StdQTException {
        setGraphicsMode(new GraphicsMode(paramInt, paramQDColor));
    }

    public void setSrcRect(QDRect paramQDRect) throws StdQTException {
        StdQTException.checkError(SetDSequenceSrcRect(_ID(), paramQDRect.getRect()));
    }

    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        StdQTException.checkError(SetDSequenceMatrix(_ID(), paramMatrix.getBytes()));
    }

    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        StdQTException.checkError(GetDSequenceMatrix(_ID(), localMatrix.getBytes()));
        return localMatrix;
    }

    public void setFlags(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(SetDSequenceFlags(_ID(), paramInt1, paramInt2));
    }

    public void setMask(Region paramRegion) throws StdQTException {
        StdQTException.checkError(SetDSequenceMask(_ID(), QTObject.ID(paramRegion)));
    }

    public void setMatte(PixMap paramPixMap, QDRect paramQDRect) throws StdQTException {
        StdQTException.checkError(SetDSequenceMatte(_ID(), QTObject.ID(paramPixMap), paramQDRect.getRect()));
    }

    public void setAccuracy(int paramInt) throws StdQTException {
        StdQTException.checkError(SetDSequenceAccuracy(_ID(), paramInt));
    }

    public QDGraphics getImageBuffer() throws StdQTException {
        return QDGraphics.fromDSequenceImage(this);
    }

    public QDGraphics getScreenBuffer() throws StdQTException {
        return QDGraphics.fromDSequenceScreen(this);
    }

    public boolean ptInData(EncodedImage paramEncodedImage, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = -50;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = PtInDSequenceData(_ID(),
                                  ((ByteEncodedImage) paramEncodedImage).getBytes(),
                                  paramEncodedImage.getSize(),
                                  paramQDPoint.getPoint(),
                                  arrayOfByte);

        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = PtInDSequenceData(_ID(),
                                  ((IntEncodedImage) paramEncodedImage).getInts(),
                                  paramEncodedImage.getSize(),
                                  paramQDPoint.getPoint(),
                                  arrayOfByte);

        } else if ((paramEncodedImage instanceof RawEncodedImage)) {
            i = PtInDSequenceData(_ID(),
                                  QTObject.ID((RawEncodedImage) paramEncodedImage),
                                  paramEncodedImage.getSize(),
                                  paramQDPoint.getPoint(),
                                  arrayOfByte);
        }

        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public boolean hitTestData(EncodedImage paramEncodedImage, QDPoint paramQDPoint, int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = 0;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = HitTestDSequenceData(_ID(),
                                     ((ByteEncodedImage) paramEncodedImage).getBytes(),
                                     paramEncodedImage.getSize(),
                                     paramQDPoint.getPoint(),
                                     arrayOfInt,
                                     paramInt);

        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = HitTestDSequenceData(_ID(),
                                     ((IntEncodedImage) paramEncodedImage).getInts(),
                                     paramEncodedImage.getSize(),
                                     paramQDPoint.getPoint(),
                                     arrayOfInt,
                                     paramInt);

        } else if ((paramEncodedImage instanceof RawEncodedImage)) {
            i = HitTestDSequenceData(_ID(),
                                     QTObject.ID((RawEncodedImage) paramEncodedImage),
                                     paramEncodedImage.getSize(),
                                     paramQDPoint.getPoint(),
                                     arrayOfInt,
                                     paramInt);
        }

        StdQTException.checkError(i);
        return arrayOfInt[0] != 0;
    }

    public void flush() throws StdQTException {
        StdQTException.checkError(CDSequenceFlush(_ID()));
    }

    public void invalidate(Region paramRegion) throws StdQTException {
        StdQTException.checkError(CDSequenceInvalidate(_ID(), QTObject.ID(paramRegion)));
    }

    public void shieldCursor() throws StdQTException {
        StdQTException.checkError(ICMShieldSequenceCursor(_ID()));
    }

    public void setTimeCode(TimeCodeDef paramTimeCodeDef, TimeCodeTime paramTimeCodeTime) throws StdQTException {
        StdQTException.checkError(SetDSequenceTimeCode(_ID(), paramTimeCodeDef.getBytes(), paramTimeCodeTime.getBytes()));
    }

    private void setDataProc(Object paramObject, int paramInt) {
    }

    private static native short DecompressSequenceBegin(int[] paramArrayOfInt,
                                                        int paramInt1,
                                                        int paramInt2,
                                                        int paramInt3,
                                                        byte[] paramArrayOfByte1,
                                                        byte[] paramArrayOfByte2,
                                                        short paramShort1,
                                                        int paramInt4,
                                                        short paramShort2,
                                                        int paramInt5,
                                                        int paramInt6);

    private static native short DecompressSequenceBeginS(int[] paramArrayOfInt,
                                                         int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         int paramInt5,
                                                         byte[] paramArrayOfByte1,
                                                         byte[] paramArrayOfByte2,
                                                         short paramShort1,
                                                         int paramInt6,
                                                         short paramShort2,
                                                         int paramInt7,
                                                         int paramInt8);

    private static native short DecompressSequenceBeginS(int[] paramArrayOfInt,
                                                         int paramInt1,
                                                         byte[] paramArrayOfByte1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         byte[] paramArrayOfByte2,
                                                         byte[] paramArrayOfByte3,
                                                         short paramShort1,
                                                         int paramInt5,
                                                         short paramShort2,
                                                         int paramInt6,
                                                         int paramInt7);

    private static native short DecompressSequenceBeginS(int[] paramArrayOfInt1,
                                                         int paramInt1,
                                                         int[] paramArrayOfInt2,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         int paramInt4,
                                                         byte[] paramArrayOfByte1,
                                                         byte[] paramArrayOfByte2,
                                                         short paramShort1,
                                                         int paramInt5,
                                                         short paramShort2,
                                                         int paramInt6,
                                                         int paramInt7);

    private static native short DecompressSequenceFrameS(int paramInt1,
                                                         int paramInt2,
                                                         int paramInt3,
                                                         short paramShort,
                                                         short[] paramArrayOfShort,
                                                         int paramInt4);

    private static native short DecompressSequenceFrameS(int paramInt1,
                                                         byte[] paramArrayOfByte,
                                                         int paramInt2,
                                                         short paramShort,
                                                         short[] paramArrayOfShort,
                                                         int paramInt3);

    private static native short DecompressSequenceFrameS(int paramInt1,
                                                         int[] paramArrayOfInt,
                                                         int paramInt2,
                                                         short paramShort,
                                                         short[] paramArrayOfShort,
                                                         int paramInt3);

    private static native short DecompressSequenceFrame(int paramInt1,
                                                        int paramInt2,
                                                        short paramShort,
                                                        short[] paramArrayOfShort,
                                                        int paramInt3);

    private static native short DecompressSequenceFrame(int paramInt1,
                                                        byte[] paramArrayOfByte,
                                                        short paramShort,
                                                        short[] paramArrayOfShort,
                                                        int paramInt2);

    private static native short DecompressSequenceFrame(int paramInt1,
                                                        int[] paramArrayOfInt,
                                                        short paramShort,
                                                        short[] paramArrayOfShort,
                                                        int paramInt2);

    private static native short DecompressSequenceFrameWhen(int paramInt1,
                                                            int paramInt2,
                                                            int paramInt3,
                                                            short paramShort,
                                                            short[] paramArrayOfShort,
                                                            int paramInt4,
                                                            byte[] paramArrayOfByte);

    private static native short DecompressSequenceFrameWhen(int paramInt1,
                                                            byte[] paramArrayOfByte1,
                                                            int paramInt2,
                                                            short paramShort,
                                                            short[] paramArrayOfShort,
                                                            int paramInt3,
                                                            byte[] paramArrayOfByte2);

    private static native short DecompressSequenceFrameWhen(int paramInt1,
                                                            int[] paramArrayOfInt,
                                                            int paramInt2,
                                                            short paramShort,
                                                            short[] paramArrayOfShort,
                                                            int paramInt3,
                                                            byte[] paramArrayOfByte);

    private static native short SetDSequenceTransferMode(int paramInt, short paramShort, byte[] paramArrayOfByte);

    private static native short SetDSequenceSrcRect(int paramInt, byte[] paramArrayOfByte);

    private static native short SetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native short SetDSequenceMask(int paramInt1, int paramInt2);

    private static native short SetDSequenceMatte(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SetDSequenceAccuracy(int paramInt1, int paramInt2);

    private static native short GetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native short SetDSequenceFlags(int paramInt1, int paramInt2, int paramInt3);

    private static native short PtInDSequenceData(int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4,
                                                  byte[] paramArrayOfByte);

    private static native short PtInDSequenceData(int paramInt1,
                                                  byte[] paramArrayOfByte1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  byte[] paramArrayOfByte2);

    private static native short PtInDSequenceData(int paramInt1,
                                                  int[] paramArrayOfInt,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  byte[] paramArrayOfByte);

    private static native short CDSequenceFlush(int paramInt);

    private static native short CDSequenceInvalidate(int paramInt1, int paramInt2);

    private static native short ICMShieldSequenceCursor(int paramInt);

    private static native short HitTestDSequenceData(int paramInt1,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     int paramInt4,
                                                     int[] paramArrayOfInt,
                                                     int paramInt5);

    private static native short HitTestDSequenceData(int paramInt1,
                                                     int[] paramArrayOfInt1,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     int[] paramArrayOfInt2,
                                                     int paramInt4);

    private static native short HitTestDSequenceData(int paramInt1,
                                                     byte[] paramArrayOfByte,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     int[] paramArrayOfInt,
                                                     int paramInt4);

    private static native short SetDSequenceTimeCode(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /DSequence.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
