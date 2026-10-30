package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * The TimeCode definition class contains timecode formatting information.
 */
public final class TimeCodeDef extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 14;

    @Serial
    private static final long serialVersionUID = -1950218889120312012L;

    TimeCodeDef(int ptr) {
        super(kNativeSize);
        copyPointerToArray(ptr, 0, getBytes(), 0, kNativeSize);
    }

    /**
     * Creates a TimeCodeDef object which has no initial settings.
     */
    public TimeCodeDef() {
        super(kNativeSize);
    }

    private TimeCodeDef(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(getBytes());
        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        System.arraycopy(getBytes(), 0, movie, 0, kNativeSize);
        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(movie, i, k);
        }
        out.write(movie);
    }

    /**
     * Returns the current flag settings.
     * @return flags
     */
    public int getFlags() {
        return getIntAt(0);
    }

    /**
     * Sets the flags - dropFrame, etc
     * @param flags new flag value
     */
    public void setFlags(int flags) {
        setIntAt(0, flags);
    }

    /**
     * Returns the time scale in terms of the frameDuration.
     * TimeScale / frameDuration = actual number of frames per second
     * @return time scale
     */
    public int getTimeScale() {
        return getIntAt(4);
    }

    /**
     * Sets the time scale in terms of the frameDuration.
     * TimeScale / frameDuration = actual number of frames per second
     * @param timeScale the new time scale value
     */
    public void setTimeScale(int timeScale) {
        setIntAt(4, timeScale);
    }

    /**
     * Returns the duration of each frame.
     * TimeScale / frameDuration = actual number of frames per second
     * @return the current frame duration
     */
    public int getFrameDuration() {
        return getIntAt(8);
    }

    /**
     * Sets the duration of each frame.
     * TimeScale / frameDuration = actual number of frames per second
     * @param frameDuration new frame duration value
     */
    public void setFrameDuration(int frameDuration) {
        setIntAt(8, frameDuration);
    }

    /**
     * Returns the number of frames per second as the closest integral value. The
     * actual frames per second are found by the following:
     * TimeScale / frameDuration = actual number of frames per second
     * @return the specified, integral, frames per second
     */
    public int getFramesPerSecond() {
        return this.bytes[12];
    }

    /**
     * Sets the number of frames per second as the closest integral value. The
     * actual frames per second are found by the following:
     * TimeScale / frameDuration = actual number of frames per second
     * @param fps the specified, integral, frames per second
     */
    public void setFramesPerSecond(int fps) {
        this.bytes[12] = ((byte) fps);
    }

    /**
     * String representation of class.
     */
    public String toString() {
        return getClass().getName() + "[fps=" + getFramesPerSecond() + "dropFrame=" + ((getFlags() & 0x1) == 1) + "]";
    }

    /**
     * Makes a copy of a object.
     */
    @Override
    public Object clone() {
        return new TimeCodeDef(this.bytes);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
