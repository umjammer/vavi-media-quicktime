
package quicktime.app.display;

import quicktime.QTException;
import quicktime.app.spaces.Listener;
import quicktime.qd.QDRect;
import quicktime.qd.Region;


/**
 * @deprecated
 */
public abstract interface Drawable extends Listener {
    /**
     * @deprecated
     */
    public abstract void setDisplayBounds(QDRect paramQDRect) throws QTException;

    /**
     * @deprecated
     */
    public abstract QDRect getDisplayBounds() throws QTException;

    /**
     * @deprecated
     */
    public abstract void redraw(Region paramRegion) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/Drawable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
