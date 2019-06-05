
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
 * @deprecated
 */
public abstract class SimpleSpace implements Space {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected SimpleSpace(Protocol paramProtocol) throws QTException {
        this.prots = paramProtocol;
    }

    /**
     * @deprecated
     */
    protected SimpleSpace(Protocol paramProtocol, int paramInt1, int paramInt2) throws QTException {
        this.prots = paramProtocol;
        this.timer = new Timer(paramInt1, paramInt2, this);
    }

    protected Vector controllersVec = new Vector();

    protected Vector containedSpaces = new Vector();

    protected TickleList ticklers = new TickleList();

    protected Timer timer;

    private Object listenerSource;

    private Protocol prots;

    /**
     * @deprecated
     */
    public void timeChanged(int paramInt) throws QTException {
        this.ticklers.timeChanged(paramInt);
    }

    /**
     * @deprecated
     */
    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        return !this.ticklers.isEmpty() ? this.ticklers.tickle(paramFloat, paramInt) : false;
    }

    /**
     * @deprecated
     */
    public Enumeration tickleList() {
        return this.ticklers.members();
    }

    /**
     * @deprecated
     */
    public Object getSource() {
        return this.listenerSource;
    }

    /**
     * @deprecated
     */
    public Protocol getProtocol() {
        return this.prots;
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return getProtocol().passProtocol(paramObject);
    }

    /**
     * @deprecated
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
     * @deprecated
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
     * @deprecated
     */
    public synchronized void removedFrom(Object paramObject) {
        if (this.listenerSource != null) {
            if (paramObject != this.listenerSource) {
                QTRuntimeException.handleOrThrow(
                                                 new QTRuntimeException(new QTRuntimeException("Remove from object Space has no interest in")),
                                                 this,
                                                 "removedFrom");
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
     * @deprecated
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
                while (((Enumeration) localObject2).hasMoreElements())
                    ((CollectionController) localObject1).addMember(((Enumeration) localObject2).nextElement());
            }
        }
        if ((paramController instanceof TicklishController)) {
            this.ticklers.addMember(paramController);
        }
    }

    /**
     * @deprecated
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
            while (localEnumeration.hasMoreElements())
                ((CollectionController) localObject).removeMember(localEnumeration.nextElement());
        }
        if ((paramController instanceof TicklishController)) {
            this.ticklers.removeMember(paramController);
        }
    }

    /**
     * @deprecated
     */
    public Enumeration controllers() {
        return this.controllersVec.elements();
    }

    /**
     * @deprecated
     */
    public Timer getTimer() {
        return this.timer;
    }

    /**
     * @deprecated
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
     * @deprecated
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
     * @deprecated
     */
    public synchronized void removeAllControllers() throws QTException {
        Enumeration localEnumeration = controllers();
        while (localEnumeration.hasMoreElements()) {
            removeController((Controller) localEnumeration.nextElement());
        }
    }

    /**
     * @deprecated
     */
    public synchronized void removeAllMembers() throws QTException {
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements()) {
            removeMember(localEnumeration.nextElement());
        }
    }

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
     * @deprecated
     */
    public abstract boolean addMember(Object paramObject) throws QTException;

    /**
     * @deprecated
     */
    public abstract void removeMember(Object paramObject) throws QTException;
}
