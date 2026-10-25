package quicktime.std.comp;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;

/**
 * Representation of the ComponentIdentifier.
 */
public class ComponentIdentifier extends QTObject implements QuickTimeLib {

    private static Object linkage;

    private ComponentDescription cacheInfo;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(ComponentIdentifier.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    /**
     * Determine the next Component as specified. Will return null if it cannot find a
     * component that matches the specifications.
     * QuickTime::FindNextComponent
     * @param desc describes the search parameters.
     * @return a component identifier or zero if none found.
     * @return {@link quicktime.QTException}
     */
    public static final ComponentIdentifier find(ComponentDescription paramComponentDescription) throws QTException {
        return find(null, paramComponentDescription);
    }

    /**
     * Determine the next Component as specified. Will return null if it cannot find a
     * component that matches the specifications.
     * QuickTime::FindNextComponent
     * @param comp a previously found component
     * @param desc describes the search parameters.
     * @return a component identifier or zero if none found.
     * @return {@link quicktime.QTException}
     */
    public static final ComponentIdentifier find(ComponentIdentifier paramComponentIdentifier, ComponentDescription paramComponentDescription) throws QTException {
        int i = 0;
        if (paramComponentIdentifier == null)
            i = FindNextComponent(0, paramComponentDescription.getBytes());
        else {
            i = FindNextComponent(QTObject.ID(paramComponentIdentifier), paramComponentDescription.getBytes());
        }
        return i == 0 ? null : new ComponentIdentifier(i);
    }

    private ComponentIdentifier(int paramInt) throws QTException {
        this(paramInt, null);
    }

    protected ComponentIdentifier(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Returns all of the registration information for a component.
     * QuickTime::GetComponentInfo
     * @return information about the specified component.
     * @return {@link quicktime.std.StdQTException}
     */
    public final ComponentDescription getInfo() throws StdQTException {
        if (this.cacheInfo == null) {
            this.cacheInfo = new ComponentDescription();
            int i = NewEmptyHandle();
            int j = NewEmptyHandle();
            StdQTException.checkError(GetComponentInfo(_ID(), this.cacheInfo.getBytes(), i, j, 0));
            if (GetHandleSize(i) > 0) {
                this.cacheInfo.name = getPStringFrom(i);
            }
            DisposeHandle(i);
            if (GetHandleSize(j) > 0) {
                this.cacheInfo.info = getPStringFrom(j);
            }
            DisposeHandle(j);
        }
        return this.cacheInfo;
    }

    private static String getPStringFrom(int paramInt) {
        int i = getByteFromHandle(paramInt, 0);
        int j = 1;
        if (i > 0) {
            byte[] arrayOfByte = new byte[i];
            for (int k = 1; k <= arrayOfByte.length; k++) arrayOfByte[(k - 1)] = getByteFromHandle(paramInt, k);
            return new String(arrayOfByte);
        }
        return null;
    }

    /**
     * Prints the Component Info.
     */
    public String toString() {
        String str = super.toString();
        try {
            str = str + "[type=" + QTUtils.fromOSType(EndianOrder.flipNativeToBigEndian32(getInfo().getType())) + ",subType=" + QTUtils.fromOSType(EndianOrder.flipNativeToBigEndian32(getInfo().getSubType())) + "]";
        } catch (QTException localQTException) {
        }
        return str;
    }

    private static int FindNextComponent(int paramInt, byte[] paramArrayOfByte) {
        return ComponentManager.FindNextComponent(paramInt, paramArrayOfByte);
    }

    private static short GetComponentInfo(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4) {
        return ComponentManager.GetComponentInfo(paramInt1, paramArrayOfByte, paramInt2, paramInt3, paramInt4);
    }

    private static int GetHandleSize(int paramInt) {
        return MemoryManager.GetHandleSize(paramInt);
    }

    private static int NewHandle(int paramInt) {
        return MemoryManager.NewHandle(paramInt);
    }

    private static int NewEmptyHandle() {
        return MemoryManager.NewEmptyHandle();
    }

    private static void DisposeHandle(int paramInt) {
        MemoryManager.DisposeHandle(paramInt);
    }

    private static byte getByteFromHandle(int paramInt1, int paramInt2) {
        return MemoryManager.getByteFromHandle(paramInt1, paramInt2);
    }
}

