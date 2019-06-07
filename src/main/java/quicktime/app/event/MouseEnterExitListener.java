package quicktime.app.event;

/**
 * Deprecated.
 * An interface that is used to express an interest in recieving
 * mouse events where the mouse movement (or drag) enters or exits
 * a given space.
 */
public abstract interface MouseEnterExitListener extends MouseMoveListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse enters a space.
     */
    public abstract void mouseEntered(QTMouseEvent paramQTMouseEvent);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse exits a space.
     */
    public abstract void mouseExited(QTMouseEvent paramQTMouseEvent);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseEnterExitListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
