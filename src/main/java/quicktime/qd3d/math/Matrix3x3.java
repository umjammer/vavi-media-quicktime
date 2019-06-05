
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
import quicktime.qd.QDPoint;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.UtilException;


public final class Matrix3x3 extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {
    private static Object linkage;

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

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

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

    public float getAt00() {
        return getFloatAt(0);
    }

    public void setAt00(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public float getAt01() {
        return getFloatAt(4);
    }

    public void setAt01(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public float getAt02() {
        return getFloatAt(8);
    }

    public void setAt02(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    public float getAt10() {
        return getFloatAt(12);
    }

    public void setAt10(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    public float getAt11() {
        return getFloatAt(16);
    }

    public void setAt11(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    public float getAt12() {
        return getFloatAt(20);
    }

    public void setAt12(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    public float getAt20() {
        return getFloatAt(24);
    }

    public void setAt20(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    public float getAt21() {
        return getFloatAt(28);
    }

    public void setAt21(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    public float getAt22() {
        return getFloatAt(32);
    }

    public void setAt22(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    public Matrix3x3 copy() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Copy(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    public void setIdentity() {
        int i = Q3Matrix3x3_SetIdentity(getBytes());
    }

    public Matrix3x3 transpose() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Transpose(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    public Matrix3x3 invert() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Invert(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    public Matrix3x3 adjoint() {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Adjoint(getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    public Matrix3x3 multiply(Matrix3x3 paramMatrix3x3) {
        Matrix3x3 localMatrix3x3 = new Matrix3x3((float[][]) null);
        int i = Q3Matrix3x3_Multiply(getBytes(), paramMatrix3x3.getBytes(), localMatrix3x3.getBytes());
        return localMatrix3x3;
    }

    public float determinant() {
        return Q3Matrix3x3_Determinant(getBytes());
    }

    public void setTranslate(float paramFloat1, float paramFloat2) {
        int i = Q3Matrix3x3_SetTranslate(getBytes(), paramFloat1, paramFloat2);
    }

    public void setScale(float paramFloat1, float paramFloat2) {
        int i = Q3Matrix3x3_SetScale(getBytes(), paramFloat1, paramFloat2);
    }

    public void setRotateAboutPoint(QDPoint paramQDPoint, float paramFloat) {
        int i = Q3Matrix3x3_SetRotateAboutPoint(getBytes(), paramQDPoint.getQ3Point2D(), paramFloat);
    }

    public Object clone() {
        return copy();
    }

    private static native int Q3Matrix3x3_Copy(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix3x3_SetIdentity(byte[] paramArrayOfByte);

    private static native int Q3Matrix3x3_Transpose(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix3x3_Invert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix3x3_Adjoint(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix3x3_Multiply(byte[] paramArrayOfByte1,
                                                   byte[] paramArrayOfByte2,
                                                   byte[] paramArrayOfByte3);

    private static native float Q3Matrix3x3_Determinant(byte[] paramArrayOfByte);

    private static native int Q3Matrix3x3_SetTranslate(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2);

    private static native int Q3Matrix3x3_SetScale(byte[] paramArrayOfByte, float paramFloat1, float paramFloat2);

    private static native int Q3Matrix3x3_SetRotateAboutPoint(byte[] paramArrayOfByte1,
                                                              byte[] paramArrayOfByte2,
                                                              float paramFloat);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Matrix3x3.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
