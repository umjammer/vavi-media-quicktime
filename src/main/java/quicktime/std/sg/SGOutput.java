package quicktime.std.sg;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.DataRef;
import quicktime.util.QTUtils;


/**
 * Assists in capturing to Multiple files.
 * The SGOutput implements methods for a capturing to multiple files.
 * Introduced in QuickTime 3.0, capturing to multople files can improve the
 * Performance and flexibility of cpatures and enable larger total captures.
 * refer to SGOutput in QuickTime Components
 */
public final class SGOutput extends QTObject implements QuickTimeLib, PrimitivesLib {

    private SequenceGrabber owner;

    /**
     * The SGNewOutput function creates a new sequence grabber output. You specify
     * the outputÕs destination container using a data reference. Once you have
     * created the sequence grabber output, you can use the SGSetChannelOutput
     * function to assign the output to a sequence grabber channel.
     *
     * @param owner      your connection to the sequence grabber.
     * @param dataRef    A handle to the destination container.
     * @param whereFlags that control the record operation. You must set either
     *                   seqGrabToDisk or seqGrabToMemory to 1. Set unused flags to 0.
     * @see "QuickTime::SGNewOutput"
     */
    public SGOutput(SequenceGrabber owner, DataRef dataRef, int whereFlags) throws StdQTException {
        super(allocate(owner, QTObject.ID(dataRef), dataRef.getType(), whereFlags), owner);
        setOwner(owner);
    }

    SGOutput(SequenceGrabber owner, int dataRef, int dataRefType, int flags) throws StdQTException {
        super(allocate(owner, dataRef, dataRefType, flags), owner);
        setOwner(owner);
    }

    SGOutput(int nativeRep, SequenceGrabber owner) throws StdQTException {
        super(nativeRep, owner);
        setOwner(owner);
    }

    private static int allocate(SequenceGrabber sg, int dataRef, int dataRefType, int flags) throws StdQTException {
        int[] output = {0};
        int i = SGNewOutput(QTObject.ID(sg), dataRef, dataRefType, flags, output);
        StdQTException.checkError(i);
        return output[0];
    }

    private void setOwner(SequenceGrabber owner) throws StdQTException {
        if (owner == null)
            throw new StdQTException(-50);
        this.owner = owner;
    }

    /**
     * Disposes of an output associated with a sequence grabber.
     *
     * @see "QuickTime::SGDisposeOutput"
     */
    @Override
    public void disposeQTObject() throws QTException {
        if (!this.owner.isValid())
            return;
        int i = SGDisposeOutput(QTObject.ID(this.owner), _ID());
        StdQTException.checkError(i);
        super.disposeQTObject();
    }

    /**
     * @param maxOffset the maximum offset for data written to this output.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetOutputMaximumOffset"
     */
    public void setMaximumOffset(long maxOffset) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        setLongInArray(arrayOfByte, 0, maxOffset);
        int i = SGSetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), arrayOfByte);
        StdQTException.checkError(i);
    }

    /**
     * @see "QuickTime::SGGetOutputMaximumOffset"
     */
    public long getMaximumOffset() throws StdQTException {
        byte[] maxOffset = new byte[8];
        int i = SGGetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), maxOffset);
        StdQTException.checkError(i);
        return getLongFromArray(maxOffset, 0);
    }

    /**
     * @param whereFlags determines where the output is sent, eg seqGrabToDisk
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetOutputFlags"
     */
    public void setOutputFlags(int whereFlags) throws StdQTException {
        int i = SGSetOutputFlags(QTObject.ID(this.owner), _ID(), whereFlags);
        StdQTException.checkError(i);
    }

    /**
     * @param c the channel you want to assign the output to
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetChannelOutput"
     */
    public void setChannel(SGChannel c) throws StdQTException {
        int i = SGSetChannelOutput(QTObject.ID(this.owner), QTObject.ID(c), _ID());
        StdQTException.checkError(i);
    }

    /**
     * @see "QuickTime::SGGetDataOutputStorageSpaceRemaining"
     */
    public long getDataStorageSpaceRemaining() throws StdQTException {
        int[] space = {0};
        int i = SGGetDataOutputStorageSpaceRemaining(QTObject.ID(this.owner), _ID(), space);
        StdQTException.checkError(i);
        return QTUtils.UInt2Long(space[0]);
    }

    /**
     * @see "QuickTime::SGGetOutputNextOutput("
     */
    public SGOutput getNextOutput() throws StdQTException {
        int[] next = {0};
        int i = SGGetOutputNextOutput(QTObject.ID(this.owner), _ID(), next);
        StdQTException.checkError(i);
        return new SGOutput(next[0], this.owner);
    }

    /**
     * @param nextOutput specifies the next output to be used after the output represented by this object.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetOutputNextOutput("
     */
    public void setNextOutput(SGOutput nextOutput) throws StdQTException {
        int i = SGSetOutputNextOutput(QTObject.ID(this.owner), _ID(), QTObject.ID(nextOutput));
        StdQTException.checkError(i);
    }

    /**
     * @see "QuickTime::SGGetOutputDataReference("
     */
    public DataRef getDataReference() throws StdQTException {
        return DataRef.fromSGOutput(this.owner, this);
    }

    private static int SGNewOutput(int sg, int dataRef, int dataRefType, int flags, int[] output) {
        return SequenceGrabberManager.SGNewOutput(sg, dataRef, dataRefType, flags, output);
    }

    private static int SGDisposeOutput(int sg, int output) {
        return SequenceGrabberManager.SGDisposeOutput(sg, output);
    }

    private static int SGSetOutputMaximumOffset(int sg, int output, byte[] maxOffset) {
        return SequenceGrabberManager.SGSetOutputMaximumOffset(sg, output, maxOffset);
    }

    private static int SGGetOutputMaximumOffset(int sg, int output, byte[] maxOffset) {
        return SequenceGrabberManager.SGGetOutputMaximumOffset(sg, output, maxOffset);
    }

    private static int SGSetOutputFlags(int sg, int output, int flags) {
        return SequenceGrabberManager.SGSetOutputFlags(sg, output, flags);
    }

    private static int SGSetChannelOutput(int sg, int ref, int output) {
        return SequenceGrabberManager.SGSetChannelOutput(sg, ref, output);
    }

    private static int SGGetDataOutputStorageSpaceRemaining(int sg, int output, int[] space) {
        return SequenceGrabberManager.SGGetDataOutputStorageSpaceRemaining(sg, output, space);
    }

    private static int SGGetOutputNextOutput(int sg, int output, int[] next) {
        return SequenceGrabberManager.SGGetOutputNextOutput(sg, output, next);
    }

    private static int SGSetOutputNextOutput(int sg, int output, int next) {
        return SequenceGrabberManager.SGSetOutputNextOutput(sg, output, next);
    }

    private static void setLongInArray(byte[] array, int offset, long value) {
        MemoryManager.setLongInArray(array, offset, value);
    }

    private static long getLongFromArray(byte[] array, int offset) {
        return MemoryManager.getLongFromArray(array, offset);
    }
}
