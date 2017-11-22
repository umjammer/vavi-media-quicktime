
package quicktime.app.event;

import java.util.Vector;

import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;


/**
 * @deprecated
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
     */
    public static void registerMaker(EventDispatcherMaker paramEventDispatcherMaker) {
        if (vec == null)
            vec = new Vector<>();
        vec.addElement(paramEventDispatcherMaker);
    }

    /**
     * @deprecated
     */
    public static void removeMaker(EventDispatcherMaker paramEventDispatcherMaker) {
        if (vec != null)
            vec.removeElement(paramEventDispatcherMaker);
        if (vec.size() == 0) {
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
                EventDispatcher localEventDispatcher = localObject.make(paramListenerController,
                                                                                                 paramSpace,
                                                                                                 paramObject);
                if (localEventDispatcher != null) {
                    return localEventDispatcher;
                }
            }
        }
        Class<?> localClass = paramObject.getClass();
        Object localObject = localClass.getName();
        while (!localClass.getName().equals("java.lang.Object")) {
            localObject = localClass.getName();
            localClass = localClass.getSuperclass();
        }

        if ((((String) localObject).equals("java.awt.Component")) && ((paramListenerController instanceof QTMouseController))) {
            return new AWTMouseEventDispatcher((QTMouseController) paramListenerController, paramSpace, paramObject);
        }
        return null;
    }

    protected abstract void addedTo();

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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /EventDispatcher.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
