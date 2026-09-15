package quicktime.app.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Deprecated.
 * This interface extends the MouseMotionListener interface to
 * notify its listeners of mouse motion events that enter or exit a particular
 * target.
 */
public abstract interface QTMouseMotionListener extends MouseMotionListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    public abstract void mouseEnteredTarget(MouseEvent paramMouseEvent);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    public abstract void mouseExitedTarget(MouseEvent paramMouseEvent);
}

