package quicktime.streaming;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.util.QTPointerRef;

/**
 * Presentation Parameter object
 */
public final class PresParams extends QTPointerRef {

    /**
     * This is the size of this class
     */
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

    /**
     * Constructs a PresParams object
     * @param scale time scale of the presentation
     * @param flags presentation flags
     * @param params MediaParams
     */
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

    /**
     * Retrieves the version.
     * @return version of the presenter
     */
    public int getVersion() {
        return getIntAt(0);
    }

    /**
     * Retrieves the flags.
     * @return broadcaster flags
     */
    public int getFlags() {
        return getIntAt(8);
    }

    /**
     * Retrieves the time scale associated with the Broadcaster.
     * @return current time scale
     */
    public int getTimeScale() {
        return getIntAt(12);
    }

    /**
     * Sets the version of the Broadcaster params.
     * @param version the version
     */
    public void setVersion(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * Sets the flags of the Broadcaster params.
     * @param flags the flags
     */
    public void setFlags(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * Sets the time scale of the Broadcaster params.
     * @param scale the time scale
     */
    public void setTimeScale(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * Sets the media params of the presentation params.
     * @param m MediaParams
     */
    public void setMediaParams(MediaParams paramMediaParams) {
        setIntAt(16, QTObject.ID(paramMediaParams));
        this.myMediaParams = paramMediaParams;
    }

    /**
     * Gets the media params of the presentation params.
     * @return MediaParams
     */
    public MediaParams getMediaParams() {
        return this.myMediaParams;
    }

    /**
     * Sets the edit list
     */
    public void setEditList(EditList paramEditList) {
        setIntAt(4, QTObject.ID(paramEditList));
        this.editList = paramEditList;
    }

    /**
     * Gets the edit list
     * @return edit list
     */
    public EditList getEditList() {
        return this.editList;
    }

    /**
     * Sets the notification procedure
     * @param proc notification procedure
     */
    public void setNotificationProc(StreamNotifyProc paramStreamNotifyProc) {
        if (paramStreamNotifyProc == null) {
            removeNotificationProc();
        } else {
            this.notifyUPP = new StreamDispatcher(paramStreamNotifyProc);
            setIntAt(20, this.notifyUPP.ID());
        }
    }

    /**
     * Sets the notification procedure
     */
    public void removeNotificationProc() {
        setIntAt(20, 0);
        this.notifyUPP.cleanup();
        this.notifyUPP = null;
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + ",editList=" + getEditList() + ",flags=" + getFlags() + ",timeScale" + getTimeScale() + ",mediaParams=" + getMediaParams() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/streaming
 * /PresParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
