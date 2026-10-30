package quicktime.app.event;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Space;

/**
 * @deprecated
 * This class is used to dispatch events to a QTMouseController when the source of events is some kind
 * of java.awt.Component.
 * @see quicktime.app.event.EventDispatcher
 */
public class AWTMouseEventDispatcher extends EventDispatcher implements MouseListener, MouseMotionListener {

    private QTMouseEvent theEvent;

    private boolean addedButtonForEE;

    private boolean addedMouseMoveForEE;

    private boolean addedMouseMoveForTarget;

    private boolean addedButtonForTarget;

    /**
     * @deprecated
     * The space that is the source of the QTMouseEvent that is broadcast to the QTMouseController* @deprecated since QTJava 6.1
     */
    protected Space space;

    /**
     * @deprecated
     * The controller that will dispatch the events to any of its registered listeners* @deprecated since QTJava 6.1
     */
    protected QTMouseController cont;

    /**
     * @deprecated
     * The java.awt.Component which the space has been added to.* @deprecated since QTJava 6.1
     */
    protected Component comp;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Creates an AWTMouseEventDispatcher for the specified QTMouseController.
     * @param cont the controller which will distribute the events. This controller is attached to
     *  some kind of quicktime.app.display.QTDisplaySpace object.
     * @param interest The interest object must be some kind of java.awt.Component of a class cast exception
     *  will be generated.
     * @param space The space will be the Object (generally a QTDisplaySpace) which the controller
     *  has been attached to and will become the source of events - ie. the space within which the controllers
     *  events occur - for the controller
     */
    public AWTMouseEventDispatcher(QTMouseController cont, Space interest, Object space) {
        this.comp = ((Component) space);
        this.cont = cont;
        this.space = interest;
    }

    private boolean hasMouseListener = false;

    private boolean hasMMotionListener = false;

    /**
     * @deprecated
     * When the ListenerController is addedTo its source of interest this method is called.* @deprecated since QTJava 6.1
     */
    @Override
    protected void addedTo() {
        if (this.theEvent == null) {
            this.theEvent = new QTMouseEvent(this.space);
        }
    }

    /**
     * @deprecated
     * When the ListenerController is removedFrom its source of interest this method is called.* @deprecated since QTJava 6.1
     */
    @Override
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

    @Override
    void addMouseButtonListener() {
        addAWTMouseListener();
    }

    @Override
    void removeMouseButtonListener() {
        removeAWTMouseListener();
    }

    @Override
    void addMouseMoveListener() {
        addAllAWTMouseListeners();
    }

    @Override
    void removeMouseMoveListener() {
        removeAWTMouseListener();
        removeAWTMouseMotionListener();
    }

    @Override
    void addMouseEnterExitListener() {
        addAllAWTMouseListeners();
    }

    @Override
    void removeMouseEnterExitListener() {
        removeAWTMouseListener();
        removeAWTMouseMotionListener();
    }

    @Override
    void addMouseTargetListener() {
        addAllAWTMouseListeners();
    }

    @Override
    void removeMouseTargetListener() {
        removeAWTMouseListener();
        removeAWTMouseMotionListener();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse has been clicked on a component.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.theEvent.initialiseEventProfile(500, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mouseClicked(this.theEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button has been pressed on a component.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        this.theEvent.initialiseEventProfile(501, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mousePressed(this.theEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button has been released on a component.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        this.theEvent.initialiseEventProfile(502, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mouseReleased(this.theEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse enters a component.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        this.theEvent.initialiseEventProfile(504, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mouseEntered(this.theEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse exits a component.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.theEvent.initialiseEventProfile(505, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mouseExited(this.theEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  Mouse drag events will continue to be delivered to
     * the component where the first originated until the mouse button is
     * released (regardless of whether the mouse position is within the
     * bounds of the component).
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        this.theEvent.initialiseEventProfile(506, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mouseDragged(this.theEvent);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Invoked when the mouse button has been moved on a component
     * (with no buttons no down).
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        this.theEvent.initialiseEventProfile(503, e.getX(), e.getY(), e.getWhen(), e.getModifiers(), e.getClickCount());
        this.cont.mouseMoved(this.theEvent);
    }
}
