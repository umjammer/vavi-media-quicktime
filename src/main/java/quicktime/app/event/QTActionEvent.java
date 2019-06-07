package quicktime.app.event;

import java.util.EventObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Constructs an QTActionEvent object with the specified source object.
     * @param source the object where the event originated
     * @param command the command string for this action event
     */
    public QTActionEvent(Object paramObject, String paramString) {
        this(paramObject, paramString, 0);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the command name associated with this action.
     */
    public String getActionCommand() {
        return this.actionCommand;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the modifiers held down during this action event.
     */
    public int getModifiers() {
        return this.modifiers;
    }

    private boolean consume = false;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTActionEvents can be consumed by any of their listeners.
     * If the event is consumed it will not be passed on to
     * any other listeners. Calling this method indicates that
     * the event is consumed.
     */
    public void consume() {
        this.consume = true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return true if the event is consumed.
     */
    public boolean isConsumed() {
        return this.consume;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return a String representation of this object.
     */
    public String toString() {
        return getClass().getName() + "[actionCommand=" + this.actionCommand + ",source=" + this.source + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTActionEvent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
