
package quicktime.std.movies.media;

import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;


public final class LevelMeterInfo extends QTByteObject {
    public static final int kNativeSize = 4;

    public LevelMeterInfo() {
        super(4);
    }

    LevelMeterInfo(byte[] paramArrayOfByte) {
        super(paramArrayOfByte);
    }

    public int getNumChannels() {
        return getShortAt(0);
    }

    public int getLeftMeterLevel() {
        return QTUtils.UByte2Int(getByteAt(2));
    }

    public int getRightMeterLevel() {
        return QTUtils.UByte2Int(getByteAt(3));
    }

    public String toString() {
        return getClass().getName() + "[channels=" + getNumChannels() + ",leftLevel=" + getLeftMeterLevel() + ",rightLevel="
               + getRightMeterLevel() + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/LevelMeterInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
