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
 * Matrix4x4 contains information for a 4 x 4 float matrix.
 *
 * refer to Q3Matrix4x4 in QuickDraw3D
 */
public final class Matrix4x4 extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 64;

    @Serial
    private static final long serialVersionUID = -1974342924500452995L;

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
     * Creates a Matrix4x4 object and sets it to the identity matrix.
     */
    public Matrix4x4() {
        super(kNativeSize);
        setIdentity();
    }

    public Matrix4x4(float[][] values) {
        super(kNativeSize);
        setAt00(values[0][0]);
        setAt01(values[0][1]);
        setAt02(values[0][2]);
        setAt03(values[0][3]);
        setAt10(values[1][0]);
        setAt11(values[1][1]);
        setAt12(values[1][2]);
        setAt13(values[1][3]);
        setAt20(values[2][0]);
        setAt21(values[2][1]);
        setAt22(values[2][2]);
        setAt23(values[2][3]);
        setAt30(values[3][0]);
        setAt31(values[3][1]);
        setAt32(values[3][2]);
        setAt33(values[3][3]);
    }

    private Matrix4x4(Object noInit) {
        super(kNativeSize);
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
        Matrix4x4 matrix4x44 = copy();
        try {
            EndianOrder.flipNativeToBigEndian(matrix4x44, 0, getEndianDescriptor());
        } catch (UtilException e) {
            e.printStackTrace();
        }
        out.write(matrix4x44.getBytes());
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
    public void setAt00(float val) {
        setFloatAt(0, val);
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
    public void setAt01(float val) {
        setFloatAt(4, val);
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
    public void setAt02(float val) {
        setFloatAt(8, val);
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
    public void setAt03(float val) {
        setFloatAt(12, val);
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
    public void setAt10(float val) {
        setFloatAt(16, val);
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
    public void setAt11(float val) {
        setFloatAt(20, val);
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
    public void setAt12(float val) {
        setFloatAt(24, val);
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
    public void setAt13(float val) {
        setFloatAt(28, val);
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
    public void setAt20(float val) {
        setFloatAt(32, val);
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
    public void setAt21(float val) {
        setFloatAt(36, val);
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
    public void setAt22(float val) {
        setFloatAt(40, val);
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
    public void setAt23(float val) {
        setFloatAt(44, val);
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
    public void setAt30(float val) {
        setFloatAt(48, val);
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
    public void setAt31(float val) {
        setFloatAt(52, val);
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
    public void setAt32(float val) {
        setFloatAt(56, val);
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
    public void setAt33(float val) {
        setFloatAt(60, val);
    }

    /**
     * Returns a copy of the matrix.
     * @see "QuickDraw3D::Q3Matrix4x4_Copy"
     * @return a copy of matrix3x3.
     */
    public Matrix4x4 copy() {
        Matrix4x4 matrix4x44 = new Matrix4x4(null);
        int i = Q3Matrix4x4_Copy(getBytes(), matrix4x44.getBytes());
        return matrix4x44;
    }

    /**
     * Sets the Matrix to the 4-by-4 identity matrix.
     * @see "QuickDraw3D::Q3Matrix4x4_SetIdentity"
     */
    public void setIdentity() {
        int i = Q3Matrix4x4_SetIdentity(getBytes());
    }

    /**
     * Returns the transpose of the matrix.
     * @see "QuickDraw3D::Q3Matrix4x4_Transpose"
     * @return the transpose matrix
     */
    public Matrix4x4 transpose() {
        Matrix4x4 matrix4x44 = new Matrix4x4(null);
        int i = Q3Matrix4x4_Transpose(getBytes(), matrix4x44.getBytes());
        return matrix4x44;
    }

    /**
     * Returns the inverse of the matrix.
     * @see "QuickDraw3D::Q3Matrix4x4_Invert"
     * @return the invert matrix
     */
    public Matrix4x4 invert() {
        Matrix4x4 matrix4x44 = new Matrix4x4(null);
        int i = Q3Matrix4x4_Invert(getBytes(), matrix4x44.getBytes());
        return matrix4x44;
    }

    /**
     * Returns the product of the two 4-by-4 matrices, this and matrixB.
     * @see "QuickDraw3D::Q3Matrix4x4_Multiply"
     * @param matrixB a matrix
     * @return the product
     */
    public Matrix4x4 multiply(Matrix4x4 matrixB) {
        Matrix4x4 matrix4x44 = new Matrix4x4(null);
        int i = Q3Matrix4x4_Multiply(getBytes(), matrixB.getBytes(), matrix4x44.getBytes());
        return matrix4x44;
    }

    /**
     * Returns the determinant of this matrix.
     * @see "QuickDraw3D::Q3Matrix4x4_Determinant"
     * @return the determinanta
     */
    public float determinant() {
        return Q3Matrix4x4_Determinant(getBytes());
    }

    /**
     * Translates a matrix by the amount xTrans along the x coordinate axis,
     * by the amount yTrans along the y coordinate axis and by the amount zTrans along the z coordinate axis.
     * @see "QuickDraw3D::Q3Matrix4x4_SetTranslate"
     * @param xTrans The desired amount of translation along the x coordinate axis.
     * @param yTrans The desired amount of translation along the y coordinate axis.
     * @param zTrans The desired amount of translation along the z coordinate axis.
     */
    public void setTranslate(float xTrans, float yTrans, float zTrans) {
        int i = Q3Matrix4x4_SetTranslate(getBytes(), xTrans, yTrans, zTrans);
    }

    /**
     * Scales a matrix by the amount xScale along the x coordinate axis,
     * by the amount yScale along the y coordinate axis
     * and by the amount zScale along the z coordinate axis.
     * @see "QuickDraw3D::Q3Matrix4x4_SetScale"
     * @param xScale The desired amount of scaling along the x coordinate axis.
     * @param yScale The desired amount of scaling along the y coordinate axis.
     * @param zScale The desired amount of scaling along the z coordinate axis.
     */
    public void setScale(float xScale, float yScale, float zScale) {
        int i = Q3Matrix4x4_SetScale(getBytes(), xScale, yScale, zScale);
    }

    /**
     * Rotates a matrix by the specified angle around the point origin.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotateAboutPoint"
     * @param origin The desired origin of rotation.
     * @param xAngle The desired angle of rotation around the x component of origin, in radians.
     * @param yAngle The desired angle of rotation around the y component of origin, in radians.
     * @param zAngle The desired angle of rotation around the z component of origin, in radians.
     */
    public void setRotateAboutPoint(Point3D origin, float xAngle, float yAngle, float zAngle) {
        int i = Q3Matrix4x4_SetRotateAboutPoint(getBytes(), origin.getBytes(), xAngle, yAngle, zAngle);
    }

    /**
     * Sets a rotate-about-axis matrix that rotates an object by the specified angle
     * around the axis determined by the point origin and the specified orientation.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotateAboutAxis"
     * @param origin The desired origin of rotation.
     * @param orientation The desired orientation of the axis of rotation.
     * @param angle The desired angle of rotation, in radians.
     */
    public void setRotateAboutAxis(Point3D origin, Vector3D orientation, float angle) {
        int i = Q3Matrix4x4_SetRotateAboutAxis(getBytes(), origin.getBytes(), orientation.getBytes(), angle);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angle around the x axis.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotate_X"
     * @param angle The desired angle of rotation around the x coordinate axis, in radians.
     */
    public void setRotateX(float angle) {
        int i = Q3Matrix4x4_SetRotate_X(getBytes(), angle);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angle around the y axis.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotate_Y"
     * @param angle The desired angle of rotation around the y coordinate axis, in radians.
     */
    public void setRotateY(float angle) {
        int i = Q3Matrix4x4_SetRotate_Y(getBytes(), angle);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angle around the x axis.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotate_Z"
     * @param angle The desired angle of rotation around the z coordinate axis, in radians.
     */
    public void setRotateZ(float angle) {
        int i = Q3Matrix4x4_SetRotate_Z(getBytes(), angle);
    }

    /**
     * Sets a rotational matrix that rotates an object by the specified angles around their
     * specified axis.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotate_XYZ"
     * @param xAngle The desired angle of rotation around the x coordinate axis, in radians.
     * @param yAngle The desired angle of rotation around the y coordinate axis, in radians.
     * @param zAngle The desired angle of rotation around the z coordinate axis, in radians.
     */
    public void setRotateXYZ(float xAngle, float yAngle, float zAngle) {
        int i = Q3Matrix4x4_SetRotate_XYZ(getBytes(), xAngle, yAngle, zAngle);
    }

    /**
     * Sets a rotational matrix that rotates objects
     * around the origin in such a way that the transformed vector v1 matches the
     * vector v2. Both v1 and v2 should be normalized.
     * @see "QuickDraw3D::Q3Matrix4x4_SetRotateVectorToVector"
     * @param v1 A three-dimensional vector.
     * @param v2 A three-dimensional vector.
     */
    public void setRotateVectorToVector(Vector3D v1, Vector3D v2) {
        int i = Q3Matrix4x4_SetRotateVectorToVector(getBytes(), v1.getBytes(), v2.getBytes());
    }

    /**
     * Sets a 4-by-4 matrix that represents the quaternion specified by the quaternion parameter.
     * @see "QuickDraw3D::Q3Matrix4x4_SetQuaternion"
     * @param quaternion A quaternion.
     */
    public void setQuaternion(Quaternion quaternion) {
        int i = Q3Matrix4x4_SetQuaternion(getBytes(), quaternion.getBytes());
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return copy();
    }

    private static int Q3Matrix4x4_Copy(byte[] matrix4x4, byte[] result) {
        return QD3DManager.Q3Matrix4x4_Copy(matrix4x4, result);
    }

    private static int Q3Matrix4x4_SetIdentity(byte[] matrix4x4) {
        return QD3DManager.Q3Matrix4x4_SetIdentity(matrix4x4);
    }

    private static int Q3Matrix4x4_Transpose(byte[] matrix4x4, byte[] result) {
        return QD3DManager.Q3Matrix4x4_Transpose(matrix4x4, result);
    }

    private static int Q3Matrix4x4_Invert(byte[] matrix4x4, byte[] result) {
        return QD3DManager.Q3Matrix4x4_Invert(matrix4x4, result);
    }

    private static int Q3Matrix4x4_Multiply(byte[] matrixA, byte[] matrixB, byte[] result) {
        return QD3DManager.Q3Matrix4x4_Multiply(matrixA, matrixB, result);
    }

    private static float Q3Matrix4x4_Determinant(byte[] matrix4x4) {
        return QD3DManager.Q3Matrix4x4_Determinant(matrix4x4);
    }

    private static int Q3Matrix4x4_SetTranslate(byte[] matrix4x4, float xTrans, float yTrans, float zTrans) {
        return QD3DManager.Q3Matrix4x4_SetTranslate(matrix4x4, xTrans, yTrans, zTrans);
    }

    private static int Q3Matrix4x4_SetScale(byte[] matrix4x4, float xScale, float yScale, float zScale) {
        return QD3DManager.Q3Matrix4x4_SetScale(matrix4x4, xScale, yScale, zScale);
    }

    private static int Q3Matrix4x4_SetRotateAboutPoint(byte[] matrix4x4, byte[] origin, float xAngle, float yAngle, float zAngle) {
        return QD3DManager.Q3Matrix4x4_SetRotateAboutPoint(matrix4x4, origin, xAngle, yAngle, zAngle);
    }

    private static int Q3Matrix4x4_SetRotateAboutAxis(byte[] matrix4x4, byte[] origin, byte[] orientation, float angle) {
        return QD3DManager.Q3Matrix4x4_SetRotateAboutAxis(matrix4x4, origin, orientation, angle);
    }

    private static int Q3Matrix4x4_SetRotate_X(byte[] matrix4x4, float angle) {
        return QD3DManager.Q3Matrix4x4_SetRotate_X(matrix4x4, angle);
    }

    private static int Q3Matrix4x4_SetRotate_Y(byte[] matrix4x4, float angle) {
        return QD3DManager.Q3Matrix4x4_SetRotate_Y(matrix4x4, angle);
    }

    private static int Q3Matrix4x4_SetRotate_Z(byte[] matrix4x4, float angle) {
        return QD3DManager.Q3Matrix4x4_SetRotate_Z(matrix4x4, angle);
    }

    private static int Q3Matrix4x4_SetRotate_XYZ(byte[] matrix4x4, float xAngle, float yAngle, float zAngle) {
        return QD3DManager.Q3Matrix4x4_SetRotate_XYZ(matrix4x4, xAngle, yAngle, zAngle);
    }

    private static int Q3Matrix4x4_SetRotateVectorToVector(byte[] matrix4x4, byte[] v1, byte[] v2) {
        return QD3DManager.Q3Matrix4x4_SetRotateVectorToVector(matrix4x4, v1, v2);
    }

    private static int Q3Matrix4x4_SetQuaternion(byte[] matrix, byte[] quaternion) {
        return QD3DManager.Q3Matrix4x4_SetQuaternion(matrix, quaternion);
    }
}
