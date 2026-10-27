package quicktime.app.image;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Sequencer;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;

/**
 * @deprecated
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
     * @deprecated
     * @since QTJava 6.1
     * Construct an ImageSequencer that will iterate over an ImageDataSequence.
     * @param images the images
     */
    public ImageSequencer(ImageDataSequence paramImageDataSequence) {
        setImageData(paramImageDataSequence);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current image data that is being used by the ImageSequencer
     * @return the image data
     */
    public ImageDataSequence getImageData() {
        return this.mImages;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the current Image Data that the ImageSequencer is using.
     * You will NOT see a change in the ImageSequencer's appearance
     * until your next draw call
     * @param images the new image data
     */
    public void setImageData(ImageDataSequence paramImageDataSequence) {
        this.mImages = paramImageDataSequence;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current image description
     * @return the ImageDescription of an Image
     * @throws quicktime.QTException
     */
    @Override
    public ImageDescription getDescription() throws QTException {
        return this.mImages.getDescription();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current image data
     * @return the image data
     */
    @Override
    public EncodedImage getImage() {
        return this.mImages.getImage(getCurrentFrame());
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return the number of frames
     * @return the current number of frames of the sequencers data sequence
     */
    @Override
    public int size() {
        return this.mImages.size();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns a string representation of this class.
     */
    public String toString() {
        return super.toString() + "[imageData=" + this.mImages + "]";
    }
}
