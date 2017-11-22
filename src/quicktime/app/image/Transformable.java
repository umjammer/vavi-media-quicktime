
package quicktime.app.image;

import quicktime.QTException;
import quicktime.qd.QDDimension;
import quicktime.std.image.Matrix;


/**
 * @deprecated
 */
public abstract interface Transformable {
    /**
     * @deprecated
     */
    public abstract QDDimension getOriginalSize() throws QTException;

    /**
     * @deprecated
     */
    public abstract Matrix getMatrix() throws QTException;

    /**
     * @deprecated
     */
    public abstract void setMatrix(Matrix paramMatrix) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Transformable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
