package quicktime.app.event;

/**
 * Deprecated.
 * An interface for expressing an interest in mouse movement events
 * within a given space where the mouse movement is based around
 * a selected object(s)) within that space.
 */
public abstract interface MouseTargetListener extends QTMouseListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved or mouseDragged event enters a Target within an enclosing space.
     * The target of the QTMouseEvent is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    public abstract void mouseTargetEntered(QTMouseEvent paramQTMouseEvent);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event exits a Target within an enclosing space.
     * The target of the QTMouseEvent is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    public abstract void mouseTargetExited(QTMouseEvent paramQTMouseEvent);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseTargetListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
