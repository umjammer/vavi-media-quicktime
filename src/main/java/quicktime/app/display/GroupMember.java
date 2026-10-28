package quicktime.app.display;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.Region;

/**
 * @deprecated
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
     * @deprecated
     * @since QTJava 6.1
     * Construct a GroupMember object.
     * @param group the GroupDrawable of which d is a member
     * @param d the QTDrawable member
     * @param l the member's current layer
     */
    protected GroupMember(GroupDrawable group, QTDrawable d, int l) throws QTException {
        this.drawer = d;
        this.layer = l;
        this.group = group;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the GroupMember is the same drawer and
     * has the same layout characteristics.
     * @param obj the other object to test for equality
     * @return true if equal else false
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj instanceof GroupMember localGroupMember)) {
            return (localGroupMember.layer == this.layer) && (localGroupMember.drawer == this.drawer);
        }
        return false;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Layer is a read only property - it is the front-to-back positioning of the
     * drawer in its Group, 0 is frontmost. To reset the layer you call the
     * setLayer method of the group.
     * @return the current layer of the drawer within the group
     */
    @Override
    public int getLayer() {
        return this.layer;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the member's layer within its group.
     * @param l the new layer
     * @throws quicktime.QTException
     */
    @Override
    public void setLayer(int l) throws QTException {
        this.group.setMemberLayer(this.drawer, l);
    }

    /**
     * @deprecated
     * The current drawing object that the settings relate to * @deprecated since QTJava 6.1
     */
    public final QTDrawable getDrawer() {
        return this.drawer;
    }

    /**
     * @deprecated
     * Returns a string representation of this object* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[" + this.drawer + ",layer=" + this.layer + "]";
    }
}
