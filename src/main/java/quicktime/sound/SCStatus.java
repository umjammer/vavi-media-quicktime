package quicktime.sound;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * Sound Channel Status object
 */
public final class SCStatus extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 24;

    SCStatus() {
        super(kNativeSize);
    }

    private SCStatus(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(this.bytes);
        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(this.bytes, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(this.bytes, i, k);
        }
        int i = getIntFromArray(this.bytes, 16);
        setIntInArray(this.bytes, 16, EndianOrder.flipBigEndianToNative32(i));
        int j = getIntFromArray(this.bytes, 20);
        setIntInArray(this.bytes, 20, EndianOrder.flipBigEndianToNative32(i));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(movie, i, k);
        }
        for (int i = 12; i < 16; i++) {
            movie[i] = getByteFromArray(getBytes(), i);
        }
        int i = getChannelAttributes();
        setIntInArray(movie, 16, EndianOrder.flipNativeToBigEndian32(i));
        int j = getCPULoad();
        setIntInArray(movie, 20, EndianOrder.flipNativeToBigEndian32(j));
        out.write(movie);
    }

    /**
     * Retrieves the start time.
     *
     * @return start time of sound in sound channel
     */
    public int getStartTime() {
        return getIntAt(0);
    }

    /**
     * Retrieves the end time.
     *
     * @return end time of sound in sound channel
     */
    public int getEndTime() {
        return getIntAt(4);
    }

    /**
     * Retrieves the current time.
     *
     * @return current time of sound in sound channel
     */
    public int getCurrentTime() {
        return getIntAt(8);
    }

    /**
     * Returns the busy status of the sound channel
     *
     * @return true if the sound channel is busy
     */
    public boolean getChannelBusy() {
        int i = getByteAt(12);
        return i != 0;
    }

    /**
     * Retrieves pause status of the sound channel
     *
     * @return true if the channel is busy
     */
    public boolean getChannelPaused() {
        int i = getByteAt(14);
        return i != 0;
    }

    /**
     * Retrieves the attributes of the sound channel
     *
     * @return attributes of the channel
     */
    public int getChannelAttributes() {
        return getIntAt(16);
    }

    /**
     * Retrieves the cpu load of the sound channel
     *
     * @return cpu load of the channel
     */
    public int getCPULoad() {
        return getIntAt(20);
    }

    /**
     * clones the current SCStatus object
     *
     * @return the cloned object
     */
    @Override
    public Object clone() {
        return new SCStatus(this.bytes);
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[startTime=" + getStartTime() + ",endTime=" + getEndTime() + ",currentTime" + getCurrentTime() + ",channelBusy" + getChannelBusy() + ",channelPaused" + getChannelPaused() + "]";
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static byte getByteFromArray(byte[] array, int offset) {
        return MemoryManager.getByteFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }
}
