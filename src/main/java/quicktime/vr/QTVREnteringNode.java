package quicktime.vr;

/**
 * This interface is defined by an application to receive and process
 * interactions with a VR movie's nodes
 * @see {@link quicktime.vr.QTVRInstance}
 */
public abstract interface QTVREnteringNode {

    /**
     * This method is called by MoviesTask when a user is entering a new node
     * in a VR movie.
     * @param vr the QTVRInstance
     * @param nodeID the nodeID that the user is entering
     * @return an error code or zero if no error
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
    public abstract int execute(QTVRInstance paramQTVRInstance, int paramInt);
}

