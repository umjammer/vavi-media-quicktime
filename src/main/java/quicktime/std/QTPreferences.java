package quicktime.std;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.movies.AtomContainer;
import quicktime.util.QTHandle;
import quicktime.util.QTHandleRef;
import quicktime.util.QTUtils;


public final class QTPreferences implements QuickTimeLib {

    static class AC extends AtomContainer {

        AC(int nativeRep) {
            super(nativeRep, null, false);
        }
    }

    /**
     * This method returns all the application and system access keys of a specified access key type.
     *
     * @param keyType the type of access keys to return.
     * @return The AtomContainer contains atoms of type kAccessKeyAtomType at the top level.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTGetAccessKeys"
     */
    public static AtomContainer getAccessKeys(String keyType) throws StdQTException {
        int[] keys = {0};
        int i = QTGetAccessKeys(QTUtils.String2PString(keyType, 255), 0, keys);
        StdQTException.checkError(i);
        return new AC(keys[0]);
    }

    /**
     * This method registers an access key.
     * To register a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To register an application access key, set flags parameter to 0.
     * If the access key has already been registered, no error is returned, and the request is simply ignored
     *
     * @param keyType   the type of access keys to be registered.
     * @param flags     flag specifying the operation of this method.
     * @param accessKey the key to be registered.
     * @throws quicktime.QTException
     * @see "QuickTime::QTRegisterAccessKey"
     */
    public static void registerAccessKey(String keyType, int flags, String accessKey) throws QTException {
        QTHandle qtHandle = new QTHandle(accessKey.length(), false);
        qtHandle.copyFromArray(0, accessKey.getBytes(), 0, accessKey.length());
        int i = QTRegisterAccessKey(QTUtils.String2PString(keyType, 255), flags, QTObject.ID(qtHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method registers an access key.
     * To register a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To register an application access key, set flags parameter to 0.
     * If the access key has already been registered, no error is returned, and the request is simply ignored
     *
     * @param keyType   the type of access keys to be registered.
     * @param flags     flag specifying the operation of this method.
     * @param accessKey the key to be registered.
     * @throws quicktime.QTException
     * @see "QuickTime::QTRegisterAccessKey"
     */
    public static void registerAccessKey(String keyType, int flags, QTHandleRef accessKey) throws QTException {
        int i = QTRegisterAccessKey(QTUtils.String2PString(keyType, 255), flags, QTObject.ID(accessKey));
        StdQTException.checkError(i);
    }

    /**
     * This method removes an access key.
     * To remove a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To remove an application access key, set flags parameter to 0.
     *
     * @param keyType   the type of the access key to be removed.
     * @param flags     flag specifying the operation of this method.
     * @param accessKey the key to be removed.
     * @throws quicktime.QTException
     * @see "QuickTime::QTUnregisterAccessKey"
     */
    public static void unregisterAccessKey(String keyType, int flags, String accessKey) throws QTException {
        QTHandle qtHandle = new QTHandle(accessKey.length(), false);
        qtHandle.copyFromArray(0, accessKey.getBytes(), 0, accessKey.length());
        int i = QTUnregisterAccessKey(QTUtils.String2PString(keyType, 255), flags, QTObject.ID(qtHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method removes an access key.
     * To remove a system access key, set flags parameter to kAccessKeySystemFlag (1).
     * To remove an application access key, set flags parameter to 0.
     *
     * @param keyType   the type of the access key to be removed.
     * @param flags     flag specifying the operation of this method.
     * @param accessKey the key to be removed.
     * @throws quicktime.QTException
     * @see "QuickTime::QTUnregisterAccessKey"
     */
    public static void unregisterAccessKey(String keyType, int flags, QTHandleRef accessKey) throws QTException {
        int i = QTUnregisterAccessKey(QTUtils.String2PString(keyType, 255), flags, QTObject.ID(accessKey));
        StdQTException.checkError(i);
    }

    /**
     * This method retrieves the preference value associated with a specific preference type.
     *
     * @param preferenceType the type of the preference to be retrieved.
     * @return the atom container encapsulating the specific preference
     * @throws quicktime.QTException
     * @see "QuickTime::GetQuickTimePreference"
     */
    public static AtomContainer getPreference(int preferenceType) throws QTException {
        int[] container = {0};
        int i = GetQuickTimePreference(preferenceType, container);
        StdQTException.checkError(i);
        if (container[0] == 0)
            return null;
        return new AC(container[0]);
    }

    /**
     * This method sets the preference value associated with a specific preference type.
     *
     * @param preferenceType the type of the preference to be retrieved.
     * @param prefAtom       the atom container encapsulating the values of the specified preference
     * @throws quicktime.QTException
     * @see "QuickTime::SetQuickTimePreference"
     */
    public static void setPreference(int preferenceType, AtomContainer prefAtom) throws QTException {
        StdQTException.checkError(SetQuickTimePreference(preferenceType, QTObject.ID(prefAtom)));
    }

    private static short QTGetAccessKeys(byte[] accessKeyType, int flags, int[] keys) {
        return PlatformManager.QTGetAccessKeys(accessKeyType, flags, keys);
    }

    private static short QTRegisterAccessKey(byte[] accessKeyType, int flags, int accessKey) {
        return PlatformManager.QTRegisterAccessKey(accessKeyType, flags, accessKey);
    }

    private static short QTUnregisterAccessKey(byte[] accessKeyType, int flags, int accessKey) {
        return PlatformManager.QTUnregisterAccessKey(accessKeyType, flags, accessKey);
    }

    private static short GetQuickTimePreference(int type, int[] container) {
        return PlatformManager.GetQuickTimePreference(type, container);
    }

    private static short SetQuickTimePreference(int type, int container) {
        return PlatformManager.SetQuickTimePreference(type, container);
    }
}
