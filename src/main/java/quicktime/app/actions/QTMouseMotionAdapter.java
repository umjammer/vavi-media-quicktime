package quicktime.app.actions;

import java.awt.event.MouseEvent;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * The QTMouseMotionAdapter adapter which receives mouse events.
 * The methods in this class are empty;  this class is provided as a
 * convenience for easily creating listeners by extending this class
 * and overriding only the methods of interest.
 */
public abstract class QTMouseMotionAdapter implements QTMouseMotionListener {

    /**
     * @deprecated
     */
    @Override
    public void mouseDragged(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     */
    @Override
    public void mouseMoved(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    @Override
    public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
    }

    /**
     * @deprecated
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    @Override
    public void mouseExitedTarget(MouseEvent paramMouseEvent) {
    }

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }
}

