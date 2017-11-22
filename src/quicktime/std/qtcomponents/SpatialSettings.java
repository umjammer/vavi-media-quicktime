
package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.image.CodecComponent;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class SpatialSettings extends QTByteObject implements PrimitivesLib {
    private static Object linkage;

    public static final int kNativeSize = 14;

    static final long serialVersionUID = -5046382978690696504L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SpatialSettings.class);
                    }
                });
            }
        }.establish();
    }

    public SpatialSettings(int paramInt1, CodecComponent paramCodecComponent, int paramInt2, int paramInt3) {
        this();
        setCodecType(paramInt1);
        setCodec(paramCodecComponent);
        setDepth(paramInt2);
        setSpatialQuality(paramInt3);
    }

    SpatialSettings() {
        super(14);
    }

    public SpatialSettings(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[14];

        paramObjectInputStream.read(this.bytes);

        int i = getShortFromArray(this.bytes, 0);
        int j = EndianOrder.flipBigEndianToNative32(i);
        setIntInArray(this.bytes, 0, j);

        short s1 = getShortFromArray(this.bytes, 8);
        short s2 = EndianOrder.flipBigEndianToNative16(s1);
        setShortInArray(this.bytes, 8, s2);

        int k = getShortFromArray(this.bytes, 10);
        int m = EndianOrder.flipBigEndianToNative32(k);
        setIntInArray(this.bytes, 0, m);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[14];

        int i = getShortFromArray(getBytes(), 0);
        int j = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(arrayOfByte, 0, j);

        short s1 = getShortFromArray(getBytes(), 8);
        short s2 = EndianOrder.flipNativeToBigEndian16(s1);
        setShortInArray(arrayOfByte, 8, s2);

        int k = getShortFromArray(getBytes(), 10);
        int m = EndianOrder.flipNativeToBigEndian32(k);
        setIntInArray(arrayOfByte, 0, m);

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getCodecType() {
        return getIntAt(0);
    }

    public void setCodecType(int paramInt) {
        setIntAt(0, paramInt);
    }

    public CodecComponent getCodec() {
        return CodecComponent.fromSpatialSettings(this);
    }

    public void setCodec(CodecComponent paramCodecComponent) {
        setIntAt(4, QTObject.ID(paramCodecComponent));
    }

    public int getDepth() {
        return getShortAt(8);
    }

    public void setDepth(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    public int getSpatialQuality() {
        return getIntAt(10);
    }

    public void setSpatialQuality(int paramInt) {
        setIntAt(10, paramInt);
    }

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/SpatialSettings.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
