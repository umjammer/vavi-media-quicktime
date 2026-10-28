package quicktime.qd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * This class represents a width and height dimension.
 */
public class QDDimension extends QTByteObject implements PrimitivesLib, Cloneable {

    private static final int kNativeSize = 8;

    @Serial
    private static final long serialVersionUID = -3790051028201084503L;

    /**
     * Returns the EndianDescriptor for QDRect Rect.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
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
    public QDDimension(int width, int height) {
        super(kNativeSize);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Constructs a QDDimension and initializes it to the specified width and
     * specified height.
     * @param width the specified width dimension
     * @param height the specified height dimension
     */
    public QDDimension(float width, float height) {
        super(kNativeSize);
        setWidth(width);
        setHeight(height);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        byte[] arrayOfByte = new byte[12];
        paramObjectInputStream.read(arrayOfByte);
        int i = EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 0));
        if (i == 1) {
            setIntInArray(getBytes(), 0, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 4)));
            setIntInArray(getBytes(), 4, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, 8)));
        }
    }

    @Serial
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
    public void setWidth(int width) {
        setFloatAt(0, width);
    }

    /**
     * Set the width.
     * @param width the new width for this QDDimension object.
     */
    public void setWidth(float width) {
        setFloatAt(0, width);
    }

    /**
     * Set the height.
     * @param height the new height for this QDDimension object.
     */
    public void setHeight(int height) {
        setFloatAt(4, height);
    }

    /**
     * Set the height.
     * @param height the new height for this QDDimension object.
     */
    public void setHeight(float height) {
        setFloatAt(4, height);
    }

    /**
     * Return a copy of the QDDimension
     * @return a QDDimension
     */
    @Override
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
        if ((paramObject instanceof QDDimension localQDDimension)) {
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

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
