
package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class NewPresentationParams extends QTPointerRef {
    public static final int kNativeSize = 36;

    private StreamDispatcher notifyUPP;

    static {
        if (QTSession.hasSecurityRestrictions()) {
            throw new SecurityException("Only able to broadcast with security settings when class is signed");
        }
        if (QTSession.isCurrentOS(2)) {
            throw new RuntimeException("The broadcaster API is not supported on windows");
        }
    }

    NewPresentationParams() throws QTException {
        super(36, true);
    }

    public NewPresentationParams(int paramInt, QTPointer paramQTPointer, EditList paramEditList) throws QTException {
        super(36, true);
        setDataType(paramInt);
        setData(paramQTPointer);
        setEditList(paramEditList);
    }

    private QTPointer data = null;

    private EditList el = null;

    public void setDataType(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getDataType() {
        return getIntAt(0);
    }

    public void setData(QTPointer paramQTPointer) {
        setIntAt(4, QTObject.ID(paramQTPointer));
        setIntAt(8, paramQTPointer.getSize());
        this.data = paramQTPointer;
    }

    public QTPointer getData() {
        return this.data;
    }

    private void setEditList(EditList paramEditList) {
        setIntAt(12, QTObject.ID(paramEditList));
        this.el = paramEditList;
    }

    public EditList getEditList() {
        return this.el;
    }

    public void setFlags(int paramInt) {
        setIntAt(16, paramInt);
    }

    public int getFlags() {
        return getIntAt(16);
    }

    public void setTimeScale(int paramInt) {
        setIntAt(20, paramInt);
    }

    public int getTimeScale() {
        return getIntAt(20);
    }

    public void setMediaParams(MediaParams paramMediaParams) {
        setIntAt(24, QTObject.ID(paramMediaParams));
    }

    public void setNotificationProc(StreamNotifyProc paramStreamNotifyProc) {
        if (paramStreamNotifyProc == null) {
            setIntAt(28, 0);
            this.notifyUPP.cleanup();
            this.notifyUPP = null;
        } else {
            this.notifyUPP = new StreamDispatcher(paramStreamNotifyProc);
            setIntAt(28, this.notifyUPP.ID());
        }
    }

    public String toString() {
        return getClass().getName() + "[dataType=" + QTUtils.fromOSType(getDataType()) + ",data=" + getData() + ",editList"
               + getEditList() + ",flags" + getFlags() + ",timeScale" + getTimeScale() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /NewPresentationParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
