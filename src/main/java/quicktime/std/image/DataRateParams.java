package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


/**
 * The Image Compression Manager provides this object to allow your application
 * to communicate information to compressors that can constrain compressed data
 * to a specific data rate.
 * QuickTime:DataRateParams
 */
public final class DataRateParams extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 24;

    @Serial
    private static final long serialVersionUID = 7326971667474597100L;

    /**
     * Creates a DataRateParams object with no values set.
     */
    public DataRateParams() {
        super(24);
    }

    /**
     * Create a DataRateParams object and set the relevant values.
     *
     * @param dataRate the bytes/second to which the data rate must be constrained
     * @param dataOverrun number of bytes above or below the desired rate, if your
     *                     application does not know the data overrun, this value
     *                     should be zero.
     * @param frameDuration in milliseconds
     * @param keyFrameRate the frequency of key frames
     * @param minSpatialQuality the minimum spatial quality the compressor should
     *                           use to meet the requested data rate
     * @param minTemporalQuality the minimum temporal quality the compressor should
     *                            use to meet the requested data rate
     */
    public DataRateParams(int dataRate, int dataOverrun, int frameDuration, int keyFrameRate, int minSpatialQuality, int minTemporalQuality) {
        this();
        setDataRate(dataRate);
        setDataOverrun(dataOverrun);
        setFrameDuration(frameDuration);
        setKeyFrameRate(keyFrameRate);
        setMinSpatialQuality(minSpatialQuality);
        setMinTemporalQuality(minTemporalQuality);
    }

    private DataRateParams(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[24];
        byte[] arrayOfByte = new byte[24];
        paramObjectInputStream.read(arrayOfByte);
        for (int i = 0; i < 24; i += 4) {
            int j = getIntFromArray(arrayOfByte, i);
            int k = EndianOrder.flipBigEndianToNative32(j);
            setIntInArray(getBytes(), i, k);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[24];
        for (int i = 0; i < 24; i += 4) {
            int j = getIntFromArray(getBytes(), i);
            int k = EndianOrder.flipNativeToBigEndian32(j);
            setIntInArray(arrayOfByte, i, k);
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Gets the bytes per second to which the data rate is constrained.
     */
    public int getDataRate() {
        return getIntAt(0);
    }

    /**
     * Gets the current number of bytes above or below the desired rate. A value of
     * zero means that the data rate is being met exactly.
     *
     * @return the data overrun in bytes
     */
    public int getDataOverrun() {
        return getIntAt(4);
    }

    /**
     * Gets the duration of the current frame in milliseconds.
     *
     * @return the current frame duration in milliseconds
     */
    public int getFrameDuration() {
        return getIntAt(8);
    }

    /**
     * Gets the frequence of key frames. This frequency is normally identical
     * to the key frame rate passed to the compressBegin method.
     *
     * @return the key frame rate
     */
    public int getKeyFrameRate() {
        return getIntAt(12);
    }

    /**
     * Gets the minimum spatial quality the compressor should use to meet the
     * requested data rate.
     *
     * @return the minimum spatial quality
     */
    public int getMinSpatialQuality() {
        return getIntAt(16);
    }

    /**
     * Gets the minimum temporal quality the compressor should use to meet the
     * requested data rate.
     *
     * @return the minimum temporal quality
     */
    public int getMinTemporalQuality() {
        return getIntAt(20);
    }

    /**
     * Sets the bytes per second to which the data rate must be constrained.
     *
     * @param dataRate in bytes/second
     */
    public void setDataRate(int dataRate) {
        setIntAt(0, dataRate);
    }

    /**
     * Sets the number of bytes above or below the desired data rate. If your
     * application does not know the data overrun, this value should be set to
     * zero.
     *
     * @param dataOverrun number of bytes
     */
    public void setDataOverrun(int dataOverrun) {
        setIntAt(4, dataOverrun);
    }

    /**
     * Sets the duration of the frame in milliseconds.
     *
     * @param frameDuration in milliseconds
     */
    public void setFrameDuration(int frameDuration) {
        setIntAt(8, frameDuration);
    }

    /**
     * Sets the frequence of key frames. This frequency is normally identical
     * to the key frame rate passed to the compressBegin method.
     *
     * @param keyFrameRate the frequency
     */
    public void setKeyFrameRate(int keyFrameRate) {
        setIntAt(12, keyFrameRate);
    }

    /**
     * Sets the minimum spatial quality the compressor should use to meet the
     * requested data rate.
     *
     * @param minSpatialQuality an int
     */
    public void setMinSpatialQuality(int minSpatialQuality) {
        setIntAt(16, minSpatialQuality);
    }

    /**
     * Sets the minimum temporal quality the compressor should use to meet the
     * requested data rate.
     *
     * @param minTemporalQuality an int
     */
    public void setMinTemporalQuality(int minTemporalQuality) {
        setIntAt(20, minTemporalQuality);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new DataRateParams(getBytes());
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
