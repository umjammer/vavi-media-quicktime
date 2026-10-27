package quicktime.app.actions;

import quicktime.app.time.Ticklish;

/**
 * @deprecated
 * An object that implements this interface is an object that is defined to
 * invoke some action upon its target according to the time constraints
 * that are supplied by the caller of the tickle method. How the targetable
 * interprets and acts upon these time constraints is up to the object itself.
 */
public interface Targetable extends Ticklish {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current target of the Targetable object - this is the object
     * that the Targetable will act upon.
     * @return the current target
     */
    Object getTarget();

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the target.
     * @param target the new target for the action list
     */
    void setTarget(Object paramObject);
}

