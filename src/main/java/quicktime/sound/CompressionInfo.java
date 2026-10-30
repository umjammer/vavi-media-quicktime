package quicktime.sound;

import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * Contains information about the compression format of sound data.
 */
public final class CompressionInfo extends QTByteObject implements SoundLib, InterfaceLib {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    /**
     * Gets the CompressionInfo structure given the supplied information
     * @see "QuickTime::GetCompressionInfo"
     * @param compressionID the id of the compression type
     * @param format the OSType that describes the compression used
     * @param numChannels the number of channels in the sound data
     * @param sampleSize the size of the sample
     * @return a CompressionInfo object
     * @throws quicktime.sound.SoundException
     */
    public static CompressionInfo get(int compressionID, int format, int numChannels, int sampleSize) throws SoundException {
        CompressionInfo compressionInfo = new CompressionInfo();
        SoundException.checkError(GetCompressionInfo((short) compressionID, format, (short) numChannels, (short) sampleSize, compressionInfo.getBytes()));
        return compressionInfo;
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
    public void setRecordSize(int recordSize) {
        setIntAt(0, recordSize);
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
    public void setFormat(int format) {
        setIntAt(4, format);
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
    public void setCompressionID(int compressionID) {
        setShortAt(8, (short) compressionID);
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
    public void setSamplesPerPacket(int samplesPerPacket) {
        setShortAt(10, (short) samplesPerPacket);
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
    public void setBytesPerPacket(int bytesPerPacket) {
        setShortAt(12, (short) bytesPerPacket);
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
    public void setBytesPerFrame(int bytesPerFrame) {
        setShortAt(14, (short) bytesPerFrame);
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
    public void setBytesPerSample(int bytesPerSample) {
        setShortAt(16, (short) bytesPerSample);
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() +
                "[recordSize=" + getRecordSize() +
                ",format=" + QTUtils.fromOSType(getFormat()) +
                ",compressionID=" + getCompressionID() +
                ",samplesPerPacket=" + getSamplesPerPacket() +
                ",bytesPerPacket=" + getBytesPerPacket() +
                ",bytesPerFrame=" + getBytesPerFrame() +
                ",bytesPerSample=" + getBytesPerSample() +
                "]";
    }

    private static short GetCompressionInfo(short compressionID, int format, short numChannels, short sampleSize, byte[] info) {
        return ImageCompressionManager.GetCompressionInfo(compressionID, format, numChannels, sampleSize, info);
    }
}
