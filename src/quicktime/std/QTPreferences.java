
package quicktime.std;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.movies.AtomContainer;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;


public final class QTPreferences implements QuickTimeLib {
    private static Object linkage;

    static class AC extends AtomContainer {
        AC(int paramInt) {
            super(paramInt, null, false);
        }
    }

    static {
        class PrivelegedAction {
            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTPreferences.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    public static AtomContainer getAccessKeys(String paramString) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = QTGetAccessKeys(QTUtils.String2PString(paramString, 255), 0, arrayOfInt);
        StdQTException.checkError(i);
        return new AC(arrayOfInt[0]);
    }

    public static void registerAccessKey(String paramString1, int paramInt, String paramString2) throws QTException {
        QTHandle localQTHandle = new QTHandle(paramString2.length(), false);
        localQTHandle.copyFromArray(0, paramString2.getBytes(), 0, paramString2.length());

        int i = QTRegisterAccessKey(QTUtils.String2PString(paramString1, 255), paramInt, QTObject.ID(localQTHandle));
        StdQTException.checkError(i);
    }

    public static void registerAccessKey(String paramString, int paramInt, QTHandleRef paramQTHandleRef) throws QTException {
        int i = QTRegisterAccessKey(QTUtils.String2PString(paramString, 255), paramInt, QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
    }

    public static void unregisterAccessKey(String paramString1, int paramInt, String paramString2) throws QTException {
        QTHandle localQTHandle = new QTHandle(paramString2.length(), false);
        localQTHandle.copyFromArray(0, paramString2.getBytes(), 0, paramString2.length());

        int i = QTUnregisterAccessKey(QTUtils.String2PString(paramString1, 255), paramInt, QTObject.ID(localQTHandle));
        StdQTException.checkError(i);
    }

    public static void unregisterAccessKey(String paramString, int paramInt, QTHandleRef paramQTHandleRef) throws QTException {
        int i = QTUnregisterAccessKey(QTUtils.String2PString(paramString, 255), paramInt, QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
    }

    public static AtomContainer getPreference(int paramInt) throws QTException {
        int[] arrayOfInt = {
            0
        };
        int i = GetQuickTimePreference(paramInt, arrayOfInt);
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0)
            return null;
        return new AC(arrayOfInt[0]);
    }

    public static void setPreference(int paramInt, AtomContainer paramAtomContainer) throws QTException {
        StdQTException.checkError(SetQuickTimePreference(paramInt, QTObject.ID(paramAtomContainer)));
    }

    private static native short QTGetAccessKeys(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt);

    private static native short QTRegisterAccessKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short QTUnregisterAccessKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short GetQuickTimePreference(int paramInt, int[] paramArrayOfInt);

    private static native short SetQuickTimePreference(int paramInt1, int paramInt2);
}
