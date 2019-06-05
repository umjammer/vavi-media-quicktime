
package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.ImageDescription;
import quicktime.util.EncodedImage;


/**
 * @deprecated
 */
public abstract interface ImageSpec {
    /**
     * @deprecated
     */
    public abstract ImageDescription getDescription() throws QTException;

    /**
     * @deprecated
     */
    public abstract EncodedImage getImage() throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageSpec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
