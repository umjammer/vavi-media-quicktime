
package com.apple.mrj.macos.carbon;

public class Timer implements Runnable, EventLoopTimerInterface {

    private static final boolean DEBUG = false;

    private EventLoopTimerClosureUPP fProc;

    private int fRef;

    private double fInterval;

    private Runnable fTarget;

    public Timer(double d, Runnable runnable) {
        if (d < 0.0D) {
            throw new IllegalArgumentException("Negative Timer interval " + d);
        } else {
            fProc = new EventLoopTimerClosureUPP(this);
            fInterval = d;
            setTarget(runnable);
            return;
        }
    }

    public static void startOneShot(final Runnable target) {
        if (target == null) {
            throw new IllegalArgumentException();
        } else {
            class Deferrer extends Timer {

                public void run() {
                    target.run();
                    stop();
                    dispose();
                }

                Deferrer() {
                    super(0.0D, null);
                }
            }

            Deferrer deferrer = new Deferrer();
            deferrer.start(0.0D);
            return;
        }
    }

    public static void startOneShotAndBlock(final Runnable target) {
        if (target == null)
            throw new IllegalArgumentException();
        final Object sync = new Object();
        class Deferrer extends Timer {

            public void run() {
                try {
                    target.run();
                } finally {
                    synchronized (sync) {
                        sync.notify();
                    }
                }
            }

            Deferrer() {
                super(0.0D, null);
            }
        }

        Deferrer deferrer = new Deferrer();
        synchronized (sync) {
            deferrer.start(0.0D);
            try {
                sync.wait();
            } catch (InterruptedException e) {
            }
            deferrer.stop();
            deferrer.dispose();
        }
    }

    public void dispose() {
        if (fProc != null) {
            stop();
            fProc.dispose();
            fProc = null;
        }
    }

    public void finalize() {
        dispose();
    }

    public void setTarget(Runnable runnable) {
        fTarget = runnable == null ? this : runnable;
    }

    public Runnable getTarget() {
        return fTarget;
    }

    public void start(double d) {
        int ai[] = new int[1];
        if (fRef != 0) {
            HIToolBoxFunctions.SetEventLoopTimerNextFireTime(fRef, d);
        } else {
            int i = HIToolBoxFunctions.InstallEventLoopTimer(HIToolBoxFunctions.GetMainEventLoop(),
                                                             d,
                                                             fInterval,
                                                             fProc.getClosure(),
                                                             0,
                                                             ai);
            fRef = ai[0];
            if (fRef == 0)
                throw new Error("Couldn't start Timer -- InstallEventLoopTimer failed");
        }
    }

    public void stop() {
        if (fRef != 0) {
            HIToolBoxFunctions.RemoveEventLoopTimer(fRef);
            fRef = 0;
        }
    }

    public void run() {
    }

    public void EventLoopTimer(int i, int j) {
        try {
            fTarget.run();
        } catch (Throwable throwable) {
            System.err.println("A Buckyball Timer threw an exception:");
            throwable.printStackTrace();
        }
    }
}
