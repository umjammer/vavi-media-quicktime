package quicktime.app.actions;

import java.awt.AWTEventMulticaster;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * This is a generic responder for MouseEvents. It places no conditions on the the containing
 * space or target. An application can also register QTMouseListeners and MouseListeners with this responder
 * in which case the GenericResponder will invoke the appropriate method on the registered listeners.
 *
 * @see quicktime.app.event.QTMouseController
 * @deprecated
 */
public class GenericResponder extends MouseResponder {

    /**
     * This is the Transformable target of the Dragger action
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected Object target;

    /**
     * This is the QTDrawable space within which the Dragger's target is enclosed and contained
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected Object space;

    private QTMouseMotionListener qtMouseListener;

    private MouseListener mouseListener;

    private MouseMotionListener mouseMotionListener;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseListener interface.
     *
     * @param modifierKeyMask        the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder#kMouseEvents
     * @since QTJava 6.1
     * @deprecated
     */
    public static GenericResponder asMouseListener(int modifierKeyMask, int modifierTestConditions) {
        return new GenericResponder(modifierKeyMask, modifierTestConditions, 51);
    }

    /**
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseMotionListener interface.
     *
     * @param modifierKeyMask        the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder#kMouseMotionEvents
     * @since QTJava 6.1
     * @deprecated
     */
    public static GenericResponder asMouseMotionListener(int modifierKeyMask, int modifierTestConditions) {
        return new GenericResponder(modifierKeyMask, modifierTestConditions, 12);
    }

    /**
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseListener or MouseMotionListener interfaces.
     *
     * @param modifierKeyMask        the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder#kMouseOrMouseMotionEvents
     * @since QTJava 6.1
     * @deprecated
     */
    public static GenericResponder asMouseOrMouseMotionListener(int modifierKeyMask, int modifierTestConditions) {
        return new GenericResponder(modifierKeyMask, modifierTestConditions, 63);
    }

    /**
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the QTMouseMotionListener interface. This is ideal for doing
     * rollover effects on member objects in a QT space where the objects are stationary as
     * these objects have no need for the MouseExit event.
     *
     * @param modifierKeyMask        the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder#kQTMouseMotionEvents
     * @since QTJava 6.1
     * @deprecated
     */
    public static GenericResponder asQTMouseMotionListener(int modifierKeyMask, int modifierTestConditions) {
        return new GenericResponder(modifierKeyMask, modifierTestConditions, 76);
    }

    /**
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the QTMouseMotionListener interface. This is ideal for doing
     * rollover effects on member objects in a QT space where the objects can be
     * repositioned by dragging by the user as the object is notified by
     * a MouseExit method from the MouseListener interest.
     *
     * @param modifierKeyMask        the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder#kRolloverEvents
     * @since QTJava 6.1
     * @deprecated
     */
    public static GenericResponder asRolloverListener(int modifierKeyMask, int modifierTestConditions) {
        return new GenericResponder(modifierKeyMask, modifierTestConditions, 100);
    }

    /**
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseListener, MouseMotionListener or QTMouseMotionListener interfaces.
     *
     * @param modifierKeyMask        the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder#kAnyMouseEvent
     * @since QTJava 6.1
     * @deprecated
     */
    public static GenericResponder asAnyMouseListener(int modifierKeyMask, int modifierTestConditions) {
        return new GenericResponder(modifierKeyMask, modifierTestConditions, 127);
    }

    /**
     * Set some parameters that will create a GenericResponder that will respond
     * to all mouse events (including mouseMoveTarget events).
     *
     * @param modifierKeyMask        determines which modifier keys (or none) which must
     *                               be depressed for the action to be invoked.
     * @param modifierTestConditions determines the conditions under which the responder is
     *                               activated when the specified modifierKeyMask conditions are met.
     * @param eventTypes             specifies the mouse event types that the responder is interested in.
     * @since QTJava 6.1
     * @deprecated
     */
    public GenericResponder(int modifierKeyMask, int modifierTestConditions, int eventTypes) {
        super(modifierKeyMask, modifierTestConditions, eventTypes);
    }

