package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;

/**
 * @deprecated
 * This interface is used so that objects can have their images set to the specified
 * image in the setImageData call by other objects that provide image data or
 * services to iterate over a collection of images.
 */
public interface ImageSettable {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method should make the object's current image the specified image.
     * @param imageData the new image data that the object should make its current image.
     * @param desc this is the description that describes the image data
     * @throws quicktime.QTException
     */
    void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException;
}
