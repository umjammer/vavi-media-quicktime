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
 * Refer to TQ3Vector2D in QuickDraw3D
 */
public final class Vector2D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 8;

    @Serial
    private static final long serialVersionUID = 8942845454986567012L;

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
     * Creates a Point2D object containing no data.
     */
    public Vector2D() {
        super(kNativeSize);
    }

    /**
     * Creates a Point2D object containing with given data.
     * @param x The final x coordinate.
     * @param y The final y coordinate.
     */
    public Vector2D(float x, float y) {
        this();
        setX(x);
        setY(y);
    }

    private Vector2D(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[8];
        in.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        Vector2D vector2D = (Vector2D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(vector2D, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        out.write(vector2D.getBytes());
    }

    /**
     * returns the position in the x direction
     */
    public float getX() {
        return getFloatAt(0);
    }

    /**
     * returns the position in the y direction
     */
    public float getY() {
        return getFloatAt(4);
    }

    /**
     * sets the position in the x direction
     */
    public void setX(float x) {
        setFloatAt(0, x);
    }

    /**
     * sets the position in the y direction
     */
    public void setY(float y) {
        setFloatAt(4, y);
    }

    /**
     * Returns a two-dimensional vector that is the result of
     * multiplying each of the components of this by the value of
     * the scalar parameter.
     * @see "QuickDraw3D::Q3Vector2D_Scale"
     * @param scalar A floating-point number.
     * @return a new scaled vector.
     */
    public Vector2D scale(float scalar) {
        Vector2D vector2D = new Vector2D();
        int i = Q3Vector2D_Scale(getBytes(), scalar, vector2D.getBytes());
        return vector2D;
    }

    /**
     * Returns the length of a two-dimensional vector.
     * @see "QuickDraw3D::Q3Vector2D_Length"
     * @return the length of the vector.
     */
    public float length() {
        return Q3Vector2D_Length(getBytes());
    }

    /**
     * Returns the normalized form of the vector.
     * @see "QuickDraw3D::Q3Vector2D_Normalize"
     * @return the normalized form of the vector
     */
    public Vector2D normalize() {
        Vector2D vector2D = new Vector2D();
        int i = Q3Vector2D_Normalize(getBytes(), vector2D.getBytes());
        return vector2D;
    }

    /**
     * Returns the two-dimensional vector that is the sum of the two vectors this and v2.
     * @see "QuickDraw3D::Q3Vector2D_Add"
     * @param v2 A two-dimensional vector.
     * @return the sum of v1 and v2.
     */
    public Vector2D add(Vector2D v2) {
        Vector2D vector2D = new Vector2D();
        int i = Q3Vector2D_Add(getBytes(), v2.getBytes(), vector2D.getBytes());
        return vector2D;
    }

    /**
     * Returns the two-dimensional vector that is the result of subtracting v2 from this.
     * @see "QuickDraw3D::Q3Vector2D_Subtract"
     * @param v2 A two-dimensional vector.
     * @return the result of subtracting v2 from this.
     */
    public Vector2D subtract(Vector2D v2) {
        Vector2D vector2D = new Vector2D();
        int i = Q3Vector2D_Subtract(getBytes(), v2.getBytes(), vector2D.getBytes());
        return vector2D;
    }

    /**
     * Returns the cross product of this vector and vector v2.
     * @see "QuickDraw3D::Q3Vector2D_Cross"
     * @param v2 A two-dimensional vector.
     * @return a float
     */
    public float cross(Vector2D v2) {
        return Q3Vector2D_Cross(getBytes(), v2.getBytes());
    }

    /**
     * Returns the dot product of this vector and vector v2.
     * @see "QuickDraw3D::Q3Vector2D_Dot"
     * @param v2 A two-dimensional vector.
     * @return a float
     */
    public float dot(Vector2D v2) {
        return Q3Vector2D_Dot(getBytes(), v2.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new Vector2D(getBytes());
    }

    private static int Q3Vector2D_Scale(byte[] vector2D, float scalar, byte[] result) {
        return QD3DManager.Q3Vector2D_Scale(vector2D, scalar, result);
    }

    private static float Q3Vector2D_Length(byte[] vector2D) {
        return QD3DManager.Q3Vector2D_Length(vector2D);
    }

    private static int Q3Vector2D_Normalize(byte[] vector2D, byte[] result) {
        return QD3DManager.Q3Vector2D_Normalize(vector2D, result);
    }

    private static int Q3Vector2D_Add(byte[] v1, byte[] v2, byte[] result) {
        return QD3DManager.Q3Vector2D_Add(v1, v2, result);
    }

    private static int Q3Vector2D_Subtract(byte[] v1, byte[] v2, byte[] result) {
        return QD3DManager.Q3Vector2D_Subtract(v1, v2, result);
    }

    private static float Q3Vector2D_Cross(byte[] v1, byte[] v2) {
        return QD3DManager.Q3Vector2D_Cross(v1, v2);
    }

    private static float Q3Vector2D_Dot(byte[] v1, byte[] v2) {
        return QD3DManager.Q3Vector2D_Dot(v1, v2);
    }
}
