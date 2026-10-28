package quicktime.app.event;

/**
 * @deprecated
 * An interface for expressing an interest in mouse movement events
 * within a given space where the mouse movement is based around
 * a selected object(s)) within that space.
 */
public interface MouseTargetListener extends QTMouseListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved or mouseDragged event enters a Target within an enclosing space.
     * The target of the QTMouseEvent is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    void mouseTargetEntered(QTMouseEvent e);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event exits a Target within an enclosing space.
     * The target of the QTMouseEvent is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    void mouseTargetExited(QTMouseEvent e);
}
