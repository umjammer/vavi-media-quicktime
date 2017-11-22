
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.event.DragAdapter;
import quicktime.app.event.QTMouseEvent;
import quicktime.app.image.Transformable;


/**
 * @deprecated
 */
public class DragAction extends DragAdapter {
    private Actionable actor;

    private TransformMatrix transformer;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public DragAction(TransformMatrix paramTransformMatrix) {
        this.transformer = paramTransformMatrix;
    }

    /**
     * @deprecated
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * @deprecated
     */
    public void setActionable(Actionable paramActionable) {
        this.actor = paramActionable;
    }

    /**
     * @deprecated
     */
    public void setTransformer(TransformMatrix paramTransformMatrix) {
        this.transformer = paramTransformMatrix;
    }

    /**
     * @deprecated
     */
    public TransformMatrix getTransformer() {
        return this.transformer;
    }

    /**
     * @deprecated
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        try {
            this.transformer.initialize((QTDisplaySpace) paramQTMouseEvent.getSource(),
                                        (Transformable) paramQTMouseEvent.getTarget(),
                                        paramQTMouseEvent.getX(),
                                        paramQTMouseEvent.getY());

        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    /**
     * @deprecated
     */
    public void mouseDragged(QTMouseEvent paramQTMouseEvent) {
        try {
            if ((this.transformer.transformTarget(paramQTMouseEvent.getX(), paramQTMouseEvent.getY())) && (this.actor != null))
                this.actor.trigger();
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mouseDragged");
        }
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/DragAction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
