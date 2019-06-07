package quicktime.app.event;

import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.anim.TwoDSprite;
import quicktime.app.display.QTDrawable;
import quicktime.app.spaces.Collection;
import quicktime.app.spaces.Space;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;

/**
 * Deprecated.
 * A QTMouseTargetController deals with mouse events in a space where the mouse event
 * occurs over (or selects) a member of that space. Members are selected based on
 * being the frontmost member of the space (or of the members explicitly added to the controller).
 *
 * This controller is also a collection. It acts in two modes. If its wholespace variable
 * is set to true then any of the members of the space can be selected. It wholespace is
 * false then only those members of the space that are also members of the controller can
 * be selected. Generally controllers represent a collection of members that will require
 * the same kinds of mouse behaviours.
 *
 * This class specialises the handling of the events of its superclass in that
 * for the event to be dispatched to its listeners the controller must have a selected target.
 * In these cases the target attribute of the mouse event is set and the mouse event
 * is broadcast to the appropriate listeners. For instance, if the user clicks in an "empty"
 * area of the space the mouse clicked event won't be broadcast. However, if the user clicks
 * on a member of the space and either wholespace is true or the selected member is a member
 * of the controller, then and only then will the event be broadcast.
 * The exception to this rule is that MouseEnter and MouseExit events will be broadcast regardless.
 * MouseDrag events are considered to be targeted to a target that was selected by a mouse press
 * event and are broadcast until the user releases the mouse. MouseMove events will only be broadcast
 * when they occur whilst over a selected target.
 *
 * In addition to these events a listener can be registered with this controller which is interested
 * in receiving notification of mouse move or drag events that enter and exit members of the space.
 *
 * If members are added to the controller the controller will query the space it is attached to
 * to see if those members are appropriate members for that space. This can only be done
 * after the controller is attached to a space. If members are added to the controller before it
 * is added to a space, this appropriateness test is performed. If a member is found to inappropriate
 * it is removed without notification. The member must also be a member of the space.
 */
public class QTMouseTargetController extends QTMouseController implements Collection {

    private Object mousePressTarget;

