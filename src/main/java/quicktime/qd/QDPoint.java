
package quicktime.qd;

import java.awt.Point;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTSession;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTUtils;


public final class QDPoint implements PrimitivesLib, Cloneable {
    private static Object linkage;

    private static final int kNativeSize = 8;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QDPoint.class);
                    }
                });
            }
        }.establish();
    }

    public static final EndianDescriptor getEndianDescriptorFixedPoint() {
        return EndianDescriptor.flipAll32;
    }

    public static final EndianDescriptor getEndianDescriptorTQ3Point2D() {
        return EndianDescriptor.flipAll32;
    }

    public static final EndianDescriptor getEndianDescriptorQTVRFloatPoint() {
        return EndianDescriptor.flipAll32;
    }

    private static boolean doEndianSwap = (!QTSession.isCurrentOS(1)) && (!QTSession.isCurrentOS(4));

    public static final int kIsFixedPoint = 8;
    public static final int kIsGXPoint = 8;
    public static final int kIsQ3Point2D = -8;
    public static final int kIsQTVRFloatPoint = -16;

    private short x;

    private short y;

    public static QDPoint fromArray(byte[] paramArrayOfByte, int paramInt) {
        return fromArray(paramArrayOfByte, paramInt, 0);
    }

    public static QDPoint fromArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if (paramArrayOfByte.length - paramInt2 < 8) {
            throw new IllegalArgumentException("Mismatch between flag and supplied ptBytes");
        }
        if (paramInt1 == 8) {
            int i = getIntFromArray(paramArrayOfByte, paramInt2);
            int j = getIntFromArray(paramArrayOfByte, paramInt2 + 4);
            return new QDPoint(QTUtils.Fix2X(i), QTUtils.Fix2X(j));
        }
        if ((paramInt1 == -8) || (paramInt1 == -16)) {
            float f1 = getFloatFromArray(paramArrayOfByte, paramInt2);
            float f2 = getFloatFromArray(paramArrayOfByte, paramInt2 + 4);
            return new QDPoint(f1, f2);
        }
        throw new IllegalArgumentException("Unknown point bytes format");
    }

    public QDPoint(int paramInt1, int paramInt2) {
        setX(paramInt1);
        setY(paramInt2);
    }

    public QDPoint(float paramFloat1, float paramFloat2) {
        setX(paramFloat1);
        setY(paramFloat2);
    }

    /**
     * @deprecated
     */
    public QDPoint(Point paramPoint) {
        this(paramPoint.x, paramPoint.y);
    }

    private QDPoint(short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        this.x = paramShort1;
        this.y = paramShort2;
        if (paramArrayOfByte != null) {
            this.floatPt = new byte[8];
            System.arraycopy(paramArrayOfByte, 0, this.floatPt, 0, 8);
        }
    }

    byte[] floatPt = null;

    public int getPoint() {
        return doEndianSwap ? this.x << 16 | this.y & 0xFFFF : this.y << 16 | this.x & 0xFFFF;
    }

    public byte[] getGXPoint() {
        return getFixedPoint();
    }

    public byte[] getFixedPoint() {
        byte[] arrayOfByte = new byte[8];
        setIntInArray(arrayOfByte, 0, QTUtils.X2Fix(getXF()));
        setIntInArray(arrayOfByte, 4, QTUtils.X2Fix(getYF()));
        return arrayOfByte;
    }

    public byte[] getQ3Point2D() {
        if (this.floatPt == null) {
            this.floatPt = new byte[8];
            setX(getX());
            setY(getY());
        }
        return this.floatPt;
    }

    public byte[] getQTVRFloatPoint() {
        return getQ3Point2D();
    }

    public int getX() {
        return this.x;
    }

    public float getXF() {
        return this.floatPt != null ? getFloatFromArray(this.floatPt, 0) : getX();
    }

    public void setX(int paramInt) {
        if (this.floatPt == null)
            this.x = ((short) paramInt);
        else {
            setX(paramInt);
        }
    }

    public void setX(float paramFloat) {
        if (this.floatPt == null)
            this.floatPt = new byte[8];
        setFloatInArray(this.floatPt, 0, paramFloat);
        this.x = ((short) (int) paramFloat);
    }

    public int getY() {
        return this.y;
    }

    public float getYF() {
        return this.floatPt != null ? getFloatFromArray(this.floatPt, 4) : getY();
    }

    public void setY(int paramInt) {
        if (this.floatPt == null)
            this.y = ((short) paramInt);
        else {
            setY(paramInt);
        }
    }

    public void setY(float paramFloat) {
        if (this.floatPt == null)
            this.floatPt = new byte[8];
        setFloatInArray(this.floatPt, 4, paramFloat);
        this.y = ((short) (int) paramFloat);
    }

    public void translate(int paramInt1, int paramInt2) {
        move(getX() + paramInt1, getY() + paramInt2);
    }

    public void translate(float paramFloat1, float paramFloat2) {
        move(getXF() + paramFloat1, getYF() + paramFloat2);
    }

    public void move(int paramInt1, int paramInt2) {
        setX(paramInt1);
        setY(paramInt2);
    }

    public void move(float paramFloat1, float paramFloat2) {
        setX(paramFloat1);
        setY(paramFloat2);
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof QDPoint)) {
            QDPoint localQDPoint = (QDPoint) paramObject;
            return (localQDPoint.getXF() == getXF()) && (localQDPoint.getYF() == getYF());
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[x=" + getXF() + ",y=" + getYF() + "]";
    }

    public Object clone() {
        return copy();
    }

    public QDPoint copy() {
        return new QDPoint(this.x, this.y, this.floatPt);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native float getFloatFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * QDPoint.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
