
package quicktime.std.qtcomponents;

import quicktime.util.QTHandleRef;


public final class TimeCodeInfo {
    public int frameNumber;

    public TimeCodeDef definition;

    public TimeCodeTime time;

    public int counter;

    public QTHandleRef userData;

    public TimeCodeInfo() {
        this.frameNumber = 0;
        this.definition = new TimeCodeDef();
        this.time = new TimeCodeTime();
        this.counter = 0;
        this.userData = null;
    }

    public TimeCodeInfo(int paramInt,
            TimeCodeDef paramTimeCodeDef,
            TimeCodeTime paramTimeCodeTime,
            QTHandleRef paramQTHandleRef) {
        this.frameNumber = paramInt;
        this.definition = paramTimeCodeDef;
        this.time = paramTimeCodeTime;
        this.counter = 0;
        this.userData = paramQTHandleRef;
    }

    public TimeCodeInfo(int paramInt1, TimeCodeDef paramTimeCodeDef, int paramInt2, QTHandleRef paramQTHandleRef) {
        this.frameNumber = paramInt1;
        this.definition = paramTimeCodeDef;
        this.time = null;
        this.counter = paramInt2;
        this.userData = paramQTHandleRef;
    }

    public String toString() {
        return getClass().getName() + "[" + "frameNumber=" + this.frameNumber + "definition=" + this.definition.toString()
               + "time=" + this.time.toString() + "counter=" + this.counter + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TimeCodeInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
