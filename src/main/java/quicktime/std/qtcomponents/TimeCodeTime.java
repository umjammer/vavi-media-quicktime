package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quicktime.util.QTByteObject;

/**
 * The TimeCodeTime class represents time as HH:MM:SS:FF.
 * No range checking is done on values set, and no frame rate is known.
 * It is purely an information class
 */
public final class TimeCodeTime extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 4;

    static final long serialVersionUID = -6484583375141374076L;

    /**
     * Creates a TimeCodeDef object which has no initial settings.
     * @param hh hours value
     * @param mm minutes value
     * @param ss seconds value
     * @param ff frames value
     */
    public TimeCodeTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this();
        setHours(paramInt1);
        setMinutes(paramInt2);
        setSeconds(paramInt3);
        setFrames(paramInt4);
    }

    /**
     * Creates a TimeCodeTime object with values set to 0.
     */
    public TimeCodeTime() {
        super(kNativeSize);
    }

    /**
     * This is packed format that is referred to as the counter representation
     * of the TimeCodeTime structure.
     * @param counter in packed format HH:MM:SS:FF
     */
    public TimeCodeTime(int paramInt) {
        this();
        setHours((paramInt & 0xFF000000) >>> 24);
        setMinutes((paramInt & 0xFF0000) >>> 16);
        setSeconds((paramInt & 0xFF00) >>> 8);
        setFrames(paramInt & 0xFF);
    }

    private TimeCodeTime(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[kNativeSize];
        paramObjectInputStream.read(getBytes());
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    /**
     * Returns the TimeCodeTime as a counter.
     * @return an int
     */
    public int toCounter() {
        int i = getHours();
        int j = getMinutes();
        int k = getSeconds();
        int m = getFrames();
        return (0xFF & i) << 24 | (0xFF & j) << 16 | (0xFF & k) << 8 | 0xFF & m;
    }

    /**
     * Returns the current hours settings.
     * @return hours value
     */
    public int getHours() {
        return getByteAt(0);
    }

    /**
     * Sets the hours value
     * @param hh hours value
     */
    public void setHours(int paramInt) {
        setByteAt(0, (byte) paramInt);
    }

    /**
     * Returns the current minutes settings.
     * @return minutes value
     */
    public int getMinutes() {
        return getByteAt(1);
    }

    /**
     * Sets the minutes value
     * @param mm minutes value
     */
    public void setMinutes(int paramInt) {
        setByteAt(1, (byte) paramInt);
    }

    /**
     * Returns the current seconds settings.
     * @return seconds value
     */
    public int getSeconds() {
        return getByteAt(2);
    }

    /**
     * Sets the seconds value
     * @param ss seconds value
     */
    public void setSeconds(int paramInt) {
        setByteAt(2, (byte) paramInt);
    }

    /**
     * Returns the current frames settings.
     * @return frames value
     */
    public int getFrames() {
        return getByteAt(3);
    }

    /**
     * Sets the frames value
     * @param ff frames value
     */
    public void setFrames(int paramInt) {
        setByteAt(3, (byte) paramInt);
    }

    /**
     * String representation of class.
     */
    public String toString() {
        return getClass().getName() + "[" + getHours() + ":" + getMinutes() + ":" + getSeconds() + ":" + getFrames() + "]";
    }

    /**
     * Makes a copy of a object.
     */
    public Object clone() {
        return new TimeCodeTime(getBytes());
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TimeCodeTime.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
