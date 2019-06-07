package quicktime.std.sg;

import quicktime.QTObject;
import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;

/**
 * SGDeviceList objects contain a list of device name structures.
 */
public class SGDeviceList extends QTHandleRef {

    private SequenceGrabber sg;

    SGDeviceList(int paramInt, Object paramObject, SequenceGrabber paramSequenceGrabber) {
        super(paramInt, paramObject, false, QTObject.ID(paramSequenceGrabber));
        this.sg = paramSequenceGrabber;
    }

    /**
     * @return the number of devices that are available to the SGChannel.
     */
    public int getCount() {
        return getShortAt(0);
    }

    /**
     * @return the currently selected device of the available devices.
     */
    public int getSelectedIndex() {
        return getShortAt(2);
    }

    /**
     * Returns the device name at the specified (0-based) index.
     * @param whichOne the device of this list to retrieve the name
     * @return the specified Device's name
     * @return {@link quicktime.std.StdQTException}
     */
    public SGDeviceName getDeviceName(int paramInt) throws StdQTException {
        if ((paramInt < 0) || (paramInt >= getShortAt(0)))
            throw new StdQTException(-50);
        SGDeviceName localSGDeviceName = new SGDeviceName();
        copyToArray(8 + 80 * paramInt, localSGDeviceName.getBytes(), 0, 80);
        return localSGDeviceName;
    }

    /**
     * A String representation of the class.
     * @return a String represenation of this object
     */
    public String toString() {
        return getClass().getName() + "[count=" + getCount() + ",selectedIndex=" + getSelectedIndex() + "]";
    }
}
