
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


public final class Quaternion extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 16;

    static final long serialVersionUID = -3634140697957637093L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }

        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Quaternion.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    public Quaternion() {
        super(16);
        setIdentity();
    }

    public Quaternion(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this();

        setW(paramFloat1);
        setX(paramFloat2);
        setY(paramFloat3);
        setZ(paramFloat4);
    }

    private Quaternion(Object paramObject) {
        super(16);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[16];

        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Quaternion localQuaternion = (Quaternion) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localQuaternion, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        paramObjectOutputStream.write(localQuaternion.getBytes());
    }

    public float getW() {
        return getFloatAt(0);
    }

    public float getX() {
        return getFloatAt(4);
    }

    public float getY() {
        return getFloatAt(8);
    }

    public float getZ() {
        return getFloatAt(12);
    }

    public void setW(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public void setX(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public void setY(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    public void setZ(float paramFloat) {
        setFloatAt(12, paramFloat);
    }

    public Quaternion copy() {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Copy(getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    public void setIdentity() {
        int i = Q3Quaternion_SetIdentity(getBytes());
    }

    public boolean isIdentity() {
        return Q3Quaternion_IsIdentity(getBytes()) != 0;
    }

    public Quaternion invert() {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Invert(getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    public Quaternion multiply(Quaternion paramQuaternion) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Multiply(getBytes(), paramQuaternion.getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    public Quaternion normalize() {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_Normalize(getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    public float dot(Quaternion paramQuaternion) {
        return Q3Quaternion_Dot(getBytes(), paramQuaternion.getBytes());
    }

    public void setRotateAboutAxis(Vector3D paramVector3D, float paramFloat) {
        int i = Q3Quaternion_SetRotateAboutAxis(getBytes(), paramVector3D.getBytes(), paramFloat);
    }

    public void setRotateX(float paramFloat) {
        int i = Q3Quaternion_SetRotate_X(getBytes(), paramFloat);
    }

    public void setRotateY(float paramFloat) {
        int i = Q3Quaternion_SetRotate_Y(getBytes(), paramFloat);
    }

    public void setRotateZ(float paramFloat) {
        int i = Q3Quaternion_SetRotate_Z(getBytes(), paramFloat);
    }

    public void setRotateXYZ(float paramFloat1, float paramFloat2, float paramFloat3) {
        int i = Q3Quaternion_SetRotate_XYZ(getBytes(), paramFloat1, paramFloat2, paramFloat3);
    }

    public void setMatrix(Matrix4x4 paramMatrix4x4) {
        int i = Q3Quaternion_SetMatrix(getBytes(), paramMatrix4x4.getBytes());
    }

    public void setRotateVectorToVector(Vector3D paramVector3D1, Vector3D paramVector3D2) {
        int i = Q3Quaternion_SetRotateVectorToVector(getBytes(), paramVector3D1.getBytes(), paramVector3D2.getBytes());
    }

    public Quaternion matchReflection(Quaternion paramQuaternion) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_MatchReflection(getBytes(), paramQuaternion.getBytes(), localQuaternion.getBytes());
        return localQuaternion;
    }

    public Quaternion interpolateFast(Quaternion paramQuaternion, float paramFloat) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_InterpolateFast(getBytes(), paramQuaternion.getBytes(), paramFloat, localQuaternion.getBytes());
        return localQuaternion;
    }

    public Quaternion interpolateLinear(Quaternion paramQuaternion, float paramFloat) {
        Quaternion localQuaternion = new Quaternion(null);
        int i = Q3Quaternion_InterpolateLinear(getBytes(), paramQuaternion.getBytes(), paramFloat, localQuaternion.getBytes());
        return localQuaternion;
    }

    public Object clone() {
        return copy();
    }

    private static native int Q3Quaternion_Copy(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Quaternion_SetIdentity(byte[] paramArrayOfByte);

    private static native int Q3Quaternion_IsIdentity(byte[] paramArrayOfByte);

    private static native int Q3Quaternion_Invert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Quaternion_Multiply(byte[] paramArrayOfByte1,
                                                    byte[] paramArrayOfByte2,
                                                    byte[] paramArrayOfByte3);

    private static native int Q3Quaternion_Normalize(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native float Q3Quaternion_Dot(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Quaternion_SetRotateAboutAxis(byte[] paramArrayOfByte1,
                                                              byte[] paramArrayOfByte2,
                                                              float paramFloat);

    private static native int Q3Quaternion_SetRotate_X(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Quaternion_SetRotate_Y(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Quaternion_SetRotate_Z(byte[] paramArrayOfByte, float paramFloat);

    private static native int Q3Quaternion_SetRotate_XYZ(byte[] paramArrayOfByte,
                                                         float paramFloat1,
                                                         float paramFloat2,
                                                         float paramFloat3);

    private static native int Q3Quaternion_SetMatrix(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Quaternion_SetRotateVectorToVector(byte[] paramArrayOfByte1,
                                                                   byte[] paramArrayOfByte2,
                                                                   byte[] paramArrayOfByte3);

    private static native int Q3Quaternion_MatchReflection(byte[] paramArrayOfByte1,
                                                           byte[] paramArrayOfByte2,
                                                           byte[] paramArrayOfByte3);

    private static native int Q3Quaternion_InterpolateFast(byte[] paramArrayOfByte1,
                                                           byte[] paramArrayOfByte2,
                                                           float paramFloat,
                                                           byte[] paramArrayOfByte3);

    private static native int Q3Quaternion_InterpolateLinear(byte[] paramArrayOfByte1,
                                                             byte[] paramArrayOfByte2,
                                                             float paramFloat,
                                                             byte[] paramArrayOfByte3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd3d/math
 * /Quaternion.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
