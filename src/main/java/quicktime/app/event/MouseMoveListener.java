package quicktime.app.event;

/**
 * @deprecated
 * An interface that is used to express an interest in recieving
 * mouse movement events that occur within
 * a given space.
 */
public interface MouseMoveListener extends QTMouseListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button is pressed in a space and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the space where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the space).
     */
    void mouseDragged(QTMouseEvent paramQTMouseEvent);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse button has been moved in a space
     * (with no buttons no down).
     */
    void mouseMoved(QTMouseEvent paramQTMouseEvent);
}
