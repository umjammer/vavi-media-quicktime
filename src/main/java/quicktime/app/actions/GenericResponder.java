package quicktime.app.actions;

import java.awt.AWTEventMulticaster;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * This is a generic responder for MouseEvents. It places no conditions on the the containing
 * space or target. An application can also register QTMouseListeners and MouseListeners with this responder
 * in which case the GenericResponder will invoke the appropriate method on the registered listeners.
 * @see {@link quicktime.app.event.QTMouseController}
 */
public class GenericResponder extends MouseResponder {

    /**
     * Deprecated.
     * This is the Transformable target of the Dragger action * @deprecated since QTJava 6.1
     */
    protected Object target;

    /**
     * Deprecated.
     * This is the QTDrawable space within which the Dragger's target is enclosed and contained* @deprecated since QTJava 6.1
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseListener interface.
     * @param modifierKeyMask the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder.kMouseEvents
     */
    public static GenericResponder asMouseListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 51);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseMotionListener interface.
     * @param modifierKeyMask the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder.kMouseMotionEvents
     */
    public static GenericResponder asMouseMotionListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 12);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseListener or MouseMotionListener interfaces.
     * @param modifierKeyMask the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder.kMouseOrMouseMotionEvents
     */
    public static GenericResponder asMouseOrMouseMotionListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 63);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the QTMouseMotionListener interface. This is ideal for doing
     * rollover effects on member objects in a QT space where the objects are stationary as
     * these objects have no need for the MouseExit event.
     * @param modifierKeyMask the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder.kQTMouseMotionEvents
     */
    public static GenericResponder asQTMouseMotionListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 76);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the QTMouseMotionListener interface. This is ideal for doing
     * rollover effects on member objects in a QT space where the objects can be
     * repositioned by dragging by the user as the object is notified by
     * a MouseExit method from the MouseListener interest.
     * @param modifierKeyMask the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder.kRolloverEvents
     */
    public static GenericResponder asRolloverListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 100);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a GenericResponder that is interested in mouse events that
     * are covered by the MouseListener, MouseMotionListener or QTMouseMotionListener interfaces.
     * @param modifierKeyMask the state of the modifier keys to activate this responder
     * @param modifierTestConditions the match criteria that are applied to the modifier keys
     * @return a GenericResponder
     * @see MouseResponder.kAnyMouseEvent
     */
    public static GenericResponder asAnyMouseListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 127);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set some parameters that will create a GenericResponder that will respond
     * to all mouse events (including mouseMoveTarget events).
     * @param modifierKeyMask determines which modifier keys (or none) which must
     *  be depressed for the action to be invoked.
     * @param modifierTestConditions determines the conditions under which the responder is
     *  activated when the specified modifierKeyMask conditions are met.
     * @param eventTypes specifies the mouse event types that the responder is interested in.
     */
    public GenericResponder(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, paramInt2, paramInt3);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The MouseController asks its MouseResponder if the space it has been added to is
     * an appropriate Space for the MouseResponder to deal with. If not the MouseResponder
     * subclass that implements this method throws a ClassCastException and the MouseController
     * will not be added to the Space.
     * @param s the Space to which the responder's controller has been added.
     */
    protected void setTargetSpace(Object paramObject) {
        this.space = paramObject;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the MouseController when it has returned a target
     * (say from a mousePressed event) and the Controller sets the target that the
     * MouseResponder will then respond to.
     * @param target the object that is the selected target of the MouseEvent
     */
    protected void setTarget(Object paramObject) {
        this.target = paramObject;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When the Responder is deactivated from the MouseController the controller
     * uses this method to tell the Responder that it no longer has a target
     * to act upon
     */
    protected void removeTarget() {
        this.target = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Space that the controller has been attached too.
     * @return the container space.
     */
    public Object getSpace() {
        return this.space;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Target that the controller is currently controlling. This will be
     * null if the mouse event is not targetted, or the Responder is not currently
     * responding to a mouse event.
     * @return the current target or null
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The MouseController delegates the appropriateness of objects that it can control
     * to the MouseResponder. The MouseResponder returns true if the given object is
     * the type of object that it can control.
     * @param object the object to test
     * @return true if the object is the type of object that the Responder can control
     */
    public boolean isAppropriate(Object paramObject) {
        return true;
    }

    /**
     * @deprecated
     */
    public void mouseClicked(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseClicked(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseEntered(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseEntered(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseExited(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseExited(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null) {
            this.qtMouseListener.mouseEnteredTarget(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseExitedTarget(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null) {
            this.qtMouseListener.mouseExitedTarget(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseMoved(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null)
            this.qtMouseListener.mouseMoved(paramMouseEvent);
        if (this.mouseMotionListener != null) {
            this.mouseMotionListener.mouseMoved(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mousePressed(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null)
            this.qtMouseListener.mouseDragged(paramMouseEvent);
        if (this.mouseMotionListener != null) {
            this.mouseMotionListener.mouseDragged(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseReleased(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseReleased(paramMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Registers a QTMouseMotionListener to this responder. The listener will be invoked with any of the
     * mouse events the responder is invoked by. If the Responder has not been created to listen to these
     * type of events then the listener will not be registered. The application can test this
     * by calling:
     *
     * myGenericResponder.isInterested (MouseResponder.kQTMouseMotionEvents)
     *
     * The intereste in mouseMotionEvents is a contained interest of QTMouseMotion events, and is thus
     * the minimal requirement to register this kind of listener.
     * @param ql the new QTMouseMotionListener
     */
    public void addQTMouseMotionListener(QTMouseMotionListener paramQTMouseMotionListener) {
        if (isInterested(76)) {
            this.qtMouseListener = GREventMulticaster.add(this.qtMouseListener, paramQTMouseMotionListener);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes the specified QTMouseMotionListener from the GenericResponder.
     * @param ql the QTMouseMotionListener to remove
     */
    public void removeQTMouseMotionListener(QTMouseMotionListener paramQTMouseMotionListener) {
        this.qtMouseListener = GREventMulticaster.remove(this.qtMouseListener, paramQTMouseMotionListener);
    }

    /**
     * @deprecated
     */
    public void addMouseListener(MouseListener paramMouseListener) {
        if (isInterested(51)) {
            this.mouseListener = GREventMulticaster.add(this.mouseListener, paramMouseListener);
        }
    }

    /**
     * @deprecated
     */
    public void removeMouseListener(MouseListener paramMouseListener) {
        this.mouseListener = GREventMulticaster.remove(this.mouseListener, paramMouseListener);
    }

    /**
     * @deprecated
     */
    public void addMouseMotionListener(MouseMotionListener paramMouseMotionListener) {
        if (isInterested(12)) {
            this.mouseMotionListener = GREventMulticaster.add(this.mouseMotionListener, paramMouseMotionListener);
        }
    }

    /**
     * @deprecated
     */
    public void removeMouseMotionListener(MouseMotionListener paramMouseMotionListener) {
        this.mouseMotionListener = GREventMulticaster.remove(this.mouseMotionListener, paramMouseMotionListener);
    }

    static class GREventMulticaster extends AWTEventMulticaster implements QTMouseMotionListener {

        GREventMulticaster(EventListener paramEventListener1, EventListener paramEventListener2) {
            super(paramEventListener1, paramEventListener2);
        }

        public static QTMouseMotionListener add(QTMouseMotionListener paramQTMouseMotionListener1, QTMouseMotionListener paramQTMouseMotionListener2) {
            if (paramQTMouseMotionListener1 == null)
                return paramQTMouseMotionListener2;
            if (paramQTMouseMotionListener2 == null)
                return paramQTMouseMotionListener1;
            return new GREventMulticaster(paramQTMouseMotionListener1, paramQTMouseMotionListener2);
        }

        public static QTMouseMotionListener remove(QTMouseMotionListener paramQTMouseMotionListener1, QTMouseMotionListener paramQTMouseMotionListener2) {
            if ((paramQTMouseMotionListener1 == paramQTMouseMotionListener2) || (paramQTMouseMotionListener1 == null))
                return null;
            if ((paramQTMouseMotionListener1 instanceof GREventMulticaster)) {
                return (QTMouseMotionListener) ((GREventMulticaster) paramQTMouseMotionListener1).remove(paramQTMouseMotionListener2);
            }
            return paramQTMouseMotionListener1;
        }

        public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
            ((QTMouseMotionListener) this.a).mouseEnteredTarget(paramMouseEvent);
            ((QTMouseMotionListener) this.b).mouseEnteredTarget(paramMouseEvent);
        }

        public void mouseExitedTarget(MouseEvent paramMouseEvent) {
            ((QTMouseMotionListener) this.a).mouseExitedTarget(paramMouseEvent);
            ((QTMouseMotionListener) this.b).mouseExitedTarget(paramMouseEvent);
        }
    }
}
