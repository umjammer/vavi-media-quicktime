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
 * Vector3D contains information for three dimensional vectors.
 *
 * refer to TQ3Vector3D in QuickDraw3d
 */
public final class Vector3D extends QTByteObject implements QuickTimeLib, QuickDraw3DLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 12;

    static final long serialVersionUID = -8566142369958926912L;

    static {
        if (QTSession.isCurrentOS(4)) {
            throw new QTRuntimeException("Unsupported OS");
        }
        class PrivelegedAction {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Vector3D.class);
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
    public Vector3D(float paramFloat1, float paramFloat2, float paramFloat3) {
        this();
        setX(paramFloat1);
        setY(paramFloat2);
        setZ(paramFloat3);
    }

    private Vector3D(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[12];
        paramObjectInputStream.read(this.bytes);
        try {
            EndianOrder.flipBigEndianToNative(this, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        Vector3D localVector3D = (Vector3D) clone();
        try {
            EndianOrder.flipNativeToBigEndian(localVector3D, 0, getEndianDescriptor());
        } catch (UtilException localUtilException) {
            localUtilException.printStackTrace();
        }
        paramObjectOutputStream.write(localVector3D.getBytes());
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
    public void setX(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * sets the position in the Y direction
     */
    public void setY(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * sets the position in the Z direction
     */
    public void setZ(float paramFloat) {
        setFloatAt(8, paramFloat);
    }

    /**
     * Returns a three-dimensional vector that is the result of
     * multiplying each of the components of this by the value of
     * the scalar parameter.
     * QuickDraw3D::Q3Vector3D_Scale
     * @param scalar A floating-point number.
     * @return a new scaled vector.
     */
    public Vector3D scale(float paramFloat) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Vector3D_Scale(getBytes(), paramFloat, localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the length of a three-dimensional vector.
     * QuickDraw3D::Q3Vector3D_Length
     * @return the length of the vector.
     */
    public float length() {
        return Q3Vector3D_Length(getBytes());
    }

    /**
     * Returns the normalized form of the vector.
     * QuickDraw3D::Q3Vector3D_Normalize
     * @return the normalized form of the vector
     */
    public Vector3D normalize() {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Vector3D_Normalize(getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the three-dimensional vector that is the sum of the two vectors this and v2.
     * QuickDraw3D::Q3Vector3D_Add
     * @param v2 A three-dimensional vector.
     * @return the sum of v1 and v2.
     */
    public Vector3D add(Vector3D paramVector3D) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Vector3D_Add(getBytes(), paramVector3D.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the three-dimensional vector that is the result of subtracting v2 from this.
     * QuickDraw3D::Q3Vector3D_Subtract
     * @param v2 A three-dimensional vector.
     * @return the result of subtracting v2 from this.
     */
    public Vector3D subtract(Vector3D paramVector3D) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Vector3D_Subtract(getBytes(), paramVector3D.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the cross product of this vector and vector v2.
     * QuickDraw3D::Q3Vector3D_Cross
     * @param v2 A two-dimensional vector.
     * @return a vector
     */
    public Vector3D cross(Vector3D paramVector3D) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Vector3D_Cross(getBytes(), paramVector3D.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns the dot product of this vector and vector v2.
     * QuickDraw3D::Q3Vector3D_Dot
     * @param v2 A two-dimensional vector.
     * @return a vector
     */
    public float dot(Vector2D paramVector2D) {
        return Q3Vector3D_Dot(getBytes(), paramVector2D.getBytes());
    }

    /**
     * Return a three-dimensional vector that is the result of
     * transforming the vector specified by the vector parameter using the quaternion
     * specified by the quaternion parameter.
     * QuickDraw3D::Q3Vector3D_TransformQuaternion
     * @param quaternion A quaternion.
     * @return a three-dimensional vector
     */
    public Vector3D transformQuaternion(Quaternion paramQuaternion) {
        Vector3D localVector3D = new Vector3D();
        int i = Q3Vector3D_TransformQuaternion(getBytes(), paramQuaternion.getBytes(), localVector3D.getBytes());
        return localVector3D;
    }

    /**
     * Returns a copy of this class.
     */
    public Object clone() {
        return new Vector3D(getBytes());
    }

    /**
     * Returns a string representation of this object.
     */
    public String toString() {
        return super.toString() + "[x=" + getX() + ",y=" + getY() + ",z=" + getX() + "]";
    }

    private static native int Q3Vector3D_Scale(byte[] paramArrayOfByte1, float paramFloat, byte[] paramArrayOfByte2);

    private static native float Q3Vector3D_Length(byte[] paramArrayOfByte);

    private static native int Q3Vector3D_Normalize(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Vector3D_Add(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native int Q3Vector3D_Subtract(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native int Q3Vector3D_Cross(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);

    private static native float Q3Vector3D_Dot(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

    private static native int Q3Vector3D_TransformQuaternion(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
}
