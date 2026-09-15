package quicktime.vr;

/**
 * This interface is defined by an application to receive and process
 * interactions with a VR movie's nodes
 * @see {@link quicktime.vr.QTVRInstance}
 */
public abstract interface QTVRLeavingNode {

    /**
     * This method is called by MoviesTask when a user is leaving a node
     * in a VR movie.
     * @param vr the QTVRInstance
     * @param fromNodeID the nodeID that the user is leaving
     * @param toNodeID the nodeID that the user is entering
     * @param cancel To cancel
     *  the leaving of the fromNode the application should set the first element of
     *  the boolean[] to true (by default it is set to false and the user will be able
     *  to leave the current node).
     * @return an error code or zero if no error.
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
    public abstract int execute(QTVRInstance paramQTVRInstance, int paramInt1, int paramInt2, boolean[] paramArrayOfBoolean);
}

