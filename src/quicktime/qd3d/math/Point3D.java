
package quicktime.qd3d.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;


public final class Point3D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 12;

    static final long serialVersionUID = 8457546849143567880L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }

        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative
                                .linkNativeMethods(Point3D.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    public Point3D() {
        super(12);
    }

    public Point3D(float paramFloat1, float paramFloat2, float paramFloat3) {
        this();

        setX(paramFloat1);
        setY(paramFloat2);
        setZ(paramFloat3);
    }

    private Point3D(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[12];

        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Point3D localPoint3D = (Point3D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localPoint3D, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        paramObjectOutputStream.write(localPoint3D.getBytes());
    }

    public float getX() {
        return getFloatAt(0);
    }

    public float getY() {
        return getFloatAt(4);
    }

    public float getZ() {
        return getFloatAt(8);
    }

    public void setX(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public void setY(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public void setZ(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    public Vector3D subtract(Point3D paramPoint3D) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Point3D_Subtract(getBytes(), paramPoint3D.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    public float distance(Point3D paramPoint3D) {
        return Q3Point3D_Distance(getBytes(), paramPoint3D.getBytes());
    }

    public float distanceSquared(Point3D paramPoint3D) {
        return Q3Point3D_DistanceSquared(getBytes(), paramPoint3D.getBytes());
    }

    public Point3D rRatio(Point3D paramPoint3D, float paramFloat1, float paramFloat2) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_RRatio(getBytes(), paramPoint3D.getBytes(), paramFloat1, paramFloat2, localPoint3D.getBytes());
        return localPoint3D;
    }

    public Point3D add(Vector3D paramVector3D) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_Vector3D_Add(getBytes(), paramVector3D.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    public Point3D subtract(Vector3D paramVector3D) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_Vector3D_Subtract(getBytes(), paramVector3D.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    public Vector3D cross(Point3D paramPoint3D1, Point3D paramPoint3D2) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Point3D_CrossProductTri(getBytes(),
                                          paramPoint3D1.getBytes(),
                                          paramPoint3D2.getBytes(),
                                          localVector3D.getBytes());
        return localVector3D;
    }

    public Point3D transformQuaternion(Quaternion paramQuaternion) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_TransformQuaternion(getBytes(), paramQuaternion.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    public Object clone() {
        return new Point3D(getBytes());
    }

    public String toString() {
        return super.toString() + "[x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "]";
    }

    private static native int Q3Point3D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native float Q3Point3D_Distance(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native float Q3Point3D_DistanceSquared(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Point3D_RRatio(byte[] paramArrayOfByte1,
                                               byte[] paramArrayOfByte2,
                                               float paramFloat1,
                                               float paramFloat2,
                                               byte[] paramArrayOfByte3);

    private static native int Q3Point3D_Vector3D_Add(byte[] paramArrayOfByte1,
                                                     byte[] paramArrayOfByte2,
                                                     byte[] paramArrayOfByte3);

    private static native int Q3Point3D_Vector3D_Subtract(byte[] paramArrayOfByte1,
                                                          byte[] paramArrayOfByte2,
                                                          byte[] paramArrayOfByte3);

    private static native int Q3Point3D_CrossProductTri(byte[] paramArrayOfByte1,
                                                        byte[] paramArrayOfByte2,
                                                        byte[] paramArrayOfByte3,
                                                        byte[] paramArrayOfByte4);

    private static native int Q3Point3D_TransformQuaternion(byte[] paramArrayOfByte1,
                                                            byte[] paramArrayOfByte2,
                                                            byte[] paramArrayOfByte3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Point3D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
