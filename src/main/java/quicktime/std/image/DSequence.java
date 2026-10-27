package quicktime.std.image;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.QuickDrawManager;
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


/**
 * This class is used for decompressing a sequence of images.
 * The Movie Toolbox handles the details of decompressing image sequences in
 * QuickTime movies. If you need to decompress other sequences, your application
 * can use this class.
 * @see quicktime.std.image.CDSequence,
 * {@link quicktime.std.image.CSequence}
 */
public final class DSequence extends CDSequence {

    /**
     * Creating an instance of this class signals the beginning of the process of
     * decompressing a sequence of frames.
     * @see "QuickTime::DecompressSequenceBegin"
     * @param desc an ImageDescription object which describes the compressed data
     * @param port a QDGraphics for the destination image. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to decompress, or null to use
     *                  the rectangle defined in the ImageDescription associated with this sequence
     * @param matrix a Matrix that specifies how to transform the image during decompression
     * @param mask a RgnHandle which defines a clipping region in the destination
     *                  coordinate system, or null
     * @param flags flags providing further control information
     * @param accuracy the accuracy desired in the decompressed image
     * @param codec a Decompressor identifier
     */
    public DSequence(ImageDescription desc, QDGraphics port, QDRect srcRect, Matrix matrix, Region mask, int flags, int accuracy, CodecComponent codec) throws QTException {
        super(begin(desc, port, srcRect, matrix, mask, flags, accuracy, codec), port);
        this.mImageDescription = desc;
    }

    /**
     * This constructor allows you to pass a compressed sample so the codec can perform
     * preflighting before the first decompressFrame call.
     * @see "QuickTime::DecompressSequenceBeginS"
     * @param desc an ImageDescription object which describes the compressed data
     * @param data encoded image data
     * @param port a QDGraphics for the destination image. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to decompress, or null to use
     *                  the rectangle defined in the ImageDescription associated with this sequence
     * @param matrix a Matrix that specifies how to transform the image during decompression
     * @param mask a RgnHandle which defines a clipping region in the destination
     *                  coordinate system, or null
     * @param flags flags providing further control information
     * @param accuracy the accuracy desired in the decompressed image
     * @param codec a Decompressor identifier
     */
    public DSequence(ImageDescription desc, EncodedImage data, QDGraphics port, QDRect srcRect, Matrix matrix, Region flags, int mask, int accuracy, CodecComponent codec) throws QTException {
        super(beginS(desc, data, port, srcRect, matrix, flags, mask, accuracy, codec), port);
        this.mImageDescription = desc;
    }

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private static final int codecDSeqFlushInstdOfDirtying = 256;

