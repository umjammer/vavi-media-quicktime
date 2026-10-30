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
    public static IntEncodedImage fromIntArray(int[] ints) {
        return new IntEncodedImage(ints);
    }

    /**
     * This will make a ByteEncodedImage from an array of ints. It will not copy
     * the int array but creates a proxy for the same ints. It will set the
     * row bytes as specified.
     */
    public static IntEncodedImage fromIntArray(int[] ints, int rowBytes) {
        return new IntEncodedImage(ints, rowBytes);
    }

    /**
     * Creates a IntEncodedImage from raw data. It will copy the data
     */
    public IntEncodedImage(RawEncodedImage rawEncodedImage) {
        int i = rawEncodedImage.getSize();
        if (i % 4 != 0)
            i++;
        this.ints = new int[i];
        rawEncodedImage.copyToArray(0, this.ints, 0, rawEncodedImage.getSize());
        this.rowBytes = rawEncodedImage.getRowBytes();
    }

    /**
     * Creates a new IntEncodedImage of specified number of ints.
     */
    public IntEncodedImage(int size) {
        this(size, -1);
    }

    /**
     * Creates a new IntEncodedImage of specified number of ints.
     */
    public IntEncodedImage(int size, int rowBytes) {
        this.ints = new int[size];
        rowBytes = rowBytes;
    }

    private IntEncodedImage(int[] ints) {
        this(ints, -1);
    }

    private IntEncodedImage(int[] ints, int rowBytes) {
        this.ints = ints;
        this.rowBytes = rowBytes;
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
    public byte getByte(int offset) {
        if ((offset > getSize()) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getByteFromArray(this.ints, offset);
    }

    /**
     * Returns the short at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public short getShort(int offset) {
        if ((offset > getSize() - 1) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getShortFromArray(this.ints, offset);
    }

    /**
     * Returns the int at the specified offset.
     * The offset is specified in bytes into the encoded image object.
     */
    @Override
    public int getInt(int offset) {
        if ((offset > getSize() - 3) || (offset < 0))
            throw new ArrayIndexOutOfBoundsException();
        return getIntFromArray(this.ints, offset);
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj != null) && ((obj instanceof IntEncodedImage))) {
            int[] arrayOfInt = ((IntEncodedImage) obj).ints;
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
        int[] ints = new int[this.ints.length];
        System.arraycopy(this.ints, 0, ints, 0, this.ints.length);
        return new IntEncodedImage(ints, this.rowBytes);
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return getClass().getName();
    }

    private static byte getByteFromArray(int[] array, int offset) {
        return MemoryManager.getByteFromArray(array, offset);
    }

    private static short getShortFromArray(int[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(int[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }
}
