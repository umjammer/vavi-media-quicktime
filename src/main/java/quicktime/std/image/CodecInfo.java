
package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


public final class CodecInfo extends QTByteObject implements QuickTimeLib, Cloneable {
    private static Object linkage;

    public static final int kNativeSize = 72;

    static final long serialVersionUID = 1284575243605950382L;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(CodecInfo.class);
                    }
                });
            }
        }.establish();
    }

    public CodecInfo(int paramInt, CodecComponent paramCodecComponent) throws StdQTException {
        super(72);
        int i = GetCodecInfo(this.bytes, paramInt, QTObject.ID(paramCodecComponent));
        StdQTException.checkError(i);
    }

    private CodecInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[72];

        paramObjectInputStream.read(this.bytes);

        for (int i = 32; i < 36; i += 2) {
            setShortInArray(this.bytes, 32, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 36; i < 52; i += 4) {
            setIntInArray(this.bytes, 34, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 54; i < 58; i += 2) {
            setShortInArray(this.bytes, 54, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 60; i < 68; i += 2) {
            setShortInArray(this.bytes, 54, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[72];
        System.arraycopy(getBytes(), 0, arrayOfByte, 0, 72);

        for (int i = 32; i < 36; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 36; i < 52; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 54; i < 58; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 60; i < 68; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(this.bytes, i)));
        }
        setIntInArray(this.bytes, 60, 0);

        paramObjectOutputStream.write(arrayOfByte);
    }

    public static int codecVersion() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = CodecManagerVersion(arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public String getTypeName() throws UtilException {
        return getPStringAt(0);
    }

    public short getVersion() {
        return getShortAt(32);
    }

    public short getRevisionLevel() {
        return getShortAt(34);
    }

    public int getVendor() {
        return getIntAt(36);
    }

    public int getDecompressFlags() {
        return getIntAt(40);
    }

    public int getCompressFlags() {
        return getIntAt(44);
    }

    public int getFormatFlags() {
        return getIntAt(48);
    }

    public int getCompressionAccuracy() {
        return QTUtils.UByte2Int(getByteAt(52));
    }

    public int getDecompressionAccuracy() {
        return QTUtils.UByte2Int(getByteAt(53));
    }

    public int getCompressionSpeed() {
        return QTUtils.UShort2Int(getShortAt(54));
    }

    public int getDecompressionSpeed() {
        return QTUtils.UShort2Int(getShortAt(56));
    }

    public int getCompressionLevel() {
        return QTUtils.UByte2Int(getByteAt(58));
    }

    public short getMinimumHeight() {
        return getShortAt(60);
    }

    public short getMinimumWidth() {
        return getShortAt(62);
    }

    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + "revisionLevel=" + getRevisionLevel() + "vendor="
               + getVendor() + "decompressFlags=" + getDecompressFlags() + "compressFlags=" + getCompressFlags()
               + "formatFlags=" + getFormatFlags() + "compressionAccuracy=" + getCompressionAccuracy()
               + "decompressionAccuracy=" + getDecompressionAccuracy() + "compressionLevel=" + getCompressionLevel()
               + "minimumHeight=" + getMinimumHeight() + "minimumWidth=" + getMinimumWidth() + "]";
    }

    public Object clone() {
        return new CodecInfo(this.bytes);
    }

    private static native short GetCodecInfo(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short CodecManagerVersion(int[] paramArrayOfInt);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort);

    private static native int getIntFromArray(byte[] paramArrayOfByte, int paramInt);

    private static native short getShortFromArray(byte[] paramArrayOfByte, int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/image
 * /CodecInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
