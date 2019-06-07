package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;

public final class SynthesizerDescription extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    private static final int kNativeSize = 132;

    static final long serialVersionUID = -3131954178923067174L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SynthesizerDescription.class);
                    }
                });
            }
        }.establish();
    }

    SynthesizerDescription() {
        super(132);
    }

    private SynthesizerDescription(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[132];
        byte[] arrayOfByte = new byte[132];
        paramObjectInputStream.read(arrayOfByte);
        int i = getIntFromArray(arrayOfByte, 0);
        int j = EndianOrder.flipBigEndianToNative32(i);
        setIntInArray(getBytes(), 0, j);
        System.arraycopy(arrayOfByte, 4, getBytes(), 0, 32);
        for (int k = 36; k < 116; k += 4) {
            int m = getIntFromArray(arrayOfByte, k);
            int n = EndianOrder.flipBigEndianToNative32(m);
            setIntInArray(getBytes(), k, n);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[132];
        int i = getIntFromArray(getBytes(), 0);
        int j = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(arrayOfByte, 0, j);
        System.arraycopy(getBytes(), 4, arrayOfByte, 0, 32);
        for (int k = 36; k < 116; k += 4) {
            int m = getIntFromArray(getBytes(), k);
            int n = EndianOrder.flipNativeToBigEndian32(m);
            setIntInArray(arrayOfByte, k, n);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * synthesizer type (must be same as component subtype)
     */
    public int getSynthesizerType() {
        return getIntAt(0);
    }

    /**
     * Return text name of synthesizer type
     */
    public String getName() throws UtilException {
        return getPStringAt(4);
    }

    /**
     * Information about how the synthesizer works.
     */
    public int getFlags() {
        return getIntAt(36);
    }

    /**
     * maximum polyphony
     */
    public int getVoiceCount() {
        return getIntAt(40);
    }

    /**
     * maximum multi-timbrality (and midi channels)
     */
    public int getPartCount() {
        return getIntAt(44);
    }

    /**
     * non gm, built in (rom) instruments only
     */
    public int getInstrumentCount() {
        return getIntAt(48);
    }

    /**
     * plus n-more are user modifiable
     */
    public int getModifiableInstrumentCount() {
        return getIntAt(52);
    }

    /**
     * (midi device only) which channels device always uses
     */
    public int getChannelMask() {
        return getIntAt(56);
    }

    /**
     * maximum multi-timbrality of drum parts
     */
    public int getDrumPartCount() {
        return getIntAt(60);
    }

    /**
     * non gm, built in (rom) drumkits only
     */
    public int getDrumCount() {
        return getIntAt(64);
    }

    /**
     * plus n-more are user modifiable
     */
    public int getModifiableDrumCount() {
        return getIntAt(68);
    }

    /**
     * (midi device only) which channels device always uses
     */
    public int getDrumChannelMask() {
        return getIntAt(72);
    }

    /**
     * number of audio outputs (usually two)
     */
    public int getOutputCount() {
        return getIntAt(76);
    }

    /**
     * response time in µSec
     */
    public int getLatency() {
        return getIntAt(80);
    }

    /**
     * Returns true if controller number (1->128) is available.
     */
    public boolean controllerAvailable(int paramInt) {
        return doMask(paramInt, 84);
    }

    /**
     * Return true if the general MIDI instrument (1->128) is available.
     */
    public boolean gmInstrumentAvailable(int paramInt) {
        return doMask(paramInt, 100);
    }

    /**
     * Return true if the general MIDI drum kit (1->128) is available.
     */
    public boolean gmDrumAvailable(int paramInt) {
        return doMask(paramInt, 116);
    }

    private final boolean doMask(int paramInt1, int paramInt2) {
        int i = paramInt1 < 116 ? 64 : paramInt1 < 100 ? 32 : paramInt1 < 84 ? 0 : 96;
        int j = paramInt1 == 0 ? 0 : 1 << --paramInt1 - i;
        return (j & getIntAt(paramInt2 + i / 32)) != 0;
    }

    /**
     * String representation of class.
     */
    public String toString() {
        return getClass().getName() + "[synthType=" + QTUtils.fromOSType(getSynthesizerType()) + "]";
    }

    /**
     * Return a copy of the current class.
     */
    public Object clone() {
        return new SynthesizerDescription(this.bytes);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /SynthesizerDescription.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
