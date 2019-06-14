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

/**
 * The Pict class represents sequences of drawing commands, while providing a common medium for sharing image data.
 * Methods are implemented for a corresponding QuickDraw structure used by QuickTime.
 * refer to Pictures in QuickDraw
 */
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

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * Creates an 80-by-80 pixel thumbnail picture from the pixmap associated with this QDGraphics.
     * QuickTime::MakeThumbnailFromPixMap()
     * @param qd the QDGraphics from which to make the thumbnail
     * @param src a QDRect object defining the portion of the image to use for the thumbnail
     * @param colorDepth specifies the depth at which the image is likely to be viewed
     * @return a Pict object representing the thumbnail
     * @return {@link quicktime.QTException}
     */
    public static Pict thumbnailFromQDGraphics(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt) throws QTException {
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

    /**
     * This method creates a QuickDraw picture from the specified track at the specified time.
     * QuickTime::GetTrackPict()
     * @param t the track object
     * @param time Specifies the track image for the picture. The time parameter contains the time from which the image is taken.
     * @return a Pict object for the specified time.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Track#getPict(int)}
     */
    public static Pict fromTrack(Track paramTrack, int paramInt) throws QTException {
        int i = GetTrackPict(QTObject.ID(paramTrack), paramInt);
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    /**
     * This method creates a picture from the specified movie at the specified time.
     * This function uses only those movie tracks that are currently enabled and would therefore
     * be used in playback. Your application may call this function even if the movie is inactive.
     * QuickTime::GetMoviePict()
     * @param time contains the time from which the image is taken.
     * @param m the Movie object
     * @return a Pict object
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getPict(int)}
     */
    public static Pict fromMovie(Movie paramMovie, int paramInt) throws QTException {
        int i = GetMoviePict(QTObject.ID(paramMovie), paramInt);
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    /**
     * This method creates a picture that contains a movie's poster.
     * returns a PicHandle object for the image.
     * QuickTime::GetMoviePosterPict()
     * @param m the Movie object
     * @return a Pict object
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.movies.Movie#getPosterPict()}
     */
    public static Pict fromMovie(Movie paramMovie) throws QTException {
        int i = GetMoviePosterPict(QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    /**
     * This method creates a picture that contains a representation of the presentation
     * returns a PicHandle object for the image.
     * QuickTime::QTSPresGetPicture()
     * @param p the Presentation object
     * @param s the stream object
     * @return a Pict object
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.streaming.Presentation#getPicture(quicktime.streaming.Stream)}
     */
    public static Pict fromPresentation(Presentation paramPresentation, Stream paramStream) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(QTSPresGetPicture(QTObject.ID(paramPresentation), QTObject.ID(paramStream), arrayOfInt));
        return arrayOfInt[0] == 0 ? null : new Pict(arrayOfInt[0]);
    }

    /**
     * Provides a simple interface to obtain a QuickDraw picture from a sequence grabber component.
     * The sequence grabber can display the picture directly, or it can write the picture to an
     * offscreen buffer. This function is limited in scope, however, and does not allow you to
     * control all of the parameters that govern the operation. When you call this function,
     * the sequence grabber component obtains and configures appropriate sequence grabber
     * channel components (if necessary), grabs the data, and then releases any components
     * it obtained.
     * QuickTime::SGGrabPict
     * @param bounds The bounding rectangle.
     * @param offscreenDepth The bit depth of the Pict.
     * @param grabPictFlags The flags for the operation.
     * @return The Pict.
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.sg.SequenceGrabber#grabPict(quicktime.qd.QDRect, int, int)}
     */
    public static Pict fromSequenceGrabber(SequenceGrabber paramSequenceGrabber, QDRect paramQDRect, int paramInt1, int paramInt2) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = SGGrabPict(QTObject.ID(paramSequenceGrabber), arrayOfInt, paramQDRect.getRect(), (short) paramInt1, paramInt2);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? null : new Pict(arrayOfInt[0]);
    }

    /**
     * Creates a new Pict containing the image currently in use by this import component.
     * QuickTime::GraphicsImportGetAsPicture
     * @return a pict
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getAsPicture()}
     */
    public static Pict fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsImportGetAsPicture(QTObject.ID(paramGraphicsImporter), arrayOfInt);
        StdQTException.checkError(i);
        return new Pict(arrayOfInt[0]);
    }

    /**
     * Creates a new Pict containing the image currently in use by this import component.
     * QuickTime::GraphicsExportGetInputPicture
     * @return a pict
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getInputPicture()}
     */
    public static Pict fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GraphicsExportGetInputPicture(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return new Pict(arrayOfInt[0]);
    }

    /**
     * Compresses a Pict returning a new Pict.
     * QuickTime::SCCompressPicture()
     * @param src the source Pict.
     * @return a Pict
     * @return {@link quicktime.QTException}
     * @see {@link quicktime.std.qtcomponents.ImageCompressionDialog#compressPicture(quicktime.qd.Pict)}
     */
    public static Pict fromImageCompressionDialog(ImageCompressionDialog paramImageCompressionDialog, Pict paramPict) throws QTException {
        Pict localPict = new Pict(4, true);
        int i = SCCompressPicture(QTObject.ID(paramImageCompressionDialog), QTObject.ID(paramPict), QTObject.ID(localPict));
        if (i == 1)
            throw new StdQTException(-128);
        StdQTException.checkError(i);
        return localPict;
    }

    /**
     * Opens a PICT that is stored in the file. This method does no checking
     * to ensure that the file is valid PICT file.
     * @param file the PICT file
     * @return a Pict
     * @return {@link quicktime.QTException}
     * @return java.io.IOException
     */
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

    /**
     * Use OpenCPicture to begin defining a picture, collecting all of your subsequent
     * drawing commands in this record. The returned Pict must be closed before any
     * other methods can be applied to it.
     * QuickDraw::OpenCPicture
     * @param port the port which the open picture will record operations from
     * @param params the parameters for the new Pict
     * @return a Pict
     * @return {@link quicktime.QTException}
     */
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

    /**
     * Create a Pict object from an array of bytes. This is expected to be in
     * the format of a Pict Handle - ie. if you are reading this in from a Pict file
     * you must drop the first 512 bytes from the file.
     * @param pictBytes the bytes of the pict handle
     */
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

    /**
     * Returns the picture frame rectangle referenced of the Pict.
     */
    public QDRect getPictFrame() {
        byte[] arrayOfByte = new byte[8];
        getBytesAt(2, 8, arrayOfByte, 0);
        if (QTSession.isCurrentOS(2)) {
            for (int i = 0; i < 8; i += 2) setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(arrayOfByte, i)));
        }
        return QDRect.fromArray(arrayOfByte, 8);
    }

    /**
     * Returns true if the pict is open for recording drawing operations.
     * @return a boolean indicating open status
     */
    public boolean isOpen() {
        return gw != null;
    }

    /**
     * Completes the collection of drawing commands and picture comments that define your picture.
     * QuickDraw::ClosePicture()
     */
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

    /**
     * This allows your application to compress a single-frame image stored as a picture.
     * Returns the result in a new picture.
     * QuickTime::CompressPicture()
     * @param quality Specifies the desired compressed image quality
     * @param cType Specifies the compressor type
     * @return dstPicture The destination compressed image
     * @return {@link quicktime.QTException}
     */
    public Pict compress(int paramInt1, int paramInt2) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict localPict = new Pict(4, true);
        QDException.checkError(CompressPicture(_ID(), QTObject.ID(localPict), paramInt1, paramInt2));
        return localPict;
    }

    /**
     * This allows your application to compress a single-frame image with more variance that stored as a picture.
     * Returns the result in a new picture.
     * QuickTime::FCompressPicture()
     * @param colorDepth Specifies the depth at which the image is to be compressed
     *          // * @param clut Specifies the custom color lookup table. Uses source color table only.
     * @param quality Specifies the desired compressed image quality
     * @param doDither Specifies whether to dither or not
     * @param compressAgain Indicates whether to recompress compressed image data in the picture
     * @param cType Specifies the compressor type
     * @param codec Specifies the compression indentifier
     * @return dstPicture The destination compressed image
     * @return {@link quicktime.QTException}
     */
    public Pict fCompress(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict localPict = new Pict(4, true);
        QDException.checkError(FCompressPicture(_ID(), QTObject.ID(localPict), (short) paramInt1, 0, paramInt2, (short) paramInt3, (short) paramInt4, 0, paramInt5, paramInt6));
        return localPict;
    }

    /**
     * To draw a picture on any type of output device.
     * QuickTime::DrawPicture()
     * @param cg Specified graph port on which it will be drawn. This may NOT be null.
     * @param frameRect Specified rectangle with the coordinates in which to draw the picture.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * Allows the drawing of an image that is stored as a picture.
     * Also, trims that image to fit the region you specify.
     * QuickTime::DrawTrimmedPicture()
     * @param cg Specified graph port on which it will be drawn. This may NOT be null.
     * @param frameRect Specifies the rectangle into which the decompressed image is to be loaded
     * @param trimMask Specifies the destination clipping region
     * @param doDither Specifies whether to dither or not
     * @param {@link quicktime.QTException}
     */
    public void drawTrimmed(QDGraphics paramQDGraphics, QDRect paramQDRect, Region paramRegion, int paramInt) throws QTException {
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

    /**
     * This allows your application to create an 80-by-80 pixel thumbnail picture from a specified picture.
     * QuickTime::MakeThumbnailFromPicture()
     * @param colorDepth Specifies the desired depth at which the image is likely to be viewed
     * @return The desired thumbnail picture
     * @return {@link quicktime.QTException}
     */
    public Pict makeThumbnail(int paramInt) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict localPict = new Pict(4, false);
        QDException.checkError(MakeThumbnailFromPicture(_ID(), (short) paramInt, QTObject.ID(localPict), 0));
        return localPict;
    }

    /**
     * Returns a version of the Pict as an EncodedImage object. This references
     * the same data as is held within the Pict, which is locked and must be kept
     * locked for the duration of the lifetime of the EncodedImage.
     */
    public RawEncodedImage toEncodedImage() throws QDException {
        if (isOpen())
            throw new QDException(-9);
        return RawEncodedImage.fromQTHandle(this);
    }

    /**
     * Write out the Pict to the specified file.
     * @param file the destination file that the Pict will be written to.
     * @param {@link quicktime.qd.QDException}
     * @param java.io.IOException
     */
    public void writeToFile(File paramFile) throws QDException, IOException {
        if (isOpen())
            throw new QDException(-9);
        FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
        localFileOutputStream.write(new byte[512]);
        byte[] arrayOfByte = getBytes();
        localFileOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
        localFileOutputStream.close();
    }

    /**
     * Print information about this object.
     * @return a string representation of this object
     */
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

    private static native short MakeThumbnailFromPixMap(int paramInt1, byte[] paramArrayOfByte, short paramShort, int paramInt2, int paramInt3);

    private static native void UnlockPixels(int paramInt);

    private static native int GetTrackPict(int paramInt1, int paramInt2);

    private static native short GetMoviesError();

    private static native int GetMoviePict(int paramInt1, int paramInt2);

    private static native int GetMoviePosterPict(int paramInt);

    private static native int SGGrabPict(int paramInt1, int[] paramArrayOfInt, byte[] paramArrayOfByte, short paramShort, int paramInt2);

    private static native int GraphicsImportGetAsPicture(int paramInt, int[] paramArrayOfInt);

    private static native int SCCompressPicture(int paramInt1, int paramInt2, int paramInt3);

    private static native int OpenCPicture(byte[] paramArrayOfByte);

    private static native short QDError();

    private static native void ClosePicture();

    private static native short CompressPicture(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short FCompressPicture(int paramInt1, int paramInt2, short paramShort1, int paramInt3, int paramInt4, short paramShort2, short paramShort3, int paramInt5, int paramInt6, int paramInt7);

    private static native void DrawPicture(int paramInt, byte[] paramArrayOfByte);

    private static native short DrawTrimmedPicture(int paramInt1, byte[] paramArrayOfByte, int paramInt2, short paramShort, int paramInt3);

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
