package quicktime.app.event;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Space;

/**
 * Deprecated.
 * This class is used to dispatch events to a QTMouseController when the source of events is some kind
 * of java.awt.Component.
 * @see {@link quicktime.app.event.EventDispatcher}
 */
public class AWTMouseEventDispatcher extends EventDispatcher implements MouseListener, MouseMotionListener {

    private QTMouseEvent theEvent;

    private boolean addedButtonForEE;

    private boolean addedMouseMoveForEE;

    private boolean addedMouseMoveForTarget;

    private boolean addedButtonForTarget;

    /**
     * Deprecated.
     * The space that is the source of the QTMouseEvent that is broadcast to the QTMouseController* @deprecated since QTJava 6.1
     */
    protected Space space;

    /**
     * Deprecated.
     * The controller that will dispatch the events to any of its registered listeners* @deprecated since QTJava 6.1
     */
    protected QTMouseController cont;

    /**
     * Deprecated.
     * The java.awt.Component which the space has been added to.* @deprecated since QTJava 6.1
     */
    protected Component comp;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Creates an AWTMouseEventDispatcher for the specified QTMouseController.
     * @param cont the controller which will distribute the events. This controller is attached to
     *  some kind of quicktime.app.display.QTDisplaySpace object.
     * @param interest The interest object must be some kind of java.awt.Component of a class cast exception
     *  will be generated.
     * @param space The space will be the Object (generally a QTDisplaySpace) which the controller
     *  has been attached to and will become the source of events - ie. the space within which the controllers
     *  events occur - for the controller
     */
    public AWTMouseEventDispatcher(QTMouseController paramQTMouseController, Space paramSpace, Object paramObject) {
        this.comp = ((Component) paramObject);
        this.cont = paramQTMouseController;
        this.space = paramSpace;
    }

    private boolean hasMouseListener = false;

    private boolean hasMMotionListener = false;

    /**
     * Deprecated.
     * When the ListenerController is addedTo its source of interest this method is called.* @deprecated since QTJava 6.1
     */
    protected void addedTo() {
        if (this.theEvent == null) {
            this.theEvent = new QTMouseEvent(this.space);
        }
    }

    /**
     * Deprecated.
     * When the ListenerController is removedFrom its source of interest this method is called.* @deprecated since QTJava 6.1
     */
    protected void removedFrom() {
        this.comp.removeMouseListener(this);
        this.comp.removeMouseMotionListener(this);
        this.comp = null;
        this.theEvent = null;
        this.hasMouseListener = false;
        this.hasMMotionListener = false;
    }

    private void addAWTMouseListener() {
        if (!this.hasMouseListener) {
            this.comp.addMouseListener(this);
            this.hasMouseListener = true;
        }
    }

    private void addAllAWTMouseListeners() {
        addAWTMouseListener();
        if (!this.hasMMotionListener) {
            this.comp.addMouseMotionListener(this);
            this.hasMMotionListener = true;
        }
    }

    private void removeAWTMouseListener() {
        if (this.cont.hasMouseButtonListener())
            return;
        if (this.cont.hasMouseMoveListener())
            return;
        if (this.cont.hasMouseEnterExitListener())
            return;
        if (((this.cont instanceof QTMouseTargetController)) && (((QTMouseTargetController) this.cont).hasMouseTargetListener())) {
            return;
        }
        this.comp.removeMouseListener(this);
        this.hasMouseListener = false;
    }

    private void removeAWTMouseMotionListener() {
        if (this.cont.hasMouseMoveListener())
            return;
        if (this.cont.hasMouseEnterExitListener())
            return;
        if (((this.cont instanceof QTMouseTargetController)) && (((QTMouseTargetController) this.cont).hasMouseTargetListener())) {
            return;
        }
        this.comp.removeMouseMotionListener(this);
        this.hasMMotionListener = false;
    }

    void addMouseButtonListener() {
        addAWTMouseListener();
    }

    void removeMouseButtonListener() {
        removeAWTMouseListener();
    }

    void addMouseMoveListener() {
        addAllAWTMouseListeners();
    }

    void removeMouseMoveListener() {
        removeAWTMouseListener();
        removeAWTMouseMotionListener();
    }

    void addMouseEnterExitListener() {
        addAllAWTMouseListeners();
    }

    void removeMouseEnterExitListener() {
        removeAWTMouseListener();
        removeAWTMouseMotionListener();
    }

    void addMouseTargetListener() {
        addAllAWTMouseListeners();
    }

    void removeMouseTargetListener() {
        removeAWTMouseListener();
        removeAWTMouseMotionListener();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse has been clicked on a component.
     */
    public void mouseClicked(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(500, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mouseClicked(this.theEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(501, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mousePressed(this.theEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(502, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mouseReleased(this.theEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(504, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mouseEntered(this.theEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(505, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mouseExited(this.theEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the component where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the component).
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(506, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mouseDragged(this.theEvent);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
    public void mouseMoved(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(503, paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getClickCount());
        this.cont.mouseMoved(this.theEvent);
    }
}
