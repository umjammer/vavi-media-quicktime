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

/**
 * Describes the MIDI data that is passed by note allocation calls.
 */
public final class MusicMIDIPacket extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 255;

    static final long serialVersionUID = -3977273754888819406L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(MusicMIDIPacket.class);
                    }
                });
            }
        }.establish();
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[255];
        paramObjectInputStream.read(getBytes());
        setShortInArray(getBytes(), 0, EndianOrder.flipBigEndianToNative16(getShortFromArray(getBytes(), 0)));
        setIntInArray(getBytes(), 2, EndianOrder.flipBigEndianToNative32(getIntFromArray(getBytes(), 2)));
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[255];
        System.arraycopy(getBytes(), 0, arrayOfByte, 0, 255);
        setShortInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 0)));
        setIntInArray(arrayOfByte, 2, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), 2)));
        paramObjectOutputStream.write(arrayOfByte);
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
    public MusicMIDIPacket(byte[] paramArrayOfByte) {
        this();
        setMIDIData(paramArrayOfByte);
    }

    /**
     * Gets the current MIDI data. The length of the packet is the length of the
     * data array that is returned
     * @return the data array containing MIDI data
     */
    public byte[] getMIDIData() {
        int i = getShortAt(0);
        byte[] arrayOfByte = new byte[i];
        getBytesAt(6, i, arrayOfByte, 0);
        return arrayOfByte;
    }

    /**
     * Sets the current MIDI data. The length of the packet is the length of the
     * data array that is supplied. The maximum length of the data packet is 249
     * bytes - a longer byte array will be truncated to the first 249 bytes
     * @param mData the MIDI data
     */
    public void setMIDIData(byte[] paramArrayOfByte) {
        int i = paramArrayOfByte.length > 249 ? 249 : paramArrayOfByte.length;
        setShortAt(0, (short) i);
        setIntAt(2, 0);
        setBytesAt(6, i, paramArrayOfByte, 0);
    }

    /**
     * Sets the current MIDI data byte at index to the supplied value.
     * @param index the data byte to set
     * @param val the new value
     */
    public void setDataByte(int paramInt1, int paramInt2) {
        if (paramInt1 < getShortAt(0)) {
            setByteAt(paramInt1 + 6, (byte) paramInt2);
        }
    }

    /**
     * Gets the reserved field setting. Will contain zero or one of the kMusicPacket.. constants
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
    public Object clone() {
        return new MusicMIDIPacket(this.bytes);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /MusicMIDIPacket.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
