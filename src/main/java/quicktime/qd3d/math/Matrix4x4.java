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

/**
 * Matrix4x4 contains information for a 4 x 4 float matrix.
 *
 * refer to Q3Matrix4x4 in QuickDraw3D
 */
public final class Matrix4x4 extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 64;

    static final long serialVersionUID = -1974342924500452995L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Matrix4x4.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates a Matrix4x4 object and sets it to the identity matrix.
     */
    public Matrix4x4() {
        super(64);
        setIdentity();
    }

    public Matrix4x4(float[][] paramArrayOfFloat) {
        super(64);
        setAt00(paramArrayOfFloat[0][0]);
        setAt01(paramArrayOfFloat[0][1]);
        setAt02(paramArrayOfFloat[0][2]);
        setAt03(paramArrayOfFloat[0][3]);
        setAt10(paramArrayOfFloat[1][0]);
        setAt11(paramArrayOfFloat[1][1]);
        setAt12(paramArrayOfFloat[1][2]);
        setAt13(paramArrayOfFloat[1][3]);
        setAt20(paramArrayOfFloat[2][0]);
        setAt21(paramArrayOfFloat[2][1]);
        setAt22(paramArrayOfFloat[2][2]);
        setAt23(paramArrayOfFloat[2][3]);
        setAt30(paramArrayOfFloat[3][0]);
        setAt31(paramArrayOfFloat[3][1]);
        setAt32(paramArrayOfFloat[3][2]);
        setAt33(paramArrayOfFloat[3][3]);
    }

    private Matrix4x4(Object paramObject) {
        super(64);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[64];
        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Matrix4x4 localMatrix4x4 = copy();
        try {
            EndianOrder.flipNativeToBigEndian(localMatrix4x4, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        paramObjectOutputStream.write(localMatrix4x4.getBytes());
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt00() {
        return getFloatAt(0);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt00(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt01() {
        return getFloatAt(4);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt01(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt02() {
        return getFloatAt(8);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt02(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt03() {
        return getFloatAt(12);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt03(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt10() {
        return getFloatAt(16);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt10(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt11() {
        return getFloatAt(20);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt11(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt12() {
        return getFloatAt(24);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt12(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt13() {
        return getFloatAt(28);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt13(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt20() {
        return getFloatAt(32);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt20(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt21() {
        return getFloatAt(36);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt21(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt22() {
        return getFloatAt(40);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt22(float paramFloat) {
        setFloatAt(40, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt23() {
        return getFloatAt(44);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt23(float paramFloat) {
        setFloatAt(44, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt30() {
        return getFloatAt(48);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt30(float paramFloat) {
        setFloatAt(48, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt31() {
        return getFloatAt(52);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt31(float paramFloat) {
        setFloatAt(52, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt32() {
        return getFloatAt(56);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt32(float paramFloat) {
        setFloatAt(56, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt33() {
        return getFloatAt(60);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt33(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    /**
     * Returns a copy of the matrix.
     * QuickDraw3D::Q3Matrix4x4_Copy
     * @return a copy of matrix3x3.
     */
    public Matrix4x4 copy() {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Copy(getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    /**
     * Sets the Matrix to the 4-by-4 identity matrix.
     * QuickDraw3D::Q3Matrix4x4_SetIdentity
     */
    public void setIdentity() {
        int i = Q3Matrix4x4_SetIdentity(getBytes());
    }

    /**
     * Returns the transpose of the matrix.
     * QuickDraw3D::Q3Matrix4x4_Transpose
     * @return the transpose matrix
     */
    public Matrix4x4 transpose() {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Transpose(getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    /**
     * Returns the inverse of the matrix.
     * QuickDraw3D::Q3Matrix4x4_Invert
     * @return the invert matrix
     */
    public Matrix4x4 invert() {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Invert(getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    /**
     * Returns the product of the two 4-by-4 matrices, this and matrixB.
     * QuickDraw3D::Q3Matrix4x4_Multiply
     * @param matrixB a matrix
     * @return the product
     */
    public Matrix4x4 multiply(Matrix4x4 paramMatrix4x4) {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Multiply(getBytes(), paramMatrix4x4.getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    /**
     * Returns the determinant of this matrix.
     * QuickDraw3D::Q3Matrix4x4_Determinant
     * @return the determinanta
     */
    public float determinant() {
        return Q3Matrix4x4_Determinant(getBytes());
    }

    /**
     * Translates a matrix by the amount xTrans along the x coordinate axis,
     * by the amount yTrans along the y coordinate axis and by the amount zTrans along the z coordinate axis.
     * QuickDraw3D::Q3Matrix4x4_SetTranslate
     * @param xTrans The desired amount of translation along the x coordinate axis.
     * @param yTrans The desired amount of translation along the y coordinate axis.
     * @param zTrans The desired amount of translation along the z coordinate axis.
     */
    public void setTranslate(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetTranslate(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    /**
     * Scales a matrix by the amount xScale along the x coordinate axis,
     * by the amount yScale along the y coordinate axis
     * and by the amount zScale along the z coordinate axis.
     * QuickDraw3D::Q3Matrix4x4_SetScale
     * @param xScale The desired amount of scaling along the x coordinate axis.
     * @param yScale The desired amount of scaling along the y coordinate axis.
     * @param zScale The desired amount of scaling along the z coordinate axis.
     */
    public void setScale(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetScale(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    /**
     * Rotates a matrix by the specified angle around the point origin.
     * QuickDraw3D::Q3Matrix4x4_SetRotateAboutPoint
     * @param origin The desired origin of rotation.
     * @param xAngle The desired angle of rotation around the x component of origin, in radians.
     * @param yAngle The desired angle of rotation around the y component of origin, in radians.
     * @param zAngle The desired angle of rotation around the z component of origin, in radians.
     */
    public void setRotateAboutPoint(Point3D paramPoint3D, float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetRotateAboutPoint(getBytes(), paramPoint3D.getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    /**
     * Sets a rotate-about-axis matrix that rotates an object by the specified angle
     * around the axis determined by the point origin and the specified orientation.
     * QuickDraw3D::Q3Matrix4x4_SetRotateAboutAxis
     * @param origin The desired origin of rotation.
     * @param orientation The desired orientation of the axis of rotation.
     * @param angle The desired angle of rotation, in radians.
     */
    public void setRotateAboutAxis(Point3D paramPoint3D, Vector3D paramVector3D, float paramFloat) {
        int i = Q3Matrix4x4_SetRotateAboutAxis(getBytes(), paramPoint3D.getBytes(), paramVector3D.getBytes(), paramFloat);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angle around the x axis.
     * QuickDraw3D::Q3Matrix4x4_SetRotate_X
     * @param angle The desired angle of rotation around the x coordinate axis, in radians.
     */
    public void setRotateX(float paramFloat) {
        int i = Q3Matrix4x4_SetRotate_X(getBytes(), paramFloat);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angle around the y axis.
     * QuickDraw3D::Q3Matrix4x4_SetRotate_Y
     * @param angle The desired angle of rotation around the y coordinate axis, in radians.
     */
    public void setRotateY(float paramFloat) {
        int i = Q3Matrix4x4_SetRotate_Y(getBytes(), paramFloat);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angle around the x axis.
     * QuickDraw3D::Q3Matrix4x4_SetRotate_Z
     * @param angle The desired angle of rotation around the z coordinate axis, in radians.
     */
    public void setRotateZ(float paramFloat) {
        int i = Q3Matrix4x4_SetRotate_Z(getBytes(), paramFloat);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angles around their
     * specified axis.
     * QuickDraw3D::Q3Matrix4x4_SetRotate_XYZ
     * @param xAngle The desired angle of rotation around the x coordinate axis, in radians.
     * @param yAngle The desired angle of rotation around the y coordinate axis, in radians.
     * @param zAngle The desired angle of rotation around the z coordinate axis, in radians.
     */
    public void setRotateXYZ(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetRotate_XYZ(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    /**
     * Sets a rotational matrix that rotates objects
     * around the origin in such a way that the transformed vector v1 matches the
     * vector v2. Both v1 and v2 should be normalized.
     * QuickDraw3D::Q3Matrix4x4_SetRotateVectorToVector
     * @param v1 A three-dimensional vector.
     * @param v2 A three-dimensional vector.
     */
    public void setRotateVectorToVector(Vector3D paramVector3D1, Vector3D paramVector3D2) {
        int i = Q3Matrix4x4_SetRotateVectorToVector(getBytes(), paramVector3D1.getBytes(), paramVector3D2.getBytes());
    }

    /**
     * Sets a 4-by-4 matrix that represents the quaternion specified by the quaternion parameter.
     * QuickDraw3D::Q3Matrix4x4_SetQuaternion
     * @param quaternion A quaternion.
     */
    public void setQuaternion(Quaternion paramQuaternion) {
        int i = Q3Matrix4x4_SetQuaternion(getBytes(), paramQuaternion.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        return copy();
    }

    private static native int Q3Matrix4x4_Copy(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix4x4_SetIdentity(byte[] paramArrayOfByte);

    private static native int Q3Matrix4x4_Transpose(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix4x4_Invert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix4x4_Multiply(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native float Q3Matrix4x4_Determinant(byte[] paramArrayOfByte);

    private static native int Q3Matrix4x4_SetTranslate(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3);

    private static native int Q3Matrix4x4_SetScale(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3);

    private static native int Q3Matrix4x4_SetRotateAboutPoint(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat1, float paramFloat2, float paramFloat3);

    private static native int Q3Matrix4x4_SetRotateAboutAxis(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_X(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_Y(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_Z(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_XYZ(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2, float paramFloat3);

    private static native int Q3Matrix4x4_SetRotateVectorToVector(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native int Q3Matrix4x4_SetQuaternion(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Matrix4x4.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
