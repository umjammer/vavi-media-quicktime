package quicktime.std.sg;

import quicktime.util.QTByteObject;
import quicktime.util.QTUtils;

/**
 * Each device name structure
 * identifies a single device that may be used by the channel. The SGDeviceName data type defines
 * the format of a device name structure.
 */
public class SGDeviceName extends QTByteObject {

    /**
     * the size of the native structure
     */
    public static final int kNativeSize = 80;

    SGDeviceName() {
        super(80);
    }

    /**
     * @return the name of this device.
     */
    public String getName() {
        return QTUtils.PString2String(getBytes(), 0);
    }

    /**
     * @return the flags associated with this device
     */
    public int getFlags() {
        return getIntAt(68);
    }

    /**
     * Returns a string representation of this object
     * @return a String represenation of this object
     */
    public String toString() {
        return getClass().getName() + "[flags=" + getFlags() + ",name=" + getName() + "]";
    }
}

