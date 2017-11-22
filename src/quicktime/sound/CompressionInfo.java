
package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class CompressionInfo extends QTByteObject implements SoundLib, InterfaceLib {
    private static Object linkage;

    public static final int kNativeSize = 20;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(CompressionInfo.class);
                    }
                });
            }
        }.establish();
    }

    public static CompressionInfo get(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws SoundException {
        CompressionInfo localCompressionInfo = new CompressionInfo();
        SoundException.checkError(GetCompressionInfo((short) paramInt1,
                                                     paramInt2,
                                                     (short) paramInt3,
                                                     (short) paramInt4,
                                                     localCompressionInfo.getBytes()));

        return localCompressionInfo;
    }

    public CompressionInfo() {
        super(20);
        setRecordSize(20);
    }

    public int getRecordSize() {
        return getIntAt(0);
    }

    public void setRecordSize(int paramInt) {
        setIntAt(0, paramInt);
    }

    public int getFormat() {
        return getIntAt(4);
    }

    public void setFormat(int paramInt) {
        setIntAt(4, paramInt);
    }

    public int getCompressionID() {
        return getShortAt(8);
    }

    public void setCompressionID(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    public int getSamplesPerPacket() {
        return QTUtils.UShort2Int(getShortAt(10));
    }

    public void setSamplesPerPacket(int paramInt) {
        setShortAt(10, (short) paramInt);
    }

    public int getBytesPerPacket() {
        return QTUtils.UShort2Int(getShortAt(12));
    }

    public void setBytesPerPacket(int paramInt) {
        setShortAt(12, (short) paramInt);
    }

    public int getBytesPerFrame() {
        return QTUtils.UShort2Int(getShortAt(14));
    }

    public void setBytesPerFrame(int paramInt) {
        setShortAt(14, (short) paramInt);
    }

    public int getBytesPerSample() {
        return QTUtils.UShort2Int(getShortAt(16));
    }

    public void setBytesPerSample(int paramInt) {
        setShortAt(16, (short) paramInt);
    }

    public String toString() {
        return getClass().getName() + "[recordSize=" + getRecordSize() + ",format=" + QTUtils.fromOSType(getFormat())
               + ",compressionID=" + getCompressionID() + ",samplesPerPacket=" + getSamplesPerPacket() + ",bytesPerPacket="
               + getBytesPerPacket() + ",bytesPerFrame=" + getBytesPerFrame() + ",bytesPerSample=" + getBytesPerSample() + "]";
    }

    private static native short GetCompressionInfo(short paramShort1,
                                                   int paramInt,
                                                   short paramShort2,
                                                   short paramShort3,
                                                   byte[] paramArrayOfByte);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * CompressionInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
