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

    SGOutput(SequenceGrabber paramSequenceGrabber, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        super(allocate(paramSequenceGrabber, paramInt1, paramInt2, paramInt3), paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
    }

    SGOutput(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
    }

    private static int allocate(SequenceGrabber paramSequenceGrabber, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int[] arrayOfInt = {0};
        int i = SGNewOutput(QTObject.ID(paramSequenceGrabber), paramInt1, paramInt2, paramInt3, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private void setOwner(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        if (paramSequenceGrabber == null)
            throw new StdQTException(-50);
        this.owner = paramSequenceGrabber;
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
        byte[] arrayOfByte = new byte[8];
        int i = SGGetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), arrayOfByte);
        StdQTException.checkError(i);
        return getLongFromArray(arrayOfByte, 0);
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
        int[] arrayOfInt = {0};
        int i = SGGetDataOutputStorageSpaceRemaining(QTObject.ID(this.owner), _ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.UInt2Long(arrayOfInt[0]);
    }

    /**
     * @see "QuickTime::SGGetOutputNextOutput("
     */
    public SGOutput getNextOutput() throws StdQTException {
        int[] arrayOfInt = {0};
        int i = SGGetOutputNextOutput(QTObject.ID(this.owner), _ID(), arrayOfInt);
        StdQTException.checkError(i);
        return new SGOutput(arrayOfInt[0], this.owner);
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

    private static int SGNewOutput(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGNewOutput(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static int SGDisposeOutput(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGDisposeOutput(paramInt1, paramInt2);
    }

    private static int SGSetOutputMaximumOffset(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetOutputMaximumOffset(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SGGetOutputMaximumOffset(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGGetOutputMaximumOffset(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int SGSetOutputFlags(int paramInt1, int paramInt2, int paramInt3) {
        return SequenceGrabberManager.SGSetOutputFlags(paramInt1, paramInt2, paramInt3);
    }

    private static int SGSetChannelOutput(int paramInt1, int paramInt2, int paramInt3) {
        return SequenceGrabberManager.SGSetChannelOutput(paramInt1, paramInt2, paramInt3);
    }

    private static int SGGetDataOutputStorageSpaceRemaining(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetDataOutputStorageSpaceRemaining(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SGGetOutputNextOutput(int paramInt1, int paramInt2, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetOutputNextOutput(paramInt1, paramInt2, paramArrayOfInt);
    }

    private static int SGSetOutputNextOutput(int paramInt1, int paramInt2, int paramInt3) {
        return SequenceGrabberManager.SGSetOutputNextOutput(paramInt1, paramInt2, paramInt3);
    }

    private static void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong) {
        MemoryManager.setLongInArray(paramArrayOfByte, paramInt, paramLong);
    }

    private static long getLongFromArray(byte[] paramArrayOfByte, int paramInt) {
        return MemoryManager.getLongFromArray(paramArrayOfByte, paramInt);
    }
}
