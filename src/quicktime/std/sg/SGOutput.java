
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

    public SGOutput(SequenceGrabber paramSequenceGrabber, DataRef paramDataRef, int paramInt) throws StdQTException {
        super(allocate(paramSequenceGrabber, QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt),
              paramSequenceGrabber);
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

    private static int allocate(SequenceGrabber paramSequenceGrabber,
                                int paramInt1,
                                int paramInt2,
                                int paramInt3) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = SGNewOutput(QTObject.ID(paramSequenceGrabber), paramInt1, paramInt2, paramInt3, arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    private void setOwner(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        if (paramSequenceGrabber == null)
            throw new StdQTException(-50);
        this.owner = paramSequenceGrabber;
    }

    public void disposeQTObject() throws QTException {
        if (!this.owner.isValid())
            return;
        int i = SGDisposeOutput(QTObject.ID(this.owner), _ID());
        StdQTException.checkError(i);
        super.disposeQTObject();
    }

    public void setMaximumOffset(long paramLong) throws StdQTException {
        byte[] arrayOfByte = new byte[8];
        setLongInArray(arrayOfByte, 0, paramLong);
        int i = SGSetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), arrayOfByte);
        StdQTException.checkError(i);
    }

    public long getMaximumOffset() throws StdQTException {
        byte[] arrayOfByte = new byte[8];

        int i = SGGetOutputMaximumOffset(QTObject.ID(this.owner), _ID(), arrayOfByte);
        StdQTException.checkError(i);

        return getLongFromArray(arrayOfByte, 0);
    }

    public void setOutputFlags(int paramInt) throws StdQTException {
        int i = SGSetOutputFlags(QTObject.ID(this.owner), _ID(), paramInt);
        StdQTException.checkError(i);
    }

    public void setChannel(SGChannel paramSGChannel) throws StdQTException {
        int i = SGSetChannelOutput(QTObject.ID(this.owner), QTObject.ID(paramSGChannel), _ID());
        StdQTException.checkError(i);
    }

    public long getDataStorageSpaceRemaining() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        int i = SGGetDataOutputStorageSpaceRemaining(QTObject.ID(this.owner), _ID(), arrayOfInt);
        StdQTException.checkError(i);
        return QTUtils.UInt2Long(arrayOfInt[0]);
    }

    public SGOutput getNextOutput() throws StdQTException {
        int[] arrayOfInt = {
            0
        };

        int i = SGGetOutputNextOutput(QTObject.ID(this.owner), _ID(), arrayOfInt);
        StdQTException.checkError(i);
        return new SGOutput(arrayOfInt[0], this.owner);
    }

    public void setNextOutput(SGOutput paramSGOutput) throws StdQTException {
        int i = SGSetOutputNextOutput(QTObject.ID(this.owner), _ID(), QTObject.ID(paramSGOutput));
        StdQTException.checkError(i);
    }

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
