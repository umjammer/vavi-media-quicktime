package quicktime.std.qtcomponents;

import quicktime.QTException;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;


/**
 * This class maps the TimeCodeDescription record of the MovieToolbox.
 * TimeCodeDescription are used when adding and getting media samples to/from TimeCode tracks.
 */
public final class TimeCodeDescription extends SampleDescription implements PrimitivesLib {

    private static EndianDescriptor ed;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 38;

    protected static EndianDescriptor makeED() {
        EndianDescriptor ed = SampleDescription.makeED();
        EndianFlipSpec spec = new EndianFlipSpec(16, 4, 1);
        ed.addFlipSpec(spec);
        EndianFlipSpec endianFlipSpec2 = new EndianFlipSpec(20, 4, 3);
        ed.addFlipSpec(spec);
        return ed;
    }

    /**
     * Returns the EndianDescriptor for this class.
     *
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

    private TimeCodeDescription(int nativeRep) {
        super(nativeRep, null, false);
    }

    /**
     * Implementation of Cloneable Interface.
     */
    @Override
    public Object clone() {
        return new TimeCodeDescription(makeAndCopyHandle());
    }

    /**
     * Returns the current time code definition
     *
     * @return time code definition
     */
    public TimeCodeDef getTimeCodeDef() {
        TimeCodeDef timeCodeDef = new TimeCodeDef(lockAndDeref(20));
        if (!isLocked())
            unlock();
        return timeCodeDef;
    }

    /**
     * Sets the values from the incoming TimeCode definition into the existing description.
     *
     * @param tcd the incoming definitions.
     */
    public void setTimeCodeDef(TimeCodeDef tcd) {
        copyArrayToPointer(tcd.getBytes(), 0, lockAndDeref(20), 0, 14);
        if (!isLocked())
            unlock();
    }

    /**
     * String representation of class.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[format=" + QTUtils.fromOSType(getDataFormat()) + getTimeCodeDef() + "]";
    }

    private static void copyArrayToPointer(byte[] destArray, int arrayOffset, int ptr, int length, int size) {
        MemoryManager.copyArrayToPointer(destArray, arrayOffset, ptr, length, size);
    }
}
