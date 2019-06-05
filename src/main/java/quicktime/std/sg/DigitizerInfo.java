
package quicktime.std.sg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class DigitizerInfo extends QTByteObject implements PrimitivesLib, QuickTimeLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 42;

    private static final long serialVersionUID = -1283193746361395107L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(DigitizerInfo.class);
                    }
                });
            }
        }.establish();
    }

    DigitizerInfo() {
        super(42);
    }

    DigitizerInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[42];

        paramObjectInputStream.read(this.bytes);

        setShortInArray(this.bytes, 0, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, 0)));

        for (int i = 4; i < 18; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 28; i < 38; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[42];

        setShortInArray(arrayOfByte, 0, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), 0)));

        for (int i = 2; i < 18; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        setIntInArray(arrayOfByte, 20, 0);
        setIntInArray(arrayOfByte, 24, 0);

        for (int i = 28; i < 38; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        setIntInArray(arrayOfByte, 38, 0);

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getInputCapabilityFlags() {
        return getIntAt(2);
    }

    public int getOutputCapabilityFlags() {
        return getIntAt(6);
    }

    public short getMinDestHeight() {
        return getShortAt(28);
    }

    public short getMinDestWidth() {
        return getShortAt(30);
    }

    public short getmaxDestHeight() {
        return getShortAt(32);
    }

    public short getmaxDestWidth() {
        return getShortAt(34);
    }

    public short getblendLevels() {
        return getShortAt(36);
    }

    public Object clone() {
        return new DigitizerInfo(this.bytes);
    }

    public String toString() {
        return getClass().getName() + " MinDestHeight: " + getMinDestHeight() + " MinDestWidth: " + getMinDestWidth()
               + " \nmaxDestHeight: " + getmaxDestHeight() + " MaxDestWidth: " + getmaxDestWidth() + " \nblendLevels: "
               + getblendLevels() + " InputCapabilityFlags: " + getInputCapabilityFlags() + " \nOutputCapabilityFlags: "
               + getOutputCapabilityFlags();
    }

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * DigitizerInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
