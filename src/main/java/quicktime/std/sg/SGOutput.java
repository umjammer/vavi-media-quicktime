package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
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

    private static Object linkage;

    private SequenceGrabber owner;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SGOutput.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * The SGNewOutput function creates a new sequence grabber output. You specify
     * the outputÕs destination container using a data reference. Once you have
     * created the sequence grabber output, you can use the SGSetChannelOutput
     * function to assign the output to a sequence grabber channel.
     *
     * QuickTime::SGNewOutput
     * @param owner your connection to the sequence grabber.
     * @param dataRef A handle to the destination container.
     * @param whereFlags that control the record operation. You must set either
     *  seqGrabToDisk or seqGrabToMemory to 1. Set unused flags to 0.
     */
    public SGOutput(SequenceGrabber paramSequenceGrabber, DataRef paramDataRef, int paramInt) throws StdQTException {
        super(allocate(paramSequenceGrabber, QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt), paramSequenceGrabber);
        setOwner(paramSequenceGrabber);
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
        int[] arrayOfInt = { 0 };
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
     * QuickTime::SGDisposeOutput
     */
    public void disposeQTObject() throws QTException {
        if (!this.owner.isValid())
            return;
        int i = SGDisposeOutput(QTObject.ID(this.owner), _ID());
        StdQTException.checkError(i);
        super.disposeQTObject();
    }

    /**
     * QuickTime::SGSetOutputMaximumOffset
     * @param maxOffset the maximum offset for data written to this output.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMaximumOffset(long paramLong) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        setLongInArray(arrayOfByte, 0, paramLong);
        int i = SGSetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), arrayOfByte);
        StdQTException.checkError(i);
    }

    /**
     * QuickTime::SGGetOutputMaximumOffset
     */
    public long getMaximumOffset() throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        int i = SGGetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), arrayOfByte);
        StdQTException.checkError(i);
        return getLongFromArray(arrayOfByte, 0);
    }

    /**
     * QuickTime::SGSetOutputFlags
     * @param whereFlags determines where the output is sent, eg seqGrabToDisk
     * @param {@link quicktime.std.StdQTException}
     */
    public void setOutputFlags(int paramInt) throws StdQTException {
        int i = SGSetOutputFlags(QTObject.ID(this.owner), _ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * QuickTime::SGSetChannelOutput
     * @param c the channel you want to assign the output to
     * @param {@link quicktime.std.StdQTException}
     */
    public void setChannel(SGChannel paramSGChannel) throws StdQTException {
        int i = SGSetChannelOutput(QTObject.ID(this.owner), QTObject.ID(paramSGChannel), _ID());
        StdQTException.checkError(i);
    }

    /**
     * QuickTime::SGGetDataOutputStorageSpaceRemaining
     */
    public long getDataStorageSpaceRemaining() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = SGGetDataOutputStorageSpaceRemaining(QTObject.ID(this.owner), _ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.UInt2Long(arrayOfInt[0]);
    }

    /**
     * QuickTime::SGGetOutputNextOutput(
     */
    public SGOutput getNextOutput() throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = SGGetOutputNextOutput(QTObject.ID(this.owner), _ID(), arrayOfInt);
        StdQTException.checkError(i);
        return new SGOutput(arrayOfInt[0], this.owner);
    }

    /**
     * QuickTime::SGSetOutputNextOutput(
     * @param nextOutput specifies the next output to be used after the output represented by this object.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setNextOutput(SGOutput paramSGOutput) throws StdQTException {
        int i = SGSetOutputNextOutput(QTObject.ID(this.owner), _ID(), QTObject.ID(paramSGOutput));
        StdQTException.checkError(i);
    }

    /**
     * QuickTime::SGGetOutputDataReference(
     */
    public DataRef getDataReference() throws StdQTException {
        return DataRef.fromSGOutput(this.owner, this);
    }

    private static native int SGNewOutput(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt);

    private static native int SGDisposeOutput(int paramInt1, int paramInt2);

    private static native int SGSetOutputMaximumOffset(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SGGetOutputMaximumOffset(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int SGSetOutputFlags(int paramInt1, int paramInt2, int paramInt3);

    private static native int SGSetChannelOutput(int paramInt1, int paramInt2, int paramInt3);

    private static native int SGGetDataOutputStorageSpaceRemaining(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SGGetOutputNextOutput(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native int SGSetOutputNextOutput(int paramInt1, int paramInt2, int paramInt3);

    private static native void setLongInArray(byte[] paramArrayOfByte, int paramInt, long paramLong);

    private static native long getLongFromArray(byte[] paramArrayOfByte, int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGOutput.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
