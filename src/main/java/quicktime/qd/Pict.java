package quicktime.qd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.StreamingManager;
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

    private static final int kNativeSize = 10;

    private static QDGraphics gw = null;

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * Creates an 80-by-80 pixel thumbnail picture from the pixmap associated with this QDGraphics.
     * @see "QuickTime::MakeThumbnailFromPixMap()"
     * @param qd the QDGraphics from which to make the thumbnail
     * @param src a QDRect object defining the portion of the image to use for the thumbnail
     * @param colorDepth specifies the depth at which the image is likely to be viewed
     * @return a Pict object representing the thumbnail
     * @throws quicktime.QTException
     */
    public static Pict thumbnailFromQDGraphics(QDGraphics qd, QDRect src, int colorDepth) throws QTException {
        Pict thumbnail = new Pict(4, false);
        if (qd == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(qd), 0);
            int j = GetGWorldPixMap(QTObject.ID(qd));
            LockPixels(j);
            i = MakeThumbnailFromPixMap(j, src.getRect(), (short) colorDepth, QTObject.ID(thumbnail), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        QDException.checkError(i);
        return thumbnail;
    }

    /**
     * This method creates a QuickDraw picture from the specified track at the specified time.
     * @see "QuickTime::GetTrackPict()"
     * @param t the track object
     * @param time Specifies the track image for the picture. The time parameter contains the time from which the image is taken.
     * @return a Pict object for the specified time.
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Track#getPict(int)
     */
    public static Pict fromTrack(Track t, int time) throws QTException {
        int i = GetTrackPict(QTObject.ID(t), time);
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    /**
     * This method creates a picture from the specified movie at the specified time.
     * This function uses only those movie tracks that are currently enabled and would therefore
     * be used in playback. Your application may call this function even if the movie is inactive.
     * @see "QuickTime::GetMoviePict()"
     * @param time contains the time from which the image is taken.
     * @param m the Movie object
     * @return a Pict object
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getPict(int)
     */
    public static Pict fromMovie(Movie time, int m) throws QTException {
        int i = GetMoviePict(QTObject.ID(time), m);
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    /**
     * This method creates a picture that contains a movie's poster.
     * returns a PicHandle object for the image.
     * @see "QuickTime::GetMoviePosterPict()"
     * @param m the Movie object
     * @return a Pict object
     * @throws quicktime.QTException
     * @see quicktime.std.movies.Movie#getPosterPict()
     */
    public static Pict fromMovie(Movie m) throws QTException {
        int i = GetMoviePosterPict(QTObject.ID(m));
        StdQTException.checkError(GetMoviesError());
        return new Pict(i);
    }

    /**
     * This method creates a picture that contains a representation of the presentation
     * returns a PicHandle object for the image.
     * @see "QuickTime::QTSPresGetPicture()"
     * @param p the Presentation object
     * @param s the stream object
     * @return a Pict object
     * @throws quicktime.QTException
     * @see quicktime.streaming.Presentation#getPicture(quicktime.streaming.Stream)
     */
    public static Pict fromPresentation(Presentation p, Stream s) throws QTException {
        int[] picture = new int[1];
        StdQTException.checkError(QTSPresGetPicture(QTObject.ID(p), QTObject.ID(s), picture));
        return picture[0] == 0 ? null : new Pict(picture[0]);
    }

    /**
     * Provides a simple interface to obtain a QuickDraw picture from a sequence grabber component.
     * The sequence grabber can display the picture directly, or it can write the picture to an
     * offscreen buffer. This function is limited in scope, however, and does not allow you to
     * control all of the parameters that govern the operation. When you call this function,
     * the sequence grabber component obtains and configures appropriate sequence grabber
     * channel components (if necessary), grabs the data, and then releases any components
     * it obtained.
     * @see "QuickTime::SGGrabPict"
     * @param bounds The bounding rectangle.
     * @param offscreenDepth The bit depth of the Pict.
     * @param grabPictFlags The flags for the operation.
     * @return The Pict.
     * @throws quicktime.QTException
     * @see quicktime.std.sg.SequenceGrabber#grabPict(quicktime.qd.QDRect, int, int)
     */
    public static Pict fromSequenceGrabber(SequenceGrabber sg, QDRect bounds, int offscreenDepth, int grabPictFlags) throws QTException {
        int[] pict = new int[1];
        int i = SGGrabPict(QTObject.ID(sg), pict, bounds.getRect(), (short) offscreenDepth, grabPictFlags);
        StdQTException.checkError(i);
        return pict[0] == 0 ? null : new Pict(pict[0]);
    }

    /**
     * Creates a new Pict containing the image currently in use by this import component.
     * @see "QuickTime::GraphicsImportGetAsPicture"
     * @return a pict
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsImporter#getAsPicture()
     */
    public static Pict fromGraphicsImporter(GraphicsImporter gi) throws QTException {
        int[] pict = { 0 };
        int i = GraphicsImportGetAsPicture(QTObject.ID(gi), pict);
        StdQTException.checkError(i);
        return new Pict(pict[0]);
    }

    /**
     * Creates a new Pict containing the image currently in use by this import component.
     * @see "QuickTime::GraphicsExportGetInputPicture"
     * @return a pict
     * @throws quicktime.QTException
     * @see quicktime.std.image.GraphicsExporter#getInputPicture()
     */
    public static Pict fromGraphicsExporter(GraphicsExporter ge) throws QTException {
        int[] picture = { 0 };
        int i = GraphicsExportGetInputPicture(QTObject.ID(ge), picture);
        StdQTException.checkError(i);
        return new Pict(picture[0]);
    }

    /**
     * Compresses a Pict returning a new Pict.
     * @see "QuickTime::SCCompressPicture()"
     * @param src the source Pict.
     * @return a Pict
     * @throws quicktime.QTException
     * @see quicktime.std.qtcomponents.ImageCompressionDialog#compressPicture(quicktime.qd.Pict)
     */
    public static Pict fromImageCompressionDialog(ImageCompressionDialog ci, Pict src) throws QTException {
        Pict dstPicture = new Pict(4, true);
        int i = SCCompressPicture(QTObject.ID(ci), QTObject.ID(src), QTObject.ID(dstPicture));
        if (i == 1)
            throw new StdQTException(-128);
        StdQTException.checkError(i);
        return dstPicture;
    }

    /**
     * Opens a PICT that is stored in the file. This method does no checking
     * to ensure that the file is valid PICT file.
     * @param file the PICT file
     * @return a Pict
     * @throws quicktime.QTException
     * @throws java.io.IOException
     */
    public static Pict fromFile(File file) throws QTException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] srcArray = new byte[fileInputStream.available() - 512];
        fileInputStream.skip(512L);
        fileInputStream.read(srcArray, 0, srcArray.length);
        fileInputStream.close();
        Pict pict = new Pict(srcArray.length, false);
        pict.copyFromArray(0, srcArray, 0, srcArray.length);
        return pict;
    }

    /**
     * Use OpenCPicture to begin defining a picture, collecting all of your subsequent
     * drawing commands in this record. The returned Pict must be closed before any
     * other methods can be applied to it.
     * @see "QuickDraw::OpenCPicture"
     * @param port the port which the open picture will record operations from
     * @param params the parameters for the new Pict
     * @return a Pict
     * @throws quicktime.QTException
     */
    public static Pict open(QDGraphics port, OpenCPicParams params) throws QTException {
        if (gw != null)
            throw new QDException(-9);
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(port), 0);
            gw = port;
            i = OpenCPicture(params.getBytes());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
            try {
                QDException.checkError(QDError());
            } catch (QTException e) {
                gw = null;
                throw e;
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
    public Pict(byte[] pictBytes) throws QTException {
        super(pictBytes.length, false);
        copyFromArray(0, pictBytes, 0, pictBytes.length);
    }

    private Pict(int nativeRep) throws QTException {
        super(nativeRep, null, false);
    }

    private Pict(int size, boolean clear) throws QTException {
        super(size, clear);
    }

    /**
     * Returns the picture frame rectangle referenced of the Pict.
     */
    public QDRect getPictFrame() {
        byte[] destArray = new byte[8];
        getBytesAt(2, 8, destArray, 0);
        if (QTSession.isCurrentOS(2)) {
            for (int i = 0; i < 8; i += 2) setShortInArray(destArray, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(destArray, i)));
        }
        return QDRect.fromArray(destArray, 8);
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
     * @see "QuickDraw::ClosePicture()"
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
     * @see "QuickTime::CompressPicture()"
     * @param quality Specifies the desired compressed image quality
     * @param cType Specifies the compressor type
     * @return dstPicture The destination compressed image
     * @throws quicktime.QTException
     */
    public Pict compress(int quality, int cType) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict dstPicture = new Pict(4, true);
        QDException.checkError(CompressPicture(_ID(), QTObject.ID(dstPicture), quality, cType));
        return dstPicture;
    }

    /**
     * This allows your application to compress a single-frame image with more variance that stored as a picture.
     * Returns the result in a new picture.
     * @see "QuickTime::FCompressPicture()"
     * @param colorDepth Specifies the depth at which the image is to be compressed
     * param clut Specifies the custom color lookup table. Uses source color table only.
     * @param quality Specifies the desired compressed image quality
     * @param doDither Specifies whether to dither or not
     * @param compressAgain Indicates whether to recompress compressed image data in the picture
     * @param cType Specifies the compressor type
     * @param codec Specifies the compression indentifier
     * @return dstPicture The destination compressed image
     * @throws quicktime.QTException
     */
    public Pict fCompress(int colorDepth, int quality, int doDither, int compressAgain, int cType, int codec) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict dstPicture = new Pict(4, true);
        QDException.checkError(FCompressPicture(_ID(), QTObject.ID(dstPicture), (short) colorDepth, 0, quality, (short) doDither, (short) compressAgain, 0, cType, codec));
        return dstPicture;
    }

    /**
     * To draw a picture on any type of output device.
     * @see "QuickTime::DrawPicture()"
     * @param cg Specified graph port on which it will be drawn. This may NOT be null.
     * @param frameRect Specified rectangle with the coordinates in which to draw the picture.
     * @throws quicktime.QTException
     */
    public void draw(QDGraphics cg, QDRect frameRect) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        if (cg == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(cg), 0);
            DrawPicture(_ID(), frameRect.getBytes());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        QDException.checkError(QDError());
    }

    /**
     * Allows the drawing of an image that is stored as a picture.
     * Also, trims that image to fit the region you specify.
     * @see "QuickTime::DrawTrimmedPicture()"
     * @param cg Specified graph port on which it will be drawn. This may NOT be null.
     * @param frameRect Specifies the rectangle into which the decompressed image is to be loaded
     * @param trimMask Specifies the destination clipping region
     * @param doDither Specifies whether to dither or not
     * @throws quicktime.QTException
     */
    public void drawTrimmed(QDGraphics cg, QDRect frameRect, Region trimMask, int doDither) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        if (cg == null) {
            throw new StdQTException(-50);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(cg), 0);
            i = DrawTrimmedPicture(_ID(), frameRect.getBytes(), QTObject.ID(trimMask), (short) doDither, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        QDException.checkError(i);
    }

    /**
     * This allows your application to create an 80-by-80 pixel thumbnail picture from a specified picture.
     * @see "QuickTime::MakeThumbnailFromPicture()"
     * @param colorDepth Specifies the desired depth at which the image is likely to be viewed
     * @return The desired thumbnail picture
     * @throws quicktime.QTException
     */
    public Pict makeThumbnail(int colorDepth) throws QTException {
        if (isOpen())
            throw new QDException(-9);
        Pict thumbnail = new Pict(4, false);
        QDException.checkError(MakeThumbnailFromPicture(_ID(), (short) colorDepth, QTObject.ID(thumbnail), 0));
        return thumbnail;
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
     * @throws quicktime.qd.QDException
     * @throws java.io.IOException
     */
    public void writeToFile(File file) throws QDException, IOException {
        if (isOpen())
            throw new QDException(-9);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(new byte[512]);
        byte[] movie = getBytes();
        fileOutputStream.write(movie, 0, movie.length);
        fileOutputStream.close();
    }

    /**
     * Print information about this object.
     * @return a string representation of this object
     */
    public String toString() {
        return super.toString() +
                "[pictFrame=" + getPictFrame() +
                ",isOpen=" + isOpen() +
                "]";
    }

    @Override
    protected void _dispose() {
        try {
            close();
        } catch (QDException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this, "close");
        }
    }

    private static int GetGWorldPixMap(int gworld) {
        return QuickDrawManager.GetGWorldPixMap(gworld);
    }

    private static byte LockPixels(int pixMap) {
        return QuickDrawManager.LockPixels(pixMap);
    }

    private static short MakeThumbnailFromPixMap(int src, byte[] srcRect, short colorDepth, int thumbnail, int progressProc) {
        return ImageCompressionManager.MakeThumbnailFromPixMap(src, srcRect, colorDepth, thumbnail, progressProc);
    }

    private static void UnlockPixels(int pixMap) {
        QuickDrawManager.UnlockPixels(pixMap);
    }

    private static int GetTrackPict(int track, int time) {
        return MovieToolbox.GetTrackPict(track, time);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int GetMoviePict(int movie, int time) {
        return MovieToolbox.GetMoviePict(movie, time);
    }

    private static int GetMoviePosterPict(int movie) {
        return MovieToolbox.GetMoviePosterPict(movie);
    }

    private static int SGGrabPict(int sg, int[] pict, byte[] bounds, short offscreenDepth, int grabPictFlags) {
        return SequenceGrabberManager.SGGrabPict(sg, pict, bounds, offscreenDepth, grabPictFlags);
    }

    private static int GraphicsImportGetAsPicture(int gi, int[] pict) {
        return GraphicsImportManager.GraphicsImportGetAsPicture(gi, pict);
    }

    private static int SCCompressPicture(int ci, int srcPicture, int dstPicture) {
        return ImageCompressionManager.SCCompressPicture(ci, srcPicture, dstPicture);
    }

    private static int OpenCPicture(byte[] params) {
        return QuickDrawManager.OpenCPicture(params);
    }

    private static short QDError() {
        return QuickDrawManager.QDError();
    }

    private static void ClosePicture() {
        QuickDrawManager.ClosePicture();
    }

    private static short CompressPicture(int srcPicture, int dstPicture, int quality, int cType) {
        return ImageCompressionManager.CompressPicture(srcPicture, dstPicture, quality, cType);
    }

    private static short FCompressPicture(int srcPicture, int dstPicture, short colorDepth, int ctable, int quality, short doDither, short compressAgain, int progressProc, int cType, int codec) {
        return ImageCompressionManager.FCompressPicture(srcPicture, dstPicture, colorDepth, ctable, quality, doDither, compressAgain, progressProc, cType, codec);
    }

    private static void DrawPicture(int pict, byte[] dstRect) {
        QuickDrawManager.DrawPicture(pict, dstRect);
    }

    private static short DrawTrimmedPicture(int pict, byte[] frame, int trimMask, short doDither, int gEtc) {
        return QuickDrawManager.DrawTrimmedPicture(pict, frame, trimMask, doDither, gEtc);
    }

    private static short MakeThumbnailFromPicture(int picture, int colorDepth, int thumbnail, int progressProc) {
        return ImageCompressionManager.MakeThumbnailFromPicture(picture, colorDepth, thumbnail, progressProc);
    }

    private static int GraphicsExportGetInputPicture(int ge, int[] picture) {
        return GraphicsExportManager.GraphicsExportGetInputPicture(ge, picture);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static void GetGWorld(int[] destArray1, int[] destArray2) {
        QuickDrawManager.GetGWorld(destArray1, destArray2);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static short QTSPresGetPicture(int presentation, int stream, int[] picture) {
        return StreamingManager.QTSPresGetPicture(presentation, stream, picture);
    }
}
