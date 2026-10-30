package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.Ticklish;

/**
 * @deprecated
 * This class is the base class for all periodically recurring time based actions.
 * The Actionable object (if defined) will
 * be an object that has its trigger method called when the PeriodicAction sub-class
 * action is executed or when a constraining condition of the action is reached.
 * The action is tickled periodically and whether it will do its action is dependant
 * upon the time interval as specified by the scale and period and the time and
 * effective rate of the tickle call.
 *
 * scale == 10, period = 1 => action executed 10 times a second at er == 1
 * scale == 2, period = 1 => action is executed 2 times a second at er == 1
 * scale == 1, scale = 2 => action is executed once every 2 seconds at er == 1
 *
 * Every time the action is executed the time is kept and consequent calls to tickle
 * result in a comparison between the new time, the rate and the last time the action
 * was executed. Whether the action is executed is calculated by:
 * if (currentTime <= period / scale * 1000 / effectiveRate + lastTime) at rate > 0.
 * The tickle method is not called with an effectiveRate of 0.
 * Whether the doAction method is called can also be affected by a flag that controls
 * whether an action is triggered depending on rate settings.
 */
public abstract class PeriodicAction implements Ticklish {

    private static final boolean doPrint = false;

    /**
     * The actionable actor object that can be invoked by an action if a constraining
     * condition is reached
     * @deprecated
     * @since QTJava 6.1
     */
    protected Actionable actor;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Create a PeriodicAction. Subclasses are responsible for defining the action
     * that occurs whenever the action is invoked.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @deprecated
     * @since QTJava 6.1
     */
    protected PeriodicAction(int scale, int period) {
        setScale(scale);
        setPeriod(period);
    }

    /**
     * If true then the tickle method returns true and the action is considered to
     * be still active by its owner and will be invoked on consequent tickles.
     * @deprecated
     * @since QTJava 6.1
     */
    protected final boolean reschedule = true;

    private int lastTime;

    private int scale = 1;

    private int period = 1;

    private float multiplier;

    private int triggerCondition = 3;

    /**
     * This method should be called by the actions owner whenver a time condition
     * of the owner's time line has changed, as this can effect the trigger
     * conditions of the action.
     * @see quicktime.app.time.Ticklish
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public void timeChanged(int lastTime) throws QTException {
        this.lastTime = lastTime;
    }

    /**
     * This method can be called as often as you like, though effective rate
     * cannot be zero.
     * The provided time and rate values are used to calculate whether an action
     * should be executed. If an action should be executed this method calls
     * the doAction method which should perform the action.
     * @param effectiveRate the effective rate of the Timer when the time condition matches
     * @param currentTime the time that the Timer invoked the tickle method.
     * @return true if the action is still interested in being tickled.
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    @Override
    public boolean tickle(float effectiveRate, int currentTime) throws QTException {
        if (this.period == 0) {
            this.lastTime = currentTime;
            doAction(effectiveRate, currentTime);
            if ((this.actor != null) && (constraintReached()))
                this.actor.trigger();
            return this.reschedule;
        }
        int i = (int) (this.multiplier / effectiveRate);
        int j = this.lastTime + i;
        if (((i < 0) && (currentTime <= j) && ((this.triggerCondition | 0x2) != 0)) || ((i > 0) && (currentTime >= j) && ((this.triggerCondition | 0x1) != 0))) {
            this.lastTime = currentTime;
            doAction(effectiveRate, currentTime);
            if ((this.actor != null) && (constraintReached())) {
                this.actor.trigger();
            }
        }
        return this.reschedule;
    }

    /**
     * This method sets the actionable object of the action.
     * @deprecated
     * @since QTJava 6.1
     */
    public void setActionable(Actionable actionable) {
        this.actor = actionable;
    }

    /**
     * This method retrieves the current actor that is attached to the action
     * @deprecated
     * @since QTJava 6.1
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * Sets the current period of the action - how many scale ticks will elapse
     * before the action is executed. If the period is zero the doAction method
     * is called whenever the PeriodicAction is tickled.
     * @param period the new period
     * @deprecated
     * @since QTJava 6.1
     */
    public void setPeriod(int period) {
        this.period = period;
        doMultiplier();
    }

    /**
     * Returns the current period of the action
     * @return the period
     * @deprecated
     * @since QTJava 6.1
     */
    public int getPeriod() {
        return this.period;
    }

    /**
     * Sets the scale of the action - this is the resolution of a second at rate == 1,
     * thus a scale of 10 is 100msecs at rate == 1, 50msecs at rate == 2 etc. This value
     * must be greater than zero.
     * @param s the new scale or resolution of the action
     * @deprecated
     * @since QTJava 6.1
     */
    public void setScale(int s) {
        if (s < 1)
            s = 1;
        this.scale = s;
        doMultiplier();
    }

    /**
     * Return the current scale of the action.
     * @return the current scale
     * @deprecated
     * @since QTJava 6.1
     */
    public int getScale() {
        return this.scale;
    }

    /**
     * Returns the trigger condition of the action - an action can trigger
     * either when rate is positive, rate is negative or either.
     * @return the current trigger condition
     * @see "quicktime.std.StdConstants.triggerTimeFwd"
     * @see "quicktime.std.StdConstants.triggerTimeBwd"
     * @see "quicktime.std.StdConstants.triggerTimeEither"
     * @deprecated
     * @since QTJava 6.1
     */
    public int getTriggerCondition() {
        return this.triggerCondition;
    }

    /**
     * Sets the trigger condition of the action - an action can trigger
     * either when rate is positive, rate is negative or either.
     * @param cond the current trigger condition
     * @see "quicktime.std.StdConstants.triggerTimeFwd"
     * @see "quicktime.std.StdConstants.triggerTimeBwd"
     * @see "quicktime.std.StdConstants.triggerTimeEither"
     * @deprecated
     * @since QTJava 6.1
     */
    public void setTriggerCondition(int cond) {
        this.triggerCondition = cond;
    }

    /**
     * Return a String representation of this object.
     * @return a String
     * @deprecated
     * @since QTJava 6.1
     */
    public String toString() {
        return super.toString() + "[scale=" + getScale() + ",period=" + getPeriod() + "]";
    }

    /**
     * Returns true if the objects are equal or false otherwise.
     * @param obj the object to test
     * @return a boolean
     * @deprecated
     * @since QTJava 6.1
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj instanceof PeriodicAction periodicAction)) {
            if ((this.reschedule != periodicAction.reschedule) || (this.lastTime != periodicAction.lastTime) || (this.scale != periodicAction.scale) || (this.period != periodicAction.period) || (this.triggerCondition != periodicAction.triggerCondition)) {
                return false;
            }
            if (this != obj)
                return false;
            return true;
        }
        return false;
    }

    private void doMultiplier() {
        this.multiplier = (this.period / this.scale * 1000.0F);
    }

    /**
     * This method is called by the tickle method when an action becomes active.
     * Subclasses should do their desired actions
     * when this method is called.
     * @param er the actual rate at which the action has been triggered
     * @param t the time in Ticklish.kScale that the action has been triggered
     * @throws quicktime.QTException
     * @deprecated
     * @since QTJava 6.1
     */
    protected abstract void doAction(float er, int t) throws QTException;

    /**
     * If after the doAction method has executed this method returns true then
     * if the Action has an Actionable object this actionable is triggered. If
     * constraintReached returns false no further action is taken.
     * @return true if the actionable object (if set) should be triggered
     * @deprecated
     * @since QTJava 6.1
     */
    protected abstract boolean constraintReached();
}
