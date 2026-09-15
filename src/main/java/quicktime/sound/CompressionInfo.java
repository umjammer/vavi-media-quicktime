package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Contains information about the compression format of sound data.
 */
public final class CompressionInfo extends QTByteObject implements SoundLib, InterfaceLib {

    private static Object linkage;

    /**
     * This is the size of this class
     */
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

    /**
     * Gets the CompressionInfo structure given the supplied information
     * QuickTime::GetCompressionInfo
     * @param compressionID the id of the compression type
     * @param format the OSType that describes the compression used
     * @param numChannels the number of channels in the sound data
     * @param sampleSize the size of the sample
     * @return a CompressionInfo object
     * @return {@link quicktime.sound.SoundException}
     */
    public static CompressionInfo get(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws SoundException {
        CompressionInfo localCompressionInfo = new CompressionInfo();
        SoundException.checkError(GetCompressionInfo((short) paramInt1, paramInt2, (short) paramInt3, (short) paramInt4, localCompressionInfo.getBytes()));
        return localCompressionInfo;
    }

    /**
     * Makes an instance of this class.
     * This will set the record size to the kNativeSize value.
     */
    public CompressionInfo() {
        super(20);
        setRecordSize(20);
    }

    /**
     * The recordSize.
     * @return <DD/>
     */
    public int getRecordSize() {
        return getIntAt(0);
    }

    /**
     * The recordSize.
     * @param recordSize -
     */
    public void setRecordSize(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * The format.
     * @return <DD/>
     */
    public int getFormat() {
        return getIntAt(4);
    }

    /**
     * The format.
     * @param format -
     */
    public void setFormat(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * The compressionID.
     * @return <DD/>
     */
    public int getCompressionID() {
        return getShortAt(8);
    }

    /**
     * The compressionID.
     * @param compressionID -
     */
    public void setCompressionID(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    /**
     * The samplesPerPacket.
     * @return <DD/>
     */
    public int getSamplesPerPacket() {
        return QTUtils.UShort2Int(getShortAt(10));
    }

    /**
     * The samplesPerPacket.
     * @param samplesPerPacket -
     */
    public void setSamplesPerPacket(int paramInt) {
        setShortAt(10, (short) paramInt);
    }

    /**
     * The bytesPerPacket.
     * @return <DD/>
     */
    public int getBytesPerPacket() {
        return QTUtils.UShort2Int(getShortAt(12));
    }

    /**
     * The bytesPerPacket.
     * @param bytesPerPacket -
     */
    public void setBytesPerPacket(int paramInt) {
        setShortAt(12, (short) paramInt);
    }

    /**
     * The bytesPerFrame.
     * @return <DD/>
     */
    public int getBytesPerFrame() {
        return QTUtils.UShort2Int(getShortAt(14));
    }

    /**
     * The bytesPerFrame.
     * @param bytesPerFrame -
     */
    public void setBytesPerFrame(int paramInt) {
        setShortAt(14, (short) paramInt);
    }

    /**
     * The bytesPerSample.
     * @return <DD/>
     */
    public int getBytesPerSample() {
        return QTUtils.UShort2Int(getShortAt(16));
    }

    /**
     * The bytesPerSample.
     * @param bytesPerSample -
     */
    public void setBytesPerSample(int paramInt) {
        setShortAt(16, (short) paramInt);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[recordSize=" + getRecordSize() + ",format=" + QTUtils.fromOSType(getFormat()) + ",compressionID=" + getCompressionID() + ",samplesPerPacket=" + getSamplesPerPacket() + ",bytesPerPacket=" + getBytesPerPacket() + ",bytesPerFrame=" + getBytesPerFrame() + ",bytesPerSample=" + getBytesPerSample() + "]";
    }

    private static native short GetCompressionInfo(short paramShort1, int paramInt, short paramShort2, short paramShort3, byte[] paramArrayOfByte);
}

