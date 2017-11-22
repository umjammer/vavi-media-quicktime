
package quicktime.app.event;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.anim.SWCompositor;
import quicktime.app.display.GroupDrawable;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;
import quicktime.qd.QDRect;


/**
 * @deprecated
 */
public class QTMouseController implements ListenerController {
    private Object listenerSource;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private boolean isWithinItsSpace = false;

    private boolean isInEnclosingSpace = false;

    SWCompositor compSpace;

    QTDisplaySpace displaySpace;

    GroupDrawable drawSpace;

    EventDispatcher handler;

    MouseButtonListener mouseButtonListener;

    MouseEnterExitListener mouseEnterExitListener;

    MouseMoveListener mouseMoveListener;

    /**
     * @deprecated
     */
    protected Space space;

    /**
     * @deprecated
     */
    public Space getSpace() {
        return this.space;
    }

    /**
     * @deprecated
     */
    public void addedToSpace(Space paramSpace) {
        this.space = paramSpace;
        if ((this.space instanceof SWCompositor)) {
            this.compSpace = ((SWCompositor) this.space);
        } else if ((this.space instanceof GroupDrawable))
            this.drawSpace = ((GroupDrawable) this.space);
        if ((this.space instanceof QTDisplaySpace))
            this.displaySpace = ((QTDisplaySpace) this.space);
        if ((this.listenerSource != null) && (this.handler == null)) {
            addHandler();
        }
    }

