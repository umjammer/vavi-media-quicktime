package quicktime.std.music;

import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * A KnobDescription contains information about a particular knob
 * that is used by a particular MusicComponent
 */
public class KnobDescription extends QTByteObject {

    public static final int kNativeSize = 84;

    KnobDescription() {
        super(84);
    }

    /**
     * @return the name of the knob
     */
    public String getName() {
        return QTUtils.PString2String(getBytes(), 0);
    }

    /**
     * @return the lowest value of the knob
     */
    public int getLowValue() {
        return getIntAt(64);
    }

    /**
     * @return the highest value of the knob
     */
    public int getHighValue() {
        return getIntAt(68);
    }

    /**
     * @return the default value of the knob
     */
    public int getDefaultValue() {
        return getIntAt(72);
    }

    /**
     * @return the knob's flags
     */
    public int getFlags() {
        return getIntAt(76);
    }

    /**
     * @return the ID of the knob - to use in other calls to the MusicComponent
     */
    public int getKnobID() {
        return getIntAt(80);
    }
}
