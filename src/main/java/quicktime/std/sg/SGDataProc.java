
package quicktime.std.sg;

import quicktime.QTException;
import quicktime.util.QTPointerRef;


public abstract interface SGDataProc {
    public abstract int execute(SGChannel paramSGChannel,
                                QTPointerRef paramQTPointerRef,
                                int paramInt1,
                                int paramInt2,
                                int paramInt3,
                                int paramInt4) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGDataProc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
