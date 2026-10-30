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
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(this.bytes);
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 12; i < 16; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        setIntInArray(this.bytes, 16, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, 16)));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        for (int i = 0; i < 12; i += 4) {
            setIntInArray(movie, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        for (int i = 12; i < 16; i += 2) {
            setShortInArray(movie, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        setIntInArray(movie, 16, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 16)));
        for (int i = 16; i < 32; i += 4) {
            setIntInArray(movie, i, 0);
        }
        out.write(movie);
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

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
