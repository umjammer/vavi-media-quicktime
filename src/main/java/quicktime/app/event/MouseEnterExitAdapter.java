package quicktime.app.event;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * A convenience adapter class for dealing with mouse events that enter or exit a space.
 */
public class MouseEnterExitAdapter extends MouseMoveAdapter implements MouseEnterExitListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse enters a space.
     */
    @Override
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse exits a space.
     */
    @Override
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
    }

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }
}
