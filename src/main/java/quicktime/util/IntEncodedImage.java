package quicktime.util;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;


/**
 * IntEncodedImage keep the encoded image data as an array of ints.
 */
public class IntEncodedImage implements PrimitivesLib, EncodedImage, Cloneable {

    private final int[] ints;

    private int rowBytes;

    /**
     * This will make a ByteEncodedImage from an array of ints. It will not copy
     * the int array but creates a proxy for the same ints.
     */
    public static IntEncodedImage fromIntArray(int[] paramArrayOfInt) {
        return new IntEncodedImage(paramArrayOfInt);
    }

    /**
     * This will make a ByteEncodedImage from an array of ints. It will not copy
     * the int array but creates a proxy for the same ints. It will set the
     * row bytes as specified.
     */
    public static IntEncodedImage fromIntArray(int[] paramArrayOfInt, int paramInt) {
        return new IntEncodedImage(paramArrayOfInt, paramInt);
    }

    /**
     * Creates a IntEncodedImage from raw data. It will copy the data
     */
    public IntEncodedImage(RawEncodedImage paramRawEncodedImage) {
        int i = paramRawEncodedImage.getSize();
        if (i % 4 != 0)
            i++;
        this.ints = new int[i];
        paramRawEncodedImage.copyToArray(0, this.ints, 0, paramRawEncodedImage.getSize());
        this.rowBytes = paramRawEncodedImage.getRowBytes();
    }

    /**
     * Creates a new IntEncodedImage of specified number of ints.
     */
    public IntEncodedImage(int paramInt) {
        this(paramInt, -1);
    }

    /**
     * Creates a new IntEncodedImage of specified number of ints.
     */
    public IntEncodedImage(int paramInt1, int paramInt2) {
        this.ints = new int[paramInt1];
        paramInt2 = paramInt2;
    }

    private IntEncodedImage(int[] paramArrayOfInt) {
        this(paramArrayOfInt, -1);
    }

    private IntEncodedImage(int[] paramArrayOfInt, int paramInt) {
        this.ints = paramArrayOfInt;
        this.rowBytes = paramInt;
    }

    /**
     * Returns the internal int array. This is NOT a copy so any alteration
     * of the int values will effect any usage of this object.
     */
    public int[] getInts() {
        return this.ints;
    }

    /**
     * Returns the size in bytes of the EncodedImage data
     */
    @Override
    public int getSize() {
        return this.ints.length * 4;
    }

    /**
     * Returns the byte at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public byte getByte(int paramInt) {
        if ((paramInt > getSize()) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteFromArray(this.ints, paramInt);
    }

    /**
     * Returns the short at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public short getShort(int paramInt) {
        if ((paramInt > getSize() - 1) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortFromArray(this.ints, paramInt);
    }

    /**
     * Returns the int at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public int getInt(int paramInt) {
        if ((paramInt > getSize() - 3) || (paramInt < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntFromArray(this.ints, paramInt);
    }

    /**
     * Returns either kRowBytesUnknown or the number of bytes per row
     * that the encoded image data is comprised of. This number will
     * at least be as big as width*pixelSize, and maybe larger.
     */
    @Override
    public int getRowBytes() {
        return this.rowBytes;
    }

    /**
     * Returns true if the two objects have the same value.
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject != null) && ((paramObject instanceof IntEncodedImage))) {
            int[] arrayOfInt = ((IntEncodedImage) paramObject).ints;
            if (arrayOfInt.length != this.ints.length)
                return false;
            for (int i = 0; i < this.ints.length; i++) if (this.ints[i] != arrayOfInt[i])
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns a copy of the current object
     */
    @Override
    public Object clone() {
        int[] arrayOfInt = new int[this.ints.length];
        System.arraycopy(this.ints, 0, arrayOfInt, 0, this.ints.length);
        return new IntEncodedImage(arrayOfInt, this.rowBytes);
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName();
    }

    private static byte getByteFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getByteFromArray(paramArrayOfInt, paramInt);
    }

    private static short getShortFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfInt, paramInt);
    }

    private static int getIntFromArray(int[] paramArrayOfInt, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfInt, paramInt);
    }
}
