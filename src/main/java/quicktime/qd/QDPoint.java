package quicktime.qd;

import java.awt.Point;

import quicktime.QTSession;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.QTUtils;


public final class QDPoint implements PrimitivesLib, Cloneable {

    private static final int kNativeSize = 8;

    /**
     * Returns the EndianDescriptor for FixedPoint point.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorFixedPoint() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for TQ3Point2D point.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorTQ3Point2D() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for TQ3Point2D point.
     *
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
     *
     * @param ptBytes the byte array containing the packed point values
     * @param ptType  the type of point to be found in the array
     * @return a QDPoint
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
    public static QDPoint fromArray(byte[] ptBytes, int ptType) {
        return fromArray(ptBytes, ptType, 0);
    }

    /**
     * Creates a QDPoint from an array of bytes that represent a FixedPoint, so the
     * byte array must be 8bytes long at least.
     *
     * @param ptBytes the byte array containing the packed point values
     * @param ptType  the type of point to be found in the array
     * @param offset  the offset in the array where the point is to be found
     * @return a QDPoint
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
    public static QDPoint fromArray(byte[] ptBytes, int ptType, int offset) {
        if (ptBytes.length - offset < kNativeSize) {
            throw new IllegalArgumentException("Mismatch between flag and supplied ptBytes");
        }
        if (ptType == 8) {
            int i = getIntFromArray(ptBytes, offset);
            int j = getIntFromArray(ptBytes, offset + 4);
            return new QDPoint(QTUtils.Fix2X(i), QTUtils.Fix2X(j));
        }
        if (ptType == kIsQ3Point2D || ptType == kIsQTVRFloatPoint) {
            float f1 = getFloatFromArray(ptBytes, offset);
            float f2 = getFloatFromArray(ptBytes, offset + 4);
            return new QDPoint(f1, f2);
        }
        throw new IllegalArgumentException("Unknown point bytes format");
    }

    /**
     * Create a QDPoint at specified location
     *
     * @param x x location
     * @param y y location
     */
    public QDPoint(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Create a QDPoint at specified location
     *
     * @param x x location
     * @param y y location
     */
    public QDPoint(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * @deprecated <I>As of QTJava v 4.0</I>
     * Create a QDPoint at specified location
     */
    public QDPoint(Point point) {
        this(point.x, point.y);
    }

    private QDPoint(short x, short y, byte[] floatPt) {
        this.x = x;
        this.y = y;
        if (floatPt != null) {
            this.floatPt = new byte[kNativeSize];
            System.arraycopy(floatPt, 0, this.floatPt, 0, kNativeSize);
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
        byte[] fixedPoint = new byte[kNativeSize];
        setIntInArray(fixedPoint, 0, QTUtils.X2Fix(getXF()));
        setIntInArray(fixedPoint, 4, QTUtils.X2Fix(getYF()));
        return fixedPoint;
    }

    /**
     * Returns a byte array that contains the packed value of a 3Point2D
     */
    public byte[] getQ3Point2D() {
        if (this.floatPt == null) {
            this.floatPt = new byte[kNativeSize];
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
     *
     * @return the current x location
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the x location of the point.
     *
     * @return the current x location
     */
    public float getXF() {
        return this.floatPt != null ? getFloatFromArray(this.floatPt, 0) : getX();
    }

    /**
     * Set the x location of the point.
     *
     * @param x the new x location
     */
    public void setX(int x) {
        if (this.floatPt == null)
            this.x = ((short) x);
        else {
            setX(x);
        }
    }

    /**
     * Set the x location of the point.
     *
     * @param x the new x location
     */
    public void setX(float x) {
        if (this.floatPt == null)
            this.floatPt = new byte[kNativeSize];
        setFloatInArray(this.floatPt, 0, x);
        this.x = ((short) (int) x);
    }

    /**
     * Get the y location of the point.
     *
     * @return the current y location
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get the y location of the point.
     *
     * @return the current y location
     */
    public float getYF() {
        return this.floatPt != null ? getFloatFromArray(this.floatPt, 4) : getY();
    }

    /**
     * Set the y location of the point.
     *
     * @param y the new y location
     */
    public void setY(int y) {
        if (this.floatPt == null)
            this.y = ((short) y);
        else {
            setY(y);
        }
    }

    /**
     * Set the y location of the point.
     *
     * @param y the new y location
     */
    public void setY(float y) {
        if (this.floatPt == null)
            this.floatPt = new byte[kNativeSize];
        setFloatInArray(this.floatPt, 4, y);
        this.y = ((short) (int) y);
    }

    /**
     * Translates the point, by deltaX and deltaY.
     */
    public void translate(int dx, int dy) {
        move(getX() + dx, getY() + dy);
    }

    /**
     * Translates the point, by deltaX and deltaY.
     */
    public void translate(float dx, float dy) {
        move(getXF() + dx, getYF() + dy);
    }

    /**
     * Moves the point, to coordinates x, y.
     */
    public void move(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Moves the point, to coordinates x, y.
     */
    public void move(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * Checks whether two int points are equal.
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj instanceof QDPoint point)) {
            return (point.getXF() == getXF()) && (point.getYF() == getYF());
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
     *
     * @return a QDPoint
     */
    @Override
    public Object clone() {
        return copy();
    }

    /**
     * Returns a copy of the QDPoint with the correct return type.
     *
     * @return a QDPoint
     */
    public QDPoint copy() {
        return new QDPoint(this.x, this.y, this.floatPt);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static float getFloatFromArray(byte[] array, int offset) {
        return MemoryManager.getFloatFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static void setFloatInArray(byte[] array, int offset, float value) {
        MemoryManager.setFloatInArray(array, offset, value);
    }
}
