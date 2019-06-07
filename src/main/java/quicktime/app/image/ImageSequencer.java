package quicktime.app.image;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Sequencer;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;

/**
 * Deprecated.
 * This class handles the obtaining of a specific frame of data
 * and its accompanying description from an ImageDataSequence object
 */
public class ImageSequencer extends Sequencer implements ImageSpec {

    private ImageDataSequence mImages;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Construct an ImageSequencer that will iterate over an ImageDataSequence.
     * @param images the images
     */
    public ImageSequencer(ImageDataSequence paramImageDataSequence) {
        setImageData(paramImageDataSequence);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current image data that is being used by the ImageSequencer
     * @return the image data
     */
    public ImageDataSequence getImageData() {
        return this.mImages;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set the current Image Data that the ImageSequencer is using.
     * You will NOT see a change in the ImageSequencer's appearance
     * until your next draw call
     * @param images the new image data
     */
    public void setImageData(ImageDataSequence paramImageDataSequence) {
        this.mImages = paramImageDataSequence;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current image description
     * @return the ImageDescription of an Image
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getDescription() throws QTException {
        return this.mImages.getDescription();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current image data
     * @return the image data
     */
    public EncodedImage getImage() {
        return this.mImages.getImage(getCurrentFrame());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the number of frames
     * @return the current number of frames of the sequencers data sequence
     */
    public int size() {
        return this.mImages.size();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a string representation of this class.
     */
    public String toString() {
        return super.toString() + "[imageData=" + this.mImages + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageSequencer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
