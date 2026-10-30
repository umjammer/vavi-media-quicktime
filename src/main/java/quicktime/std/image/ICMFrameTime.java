package quicktime.std.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.std.clocks.TimeBase;
import quicktime.util.EndianOrder;
import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


/**
 * No QT documentation available for the ICMTimeFrameRecord struct
 * QuickTime:ICMTimeFrameRecord
 */
public final class ICMFrameTime extends QTByteObject implements PrimitivesLib, Cloneable {

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 48;

    @Serial
    private static final long serialVersionUID = 8668877262716984973L;

    /**
     * Creates an ICMFrameTime object. It will set the size of the record to the extended TimeFrameRecord
     * and will set the flag to tell QT that it is the extended record structure.
     */
    public ICMFrameTime() {
        super(48);
        setRecordSize(48);
        setFlags(1);
    }

    private ICMFrameTime(byte[] bytes) {
        super(bytes);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException {
        this.bytes = new byte[48];
        in.read(this.bytes);
        long l1 = getLongFromArray(this.bytes, 0);
        setLongInArray(this.bytes, 0, EndianOrder.flipBigEndianToNative64(l1));
        int i = getIntFromArray(this.bytes, 8);
        setIntInArray(this.bytes, 8, EndianOrder.flipBigEndianToNative32(i));
        setIntInArray(this.bytes, 12, 0);
        for (int j = 16; j < 36; j += 4) {
            int k = getIntFromArray(this.bytes, j);
            int m = EndianOrder.flipBigEndianToNative32(k);
            setIntInArray(this.bytes, j, m);
        }
        long l2 = getLongFromArray(this.bytes, 36);
        setLongInArray(this.bytes, 36, EndianOrder.flipBigEndianToNative64(l2));
        int m = getIntFromArray(this.bytes, 44);
        setIntInArray(this.bytes, 44, EndianOrder.flipNativeToBigEndian32(m));
        setBase(TimeBase.fromICMFrameTime(this));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] movie = new byte[48];
        long l1 = getValue();
        setLongInArray(movie, 0, EndianOrder.flipNativeToBigEndian64(l1));
        int i = getScale();
        setIntInArray(movie, 8, EndianOrder.flipNativeToBigEndian32(i));
        setIntInArray(movie, 12, 0);
        for (int j = 16; j < 36; j += 4) {
            int k = getIntFromArray(getBytes(), j);
            int m = EndianOrder.flipNativeToBigEndian32(k);
            setIntInArray(movie, j, m);
        }
        long l2 = getVirtualStartTime();
        setLongInArray(movie, 36, EndianOrder.flipNativeToBigEndian64(l2));
        int m = getVirtualDuration();
        setIntInArray(movie, 44, EndianOrder.flipNativeToBigEndian32(m));
        out.write(movie);
    }

    private void setRecordSize(int recordSize) {
        setIntAt(24, recordSize);
    }

    /**
     * No QT documentation available.
     *
     * @param value -
     */
    public void setValue(long value) {
        setLongAt(0, value);
    }

    /**
     * No QT documentation available.
     *
     * @param scale -
     */
    public void setScale(int scale) {
        setIntAt(8, scale);
    }

    /**
     * No QT documentation available.
     *
     * @param duration -
     */
    public void setDuration(int duration) {
        setIntAt(16, duration);
    }

    /**
     * No QT documentation available.
     *
     * @param rate -
     */
    public void setRate(float rate) {
        setIntAt(20, QTUtils.X2Fix(rate));
    }

    /**
     * No QT documentation available.
     *
     * @param frameNumber -
     */
    public void setFrameNumber(int frameNumber) {
        setIntAt(28, frameNumber);
    }

    /**
     * No QT documentation available.
     *
     * @return <DD/>
     */
    public long getValue() {
        return getLongAt(0);
    }

    /**
     * No QT documentation available.
     *
     * @return <DD/>
     */
    public int getScale() {
        return getIntAt(8);
    }

    /**
     * @return the current time base - can be null
     */
    public TimeBase getBase() {
        return TimeBase.fromICMFrameTime(this);
    }

    /**
     * @param base the time base that will use this object - can be null
     */
    public void setBase(TimeBase base) {
        setIntAt(12, QTObject.ID(base));
    }

    /**
     * No QT documentation available.
     *
     * @return <DD/>
     */
    public int getDuration() {
        return getIntAt(16);
    }

    /**
     * No QT documentation available.
     *
     * @return <DD/>
     */
    public float getRate() {
        return QTUtils.Fix2X(getIntAt(20));
    }

    /**
     * No QT documentation available.
     *
     * @return <DD/>
     */
    public int getFrameNumber() {
        return getIntAt(28);
    }

    /**
     * @param flags the flags for this structure
     */
    public void setFlags(int flags) {
        setIntAt(32, flags);
    }

    /**
     * @return the current flags
     */
    public int getFlags() {
        return getIntAt(32);
    }

    /**
     * @param time the new virtual start time
     */
    public void setVirtualStartTime(long time) {
        setLongAt(36, time);
    }

    /**
     * @return the virtual start time
     */
    public long getVirtualStartTime() {
        return getLongAt(36);
    }

    /**
     * @param duration the virtual duration
     */
    public void setVirtualDuration(int duration) {
        setIntAt(44, duration);
    }

    /**
     * @return the virtual duration
     */
    public int getVirtualDuration() {
        return getIntAt(44);
    }

    /**
     * Returns a copy of this class.
     */
    @Override
    public Object clone() {
        return new ICMFrameTime(this.bytes);
    }

    /**
     * Returns a string representation of the class
     */
    public String toString() {
        return getClass().getName();
    }

    private static void setLongInArray(byte[] array, int offset, long value) {
        MemoryManager.setLongInArray(array, offset, value);
    }

    private static void setIntInArray(byte[] array, int offset, int value) {
        MemoryManager.setIntInArray(array, offset, value);
    }

    private static long getLongFromArray(byte[] array, int offset) {
        return MemoryManager.getLongFromArray(array, offset);
    }

    private static int getIntFromArray(byte[] array, int offset) {
        return MemoryManager.getIntFromArray(array, offset);
    }
}
