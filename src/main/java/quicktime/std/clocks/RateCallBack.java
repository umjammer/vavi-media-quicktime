package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;

/**
 * Provides scheduled operations with time base time rate changes.
 * QuickTime::QTCallBack
 */
public abstract class RateCallBack extends QTCallBack {

    /**
     * Creates a new callback event that will invoke its Callback object when the rate of the
     * specified timebase reaches a certain value.
     * The callback is not active until you schedule it by calling the callMeWhen method.
     * QuickTime::NewCallBack()
     * @param tb the time base that the callback is attached too.
     * @param rate the specified rate which will invoke the Callback
     * @param flags the conditions under which the Callback will be executed
     */
    public RateCallBack(TimeBase paramTimeBase, float paramFloat, int paramInt) throws QTException {
        super(paramTimeBase, 2);
        this.rate = paramFloat;
        this.flags = paramInt;
    }

    /**
     * Determines the rate that is used in the callback. Default is 1.0.
     */
    public float rate = 1.0F;

    /**
     * Determines when the callback will be called.
     */
    public int flags;

    /**
     * Makes the callback active.
     * The rate value should be set before this method is called.
     * QuickTime::CallMeWhen
     */
    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(QTUtils.X2Fix(this.rate), this.flags, 0);
    }

    /**
     * String representation of this class.
     */
    public String toString() {
        return getClass().getName() + "[rate=" + this.rate + ",flags=" + this.flags + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/RateCallBack.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
