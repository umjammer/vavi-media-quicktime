package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.event.DragAdapter;
import quicktime.app.event.QTMouseEvent;
import quicktime.app.image.Transformable;

/**
 * Deprecated.
 * The DragAction is a drag adapter that delegates the role of responding to a
 * mouse drag event to the TransformMatrix transformer. It is a utility class
 * that assumes that the mousePressed method will select a Transformable target
 * and the mouseDragged method will alter the matrix of this target
 * in some manner.
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
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a DragAction object.
     * @param transformer this is the object that will apply the matrix transformations in reponse
     *  to a mouseDragged event.
     */
    public DragAction(TransformMatrix paramTransformMatrix) {
        this.transformer = paramTransformMatrix;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method retreives the current actor that is attached to the action
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the actionable object of the action.
     */
    public void setActionable(Actionable paramActionable) {
        this.actor = paramActionable;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the actionable object of the action.
     */
    public void setTransformer(TransformMatrix paramTransformMatrix) {
        this.transformer = paramTransformMatrix;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method retreives the current actor that is attached to the action
     */
    public TransformMatrix getTransformer() {
        return this.transformer;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Initializes the currently set TransformMatrix object to the current coordinates of
     * the mouse event.
     * @param event the mouse event that triggers the drag action
     */
    public void mousePressed(QTMouseEvent paramQTMouseEvent) {
        try {
            this.transformer.initialize((QTDisplaySpace) paramQTMouseEvent.getSource(), (Transformable) paramQTMouseEvent.getTarget(), paramQTMouseEvent.getX(), paramQTMouseEvent.getY());
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Responds to the mouseDragged event by calling the transformTarget (int x, int y) method
     * on the set TransformMatrix object.
     * @param event the mouse event that triggers the drag action
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

