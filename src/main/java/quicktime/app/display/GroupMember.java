package quicktime.app.display;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.Region;

/**
 * Deprecated.
 * This class contains information that is attached to a QTDrawable object that
 * is attached to a GroupDrawable object.
 */
public class GroupMember implements Layerable {

    int layer;

    QTDrawable drawer;

    GroupDrawable group;

    Region clipRegion;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Construct a GroupMember object.
     * @param group the GroupDrawable of which d is a member
     * @param d the QTDrawable member
     * @param l the member's current layer
     */
    protected GroupMember(GroupDrawable paramGroupDrawable, QTDrawable paramQTDrawable, int paramInt) throws QTException {
        this.drawer = paramQTDrawable;
        this.layer = paramInt;
        this.group = paramGroupDrawable;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the GroupMember is the same drawer and
     * has the same layout characteristics.
     * @param obj the other object to test for equality
     * @return true if equal else false
     */
    public boolean equals(Object paramObject) {
        if (this == paramObject)
            return true;
        if ((paramObject instanceof GroupMember)) {
            GroupMember localGroupMember = (GroupMember) paramObject;
            return (localGroupMember.layer == this.layer) && (localGroupMember.drawer == this.drawer);
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Layer is a read only property - it is the front-to-back positioning of the
     * drawer in its Group, 0 is frontmost. To reset the layer you call the
     * setLayer method of the group.
     * @return the current layer of the drawer within the group
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set the member's layer within its group.
     * @param l the new layer
     * @param {@link quicktime.QTException}
     */
    public void setLayer(int paramInt) throws QTException {
        this.group.setMemberLayer(this.drawer, paramInt);
    }

    /**
     * Deprecated.
     * The current drawing object that the settings relate to * @deprecated since QTJava 6.1
     */
    public final QTDrawable getDrawer() {
        return this.drawer;
    }

    /**
     * Deprecated.
     * Returns a string representation of this object* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[" + this.drawer + ",layer=" + this.layer + "]";
    }
}

