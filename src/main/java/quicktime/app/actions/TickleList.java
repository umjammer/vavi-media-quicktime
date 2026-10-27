package quicktime.app.actions;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.CollectionController;
import quicktime.app.spaces.Space;
import quicktime.app.spaces.TicklishController;
import quicktime.app.time.Ticklish;

/**
 * An TickleList is a list that contains Ticklish members. Each time the TickleList
 * is tickled (it itself implements the Ticklish interface and can be a member of a
 * TickleList) it will tickle all of its members. If the member's tickle method returns
 * true the Ticklish member is kept active. If the tickle method returns false the
 * member must be re-enabled for it to be tickled. A TickleList's tickle method will
 * return false if the list is empty.
 *
 * Members of a TickleList are kept in a TickleNode object which is where the active
 * state of the node is kept. This information is only relevant for the list itself.
 * When a Ticklish member is added to a TickleList the addMember method calls the
 * makeTickleNode method. A subclass can define this method to make a subclass of
 * TickleNode to store extra information about Ticklish members in a TickleList.
 *
 * A singly-linked list is used to implement the TickleList. A list is used
 * for fast iteration through the list whenever the tickle method is called.
 * Insertion and Removal of members is of course a more expensive operation.
 *
 * The TickleList also implements the TicklishController interface thus
 * enabling it to be added to a Space as a controller for objects in that
 * space. It implements the Collection interface to indicate that it
 * contains a collection of objects. The TicklishController aspect of
 * the TickleList is generally used to schedule PeriodicActions that control
 * required aspects of the Space's members.
 *
 * @deprecated
 */
public class TickleList implements TicklishController, CollectionController {

