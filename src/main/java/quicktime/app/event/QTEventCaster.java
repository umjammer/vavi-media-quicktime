
package quicktime.app.event;

import java.util.EventListener;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public class QTEventCaster implements
                           EventListener,
                           QTActionListener,
                           MouseButtonListener,
                           MouseMoveListener,
                           MouseEnterExitListener,
                           MouseTargetListener {
    protected final EventListener a;

    protected final EventListener b;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected QTEventCaster(EventListener paramEventListener1, EventListener paramEventListener2) {
        this.a = paramEventListener1;
        this.b = paramEventListener2;
    }

    /**
     * @deprecated
     */
    public static MouseButtonListener add(MouseButtonListener paramMouseButtonListener1,
                                          MouseButtonListener paramMouseButtonListener2) {
        return (MouseButtonListener) addListener(paramMouseButtonListener1, paramMouseButtonListener2);
    }

    /**
     * @deprecated
     */
    public static MouseButtonListener remove(MouseButtonListener paramMouseButtonListener1,
                                             MouseButtonListener paramMouseButtonListener2) {
        return (MouseButtonListener) removeListener(paramMouseButtonListener1, paramMouseButtonListener2);
    }

    /**
     * @deprecated
     */
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
        MouseButtonListener localMouseButtonListener = (MouseButtonListener) this.a;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mouseClicked(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseButtonListener = (MouseButtonListener) this.b;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseButtonListener.mouseClicked(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        MouseButtonListener localMouseButtonListener = (MouseButtonListener) this.a;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mousePressed(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseButtonListener = (MouseButtonListener) this.b;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mousePressed(paramQTMouseEvent);
    }

    public boolean matchModifierFilter(int paramInt) {
        return true;
    }

    /**
     * @deprecated
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        MouseButtonListener localMouseButtonListener = (MouseButtonListener) this.a;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mouseReleased(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseButtonListener = (MouseButtonListener) this.b;
        if (localMouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseButtonListener.mouseReleased(paramQTMouseEvent);
    }

    public static MouseMoveListener add(MouseMoveListener paramMouseMoveListener1, MouseMoveListener paramMouseMoveListener2) {
        return (MouseMoveListener) addListener(paramMouseMoveListener1, paramMouseMoveListener2);
    }

    /**
     * @deprecated
     */
    public static MouseMoveListener remove(MouseMoveListener paramMouseMoveListener1,
                                           MouseMoveListener paramMouseMoveListener2) {
        return (MouseMoveListener) removeListener(paramMouseMoveListener1, paramMouseMoveListener2);
    }

    /**
     * @deprecated
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        MouseMoveListener localMouseMoveListener = (MouseMoveListener) this.a;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseMoveListener.mouseDragged(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseMoveListener = (MouseMoveListener) this.b;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseMoveListener.mouseDragged(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseMoved(QTMouseEvent paramQTMouseEvent) {
        MouseMoveListener localMouseMoveListener = (MouseMoveListener) this.a;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseMoveListener.mouseMoved(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseMoveListener = (MouseMoveListener) this.b;
        if (localMouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseMoveListener.mouseMoved(paramQTMouseEvent);
    }

    public static MouseEnterExitListener add(MouseEnterExitListener paramMouseEnterExitListener1,
                                             MouseEnterExitListener paramMouseEnterExitListener2) {
        return (MouseEnterExitListener) addListener(paramMouseEnterExitListener1, paramMouseEnterExitListener2);
    }

    /**
     * @deprecated
     */
    public static MouseEnterExitListener remove(MouseEnterExitListener paramMouseEnterExitListener1,
                                                MouseEnterExitListener paramMouseEnterExitListener2) {
        return (MouseEnterExitListener) removeListener(paramMouseEnterExitListener1, paramMouseEnterExitListener2);
    }

    /**
     * @deprecated
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
        MouseEnterExitListener localMouseEnterExitListener = (MouseEnterExitListener) this.a;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseEnterExitListener.mouseEntered(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseEnterExitListener = (MouseEnterExitListener) this.b;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseEnterExitListener.mouseEntered(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
        MouseEnterExitListener localMouseEnterExitListener = (MouseEnterExitListener) this.a;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseEnterExitListener.mouseExited(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseEnterExitListener = (MouseEnterExitListener) this.b;
        if (localMouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseEnterExitListener.mouseExited(paramQTMouseEvent);
    }

    public static MouseTargetListener add(MouseTargetListener paramMouseTargetListener1,
                                          MouseTargetListener paramMouseTargetListener2) {
        return (MouseTargetListener) addListener(paramMouseTargetListener1, paramMouseTargetListener2);
    }

    public static MouseTargetListener remove(MouseTargetListener paramMouseTargetListener1,
                                             MouseTargetListener paramMouseTargetListener2) {
        return (MouseTargetListener) removeListener(paramMouseTargetListener1, paramMouseTargetListener2);
    }

    /**
     * @deprecated
     */
    public void mouseTargetEntered(QTMouseEvent paramQTMouseEvent) {
        MouseTargetListener localMouseTargetListener = (MouseTargetListener) this.a;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseTargetListener = (MouseTargetListener) this.b;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseTargetListener.mouseTargetEntered(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseTargetExited(QTMouseEvent paramQTMouseEvent) {
        MouseTargetListener localMouseTargetListener = (MouseTargetListener) this.a;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            localMouseTargetListener.mouseTargetExited(paramQTMouseEvent);
        if (paramQTMouseEvent.isConsumed())
            return;
        localMouseTargetListener = (MouseTargetListener) this.b;
        if (localMouseTargetListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) {
            localMouseTargetListener.mouseTargetExited(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public static QTActionListener add(QTActionListener paramQTActionListener1, QTActionListener paramQTActionListener2) {
        return (QTActionListener) addListener(paramQTActionListener1, paramQTActionListener2);
    }

    /**
     * @deprecated
     */
    public static QTActionListener remove(QTActionListener paramQTActionListener1, QTActionListener paramQTActionListener2) {
        return (QTActionListener) removeListener(paramQTActionListener1, paramQTActionListener2);
    }

    /**
     * @deprecated
     */
    public void actionPerformed(QTActionEvent paramQTActionEvent) {
        ((QTActionListener) this.a).actionPerformed(paramQTActionEvent);
        if (paramQTActionEvent.isConsumed())
            return;
        ((QTActionListener) this.b).actionPerformed(paramQTActionEvent);
    }

    /**
     * @deprecated
     */
    protected static EventListener addListener(EventListener paramEventListener1, EventListener paramEventListener2) {
        if (paramEventListener1 == null)
            return paramEventListener2;
        if (paramEventListener2 == null)
            return paramEventListener1;
        return new QTEventCaster(paramEventListener1, paramEventListener2);
    }

    /**
     * @deprecated
     */
    protected static EventListener removeListener(EventListener paramEventListener1, EventListener paramEventListener2) {
        if ((paramEventListener1 == paramEventListener2) || (paramEventListener1 == null))
            return null;
        if ((paramEventListener1 instanceof QTEventCaster)) {
            return ((QTEventCaster) paramEventListener1)._remove(paramEventListener2);
        }
        return paramEventListener1;
    }

    EventListener _remove(EventListener paramEventListener) {
        if (paramEventListener == this.a)
            return this.b;
        if (paramEventListener == this.b)
            return this.a;
        EventListener localEventListener1 = removeListener(this.a, paramEventListener);
        EventListener localEventListener2 = removeListener(this.b, paramEventListener);
        if ((localEventListener1 == this.a) && (localEventListener2 == this.b)) {
            return this;
        }
        return addListener(localEventListener1, localEventListener2);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTEventCaster.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
