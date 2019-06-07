package quicktime.app.event;

import java.util.EventObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDPoint;

/**
 * Deprecated.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse clicked event type.
     */
    public static final int kMouseClick = 500;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse pressed event type.
     */
    public static final int kMousePressed = 501;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse released event type.
     */
    public static final int kMouseReleased = 502;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse moved event type.
     */
    public static final int kMouseMoved = 503;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse entered event type.
     */
    public static final int kMouseEntered = 504;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse exited event type.
     */
    public static final int kMouseExited = 505;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse dragged event type.
     */
    public static final int kMouseDragged = 506;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The mouse entered a target
     */
    public static final int kMouseTargetEnter = 507;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Constructs an QTMouseEvent object with the specified source object.
     * @param source the object where the event originated
     */
    public QTMouseEvent(Object paramObject) {
        super(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Constructs an QTMouseEvent object with the specified source object.
     * @param source the object where the event originated
     * @param eventType the type of mouse event
     * @param x the x location of the event
     * @param y the y location of the event
     * @param when when the event occured
     * @param modifiers the modifiers held down during this action
     * @param clickCount the number of clicks within the system click time that occured.
     */
    public QTMouseEvent(Object paramObject, int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5) {
        super(paramObject);
        this.eventType = paramInt1;
        this.modifiers = paramInt4;
        this.x = paramInt2;
        this.y = paramInt3;
        this.when = paramLong;
        this.clickCount = paramInt5;
    }

    private QTMouseEvent(Object paramObject, int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5, boolean paramBoolean) {
        super(paramObject);
    }

    /**
     * Deprecated.
     * @return the location of the event in the space's local coordinates * @deprecated since QTJava 6.1
     */
    public QDPoint getLocation() {
        this.cacheLocationPt.setX(this.x);
        this.cacheLocationPt.setY(this.y);
        return this.cacheLocationPt;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the modifiers held down during this action event.
     */
    public int getModifiers() {
        return this.modifiers;
    }

    /**
     * Deprecated.
     * @return the target or null of the event* @deprecated since QTJava 6.1
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @param target the target of the event
     */
    public void setTarget(Object paramObject) {
        this.target = paramObject;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the x position of the event relative to the source's enclosing space.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the y position of the event relative to the source's enclosing space.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the when the event happened.
     */
    public long getWhen() {
        return this.when;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the number of clicks.
     */
    public int getClickCount() {
        return this.clickCount;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return the type of mouse event.
     */
    public int getEventType() {
        return this.eventType;
    }

    void setEventType(int paramInt) {
        this.eventType = paramInt;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Translates the location of the event.
     * @param x the offset to the x coordinate position
     * @param y the offset to the y coordinate position
     */
    public synchronized void translate(int paramInt1, int paramInt2) {
        this.x += paramInt1;
        this.y += paramInt2;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the characteristics of the MouseEvent. This will also set the target to null and consumed to false.
     * @param eventType the type of mouse event
     * @param x the x location of the event
     * @param y the y location of the event
     * @param when when the event occured
     * @param modifiers the modifiers held down during this action
     * @param clickCount the number of clicks within the system click time that occured.
     */
    public synchronized void initialiseEventProfile(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5) {
        this.eventType = paramInt1;
        this.modifiers = paramInt4;
        this.x = paramInt2;
        this.y = paramInt3;
        this.when = paramLong;
        this.clickCount = paramInt5;
        this.consumed = false;
        this.target = null;
    }

    private boolean consumed = false;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTMouseEvents can be consumed by any of their listeners.
     * If the event is consumed it will not be passed on to
     * any other listeners. Calling this method indicates that
     * the event is consumed.
     */
    public void consume() {
        this.consumed = true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return true if the event is consumed.
     */
    public boolean isConsumed() {
        return this.consumed;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @return a String representation of this object.
     */
    public String toString() {
        String str1 = null;
        switch(this.eventType) {
            case 500:
                str1 = "click";
                break;
            case 501:
                str1 = "pressed";
                break;
            case 502:
                str1 = "released";
                break;
            case 503:
                str1 = "moved";
                break;
            case 506:
                str1 = "dragged";
                break;
            case 507:
                str1 = "targetEnter";
                break;
            case 508:
                str1 = "targetExit";
                break;
            case 504:
                str1 = "entered";
                break;
            case 505:
                str1 = "exited";
                break;
            default:
                str1 = "unknown";
        }
        String str2 = "";
        if (this.target != null) {
            str2 = ",target=" + this.target;
        }
        return getClass().getName() + "[eventType=" + str1 + ",(" + this.x + "," + this.y + ")" + ",mods=" + this.modifiers + ",when=" + this.when + ",clicks=" + this.clickCount + ",consumed=" + this.consumed + ",source=" + this.source + str2 + "]";
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTMouseEvent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
