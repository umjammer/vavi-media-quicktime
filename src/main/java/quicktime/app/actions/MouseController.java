
package quicktime.app.actions;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.spaces.Collection;
import quicktime.app.spaces.ListenerController;
import quicktime.app.spaces.Space;


/**
 * @deprecated
 */
public abstract class MouseController implements ListenerController, Collection, MouseListener, MouseMotionListener {
    private boolean wholespace;

    private Vector table;

    private MouseResponder responder;

    private Object mouseMoveCurrentTarget;

    private Object originalDragTarget;

    private Object currentTargetInDrag;

    protected int xLocationOffset;

    protected int yLocationOffset;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected MouseController(MouseResponder paramMouseResponder, boolean paramBoolean) {
        this.responder = paramMouseResponder;
        if (paramMouseResponder == null)
            throw new NullPointerException("Responder must NOT be null");
        this.wholespace = paramBoolean;
        this.table = new Vector();
        this.responder.controller = this;
    }

    /**
     * @deprecated
     */
    public final void addedToSpace(Space paramSpace) {
        setTargetSpace(paramSpace);
        this.responder.setTargetSpace(paramSpace);
    }

    /**
     * @deprecated
     */
    public void removedFromSpace() {
        setTargetSpace(null);
        this.responder.setTargetSpace(null);
    }

    /**
     * @deprecated
     */
    protected MouseResponder getResponder() {
        return this.responder;
    }

    /**
     * @deprecated
     */
    public void addedTo(Object paramObject) {
        if ((paramObject instanceof Component)) {
            if (this.responder.isInterested(51))
                ((Component) paramObject).addMouseListener(this);
            if (this.responder.isInterested(12)) {
                ((Component) paramObject).addMouseMotionListener(this);
            }
        }
    }

    /**
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
        if ((paramObject instanceof Component)) {
            ((Component) paramObject).removeMouseListener(this);
            ((Component) paramObject).removeMouseMotionListener(this);
        }
    }

    /**
     * @deprecated
     */
    public boolean isWholespace() {
        return this.wholespace;
    }

    /**
     * @deprecated
     */
    public void setWholespace(boolean paramBoolean) {
        this.wholespace = paramBoolean;
    }

    /**
     * @deprecated
     */
    public boolean addMember(Object paramObject) {
        if (!isAppropriate(paramObject))
            return false;
        if (!hasMember(paramObject))
            this.table.addElement(paramObject);
        return true;
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return this.responder.isAppropriate(paramObject);
    }

    /**
     * @deprecated
     */
    public void removeMember(Object paramObject) {
        this.table.removeElement(paramObject);
    }

    /**
     * @deprecated
     */
    public Enumeration members() {
        return this.table.elements();
    }

    /**
     * @deprecated
     */
    public boolean isEmpty() {
        return this.table.isEmpty();
    }

    /**
     * @deprecated
     */
    public int size() {
        return this.table.size();
    }

    /**
     * @deprecated
     */
    public boolean hasMember(Object paramObject) {
        return paramObject == null ? false : this.table.contains(paramObject);
    }

    /**
     * @deprecated
     */
    public void mouseEntered(MouseEvent paramMouseEvent) {
        if (this.responder.isInterested(16, paramMouseEvent.getModifiers())) {
            boolean bool = this.responder.isActive();
            if (!bool) {
                this.responder.activate(16);
                responderActivated();
            }
            this.responder.mouseEntered(paramMouseEvent);
            if (!bool) {
                deactivateResponder();
            }
        }
    }

    /**
     * @deprecated
     */
    public void mouseExited(MouseEvent paramMouseEvent) {
        if (this.responder.isInterested(32, paramMouseEvent.getModifiers())) {
            boolean bool = this.responder.isActive();
            if (!bool) {
                this.responder.activate(32);
                responderActivated();
            }
            this.responder.mouseExited(paramMouseEvent);
            if (this.mouseMoveCurrentTarget != null) {
                this.responder.mouseExitedTarget(paramMouseEvent);
                this.mouseMoveCurrentTarget = null;
            }
            if (bool) {
                deactivateResponder();
            }
        }
    }

    /**
     * @deprecated
     */
    protected void setTranslation() {
        this.xLocationOffset = 0;
        this.yLocationOffset = 0;
    }

    /**
     * @deprecated
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
        if (this.responder.isInterested(2, paramMouseEvent.getModifiers())) {
            setTranslation();
            paramMouseEvent.translatePoint(-this.xLocationOffset, -this.yLocationOffset);
            if (_activateResponder(paramMouseEvent, 2)) {
                this.responder.mousePressed(paramMouseEvent);
            }
            paramMouseEvent.translatePoint(this.xLocationOffset, this.yLocationOffset);
        }
    }

    /**
     * @deprecated
     */
    public void mouseReleased(MouseEvent paramMouseEvent) {
        if ((this.responder.isInterested(2, paramMouseEvent.getModifiers())) && (this.responder.isActive())) {
            setTranslation();
            paramMouseEvent.translatePoint(-this.xLocationOffset, -this.yLocationOffset);
            this.responder.mouseReleased(paramMouseEvent);
            if (this.mouseMoveCurrentTarget == null) {
                deactivateResponder();
            } else if (this.currentTargetInDrag == null) {
                deactivateResponder();
                this.mouseMoveCurrentTarget = null;
            }
            this.originalDragTarget = null;
            paramMouseEvent.translatePoint(this.xLocationOffset, this.yLocationOffset);
        }
    }

