package quicktime.std.sg;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.Matrix;


public abstract class VisualChannel extends SGChannel implements QuickTimeLib {

    VisualChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
    }

    VisualChannel(SequenceGrabber owner, int channelType) throws StdQTException {
        super(owner, channelType);
    }

    /**
     * Allows you to specify a channel's display boundary rectangle.
     * @see "QuickTime::SGSetChannelBounds"
     * @param bounds the new display bounds
     * @throws quicktime.std.StdQTException
     */
    public void setBounds(QDRect bounds) throws StdQTException {
        StdQTException.checkError(SGSetChannelBounds(_ID(), bounds.getRect()));
    }

    /**
     * Allows you to get a channel's display boundary rectangle.
     * @see "QuickTime::SGGetChannelBounds"
     * @return the current display bounds
     * @throws quicktime.std.StdQTException
     */
    public QDRect getBounds() throws StdQTException {
        QDRect bounds = new QDRect();
        StdQTException.checkError(SGGetChannelBounds(_ID(), bounds.getRect()));
        return bounds;
    }

    /**
     * Allows you to set a channel's clipping region.
     * @see "QuickTime::SGSetChannelClip"
     * @param theClip a new clipping region or null to clear the clipping region.
     * @throws quicktime.std.StdQTException
     */
    public void setClip(Region theClip) throws StdQTException {
        int i = SGSetChannelClip(_ID(), QTObject.ID(theClip));
        StdQTException.checkError(i);
    }

    /**
     * Allows you to retrieve a channels clipping region.
     * @see "QuickTime::SGGetChannelClip"
     * @return the clipping region or null if no clip.
     * @throws quicktime.QTException
     */
    public Region getClip() throws QTException {
        return Region.fromVideoChannel(this);
    }

    /**
     * Allows you to set a channel's display transformation matrix.
     * @see "QuickTime::SGSetChannelMatrix"
     */
    public void setMatrix(Matrix matrix) throws StdQTException {
        int i = SGSetChannelMatrix(_ID(), matrix == null ? null : matrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Allows you to get a channel's display transformation matrix.
     * @see "QuickTime::SGGetChannelMatrix"
     * @return the current matrix of the channel.
     * @throws quicktime.std.StdQTException
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix matrix = new Matrix();
        int i = SGGetChannelMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(i);
        return matrix;
    }

    private static int SGSetChannelBounds(int ref, byte[] bounds) {
        return SequenceGrabberManager.SGSetChannelBounds(ref, bounds);
    }

    private static int SGGetChannelBounds(int ref, byte[] bounds) {
        return SequenceGrabberManager.SGGetChannelBounds(ref, bounds);
    }

    private static int SGSetChannelClip(int ref, int rgn) {
        return SequenceGrabberManager.SGSetChannelClip(ref, rgn);
    }

    private static int SGSetChannelMatrix(int ref, byte[] matrix) {
        return SequenceGrabberManager.SGSetChannelMatrix(ref, matrix);
    }

    private static int SGGetChannelMatrix(int ref, byte[] matrix) {
        return SequenceGrabberManager.SGGetChannelMatrix(ref, matrix);
    }
}
