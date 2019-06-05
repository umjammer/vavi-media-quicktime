
package quicktime.app.actions;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.Ticklish;


/**
 * @deprecated
 */
public class TickleNode {
    TickleNode next;

    Ticklish action;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected TickleNode(Ticklish paramTicklish) {
        this.action = paramTicklish;
    }

    boolean active = true;

    /**
     * @deprecated
     */
    public Ticklish getTicklish() {
        return this.action;
    }

    /**
     * @deprecated
     */
    public void setTicklish(Ticklish paramTicklish) {
        this.action = paramTicklish;
    }

    /**
     * @deprecated
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @deprecated
     */
    public void setActive(boolean paramBoolean) {
        this.active = paramBoolean;
    }

    /**
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "[action=" + this.action + ",active=" + this.active + ",next=" + this.next + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/TickleNode.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
