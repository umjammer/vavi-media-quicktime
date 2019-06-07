package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;

/**
 * Deprecated.
 * This interface is used so that objects can have their images set to the specified
 * image in the setImageData call by other objects that provide image data or
 * services to iterate over a collection of images.
 */
public abstract interface ImageSettable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should make the object's current image the specified image.
     * @param imageData the new image data that the object should make its current image.
     * @param desc this is the description that describes the image data
     * @param {@link quicktime.QTException}
     */
    public abstract void setImageData(EncodedImage paramEncodedImage, ImageDescription paramImageDescription) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageSettable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
