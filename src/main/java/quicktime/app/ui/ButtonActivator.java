
package quicktime.app.ui;

import quicktime.app.event.QTMouseEvent;


/**
 * @deprecated
 */
public class ButtonActivator extends quicktime.app.event.MouseButtonAdapter implements quicktime.app.event.MouseTargetListener {
    /**
     * @deprecated
     */
    protected Object target;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.pressed();
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException),
                                                           localQTButton,
                                                           "pressed");
            }
        }
    }

    /**
     * @deprecated
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.released();
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException),
                                                           localQTButton,
                                                           "pressed");
            }
        }
    }

    /**
     * @deprecated
     */
    public void mouseTargetEntered(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.entered(paramQTMouseEvent);
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException),
                                                           localQTButton,
                                                           "pressed");
            }
        }
    }

    /**
     * @deprecated
     */
    public void mouseTargetExited(QTMouseEvent paramQTMouseEvent) {
        if ((paramQTMouseEvent.getTarget() instanceof QTButton)) {
            QTButton localQTButton = (QTButton) paramQTMouseEvent.getTarget();
            try {
                localQTButton.exited(paramQTMouseEvent);
            } catch (quicktime.QTException localQTException) {
                quicktime.QTRuntimeException.handleOrThrow(new quicktime.QTRuntimeException(localQTException),
                                                           localQTButton,
                                                           "pressed");
            }
        }
    }
}
