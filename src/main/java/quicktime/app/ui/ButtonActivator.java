package quicktime.app.ui;

import quicktime.app.event.QTMouseEvent;

/**
 * Deprecated.
 * The ButtonActivator class responds only to the QTMouseEvents on the QTButton object.
 */
public class ButtonActivator extends quicktime.app.event.MouseButtonAdapter implements quicktime.app.event.MouseTargetListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This is the Transformable target of the action
     */
    protected Object target;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been pressed on a found and valid QTButton within the enclosing space.
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.pressed();
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException), localQTButton, "pressed");
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been released on a found and valid QTButton within the enclosing space.
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.released();
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException), localQTButton, "pressed");
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event enters a QTButton within an enclosing space. The QTButton is set for events of this kind.
     * @param event the QTMouseEvent that triggered this event
     */
    public void mouseTargetEntered(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.entered(paramQTMouseEvent);
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException), localQTButton, "pressed");
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event exits the QTButton within an enclosing space. The QTButton is set for events of this kind.
     * @param event the QTMouseEvent that triggered this event
     */
    public void mouseTargetExited(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.exited(paramQTMouseEvent);
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException), localQTButton, "pressed");
            }
        }
    }
}
