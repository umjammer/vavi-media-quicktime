package quicktime.sound;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.InterfaceLib;
import quicktime.std.clocks.TimeRecord;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class ExtScheduledSoundHeader extends QTPointerRef implements InterfaceLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 106;

    private QTPointerRef ref;

    /**
     * Create an ExtScheduledSoundHeader object when the sample data format is uncompressed.
     * @param data the data used by the sound header
     * @param numChannels the number of channels to be used. 1 = mono, 2 = streo
     * @param rate the sample rate of the sound data
     * @param sampleSizeBits number of bits per each sample.
     */
    public ExtScheduledSoundHeader(QTPointerRef data, int numChannels, float rate, int sampleSizeBits, int format) throws QTException {
        this(data, numChannels, rate, data.getSize() / numChannels / (sampleSizeBits / 8), sampleSizeBits, format, 0, 0);
    }

    /**
     * Create an ExtScheduledSoundHeader object for use when the sample data is compressed.
     * @param data the data used by the sound header
     * @param numChannels the number of channels to be used. 1 = mono, 2 = streo
     * @param rate the sample rate of the sound data
     * @param numFrames the number of frames of sound data
     * @param format the sound format described by the header
     * @param sampleSizeBits number of bits per each sample.
     * @param compressionID the ID of the compression component used to compress this sound
     * @param packetSize the size of each sound packet
     */
    public ExtScheduledSoundHeader(QTPointerRef data, int numChannels, float rate, int numFrames, int format, int sampleSizeBits, int compressionID, int packetSize) throws QTException {
        super(106, true);
        common_init();
        setSampleData(data);
        setNumChannels(numChannels);
        setSampleRate(rate);
        setNumFrames(numFrames);
        setFormat(sampleSizeBits);
        setSampleSize(format);
        setCompressionID(compressionID);
        setPacketSize(packetSize);
    }

    /**
     * Create an ExtScheduledSoundHeader object for the scheduled sound calls.
     * @param data the data used by the sound header
     * @param numChannels the number of channels to be used. 1 = mono, 2 = streo
     * @param rate the sample rate of the sound data
     * @param numFrames the number of frames of sound data
     * @param format the sound format described by the header
     * @param sampleSizeBits number of bits per each sample.
     * @param compressionID the ID of the compression component used to compress this sound
     * @param packetSize the size of each sound packet
     * @param startTime a TimeRecord specifying the time the sound will start playing
     * @param extendedFlags the extended flags field
     */
    public ExtScheduledSoundHeader(QTPointerRef data, int numChannels, float rate, int numFrames, int format, int sampleSizeBits, int compressionID, int packetSize, TimeRecord startTime, int extendedFlags) throws QTException {
        super(106, true);
        common_init();
        setSampleData(data);
        setNumChannels(numChannels);
        setSampleRate(rate);
        setNumFrames(numFrames);
        setFormat(sampleSizeBits);
        setSampleSize(format);
        setCompressionID(compressionID);
        setPacketSize(packetSize);
        setStartTime(startTime);
        setExtendedFlags(extendedFlags);
    }

    private void common_init() {
        this.ref = null;
        setByteAt(20, (byte) -2);
        setByteAt(21, (byte) 60);
        setIntAt(66, 4);
        setIntAt(94, 106);
    }

    ExtScheduledSoundHeader(int nativeRep, Object ref) {
        super(nativeRep, 106, ref);
    }

    /**
     * This returns a pointer to the sample data
     * @return a QTPointerRef
     */
    public QTPointerRef getSampleData() {
        return this.ref;
    }

    /**
     * This sets the data of the sound header. The data must be passed in as a QTPointerRef
     * @param ref the data pointer
     */
    public void setSampleData(QTPointerRef ref) {
        this.ref = ref;
        setIntAt(0, QTObject.ID(ref));
        setIntAt(102, ref.getSize());
    }

    /**
     * This returns the number of channels. A value of one means the sound is mono
     * @return number of channels
     */
    public int getNumChannels() {
        return getIntAt(4);
    }

    /**
     * This sets the number of channels used by the sound data
     * @param numChannels number of channels, 1 means mono
     */
    public void setNumChannels(int numChannels) {
        setIntAt(4, numChannels);
    }

    /**
     * This method gets the sample rate of the sound
     * @return sample rate
     */
    public float getSampleRate() {
        return QTUtils.UFix2X(getIntAt(8));
    }

    /**
     * Sets the sample rate of the sound
     * @param rate the sample rate as a floating point number
     */
    public void setSampleRate(float rate) {
        setIntAt(8, QTUtils.X2UFix(rate));
    }

    public int getNumFrames() {
        return getIntAt(22);
    }

    /**
     * This method sets number of frames in the sound
     * @param frames number of frames
     */
    public void setNumFrames(int frames) {
        setIntAt(22, frames);
    }

    public int getFormat() {
        return getIntAt(40);
    }

    /**
     * This method sets format of the sound
     * @param format format of the sound
     */
    public void setFormat(int format) {
        setIntAt(40, format);
    }

    /**
     * This sets the size of the samples in bits per channel
     * @param sampleSizeBits the number of bits per channel
     */
    public void setSampleSize(int sampleSizeBits) {
        setShortAt(62, (short) sampleSizeBits);
    }

    /**
     * This gets the size of the samples in bits per channel
     */
    public int getSampleSize() {
        return getShortAt(62);
    }

    public int getCompressionID() {
        return getShortAt(56);
    }

    /**
     * This method sets id of the compression component associated with the sound
     * @param id compressor id
     */
    public void setCompressionID(int id) {
        setShortAt(56, (short) id);
    }

    public int getPacketSize() {
        return getShortAt(58);
    }

    /**
     * This method sets the packet size of the sound
     * @param size size of the sound packet
     */
    public void setPacketSize(int size) {
        setShortAt(58, (short) size);
    }

    public TimeRecord getStartTime() throws QTException {
        return new TimeRecord(getIntAt(86), getLongAt(78));
    }

    /**
     * This method sets the time that the sound should begin playing
     * @param startTime the time the sound should play (in the future)
     */
    public void setStartTime(TimeRecord startTime) {
        BlockMove(QTObject.ID(startTime), _ID() + 78, 16);
    }

    /**
     * Gets the value of the extended flags field.
     */
    public int getExtendedFlags() {
        return getIntAt(98);
    }

    /**
     * Sets the value of the extended flags field.
     */
    public void setExtendedFlags(int extendedFlags) {
        setIntAt(98, extendedFlags);
    }

    /**
     * A String representation of the class.
     * @return a String representation of this object
     */
    public String toString() {
        return getClass().getName() + "[" +
                "data:" + this.ref +
                ",numChannels:" + getNumChannels() +
                ",sampleRate:" + getSampleRate() +
                ",sampleSize:" + getSampleSize() +
                ",sampleFormat:" + QTUtils.fromOSType(getFormat()) +
                ",compressionID:" + getCompressionID() +
                "]";
    }

    private static void BlockMove(int src, int dst, int size) {
        MemoryManager.BlockMove(src, dst, size);
    }
}
