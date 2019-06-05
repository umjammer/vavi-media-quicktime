
package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointer;


public final class SourcerLoopParams extends QTPointer {
    public static final int kNativeSize = 20;

    SourcerLoopParams() throws QTException {
        super(20, true);
    }

    SourcerLoopParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    public SourcerLoopParams(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        this(1, 0, paramInt1, paramInt2, paramInt3);
    }

    public SourcerLoopParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws QTException {
        super(20, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setLoopFlags(paramInt3);
        setFlagsMask(paramInt4);
        setNumLoops(paramInt5);
    }

    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getVersion() {
        return getIntAt(0);
    }

    public void setFlags(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getFlags() {
        return getIntAt(4);
    }

    public void setLoopFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getLoopFlags() {
        return getIntAt(8);
    }

    public void setFlagsMask(int paramInt) {
        setIntAt(12, paramInt);
    }

    public int getFlagsMask() {
        return getIntAt(12);
    }

    public void setNumLoops(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getNumLoops() {
        return getIntAt(16);
    }

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",loopFlags=" + getLoopFlags()
               + ",flagsMask" + getFlagsMask() + ",numLoops" + getFlagsMask() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerLoopParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
