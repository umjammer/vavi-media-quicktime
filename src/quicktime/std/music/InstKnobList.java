
package quicktime.std.music;

import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class InstKnobList extends QTByteObject {
    private static final int kNativeSize = 16;

    public InstKnobList() {
        super(16);
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(1));
        setKnobFlags(1);
        setIntAt(8, EndianOrder.flipNativeToBigEndian32(1));
        setIntAt(12, EndianOrder.flipNativeToBigEndian32(1));
    }

    public InstKnobList(int paramInt, InstKnob[] paramArrayOfInstKnob) {
        super(8 + paramArrayOfInstKnob.length * 8);
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(paramArrayOfInstKnob.length));
        setKnobFlags(paramInt);
        for (int i = 0; i < paramArrayOfInstKnob.length; i++) {
            setKnob(i, paramArrayOfInstKnob[i]);
        }
    }

    public int getKnobCount() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    public int getKnobFlags() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(4));
    }

    public void setKnobFlags(int paramInt) {
        setIntAt(4, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    public void setKnob(int paramInt, InstKnob paramInstKnob) {
        setIntAt(paramInt * 8 + 8, paramInstKnob.getNumber());
        setIntAt(paramInt * 8 + 12, paramInstKnob.getValue());
    }

    public InstKnob getKnob(int paramInt) {
        InstKnob localInstKnob = new InstKnob();
        localInstKnob._setNumber(getIntAt(paramInt * 8 + 8));
        localInstKnob._setValue(getIntAt(paramInt * 8 + 12));
        return localInstKnob;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /InstKnobList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
