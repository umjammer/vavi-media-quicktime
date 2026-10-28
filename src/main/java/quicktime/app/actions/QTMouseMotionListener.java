package quicktime.app.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @deprecated
 * This interface extends the MouseMotionListener interface to
 * notify its listeners of mouse motion events that enter or exit a particular
 * target.
 */
public interface QTMouseMotionListener extends MouseMotionListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    void mouseEnteredTarget(MouseEvent e);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    void mouseExitedTarget(MouseEvent e);
}