    /**
     * The MouseController asks its MouseResponder if the space it has been added to is
     * an appropriate Space for the MouseResponder to deal with. If not the MouseResponder
     * subclass that implements this method throws a ClassCastException and the MouseController
     * will not be added to the Space.
     *
     * @param s the Space to which the responder's controller has been added.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void setTargetSpace(Object s) {
        this.space = s;
    }

    /**
     * This method is called by the MouseController when it has returned a target
     * (say from a mousePressed event) and the Controller sets the target that the
     * MouseResponder will then respond to.
     *
     * @param target the object that is the selected target of the MouseEvent
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void setTarget(Object target) {
        this.target = target;
    }

    /**
     * When the Responder is deactivated from the MouseController the controller
     * uses this method to tell the Responder that it no longer has a target
     * to act upon
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void removeTarget() {
        this.target = null;
    }

    /**
     * Returns the Space that the controller has been attached too.
     *
     * @return the container space.
     * @since QTJava 6.1
     * @deprecated
     */
    public Object getSpace() {
        return this.space;
    }

    /**
     * Returns the Target that the controller is currently controlling. This will be
     * null if the mouse event is not targetted, or the Responder is not currently
     * responding to a mouse event.
     *
     * @return the current target or null
     * @since QTJava 6.1
     * @deprecated
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * The MouseController delegates the appropriateness of objects that it can control
     * to the MouseResponder. The MouseResponder returns true if the given object is
     * the type of object that it can control.
     *
     * @param object the object to test
     * @return true if the object is the type of object that the Responder can control
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isAppropriate(Object object) {
        return true;
    }

    /**
     * Invoked when the mouse has been clicked on a found and valid target within the enclosing space.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseClicked(e);
        }
    }

    /**
     * Invoked when the mouse enters a QTDrawable space. There is no target set for events of this kind.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseEntered(e);
        }
    }

    /**
     * Invoked when the mouse exits a QTDrawable space. There is no target set for events of this kind.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseExited(MouseEvent e) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseExited(e);
        }
    }

    /**
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The Target is set for events of this kind.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseEnteredTarget(MouseEvent e) {
        if (this.qtMouseListener != null) {
            this.qtMouseListener.mouseEnteredTarget(e);
        }
    }

    /**
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The Target is set for events of this kind.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseExitedTarget(MouseEvent e) {
        if (this.qtMouseListener != null) {
            this.qtMouseListener.mouseExitedTarget(e);
        }
    }

    /**
     * Invoked when the mouse button has been moved over a space. There is no target set for events of this kind.
     * (with no buttons no down).
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.qtMouseListener != null)
            this.qtMouseListener.mouseMoved(e);
        if (this.mouseMotionListener != null) {
            this.mouseMotionListener.mouseMoved(e);
        }
    }

    /**
     * Invoked when the mouse has been pressed on a found and valid target within the enclosing space.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (this.mouseListener != null) {
            this.mouseListener.mousePressed(e);
        }
    }

    /**
     * Invoked when the mouse has been dragged on a found and valid target within the enclosing space.
     * This method is only called if the responder has been activated by a mouse pressed event.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.qtMouseListener != null)
            this.qtMouseListener.mouseDragged(e);
        if (this.mouseMotionListener != null) {
            this.mouseMotionListener.mouseDragged(e);
        }
    }

    /**
     * Invoked when the mouse has been released after being pressed on a found and valid target within the enclosing space.
     *
     * @param e the MouseEvent that triggered this event
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseReleased(e);
        }
    }

    /**
     * Registers a QTMouseMotionListener to this responder. The listener will be invoked with any of the
     * mouse events the responder is invoked by. If the Responder has not been created to listen to these
     * type of events then the listener will not be registered. The application can test this
     * by calling:
     * <pre>
     * myGenericResponder.isInterested (MouseResponder.kQTMouseMotionEvents)
     * </pre>
     * The interest in mouseMotionEvents is a contained interest of QTMouseMotion events, and is thus
     * the minimal requirement to register this kind of listener.
     *
     * @param ql the new QTMouseMotionListener
     * @since QTJava 6.1
     * @deprecated
     */
    public void addQTMouseMotionListener(QTMouseMotionListener ql) {
        if (isInterested(76)) {
            this.qtMouseListener = GREventMulticaster.add(this.qtMouseListener, ql);
        }
    }

