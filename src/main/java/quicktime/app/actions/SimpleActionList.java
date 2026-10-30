package quicktime.app.actions;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * The SimpleActionList class extends the TickleList to
 * provide a list of Targetable objects. The Targetable interface
 * extends the Ticklish interface providing a Ticklish object
 * with the concept of a target which will be acted upon by
 * in its tickle method. By providing a List that requires members
 * to be Targetable objects, the list can remove all of the actions
 * that are associated with a particular target and return an enumeration
 * that is capable of enumerating over the list's targets.
 *
 * @see quicktime.app.actions.Targetable
 * @deprecated
 */
public class SimpleActionList extends TickleList {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Return an Enumeration that is capable of enumerating through
     * the targets that are targetted by the actions. As any of the
     * actions that are added can target the same object, this Enumerator
     * should iterate through each of the targets, with each target
     * being unique.
     *
     * @return an Enumerator
     * @since QTJava 6.1
     * @deprecated
     */
    public Enumeration targets() {
        return new TargetEnumerator(this.list, null);
    }

    /**
     * Returns true if the object implements the Targetable interface. Only
     * Targetable objects can be members of the SimpleActionList
     *
     * @param object an object to test for appropriateness of this list
     * @return true if the object implements the Targetable interface
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof Targetable;
    }

    /**
     * Removes any and all actions that target the supplied target.
     *
     * @param target the target which determines which actions to remove
     * @since QTJava 6.1
     * @deprecated
     */
    public synchronized void removeTarget(Object target) {
        if (this.list != null) {
            Object localObject1;
            do {
                localObject1 = ((Targetable) this.list.action).getTarget();
                if ((localObject1 == null) || (!localObject1.equals(target)))
                    break;
                this.list = this.list.next;
            } while (this.list != null);
            TickleNode tickleNode3 = this.list;
            TickleNode tickleNode = tickleNode3 != null ? tickleNode3.next : null;
            while (tickleNode != null) {
                Object localObject2 = ((Targetable) tickleNode.action).getTarget();
                if ((localObject2 != null) && (localObject2.equals(target))) {
                    tickleNode3.next = tickleNode.next;
                    tickleNode = tickleNode.next;
                } else {
                    tickleNode3 = tickleNode;
                    tickleNode = tickleNode.next;
                }
            }
        }
    }

    static class TargetEnumerator implements Enumeration<Object> {

        TickleNode current;

        TargetEnumerator(TickleNode current, SimpleActionList list) {
            this(current);
        }

        private TargetEnumerator(TickleNode current) {
            this.current = current;
        }

        @Override
        public boolean hasMoreElements() {
            while (this.current != null) {
                if (!this.rt.contains(((Targetable) this.current.action).getTarget()))
                    return true;
                this.current = this.current.next;
            }
            return false;
        }

        @Override
        public Object nextElement() {
            if (this.current == null)
                throw new NoSuchElementException("ActionList");
            TickleNode tickleNode = this.current;
            this.current = this.current.next;
            Object target = ((Targetable) tickleNode.action).getTarget();
            this.rt.addElement(target);
            return target;
        }

        private final Vector<Object> rt = new Vector<>();
    }
}
