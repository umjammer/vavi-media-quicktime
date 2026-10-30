package quicktime.app.event;

import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;

/**
 * @deprecated
 * Creates an object that will dispatch events to the specified
 * ListenerController that has been addedTo the specified space which
 * will become the source of events for that controller.
 * The interest object is the object that the space has been attached
 * to, and is generally the object where the events originate that
 * the dispatcher will then dispatch to the ListenerController
 *
 * If any of the three parameters are not suitable for a particular
 * event dispatcher then the make method can return null.
 * @see quicktime.app.event.EventDispatcher
 */
public interface EventDispatcherMaker {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Make an EventDispathcer for the specified ListenerController, where
     * the events originate from the interest object.
     */
    EventDispatcher make(ListenerController listenerController, Space space, Object source);
}
