package quicktime.app.spaces;

import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.actions.TickleList;
import quicktime.app.time.Timeable;
import quicktime.app.time.Timer;
import quicktime.std.clocks.TimeBase;

/**
 * Deprecated.
 * SimpleSpace provides a default reference implementation of the Space interface.
 * The following actions are taken by the SimpleSpace class when members or controllers
 * are added to the Space.
 *
 * If the member is a Space then the contained Space will have the same source for
 * its Listener members as the parent Space. The Space's TimeBase will be slaved to
 * the parent Space.
 * If the member or controller (TimeableController) implements the Timeable interface then the TimeBase
 * of the member or controller will be slaved to the Space.
 * If the member or controller (ListenerController) implements the Listener interface then the source
 * of the Listener interest will be the source of the parent Space - that is all members
 * and controllers will have the same Listener source.
 * If the controller implements the Ticklish interface (TicklishController) it will be added to the
 * ticklishList and the tickle and timeChanged methods of the Space will tickle and timeChange
 * all members of this ticklishList.
 * If the controller implements the Collection interface (CollectionController) and the
 * isWholespace method of the controller returns true then all members that are added to the Space
 * will also be added to the controller. It is the responsibility of the Controller to determine
 * if the member is an appropriate member for it to control, and if so add it to its collection
 * of objects it controls. If isWholespace is false then members must explicitly added to the controllers.
 * When a member is removed from a Space the CollectionController will
 * be told to remove that member form its collection of objects it controls (regardless of the
 * isWholespace method).
 * All Controllers that are added to a Space are considered to be active. There is no
 * explicit way for a Controller to deactivate itself, though internally a particular implementation
 * of the Controller interface may choose to be inactive or active. However the Space knows no such
 * state of the Controller and thus treats all attached Controllers as active.
 *
 * The SimpleSpace can be used in different settings - ie. it has no predefined application
 * for managing display (presentation) or model contexts. The QTJava library provides DisplaySpaces
 * as extensions and concrete classes of the SimpleSpace. However applications can also use
 * SimpleSpace as a parent class for other kinds of Spaces, such as model spaces.
 *
 * The SimpleSpace has no knowledge or implementation of the type of collection that is used to
 * hold the collection of members. It is a delegated responsibility of the subclass to implement
 * the Collection methods. There are two protected methods that the subclass should call after
 * they have successfully added or removed a member - memberAdded and memberRemoved respectively.
 * These methods ensure the behaviour of members as outlined.
 * @see quicktime.app.display.SWCompositor,
 * {@link quicktime.app.display.GroupDrawable}
 */
