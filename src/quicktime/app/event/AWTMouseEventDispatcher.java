
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
 */
public class AWTMouseEventDispatcher extends EventDispatcher implements MouseListener, MouseMotionListener {
    private QTMouseEvent theEvent;

    private boolean addedButtonForEE;

    private boolean addedMouseMoveForEE;

    private boolean addedMouseMoveForTarget;

    private boolean addedButtonForTarget;

    protected Space space;

    protected QTMouseController cont;

    protected Component comp;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public AWTMouseEventDispatcher(QTMouseController paramQTMouseController, Space paramSpace, Object paramObject) {
        this.comp = ((Component) paramObject);
        this.cont = paramQTMouseController;
        this.space = paramSpace;
    }

    private boolean hasMouseListener = false;

    private boolean hasMMotionListener = false;

    protected void addedTo() {
        if (this.theEvent == null) {
            this.theEvent = new QTMouseEvent(this.space);
        }
    }

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
        if (((this.cont instanceof QTMouseTargetController))
            && (((QTMouseTargetController) this.cont).hasMouseTargetListener())) {
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
        if (((this.cont instanceof QTMouseTargetController))
            && (((QTMouseTargetController) this.cont).hasMouseTargetListener())) {
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
     * @deprecated
     */
    public void mouseClicked(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(500,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mouseClicked(this.theEvent);
    }

    /**
     * @deprecated
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(501,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mousePressed(this.theEvent);
    }

    /**
     * @deprecated
     */
    public void mouseReleased(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(502,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mouseReleased(this.theEvent);
    }

    /**
     * @deprecated
     */
    public void mouseEntered(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(504,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mouseEntered(this.theEvent);
    }

    /**
     * @deprecated
     */
    public void mouseExited(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(505,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mouseExited(this.theEvent);
    }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(506,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mouseDragged(this.theEvent);
    }

    /**
     * @deprecated
     */
    public void mouseMoved(MouseEvent paramMouseEvent) {
        this.theEvent.initialiseEventProfile(503,
                                             paramMouseEvent.getX(),
                                             paramMouseEvent.getY(),
                                             paramMouseEvent.getWhen(),
                                             paramMouseEvent.getModifiers(),
                                             paramMouseEvent.getClickCount());
        this.cont.mouseMoved(this.theEvent);
    }
}
