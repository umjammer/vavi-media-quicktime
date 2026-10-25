package quicktime.sound;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;

public final class SoundComponentData extends QTByteObject implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 28;

    private QTPointer buff;

    private static final long serialVersionUID = -7514870412490785051L;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SoundComponentData.class);
                    }
                });
            }
        }.establish();
    }

    public SoundComponentData() {
        super(28);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[28];
        paramObjectInputStream.read(this.bytes);
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

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[28];
        for (int i = 0; i < 8; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        for (int i = 8; i < 12; i += 2) {
            setShortInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian16(getShortFromArray(getBytes(), i)));
        }
        for (int i = 12; i < 28; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        setIntInArray(arrayOfByte, 20, 0);
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * A set of bit flags whose meanings are specific to a particular sound component.
     */
    public final int getFlags() {
        return getIntAt(0);
    }

    /**
     * A set of bit flags whose meanings are specific to a particular sound component.
     */
    public final void setFlags(int paramInt) {
        setIntAt(0, paramInt);
    }

    /**
     * The format of the data a sound component is producing.
     */
    public final int getFormat() {
        return getIntAt(4);
    }

    /**
     * The format of the data a sound component is producing.
     */
    public final void setFormat(int paramInt) {
        setIntAt(4, paramInt);
    }

    /**
     * The number of channels of sound in the output data stream.
     */
    public final int getNumChannels() {
        return getShortAt(8);
    }

    /**
     * The number of channels of sound in the output data stream.
     */
    public final void setNumChannels(int paramInt) {
        setShortAt(8, (short) paramInt);
    }

    /**
     * The size, in bits, of each sample in the output data stream.
     */
    public final int getSampleSize() {
        return getShortAt(10);
    }

    /**
     * The size, in bits, of each sample in the output data stream.
     */
    public final void setSampleSize(int paramInt) {
        setShortAt(10, (short) paramInt);
    }

    /**
     * The sample rate for the audio data.
     */
    public final float getSampleRate() {
        return QTUtils.UFix2X(getIntAt(12));
    }

    /**
     * The sample rate for the audio data.
     */
    public final void setSampleRate(float paramFloat) {
        setIntAt(12, QTUtils.X2UFix(paramFloat));
    }

    /**
     * The number of samples in the buffer pointed to by the buffer field. For compressed sounds, this field indicates the number of compressed samples in the sound, not the size of the buffer.
     */
    public final int getSampleCount() {
        return getIntAt(16);
    }

    /**
     * The number of samples in the buffer pointed to by the buffer field. For compressed sounds, this field indicates the number of compressed samples in the sound, not the size of the buffer.
     */
    public final void setSampleCount(int paramInt) {
        setIntAt(16, paramInt);
    }

    /**
     * The buffer that contains the sound data.
     */
    public final QTPointer getBuffer() {
        return QTPointer.fromSCData(this);
    }

    /**
     * The buffer that contains the sound data.
     * If the size of the incoming buffer is less than (getSampleSize() / 8 * getSampleCount())
     * a paramErr exception is thrown as the buffer is not large enough to hold the specified samples.
     * This also means that the sample size and sample count fields must be set before the buffer is set.
     */
    public final void setBuffer(QTPointer paramQTPointer) throws SoundException {
        if (paramQTPointer.getSize() < getSampleSize() / 8 * getSampleCount())
            throw new SoundException(-50);
        setIntAt(20, QTObject.ID(paramQTPointer));
        this.buff = paramQTPointer;
    }

    /**
     * Returns a String representation of this object.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",format=" + QTUtils.fromOSType(getFormat()) + ",numChannels=" + getNumChannels() + ",sampleSize=" + getSampleSize() + ",sampleRate=" + getSampleRate() + ",sampleCount=" + getSampleCount() + "]";
    }

    private static short getShortFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getShortFromArray(paramArrayOfByte, paramInt);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setShortInArray(byte[] paramArrayOfByte, int paramInt, short paramShort) {
        MemoryManager.setShortInArray(paramArrayOfByte, paramInt, paramShort);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
