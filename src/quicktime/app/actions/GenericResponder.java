
package quicktime.app.actions;

import java.awt.AWTEventMulticaster;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public class GenericResponder extends MouseResponder {
    protected Object target;

    protected Object space;

    private QTMouseMotionListener qtMouseListener;

    private MouseListener mouseListener;

    private MouseMotionListener mouseMotionListener;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public static GenericResponder asMouseListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 51);
    }

    /**
     * @deprecated
     */
    public static GenericResponder asMouseMotionListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 12);
    }

    /**
     * @deprecated
     */
    public static GenericResponder asMouseOrMouseMotionListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 63);
    }

    /**
     * @deprecated
     */
    public static GenericResponder asQTMouseMotionListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 76);
    }

    /**
     * @deprecated
     */
    public static GenericResponder asRolloverListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 100);
    }

    /**
     * @deprecated
     */
    public static GenericResponder asAnyMouseListener(int paramInt1, int paramInt2) {
        return new GenericResponder(paramInt1, paramInt2, 127);
    }

    /**
     * @deprecated
     */
    public GenericResponder(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, paramInt2, paramInt3);
    }

    /**
     * @deprecated
     */
    protected void setTargetSpace(Object paramObject) {
        this.space = paramObject;
    }

    /**
     * @deprecated
     */
    protected void setTarget(Object paramObject) {
        this.target = paramObject;
    }

    /**
     * @deprecated
     */
    protected void removeTarget() {
        this.target = null;
    }

    /**
     * @deprecated
     */
    public Object getSpace() {
        return this.space;
    }

    /**
     * @deprecated
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return true;
    }

    /**
     * @deprecated
     */
    public void mouseClicked(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseClicked(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseEntered(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseEntered(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseExited(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseExited(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null) {
            this.qtMouseListener.mouseEnteredTarget(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseExitedTarget(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null) {
            this.qtMouseListener.mouseExitedTarget(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseMoved(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null)
            this.qtMouseListener.mouseMoved(paramMouseEvent);
        if (this.mouseMotionListener != null) {
            this.mouseMotionListener.mouseMoved(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mousePressed(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        if (this.qtMouseListener != null)
            this.qtMouseListener.mouseDragged(paramMouseEvent);
        if (this.mouseMotionListener != null) {
            this.mouseMotionListener.mouseDragged(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void mouseReleased(MouseEvent paramMouseEvent) {
        if (this.mouseListener != null) {
            this.mouseListener.mouseReleased(paramMouseEvent);
        }
    }

    /**
     * @deprecated
     */
    public void addQTMouseMotionListener(QTMouseMotionListener paramQTMouseMotionListener) {
        if (isInterested(76)) {
            this.qtMouseListener = GREventMulticaster.add(this.qtMouseListener, paramQTMouseMotionListener);
        }
    }

    /**
     * @deprecated
     */
    public void removeQTMouseMotionListener(QTMouseMotionListener paramQTMouseMotionListener) {
        this.qtMouseListener = GREventMulticaster.remove(this.qtMouseListener, paramQTMouseMotionListener);
    }

    /**
     * @deprecated
     */
    public void addMouseListener(MouseListener paramMouseListener) {
        if (isInterested(51)) {
            this.mouseListener = GREventMulticaster.add(this.mouseListener, paramMouseListener);
        }
    }

    /**
     * @deprecated
     */
    public void removeMouseListener(MouseListener paramMouseListener) {
        this.mouseListener = GREventMulticaster.remove(this.mouseListener, paramMouseListener);
    }

    /**
     * @deprecated
     */
    public void addMouseMotionListener(MouseMotionListener paramMouseMotionListener) {
        if (isInterested(12)) {
            this.mouseMotionListener = GREventMulticaster.add(this.mouseMotionListener, paramMouseMotionListener);
        }
    }

    /**
     * @deprecated
     */
    public void removeMouseMotionListener(MouseMotionListener paramMouseMotionListener) {
        this.mouseMotionListener = GREventMulticaster.remove(this.mouseMotionListener, paramMouseMotionListener);
    }

    static class GREventMulticaster extends AWTEventMulticaster implements QTMouseMotionListener {
        GREventMulticaster(EventListener paramEventListener1, EventListener paramEventListener2) {
            super(paramEventListener1, paramEventListener2);
        }

        public static QTMouseMotionListener add(QTMouseMotionListener paramQTMouseMotionListener1,
                                                QTMouseMotionListener paramQTMouseMotionListener2) {
            if (paramQTMouseMotionListener1 == null)
                return paramQTMouseMotionListener2;
            if (paramQTMouseMotionListener2 == null)
                return paramQTMouseMotionListener1;
            return new GREventMulticaster(paramQTMouseMotionListener1, paramQTMouseMotionListener2);
        }

        public static QTMouseMotionListener remove(QTMouseMotionListener paramQTMouseMotionListener1,
                                                   QTMouseMotionListener paramQTMouseMotionListener2) {
            if ((paramQTMouseMotionListener1 == paramQTMouseMotionListener2) || (paramQTMouseMotionListener1 == null))
                return null;
            if ((paramQTMouseMotionListener1 instanceof GREventMulticaster)) {
                return (QTMouseMotionListener) ((GREventMulticaster) paramQTMouseMotionListener1)
                        .remove(paramQTMouseMotionListener2);
            }
            return paramQTMouseMotionListener1;
        }

        public void mouseEnteredTarget(MouseEvent paramMouseEvent) {
            ((QTMouseMotionListener) this.a).mouseEnteredTarget(paramMouseEvent);
            ((QTMouseMotionListener) this.b).mouseEnteredTarget(paramMouseEvent);
        }

        public void mouseExitedTarget(MouseEvent paramMouseEvent) {
            ((QTMouseMotionListener) this.a).mouseExitedTarget(paramMouseEvent);
            ((QTMouseMotionListener) this.b).mouseExitedTarget(paramMouseEvent);
        }
    }
}
