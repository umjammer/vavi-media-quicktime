
package quicktime.std.music;

import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public class KnobDescription extends QTByteObject {
    public static final int kNativeSize = 84;

    KnobDescription() {
        super(84);
    }

    public String getName() {
        return QTUtils.PString2String(getBytes(), 0);
    }

    public int getLowValue() {
        return getIntAt(64);
    }

    public int getHighValue() {
        return getIntAt(68);
    }

    public int getDefaultValue() {
        return getIntAt(72);
    }

    public int getFlags() {
        return getIntAt(76);
    }

    public int getKnobID() {
        return getIntAt(80);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /KnobDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
