
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
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

    public void setBounds(QDRect paramQDRect) throws StdQTException {
        StdQTException.checkError(SGSetChannelBounds(_ID(), paramQDRect.getRect()));
    }

    public QDRect getBounds() throws StdQTException {
        QDRect localQDRect = new QDRect();
        StdQTException.checkError(SGGetChannelBounds(_ID(), localQDRect.getRect()));

        return localQDRect;
    }

    public void setClip(Region paramRegion) throws StdQTException {
        int i = SGSetChannelClip(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    public Region getClip() throws QTException {
        return Region.fromVideoChannel(this);
    }

    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        int i = SGSetChannelMatrix(_ID(), paramMatrix == null ? null : paramMatrix.getBytes());
        StdQTException.checkError(i);
    }

    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        int i = SGGetChannelMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(i);
        return localMatrix;
    }

    private static native int SGSetChannelBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int SGGetChannelBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetChannelClip(int paramInt1, int paramInt2);

    private static native int SGSetChannelMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native int SGGetChannelMatrix(int paramInt, byte[] paramArrayOfByte);
}
