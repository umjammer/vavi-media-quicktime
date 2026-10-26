package quicktime.qd3d.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.QD3DManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickDraw3DLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;

/**
 * Matrix3x3 contains information for a 3 x 3 float matrix.
 *
 * refer to TQ3Matrix3x3 in QuickDraw3d
 */
public final class Matrix3x3 extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 36;

    static final long serialVersionUID = 6277243717121923106L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Matrix3x3.class);
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
     * Creates a Matrix3x3 object and sets it to the identity matrix.
     */
    public Matrix3x3() {
        super(36);
        setIdentity();
    }

    public Matrix3x3(float[][] paramArrayOfFloat) {
        super(36);
        setAt00(paramArrayOfFloat[0][0]);
        setAt01(paramArrayOfFloat[0][1]);
        setAt02(paramArrayOfFloat[0][2]);
        setAt10(paramArrayOfFloat[1][0]);
        setAt11(paramArrayOfFloat[1][1]);
        setAt12(paramArrayOfFloat[1][2]);
        setAt20(paramArrayOfFloat[2][0]);
        setAt21(paramArrayOfFloat[2][1]);
        setAt22(paramArrayOfFloat[2][2]);
    }

    private Matrix3x3(Object paramObject) {
        super(36);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[36];
        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Matrix3x3 localMatrix3x3 = copy();
        try {
            EndianOrder.flipNativeToBigEndian(localMatrix3x3, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        paramObjectOutputStream.write(localMatrix3x3.getBytes());
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
    public float getAt10() {
        return getFloatAt(12);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt10(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt11() {
        return getFloatAt(16);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt11(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt12() {
        return getFloatAt(20);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt12(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt20() {
        return getFloatAt(24);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt20(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt21() {
        return getFloatAt(28);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt21(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    /**
     * Returns the value at the specified index.
     * @return a float
     */
    public float getAt22() {
        return getFloatAt(32);
    }

    /**
     * Sets the value at the specified index.
     * @param val the new float value
     */
    public void setAt22(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    /**
     * Returns a copy of the matrix.
     * QuickDraw3D::Q3Matrix3x3_Copy
     * @return a copy of matrix3x3.
     */
    public Matrix3x3 copy() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Copy(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    /**
     * Sets the Matrix to the 3-by-3 identity matrix.
     * QuickDraw3D::Q3Matrix3x3_SetIdentity
     */
    public void setIdentity() {
        int i = Q3Matrix3x3_SetIdentity(getBytes());
    }

    /**
     * Returns the transpose of the matrix.
     * QuickDraw3D::Q3Matrix3x3_Transpose
     * @return the transpose matrix
     */
    public Matrix3x3 transpose() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Transpose(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    /**
     * Returns the inverse of the matrix.
     * QuickDraw3D::Q3Matrix3x3_Invert
     * @return the invert matrix
     */
    public Matrix3x3 invert() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Invert(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    /**
     * Returns the adjoint of the matrix.
     * QuickDraw3D::Q3Matrix3x3_Adjoint
     * @return the adjoint matrix
     */
    public Matrix3x3 adjoint() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Adjoint(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    /**
     * Returns the product of the two 3-by-3 matrices, this and matrixB.
     * QuickDraw3D::Q3Matrix3x3_Multiply
     * @param matrixB a matrix
     * @return the product
     */
    public Matrix3x3 multiply(Matrix3x3 paramMatrix3x3) {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Multiply(getBytes(), paramMatrix3x3.getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    /**
     * Returns the determinant of this matrix.
     * QuickDraw3D::Q3Matrix3x3_Determinant
     * @return the determinanta
     */
    public float determinant() {
        return Q3Matrix3x3_Determinant(getBytes());
    }

    /**
     * Translates a matrix by the amount xTrans along the x coordinate axis
     * and by the amount yTrans along the y coordinate axis.
     * QuickDraw3D::Q3Matrix3x3_SetTranslate
     * @param xTrans The desired amount of translation along the x coordinate axis.
     * @param yTrans The desired amount of translation along the y coordinate axis.
     */
    public void setTranslate(float paramFloat1, float paramFloat2) {
        int i = Q3Matrix3x3_SetTranslate(getBytes(), paramFloat1, paramFloat2);
    }

    /**
     * Scales a matrix by the amount xScale along the x coordinate axis
     * and by the amount yScale along the y coordinate axis.
     * QuickDraw3D::Q3Matrix3x3_SetScale
     * @param xScale The desired amount of scaling along the x coordinate axis.
     * @param yScale The desired amount of scaling along the y coordinate axis.
     */
    public void setScale(float paramFloat1, float paramFloat2) {
        int i = Q3Matrix3x3_SetScale(getBytes(), paramFloat1, paramFloat2);
    }

    /**
     * Rotates a matrix by the specified angle around the point origin.
     * QuickDraw3D::Q3Matrix3x3_SetRotateAboutPoint
     * @param origin The desired origin of rotation.
     * @param angle The desired angle of rotation, in radians.
     */
    public void setRotateAboutPoint(QDPoint paramQDPoint, float paramFloat) {
        int i = Q3Matrix3x3_SetRotateAboutPoint(getBytes(), paramQDPoint.getQ3Point2D(), paramFloat);
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        return copy();
    }

    private static int Q3Matrix3x3_Copy(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Matrix3x3_Copy(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Matrix3x3_SetIdentity(byte[] paramArrayOfByte) {
        return QD3DManager.Q3Matrix3x3_SetIdentity(paramArrayOfByte);
    }

    private static int Q3Matrix3x3_Transpose(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Matrix3x3_Transpose(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Matrix3x3_Invert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Matrix3x3_Invert(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Matrix3x3_Adjoint(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        return QD3DManager.Q3Matrix3x3_Adjoint(paramArrayOfByte1, paramArrayOfByte2);
    }

    private static int Q3Matrix3x3_Multiply(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3) {
        return QD3DManager.Q3Matrix3x3_Multiply(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    }

    private static float Q3Matrix3x3_Determinant(byte[] paramArrayOfByte) {
        return QD3DManager.Q3Matrix3x3_Determinant(paramArrayOfByte);
    }

    private static int Q3Matrix3x3_SetTranslate(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2) {
        return QD3DManager.Q3Matrix3x3_SetTranslate(paramArrayOfByte, paramFloat1, paramFloat2);
    }

    private static int Q3Matrix3x3_SetScale(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2) {
        return QD3DManager.Q3Matrix3x3_SetScale(paramArrayOfByte, paramFloat1, paramFloat2);
    }

    private static int Q3Matrix3x3_SetRotateAboutPoint(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, float paramFloat) {
        return QD3DManager.Q3Matrix3x3_SetRotateAboutPoint(paramArrayOfByte1, paramArrayOfByte2, paramFloat);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Matrix3x3.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
