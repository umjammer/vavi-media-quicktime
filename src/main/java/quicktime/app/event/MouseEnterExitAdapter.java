package quicktime.app.event;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * A convenience adapter class for dealing with mouse events that enter or exit a space.
 */
public class MouseEnterExitAdapter extends MouseMoveAdapter implements MouseEnterExitListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse enters a space.
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse exits a space.
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
    }

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseEnterExitAdapter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
