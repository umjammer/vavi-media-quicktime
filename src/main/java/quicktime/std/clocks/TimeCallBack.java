package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;

/**
 * Provides scheduled operations at specified time values.
 * Refer to QTCallBack in QuickTime
 */
public abstract class TimeCallBack extends QTCallBack {

    /**
     * Determines the scale that is used in the callback.
     */
    public int scale;

    /**
     * Determines the time value that is used in the callback
     */
    public int value;

    /**
     * Determines when the callback will be called.
     */
    public int flags;

    /**
     * This allows your application to obtain a new callback.
     * @see "QuickTime::NewCallBack()"
     * @param tb the time base that the callback is attached too.
     * @param scale the scale that is used to determine the value in seconds of the value parametre
     * @param value the value in units as specified by the scale
     * @param flags the conditions under which the Callback will be executed
     */
    public TimeCallBack(TimeBase paramTimeBase, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        super(paramTimeBase, 1);
        this.scale = paramInt1;
        this.value = paramInt2;
        this.flags = paramInt3;
    }

    /**
     * Specifies the time when the callback sholud be called.
     * @see "QuickTime::CallMeWhen"
     */
    @Override
    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(this.flags, this.value, this.scale);
    }

    /**
     * String representation of this class.
     */
    public String toString() {
        return super.toString() + "[scale=" + this.scale + ",value=" + this.value + ",flags=" + this.flags + "]";
    }
}