    /**
     * @deprecated
     * @since QTJava 6.1
     * This TickleNode is the head of the singly-linked list. If this member
     * is null then the list is empty.
     */
    protected TickleNode list;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private boolean wholespace = false;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked by the timer when a time condition of its time base has changed.
     */
    @Override
    public synchronized void timeChanged(int paramInt) throws QTException {
        TickleNode localTickleNode = this.list;
        while (localTickleNode != null) {
            if (localTickleNode.active)
                localTickleNode.action.timeChanged(paramInt);
            localTickleNode = localTickleNode.next;
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The Timer invokes the tickle method when the invocation constraints of
     * the Timer are reached.
     * @param effectiveRate the effective rate of the Timer when the time condition matches
     * @param currentTime the time that the Timer invoked the tickle method.
     * @return true if the Ticklish object should receive a consequent tickle, or false
     *  if the Ticklish object should not be tickled again.
     * @throws quicktime.QTException
     */
    @Override
    public synchronized boolean tickle(float effectiveRate, int currentTime) throws QTException {
        TickleNode localTickleNode = this.list;
        while (localTickleNode != null) {
            if ((localTickleNode.active) && (!localTickleNode.action.tickle(effectiveRate, currentTime)))
                localTickleNode.active = false;
            localTickleNode = localTickleNode.next;
        }
        return this.list != null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return an Enumeration that is capable of enumerating through
     * the actions that have been added to the action list. The objects
     * of this enumeration are the Ticklish members.
     * @return an Enumeration object
     */
    @Override
    public Enumeration members() {
        return new ListEnumerator(this.list, null);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     * @return a boolean
     */
    @Override
    public boolean isEmpty() {
        return this.list == null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the number of members in the collection, ie. the collection's size.
     * @return the size of the collection.
     */
    @Override
    public int size() {
        int i = 0;
        TickleNode localTickleNode = this.list;
        while (localTickleNode != null) {
            i++;
            localTickleNode = localTickleNode.next;
        }
        return i;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     * The candidate member must implement the Ticklish interface.
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     * @throws quicktime.QTException
     */
    @Override
    public synchronized boolean addMember(Object member) throws QTException {
        if (!isAppropriate(member))
            return false;
        if (hasMember(member))
            return true;
        Ticklish localTicklish = (Ticklish) member;
        if (this.list == null) {
            this.list = makeTickleNode(localTicklish);
        } else {
            TickleNode localObject = this.list;
            TickleNode localTickleNode = localObject.next;
            while (localTickleNode != null) {
                localObject = localTickleNode;
                localTickleNode = localTickleNode.next;
            }
            localObject.next = makeTickleNode(localTicklish);
        }
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Used by the addMember method to make a TickleNode that holds the
     * supplied Ticklish object in the TickleList. Subclasses can
     * subclass the TickleNode and return their subclass if required.
     * @param t the new Ticklish member of the list.
     * @return the TickleNode that is used to hold the Ticklish member in the list
     */
    protected TickleNode makeTickleNode(Ticklish paramTicklish) {
        return new TickleNode(paramTicklish);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the active state of a member of the Ticklish list.
     * This state is only viable whilst the supplied member is a member
     * of the list.
     * @param member the member of the list.
     * @param flag if true the member is active and will be tickled, if false
     *  it is inactive and it won't be tickled when the list is tickled.
     */
    public void setMemberActive(Ticklish member, boolean flag) {
        TickleNode localTickleNode = _findNode(member);
        if (localTickleNode != null) {
            localTickleNode.active = flag;
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current active state of the supplied member.
     * If the member is not a member of the list this call returns false.
     * @param member a Ticklish member of this list
     * @return a boolean indicative the active state of the member
     */
    public boolean isMemberActive(Ticklish member) {
        TickleNode localTickleNode = _findNode(member);
        if (localTickleNode != null) {
            return localTickleNode.active;
        }
        return false;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the provided object is a object of the collection.
     * @param object the object to test for membership
     * @return a boolean
     */
    @Override
    public boolean hasMember(Object object) {
        return _findNode(object) != null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the TickleNode that corresponds to the member object.
     * If the object is not a member this returns null.
     * @return a TickleNode
     */
    public TickleNode findNode(Object paramObject) {
        return _findNode(paramObject);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns an Enumeration that is capable of enumerating over the nodes of
     * a list.
     * @return an Enumeration
     */
    public Enumeration nodes() {
        return new NodeEnumerator(this.list, null);
    }

    private synchronized TickleNode _findNode(Object paramObject) {
        TickleNode localTickleNode = this.list;
        while (localTickleNode != null) {
            if (localTickleNode.action.equals(paramObject)) {
                return localTickleNode;
            }
            localTickleNode = localTickleNode.next;
        }
        return null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the object is a member of the collection it is removed.
     * @param member the object to remove from the collection
     */
    @Override
    public synchronized void removeMember(Object member) {
        if (this.list == null)
            return;
        if (this.list.action.equals(member)) {
            this.list = this.list.next;
            return;
        }
        TickleNode localTickleNode1 = this.list;
        TickleNode localTickleNode2 = localTickleNode1.next;
        while (localTickleNode2 != null) {
            if (localTickleNode2.action.equals(member)) {
                localTickleNode1.next = localTickleNode2.next;
                break;
            }
            localTickleNode2 = localTickleNode2.next;
            localTickleNode1 = localTickleNode1.next;
        }
    }

    /**
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The object must implement the Ticklish interface.
     * Subclasses can refine this test to ensure a specialized type of Ticklish
     * interface.
     * @deprecated
     * @since QTJava 6.1
     * @param object the object to test
     * @return true if the supplied object implements the Ticklish interface.
     * @see quicktime.app.spaces.DynamicCollection,
     * {@link quicktime.app.spaces.Protocol}
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof Ticklish;
    }

    /**
     * Returns the current setting of the wholespace flag.
     * @deprecated
     * @since QTJava 6.1
     * @return true if any member of the Space is a candidate for being controlled
     *  by this controller. If false then objects must be explicitly added to
     *  the space.
     */
    @Override
    public boolean isWholespace() {
        return this.wholespace;
    }

    /**
     * This sets the wholespace characteristic. If the TickleList is added to a
     * Space and wholspace is true, then the Space will query the list with existing
     * and new members, and if appropriate, they will be added as members to this
     * Controller.
     * @deprecated
     * @since QTJava 6.1
     * @param flag the wholespace flag
     * @see quicktime.app.spaces.CollectionController
     */
    public void setWholespace(boolean flag) {
        this.wholespace = flag;
    }

    static class ListEnumerator implements Enumeration {

        ListEnumerator(TickleNode paramTickleNode, TickleList param1) {
            this(paramTickleNode);
        }

        private ListEnumerator(TickleNode paramTickleNode) {
            this.current = paramTickleNode;
        }

        private TickleNode current;

        @Override
        public boolean hasMoreElements() {
            return this.current != null;
        }

        @Override
        public Object nextElement() {
            if (this.current == null)
                throw new NoSuchElementException("TickleList");
            TickleNode localTickleNode = this.current;
            this.current = this.current.next;
            return localTickleNode.action;
        }
    }

    static class NodeEnumerator implements Enumeration {

        private TickleNode current;

        NodeEnumerator(TickleNode paramTickleNode, TickleList param1) {
            this(paramTickleNode);
        }

        private NodeEnumerator(TickleNode paramTickleNode) {
            this.current = paramTickleNode;
        }

        @Override
        public boolean hasMoreElements() {
            return this.current != null;
        }

        @Override
        public Object nextElement() {
            if (this.current == null)
                throw new NoSuchElementException("TickleList");
            TickleNode localTickleNode = this.current;
            this.current = this.current.next;
            return localTickleNode;
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        StringBuilder str = new StringBuilder(getClass().getName() + "[");
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements()) str.append(localEnumeration.nextElement()).append(",");
        str.append("]");
        return str.toString();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the Space when a Controller is being addedTo the Space s.
     * If the Controller is not an appropriate controller for that kind of Space this
     * method can throw an exception and the controller won't be added to the Space.
     * @param s the Space being added to
     */
    @Override
    public void addedToSpace(Space paramSpace) {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Called by the Space when a Controller is being removed
     * from a Space. This is only called by the Space on controllers that have
     * previously been added, thus the space they are being removed from
     * is the space they were added to.
     */
    @Override
    public void removedFromSpace() {
    }
}

