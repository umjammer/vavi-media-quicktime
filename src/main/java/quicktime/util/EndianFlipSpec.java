package quicktime.util;

/**
 * This class is used to describe the fields of a data-structure that
 * should have its endian order flipped. The class is used to describe
 * a particular part of a data-structure that should be flipped around
 * a consistent size, eg. flip 3 16bit values from 2bytes into object n.
 *
 * The class is used when an EndianDescriptor is created that describe the
 * endian flipping requirements of a particular data-structure. An EndianDescriptor
 * can be built that contains a collection of EndianFlipSpec objects that
 * completely describe the endian flipping characteristics of the data. An
 * EndianFlipSpec is an individual specification for contiguous fields that
 * have the same flipping characteristics.
 *
 * A
 * @see quicktime.util.EndianDescriptor
 */
public class EndianFlipSpec {

    /**
     * These constants are used as a format flag to describe the size of
     * the value that the spec will flip around.
     */
    public static final int kFlip16BitValue = 2;

    /**
     * These constants are used as a format flag to describe the size of
     * the value that the spec will flip around.
     */
    public static final int kFlip32BitValue = 4;

    /**
     * These constants are used as a format flag to describe the size of
     * the value that the spec will flip around.
     */
    public static final int kFlip64BitValue = 8;

    final int offset;

    final int sizeFlag;

    final int num;

    /**
     * Creates an EndianFlipSpec with the given offset from the start of the
     * object it describes, the size of the flipping and the number of
     * the specified flippingSize objects that should be flipped.
     * @param offset the offset from the start of the object to be flipped
     * @param sizeFlag the size of the flipping that should be applied
     * @param num the number of sizeFlag flips that should be applied.
     */
    public EndianFlipSpec(int offset, int sizeFlag, int num) {
        this.offset = offset;
        this.sizeFlag = sizeFlag;
        this.num = num;
    }

    /**
     * The offset from the start of the object where this particular flip specification
     * should be applied.
     * @return an int
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * The size of the endian flipping that should be applied, ie. flipping around 16, 32
     * or 64 bit values.
     * @return an int
     */
    public int getSizeFlag() {
        return this.sizeFlag;
    }

    /**
     * The number of size flag values that should be flipped from the given offset.
     * @return an int
     */
    public int getNumberOfFlips() {
        return this.num;
    }

    /**
     * Returns true if the supplied object has the same values as this object.
     * @return a boolean
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if ((obj instanceof EndianFlipSpec endianFlipSpec)) {
            return (endianFlipSpec.offset == this.offset) && (endianFlipSpec.sizeFlag == this.sizeFlag) && (endianFlipSpec.num == this.num);
        }
        return false;
    }

    /**
     * Return a string representation of this object.
     * @return a String
     */
    public String toString() {
        return "flipSpec[offset=" + this.offset + ",sizeFlag=" + this.sizeFlag + ",num=" + this.num + "]";
    }
}
