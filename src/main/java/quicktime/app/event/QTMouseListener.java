package quicktime.app.event;

import java.util.EventListener;

/**
 * Deprecated.
 * A generic listener for QTMouse events.
 * It supplies the concept that interested listeners can filter their reception
 * of QTMouseEvents based on the state of the modifier keys when the event was created.
 */
public abstract interface QTMouseListener extends EventListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @param mods the state of the modifier keys.
     * @return true if a particular listener is interested in receiving the mouse event(s)
     *  it is interested in with the given modifier state, or false if otherwise.
     */
    public abstract boolean matchModifierFilter(int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTMouseListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
