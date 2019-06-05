
package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointer;


public final class SourcerCallbackParams extends QTPointer {
    public static final int kNativeSize = 16;

    private StreamDispatcher notifyUPP;

    SourcerCallbackParams() throws QTException {
        super(16, true);
    }

    SourcerCallbackParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    public SourcerCallbackParams(int paramInt, StreamNotifyProc paramStreamNotifyProc) throws QTException {
        this(1, paramInt, paramStreamNotifyProc);
    }

    public SourcerCallbackParams(int paramInt1, int paramInt2, StreamNotifyProc paramStreamNotifyProc) throws QTException {
        super(16, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setProc(paramStreamNotifyProc);
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

    public void setProc(StreamNotifyProc paramStreamNotifyProc) {
        if (paramStreamNotifyProc == null) {
            setIntAt(8, 0);
            if (this.notifyUPP != null) {
                this.notifyUPP.cleanup();
                this.notifyUPP = null;
            }
        } else {
            this.notifyUPP = new StreamDispatcher(paramStreamNotifyProc);
            setIntAt(8, this.notifyUPP.ID());
        }
    }

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",proc" + this.notifyUPP + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /SourcerCallbackParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
