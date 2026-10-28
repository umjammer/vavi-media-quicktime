package quicktime.app.event;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * A convenience adapter class for dealing with Drag events.
 */
public class DragAdapter implements MouseMoveListener, MouseButtonListener {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * An event is only passed on to interested listeners if those
     * listeners return true to this filter call. By default this
     * method returns true.
     * @param mods the state of the modifier keys.
     * @return true if a particular listener is interested in receiving the mouse event(s)
     *  it is interested in with the given modifier state, or false if otherwise.
     */
    @Override
    public boolean matchModifierFilter(int mods) {
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse button has been moved in a space
     * (with no buttons no down).
     */
    @Override
    public void mouseMoved(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button is pressed in a space and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the space where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the space).
     */
    @Override
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button has been released where the press occured within the space.
     */
    @Override
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been pressed in a space.
     */
    @Override
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been clicked in a space.
     */
    @Override
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
    }
}
