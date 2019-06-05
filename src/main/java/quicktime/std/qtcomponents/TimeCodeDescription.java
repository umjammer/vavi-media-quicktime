
package quicktime.std.qtcomponents;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.std.movies.media.SampleDescription;
import quicktime.util.EndianDescriptor;
import quicktime.util.EndianFlipSpec;
import quicktime.util.QTUtils;


public final class TimeCodeDescription extends SampleDescription implements PrimitivesLib {
    private static Object linkage;

    private static EndianDescriptor ed;

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

    public static EndianDescriptor getEndianDescriptor() {
        if (ed == null)
            ed = makeED();
        return ed;
    }

    public TimeCodeDescription() throws QTException {
        super(kNativeSize, true, 1953325924);
    }

    private TimeCodeDescription(int paramInt) {
        super(paramInt, null, false);
    }

    public Object clone() {
        return new TimeCodeDescription(makeAndCopyHandle());
    }

    public TimeCodeDef getTimeCodeDef() {
        TimeCodeDef localTimeCodeDef = new TimeCodeDef(lockAndDeref(20));
        if (!isLocked())
            unlock();
        return localTimeCodeDef;
    }

    public void setTimeCodeDef(TimeCodeDef paramTimeCodeDef) {
        copyArrayToPointer(paramTimeCodeDef.getBytes(), 0, lockAndDeref(20), 0, 14);
        if (!isLocked())
            unlock();
    }

    public String toString() {
        return getClass().getName() + "[format=" + QTUtils.fromOSType(getDataFormat()) + getTimeCodeDef() + "]";
    }

    private static native void copyArrayToPointer(byte[] paramArrayOfByte,
                                                  int paramInt1,
                                                  int paramInt2,
                                                  int paramInt3,
                                                  int paramInt4);
}
