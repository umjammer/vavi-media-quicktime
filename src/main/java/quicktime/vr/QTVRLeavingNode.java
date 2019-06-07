package quicktime.vr;

/**
 * This interface is defined by an application to receive and process
 * interactions with a VR movie's nodes
 * @see {@link quicktime.vr.QTVRInstance}
 */
public abstract interface QTVRLeavingNode {

    public abstract int execute(QTVRInstance paramQTVRInstance, int paramInt1, int paramInt2, boolean[] paramArrayOfBoolean);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRLeavingNode.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
