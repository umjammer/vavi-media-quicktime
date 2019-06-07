package quicktime.app.event;

import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * A convenience class for dealing with mouse movement events
 * within a given space where the mouse movement is based around
 * a selected object(s)) within that space.
 */
public class MouseTargetAdapter implements MouseTargetListener {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * An event is only passed on to interested listeners if those
     * listeners return true to this filter call. By default this
     * method returns true.
     * @param mods the state of the modifier keys.
     * @return true if a particular listener is interested in receiving the mouse event(s)
     *  it is interested in with the given modifier state, or false if otherwise.
     */
    public boolean matchModifierFilter(int paramInt) {
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event exits a Target within an enclosing space.
     * The target of the QTMouseEvent is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    public void mouseTargetExited(QTMouseEvent paramQTMouseEvent) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved or mouseDragged event enters a Target within an enclosing space.
     * The target of the QTMouseEvent is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    public void mouseTargetEntered(QTMouseEvent paramQTMouseEvent) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /MouseTargetAdapter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
