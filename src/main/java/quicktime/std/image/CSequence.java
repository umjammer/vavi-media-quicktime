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

    private static int begin(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, CodecComponent paramCodecComponent, int paramInt3, int paramInt4, int paramInt5, ColorTable paramColorTable, int paramInt6) throws QTException {
        int i = 0;
        int[] arrayOfInt = { 0 };
        if ((paramQDGraphics == null) || (paramQDRect == null))
            throw new QTException(-50);
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(paramQDGraphics), 0);
            csImageDescription = new ImageDescription(0);
            i = CompressSequenceBegin(arrayOfInt, QTObject.ID(paramQDGraphics.getPixMap()), 0, paramQDRect.getRect(), null, (short) paramInt1, paramInt2, QTObject.ID(paramCodecComponent), paramInt3, paramInt4, paramInt5, QTObject.ID(paramColorTable), (short) paramInt6, QTObject.ID(csImageDescription));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return arrayOfInt[0];
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
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];
        int i = 0;
        if ((src == null) || (srcRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = CompressSequenceFrame(_ID(), QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) flags, data.getInts(), arrayOfInt, arrayOfByte, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return new CompressedFrameInfo(arrayOfInt[0], arrayOfByte[0]);
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
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];
        int i = 0;
        if ((src == null) || (srcRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = CompressSequenceFrame(_ID(), QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) flags, data.getBytes(), arrayOfInt, arrayOfByte, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return new CompressedFrameInfo(arrayOfInt[0], arrayOfByte[0]);
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
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];
        int i = 0;
        if ((src == null) || (srcRect == null)) {
            throw new StdQTException(-50);
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(src), 0);
            i = CompressSequenceFrame(_ID(), QTObject.ID(src.getPixMap()), srcRect.getRect(), (short) flags, QTObject.ID(data), arrayOfInt, arrayOfByte, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        return new CompressedFrameInfo(arrayOfInt[0], arrayOfByte[0]);
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
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequenceKeyFrameRate(_ID(), arrayOfInt));
        return arrayOfInt[0];
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
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequenceFrameNumber(_ID(), arrayOfInt));
        return arrayOfInt[0];
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
        DataRateParams localDataRateParams = new DataRateParams();
        StdQTException.checkError(GetCSequenceDataRateParams(_ID(), localDataRateParams.getBytes()));
        return localDataRateParams;
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

    private void setFlushProc(Object paramObject, int paramInt) {
    }

    private static short CompressSequenceBegin(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, short paramShort1, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, short paramShort2, int paramInt9) {
        return ImageCompressionManager.CompressSequenceBegin(paramArrayOfInt, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramShort1, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramShort2, paramInt9);
    }

    private static short CompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, short paramShort, byte[] paramArrayOfByte2, int[] paramArrayOfInt, byte[] paramArrayOfByte3, int paramInt3) {
        return ImageCompressionManager.CompressSequenceFrame(paramInt1, paramInt2, paramArrayOfByte1, paramShort, paramArrayOfByte2, paramArrayOfInt, paramArrayOfByte3, paramInt3);
    }

    private static short CompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, short paramShort, int paramInt3, int[] paramArrayOfInt, byte[] paramArrayOfByte2, int paramInt4) {
        return ImageCompressionManager.CompressSequenceFrame(paramInt1, paramInt2, paramArrayOfByte1, paramShort, paramInt3, paramArrayOfInt, paramArrayOfByte2, paramInt4);
    }

    private static short CompressSequenceFrame(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, short paramShort, int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte2, int paramInt3) {
        return ImageCompressionManager.CompressSequenceFrame(paramInt1, paramInt2, paramArrayOfByte1, paramShort, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfByte2, paramInt3);
    }

    private static short GetCSequenceMaxCompressionSize(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCSequenceMaxCompressionSize(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static short SetCSequenceQuality(int paramInt1, int paramInt2, int paramInt3) {
        return ImageCompressionManager.SetCSequenceQuality(paramInt1, paramInt2, paramInt3);
    }

    private static short SetCSequenceKeyFrameRate(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SetCSequenceKeyFrameRate(paramInt1, paramInt2);
    }

    private static short GetCSequenceKeyFrameRate(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCSequenceKeyFrameRate(paramInt, paramArrayOfInt);
    }

    private static short SetCSequenceFrameNumber(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SetCSequenceFrameNumber(paramInt1, paramInt2);
    }

    private static short GetCSequenceFrameNumber(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCSequenceFrameNumber(paramInt, paramArrayOfInt);
    }

    private static short SetCSequencePrev(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SetCSequencePrev(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static short SetCSequenceDataRateParams(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.SetCSequenceDataRateParams(paramInt, paramArrayOfByte);
    }

    private static short GetCSequenceDataRateParams(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.GetCSequenceDataRateParams(paramInt, paramArrayOfByte);
    }

    private static short SetCSequencePreferredPacketSize(int paramInt1, int paramInt2) {
        return ImageCompressionManager.SetCSequencePreferredPacketSize(paramInt1, paramInt2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }
}
