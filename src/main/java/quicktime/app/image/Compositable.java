package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.GraphicsMode;

/**
 * Deprecated.
 * A Compositable object is an object that is able to apply graphics mode
 * operations to the Image data that it is presenting.
 */
public abstract interface Compositable extends ImageSpec {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the graphics mode property of the Compositable object.
     * @param gMode Specifies the graphics mode.
     * @param {@link quicktime.QTException}
     */
    public abstract void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current graphics mode of the Compositable object.
     * @return a Graphics mode object
     * @return {@link quicktime.QTException}
     */
    public abstract GraphicsMode getGraphicsMode() throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Compositable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
