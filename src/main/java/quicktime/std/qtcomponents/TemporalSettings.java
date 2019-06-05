
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
import quicktime.util.QTUtils;


public final class TemporalSettings extends QTByteObject implements PrimitivesLib {
    private static Object linkage;

    public static final int kNativeSize = 12;

    private static final long serialVersionUID = -5866822543021494879L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(TemporalSettings.class);
                    }
                });
            }
        }.establish();
    }

    public TemporalSettings(int paramInt1, float paramFloat, int paramInt2) {
        this();
        setTemporalQuality(paramInt1);
        setFrameRate(paramFloat);

        setKeyFrameRate(paramInt2);
    }

    TemporalSettings() {
        super(12);
    }

    public TemporalSettings(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[12];

        paramObjectInputStream.read(this.bytes);

        for (int i = 0; i < 12; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[12];

        for (int i = 0; i < 12; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getTemporalQuality() {
        return getIntAt(0);
    }

    public void setTemporalQuality(int paramInt) {
        setIntAt(0, paramInt);
    }

    public float getFrameRate() {
        return QTUtils.Fix2X(getIntAt(4));
    }

    public void setFrameRate(float paramFloat) {
        setIntAt(4, QTUtils.X2Fix(paramFloat));
    }

    public int getKeyFrameRate() {
        return getIntAt(8);
    }

    public void setKeyFrameRate(int paramInt) {
        setIntAt(8, paramInt);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/TemporalSettings.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
