package quicktime.qd;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * This class corresponds to the Rect and FixedRect Data structures.
 * It has a current limitation of x and y < 32767 and x+width, y+height < 32767.
 * <p>
 * The width and height values are the number of pixels that will be seen - thus
 * if x == 1 and width is 3, the pixels drawn will be pixels at 1, 2, 3.
 */
public final class QDRect extends QTByteObject implements PrimitivesLib, Cloneable {

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

    static boolean isObject(Object object, String className) {
        Class<?> superclass = object.getClass();
        String name = superclass.getName();
        while (!superclass.getName().equals("java.lang.Object")) {
            name = superclass.getName();
            superclass = superclass.getSuperclass();
        }
        return name.equals(className);
    }

    /**
     * Returns the EndianDescriptor for FixedRect Rect.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorFixedRect() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Returns the EndianDescriptor for QDRect Rect.
     *
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptorRect() {
        return EndianDescriptor.flipAll16;
    }

    /**
     * Creates a QDRect from an array of bytes. The minimum length of the array
     * must equate to the requirements of the size of the native structure to
     * store the result.
     * <p>
     * kIsRect, length == 8, top, left, right, bottom of 2 bytes each
     * kIsFixedRect, length == 16, left, top, right, bottom of 4 bytes each (fixed format 16.16)
     *
     * @param rectBytes the byte array containing the rect values
     * @param flag      flag that describes the format of the byte array
     * @return a QDRect
     * @throws java.lang.IllegalArgumentException if mismatch between minimum length of byte array and format flag
     */
    public static QDRect fromArray(byte[] rectBytes, int flag) {
        if (flag > rectBytes.length)
            throw new IllegalArgumentException("Mismatch between flag and supplied rectBytes");
        switch (flag) {
            case 8:
                return new QDRect(rectBytes, null);
            case 16:
                QDRect rect = new QDRect();
                rect.fixedRect = new byte[16];
                System.arraycopy(rectBytes, 0, rect.fixedRect, 0, 16);
                rect._setXI((int) rect.getXF());
                rect._setYI((int) rect.getYF());
                rect._setRightI((int) rect.getRightF());
                rect._setBottomI((int) rect.getBottomF());
                return rect;
        }
        throw new IllegalArgumentException("Unknown rect format");
    }

    /**
     * Constructs a new rectangle, with (0, 0) origin and 0 width and height
     */
    public QDRect() {
        super(kNativeSize);
    }

    /**
     * Constructs and initializes a rectangle with the specified parameters.
     * Int values will be cast to shorts.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(int x, int y, int width, int height) {
        this();
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Constructs and initializes a rectangle with the specified parameters.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(float x, float y, float width, float height) {
        this();
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Constructs a rectangle and initializes it with the specified width and
     * height parameters.
     *
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(int width, int height) {
        this(0, 0, width, height);
    }

    /**
     * Constructs a rectangle and initializes it with the specified width and
     * height parameters.
     *
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public QDRect(float width, float height) {
        this(0.0F, 0.0F, width, height);
    }

    /**
     * Constructs a QDRect with an origin specified and 0 width and height.
     *
     * @param origin the origin point of the QDRect
     */
    public QDRect(QDPoint origin) {
        this(origin.getX(), origin.getY(), 0, 0);
    }

    /**
     * @param origin the origin point of the QDRect
     *               {@link quicktime.qd.QDRect#QDRect(quicktime.qd.QDPoint)}
     * @deprecated <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin specified and 0 width and height.
     */
    public QDRect(Point origin) {
        this(origin.x, origin.y, 0, 0);
    }

    /**
     * Constructs a QDRect with an origin of 0,0 and specified size.
     *
     * @param size the size of the QDRect.
     */
    public QDRect(QDDimension size) {
        this(0, 0, size.getWidth(), size.getHeight());
    }

    /**
     * @param size the size of the QDRect.
     *             {@link quicktime.qd.QDRect#QDRect(quicktime.qd.QDDimension)}
     * @deprecated <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin of 0,0 and specified size.
     */
    public QDRect(Dimension size) {
        this(0, 0, size.width, size.height);
    }

