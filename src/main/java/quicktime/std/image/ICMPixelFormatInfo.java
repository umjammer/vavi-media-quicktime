package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public class ICMPixelFormatInfo extends QTByteObject implements QuickTimeLib {

    private static final int kNativeSize = 36;

    private static final byte[] ar = new byte[36];

    @Serial
    private static final long serialVersionUID = 8739457055061219117L;

    /**
     * Returns true if the provided pixelFormat is a valid pixel format.
     * @see "QuickTime::ICMGetPixelFormatInfo"
     * @param pixelFormat Specifies the pixel format.
     * @return a boolean
     */
    public static boolean isValidPixelFormat(int pixelFormat) {
        int i = ICMGetPixelFormatInfo(pixelFormat, ar);
        return i == 0;
    }

    /**
     * Set the PixelFormat info.
     * @see "QuickTime::ICMSetPixelFormatInfo"
     * @param pixelFormat Specifies the pixel format.
     * @return an ICMPixelFormatInfo structure
     * @throws quicktime.std.StdQTException
     */
    public static ICMPixelFormatInfo setPixelFormatInfo(int pixelFormat) throws StdQTException {
        if (!QTSession.isCurrentOS(4)) {
            ICMPixelFormatInfo icmPixelFormatInfo = new ICMPixelFormatInfo();
            int i = ICMSetPixelFormatInfo(pixelFormat, icmPixelFormatInfo.getBytes());
            StdQTException.checkError(i);
            return icmPixelFormatInfo;
        }
        throw new QTRuntimeException("Unsupported OS");
    }

    /**
     * Get the PixelFormat info.
     * @see "QuickTime::ICMGetPixelFormatInfo"
     * @param pixelFormat Specifies the pixel format.
     * @return an ICMPixelFormatInfo structure
     * @throws quicktime.std.StdQTException
     */
    public static ICMPixelFormatInfo getPixelFormatInfo(int pixelFormat) throws StdQTException {
        ICMPixelFormatInfo pixelFormatInfo = new ICMPixelFormatInfo();
        int i = ICMGetPixelFormatInfo(pixelFormat, pixelFormatInfo.getBytes());
        StdQTException.checkError(i);
        return pixelFormatInfo;
    }

    private ICMPixelFormatInfo() {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[36];
        in.read(this.bytes);
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[36];
        int i = getIntFromArray(getBytes(), 0);
        int j = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(movie, 0, j);
        int k = getFormatFlags();
        int m = EndianOrder.flipNativeToBigEndian32(i);
        setIntInArray(movie, 4, m);
        for (int n = 8; n < 36; n += 2) {
            short s1 = getShortFromArray(movie, n);
            short s2 = EndianOrder.flipNativeToBigEndian16(s1);
            setShortInArray(movie, n, s2);
        }
        out.write(movie);
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

    private static short ICMSetPixelFormatInfo(int pixelFormat, byte[] info) {
        return ImageCompressionManager.ICMSetPixelFormatInfo(pixelFormat, info);
    }

    private static short ICMGetPixelFormatInfo(int pixelFormat, byte[] info) {
        return ImageCompressionManager.ICMGetPixelFormatInfo(pixelFormat, info);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }
}
