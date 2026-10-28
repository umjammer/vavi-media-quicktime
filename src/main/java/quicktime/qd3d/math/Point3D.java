package quicktime.qd3d.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;


/**
 * Point3D contains information for the three dimensional point.
 * <p>
 * Refer to TQ3Point3D in QuickDraw3d
 */
public final class Point3D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

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
    }

    /**
     * Returns the EndianDescriptor for this class.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a Point3D object containing no data.
     */
    public Point3D() {
        super(kNativeSize);
    }

    /**
     * Creates a Point3D object containing with given data.
     *
     * @param x The final x coordinate.
     * @param y The final y coordinate.
     * @param z The final z coordinate.
     */
    public Point3D(float x, float y, float z) {
        this();
        setX(x);
        setY(y);
        setZ(z);
    }

    private Point3D(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
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
     *
     * @param p2 A three-dimensional point
     * @return a three-dimensional vector that is the result of subtracting the point p2 from p1.
     * @see "QuickDraw3D::Q3Point3D_Subtract"
     */
    public Vector3D subtract(Point3D p2) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Point3D_Subtract(getBytes(), p2.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the absolute value of the distance between points this and p2.
     *
     * @param p2 A three-dimensional point
     * @return the distance
     * @see "QuickDraw3D::Q3Point3D_Distance"
     */
    public float distance(Point3D p2) {
        return Q3Point3D_Distance(getBytes(), p2.getBytes());
    }

    /**
     * Returns the squared value of the distance between points this and p2.
     *
     * @param p2 A three-dimensional point
     * @return the distance
     * @see "QuickDraw3D::Q3Point3D_DistanceSquared"
     */
    public float distanceSquared(Point3D p2) {
        return Q3Point3D_DistanceSquared(getBytes(), p2.getBytes());
    }

    /**
     * Returns the three-dimensional point that lies on the line segment between
     * the points p1 and p2 and that is at a distance from the first point determined by
     * the ratio r1/(r1 + r2).
     *
     * @param p2 A three-dimensional point.
     * @param r1 A floating-point number.
     * @param r2 A floating-point number.
     * @return a 3DPoint
     * @see "QuickDraw3D::Q3Point3D_RRatio"
     */
    public Point3D rRatio(Point3D p2, float r1, float r2) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_RRatio(getBytes(), p2.getBytes(), r1, r2, localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns the three-dimensional point that is the result of adding the
     * vector vector3D to this.
     *
     * @param vector3D A three-dimensional vector.
     * @return a three-dimensional point
     * @see "QuickDraw3D::Q3Point3D_Vector3D_Add"
     */
    public Point3D add(Vector3D vector3D) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_Vector3D_Add(getBytes(), vector3D.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns the three-dimensional point that is the result of subtracting the
     * vector vector3D from this.
     *
     * @param vector3D A three-dimensional vector.
     * @return a three-dimensional point
     * @see "QuickDraw3D::Q3Point3D_Vector3D_Subtract"
     */
    public Point3D subtract(Vector3D vector3D) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_Vector3D_Subtract(getBytes(), vector3D.getBytes(), localPoint3D.getBytes());
        return localPoint3D;
    }

    /**
     * Returns the cross product of the two vectors determined by
     * subtracting point2 from this and point3 from this
     *
     * @param point2 A three-dimensional point.
     * @param point3 A three-dimensional point.
     * @return a vector
     * @see "QuickDraw3D::Q3Point3D_CrossProductTri"
     */
    public Vector3D cross(Point3D point2, Point3D point3) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Point3D_CrossProductTri(getBytes(), point2.getBytes(), point3.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Return a three-dimensional point that is the result of
     * transforming the vector specified by the vector parameter using the quaternion
     * specified by the quaternion parameter.
     *
     * @param quaternion A quaternion.
     * @return a three-dimensional point
     * @see "QuickDraw3D::Q3Point3D_TransformQuaternion"
     */
    public Point3D transformQuaternion(Quaternion quaternion) {
        Point3D localPoint3D = new Point3D();
        int i = Q3Point3D_TransformQuaternion(getBytes(), quaternion.getBytes(), localPoint3D.getBytes());
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
