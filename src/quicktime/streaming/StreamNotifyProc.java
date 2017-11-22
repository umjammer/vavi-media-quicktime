
package quicktime.streaming;

import quicktime.std.comp.Component;
import quicktime.util.QTPointer;


public abstract class StreamNotifyProc {
    public int execute(int paramInt) {
        return 0;
    }

    public int execute(int paramInt, Stream paramStream) {
        return 0;
    }

    public int execute(int paramInt, Presentation paramPresentation) {
        return 0;
    }

    public int execute(int paramInt, Stream paramStream, Component paramComponent) {
        return 0;
    }

    public int execute(int paramInt, StatusParams paramStatusParams) {
        return 0;
    }

    public int execute(int paramInt, String paramString) {
        return 0;
    }

    public int execute(int paramInt1, int paramInt2, String paramString, int paramInt3) {
        return 0;
    }

    public int execute(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        return 0;
    }

    public int execute(int paramInt1, int paramInt2, int paramInt3) {
        return 0;
    }

    public int execute(int paramInt1, int paramInt2, QTPointer paramQTPointer) {
        return 0;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /StreamNotifyProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
