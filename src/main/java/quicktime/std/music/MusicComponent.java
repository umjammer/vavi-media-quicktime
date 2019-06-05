
package quicktime.std.music;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.comp.Component;
import quicktime.util.QTUtils;


public final class MusicComponent extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(MusicComponent.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * @deprecated
     */
    public MusicComponent() throws QTException {
        this(0);
    }

    /**
     * @deprecated
     */
    public MusicComponent(int paramInt) throws QTException {
        super(1836413801, paramInt);
    }

    MusicComponent(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public SynthesizerDescription getDescription() throws StdQTException {
        SynthesizerDescription localSynthesizerDescription = new SynthesizerDescription();
        StdQTException.checkError(MusicGetDescription(_ID(), localSynthesizerDescription.getBytes()));

        return localSynthesizerDescription;
    }

    public AtomicInstrument getPartAtomicInstrument(int paramInt1, int paramInt2) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(MusicGetPartAtomicInstrument(_ID(), paramInt1, arrayOfInt, paramInt2));

        if (arrayOfInt[0] == 0)
            return null;
        return new AtomicInstrument(arrayOfInt[0], this);
    }

    public int getKnob(int paramInt) throws StdQTException {
        int i = MusicGetKnob(_ID(), paramInt);
        if ((i >>> 16 == 32768) && ((i | 0xFFFF) != 0))
            throw new StdQTException(i | 0xFFFF);
        return i;
    }

    public void setKnob(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(MusicSetKnob(_ID(), paramInt1, paramInt2));
    }

    public String getPartName(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[32];
        StdQTException.checkError(MusicGetPartName(_ID(), paramInt, arrayOfByte));

        return QTUtils.PString2String(arrayOfByte, 0);
    }

    public void setPartName(int paramInt, String paramString) throws StdQTException {
        StdQTException.checkError(MusicSetPartName(_ID(), paramInt, QTUtils.String2PString(paramString, 31)));
    }

    public void setMasterTune(float paramFloat) throws StdQTException {
        StdQTException.checkError(MusicSetMasterTune(_ID(), QTUtils.X2Fix(paramFloat)));
    }

    public float getMasterTune() throws StdQTException {
        int i = MusicGetMasterTune(_ID());
        if (i < 0)
            throw new StdQTException(i);
        return QTUtils.Fix2X(i);
    }

    public KnobDescription getInstrumentKnobDescrption(int paramInt) throws StdQTException {
        KnobDescription localKnobDescription = new KnobDescription();
        StdQTException.checkError(MusicGetInstrumentKnobDescription(_ID(), paramInt, localKnobDescription.getBytes()));

        return localKnobDescription;
    }

    public KnobDescription getDrumKnobDescrption(int paramInt) throws StdQTException {
        KnobDescription localKnobDescription = new KnobDescription();
        StdQTException.checkError(MusicGetDrumKnobDescription(_ID(), paramInt, localKnobDescription.getBytes()));

        return localKnobDescription;
    }

    public KnobDescription getKnobDescrption(int paramInt) throws StdQTException {
        KnobDescription localKnobDescription = new KnobDescription();
        StdQTException.checkError(MusicGetKnobDescription(_ID(), paramInt, localKnobDescription.getBytes()));

        return localKnobDescription;
    }

    private static native int MusicGetDescription(int paramInt, byte[] paramArrayOfByte);

    private static native int MusicGetPartAtomicInstrument(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);

    private static native int MusicGetPartName(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MusicSetPartName(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MusicGetKnob(int paramInt1, int paramInt2);

    private static native int MusicSetKnob(int paramInt1, int paramInt2, int paramInt3);

    private static native int MusicGetMasterTune(int paramInt);

    private static native int MusicSetMasterTune(int paramInt1, int paramInt2);

    private static native int MusicGetInstrumentKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MusicGetDrumKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MusicGetKnobDescription(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /MusicComponent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
