
package quicktime.app.event;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public class MouseButtonAdapter implements MouseButtonListener {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public boolean matchModifierFilter(int paramInt) {
        return true;
    }

    /**
     * @deprecated
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * @deprecated
     */
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseButtonAdapter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
