package quicktime.std;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.PlatformManager;
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
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(QTPreferences.class));
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * This method returns all the application and system access keys of a specified access key type.
     * @see "QuickTime::QTGetAccessKeys"
     * @param keyType the type of access keys to return.
     * @return The AtomContainer contains atoms of type kAccessKeyAtomType at the top level.
     * @throws quicktime.std.StdQTException
     */
    public static AtomContainer getAccessKeys(String paramString) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = QTGetAccessKeys(QTUtils.String2PString(paramString, 255), 0, arrayOfInt);
        StdQTException.checkError(i);
        return new AC(arrayOfInt[0]);
    }

    /**
     * This method registers an access key.
     * To register a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To register an application access key, set flags parameter to 0.
     * If the access key has already been registered, no error is returned, and the request is simply ignored
     * @see "QuickTime::QTRegisterAccessKey"
     * @param keyType the type of access keys to be registered.
     * @param flags flag specifying the operation of this method.
     * @param accessKey the key to be registered.
     * @throws quicktime.QTException
     */
    public static void registerAccessKey(String paramString1, int paramInt, String paramString2) throws QTException {
        QTHandle localQTHandle = new QTHandle(paramString2.length(), false);
        localQTHandle.copyFromArray(0, paramString2.getBytes(), 0, paramString2.length());
        int i = QTRegisterAccessKey(QTUtils.String2PString(paramString1, 255), paramInt, QTObject.ID(localQTHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method registers an access key.
     * To register a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To register an application access key, set flags parameter to 0.
     * If the access key has already been registered, no error is returned, and the request is simply ignored
     * @see "QuickTime::QTRegisterAccessKey"
     * @param keyType the type of access keys to be registered.
     * @param flags flag specifying the operation of this method.
     * @param accessKey the key to be registered.
     * @throws quicktime.QTException
     */
    public static void registerAccessKey(String paramString, int paramInt, QTHandleRef paramQTHandleRef) throws QTException {
        int i = QTRegisterAccessKey(QTUtils.String2PString(paramString, 255), paramInt, QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
    }

    /**
     * This method removes an access key.
     * To remove a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To remove an application access key, set flags parameter to 0.
     * @see "QuickTime::QTUnregisterAccessKey"
     * @param keyType the type of the access key to be removed.
     * @param flags flag specifying the operation of this method.
     * @param accessKey the key to be removed.
     * @throws quicktime.QTException
     */
    public static void unregisterAccessKey(String paramString1, int paramInt, String paramString2) throws QTException {
        QTHandle localQTHandle = new QTHandle(paramString2.length(), false);
        localQTHandle.copyFromArray(0, paramString2.getBytes(), 0, paramString2.length());
        int i = QTUnregisterAccessKey(QTUtils.String2PString(paramString1, 255), paramInt, QTObject.ID(localQTHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method removes an access key.
     * To remove a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To remove an application access key, set flags parameter to 0.
     * @see "QuickTime::QTUnregisterAccessKey"
     * @param keyType the type of the access key to be removed.
     * @param flags flag specifying the operation of this method.
     * @param accessKey the key to be removed.
     * @throws quicktime.QTException
     */
    public static void unregisterAccessKey(String paramString, int paramInt, QTHandleRef paramQTHandleRef) throws QTException {
        int i = QTUnregisterAccessKey(QTUtils.String2PString(paramString, 255), paramInt, QTObject.ID(paramQTHandleRef));
        StdQTException.checkError(i);
    }

    /**
     * This method retrieves the preference value associated with a specific preference type.
     * @see "QuickTime::GetQuickTimePreference"
     * @param preferenceType the type of the preference to be retrieved.
     * @return the atom container encapsulating the specific preference
     * @throws quicktime.QTException
     */
    public static AtomContainer getPreference(int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        int i = GetQuickTimePreference(paramInt, arrayOfInt);
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0)
            return null;
        return new AC(arrayOfInt[0]);
    }

    /**
     * This method sets the preference value associated with a specific preference type.
     * @see "QuickTime::SetQuickTimePreference"
     * @param preferenceType the type of the preference to be retrieved.
     * @param prefAtom the atom container encapsulating the values of the specified preference
     * @throws quicktime.QTException
     */
    public static void setPreference(int paramInt, AtomContainer paramAtomContainer) throws QTException {
        StdQTException.checkError(SetQuickTimePreference(paramInt, QTObject.ID(paramAtomContainer)));
    }

    private static short QTGetAccessKeys(byte[] paramArrayOfByte, int paramInt, int[] paramArrayOfInt) {
        return PlatformManager.QTGetAccessKeys(paramArrayOfByte, paramInt, paramArrayOfInt);
    }

    private static short QTRegisterAccessKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        return PlatformManager.QTRegisterAccessKey(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static short QTUnregisterAccessKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        return PlatformManager.QTUnregisterAccessKey(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static short GetQuickTimePreference(int paramInt, int[] paramArrayOfInt) {
        return PlatformManager.GetQuickTimePreference(paramInt, paramArrayOfInt);
    }

    private static short SetQuickTimePreference(int paramInt1, int paramInt2) {
        return PlatformManager.SetQuickTimePreference(paramInt1, paramInt2);
    }
}
