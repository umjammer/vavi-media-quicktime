
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


public final class Vector2D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 8;

    static final long serialVersionUID = 8942845454986567012L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }

        class PrivelegedAction {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Vector2D.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    public Vector2D() {
        super(kNativeSize);
    }

    public Vector2D(float paramFloat1, float paramFloat2) {
        this();

        setX(paramFloat1);
        setY(paramFloat2);
    }

    private Vector2D(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];

        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Vector2D localVector2D = (Vector2D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localVector2D, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }

        paramObjectOutputStream.write(localVector2D.getBytes());
    }

    public float getX() {
        return getFloatAt(0);
    }

    public float getY() {
        return getFloatAt(4);
    }

    public void setX(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    public void setY(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    public Vector2D scale(float paramFloat) {
        Vector2D localVector2D = new Vector2D();
        int i = Q3Vector2D_Scale(getBytes(), paramFloat, localVector2D.getBytes());
        return localVector2D;
    }

    public float length() {
        return Q3Vector2D_Length(getBytes());
    }

    public Vector2D normalize() {
        Vector2D localVector2D = new Vector2D();
        int i = Q3Vector2D_Normalize(getBytes(), localVector2D.getBytes());
        return localVector2D;
    }

    public Vector2D add(Vector2D paramVector2D) {
        Vector2D localVector2D = new Vector2D();
        int i = Q3Vector2D_Add(getBytes(), paramVector2D.getBytes(), localVector2D.getBytes());
        return localVector2D;
    }

    public Vector2D subtract(Vector2D paramVector2D) {
        Vector2D localVector2D = new Vector2D();
        int i = Q3Vector2D_Subtract(getBytes(), paramVector2D.getBytes(), localVector2D.getBytes());
        return localVector2D;
    }

    public float cross(Vector2D paramVector2D) {
        return Q3Vector2D_Cross(getBytes(), paramVector2D.getBytes());
    }

    public float dot(Vector2D paramVector2D) {
        return Q3Vector2D_Dot(getBytes(), paramVector2D.getBytes());
    }

    public Object clone() {
        return new Vector2D(getBytes());
    }

    private static native int Q3Vector2D_Scale(byte[] paramArrayOfByte1, float paramFloat, byte[] paramArrayOfByte2);

    private static native float Q3Vector2D_Length(byte[] paramArrayOfByte);

    private static native int Q3Vector2D_Normalize(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Vector2D_Add(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native int Q3Vector2D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native float Q3Vector2D_Cross(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native float Q3Vector2D_Dot(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
}
