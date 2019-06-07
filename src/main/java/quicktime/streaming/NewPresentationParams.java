package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.util.QTPointer;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

/**
 * This class is used to specify paramters for the creation of a Presentation object
 */
public final class NewPresentationParams extends QTPointerRef {

    /**
     * This is the size of this class
     */
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

    /**
     * Create a NewPresentationParams object
     * @param dataType the type of the data to be used by the presenter
     * @param data data
     * @param list a handle containing a list of edit items
     */
    public NewPresentationParams(int paramInt, QTPointer paramQTPointer, EditList paramEditList) throws QTException {
        super(36, true);
        setDataType(paramInt);
        setData(paramQTPointer);
        setEditList(paramEditList);
    }

    private QTPointer data = null;

    private EditList el = null;

    /**
     * Sets the data type of the params
     * @param dataType data type
     */
    public void setDataType(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Gets the type of the presentation data
     */
    public int getDataType() {
        return getIntAt(0);
    }

    /**
     * Sets the presentation data
     * @param data presentation data
     */
    public void setData(QTPointer paramQTPointer) {
        setIntAt(4, QTObject.ID(paramQTPointer));
        setIntAt(8, paramQTPointer.getSize());
        this.data = paramQTPointer;
    }

    /**
     * Gets the presentation data
     */
    public QTPointer getData() {
        return this.data;
    }

    private void setEditList(EditList paramEditList) {
        setIntAt(12, QTObject.ID(paramEditList));
        this.el = paramEditList;
    }

    /**
     * Gets the presentation's edit list parameter
     */
    public EditList getEditList() {
        return this.el;
    }

    /**
     * Sets the flags of the presentation
     * @param flags presentation flags
     */
    public void setFlags(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * Gets the presentation flags
     */
    public int getFlags() {
        return getIntAt(16);
    }

    /**
     * Sets the time scale of the presentation
     * @param timeScale time scale
     */
    public void setTimeScale(int paramInt) {
        setIntAt(20, paramInt);
    }

    /**
     * Gets the time scale of the presentation params
     */
    public int getTimeScale() {
        return getIntAt(20);
    }

    /**
     * Associates a MediaParams object with the presentation
     * @param mp MediaParams
     */
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

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[dataType=" + QTUtils.fromOSType(getDataType()) + ",data=" + getData() + ",editList" + getEditList() + ",flags" + getFlags() + ",timeScale" + getTimeScale() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /NewPresentationParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
