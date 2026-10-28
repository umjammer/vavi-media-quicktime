package quicktime.app.actions;

import java.awt.event.MouseEvent;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * This class is an abstract class that is used to provide a base level functionality for
 * objects that respond to Mouse events.
 *
 * A responder responds to a mouse event based on three conditions being met.
 *
 * Modifier keys (or none) that determine if the responder is activated
 * Matching conditions for the specified modifier keys - the default is kExactMatch
 * Events that the particular responder is interested in. Some responders may only
 * be interested in particular mouse events, for instance the Dragger is only interested in
 * kPressReleaseEvent | kDragEvent
 *
 * A responder is associated with a controller and when the event and modifiers are matched
 * the controller activates the responder and sends events to the responder whilst the
 * responder is active.
 *
 * A responder in turn has a target for many mouse events (click, press, drag, release). In these
 * event types the responder is only activated if the controller finds a target where the mouse was
 * pressed. A mouseRelease event may NOT have occured when the mouse was over the selected target, whereas
 * a mouseClick event indicates that the mouse was pressed and released over a particular target
 * within the system mouse click time.
 *
 * Mouse move, enter and exit events do not have a target - they relate to the space
 * within which the controller has a declared interest.
 * @see quicktime.app.event.QTMouseController
 */
public abstract class MouseResponder {

    /**
     * @deprecated
     */
    public static final int kNoModifiersMask = 0;

    /**
     * @deprecated
     */
    public static final int kAnyModifiersMask = 0;

    /**
     * @deprecated
     */
    public static final int kNoModifiers = -1;

    /**
     * @deprecated
     */
    public static final int kAnyModifiers = -2;

    /**
     * @deprecated
     */
    public static final int kAnyOneOfModifiers = -3;

    /**
     * @deprecated
     */
    public static final int kModifiersDisregarded = -4;

    /**
     * @deprecated
     */
    public static final int kModifiersExactMatch = -5;

    /**
     * @deprecated
     */
    public static final int kSpecifiedModifiersOrNone = -6;

    /**
     * @deprecated
     */
    public static final int kClickEvents = 1;

    /**
     * @deprecated
     */
    public static final int kPressReleaseEvents = 2;

    /**
     * @deprecated
     */
    public static final int kDragEvents = 4;

    /**
     * @deprecated
     */
    public static final int kMoveEvents = 8;

    /**
     * @deprecated
     */
    public static final int kEnterEvents = 16;

    /**
     * @deprecated
     */
    public static final int kExitEvents = 32;

    /**
     * @deprecated
     */
    public static final int kTargetMoveEvents = 64;

    /**
     * @deprecated
     */
    public static final int kMouseEvents = 51;

    /**
     * @deprecated
     */
    public static final int kMouseMotionEvents = 12;

    /**
     * @deprecated
     */
    public static final int kQTMouseMotionEvents = 76;

    /**
     * @deprecated
     */
    public static final int kRolloverEvents = 100;

    /**
     * @deprecated
     */
    public static final int kMouseOrMouseMotionEvents = 63;

    /**
     * @deprecated
     */
    public static final int kAnyMouseEvent = 127;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * You pass in to this constructor the modifier keys and the conditions of the modifier test
     * that will trigger the activation of the responder object.
     * @param modifierKeyMask the modifier key's mask
     * @param modifierTestConditions the test conditions under which the modifier mask is tested
     * @param eventTypes the mouse events the responder will respond to. This is a read-only
     *  property of the responder and determines the kinds of mouse events the responder responds to.
     */
    protected MouseResponder(int modifierKeyMask, int modifierTestConditions, int eventTypes) {
        this.modifierKeyMask = modifierKeyMask;
        this.modifierTestConditions = modifierTestConditions;
        this.eventInterests = eventTypes;
    }

    private int activationEvent = 0;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mask that is used to test which modifier keys will be regarded as valid
     */
    public int modifierKeyMask;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The conditions of the modifier keys that will invoke an event
     */
    public int modifierTestConditions;

