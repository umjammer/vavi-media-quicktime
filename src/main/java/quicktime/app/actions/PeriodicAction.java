
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.Ticklish;


/**
 * @deprecated
 */
public abstract class PeriodicAction implements Ticklish {
    private static final boolean doPrint = false;

    /**
     * @deprecated
     */
    protected Actionable actor;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected PeriodicAction(int paramInt1, int paramInt2) {
        setScale(paramInt1);
        setPeriod(paramInt2);
    }

    /**
     * @deprecated
     */
    protected boolean reschedule = true;

    private int lastTime;

    private int scale = 1;

    private int period = 1;

    private float multiplier;

    private int triggerCondition = 3;

    /**
     * @deprecated
     */
    public void timeChanged(int paramInt) throws QTException {
        this.lastTime = paramInt;
    }

    /**
     * @deprecated
     */
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

        if (((i < 0) && (paramInt <= j) && ((this.triggerCondition | 0x2) != 0))
            || ((i > 0) && (paramInt >= j) && ((this.triggerCondition | 0x1) != 0))) {

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
     */
    public void setActionable(Actionable paramActionable) {
        this.actor = paramActionable;
    }

    /**
     * @deprecated
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * @deprecated
     */
    public void setPeriod(int paramInt) {
        this.period = paramInt;
        doMultiplier();
    }

    /**
     * @deprecated
     */
    public int getPeriod() {
        return this.period;
    }

    /**
     * @deprecated
     */
    public void setScale(int paramInt) {
        if (paramInt < 1)
            paramInt = 1;
        this.scale = paramInt;
        doMultiplier();
    }

    /**
     * @deprecated
     */
    public int getScale() {
        return this.scale;
    }

    /**
     * @deprecated
     */
    public int getTriggerCondition() {
        return this.triggerCondition;
    }

    /**
     * @deprecated
     */
    public void setTriggerCondition(int paramInt) {
        this.triggerCondition = paramInt;
    }

    /**
     * @deprecated
     */
    public String toString() {
        return super.toString() + "[scale=" + getScale() + ",period=" + getPeriod() + "]";
    }

    /**
     * @deprecated
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof PeriodicAction)) {
            PeriodicAction localPeriodicAction = (PeriodicAction) paramObject;
            if ((this.reschedule != localPeriodicAction.reschedule) || (this.lastTime != localPeriodicAction.lastTime)
                || (this.scale != localPeriodicAction.scale) || (this.period != localPeriodicAction.period)
                || (this.triggerCondition != localPeriodicAction.triggerCondition)) {

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
     */
    protected abstract void doAction(float paramFloat, int paramInt) throws QTException;

    /**
     * @deprecated
     */
    protected abstract boolean constraintReached();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/PeriodicAction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
