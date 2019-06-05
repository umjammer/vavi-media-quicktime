
package quicktime.app.event;

import java.util.EventObject;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.qd.QDPoint;


/**
 * @deprecated
 */
public class QTMouseEvent extends EventObject {
    /**
     * @deprecated
     */
    public static final int kMouseClick = 500;

    /**
     * @deprecated
     */
    public static final int kMousePressed = 501;

    /**
     * @deprecated
     */
    public static final int kMouseReleased = 502;

    /**
     * @deprecated
     */
    public static final int kMouseMoved = 503;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public static final int kMouseEntered = 504;

    /**
     * @deprecated
     */
    public static final int kMouseExited = 505;

    /**
     * @deprecated
     */
    public static final int kMouseDragged = 506;

    /**
     * @deprecated
     */
    public static final int kMouseTargetEnter = 507;

    /**
     * @deprecated
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
     */
    public QTMouseEvent(Object paramObject) {
        super(paramObject);
    }

    /**
     * @deprecated
     */
    public QTMouseEvent(Object paramObject,
            int paramInt1,
            int paramInt2,
            int paramInt3,
            long paramLong,
            int paramInt4,
            int paramInt5) {
        super(paramObject);
        this.eventType = paramInt1;
        this.modifiers = paramInt4;
        this.x = paramInt2;
        this.y = paramInt3;
        this.when = paramLong;
        this.clickCount = paramInt5;
    }

    private QTMouseEvent(Object paramObject,
            int paramInt1,
            int paramInt2,
            int paramInt3,
            long paramLong,
            int paramInt4,
            int paramInt5,
            boolean paramBoolean) {
        super(paramObject);
    }

    public QDPoint getLocation() {
        this.cacheLocationPt.setX(this.x);
        this.cacheLocationPt.setY(this.y);
        return this.cacheLocationPt;
    }

    /**
     * @deprecated
     */
    public int getModifiers() {
        return this.modifiers;
    }

    public Object getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     */
    public void setTarget(Object paramObject) {
        this.target = paramObject;
    }

    /**
     * @deprecated
     */
    public int getX() {
        return this.x;
    }

    /**
     * @deprecated
     */
    public int getY() {
        return this.y;
    }

    /**
     * @deprecated
     */
    public long getWhen() {
        return this.when;
    }

    /**
     * @deprecated
     */
    public int getClickCount() {
        return this.clickCount;
    }

    /**
     * @deprecated
     */
    public int getEventType() {
        return this.eventType;
    }

    void setEventType(int paramInt) {
        this.eventType = paramInt;
    }

    /**
     * @deprecated
     */
    public synchronized void translate(int paramInt1, int paramInt2) {
        this.x += paramInt1;
        this.y += paramInt2;
    }

    /**
     * @deprecated
     */
    public synchronized void initialiseEventProfile(int paramInt1,
                                                    int paramInt2,
                                                    int paramInt3,
                                                    long paramLong,
                                                    int paramInt4,
                                                    int paramInt5) {
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
     * @deprecated
     */
    public void consume() {
        this.consumed = true;
    }

    /**
     * @deprecated
     */
    public boolean isConsumed() {
        return this.consumed;
    }

    /**
     * @deprecated
     */
    public String toString() {
        String str1 = null;
        switch (this.eventType) {
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

        return getClass().getName() + "[eventType=" + str1 + ",(" + this.x + "," + this.y + ")" + ",mods=" + this.modifiers
               + ",when=" + this.when + ",clicks=" + this.clickCount + ",consumed=" + this.consumed + ",source=" + this.source
               + str2 + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTMouseEvent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