    private int eventInterests = 0;

    MouseController controller;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the MouseController that the responder is attached to.
     * @return a MouseController
     */
    public MouseController getMouseController() {
        return this.controller;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the repsonder is interested in the specified event.
     * @param eventType to test interest
     * @return true if interested, false if not
     */
    public boolean isInterested(int eventType) {
        return (this.eventInterests & eventType) != 0;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the repsonder is interested in the specified event.
     * @param eventType to test interest
     * @param eventModifiers modifier keys of mouse event
     * @return true if interested, false if not
     */
    public boolean isInterested(int eventType, int eventModifiers) {
        return ((this.eventInterests & eventType) != 0) && (doModifiersTest(eventModifiers));
    }

    /**
     * @deprecated
     */
    private boolean doModifiersTest(int paramInt) {
        paramInt &= 0xF;
        return switch (this.modifierTestConditions) {
            case -6 -> (paramInt == 0) || (paramInt == this.modifierKeyMask);
            case -3 -> (paramInt & this.modifierKeyMask) != 0;
            case -4 -> true;
            case -2 -> paramInt != 0;
            case -1 -> paramInt == 0;
            case -5 -> paramInt == this.modifierKeyMask;
            default -> false;
        };
    }

    void activate(int paramInt) {
        this.activationEvent = paramInt;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the event which activated the responder. If
     * the responder is inactive this will return 0.
     * @return the event type.
     */
    public int activationEventType() {
        return this.activationEvent;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the responder is currently active.
     * @return true if active, false if not
     */
    public boolean isActive() {
        return this.activationEvent != 0;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A subclass can call this method to deactivate a responder.
     * It will not cancel a responder from becoming active again.
     * For instance in drags if the object should no longer be dragged
     * but the user is still going to broadcast mouseDrag events then
     * you can deactivate the responder to ensure that the target will
     * no longer be dragged.
     */
    protected void deactivate() {
        this.activationEvent = 0;
        removeTarget();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The MouseController asks its MouseResponder if the space it has been added to is
     * an appropriate Space for the MouseResponder to deal with. If not the MouseResponder
     * subclass that implements this method throws a ClassCastException and the MouseController
     * will not be added to the Space.
     * @param s the Space to which the responder's controller has been added.
     */
    protected abstract void setTargetSpace(Object s);

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the MouseController when it has returned a target
     * (say from a mousePressed event) and the Controller sets the target that the
     * MouseResponder will then respond to.
     * @param target the object that is the selected target of the MouseEvent
     */
    protected abstract void setTarget(Object target);

    /**
     * @deprecated
     * @since QTJava 6.1
     * When the Responder is deactivated from the MouseController the controller
     * uses this method to tell the Responder that it no longer has a target
     * to act upon
     */
    protected abstract void removeTarget();

    /**
     * @deprecated
     * @since QTJava 6.1
     * The MouseController delegates the appropriateness of objects that it can control
     * to the MouseResponder. The MouseResponder returns true if the given object is
     * the type of object that it can control.
     * @param object the object to test
     * @return true if the object is the type of object that the Responder can control
     */
    public abstract boolean isAppropriate(Object object);

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been clicked on a found and valid target within the enclosing space.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse enters a QTDrawable space. There is no target set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse exits a QTDrawable space. There is no target set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event enters a Target within an enclosing space. The Target is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseEnteredTarget(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouseMoved event exits a Target within an enclosing space. The Target is set for events of this kind.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseExitedTarget(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse button has been moved over a space. There is no target set for events of this kind.
     * (with no buttons no down).
     * @param e the MouseEvent that triggered this event
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been pressed on a found and valid target within the enclosing space.
     * @param e the MouseEvent that triggered this event
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been dragged on a found and valid target within the enclosing space.
     * This method is only called if the responder has been activated by a mouse pressed event.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been released after being pressed on a found and valid target within the enclosing space.
     * @param e the MouseEvent that triggered this event
     */
    public void mouseReleased(MouseEvent e) {
    }
}

