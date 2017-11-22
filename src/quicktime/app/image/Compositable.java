
package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.GraphicsMode;


/**
 * @deprecated
 */
public abstract interface Compositable extends ImageSpec {
    /**
     * @deprecated
     */
    public abstract void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException;

    /**
     * @deprecated
     */
    public abstract GraphicsMode getGraphicsMode() throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Compositable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
