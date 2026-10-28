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
    public PresParams(int scale, int flags, MediaParams params) throws QTException {
        super(28, true);
        setVersion(1);
        setEditList(null);
        setFlags(flags);
        setTimeScale(scale);
        setMediaParams(params);
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
    public void setVersion(int version) {
        setIntAt(0, version);
    }

    /**
     * Sets the flags of the Broadcaster params.
     * @param flags the flags
     */
    public void setFlags(int flags) {
        setIntAt(8, flags);
    }

    /**
     * Sets the time scale of the Broadcaster params.
     * @param scale the time scale
     */
    public void setTimeScale(int scale) {
        setIntAt(12, scale);
    }

    /**
     * Sets the media params of the presentation params.
     * @param m MediaParams
     */
    public void setMediaParams(MediaParams m) {
        setIntAt(16, QTObject.ID(m));
        this.myMediaParams = m;
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
    public void setNotificationProc(StreamNotifyProc proc) {
        if (proc == null) {
            removeNotificationProc();
        } else {
            this.notifyUPP = new StreamDispatcher(proc);
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
