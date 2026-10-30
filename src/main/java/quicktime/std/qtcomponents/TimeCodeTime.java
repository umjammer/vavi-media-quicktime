package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.util.QTByteObject;

/**
 * The TimeCodeTime class represents time as HH:MM:SS:FF.
 * No range checking is done on values set, and no frame rate is known.
 * It is purely an information class
 */
public final class TimeCodeTime extends QTByteObject implements Cloneable {

    private static final int kNativeSize = 4;

    @Serial
    private static final long serialVersionUID = -6484583375141374076L;

    /**
     * Creates a TimeCodeDef object which has no initial settings.
     * @param hh hours value
     * @param mm minutes value
     * @param ss seconds value
     * @param ff frames value
     */
    public TimeCodeTime(int hh, int mm, int ss, int ff) {
        this();
        setHours(hh);
        setMinutes(mm);
        setSeconds(ss);
        setFrames(ff);
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
    public TimeCodeTime(int counter) {
        this();
        setHours((counter & 0xFF000000) >>> 24);
        setMinutes((counter & 0xFF0000) >>> 16);
        setSeconds((counter & 0xFF00) >>> 8);
        setFrames(counter & 0xFF);
    }

    private TimeCodeTime(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(getBytes());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.write(getBytes());
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
    public void setHours(int hh) {
        setByteAt(0, (byte) hh);
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
    public void setMinutes(int mm) {
        setByteAt(1, (byte) mm);
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
    public void setSeconds(int ss) {
        setByteAt(2, (byte) ss);
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
    public void setFrames(int ff) {
        setByteAt(3, (byte) ff);
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
    @Override
    public Object clone() {
        return new TimeCodeTime(getBytes());
    }
}
