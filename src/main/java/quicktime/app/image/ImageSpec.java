package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;

/**
 * @deprecated
 * This interface is designed to work with quicktime.app.image classes.
 * Every class that implements this interface holds a common description
 * to one or more image data frames.
 */
public interface ImageSpec {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @throws quicktime.QTException
     */
    ImageDescription getDescription() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the actual image data.
     * An Exception is throw if there was some problem retrieving the data.
     * @return the image data
     * @throws quicktime.QTException
     */
    EncodedImage getImage() throws QTException;
}
