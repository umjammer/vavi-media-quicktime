
package quicktime.app.event;

import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;


/**
 * @deprecated
 */
public abstract interface EventDispatcherMaker {
    /**
     * @deprecated
     */
    public abstract EventDispatcher make(ListenerController paramListenerController, Space paramSpace, Object paramObject);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /EventDispatcherMaker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
