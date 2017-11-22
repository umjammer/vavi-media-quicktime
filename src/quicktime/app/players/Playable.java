
package quicktime.app.players;

import quicktime.QTException;
import quicktime.app.time.Timeable;


/**
 * @deprecated
 */
public abstract interface Playable extends Timeable {
    public static final int kDurationUnknown = Integer.MIN_VALUE;

    /**
     * @deprecated
     */
    public abstract void setTime(int paramInt) throws QTException;

    /**
     * @deprecated
     */
    public abstract int getTime() throws QTException;

    /**
     * @deprecated
     */
    public abstract int getScale() throws QTException;

    /**
     * @deprecated
     */
    public abstract int getDuration() throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * players/Playable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