    /**
     * @param rect the Rectangle
     * @deprecated <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin of 0,0 and specified size.
     * NOTE: This method is deprecated. There is no direct method replacement
     */
    public QDRect(Rectangle rect) {
        this(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Constructs a QDRect with an origin and size specified.
     *
     * @param origin the origin point of the QDRect
     * @param size   the size of the QDRect.
     */
    public QDRect(QDPoint origin, QDDimension size) {
        this(origin.getX(), origin.getY(), size.getWidth(), size.getHeight());
    }

    /**
     * @param origin the origin point of the QDRect
     * @param size   the size of the QDRect.
     *               {@link quicktime.qd.QDRect#QDRect(quicktime.qd.QDPoint, quicktime.qd.QDDimension)}
     * @deprecated <I>since QTJava 4.0</I>
     * Constructs a QDRect with an origin and size specified.
     */
    public QDRect(Point origin, Dimension size) {
        this(origin.x, origin.y, size.width, size.height);
    }

    private QDRect(byte[] rect, byte[] fixedRect) {
        super(rect);
        if (fixedRect != null) {
            this.fixedRect = new byte[16];
            System.arraycopy(fixedRect, 0, this.fixedRect, 0, 16);
        }
    }

    private transient byte[] fixedRect = null;

    @Serial
    private static final long serialVersionUID = -5173727297383918112L;

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        byte[] arrayOfByte = new byte[24];
        in.read(arrayOfByte);
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(getBytes(), i, EndianOrder.flipBigEndianToNative16(getShortFromArray(arrayOfByte, i)));
        }
        this.fixedRect = new byte[16];
        for (int i = 0; i < this.fixedRect.length; i += 4) {
            setIntInArray(this.fixedRect, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, i + 8)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[24];
        for (int i = 0; i < 8; i += 2) {
            setShortInArray(movie, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        byte[] fixedRect = getFixedRect();
        for (int j = 0; j < fixedRect.length; j += 4) {
            setIntInArray(movie, j + 8, EndianOrder.flipNativeToBigEndian32(getIntFromArray(fixedRect, j)));
        }
        out.write(movie);
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
     *
     * @return the current x location
     */
    public int getX() {
        return getShortAt(2);
    }

    /**
     * Get the x location of the rect.
     *
     * @return the current x location
     */
    public float getXF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 0)) : getX();
    }

    /**
     * Set the x location of the rect.
     *
     * @param x the new x location
     */
    public void setX(int x) {
        if (this.fixedRect != null)
            setX(x);
        else {
            _setXI(x);
        }
    }

    /**
     * Set the x location of the rect.
     *
     * @param x the new x location
     */
    public void setX(float x) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setXF(x);
        _setXI((int) x);
    }

    /**
     * Get the y location of the rect.
     *
     * @return the current y location
     */
    public int getY() {
        return getShortAt(0);
    }

