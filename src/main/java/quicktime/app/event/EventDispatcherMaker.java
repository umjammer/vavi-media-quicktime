package quicktime.app.event;

import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;

/**
 * Deprecated.
 * Creates an object that will dispatch events to the specified
 * ListenerController that has been addedTo the specified space which
 * will become the source of events for that controller.
 * The interest object is the object that the space has been attached
 * to, and is generally the object where the events originate that
 * the dispatcher will then dispatch to the ListenerController
 *
 * If any of the three parameters are not suitable for a particular
 * event dispatcher then the make method can return null.
 * @see {@link quicktime.app.event.EventDispatcher}
 */
public abstract interface EventDispatcherMaker {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Make an EventDispathcer for the specified ListenerController, where
     * the events originate from the interest object.
     */
    public abstract EventDispatcher make(ListenerController paramListenerController, Space paramSpace, Object paramObject);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /EventDispatcherMaker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
