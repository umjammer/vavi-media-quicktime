package quicktime.vr;

/**
 * This interface is defined by an application to receive and process
 * interactions with a VR movie's nodes
 * @see quicktime.vr.QTVRInstance
 */
public interface QTVREnteringNode {

    /**
     * This method is called by MoviesTask when a user is entering a new node
     * in a VR movie.
     * @param vr the QTVRInstance
     * @param nodeID the nodeID that the user is entering
     * @return an error code or zero if no error
     * @see quicktime.std.clocks.QTCallBack
     */
    int execute(QTVRInstance vr, int nodeID);
}