    /**
     * Get the y location of the rect.
     *
     * @return the current y location
     */
    public float getYF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 4)) : getY();
    }

    /**
     * Set the y location of the rect.
     *
     * @param y the new y location
     */
    public void setY(int y) {
        if (this.fixedRect != null)
            setY(y);
        else {
            _setYI(y);
        }
    }

    /**
     * Set the y location of the rect.
     *
     * @param y the new y location
     */
    public void setY(float y) {
        if (this.fixedRect == null) {
            this.fixedRect = new byte[16];
        }
        _setYF(y);
        _setYI((int) y);
    }

    /**
     * Returns true if the QDRect argument is a different size.
     */
    public boolean isDifferentSize(QDRect rect) {
        return (getWidth() != rect.getWidth()) || (getHeight() != rect.getHeight());
    }

    /**
     * Returns true if the QDRect argument has a different origin.
     */
    public boolean isDifferentOrigin(QDRect rect) {
        return (getX() != rect.getX()) || (getY() != rect.getY());
    }

    /**
     * Get the width of the QDRect.
     *
     * @return the current width
     */
    public int getWidth() {
        return getRight() - getX();
    }

    /**
     * Get the width of the QDRect.
     *
     * @return the current width
     */
    public float getWidthF() {
        return this.fixedRect != null ? getRightF() - getXF() : getWidth();
    }

    /**
     * Set the width of the QDRect.
     *
     * @param width the new width
     */
    public void setWidth(int width) {
        if (this.fixedRect != null)
            setWidth(width);
        else {
            setRight(Math.abs(width) + getX());
        }
    }

    /**
     * Set the width of the QDRect.
     *
     * @param width the new width
     */
    public void setWidth(float width) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        setRight(Math.abs(width) + getXF());
        setRight(Math.abs(width) + getX());
    }

    /**
     * Get the height of the QDRect.
     *
     * @return the current height
     */
    public int getHeight() {
        return getBottom() - getY();
    }

    /**
     * Get the height of the QDRect.
     *
     * @return the current height
     */
    public float getHeightF() {
        return this.fixedRect != null ? getBottomF() - getYF() : getHeight();
    }

    /**
     * Set the height of the QDRect.
     *
     * @param height the new height
     */
    public void setHeight(int height) {
        if (this.fixedRect != null)
            setHeight(height);
        else {
            setBottom(Math.abs(height) + getY());
        }
    }

    /**
     * Set the height of the QDRect.
     *
     * @param height the new height
     */
    public void setHeight(float height) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        setBottom(Math.abs(height) + getYF());
        setBottom(Math.abs(height) + getY());
    }

    /**
     * Reshapes the QDRect to x, y, width and height.
     */
    public void reshape(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Reshapes the QDRect to x, y, width and height.
     */
    public void reshape(float x, float y, float width, float height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Moves the QDRect to x, y.
     */
    public void move(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Moves the QDRect to x, y.
     */
    public void move(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * Translates the QDRect deltaX and deltaY.
     */
    public void translate(int dx, int dy) {
        move(getX() + dx, getY() + dy);
    }

    /**
     * Translates the QDRect deltaX and deltaY.
     */
    public void translate(float dx, float dy) {
        move(getXF() + dx, getYF() + dy);
    }

    /**
     * Resizes the QDRect.
     */
    public void resize(int width, int height) {
        reshape(getX(), getY(), width, height);
    }

    /**
     * Resizes the QDRect.
     */
    public void resize(float width, float height) {
        reshape(getXF(), getYF(), width, height);
    }

    /**
     * Checks if the specified point lies inside a QDRect.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public boolean inside(int x, int y) {
        return (x >= getX()) && (x < getRight()) && (y >= getY()) && (y < getBottom());
    }

    /**
     * Checks if the specified point lies inside a QDRect.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public boolean inside(float x, float y) {
        return (x >= getXF()) && (x < getRightF()) && (y >= getYF()) && (y < getBottomF());
    }

    /**
     * Checks if two QDRects intersect.
     */
    public boolean intersects(QDRect rect) {
        if ((this.fixedRect != null) && (rect.fixedRect != null)) {
            return (rect.getRightF() >= getXF()) && (rect.getBottomF() >= getYF()) && (rect.getXF() <= getRightF()) && (rect.getYF() <= getBottomF());
        }
        return (rect.getRight() >= getX()) && (rect.getBottom() >= getY()) && (rect.getX() <= getRight()) && (rect.getY() <= getBottom());
    }

    /**
     * Computes the intersection of two QDRects, returning the
     * largets rect that encompasses both rects.
     *
     * @param r the rect to intersect with
     * @return a QDRect
     */
    public QDRect intersection(QDRect r) {
        if ((this.fixedRect != null) && (r.fixedRect != null)) {
            float f1 = Math.max(getXF(), r.getXF());
            float f2 = Math.min(getRightF(), r.getRightF());
            float f3 = Math.max(getYF(), r.getYF());
            float f4 = Math.min(getBottomF(), r.getBottomF());
            return new QDRect(f1, f3, f2 - f1, f4 - f3);
        }
        int i = Math.max(getX(), r.getX());
        int j = Math.min(getRight(), r.getRight());
        int k = Math.max(getY(), r.getY());
        int m = Math.min(getBottom(), r.getBottom());
        return new QDRect(i, k, j - i, m - k);
    }

    /**
     * Computes the union of two QDRects, returning the smallest
     * Rect that is the union of the two QDRects.
     *
     * @param r the rect to unify with this
     * @return a rect
     */
    public QDRect union(QDRect r) {
        if ((this.fixedRect != null) && (r.fixedRect != null)) {
            float f1 = Math.min(getXF(), r.getXF());
            float f2 = Math.max(getRightF(), r.getRightF());
            float f3 = Math.min(getYF(), r.getYF());
            float f4 = Math.max(getBottomF(), r.getBottomF());
            return new QDRect(f1, f3, f2 - f1, f4 - f3);
        }
        int i = Math.min(getX(), r.getX());
        int j = Math.max(getRight(), r.getRight());
        int k = Math.min(getY(), r.getY());
        int m = Math.max(getBottom(), r.getBottom());
        return new QDRect(i, k, j - i, m - k);
    }

    /**
     * Adds a point to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     */
    public void add(int x, int y) {
        int i = Math.min(getX(), x);
        int j = Math.max(getX() + getWidth(), x);
        int k = Math.min(getY(), y);
        int m = Math.max(getY() + getHeight(), y);
        setX(i);
        setY(k);
        setWidth(j - i);
        setHeight(m - k);
    }

    /**
     * Adds a point to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     */
    public void add(float x, float y) {
        float f1 = Math.min(getXF(), x);
        float f2 = Math.max(getXF() + getWidthF(), x);
        float f3 = Math.min(getYF(), y);
        float f4 = Math.max(getYF() + getHeightF(), y);
        setX(f1);
        setY(f3);
        setWidth(f2 - f1);
        setHeight(f4 - f3);
    }

    /**
     * @param pt the Point to add
     * @see quicktime.qd.QDRect#add(quicktime.qd.QDPoint)
     * @deprecated <I>as of QTJava 4.0</I>
     * Adds a point to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     */
    public void add(Point pt) {
        add(pt.x, pt.y);
    }

    /**
     * Adds a QDPoint to the QDRect. This results in the smallest
     * QDRect that contains both the existing rectangle and the new point.
     *
     * @param pt the QDPoint to add
     */
    public void add(QDPoint pt) {
        if (pt.floatPt == null)
            add(pt.getX(), pt.getY());
        else {
            add(pt.getXF(), pt.getYF());
        }
    }

    /**
     * Grows the QDRect horizontally and vertically.
     * This method modifies the QDRect so that it is
     * h units larger on both the left and right side,
     * and v units larger at both the top and bottom.
     */
    public void grow(int dx, int dy) {
        setX(getX() - dx);
        setY(getY() - dy);
        setWidth(getWidth() + dx * 2);
        setHeight(getHeight() + dy * 2);
    }

    /**
     * Grows the QDRect horizontally and vertically.
     * This method modifies the QDRect so that it is
     * h units larger on both the left and right side,
     * and v units larger at both the top and bottom.
     */
    public void grow(float dx, float dy) {
        setX(getXF() - dx);
        setY(getYF() - dy);
        setWidth(getWidthF() + dx * 2.0F);
        setHeight(getHeightF() + dy * 2.0F);
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj instanceof QDRect rect)) {
            return (rect.getXF() == getXF()) && (rect.getYF() == getYF()) && (rect.getRightF() == getRightF()) && (rect.getBottomF() == getBottomF());
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
     *
     * @return a QDRect
     */
    @Override
    public Object clone() {
        return copy();
    }

    /**
     * Returns a copy of the QDRect with the correct return type.
     *
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

    private void setRight(int right) {
        if (this.fixedRect != null)
            setRight(right);
        else {
            _setRightI(right);
        }
    }

    private void setRight(float right) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setRightF(right);
        _setRightI((int) right);
    }

    private int getBottom() {
        return getShortAt(4);
    }

    private float getBottomF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 12)) : getBottom();
    }

    private void setBottom(int bottom) {
        if (this.fixedRect != null)
            setBottom(bottom);
        else {
            _setBottomI(bottom);
        }
    }

    private void setBottom(float bottom) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setBottomF(bottom);
        _setBottomI((int) bottom);
    }

    private void _setXF(float x) {
        int i = QTUtils.X2Fix(x);
        setIntInArray(this.fixedRect, 8, getIntFromArray(this.fixedRect, 8) - getIntFromArray(this.fixedRect, 0) + i);
        setIntInArray(this.fixedRect, 0, i);
    }

    private void _setXI(int value) {
        setShortAt(6, (short) (getShortAt(6) - getShortAt(2) + value));
        setShortAt(2, (short) value);
    }

    private void _setYF(float y) {
        int i = QTUtils.X2Fix(y);
        setIntInArray(this.fixedRect, 12, getIntFromArray(this.fixedRect, 12) - getIntFromArray(this.fixedRect, 4) + i);
        setIntInArray(this.fixedRect, 4, i);
    }

    private void _setYI(int value) {
        setShortAt(4, (short) (getShortAt(4) - getShortAt(0) + value));
        setShortAt(0, (short) value);
    }

    private void _setRightF(float right) {
        setIntInArray(this.fixedRect, 8, QTUtils.X2Fix(right));
    }

    private void _setRightI(int value) {
        setShortAt(6, (short) value);
    }

    private void _setBottomF(float bottom) {
        setIntInArray(this.fixedRect, 12, QTUtils.X2Fix(bottom));
    }

    private void _setBottomI(int value) {
        setShortAt(4, (short) value);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }
}
