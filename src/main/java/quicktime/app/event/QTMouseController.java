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
 * A QTMouseController is attached to a Space. When the space is added to
 * its container (for instance a QTCanvas in the case of QTDisplaySpaces)
 * it will create an event dispatcher that will translate these container
 * events to the QTMouseEvents that this controller distributes to
 * its registered listeners.
 * <p>
 * MouseClick, MousePress and MouseRelease events will contain a click count.
 * MouseEntered and MouseExited events will be generated when the mouse
 * is moved out of the source space that the controller is attached to.
 * MouseMoved events are generated whenever the mouse is moved within
 * the source space.
 * MouseDragged events are generated when the user presses the mouse within the
 * source space and moves the mouse with the button(s) still pressed. MouseDrag
 * events are generated even if the mouse is no longer in the space, until the user
 * releases the mouse buttons.
 * <p>
 * Once created and attached to a space the program registers listeners with the
 * controller based on the type of mouse events a particular task requires.
 * The type of the registered listeners will determine the type of mouse events
 * that the controller will handle.
 *
 * @see <A href="../../../quicktime/app/event/QTMouseTargetController.html">for mouse events that
 * target a member of the source space.</A>
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
     * The space which the controller is attached too. If the controller is not
     * attached to a space this variable will be null. It is set in the addedToSpace method.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected Space space;

    /**
     * This method returns the Space which the MouseController has been attached to.
     *
     * @return the current space.
     * @since QTJava 6.1
     * @deprecated
     */
    public Space getSpace() {
        return this.space;
    }

    /**
     * This method is called by the space when a Controller is being addedTo the Space s.
     * If the Controller is not an appropriate controller for that kind of Space this
     * method can throw an exception and the controller won't be added to the Space.
     *
     * @param s the Space being added to
     * @see "#setTargetSpace()"
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void addedToSpace(Space s) {
        this.space = s;
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
     * Called by the Space when a Controller is being removed
     * from a Space. This is only called by the Space on controllers that have
     * previously been added, thus the space they are being removed from
     * is the space they were added to. Sets the targetSpace to null.
     *
     * @since QTJava 6.1
     * @deprecated
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
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. When this method is called the source
     * of interest will determine the event handling mechanism that must be installed.
     * The event dispatch to the QTMouseController is handled by an EventDispatcher
     * that is created for the QTMouseController.
     *
     * @param interest the object that is to be the source of interest for the
     *                 the object that implements this interface.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void addedTo(Object interest) {
        this.listenerSource = interest;
        if (this.handler == null) {
            addHandler();
        }
    }

    /**
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest.
     *
     * @param interest the object that was the source of interest for the
     *                 the object that implements this interface.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void removedFrom(Object interest) {
        this.listenerSource = null;
        if (this.handler != null) {
            removeActiveHandler();
        }
    }

    /**
     * This method is called when a controller is added to its space and
     * the controller needs to create its event handler
     *
     * @since QTJava 6.1
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
     * This method is called when a controller is removed from its space and it had an active event
     * handler
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected void removeActiveHandler() {
        this.handler.removedFrom();
        this.handler = null;
    }

    /**
     * This method returns true if the location of the QTMouseEvent is
     * within the Space's area, or false if it isn't. This test
     * assumes that the space is some kind of QTDisplaySpace, and can
     * be overriden for a custom Space.
     * <p>
     * If the event is located within the space the coordinates of the event
     * will be translated into the local coordinates of the space, with the top, left
     * point of the space being 0,0. Returning true will also indicate that this
     * translation has been done on the event coordinates.
     *
     * @param e the mouse event
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    protected boolean translateAndIsWithin(QTMouseEvent e) {
        if (this.displaySpace != null) {
            try {
                QDRect displayBounds = this.displaySpace.getDisplayBounds();
                int i = displayBounds.getX();
                int j = displayBounds.getY();
                if (this.compSpace != null) {
                    int k = i;
                    int m = j;
                    SWCompositor parent = this.compSpace.getParent();
                    while (parent != null) {
                        SWCompositor swCompositor2 = parent;
                        QDRect rect2 = swCompositor2.getDisplayBounds();
                        k += rect2.getX();
                        m += rect2.getY();
                        parent = swCompositor2.getParent();
                    }
                    e.translate(-k, -m);
                } else {
                    e.translate(-i, -j);
                }
                if (displayBounds.inside(e.getX() + i, e.getY() + j)) {
                    return true;
                }
                return false;
            } catch (QTException ex) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(ex), this.compSpace, "getDisplayBounds");
            }
        }
        return true;
    }

    /**
     * Add a QTMouseListener for this controller. The controller
     * will take the appropriate action based on the known mouse listeners
     * in the quicktime.app.event package.
     *
     * @param l a new QTMouseListener
     * @since QTJava 6.1
     * @deprecated
     */
    public void addQTMouseListener(QTMouseListener l) {
        if ((l instanceof MouseButtonListener))
            addMouseButtonListener((MouseButtonListener) l);
        if ((l instanceof MouseMoveListener))
            addMouseMoveListener((MouseMoveListener) l);
        if ((l instanceof MouseEnterExitListener)) {
            addMouseEnterExitListener((MouseEnterExitListener) l);
        }
    }

    /**
     * Remove a QTMouseListener for this controller.
     *
     * @param l a new QTMouseListener
     * @since QTJava 6.1
     * @deprecated
     */
    public void removeQTMouseListener(QTMouseListener l) {
        if ((l instanceof MouseButtonListener))
            removeMouseButtonListener((MouseButtonListener) l);
        if ((l instanceof MouseMoveListener))
            removeMouseMoveListener((MouseMoveListener) l);
        if ((l instanceof MouseEnterExitListener))
            removeMouseEnterExitListener((MouseEnterExitListener) l);
    }

    void addMouseButtonListener(MouseButtonListener listener) {
        boolean hadListener = hasMouseButtonListener();
        this.mouseButtonListener = QTEventCaster.add(this.mouseButtonListener, listener);
        updateMouseButtonListener(hadListener);
    }

    void removeMouseButtonListener(MouseButtonListener oldl) {
        boolean hadListener = hasMouseButtonListener();
        this.mouseButtonListener = QTEventCaster.remove(this.mouseButtonListener, oldl);
        updateMouseButtonListener(hadListener);
    }

    void updateMouseButtonListener(boolean hadListener) {
        if (this.handler != null) {
            if ((hadListener) && (!hasMouseButtonListener())) {
                this.handler.removeMouseButtonListener();
            } else if ((hasMouseButtonListener()) && (!hadListener))
                this.handler.addMouseButtonListener();
        }
    }

    boolean hasMouseButtonListener() {
        return this.mouseButtonListener != null;
    }

    /**
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch click events that occur within its Space's area to those listeners.
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mouseClicked(QTMouseEvent e) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(e.getModifiers())) && (translateAndIsWithin(e))) {
            doMouseClicked(e);
        }
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e the mouse event
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMouseClicked(QTMouseEvent e) {
        this.mouseButtonListener.mouseClicked(e);
    }

    /**
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch press events that occur within its Space's area to those listeners.
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mousePressed(QTMouseEvent e) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(e.getModifiers())) && (translateAndIsWithin(e))) {
            doMousePressed(e);
        }
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e the translated mouse event
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMousePressed(QTMouseEvent e) {
        this.mouseButtonListener.mousePressed(e);
    }

    /**
     * If the QTMouseController has a MouseButtonListener(s) it will
     * dispatch release events that occur within its Space's area to those listeners.
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mouseReleased(QTMouseEvent e) {
        if ((this.mouseButtonListener != null) && (this.mouseButtonListener.matchModifierFilter(e.getModifiers())) && (translateAndIsWithin(e))) {
            doMouseReleased(e);
        }
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e the translated mouse event
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMouseReleased(QTMouseEvent e) {
        this.mouseButtonListener.mouseReleased(e);
    }

    void addMouseMoveListener(MouseMoveListener listener) {
        boolean hadListener = hasMouseMoveListener();
        this.mouseMoveListener = QTEventCaster.add(this.mouseMoveListener, listener);
        updateMouseMoveListener(hadListener);
    }

    void removeMouseMoveListener(MouseMoveListener oldl) {
        boolean hadListener = hasMouseMoveListener();
        this.mouseMoveListener = QTEventCaster.remove(this.mouseMoveListener, oldl);
        updateMouseMoveListener(hadListener);
    }

    void updateMouseMoveListener(boolean hadListener) {
        if (this.handler != null) {
            if ((hadListener) && (!hasMouseMoveListener())) {
                this.handler.removeMouseMoveListener();
            } else if ((hasMouseMoveListener()) && (!hadListener))
                this.handler.addMouseMoveListener();
        }
    }

    boolean hasMouseMoveListener() {
        return this.mouseMoveListener != null;
    }

    boolean isInterestedInMouseMoveEvents(QTMouseEvent qtMouseEvent) {
        return (this.mouseMoveListener != null) && (this.mouseMoveListener.matchModifierFilter(qtMouseEvent.getModifiers()));
    }

    /**
     * If the QTMouseController has a MouseMoveListener(s) it will
     * dispatch drag events that occur within its Space's area to those listeners.
     * Mouse drag events will continue to be delivered to
     * the controller and its listeners where the press occured and until the mouse button is
     * released (regardless of whether the mouse is within the space or not).
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mouseDragged(QTMouseEvent e) {
        int i = 0;
        if ((this.isInEnclosingSpace) && (this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(e.getModifiers()))) {
            boolean bool = this.isWithinItsSpace;
            this.isWithinItsSpace = translateAndIsWithin(e);
            i = 1;
            if ((bool) && (!this.isWithinItsSpace)) {
                e.setEventType(505);
                doMouseExited(e);
                e.setEventType(506);
            } else if ((this.isWithinItsSpace) && (!bool)) {
                e.setEventType(504);
                doMouseEntered(e);
                e.setEventType(506);
            }
        }
        if (isInterestedInMouseMoveEvents(e)) {
            if (i == 0)
                translateAndIsWithin(e);
            doMouseDragged(e);
        }
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e the translated mouse event
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMouseDragged(QTMouseEvent e) {
        this.mouseMoveListener.mouseDragged(e);
    }

    /**
     * If the QTMouseController has a MouseMoveListener(s) it will
     * dispatch move events that occur within its Space's area to those listeners.
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mouseMoved(QTMouseEvent e) {
        if ((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(e.getModifiers()))) {
            boolean bool = this.isWithinItsSpace;
            this.isWithinItsSpace = translateAndIsWithin(e);
            if ((bool) && (!this.isWithinItsSpace)) {
                e.setEventType(505);
                doMouseExited(e);
            } else if ((this.isWithinItsSpace) && (!bool)) {
                e.setEventType(504);
                doMouseEntered(e);
            } else if (isInterestedInMouseMoveEvents(e)) {
                doMouseMoved(e, this.isWithinItsSpace);
            }
        } else if (isInterestedInMouseMoveEvents(e)) {
            doMouseMoved(e, translateAndIsWithin(e));
        }
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e                the translated mouse event
     * @param isWithinItsSpace if true the mouse moved event is within the enclosing space. If false
     *                         it isn't.
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMouseMoved(QTMouseEvent e, boolean isWithinItsSpace) {
        if (isWithinItsSpace)
            this.mouseMoveListener.mouseMoved(e);
    }

    void addMouseEnterExitListener(MouseEnterExitListener listener) {
        boolean hadListener = hasMouseEnterExitListener();
        this.mouseEnterExitListener = QTEventCaster.add(this.mouseEnterExitListener, listener);
        updateMouseEnterExitListener(hadListener);
    }

    void removeMouseEnterExitListener(MouseEnterExitListener oldl) {
        boolean hadListener = hasMouseEnterExitListener();
        this.mouseEnterExitListener = QTEventCaster.remove(this.mouseEnterExitListener, oldl);
        updateMouseEnterExitListener(hadListener);
    }

    void updateMouseEnterExitListener(boolean hadListener) {
        if (this.handler != null) {
            if ((hadListener) && (!hasMouseEnterExitListener())) {
                this.handler.removeMouseEnterExitListener();
            } else if ((hasMouseEnterExitListener()) && (!hadListener))
                this.handler.addMouseEnterExitListener();
        }
    }

    boolean hasMouseEnterExitListener() {
        return this.mouseEnterExitListener != null;
    }

    /**
     * If the QTMouseController has a MouseEnterExitListener(s) it will
     * dispatch a mouseEntered event when the mouse moves or is dragged into the space.
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mouseEntered(QTMouseEvent e) {
        if ((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(e.getModifiers()))) {
            this.isWithinItsSpace = translateAndIsWithin(e);
            if (this.isWithinItsSpace) {
                doMouseEntered(e);
            }
        }
        this.isInEnclosingSpace = true;
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e the translated mouse event
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMouseEntered(QTMouseEvent e) {
        this.mouseEnterExitListener.mouseEntered(e);
    }

    /**
     * If the QTMouseController has a MouseEnterExitListener(s) it will
     * dispatch a mouseExited event when the mouse moves or is dragged out of the space.
     *
     * @param e the QTMouseEvent of this type.
     * @since QTJava 6.1
     * @deprecated
     */
    public void mouseExited(QTMouseEvent e) {
        this.isInEnclosingSpace = false;
        if ((this.mouseEnterExitListener != null) && (this.mouseEnterExitListener.matchModifierFilter(e.getModifiers()))) {
            boolean bool = this.isWithinItsSpace;
            translateAndIsWithin(e);
            this.isWithinItsSpace = false;
            if (bool) {
                doMouseExited(e);
            }
        }
    }

    /**
     * This method dispatches the event to the appropriate listeners. Subclasses can overide
     * this method to do any particular processing that is required for this type of mouse event.
     *
     * @param e the translated mouse event
     * @since QTJava 6.1
     * @deprecated
     */
    protected void doMouseExited(QTMouseEvent e) {
        this.mouseEnterExitListener.mouseExited(e);
    }
}
