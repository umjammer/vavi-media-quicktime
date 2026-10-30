package quicktime.std.music;

import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;

/**
 * Contains a list of knobs.
 */
public final class InstKnobList extends QTByteObject {

    private static final int kNativeSize = 16;

    /**
     * Makes an instance of this class with one knob only.
     * The value of this knob and its number is set to one - this is
     * the default knob list setting with the flag set to kInstKnobMissingDefault
     */
    public InstKnobList() {
        super(16);
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(1));
        setKnobFlags(1);
        setIntAt(8, EndianOrder.flipNativeToBigEndian32(1));
        setIntAt(12, EndianOrder.flipNativeToBigEndian32(1));
    }

    /**
     * Makes a KnobList from the supplied array of knobs.
     * @param knobFlags the knobFlags
     * @param knobs the array of knobs
     */
    public InstKnobList(int knobFlags, InstKnob[] knobs) {
        super(8 + knobs.length * 8);
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(knobs.length));
        setKnobFlags(knobFlags);
        for (int i = 0; i < knobs.length; i++) {
            setKnob(i, knobs[i]);
        }
    }

    /**
     * The number of knobs in the list
     * @return <DD/>
     */
    public int getKnobCount() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    /**
     * The knob flags to use if the requested knob is not in the list.
     * @return <DD/>
     */
    public int getKnobFlags() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(4));
    }

    /**
     * The knob flags to use if the requested knob is not in the list.
     */
    public void setKnobFlags(int knobFlags) {
        setIntAt(4, EndianOrder.flipNativeToBigEndian32(knobFlags));
    }

    /**
     * Sets the specified knob at the specified index in the list.
     * The list is a zero based list.
     * @param knob the knob to set
     */
    public void setKnob(int index, InstKnob knob) {
        setIntAt(index * 8 + 8, knob.getNumber());
        setIntAt(index * 8 + 12, knob.getValue());
    }

    /**
     * Gets the knob at the specified index in the list.
     * The list is a zero based list.
     * @return a knob
     */
    public InstKnob getKnob(int index) {
        InstKnob knob = new InstKnob();
        knob._setNumber(getIntAt(index * 8 + 8));
        knob._setValue(getIntAt(index * 8 + 12));
        return knob;
    }
}
