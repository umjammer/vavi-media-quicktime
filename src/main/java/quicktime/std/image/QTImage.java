
package quicktime.std.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.PixMap;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.QTUtils;
import quicktime.util.RawEncodedImage;


public class QTImage implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTImage.class);
                    }
                });
            }
        }.establish();
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    private ImageDescription desc;

    private EncodedImage data;

    public QTImage(ImageDescription paramImageDescription, EncodedImage paramEncodedImage) {
        this.data = paramEncodedImage;
        this.desc = paramImageDescription;
    }

    public EncodedImage getImage() {
        return this.data;
    }

    public ImageDescription getDescription() {
        return this.desc;
    }

    public static final ImageDescription compress(QDGraphics paramQDGraphics,
                                                  QDRect paramQDRect,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  EncodedImage paramEncodedImage) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = CompressImage(QTObject.ID(paramQDGraphics.getPixMap()),
                                  paramQDRect.getRect(),
                                  paramInt1,
                                  paramInt2,
                                  QTObject.ID(localImageDescription),
                                  ((ByteEncodedImage) paramEncodedImage).getBytes());

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = CompressImage(QTObject.ID(paramQDGraphics.getPixMap()),
                                  paramQDRect.getRect(),
                                  paramInt1,
                                  paramInt2,
                                  QTObject.ID(localImageDescription),
                                  ((IntEncodedImage) paramEncodedImage).getInts());

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = CompressImage(QTObject.ID(paramQDGraphics.getPixMap()),
                                  paramQDRect.getRect(),
                                  paramInt1,
                                  paramInt2,
                                  QTObject.ID(localImageDescription),
                                  QTObject.ID((RawEncodedImage) paramEncodedImage));
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    public static final ImageDescription fCompress(QDGraphics paramQDGraphics,
                                                   QDRect paramQDRect,
                                                   int paramInt1,
                                                   int paramInt2,
                                                   int paramInt3,
                                                   CodecComponent paramCodecComponent,
                                                   ColorTable paramColorTable,
                                                   int paramInt4,
                                                   EncodedImage paramEncodedImage) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);

        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = FCompressImage(QTObject.ID(paramQDGraphics.getPixMap()),
                                   paramQDRect.getRect(),
                                   (short) paramInt1,
                                   paramInt2,
                                   paramInt3,
                                   QTObject.ID(paramCodecComponent),
                                   QTObject.ID(paramColorTable),
                                   (short) paramInt4,
                                   0,
                                   0,
                                   0,
                                   QTObject.ID(localImageDescription),
                                   ((ByteEncodedImage) paramEncodedImage).getBytes());

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = FCompressImage(QTObject.ID(paramQDGraphics.getPixMap()),
                                   paramQDRect.getRect(),
                                   (short) paramInt1,
                                   paramInt2,
                                   paramInt3,
                                   QTObject.ID(paramCodecComponent),
                                   QTObject.ID(paramColorTable),
                                   (short) paramInt4,
                                   0,
                                   0,
                                   0,
                                   QTObject.ID(localImageDescription),
                                   ((IntEncodedImage) paramEncodedImage).getInts());

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = FCompressImage(QTObject.ID(paramQDGraphics.getPixMap()),
                                   paramQDRect.getRect(),
                                   (short) paramInt1,
                                   paramInt2,
                                   paramInt3,
                                   QTObject.ID(paramCodecComponent),
                                   QTObject.ID(paramColorTable),
                                   (short) paramInt4,
                                   0,
                                   0,
                                   0,
                                   QTObject.ID(localImageDescription),
                                   QTObject.ID((RawEncodedImage) paramEncodedImage));
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return localImageDescription;
    }

    public static final ImageDescription convert(ImageDescription paramImageDescription,
                                                 EncodedImage paramEncodedImage1,
                                                 int paramInt1,
                                                 ColorTable paramColorTable,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 int paramInt4,
                                                 CodecComponent paramCodecComponent,
                                                 EncodedImage paramEncodedImage2) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        int i = -50;
        if ((paramEncodedImage1 instanceof ByteEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 ((ByteEncodedImage) paramEncodedImage1).getBytes(),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 ((ByteEncodedImage) paramEncodedImage2).getBytes());

            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 ((ByteEncodedImage) paramEncodedImage1).getBytes(),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 ((IntEncodedImage) paramEncodedImage2).getInts());

            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 ((ByteEncodedImage) paramEncodedImage1).getBytes(),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 QTObject.ID((RawEncodedImage) paramEncodedImage2));

            }

        } else if ((paramEncodedImage1 instanceof IntEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 ((IntEncodedImage) paramEncodedImage1).getInts(),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 ((ByteEncodedImage) paramEncodedImage2).getBytes());

            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 ((IntEncodedImage) paramEncodedImage1).getInts(),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 ((IntEncodedImage) paramEncodedImage2).getInts());

            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 ((IntEncodedImage) paramEncodedImage1).getInts(),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 QTObject.ID((RawEncodedImage) paramEncodedImage2));

            }

        } else if ((paramEncodedImage1 instanceof RawEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 QTObject.ID((RawEncodedImage) paramEncodedImage1),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 ((ByteEncodedImage) paramEncodedImage2).getBytes());

            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 QTObject.ID((RawEncodedImage) paramEncodedImage1),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 ((IntEncodedImage) paramEncodedImage2).getInts());

            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = ConvertImage(QTObject.ID(paramImageDescription),
                                 QTObject.ID((RawEncodedImage) paramEncodedImage1),
                                 (short) paramInt1,
                                 QTObject.ID(paramColorTable),
                                 paramInt2,
                                 paramInt3,
                                 paramInt4,
                                 QTObject.ID(paramCodecComponent),
                                 QTObject.ID(localImageDescription),
                                 QTObject.ID((RawEncodedImage) paramEncodedImage2));
            }
        }

        StdQTException.checkError(i);
        return localImageDescription;
    }

    public static final void trim(ImageDescription paramImageDescription,
                                  EncodedImage paramEncodedImage1,
                                  EncodedImage paramEncodedImage2,
                                  QDRect paramQDRect) throws StdQTException {
        int i = -50;
        if ((paramEncodedImage1 instanceof ByteEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              ((ByteEncodedImage) paramEncodedImage1).getBytes(),
                              0,
                              0,
                              ((ByteEncodedImage) paramEncodedImage2).getBytes(),
                              0,
                              0,
                              paramQDRect.getRect(),
                              0);

            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              ((ByteEncodedImage) paramEncodedImage1).getBytes(),
                              0,
                              0,
                              ((IntEncodedImage) paramEncodedImage2).getInts(),
                              0,
                              0,
                              paramQDRect.getRect(),
                              0);

            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              ((ByteEncodedImage) paramEncodedImage1).getBytes(),
                              0,
                              0,
                              QTObject.ID((RawEncodedImage) paramEncodedImage2),
                              0,
                              0,
                              paramQDRect.getRect(),
                              0);

            }

        } else if ((paramEncodedImage1 instanceof IntEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              ((IntEncodedImage) paramEncodedImage1).getInts(),
                              0,
                              0,
                              ((ByteEncodedImage) paramEncodedImage2).getBytes(),
                              0,
                              0,
                              paramQDRect.getBytes(),
                              0);

            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              ((IntEncodedImage) paramEncodedImage1).getInts(),
                              0,
                              0,
                              ((IntEncodedImage) paramEncodedImage2).getInts(),
                              0,
                              0,
                              paramQDRect.getBytes(),
                              0);

            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              ((IntEncodedImage) paramEncodedImage1).getInts(),
                              0,
                              0,
                              QTObject.ID((RawEncodedImage) paramEncodedImage2),
                              0,
                              0,
                              paramQDRect.getBytes(),
                              0);

            }

        } else if ((paramEncodedImage1 instanceof RawEncodedImage)) {
            if ((paramEncodedImage2 instanceof ByteEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              QTObject.ID((RawEncodedImage) paramEncodedImage1),
                              0,
                              0,
                              ((ByteEncodedImage) paramEncodedImage2).getBytes(),
                              0,
                              0,
                              paramQDRect.getBytes(),
                              0);

            } else if ((paramEncodedImage2 instanceof IntEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              QTObject.ID((RawEncodedImage) paramEncodedImage1),
                              0,
                              0,
                              ((IntEncodedImage) paramEncodedImage2).getInts(),
                              0,
                              0,
                              paramQDRect.getBytes(),
                              0);

            } else if ((paramEncodedImage2 instanceof RawEncodedImage)) {
                i = TrimImage(QTObject.ID(paramImageDescription),
                              QTObject.ID((RawEncodedImage) paramEncodedImage1),
                              0,
                              0,
                              QTObject.ID((RawEncodedImage) paramEncodedImage2),
                              0,
                              0,
                              paramQDRect.getBytes(),
                              0);
            }
        }

        StdQTException.checkError(i);
    }

    public static final void decompress(EncodedImage paramEncodedImage,
                                        ImageDescription paramImageDescription,
                                        QDGraphics paramQDGraphics,
                                        QDRect paramQDRect,
                                        int paramInt) throws StdQTException, QDException {
        decompress(paramEncodedImage, paramImageDescription, paramQDGraphics, null, paramQDRect, paramInt, null);
    }

    public static final void decompress(EncodedImage paramEncodedImage,
                                        ImageDescription paramImageDescription,
                                        QDGraphics paramQDGraphics,
                                        QDRect paramQDRect1,
                                        QDRect paramQDRect2,
                                        int paramInt,
                                        Region paramRegion) throws StdQTException, QDException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = DecompressImage(((ByteEncodedImage) paramEncodedImage).getBytes(),
                                    QTObject.ID(paramImageDescription),
                                    QTObject.ID(paramQDGraphics.getPixMap()),
                                    paramQDRect1 == null ? null : paramQDRect1.getRect(),
                                    paramQDRect2.getRect(),
                                    (short) paramInt,
                                    QTObject.ID(paramRegion));

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = DecompressImage(((IntEncodedImage) paramEncodedImage).getInts(),
                                    QTObject.ID(paramImageDescription),
                                    QTObject.ID(paramQDGraphics.getPixMap()),
                                    paramQDRect1 == null ? null : paramQDRect1.getRect(),
                                    paramQDRect2.getRect(),
                                    (short) paramInt,
                                    QTObject.ID(paramRegion));

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = DecompressImage(QTObject.ID((RawEncodedImage) paramEncodedImage),
                                    QTObject.ID(paramImageDescription),
                                    QTObject.ID(paramQDGraphics.getPixMap()),
                                    paramQDRect1 == null ? null : paramQDRect1.getRect(),
                                    paramQDRect2.getRect(),
                                    (short) paramInt,
                                    QTObject.ID(paramRegion));
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    public static final void fDecompress(EncodedImage paramEncodedImage,
                                         ImageDescription paramImageDescription,
                                         QDGraphics paramQDGraphics,
                                         QDRect paramQDRect1,
                                         Matrix paramMatrix,
                                         int paramInt1,
                                         Region paramRegion,
                                         PixMap paramPixMap,
                                         QDRect paramQDRect2,
                                         int paramInt2,
                                         CodecComponent paramCodecComponent) throws StdQTException, QDException {
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = FDecompressImage(QTObject.ID((RawEncodedImage) paramEncodedImage),
                                 QTObject.ID(paramImageDescription),
                                 QTObject.ID(paramQDGraphics.getPixMap()),
                                 paramQDRect1 == null ? null : paramQDRect1.getRect(),
                                 paramMatrix == null ? null : paramMatrix.getBytes(),
                                 (short) paramInt1,
                                 QTObject.ID(paramRegion),
                                 QTObject.ID(paramPixMap),
                                 paramQDRect2 == null ? null : paramQDRect2.getRect(),
                                 paramInt2,
                                 QTObject.ID(paramCodecComponent),
                                 0,
                                 0,
                                 0);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    public static final int getMaxCompressionSize(QDGraphics paramQDGraphics,
                                                  QDRect paramQDRect,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  CodecComponent paramCodecComponent) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = GetMaxCompressionSize(QTObject.ID(paramQDGraphics.getPixMap()),
                                      paramQDRect == null ? null : paramQDRect.getRect(),
                                      (short) paramInt1,
                                      paramInt2,
                                      paramInt3,
                                      QTObject.ID(paramCodecComponent),
                                      arrayOfInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public static final int getCompressedSize(ImageDescription paramImageDescription,
                                              EncodedImage paramEncodedImage) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = -50;
        if ((paramEncodedImage instanceof ByteEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(paramImageDescription),
                                       ((ByteEncodedImage) paramEncodedImage).getBytes(),
                                       0,
                                       0,
                                       arrayOfInt);

        } else if ((paramEncodedImage instanceof IntEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(paramImageDescription),
                                       ((IntEncodedImage) paramEncodedImage).getInts(),
                                       0,
                                       0,
                                       arrayOfInt);

        } else if ((paramEncodedImage instanceof RawEncodedImage)) {
            i = GetCompressedImageSize(QTObject.ID(paramImageDescription),
                                       QTObject.ID((RawEncodedImage) paramEncodedImage),
                                       0,
                                       0,
                                       arrayOfInt);
        }

        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public static final int getCompressionTime(QDGraphics paramQDGraphics,
                                               QDRect paramQDRect,
                                               int paramInt1,
                                               int paramInt2,
                                               CodecComponent paramCodecComponent,
                                               int paramInt3,
                                               int paramInt4) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];

        int i = 0;
        int j = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            if (paramQDGraphics != null) {
                SetGWorld(QTObject.ID(paramQDGraphics), 0);
                i = QTObject.ID(paramQDGraphics.getPixMap());
            }
            j = GetCompressionTime(i,
                                   paramQDRect == null ? null : paramQDRect.getRect(),
                                   (short) paramInt1,
                                   paramInt2,
                                   QTObject.ID(paramCodecComponent),
                                   paramInt3,
                                   paramInt4,
                                   arrayOfInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(j);
        return arrayOfInt[0];
    }

    public static final float getSimilarity(QDGraphics paramQDGraphics,
                                            QDRect paramQDRect,
                                            ImageDescription paramImageDescription,
                                            EncodedImage paramEncodedImage) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = GetSimilarity(QTObject.ID(paramQDGraphics.getPixMap()),
                                  paramQDRect.getRect(),
                                  QTObject.ID(paramImageDescription),
                                  ((ByteEncodedImage) paramEncodedImage).getBytes(),
                                  arrayOfInt);

            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = GetSimilarity(QTObject.ID(paramQDGraphics.getPixMap()),
                                  paramQDRect.getBytes(),
                                  QTObject.ID(paramImageDescription),
                                  ((IntEncodedImage) paramEncodedImage).getInts(),
                                  arrayOfInt);

            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = GetSimilarity(QTObject.ID(paramQDGraphics.getPixMap()),
                                  paramQDRect.getBytes(),
                                  QTObject.ID(paramImageDescription),
                                  QTObject.ID((RawEncodedImage) paramEncodedImage),
                                  arrayOfInt);
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native short CompressImage(int paramInt1,
                                              byte[] paramArrayOfByte,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              int paramInt5);

    private static native short CompressImage(int paramInt1,
                                              byte[] paramArrayOfByte1,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              byte[] paramArrayOfByte2);

    private static native short CompressImage(int paramInt1,
                                              byte[] paramArrayOfByte,
                                              int paramInt2,
                                              int paramInt3,
                                              int paramInt4,
                                              int[] paramArrayOfInt);

    private static native short FCompressImage(int paramInt1,
                                               byte[] paramArrayOfByte,
                                               short paramShort1,
                                               int paramInt2,
                                               int paramInt3,
                                               int paramInt4,
                                               int paramInt5,
                                               short paramShort2,
                                               int paramInt6,
                                               int paramInt7,
                                               int paramInt8,
                                               int paramInt9,
                                               int paramInt10);

    private static native short FCompressImage(int paramInt1,
                                               byte[] paramArrayOfByte1,
                                               short paramShort1,
                                               int paramInt2,
                                               int paramInt3,
                                               int paramInt4,
                                               int paramInt5,
                                               short paramShort2,
                                               int paramInt6,
                                               int paramInt7,
                                               int paramInt8,
                                               int paramInt9,
                                               byte[] paramArrayOfByte2);

    private static native short FCompressImage(int paramInt1,
                                               byte[] paramArrayOfByte,
                                               short paramShort1,
                                               int paramInt2,
                                               int paramInt3,
                                               int paramInt4,
                                               int paramInt5,
                                               short paramShort2,
                                               int paramInt6,
                                               int paramInt7,
                                               int paramInt8,
                                               int paramInt9,
                                               int[] paramArrayOfInt);

    private static native short ConvertImage(int paramInt1,
                                             byte[] paramArrayOfByte1,
                                             short paramShort,
                                             int paramInt2,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             byte[] paramArrayOfByte2);

    private static native short ConvertImage(int paramInt1,
                                             byte[] paramArrayOfByte,
                                             short paramShort,
                                             int paramInt2,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int[] paramArrayOfInt);

    private static native short ConvertImage(int paramInt1,
                                             byte[] paramArrayOfByte,
                                             short paramShort,
                                             int paramInt2,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int paramInt8);

    private static native short ConvertImage(int paramInt1,
                                             int[] paramArrayOfInt,
                                             short paramShort,
                                             int paramInt2,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             byte[] paramArrayOfByte);

    private static native short ConvertImage(int paramInt1,
                                             int[] paramArrayOfInt1,
                                             short paramShort,
                                             int paramInt2,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int[] paramArrayOfInt2);

    private static native short ConvertImage(int paramInt1,
                                             int[] paramArrayOfInt,
                                             short paramShort,
                                             int paramInt2,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int paramInt8);

    private static native short ConvertImage(int paramInt1,
                                             int paramInt2,
                                             short paramShort,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int paramInt8,
                                             byte[] paramArrayOfByte);

    private static native short ConvertImage(int paramInt1,
                                             int paramInt2,
                                             short paramShort,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int paramInt8,
                                             int[] paramArrayOfInt);

    private static native short ConvertImage(int paramInt1,
                                             int paramInt2,
                                             short paramShort,
                                             int paramInt3,
                                             int paramInt4,
                                             int paramInt5,
                                             int paramInt6,
                                             int paramInt7,
                                             int paramInt8,
                                             int paramInt9);

    private static native short TrimImage(int paramInt1,
                                          int paramInt2,
                                          int paramInt3,
                                          int paramInt4,
                                          byte[] paramArrayOfByte1,
                                          int paramInt5,
                                          int paramInt6,
                                          byte[] paramArrayOfByte2,
                                          int paramInt7);

    private static native short TrimImage(int paramInt1,
                                          int paramInt2,
                                          int paramInt3,
                                          int paramInt4,
                                          int[] paramArrayOfInt,
                                          int paramInt5,
                                          int paramInt6,
                                          byte[] paramArrayOfByte,
                                          int paramInt7);

    private static native short TrimImage(int paramInt1,
                                          int paramInt2,
                                          int paramInt3,
                                          int paramInt4,
                                          int paramInt5,
                                          int paramInt6,
                                          int paramInt7,
                                          byte[] paramArrayOfByte,
                                          int paramInt8);

    private static native short TrimImage(int paramInt1,
                                          int[] paramArrayOfInt,
                                          int paramInt2,
                                          int paramInt3,
                                          byte[] paramArrayOfByte1,
                                          int paramInt4,
                                          int paramInt5,
                                          byte[] paramArrayOfByte2,
                                          int paramInt6);

    private static native short TrimImage(int paramInt1,
                                          int[] paramArrayOfInt1,
                                          int paramInt2,
                                          int paramInt3,
                                          int[] paramArrayOfInt2,
                                          int paramInt4,
                                          int paramInt5,
                                          byte[] paramArrayOfByte,
                                          int paramInt6);

    private static native short TrimImage(int paramInt1,
                                          int[] paramArrayOfInt,
                                          int paramInt2,
                                          int paramInt3,
                                          int paramInt4,
                                          int paramInt5,
                                          int paramInt6,
                                          byte[] paramArrayOfByte,
                                          int paramInt7);

    private static native short TrimImage(int paramInt1,
                                          byte[] paramArrayOfByte1,
                                          int paramInt2,
                                          int paramInt3,
                                          byte[] paramArrayOfByte2,
                                          int paramInt4,
                                          int paramInt5,
                                          byte[] paramArrayOfByte3,
                                          int paramInt6);

    private static native short TrimImage(int paramInt1,
                                          byte[] paramArrayOfByte1,
                                          int paramInt2,
                                          int paramInt3,
                                          int[] paramArrayOfInt,
                                          int paramInt4,
                                          int paramInt5,
                                          byte[] paramArrayOfByte2,
                                          int paramInt6);

    private static native short TrimImage(int paramInt1,
                                          byte[] paramArrayOfByte1,
                                          int paramInt2,
                                          int paramInt3,
                                          int paramInt4,
                                          int paramInt5,
                                          int paramInt6,
                                          byte[] paramArrayOfByte2,
                                          int paramInt7);

    private static native short DecompressImage(byte[] paramArrayOfByte1,
                                                int paramInt1,
                                                int paramInt2,
                                                byte[] paramArrayOfByte2,
                                                byte[] paramArrayOfByte3,
                                                short paramShort,
                                                int paramInt3);

    private static native short DecompressImage(int[] paramArrayOfInt,
                                                int paramInt1,
                                                int paramInt2,
                                                byte[] paramArrayOfByte1,
                                                byte[] paramArrayOfByte2,
                                                short paramShort,
                                                int paramInt3);

    private static native short DecompressImage(int paramInt1,
                                                int paramInt2,
                                                int paramInt3,
                                                byte[] paramArrayOfByte1,
                                                byte[] paramArrayOfByte2,
                                                short paramShort,
                                                int paramInt4);

    private static native short FDecompressImage(int paramInt1,
                                                 int paramInt2,
                                                 int paramInt3,
                                                 byte[] paramArrayOfByte1,
                                                 byte[] paramArrayOfByte2,
                                                 short paramShort,
                                                 int paramInt4,
                                                 int paramInt5,
                                                 byte[] paramArrayOfByte3,
                                                 int paramInt6,
                                                 int paramInt7,
                                                 int paramInt8,
                                                 int paramInt9,
                                                 int paramInt10);

    private static native short GetMaxCompressionSize(int paramInt1,
                                                      byte[] paramArrayOfByte,
                                                      short paramShort,
                                                      int paramInt2,
                                                      int paramInt3,
                                                      int paramInt4,
                                                      int[] paramArrayOfInt);

    private static native short GetCompressedImageSize(int paramInt1,
                                                       byte[] paramArrayOfByte,
                                                       int paramInt2,
                                                       int paramInt3,
                                                       int[] paramArrayOfInt);

    private static native short GetCompressedImageSize(int paramInt1,
                                                       int[] paramArrayOfInt1,
                                                       int paramInt2,
                                                       int paramInt3,
                                                       int[] paramArrayOfInt2);

    private static native short GetCompressedImageSize(int paramInt1,
                                                       int paramInt2,
                                                       int paramInt3,
                                                       int paramInt4,
                                                       int[] paramArrayOfInt);

    private static native short GetCompressionTime(int paramInt1,
                                                   byte[] paramArrayOfByte,
                                                   short paramShort,
                                                   int paramInt2,
                                                   int paramInt3,
                                                   int paramInt4,
                                                   int paramInt5,
                                                   int[] paramArrayOfInt);

    private static native short GetSimilarity(int paramInt1,
                                              byte[] paramArrayOfByte1,
                                              int paramInt2,
                                              byte[] paramArrayOfByte2,
                                              int[] paramArrayOfInt);

    private static native short GetSimilarity(int paramInt1,
                                              byte[] paramArrayOfByte,
                                              int paramInt2,
                                              int[] paramArrayOfInt1,
                                              int[] paramArrayOfInt2);

    private static native short GetSimilarity(int paramInt1,
                                              byte[] paramArrayOfByte,
                                              int paramInt2,
                                              int paramInt3,
                                              int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /QTImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
