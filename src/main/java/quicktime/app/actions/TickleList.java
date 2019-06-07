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
 * Deprecated.
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
 */
public class TickleList implements TicklishController, CollectionController {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked by the timer when a time condition of its time base has changed.
     */
    public synchronized void timeChanged(int paramInt) throws QTException {
        TickleNode localTickleNode = this.list;
        while (localTickleNode != null) {
            if (localTickleNode.active)
                localTickleNode.action.timeChanged(paramInt);
            localTickleNode = localTickleNode.next;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Timer invokes the tickle method when the invocation constraints of
     * the Timer are reached.
     * @param effectiveRate the effective rate of the Timer when the time condition matches
     * @param currentTime the time that the Timer invoked the tickle method.
     * @return true if the Ticklish object should receive a consequent tickle, or false
     *  if the Ticklish object should not be tickled again.
     * @return {@link quicktime.QTException}
     */
    public synchronized boolean tickle(float paramFloat, int paramInt) throws QTException {
        TickleNode localTickleNode = this.list;
        while (localTickleNode != null) {
            if ((localTickleNode.active) && (!localTickleNode.action.tickle(paramFloat, paramInt)))
                localTickleNode.active = false;
            localTickleNode = localTickleNode.next;
        }
        return this.list != null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return an Enumeration that is capable of enumerating through
     * the actions that have been added to the action list. The objects
     * of this enumeration are the Ticklish members.
     * @return an Enumeration object
     */
    public Enumeration members() {
        return new ListEnumerator(this.list, null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the collection contains no members, or false if
     * it has one or more members.
     * @return a boolean
     */
    public boolean isEmpty() {
        return this.list == null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the number of members in the collection, ie. the collection's size.
     * @return the size of the collection.
     */
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Adds an object to the collection. Returns true if the object is
     * appropriate member of the collection (and thus added) and false if not.
     * The candidate member must implement the Ticklish interface.
     * @param member the object that should be added as a member to the collection
     * @return a boolean.
     * @return {@link quicktime.QTException}
     */
    public synchronized boolean addMember(Object paramObject) throws QTException {
        if (!isAppropriate(paramObject))
            return false;
        if (hasMember(paramObject))
            return true;
        Ticklish localTicklish = (Ticklish) paramObject;
        if (this.list == null) {
            this.list = makeTickleNode(localTicklish);
        } else {
            Object localObject = this.list;
            TickleNode localTickleNode = ((TickleNode) localObject).next;
            while (localTickleNode != null) {
                localObject = localTickleNode;
                localTickleNode = localTickleNode.next;
            }
            ((TickleNode) localObject).next = makeTickleNode(localTicklish);
        }
        return true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the active state of a member of the Ticklish list.
     * This state is only viable whilst the supplied member is a member
     * of the list.
     * @param member the member of the list.
     * @param flag if true the member is active and will be tickled, if false
     *  it is inactive and it won't be tickled when the list is tickled.
     */
    public void setMemberActive(Ticklish paramTicklish, boolean paramBoolean) {
        TickleNode localTickleNode = _findNode(paramTicklish);
        if (localTickleNode != null) {
            localTickleNode.active = paramBoolean;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current active state of the supplied member.
     * If the member is not a member of the list this call returns false.
     * @param member a Ticklish member of this list
     * @return a boolean indicative the active state of the member
     */
    public boolean isMemberActive(Ticklish paramTicklish) {
        TickleNode localTickleNode = _findNode(paramTicklish);
        if (localTickleNode != null) {
            return localTickleNode.active;
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is a member of the collection.
     * @param object the object to test for membership
     * @return a boolean
     */
    public boolean hasMember(Object paramObject) {
        return _findNode(paramObject) != null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the TickleNode that corresponds to the member object.
     * If the object is not a member this returns null.
     * @return a TickleNode
     */
    public TickleNode findNode(Object paramObject) {
        return _findNode(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If the object is a member of the collection it is removed.
     * @param member the object to remove from the collection
     */
    public synchronized void removeMember(Object paramObject) {
        if (this.list == null)
            return;
        if (this.list.action.equals(paramObject)) {
            this.list = this.list.next;
            return;
        }
        TickleNode localTickleNode1 = this.list;
        TickleNode localTickleNode2 = localTickleNode1.next;
        while (localTickleNode2 != null) {
            if (localTickleNode2.action.equals(paramObject)) {
                localTickleNode1.next = localTickleNode2.next;
                break;
            }
            localTickleNode2 = localTickleNode2.next;
            localTickleNode1 = localTickleNode1.next;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the provided object is an appropriate candidate for
     * membership in a collection. The object must implement the Ticklish interface.
     * Subclasses can refine this test to ensure a specialized type of Ticklish
     * interface.
     * @param object the object to test
     * @return true if the supplied object implements the Ticklish interface.
     * @see {@link quicktime.app.spaces.DynamicCollection},
     * {@link quicktime.app.spaces.Protocol}
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Ticklish;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current setting of the wholespace flag.
     * @return true if any member of the Space is a candidate for being controlled
     *  by this controller. If false then objects must be explicitly added to
     *  the space.
     */
    public boolean isWholespace() {
        return this.wholespace;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This sets the wholespace characteristic. If the TickleList is added to a
     * Space and wholspace is true, then the Space will query the list with existing
     * and new members, and if appropriate, they will be added as members to this
     * Controller.
     * @param flag the wholespace flag
     * @see {@link quicktime.app.spaces.CollectionController}
     */
    public void setWholespace(boolean paramBoolean) {
        this.wholespace = paramBoolean;
    }

    static class ListEnumerator implements Enumeration {

        ListEnumerator(TickleNode paramTickleNode, TickleList param1) {
            this(paramTickleNode);
        }

        private ListEnumerator(TickleNode paramTickleNode) {
            this.current = paramTickleNode;
        }

        private TickleNode current;

        public boolean hasMoreElements() {
            return this.current != null;
        }

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

        public boolean hasMoreElements() {
            return this.current != null;
        }

        public Object nextElement() {
            if (this.current == null)
                throw new NoSuchElementException("TickleList");
            TickleNode localTickleNode = this.current;
            this.current = this.current.next;
            return localTickleNode;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        String str = getClass().getName() + "[";
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements()) str = str + localEnumeration.nextElement() + ",";
        str = str + "]";
        return str;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the Space when a Controller is being addedTo the Space s.
     * If the Controller is not an appropriate controller for that kind of Space this
     * method can throw an exception and the controller won't be added to the Space.
     * @param s the Space being added to
     */
    public void addedToSpace(Space paramSpace) {
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Called by the Space when a Controller is being removed
     * from a Space. This is only called by the Space on controllers that have
     * previously been added, thus the space they are being removed from
     * is the space they were added to.
     */
    public void removedFromSpace() {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/TickleList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
