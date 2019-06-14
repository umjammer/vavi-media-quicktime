package quicktime.vr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTByteObject;

public final class QTVRCubicFace extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 32;

    private static EndianDescriptor ed;

    static final long serialVersionUID = 5183605583657368208L;

    /**
     * Creates a QTVRCubicFace object containing no data
     */
    public QTVRCubicFace() {
        super(32);
    }

    /**
     * Creates a QTVRCubicFace object from a byte array
     * Used in cloning
     */
    public QTVRCubicFace(byte[] paramArrayOfByte) throws QTException {
        super(paramArrayOfByte);
        if (paramArrayOfByte.length != kNativeSize) {
            throw new QTException(-50);
        }
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = new EndianDescriptor(16);
        return localEndianDescriptor;
    }

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        QTVRCubicFace localQTVRCubicFace = new QTVRCubicFace();
        paramObjectInputStream.read(localQTVRCubicFace.getBytes());
        System.arraycopy(localQTVRCubicFace.getBytes(), 0, getBytes(), 0, this.bytes.length);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        QTVRCubicFace localQTVRCubicFace = (QTVRCubicFace) clone();
        paramObjectOutputStream.write(localQTVRCubicFace.getBytes());
    }

    protected Object clone() {
        try {
            return new QTVRCubicFace(this.bytes);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException);
        }
    }

    /**
     * returns the orientationW
     */
    public float getOrientationW() {
        return getFloatAt(0);
    }

    /**
     * set the orientationW
     */
    public void setOrientationW(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * returns the orientationX
     */
    public float getOrientationX() {
        return getFloatAt(4);
    }

    /**
     * set the orientationX
     */
    public void setOrientationX(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * returns the orientationY
     */
    public float getOrientationY() {
        return getFloatAt(8);
    }

    /**
     * set the orientationY
     */
    public void setOrientationY(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    /**
     * returns the orientationZ
     */
    public float getOrientationZ() {
        return getFloatAt(12);
    }

    /**
     * set the orientationZ
     */
    public void setOrientationZ(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    /**
     * returns the center
     */
    public QDPoint getCenter() {
        return new QDPoint(getFloatAt(16), getFloatAt(20));
    }

    /**
     * set the center
     */
    public void setCenter(QDPoint paramQDPoint) {
        setFloatAt(16, paramQDPoint.getXF());
        setFloatAt(20, paramQDPoint.getYF());
    }

    /**
     * aspect>1 => tall pixels; aspect <1 => squat pixels (default = 1)
     */
    public float getAspect() {
        return getFloatAt(24);
    }

    /**
     * aspect>1 => tall pixels; aspect <1 => squat pixels (default = 1)
     */
    public void setAspect(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    /**
     * returns the skew
     */
    public float getSkew() {
        return getFloatAt(28);
    }

    /**
     * set the skew
     */
    public void setSkew(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[orientation W=" + getOrientationW() + ",X=" + getOrientationX() + ",Y=" + getOrientationY() + ",Z=" + getOrientationZ() + ",center=" + getCenter() + ",aspect=" + getAspect() + ",skew=" + getSkew() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/vr/
 * QTVRCubicFace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
