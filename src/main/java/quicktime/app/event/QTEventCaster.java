package quicktime.app.event;

import java.util.EventListener;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * The QTEventCaster is used to broadcast QTEvents to registered listeners.
 * There are two characteristics of the broadcaster that are worthy of note:
 * Broadcasters check whether the event is consumed before each invocation of
 * a listeners methods. Broadcasters also check to see if a given listener
 * is interested in an event based on the modifier filter (if defined by the listener)
 * before the event is passed onto the listener.
 */
public class QTEventCaster implements EventListener, QTActionListener, MouseButtonListener, MouseMoveListener, MouseEnterExitListener, MouseTargetListener {

    /**
     * @deprecated
     */
    protected final EventListener a;

    /**
     * @deprecated
     */
    protected final EventListener b;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @param a listener-a
     * @param b listener-b
     */
    protected QTEventCaster(EventListener a, EventListener b) {
        this.a = a;
        this.b = b;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Adds action-listener-a with action-listener-b and
     * returns the resulting multicast listener.
     */
    public static MouseButtonListener add(MouseButtonListener mouseButtonListener1, MouseButtonListener mouseButtonListener2) {
        return (MouseButtonListener) addListener(mouseButtonListener1, mouseButtonListener2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static MouseButtonListener remove(MouseButtonListener l, MouseButtonListener oldl) {
        return (MouseButtonListener) removeListener(l, oldl);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been clicked.
     */
    @Override
    public void mouseClicked(QTMouseEvent qtMouseEvent) {
        MouseButtonListener mouseButtonListener = (MouseButtonListener) this.a;
        if (mouseButtonListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseButtonListener.mouseClicked(qtMouseEvent);
        if (qtMouseEvent.isConsumed())
            return;
        mouseButtonListener = (MouseButtonListener) this.b;
        if (mouseButtonListener.matchModifierFilter(qtMouseEvent.getModifiers())) {
            mouseButtonListener.mouseClicked(qtMouseEvent);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button has been pressed.
     */
    @Override
    public void mousePressed(QTMouseEvent event) {
        MouseButtonListener mouseButtonListener = (MouseButtonListener) this.a;
        if (mouseButtonListener.matchModifierFilter(event.getModifiers()))
            mouseButtonListener.mousePressed(event);
        if (event.isConsumed())
            return;
        mouseButtonListener = (MouseButtonListener) this.b;
        if (mouseButtonListener.matchModifierFilter(event.getModifiers()))
            mouseButtonListener.mousePressed(event);
    }

    /**
     * @deprecated
     * @param mods the state of the modifier keys.
     * @return true if a particular listener is interested in receiving the mouse event(s)
     *  it is interested in with the given modifier state, or false if otherwise.
     */
    @Override
    public boolean matchModifierFilter(int mods) {
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button has been released.
     */
    @Override
    public void mouseReleased(QTMouseEvent qtMouseEvent) {
        MouseButtonListener mouseButtonListener = (MouseButtonListener) this.a;
        if (mouseButtonListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseButtonListener.mouseReleased(qtMouseEvent);
        if (qtMouseEvent.isConsumed())
            return;
        mouseButtonListener = (MouseButtonListener) this.b;
        if (mouseButtonListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseButtonListener.mouseReleased(qtMouseEvent);
    }

    /**
     * @deprecated
     */
    public static MouseMoveListener add(MouseMoveListener mouseMoveListener1, MouseMoveListener mouseMoveListener2) {
        return (MouseMoveListener) addListener(mouseMoveListener1, mouseMoveListener2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static MouseMoveListener remove(MouseMoveListener l, MouseMoveListener oldl) {
        return (MouseMoveListener) removeListener(l, oldl);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the component where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the component).
     */
    @Override
    public void mouseDragged(QTMouseEvent event) {
        MouseMoveListener mouseMoveListener = (MouseMoveListener) this.a;
        if (mouseMoveListener.matchModifierFilter(event.getModifiers()))
            mouseMoveListener.mouseDragged(event);
        if (event.isConsumed())
            return;
        mouseMoveListener = (MouseMoveListener) this.b;
        if (mouseMoveListener.matchModifierFilter(event.getModifiers())) {
            mouseMoveListener.mouseDragged(event);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
    @Override
    public void mouseMoved(QTMouseEvent qtMouseEvent) {
        MouseMoveListener mouseMoveListener = (MouseMoveListener) this.a;
        if (mouseMoveListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseMoveListener.mouseMoved(qtMouseEvent);
        if (qtMouseEvent.isConsumed())
            return;
        mouseMoveListener = (MouseMoveListener) this.b;
        if (mouseMoveListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseMoveListener.mouseMoved(qtMouseEvent);
    }

    /**
     * @deprecated
     */
    public static MouseEnterExitListener add(MouseEnterExitListener mouseEnterExitListener1, MouseEnterExitListener mouseEnterExitListener2) {
        return (MouseEnterExitListener) addListener(mouseEnterExitListener1, mouseEnterExitListener2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static MouseEnterExitListener remove(MouseEnterExitListener l, MouseEnterExitListener oldl) {
        return (MouseEnterExitListener) removeListener(l, oldl);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse enters a component.
     */
    @Override
    public void mouseEntered(QTMouseEvent qtMouseEvent) {
        MouseEnterExitListener mouseEnterExitListener = (MouseEnterExitListener) this.a;
        if (mouseEnterExitListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseEnterExitListener.mouseEntered(qtMouseEvent);
        if (qtMouseEvent.isConsumed())
            return;
        mouseEnterExitListener = (MouseEnterExitListener) this.b;
        if (mouseEnterExitListener.matchModifierFilter(qtMouseEvent.getModifiers())) {
            mouseEnterExitListener.mouseEntered(qtMouseEvent);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse exits a component.
     */
    @Override
    public void mouseExited(QTMouseEvent qtMouseEvent) {
        MouseEnterExitListener mouseEnterExitListener = (MouseEnterExitListener) this.a;
        if (mouseEnterExitListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseEnterExitListener.mouseExited(qtMouseEvent);
        if (qtMouseEvent.isConsumed())
            return;
        mouseEnterExitListener = (MouseEnterExitListener) this.b;
        if (mouseEnterExitListener.matchModifierFilter(qtMouseEvent.getModifiers()))
            mouseEnterExitListener.mouseExited(qtMouseEvent);
    }

    /**
     * @deprecated
     */
    public static MouseTargetListener add(MouseTargetListener mouseTargetListener1, MouseTargetListener mouseTargetListener2) {
        return (MouseTargetListener) addListener(mouseTargetListener1, mouseTargetListener2);
    }

    /**
     * @deprecated
     */
    public static MouseTargetListener remove(MouseTargetListener mouseTargetListener1, MouseTargetListener mouseTargetListener2) {
        return (MouseTargetListener) removeListener(mouseTargetListener1, mouseTargetListener2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    @Override
    public void mouseTargetEntered(QTMouseEvent e) {
        MouseTargetListener mouseTargetListener = (MouseTargetListener) this.a;
        if (mouseTargetListener.matchModifierFilter(e.getModifiers()))
            mouseTargetListener.mouseTargetEntered(e);
        if (e.isConsumed())
            return;
        mouseTargetListener = (MouseTargetListener) this.b;
        if (mouseTargetListener.matchModifierFilter(e.getModifiers())) {
            mouseTargetListener.mouseTargetEntered(e);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The target of a MouseResponder is set for events of this kind.
     * @param e the QTMouseEvent that triggered this event
     */
    @Override
    public void mouseTargetExited(QTMouseEvent e) {
        MouseTargetListener mouseTargetListener = (MouseTargetListener) this.a;
        if (mouseTargetListener.matchModifierFilter(e.getModifiers()))
            mouseTargetListener.mouseTargetExited(e);
        if (e.isConsumed())
            return;
        mouseTargetListener = (MouseTargetListener) this.b;
        if (mouseTargetListener.matchModifierFilter(e.getModifiers())) {
            mouseTargetListener.mouseTargetExited(e);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Adds action-listener-a with action-listener-b and
     * returns the resulting multicast listener.
     */
    public static QTActionListener add(QTActionListener qtActionListener1, QTActionListener qtActionListener2) {
        return (QTActionListener) addListener(qtActionListener1, qtActionListener2);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes the old action-listener from action-listener-l and
     * returns the resulting multicast listener.
     * @param l action-listener-l
     * @param oldl the action-listener being removed
     */
    public static QTActionListener remove(QTActionListener l, QTActionListener oldl) {
        return (QTActionListener) removeListener(l, oldl);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Broadcasts the event to any interested QTActionListeners.
     * If the event is consumed it will not be broadcast to any further
     * listeners.
     */
    @Override
    public void actionPerformed(QTActionEvent qtActionEvent) {
        ((QTActionListener) this.a).actionPerformed(qtActionEvent);
        if (qtActionEvent.isConsumed())
            return;
        ((QTActionListener) this.b).actionPerformed(qtActionEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the resulting multicast listener from adding listener-a
     * and listener-b together.
     * If listener-a is null, it returns listener-b;
     * If listener-b is null, it returns listener-a
     * If neither are null, then it creates and returns
     * a new AWTEventMulticaster instance which chains a with b.
     * @param a event listener-a
     * @param b event listener-b
     */
    protected static EventListener addListener(EventListener a, EventListener b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return new QTEventCaster(a, b);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
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
    protected static EventListener removeListener(EventListener l, EventListener oldl) {
        if ((l == oldl) || (l == null))
            return null;
        if ((l instanceof QTEventCaster)) {
            return ((QTEventCaster) l)._remove(oldl);
        }
        return l;
    }

    EventListener _remove(EventListener oldl) {
        if (oldl == this.a)
            return this.b;
        if (oldl == this.b)
            return this.a;
        EventListener eventListener1 = removeListener(this.a, oldl);
        EventListener eventListener2 = removeListener(this.b, oldl);
        if ((eventListener1 == this.a) && (eventListener2 == this.b)) {
            return this;
        }
        return addListener(eventListener1, eventListener2);
    }
}
