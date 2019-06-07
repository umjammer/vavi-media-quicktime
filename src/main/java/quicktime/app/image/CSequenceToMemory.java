package quicktime.app.image;

import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.ColorTable;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDRect;
import quicktime.std.image.CSequence;
import quicktime.std.image.CodecComponent;
import quicktime.std.image.CompressedFrameInfo;
import quicktime.std.image.ImageDescription;
import quicktime.util.RawEncodedImage;

/**
 * Deprecated.
 * This class provides a mechanism for compressing a sequence of images to memory.
 * The compressed images are maintained internally. Use the DSequenceFromMemory
 * class to decompress a sequence of images from a CSequenceToMemory object.
 *
 * When compressing a sequence of images you want to keep in memory you would use
 * this class and use the compressFrame call that belongs here. This class will then
 * handle the memory that the compressed image is loaded into. If you call one of the
 * super compressFrame calls then this class does NOT know about the compressed data.
 * @see {@link quicktime.app.image.DSequenceFromMemory}
 */
public class CSequenceToMemory {

    private Vector frames;

    private CSequence seq;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create an instance of this class in order to compress a sequence of frames
     * and store them in memory. You must call begin before you use any other methods
     * @param numFrames the number of Frames that the compression will hold
     * quicktime.std.image.CSequence.begin
     */
    public CSequenceToMemory(int paramInt) {
        this.frames = new Vector(paramInt);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create an instance of this class in order to compress a sequence of frames
     * and store them in memory.
     * @param src a QDGraphics containing the PixMap object containing the source data to be compressed
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param depth the depth of the color or 0 for the current device's depth
     * @param cType the compressor type
     * @param codec the compressor identifier
     * @param spatialQuality the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @param keyFrameRate the maximum number of frames allowed between key frames
     * @param cTab the ColorTable or null that is used in the compression
     * @param flags flags providing further control information
     */
    public CSequenceToMemory(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, CodecComponent paramCodecComponent, int paramInt3, int paramInt4, int paramInt5, ColorTable paramColorTable, int paramInt6) throws QTException {
        this.seq = new CSequence(paramQDGraphics, paramQDRect, paramInt1, paramInt2, paramCodecComponent, paramInt3, paramInt4, paramInt5, paramColorTable, paramInt6);
        this.frames = new Vector(16);
    }

    /**
     * @deprecated
     */
    Vector getFrames() {
        return this.frames;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Signals the beginning of the process of compressing a sequence of frames.
     * @param src a QDGraphics containing the PixMap object containing the source data to be compressed
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param colorDepth the depth at which the sequence is likely to be viewed
     * @param cType the compressor type
     * @param codec the compressor identifier
     * @param spatialQuality the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @param keyFrameRate the maximum number of frames allowed between key frames
     * @param clut a custum color lookup table
     * @param flags flags providing further control information
     * @param {@link quicktime.QTException}
     */
    public void begin(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt1, int paramInt2, CodecComponent paramCodecComponent, int paramInt3, int paramInt4, int paramInt5, ColorTable paramColorTable, int paramInt6) throws QTException {
        if (this.frames != null) {
            this.frames.removeAllElements();
        }
        this.seq = new CSequence(paramQDGraphics, paramQDRect, paramInt1, paramInt2, paramCodecComponent, paramInt3, paramInt4, paramInt5, paramColorTable, paramInt6);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Your application calls this method to compress one of a sequence of frames.
     * The similarity value is directly returned by this method.
     *
     * Note: See the QTImage class and Inside Mac QuickTime documentation for
     * more information on valid flag values.
     *
     * @param src a QDGraphics containing the PixMap that contains the image to be compressed
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param flags flags providing further control information
     * @return the similarity value
     * @return {@link quicktime.QTException}
     */
    public int compressFrame(QDGraphics paramQDGraphics, QDRect paramQDRect, int paramInt) throws QTException {
        RawEncodedImage localRawEncodedImage = new RawEncodedImage(this.seq.getMaxCompressionSize(paramQDGraphics), true);
        CompressedFrameInfo localCompressedFrameInfo = this.seq.compressFrame(paramQDGraphics, paramQDRect, paramInt, localRawEncodedImage);
        localRawEncodedImage.setSize(localCompressedFrameInfo.getDataSize());
        try {
            int i = this.seq.getFrameNumber() - 1;
            if (i < this.frames.size()) {
                this.frames.setElementAt(localRawEncodedImage, i);
            } else if (i > this.frames.size()) {
                this.frames.setSize(i);
                this.frames.setElementAt(localRawEncodedImage, i);
            } else {
                this.frames.addElement(localRawEncodedImage);
            }
        } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
            throw new QTException(getClass().getName() + ": Unable to grow internal Vector object.");
        }
        return localCompressedFrameInfo.getSimilarity();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getDescription() throws QTException {
        return this.seq.getDescription();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the CSequence object - this may return null if you haven't called
     * created the sequence.
     * @return the sequence
     */
    public CSequence getSequence() {
        return this.seq;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /CSequenceToMemory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