    /**
     * Removes the specified QTMouseMotionListener from the GenericResponder.
     *
     * @param ql the QTMouseMotionListener to remove
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeQTMouseMotionListener(QTMouseMotionListener ql) {
        this.qtMouseListener = GREventMulticaster.remove(this.qtMouseListener, ql);
    }

    /**
     * Registers a MouseListener to this responder. The listener will be invoked with any of the
     * mouse events the responder is invoked by. If the Responder has not been created to listen to these
     * type of events then the listener will not be registered. The application can test this
     * by calling:
     * <pre>
     * myGenericResponder.isInterested (MouseResponder.kMouseEvents)
     * </pre>
     *
     * @param ml the new MouseListener
     * @since QTJava 6.1
     * @deprecated
     */
    public void addMouseListener(MouseListener ml) {
        if (isInterested(51)) {
            this.mouseListener = GREventMulticaster.add(this.mouseListener, ml);
        }
    }

    /**
     * Removes the specified MouseListener from the GenericResponder.
     *
     * @param ml the MouseListener to remove
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeMouseListener(MouseListener ml) {
        this.mouseListener = GREventMulticaster.remove(this.mouseListener, ml);
    }

    /**
     * Registers a MouseListener to this responder. The listener will be invoked with any of the
     * mouse events the responder is invoked by. If the Responder has not been created to listen to these
     * type of events then the listener will not be registered. The application can test this
     * by calling:
     * <pre>
     * myGenericResponder.isInterested (MouseResponder.kMouseMotionEvents)
     * </pre>
     *
     * @param ml the new MouseListener
     * @since QTJava 6.1
     * @deprecated
     */
    public void addMouseMotionListener(MouseMotionListener ml) {
        if (isInterested(12)) {
            this.mouseMotionListener = GREventMulticaster.add(this.mouseMotionListener, ml);
        }
    }

    /**
     * Removes the specified MouseListener from the GenericResponder.
     *
     * @param ml the MouseListener to remove
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeMouseMotionListener(MouseMotionListener ml) {
        this.mouseMotionListener = GREventMulticaster.remove(this.mouseMotionListener, ml);
    }

    static class GREventMulticaster extends AWTEventMulticaster implements QTMouseMotionListener {

        GREventMulticaster(EventListener eventListener1, EventListener eventListener2) {
            super(eventListener1, eventListener2);
        }

        public static QTMouseMotionListener add(QTMouseMotionListener qtMouseMotionListener1, QTMouseMotionListener qtMouseMotionListener2) {
            if (qtMouseMotionListener1 == null)
                return qtMouseMotionListener2;
            if (qtMouseMotionListener2 == null)
                return qtMouseMotionListener1;
            return new GREventMulticaster(qtMouseMotionListener1, qtMouseMotionListener2);
        }

        public static QTMouseMotionListener remove(QTMouseMotionListener qtMouseMotionListener1, QTMouseMotionListener qtMouseMotionListener2) {
            if ((qtMouseMotionListener1 == qtMouseMotionListener2) || (qtMouseMotionListener1 == null))
                return null;
            if ((qtMouseMotionListener1 instanceof GREventMulticaster)) {
                return (QTMouseMotionListener) ((GREventMulticaster) qtMouseMotionListener1).remove(qtMouseMotionListener2);
            }
            return qtMouseMotionListener1;
        }

        @Override
        public void mouseEnteredTarget(MouseEvent e) {
            ((QTMouseMotionListener) this.a).mouseEnteredTarget(e);
            ((QTMouseMotionListener) this.b).mouseEnteredTarget(e);
        }

        @Override
        public void mouseExitedTarget(MouseEvent e) {
            ((QTMouseMotionListener) this.a).mouseExitedTarget(e);
            ((QTMouseMotionListener) this.b).mouseExitedTarget(e);
        }
    }
}
