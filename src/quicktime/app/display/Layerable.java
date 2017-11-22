
package quicktime.app.display;

import quicktime.QTException;


/**
 * @deprecated
 */
public abstract interface Layerable {
    /**
     * @deprecated
     */
    public static final int kBackMostLayer = 32767;

    /**
     * @deprecated
     */
    public abstract int getLayer() throws QTException;

    /**
     * @deprecated
     */
    public abstract void setLayer(int paramInt) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/Layerable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
