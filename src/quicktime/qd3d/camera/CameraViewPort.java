
package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;


public final class CameraViewPort extends QTByteObject implements Cloneable {
    public static final int kNativeSize = 16;

    static final long serialVersionUID = -480864256741555003L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    public CameraViewPort() {
        super(16);
    }

    public CameraViewPort(QDPoint paramQDPoint, float paramFloat1, float paramFloat2) {
        this();

        setOrigin(paramQDPoint);
        setWidth(paramFloat1);
        setHeight(paramFloat2);
    }

    protected CameraViewPort(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[16];

        CameraViewPort localCameraViewPort = new CameraViewPort();

        paramObjectInputStream.read(localCameraViewPort.getBytes());
        try {
            EndianOrder.flipBigEndianToNative(localCameraViewPort, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        System.arraycopy(localCameraViewPort.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        CameraViewPort localCameraViewPort = (CameraViewPort) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localCameraViewPort, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        paramObjectOutputStream.write(localCameraViewPort.getBytes());
    }

    public QDPoint getOrigin() {
        return QDPoint.fromArray(getBytes(), -8);
    }

    public float getWidth() {
        return getFloatAt(8);
    }

    public float getHeight() {
        return getFloatAt(12);
    }

    public void setOrigin(QDPoint paramQDPoint) {
        setFloatAt(0, paramQDPoint.getXF());
        setFloatAt(4, paramQDPoint.getYF());
    }

    public void setWidth(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    public void setHeight(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    public Object clone() {
        return new CameraViewPort(this.bytes);
    }

    public String toString() {
        return getClass().getName();
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * camera/CameraViewPort.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
