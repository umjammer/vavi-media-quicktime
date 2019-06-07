package quicktime.app.event;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * A convenience adapter class for dealing with Drag events.
 */
public class DragAdapter implements MouseMoveListener, MouseButtonListener {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * An event is only passed on to interested listeners if those
     * listeners return true to this filter call. By default this
     * method returns true.
     * @param mods the state of the modifier keys.
     * @return true if a particular listener is interested in receiving the mouse event(s)
     *  it is interested in with the given modifier state, or false if otherwise.
     */
    public boolean matchModifierFilter(int paramInt) {
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse button has been moved in a space
     * (with no buttons no down).
     */
    public void mouseMoved(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button is pressed in a space and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the space where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the space).
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button has been released where the press occured within the space.
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been pressed in a space.
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been clicked in a space.
     */
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /DragAdapter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
