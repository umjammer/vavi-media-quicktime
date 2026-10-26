package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;

/**
 * This class maps the TimeCodeDescription record of the MovieToolbox.
 * TimeCodeDescription are used when adding and getting media samples to/from TimeCode tracks.
 */
public final class TimeCodeDescription extends SampleDescription implements PrimitivesLib {

    private static Object linkage;

    private static EndianDescriptor ed;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 38;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(TimeCodeDescription.class);
                    }
                });
            }
        }.establish();
    }

    protected static EndianDescriptor makeED() {
        EndianDescriptor localEndianDescriptor = SampleDescription.makeED();
        EndianFlipSpec localEndianFlipSpec1 = new EndianFlipSpec(16, 4, 1);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        EndianFlipSpec localEndianFlipSpec2 = new EndianFlipSpec(20, 4, 3);
        localEndianDescriptor.addFlipSpec(localEndianFlipSpec1);
        return localEndianDescriptor;
    }

    /**
     * Returns the EndianDescriptor for this class.
     * @return an EndianDescriptor
     */
    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    /**
     * This constructor makes an empty TimeCodeDescription object.
     */
    public TimeCodeDescription() throws QTException {
        super(kNativeSize, true, 1953325924);
    }

    private TimeCodeDescription(int paramInt) {
        super(paramInt, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    public Object clone() {
        return new TimeCodeDescription(makeAndCopyHandle());
    }

    /**
     * Returns the current time code definition
     * @return time code definition
     */
    public TimeCodeDef getTimeCodeDef() {
        TimeCodeDef localTimeCodeDef = new TimeCodeDef(lockAndDeref(20));
        if (!isLocked())
            unlock();
        return localTimeCodeDef;
    }

    /**
     * Sets the values from the incoming TimeCode definition into the existing description.
     * @param tcd the incoming definitions.
     */
    public void setTimeCodeDef(TimeCodeDef paramTimeCodeDef) {
        copyArrayToPointer(paramTimeCodeDef.getBytes(), 0, lockAndDeref(20), 0, 14);
        if (!isLocked())
            unlock();
    }

    /**
     * String representation of class.
     * @return a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[format=" + QTUtils.fromOSType(getDataFormat()) + getTimeCodeDef() + "]";
    }

    private static void copyArrayToPointer(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MemoryManager.copyArrayToPointer(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
    }
}
