package quicktime.app.image;

import quicktime.QTException;
import quicktime.qd.QDDimension;
import quicktime.std.image.Matrix;

/**
 * @deprecated
 * This interface is used so that objects that can have their visual appearance transformed
 * through matrix manipulation can return their current matrix and set a new transformed
 * matrix.
 */
public interface Transformable {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @throws quicktime.QTException
     */
    QDDimension getOriginalSize() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the current matrix of the Transformable object.
     */
    Matrix getMatrix() throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    void setMatrix(Matrix matrix) throws QTException;
}
