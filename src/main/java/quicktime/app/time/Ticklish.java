
package quicktime.app.time;

import quicktime.QTException;


public abstract interface Ticklish {
    public static final int kScale = 1000;

    public abstract void timeChanged(int paramInt) throws QTException;

    public abstract boolean tickle(float paramFloat, int paramInt) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * Ticklish.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
