package quicktime.app.event;

/**
 * Deprecated.
 * An interface that is used to express an interest in recieving
 * mouse movement events that occur within
 * a given space.
 */
public abstract interface MouseMoveListener extends QTMouseListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button is pressed in a space and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the space where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the space).
     */
    public abstract void mouseDragged(QTMouseEvent paramQTMouseEvent);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse button has been moved in a space
     * (with no buttons no down).
     */
    public abstract void mouseMoved(QTMouseEvent paramQTMouseEvent);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseMoveListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
