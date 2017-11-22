
package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.std.StdQTException;


public abstract class ExtremesCallBack extends QTCallBack {
    public int flags;

    public ExtremesCallBack(TimeBase paramTimeBase, int paramInt) throws QTException {
        super(paramTimeBase, 4);
        this.flags = paramInt;
    }

    public final void callMeWhen() throws StdQTException {
        super.callMeWhen(this.flags, 0, 0);
    }

    public String toString() {
        return super.toString() + ",[flags=" + this.flags + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/ExtremesCallBack.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
