
package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


public abstract class RateCallBack extends QTCallBack {
    public RateCallBack(TimeBase paramTimeBase, float paramFloat, int paramInt) throws QTException {
        super(paramTimeBase, 2);
        this.rate = paramFloat;
        this.flags = paramInt;
    }

    public float rate = 1.0F;

    public int flags;

    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(QTUtils.X2Fix(this.rate), this.flags, 0);
    }

    public String toString() {
        return getClass().getName() + "[rate=" + this.rate + ",flags=" + this.flags + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/RateCallBack.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
