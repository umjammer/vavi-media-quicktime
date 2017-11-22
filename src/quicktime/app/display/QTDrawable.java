
package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.image.Transformable;
import quicktime.qd.QDGraphics;
import quicktime.qd.Region;


/**
 * @deprecated
 */
public abstract interface QTDrawable extends Drawable, Transformable {
    /**
     * @deprecated
     */
    public abstract void setLocation(int paramInt1, int paramInt2) throws QTException;

    /**
     * @deprecated
     */
    public abstract void setGWorld(QDGraphics paramQDGraphics) throws QTException;

    /**
     * @deprecated
     */
    public abstract QDGraphics getGWorld() throws QTException;

    /**
     * @deprecated
     */
    public abstract void setClip(Region paramRegion) throws QTException;

    /**
     * @deprecated
     */
    public abstract Region getClip() throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/QTDrawable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
