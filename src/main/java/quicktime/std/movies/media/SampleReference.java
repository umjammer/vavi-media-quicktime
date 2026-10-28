package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.util.QTPointerRef;

/**
 * The SampleReference class forms a general base class for information that can be
 * constructed or gleaned about media samples and references.
 */
public final class SampleReference extends QTPointerRef {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 18;

    /**
     * Create a Sample Reference object from the SampleReferenceVector
     */
    public SampleReference() throws QTException {
        super(18, true);
    }

    /**
     * This constructor creates a SampleReference object.
     * @param dataOffset an offset into the sample data.
     * @param dataSize Specifies media sample's data size.
     * @param durationPerSample Specifies media sample's duration.
     * @param numberOfSamples Specifies the number of samples.
     * @param sampleFlags Specifies flags for samples.
     */
    public SampleReference(int dataOffset, int dataSize, int durationPerSample, int numberOfSamples, int sampleFlags) throws QTException {
        this();
        setDataOffset(dataOffset);
        setDataSize(dataSize);
        setDurationPerSample(durationPerSample);
        setNumberOfSamples(numberOfSamples);
        setFlags(sampleFlags);
    }

    /**
     * The media sample's offset.
     */
    public int getDataOffset() {
        return getIntAt(0);
    }

    public void setDataOffset(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * The media sample's data size.
     */
    public int getDataSize() {
        return getIntAt(4);
    }

    public void setDataSize(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * The media sample's duration per sample
     */
    public int getDurationPerSample() {
        return getIntAt(8);
    }

    public void setDurationPerSample(int paramInt) {
        setIntAt(8, paramInt);
    }

    /**
     * The number of samples.
     */
    public int getNumberOfSamples() {
        return getIntAt(12);
    }

    public void setNumberOfSamples(int paramInt) {
        setIntAt(12, paramInt);
    }

    /**
     * The flags for the media samples.
     */
    public int getFlags() {
        return getShortAt(16);
    }

    public void setFlags(int paramInt) {
        setShortAt(16, (short) paramInt);
    }
}
