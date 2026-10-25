package quicktime.qd;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * This class corresponds to the Rect and FixedRect Data structures.
 * It has a current limitation of x and y < 32767 and x+width, y+height < 32767.
 *
 * The width and height values are the number of pixels that will be seen - thus
 * if x == 1 and width is 3, the pixels drawn will be pixels at 1, 2, 3.
 */
public final class QDRect extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    private static final int kNativeSize = 8;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 2 formats the byte array is encoded in.
     */
    public static final int kIsRect = 8;

    /**
     * These constants are used in the fromArray call to indicate
     * which of the 2 formats the byte array is encoded in.
     */
    public static final int kIsFixedRect = 16;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QDRect.class);
                    }
                });
            }
        }.establish();
    }

    static boolean isObject(Object paramObject, String paramString) {
        Class<?> localClass = paramObject.getClass();
        String str = localClass.getName();
        while (!localClass.getName().equals("java.lang.Object")) {
            str = localClass.getName();
            localClass = localClass.getSuperclass();
        }
        return str.equals(paramString);
    }

    /**
     * Returns the EndianDescriptor for FixedRect Rect.
     * @return an EndianDescriptor
     */
    public static final EndianDescriptor getEndianDescriptorFixedRect() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for QDRect Rect.
     * @return an EndianDescriptor
     */
    public static final EndianDescriptor getEndianDescriptorRect() {
        return EndianDescriptor.flipAll16;
    }

    /**
     * Creates a QDRect from an array of bytes. The minimum length of the array
     * must equate to the requirements of the size of the native structure to
     * store the result.
     *
     * kIsRect, length == 8, top, left, right, bottom of 2 bytes each
     * kIsFixedRect, length == 16, left, top, right, bottom of 4 bytes each (fixed format 16.16)
     *
     * @param rectBytes the byte array containing the rect values
     * @param flag flag that describes the format of the byte array
     * @return a QDRect
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
    public static QDRect fromArray(byte[] paramArrayOfByte, int paramInt) {
        if (paramInt > paramArrayOfByte.length)
            throw new IllegalArgumentException("Mismatch between flag and supplied rectBytes");
        switch(paramInt) {
            case 8:
                return new QDRect(paramArrayOfByte, null);
            case 16:
                QDRect localQDRect = new QDRect();
                localQDRect.fixedRect = new byte[16];
                System.arraycopy(paramArrayOfByte, 0, localQDRect.fixedRect, 0, 16);
                localQDRect._setXI((int) localQDRect.getXF());
                localQDRect._setYI((int) localQDRect.getYF());
                localQDRect._setRightI((int) localQDRect.getRightF());
                localQDRect._setBottomI((int) localQDRect.getBottomF());
                return localQDRect;
        }
        throw new IllegalArgumentException("Unknown rect format");
    }

    /**
     * Constructs a new rectangle, with (0, 0) origin and 0 width and height
     */
    public QDRect() {
        super(8);
    }

    /**
     * Constructs and initializes a rectangle with the specified parameters.
     * Int values will be cast to shorts.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this();
        setX(paramInt1);
        setY(paramInt2);
        setWidth(paramInt3);
        setHeight(paramInt4);
    }

    /**
     * Constructs and initializes a rectangle with the specified parameters.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this();
        setX(paramFloat1);
        setY(paramFloat2);
        setWidth(paramFloat3);
        setHeight(paramFloat4);
    }

    /**
     * Constructs a rectangle and initializes it with the specified width and
     * height parameters.
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(int paramInt1, int paramInt2) {
        this(0, 0, paramInt1, paramInt2);
    }

    /**
     * Constructs a rectangle and initializes it with the specified width and
     * height parameters.
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(float paramFloat1, float paramFloat2) {
        this(0.0F, 0.0F, paramFloat1, paramFloat2);
    }

    /**
     * Constructs a QDRect with an origin specified and 0 width and height.
     * @param origin the origin point of the QDRect
     */
    public QDRect(QDPoint paramQDPoint) {
        this(paramQDPoint.getX(), paramQDPoint.getY(), 0, 0);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin specified and 0 width and height.
     * @param origin the origin point of the QDRect
     * {@link quicktime.qd.QDRect#QDRect(quicktime.qd.QDPoint)}
     */
    public QDRect(Point paramPoint) {
        this(paramPoint.x, paramPoint.y, 0, 0);
    }

    /**
     * Constructs a QDRect with an origin of 0,0 and specified size.
     * @param size the size of the QDRect.
     */
    public QDRect(QDDimension paramQDDimension) {
        this(0, 0, paramQDDimension.getWidth(), paramQDDimension.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin of 0,0 and specified size.
     * @param size the size of the QDRect.
     * {@link quicktime.qd.QDRect#QDRect(quicktime.qd.QDDimension)}
     */
    public QDRect(Dimension paramDimension) {
        this(0, 0, paramDimension.width, paramDimension.height);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin of 0,0 and specified size.
     * NOTE: This method is deprecated. There is no direct method replacement
     * @param rect the Rectangle
     */
    public QDRect(Rectangle paramRectangle) {
        this(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
    }

    /**
     * Constructs a QDRect with an origin and size specified.
     * @param origin the origin point of the QDRect
     * @param size the size of the QDRect.
     */
    public QDRect(QDPoint paramQDPoint, QDDimension paramQDDimension) {
        this(paramQDPoint.getX(), paramQDPoint.getY(), paramQDDimension.getWidth(), paramQDDimension.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin and size specified.
     * @param origin the origin point of the QDRect
     * @param size the size of the QDRect.
     * {@link quicktime.qd.QDRect#QDRect(quicktime.qd.QDPoint, quicktime.qd.QDDimension)}
     */
    public QDRect(Point paramPoint, Dimension paramDimension) {
        this(paramPoint.x, paramPoint.y, paramDimension.width, paramDimension.height);
    }

    private QDRect(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        super(paramArrayOfByte1);
        if (paramArrayOfByte2 != null) {
            this.fixedRect = new byte[16];
            System.arraycopy(paramArrayOfByte2, 0, this.fixedRect, 0, 16);
        }
    }

    private transient byte[] fixedRect = null;

    private static final long serialVersionUID = -5173727297383918112L;

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];
        byte[] arrayOfByte = new byte[24];
        paramObjectInputStream.read(arrayOfByte);
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(getBytes(), i, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, i)));
        }
        this.fixedRect = new byte[16];
        for (int i = 0; i < this.fixedRect.length; i += 4) {
            setIntInArray(this.fixedRect, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, i + 8)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte1 = new byte[24];
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(arrayOfByte1, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        byte[] arrayOfByte2 = getFixedRect();
        for (int j = 0; j < arrayOfByte2.length; j += 4) {
            setIntInArray(arrayOfByte1, j + 8, EndianOrder.flipNativeToBigEndian32(getIntFromArray(arrayOfByte2, j)));
        }
        paramObjectOutputStream.write(arrayOfByte1);
    }

    /**
     * Returns the bytes for the QDRect
     */
    public byte[] getRect() {
        return this.bytes;
    }

    /**
     * Returns the bytes for the FixedRect
     */
    public byte[] getFixedRect() {
        if (this.fixedRect == null) {
            this.fixedRect = new byte[16];
            setX(getX());
            setY(getY());
            setWidth(getWidth());
            setHeight(getHeight());
        }
        return this.fixedRect;
    }

    /**
     * Get the x location of the rect.
     * @return the current x location
     */
    public int getX() {
        return getShortAt(2);
    }

    /**
     * Get the x location of the rect.
     * @return the current x location
     */
    public float getXF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 0)) : getX();
    }

    /**
     * Set the x location of the rect.
     * @param x the new x location
     */
    public void setX(int paramInt) {
        if (this.fixedRect != null)
            setX(paramInt);
        else {
            _setXI(paramInt);
        }
    }

    /**
     * Set the x location of the rect.
     * @param x the new x location
     */
    public void setX(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setXF(paramFloat);
        _setXI((int) paramFloat);
    }

    /**
     * Get the y location of the rect.
     * @return the current y location
     */
    public int getY() {
        return getShortAt(0);
    }

    /**
     * Get the y location of the rect.
     * @return the current y location
     */
    public float getYF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 4)) : getY();
    }

    /**
     * Set the y location of the rect.
     * @param y the new y location
     */
    public void setY(int paramInt) {
        if (this.fixedRect != null)
            setY(paramInt);
        else {
            _setYI(paramInt);
        }
    }

    /**
     * Set the y location of the rect.
     * @param y the new y location
     */
    public void setY(float paramFloat) {
        if (this.fixedRect == null) {
            this.fixedRect = new byte[16];
        }
        _setYF(paramFloat);
        _setYI((int) paramFloat);
    }

    /**
     * Returns true if the QDRect argument is a different size.
     */
    public boolean isDifferentSize(QDRect paramQDRect) {
        return (getWidth() != paramQDRect.getWidth()) || (getHeight() != paramQDRect.getHeight());
    }

    /**
     * Returns true if the QDRect argument has a different origin.
     */
    public boolean isDifferentOrigin(QDRect paramQDRect) {
        return (getX() != paramQDRect.getX()) || (getY() != paramQDRect.getY());
    }

    /**
     * Get the width of the QDRect.
     * @return the current width
     */
    public int getWidth() {
        return getRight() - getX();
    }

    /**
     * Get the width of the QDRect.
     * @return the current width
     */
    public float getWidthF() {
        return this.fixedRect != null ? getRightF() - getXF() : getWidth();
    }

    /**
     * Set the width of the QDRect.
     * @param width the new width
     */
    public void setWidth(int paramInt) {
        if (this.fixedRect != null)
            setWidth(paramInt);
        else {
            setRight(Math.abs(paramInt) + getX());
        }
    }

    /**
     * Set the width of the QDRect.
     * @param width the new width
     */
    public void setWidth(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        setRight(Math.abs(paramFloat) + getXF());
        setRight(Math.abs(paramFloat) + getX());
    }

    /**
     * Get the height of the QDRect.
     * @return the current height
     */
    public int getHeight() {
        return getBottom() - getY();
    }

    /**
     * Get the height of the QDRect.
     * @return the current height
     */
    public float getHeightF() {
        return this.fixedRect != null ? getBottomF() - getYF() : getHeight();
    }

    /**
     * Set the height of the QDRect.
     * @param height the new height
     */
    public void setHeight(int paramInt) {
        if (this.fixedRect != null)
            setHeight(paramInt);
        else {
            setBottom(Math.abs(paramInt) + getY());
        }
    }

    /**
     * Set the height of the QDRect.
     * @param height the new height
     */
    public void setHeight(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        setBottom(Math.abs(paramFloat) + getYF());
        setBottom(Math.abs(paramFloat) + getY());
    }

    /**
     * Reshapes the QDRect to x, y, width and height.
     */
    public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        setX(paramInt1);
        setY(paramInt2);
        setWidth(paramInt3);
        setHeight(paramInt4);
    }

    /**
     * Reshapes the QDRect to x, y, width and height.
     */
    public void reshape(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        setX(paramFloat1);
        setY(paramFloat2);
        setWidth(paramFloat3);
        setHeight(paramFloat4);
    }

    /**
     * Moves the QDRect to x, y.
     */
    public void move(int paramInt1, int paramInt2) {
        setX(paramInt1);
        setY(paramInt2);
    }

    /**
     * Moves the QDRect to x, y.
     */
    public void move(float paramFloat1, float paramFloat2) {
        setX(paramFloat1);
        setY(paramFloat2);
    }

    /**
     * Translates the QDRect deltaX and deltaY.
     */
    public void translate(int paramInt1, int paramInt2) {
        move(getX() + paramInt1, getY() + paramInt2);
    }

    /**
     * Translates the QDRect deltaX and deltaY.
     */
    public void translate(float paramFloat1, float paramFloat2) {
        move(getXF() + paramFloat1, getYF() + paramFloat2);
    }

    /**
     * Resizes the QDRect.
     */
    public void resize(int paramInt1, int paramInt2) {
        reshape(getX(), getY(), paramInt1, paramInt2);
    }

    /**
     * Resizes the QDRect.
     */
    public void resize(float paramFloat1, float paramFloat2) {
        reshape(getXF(), getYF(), paramFloat1, paramFloat2);
    }

    /**
     * Checks if the specified point lies inside a QDRect.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public boolean inside(int paramInt1, int paramInt2) {
        return (paramInt1 >= getX()) && (paramInt1 < getRight()) && (paramInt2 >= getY()) && (paramInt2 < getBottom());
    }

    /**
     * Checks if the specified point lies inside a QDRect.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public boolean inside(float paramFloat1, float paramFloat2) {
        return (paramFloat1 >= getXF()) && (paramFloat1 < getRightF()) && (paramFloat2 >= getYF()) && (paramFloat2 < getBottomF());
    }

    /**
     * Checks if two QDRects intersect.
     */
    public boolean intersects(QDRect paramQDRect) {
        if ((this.fixedRect != null) && (paramQDRect.fixedRect != null)) {
            return (paramQDRect.getRightF() >= getXF()) && (paramQDRect.getBottomF() >= getYF()) && (paramQDRect.getXF() <= getRightF()) && (paramQDRect.getYF() <= getBottomF());
        }
        return (paramQDRect.getRight() >= getX()) && (paramQDRect.getBottom() >= getY()) && (paramQDRect.getX() <= getRight()) && (paramQDRect.getY() <= getBottom());
    }

    /**
     * Computes the intersection of two QDRects, returning the
     * largets rect that encompasses both rects.
     * @param r the rect to intersect with
     * @return a QDRect
     */
    public QDRect intersection(QDRect paramQDRect) {
        if ((this.fixedRect != null) && (paramQDRect.fixedRect != null)) {
            float f1 = Math.max(getXF(), paramQDRect.getXF());
            float f2 = Math.min(getRightF(), paramQDRect.getRightF());
            float f3 = Math.max(getYF(), paramQDRect.getYF());
            float f4 = Math.min(getBottomF(), paramQDRect.getBottomF());
            return new QDRect(f1, f3, f2 - f1, f4 - f3);
        }
        int i = Math.max(getX(), paramQDRect.getX());
        int j = Math.min(getRight(), paramQDRect.getRight());
        int k = Math.max(getY(), paramQDRect.getY());
        int m = Math.min(getBottom(), paramQDRect.getBottom());
        return new QDRect(i, k, j - i, m - k);
    }

    /**
     * Computes the union of two QDRects, returning the smallest
     * Rect that is the union of the two QDRects.
     * @param r the rect to unify with this
     * @return a rect
     */
    public QDRect union(QDRect paramQDRect) {
        if ((this.fixedRect != null) && (paramQDRect.fixedRect != null)) {
            float f1 = Math.min(getXF(), paramQDRect.getXF());
            float f2 = Math.max(getRightF(), paramQDRect.getRightF());
            float f3 = Math.min(getYF(), paramQDRect.getYF());
            float f4 = Math.max(getBottomF(), paramQDRect.getBottomF());
            return new QDRect(f1, f3, f2 - f1, f4 - f3);
        }
        int i = Math.min(getX(), paramQDRect.getX());
        int j = Math.max(getRight(), paramQDRect.getRight());
        int k = Math.min(getY(), paramQDRect.getY());
        int m = Math.max(getBottom(), paramQDRect.getBottom());
        return new QDRect(i, k, j - i, m - k);
    }

    /**
     * Adds a point to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     */
    public void add(int paramInt1, int paramInt2) {
        int i = Math.min(getX(), paramInt1);
        int j = Math.max(getX() + getWidth(), paramInt1);
        int k = Math.min(getY(), paramInt2);
        int m = Math.max(getY() + getHeight(), paramInt2);
        setX(i);
        setY(k);
        setWidth(j - i);
        setHeight(m - k);
    }

    /**
     * Adds a point to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     */
    public void add(float paramFloat1, float paramFloat2) {
        float f1 = Math.min(getXF(), paramFloat1);
        float f2 = Math.max(getXF() + getWidthF(), paramFloat1);
        float f3 = Math.min(getYF(), paramFloat2);
        float f4 = Math.max(getYF() + getHeightF(), paramFloat2);
        setX(f1);
        setY(f3);
        setWidth(f2 - f1);
        setHeight(f4 - f3);
    }

    /**
     * Deprecated.
     * <I>as of QTJava 4.0</I>
     * Adds a point to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     * @param pt the Point to add
     * @see {@link quicktime.qd.QDRect#add(quicktime.qd.QDPoint)}
     */
    public void add(Point paramPoint) {
        add(paramPoint.x, paramPoint.y);
    }

    /**
     * Adds a QDPoint to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     * @param pt the QDPoint to add
     */
    public void add(QDPoint paramQDPoint) {
        if (paramQDPoint.floatPt == null)
            add(paramQDPoint.getX(), paramQDPoint.getY());
        else {
            add(paramQDPoint.getXF(), paramQDPoint.getYF());
        }
    }

    /**
     * Grows the QDRect horizontally and vertically.
     * This method modifies the QDRect so that it is
     * h units larger on both the left and right side,
     * and v units larger at both the top and bottom.
     */
    public void grow(int paramInt1, int paramInt2) {
        setX(getX() - paramInt1);
        setY(getY() - paramInt2);
        setWidth(getWidth() + paramInt1 * 2);
        setHeight(getHeight() + paramInt2 * 2);
    }

    /**
     * Grows the QDRect horizontally and vertically.
     * This method modifies the QDRect so that it is
     * h units larger on both the left and right side,
     * and v units larger at both the top and bottom.
     */
    public void grow(float paramFloat1, float paramFloat2) {
        setX(getXF() - paramFloat1);
        setY(getYF() - paramFloat2);
        setWidth(getWidthF() + paramFloat1 * 2.0F);
        setHeight(getHeightF() + paramFloat2 * 2.0F);
    }

    /**
     * Determines whether the QDRect is empty.
     */
    public boolean isEmpty() {
        return (getWidth() <= 0) || (getHeight() <= 0);
    }

    /**
     * Returns the hashcode for this Rectangle.
     */
    public int hashCode() {
        return getX() ^ getY() * 37 ^ getWidth() * 43 ^ getHeight() * 47;
    }

    /**
     * Checks whether two QDRects are equal.
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof QDRect)) {
            QDRect localQDRect = (QDRect) paramObject;
            return (localQDRect.getXF() == getXF()) && (localQDRect.getYF() == getYF()) && (localQDRect.getRightF() == getRightF()) && (localQDRect.getBottomF() == getBottomF());
        }
        return false;
    }

    /**
     * Returns the String representation of this QDRect's values.
     */
    public String toString() {
        return super.toString() + "[x=" + getXF() + ",y=" + getYF() + ",width=" + getWidthF() + ",height=" + getHeightF() + "]";
    }

    /**
     * Return a copy of the QDRect
     * @return a QDRect
     */
    public Object clone() {
        return copy();
    }

    /**
     * Returns a copy of the QDRect with the correct return type.
     * @return a QDRect
     */
    public QDRect copy() {
        return new QDRect(getBytes(), this.fixedRect);
    }

    private int getRight() {
        return getShortAt(6);
    }

    private float getRightF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 8)) : getRight();
    }

    private void setRight(int paramInt) {
        if (this.fixedRect != null)
            setRight(paramInt);
        else {
            _setRightI(paramInt);
        }
    }

    private void setRight(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setRightF(paramFloat);
        _setRightI((int) paramFloat);
    }

    private int getBottom() {
        return getShortAt(4);
    }

    private float getBottomF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 12)) : getBottom();
    }

    private void setBottom(int paramInt) {
        if (this.fixedRect != null)
            setBottom(paramInt);
        else {
            _setBottomI(paramInt);
        }
    }

    private void setBottom(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setBottomF(paramFloat);
        _setBottomI((int) paramFloat);
    }

    private void _setXF(float paramFloat) {
        int i = QTUtils.X2Fix(paramFloat);
        setIntInArray(this.fixedRect, 8, getIntFromArray(this.fixedRect, 8) - getIntFromArray(this.fixedRect, 0) + i);
        setIntInArray(this.fixedRect, 0, i);
    }

    private void _setXI(int paramInt) {
        setShortAt(6, (short) (getShortAt(6) - getShortAt(2) + paramInt));
        setShortAt(2, (short) paramInt);
    }

    private void _setYF(float paramFloat) {
        int i = QTUtils.X2Fix(paramFloat);
        setIntInArray(this.fixedRect, 12, getIntFromArray(this.fixedRect, 12) - getIntFromArray(this.fixedRect, 4) + i);
        setIntInArray(this.fixedRect, 4, i);
    }

    private void _setYI(int paramInt) {
        setShortAt(4, (short) (getShortAt(4) - getShortAt(0) + paramInt));
        setShortAt(0, (short) paramInt);
    }

    private void _setRightF(float paramFloat) {
        setIntInArray(this.fixedRect, 8, QTUtils.X2Fix(paramFloat));
    }

    private void _setRightI(int paramInt) {
        setShortAt(6, (short) paramInt);
    }

    private void _setBottomF(float paramFloat) {
        setIntInArray(this.fixedRect, 12, QTUtils.X2Fix(paramFloat));
    }

    private void _setBottomI(int paramInt) {
        setShortAt(4, (short) paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/QDRect
 * .class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
