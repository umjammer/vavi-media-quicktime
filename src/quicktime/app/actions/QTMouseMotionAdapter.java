
package quicktime.app.actions;

import java.awt.event.MouseEvent;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public abstract class QTMouseMotionAdapter implements QTMouseMotionListener {
    public void mouseDragged(MouseEvent paramMouseEvent) {
    }

    public void mouseMoved(MouseEvent paramMouseEvent) {
    }

    public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
    }

    public void mouseExitedTarget(MouseEvent paramMouseEvent) {
    }

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/QTMouseMotionAdapter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
