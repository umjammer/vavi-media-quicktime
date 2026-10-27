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
     * @deprecated
     * @since QTJava 6.1
     * The actionable actor object that can be invoked by an action if a constraining
     * condition is reached
     */
    protected Actionable actor;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a PeriodicAction. Subclasses are responsible for defining the action
     * that occurs whenever the action is invoked.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     */
    protected PeriodicAction(int paramInt1, int paramInt2) {
        setScale(paramInt1);
        setPeriod(paramInt2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If true then the tickle method returns true and the action is considered to
     * be still active by its owner and will be invoked on consequent tickles.
     */
    protected boolean reschedule = true;

    private int lastTime;

    private int scale = 1;

    private int period = 1;

    private float multiplier;

    private int triggerCondition = 3;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method should be called by the actions owner whenver a time condition
     * of the owner's time line has changed, as this can effect the trigger
     * conditions of the action.
     * @see quicktime.app.time.Ticklish
     */
    @Override
    public void timeChanged(int paramInt) throws QTException {
        this.lastTime = paramInt;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method can be called as often as you like, though effective rate
     * cannot be zero.
     * The provided time and rate values are used to calculate whether an action
     * should be executed. If an action should be executed this method calls
     * the doAction method which should perform the action.
     * @param effectiveRate the effective rate of the Timer when the time condition matches
     * @param currentTime the time that the Timer invoked the tickle method.
     * @return true if the action is still interested in being tickled.
     * @throws quicktime.QTException
     */
    @Override
    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        if (this.period == 0) {
            this.lastTime = paramInt;
            doAction(paramFloat, paramInt);
            if ((this.actor != null) && (constraintReached()))
                this.actor.trigger();
            return this.reschedule;
        }
        int i = (int) (this.multiplier / paramFloat);
        int j = this.lastTime + i;
        if (((i < 0) && (paramInt <= j) && ((this.triggerCondition | 0x2) != 0)) || ((i > 0) && (paramInt >= j) && ((this.triggerCondition | 0x1) != 0))) {
            this.lastTime = paramInt;
            doAction(paramFloat, paramInt);
            if ((this.actor != null) && (constraintReached())) {
                this.actor.trigger();
            }
        }
        return this.reschedule;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method sets the actionable object of the action.
     */
    public void setActionable(Actionable paramActionable) {
        this.actor = paramActionable;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method retreives the current actor that is attached to the action
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the current period of the action - how many scale ticks will elapse
     * before the action is executed. If the period is zero the doAction method
     * is called whenever the PeriodicAction is tickled.
     * @param period the new period
     */
    public void setPeriod(int paramInt) {
        this.period = paramInt;
        doMultiplier();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current period of the action
     * @return the period
     */
    public int getPeriod() {
        return this.period;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the scale of the action - this is the resolution of a second at rate == 1,
     * thus a scale of 10 is 100msecs at rate == 1, 50msecs at rate == 2 etc. This value
     * must be greater than zero.
     * @param s the new scale or resolution of the action
     */
    public void setScale(int paramInt) {
        if (paramInt < 1)
            paramInt = 1;
        this.scale = paramInt;
        doMultiplier();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return the current scale of the action.
     * @return the current scale
     */
    public int getScale() {
        return this.scale;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the trigger condition of the action - an action can trigger
     * either when rate is positive, rate is negative or either.
     * @return the current trigger condition
     * @see quicktime.std.StdConstants.triggerTimeFwd,
     * quicktime.std.StdConstants.triggerTimeBwd,
     * quicktime.std.StdConstants.triggerTimeEither
     */
    public int getTriggerCondition() {
        return this.triggerCondition;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the trigger condition of the action - an action can trigger
     * either when rate is positive, rate is negative or either.
     * @param cond the current trigger condition
     * @see quicktime.std.StdConstants.triggerTimeFwd,
     * quicktime.std.StdConstants.triggerTimeBwd,
     * quicktime.std.StdConstants.triggerTimeEither
     */
    public void setTriggerCondition(int paramInt) {
        this.triggerCondition = paramInt;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return a String representation of this object.
     * @return a String
     */
    public String toString() {
        return super.toString() + "[scale=" + getScale() + ",period=" + getPeriod() + "]";
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the objects are equal or false otherwise.
     * @param obj the object to test
     * @return a boolean
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof PeriodicAction localPeriodicAction)) {
            if ((this.reschedule != localPeriodicAction.reschedule) || (this.lastTime != localPeriodicAction.lastTime) || (this.scale != localPeriodicAction.scale) || (this.period != localPeriodicAction.period) || (this.triggerCondition != localPeriodicAction.triggerCondition)) {
                return false;
            }
            if (this != paramObject)
                return false;
            return true;
        }
        return false;
    }

    private void doMultiplier() {
        this.multiplier = (this.period / this.scale * 1000.0F);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the tickle method when an action becomes active.
     * Subclasses should do their desired actions
     * when this method is called.
     * @param er the actual rate at which the action has been triggered
     * @param t the time in Ticklish.kScale that the action has been triggered
     * @throws quicktime.QTException
     */
    protected abstract void doAction(float paramFloat, int paramInt) throws QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * If after the doAction method has exeucted this method returns true then
     * if the Action has an Actionable object this actionable is triggered. If
     * constraintReached returns false no further action is taken.
     * @return true if the actionable object (if set) should be triggered
     */
    protected abstract boolean constraintReached();
}

