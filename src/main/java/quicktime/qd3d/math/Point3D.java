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

    private Point3D(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        Point3D point3D = (Point3D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(point3D, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        out.write(point3D.getBytes());
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
    public void setX(float x) {
        setFloatAt(0, x);
    }

    /**
     * sets the position in the Y direction
     */
    public void setY(float y) {
        setFloatAt(4, y);
    }

    /**
     * sets the position in the Z direction
     */
    public void setZ(float z) {
        setFloatAt(8, z);
    }

    /**
     * Returns a three-dimensional vector that is the result of subtracting the point p2 from p1.
     *
     * @param p2 A three-dimensional point
     * @return a three-dimensional vector that is the result of subtracting the point p2 from p1.
     * @see "QuickDraw3D::Q3Point3D_Subtract"
     */
    public Vector3D subtract(Point3D p2) {
        Vector3D vector3D = new Vector3D();
        int i = Q3Point3D_Subtract(getBytes(), p2.getBytes(), vector3D.getBytes());
        return vector3D;
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
        Point3D point3D = new Point3D();
        int i = Q3Point3D_RRatio(getBytes(), p2.getBytes(), r1, r2, point3D.getBytes());
        return point3D;
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
        Point3D point3D = new Point3D();
        int i = Q3Point3D_Vector3D_Add(getBytes(), vector3D.getBytes(), point3D.getBytes());
        return point3D;
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
        Point3D point3D = new Point3D();
        int i = Q3Point3D_Vector3D_Subtract(getBytes(), vector3D.getBytes(), point3D.getBytes());
        return point3D;
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
        Vector3D vector3D = new Vector3D();
        int i = Q3Point3D_CrossProductTri(getBytes(), point2.getBytes(), point3.getBytes(), vector3D.getBytes());
        return vector3D;
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
        Point3D point3D = new Point3D();
        int i = Q3Point3D_TransformQuaternion(getBytes(), quaternion.getBytes(), point3D.getBytes());
        return point3D;
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

    private static int Q3Point3D_Subtract(byte[] p1, byte[] p2, byte[] result) {
        return QD3DManager.Q3Point3D_Subtract(p1, p2, result);
    }

    private static float Q3Point3D_Distance(byte[] p1, byte[] p2) {
        return QD3DManager.Q3Point3D_Distance(p1, p2);
    }

    private static float Q3Point3D_DistanceSquared(byte[] p1, byte[] p2) {
        return QD3DManager.Q3Point3D_DistanceSquared(p1, p2);
    }

    private static int Q3Point3D_RRatio(byte[] p1, byte[] p2, float r1, float r2, byte[] result) {
        return QD3DManager.Q3Point3D_RRatio(p1, p2, r1, r2, result);
    }

    private static int Q3Point3D_Vector3D_Add(byte[] point3D, byte[] vector3D, byte[] result) {
        return QD3DManager.Q3Point3D_Vector3D_Add(point3D, vector3D, result);
    }

    private static int Q3Point3D_Vector3D_Subtract(byte[] point3D, byte[] vector3D, byte[] result) {
        return QD3DManager.Q3Point3D_Vector3D_Subtract(point3D, vector3D, result);
    }

    private static int Q3Point3D_CrossProductTri(byte[] point1, byte[] point2, byte[] point3, byte[] crossVector) {
        return QD3DManager.Q3Point3D_CrossProductTri(point1, point2, point3, crossVector);
    }

    private static int Q3Point3D_TransformQuaternion(byte[] point3D, byte[] quaternion, byte[] result) {
        return QD3DManager.Q3Point3D_TransformQuaternion(point3D, quaternion, result);
    }
}
