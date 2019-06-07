package quicktime.vr;

/**
 * This interface is defined by an application to receive notification of
 * mouse over events when the mouse is over a hot-spot in a VR movie
 * @see {@link quicktime.vr.QTVRInstance}
 */
public abstract interface QTVRMouseOverHotSpot {

    /**
     * This method is called by MoviesTask when a user is over a hot spot
     * in a VR movie.
     * @param vr the QTVRInstance
     * @param hotSpotID the ID of the hot spot the mouse is over
     * @param flags indicate whether the mouse is entering, in or exiting the
     *  hot spot
     * @return an error code or zero if no error
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
    public abstract int execute(QTVRInstance paramQTVRInstance, int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRMouseOverHotSpot.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
