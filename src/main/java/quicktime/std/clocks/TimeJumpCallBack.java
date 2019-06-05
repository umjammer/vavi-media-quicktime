
package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;


public abstract class TimeJumpCallBack extends QTCallBack {
    public TimeJumpCallBack(TimeBase paramTimeBase) throws QTException {
        super(paramTimeBase, 3);
    }

    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(0, 0, 0);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/TimeJumpCallBack.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
