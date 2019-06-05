
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
 * @deprecated
 */
public class TickleList implements TicklishController, CollectionController {
    /**
     * @deprecated
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
     * @deprecated
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
     * @deprecated
     */
    public Enumeration members() {
        return new ListEnumerator(this.list, null);
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.list == null;
    }

    /**
     * @deprecated
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
     * @deprecated
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
     * @deprecated
     */
    protected TickleNode makeTickleNode(Ticklish paramTicklish) {
        return new TickleNode(paramTicklish);
    }

    /**
     * @deprecated
     */
    public void setMemberActive(Ticklish paramTicklish, boolean paramBoolean) {
        TickleNode localTickleNode = _findNode(paramTicklish);
        if (localTickleNode != null) {
            localTickleNode.active = paramBoolean;
        }
    }

    /**
     * @deprecated
     */
    public boolean isMemberActive(Ticklish paramTicklish) {
        TickleNode localTickleNode = _findNode(paramTicklish);
        if (localTickleNode != null) {
            return localTickleNode.active;
        }
        return false;
    }

    /**
     * @deprecated
     */
    public boolean hasMember(Object paramObject) {
        return _findNode(paramObject) != null;
    }

    /**
     * @deprecated
     */
    public TickleNode findNode(Object paramObject) {
        return _findNode(paramObject);
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Ticklish;
    }

    /**
     * @deprecated
     */
    public boolean isWholespace() {
        return this.wholespace;
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public String toString() {
        String str = getClass().getName() + "[";
        Enumeration localEnumeration = members();
        while (localEnumeration.hasMoreElements())
            str = str + localEnumeration.nextElement() + ",";
        str = str + "]";
        return str;
    }

    /**
     * @deprecated
     */
    public void addedToSpace(Space paramSpace) {
    }

    /**
     * @deprecated
     */
    public void removedFromSpace() {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/TickleList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
