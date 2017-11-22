
package quicktime.app.time;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.clocks.RateCallBack;
import quicktime.std.clocks.TimeBase;
import quicktime.std.clocks.TimeCallBack;
import quicktime.std.clocks.TimeJumpCallBack;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.movies.Movie;


public class Timer implements Timeable {
    private static final boolean debug = false;

    private static int count = 1;

    private Movie m;

    TimeBase theTimeBase;

    private Ticklish target;

    private int whichOne;

    public Timer(int paramInt1, int paramInt2) throws QTException {
        this(paramInt1, paramInt2, null);
    }

    public Timer(int paramInt1, int paramInt2, Ticklish paramTicklish) throws QTException {
        this(paramInt1, paramInt2, paramTicklish, null);
    }

    public Timer(int paramInt1, int paramInt2, Ticklish paramTicklish, Movie paramMovie) throws QTException {
        if (paramInt1 == 0)
            throw new QTException("scale cannot be zero");
        this.m = paramMovie;
        if (this.m != null) {
            this.theTimeBase = this.m.getTimeBase();
            this.isTimerTicking = (this.theTimeBase.getEffectiveRate() != 0.0F);
        } else {
            this.theTimeBase = new TimeBase();
            this.theTimeBase.setStartTime(new TimeRecord());
            TimeRecord localTimeRecord = new TimeRecord();
            localTimeRecord.setValue(86400L);
            localTimeRecord.setScale(1);
            this.theTimeBase.setStopTime(localTimeRecord);
            this.theTimeBase.setFlags(1);
        }
        this.tc = new TC(this.theTimeBase, paramInt1, Math.abs(paramInt2), 0);
        this.tj = new TJ(this.theTimeBase);
        this.whichOne = (count++);
        this.target = paramTicklish;
        this.rc = new RC(this.theTimeBase);
    }

    private boolean doesTickle = true;

    private boolean cbActive = false;

    private boolean isTimerTicking = false;

    private RC rc;

    private TC tc;

    private TJ tj;

    private float lastKnownRate;

    public boolean isTicking() {
        return this.isTimerTicking;
    }

    public boolean isTickling() {
        return this.doesTickle;
    }

    public void setTickling(boolean paramBoolean) throws StdQTException {
        this.doesTickle = paramBoolean;
        if (isActive()) {
            this.tc.cancel();
            if (this.doesTickle) {
                this.tc.value = (this.theTimeBase.getTime(this.tc.scale) + this.tc.period);
                this.tc.flags = (this.lastKnownRate > 0.0F ? 1 : 2);
                this.tc.callMeWhen();
            } else {
                this.tc.cancelAndCleanup();
            }
        }
    }

    public Ticklish getTicklish() {
        return this.target;
    }

    public final TimeBase getTimeBase() {
        return this.theTimeBase;
    }

    public synchronized void rescheduleTickle(int paramInt1, int paramInt2) throws QTException {
        if (paramInt1 == 0)
            throw new QTException("scale cannot be zero");
        this.tc.cancel();
        this.tc.scale = paramInt1;
        this.tc.period = paramInt2;
        this.tc.value = this.theTimeBase.getTime(paramInt1);
        this.tc.flags = (this.lastKnownRate > 0.0F ? 1 : 2);
        if (this.cbActive)
            this.tc.callMeWhen();
        this.doesTickle = true;
    }

    public int getScale() {
        return this.tc.scale;
    }

    public int getPeriod() {
        return Math.abs(this.tc.period);
    }

    public float getRate() {
        return this.theTimeBase.getRate();
    }

    public void setRate(float paramFloat) {
        this.theTimeBase.setRate(paramFloat);
        if (paramFloat == 0.0F) {
            this.isTimerTicking = false;
        } else {
            this.isTimerTicking = true;
        }
    }

    public synchronized void setActive(boolean paramBoolean) throws QTException {
        if (paramBoolean == this.cbActive)
            return;
        this.cbActive = paramBoolean;

        this.tc.cancel();
        this.tj.cancel();
        this.rc.cancel();

        if (this.cbActive) {
            this.lastKnownRate = this.theTimeBase.getEffectiveRate();

            this.rc.callMeWhen();
            this.tj.callMeWhen();
            this.isTimerTicking = (this.lastKnownRate != 0.0F);

            if (this.lastKnownRate != 0.0F) {
                this.tc.period = Math.abs(this.tc.period);
                if (this.lastKnownRate < 0.0F)
                    this.tc.period = (-this.tc.period);
                this.tc.value = this.theTimeBase.getTime(this.tc.scale);
                doTimeChanged(this.lastKnownRate, this.theTimeBase.getTime(1000));
            }
        } else {
            this.rc.cancelAndCleanup();
            this.tj.cancelAndCleanup();
            this.tc.cancelAndCleanup();
        }
    }

