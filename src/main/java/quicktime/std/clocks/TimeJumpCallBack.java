package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;

/**
 * Provides scheduled operations when a program's abruptly changes the time value of the callback time base.
 * Refer to QTCallBack in QuickTime
 */
public abstract class TimeJumpCallBack extends QTCallBack {

    /**
     * This allows your application to obtain a new callback.
     * @see "QuickTime::NewCallBack()"
     * @param tb the time base that the callback is attached too.
     */
    public TimeJumpCallBack(TimeBase tb) throws QTException {
        super(tb, 3);
    }

    /**
     * Specifies the time when the callback sholud be called.
     * Should set flags that specify certain scheduling information before calling this function
     * @see "QuickTime::CallMeWhen"
     */
    @Override
    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(0, 0, 0);
    }
}
