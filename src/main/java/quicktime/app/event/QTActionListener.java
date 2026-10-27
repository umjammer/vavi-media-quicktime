package quicktime.app.event;

import java.util.EventListener;

/**
 * @deprecated
 * This Listener is used to register an interest in QTActionEvents
 * that maybe generated.
 */
public interface QTActionListener extends EventListener {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when an action occurs.
     */
    void actionPerformed(QTActionEvent paramQTActionEvent);
}
