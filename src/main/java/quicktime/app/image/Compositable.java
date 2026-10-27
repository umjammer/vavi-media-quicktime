package quicktime.app.image;

import quicktime.QTException;
import quicktime.std.image.GraphicsMode;


/**
 * A Compositable object is an object that is able to apply graphics mode
 * operations to the Image data that it is presenting.
 *
 * @deprecated
 */
public interface Compositable extends ImageSpec {

    /**
     * This method sets the graphics mode property of the Compositable object.
     *
     * @param gMode  Specifies the graphics mode.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    void setGraphicsMode(GraphicsMode gMode) throws QTException;

    /**
     * Returns the current graphics mode of the Compositable object.
     *
     * @return a Graphics mode object
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    GraphicsMode getGraphicsMode() throws QTException;
}
