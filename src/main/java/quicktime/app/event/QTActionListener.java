package quicktime.app.event;

import java.util.EventListener;

/**
 * Deprecated.
 * This Listener is used to register an interest in QTActionEvents
 * that maybe generated.
 */
public abstract interface QTActionListener extends EventListener {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when an action occurs.
     */
    public abstract void actionPerformed(QTActionEvent paramQTActionEvent);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTActionListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
