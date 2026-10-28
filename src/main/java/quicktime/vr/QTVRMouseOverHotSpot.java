package quicktime.vr;

/**
 * This interface is defined by an application to receive notification of
 * mouse over events when the mouse is over a hot-spot in a VR movie
 * @see quicktime.vr.QTVRInstance
 */
public interface QTVRMouseOverHotSpot {

    /**
     * This method is called by MoviesTask when a user is over a hot spot
     * in a VR movie.
     * @param vr the QTVRInstance
     * @param hotSpotID the ID of the hot spot the mouse is over
     * @param flags indicate whether the mouse is entering, in or exiting the
     *  hot spot
     * @return an error code or zero if no error
     * @see quicktime.std.clocks.QTCallBack
     */
    int execute(QTVRInstance vr, int hotSpotID, int flags);
}

