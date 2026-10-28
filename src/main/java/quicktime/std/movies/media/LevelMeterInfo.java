package quicktime.std.movies.media;

import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * This class returns information from the SoundMediaHandler of the number of channels available
 * for metering and the left and right meter level
 */
public final class LevelMeterInfo extends QTByteObject {

    public static final int kNativeSize = 4;

    public LevelMeterInfo() {
        super(4);
    }

    LevelMeterInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    /**
     * mono or stereo source
     * @return the number of channels
     */
    public int getNumChannels() {
        return getShortAt(0);
    }

    /**
     * @return the left meter lever 0-255 range
     */
    public int getLeftMeterLevel() {
        return QTUtils.UByte2Int(getByteAt(2));
    }

    /**
     * @return the right meter lever 0-255 range
     */
    public int getRightMeterLevel() {
        return QTUtils.UByte2Int(getByteAt(3));
    }

    /**
     * Returns a string representation of this object
     * @return a String represenation of this object
     */
    public String toString() {
        return getClass().getName() + "[channels=" + getNumChannels() + ",leftLevel=" + getLeftMeterLevel() + ",rightLevel=" + getRightMeterLevel() + "]";
    }
}
