
package quicktime.std.sg;

import quicktime.QTObject;
import quicktime.std.StdQTException;
import quicktime.util.QTHandleRef;


public class SGDeviceList extends QTHandleRef {
    private SequenceGrabber sg;

    SGDeviceList(int paramInt, Object paramObject, SequenceGrabber paramSequenceGrabber) {
        super(paramInt, paramObject, false, QTObject.ID(paramSequenceGrabber));
        this.sg = paramSequenceGrabber;
    }

    public int getCount() {
        return getShortAt(0);
    }

    public int getSelectedIndex() {
        return getShortAt(2);
    }

    public SGDeviceName getDeviceName(int paramInt) throws StdQTException {
        if ((paramInt < 0) || (paramInt >= getShortAt(0)))
            throw new StdQTException(-50);
        SGDeviceName localSGDeviceName = new SGDeviceName();
        copyToArray(8 + 80 * paramInt, localSGDeviceName.getBytes(), 0, 80);
        return localSGDeviceName;
    }

    public String toString() {
        return getClass().getName() + "[count=" + getCount() + ",selectedIndex=" + getSelectedIndex() + "]";
    }
}
