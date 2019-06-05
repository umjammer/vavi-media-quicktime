
package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.util.QTPointerRef;


public final class PresParams extends QTPointerRef {
    public static final int kNativeSize = 28;

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
    }

    PresParams() throws QTException {
        super(28, true);
    }

    public PresParams(int paramInt1, int paramInt2, MediaParams paramMediaParams) throws QTException {
        super(28, true);
        setVersion(1);
        setEditList(null);
        setFlags(paramInt2);
        setTimeScale(paramInt1);
        setMediaParams(paramMediaParams);
    }

    private MediaParams myMediaParams = null;

    private EditList editList = null;

    private StreamDispatcher notifyUPP;

    public int getVersion() {
        return getIntAt(0);
    }

    public int getFlags() {
        return getIntAt(8);
    }

    public int getTimeScale() {
        return getIntAt(12);
    }

    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    public void setFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    public void setTimeScale(int paramInt) {
        setIntAt(12, paramInt);
    }

    public void setMediaParams(MediaParams paramMediaParams) {
        setIntAt(16, QTObject.ID(paramMediaParams));
        this.myMediaParams = paramMediaParams;
    }

    public MediaParams getMediaParams() {
        return this.myMediaParams;
    }

    public void setEditList(EditList paramEditList) {
        setIntAt(4, QTObject.ID(paramEditList));
        this.editList = paramEditList;
    }

    public EditList getEditList() {
        return this.editList;
    }

    public void setNotificationProc(StreamNotifyProc paramStreamNotifyProc) {
        if (paramStreamNotifyProc == null) {
            removeNotificationProc();
        } else {
            this.notifyUPP = new StreamDispatcher(paramStreamNotifyProc);
            setIntAt(20, this.notifyUPP.ID());
        }
    }

    public void removeNotificationProc() {
        setIntAt(20, 0);
        this.notifyUPP.cleanup();
        this.notifyUPP = null;
    }

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",editList=" + getEditList() + ",flags=" + getFlags()
               + ",timeScale" + getTimeScale() + ",mediaParams=" + getMediaParams() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /PresParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
