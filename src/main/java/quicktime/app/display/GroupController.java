package quicktime.app.display;

import java.awt.event.MouseEvent;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.MouseController;
import quicktime.app.actions.MouseResponder;
import quicktime.app.spaces.Space;
import quicktime.qd.Region;

/**
 * Deprecated.
 * This controller provides specific semantics for mouse control that deal
 * specifically with GroupDrawable objects.
 *
 * It implements the getSelected method to return the fronmost QTDrawable object
 * at the location of the mouse event.
 * @see {@link quicktime.app.event.QTMouseController}
 */
public class GroupController extends MouseController {

    private GroupDrawable group;

    private QTDrawable current;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Constructs a GroupController with a MouseResponder that will
     * respond to the mouse events of the controller.
     * @param mr the mouse responder that will respond to mouse events
     */
    public GroupController(MouseResponder paramMouseResponder, boolean paramBoolean) {
        super(paramMouseResponder, paramBoolean);
    }

    private boolean isActive = false;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current GroupDrawable space that the controller is controlling.
     * @return the current space
     */
    public Space getSpace() {
        return this.group;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set the target space. If the target object is not a GroupDrawable a ClassCastException
     * is thrown.
     * @param target the new GroupDrawable space for the controller.
     */
    protected void setTargetSpace(Space paramSpace) {
        this.group = ((GroupDrawable) paramSpace);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called when a GroupController has received a mouse
     * event in its space. This method returns the frontmost QTDrawable object at the
     * event's location if it finds such an object at the location of the event.
     * @param event a mouse down event
     * @return a QTDrawable object that is located at the event coordinates or null
     * @return {@link quicktime.QTException}
     */
    protected Object getSelected(MouseEvent paramMouseEvent) throws QTException {
        this.current = ((QTDrawable) this.group.hitTest(paramMouseEvent.getX(), paramMouseEvent.getY()));
        if ((isWholespace()) || (hasMember(this.current)))
            return this.current;
        return null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A method that is called whenever the MouseController deactivates its responder.
     * A subclass should use this method to deactivate a responder (either explicitly or calling super.deactivateResponder).
     * For example a user may have
     * dragged a member out of a group, so the drag action's target is no longer valid and
     * the Responder has no target to act upon.
     */
    protected void deactivateResponder() {
        this.current = null;
        this.isActive = false;
        super.deactivateResponder();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A notification method that is called whenever the MouseController activates its responder.
     */
    protected void responderActivated() {
        this.isActive = true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Drag the target QTDrawable that is effected by the delegated MouseResponder.
     * This method ensures that the display logic of the GroupDrawable is kept
     * updated due to any changes in the display characteristics of a mouseDragged
     * event.
     * @param e the mouse event that triggered the drag
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        if (!this.isActive)
            return;
        try {
            synchronized (this.group) {
                MouseResponder localMouseResponder = getResponder();
                if ((localMouseResponder.isInterested(4)) && (localMouseResponder.isActive())) {
                    QTDrawable localQTDrawable = this.current;
                    doDragTarget(paramMouseEvent);
                    this.current = localQTDrawable;
                    Region localRegion1 = new Region(this.current.getDisplayBounds());
                    localMouseResponder.mouseDragged(paramMouseEvent);
                    Region localRegion2 = new Region(this.current.getDisplayBounds());
                    Region localRegion3 = localRegion1.union(localRegion2).diff(localRegion2);
                    if (!localRegion3.empty()) {
                        this.group.redrawMember(this.current, localRegion3);
                    }
                    if (!this.group.hasMember(this.current))
                        deactivateResponder();
                }
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mouseDragged");
        }
    }
}

