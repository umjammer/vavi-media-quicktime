
package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;


public abstract class TimeCallBack extends QTCallBack {
    public int scale;

    public int value;

    public int flags;

    public TimeCallBack(TimeBase paramTimeBase, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        super(paramTimeBase, 1);
        this.scale = paramInt1;
        this.value = paramInt2;
        this.flags = paramInt3;
    }

    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(this.flags, this.value, this.scale);
    }

    public String toString() {
        return super.toString() + "[scale=" + this.scale + ",value=" + this.value + ",flags=" + this.flags + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/TimeCallBack.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