    /**
     * @deprecated
     */
    public void mouseClicked(MouseEvent paramMouseEvent) {
        if (this.responder.isInterested(1, paramMouseEvent.getModifiers())) {
            setTranslation();
            paramMouseEvent.translatePoint(-this.xLocationOffset, -this.yLocationOffset);
            if (_activateResponder(paramMouseEvent, 1)) {
                this.responder.mouseClicked(paramMouseEvent);
                deactivateResponder();
            }
            paramMouseEvent.translatePoint(this.xLocationOffset, this.yLocationOffset);
        }
    }

    /**
     * @deprecated
     */
    public void mouseMoved(MouseEvent paramMouseEvent) {
        if (!this.responder.isInterested(72, paramMouseEvent.getModifiers())) {
            return;
        }
        setTranslation();
        paramMouseEvent.translatePoint(-this.xLocationOffset, -this.yLocationOffset);
        if (this.responder.isInterested(64, paramMouseEvent.getModifiers())) {
            try {
                Object localObject = getSelected(paramMouseEvent);
                if (localObject == null) {
                    if (this.mouseMoveCurrentTarget != null) {
                        this.responder.mouseExitedTarget(paramMouseEvent);
                        deactivateResponder();
                        this.mouseMoveCurrentTarget = null;
                    }
                } else if (this.responder.isAppropriate(localObject)) {
                    if (this.mouseMoveCurrentTarget == null) {
                        this.responder.setTarget(localObject);
                        this.responder.activate(64);
                        responderActivated();
                        this.responder.mouseEnteredTarget(paramMouseEvent);
                        this.mouseMoveCurrentTarget = localObject;
                    } else if (this.mouseMoveCurrentTarget != localObject) {
                        this.responder.mouseExitedTarget(paramMouseEvent);
                        deactivateResponder();
                        this.responder.setTarget(localObject);
                        this.responder.activate(64);
                        responderActivated();
                        this.responder.mouseEnteredTarget(paramMouseEvent);
                        this.mouseMoveCurrentTarget = localObject;
                    } else {
                        this.responder.mouseMoved(paramMouseEvent);
                        paramMouseEvent.translatePoint(this.xLocationOffset, this.yLocationOffset);
                    }
                } else if (this.mouseMoveCurrentTarget != null) {
                    this.responder.mouseExitedTarget(paramMouseEvent);
                    deactivateResponder();
                    this.mouseMoveCurrentTarget = null;
                }
            } catch (QTException localQTException) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "getSelected");
            }
        }
        if (this.responder.isInterested(8, paramMouseEvent.getModifiers())) {
            boolean bool = this.responder.isActive();
            if (!bool) {
                this.responder.activate(8);
                responderActivated();
            }
            this.responder.mouseMoved(paramMouseEvent);
            if (!bool)
                deactivateResponder();
        }
        paramMouseEvent.translatePoint(this.xLocationOffset, this.yLocationOffset);
    }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        if ((this.responder.isInterested(4)) && (this.responder.isActive())) {
            setTranslation();
            paramMouseEvent.translatePoint(-this.xLocationOffset, -this.yLocationOffset);
            doDragTarget(paramMouseEvent);
            this.responder.mouseDragged(paramMouseEvent);
            paramMouseEvent.translatePoint(this.xLocationOffset, this.yLocationOffset);
        }
    }

    protected void doDragTarget(MouseEvent paramMouseEvent) {
        if (this.responder.isInterested(64, paramMouseEvent.getModifiers())) {
            try {
                Object localObject = getSelected(paramMouseEvent);
                if ((localObject == null) && (this.currentTargetInDrag != null)) {
                    this.responder.mouseExitedTarget(paramMouseEvent);
                    this.currentTargetInDrag = null;
                } else if ((localObject != null) && (this.currentTargetInDrag == null)
                           && (localObject == this.originalDragTarget)) {
                    this.responder.mouseEnteredTarget(paramMouseEvent);
                    this.currentTargetInDrag = localObject;
                }
            } catch (QTException localQTException) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "getSelected");
            }
        }
    }

    private boolean _activateResponder(MouseEvent paramMouseEvent, int paramInt) {
        try {
            Object localObject = getSelected(paramMouseEvent);
            if ((localObject != null) && (this.responder.isAppropriate(localObject))) {
                this.responder.activate(paramInt);
                this.responder.setTarget(localObject);
                this.originalDragTarget = localObject;
                this.currentTargetInDrag = localObject;
                responderActivated();
                return true;
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "activateResponder");
        }
        return false;
    }

    /**
     * @deprecated
     */
    protected void deactivateResponder() {
        this.responder.deactivate();
        this.originalDragTarget = null;
    }

    /**
     * @deprecated
     */
    public abstract Space getSpace();

    /**
     * @deprecated
     */
    protected abstract void setTargetSpace(Space paramSpace);

    /**
     * @deprecated
     */
    protected abstract Object getSelected(MouseEvent paramMouseEvent) throws QTException;

    /**
     * @deprecated
     */
    protected void responderActivated() {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/MouseController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
