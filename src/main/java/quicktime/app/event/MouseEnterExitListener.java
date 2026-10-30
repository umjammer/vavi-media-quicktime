package quicktime.app.event;

/**
 * @deprecated
 * An interface that is used to express an interest in recieving
 * mouse events where the mouse movement (or drag) enters or exits
 * a given space.
 */
public interface MouseEnterExitListener extends MouseMoveListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse enters a space.
     */
    void mouseEntered(QTMouseEvent qtMouseEvent);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse exits a space.
     */
    void mouseExited(QTMouseEvent qtMouseEvent);
}
