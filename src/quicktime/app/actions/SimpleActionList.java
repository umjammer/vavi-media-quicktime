
package quicktime.app.actions;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public class SimpleActionList extends TickleList {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public Enumeration targets() {
        return new TargetEnumerator(this.list, null);
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Targetable;
    }

    /**
     * @deprecated
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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/SimpleActionList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
