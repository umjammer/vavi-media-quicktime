package quicktime.app.time;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.clocks.RateCallBack;
import quicktime.std.clocks.TimeBase;
import quicktime.std.clocks.TimeCallBack;
import quicktime.std.clocks.TimeJumpCallBack;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.movies.Movie;

/**
 * This class provides timing services for either a subclass or a target object that
 * implements the Ticklish interface. The Timer invokes the tickle method based on
 * the rate that it is ticking and the scale and period of the timer itself. For instance
 *
 * 10 scale, 1 period => the tickle method is invoked 10 times a second at a rate of 1
 * 1 scale, 1 period => the tickle method is invoked 1 time a second at a rate of 1
 * 1 scale, 2 period => the tickle method is invoked every 2 seconds at a rate of 1
 * 3 scale, 2 period => the tickle method is invoked 3 times every 2 seconds at a rate of 1
 *
 * The Timer uses a quicktime TimeBase object as its time base. The timer's TimeBase can
 * be slaved to or be a master of any other TimeBase. If this relationship is established the
 * actual or effective rate of the TimeBase is determined by the
 * rate of its own TimeBase and the rates of all of its master TimeBases.
 *
 * The Timer can be in one of three states.
 * Active and tickling - in which case it issues both timeChanged and tickle calls
 * Active and not tickling - in which case it issues timeChanged calls but not tickle calls
 * Inactive - neither timeChanged or tickle calls are issued.
 *
 * By default a Timer is inactive and an application using a Timer must make it active. A Timer will
 * once active, tickle if tickling is enabled (which is the default setting).
 * If an application wants to use a Timer without its tickle services, it can
 * disable the tickle capability and reenable it later if appropriate.
 *
 * Whenever the condition of the time base's time line changes the Timer notifies its subclass
 * or Ticklish target through invoking the timeChanged method with the new time. The conditions
 * under which this method is invoked are when the time value jumps or the effective rate changes from
 * positive to negative or vice versa.
 *
 * Whenever the tickle method is invoked the effective rate
 * of the Timer's TimeBase is passes on, as is the time of the tickle method. In both cases this
 * time value is specified in terms of the Ticklish.kScale value. If the timer is ticking (ie. its
 * effective rate is not equal to zero) then the time when a timeChanged notification was sent
 * will also be the time value that the next tickle method will receive.
 *
 * If the tickle method returns true then the Timer will tickle again when the specified scale and period
 * has elapsed. If the tickle method returns false then the Timer will not tickle again until the Timer's
 * tickling has been reenabled.
 *
 * It does not guarantee that the tickle method will be called
 * by the specified interval. This is affected by two factors - latencies in Java threads or other activity
 * in the runtime that does not allow the invocation of the timer's tickle, and the amount of time that the
 * the tickle method itself takes to execute. In these cases (ie. the next tickle is late) it will be called at
 * the earliest possible time. For example if you have a tickle interval of 100 msecs and the previous
 * tickle took 150msecs to execute, then the next tickle will be given at the earlies possible return.
 * A consequent tickle to that will be in the interval specified or ASAP if this consequent tickle takes
 * longer than the specified time.
 *
 * @see {@link quicktime.std.clocks.TimeBase},
 * {@link quicktime.app.time.Ticklish},
 * {@link quicktime.app.time.Timeable}
 */
public class Timer implements Timeable {

    private static final boolean debug = false;

    private static int count = 1;

    private Movie m;

    TimeBase theTimeBase;

    private Ticklish target;

    private int whichOne;

    /**
     * Create a Timer.
     * @param scale the scale of the timer's tickle invocation period
     * @param period the number of scale size ticks that elapse between each invocation of the tickle method
     */
    public Timer(int paramInt1, int paramInt2) throws QTException {
        this(paramInt1, paramInt2, null);
    }

    /**
     * Create a Timer.
     * @param scale the scale of the timer's tickle invocation period
     * @param period the number of scale size ticks that elapse between each invocation of the tickle method
     * @param target the Ticklish target of the Timer - if this is not null then the target's tickle and
     *  timeChanged method will be invoked by the Timer.
     */
    public Timer(int paramInt1, int paramInt2, Ticklish paramTicklish) throws QTException {
        this(paramInt1, paramInt2, paramTicklish, null);
    }

    /**
     * Create a Timer.
     * @param scale the scale of the timer's tickle invocation period
     * @param period the number of scale size ticks that elapse between each invocation of the tickle method
     * @param target the Ticklish target of the Timer - if this is not null then the target's tickle and
     *  timeChanged method will be invoked by the Timer.
     * @param mov the Movie from which the TimeBase of the timer will be derived.
     */
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

