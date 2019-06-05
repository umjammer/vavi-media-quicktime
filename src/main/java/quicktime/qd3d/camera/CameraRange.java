
package quicktime.qd3d.camera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;


public final class CameraRange extends QTByteObject implements Cloneable {
    public static final int kNativeSize = 8;

    static final long serialVersionUID = -4968315887114751172L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    public CameraRange() {
        super(8);
    }

    public CameraRange(float paramFloat1, float paramFloat2) {
        this();

        setHither(paramFloat1);
        setYon(paramFloat2);
    }

    private CameraRange(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];

        CameraRange localCameraRange = new CameraRange();

        paramObjectInputStream.read(localCameraRange.getBytes());
        try {
            EndianOrder.flipBigEndianToNative(localCameraRange, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        System.arraycopy(localCameraRange.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        CameraRange localCameraRange = (CameraRange) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localCameraRange, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        paramObjectOutputStream.write(localCameraRange.getBytes());
    }

    public float getHither() {
        return getFloatAt(0);
    }

    public float getYon() {
        return getFloatAt(4);
    }

    public void setHither(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public void setYon(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public Object clone() {
        return new CameraRange(getBytes());
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * camera/CameraRange.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