public abstract class SimpleSpace implements Space {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Called by classes that extend the SimpleSpace. The sub class must pass in
     * a Protocol object that defines the protocol (ie. the Class type of member objects)
     * that the sub-class Space knows how to deal with. This ensure that members of a Space
     * meet some minimal functionality.
     *
     * This constructor does not create a Timer, the subclass must create a Timer in its
     * constructor. This is provided so that subclasses can customise the Timer.
     * @param protocols the Protocol that is enforced on candidate member objects of this space.
     * {@link quicktime.app.spaces.Protocol}
     * ,
     * {@link quicktime.app.time.Timer}
     */
    protected SimpleSpace(Protocol paramProtocol) throws QTException {
        this.prots = paramProtocol;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Called by classes that extend the SimpleSpace. The sub class must pass in
     * a Protocol object that defines the protocol (ie. the Class type of member objects)
     * that the sub-class Space knows how to deal with. This ensure that members of a Space
     * meet some minimal functionality. The constructor also provides a scale and period
     * for the construction of the Space's Timer. This scale and period can be
     * reset at any time using appropriate methods on the Timer class.
     * @param protocols the Protocol that is enforced on candidate member objects of this space.
     * @param scale the scale of the Space's Timer - this may NOT be zero.
     * @param period the period of the Space's Timer.
     * {@link quicktime.app.spaces.Protocol}
     * ,
     * {@link quicktime.app.time.Timer}
     */
    protected SimpleSpace(Protocol paramProtocol, int paramInt1, int paramInt2) throws QTException {
        this.prots = paramProtocol;
        this.timer = new Timer(paramInt1, paramInt2, this);
    }

    /**
     * Deprecated.
     * This vector is used to collect the Controllers that are added to the Space* @deprecated since QTJava 6.1
     */
    protected Vector controllersVec = new Vector();

    /**
     * Deprecated.
     * This vector is used to collect any Spaces that are added as members to this Space* @deprecated since QTJava 6.1
     */
    protected Vector containedSpaces = new Vector();

    /**
     * Deprecated.
     * This TickleList is used to collect any ticklish controllers that are added to the Space* @deprecated since QTJava 6.1
     */
    protected TickleList ticklers = new TickleList();

    /**
     * Deprecated.
     * This is the Timer that both provides a TimeBase for the Space and tickles the Space
     * based on the scale and period of the Timer* @deprecated since QTJava 6.1
     */
    protected Timer timer;

    private Object listenerSource;

    private Protocol prots;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked by the timer when a time condition of its time base has changed.
     */
    public void timeChanged(int paramInt) throws QTException {
        this.ticklers.timeChanged(paramInt);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Timer invokes the tickle method when the invocation constraints of
     * the Timer are reached. If the sub-class Space has its own tickle requirements
     * then it should overide the tickle method, however to ensure that any ticklish
     * controllers are tickled the overiding implementation should first call super.tickle.
     * @param er the effective rate of the Timer when the time condition matches
     * @param time the time that the Timer invoked the tickle method.
     * @return true if the Ticklish object should receive a consequent tickle, or false
     *  if the Ticklish object should not be tickled again.
     * @return {@link quicktime.QTException}
     */
    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        return !this.ticklers.isEmpty() ? this.ticklers.tickle(paramFloat, paramInt) : false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an Enumeration of all of the Controllers that implement the Ticklish interface
     * (TicklishControllers) that have been added to the Space. These controllers are tickled
     * each time the Space itself is tickled by its Timer.
     * @return an Enumeration of Ticklish objects
     */
    public Enumeration tickleList() {
        return this.ticklers.members();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This object is the source for all of the members and controllers of the Space that
     * implement the Listener interface. All such members and controllers will have the same
     * source as the parent space when it is addedTo a Listener source. If the parent Space
     * has not been added to a Listener source this returns null.
     * @return the Listener source object
     */
    public Object getSource() {
        return this.listenerSource;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Protocol object that defines if a member is an appropriate member of
     * the Space.
     * @return a Protocol object.
     */
    public Protocol getProtocol() {
        return this.prots;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the Object object is an appropriate member of the Space. To be an
     * appropriate member the object must pass the passProtocol method of the Protocol
     * object of the Space.
     * @param object the Object to test
     * @return true if the object passes the current protocol of the Protocol object of the Space.
     * @see {@link quicktime.app.spaces.DynamicCollection},
     * {@link quicktime.app.spaces.Protocol}
     */
    public boolean isAppropriate(Object paramObject) {
        return getProtocol().passProtocol(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the specified QTDrawable is a member of this Group.
     * @param object the object to test for membership
     * @return a boolean
     */
    public synchronized boolean hasMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements()) {
            if (localEnumeration.nextElement().equals(paramObject))
                return true;
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by some kind of container object when the Listener
     * is added to the object that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the Listener object.
     */
    public synchronized void addedTo(Object paramObject) {
        if (this.listenerSource != null)
            removedFrom(this.listenerSource);
        this.listenerSource = paramObject;
        if (this.listenerSource != null) {
            Enumeration localEnumeration = this.containedSpaces.elements();
            Object localObject;
            while (localEnumeration.hasMoreElements()) {
                localObject = (Space) localEnumeration.nextElement();
                ((Space) localObject).addedTo(this.listenerSource);
            }
            localEnumeration = members();
            while (localEnumeration.hasMoreElements()) {
                localObject = localEnumeration.nextElement();
                if (((localObject instanceof Listener)) && (!(localObject instanceof Space))) {
                    ((Listener) localObject).addedTo(this.listenerSource);
                }
            }
            localEnumeration = controllers();
            while (localEnumeration.hasMoreElements()) {
                localObject = (Controller) localEnumeration.nextElement();
                if ((localObject instanceof ListenerController)) {
                    ((ListenerController) localObject).addedTo(this.listenerSource);
                }
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the Listener object
     * is removed from the object that is the source of the interest.
     * @param interest the object that is to be the source of interest for the
     *  the Listener object.
     */
    public synchronized void removedFrom(Object paramObject) {
        if (this.listenerSource != null) {
            if (paramObject != this.listenerSource) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(new QTRuntimeException("Remove from object Space has no interest in")), this, "removedFrom");
                return;
            }
            Enumeration localEnumeration = controllers();
            Object localObject;
            while (localEnumeration.hasMoreElements()) {
                localObject = (Controller) localEnumeration.nextElement();
                if ((localObject instanceof ListenerController)) {
                    ((ListenerController) localObject).removedFrom(this.listenerSource);
                }
            }
            localEnumeration = this.containedSpaces.elements();
            while (localEnumeration.hasMoreElements()) {
                localObject = (Space) localEnumeration.nextElement();
                ((Space) localObject).removedFrom(this.listenerSource);
            }
            localEnumeration = members();
            while (localEnumeration.hasMoreElements()) {
                localObject = localEnumeration.nextElement();
                if (((localObject instanceof Listener)) && (!(localObject instanceof Space)))
                    ((Listener) localObject).removedFrom(this.listenerSource);
            }
            this.listenerSource = null;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is used by the application to add a Controller to
     * the Space. A Controller will control members of a Space.
     * @param c the Controller to add to the Space.
     * @param {@link quicktime.QTException}
     */
    public synchronized void addController(Controller paramController) throws QTException {
        if (paramController == null)
            return;
        if (this.controllersVec.contains(paramController))
            return;
        paramController.addedToSpace(this);
        this.controllersVec.addElement(paramController);
        Object localObject1;
        Object localObject2;
        if ((paramController instanceof TimeableController)) {
            localObject1 = ((Timeable) paramController).getTimeBase();
            localObject2 = this.timer.getTimeBase();
            ((TimeBase) localObject1).setMasterTimeBase((TimeBase) localObject2, null);
        }
        if (((paramController instanceof ListenerController)) && (this.listenerSource != null)) {
            ((ListenerController) paramController).addedTo(this.listenerSource);
        }
        if ((paramController instanceof CollectionController)) {
            localObject1 = (CollectionController) paramController;
            if (((CollectionController) localObject1).isWholespace()) {
                localObject2 = members();
                while (((Enumeration) localObject2).hasMoreElements()) ((CollectionController) localObject1).addMember(((Enumeration) localObject2).nextElement());
            }
        }
        if ((paramController instanceof TicklishController)) {
            this.ticklers.addMember(paramController);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes a controller that was previously added to the Space.
     * @param c the Controller to remove from the Space.
     * @param {@link quicktime.QTException}
     */
    public synchronized void removeController(Controller paramController) throws QTException {
        if (paramController == null)
            return;
        if (!this.controllersVec.contains(paramController))
            return;
        this.controllersVec.removeElement(paramController);
        paramController.removedFromSpace();
        Object localObject;
        if ((paramController instanceof TimeableController)) {
            localObject = ((Timeable) paramController).getTimeBase();
            ((TimeBase) localObject).setMasterTimeBase(null, null);
        }
        if (((paramController instanceof ListenerController)) && (this.listenerSource != null)) {
            ((ListenerController) paramController).removedFrom(this.listenerSource);
        }
        if ((paramController instanceof CollectionController)) {
            localObject = (CollectionController) paramController;
            Enumeration localEnumeration = members();
            while (localEnumeration.hasMoreElements()) ((CollectionController) localObject).removeMember(localEnumeration.nextElement());
        }
        if ((paramController instanceof TicklishController)) {
            this.ticklers.removeMember(paramController);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an Enumeration of all Controllers that are currently
     * attached to the Space.
     * @return an Enumeration
     */
    public Enumeration controllers() {
        return this.controllersVec.elements();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the Timer of the Space. This Timer provids a TimeBase for the Space and
     * if active will tickle and send timeChanged calls to the TicklishControllers that
     * are attached to the Space.
     * @return a Timer
     */
    public Timer getTimer() {
        return this.timer;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When the sub-class defines the addMember method and has added the member
     * to its collection it should call memberAdded to ensure that the member
     * roles as defined within the Space are correctly established.
     * @param member the object that has been added to the Space.
     * @param {@link quicktime.QTException}
     */
    protected synchronized void memberAdded(Object paramObject) throws QTException {
        Object localObject2;
        Object localObject3;
        if ((paramObject instanceof Space)) {
            Object localObject1 = (Space) paramObject;
            localObject2 = ((Space) localObject1).getTimer().getTimeBase();
            localObject3 = this.timer.getTimeBase();
            ((TimeBase) localObject2).setMasterTimeBase((TimeBase) localObject3, null);
            this.containedSpaces.addElement(paramObject);
            if (this.listenerSource != null)
                ((Space) localObject1).addedTo(this.listenerSource);
        } else {
            if ((paramObject instanceof Timeable)) {
                Object localObject1 = ((Timeable) paramObject).getTimeBase();
                localObject2 = this.timer.getTimeBase();
                ((TimeBase) localObject1).setMasterTimeBase((TimeBase) localObject2, null);
            }
            if (((paramObject instanceof Listener)) && (this.listenerSource != null)) {
                ((Listener) paramObject).addedTo(this.listenerSource);
            }
        }
        Object localObject1 = controllers();
        while (((Enumeration) localObject1).hasMoreElements()) {
            localObject2 = ((Enumeration) localObject1).nextElement();
            if ((localObject2 instanceof CollectionController)) {
                localObject3 = (CollectionController) localObject2;
                if (((CollectionController) localObject3).isWholespace()) {
                    ((CollectionController) localObject3).addMember(paramObject);
                }
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When the sub-class defines the removeMember method and has removed the member
     * from its collection it should call memberRemoved to ensure that the member
     * roles as defined within the Space are correctly maintained.
     * @param member the object that has been added to the Space.
     * @param {@link quicktime.QTException}
     */
    protected synchronized void memberRemoved(Object paramObject) throws QTException {
        Object localObject2;
        if ((paramObject instanceof Space)) {
            Object localObject1 = (Space) paramObject;
            localObject2 = ((Space) localObject1).getTimer().getTimeBase();
            ((TimeBase) localObject2).setMasterTimeBase(null, null);
            this.containedSpaces.removeElement(paramObject);
            if (this.listenerSource != null)
                ((Space) localObject1).removedFrom(this.listenerSource);
        } else {
            if ((paramObject instanceof Timeable)) {
                Object localObject1 = ((Timeable) paramObject).getTimeBase();
                ((TimeBase) localObject1).setMasterTimeBase(null, null);
            }
            if (((paramObject instanceof Listener)) && (this.listenerSource != null)) {
                ((Listener) paramObject).removedFrom(this.listenerSource);
            }
        }
        Object localObject1 = controllers();
        while (((Enumeration) localObject1).hasMoreElements()) {
            localObject2 = ((Enumeration) localObject1).nextElement();
            if ((localObject2 instanceof CollectionController)) {
                ((CollectionController) localObject2).removeMember(paramObject);
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will remove all of the controllers that are currently attached
     * to the Space.
     */
    public synchronized void removeAllControllers() throws QTException {
        Enumeration localEnumeration = controllers();
        while (localEnumeration.hasMoreElements()) {
            removeController((Controller) localEnumeration.nextElement());
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will remove all of the members that are currently contained
     * by the space.
     */
    public synchronized void removeAllMembers() throws QTException {
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements()) {
            removeMember(localEnumeration.nextElement());
        }
    }

    /**
     * Deprecated.
     * java.lang.Throwable
     */
    protected void finalize() throws Throwable {
        synchronized (QTSession.terminationLock()) {
            if (QTSession.isInitialized()) {
                removeAllControllers();
                removeAllMembers();
            }
        }
        super.finalize();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     * @return {@link quicktime.QTException}
     */
    public abstract boolean addMember(Object paramObject) throws QTException;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the object is a member of the collection it is removed.
     * @param member the object to remove from the collection
     * @param {@link quicktime.QTException}
     */
    public abstract void removeMember(Object paramObject) throws QTException;
}
