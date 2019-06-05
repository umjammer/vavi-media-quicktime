
package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class TimeCodeDef extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

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

    public int getFlags() {
        return getIntAt(0);
    }

    public void setFlags(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getTimeScale() {
        return getIntAt(4);
    }

    public void setTimeScale(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getFrameDuration() {
        return getIntAt(8);
    }

    public void setFrameDuration(int paramInt) {
        setIntAt(8, paramInt);
    }

    public int getFramesPerSecond() {
        return this.bytes[12];
    }

    public void setFramesPerSecond(int paramInt) {
        this.bytes[12] = ((byte) paramInt);
    }

    public String toString() {
        return getClass().getName() + "[fps=" + getFramesPerSecond() + "dropFrame=" + ((getFlags() & 0x1) == 1) + "]";
    }

    public Object clone() {
        return new TimeCodeDef(this.bytes);
    }

    private static native void copyPointerToArray(int paramInt1,
                                                  int paramInt2,
                                                  byte[] paramArrayOfByte,
                                                  int paramInt3,
                                                  int paramInt4);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TimeCodeDef.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