    /**
     * @deprecated
     */
    public void removedFromSpace() {
        this.space = null;
        this.compSpace = null;
        this.displaySpace = null;
        this.drawSpace = null;
        if ((this.listenerSource != null) && (this.handler != null)) {
            removeActiveHandler();
        }
    }

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
        this.listenerSource = paramObject;
        if (this.handler == null) {
            addHandler();
        }
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
        this.listenerSource = null;
        if (this.handler != null) {
            removeActiveHandler();
        }
    }

    /**
     * @deprecated
     */
    protected void addHandler() {
        this.handler = EventDispatcher.makeDispatcher(this, getSpace(), this.listenerSource);
        if (this.handler != null) {
            this.handler.addedTo();
            if (hasMouseButtonListener())
                this.handler.addMouseButtonListener();
            if (hasMouseMoveListener())
                this.handler.addMouseMoveListener();
            if (hasMouseEnterExitListener()) {
                this.handler.addMouseEnterExitListener();
            }
        }
    }

    /**
     * @deprecated
     */
    protected void removeActiveHandler() {
        this.handler.removedFrom();
        this.handler = null;
    }

    /**
     * @deprecated
     */
    protected boolean translateAndIsWithin(QTMouseEvent paramQTMouseEvent) {
        if (this.displaySpace != null) {
            try {
                QDRect localQDRect1 = this.displaySpace.getDisplayBounds();
                int i = localQDRect1.getX();
                int j = localQDRect1.getY();
                if (this.compSpace != null) {
                    int k = i;
                    int m = j;

                    SWCompositor localSWCompositor1 = this.compSpace.getParent();
                    while (localSWCompositor1 != null) {
                        SWCompositor localSWCompositor2 = localSWCompositor1;
                        QDRect localQDRect2 = localSWCompositor2.getDisplayBounds();
                        k += localQDRect2.getX();
                        m += localQDRect2.getY();
                        localSWCompositor1 = localSWCompositor2.getParent();
                    }
                    paramQTMouseEvent.translate(-k, -m);
                } else {
                    paramQTMouseEvent.translate(-i, -j);
                }

                if (localQDRect1.inside(paramQTMouseEvent.getX() + i, paramQTMouseEvent.getY() + j)) {
                    return true;
                }
                return false;
            } catch (QTException localQTException) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.compSpace, "getDisplayBounds");
            }
        }
        return true;
    }

    /**
     * @deprecated
     */
    public void addQTMouseListener(QTMouseListener paramQTMouseListener) {
        if ((paramQTMouseListener instanceof MouseButtonListener))
            addMouseButtonListener((MouseButtonListener) paramQTMouseListener);
        if ((paramQTMouseListener instanceof MouseMoveListener))
            addMouseMoveListener((MouseMoveListener) paramQTMouseListener);
        if ((paramQTMouseListener instanceof MouseEnterExitListener)) {
            addMouseEnterExitListener((MouseEnterExitListener) paramQTMouseListener);
        }
    }

    /**
     * @deprecated
     */
    public void removeQTMouseListener(QTMouseListener paramQTMouseListener) {
        if ((paramQTMouseListener instanceof MouseButtonListener))
            removeMouseButtonListener((MouseButtonListener) paramQTMouseListener);
        if ((paramQTMouseListener instanceof MouseMoveListener))
            removeMouseMoveListener((MouseMoveListener) paramQTMouseListener);
        if ((paramQTMouseListener instanceof MouseEnterExitListener))
            removeMouseEnterExitListener((MouseEnterExitListener) paramQTMouseListener);
    }

    void addMouseButtonListener(MouseButtonListener paramMouseButtonListener) {
        boolean bool = hasMouseButtonListener();
        this.mouseButtonListener = QTEventCaster.add(this.mouseButtonListener, paramMouseButtonListener);
        updateMouseButtonListener(bool);
    }

    void removeMouseButtonListener(MouseButtonListener paramMouseButtonListener) {
        boolean bool = hasMouseButtonListener();
        this.mouseButtonListener = QTEventCaster.remove(this.mouseButtonListener, paramMouseButtonListener);
        updateMouseButtonListener(bool);
    }

    void updateMouseButtonListener(boolean paramBoolean) {
        if (this.handler != null) {
            if ((paramBoolean) && (!hasMouseButtonListener())) {
                this.handler.removeMouseButtonListener();
            } else if ((hasMouseButtonListener()) && (!paramBoolean))
                this.handler.addMouseButtonListener();
        }
    }

    boolean hasMouseButtonListener() {
        return this.mouseButtonListener != null;
    }

    /**
     * @deprecated
     */
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null)
            && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            && (translateAndIsWithin(paramQTMouseEvent))) {
            doMouseClicked(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseClicked(QTMouseEvent paramQTMouseEvent) {
        this.mouseButtonListener.mouseClicked(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null)
            && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            && (translateAndIsWithin(paramQTMouseEvent))) {
            doMousePressed(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void doMousePressed(QTMouseEvent paramQTMouseEvent) {
        this.mouseButtonListener.mousePressed(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null)
            && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))
            && (translateAndIsWithin(paramQTMouseEvent))) {
            doMouseReleased(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseReleased(QTMouseEvent paramQTMouseEvent) {
        this.mouseButtonListener.mouseReleased(paramQTMouseEvent);
    }

    void addMouseMoveListener(MouseMoveListener paramMouseMoveListener) {
        boolean bool = hasMouseMoveListener();
        this.mouseMoveListener = QTEventCaster.add(this.mouseMoveListener, paramMouseMoveListener);
        updateMouseMoveListener(bool);
    }

    void removeMouseMoveListener(MouseMoveListener paramMouseMoveListener) {
        boolean bool = hasMouseMoveListener();
        this.mouseMoveListener = QTEventCaster.remove(this.mouseMoveListener, paramMouseMoveListener);
        updateMouseMoveListener(bool);
    }

    void updateMouseMoveListener(boolean paramBoolean) {
        if (this.handler != null) {
            if ((paramBoolean) && (!hasMouseMoveListener())) {
                this.handler.removeMouseMoveListener();
            } else if ((hasMouseMoveListener()) && (!paramBoolean))
                this.handler.addMouseMoveListener();
        }
    }

    boolean hasMouseMoveListener() {
        return this.mouseMoveListener != null;
    }

    boolean isInterestedInMouseMoveEvents(QTMouseEvent paramQTMouseEvent) {
        return (this.mouseMoveListener != null)
               && (this.mouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()));
    }

    /**
     * @deprecated
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        int i = 0;
        if ((this.isInEnclosingSpace) && (this.mouseEnterExitListener != null)
            && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            boolean bool = this.isWithinItsSpace;
            this.isWithinItsSpace = translateAndIsWithin(paramQTMouseEvent);
            i = 1;
            if ((bool) && (!this.isWithinItsSpace)) {
                paramQTMouseEvent.setEventType(505);
                doMouseExited(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(506);
            } else if ((this.isWithinItsSpace) && (!bool)) {
                paramQTMouseEvent.setEventType(504);
                doMouseEntered(paramQTMouseEvent);
                paramQTMouseEvent.setEventType(506);
            }
        }

        if (isInterestedInMouseMoveEvents(paramQTMouseEvent)) {
            if (i == 0)
                translateAndIsWithin(paramQTMouseEvent);
            doMouseDragged(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseDragged(QTMouseEvent paramQTMouseEvent) {
        this.mouseMoveListener.mouseDragged(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    public void mouseMoved(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseEnterExitListener != null)
            && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            boolean bool = this.isWithinItsSpace;
            this.isWithinItsSpace = translateAndIsWithin(paramQTMouseEvent);
            if ((bool) && (!this.isWithinItsSpace)) {
                paramQTMouseEvent.setEventType(505);
                doMouseExited(paramQTMouseEvent);
            } else if ((this.isWithinItsSpace) && (!bool)) {
                paramQTMouseEvent.setEventType(504);
                doMouseEntered(paramQTMouseEvent);
            } else if (isInterestedInMouseMoveEvents(paramQTMouseEvent)) {
                doMouseMoved(paramQTMouseEvent, this.isWithinItsSpace);
            }
        } else if (isInterestedInMouseMoveEvents(paramQTMouseEvent)) {
            doMouseMoved(paramQTMouseEvent, translateAndIsWithin(paramQTMouseEvent));
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseMoved(QTMouseEvent paramQTMouseEvent, boolean paramBoolean) {
        if (paramBoolean)
            this.mouseMoveListener.mouseMoved(paramQTMouseEvent);
    }

    void addMouseEnterExitListener(MouseEnterExitListener paramMouseEnterExitListener) {
        boolean bool = hasMouseEnterExitListener();
        this.mouseEnterExitListener = QTEventCaster.add(this.mouseEnterExitListener, paramMouseEnterExitListener);
        updateMouseEnterExitListener(bool);
    }

    void removeMouseEnterExitListener(MouseEnterExitListener paramMouseEnterExitListener) {
        boolean bool = hasMouseEnterExitListener();
        this.mouseEnterExitListener = QTEventCaster.remove(this.mouseEnterExitListener, paramMouseEnterExitListener);
        updateMouseEnterExitListener(bool);
    }

    void updateMouseEnterExitListener(boolean paramBoolean) {
        if (this.handler != null) {
            if ((paramBoolean) && (!hasMouseEnterExitListener())) {
                this.handler.removeMouseEnterExitListener();
            } else if ((hasMouseEnterExitListener()) && (!paramBoolean))
                this.handler.addMouseEnterExitListener();
        }
    }

    boolean hasMouseEnterExitListener() {
        return this.mouseEnterExitListener != null;
    }

    /**
     * @deprecated
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseEnterExitListener != null)
            && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            this.isWithinItsSpace = translateAndIsWithin(paramQTMouseEvent);
            if (this.isWithinItsSpace) {
                doMouseEntered(paramQTMouseEvent);
            }
        }
        this.isInEnclosingSpace = true;
    }

    /**
     * @deprecated
     */
    protected void doMouseEntered(QTMouseEvent paramQTMouseEvent) {
        this.mouseEnterExitListener.mouseEntered(paramQTMouseEvent);
    }

    /**
     * @deprecated
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
        this.isInEnclosingSpace = false;
        if ((this.mouseEnterExitListener != null)
            && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            boolean bool = this.isWithinItsSpace;
            translateAndIsWithin(paramQTMouseEvent);
            this.isWithinItsSpace = false;
            if (bool) {
                doMouseExited(paramQTMouseEvent);
            }
        }
    }

    /**
     * @deprecated
     */
    protected void doMouseExited(QTMouseEvent paramQTMouseEvent) {
        this.mouseEnterExitListener.mouseExited(paramQTMouseEvent);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/event
 * /QTMouseController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
