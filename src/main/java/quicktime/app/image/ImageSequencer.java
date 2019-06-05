
package quicktime.app.image;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Sequencer;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;


/**
 * @deprecated
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
     */
    public ImageSequencer(ImageDataSequence paramImageDataSequence) {
        setImageData(paramImageDataSequence);
    }

    /**
     * @deprecated
     */
    public ImageDataSequence getImageData() {
        return this.mImages;
    }

    /**
     * @deprecated
     */
    public void setImageData(ImageDataSequence paramImageDataSequence) {
        this.mImages = paramImageDataSequence;
    }

    /**
     * @deprecated
     */
    public ImageDescription getDescription() throws QTException {
        return this.mImages.getDescription();
    }

    /**
     * @deprecated
     */
    public EncodedImage getImage() {
        return this.mImages.getImage(getCurrentFrame());
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.mImages.size();
    }

    /**
     * @deprecated
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