    public boolean isActive() {
        return this.cbActive;
    }

    public String toString() {
        return getClass().getName() + "[isActive=" + isActive() + ",whichOne=" + this.whichOne + ",rate="
               + this.theTimeBase.getRate() + ",efRate=" + this.theTimeBase.getEffectiveRate() + ",scale=" + getScale() + "]";
    }

    protected void timeChanged(int paramInt) throws QTException {
        if (this.target != null) {
            this.target.timeChanged(paramInt);
        }
    }

    protected boolean tickle(float paramFloat, int paramInt) throws QTException {
        if (this.target != null)
            return this.target.tickle(paramFloat, paramInt);
        return false;
    }

    protected void finalize() throws Throwable {
        setActive(false);
        super.finalize();
    }

    private final synchronized void doTimeChanged(float paramFloat, int paramInt) throws QTException {
        if (this.cbActive) {
            timeChanged(paramInt);
            this.lastKnownRate = paramFloat;
            if ((this.lastKnownRate != 0.0F) && (this.doesTickle) && (this.cbActive)) {
                doReschedule(this.lastKnownRate, true);
            } else {
                this.tc.cancel();
            }
        }
    }

    private final void doTickle(float paramFloat, int paramInt) throws QTException {
        if (paramFloat != 0.0F) {
            synchronized (this) {
                if (this.cbActive) {
                    if ((tickle(paramFloat, paramInt)) && (this.cbActive)) {
                        doReschedule(paramFloat, false);
                    } else
                        setTickling(false);
                }
            }
        }
    }

    private void doReschedule(float paramFloat, boolean paramBoolean) throws QTException {
        this.tc.cancel();

        int i = this.theTimeBase.getTime(this.tc.scale);
        if (paramBoolean) {
            this.tc.value = i;
        } else if (paramFloat > 0.0F) {
            this.tc.value += this.tc.period;
            if (this.tc.value <= i) {
                this.tc.value = i;
            }
        } else {
            this.tc.value += this.tc.period;
            if (this.tc.value > i) {
                this.tc.value = i;
            }
        }

        this.tc.callMeWhen();
    }

    private class TC extends TimeCallBack {
        int period;

        TC(TimeBase paramTimeBase, int paramInt1, int paramInt2, int paramInt3) throws QTException {
            super(paramTimeBase, paramInt1, paramInt2, 3);
            this.period = paramInt2;
        }

        public void execute() {
            try {
                Timer.this.doTickle(this.rateWhenCalled, this.timeWhenCalledMsecs);
            } catch (QTException localQTException) {
                localQTException.printStackTrace();
            }
        }
    }

    private class TJ extends TimeJumpCallBack {
        TJ(TimeBase paramTimeBase) throws QTException {
            super(paramTimeBase);
        }

        public void execute() {
            Timer.this.tc.cancel();
            try {
                Timer.this.doTimeChanged(this.rateWhenCalled, this.timeWhenCalledMsecs);
                cancel();
                if (Timer.this.isActive())
                    callMeWhen();
            } catch (QTException localQTException) {
                localQTException.printStackTrace();
            }
        }
    }

    private class RC extends RateCallBack {
        RC(TimeBase paramTimeBase) throws QTException {
            super(paramTimeBase, 0.0F, 0);
        }

        public void execute() {
            try {
                if (this.rateWhenCalled > 0.0F) {
                    Timer.this.isTimerTicking = true;
                    if (Timer.this.lastKnownRate <= 0.0F) {
                        Timer.this.tc.cancel();
                        Timer.this.tc.period = Math.abs(Timer.this.tc.period);
                        Timer.this.doTimeChanged(this.rateWhenCalled, this.timeWhenCalledMsecs);
                    }
                } else if (this.rateWhenCalled < 0.0F) {
                    Timer.this.isTimerTicking = true;
                    if (Timer.this.lastKnownRate >= 0.0F) {
                        Timer.this.tc.cancel();
                        Timer.this.tc.period = Math.abs(Timer.this.tc.period);
                        Timer.this.tc.period = (-Timer.this.tc.period);
                        Timer.this.doTimeChanged(this.rateWhenCalled, this.timeWhenCalledMsecs);
                    }
                } else {
                    Timer.this.isTimerTicking = false;
                    if (Timer.this.lastKnownRate != 0.0F) {
                        Timer.this.tc.cancel();
                        Timer.this.doTimeChanged(this.rateWhenCalled, this.timeWhenCalledMsecs);
                    }
                }

                cancel();
                if (Timer.this.isActive()) {
                    callMeWhen();
                }
            } catch (QTException localQTException) {
                localQTException.printStackTrace();
            }
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * Timer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
