package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Contains information about the mix state of a specified part.
 */
public final class InstSampleDesc extends QTByteObject {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 46;

    @Serial
    private static final long serialVersionUID = -7868738374047072834L;

    /**
     * Makes a instance of this class.
     */
    public InstSampleDesc() {
        super(46);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[46];
        paramObjectInputStream.read(getBytes());
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.write(getBytes());
    }

    /**
     * The data format type. This is either 'twos', for signed data,
     * or 'raw ', for unsigned data.
     * @return <DD/>
     */
    public int getDataFormat() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(0));
    }

    /**
     * The data format type. This is either 'twos', for signed data,
     * or 'raw ', for unsigned data.
     * @param dataFormat -
     */
    public void setDataFormat(int dataFormat) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(dataFormat));
    }

    /**
     * The number of channels of data present in the sample.
     * @return <DD/>
     */
    public int getNumChannels() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(4));
    }

    /**
     * The number of channels of data present in the sample.
     * @param numChannels -
     */
    public void setNumChannels(int numChannels) {
        setShortAt(4, EndianOrder.flipNativeToBigEndian16((short) numChannels));
    }

    /**
     * The size of the sampleÑ 8-bit or 16-bit.
     * @return <DD/>
     */
    public int getSampleSize() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(6));
    }

    /**
     * The size of the sampleÑ 8-bit or 16-bit.
     * @param sampleSize -
     */
    public void setSampleSize(int sampleSize) {
        setShortAt(6, EndianOrder.flipNativeToBigEndian16((short) sampleSize));
    }

    /**
     * The rate at which to play the sample.
     * @return <DD/>
     */
    public float getSampleRate() {
        return QTUtils.Fix2X(getSampleRateRaw());
    }

    /**
     * The rate at which to play the sample.
     * @param sampleRate -
     */
    public void setSampleRate(float sampleRate) {
        setSampleRateRaw(QTUtils.X2Fix(sampleRate));
    }

    /**
     * The rate at which to play the sample in unsigned 16.16 format
     * @return <DD/>
     */
    public int getSampleRateRaw() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(8));
    }

    /**
     * The rate at which to play the sample in unsigned 16.16 format
     * @param sampleRate -
     */
    public void setSampleRateRaw(int sampleRate) {
        setIntAt(8, EndianOrder.flipNativeToBigEndian32(sampleRate));
    }

    /**
     * The ID number of a sample data atom that contains the sample audio data.
     * @return <DD/>
     */
    public int getSampleDataID() {
        return EndianOrder.flipBigEndianToNative16(getShortAt(12));
    }

    /**
     * The ID number of a sample data atom that contains the sample audio data.
     * @param sampleDataID -
     */
    public void setSampleDataID(int sampleDataID) {
        setShortAt(12, EndianOrder.flipNativeToBigEndian16((short) sampleDataID));
    }

    /**
     * Set to zero - this is the default.
     * @return <DD/>
     */
    public int getOffset() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(14));
    }

    /**
     * Set to zero - this is the default.
     * @param offset -
     */
    public void setOffset(int offset) {
        setIntAt(14, EndianOrder.flipNativeToBigEndian32(offset));
    }

    /**
     * The number of data samples in the sound.
     * @return <DD/>
     */
    public int getNumSamples() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(18));
    }

    /**
     * The number of data samples in the sound.
     * @param numSamples -
     */
    public void setNumSamples(int numSamples) {
        setIntAt(18, EndianOrder.flipNativeToBigEndian32(numSamples));
    }

    /**
     * The type of loop.
     * @return <DD/>
     */
    public int getLoopType() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(22));
    }

    /**
     * The type of loop.
     * @param loopType -
     */
    public void setLoopType(int loopType) {
        setIntAt(22, EndianOrder.flipNativeToBigEndian32(loopType));
    }

    /**
     * Indicates the beginning of the portion of the sample that is
     * looped if the sound is sustained. The position is given in
     * the number of data samples from the start of the sound.
     * @return <DD/>
     */
    public int getLoopStart() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(26));
    }

    /**
     * Indicates the beginning of the portion of the sample that is
     * looped if the sound is sustained. The position is given in
     * the number of data samples from the start of the sound.
     * @param loopStart -
     */
    public void setLoopStart(int loopStart) {
        setIntAt(26, EndianOrder.flipNativeToBigEndian32(loopStart));
    }

    /**
     * Indicates the end of the portion of the sample that is
     * looped if the sound is sustained. The position is given in
     * the number of data samples from the start of the sound.
     * @return <DD/>
     */
    public int getLoopEnd() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(30));
    }

    /**
     * Indicates the end of the portion of the sample that is
     * looped if the sound is sustained. The position is given in
     * the number of data samples from the start of the sound.
     * @param loopEnd -
     */
    public void setLoopEnd(int loopEnd) {
        setIntAt(30, EndianOrder.flipNativeToBigEndian32(loopEnd));
    }

    /**
     * The number of the MIDI note produced if the sample is played at the rate specified in sampleRate.
     * @return <DD/>
     */
    public int getPitchNormal() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(34));
    }

    /**
     * The number of the MIDI note produced if the sample is played at the rate specified in sampleRate.
     * @param pitchNormal -
     */
    public void setPitchNormal(int pitchNormal) {
        setIntAt(34, EndianOrder.flipNativeToBigEndian32(pitchNormal));
    }

    /**
     * The lowest pitch at which to play the sample. Use for
     * instruments, such as pianos, that have different samples to
     * use for different pitch ranges.
     * @return <DD/>
     */
    public int getPitchLow() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(38));
    }

    /**
     * The lowest pitch at which to play the sample. Use for
     * instruments, such as pianos, that have different samples to
     * use for different pitch ranges.
     * @param pitchLow -
     */
    public void setPitchLow(int pitchLow) {
        setIntAt(38, EndianOrder.flipNativeToBigEndian32(pitchLow));
    }

    /**
     * The highest pitch at which to play the sample. Use for
     * instruments, such as pianos, that have different samples to
     * use for different pitch ranges.
     * @return <DD/>
     */
    public int getPitchHigh() {
        return EndianOrder.flipBigEndianToNative32(getIntAt(42));
    }

    /**
     * The highest pitch at which to play the sample. Use for
     * instruments, such as pianos, that have different samples to
     * use for different pitch ranges.
     * @param pitchHigh -
     */
    public void setPitchHigh(int pitchHigh) {
        setIntAt(42, EndianOrder.flipNativeToBigEndian32(pitchHigh));
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[dataFormat=" + QTUtils.fromOSType(getDataFormat()) + ",numChannels=" + getNumChannels() + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + ",sampleDataID=" + getSampleDataID() + ",offset=" + getOffset() + ",numSamples=" + getNumSamples() + ",loopType=" + getLoopType() + ",loopStart=" + getLoopStart() + ",loopEnd=" + getLoopEnd() + ",pitchNormal=" + getPitchNormal() + ",pitchLow=" + getPitchLow() + ",pitchHigh=" + getPitchHigh() + "]";
    }
}
