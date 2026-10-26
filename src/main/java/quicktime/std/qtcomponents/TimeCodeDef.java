package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;

/**
 * The TimeCode definition class contains timecode formatting information.
 */
public final class TimeCodeDef extends QTByteObject implements PrimitivesLib, Cloneable {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 14;

    static final long serialVersionUID = -1950218889120312012L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(TimeCodeDef.class);
                    }
                });
            }
        }.establish();
    }

    TimeCodeDef(int paramInt) {
        super(14);
        copyPointerToArray(paramInt, 0, getBytes(), 0, 14);
    }

    /**
     * Creates a TimeCodeDef object which has no initial settings.
     */
    public TimeCodeDef() {
        super(14);
    }

    private TimeCodeDef(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[14];
        paramObjectInputStream.read(getBytes());
        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[14];
        System.arraycopy(getBytes(), 0, arrayOfByte, 0, 14);
        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }
        paramObjectOutputStream.write(arrayOfByte);
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
    public void setFlags(int paramInt) {
        setIntAt(0, paramInt);
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
    public void setTimeScale(int paramInt) {
        setIntAt(4, paramInt);
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
    public void setFrameDuration(int paramInt) {
        setIntAt(8, paramInt);
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
    public void setFramesPerSecond(int paramInt) {
        this.bytes[12] = ((byte) paramInt);
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
    public Object clone() {
        return new TimeCodeDef(this.bytes);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}

