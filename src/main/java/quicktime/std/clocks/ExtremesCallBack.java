package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;

/**
 * Provides scheduled operations at the start or stop times of the callback time base.
 * refer to QTCallBack in QuickTime
 */
public abstract class ExtremesCallBack extends QTCallBack {

    /**
     * Determines when the callback will be called.
     */
    public int flags;

    /**
     * This allows your application to obtain a new callback.
     * @see "QuickTime::NewCallBack()"
     * @param tb the time base that the callback is attached too.
     * @param flags the conditions under which the Callback will be executed
     */
    public ExtremesCallBack(TimeBase tb, int flags) throws QTException {
        super(tb, 4);
        this.flags = flags;
    }

    /**
     * Specifies the time when the callback sholud be called.
     * Should set flags specify certain scheduling information
     * as triggerAtStart or triggerAtStop.
     * @see "QuickTime::CallMeWhen"
     */
    @Override
    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(this.flags, 0, 0);
    }

    /**
     * String representation of this class.
     */
    public String toString() {
        return super.toString() + ",[flags=" + this.flags + "]";
    }
}

