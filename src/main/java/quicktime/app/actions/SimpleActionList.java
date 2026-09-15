package quicktime.app.actions;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * The SimpleActionList class extends the TickleList to
 * provide a list of Targetable objects. The Targetable interface
 * extends the Ticklish interface providing a Ticklish object
 * with the concept of a target which will be acted upon by
 * in its tickle method. By providing a List that requires members
 * to be Targetable objects, the list can remove all of the actions
 * that are associated with a particular target and return an enumeration
 * that is capable of enumerating over the list's targets.
 *
 * @see {@link quicktime.app.actions.Targetable}
 */
public class SimpleActionList extends TickleList {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return an Enumeration that is capable of enumerating through
     * the targets that are targetted by the actions. As any of the
     * actions that are added can target the same object, this Enumerator
     * should iterate through each of the targets, with each target
     * being unique.
     * @return an Enumerator
     */
    public Enumeration targets() {
        return new TargetEnumerator(this.list, null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the object implements the Targetable interface. Only
     * Targetable objects can be members of the SimpleActionList
     * @param object an object to test for appropriateness of this list
     * @return true if the object implements the Targetable interface
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Targetable;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes any and all actions that target the supplied target.
     * @param target the target which determines which actions to remove
     */
    public synchronized void removeTarget(Object paramObject) {
        if (this.list != null) {
            Object localObject1;
            do {
                localObject1 = ((Targetable) this.list.action).getTarget();
                if ((localObject1 == null) || (!localObject1.equals(paramObject)))
                    break;
                this.list = this.list.next;
            } while (this.list != null);
            localObject1 = this.list;
            TickleNode localTickleNode = localObject1 != null ? ((TickleNode) localObject1).next : null;
            while (localTickleNode != null) {
                Object localObject2 = ((Targetable) localTickleNode.action).getTarget();
                if ((localObject2 != null) && (localObject2.equals(paramObject))) {
                    ((TickleNode) localObject1).next = localTickleNode.next;
                    localTickleNode = localTickleNode.next;
                } else {
                    localObject1 = localTickleNode;
                    localTickleNode = localTickleNode.next;
                }
            }
        }
    }

    static class TargetEnumerator implements Enumeration {

        TickleNode current;

        TargetEnumerator(TickleNode paramTickleNode, SimpleActionList param1) {
            this(paramTickleNode);
        }

        private TargetEnumerator(TickleNode paramTickleNode) {
            this.current = paramTickleNode;
        }

        public boolean hasMoreElements() {
            while (this.current != null) {
                if (!this.rt.contains(((Targetable) this.current.action).getTarget()))
                    return true;
                this.current = this.current.next;
            }
            return false;
        }

        public Object nextElement() {
            if (this.current == null)
                throw new NoSuchElementException("ActionList");
            TickleNode localTickleNode = this.current;
            this.current = this.current.next;
            Object localObject = ((Targetable) localTickleNode.action).getTarget();
            this.rt.addElement(localObject);
            return localObject;
        }

        private Vector rt = new Vector();
    }
}

