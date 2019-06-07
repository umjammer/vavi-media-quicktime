package quicktime.app.event;

/**
 * Deprecated.
 * An interface that is used to express an interest in recieving
 * mouse events in a space that have to do with the user
 * pressing, clicking or releasing the mouse button.
 */
public abstract interface MouseButtonListener extends QTMouseListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been clicked in a space.
     */
    public abstract void mouseClicked(QTMouseEvent paramQTMouseEvent);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been pressed in a space.
     */
    public abstract void mousePressed(QTMouseEvent paramQTMouseEvent);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button has been released where the press occured within the space.
     */
    public abstract void mouseReleased(QTMouseEvent paramQTMouseEvent);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseButtonListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
