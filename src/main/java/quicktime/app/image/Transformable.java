package quicktime.app.image;

import quicktime.QTException;
import quicktime.qd.QDDimension;
import quicktime.std.image.Matrix;

/**
 * Deprecated.
 * This interface is used so that objects that can have their visual appearance transformed
 * through matrix manipulation can return their current matrix and set a new transformed
 * matrix.
 */
public abstract interface Transformable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the original size
     * @return {@link quicktime.QTException}
     */
    public abstract QDDimension getOriginalSize() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the current matrix of the Transformable object.
     */
    public abstract Matrix getMatrix() throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    public abstract void setMatrix(Matrix paramMatrix) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Transformable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
