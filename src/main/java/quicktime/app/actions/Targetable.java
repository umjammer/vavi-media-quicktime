package quicktime.app.actions;

import quicktime.app.time.Ticklish;

/**
 * Deprecated.
 * An object that implements this interface is an object that is defined to
 * invoke some action upon its target according to the time constraints
 * that are supplied by the caller of the tickle method. How the targetable
 * interprets and acts upon these time constraints is up to the object itself.
 */
public abstract interface Targetable extends Ticklish {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current target of the Targetable object - this is the object
     * that the Targetable will act upon.
     * @return the current target
     */
    public abstract Object getTarget();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set the target.
     * @param target the new target for the action list
     */
    public abstract void setTarget(Object paramObject);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/Targetable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
