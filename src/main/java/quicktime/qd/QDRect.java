
package quicktime.qd;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class QDRect extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    private static final int kNativeSize = 8;

    public static final int kIsRect = 8;

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

    public static final EndianDescriptor getEndianDescriptorFixedRect() {
        return EndianDescriptor.flipAll32;
    }

    public static final EndianDescriptor getEndianDescriptorRect() {
        return EndianDescriptor.flipAll16;
    }

    public static QDRect fromArray(byte[] paramArrayOfByte, int paramInt) {
        if (paramInt > paramArrayOfByte.length)
            throw new IllegalArgumentException("Mismatch between flag and supplied rectBytes");
        switch (paramInt) {
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

    public QDRect() {
        super(8);
    }

    public QDRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this();
        setX(paramInt1);
        setY(paramInt2);
        setWidth(paramInt3);
        setHeight(paramInt4);
    }

    public QDRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this();
        setX(paramFloat1);
        setY(paramFloat2);
        setWidth(paramFloat3);
        setHeight(paramFloat4);
    }

    public QDRect(int paramInt1, int paramInt2) {
        this(0, 0, paramInt1, paramInt2);
    }

    public QDRect(float paramFloat1, float paramFloat2) {
        this(0.0F, 0.0F, paramFloat1, paramFloat2);
    }

    public QDRect(QDPoint paramQDPoint) {
        this(paramQDPoint.getX(), paramQDPoint.getY(), 0, 0);
    }

    /**
     * @deprecated
     */
    public QDRect(Point paramPoint) {
        this(paramPoint.x, paramPoint.y, 0, 0);
    }

    public QDRect(QDDimension paramQDDimension) {
        this(0, 0, paramQDDimension.getWidth(), paramQDDimension.getHeight());
    }

    /**
     * @deprecated
     */
    public QDRect(Dimension paramDimension) {
        this(0, 0, paramDimension.width, paramDimension.height);
    }

    /**
     * @deprecated
     */
    public QDRect(Rectangle paramRectangle) {
        this(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
    }

    public QDRect(QDPoint paramQDPoint, QDDimension paramQDDimension) {
        this(paramQDPoint.getX(), paramQDPoint.getY(), paramQDDimension.getWidth(), paramQDDimension.getHeight());
    }

    /**
     * @deprecated
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

    public byte[] getRect() {
        return this.bytes;
    }

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

    public int getX() {
        return getShortAt(2);
    }

    public float getXF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 0)) : getX();
    }

    public void setX(int paramInt) {
        if (this.fixedRect != null)
            setX(paramInt);
        else {
            _setXI(paramInt);
        }
    }

    public void setX(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        _setXF(paramFloat);
        _setXI((int) paramFloat);
    }

    public int getY() {
        return getShortAt(0);
    }

    public float getYF() {
        return this.fixedRect != null ? QTUtils.Fix2X(getIntFromArray(this.fixedRect, 4)) : getY();
    }

    public void setY(int paramInt) {
        if (this.fixedRect != null)
            setY(paramInt);
        else {
            _setYI(paramInt);
        }
    }

    public void setY(float paramFloat) {
        if (this.fixedRect == null) {
            this.fixedRect = new byte[16];
        }
        _setYF(paramFloat);
        _setYI((int) paramFloat);
    }

    public boolean isDifferentSize(QDRect paramQDRect) {
        return (getWidth() != paramQDRect.getWidth()) || (getHeight() != paramQDRect.getHeight());
    }

    public boolean isDifferentOrigin(QDRect paramQDRect) {
        return (getX() != paramQDRect.getX()) || (getY() != paramQDRect.getY());
    }

    public int getWidth() {
        return getRight() - getX();
    }

    public float getWidthF() {
        return this.fixedRect != null ? getRightF() - getXF() : getWidth();
    }

    public void setWidth(int paramInt) {
        if (this.fixedRect != null)
            setWidth(paramInt);
        else {
            setRight(Math.abs(paramInt) + getX());
        }
    }

    public void setWidth(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        setRight(Math.abs(paramFloat) + getXF());
        setRight(Math.abs(paramFloat) + getX());
    }

    public int getHeight() {
        return getBottom() - getY();
    }

    public float getHeightF() {
        return this.fixedRect != null ? getBottomF() - getYF() : getHeight();
    }

    public void setHeight(int paramInt) {
        if (this.fixedRect != null)
            setHeight(paramInt);
        else {
            setBottom(Math.abs(paramInt) + getY());
        }
    }

    public void setHeight(float paramFloat) {
        if (this.fixedRect == null)
            this.fixedRect = new byte[16];
        setBottom(Math.abs(paramFloat) + getYF());
        setBottom(Math.abs(paramFloat) + getY());
    }

    public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        setX(paramInt1);
        setY(paramInt2);
        setWidth(paramInt3);
        setHeight(paramInt4);
    }

    public void reshape(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        setX(paramFloat1);
        setY(paramFloat2);
        setWidth(paramFloat3);
        setHeight(paramFloat4);
    }

    public void move(int paramInt1, int paramInt2) {
        setX(paramInt1);
        setY(paramInt2);
    }

    public void move(float paramFloat1, float paramFloat2) {
        setX(paramFloat1);
        setY(paramFloat2);
    }

    public void translate(int paramInt1, int paramInt2) {
        move(getX() + paramInt1, getY() + paramInt2);
    }

    public void translate(float paramFloat1, float paramFloat2) {
        move(getXF() + paramFloat1, getYF() + paramFloat2);
    }

    public void resize(int paramInt1, int paramInt2) {
        reshape(getX(), getY(), paramInt1, paramInt2);
    }

    public void resize(float paramFloat1, float paramFloat2) {
        reshape(getXF(), getYF(), paramFloat1, paramFloat2);
    }

    public boolean inside(int paramInt1, int paramInt2) {
        return (paramInt1 >= getX()) && (paramInt1 < getRight()) && (paramInt2 >= getY()) && (paramInt2 < getBottom());
    }

    public boolean inside(float paramFloat1, float paramFloat2) {
        return (paramFloat1 >= getXF()) && (paramFloat1 < getRightF()) && (paramFloat2 >= getYF())
               && (paramFloat2 < getBottomF());
    }

    public boolean intersects(QDRect paramQDRect) {
        if ((this.fixedRect != null) && (paramQDRect.fixedRect != null)) {
            return (paramQDRect.getRightF() >= getXF()) && (paramQDRect.getBottomF() >= getYF())
                   && (paramQDRect.getXF() <= getRightF()) && (paramQDRect.getYF() <= getBottomF());
        }

        return (paramQDRect.getRight() >= getX()) && (paramQDRect.getBottom() >= getY()) && (paramQDRect.getX() <= getRight())
               && (paramQDRect.getY() <= getBottom());
    }

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
     * @deprecated
     */
    public void add(Point paramPoint) {
        add(paramPoint.x, paramPoint.y);
    }

    public void add(QDPoint paramQDPoint) {
        if (paramQDPoint.floatPt == null)
            add(paramQDPoint.getX(), paramQDPoint.getY());
        else {
            add(paramQDPoint.getXF(), paramQDPoint.getYF());
        }
    }

    public void grow(int paramInt1, int paramInt2) {
        setX(getX() - paramInt1);
        setY(getY() - paramInt2);
        setWidth(getWidth() + paramInt1 * 2);
        setHeight(getHeight() + paramInt2 * 2);
    }

    public void grow(float paramFloat1, float paramFloat2) {
        setX(getXF() - paramFloat1);
        setY(getYF() - paramFloat2);
        setWidth(getWidthF() + paramFloat1 * 2.0F);
        setHeight(getHeightF() + paramFloat2 * 2.0F);
    }

    public boolean isEmpty() {
        return (getWidth() <= 0) || (getHeight() <= 0);
    }

    public int hashCode() {
        return getX() ^ getY() * 37 ^ getWidth() * 43 ^ getHeight() * 47;
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof QDRect)) {
            QDRect localQDRect = (QDRect) paramObject;
            return (localQDRect.getXF() == getXF()) && (localQDRect.getYF() == getYF())
                   && (localQDRect.getRightF() == getRightF()) && (localQDRect.getBottomF() == getBottomF());
        }

        return false;
    }

    public String toString() {
        return super.toString() + "[x=" + getXF() + ",y=" + getYF() + ",width=" + getWidthF() + ",height=" + getHeightF() + "]";
    }

    public Object clone() {
        return copy();
    }

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

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/QDRect
 * .class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
