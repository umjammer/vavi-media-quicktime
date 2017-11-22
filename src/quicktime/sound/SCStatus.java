
package quicktime.sound;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class SCStatus extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 24;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SCStatus.class);
                    }
                });
            }
        }.establish();
    }

    SCStatus() {
        super(24);
    }

    private SCStatus(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[24];

        paramObjectInputStream.read(this.bytes);

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

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[24];

        for (int i = 0; i < 12; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }

        for (int i = 12; i < 16; i++) {
            arrayOfByte[i] = getByteFromArray(getBytes(), i);
        }

        int i = getChannelAttributes();
        setIntInArray(arrayOfByte, 16, EndianOrder.flipNativeToBigEndian32(i));

        int j = getCPULoad();
        setIntInArray(arrayOfByte, 20, EndianOrder.flipNativeToBigEndian32(j));

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getStartTime() {
        return getIntAt(0);
    }

    public int getEndTime() {
        return getIntAt(4);
    }

    public int getCurrentTime() {
        return getIntAt(8);
    }

    public boolean getChannelBusy() {
        int i = getByteAt(12);
        return i != 0;
    }

    public boolean getChannelPaused() {
        int i = getByteAt(14);
        return i != 0;
    }

    public int getChannelAttributes() {
        return getIntAt(16);
    }

    public int getCPULoad() {
        return getIntAt(20);
    }

    public Object clone() {
        return new SCStatus(this.bytes);
    }

    public String toString() {
        return getClass().getName() + "[startTime=" + getStartTime() + ",endTime=" + getEndTime() + ",currentTime"
               + getCurrentTime() + ",channelBusy" + getChannelBusy() + ",channelPaused" + getChannelPaused() + "]";
    }

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native byte getByteFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SCStatus.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
