package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTObject;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;


/**
 * Corresponds to the Compressor Information Structure which describes
 * the capabilities of compressors.
 */
public final class CodecInfo extends QTByteObject implements QuickTimeLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 72;

    @Serial
    private static final long serialVersionUID = 1284575243605950382L;

    /**
     * Constructs a CodecInfo.
     * @see "QuickTime::GetCodecInfo"
     * @param cType specifies the compressor type.
     * @param codec specifies a compressor identifier.
     */
    public CodecInfo(int cType, CodecComponent codec) throws StdQTException {
        super(72);
        int i = GetCodecInfo(this.bytes, cType, QTObject.ID(codec));
        StdQTException.checkError(i);
    }

    private CodecInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
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

    @Serial
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

    /**
     * determines the version of the installed image compression manager.
     * @see "see QuickTime::CodecManagerVersion"
     * @return version.
     * @throws quicktime.std.StdQTException
     */
    public static int codecVersion() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = CodecManagerVersion(arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Get the compression algorithm used by the component.
     * @return the name of the compression algorithm.
     * @throws quicktime.util.UtilException
     */
    public String getTypeName() throws UtilException {
        return getPStringAt(0);
    }

    /**
     * Get the version of the compressed data.
     * @return the version.
     */
    public short getVersion() {
        return getShortAt(32);
    }

    /**
     * Get the version of the component.
     * @return the component version.
     */
    public short getRevisionLevel() {
        return getShortAt(34);
    }

    /**
     * Get the developer of the component
     * @return the name of the developer.
     */
    public int getVendor() {
        return getIntAt(36);
    }

    /**
     * Get contains flags that specify the decompression capabilities of
     * the component.
     * @return decompress flags.
     */
    public int getDecompressFlags() {
        return getIntAt(40);
    }

    /**
     * Get the flags that specify the compression capabilities of the
     * component.
     * @return compress flags.
     */
    public int getCompressFlags() {
        return getIntAt(44);
    }

    /**
     * Get the flags that describes the possible format for compressed
     * data produced by this component.
     * @return format flags.
     */
    public int getFormatFlags() {
        return getIntAt(48);
    }

    /**
     * Get the relative accuracy of the compression algorithm.
     * @return compression accuracy.
     */
    public int getCompressionAccuracy() {
        return QTUtils.UByte2Int(getByteAt(52));
    }

    /**
     * Get the relative accuracy of the decompression algorithm.
     * @return decompression accuracy.
     */
    public int getDecompressionAccuracy() {
        return QTUtils.UByte2Int(getByteAt(53));
    }

    /**
     * Get the relative speed of the component for compression operations.
     * @return compression speed.
     */
    public int getCompressionSpeed() {
        return QTUtils.UShort2Int(getShortAt(54));
    }

    /**
     * Get the relative speed of the component for decompression operations.
     * @return decompression speed.
     */
    public int getDecompressionSpeed() {
        return QTUtils.UShort2Int(getShortAt(56));
    }

    /**
     * Get the relative compression acheived by this component.
     * @return compression level.
     */
    public int getCompressionLevel() {
        return QTUtils.UByte2Int(getByteAt(58));
    }

    /**
     * Get the height in pixels of the smallest image the component can handle.
     * @return minimum height.
     */
    public short getMinimumHeight() {
        return getShortAt(60);
    }

    /**
     * Get the width in pixels of the smallest image the component can handle.
     * @return minimum width.
     */
    public short getMinimumWidth() {
        return getShortAt(62);
    }

    /**
     * Returns the String representation of this Codec Information structure.
     */
    public String toString() {
        return getClass().getName() + "[version=" + getVersion() + "revisionLevel=" + getRevisionLevel() + "vendor=" + getVendor() + "decompressFlags=" + getDecompressFlags() + "compressFlags=" + getCompressFlags() + "formatFlags=" + getFormatFlags() + "compressionAccuracy=" + getCompressionAccuracy() + "decompressionAccuracy=" + getDecompressionAccuracy() + "compressionLevel=" + getCompressionLevel() + "minimumHeight=" + getMinimumHeight() + "minimumWidth=" + getMinimumWidth() + "]";
    }

    /**
     * Return a copy of the CodecInfo.
     */
    @Override
    public Object clone() {
        return new CodecInfo(this.bytes);
    }

    private static short GetCodecInfo(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        return ImageCompressionManager.GetCodecInfo(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static short CodecManagerVersion(int[] paramArrayOfInt) {
        return ImageCompressionManager.CodecManagerVersion(paramArrayOfInt);
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
