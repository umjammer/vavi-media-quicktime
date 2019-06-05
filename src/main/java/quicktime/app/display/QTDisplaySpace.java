
package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.image.Transformable;
import quicktime.app.spaces.Space;


/**
 * @deprecated
 */
public abstract interface QTDisplaySpace extends QTDrawable, Space {
    /**
     * @deprecated
     */
    public abstract int getFrontLayer() throws QTException;

    /**
     * @deprecated
     */
    public abstract int getBackLayer() throws QTException;

    /**
     * @deprecated
     */
    public abstract Layerable getLayerable(Object paramObject);

    /**
     * @deprecated
     */
    public abstract Transformable getTransformable(Object paramObject);

    /**
     * @deprecated
     */
    public abstract boolean addMember(Object paramObject, int paramInt) throws QTException;

    /**
     * @deprecated
     */
    public abstract Object hitTest(int paramInt1, int paramInt2) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/QTDisplaySpace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
