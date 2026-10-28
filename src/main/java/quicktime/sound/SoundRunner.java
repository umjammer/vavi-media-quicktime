
package quicktime.sound;

import quicktime.QTSession;
import quicktime.jdirect.MethodClosure;


final class SoundRunner {

    private static int activeCallbacks = 0;

    private static Thread idleThread;

    private static int whichThread;

    private Thread runnerThread;

    private SoundMethodClosure mMethodClosure;

    SoundRunner(SPB paramSPB, SICompletion paramSICompletion) {
        this.soundParamBlock = paramSPB;
        this.completion = paramSICompletion;
    }

    SoundRunner(SoundCallBack paramSoundCallBack) {
        this.callback = paramSoundCallBack;
    }

    private SInterruptClosure mInterruptClosure;

    private SPB soundParamBlock;

    private SICompletion completion;

    private SndChannel sndChan;

    private SoundCallBack callback;

    private boolean notified = false;

    private boolean completionDone = false;

    void setSndChannel(SndChannel paramSndChannel) {
        this.sndChan = paramSndChannel;
    }

    private void siCompletion(int paramInt) {
        doNotify();
    }

    private void sndCallBack(int paramInt1, int paramInt2) {
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

        SoundMethodClosure(Object paramObject, String paramString1, String paramString2, int paramInt) {
            super(paramObject, paramString1, paramString2, paramInt);
        }

        int getClosure() {
            return this.closure;
        }
    }

    static class SInterruptClosure extends com.apple.mrj.jdirect.CompletionRoutineNotifier {

        SInterruptClosure(int paramInt, Object paramObject) {
            super(paramInt, paramObject);
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
