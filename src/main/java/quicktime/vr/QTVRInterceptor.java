package quicktime.vr;

/**
 * This interface is defined by an application to receive and process
 * interactions with a VR movie's nodes
 * @see quicktime.vr.QTVRInstance
 */
public interface QTVRInterceptor {

    /**
     * This method is called by MoviesTask when a user is entering a new node
     * in a VR movie.
     * @param vr the QTVRInstance
     * @return a boolean value that indicates whether the QuickTime VR Manager should NOT call the
     *        intercepted function when your intercept procedure returns true or should call the intercepted
     *        function false.
     * @see quicktime.std.clocks.QTCallBack
     */
    boolean execute(QTVRInstance vr, QTVRInterceptRecord qtvrMsg);
}
