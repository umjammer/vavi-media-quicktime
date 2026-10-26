package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
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

    /**
     * Returns true if the provided pixelFormat is a valid pixel format.
     * QuickTime::ICMGetPixelFormatInfo
     * @param pixelFormat Specifies the pixel format.
     * @return a boolean
     */
    public static boolean isValidPixelFormat(int paramInt) {
        int i = ICMGetPixelFormatInfo(paramInt, ar);
        return i == 0;
    }

    /**
     * Set the PixelFormat info.
     * QuickTime::ICMSetPixelFormatInfo
     * @param pixelFormat Specifies the pixel format.
     * @return an ICMPixelFormatInfo structure
     * @return {@link quicktime.std.StdQTException}
     */
    public static ICMPixelFormatInfo setPixelFormatInfo(int paramInt) throws StdQTException {
        if (!QTSession.isCurrentOS(4)) {
            ICMPixelFormatInfo localICMPixelFormatInfo = new ICMPixelFormatInfo();
            int i = ICMSetPixelFormatInfo(paramInt, localICMPixelFormatInfo.getBytes());
            StdQTException.checkError(i);
            return localICMPixelFormatInfo;
        }
        throw new QTRuntimeException("Unsupported OS");
    }

    /**
     * Get the PixelFormat info.
     * QuickTime::ICMGetPixelFormatInfo
     * @param pixelFormat Specifies the pixel format.
     * @return an ICMPixelFormatInfo structure
     * @return {@link quicktime.std.StdQTException}
     */
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

    /**
     * Return a String representation of this class.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[formatFlags=" + getFormatFlags() + "]";
    }

    private static short ICMSetPixelFormatInfo(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.ICMSetPixelFormatInfo(paramInt, paramArrayOfByte);
    }

    private static short ICMGetPixelFormatInfo(int paramInt, byte[] paramArrayOfByte) {
        return ImageCompressionManager.ICMGetPixelFormatInfo(paramInt, paramArrayOfByte);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /ICMPixelFormatInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