    private static int begin(ImageDescription desc, QDGraphics port, QDRect srcRect, Matrix matrix, Region flags, int mask, int accuracy, CodecComponent codec) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = 0;
        if (port == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(QTObject.ID(port), 0);
            i = DecompressSequenceBegin(arrayOfInt, QTObject.ID(desc), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (QTSession.isCurrentOS(4)) {
            StdQTException.checkError(SetDSequenceFlags(arrayOfInt[0], codecDSeqFlushInstdOfDirtying, codecDSeqFlushInstdOfDirtying));
        }
        return arrayOfInt[0];
    }

    private static int beginS(ImageDescription desc, EncodedImage paramEncodedImage, QDGraphics port, QDRect srcRect, Matrix matrix, Region flags, int mask, int accuracy, CodecComponent codec) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = -50;
        if (port == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(port), 0);
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = DecompressSequenceBeginS(arrayOfInt, QTObject.ID(desc), ((ByteEncodedImage) paramEncodedImage).getBytes(), paramEncodedImage.getSize(), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = DecompressSequenceBeginS(arrayOfInt, QTObject.ID(desc), ((IntEncodedImage) paramEncodedImage).getInts(), paramEncodedImage.getSize(), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = DecompressSequenceBeginS(arrayOfInt, QTObject.ID(desc), QTObject.ID((RawEncodedImage) paramEncodedImage), paramEncodedImage.getSize(), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (QTSession.isCurrentOS(4))
            StdQTException.checkError(SetDSequenceFlags(arrayOfInt[0], codecDSeqFlushInstdOfDirtying, codecDSeqFlushInstdOfDirtying));
        return arrayOfInt[0];
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrameS"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameS(EncodedImage data, int inFlags) throws StdQTException {
        if ((data instanceof RawEncodedImage))
            return decompressFrameS((RawEncodedImage) data, inFlags);
        if ((data instanceof IntEncodedImage))
            return decompressFrameS((IntEncodedImage) data, inFlags);
        if ((data instanceof ByteEncodedImage)) {
            return decompressFrameS((ByteEncodedImage) data, inFlags);
        }
        throw new StdQTException(-50);
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrameS"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameS(ByteEncodedImage data, int inFlags) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameS(_ID(), data.getBytes(), data.getSize(), (short) inFlags, arrayOfShort, 0);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrameS"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameS(IntEncodedImage data, int inFlags) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameS(_ID(), data.getInts(), data.getSize(), (short) inFlags, arrayOfShort, 0);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrameS"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameS(RawEncodedImage data, int inFlags) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameS(_ID(), QTObject.ID(data), data.getSize(), (short) inFlags, arrayOfShort, 0);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrame"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrame(EncodedImage data, int inFlags) throws StdQTException {
        if ((data instanceof RawEncodedImage))
            return decompressFrame((RawEncodedImage) data, inFlags);
        if ((data instanceof IntEncodedImage))
            return decompressFrame((IntEncodedImage) data, inFlags);
        if ((data instanceof ByteEncodedImage)) {
            return decompressFrame((ByteEncodedImage) data, inFlags);
        }
        throw new StdQTException(-50);
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrame"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrame(ByteEncodedImage data, int inFlags) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrame(_ID(), data.getBytes(), (short) inFlags, arrayOfShort, 0);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrame"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrame(IntEncodedImage data, int inFlags) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrame(_ID(), data.getInts(), (short) inFlags, arrayOfShort, 0);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Your application calls this method to decompress one of a sequence of frames.
     * @see "QuickTime::DecompressSequenceFrame"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrame(RawEncodedImage data, int inFlags) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrame(_ID(), QTObject.ID(data), (short) inFlags, arrayOfShort, 0);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Queues a frame for decompression and specifies the time at which
     * decompression will begin.
     * @see "QuickTime::DecompressSequenceFrameWhen"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @param frameTime an ICMFrameTime object that contains the frame's time information,
     *                   including the time at which the frame should be displayed, its
     *                   duration, and the movie's playback rate.
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameWhen(ByteEncodedImage data, int inFlags, ICMFrameTime frameTime) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(), data.getBytes(), data.getSize(), (short) inFlags, arrayOfShort, 0, frameTime.getBytes());
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Queues a frame for decompression and specifies the time at which
     * decompression will begin.
     * @see "QuickTime::DecompressSequenceFrameWhen"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @param frameTime an ICMFrameTime object that contains the frame's time information,
     *                   including the time at which the frame should be displayed, its
     *                   duration, and the movie's playback rate.
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameWhen(IntEncodedImage data, int inFlags, ICMFrameTime frameTime) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(), data.getInts(), data.getSize(), (short) inFlags, arrayOfShort, 0, frameTime.getBytes());
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Queues a frame for decompression and specifies the time at which
     * decompression will begin.
     * @see "QuickTime::DecompressSequenceFrameWhen"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @param frameTime an ICMFrameTime object that contains the frame's time information,
     *                   including the time at which the frame should be displayed, its
     *                   duration, and the movie's playback rate.
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameWhen(RawEncodedImage data, int inFlags, ICMFrameTime frameTime) throws StdQTException {
        short[] arrayOfShort = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(), QTObject.ID(data), data.getSize(), (short) inFlags, arrayOfShort, 0, frameTime.getBytes());
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * Queues a frame for decompression and specifies the time at which
     * decompression will begin.
     * @see "QuickTime::DecompressSequenceFrameWhen"
     * @param data encoded image data
     * @param inFlags flags providing further control information
     * @param frameTime an ICMFrameTime object that contains the frame's time information,
     *                   including the time at which the frame should be displayed, its
     *                   duration, and the movie's playback rate.
     * @return the outFlags value
     * @throws quicktime.std.StdQTException
     */
    public int decompressFrameWhen(EncodedImage data, int inFlags, ICMFrameTime frameTime) throws StdQTException {
        if ((data instanceof RawEncodedImage))
            return decompressFrameWhen((RawEncodedImage) data, inFlags, frameTime);
        if ((data instanceof IntEncodedImage))
            return decompressFrameWhen((IntEncodedImage) data, inFlags, frameTime);
        if ((data instanceof ByteEncodedImage)) {
            return decompressFrameWhen((ByteEncodedImage) data, inFlags, frameTime);
        }
        throw new StdQTException(-50);
    }

    /**
     * This method sets the mode used when drawing the decompressed image.
     * @see "QuickTime::SetDSequenceTransferMode"
     * @param mode the transfer mode to use when drawing the decompressed image
     * @throws quicktime.std.StdQTException
     */
    public void setGraphicsMode(GraphicsMode mode) throws StdQTException {
        StdQTException.checkError(SetDSequenceTransferMode(_ID(), (short) mode.getGraphicsMode(), mode.getColor().getRGBColor()));
    }

    /**
     * This method sets the mode used when drawing the decompressed image.
     * @see "QuickTime::SetDSequenceTransferMode"
     * @param mode the transfer mode to use when drawing the decompressed image
     * @param opColor an QDColor object to be used in addPin, subPin, blend and
     *                 transparent operations. If null, the opColor is left unchanged.
     * @throws quicktime.std.StdQTException
     */
    public void setTransferMode(int mode, QDColor opColor) throws StdQTException {
        setGraphicsMode(new GraphicsMode(mode, opColor));
    }

    /**
     * This method defines the portion of the image to decompress.
     * @see "QuickTime::SetDSequenceSrcRect"
     * @param srcRect a QDRect object defining the portion of the object to decompress
     * @throws quicktime.std.StdQTException
     */
    public void setSrcRect(QDRect srcRect) throws StdQTException {
        StdQTException.checkError(SetDSequenceSrcRect(_ID(), srcRect.getRect()));
    }

    /**
     * This method assigns a mapping matrix to the sequence.
     * @see "QuickTime::SetDSequenceMatrix"
     * @param matrix a Matrix object that specifies how to transform the image during
     *                 decompression
     * @throws quicktime.std.StdQTException
     */
    public void setMatrix(Matrix matrix) throws StdQTException {
        StdQTException.checkError(SetDSequenceMatrix(_ID(), matrix.getBytes()));
    }

    /**
     * This method returns the matrix associated with the sequence
     * @see "QuickTime::GetDSequenceMatrix"
     * @return Matrix a matrix object that specifies the transform of the image sequence
     * @throws quicktime.std.StdQTException
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        StdQTException.checkError(GetDSequenceMatrix(_ID(), localMatrix.getBytes()));
        return localMatrix;
    }

    /**
     * This method sets the flags of the image sequence
     * @see "QuickTime::SetDSequenceFlags"
     * @param flags the flags associated with the sequence
     * @param flagsMask the masks to use for filtering out unwanted flags
     * @throws quicktime.std.StdQTException
     */
    public void setFlags(int flags, int flagsMask) throws StdQTException {
        StdQTException.checkError(SetDSequenceFlags(_ID(), flags, flagsMask));
    }

    /**
     * This method assigns a clipping region to the sequence.
     * @see "QuickTime::SetDSequenceMask"
     * @param mask a Region object which is applied to the desitnation image. If null
     *               then masking is stopped.
     * @throws quicktime.std.StdQTException
     */
    public void setMask(Region mask) throws StdQTException {
        StdQTException.checkError(SetDSequenceMask(_ID(), QTObject.ID(mask)));
    }

    /**
     * This method assigns a blend matte to the sequence.
     * @see "QuickTime::SetDSequenceMatte"
     * @param matte a PixMap object which contains a blend matte
     * @param matteRect a QDRect object which defines the boundary of the matte
     * @throws quicktime.std.StdQTException
     */
    public void setMatte(PixMap matte, QDRect matteRect) throws StdQTException {
        StdQTException.checkError(SetDSequenceMatte(_ID(), QTObject.ID(matte), matteRect.getRect()));
    }

    /**
     * This method adjusts the decompression accuracy for this sequence.
     * @see "QuickTime::SetDSequenceAccuracy"
     * @param accuracy the accuracy desired in the decompressed image
     * @throws quicktime.std.StdQTException
     */
    public void setAccuracy(int accuracy) throws StdQTException {
        StdQTException.checkError(SetDSequenceAccuracy(_ID(), accuracy));
    }

    /**
     * This method helps you determine the location of the offscreen image buffer
     * allocated by the decompressor.
     * @see "QuickTime::GetDSequenceImageBuffer"
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @throws quicktime.std.StdQTException
     */
    public QDGraphics getImageBuffer() throws StdQTException {
        return QDGraphics.fromDSequenceImage(this);
    }

    /**
     * This method enables you to determine the location of the offscreen buffer
     * allocated by the decompressor.
     * @see "QuickTime::GetDSequenceScreenBuffer"
     * @return a QDGraphics object which is the graphics world for the screen buffer
     * @throws quicktime.std.StdQTException
     */
    public QDGraphics getScreenBuffer() throws StdQTException {
        return QDGraphics.fromDSequenceScreen(this);
    }

    /**
     * Tests to see if an image contains data at a given point.
     * @see "QuickTime::PtInDSequenceData"
     * @param data encoded image data
     * @param where a Point object, (0,0) is upper left corner of the image
     * @return true if the compressed data contains data at the point specified
     *               by the where parameter. false if the specified point falls within
     *               a blank portion of the image.
     * @throws quicktime.std.StdQTException
     */
    public boolean ptInData(EncodedImage data, QDPoint where) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = -50;
        if ((data instanceof ByteEncodedImage)) {
            i = PtInDSequenceData(_ID(), ((ByteEncodedImage) data).getBytes(), data.getSize(), where.getPoint(), arrayOfByte);
        } else if ((data instanceof IntEncodedImage)) {
            i = PtInDSequenceData(_ID(), ((IntEncodedImage) data).getInts(), data.getSize(), where.getPoint(), arrayOfByte);
        } else if ((data instanceof RawEncodedImage)) {
            i = PtInDSequenceData(_ID(), QTObject.ID((RawEncodedImage) data), data.getSize(), where.getPoint(), arrayOfByte);
        }
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * The HitTestDSequenceData function allows the calling application to perform hit testing on compressed data.
     * @see "QuickTime::HitTestDSequenceData"
     * @param data compressed data in the format specified by the description of the DSequence.
     * @param where A QuickDraw Point (0,0) based at the top-left corner of the image.
     * @param hitFlags indicates the hit-testing behaviour that is sought
     * @return true if the compressed data contains data at the point specified by the where parameter.
     * @throws quicktime.std.StdQTException
     */
    public boolean hitTestData(EncodedImage data, QDPoint where, int hitFlags) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        if ((data instanceof ByteEncodedImage)) {
            i = HitTestDSequenceData(_ID(), ((ByteEncodedImage) data).getBytes(), data.getSize(), where.getPoint(), arrayOfInt, hitFlags);
        } else if ((data instanceof IntEncodedImage)) {
            i = HitTestDSequenceData(_ID(), ((IntEncodedImage) data).getInts(), data.getSize(), where.getPoint(), arrayOfInt, hitFlags);
        } else if ((data instanceof RawEncodedImage)) {
            i = HitTestDSequenceData(_ID(), QTObject.ID((RawEncodedImage) data), data.getSize(), where.getPoint(), arrayOfInt, hitFlags);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0] != 0;
    }

    /**
     * Stops a decompression sequence, aborting processing of any queued frames.
     * @see "QuickTime::CDSequenceFlush"
     */
    public void flush() throws StdQTException {
        StdQTException.checkError(CDSequenceFlush(_ID()));
    }

    /**
     * Notifies the Image Compression Manager that the destination port for the given image
     * decompression sequence has been invalidated.
     * @see "QuickTime::CDSequenceInvalidate"
     * @param invalRgn a Region describing the invalid portion of the image
     * @throws quicktime.std.StdQTException
     */
    public void invalidate(Region invalRgn) throws StdQTException {
        StdQTException.checkError(CDSequenceInvalidate(_ID(), QTObject.ID(invalRgn)));
    }

    /**
     * Hides the cursor during decompression operations.
     * @see "QuickTime::ICMShieldSequenceCursor"
     */
    public void shieldCursor() throws StdQTException {
        StdQTException.checkError(ICMShieldSequenceCursor(_ID()));
    }

    /**
     * Sets the timecode value for the frame that is about to be decompressed.
     * QuickTime__SetDSequenceTimeCode
     */
    public void setTimeCode(TimeCodeDef paramTimeCodeDef, TimeCodeTime paramTimeCodeTime) throws StdQTException {
        StdQTException.checkError(SetDSequenceTimeCode(_ID(), paramTimeCodeDef.getBytes(), paramTimeCodeTime.getBytes()));
    }

    private void setDataProc(Object paramObject, int paramInt) {
    }

    private static short DecompressSequenceBegin(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt4, short paramShort2, int paramInt5, int paramInt6) {
        return ImageCompressionManager.DecompressSequenceBegin(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfByte2, paramShort1, paramInt4, paramShort2, paramInt5, paramInt6);
    }

    private static short DecompressSequenceBeginS(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt6, short paramShort2, int paramInt7, int paramInt8) {
        return ImageCompressionManager.DecompressSequenceBeginS(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfByte1, paramArrayOfByte2, paramShort1, paramInt6, paramShort2, paramInt7, paramInt8);
    }

    private static short DecompressSequenceBeginS(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, short paramShort1, int paramInt5, short paramShort2, int paramInt6, int paramInt7) {
        return ImageCompressionManager.DecompressSequenceBeginS(paramArrayOfInt, paramInt1, paramArrayOfByte1, paramInt2, paramInt3, paramInt4, paramArrayOfByte2, paramArrayOfByte3, paramShort1, paramInt5, paramShort2, paramInt6, paramInt7);
    }

    private static short DecompressSequenceBeginS(int[] paramArrayOfInt1, int paramInt1, int[] paramArrayOfInt2, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt5, short paramShort2, int paramInt6, int paramInt7) {
        return ImageCompressionManager.DecompressSequenceBeginS(paramArrayOfInt1, paramInt1, paramArrayOfInt2, paramInt2, paramInt3, paramInt4, paramArrayOfByte1, paramArrayOfByte2, paramShort1, paramInt5, paramShort2, paramInt6, paramInt7);
    }

    private static short DecompressSequenceFrameS(int paramInt1, int paramInt2, int paramInt3, short paramShort, short[] paramArrayOfShort, int paramInt4) {
        return ImageCompressionManager.DecompressSequenceFrameS(paramInt1, paramInt2, paramInt3, paramShort, paramArrayOfShort, paramInt4);
    }

    private static short DecompressSequenceFrameS(int paramInt1, byte[] paramArrayOfByte, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3) {
        return ImageCompressionManager.DecompressSequenceFrameS(paramInt1, paramArrayOfByte, paramInt2, paramShort, paramArrayOfShort, paramInt3);
    }

    private static short DecompressSequenceFrameS(int paramInt1, int[] paramArrayOfInt, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3) {
        return ImageCompressionManager.DecompressSequenceFrameS(paramInt1, paramArrayOfInt, paramInt2, paramShort, paramArrayOfShort, paramInt3);
    }

    private static short DecompressSequenceFrame(int paramInt1, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3) {
        return ImageCompressionManager.DecompressSequenceFrame(paramInt1, paramInt2, paramShort, paramArrayOfShort, paramInt3);
    }

    private static short DecompressSequenceFrame(int paramInt1, byte[] paramArrayOfByte, short paramShort, short[] paramArrayOfShort, int paramInt2) {
        return ImageCompressionManager.DecompressSequenceFrame(paramInt1, paramArrayOfByte, paramShort, paramArrayOfShort, paramInt2);
    }

    private static short DecompressSequenceFrame(int paramInt1, int[] paramArrayOfInt, short paramShort, short[] paramArrayOfShort, int paramInt2) {
        return ImageCompressionManager.DecompressSequenceFrame(paramInt1, paramArrayOfInt, paramShort, paramArrayOfShort, paramInt2);
    }

    private static short DecompressSequenceFrameWhen(int paramInt1, int paramInt2, int paramInt3, short paramShort, short[] paramArrayOfShort, int paramInt4, byte[] paramArrayOfByte) {
        return ImageCompressionManager.DecompressSequenceFrameWhen(paramInt1, paramInt2, paramInt3, paramShort, paramArrayOfShort, paramInt4, paramArrayOfByte);
    }

    private static short DecompressSequenceFrameWhen(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3, byte[] paramArrayOfByte2) {
        return ImageCompressionManager.DecompressSequenceFrameWhen(paramInt1, paramArrayOfByte1, paramInt2, paramShort, paramArrayOfShort, paramInt3, paramArrayOfByte2);
    }

    private static short DecompressSequenceFrameWhen(int paramInt1, int[] paramArrayOfInt, int paramInt2, short paramShort, short[] paramArrayOfShort, int paramInt3, byte[] paramArrayOfByte) {
        return ImageCompressionManager.DecompressSequenceFrameWhen(paramInt1, paramArrayOfInt, paramInt2, paramShort, paramArrayOfShort, paramInt3, paramArrayOfByte);
    }

    private static short SetDSequenceTransferMode(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SetDSequenceTransferMode(paramInt, paramShort, paramArrayOfByte);
    }

    private static short SetDSequenceSrcRect(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SetDSequenceSrcRect(paramInt, paramArrayOfByte);
    }

    private static short SetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SetDSequenceMatrix(paramInt, paramArrayOfByte);
    }

    private static short SetDSequenceMask(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SetDSequenceMask(paramInt1, paramInt2);
    }

    private static short SetDSequenceMatte(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SetDSequenceMatte(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short SetDSequenceAccuracy(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SetDSequenceAccuracy(paramInt1, paramInt2);
    }

    private static short GetDSequenceMatrix(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.GetDSequenceMatrix(paramInt, paramArrayOfByte);
    }

    private static short SetDSequenceFlags(int paramInt1, int paramInt2, int paramInt3) {
        return ImageCompressionManager.SetDSequenceFlags(paramInt1, paramInt2, paramInt3);
    }

    private static short PtInDSequenceData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte) {
        return ImageCompressionManager.PtInDSequenceData(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
    }

    private static short PtInDSequenceData(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, int paramInt3, byte[] paramArrayOfByte2) {
        return ImageCompressionManager.PtInDSequenceData(paramInt1, paramArrayOfByte1, paramInt2, paramInt3, paramArrayOfByte2);
    }

    private static short PtInDSequenceData(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3, byte[] paramArrayOfByte) {
        return ImageCompressionManager.PtInDSequenceData(paramInt1, paramArrayOfInt, paramInt2, paramInt3, paramArrayOfByte);
    }

    private static short CDSequenceFlush(int paramInt) {
        return ImageCompressionManager.CDSequenceFlush(paramInt);
    }

    private static short CDSequenceInvalidate(int paramInt1, int paramInt2) {
        return ImageCompressionManager.CDSequenceInvalidate(paramInt1, paramInt2);
    }

    private static short ICMShieldSequenceCursor(int paramInt) {
        return ImageCompressionManager.ICMShieldSequenceCursor(paramInt);
    }

    private static short HitTestDSequenceData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5) {
        return ImageCompressionManager.HitTestDSequenceData(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, paramInt5);
    }

    private static short HitTestDSequenceData(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int paramInt4) {
        return ImageCompressionManager.HitTestDSequenceData(paramInt1, paramArrayOfInt1, paramInt2, paramInt3, paramArrayOfInt2, paramInt4);
    }

    private static short HitTestDSequenceData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4) {
        return ImageCompressionManager.HitTestDSequenceData(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramArrayOfInt, paramInt4);
    }

    private static short SetDSequenceTimeCode(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return ImageCompressionManager.SetDSequenceTimeCode(paramInt, paramArrayOfByte1, paramArrayOfByte2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }
}
