
package quicktime.app.time;

import quicktime.QTException;
import quicktime.std.clocks.TimeBase;


public abstract interface Timeable {
    public abstract TimeBase getTimeBase() throws QTException;

    public abstract void setRate(float paramFloat) throws QTException;

    public abstract float getRate() throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * Timeable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
