package quicktime.app.actions;

/**
 * Deprecated.
 * This interface provides a way for Actions to trigger actions on objects when their particular
 * constraining condition has been reached.
 * @see {@link quicktime.app.actions.BounceAction},
 * {@link quicktime.app.actions.Dragger}
 */
public abstract interface Actionable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the action that the actionable object is attached to.
     */
    public abstract void trigger();
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/Actionable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
