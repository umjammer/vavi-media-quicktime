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
 * Presents information on the currently playing tune.
 * @see "QuickTime::TuneGetStatus"
 */
public final class TuneStatus extends QTByteObject implements PrimitivesLib {

    private static final int kNativeSize = 32;

    @Serial
    private static final long serialVersionUID = 7818564353002743338L;

    TuneStatus() {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        paramObjectInputStream.read(this.bytes);
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 12; i < 16; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        setIntInArray(this.bytes, 16, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, 16)));
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[kNativeSize];
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

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
