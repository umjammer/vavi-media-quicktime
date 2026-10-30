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
 * @see quicktime.std.image.CDSequence
 * @see quicktime.std.image.CSequence
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
        int[] seqID = new int[1];
        int i = 0;
        if (port == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(QTObject.ID(port), 0);
            i = DecompressSequenceBegin(seqID, QTObject.ID(desc), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (QTSession.isCurrentOS(4)) {
            StdQTException.checkError(SetDSequenceFlags(seqID[0], codecDSeqFlushInstdOfDirtying, codecDSeqFlushInstdOfDirtying));
        }
        return seqID[0];
    }

    private static int beginS(ImageDescription desc, EncodedImage data, QDGraphics port, QDRect srcRect, Matrix matrix, Region flags, int mask, int accuracy, CodecComponent codec) throws StdQTException {
        int[] seqID = { 0 };
        int i = -50;
        if (port == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(port), 0);
            if ((data instanceof ByteEncodedImage)) {
                i = DecompressSequenceBeginS(seqID, QTObject.ID(desc), ((ByteEncodedImage) data).getBytes(), data.getSize(), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            } else if ((data instanceof IntEncodedImage)) {
                i = DecompressSequenceBeginS(seqID, QTObject.ID(desc), ((IntEncodedImage) data).getInts(), data.getSize(), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            } else if ((data instanceof RawEncodedImage)) {
                i = DecompressSequenceBeginS(seqID, QTObject.ID(desc), QTObject.ID((RawEncodedImage) data), data.getSize(), QTObject.ID(port), 0, srcRect == null ? null : srcRect.getRect(), matrix == null ? null : matrix.getBytes(), (short) 0, QTObject.ID(flags), (short) mask, accuracy, QTObject.ID(codec));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (QTSession.isCurrentOS(4))
            StdQTException.checkError(SetDSequenceFlags(seqID[0], codecDSeqFlushInstdOfDirtying, codecDSeqFlushInstdOfDirtying));
        return seqID[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrameS(_ID(), data.getBytes(), data.getSize(), (short) inFlags, outFlags, 0);
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrameS(_ID(), data.getInts(), data.getSize(), (short) inFlags, outFlags, 0);
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrameS(_ID(), QTObject.ID(data), data.getSize(), (short) inFlags, outFlags, 0);
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrame(_ID(), data.getBytes(), (short) inFlags, outFlags, 0);
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrame(_ID(), data.getInts(), (short) inFlags, outFlags, 0);
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrame(_ID(), QTObject.ID(data), (short) inFlags, outFlags, 0);
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(), data.getBytes(), data.getSize(), (short) inFlags, outFlags, 0, frameTime.getBytes());
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(), data.getInts(), data.getSize(), (short) inFlags, outFlags, 0, frameTime.getBytes());
        StdQTException.checkError(i);
        return outFlags[0];
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
        short[] outFlags = new short[1];
        int i = DecompressSequenceFrameWhen(_ID(), QTObject.ID(data), data.getSize(), (short) inFlags, outFlags, 0, frameTime.getBytes());
        StdQTException.checkError(i);
        return outFlags[0];
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
        Matrix matrix = new Matrix();
        StdQTException.checkError(GetDSequenceMatrix(_ID(), matrix.getBytes()));
        return matrix;
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
        byte[] hit = new byte[1];
        int i = -50;
        if ((data instanceof ByteEncodedImage)) {
            i = PtInDSequenceData(_ID(), ((ByteEncodedImage) data).getBytes(), data.getSize(), where.getPoint(), hit);
        } else if ((data instanceof IntEncodedImage)) {
            i = PtInDSequenceData(_ID(), ((IntEncodedImage) data).getInts(), data.getSize(), where.getPoint(), hit);
        } else if ((data instanceof RawEncodedImage)) {
            i = PtInDSequenceData(_ID(), QTObject.ID((RawEncodedImage) data), data.getSize(), where.getPoint(), hit);
        }
        StdQTException.checkError(i);
        return hit[0] != 0;
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
        int[] hit = { 0 };
        int i = 0;
        if ((data instanceof ByteEncodedImage)) {
            i = HitTestDSequenceData(_ID(), ((ByteEncodedImage) data).getBytes(), data.getSize(), where.getPoint(), hit, hitFlags);
        } else if ((data instanceof IntEncodedImage)) {
            i = HitTestDSequenceData(_ID(), ((IntEncodedImage) data).getInts(), data.getSize(), where.getPoint(), hit, hitFlags);
        } else if ((data instanceof RawEncodedImage)) {
            i = HitTestDSequenceData(_ID(), QTObject.ID((RawEncodedImage) data), data.getSize(), where.getPoint(), hit, hitFlags);
        }
        StdQTException.checkError(i);
        return hit[0] != 0;
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
    public void setTimeCode(TimeCodeDef timeCodeDef, TimeCodeTime timeCodeTime) throws StdQTException {
        StdQTException.checkError(SetDSequenceTimeCode(_ID(), timeCodeDef.getBytes(), timeCodeTime.getBytes()));
    }

    private void setDataProc(Object proc, int refCon) {
    }

    private static short DecompressSequenceBegin(int[] seqID, int desc, int port, int gdh, byte[] srcRect, byte[] matrix, short mode, int mask, short flags, int accuracy, int codec) {
        return ImageCompressionManager.DecompressSequenceBegin(seqID, desc, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    private static short DecompressSequenceBeginS(int[] seqID, int desc, int data, int dataSize, int port, int gdh, byte[] srcRect, byte[] matrix, short mode, int mask, short flags, int accuracy, int codec) {
        return ImageCompressionManager.DecompressSequenceBeginS(seqID, desc, data, dataSize, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    private static short DecompressSequenceBeginS(int[] seqID, int desc, byte[] data, int dataSize, int port, int gdh, byte[] srcRect, byte[] matrix, short mode, int mask, short flags, int accuracy, int codec) {
        return ImageCompressionManager.DecompressSequenceBeginS(seqID, desc, data, dataSize, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    private static short DecompressSequenceBeginS(int[] seqID, int desc, int[] data, int dataSize, int port, int gdh, byte[] srcRect, byte[] matrix, short mode, int mask, short flags, int accuracy, int codec) {
        return ImageCompressionManager.DecompressSequenceBeginS(seqID, desc, data, dataSize, port, gdh, srcRect, matrix, mode, mask, flags, accuracy, codec);
    }

    private static short DecompressSequenceFrameS(int seqID, int data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion) {
        return ImageCompressionManager.DecompressSequenceFrameS(seqID, data, dataSize, inFlags, outFlags, asyncCompletion);
    }

    private static short DecompressSequenceFrameS(int seqID, byte[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion) {
        return ImageCompressionManager.DecompressSequenceFrameS(seqID, data, dataSize, inFlags, outFlags, asyncCompletion);
    }

    private static short DecompressSequenceFrameS(int seqID, int[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion) {
        return ImageCompressionManager.DecompressSequenceFrameS(seqID, data, dataSize, inFlags, outFlags, asyncCompletion);
    }

    private static short DecompressSequenceFrame(int seqID, int data, short inFlags, short[] outFlags, int asyncCompletion) {
        return ImageCompressionManager.DecompressSequenceFrame(seqID, data, inFlags, outFlags, asyncCompletion);
    }

    private static short DecompressSequenceFrame(int seqID, byte[] data, short inFlags, short[] outFlags, int asyncCompletion) {
        return ImageCompressionManager.DecompressSequenceFrame(seqID, data, inFlags, outFlags, asyncCompletion);
    }

    private static short DecompressSequenceFrame(int seqID, int[] data, short inFlags, short[] outFlags, int asyncCompletion) {
        return ImageCompressionManager.DecompressSequenceFrame(seqID, data, inFlags, outFlags, asyncCompletion);
    }

    private static short DecompressSequenceFrameWhen(int seqID, int data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion, byte[] frameTime) {
        return ImageCompressionManager.DecompressSequenceFrameWhen(seqID, data, dataSize, inFlags, outFlags, asyncCompletion, frameTime);
    }

    private static short DecompressSequenceFrameWhen(int seqID, byte[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion, byte[] frameTime) {
        return ImageCompressionManager.DecompressSequenceFrameWhen(seqID, data, dataSize, inFlags, outFlags, asyncCompletion, frameTime);
    }

    private static short DecompressSequenceFrameWhen(int seqID, int[] data, int dataSize, short inFlags, short[] outFlags, int asyncCompletion, byte[] frameTime) {
        return ImageCompressionManager.DecompressSequenceFrameWhen(seqID, data, dataSize, inFlags, outFlags, asyncCompletion, frameTime);
    }

    private static short SetDSequenceTransferMode(int seqID, short mode, byte[] opColor) {
        return ImageCompressionManager.SetDSequenceTransferMode(seqID, mode, opColor);
    }

    private static short SetDSequenceSrcRect(int seqID, byte[] srcRect) {
        return ImageCompressionManager.SetDSequenceSrcRect(seqID, srcRect);
    }

    private static short SetDSequenceMatrix(int seqID, byte[] matrix) {
        return ImageCompressionManager.SetDSequenceMatrix(seqID, matrix);
    }

    private static short SetDSequenceMask(int seqID, int mask) {
        return ImageCompressionManager.SetDSequenceMask(seqID, mask);
    }

    private static short SetDSequenceMatte(int seqID, int matte, byte[] matteRect) {
        return ImageCompressionManager.SetDSequenceMatte(seqID, matte, matteRect);
    }

    private static short SetDSequenceAccuracy(int seqID, int accuracy) {
        return ImageCompressionManager.SetDSequenceAccuracy(seqID, accuracy);
    }

    private static short GetDSequenceMatrix(int seqID, byte[] matrix) {
        return ImageCompressionManager.GetDSequenceMatrix(seqID, matrix);
    }

    private static short SetDSequenceFlags(int seqID, int flags, int flagsMask) {
        return ImageCompressionManager.SetDSequenceFlags(seqID, flags, flagsMask);
    }

    private static short PtInDSequenceData(int seqID, int data, int dataSize, int where, byte[] hit) {
        return ImageCompressionManager.PtInDSequenceData(seqID, data, dataSize, where, hit);
    }

    private static short PtInDSequenceData(int seqID, byte[] data, int dataSize, int where, byte[] hit) {
        return ImageCompressionManager.PtInDSequenceData(seqID, data, dataSize, where, hit);
    }

    private static short PtInDSequenceData(int seqID, int[] data, int dataSize, int where, byte[] hit) {
        return ImageCompressionManager.PtInDSequenceData(seqID, data, dataSize, where, hit);
    }

    private static short CDSequenceFlush(int seqID) {
        return ImageCompressionManager.CDSequenceFlush(seqID);
    }

    private static short CDSequenceInvalidate(int seqID, int invalRgn) {
        return ImageCompressionManager.CDSequenceInvalidate(seqID, invalRgn);
    }

    private static short ICMShieldSequenceCursor(int seqID) {
        return ImageCompressionManager.ICMShieldSequenceCursor(seqID);
    }

    private static short HitTestDSequenceData(int seqID, int data, int dataSize, int where, int[] hit, int hitFlags) {
        return ImageCompressionManager.HitTestDSequenceData(seqID, data, dataSize, where, hit, hitFlags);
    }

    private static short HitTestDSequenceData(int seqID, int[] data, int dataSize, int where, int[] hit, int hitFlags) {
        return ImageCompressionManager.HitTestDSequenceData(seqID, data, dataSize, where, hit, hitFlags);
    }

    private static short HitTestDSequenceData(int seqID, byte[] data, int dataSize, int where, int[] hit, int hitFlags) {
        return ImageCompressionManager.HitTestDSequenceData(seqID, data, dataSize, where, hit, hitFlags);
    }

    private static short SetDSequenceTimeCode(int seqID, byte[] timeCodeFormat, byte[] timeCodeTime) {
        return ImageCompressionManager.SetDSequenceTimeCode(seqID, timeCodeFormat, timeCodeTime);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }
}
