package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * Describes the MIDI data that is passed by note allocation calls.
 */
public final class MusicMIDIPacket extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 255;

    @Serial
    private static final long serialVersionUID = -3977273754888819406L;

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[255];
        in.read(getBytes());
        setShortInArray(getBytes(), 0, EndianOrder.flipBigEndianToNative16(getShortFromArray(getBytes(), 0)));
        setIntInArray(getBytes(), 2, EndianOrder.flipBigEndianToNative32(getIntFromArray(getBytes(), 2)));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[255];
        System.arraycopy(getBytes(), 0, movie, 0, 255);
        setShortInArray(movie, 0, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 0)));
        setIntInArray(movie, 2, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), 2)));
        out.write(movie);
    }

    /**
     * The MusicMIDIPacket is used to contain MIDI messages that can be sent.
     * out through QT
     * QuickTime__MusicMIDIPacket
     */
    public MusicMIDIPacket() {
        super(255);
    }

    /**
     * The incoming byte[] contains MIDI data only.
     */
    public MusicMIDIPacket(byte[] mData) {
        this();
        setMIDIData(mData);
    }

    /**
     * Gets the current MIDI data. The length of the packet is the length of the
     * data array that is returned
     *
     * @return the data array containing MIDI data
     */
    public byte[] getMIDIData() {
        int i = getShortAt(0);
        byte[] destArray = new byte[i];
        getBytesAt(6, i, destArray, 0);
        return destArray;
    }

    /**
     * Sets the current MIDI data. The length of the packet is the length of the
     * data array that is supplied. The maximum length of the data packet is 249
     * bytes - a longer byte array will be truncated to the first 249 bytes
     *
     * @param mData the MIDI data
     */
    public void setMIDIData(byte[] mData) {
        int i = Math.min(mData.length, 249);
        setShortAt(0, (short) i);
        setIntAt(2, 0);
        setBytesAt(6, i, mData, 0);
    }

    /**
     * Sets the current MIDI data byte at index to the supplied value.
     *
     * @param index the data byte to set
     * @param val   the new value
     */
    public void setDataByte(int index, int val) {
        if (index < getShortAt(0)) {
            setByteAt(index + 6, (byte) val);
        }
    }

    /**
     * Gets the reserved field setting. Will contain zero or one of the kMusicPacket.. constants
     *
     * @return the reserved field value
     */
    public int getReserved() {
        return getIntAt(2);
    }

    /**
     * Return a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[length=" + (getShortAt(0) - 6) + "]";
    }

    /**
     * Make a copy of the MusicMIDIPacket.
     */
    @Override
    public Object clone() {
        return new MusicMIDIPacket(this.bytes);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }
}
