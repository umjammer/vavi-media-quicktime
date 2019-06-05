
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


public final class MusicMIDIPacket extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

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

    public MusicMIDIPacket() {
        super(255);
    }

    public MusicMIDIPacket(byte[] paramArrayOfByte) {
        this();
        setMIDIData(paramArrayOfByte);
    }

    public byte[] getMIDIData() {
        int i = getShortAt(0);
        byte[] arrayOfByte = new byte[i];
        getBytesAt(6, i, arrayOfByte, 0);
        return arrayOfByte;
    }

    public void setMIDIData(byte[] paramArrayOfByte) {
        int i = paramArrayOfByte.length > 249 ? 249 : paramArrayOfByte.length;
        setShortAt(0, (short) i);
        setIntAt(2, 0);
        setBytesAt(6, i, paramArrayOfByte, 0);
    }

    public void setDataByte(int paramInt1, int paramInt2) {
        if (paramInt1 < getShortAt(0)) {
            setByteAt(paramInt1 + 6, (byte) paramInt2);
        }
    }

    public int getReserved() {
        return getIntAt(2);
    }

    public String toString() {
        return getClass().getName() + "[length=" + (getShortAt(0) - 6) + "]";
    }

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
