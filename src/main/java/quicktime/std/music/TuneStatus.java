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
 * Presents information on the currently playing tune.
 * QuickTime::TuneGetStatus
 */
public final class TuneStatus extends QTByteObject implements PrimitivesLib {

    private static Object linkage;

    private static final int kNativeSize = 32;

    private static final long serialVersionUID = 7818564353002743338L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(TuneStatus.class);
                    }
                });
            }
        }.establish();
    }

    TuneStatus() {
        super(32);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[32];
        paramObjectInputStream.read(this.bytes);
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 12; i < 16; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        setIntInArray(this.bytes, 16, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, 16)));
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[32];
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        for (int i = 12; i < 16; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        setIntInArray(arrayOfByte, 16, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 16)));
        for (int i = 16; i < 32; i += 4) {
            setIntInArray(arrayOfByte, i, 0);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    int getTuneDataPtr() {
        return getIntAt(0);
    }

    /**
     * position within currently playing piece
     */
    public int getTunePosition() {
        return (getIntAt(4) - getIntAt(0)) / 4;
    }

    /**
     * current tune time
     */
    public int getTime() {
        return getIntAt(8);
    }

    /**
     * how many pieces queued up?
     */
    public int getQueueCount() {
        return getShortAt(12);
    }

    /**
     * How many more tunepieces can be queued
     */
    public int getQueueSpots() {
        return getShortAt(14);
    }

    /**
     * How much time is queued up? (can be very inaccurate)
     */
    public int getQueueTime() {
        return getIntAt(16);
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /TuneStatus.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
