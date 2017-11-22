
package quicktime.std.clocks;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;


public abstract class QTCallBack extends QTObject implements QuickTimeLib {
    private static Object linkage;

    static final int callBackAtTime = 1;

    static final int callBackAtRate = 2;

    static final int callBackAtTimeJump = 3;

    static final int callBackAtExtremes = 4;

    static final int callBackAtInterrupt = 32768;

    static final int callBackAtDeferredTask = 16384;

    private TimeBase tb;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(QTCallBack.class);
                    }
                });
            }
        }.establish();
    }

    QTCallBack(TimeBase paramTimeBase, int paramInt) throws QTException {
        super(allocate(paramTimeBase, paramInt));
        this.tb = paramTimeBase;
    }

    private static int allocate(TimeBase paramTimeBase, int paramInt) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = NewCallBack(QTObject.ID(paramTimeBase), (short) (paramInt | 0x8000));
        }
        if (i == 0)
            throw new StdQTException("QTCallback.<init>");
        return i;
    }

    public CBRunner upp = null;

    public int timeWhenCalledMsecs;

    public float rateWhenCalled;

    final void callMeWhen(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        if (this.upp == null) {
            this.upp = new CBRunner(this);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = CallMeWhen(_ID(), this.upp.getClosure(), 0, paramInt1, paramInt2, paramInt3);
        }
        StdQTException.checkError(i);
    }

    public final void cancel() {
        synchronized (QTNative.globalsLock) {
            CancelCallBack(_ID());
        }
    }

    public final void cancelAndCleanup() {
        try {
            synchronized (QTNative.globalsLock) {
                CancelCallBack(_ID());
            }
        } catch (QTNullPointerException localQTNullPointerException) {
        }
        if (this.upp != null) {
            this.upp.cleanup();
            this.upp = null;
        }
    }

    public final TimeBase getTimeBase() {
        return TimeBase.fromQTCallBack(this);
    }

    public final int getType() {
        return GetCallBackType(_ID());
    }

    public String toString() {
        return getClass().getName() + "[timeWhenCalled=" + this.timeWhenCalledMsecs + ",rateWhenCalled=" + this.rateWhenCalled
               + "]";
    }

    public abstract void callMeWhen() throws StdQTException;

    public abstract void execute();

    private static native short CallMeWhen(int paramInt1,
                                           int paramInt2,
                                           int paramInt3,
                                           int paramInt4,
                                           int paramInt5,
                                           int paramInt6);

    private static native short GetCallBackType(int paramInt);

    private static native void CancelCallBack(int paramInt);

    private static native int NewCallBack(int paramInt, short paramShort);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * clocks/QTCallBack.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
