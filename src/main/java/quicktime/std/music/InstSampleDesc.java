package quicktime.std.music;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    static final long serialVersionUID = -7868738374047072834L;

    /**
     * Makes a instance of this class.
     */
    public InstSampleDesc() {
        super(46);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[46];
        paramObjectInputStream.read(getBytes());
    }

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
    public void setDataFormat(int paramInt) {
        setIntAt(0, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setNumChannels(int paramInt) {
        setShortAt(4, EndianOrder.flipNativeToBigEndian16((short) paramInt));
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
    public void setSampleSize(int paramInt) {
        setShortAt(6, EndianOrder.flipNativeToBigEndian16((short) paramInt));
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
    public void setSampleRate(float paramFloat) {
        setSampleRateRaw(QTUtils.X2Fix(paramFloat));
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
    public void setSampleRateRaw(int paramInt) {
        setIntAt(8, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setSampleDataID(int paramInt) {
        setShortAt(12, EndianOrder.flipNativeToBigEndian16((short) paramInt));
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
    public void setOffset(int paramInt) {
        setIntAt(14, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setNumSamples(int paramInt) {
        setIntAt(18, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setLoopType(int paramInt) {
        setIntAt(22, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setLoopStart(int paramInt) {
        setIntAt(26, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setLoopEnd(int paramInt) {
        setIntAt(30, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setPitchNormal(int paramInt) {
        setIntAt(34, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setPitchLow(int paramInt) {
        setIntAt(38, EndianOrder.flipNativeToBigEndian32(paramInt));
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
    public void setPitchHigh(int paramInt) {
        setIntAt(42, EndianOrder.flipNativeToBigEndian32(paramInt));
    }

    /**
     * Returns a string representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[dataFormat=" + QTUtils.fromOSType(getDataFormat()) + ",numChannels=" + getNumChannels() + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + ",sampleDataID=" + getSampleDataID() + ",offset=" + getOffset() + ",numSamples=" + getNumSamples() + ",loopType=" + getLoopType() + ",loopStart=" + getLoopStart() + ",loopEnd=" + getLoopEnd() + ",pitchNormal=" + getPitchNormal() + ",pitchLow=" + getPitchLow() + ",pitchHigh=" + getPitchHigh() + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/music
 * /InstSampleDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
