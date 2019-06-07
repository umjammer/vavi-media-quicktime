package quicktime.app.sg;

/**
 * Deprecated.
 * An object that implements this interface can be given to the SGDrawer
 * class to notify the application when a sequence grabbing operation with
 * a stop time set has been reached.
 */
public abstract interface SGCaptureCallback {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Called when the SequenceGrabber has completed capturing.
     * @param sg the SGDrawer
     */
    public abstract void finish(SGDrawer paramSGDrawer);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/sg/
 * SGCaptureCallback.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
