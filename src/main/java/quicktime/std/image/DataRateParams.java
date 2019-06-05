
package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class DataRateParams extends QTByteObject implements PrimitivesLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 24;

    static final long serialVersionUID = 7326971667474597100L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DataRateParams.class);
                    }
                });
            }
        }.establish();
    }

    public DataRateParams() {
        super(24);
    }

    public DataRateParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        this();
        setDataRate(paramInt1);
        setDataOverrun(paramInt2);
        setFrameDuration(paramInt3);
        setKeyFrameRate(paramInt4);
        setMinSpatialQuality(paramInt5);
        setMinTemporalQuality(paramInt6);
    }

    private DataRateParams(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[24];

        byte[] arrayOfByte = new byte[24];

        paramObjectInputStream.read(arrayOfByte);

        for (int i = 0; i < 24; i += 4) {
            int j = getIntFromArray(arrayOfByte, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[24];

        for (int i = 0; i < 24; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getDataRate() {
        return getIntAt(0);
    }

    public int getDataOverrun() {
        return getIntAt(4);
    }

    public int getFrameDuration() {
        return getIntAt(8);
    }

    public int getKeyFrameRate() {
        return getIntAt(12);
    }

    public int getMinSpatialQuality() {
        return getIntAt(16);
    }

    public int getMinTemporalQuality() {
        return getIntAt(20);
    }

    public void setDataRate(int paramInt) {
        setIntAt(0, paramInt);
    }

    public void setDataOverrun(int paramInt) {
        setIntAt(4, paramInt);
    }

    public void setFrameDuration(int paramInt) {
        setIntAt(8, paramInt);
    }

    public void setKeyFrameRate(int paramInt) {
        setIntAt(12, paramInt);
    }

    public void setMinSpatialQuality(int paramInt) {
        setIntAt(16, paramInt);
    }

    public void setMinTemporalQuality(int paramInt) {
        setIntAt(20, paramInt);
    }

    public Object clone() {
        return new DataRateParams(getBytes());
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /DataRateParams.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
