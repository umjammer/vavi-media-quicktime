package quicktime.std.image;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.QuickDrawManager;
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


/**
 * This class is used for compressing a sequence of images.
 * @see quicktime.std.image.CDSequence,
 * {@link quicktime.std.image.DSequence}
 */
public final class CSequence extends CDSequence implements QuickTimeLib {

    /**
     * Creating an instance of this class signals the beginning of the process of compressing
     * a sequence of frames. The ICM will choose the color depth and will allocate a previous
     * image buffer.
     * @see "QuickTime::CompressSequenceBegin"
     * @param src a QDGraphics object containing the PixMap which is the source data to be compressed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param cType the compressor type
     * @param codec the compressor identifier
     * @param spatialQuality the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @param keyFrameRate the maximum number of frames allowed between key frames
     * @param flags flags providing further control information
     */
    public CSequence(QDGraphics src, QDRect srcRect, int cType, CodecComponent codec, int spatialQuality, int temporalQuality, int keyFrameRate, int flags) throws QTException {
        this(src, srcRect, 0, cType, codec, spatialQuality, temporalQuality, keyFrameRate, null, flags);
    }

    /**
     * Creating an instance of this class signals the beginning of the process of compressing
     * a sequence of frames.
     * @see "QuickTime::CompressSequenceBegin"
     * @param src a QDGraphics object containing the PixMap which is the source data to be compressed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param colorDepth the depth at which the sequence is likely to be viewed
     * @param cType the compressor type
     * @param codec the compressor identifier
     * @param spatialQuality the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @param keyFrameRate the maximum number of frames allowed between key frames
     * @param clut a custum color lookup table
     * @param flags flags providing further control information
     */
    public CSequence(QDGraphics src, QDRect srcRect, int colorDepth, int cType, CodecComponent codec, int spatialQuality, int temporalQuality, int keyFrameRate, ColorTable clut, int flags) throws QTException {
        super(begin(src, srcRect, colorDepth, cType, codec, spatialQuality, temporalQuality, keyFrameRate, clut, flags), src);
        this.mImageDescription = csImageDescription;
        csImageDescription = null;
    }

