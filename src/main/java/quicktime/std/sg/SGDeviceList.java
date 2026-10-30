package quicktime.std.sg;

import quicktime.QTObject;
import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;

/**
 * SGDeviceList objects contain a list of device name structures.
 */
public class SGDeviceList extends QTHandleRef {

    private final SequenceGrabber sg;

    SGDeviceList(int nativeRep, Object ref, SequenceGrabber sg) {
        super(nativeRep, ref, false, QTObject.ID(sg));
        this.sg = sg;
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
     * @throws quicktime.std.StdQTException
     */
    public SGDeviceName getDeviceName(int whichOne) throws StdQTException {
        if ((whichOne < 0) || (whichOne >= getShortAt(0)))
            throw new StdQTException(-50);
        SGDeviceName deviceName = new SGDeviceName();
        copyToArray(8 + 80 * whichOne, deviceName.getBytes(), 0, 80);
        return deviceName;
    }

    /**
     * A String representation of the class.
     * @return a String represenation of this object
     */
    public String toString() {
        return getClass().getName() + "[count=" + getCount() + ",selectedIndex=" + getSelectedIndex() + "]";
    }
}
