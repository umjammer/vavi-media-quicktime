
package quicktime.sound;

import quicktime.QTSession;
import quicktime.jdirect.MethodClosure;


final class SoundRunner {

    private static int activeCallbacks = 0;

    private static Thread idleThread;

    private static int whichThread;

    private Thread runnerThread;

    private SoundMethodClosure mMethodClosure;

    SoundRunner(SPB soundParamBlock, SICompletion completion) {
        this.soundParamBlock = soundParamBlock;
        this.completion = completion;
    }

    SoundRunner(SoundCallBack callback) {
        this.callback = callback;
    }

    private SInterruptClosure mInterruptClosure;

    private SPB soundParamBlock;

    private SICompletion completion;

    private SndChannel sndChan;

    private SoundCallBack callback;

    private boolean notified = false;

    private boolean completionDone = false;

    void setSndChannel(SndChannel sndChannel) {
        this.sndChan = sndChannel;
    }

    private void siCompletion(int spb) {
        doNotify();
    }

    private void sndCallBack(int chan, int cmd) {
        doNotify();
    }

    private synchronized void doWait() {
        try {
            if (this.notified)
                return;
            wait();
            this.notified = true;
        } catch (InterruptedException e) {
            this.notified = false;
        }
    }

    private synchronized void doNotify() {
        this.notified = true;
        notifyAll();
    }

    void cleanup() {
        this.completionDone = true;
        if (QTSession.isCurrentOS(1)) {
            activeCallbacks -= 1;
            if (activeCallbacks < 1) {
                idleThread = null;
                activeCallbacks = 0;
            }
        }
        if (!this.notified)
            doNotify();
        this.mInterruptClosure = null;
        this.mMethodClosure = null;
        this.runnerThread = null;
    }

    int getClosure() {
        setUpThread();
        if (this.mInterruptClosure != null)
            return this.mInterruptClosure.fetchPointer();
        if (this.mMethodClosure != null) {
            return this.mMethodClosure.getClosure();
        }
        throw new quicktime.QTRuntimeException("No Method Closure object created");
    }

    private void setUpThread() {
        if (this.runnerThread != null)
            return;
        if (QTSession.isCurrentOS(1)) {
            this.mInterruptClosure = this.soundParamBlock != null
                    ? new SInterruptClosure(192, this)
                    : new SInterruptClosure(960, this);

            activeCallbacks += 1;
            if (idleThread == null) {
                idleThread = new Thread(new MRJIdler());
                idleThread.start();
            }
        } else {
            this.mMethodClosure = this.soundParamBlock != null
                    ? new SoundMethodClosure(this, "siCompletion", "(I)V", 192)
                    : new SoundMethodClosure(this, "sndCallBack", "(II)V", 960);
        }

        if (QTSession.getJavaVersion() == 65537) {
            this.runnerThread = this.soundParamBlock != null
                    ? new Thread(new CompletionRunner(), "Sound Completion Thread:" + ++whichThread)
                    : new Thread(new SndCallbackRunner(), "SndChannel CallBack Thread:" + ++whichThread);

        } else {
            this.runnerThread = soundParamBlock != null
                    ? new Thread(new CompletionRunner(), "Sound Completion Thread:" + ++whichThread)
                    : new Thread(new SndCallbackRunner(), "SndChannel CallBack Thread:" + ++whichThread);
        }

        this.runnerThread.setPriority(10);
        this.notified = false;
        this.completionDone = false;
        this.runnerThread.start();
    }

    class CompletionRunner implements Runnable {

        CompletionRunner() {
        }

        @Override
        public void run() {
            for (; ; ) {
                SoundRunner.this.doWait();
                if ((!SoundRunner.this.notified) || (SoundRunner.this.completionDone))
                    break;
                SoundRunner.this.completion.execute(SoundRunner.this.soundParamBlock);
                SoundRunner.this.notified = false;
            }

            SoundRunner.this.cleanup();
        }
    }

    class SndCallbackRunner implements Runnable {

        SndCallbackRunner() {
        }

        @Override
        public void run() {
            for (; ; ) {
                SoundRunner.this.doWait();
                if ((!SoundRunner.this.notified) || (SoundRunner.this.completionDone)) {
                    break;
                }
                SoundRunner.this.callback.execute(SoundRunner.this.sndChan);

                SoundRunner.this.notified = false;
            }

            SoundRunner.this.cleanup();
        }
    }

    static class SoundMethodClosure extends MethodClosure {

        SoundMethodClosure(Object target, String methodName, String descriptor, int procInfo) {
            super(target, methodName, descriptor, procInfo);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class SInterruptClosure extends com.apple.mrj.jdirect.CompletionRoutineNotifier {

        SInterruptClosure(int routine, Object target) {
            super(routine, target);
        }

        int fetchPointer() {
            return getPointer();
        }
    }

    static class MRJIdler implements Runnable {

        @Override
        public void run() {
            try {
                while (SoundRunner.activeCallbacks >= 1) {
                    Thread.sleep(10L);
                }
            } catch (InterruptedException _) {
            }
            SoundRunner.idleThread = null;
        }
    }
}
