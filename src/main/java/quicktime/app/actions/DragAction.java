package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.event.DragAdapter;
import quicktime.app.event.QTMouseEvent;
import quicktime.app.image.Transformable;


/**
 * The DragAction is a drag adapter that delegates the role of responding to a
 * mouse drag event to the TransformMatrix transformer. It is a utility class
 * that assumes that the mousePressed method will select a Transformable target
 * and the mouseDragged method will alter the matrix of this target
 * in some manner.
 *
 * @deprecated
 */
@Deprecated
public class DragAction extends DragAdapter {

    private Actionable actor;

    private TransformMatrix transformer;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Create a DragAction object.
     *
     * @param transformer this is the object that will apply the matrix transformations in response
     *                    to a mouseDragged event.
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public DragAction(TransformMatrix transformer) {
        this.transformer = transformer;
    }

    /**
     * This method retrieves the current actor that is attached to the action
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * This method sets the actionable object of the action.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public void setActionable(Actionable actionable) {
        this.actor = actionable;
    }

    /**
     * This method sets the actionable object of the action.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public void setTransformer(TransformMatrix transformer) {
        this.transformer = transformer;
    }

    /**
     * This method retrieves the current actor that is attached to the action
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public TransformMatrix getTransformer() {
        return this.transformer;
    }

    /**
     * Initializes the currently set TransformMatrix object to the current coordinates of
     * the mouse event.
     *
     * @param event the mouse event that triggers the drag action
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void mousePressed(QTMouseEvent event) {
        try {
            this.transformer.initialize((QTDisplaySpace) event.getSource(), (Transformable) event.getTarget(), event.getX(), event.getY());
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this, "mousePressed");
        }
    }

    /**
     * Responds to the mouseDragged event by calling the transformTarget (int x, int y) method
     * on the set TransformMatrix object.
     *
     * @param event the mouse event that triggers the drag action
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    @Deprecated
    public void mouseDragged(QTMouseEvent event) {
        try {
            if ((this.transformer.transformTarget(event.getX(), event.getY())) && (this.actor != null))
                this.actor.trigger();
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this, "mouseDragged");
        }
    }
}
