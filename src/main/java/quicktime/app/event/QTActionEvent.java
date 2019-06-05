
package quicktime.app.event;

import java.util.EventObject;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
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
     */
    public QTActionEvent(Object paramObject, String paramString) {
        this(paramObject, paramString, 0);
    }

    /**
     * @deprecated
     */
    public QTActionEvent(Object paramObject, String paramString, int paramInt) {
        super(paramObject);
        this.actionCommand = paramString;
        this.modifiers = paramInt;
    }

    /**
     * @deprecated
     */
    public String getActionCommand() {
        return this.actionCommand;
    }

    /**
     * @deprecated
     */
    public int getModifiers() {
        return this.modifiers;
    }

    private boolean consume = false;

    /**
     * @deprecated
     */
    public void consume() {
        this.consume = true;
    }

    /**
     * @deprecated
     */
    public boolean isConsumed() {
        return this.consume;
    }

    /**
     * @deprecated
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
