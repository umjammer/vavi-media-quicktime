package quicktime.app.event;

import java.util.EventListener;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * The QTEventCaster is used to broadcast QTEvents to registered listeners.
 * There are two characteristics of the broadcaster that are worthy of note:
 * Broadcasters check whether the event is consumed before each invocation of
 * a listeners methods. Broadcasters also check to see if a given listener
 * is interested in an event based on the modifier filter (if defined by the listener)
 * before the event is passed onto the listener.
 */
public class QTEventCaster implements EventListener, QTActionListener, MouseButtonListener, MouseMoveListener, MouseEnterExitListener, MouseTargetListener {

    /**
     * Deprecated.
     */
    protected final EventListener a;

    /**
     * Deprecated.
     */
    protected final EventListener b;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @param a listener-a
     * @param b listener-b
     */
    protected QTEventCaster(EventListener paramEventListener1, EventListener paramEventListener2) {
        this.a = paramEventListener1;
        this.b = paramEventListener2;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds action-listener-a with action-listener-b and
     * returns the resulting multicast listener.
     */
    public static MouseButtonListener add(MouseButtonListener paramMouseButtonListener1, MouseButtonListener paramMouseButtonListener2) {
        return (MouseButtonListener) addListener(paramMouseButtonListener1, paramMouseButtonListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static MouseButtonListener remove(MouseButtonListener paramMouseButtonListener1, MouseButtonListener paramMouseButtonListener2) {
        return (MouseButtonListener) removeListener(paramMouseButtonListener1, paramMouseButtonListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been clicked.
     */
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
        MouseButtonListener localMouseButtonListener = (MouseButtonListener) this.a;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mouseClicked(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseButtonListener = (MouseButtonListener) this.b;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseButtonListener.mouseClicked(paramQTMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button has been pressed.
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        MouseButtonListener localMouseButtonListener = (MouseButtonListener) this.a;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mousePressed(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseButtonListener = (MouseButtonListener) this.b;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mousePressed(paramQTMouseEvent);
    }

    /**
     * Deprecated.
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
     * Invoked when a mouse button has been released.
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        MouseButtonListener localMouseButtonListener = (MouseButtonListener) this.a;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mouseReleased(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseButtonListener = (MouseButtonListener) this.b;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mouseReleased(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     */
    public static MouseMoveListener add(MouseMoveListener paramMouseMoveListener1, MouseMoveListener paramMouseMoveListener2) {
        return (MouseMoveListener) addListener(paramMouseMoveListener1, paramMouseMoveListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static MouseMoveListener remove(MouseMoveListener paramMouseMoveListener1, MouseMoveListener paramMouseMoveListener2) {
        return (MouseMoveListener) removeListener(paramMouseMoveListener1, paramMouseMoveListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the component where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the component).
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        MouseMoveListener localMouseMoveListener = (MouseMoveListener) this.a;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseMoveListener.mouseDragged(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseMoveListener = (MouseMoveListener) this.b;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseMoveListener.mouseDragged(paramQTMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
    public void mouseMoved(QTMouseEvent paramQTMouseEvent) {
        MouseMoveListener localMouseMoveListener = (MouseMoveListener) this.a;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseMoveListener.mouseMoved(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseMoveListener = (MouseMoveListener) this.b;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseMoveListener.mouseMoved(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     */
    public static MouseEnterExitListener add(MouseEnterExitListener paramMouseEnterExitListener1, MouseEnterExitListener paramMouseEnterExitListener2) {
        return (MouseEnterExitListener) addListener(paramMouseEnterExitListener1, paramMouseEnterExitListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static MouseEnterExitListener remove(MouseEnterExitListener paramMouseEnterExitListener1, MouseEnterExitListener paramMouseEnterExitListener2) {
        return (MouseEnterExitListener) removeListener(paramMouseEnterExitListener1, paramMouseEnterExitListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
        MouseEnterExitListener localMouseEnterExitListener = (MouseEnterExitListener) this.a;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseEnterExitListener.mouseEntered(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseEnterExitListener = (MouseEnterExitListener) this.b;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseEnterExitListener.mouseEntered(paramQTMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
        MouseEnterExitListener localMouseEnterExitListener = (MouseEnterExitListener) this.a;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseEnterExitListener.mouseExited(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseEnterExitListener = (MouseEnterExitListener) this.b;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseEnterExitListener.mouseExited(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     */
    public static MouseTargetListener add(MouseTargetListener paramMouseTargetListener1, MouseTargetListener paramMouseTargetListener2) {
        return (MouseTargetListener) addListener(paramMouseTargetListener1, paramMouseTargetListener2);
    }

    /**
     * Deprecated.
     */
    public static MouseTargetListener remove(MouseTargetListener paramMouseTargetListener1, MouseTargetListener paramMouseTargetListener2) {
        return (MouseTargetListener) removeListener(paramMouseTargetListener1, paramMouseTargetListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    public void mouseTargetEntered(QTMouseEvent paramQTMouseEvent) {
        MouseTargetListener localMouseTargetListener = (MouseTargetListener) this.a;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseTargetListener = (MouseTargetListener) this.b;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    public void mouseTargetExited(QTMouseEvent paramQTMouseEvent) {
        MouseTargetListener localMouseTargetListener = (MouseTargetListener) this.a;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseTargetListener.mouseTargetExited(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseTargetListener = (MouseTargetListener) this.b;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseTargetListener.mouseTargetExited(paramQTMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds action-listener-a with action-listener-b and
     * returns the resulting multicast listener.
     */
    public static QTActionListener add(QTActionListener paramQTActionListener1, QTActionListener paramQTActionListener2) {
        return (QTActionListener) addListener(paramQTActionListener1, paramQTActionListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static QTActionListener remove(QTActionListener paramQTActionListener1, QTActionListener paramQTActionListener2) {
        return (QTActionListener) removeListener(paramQTActionListener1, paramQTActionListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Broadcasts the event to any interested QTActionListeners.
     * If the event is consumed it will not be broadcast to any further
     * listeners.
     */
    public void actionPerformed(QTActionEvent paramQTActionEvent) {
        ((QTActionListener) this.a).actionPerformed(paramQTActionEvent);
        if (paramQTActionEvent.isConsumed())
            return;
        ((QTActionListener) this.b).actionPerformed(paramQTActionEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the resulting multicast listener from adding listener-a
     * and listener-b together.
     * If listener-a is null, it returns listener-b;
     * If listener-b is null, it returns listener-a
     * If neither are null, then it creates and returns
     * a new AWTEventMulticaster instance which chains a with b.
     * @param a event listener-a
     * @param b event listener-b
     */
    protected static EventListener addListener(EventListener paramEventListener1, EventListener paramEventListener2) {
        if (paramEventListener1 == null)
            return paramEventListener2;
        if (paramEventListener2 == null)
            return paramEventListener1;
        return new QTEventCaster(paramEventListener1, paramEventListener2);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the resulting multicast listener after removing the
     * old listener from listener-l.
     * If listener-l equals the old listener OR listener-l is null,
     * returns null.
     * Else if listener-l is an instance of AWTEventMulticaster,
     * then it removes the old listener from it.
     * Else, returns listener l.
     * @param l the listener being removed from
     * @param oldl the listener being removed
     */
    protected static EventListener removeListener(EventListener paramEventListener1, EventListener paramEventListener2) {
        if ((paramEventListener1 == paramEventListener2) || (paramEventListener1 == null))
            return null;
        if ((paramEventListener1 instanceof QTEventCaster)) {
            return ((QTEventCaster) paramEventListener1)._remove(paramEventListener2);
        }
        return paramEventListener1;
    }

    EventListener _remove(EventListener paramEventListener) {
        if (paramEventListener == this.a)
            return this.b;
        if (paramEventListener == this.b)
            return this.a;
        EventListener localEventListener1 = removeListener(this.a, paramEventListener);
        EventListener localEventListener2 = removeListener(this.b, paramEventListener);
        if ((localEventListener1 == this.a) && (localEventListener2 == this.b)) {
            return this;
        }
        return addListener(localEventListener1, localEventListener2);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTEventCaster.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
