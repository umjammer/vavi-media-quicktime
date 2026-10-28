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
    public NewPresentationParams(int dataType, QTPointer data, EditList list) throws QTException {
        super(36, true);
        setDataType(dataType);
        setData(data);
        setEditList(list);
    }

    private QTPointer data = null;

    private EditList el = null;

    /**
     * Sets the data type of the params
     * @param dataType data type
     */
    public void setDataType(int dataType) {
        setIntAt(0, dataType);
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
    public void setData(QTPointer data) {
        setIntAt(4, QTObject.ID(data));
        setIntAt(8, data.getSize());
        this.data = data;
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
    public void setFlags(int flags) {
        setIntAt(16, flags);
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
    public void setTimeScale(int timeScale) {
        setIntAt(20, timeScale);
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
    public void setMediaParams(MediaParams mp) {
        setIntAt(24, QTObject.ID(mp));
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
