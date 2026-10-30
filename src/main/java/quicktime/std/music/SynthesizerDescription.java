package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


public final class SynthesizerDescription extends QTByteObject implements PrimitivesLib, Cloneable {

    private static final int kNativeSize = 132;

    @Serial
    private static final long serialVersionUID = -3131954178923067174L;

    SynthesizerDescription() {
        super(132);
    }

    private SynthesizerDescription(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        byte[] arrayOfByte = new byte[kNativeSize];
        in.read(arrayOfByte);
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        int i = getIntFromArray(getBytes(), 0);
        int j = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(movie, 0, j);
        System.arraycopy(getBytes(), 4, movie, 0, 32);
        for (int k = 36; k < 116; k += 4) {
            int m = getIntFromArray(getBytes(), k);
            int n = EndianOrder.flipNativeToBigEndian32(m);
            setIntInArray(movie, k, n);
        }
        out.write(movie);
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
    public boolean controllerAvailable(int controller) {
        return doMask(controller, 84);
    }

    /**
     * Return true if the general MIDI instrument (1->128) is available.
     */
    public boolean gmInstrumentAvailable(int instrument) {
        return doMask(instrument, 100);
    }

    /**
     * Return true if the general MIDI drum kit (1->128) is available.
     */
    public boolean gmDrumAvailable(int drum) {
        return doMask(drum, 116);
    }

    private boolean doMask(int number, int offset) {
        int i = number < 116 ? 64 : number < 100 ? 32 : number < 84 ? 0 : 96;
        int j = number == 0 ? 0 : 1 << --number - i;
        return (j & getIntAt(offset + i / 32)) != 0;
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
    @Override
    public Object clone() {
        return new SynthesizerDescription(this.bytes);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
