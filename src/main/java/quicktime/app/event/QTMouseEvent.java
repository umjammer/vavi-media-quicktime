package quicktime.app.event;

import java.util.EventObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDPoint;

/**
 * @deprecated
 * This class is used by QuickTime objects that have an action associated
 * with them. When those object's actions are fired they will generate
 * an action event, with those objects as the source of the event,
 * to any registered listeners
 *
 * A QTMouseEvent's source is the space within which the event occured.
 * Some controllers that broadcast a QTMouseEvent may also have
 * targetted the mouse event to a particular member object in the
 * space.
 */
public class QTMouseEvent extends EventObject {

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse clicked event type.
     */
    public static final int kMouseClick = 500;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse pressed event type.
     */
    public static final int kMousePressed = 501;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse released event type.
     */
    public static final int kMouseReleased = 502;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse moved event type.
     */
    public static final int kMouseMoved = 503;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse entered event type.
     */
    public static final int kMouseEntered = 504;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse exited event type.
     */
    public static final int kMouseExited = 505;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse dragged event type.
     */
    public static final int kMouseDragged = 506;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse entered a target
     */
    public static final int kMouseTargetEnter = 507;

    /**
     * @deprecated
     * @since QTJava 6.1
     * The mouse exited a target
     */
    public static final int kMouseTargetExit = 508;

    int modifiers;

    int x;

    int y;

    int eventType;

    int clickCount;

    long when;

    Object target;

    private QDPoint cacheLocationPt;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructs an QTMouseEvent object with the specified source object.
     * @param source the object where the event originated
     */
    public QTMouseEvent(Object source) {
        super(source);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructs an QTMouseEvent object with the specified source object.
     * @param source the object where the event originated
     * @param eventType the type of mouse event
     * @param x the x location of the event
     * @param y the y location of the event
     * @param when when the event occured
     * @param modifiers the modifiers held down during this action
     * @param clickCount the number of clicks within the system click time that occured.
     */
    public QTMouseEvent(Object source, int eventType, int x, int y, long when, int modifiers, int clickCount) {
        super(source);
        this.eventType = eventType;
        this.modifiers = modifiers;
        this.x = x;
        this.y = y;
        this.when = when;
        this.clickCount = clickCount;
    }

    private QTMouseEvent(Object paramObject, int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5, boolean paramBoolean) {
        super(paramObject);
    }

    /**
     * @deprecated
     * @return the location of the event in the space's local coordinates * @deprecated since QTJava 6.1
     */
    public QDPoint getLocation() {
        this.cacheLocationPt.setX(this.x);
        this.cacheLocationPt.setY(this.y);
        return this.cacheLocationPt;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the modifiers held down during this action event.
     */
    public int getModifiers() {
        return this.modifiers;
    }

    /**
     * @deprecated
     * @return the target or null of the event* @deprecated since QTJava 6.1
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @param target the target of the event
     */
    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the x position of the event relative to the source's enclosing space.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the y position of the event relative to the source's enclosing space.
     */
    public int getY() {
        return this.y;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the when the event happened.
     */
    public long getWhen() {
        return this.when;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the number of clicks.
     */
    public int getClickCount() {
        return this.clickCount;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the type of mouse event.
     */
    public int getEventType() {
        return this.eventType;
    }

    void setEventType(int paramInt) {
        this.eventType = paramInt;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Translates the location of the event.
     * @param x the offset to the x coordinate position
     * @param y the offset to the y coordinate position
     */
    public synchronized void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the characteristics of the MouseEvent. This will also set the target to null and consumed to false.
     * @param eventType the type of mouse event
     * @param x the x location of the event
     * @param y the y location of the event
     * @param when when the event occured
     * @param modifiers the modifiers held down during this action
     * @param clickCount the number of clicks within the system click time that occured.
     */
    public synchronized void initialiseEventProfile(int eventType, int x, int y, long when, int modifiers, int clickCount) {
        this.eventType = eventType;
        this.modifiers = modifiers;
        this.x = x;
        this.y = y;
        this.when = when;
        this.clickCount = clickCount;
        this.consumed = false;
        this.target = null;
    }

    private boolean consumed = false;

    /**
     * @deprecated
     * @since QTJava 6.1
     * QTMouseEvents can be consumed by any of their listeners.
     * If the event is consumed it will not be passed on to
     * any other listeners. Calling this method indicates that
     * the event is consumed.
     */
    public void consume() {
        this.consumed = true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return true if the event is consumed.
     */
    public boolean isConsumed() {
        return this.consumed;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return a String representation of this object.
     */
    public String toString() {
        String str1 = switch (this.eventType) {
            case 500 -> "click";
            case 501 -> "pressed";
            case 502 -> "released";
            case 503 -> "moved";
            case 506 -> "dragged";
            case 507 -> "targetEnter";
            case 508 -> "targetExit";
            case 504 -> "entered";
            case 505 -> "exited";
            default -> "unknown";
        };
        String str2 = "";
        if (this.target != null) {
            str2 = ",target=" + this.target;
        }
        return getClass().getName() + "[eventType=" + str1 + ",(" + this.x + "," + this.y + ")" + ",mods=" + this.modifiers + ",when=" + this.when + ",clicks=" + this.clickCount + ",consumed=" + this.consumed + ",source=" + this.source + str2 + "]";
    }
}