    private boolean inPressDraggingTarget;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a QTMouseTargetController with a default setting of wholespace equals true.
     */
    public QTMouseTargetController() {
        this(true);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates a QTMouseTargetController.
     * @param wholespace the initial setting of the wholespace variable.
     */
    public QTMouseTargetController(boolean paramBoolean) {
        this.wholespace = paramBoolean;
        this.memberVec = new Vector();
    }

    private QDPoint cachePoint = new QDPoint(0, 0);

    MouseTargetListener mouseTargetListener;

    private Object currentMouseMovedTarget = null;

    private boolean wholespace;

    private Vector memberVec;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the space when a Controller is being addedTo the Space s.
     * If the Controller is not an appropriate controller for that kind of Space this
     * method can throw an exception and the controller won't be added to the Space.
     *
     * This method will also ensure that any members of the controller are both appropriate
     * objects for the space and are also members of the space.
     * @param s the Space being added to
     * @see setTargetSpace()
     */
    public synchronized void addedToSpace(Space paramSpace) {
        super.addedToSpace(paramSpace);
        for (int i = 0; i < this.memberVec.size(); i++) {
            Object localObject = this.memberVec.elementAt(i);
            if (this.compSpace != null) {
                if (!(localObject instanceof TwoDSprite)) {
                    this.memberVec.removeElementAt(i);
                    TwoDSprite localTwoDSprite = this.compSpace.getMemberSprite(localObject);
                    if (localTwoDSprite != null) {
                        this.memberVec.insertElementAt(localTwoDSprite, i);
                    } else {
                        i--;
                    }
                } else if (!this.space.hasMember(localObject)) {
                    this.memberVec.removeElementAt(i);
                    i--;
                }
            } else if (!this.space.hasMember(localObject)) {
                this.memberVec.removeElementAt(i);
                i--;
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called when a controller is removed from its space and it had an active event
     * handler
     */
    protected void removeActiveHandler() {
        if ((this.mouseTargetListener != null) && (this.currentMouseMovedTarget != null)) {
            QTMouseEvent localQTMouseEvent = new QTMouseEvent(this.space);
            localQTMouseEvent.initialiseEventProfile(508, -1, -1, 0L, 0, 0);
            localQTMouseEvent.setTarget(this.currentMouseMovedTarget);
            doMouseTargetExit(localQTMouseEvent);
            this.currentMouseMovedTarget = null;
        }
        super.removeActiveHandler();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called when a controller is added to its space and
     * the controller needs to create its event handler
     */
    protected void addHandler() {
        super.addHandler();
        if (hasMouseTargetListener())
            this.handler.addMouseTargetListener();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current setting of the wholespace flag.
     * @return a boolean
     */
    public boolean isWholespace() {
        return this.wholespace;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the new value of the wholespace flag.
     * @param flag if true all objects in the controller's space are potentially controllable.
     */
    public void setWholespace(boolean paramBoolean) {
        this.wholespace = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If wholespace is set to false, then only objects that are explicitly added
     * to the controller will be controlled by it.
     * Use this method to add these objects to the controller.
     * @param member the new member object that will be controlled.
     * @return a boolean indicating whether the object was added or not.
     */
    public synchronized boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        if (!hasMember(paramObject)) {
            if (this.compSpace != null) {
                if (!(paramObject instanceof TwoDSprite)) {
                    TwoDSprite localTwoDSprite = this.compSpace.getMemberSprite(paramObject);
                    if (localTwoDSprite == null) {
                        return false;
                    }
                    this.memberVec.addElement(localTwoDSprite);
                    return true;
                }
                if (!this.compSpace.hasMember(paramObject)) {
                    return false;
                }
            } else if ((this.space != null) && (!this.space.hasMember(paramObject))) {
                return false;
            }
            this.memberVec.addElement(paramObject);
        }
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The provided QTJava collections use this method
     * to ensure a minimal requirement of Class or Interface instanceof tests to
     * ensure the type of object that can be added to a collection. Some collections
     * can have a protocol that allows an application to further refine and tighten
     * the type of object that is allowed within an individual collection.
     * @param object the object to test
     * @return true if the supplied object is appropriate for this collection, or false if not.
     *  This method will also return true if the Controller has not been addedTo a space, in which
     *  case when it is the appropriate test will be performed.
     * @see {@link quicktime.app.spaces.DynamicCollection},
     * {@link quicktime.app.spaces.Protocol}
     */
    public boolean isAppropriate(Object paramObject) {
        if (this.space != null) {
            return this.space.isAppropriate(paramObject);
        }
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes a member from the collection of objects that the controller controls
     * @param member the object that will be removed from the controller.
     */
    public void removeMember(Object paramObject) {
        this.memberVec.removeElement(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an enumeration of all members of this controller. If wholespace is set to
     * true then this Enumeration may return an empty collection as the default behaviour
     * of the MouseController is that if wholespace is true any object in the space is
     * controllable and thus there is no requirement to explicitly add members to this
     * Controller.
     * @return an Enumeration
     */
    public Enumeration members() {
        return this.memberVec.elements();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the controller's collection of controllable objects is empty.
     * If wholespace is true this does not mean that the controller is not capable of
     * controlling object in its Space as the default behaviour
     * of the MouseController is that if wholespace is true any object in the space is
     * controllable and thus there is no requirement to explicitly add members to this
     * Controller.
     * @return a boolean
     */
    public boolean isEmpty() {
        return this.memberVec.isEmpty();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the number of members that have been explicitly added to the Controller.
     * @return an int
     */
    public int size() {
        return this.memberVec.size();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the member has been explicitly added to this controller.
     * As the default behaviour
     * of the MouseController is that if wholespace is true any object in the space is
     * controllable and thus there is no requirement to explicitly add members to this
     * Controller. Consequently this may return false, but if the object is a member
     * of the controller's space it may still be controllable by this controller iff
     * wholespace is set to true.
     * @param object the object to test for membership
     * @return a boolean
     */
    public boolean hasMember(Object paramObject) {
        return paramObject == null ? false : this.memberVec.contains(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Add a QTMouseListener for this controller. The controller
     * will take the appropriate action based on the known mouse listeners
     * in the quicktime.app.event package.
     * @param l a new QTMouseListener
     */
    public void addQTMouseListener(QTMouseListener paramQTMouseListener) {
        if ((paramQTMouseListener instanceof MouseTargetListener))
            addMouseTargetListener((MouseTargetListener) paramQTMouseListener);
        super.addQTMouseListener(paramQTMouseListener);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Remove a QTMouseListener for this controller.
     * @param l a new QTMouseListener
     */
    public void removeQTMouseListener(QTMouseListener paramQTMouseListener) {
        if ((paramQTMouseListener instanceof MouseTargetListener))
            removeMouseTargetListener((MouseTargetListener) paramQTMouseListener);
        super.removeQTMouseListener(paramQTMouseListener);
    }

    void addMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        boolean bool = hasMouseTargetListener();
        this.mouseTargetListener = QTEventCaster.add(this.mouseTargetListener, paramMouseTargetListener);
        updateMouseTargetListener(bool);
    }

    void removeMouseTargetListener(MouseTargetListener paramMouseTargetListener) {
        boolean bool = hasMouseTargetListener();
        this.mouseTargetListener = QTEventCaster.remove(this.mouseTargetListener, paramMouseTargetListener);
        updateMouseTargetListener(bool);
    }

    void updateMouseTargetListener(boolean paramBoolean) {
        if (this.handler != null) {
            if ((paramBoolean) && (!hasMouseTargetListener())) {
                this.handler.removeMouseTargetListener();
            } else if ((hasMouseTargetListener()) && (!paramBoolean))
                this.handler.addMouseTargetListener();
        }
    }

    boolean hasMouseTargetListener() {
        return this.mouseTargetListener != null;
    }

    boolean isInterestedInMouseMoveEvents(QTMouseEvent paramQTMouseEvent) {
        return ((this.mouseTargetListener != null) && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) || (super.isInterestedInMouseMoveEvents(paramQTMouseEvent));
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMousePressed(QTMouseEvent paramQTMouseEvent) {
        this.mousePressTarget = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
        if (this.mousePressTarget != null) {
            this.inPressDraggingTarget = true;
            paramQTMouseEvent.setTarget(this.mousePressTarget);
            this.mouseButtonListener.mousePressed(paramQTMouseEvent);
            this.currentMouseMovedTarget = null;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch release events that occur within its Space's area to those listeners.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if (translateAndIsWithin(paramQTMouseEvent)) {
                doMouseReleased(paramQTMouseEvent, true);
            } else if (this.mousePressTarget != null) {
                doMouseReleased(paramQTMouseEvent, false);
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseReleased(QTMouseEvent paramQTMouseEvent, boolean paramBoolean) {
        paramQTMouseEvent.setTarget(this.mousePressTarget);
        this.mouseButtonListener.mouseReleased(paramQTMouseEvent);
        if (this.inPressDraggingTarget) {
            this.currentMouseMovedTarget = this.mousePressTarget;
        } else {
            this.currentMouseMovedTarget = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
            if ((this.mouseTargetListener != null) && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())))
                if (paramBoolean) {
                    Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                    if (localObject != null) {
                        paramQTMouseEvent.setEventType(507);
                        paramQTMouseEvent.setTarget(localObject);
                        doMouseTargetEnter(paramQTMouseEvent);
                        paramQTMouseEvent.setEventType(502);
                    }
                    this.currentMouseMovedTarget = localObject;
                } else {
                    this.currentMouseMovedTarget = null;
                }
        }
        this.mousePressTarget = null;
        paramQTMouseEvent.setTarget(null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseClicked(QTMouseEvent paramQTMouseEvent) {
        Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
        if (localObject != null) {
            paramQTMouseEvent.setTarget(localObject);
            this.mouseButtonListener.mouseClicked(paramQTMouseEvent);
            paramQTMouseEvent.setTarget(null);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the QTMouseController has a MouseMoveListener(s) it will
     * dispatch drag events that occur within its Space's area to those listeners.
     * Mouse drag events will continue to be delivered to
     * the controller and its listeners where the press occured and until the mouse button is
     * released (regardless of whether the mouse is within the space or not).
     * @param e the QTMouseEvent of this type.
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        if (this.mousePressTarget == null) {
            return;
        }
        super.mouseDragged(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseDragged(QTMouseEvent paramQTMouseEvent) {
        paramQTMouseEvent.setTarget(this.mousePressTarget);
        if ((this.mouseMoveListener != null) && (this.mouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if (this.drawSpace != null) {
                try {
                    QTDrawable localQTDrawable = (QTDrawable) this.mousePressTarget;
                    Region localRegion1 = null;
                    localRegion1 = new Region(localQTDrawable.getDisplayBounds());
                    this.mouseMoveListener.mouseDragged(paramQTMouseEvent);
                    Region localRegion2 = new Region(localQTDrawable.getDisplayBounds());
                    Region localRegion3 = localRegion1.union(localRegion2).diff(localRegion2);
                    if (!localRegion3.empty())
                        this.drawSpace.redrawMember(localQTDrawable, localRegion3);
                } catch (QTException localQTException) {
                    QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mouseDragged");
                }
            } else {
                this.mouseMoveListener.mouseDragged(paramQTMouseEvent);
            }
        }
        if ((this.mouseTargetListener != null) && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
            if ((localObject == null) && (this.inPressDraggingTarget)) {
                paramQTMouseEvent.setEventType(508);
                doMouseTargetExit(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(506);
                this.inPressDraggingTarget = false;
            } else if (localObject != null) {
                if ((this.inPressDraggingTarget) && (localObject != this.mousePressTarget)) {
                    paramQTMouseEvent.setEventType(508);
                    doMouseTargetExit(paramQTMouseEvent);
                    paramQTMouseEvent.setEventType(506);
                    this.inPressDraggingTarget = false;
                } else if ((!this.inPressDraggingTarget) && (localObject == this.mousePressTarget)) {
                    paramQTMouseEvent.setEventType(507);
                    doMouseTargetEnter(paramQTMouseEvent);
                    paramQTMouseEvent.setEventType(506);
                    this.inPressDraggingTarget = true;
                }
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseTargetExit(QTMouseEvent paramQTMouseEvent) {
        this.mouseTargetListener.mouseTargetExited(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseTargetEnter(QTMouseEvent paramQTMouseEvent) {
        this.mouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     * @param isWithinItsSpace if true the mouse moved event is within the enclosing space. If false
     *  it isn't.
     */
    protected void doMouseMoved(QTMouseEvent paramQTMouseEvent, boolean paramBoolean) {
        if ((this.mouseTargetListener != null) && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if ((!paramBoolean) && (this.currentMouseMovedTarget != null)) {
                paramQTMouseEvent.setEventType(508);
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                doMouseTargetExit(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(503);
                this.currentMouseMovedTarget = null;
                return;
            }
            Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
            if (localObject == null) {
                if (this.currentMouseMovedTarget != null) {
                    paramQTMouseEvent.setEventType(508);
                    paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                    doMouseTargetExit(paramQTMouseEvent);
                    paramQTMouseEvent.setEventType(503);
                    this.currentMouseMovedTarget = null;
                }
            } else if (this.currentMouseMovedTarget == null) {
                paramQTMouseEvent.setEventType(507);
                paramQTMouseEvent.setTarget(localObject);
                doMouseTargetEnter(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(503);
                this.currentMouseMovedTarget = localObject;
            } else if (this.currentMouseMovedTarget != localObject) {
                paramQTMouseEvent.setEventType(508);
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                doMouseTargetExit(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(507);
                paramQTMouseEvent.setTarget(localObject);
                doMouseTargetEnter(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(503);
                this.currentMouseMovedTarget = localObject;
            } else if ((this.mouseMoveListener != null) && (this.mouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                this.mouseMoveListener.mouseMoved(paramQTMouseEvent);
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the QTMouseController has a MouseEnterExitListener(s) it will
     * dispatch a mouseEntered event when the mouse moves or is dragged into the space.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
        super.mouseEntered(paramQTMouseEvent);
        if (((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) ? 1 : 0) == 0) {
            doMouseEnter_Target(paramQTMouseEvent);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseEntered(QTMouseEvent paramQTMouseEvent) {
        super.doMouseEntered(paramQTMouseEvent);
        doMouseEnter_Target(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the QTMouseController has a MouseEnterExitListener(s) it will
     * dispatch a mouseExited event when the mouse moves or is dragged out of the space.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
        if (((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) ? 1 : 0) == 0) {
            doMouseExit_Target(paramQTMouseEvent);
        }
        super.mouseExited(paramQTMouseEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseExited(QTMouseEvent paramQTMouseEvent) {
        doMouseExit_Target(paramQTMouseEvent);
        super.doMouseExited(paramQTMouseEvent);
    }

    private void doMouseEnter_Target(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseTargetListener != null) && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            Object localObject;
            if (this.mousePressTarget != null) {
                localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                if ((localObject != null) && (!this.inPressDraggingTarget) && (localObject == this.mousePressTarget)) {
                    paramQTMouseEvent.setEventType(507);
                    paramQTMouseEvent.setTarget(this.mousePressTarget);
                    doMouseTargetEnter(paramQTMouseEvent);
                    this.inPressDraggingTarget = true;
                }
            } else {
                localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                if (localObject != null) {
                    paramQTMouseEvent.setEventType(507);
                    paramQTMouseEvent.setTarget(localObject);
                    doMouseTargetEnter(paramQTMouseEvent);
                    this.currentMouseMovedTarget = localObject;
                }
            }
            paramQTMouseEvent.setTarget(null);
            paramQTMouseEvent.setEventType(504);
        }
    }

    private void doMouseExit_Target(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseTargetListener != null) && (this.mouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            if (this.mousePressTarget != null) {
                Object localObject = getHitTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
                if ((localObject == null) && (this.inPressDraggingTarget)) {
                    paramQTMouseEvent.setEventType(508);
                    paramQTMouseEvent.setTarget(this.mousePressTarget);
                    doMouseTargetExit(paramQTMouseEvent);
                    this.inPressDraggingTarget = false;
                }
            } else if (this.currentMouseMovedTarget != null) {
                paramQTMouseEvent.setEventType(508);
                paramQTMouseEvent.setTarget(this.currentMouseMovedTarget);
                doMouseTargetExit(paramQTMouseEvent);
                this.currentMouseMovedTarget = null;
            }
            paramQTMouseEvent.setTarget(null);
            paramQTMouseEvent.setEventType(505);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the frontmost object at the specified location that is a member of the space
     * if wholespace is true, or the frontmost object in the controller's collection if
     * wholespace is false.
     * @param x the x location in the local coordinates of the space.
     * @param y the y location in the local coordinates of the space.
     */
    protected Object getHitTarget(int paramInt1, int paramInt2) {
        if (this.displaySpace != null) {
            try {
                QDRect localQDRect = this.displaySpace.getDisplayBounds();
                int i = paramInt1 + localQDRect.getX();
                int j = paramInt2 + localQDRect.getY();
                if (!localQDRect.inside(i, j)) {
                    return null;
                }
                if (isWholespace()) {
                    return this.displaySpace.hitTest(i, j);
                }
                if (this.drawSpace != null) {
                    Object localObject = this.displaySpace.hitTest(i, j);
                    if (hasMember(localObject)) {
                        return localObject;
                    }
                    return null;
                }
                Object localObject = null;
                Enumeration localEnumeration = members();
                this.cachePoint.setX(i);
                this.cachePoint.setY(j);
                while (localEnumeration.hasMoreElements()) {
                    TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                    if (localTwoDSprite.hitTest(this.compSpace.hitTestFlag | 0x10, this.cachePoint)) {
                        if (localObject != null) {
                            if (localTwoDSprite.getLayer() < ((TwoDSprite) localObject).getLayer())
                                localObject = localTwoDSprite;
                        } else {
                            localObject = localTwoDSprite;
                        }
                    }
                }
                return localObject;
            } catch (QTException localQTException) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.displaySpace, "getDisplayBounds");
            }
        }
        return null;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTMouseTargetController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
