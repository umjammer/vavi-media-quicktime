package quicktime.app.actions;

/**
 * @deprecated
 * This interface provides a way for Actions to trigger actions on objects when their particular
 * constraining condition has been reached.
 * @see quicktime.app.actions.BounceAction,
 * {@link quicktime.app.actions.Dragger}
 */
public interface Actionable {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the action that the actionable object is attached to.
     */
    void trigger();
}

