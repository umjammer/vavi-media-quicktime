package quicktime.app.ui;

import quicktime.app.event.QTMouseEvent;


/**
 * The ButtonActivator class responds only to the QTMouseEvents on the QTButton object.
 *
 * @deprecated
 */
public class ButtonActivator extends quicktime.app.event.MouseButtonAdapter implements quicktime.app.event.MouseTargetListener {

    /**
     * This is the Transformable target of the action
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected Object target;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Invoked when the mouse has been pressed on a found and valid QTButton within the enclosing space.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mousePressed(QTMouseEvent event) {
        if ((event.getTarget() instanceof QTButton qtButton)) {
            try {
                qtButton.pressed();
            } catch (quicktime.QTException e) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(e), qtButton, "pressed");
            }
        }
    }

    /**
     * Invoked when the mouse has been released on a found and valid QTButton within the enclosing space.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseReleased(QTMouseEvent qtMouseEvent) {
        if ((qtMouseEvent.getTarget() instanceof QTButton qtButton)) {
            try {
                qtButton.released();
            } catch (quicktime.QTException e) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(e), qtButton, "pressed");
            }
        }
    }

    /**
     * Invoked when a mouseMoved event enters a QTButton within an enclosing space. The QTButton is set for events of this kind.
     *
     * @param event the QTMouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseTargetEntered(QTMouseEvent event) {
        if ((event.getTarget() instanceof QTButton qtButton)) {
            try {
                qtButton.entered(event);
            } catch (quicktime.QTException e) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(e), qtButton, "pressed");
            }
        }
    }

    /**
     * Invoked when a mouseMoved event exits the QTButton within an enclosing space. The QTButton is set for events of this kind.
     *
     * @param event the QTMouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseTargetExited(QTMouseEvent event) {
        if ((event.getTarget() instanceof QTButton qtButton)) {
            try {
                qtButton.exited(event);
            } catch (quicktime.QTException e) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(e), qtButton, "pressed");
            }
        }
    }
}
