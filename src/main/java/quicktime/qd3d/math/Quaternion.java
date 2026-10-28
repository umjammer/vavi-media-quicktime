package quicktime.qd3d.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.MatrixManager;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;


/**
 * Quaternion A quadruple of floating-point numbers that obeys the laws of quaternion arithmetic.
 * A Quaternion transform will rotate and twist an object according to the mathematical properties of quaternions.
 * <p>
 * Refer to TQ3Quaternion in QuickDraw3d
 */
public final class Quaternion extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 16;

    @Serial
    private static final long serialVersionUID = -3634140697957637093L;

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
     * Creates a Quaternion object set to the identity.
     *
     * @see "QuickDraw3D::Q3Quaternion_SetIdentity"
     */
    public Quaternion() {
        super(kNativeSize);
        setIdentity();
    }

    /**
     * Creates a Quaternion object containing given data.
     *
     * @param w The final w value.
     * @param x The final x position.
     * @param y The final y position.
     * @param z The final z position.
     */
    public Quaternion(float w, float x, float y, float z) {
        this();
        setW(w);
        setX(x);
        setY(y);
        setZ(z);
    }

    private Quaternion(Object paramObject) {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Quaternion localQuaternion = (Quaternion) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localQuaternion, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        paramObjectOutputStream.write(localQuaternion.getBytes());
    }

    /**
     * returns the w value
     */
    public float getW() {
        return getFloatAt(0);
    }

    /**
     * returns the position in the x direction
     */
    public float getX() {
        return getFloatAt(4);
    }

    /**
     * returns the position in the y direction
     */
    public float getY() {
        return getFloatAt(8);
    }

    /**
     * returns the position in the z direction
     */
    public float getZ() {
        return getFloatAt(12);
    }

    /**
     * sets the w value
     */
    public void setW(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * sets the position in the x direction
     */
    public void setX(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * sets the position in the y direction
     */
    public void setY(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    /**
     * sets the position in the z direction
     */
    public void setZ(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    /**
     * Returns a copy of the quaternion.
     *
     * @return a copy of quaternion.
     * @see "QuickDraw3D::Q3Quaternion_Copy"
     */
    public Quaternion copy() {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Copy(getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Sets the Quaternion to the identity quaternion.
     *
     * @see "QuickDraw3D::Q3Quaternion_SetIdentity"
     */
    public void setIdentity() {
        int i = Q3Quaternion_SetIdentity(getBytes());
    }

    /**
     * Returns rue if the quaternion is an identity quaternion.
     *
     * @return true if the quaternion is an identity quaternion
     * @see "QuickDraw3D::Q3Quaternion_IsIdentity"
     */
    public boolean isIdentity() {
        return Q3Quaternion_IsIdentity(getBytes()) != 0;
    }

    /**
     * Returns the inverse of the quaternion.
     *
     * @return the invert quaternion
     * @see "QuickDraw3D::Q3Quaternion_Invert"
     */
    public Quaternion invert() {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Invert(getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Returns the product of the two quaternions, this and q2.
     *
     * @param q2 a quaternion
     * @return the product
     * @see "QuickDraw3D::Q3Quaternion_Multiply"
     */
    public Quaternion multiply(Quaternion q2) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Multiply(getBytes(), q2.getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Returns a normalized the quaternion.
     *
     * @return the normalized quaternion
     * @see "QuickDraw3D::Q3Quaternion_Normalize"
     */
    public Quaternion normalize() {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Normalize(getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Returns the dot product of two quaternions, this and q2.
     *
     * @param q2 A quaternion.
     * @return the dot product
     * @see "QuickDraw3D::Q3Quaternion_Dot"
     */
    public float dot(Quaternion q2) {
        return Q3Quaternion_Dot(getBytes(), q2.getBytes());
    }

    /**
     * Sets a a rotate-about-axis quaternion that rotates an
     * object by the angle angle around the axis specified by the axis parameter.
     *
     * @param axis  The desired axis of rotation.
     * @param angle The desired angle of rotation, in radians.
     * @see "QuickDraw3D::Q3Quaternion_SetRotateAboutAxis"
     */
    public void setRotateAboutAxis(Vector3D axis, float angle) {
        int i = Q3Quaternion_SetRotateAboutAxis(getBytes(), axis.getBytes(), angle);
    }

    /**
     * Sets a rotational quaternion that rotates an object by the specified angle around the x axis.
     *
     * @param angle The desired angle of rotation around the x coordinate axis, in radians.
     * @see "QuickDraw3D::Q3Quaternion_SetRotate_X"
     */
    public void setRotateX(float angle) {
        int i = Q3Quaternion_SetRotate_X(getBytes(), angle);
    }

    /**
     * Sets a rotational quaternion that rotates an object by the specified angle around the y axis.
     *
     * @param angle The desired angle of rotation around the y coordinate axis, in radians.
     * @see "QuickDraw3D::Q3Quaternion_SetRotate_Y"
     */
    public void setRotateY(float angle) {
        int i = Q3Quaternion_SetRotate_Y(getBytes(), angle);
    }

    /**
     * Sets a rotational quaternion that rotates an object by the specified angle around the x axis.
     *
     * @param angle The desired angle of rotation around the z coordinate axis, in radians.
     * @see "QuickDraw3D::Q3Quaternion_SetRotate_Z"
     */
    public void setRotateZ(float angle) {
        int i = Q3Quaternion_SetRotate_Z(getBytes(), angle);
    }

    /**
     * Sets a rotational quaternion that rotates an object by the specified angles around their
     * specified axis.
     *
     * @param xAngle The desired angle of rotation around the x coordinate axis, in radians.
     * @param yAngle The desired angle of rotation around the y coordinate axis, in radians.
     * @param zAngle The desired angle of rotation around the z coordinate axis, in radians.
     * @see "QuickDraw3D::Q3Quaternion_SetRotate_XYZ"
     */
    public void setRotateXYZ(float xAngle, float yAngle, float zAngle) {
        int i = Q3Quaternion_SetRotate_XYZ(getBytes(), xAngle, yAngle, zAngle);
    }

    /**
     * Sets a quaternion that has the same transformational properties as the matrix
     * specified by the matrix parameter.
     *
     * @param matrix A 4-by-by matrix.
     * @see "QuickDraw3D::Q3Quaternion_SetMatrix"
     */
    public void setMatrix(Matrix4x4 matrix) {
        int i = Q3Quaternion_SetMatrix(getBytes(), matrix.getBytes());
    }

    /**
     * Sets a rotational quaternion that rotates objects
     * around the origin in such a way that the transformed vector v1 matches the
     * vector v2. Both v1 and v2 should be normalized.
     *
     * @param v1 A three-dimensional vector.
     * @param v2 A three-dimensional vector.
     * @see "QuickDraw3D::Q3Quaternion_SetRotateVectorToVector"
     */
    public void setRotateVectorToVector(Vector3D v1, Vector3D v2) {
        int i = Q3Quaternion_SetRotateVectorToVector(getBytes(), v1.getBytes(), v2.getBytes());
    }

    /**
     * Returns a quaternion that is either identical to the quaternion
     * specified by the q1 parameter or is the negative of q1, depending on whether q1
     * or its negative matches the orientation of the quaternion specified by the q2
     * parameter.
     *
     * @param q2 A quaternion.
     * @return a quaternion that is either q1 or the negative of q1, and that matches the orientation of q2.
     * @see "QuickDraw3D::Q3Quaternion_MatchReflection"
     */
    public Quaternion matchReflection(Quaternion q2) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_MatchReflection(getBytes(), q2.getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Return a a quaternion that is a fast interpolation between the this and the
     * specified quaternion.
     *
     * @param q2 A quaternion.
     * @param t  An interpolation factor. This parameter should contain a value between 0.0 and 1.0.
     * @return a quaternion
     * @see "QuickDraw3D::Q3Quaternion_InterpolateFast"
     */
    public Quaternion interpolateFast(Quaternion q2, float t) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_InterpolateFast(getBytes(), q2.getBytes(), t, localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Return a a quaternion that is a linear interpolation between the this and the
     * specified quaternion.
     *
     * @param q2 A quaternion.
     * @param t  An interpolation factor. This parameter should contain a value between 0.0 and 1.0.
     * @return a quaternion
     * @see "QuickDraw3D::Q3Quaternion_InterpolateLinear"
     */
    public Quaternion interpolateLinear(Quaternion q2, float t) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_InterpolateLinear(getBytes(), q2.getBytes(), t, localQuaternion.getBytes());
        return localQuaternion;
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return copy();
    }

    private static int Q3Quaternion_Copy(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Quaternion_Copy(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Quaternion_SetIdentity(byte[] paramArrayOfByte) {
        return QD3DManager.Q3Quaternion_SetIdentity(paramArrayOfByte);
    }

    private static int Q3Quaternion_IsIdentity(byte[] paramArrayOfByte) {
        return QD3DManager.Q3Quaternion_IsIdentity(paramArrayOfByte);
    }

    private static int Q3Quaternion_Invert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Quaternion_Invert(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Quaternion_Multiply(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Quaternion_Multiply(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int Q3Quaternion_Normalize(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Quaternion_Normalize(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static float Q3Quaternion_Dot(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Quaternion_Dot(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Quaternion_SetRotateAboutAxis(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat) {
        return QD3DManager.Q3Quaternion_SetRotateAboutAxis(paramArrayOfByte1, paramArrayOfByte2, paramFloat);
    }

    private static int Q3Quaternion_SetRotate_X(byte[] paramArrayOfByte, float paramFloat) {
        return QD3DManager.Q3Quaternion_SetRotate_X(paramArrayOfByte, paramFloat);
    }

    private static int Q3Quaternion_SetRotate_Y(byte[] paramArrayOfByte, float paramFloat) {
        return QD3DManager.Q3Quaternion_SetRotate_Y(paramArrayOfByte, paramFloat);
    }

    private static int Q3Quaternion_SetRotate_Z(byte[] paramArrayOfByte, float paramFloat) {
        return QD3DManager.Q3Quaternion_SetRotate_Z(paramArrayOfByte, paramFloat);
    }

    private static int Q3Quaternion_SetRotate_XYZ(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3) {
        return QD3DManager.Q3Quaternion_SetRotate_XYZ(paramArrayOfByte, paramFloat1, paramFloat2, paramFloat3);
    }

    private static int Q3Quaternion_SetMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return MatrixManager.Q3Quaternion_SetMatrix(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Quaternion_SetRotateVectorToVector(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Quaternion_SetRotateVectorToVector(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int Q3Quaternion_MatchReflection(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Quaternion_MatchReflection(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static int Q3Quaternion_InterpolateFast(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Quaternion_InterpolateFast(paramArrayOfByte1, paramArrayOfByte2, paramFloat, paramArrayOfByte3);
    }

    private static int Q3Quaternion_InterpolateLinear(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Quaternion_InterpolateLinear(paramArrayOfByte1, paramArrayOfByte2, paramFloat, paramArrayOfByte3);
    }
}
