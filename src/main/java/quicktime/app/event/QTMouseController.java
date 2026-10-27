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
 * A QTMouseController is attached to a Space. When the space is added to
 * its container (for instance a QTCanvas in the case of QTDisplaySpaces)
 * it will create an event dispatcher that will translate these container
 * events to the QTMouseEvents that this controller distributes to
 * its registered listeners.
 *
 * MouseClick, MousePress and MouseRelease events will contain a click count.
 * MouseEntered and MouseExited events will be generated when the mouse
 * is moved out of the source space that the controller is attached to.
 * MouseMoved events are generated whenever the mouse is moved within
 * the source space.
 * MouseDragged events are generated when the user presses the mouse within the
 * source space and moves the mouse with the button(s) still pressed. MouseDrag
 * events are generated even if the mouse is no longer in the space, until the user
 * releases the mouse buttons.
 *
 * Once created and attached to a space the program registers listeners with the
 * controller based on the type of mouse events a particular task requires.
 * The type of the registered listeners will determine the type of mouse events
 * that the controller will handle.
 * @see <A href="../../../quicktime/app/event/QTMouseTargetController.html">for mouse events that
 *  target a member of the source space.</A>
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
     * @since QTJava 6.1
     * The space which the controller is attached too. If the controller is not
     * attached to a space this variable will be null. It is set in the addedToSpace method.
     */
    protected Space space;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns the Space which the MouseController has been attached to.
     * @return the current space.
     */
    public Space getSpace() {
        return this.space;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the space when a Controller is being addedTo the Space s.
     * If the Controller is not an appropriate controller for that kind of Space this
     * method can throw an exception and the controller won't be added to the Space.
     * @param s the Space being added to
     * @see setTargetSpace()
     */
    @Override
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
     * @since QTJava 6.1
     * Called by the Space when a Controller is being removed
     * from a Space. This is only called by the Space on controllers that have
     * previously been added, thus the space they are being removed from
     * is the space they were added to. Sets the targetSpace to null.
     */
    @Override
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
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. When this method is called the source
     * of interest will determine the event handling mechanism that must be installed.
     * The event dispatch to the QTMouseController is handled by an EventDispatcher
     * that is created for the QTMouseController.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void addedTo(Object paramObject) {
        this.listenerSource = paramObject;
        if (this.handler == null) {
            addHandler();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    @Override
    public void removedFrom(Object paramObject) {
        this.listenerSource = null;
        if (this.handler != null) {
            removeActiveHandler();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called when a controller is added to its space and
     * the controller needs to create its event handler
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
     * @since QTJava 6.1
     * This method is called when a controller is removed from its space and it had an active event
     * handler
     */
    protected void removeActiveHandler() {
        this.handler.removedFrom();
        this.handler = null;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method returns true if the location of the QTMouseEvent is
     * within the Space's area, or false if it isn't. This test
     * assumes that the space is some kind of QTDisplaySpace, and can
     * be overriden for a custom Space.
     *
     * If the event is located within the space the coordinates of the event
     * will be translated into the local coordinates of the space, with the top, left
     * point of the space being 0,0. Returning true will also indicate that this
     * translation has been done on the event coordinates.
     * @param e the mouse event
     * @return a boolean
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
     * @since QTJava 6.1
     * Add a QTMouseListener for this controller. The controller
     * will take the appropriate action based on the known mouse listeners
     * in the quicktime.app.event package.
     * @param l a new QTMouseListener
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
     * @since QTJava 6.1
     * Remove a QTMouseListener for this controller.
     * @param l a new QTMouseListener
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
     * @since QTJava 6.1
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch click events that occur within its Space's area to those listeners.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseClicked(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) && (translateAndIsWithin(paramQTMouseEvent))) {
            doMouseClicked(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the mouse event
     */
    protected void doMouseClicked(QTMouseEvent paramQTMouseEvent) {
        this.mouseButtonListener.mouseClicked(paramQTMouseEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch press events that occur within its Space's area to those listeners.
     * @param e the QTMouseEvent of this type.
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) && (translateAndIsWithin(paramQTMouseEvent))) {
            doMousePressed(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMousePressed(QTMouseEvent paramQTMouseEvent) {
        this.mouseButtonListener.mousePressed(paramQTMouseEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch release events that occur within its Space's area to those listeners.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseReleased(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(paramQTMouseEvent.getModifiers())) && (translateAndIsWithin(paramQTMouseEvent))) {
            doMouseReleased(paramQTMouseEvent);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
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
        return (this.mouseMoveListener != null) && (this.mouseMoveListener.matchModifierFilter(paramQTMouseEvent.getModifiers()));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the QTMouseController has a MouseMoveListener(s) it will
     * dispatch drag events that occur within its Space's area to those listeners.
     * Mouse drag events will continue to be delivered to
     * the controller and its listeners where the press occured and until the mouse button is
     * released (regardless of whether the mouse is within the space or not).
     * @param e the QTMouseEvent of this type.
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        int i = 0;
        if ((this.isInEnclosingSpace) && (this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
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
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseDragged(QTMouseEvent paramQTMouseEvent) {
        this.mouseMoveListener.mouseDragged(paramQTMouseEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the QTMouseController has a MouseMoveListener(s) it will
     * dispatch move events that occur within its Space's area to those listeners.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseMoved(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
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
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     * @param isWithinItsSpace if true the mouse moved event is within the enclosing space. If false
     *  it isn't.
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
     * @since QTJava 6.1
     * If the QTMouseController has a MouseEnterExitListener(s) it will
     * dispatch a mouseEntered event when the mouse moves or is dragged into the space.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseEntered(QTMouseEvent paramQTMouseEvent) {
        if ((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
            this.isWithinItsSpace = translateAndIsWithin(paramQTMouseEvent);
            if (this.isWithinItsSpace) {
                doMouseEntered(paramQTMouseEvent);
            }
        }
        this.isInEnclosingSpace = true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseEntered(QTMouseEvent paramQTMouseEvent) {
        this.mouseEnterExitListener.mouseEntered(paramQTMouseEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If the QTMouseController has a MouseEnterExitListener(s) it will
     * dispatch a mouseExited event when the mouse moves or is dragged out of the space.
     * @param e the QTMouseEvent of this type.
     */
    public void mouseExited(QTMouseEvent paramQTMouseEvent) {
        this.isInEnclosingSpace = false;
        if ((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(paramQTMouseEvent.getModifiers()))) {
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
     * @since QTJava 6.1
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     * @param e the translated mouse event
     */
    protected void doMouseExited(QTMouseEvent paramQTMouseEvent) {
        this.mouseEnterExitListener.mouseExited(paramQTMouseEvent);
    }
}
