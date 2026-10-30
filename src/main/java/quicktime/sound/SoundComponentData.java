package quicktime.sound;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


public final class SoundComponentData extends QTByteObject implements PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 28;

    private QTPointer buff;

    @Serial
    private static final long serialVersionUID = -7514870412490785051L;

    public SoundComponentData() {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[kNativeSize];
        in.read(this.bytes);
        for (int i = 0; i < 8; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
        for (int i = 8; i < 12; i += 2) {
            setShortInArray(this.bytes, i, EndianOrder.flipBigEndianToNative16(getShortFromArray(this.bytes, i)));
        }
        for (int i = 0; ; i += 4) {
            setIntInArray(this.bytes, i, EndianOrder.flipBigEndianToNative32(getIntFromArray(this.bytes, i)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[kNativeSize];
        for (int i = 0; i < 8; i += 4) {
            setIntInArray(movie, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        for (int i = 8; i < 12; i += 2) {
            setShortInArray(movie, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        for (int i = 12; i < 28; i += 4) {
            setIntInArray(movie, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        setIntInArray(movie, 20, 0);
        out.write(movie);
    }

    /**
     * A set of bit flags whose meanings are specific to a particular sound component.
     */
    public int getFlags() {
        return getIntAt(0);
    }

    /**
     * A set of bit flags whose meanings are specific to a particular sound component.
     */
    public void setFlags(int flags) {
        setIntAt(0, flags);
    }

    /**
     * The format of the data a sound component is producing.
     */
    public int getFormat() {
        return getIntAt(4);
    }

    /**
     * The format of the data a sound component is producing.
     */
    public void setFormat(int format) {
        setIntAt(4, format);
    }

    /**
     * The number of channels of sound in the output data stream.
     */
    public int getNumChannels() {
        return getShortAt(8);
    }

    /**
     * The number of channels of sound in the output data stream.
     */
    public void setNumChannels(int numChannels) {
        setShortAt(8, (short) numChannels);
    }

    /**
     * The size, in bits, of each sample in the output data stream.
     */
    public int getSampleSize() {
        return getShortAt(10);
    }

    /**
     * The size, in bits, of each sample in the output data stream.
     */
    public void setSampleSize(int sampleSize) {
        setShortAt(10, (short) sampleSize);
    }

    /**
     * The sample rate for the audio data.
     */
    public float getSampleRate() {
        return QTUtils.UFix2X(getIntAt(12));
    }

    /**
     * The sample rate for the audio data.
     */
    public void setSampleRate(float sampleRate) {
        setIntAt(12, QTUtils.X2UFix(sampleRate));
    }

    /**
     * The number of samples in the buffer pointed to by the buffer field. For compressed sounds, this field indicates the number of compressed samples in the sound, not the size of the buffer.
     */
    public int getSampleCount() {
        return getIntAt(16);
    }

    /**
     * The number of samples in the buffer pointed to by the buffer field. For compressed sounds, this field indicates the number of compressed samples in the sound, not the size of the buffer.
     */
    public void setSampleCount(int sampleCount) {
        setIntAt(16, sampleCount);
    }

    /**
     * The buffer that contains the sound data.
     */
    public QTPointer getBuffer() {
        return QTPointer.fromSCData(this);
    }

    /**
     * The buffer that contains the sound data.
     * If the size of the incoming buffer is less than (getSampleSize() / 8 * getSampleCount())
     * a paramErr exception is thrown as the buffer is not large enough to hold the specified samples.
     * This also means that the sample size and sample count fields must be set before the buffer is set.
     */
    public void setBuffer(QTPointer buffer) throws SoundException {
        if (buffer.getSize() < getSampleSize() / 8 * getSampleCount())
            throw new SoundException(-50);
        setIntAt(20, QTObject.ID(buffer));
        this.buff = buffer;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",format=" + QTUtils.fromOSType(getFormat()) + ",numChannels=" + getNumChannels() + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + ",sampleCount=" + getSampleCount() + "]";
    }

    private static short getShortFromArray(byte[] array, int offset) {
        return MemoryManager.getShortFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }

    private static void setShortInArray(byte[] array, int offset, short value) {
        MemoryManager.setShortInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }
}
