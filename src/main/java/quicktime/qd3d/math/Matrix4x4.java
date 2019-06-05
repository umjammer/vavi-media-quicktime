
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


public final class Matrix4x4 extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {
    private static Object linkage;

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

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

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

    public float getAt03() {
        return getFloatAt(12);
    }

    public void setAt03(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    public float getAt10() {
        return getFloatAt(16);
    }

    public void setAt10(float paramFloat) {
        setFloatAt(16, paramFloat);
    }

    public float getAt11() {
        return getFloatAt(20);
    }

    public void setAt11(float paramFloat) {
        setFloatAt(20, paramFloat);
    }

    public float getAt12() {
        return getFloatAt(24);
    }

    public void setAt12(float paramFloat) {
        setFloatAt(24, paramFloat);
    }

    public float getAt13() {
        return getFloatAt(28);
    }

    public void setAt13(float paramFloat) {
        setFloatAt(28, paramFloat);
    }

    public float getAt20() {
        return getFloatAt(32);
    }

    public void setAt20(float paramFloat) {
        setFloatAt(32, paramFloat);
    }

    public float getAt21() {
        return getFloatAt(36);
    }

    public void setAt21(float paramFloat) {
        setFloatAt(36, paramFloat);
    }

    public float getAt22() {
        return getFloatAt(40);
    }

    public void setAt22(float paramFloat) {
        setFloatAt(40, paramFloat);
    }

    public float getAt23() {
        return getFloatAt(44);
    }

    public void setAt23(float paramFloat) {
        setFloatAt(44, paramFloat);
    }

    public float getAt30() {
        return getFloatAt(48);
    }

    public void setAt30(float paramFloat) {
        setFloatAt(48, paramFloat);
    }

    public float getAt31() {
        return getFloatAt(52);
    }

    public void setAt31(float paramFloat) {
        setFloatAt(52, paramFloat);
    }

    public float getAt32() {
        return getFloatAt(56);
    }

    public void setAt32(float paramFloat) {
        setFloatAt(56, paramFloat);
    }

    public float getAt33() {
        return getFloatAt(60);
    }

    public void setAt33(float paramFloat) {
        setFloatAt(60, paramFloat);
    }

    public Matrix4x4 copy() {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Copy(getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    public void setIdentity() {
        int i = Q3Matrix4x4_SetIdentity(getBytes());
    }

    public Matrix4x4 transpose() {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Transpose(getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    public Matrix4x4 invert() {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Invert(getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    public Matrix4x4 multiply(Matrix4x4 paramMatrix4x4) {
        Matrix4x4 localMatrix4x4 = new Matrix4x4((float[][]) null);
        int i = Q3Matrix4x4_Multiply(getBytes(), paramMatrix4x4.getBytes(), localMatrix4x4.getBytes());
        return localMatrix4x4;
    }

    public float determinant() {
        return Q3Matrix4x4_Determinant(getBytes());
    }

    public void setTranslate(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetTranslate(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    public void setScale(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetScale(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    public void setRotateAboutPoint(Point3D paramPoint3D, float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetRotateAboutPoint(getBytes(), paramPoint3D.getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    public void setRotateAboutAxis(Point3D paramPoint3D, Vector3D paramVector3D, float paramFloat) {
        int i = Q3Matrix4x4_SetRotateAboutAxis(getBytes(), paramPoint3D.getBytes(), paramVector3D.getBytes(), paramFloat);
    }

    public void setRotateX(float paramFloat) {
        int i = Q3Matrix4x4_SetRotate_X(getBytes(), paramFloat);
    }

    public void setRotateY(float paramFloat) {
        int i = Q3Matrix4x4_SetRotate_Y(getBytes(), paramFloat);
    }

    public void setRotateZ(float paramFloat) {
        int i = Q3Matrix4x4_SetRotate_Z(getBytes(), paramFloat);
    }

    public void setRotateXYZ(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Matrix4x4_SetRotate_XYZ(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    public void setRotateVectorToVector(Vector3D paramVector3D1, Vector3D paramVector3D2) {
        int i = Q3Matrix4x4_SetRotateVectorToVector(getBytes(), paramVector3D1.getBytes(), paramVector3D2.getBytes());
    }

    public void setQuaternion(Quaternion paramQuaternion) {
        int i = Q3Matrix4x4_SetQuaternion(getBytes(), paramQuaternion.getBytes());
    }

    public Object clone() {
        return copy();
    }

    private static native int Q3Matrix4x4_Copy(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix4x4_SetIdentity(byte[] paramArrayOfByte);

    private static native int Q3Matrix4x4_Transpose(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix4x4_Invert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Matrix4x4_Multiply(byte[] paramArrayOfByte1,
                                                   byte[] paramArrayOfByte2,
                                                   byte[] paramArrayOfByte3);

    private static native float Q3Matrix4x4_Determinant(byte[] paramArrayOfByte);

    private static native int Q3Matrix4x4_SetTranslate(byte[] paramArrayOfByte,
                                                       float paramFloat1,
                                                       float paramFloat2,
                                                       float paramFloat3);

    private static native int Q3Matrix4x4_SetScale(byte[] paramArrayOfByte,
                                                   float paramFloat1,
                                                   float paramFloat2,
                                                   float paramFloat3);

    private static native int Q3Matrix4x4_SetRotateAboutPoint(byte[] paramArrayOfByte1,
                                                              byte[] paramArrayOfByte2,
                                                              float paramFloat1,
                                                              float paramFloat2,
                                                              float paramFloat3);

    private static native int Q3Matrix4x4_SetRotateAboutAxis(byte[] paramArrayOfByte1,
                                                             byte[] paramArrayOfByte2,
                                                             byte[] paramArrayOfByte3,
                                                             float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_X(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_Y(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_Z(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Matrix4x4_SetRotate_XYZ(byte[] paramArrayOfByte,
                                                        float paramFloat1,
                                                        float paramFloat2,
                                                        float paramFloat3);

    private static native int Q3Matrix4x4_SetRotateVectorToVector(byte[] paramArrayOfByte1,
                                                                  byte[] paramArrayOfByte2,
                                                                  byte[] paramArrayOfByte3);

    private static native int Q3Matrix4x4_SetQuaternion(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Matrix4x4.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
