package quicktime.app.actions;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.time.Ticklish;

/**
 * @deprecated
 * The TickleNode is used by the TicklList to contain its
 * Ticklish members.
 * @see quicktime.app.actions.TickleList
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
     * @since QTJava 6.1
     * Create a TickleNode for the supplied Ticklish
     * @param action the Ticklish member
     */
    protected TickleNode(Ticklish action) {
        this.action = action;
    }

    boolean active = true;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return the Ticklish that is held by this node
     * @return the TIcklish
     */
    public Ticklish getTicklish() {
        return this.action;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the Ticklish that is held by this node
     */
    public void setTicklish(Ticklish paramTicklish) {
        this.action = paramTicklish;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Allows the application to determine if this node of a TickleList is
     * currently active and thus its ticklish will be tickled by the list.
     * @return a boolean
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Allows the application to activate or deactivate a TickleNode that is
     * currently a member of a list.
     * @param flag a boolean
     */
    public void setActive(boolean flag) {
        this.active = flag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Return a String representation of this object
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[action=" + this.action + ",active=" + this.active + ",next=" + this.next + "]";
    }
}

