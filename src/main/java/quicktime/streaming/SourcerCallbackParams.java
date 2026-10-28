package quicktime.streaming;

import quicktime.QTException;
import quicktime.util.QTPointer;

/**
 * This class provides a representation the native QTSSourcerCallbackProcParams structure
 * that provides callback registration services for the Sourcer
 */
public final class SourcerCallbackParams extends QTPointer {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 16;

    private StreamDispatcher notifyUPP;

    SourcerCallbackParams() throws QTException {
        super(16, true);
    }

    SourcerCallbackParams(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
    }

    /**
     * Constructs an SourcerCallbackParams object based on the native SourcerCallbackParams structure
     * @param proc the notify proc to be registered with the sourcer
     */
    public SourcerCallbackParams(int paramInt, StreamNotifyProc paramStreamNotifyProc) throws QTException {
        this(1, paramInt, paramStreamNotifyProc);
    }

    /**
     * Constructs an SourcerCallbackParams object based on the native SourcerCallbackParams structure
     * @param vers version of the callback params structure
     * @param proc the notify proc to be registered with the sourcer
     */
    public SourcerCallbackParams(int paramInt1, int paramInt2, StreamNotifyProc paramStreamNotifyProc) throws QTException {
        super(16, true);
        setVersion(paramInt1);
        setFlags(paramInt2);
        setProc(paramStreamNotifyProc);
    }

    /**
     * Sets the version of the sourcer callback params object
     * @param vers version
     */
    public void setVersion(int vers) {
        setIntAt(0, vers);
    }

    /**
     * Gets the version of the sourcer callback params object
     * @return version
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Sets the flags of the sourcer callback object
     * @param flags sourcer callback flags
     */
    public void setFlags(int flags) {
        setIntAt(4, flags);
    }

    /**
     * Gets the flags
     */
    public int getFlags() {
        return getIntAt(4);
    }

    /**
     * Sets the callback procedure for the Sourcer object. If the
     * proc is null, the current callback proc will be removed
     * @param proc StreamNotify Proc
     */
    public void setProc(StreamNotifyProc proc) {
        if (proc == null) {
            setIntAt(8, 0);
            if (this.notifyUPP != null) {
                this.notifyUPP.cleanup();
                this.notifyUPP = null;
            }
        } else {
            this.notifyUPP = new StreamDispatcher(proc);
            setIntAt(8, this.notifyUPP.ID());
        }
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",flags=" + getFlags() + ",proc" + this.notifyUPP + "]";
    }
}
