package quicktime.qd;

import java.awt.Point;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
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
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QDPoint.class));
            }
        }.establish();
    }

    /**
     * Returns the EndianDescriptor for FixedPoint point.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorFixedPoint() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for TQ3Point2D point.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorTQ3Point2D() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for TQ3Point2D point.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorQTVRFloatPoint() {
        return EndianDescriptor.flipAll32;
    }

    private static final boolean doEndianSwap = (!QTSession.isCurrentOS(1)) && (!QTSession.isCurrentOS(4));

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 2 formats the byte array is encoded in.
     */
    public static final int kIsFixedPoint = 8;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 2 formats the byte array is encoded in.
     */
    public static final int kIsGXPoint = 8;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 2 formats the byte array is encoded in.
     */
    public static final int kIsQ3Point2D = -8;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 2 formats the byte array is encoded in.
     */
    public static final int kIsQTVRFloatPoint = -16;

    private short x;

    private short y;

    /**
     * Creates a QDPoint from an array of bytes that represent a FixedPoint, so the
     * byte array must be 8bytes long at least.
     * @param ptBytes the byte array containing the packed point values
     * @param ptType the type of point to be found in the array
     * @return a QDPoint
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
    public static QDPoint fromArray(byte[] paramArrayOfByte, int paramInt) {
        return fromArray(paramArrayOfByte, paramInt, 0);
    }

    /**
     * Creates a QDPoint from an array of bytes that represent a FixedPoint, so the
     * byte array must be 8bytes long at least.
     * @param ptBytes the byte array containing the packed point values
     * @param ptType the type of point to be found in the array
     * @param offset the offset in the array where the point is to be found
     * @return a QDPoint
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
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

    /**
     * Create a QDPoint at specified location
     * @param x x location
     * @param y y location
     */
    public QDPoint(int paramInt1, int paramInt2) {
        setX(paramInt1);
        setY(paramInt2);
    }

    /**
     * Create a QDPoint at specified location
     * @param x x location
     * @param y y location
     */
    public QDPoint(float paramFloat1, float paramFloat2) {
        setX(paramFloat1);
        setY(paramFloat2);
    }

    /**
     * @deprecated
     * <I>As of QTJava v 4.0</I>
     * Create a QDPoint at specified location
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

    /**
     * Returns the QDPoint packed into a 32bit integer
     */
    public int getPoint() {
        return doEndianSwap ? this.x << 16 | this.y & 0xFFFF : this.y << 16 | this.x & 0xFFFF;
    }

    /**
     * Returns a byte array that contains the packed value of a GXPoint
     */
    public byte[] getGXPoint() {
        return getFixedPoint();
    }

    /**
     * Returns a byte array that contains the packed value of a FixedPoint
     */
    public byte[] getFixedPoint() {
        byte[] arrayOfByte = new byte[8];
        setIntInArray(arrayOfByte, 0, QTUtils.X2Fix(getXF()));
        setIntInArray(arrayOfByte, 4, QTUtils.X2Fix(getYF()));
        return arrayOfByte;
    }

    /**
     * Returns a byte array that contains the packed value of a 3Point2D
     */
    public byte[] getQ3Point2D() {
        if (this.floatPt == null) {
            this.floatPt = new byte[8];
            setX(getX());
            setY(getY());
        }
        return this.floatPt;
    }

    /**
     * Returns a byte array that contains the packed value of a QTVRFloatPoint
     */
    public byte[] getQTVRFloatPoint() {
        return getQ3Point2D();
    }

    /**
     * Get the x location of the point.
     * @return the current x location
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the x location of the point.
     * @return the current x location
     */
    public float getXF() {
        return this.floatPt != null ? getFloatFromArray(this.floatPt, 0) : getX();
    }

    /**
     * Set the x location of the point.
     * @param x the new x location
     */
    public void setX(int paramInt) {
        if (this.floatPt == null)
            this.x = ((short) paramInt);
        else {
            setX(paramInt);
        }
    }

    /**
     * Set the x location of the point.
     * @param x the new x location
     */
    public void setX(float paramFloat) {
        if (this.floatPt == null)
            this.floatPt = new byte[8];
        setFloatInArray(this.floatPt, 0, paramFloat);
        this.x = ((short) (int) paramFloat);
    }

    /**
     * Get the y location of the point.
     * @return the current y location
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get the y location of the point.
     * @return the current y location
     */
    public float getYF() {
        return this.floatPt != null ? getFloatFromArray(this.floatPt, 4) : getY();
    }

    /**
     * Set the y location of the point.
     * @param y the new y location
     */
    public void setY(int paramInt) {
        if (this.floatPt == null)
            this.y = ((short) paramInt);
        else {
            setY(paramInt);
        }
    }

    /**
     * Set the y location of the point.
     * @param y the new y location
     */
    public void setY(float paramFloat) {
        if (this.floatPt == null)
            this.floatPt = new byte[8];
        setFloatInArray(this.floatPt, 4, paramFloat);
        this.y = ((short) (int) paramFloat);
    }

    /**
     * Translates the point, by deltaX and deltaY.
     */
    public void translate(int paramInt1, int paramInt2) {
        move(getX() + paramInt1, getY() + paramInt2);
    }

    /**
     * Translates the point, by deltaX and deltaY.
     */
    public void translate(float paramFloat1, float paramFloat2) {
        move(getXF() + paramFloat1, getYF() + paramFloat2);
    }

    /**
     * Moves the point, to coordinates x, y.
     */
    public void move(int paramInt1, int paramInt2) {
        setX(paramInt1);
        setY(paramInt2);
    }

    /**
     * Moves the point, to coordinates x, y.
     */
    public void move(float paramFloat1, float paramFloat2) {
        setX(paramFloat1);
        setY(paramFloat2);
    }

    /**
     * Checks whether two int points are equal.
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof QDPoint localQDPoint)) {
            return (localQDPoint.getXF() == getXF()) && (localQDPoint.getYF() == getYF());
        }
        return false;
    }

    /**
     * Returns the String representation of this QDPoint's coordinate.
     */
    public String toString() {
        return getClass().getName() + "[x=" + getXF() + ",y=" + getYF() + "]";
    }

    /**
     * Return a copy of the QDPoint
     * @return a QDPoint
     */
    @Override
    public Object clone() {
        return copy();
    }

    /**
     * Returns a copy of the QDPoint with the correct return type.
     * @return a QDPoint
     */
    public QDPoint copy() {
        return new QDPoint(this.x, this.y, this.floatPt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static float getFloatFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getFloatFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static void setFloatInArray(byte[] paramArrayOfByte, int paramInt, float paramFloat) {
        MemoryManager.setFloatInArray(paramArrayOfByte, paramInt, paramFloat);
    }
}

