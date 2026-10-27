package quicktime.streaming;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

/**
 * This class represents a QTSStatusParam structure that is returned when a notification proc
 * is called with a kQTSStatusNotification message
 */
public class StatusParams extends QTPointerRef implements PrimitivesLib {

    private static Object linkage;

    /**
     * This is the size of this class
     */
    public static final int kNativeSize = 20;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(StatusParams.class));
            }
        }
        new PrivelegedAction().establish();
    }

    StatusParams(int paramInt, Object paramObject) {
        super(paramInt, kNativeSize, paramObject);
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

    private String getStringFromPointer(int paramInt) {
        if (paramInt != 0) {
            int i = 0;
            int j = 0;
            while (getByteFromPointer(paramInt, j++) != 0) i++;
            byte[] arrayOfByte = new byte[i];
            copyPointerToArray(paramInt, 0, arrayOfByte, 0, i);
            return QTUtils.CString2String(arrayOfByte, 0);
        }
        return "";
    }

    private static byte getByteFromPointer(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromPointer(paramInt1, paramInt2);
    }

    private static void copyPointerToArray(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4) {
        MemoryManager.copyPointerToArray(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4);
    }
}
