package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.Matrix;

public abstract class VisualChannel extends SGChannel implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(VisualChannel.class);
                    }
                });
            }
        }.establish();
    }

    VisualChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    VisualChannel(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        super(paramSequenceGrabber, paramInt);
    }

    /**
     * Allows you to specify a channel's display boundary rectangle.
     * QuickTime::SGSetChannelBounds
     * @param bounds the new display bounds
     * @param {@link quicktime.std.StdQTException}
     */
    public void setBounds(QDRect paramQDRect) throws StdQTException {
        StdQTException.checkError(SGSetChannelBounds(_ID(), paramQDRect.getRect()));
    }

    /**
     * Allows you to get a channel's display boundary rectangle.
     * QuickTime::SGGetChannelBounds
     * @return the current display bounds
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        StdQTException.checkError(SGGetChannelBounds(_ID(), localQDRect.getRect()));
        return localQDRect;
    }

    /**
     * Allows you to set a channel's clipping region.
     * QuickTime::SGSetChannelClip
     * @param theClip a new clipping region or null to clear the clipping region.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClip(Region paramRegion) throws StdQTException {
        int i = SGSetChannelClip(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    /**
     * Allows you to retrieve a channels clipping region.
     * QuickTime::SGGetChannelClip
     * @return the clipping region or null if no clip.
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        return Region.fromVideoChannel(this);
    }

    /**
     * Allows you to set a channel's display transformation matrix.
     * QuickTime::SGSetChannelMatrix
     */
    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        int i = SGSetChannelMatrix(_ID(), paramMatrix == null ? null : paramMatrix.getBytes());
        StdQTException.checkError(i);
    }

    /**
     * Allows you to get a channel's display transformation matrix.
     * QuickTime::SGGetChannelMatrix
     * @return the current matrix of the channel.
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = SGGetChannelMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    private static int SGSetChannelBounds(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetChannelBounds(paramInt, paramArrayOfByte);
    }

    private static int SGGetChannelBounds(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGGetChannelBounds(paramInt, paramArrayOfByte);
    }

    private static int SGSetChannelClip(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGSetChannelClip(paramInt1, paramInt2);
    }

    private static int SGSetChannelMatrix(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetChannelMatrix(paramInt, paramArrayOfByte);
    }

    private static int SGGetChannelMatrix(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGGetChannelMatrix(paramInt, paramArrayOfByte);
    }
}
