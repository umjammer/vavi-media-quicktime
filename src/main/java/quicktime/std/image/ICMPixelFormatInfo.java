
package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public class ICMPixelFormatInfo extends QTByteObject implements QuickTimeLib {
    private static Object linkage;

    private static final int kNativeSize = 36;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(ICMPixelFormatInfo.class);
                    }
                });
            }
        }.establish();
    }

    private static byte[] ar = new byte[36];

    static final long serialVersionUID = 8739457055061219117L;

    public static boolean isValidPixelFormat(int paramInt) {
        int i = ICMGetPixelFormatInfo(paramInt, ar);
        return i == 0;
    }

    public static ICMPixelFormatInfo setPixelFormatInfo(int paramInt) throws StdQTException {
        if (!QTSession.isCurrentOS(4)) {
            ICMPixelFormatInfo localICMPixelFormatInfo = new ICMPixelFormatInfo();
            int i = ICMSetPixelFormatInfo(paramInt, localICMPixelFormatInfo.getBytes());
            StdQTException.checkError(i);
            return localICMPixelFormatInfo;
        }
        throw new QTRuntimeException("Unsupported OS");
    }

    public static ICMPixelFormatInfo getPixelFormatInfo(int paramInt) throws StdQTException {
        ICMPixelFormatInfo localICMPixelFormatInfo = new ICMPixelFormatInfo();
        int i = ICMGetPixelFormatInfo(paramInt, localICMPixelFormatInfo.getBytes());
        StdQTException.checkError(i);
        return localICMPixelFormatInfo;
    }

    private ICMPixelFormatInfo() {
        super(36);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[36];

        paramObjectInputStream.read(this.bytes);

        int i = getIntFromArray(this.bytes, 0);
        int j = EndianOrder.flipBigEndianToNative32(i);
        setIntInArray(this.bytes, 0, j);

        int k = getIntFromArray(this.bytes, 4);
        int m = EndianOrder.flipBigEndianToNative32(k);
        setIntInArray(this.bytes, 4, m);

        for (int n = 8; n < 36; n += 2) {
            short s1 = getShortFromArray(this.bytes, n);
            short s2 = EndianOrder.flipBigEndianToNative16(s1);
            setShortInArray(getBytes(), n, s2);
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[36];

        int i = getIntFromArray(getBytes(), 0);
        int j = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(arrayOfByte, 0, j);

        int k = getFormatFlags();
        int m = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(arrayOfByte, 4, m);

        for (int n = 8; n < 36; n += 2) {
            short s1 = getShortFromArray(arrayOfByte, n);
            short s2 = EndianOrder.flipNativeToBigEndian16(s1);
            setShortInArray(arrayOfByte, n, s2);
        }

        paramObjectOutputStream.write(arrayOfByte);
    }

    public int getFormatFlags() {
        return getIntAt(4);
    }

    public String toString() {
        return getClass().getName() + "[formatFlags=" + getFormatFlags() + "]";
    }

    private static native short ICMSetPixelFormatInfo(int paramInt, byte[] paramArrayOfByte);

    private static native short ICMGetPixelFormatInfo(int paramInt, byte[] paramArrayOfByte);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /ICMPixelFormatInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
