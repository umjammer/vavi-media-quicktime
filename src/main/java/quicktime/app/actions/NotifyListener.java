package quicktime.app.actions;

/**
 * Deprecated.
 * This is a marker interface for those listener objects
 * that are not valid until the source of the events
 * that the object is interested in becomes valid.
 *
 * @see {@link quicktime.app.actions.Notifier}
 */
public abstract interface NotifyListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This registers the Notifier for this listener.
     * If the notifier is not an appropriate Notifier for this listener
     * the setNotifier should return false so the Notifier knows the
     * listener is inappropriate.
     * @param notifier the Notifier that will notify the listener when it
     *  has reached the state that the listener expects.
     * @return true if the Notifier is appropriate for the NotifyListener
     */
    public abstract boolean setNotifier(Notifier paramNotifier);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Notifier will call this method when it has completed its task.
     * When the Notifier calls this method on a NotifyListener the listener
     * can assume that whatever information or state the Notifier possesses
     * is now complete and can take whatever actions are appropriate.
     */
    public abstract void notifyComplete();
}

