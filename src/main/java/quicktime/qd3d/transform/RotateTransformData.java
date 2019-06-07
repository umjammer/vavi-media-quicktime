package quicktime.qd3d.transform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;

/**
 * RotateTransformData contains information for three dimensional rotations.
 *
 * refer to RotateTransformData in QuickDraw3D
 */
public final class RotateTransformData extends QTByteObject implements Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 8;

    static final long serialVersionUID = -5583319263995802834L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a RotateTransformData object containing no data.
     */
    public RotateTransformData() {
        super(8);
    }

    /**
     * Creates a RotateTransformData object containing with given data.
     * @param axis The axis specification: 0 - X, 1 - Y, 2 - Z.
     * @param radians The angle mesuared in radians.
     */
    public RotateTransformData(int paramInt, float paramFloat) {
        this();
        setAxis(paramInt);
        setRadian(paramFloat);
    }

    private RotateTransformData(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];
        RotateTransformData localRotateTransformData = new RotateTransformData();
        paramObjectInputStream.read(localRotateTransformData.getBytes());
        try {
            EndianOrder.flipBigEndianToNative(localRotateTransformData, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        System.arraycopy(localRotateTransformData.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        RotateTransformData localRotateTransformData = (RotateTransformData) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localRotateTransformData, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        paramObjectOutputStream.write(localRotateTransformData.getBytes());
    }

    /**
     * returns the axis around which rotation will happen.
     */
    public int getAxis() {
        return getIntAt(0);
    }

    /**
     * returns angle of rotation in radians.
     */
    public float getRadian() {
        return getFloatAt(4);
    }

    /**
     * sets the the axis around which rotation will happen.
     */
    public void setAxis(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * sets angle of rotation in radians.
     */
    public void setRadian(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        return new RotateTransformData(this.bytes);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName();
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/
 * transform/RotateTransformData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
