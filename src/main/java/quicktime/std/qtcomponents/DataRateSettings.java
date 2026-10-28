package quicktime.std.qtcomponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;


public final class DataRateSettings extends QTByteObject implements PrimitivesLib {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 16;

    @Serial
    private static final long serialVersionUID = 6877811007326112776L;

    /**
     * Construct temporal settings to set the dialog display.
     */
    public DataRateSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this();
        setDataRate(paramInt1);
        setFrameDuration(paramInt2);
        setMinSpatialQuality(paramInt3);
        setMinTemporalQuality(paramInt4);
    }

    /**
     * Constructor for building a data rate settings object from a byte array
     * (such as the one you can get from an atom container)
     */
    public DataRateSettings(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    DataRateSettings() {
        super(kNativeSize);
    }

    @Serial
    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
        this.bytes = new byte[16];
        byte[] arrayOfByte = new byte[kNativeSize];
        paramObjectInputStream.read(arrayOfByte);
        for (int i = 0; i < kNativeSize; i += 4) {
            setIntInArray(getBytes(), i, EndianOrder.flipBigEndianToNative32(getIntFromArray(arrayOfByte, i)));
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        byte[] arrayOfByte = new byte[kNativeSize];
        for (int i = 0; i < kNativeSize; i += 4) {
            setIntInArray(arrayOfByte, i, EndianOrder.flipNativeToBigEndian32(getIntFromArray(getBytes(), i)));
        }
        paramObjectOutputStream.write(arrayOfByte);
    }

    /**
     * Get the default data rate in the dialog box.
     * @return the data rate
     */
    public int getDataRate() {
        return getIntAt(0);
    }

    /**
     * Specifies the default data rate in the dialog box.
     * @param dataRate the data rate
     */
    public void setDataRate(int dataRate) {
        setIntAt(0, dataRate);
    }

    /**
     * Get the default frame duration in the dialog box.
     * @return the frame duration
     */
    public int getFrameDuration() {
        return getIntAt(4);
    }

    /**
     * Specifies the default frame duration in the dialog box.
     * @param frameDuration the frameDuration
     */
    public void setFrameDuration(int frameDuration) {
        setIntAt(4, frameDuration);
    }

    /**
     * Get the default min spatial quality slider in the dialog box.
     * @return the quality setting
     */
    public int getMinSpatialQuality() {
        return getIntAt(8);
    }

    /**
     * Specifies the min spatial quality slider in the dialog box.
     * @param minSpatialQuality the quality setting
     */
    public void setMinSpatialQuality(int minSpatialQuality) {
        setIntAt(8, minSpatialQuality);
    }

    /**
     * Get the default min temporal quality slider in the dialog box.
     * @return the quality setting
     */
    public int getMinTemporalQuality() {
        return getIntAt(12);
    }

    /**
     * Specifies the min temporal quality slider in the dialog box.
     * @param minTemporalQuality the quality setting
     */
    public void setMinTemporalQuality(int minTemporalQuality) {
        setIntAt(12, minTemporalQuality);
    }

    private static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getIntFromArray(paramArrayOfByte, paramInt);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }
}
