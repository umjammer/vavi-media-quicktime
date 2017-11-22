
package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import quicktime.util.QTByteObject;


public final class TimeCodeTime extends QTByteObject implements Cloneable {
    private static final int kNativeSize = 4;

    static final long serialVersionUID = -6484583375141374076L;

    public TimeCodeTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this();
        setHours(paramInt1);
        setMinutes(paramInt2);
        setSeconds(paramInt3);
        setFrames(paramInt4);
    }

    public TimeCodeTime() {
        super(kNativeSize);
    }

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

    public int toCounter() {
        int i = getHours();
        int j = getMinutes();
        int k = getSeconds();
        int m = getFrames();
        return (0xFF & i) << 24 | (0xFF & j) << 16 | (0xFF & k) << 8 | 0xFF & m;
    }

    public int getHours() {
        return getByteAt(0);
    }

    public void setHours(int paramInt) {
        setByteAt(0, (byte) paramInt);
    }

    public int getMinutes() {
        return getByteAt(1);
    }

    public void setMinutes(int paramInt) {
        setByteAt(1, (byte) paramInt);
    }

    public int getSeconds() {
        return getByteAt(2);
    }

    public void setSeconds(int paramInt) {
        setByteAt(2, (byte) paramInt);
    }

    public int getFrames() {
        return getByteAt(3);
    }

    public void setFrames(int paramInt) {
        setByteAt(3, (byte) paramInt);
    }

    public String toString() {
        return getClass().getName() + "[" + getHours() + ":" + getMinutes() + ":" + getSeconds() + ":" + getFrames() + "]";
    }

    public Object clone() {
        return new TimeCodeTime(getBytes());
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TimeCodeTime.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
