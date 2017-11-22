
package quicktime.qd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.std.sg.SequenceGrabber;
import quicktime.streaming.Presentation;
import quicktime.streaming.Stream;
import quicktime.util.EndianOrder;
import quicktime.util.QTHandleRef;
import quicktime.util.RawEncodedImage;


public final class Pict extends QTHandleRef implements QuickTimeLib, Cloneable, QTStreamingLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Pict.class);
                    }
                });
            }
        }.establish();
    }

    private static int kNativeSize = 10;

    private static transient QDGraphics gw = null;

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    public static Pict thumbnailFromQDGraphics(QDGraphics paramQDGraphics,
                                               QDRect paramQDRect,
                                               int paramInt) throws QTException {
        Pict localPict = new Pict(4, false);
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            int j = GetGWorldPixMap(QTObject.ID(paramQDGraphics));
            LockPixels(j);
            i = MakeThumbnailFromPixMap(j, paramQDRect.getRect(), (short) paramInt, QTObject.ID(localPict), 0);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        QDException.checkError(i);
        return localPict;
    }

    public static Pict fromTrack(Track paramTrack, int paramInt) throws QTException {
        int i = GetTrackPict(QTObject.ID(paramTrack), paramInt);
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    public static Pict fromMovie(Movie paramMovie, int paramInt) throws QTException {
        int i = GetMoviePict(QTObject.ID(paramMovie), paramInt);
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    public static Pict fromMovie(Movie paramMovie) throws QTException {
        int i = GetMoviePosterPict(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    public static Pict fromPresentation(Presentation paramPresentation, Stream paramStream) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTSPresGetPicture(QTObject.ID(paramPresentation), QTObject.ID(paramStream), arrayOfInt));

        return arrayOfInt[0] == 0 ? null : new Pict(arrayOfInt[0]);
    }

    public static Pict fromSequenceGrabber(SequenceGrabber paramSequenceGrabber,
                                           QDRect paramQDRect,
                                           int paramInt1,
                                           int paramInt2) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = SGGrabPict(QTObject.ID(paramSequenceGrabber), arrayOfInt, paramQDRect.getRect(), (short) paramInt1, paramInt2);

        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Pict(arrayOfInt[0]);
    }

    public static Pict fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsImportGetAsPicture(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        StdQTException.checkError(i);
        return new Pict(arrayOfInt[0]);
    }

    public static Pict fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GraphicsExportGetInputPicture(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new Pict(arrayOfInt[0]);
    }

    public static Pict fromImageCompressionDialog(ImageCompressionDialog paramImageCompressionDialog,
                                                  Pict paramPict) throws QTException {
        Pict localPict = new Pict(4, true);
        int i = SCCompressPicture(QTObject.ID(paramImageCompressionDialog), QTObject.ID(paramPict), QTObject.ID(localPict));
        if (i == 1)
            throw new StdQTException(-128);
        StdQTException.checkError(i);
        return localPict;
    }

    public static Pict fromFile(File paramFile) throws QTException, IOException {
        FileInputStream localFileInputStream = new FileInputStream(paramFile);
        byte[] arrayOfByte = new byte[localFileInputStream.available() - 512];
        localFileInputStream.skip(512L);
        localFileInputStream.read(arrayOfByte, 0, arrayOfByte.length);
        localFileInputStream.close();
        Pict localPict = new Pict(arrayOfByte.length, false);
        localPict.copyFromArray(0, arrayOfByte, 0, arrayOfByte.length);
        return localPict;
    }

    public static Pict open(QDGraphics paramQDGraphics, OpenCPicParams paramOpenCPicParams) throws QTException {
        if (gw != null)
            throw new QDException(-9);
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            gw = paramQDGraphics;
            i = OpenCPicture(paramOpenCPicParams.getBytes());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
            try {
                QDException.checkError(QDError());
            } catch (QTException localQTException) {
                gw = null;
                throw localQTException;
            }
        }
        if (i == 0)
            throw new QDException(-108);
        return new Pict(i);
    }

    public Pict(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte.length, false);
        copyFromArray(0, paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    private Pict(int paramInt) throws QTException {
        super(paramInt, null, false);
    }

    private Pict(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    public QDRect getPictFrame() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(2, 8, arrayOfByte, 0);
        if (QTSession.isCurrentOS(2)) {
            for (int i = 0; i < 8; i += 2)
                setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(arrayOfByte, i)));
        }
        return QDRect.fromArray(arrayOfByte, 8);
    }

    public boolean isOpen() {
        return gw != null;
    }

    public void close() throws QDException {
        if (gw != null) {
            synchronized (QTNative.globalsLock) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                SetGWorld(QTObject.ID(gw), 0);
                ClosePicture();
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        gw = null;
    }

    public Pict compress(int paramInt1, int paramInt2) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict localPict = new Pict(4, true);
        QDException.checkError(CompressPicture(_ID(), QTObject.ID(localPict), paramInt1, paramInt2));
        return localPict;
    }

    public Pict fCompress(int paramInt1,
                          int paramInt2,
                          int paramInt3,
                          int paramInt4,
                          int paramInt5,
                          int paramInt6) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict localPict = new Pict(4, true);
        QDException.checkError(FCompressPicture(_ID(),
                                                QTObject.ID(localPict),
                                                (short) paramInt1,
                                                0,
                                                paramInt2,
                                                (short) paramInt3,
                                                (short) paramInt4,
                                                0,
                                                paramInt5,
                                                paramInt6));

        return localPict;
    }

    public void draw(QDGraphics paramQDGraphics, QDRect paramQDRect) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            DrawPicture(_ID(), paramQDRect.getBytes());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        QDException.checkError(QDError());
    }

    public void drawTrimmed(QDGraphics paramQDGraphics,
                            QDRect paramQDRect,
                            Region paramRegion,
                            int paramInt) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        if (paramQDGraphics == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            i = DrawTrimmedPicture(_ID(), paramQDRect.getBytes(), QTObject.ID(paramRegion), (short) paramInt, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        QDException.checkError(i);
    }

    public Pict makeThumbnail(int paramInt) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict localPict = new Pict(4, false);
        QDException.checkError(MakeThumbnailFromPicture(_ID(), (short) paramInt, QTObject.ID(localPict), 0));

        return localPict;
    }

    public RawEncodedImage toEncodedImage() throws QDException {
        if (isOpen())
            throw new QDException(-9);
        return RawEncodedImage.fromQTHandle(this);
    }

    public void writeToFile(File paramFile) throws QDException, IOException {
        if (isOpen())
            throw new QDException(-9);
        FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
        localFileOutputStream.write(new byte[512]);
        byte[] arrayOfByte = getBytes();

        localFileOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
        localFileOutputStream.close();
    }

    public String toString() {
        return super.toString() + "[pictFrame=" + getPictFrame() + ",isOpen=" + isOpen() + "]";
    }

    protected void _dispose() {
        try {
            close();
        } catch (QDException localQDException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQDException), this, "close");
        }
    }

    private static native int GetGWorldPixMap(int paramInt);

    private static native byte LockPixels(int paramInt);

    private static native short MakeThumbnailFromPixMap(int paramInt1,
                                                        byte[] paramArrayOfByte,
                                                        short paramShort,
                                                        int paramInt2,
                                                        int paramInt3);

    private static native void UnlockPixels(int paramInt);

    private static native int GetTrackPict(int paramInt1, int paramInt2);

    private static native short GetMoviesError();

    private static native int GetMoviePict(int paramInt1, int paramInt2);

    private static native int GetMoviePosterPict(int paramInt);

    private static native int SGGrabPict(int paramInt1,
                                         int[] paramArrayOfInt,
                                         byte[] paramArrayOfByte,
                                         short paramShort,
                                         int paramInt2);

    private static native int GraphicsImportGetAsPicture(int paramInt, int[] paramArrayOfInt);

    private static native int SCCompressPicture(int paramInt1, int paramInt2, int paramInt3);

    private static native int OpenCPicture(byte[] paramArrayOfByte);

    private static native short QDError();

    private static native void ClosePicture();

    private static native short CompressPicture(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short FCompressPicture(int paramInt1,
                                                 int paramInt2,
                                                 short paramShort1,
                                                 int paramInt3,
                                                 int paramInt4,
                                                 short paramShort2,
                                                 short paramShort3,
                                                 int paramInt5,
                                                 int paramInt6,
                                                 int paramInt7);

    private static native void DrawPicture(int paramInt, byte[] paramArrayOfByte);

    private static native short DrawTrimmedPicture(int paramInt1,
                                                   byte[] paramArrayOfByte,
                                                   int paramInt2,
                                                   short paramShort,
                                                   int paramInt3);

    private static native short MakeThumbnailFromPicture(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GraphicsExportGetInputPicture(int paramInt, int[] paramArrayOfInt);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native short QTSPresGetPicture(int paramInt1, int paramInt2, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/Pict.
 * class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
