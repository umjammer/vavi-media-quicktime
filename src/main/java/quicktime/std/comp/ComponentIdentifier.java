package quicktime.std.comp;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ComponentManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.EndianOrder;
import quicktime.util.QTUtils;


/**
 * Representation of the ComponentIdentifier.
 */
public class ComponentIdentifier extends QTObject implements QuickTimeLib {

    private ComponentDescription cacheInfo;

    /**
     * Determine the next Component as specified. Will return null if it cannot find a
     * component that matches the specifications.
     * @see "QuickTime::FindNextComponent"
     * @param desc describes the search parameters.
     * @return a component identifier or zero if none found.
     * @throws quicktime.QTException
     */
    public static ComponentIdentifier find(ComponentDescription desc) throws QTException {
        return find(null, desc);
    }

    /**
     * Determine the next Component as specified. Will return null if it cannot find a
     * component that matches the specifications.
     * @see "QuickTime::FindNextComponent"
     * @param comp a previously found component
     * @param desc describes the search parameters.
     * @return a component identifier or zero if none found.
     * @throws quicktime.QTException
     */
    public static ComponentIdentifier find(ComponentIdentifier comp, ComponentDescription desc) throws QTException {
        int i = 0;
        if (comp == null)
            i = FindNextComponent(0, desc.getBytes());
        else {
            i = FindNextComponent(QTObject.ID(comp), desc.getBytes());
        }
        return i == 0 ? null : new ComponentIdentifier(i);
    }

    private ComponentIdentifier(int nativeRep) throws QTException {
        this(nativeRep, null);
    }

    protected ComponentIdentifier(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Returns all of the registration information for a component.
     * @see "QuickTime::GetComponentInfo"
     * @return information about the specified component.
     * @throws quicktime.std.StdQTException
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

    private static String getPStringFrom(int h) {
        int i = getByteFromHandle(h, 0);
        int j = 1;
        if (i > 0) {
            byte[] arrayOfByte = new byte[i];
            for (int k = 1; k <= arrayOfByte.length; k++) arrayOfByte[(k - 1)] = getByteFromHandle(h, k);
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
            str = str +
                    "[type=" +
                    QTUtils.fromOSType(EndianOrder.flipNativeToBigEndian32(getInfo().getType())) +
                    ",subType=" +
                    QTUtils.fromOSType(EndianOrder.flipNativeToBigEndian32(getInfo().getSubType())) +
                    "]";
        } catch (QTException _) {
        }
        return str;
    }

    private static int FindNextComponent(int previous, byte[] desc) {
        return ComponentManager.FindNextComponent(previous, desc);
    }

    private static short GetComponentInfo(int component, byte[] desc, int nameHandle, int infoHandle, int iconHandle) {
        return ComponentManager.GetComponentInfo(component, desc, nameHandle, infoHandle, iconHandle);
    }

    private static int GetHandleSize(int h) {
        return MemoryManager.GetHandleSize(h);
    }

    private static int NewHandle(int size) {
        return MemoryManager.NewHandle(size);
    }

    private static int NewEmptyHandle() {
        return MemoryManager.NewEmptyHandle();
    }

    private static void DisposeHandle(int h) {
        MemoryManager.DisposeHandle(h);
    }

    private static byte getByteFromHandle(int h, int offset) {
        return MemoryManager.getByteFromHandle(h, offset);
    }
}
