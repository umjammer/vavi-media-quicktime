package quicktime.qd3d.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;

/**
 * Point3D contains information for the three dimensional point.
 *
 * Refer to TQ3Point3D in QuickDraw3d
 */
public final class Point3D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 12;

    @Serial
    private static final long serialVersionUID = 8457546849143567880L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(Point3D.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a Point3D object containing no data.
     */
    public Point3D() {
        super(12);
    }

    /**
     * Creates a Point3D object containing with given data.
     * @param x The final x coordinate.
     * @param y The final y coordinate.
     * @param z The final z coordinate.
     */
    public Point3D(float paramFloat1, float paramFloat2, float paramFloat3) {
        this();
        setX(paramFloat1);
        setY(paramFloat2);
        setZ(paramFloat3);
    }

    private Point3D(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[12];
        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Point3D localPoint3D = (Point3D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localPoint3D, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        paramObjectOutputStream.write(localPoint3D.getBytes());
    }

    /**
     * returns the position in the X direction
     */
    public float getX() {
        return getFloatAt(0);
    }

    /**
     * returns the position in the Y direction
     */
    public float getY() {
        return getFloatAt(4);
    }

    /**
     * returns the position in the Z direction
     */
    public float getZ() {
        return getFloatAt(8);
    }

    /**
     * sets the position in the X direction
     */
    public void setX(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * sets the position in the Y direction
     */
    public void setY(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * sets the position in the Z direction
     */
    public void setZ(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    /**
     * Returns a three-dimensional vector that is the result of subtracting the point p2 from p1.
     * @see "QuickDraw3D::Q3Point3D_Subtract"
     * @param p2 A three-dimensional point
     * @return a three-dimensional vector that is the result of subtracting the point p2 from p1.
     */
    public Vector3D subtract(Point3D paramPoint3D) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Point3D_Subtract(getBytes(), paramPoint3D.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the absolute value of the distance between points this and p2.
     * @see "QuickDraw3D::Q3Point3D_Distance"
     * @param p2 A three-dimensional point
     * @return the distance
     */
    public float distance(Point3D paramPoint3D) {
        return Q3Point3D_Distance(getBytes(), paramPoint3D.getBytes());
    }

    /**
     * Returns the squared value of the distance between points this and p2.
     * @see "QuickDraw3D::Q3Point3D_DistanceSquared"
     * @param p2 A three-dimensional point
     * @return the distance
     */
    public float distanceSquared(Point3D paramPoint3D) {
        return Q3Point3D_DistanceSquared(getBytes(), paramPoint3D.getBytes());
    }

    /**
     * Returns the three-dimensional point that lies on the line segment between
     * the points p1 and p2 and that is at a distance from the first point determined by
     * the ratio r1/(r1 + r2).
     * @see "QuickDraw3D::Q3Point3D_RRatio"
     * @param p2 A three-dimensional point.
     * @param r1 A floating-point number.
     * @param r2 A floating-point number.
     * @return a 3DPoint
     */
    public Point3D rRatio(Point3D paramPoint3D, float paramFloat1, float paramFloat2) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_RRatio(getBytes(), paramPoint3D.getBytes(), paramFloat1, paramFloat2, localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns the three-dimensional point that is the result of adding the
     * vector vector3D to this.
     * @see "QuickDraw3D::Q3Point3D_Vector3D_Add"
     * @param vector3D A three-dimensional vector.
     * @return a three-dimensional point
     */
    public Point3D add(Vector3D paramVector3D) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_Vector3D_Add(getBytes(), paramVector3D.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns the three-dimensional point that is the result of subtracting the
     * vector vector3D from this.
     * @see "QuickDraw3D::Q3Point3D_Vector3D_Subtract"
     * @param vector3D A three-dimensional vector.
     * @return a three-dimensional point
     */
    public Point3D subtract(Vector3D paramVector3D) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_Vector3D_Subtract(getBytes(), paramVector3D.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns the cross product of the two vectors determined by
     * subtracting point2 from this and point3 from this
     * @see "QuickDraw3D::Q3Point3D_CrossProductTri"
     * @param point2 A three-dimensional point.
     * @param point3 A three-dimensional point.
     * @return a vector
     */
    public Vector3D cross(Point3D paramPoint3D1, Point3D paramPoint3D2) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Point3D_CrossProductTri(getBytes(), paramPoint3D1.getBytes(), paramPoint3D2.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Return a three-dimensional point that is the result of
     * transforming the vector specified by the vector parameter using the quaternion
     * specified by the quaternion parameter.
     * @see "QuickDraw3D::Q3Point3D_TransformQuaternion"
     * @param quaternion A quaternion.
     * @return a three-dimensional point
     */
    public Point3D transformQuaternion(Quaternion paramQuaternion) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_TransformQuaternion(getBytes(), paramQuaternion.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns a copy of this object.
     */
    @Override
    public Object clone() {
        return new Point3D(getBytes());
    }

    /**
     * Returns a string representation of this object.
     */
    public String toString() {
        return super.toString() + "[x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "]";
    }

    private static int Q3Point3D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point3D_Subtract(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static float Q3Point3D_Distance(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Point3D_Distance(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static float Q3Point3D_DistanceSquared(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Point3D_DistanceSquared(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Point3D_RRatio(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat1, float paramFloat2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point3D_RRatio(paramArrayOfByte1, paramArrayOfByte2, paramFloat1, paramFloat2, paramArrayOfByte3);
    }

    private static int Q3Point3D_Vector3D_Add(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point3D_Vector3D_Add(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int Q3Point3D_Vector3D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point3D_Vector3D_Subtract(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int Q3Point3D_CrossProductTri(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4) {
        return QD3DManager.Q3Point3D_CrossProductTri(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4);
    }

    private static int Q3Point3D_TransformQuaternion(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Point3D_TransformQuaternion(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }
}
