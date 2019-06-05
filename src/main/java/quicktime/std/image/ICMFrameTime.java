
package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.clocks.TimeBase;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class ICMFrameTime extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 48;

    static final long serialVersionUID = 8668877262716984973L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ICMFrameTime.class);
                    }
                });
            }
        }.establish();
    }

    public ICMFrameTime() {
        super(48);
        setRecordSize(48);
        setFlags(1);
    }

    private ICMFrameTime(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[48];

        paramObjectInputStream.read(this.bytes);

        long l1 = getLongFromArray(this.bytes, 0);
        setLongInArray(this.bytes, 0, EndianOrder.flipBigEndianToNative64(l1));

        int i = getIntFromArray(this.bytes, 8);
        setIntInArray(this.bytes, 8, EndianOrder.flipBigEndianToNative32(i));

        setIntInArray(this.bytes, 12, 0);

        for (int j = 16; j < 36; j += 4) {
            int k = getIntFromArray(this.bytes, j);
            int m = EndianOrder.flipBigEndianToNative32(k);
            setIntInArray(this.bytes, j, m);
        }

        long l2 = getLongFromArray(this.bytes, 36);
        setLongInArray(this.bytes, 36, EndianOrder.flipBigEndianToNative64(l2));

        int m = getIntFromArray(this.bytes, 44);
        setIntInArray(this.bytes, 44, EndianOrder.flipNativeToBigEndian32(m));

        setBase(TimeBase.fromICMFrameTime(this));
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[48];

        long l1 = getValue();
        setLongInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian64(l1));

        int i = getScale();
        setIntInArray(arrayOfByte, 8, EndianOrder.flipNativeToBigEndian32(i));

        setIntInArray(arrayOfByte, 12, 0);

        for (int j = 16; j < 36; j += 4) {
            int k = getIntFromArray(getBytes(), j);
            int m = EndianOrder.flipNativeToBigEndian32(k);
            setIntInArray(arrayOfByte, j, m);
        }

        long l2 = getVirtualStartTime();
        setLongInArray(arrayOfByte, 36, EndianOrder.flipNativeToBigEndian64(l2));

        int m = getVirtualDuration();
        setIntInArray(arrayOfByte, 44, EndianOrder.flipNativeToBigEndian32(m));

        paramObjectOutputStream.write(arrayOfByte);
    }

    private void setRecordSize(int paramInt) {
        setIntAt(24, paramInt);
    }

    public void setValue(long paramLong) {
        setLongAt(0, paramLong);
    }

    public void setScale(int paramInt) {
        setIntAt(8, paramInt);
    }

    public void setDuration(int paramInt) {
        setIntAt(16, paramInt);
    }

    public void setRate(float paramFloat) {
        setIntAt(20, QTUtils.X2Fix(paramFloat));
    }

    public void setFrameNumber(int paramInt) {
        setIntAt(28, paramInt);
    }

    public long getValue() {
        return getLongAt(0);
    }

    public int getScale() {
        return getIntAt(8);
    }

    public TimeBase getBase() {
        return TimeBase.fromICMFrameTime(this);
    }

    public void setBase(TimeBase paramTimeBase) {
        setIntAt(12, QTObject.ID(paramTimeBase));
    }

    public int getDuration() {
        return getIntAt(16);
    }

    public float getRate() {
        return QTUtils.Fix2X(getIntAt(20));
    }

    public int getFrameNumber() {
        return getIntAt(28);
    }

    public void setFlags(int paramInt) {
        setIntAt(32, paramInt);
    }

    public int getFlags() {
        return getIntAt(32);
    }

    public void setVirtualStartTime(long paramLong) {
        setLongAt(36, paramLong);
    }

    public long getVirtualStartTime() {
        return getLongAt(36);
    }

    public void setVirtualDuration(int paramInt) {
        setIntAt(44, paramInt);
    }

    public int getVirtualDuration() {
        return getIntAt(44);
    }

    public Object clone() {
        return new ICMFrameTime(this.bytes);
    }

    public String toString() {
        return getClass().getName();
    }

    private static native void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native long getLongFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /ICMFrameTime.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
