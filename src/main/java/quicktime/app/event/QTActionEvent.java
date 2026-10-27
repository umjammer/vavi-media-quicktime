package quicktime.app.event;

import java.util.EventObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * @deprecated
 * This class is used by QuickTime objects that have an action associated
 * with them. When those object's actions are fired they will generate
 * an action event, with those objects as the source of the event,
 * to any registered listeners
 */
public class QTActionEvent extends EventObject {

    String actionCommand;

    int modifiers;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructs an QTActionEvent object with the specified source object.
     * @param source the object where the event originated
     * @param command the command string for this action event
     */
    public QTActionEvent(Object paramObject, String paramString) {
        this(paramObject, paramString, 0);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructs an QTActionEvent object with the specified source object.
     * @param source the object where the event originated
     * @param command the command string for this action event
     * @param modifiers the modifiers held down during this action
     */
    public QTActionEvent(Object paramObject, String paramString, int paramInt) {
        super(paramObject);
        this.actionCommand = paramString;
        this.modifiers = paramInt;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the command name associated with this action.
     */
    public String getActionCommand() {
        return this.actionCommand;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the modifiers held down during this action event.
     */
    public int getModifiers() {
        return this.modifiers;
    }

    private boolean consume = false;

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTActionEvents can be consumed by any of their listeners.
     * If the event is consumed it will not be passed on to
     * any other listeners. Calling this method indicates that
     * the event is consumed.
     */
    public void consume() {
        this.consume = true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return true if the event is consumed.
     */
    public boolean isConsumed() {
        return this.consume;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return a String representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[actionCommand=" + this.actionCommand + ",source=" + this.source + "]";
    }
}
