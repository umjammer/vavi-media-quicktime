package quicktime.app.time;

import quicktime.QTException;

/**
 * The Ticklish interface is primarily implemented by objects that are attached
 * to a Timer. The Timer can either be subclassed or provided with a target object
 * that implements the Ticklish interface.
 *
 * If the Timer has a target then when the next scheduled time value
 * is reached the target will be tickled. The time value that is given to the
 * the tickle method when it is invoked is expressed in the scale of this interface.
 * If a time state of the Timer is changed, through the time jumping from one time to
 * another or a rate change from positive to negative or vice versa, then the timer will
 * call the timeChanged method with the new time, also expressed in the scale of the
 * Ticklish.kScale value.
 * @see {@link quicktime.app.time.Timer}
 */
public abstract interface Ticklish {

    /**
     * The scale of the time values that are given to the ticklish object
     */
    public static final int kScale = 1000;

    /**
     * Invoked by the timer when a time condition of its time base has changed.
     */
    public abstract void timeChanged(int paramInt) throws QTException;

    /**
     * The Timer invokes the tickle method when the invocation constraints of
     * the Timer are reached.
     * @param er the effective rate of the Timer when the time condition matches
     * @param time the time that the Timer invoked the tickle method.
     * @return true if the Ticklish object should receive a consequent tickle, or false
     *  if the Ticklish object should not be tickled again.
     * @return {@link quicktime.QTException}
     */
    public abstract boolean tickle(float paramFloat, int paramInt) throws QTException;
}

