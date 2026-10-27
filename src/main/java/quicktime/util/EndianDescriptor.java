package quicktime.util;

import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTRuntimeException;

/**
 * An EndianDescriptor is used to describe the flipping requirements of a
 * particular data-structure. It has three modes which are determined by the
 * format flag which is provided in the constructor:
 * kFlipSpecifiedFields is used when describing an object where either
 * only some of the data should be flipped or the fields are different sizes
 * and thus should be flipped according to their size. Only when a descriptor is in
 * this state can a profile of the endian requirements be built up using the EndianFlipSpec
 * class.
 * The data is consistent for the entire size of the object, in which case
 * the format describes the size of the flipping that should be applied
 * The data should NOT be flipped at all
 *
 * An application can build EndianDescriptor objects for any custom data structures
 * that require endian flipping for consistency between platforms with different endian
 * orders and data files (like the QuickTime Movie File) that generally require data
 * to have a particular endian format.
 *
 * The EndianDescriptor objects is used with the QTUtils.endianFlip calls or the platform
 * sensitive EndianOrder.flip... methods.
 */
public class EndianDescriptor {

    /**
     * Describes the modal state of the EndianDescriptor object. Only when
     * an EndianDescriptor's format is kFlipSpecifiedFields can EndianFlipSpec
     * objects be added
     */
    public static final int kFlipSpecifiedFields = 0;

    /**
     * Describes the modal state of the EndianDescriptor object. Only when
     * an EndianDescriptor's format is kFlipSpecifiedFields can EndianFlipSpec
     * objects be added
     */
    public static final int kFlipAllFields16 = 1;

    /**
     * Describes the modal state of the EndianDescriptor object. Only when
     * an EndianDescriptor's format is kFlipSpecifiedFields can EndianFlipSpec
     * objects be added
     */
    public static final int kFlipAllFields32 = 16;

    /**
     * Describes the modal state of the EndianDescriptor object. Only when
     * an EndianDescriptor's format is kFlipSpecifiedFields can EndianFlipSpec
     * objects be added
     */
    public static final int kFlipAllFields64 = 256;

    /**
     * Describes the modal state of the EndianDescriptor object. Only when
     * an EndianDescriptor's format is kFlipSpecifiedFields can EndianFlipSpec
     * objects be added
     */
    public static final int kFlipNoFields = 4096;

    /**
     * This object is used to describe data that should be flipped around 16bit size.
     */
    public static final EndianDescriptor flipAll16 = new EndianDescriptor(1);

    /**
     * This object is used to describe data that should be flipped around 32bit size.
     */
    public static final EndianDescriptor flipAll32 = new EndianDescriptor(16);

    /**
     * This object is used to describe data that should be flipped around 64bit size.
     */
    public static final EndianDescriptor flipAll64 = new EndianDescriptor(256);

    private final int flag;

    private Vector<EndianFlipSpec> vec;

    /**
     * Create an EndianDescriptor class with the supplied format.
     * @param formatFlag specifies the mode of the EndianDescriptor object.
     */
    public EndianDescriptor(int paramInt) {
        this.flag = paramInt;
    }

    /**
     * Returns the format of the EndianDescriptor object - which determines the
     * behaviour of the descriptor when used with the endianFlip methods.
     * @return an int
     */
    public int getFormatFlag() {
        return this.flag;
    }

    /**
     * An EndianDescriptor with the format kFlipSpecifiedFields, typically describes
     * data that is not contiguous in size of location. As such a more complex
     * description of the flipping requirements of this data must be built, and
     * the addition of EndianFlipSpecs is used to describe these requirements.
     * In this case an EndianDescriptor becomes a collection of EndianFlipSpec objects
     * that completely describe the flipping requirements of the target data object.
     *
     * EndianFlipSpecs are ordered in an EndianDescriptor by the offset of these
     * EndianFlipSpecs. When a candidate EndianFlipSpec is added to an EndianDescriptor
     * object it will only be added if it's offset is unique in the descriptor's collection
     * of flip spec members. If the offset already exists in the collection the new object is
     * not added and an exception is thrown.
     *
     * If and only if the format of the EndianDescriptor is kFlipSpecifiedFields
     * can an EndianFlipSpec be added to the EndianDescriptor.
     *
     * @param spec the new spec to add to the EndianDescriptor's collection.
     */
    public void addFlipSpec(EndianFlipSpec paramEndianFlipSpec) {
        if (this.flag != 0) {
            throw new QTRuntimeException("Can't specify endian flip with " + this.flag + " format flag");
        }
        if (this.vec == null) {
            this.vec = new Vector<>();
        }
        for (int i = 0; i < this.vec.size(); i++) {
            EndianFlipSpec localEndianFlipSpec = this.vec.elementAt(i);
            if (paramEndianFlipSpec.offset == localEndianFlipSpec.offset)
                throw new QTRuntimeException("Each offset must be unique");
            if (paramEndianFlipSpec.offset < localEndianFlipSpec.offset) {
                this.vec.insertElementAt(paramEndianFlipSpec, i);
                return;
            }
        }
        this.vec.addElement(paramEndianFlipSpec);
    }

    /**
     * Removes a specified EndianFlipSpec (if found) from the EndianDescriptor.
     */
    public void removeFlipSpec(EndianFlipSpec paramEndianFlipSpec) {
        if (this.vec != null) {
            this.vec.removeElement(paramEndianFlipSpec);
        }
    }

    /**
     * Returns an EndianFlipSpec at the specified offset, or null if no
     * flip spec is found in the collection at that offset.
     * @param offset the offset of the desired EndianFlipSpec
     * @return the EndianFlipSpec with the specified offset value or null if no
     *  spec with that offset value
     */
    public EndianFlipSpec getFlipSpec(int paramInt) {
        if (this.vec == null)
            return null;
        for (int i = 0; i < this.vec.size(); i++) {
            EndianFlipSpec localEndianFlipSpec = this.vec.elementAt(i);
            if (paramInt == localEndianFlipSpec.offset)
                return localEndianFlipSpec;
            if (localEndianFlipSpec.offset > paramInt)
                return null;
        }
        return null;
    }

    /**
     * Returns an Enumeration to iterate over the collection of EndianFlipSpecs
     * or null if there is no collection to iterate over
     */
    public Enumeration<EndianFlipSpec> flipSpecs() {
        return this.vec == null ? null : this.vec.elements();
    }

    /**
     * Return a String representation of this object.
     * @return a String
     */
    public String toString() {
        StringBuilder str = new StringBuilder(getClass().getName() + "[flipFlag=");
        switch(this.flag) {
            case 0:
                str.append("kFlipSpecifiedFields");
                break;
            case 1:
                str.append("kFlipAllFields16");
                break;
            case 16:
                str.append("kFlipAllFields32");
                break;
            case 256:
                str.append("kFlipAllFields64");
                break;
            case 4096:
                str.append("kFlipNoFields");
        }
        if (this.flag == 0) {
            str.append("[");
            Enumeration<EndianFlipSpec> e = flipSpecs();
            while (e.hasMoreElements()) str.append(e.nextElement()).append(",");
            str.append("]");
        }
        str.append("]");
        return str.toString();
    }
}
