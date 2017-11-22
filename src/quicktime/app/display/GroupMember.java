
package quicktime.app.display;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.Region;


/**
 * @deprecated
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
     */
    protected GroupMember(GroupDrawable paramGroupDrawable, QTDrawable paramQTDrawable, int paramInt) throws QTException {
        this.drawer = paramQTDrawable;
        this.layer = paramInt;
        this.group = paramGroupDrawable;
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * @deprecated
     */
    public void setLayer(int paramInt) throws QTException {
        this.group.setMemberLayer(this.drawer, paramInt);
    }

    public final QTDrawable getDrawer() {
        return this.drawer;
    }

    public String toString() {
        return getClass().getName() + "[" + this.drawer + ",layer=" + this.layer + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * display/GroupMember.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
