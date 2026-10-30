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
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;


/**
 * This class provides a mechanism for compressing a sequence of images to memory.
 * The compressed images are maintained internally. Use the DSequenceFromMemory
 * class to decompress a sequence of images from a CSequenceToMemory object.
 * <p>
 * When compressing a sequence of images you want to keep in memory you would use
 * this class and use the compressFrame call that belongs here. This class will then
 * handle the memory that the compressed image is loaded into. If you call one of the
 * super compressFrame calls then this class does NOT know about the compressed data.
 *
 * @see quicktime.app.image.DSequenceFromMemory
 * @deprecated
 */
public class CSequenceToMemory {

    private final Vector<EncodedImage> frames;

    private CSequence seq;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Create an instance of this class in order to compress a sequence of frames
     * and store them in memory. You must call begin before you use any other methods
     *
     * @param numFrames the number of Frames that the compression will hold
     *                  quicktime.std.image.CSequence.begin
     * @since QTJava 6.1
     * @deprecated
     */
    public CSequenceToMemory(int numFrames) {
        this.frames = new Vector<>(numFrames);
    }

    /**
     * Create an instance of this class in order to compress a sequence of frames
     * and store them in memory.
     *
     * @param src             a QDGraphics containing the PixMap object containing the source data to be compressed
     * @param srcRect         a QDRect defining the portion of the image to compress
     * @param depth           the depth of the color or 0 for the current device's depth
     * @param cType           the compressor type
     * @param codec           the compressor identifier
     * @param spatialQuality  the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @param keyFrameRate    the maximum number of frames allowed between key frames
     * @param cTab            the ColorTable or null that is used in the compression
     * @param flags           flags providing further control information
     * @since QTJava 6.1
     * @deprecated
     */
    public CSequenceToMemory(QDGraphics src, QDRect srcRect, int depth, int cType, CodecComponent codec, int spatialQuality, int temporalQuality, int keyFrameRate, ColorTable cTab, int flags) throws QTException {
        this.seq = new CSequence(src, srcRect, depth, cType, codec, spatialQuality, temporalQuality, keyFrameRate, cTab, flags);
        this.frames = new Vector<>(16);
    }

    /**
     * @deprecated
     */
    Vector<EncodedImage> getFrames() {
        return this.frames;
    }

    /**
     * Signals the beginning of the process of compressing a sequence of frames.
     *
     * @param src             a QDGraphics containing the PixMap object containing the source data to be compressed
     * @param srcRect         a QDRect defining the portion of the image to compress
     * @param colorDepth      the depth at which the sequence is likely to be viewed
     * @param cType           the compressor type
     * @param codec           the compressor identifier
     * @param spatialQuality  the desired compressed image quality
     * @param temporalQuality the desired sequence temporal quality
     * @param keyFrameRate    the maximum number of frames allowed between key frames
     * @param clut            a custum color lookup table
     * @param flags           flags providing further control information
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public void begin(QDGraphics src, QDRect srcRect, int colorDepth, int cType, CodecComponent codec, int spatialQuality, int temporalQuality, int keyFrameRate, ColorTable clut, int flags) throws QTException {
        if (this.frames != null) {
            this.frames.removeAllElements();
        }
        this.seq = new CSequence(src, srcRect, colorDepth, cType, codec, spatialQuality, temporalQuality, keyFrameRate, clut, flags);
    }

    /**
     * Your application calls this method to compress one of a sequence of frames.
     * The similarity value is directly returned by this method.
     * <p>
     * Note: See the QTImage class and Inside Mac QuickTime documentation for
     * more information on valid flag values.
     *
     * @param src     a QDGraphics containing the PixMap that contains the image to be compressed
     * @param srcRect a QDRect defining the portion of the image to compress
     * @param flags   flags providing further control information
     * @return the similarity value
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public int compressFrame(QDGraphics src, QDRect srcRect, int flags) throws QTException {
        RawEncodedImage data = new RawEncodedImage(this.seq.getMaxCompressionSize(src), true);
        CompressedFrameInfo compressedFrameInfo = this.seq.compressFrame(src, srcRect, flags, data);
        data.setSize(compressedFrameInfo.getDataSize());
        try {
            int i = this.seq.getFrameNumber() - 1;
            if (i < this.frames.size()) {
                this.frames.setElementAt(data, i);
            } else if (i > this.frames.size()) {
                this.frames.setSize(i);
                this.frames.setElementAt(data, i);
            } else {
                this.frames.addElement(data);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new QTException(getClass().getName() + ": Unable to grow internal Vector object.");
        }
        return compressedFrameInfo.getSimilarity();
    }

    /**
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     *
     * @return the ImageDescription of an Image
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return this.seq.getDescription();
    }

    /**
     * Returns the CSequence object - this may return null if you haven't called
     * created the sequence.
     *
     * @return the sequence
     * @since QTJava 6.1
     * @deprecated
     */
    public CSequence getSequence() {
        return this.seq;
    }
}
