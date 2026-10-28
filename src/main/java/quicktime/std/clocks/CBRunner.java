
package quicktime.std.clocks;

import quicktime.QTSession;
import quicktime.internal.toolbox.ClockManager;


final class CBRunner implements Runnable, quicktime.jdirect.QuickTimeLib {

    private static int activeCallbacks = 0;

    private static Thread idleThread;

    private static int nCBThreads;

    private static String privActStrCached;

    static class MRJIdler implements Runnable {
        @Override
        public void run() {
            try {
                while (activeCallbacks >= 1) {
                    Thread.sleep(10L);
                }
            } catch (InterruptedException e) {
            }
            idleThread = null;
        }
    }

    CBRunner(QTCallBack paramQTCallBack) {
        this.cb = paramQTCallBack;

        this.callBackTimeBase = GetCallBackTimeBase(quicktime.QTObject.ID(paramQTCallBack));

        if (QTSession.isCurrentOS(1)) {
            this.mInterruptClosure = new InterruptClosure(960, this);
            activeCallbacks += 1;
            if (idleThread == null) {
                idleThread = new Thread(new MRJIdler());
                idleThread.start();
            }
        } else {
            this.mMethodClosure = new CBMethodClosure(this, "callBackProc", "(II)V", 960);
        }
        privActStrCached = "CBThread-" + nCBThreads++ + "-";
        if ((paramQTCallBack instanceof TimeCallBack)) {
            privActStrCached += "TC";
        } else if ((paramQTCallBack instanceof TimeJumpCallBack)) {
            privActStrCached += "TJ";
        } else if ((paramQTCallBack instanceof RateCallBack)) {
            privActStrCached += "RC";
        } else if ((paramQTCallBack instanceof ExtremesCallBack)) {
            privActStrCached += "EC";
        }
        if (QTSession.getJavaVersion() == 65537) {
            this.cbThread = new Thread(this, privActStrCached);

        } else {
            this.cbThread =  new Thread(CBRunner.this, privActStrCached);
        }

        this.cbThread.setPriority(10);
        this.cbThread.start();
    }

    private Thread cbThread;

    private CBMethodClosure mMethodClosure;

    private InterruptClosure mInterruptClosure;

    private final QTCallBack cb;

    private boolean callbackDone = false;

    private final int callBackTimeBase;

    private boolean notified = false;

    private boolean executing;

    void cleanup() {
        this.callbackDone = true;
        if (QTSession.isCurrentOS(1)) {
            activeCallbacks -= 1;
            if (activeCallbacks < 1) {
                idleThread = null;
                activeCallbacks = 0;
            }
        }
        doNotify();
    }

    int getClosure() {
        if (this.mInterruptClosure != null)
            return this.mInterruptClosure.fetchPointer();
        if (this.mMethodClosure != null) {
            return this.mMethodClosure.getClosure();
        }
        throw new quicktime.QTRuntimeException("No Method Closure object created");
    }

    private void callBackProc(int paramInt1, int paramInt2) {
        doNotify();
    }

    @Override
    public void run() {
        doWait();
        if (!this.callbackDone) {
            for (;;) {
                this.notified = false;

                synchronized (QTSession.terminationLock()) {
                    if (QTSession.isInitialized()) {
                        if (this.callbackDone) {
                            break;
                        }
                        this.cb.timeWhenCalledMsecs = GetTimeBaseTime(this.callBackTimeBase, 1000, 0);
                        this.cb.rateWhenCalled = (GetTimeBaseEffectiveRate(this.callBackTimeBase) / 65536.0F);

                        if (this.cb.rateWhenCalled < 0.0F) {
                            this.cb.timeWhenCalledMsecs += 1;
                        }
                        if (this.callbackDone) {
                            break;
                        }
                        this.executing = true;
                        this.cb.execute();
                    } else {
                        break;
                    }
                }
                if (!this.callbackDone) {

                    doWait();

                    if (!this.notified) {
                        break;
                    }
                }
            }
        }
        if (this.cb.upp == this) {
            this.cb.upp = null;
        }
        this.cbThread = null;
        this.mMethodClosure = null;
    }

    private synchronized void doWait() {
        this.executing = false;
        if (this.notified || this.callbackDone) {
            return;
        }
        try {
            wait();
            this.notified = true;
        } catch (InterruptedException e) {
            this.notified = false;
        }
    }

    synchronized void doNotify() {
        if (this.executing) {
            this.notified = true;
            return;
        }
        this.notified = true;
        notifyAll();
    }

    private static int GetTimeBaseEffectiveRate(int paramInt) {
        return ClockManager.GetTimeBaseEffectiveRate(paramInt);
    }

    private static int GetTimeBaseTime(int paramInt1, int paramInt2, int paramInt3) {
        return ClockManager.GetTimeBaseTime(paramInt1, paramInt2, paramInt3);
    }

    private static int GetCallBackTimeBase(int paramInt) {
        return ClockManager.GetCallBackTimeBase(paramInt);
    }

    static class CBMethodClosure extends quicktime.jdirect.MethodClosure {
        CBMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }
}
