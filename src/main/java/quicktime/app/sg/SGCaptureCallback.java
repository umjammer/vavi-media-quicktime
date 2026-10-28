package quicktime.app.sg;

/**
 * @deprecated
 * An object that implements this interface can be given to the SGDrawer
 * class to notify the application when a sequence grabbing operation with
 * a stop time set has been reached.
 */
public interface SGCaptureCallback {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Called when the SequenceGrabber has completed capturing.
     * @param sg the SGDrawer
     */
    void finish(SGDrawer sg);
}

