package quicktime.qd;

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

/**
 * This class represents a width and height dimension.
 */
public class QDDimension extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    private static final int kNativeSize = 8;

    private static final long serialVersionUID = -3790051028201084503L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QDDimension.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * Returns the EndianDescriptor for QDRect Rect.
     * @return an EndianDescriptor
     */
    public static final EndianDescriptor getEndianDescriptor() {
        return EndianDescriptor.flipAll32;
    }

    /**
     * Creates an instance of QDDimension with a width
     * of zero and a height of zero.
     */
    public QDDimension() {
        this(0, 0);
    }

    /**
     * Constructs a QDDimension and initializes it to the specified width and
     * specified height.
     * @param width the specified width dimension
     * @param height the specified height dimension
     */
    public QDDimension(int paramInt1, int paramInt2) {
        super(8);
        setWidth(paramInt1);
        setHeight(paramInt2);
    }

    /**
     * Constructs a QDDimension and initializes it to the specified width and
     * specified height.
     * @param width the specified width dimension
     * @param height the specified height dimension
     */
    public QDDimension(float paramFloat1, float paramFloat2) {
        super(8);
        setWidth(paramFloat1);
        setHeight(paramFloat2);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[8];
        byte[] arrayOfByte = new byte[12];
        paramObjectInputStream.read(arrayOfByte);
        int i = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0));
        if (i == 1) {
            setIntInArray(getBytes(), 0, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 4)));
            setIntInArray(getBytes(), 4, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 8)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[12];
        setIntInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian32(1));
        setIntInArray(arrayOfByte, 4, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, 0)));
        setIntInArray(arrayOfByte, 8, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, 4)));
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Gets the width of this QDDimension object.
     * @return the width
     */
    public int getWidth() {
        return (int) getFloatAt(0);
    }

    /**
     * Gets the width of this QDDimension object.
     * @return the width
     */
    public float getWidthF() {
        return getFloatAt(0);
    }

    /**
     * Gets the height of this QDDimension object.
     * @return the height
     */
    public int getHeight() {
        return (int) getFloatAt(4);
    }

    /**
     * Gets the height of this QDDimension object.
     * @return the height
     */
    public float getHeightF() {
        return getFloatAt(4);
    }

    /**
     * Set the width.
     * @param width the new width for this QDDimension object.
     */
    public void setWidth(int paramInt) {
        setFloatAt(0, paramInt);
    }

    /**
     * Set the width.
     * @param width the new width for this QDDimension object.
     */
    public void setWidth(float paramFloat) {
        setFloatAt(0, paramFloat);
    }

    /**
     * Set the height.
     * @param height the new height for this QDDimension object.
     */
    public void setHeight(int paramInt) {
        setFloatAt(4, paramInt);
    }

    /**
     * Set the height.
     * @param height the new height for this QDDimension object.
     */
    public void setHeight(float paramFloat) {
        setFloatAt(4, paramFloat);
    }

    /**
     * Return a copy of the QDDimension
     * @return a QDDimension
     */
    public Object clone() {
        return copy();
    }

    /**
     * Returns a copy of the QDDimension with the correct return type.
     * @return a QDDimension
     */
    public QDDimension copy() {
        return new QDDimension(getWidth(), getHeight());
    }

    /**
     * Checks whether two dimension objects have equal values.
     */
    public boolean equals(Object paramObject) {
        if (paramObject == this)
            return true;
        if ((paramObject instanceof QDDimension)) {
            QDDimension localQDDimension = (QDDimension) paramObject;
            return (getWidth() == localQDDimension.getWidth()) && (getHeight() == localQDDimension.getHeight());
        }
        return false;
    }

    /**
     * Returns a string representation of this object
     * @return a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[width=" + getWidth() + ",height=" + getHeight() + "]";
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

