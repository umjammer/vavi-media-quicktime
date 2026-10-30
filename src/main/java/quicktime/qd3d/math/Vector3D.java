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
 * Vector3D contains information for three dimensional vectors.
 *
 * refer to TQ3Vector3D in QuickDraw3d
 */
public final class Vector3D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 12;

    @Serial
    private static final long serialVersionUID = -8566142369958926912L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a Vector3D object containing no data.
     */
    public Vector3D() {
        super(kNativeSize);
    }

    /**
     * Creates a Vector3D object containing with given data.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    public Vector3D(float x, float y, float z) {
        this();
        setX(x);
        setY(y);
        setZ(z);
    }

    private Vector3D(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[12];
        in.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        Vector3D vector3D = (Vector3D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(vector3D, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        out.write(vector3D.getBytes());
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
     * Returns a three-dimensional vector that is the result of
     * multiplying each of the components of this by the value of
     * the scalar parameter.
     * @see "QuickDraw3D::Q3Vector3D_Scale"
     * @param scalar A floating-point number.
     * @return a new scaled vector.
     */
    public Vector3D scale(float scalar) {
        Vector3D vector3D = new Vector3D();
        int i = Q3Vector3D_Scale(getBytes(), scalar, vector3D.getBytes());
        return vector3D;
    }

    /**
     * Returns the length of a three-dimensional vector.
     * @see "QuickDraw3D::Q3Vector3D_Length"
     * @return the length of the vector.
     */
    public float length() {
        return Q3Vector3D_Length(getBytes());
    }

    /**
     * Returns the normalized form of the vector.
     * @see "QuickDraw3D::Q3Vector3D_Normalize"
     * @return the normalized form of the vector
     */
    public Vector3D normalize() {
        Vector3D vector3D = new Vector3D();
        int i = Q3Vector3D_Normalize(getBytes(), vector3D.getBytes());
        return vector3D;
    }

    /**
     * Returns the three-dimensional vector that is the sum of the two vectors this and v2.
     * @see "QuickDraw3D::Q3Vector3D_Add"
     * @param v2 A three-dimensional vector.
     * @return the sum of v1 and v2.
     */
    public Vector3D add(Vector3D v2) {
        Vector3D vector3D = new Vector3D();
        int i = Q3Vector3D_Add(getBytes(), v2.getBytes(), vector3D.getBytes());
        return vector3D;
    }

    /**
     * Returns the three-dimensional vector that is the result of subtracting v2 from this.
     * @see "QuickDraw3D::Q3Vector3D_Subtract"
     * @param v2 A three-dimensional vector.
     * @return the result of subtracting v2 from this.
     */
    public Vector3D subtract(Vector3D v2) {
        Vector3D vector3D = new Vector3D();
        int i = Q3Vector3D_Subtract(getBytes(), v2.getBytes(), vector3D.getBytes());
        return vector3D;
    }

    /**
     * Returns the cross product of this vector and vector v2.
     * @see "QuickDraw3D::Q3Vector3D_Cross"
     * @param v2 A two-dimensional vector.
     * @return a vector
     */
    public Vector3D cross(Vector3D v2) {
        Vector3D vector3D = new Vector3D();
        int i = Q3Vector3D_Cross(getBytes(), v2.getBytes(), vector3D.getBytes());
        return vector3D;
    }

    /**
     * Returns the dot product of this vector and vector v2.
     * @see "QuickDraw3D::Q3Vector3D_Dot"
     * @param v2 A two-dimensional vector.
     * @return a vector
     */
    public float dot(Vector2D v2) {
        return Q3Vector3D_Dot(getBytes(), v2.getBytes());
    }

    /**
     * Return a three-dimensional vector that is the result of
     * transforming the vector specified by the vector parameter using the quaternion
     * specified by the quaternion parameter.
     * @see "QuickDraw3D::Q3Vector3D_TransformQuaternion"
     * @param quaternion A quaternion.
     * @return a three-dimensional vector
     */
    public Vector3D transformQuaternion(Quaternion quaternion) {
        Vector3D vector3D = new Vector3D();
        int i = Q3Vector3D_TransformQuaternion(getBytes(), quaternion.getBytes(), vector3D.getBytes());
        return vector3D;
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new Vector3D(getBytes());
    }

    /**
     * Returns a string representation of this object.
     */
    public String toString() {
        return super.toString() + "[x=" + getX() + ",y=" + getY() + ",z=" + getX() + "]";
    }

    private static int Q3Vector3D_Scale(byte[] vector3D, float scalar, byte[] result) {
        return QD3DManager.Q3Vector3D_Scale(vector3D, scalar, result);
    }

    private static float Q3Vector3D_Length(byte[] vector3D) {
        return QD3DManager.Q3Vector3D_Length(vector3D);
    }

    private static int Q3Vector3D_Normalize(byte[] vector3D, byte[] result) {
        return QD3DManager.Q3Vector3D_Normalize(vector3D, result);
    }

    private static int Q3Vector3D_Add(byte[] v1, byte[] v2, byte[] result) {
        return QD3DManager.Q3Vector3D_Add(v1, v2, result);
    }

    private static int Q3Vector3D_Subtract(byte[] v1, byte[] v2, byte[] result) {
        return QD3DManager.Q3Vector3D_Subtract(v1, v2, result);
    }

    private static int Q3Vector3D_Cross(byte[] v1, byte[] v2, byte[] result) {
        return QD3DManager.Q3Vector3D_Cross(v1, v2, result);
    }

    private static float Q3Vector3D_Dot(byte[] v1, byte[] v2) {
        return QD3DManager.Q3Vector3D_Dot(v1, v2);
    }

    private static int Q3Vector3D_TransformQuaternion(byte[] vector3D, byte[] quaternion, byte[] result) {
        return QD3DManager.Q3Vector3D_TransformQuaternion(vector3D, quaternion, result);
    }
}
