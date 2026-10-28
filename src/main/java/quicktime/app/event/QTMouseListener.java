package quicktime.app.event;

import java.util.EventListener;

/**
 * @deprecated
 * A generic listener for QTMouse events.
 * It supplies the concept that interested listeners can filter their reception
 * of QTMouseEvents based on the state of the modifier keys when the event was created.
 */
public interface QTMouseListener extends EventListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * @param mods the state of the modifier keys.
     * @return true if a particular listener is interested in receiving the mouse event(s)
     *  it is interested in with the given modifier state, or false if otherwise.
     */
    boolean matchModifierFilter(int mods);
}
