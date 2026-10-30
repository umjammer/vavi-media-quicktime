package quicktime.std.clocks;

import quicktime.QTException;
import quicktime.QTNullPointerException;
import quicktime.QTObject;
import quicktime.internal.toolbox.ClockManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;

/**
 * Provides users with a defined operation at some time record of a clock's time base.
 *  Its methods offer access to a quicktime structure and its fields.
 *  Refer to QTCallBack in QuickTime
 *
 *  When an application schedules a QTCallBack with the callMeWhen method it can cancel
 *  and reschedule the callback as required. However, after it no longer requires the
 *  services of the QTCallBack it MUST call the cancelAndCleanup method to both cancel any
 *  impending callbacks and to dispose of the native structures that are constructed
 *  to call back from QuickTime into Java.
 *
 *  There is a very important issue that must be understood by Application developers when
 *  using these or any of the call back capabilities of QuickTime in a Java environment.
 *  QuickTime native calls are dispatched through a single gateway/lock and run to their completion.
 *
 *  A callback from QuickTime into Java is invoked through the Java application calling a native
 *  QuickTime function - in the case of the QTCallBack call back these callbacks are invoked through
 *  a direct or indirect call of the QuickTime MoviesTask function.
 *
 *  When the MoviesTask function invokes a callback and calls into Java this occurs in the same thread
 *  of execution. Any other native QuickTime calls originating from another thread are queued until the
 *  MoviesTask call is completed (including any callbacks that do not spawn their own threads). There exists
 *  a potential thread deadlock issue that can arise with the use of callbacks due to this underlying
 *
 * <I>modus operandi</I>
 * that an applications developer may cause and must deal with. This is not a bug
 *  but is a feature of the current implementation.
 *
 *  For Example:
 *  Thread 1 calls MoviesTask (Movie.task or Movie.taskAll) which invokes a callback on say a QTDrawer object which has a synchronized redraw
 *  call. The callback's action is to redraw this QTDrawer object (which will make a native call to QuickTime to
 *  draw).
 *
 *  In Thread 2 awt issues a paint call which goes through to the QTDrawer's synchronized redraw call. This redraw
 *  call goes through to a native QuickTime call which halts waiting for the currently executing MoviesTask call to
 *  complete in Thread 1.
 *
 *  However we have a deadlock - Thread 1 is unable to complete and halts at the QTDrawer's redraw call because Thread 2
 *  already has the synchronization lock for the QTDrawer object. However, thread 2 is unable to continue because
 *  Thread 1 has the native QuickTime lock.
 *
 *  One alternative is to have the callback that redraws the QTDrawer spawn another thread and redraw the QTDrawer
 *  in this spawn thread. Once the thread is spawned the callback can then complete and in the above scenario
 *  Thread 1 can finish the MoviesTask and Thread 2 would then be able to continue and the QTDrawer would redraw itself.
 *
 *  Many of the Callback methods in QuickTime are required to execute in place, in that QuickTime requires
 *  a result code in order to proceed. These callbacks should provide meaningful feedback when their execute
 *  method returns. The Subclasses of the QTCallBack however can execute asynchronously; QuickTime does not require
 *  a result code in order to proceed. This is also true of any of the execute methods with no return value.
 */
public abstract class QTCallBack extends QTObject implements QuickTimeLib {

    static final int callBackAtTime = 1;
    static final int callBackAtRate = 2;
    static final int callBackAtTimeJump = 3;
    static final int callBackAtExtremes = 4;

    static final int callBackAtInterrupt = 32768;
    static final int callBackAtDeferredTask = 16384;

    private final TimeBase tb;

    QTCallBack(TimeBase tb, int flags) throws QTException {
        super(allocate(tb, flags));
        this.tb = tb;
    }

    private static int allocate(TimeBase tb, int flags) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = NewCallBack(QTObject.ID(tb), (short) (flags | 0x8000));
        }
        if (i == 0)
            throw new StdQTException("QTCallback.<init>");
        return i;
    }

    public CBRunner upp = null;

    /**
     * This variable is set by the dispatch mechanism when the callback is invoked by QuickTime.
     *  In the execute method this variable can be queried to find out the time when the callback
     *  first appeared in Java.
     */
    public int timeWhenCalledMsecs;

    /**
     * This variable is set by the dispatch mechanism when the callback is invoked by QuickTime.
     *  In the execute method this variable can be queried to find out the rate of the call back's TimeBase
     *  when the call back was invoked.
     */
    public float rateWhenCalled;

    final void callMeWhen(int param1, int param2, int param3) throws StdQTException {
        if (this.upp == null) {
            this.upp = new CBRunner(this);
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = CallMeWhen(_ID(), this.upp.getClosure(), 0, param1, param2, param3);
        }
        StdQTException.checkError(i);
    }

    /**
     * Cancel a callback event before it executes.
     * @see " QuickTime::CancelCallBack()"
     */
    public final void cancel() {
        synchronized (QTNative.globalsLock) {
            CancelCallBack(_ID());
        }
    }

    /**
     * Cancel a callback event before it executes. This call also
     *  cleans up the method closure object that is used to dispatch
     *  the callback into Java. If a callback has ever been scheduled
     *  it is the applications responsibility to ensure that the cleanup
     *  method is called to allow the call back to be garbage collected.
     *  The call back can be rescheduled after this call (provided of course you also
     *  cleanup again!)
     * @see " QuickTime::CancelCallBack()"
     */
    public final void cancelAndCleanup() {
        try {
            synchronized (QTNative.globalsLock) {
                CancelCallBack(_ID());
            }
        } catch (QTNullPointerException e) {
        }
        if (this.upp != null) {
            this.upp.cleanup();
            this.upp = null;
        }
    }

    /**
     * Retrieve the time base of a callback event.
     * @see " QuickTime::GetCallBackTimeBase()"
     *  @return the time base of the callback.
     */
    public final TimeBase getTimeBase() {
        return TimeBase.fromQTCallBack(this);
    }

    /**
     * Retrieve a callback's event type.
     * @see " QuickTime::GetCallBackType()"
     *  @return the callback's type.
     */
    public final int getType() {
        return GetCallBackType(_ID());
    }

    /**
     * A String representation of the class.
     */
    public String toString() {
        return getClass().getName() + "[timeWhenCalled=" + this.timeWhenCalledMsecs + ",rateWhenCalled=" + this.rateWhenCalled + "]";
    }

    /**
     * Registers the callback. It will use the state of the subclass to set the parameters
     *  that control the callbacks activation. As a method on QTCallBack it does nothing.
     * @see "QuickTime::CallMeWhen"
     *  param cb CallBack interface who's execute method will be called when the callback becomes active.
     */
    public abstract void callMeWhen() throws StdQTException;

    /**
     * This method is called when the callback is executed. Subclasses define
     *  this method.
     */
    public abstract void execute();

    private static short CallMeWhen(int cb, int proc, int refCon, int param1, int param2, int param3) {
        return ClockManager.CallMeWhen(cb, proc, refCon, param1, param2, param3);
    }

    private static short GetCallBackType(int cb) {
        return ClockManager.GetCallBackType(cb);
    }

    private static void CancelCallBack(int cb) {
        ClockManager.CancelCallBack(cb);
    }

    private static int NewCallBack(int tb, short type) {
        return ClockManager.NewCallBack(tb, type);
    }
}
