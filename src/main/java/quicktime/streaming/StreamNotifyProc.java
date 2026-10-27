package quicktime.streaming;

import quicktime.std.comp.Component;
import quicktime.util.QTPointer;

/**
 * The StreamNotifyProc class is used by notification procedures in the
 * Broadcaster package. An application should subclass this class, over-riding
 * the method(s) that is of interest. Actions associated with methods not over-ridden will
 * return 0, indicating no error
 */
public abstract class StreamNotifyProc {

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSNullNotification
     * kQTSPresBeginChangingNotification
     * kQTSPresDoneChangingNotification
     * kQTSPresentationChangedNotification
     * kQTSPresentationDoneNotification
     * kQTSAnnotationsChangedNotification
     *
     * @param type the type of notification
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSStreamBeginChangingNotification
     * kQTSStreamDoneChangingNotification
     * kQTSPreviewAckNotification
     * kQTSPrerollAckNotification
     * kQTSStartAckNotification
     * kQTSStopAckNotification
     * kQTSNewStreamNotification
     * kQTSStreamGoneNotification
     *
     * @param type the type of notification
     * @param s the stream that originated the notification
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt, Stream paramStream) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSNewPresentationNotification
     * kQTSPresentationGoneNotification
     *
     * @param type the type of notification
     * @param pres the Presentation that originated the notification
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt, Presentation paramPresentation) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSStreamChangedNotification
     *
     * @param type the type of notification
     * @param s the Stream that originated the notification
     * @param comp the component that changed
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt, Stream paramStream, Component paramComponent) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSStatusNotification
     *
     * @param type the type of notification
     * @param status the StatusParams containing the notification
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt, StatusParams paramStatusParams) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSURLNotification
     *
     * @param type the type of notification
     * @param s the String containing the notification information
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt, String paramString) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSErrorNotification
     *
     * @param err this is the errorNo that has occurred.
     * @param type the type of notification
     * @param s the String containing the error information
     * @param flags the error flags
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt1, int paramInt2, String paramString, int paramInt3) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSDurationNotification
     *
     * @param type the type of notification
     * @param flags the notification flags
     * @param duration the duration
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSBandwidthAlertNotification
     *
     * @param type the type of notification
     * @param flags the notification flags
     * @param restartAt time which the alert occurred
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt1, int paramInt2, int paramInt3) {
        return 0;
    }

    /**
     * This method should be overridden to handle the following notification types:
     * kQTSNewPresDetectedNotification
     *
     * @param type the type of notification
     * @param flags the notification flags
     * @param data a pointer to the data delivered by the notification
     * @return 0 if no error occurred processing the notification
     */
    public int execute(int paramInt1, int paramInt2, QTPointer paramQTPointer) {
        return 0;
    }
}
