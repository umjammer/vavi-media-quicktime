package quicktime.app.event;

/**
 * @deprecated
 * An interface that is used to express an interest in recieving
 * mouse events in a space that have to do with the user
 * pressing, clicking or releasing the mouse button.
 */
public interface MouseButtonListener extends QTMouseListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been clicked in a space.
     */
    void mouseClicked(QTMouseEvent qtMouseEvent);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been pressed in a space.
     */
    void mousePressed(QTMouseEvent event);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button has been released where the press occured within the space.
     */
    void mouseReleased(QTMouseEvent qtMouseEvent);
}