    /**
     * Returns true if the Timer's effective rate is currently non-zero
     * @return a boolean
     */
    public boolean isTicking() {
        return this.isTimerTicking;
    }

    /**
     * Returns true if the Timer, when active, will tickle or false if the Timer, when active,
     * will not tickle. This does not indicate whether the Timer is active.
     * @return a boolean
     */
    public boolean isTickling() {
        return this.doesTickle;
    }

    /**
     * Sets the tickle state of the Timer. If true then the Timer when active will tickle.
     * If false then the Timer when active will not tickle. This does not activate or deactivate a Timer.
     * @param flag a boolean
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * Returns the current target of the Targetable object - this is the object
     * that the Targetable will act upon.
     * @return the current target
     */
    public Ticklish getTicklish() {
        return this.target;
    }

    /**
     * Returns the current time base that provides the time foundation
     * of this object.
     * @return a TimeBase
     */
    public final TimeBase getTimeBase() {
        return this.theTimeBase;
    }

    /**
     * This method will reschedule the Timer's callback mechanism to the new scale and
     * period values. Scale cannot be zero. A zero period will disable any tickle.
     * This will have the side-effect of resetting the isTickling state to true, so
     * can be used to both reschedule a tickle call and ensure that a tickle will be sent.
     * @param scale the new scale of the Timer's tickle notification
     * @param period the number of scale ticks that will elapse between notifications.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * Returns the current scale of the Timer's tickle notification.
     * @return the scale
     */
    public int getScale() {
        return this.tc.scale;
    }

    /**
     * Returns the current period (number of scale ticks) of the Timer's tickle notification.
     * @return the scale
     */
    public int getPeriod() {
        return Math.abs(this.tc.period);
    }

    /**
     * Returns the current rate of this Timer - this is the Timer's TimeBase rate. This
     * may not be the actual or effective rate that the timer is ticking at if its TimeBase
     * is slaved to another TimeBase
     * @return the current rate
     */
    public float getRate() {
        return this.theTimeBase.getRate();
    }

    /**
     * Sets the Timeable's playback rate. Typically a rate of zero will
     * mean that the player is stopped, a positive value that the player
     * is playing forwards. If the object implements the Playable interface
     * setting a negative value will play the player backwards. However, if
     * the object does not have a time line then a negative value will continue
     * to play the object in a forwards direction.
     * @param rate the new playback rate.
     */
    public void setRate(float paramFloat) {
        this.theTimeBase.setRate(paramFloat);
        if (paramFloat == 0.0F) {
            this.isTimerTicking = false;
        } else {
            this.isTimerTicking = true;
        }
    }

    /**
     * Sets the active state of the Timer; only an active Timer will tickle.
     * @param flag fi true the Timer is active, if false it is inactive
     * @param {@link quicktime.QTException}
     */
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

    /**
     * Returns the current active state of the Timer.
     * @return true if the Timer is active, or false if it is not
     */
    public boolean isActive() {
        return this.cbActive;
    }

    /**
     * Return a string representation of this class.
     */
    public String toString() {
        return getClass().getName() + "[isActive=" + isActive() + ",whichOne=" + this.whichOne + ",rate=" + this.theTimeBase.getRate() + ",efRate=" + this.theTimeBase.getEffectiveRate() + ",scale=" + getScale() + "]";
    }

    /**
     * This is called by the Timer itself when the time base of the Timer has
     * changed, through being reset to a new time or through the effective rate
     * of the Timer changing from positive to negative or vice versa
     * @param newTime the new time of the Timer's time line expressed in the Ticklish.kScale
     * @param {@link quicktime.QTException}
     */
    protected void timeChanged(int paramInt) throws QTException {
        if (this.target != null) {
            this.target.timeChanged(paramInt);
        }
    }

    /**
     * When the time from zero (or a timeChanged time value, or the previous tickle time)
     * that is specified by the scale and period of the Timer has elapsed the timer calls
     * the tickle method. The Timer will only call this method if the Timer has an
     * effective rate that is not zero.
     * @param er the effective or actual rate that the timer is ticking at.
     * @param time the current time of the time base expressed in the Ticklish.kScale
     * @return true if the Timer should remain active and tickle again or false if it should become inactive.
     * @return {@link quicktime.QTException}
     */
    protected boolean tickle(float paramFloat, int paramInt) throws QTException {
        if (this.target != null)
            return this.target.tickle(paramFloat, paramInt);
        return false;
    }

    /**
     * The finalize method deactivates the timer.
     * java.lang.Throwable
     */
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
