package quicktime.app.event;

import java.util.Vector;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;

/**
 * @deprecated
 * The EventDispatch is an abstract class that will create a subclass that will
 * dispatch events to the controller.
 * When creating an event dispatcher the make method will supply the ListenerController,
 * the space that that controller has been added to and the object that the space itself
 * has been added to. For instance adding a QTMouseController to a Compositor which is then
 * set as the client of a QTCanvas. In the make method any custom makers will be queried
 * to see if they can dispatch the kind of events the controller wants, for the space it
 * is attached to and the kind of object that the space itself has been added to. If any
 * of these objects are not appropriate objects for a given dispather the make method
 * can return null and some other event dispatcher can be found.
 *
 * Any custom makers that are registered will be queried first before the default system
 * dispatchers are queried.
 */
public abstract class EventDispatcher {

    private static Vector<EventDispatcherMaker> vec;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Register a custom event handler maker that will make an EventDispatcher
     * based on the type of controller, event interest and Space.
     */
    public static void registerMaker(EventDispatcherMaker paramEventDispatcherMaker) {
        if (vec == null)
            vec = new Vector<>();
        vec.addElement(paramEventDispatcherMaker);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Remove a custom event handler maker.
     */
    public static void removeMaker(EventDispatcherMaker paramEventDispatcherMaker) {
        if (vec != null)
            vec.removeElement(paramEventDispatcherMaker);
        if (vec.isEmpty()) {
            vec = null;
        }
    }

    /**
     * @deprecated
     */
    static EventDispatcher makeDispatcher(ListenerController paramListenerController, Space paramSpace, Object paramObject) {
        if (vec != null) {
            for (int i = 0; i < vec.size(); i++) {
                EventDispatcherMaker localObject = vec.elementAt(i);
                EventDispatcher localEventDispatcher = localObject.make(paramListenerController, paramSpace, paramObject);
                if (localEventDispatcher != null) {
                    return localEventDispatcher;
                }
            }
        }
        Class<?> localClass = paramObject.getClass();
        String localObject = localClass.getName();
        while (!localClass.getName().equals("java.lang.Object")) {
            localObject = localClass.getName();
            localClass = localClass.getSuperclass();
        }
        if ((localObject.equals("java.awt.Component")) && ((paramListenerController instanceof QTMouseController))) {
            return new AWTMouseEventDispatcher((QTMouseController) paramListenerController, paramSpace, paramObject);
        }
        return null;
    }

    /**
     * @deprecated
     * When the ListenerController is addedTo its source of interest this method is called.* @deprecated since QTJava 6.1
     */
    protected abstract void addedTo();

    /**
     * @deprecated
     * When the ListenerController is removedFrom its source of interest this method is called.* @deprecated since QTJava 6.1
     */
    protected abstract void removedFrom();

    void addMouseButtonListener() {
    }

    void removeMouseButtonListener() {
    }

    void addMouseMoveListener() {
    }

    void removeMouseMoveListener() {
    }

    void addMouseEnterExitListener() {
    }

    void removeMouseEnterExitListener() {
    }

    void addMouseTargetListener() {
    }

    void removeMouseTargetListener() {
    }
}
