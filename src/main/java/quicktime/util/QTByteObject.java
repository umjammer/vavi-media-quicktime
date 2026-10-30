package quicktime.util;

import java.io.Serializable;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;


/**
 * Provides a representation of the native data structure as a byte array.
 * The class implements the Serializable interface and can thus be serialized.
 * However the byte[] member is marked as transient because typically a QTByteObject
 * is used to contain objects that have fields, such as shorts, ints, that are larger
 * than bytes. Moving these objects from big to little to big endian run-time architectures
 * can cause problems if the endian issues of these members are not taken care of by
 * the particular class of the object that is being serialized. Thus, any subclass of QTByteObject
 * that is to be serialized defines custome read and write object methods that will
 * ensure that the serialized data that is written out is in big endian format, and if required
 * will be flipped back to native format when read in.
 */
public class QTByteObject implements PrimitivesLib, Serializable {

    protected transient byte[] bytes;

    /**
     * This call will return a QTByteObject which becomes a proxy for the
     * specified byte array. It does not copy the byte array - but references it.
     *
     * @param ar the byte array to represent
     * @return a QTByteObject with the specified array as its target bytes
     */
    public static QTByteObject fromArray(byte[] ar) {
        QTByteObject qtByteObject = new QTByteObject();
        qtByteObject.bytes = ar;
        return qtByteObject;
    }

    protected QTByteObject(int size) {
        if (size > 0) {
            this.bytes = new byte[size];
        }
    }

    protected QTByteObject(byte[] bytes) {
        this(bytes.length);
        System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
    }

    protected final void setBytesAt(int offset, int length, byte[] srcArray, int srcOffset) {
        System.arraycopy(srcArray, srcOffset, this.bytes, offset, length);
    }

    protected final void getBytesAt(int offset, int length, byte[] destArray, int destOffset) {
        System.arraycopy(this.bytes, offset, destArray, destOffset, length);
    }

    /**
     * Returns the byte array containing the packed native object.
     *
     * @return the actual byte array NOT a copy
     */
    public final byte[] getBytes() {
        return this.bytes;
    }

    /**
     * Returns the size of the represented native object.
     *
     * @return size
     */
    public int getSize() {
        return this.bytes.length;
    }

    /**
     * Returns true if the two objects have the same value.
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj != null) && ((obj instanceof QTByteObject))) {
            byte[] arrayOfByte = ((QTByteObject) obj).bytes;
            if (arrayOfByte.length != this.bytes.length)
                return false;
            for (int i = 0; i < this.bytes.length; i++)
                if (this.bytes[i] != arrayOfByte[i])
                    return false;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName();
    }

    protected final byte getByteAt(int offset) {
        return this.bytes[offset];
    }

    protected final short getShortAt(int offset) {
        return getShortFromArray(this.bytes, offset);
    }

    protected final int getIntAt(int offset) {
        return getIntFromArray(this.bytes, offset);
    }

    protected final long getLongAt(int offset) {
        return getLongFromArray(this.bytes, offset);
    }

    protected final float getFloatAt(int offset) {
        return getFloatFromArray(this.bytes, offset);
    }

    protected final double getDoubleAt(int offset) {
        return getDoubleFromArray(this.bytes, offset);
    }

    protected final void setByteAt(int offset, byte value) {
        this.bytes[offset] = value;
    }

    protected final void setShortAt(int offset, short value) {
        setShortInArray(this.bytes, offset, value);
    }

    protected final void setIntAt(int offset, int value) {
        setIntInArray(this.bytes, offset, value);
    }

    protected final void setLongAt(int offset, long value) {
        setLongInArray(this.bytes, offset, value);
    }

    protected final void setFloatAt(int offset, float value) {
        setFloatInArray(this.bytes, offset, value);
    }

    protected final void setDoubleAt(int offset, double value) {
        setDoubleInArray(this.bytes, offset, value);
    }

    protected int strlen(int offset) {
        int i = 0;
        while (getByteAt(offset++) != 0) i++;
        return i;
    }

    protected String getCStringAt(int offset) throws UtilException {
        int i = strlen(offset);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] destArray = new byte[i];
        getBytesAt(offset, i, destArray, 0);
        return new String(destArray);
    }

    protected void setCStringAt(int offset, int length, String str) throws UtilException {
        length = Math.min(length, str.length());
        if (length + offset > getSize()) {
            throw new UtilException(-50);
        }
        byte[] srcArray = str.getBytes();
        setBytesAt(offset, length, srcArray, 0);
        setByteAt(offset + length, (byte) 0);
    }

    protected String getPStringAt(int offset) throws UtilException {
        int i = getByteAt(offset++);
        if (i > getSize()) {
            throw new UtilException(-50);
        }
        byte[] destArray = new byte[i];
        getBytesAt(offset, i, destArray, 0);
        return new String(destArray);
    }

    protected void setPStringAt(int offset, int length, String str) throws UtilException {
        length = Math.min(Math.min(length, str.length()), 255);
        if (length + offset > getSize()) {
            throw new UtilException(-50);
        }
        setByteAt(offset++, (byte) length);
        byte[] srcArray = str.getBytes();
        setBytesAt(offset, length, srcArray, 0);
    }

    private QTByteObject() {
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static long getLongFromArray(byte[] array, int offset) {
        return MemoryManager.getLongFromArray(array, offset);
    }

    private static float getFloatFromArray(byte[] array, int offset) {
        return MemoryManager.getFloatFromArray(array, offset);
    }

    private static double getDoubleFromArray(byte[] array, int offset) {
        return MemoryManager.getDoubleFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static void setLongInArray(byte[] array, int offset, long value) {
        MemoryManager.setLongInArray(array, offset, value);
    }

    private static void setFloatInArray(byte[] array, int offset, float value) {
        MemoryManager.setFloatInArray(array, offset, value);
    }

    private static void setDoubleInArray(byte[] array, int offset, double value) {
        MemoryManager.setDoubleInArray(array, offset, value);
    }
}
