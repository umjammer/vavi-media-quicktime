package quicktime.streaming;

import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


/**
 * This class represents a QTSStatusParam structure that is returned when a notification proc
 * is called with a kQTSStatusNotification message
 */
public class StatusParams extends QTPointerRef implements PrimitivesLib {

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    StatusParams(int nativeRep, Object ref) {
        super(nativeRep, kNativeSize, ref);
    }

    /**
     * Retrieve the status
     */
    public int getStatus() {
        return getIntAt(0);
    }

    /**
     * Retrieve the status string
     */
    public String getStatusString() {
        return getStringFromPointer(getIntAt(4));
    }

    /**
     * Retrieve the detailed status
     */
    public int getDetailedStatus() {
        return getIntAt(8);
    }

    /**
     * Retrieve the detailed status string
     */
    public String getDetailedStatusString() {
        return getStringFromPointer(getIntAt(12));
    }

    private String getStringFromPointer(int ptr) {
        if (ptr != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(ptr, j++) != 0) i++;
            byte[] cString = new byte[i];
            copyPointerToArray(ptr, 0, cString, 0, i);
            return QTUtils.CString2String(cString, 0);
        }
        return "";
    }

    private static byte getByteFromPointer(int ptr, int offset) {
        return MemoryManager.getByteFromPointer(ptr, offset);
    }

    private static void copyPointerToArray(int ptr, int ptrOffset, byte[] array, int arrayOffset, int size) {
        MemoryManager.copyPointerToArray(ptr, ptrOffset, array, arrayOffset, size);
    }
}
