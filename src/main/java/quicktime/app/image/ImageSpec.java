package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;

/**
 * Deprecated.
 * This interface is designed to work with quicktime.app.image classes.
 * Every class that implements this interface holds a common description
 * to one or more image data frames.
 */
public abstract interface ImageSpec {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @return {@link quicktime.QTException}
     */
    public abstract ImageDescription getDescription() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data.
     * An Exception is throw if there was some problem retrieving the data.
     * @return the image data
     * @return {@link quicktime.QTException}
     */
    public abstract EncodedImage getImage() throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