    private static int begin(QDGraphics port, QDRect rect, int colorDepth, int cType, CodecComponent codec, int spatialQuality, int temporalQuality, int keyFrameRate, ColorTable ctable, int flags) throws QTException {
        int i = 0;
        int[] seqID = { 0 };
        if ((port == null) || (rect == null))
            throw new QTException(-50);
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(port), 0);
            csImageDescription = new ImageDescription(0);
            i = CompressSequenceBegin(seqID, QTObject.ID(port.getPixMap()), 0, rect.getRect(), null, (short) colorDepth, cType, QTObject.ID(codec), spatialQuality, temporalQuality, keyFrameRate, QTObject.ID(ctable), (short) flags, QTObject.ID(csImageDescription));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return seqID[0];
    }

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private static ImageDescription csImageDescription;

    /**
     * Your application calls this method to compress one of a sequence of frames.
     * The size of the compressed data and the similarity value are returned in
     * the CompressedFrameInfo object.
     * @see "QuickTime::CompressSequenceFrame"
     * @param src a QDGraphics that contains the PixMap object containing the source data to be compressed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param flags flags providing further control information
     * @param data an EncodedImage object to hold the compressed image data. It is
     *                 your program's responsibility to make sure that this object can
     *                 receive at least as much data as indicated by the
     *                 getMaxCompressionSize method.
     * @return the size of the compressed data and the similarity value
     * @throws quicktime.std.StdQTException
     * @throws quicktime.qd.QDException
     */
    public CompressedFrameInfo compressFrame(QDGraphics src, QDRect srcRect, int flags, EncodedImage data) throws StdQTException, QDException {
        if ((data instanceof RawEncodedImage))
            return compressFrame(src, srcRect, flags, (RawEncodedImage) data);
        if ((data instanceof IntEncodedImage))
            return compressFrame(src, srcRect, flags, (IntEncodedImage) data);
        if ((data instanceof ByteEncodedImage)) {
            return compressFrame(src, srcRect, flags, (ByteEncodedImage) data);
        }
        throw new StdQTException(-50);
    }

    /**
     * Your application calls this method to compress one of a sequence of frames.
     * The size of the compressed data and the similarity value are returned in
     * the CompressedFrameInfo object.
     * @see "QuickTime::CompressSequenceFrame"
     * @param src a QDGraphics that contains the PixMap object containing the source data to be compressed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param flags flags providing further control information
     * @param data an EncodedImage object to hold the compressed image data. It is
     *                 your program's responsibility to make sure that this object can
     *                 receive at least as much data as indicated by the
     *                 getMaxCompressionSize method.
     * @return the size of the compressed data and the similarity value
     * @throws quicktime.std.StdQTException
     */
    public CompressedFrameInfo compressFrame(QDGraphics src, QDRect srcRect, int flags, IntEncodedImage data) throws StdQTException {
        int[] dataSize = new int[1];
        byte[] similarity = new byte[1];
        int i = 0;
        if ((src == null) || (srcRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = CompressSequenceFrame(_ID(), QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) flags, data.getInts(), dataSize, similarity, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return new CompressedFrameInfo(dataSize[0], similarity[0]);
    }

    /**
     * Your application calls this method to compress one of a sequence of frames.
     * The size of the compressed data and the similarity value are returned in
     * the CompressedFrameInfo object.
     * @see "QuickTime::CompressSequenceFrame"
     * @param src a QDGraphics that contains the PixMap object containing the source data to be compressed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param flags flags providing further control information
     * @param data an EncodedImage object to hold the compressed image data. It is
     *                 your program's responsibility to make sure that this object can
     *                 receive at least as much data as indicated by the
     *                 getMaxCompressionSize method.
     * @return the size of the compressed data and the similarity value
     * @throws quicktime.std.StdQTException
     */
    public CompressedFrameInfo compressFrame(QDGraphics src, QDRect srcRect, int flags, ByteEncodedImage data) throws StdQTException {
        int[] dataSize = new int[1];
        byte[] similarity = new byte[1];
        int i = 0;
        if ((src == null) || (srcRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = CompressSequenceFrame(_ID(), QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) flags, data.getBytes(), dataSize, similarity, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return new CompressedFrameInfo(dataSize[0], similarity[0]);
    }

    /**
     * Your application calls this method to compress one of a sequence of frames.
     * The size of the compressed data and the similarity value are returned in
     * the CompressedFrameInfo object.
     * @see "QuickTime::CompressSequenceFrame"
     * @param src a QDGraphics that contains the PixMap object containing the source data to be compressed. This may NOT be null.
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param flags flags providing further control information
     * @param data an EncodedImage object to hold the compressed image data. It is
     *                 your program's responsibility to make sure that this object can
     *                 receive at least as much data as indicated by the
     *                 getMaxCompressionSize method.
     * @return the size of the compressed data and the similarity value
     * @throws quicktime.std.StdQTException
     */
    public CompressedFrameInfo compressFrame(QDGraphics src, QDRect srcRect, int flags, RawEncodedImage data) throws StdQTException {
        int[] dataSize = new int[1];
        byte[] similarity = new byte[1];
        int i = 0;
        if ((src == null) || (srcRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = CompressSequenceFrame(_ID(), QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) flags, QTObject.ID(data), dataSize, similarity, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return new CompressedFrameInfo(dataSize[0], similarity[0]);
    }

    /**
     * This method allows your application to determine the maximum size an image will
     * be after compression for a given compression sequence. The size returned is in the
     * number of bytes that the encoded image will require.
     * @see "QuickTime::GetCSequenceMaxCompressionSize"
     * @param src a QDGraphics that contains the PixMap which is the source image compression data. This may NOT be null.
     * @return the maximum size an image in this sequence will be after compression
     * @throws quicktime.std.StdQTException
     */
    public int getMaxCompressionSize(QDGraphics src) throws StdQTException {
        int[] size = new int[1];
        int i = 0;
        if (src == null)
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = GetCSequenceMaxCompressionSize(_ID(), QTObject.ID(src.getPixMap()), size);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return size[0];
    }

    /**
     * This method allows you to adjust the spatial or temporal quality for the
     * current sequence.
     * @see "QuickTime::SetCSequenceQuality"
     * @param spatialQuality the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @throws quicktime.std.StdQTException
     */
    public void setQuality(int spatialQuality, int temporalQuality) throws StdQTException {
        StdQTException.checkError(SetCSequenceQuality(_ID(), spatialQuality, temporalQuality));
    }

    /**
     * This method allows you to adjust the key frame rate for the current sequence.
     * @see "QuickTime::SetCSequenceKeyFrameRate"
     * @param keyFrameRate the maximum number of frames allowed between key frames
     * @throws quicktime.std.StdQTException
     */
    public void setKeyFrameRate(int keyFrameRate) throws StdQTException {
        StdQTException.checkError(SetCSequenceKeyFrameRate(_ID(), keyFrameRate));
    }

    /**
     * This method lets you determine the current key frame rate of a sequence.
     * @see "QuickTime::GetCSequenceKeyFrameRate"
     * @return the current key frame rate
     * @throws quicktime.std.StdQTException
     */
    public int getKeyFrameRate() throws StdQTException {
        int[] keyFrameRate = new int[1];
        StdQTException.checkError(GetCSequenceKeyFrameRate(_ID(), keyFrameRate));
        return keyFrameRate[0];
    }

    /**
     * This method informs the compressor in use for this sequence that frames are
     * being compressed out of order.
     * @see "QuickTime::SetCSequenceFrameNumber"
     * @param frameNumber the frame number of the frame that is being compressed
     *                     out of sequence
     * @throws quicktime.std.StdQTException
     */
    public void setFrameNumber(int frameNumber) throws StdQTException {
        StdQTException.checkError(SetCSequenceFrameNumber(_ID(), frameNumber));
    }

    /**
     * This method returns the current frame number of this sequence.
     * @see "QuickTime::GetCSequenceFrameNumber"
     * @return the current frame number
     * @throws quicktime.std.StdQTException
     */
    public int getFrameNumber() throws StdQTException {
        int[] frameNumber = new int[1];
        StdQTException.checkError(GetCSequenceFrameNumber(_ID(), frameNumber));
        return frameNumber[0];
    }

    /**
     * This method allows the application to set the pixel map and boundary rectangle
     * used by the previous frame in temporal compression. Note - this may change the maxCompressionSize
     * @see "QuickTime::SetCSequencePrev"
     * @param prev a QDGraphics that contains the PixMap defining the new previous image. This may NOT be null.
     * @param prevRect a QDRect defining the portion of the previous image to use for
     *                  temporal compression. May be to null
     * @throws quicktime.std.StdQTException
     */
    public void setPrev(QDGraphics prev, QDRect prevRect) throws StdQTException {
        int i = 0;
        if (prev == null) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(prev), 0);
            i = SetCSequencePrev(_ID(), QTObject.ID(prev.getPixMap()), prevRect != null ? prevRect.getRect() : null);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    /**
     * This method determines the location of the previous image buffer allocated
     * by the compressor.
     * @see "QuickTime::GetCSequencePrevBuffer"
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @throws quicktime.std.StdQTException
     */
    public QDGraphics prevBuffer() throws StdQTException {
        return QDGraphics.fromCSequence(this);
    }

    /**
     * This method allows the application to set data rate parameters, which communicate
     * information to compressors that can constrain compressed data in a particular
     * sequence to a specific data rate.
     * @see "QuickTime::SetCSequenceDataRateParams"
     * @param params a DataRateParams object
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.DataRateParams
     */
    public void setDataRateParams(DataRateParams params) throws StdQTException {
        StdQTException.checkError(SetCSequenceDataRateParams(_ID(), params.getBytes()));
    }

    /**
     * This method obtains the data rate parameters previously set with the
     * setCDataRateParams method.
     * @see "QuickTime::GetCSequenceDataRateParams"
     * @return a DataRateParams object, this object will not contain useful information
     *          unless the setCDataRateParams method was previously called
     * @throws quicktime.std.StdQTException
     */
    public DataRateParams getDataRateParams() throws StdQTException {
        DataRateParams dataRateParams = new DataRateParams();
        StdQTException.checkError(GetCSequenceDataRateParams(_ID(), dataRateParams.getBytes()));
        return dataRateParams;
    }

    /**
     * Sets the preferred packet size for a sequence. This was added in QuickTime 2.5
     * to support video conferencing applications.
     * @see "QuickTime::SetCSequencePreferredPacketSize"
     * @param preferredPacketSize the preferred packet size in bytes
     * @throws quicktime.std.StdQTException
     */
    public void setPreferredPacketSize(int preferredPacketSize) throws StdQTException {
        StdQTException.checkError(SetCSequencePreferredPacketSize(_ID(), preferredPacketSize));
    }

    private void setFlushProc(Object proc, int refCon) {
    }

    private static short CompressSequenceBegin(int[] seqID, int src, int prev, byte[] srcRect, byte[] prevRect, short colorDepth, int cType, int codec, int spatialQuality, int temporalQuality, int keyFrameRate, int ctable, short flags, int desc) {
        return ImageCompressionManager.CompressSequenceBegin(seqID, src, prev, srcRect, prevRect, colorDepth, cType, codec, spatialQuality, temporalQuality, keyFrameRate, ctable, flags, desc);
    }

    private static short CompressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, byte[] data, int[] dataSize, byte[] similarity, int asyncCompletion) {
        return ImageCompressionManager.CompressSequenceFrame(seqID, src, srcRect, flags, data, dataSize, similarity, asyncCompletion);
    }

    private static short CompressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, int data, int[] dataSize, byte[] similarity, int asyncCompletion) {
        return ImageCompressionManager.CompressSequenceFrame(seqID, src, srcRect, flags, data, dataSize, similarity, asyncCompletion);
    }

    private static short CompressSequenceFrame(int seqID, int src, byte[] srcRect, short flags, int[] data, int[] dataSize, byte[] similarity, int asyncCompletion) {
        return ImageCompressionManager.CompressSequenceFrame(seqID, src, srcRect, flags, data, dataSize, similarity, asyncCompletion);
    }

    private static short GetCSequenceMaxCompressionSize(int seqID, int src, int[] size) {
        return ImageCompressionManager.GetCSequenceMaxCompressionSize(seqID, src, size);
    }

    private static short SetCSequenceQuality(int seqID, int spatialQuality, int temporalQuality) {
        return ImageCompressionManager.SetCSequenceQuality(seqID, spatialQuality, temporalQuality);
    }

    private static short SetCSequenceKeyFrameRate(int seqID, int keyFrameRate) {
        return ImageCompressionManager.SetCSequenceKeyFrameRate(seqID, keyFrameRate);
    }

    private static short GetCSequenceKeyFrameRate(int seqID, int[] keyFrameRate) {
        return ImageCompressionManager.GetCSequenceKeyFrameRate(seqID, keyFrameRate);
    }

    private static short SetCSequenceFrameNumber(int seqID, int frameNumber) {
        return ImageCompressionManager.SetCSequenceFrameNumber(seqID, frameNumber);
    }

    private static short GetCSequenceFrameNumber(int seqID, int[] frameNumber) {
        return ImageCompressionManager.GetCSequenceFrameNumber(seqID, frameNumber);
    }

    private static short SetCSequencePrev(int seqID, int prev, byte[] prevRect) {
        return ImageCompressionManager.SetCSequencePrev(seqID, prev, prevRect);
    }

    private static short SetCSequenceDataRateParams(int seqID, byte[] params) {
        return ImageCompressionManager.SetCSequenceDataRateParams(seqID, params);
    }

    private static short GetCSequenceDataRateParams(int seqID, byte[] params) {
        return ImageCompressionManager.GetCSequenceDataRateParams(seqID, params);
    }

    private static short SetCSequencePreferredPacketSize(int seqID, int size) {
        return ImageCompressionManager.SetCSequencePreferredPacketSize(seqID, size);
    }

    private static void SetGWorld(int port, int gdh) {
        QuickDrawManager.SetGWorld(port, gdh);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }
}
