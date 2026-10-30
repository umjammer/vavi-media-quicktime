package quicktime.app.actions;

import java.awt.event.MouseEvent;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.image.Transformable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.image.Matrix;


/**
 * The Dragger is a particular type of MouseResponder that performs the action
 * of moving the Dragger's target (a Transformable object) according to the
 * drag movements of the mouse.
 * <p>
 * Dragger's can be constrained to keep their target object completely within
 * their enclosing space, or allow the target to be dragged out of that space
 * to a default minimum view of 1 row or column of pixels of the target.
 * <p>
 * When a constraining condition is reached the Dragger will invoke the
 * trigger method of an attached Actionable object.
 * <p>
 * NOTE: This class is no longer used and has been replaced by the new event model
 * When converting your code, use a DragAction object in quicktime.app.event
 * <p>
 * Example:
 * <p>
 * Dragger dragger = new Dragger (MouseResponder.kAnyModifiersMask);
 * GroupController controller1 = new GroupController (dragger, true);
 * drawer.addController (controller1);
 * <p>
 * This code is replaced with:
 * <p>
 * QTMouseTargetController qtmc = new QTMouseTargetController(true);
 * qtmc.addQTMouseListener(new DragAction (new TranslateMatrix()));
 * drawer.addController (qtmc);
 *
 * @see quicktime.app.event.QTMouseController
 * @deprecated
 */
@Deprecated
public class Dragger extends MouseResponder {

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainNone = 0;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainLeft = 1;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainRight = 16;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainTop = 256;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainBottom = 4096;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainHorizontal = 17;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainVertical = 4352;

    /**
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     *
     * @deprecated
     */
    @Deprecated
    public static final int kConstrainAll = 4369;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Create a Dragger object
     *
     * @param modifierKeyMask if specified will determine which modifier keys must
     *                        be depressed for the action to be invoked. The modifierTestConditions is set
     *                        in this case to MouseResonder.kModifiersExactMatch
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public Dragger(int modifierKeyMask) {
        this(modifierKeyMask, -5, 0);
    }

    /**
     * Create a Dragger object with specified modifier keys and test conditions under
     * which those modifier keys are matched.
     *
     * @param modifierKeyMask        if specified will determine which modifier keys must
     *                               be depressed for the action to be invoked.
     * @param modifierTestConditions the test conditions under which the modifier mask is tested
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public Dragger(int modifierKeyMask, int modifierTestConditions) {
        this(modifierKeyMask, modifierTestConditions, 0);
    }

    /**
     * Set some parameters that will create DragActions.
     *
     * @param modifierKeyMask        if specified will determine which modifier keys must
     *                               be depressed for the action to be invoked.
     * @param modifierTestConditions the test conditions under which the modifier mask is tested
     * @param additionalEvents       additional mouse events the responder will respond to
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    public Dragger(int modifierKeyMask, int modifierTestConditions, int additionalEvents) {
        super(modifierKeyMask, modifierTestConditions, 0x6 | additionalEvents);
    }

    private int constrained = 4369;

    /**
     * The actor that will be invoked (if set) when the dragger
     * reaches a constraining condition
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Deprecated
    protected Actionable actor;

    /**
     * This is the Transformable target of the Dragger action
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected Transformable target;

    /**
     * This is the QTDrawable space within which the Dragger's target is enclosed and contained
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected QTDrawable space;

    /**
     * The number of pixels on the horizontal axis that an object can be dragged beyond the bounds
     * if the dragging action is unconstrained. The default is one
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected final int xBufferIfUnConstrained = 1;

    /**
     * The number of pixels on the vertical axis that an object can be dragged beyond the bounds
     * if the dragging action is unconstrained. The default is one
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected final int yBufferIfUnConstrained = 1;

    private QDPoint[] origPt;

    private QDPoint[] lastPt;

    private QDRect spaceRect;

    private Matrix invSpaceMatrix;

    private Matrix origTargetMatrix;

    /**
     * This method sets the containing Space of a target of a MouseResponder.
     * For a Dragger this space must be a QTDrawable.
     *
     * @param o the containing space of the potential targets of the Dragger.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void setTargetSpace(Object o) {
        this.space = ((QTDrawable) o);
    }

    /**
     * This method sets the actionable object of the action.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public void setActionable(Actionable actionable) {
        this.actor = actionable;
    }

    /**
     * This method retrieves the current actor that is attached to the action
     *
     * @since QTJava 6.1
     * @deprecated
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * The Dragger knows how to drag objects of type Transformable around.
     * Thus this method returns true if the incoming object implements the Transformable interface.
     *
     * @param object the object to test
     * @return a boolean
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public boolean isAppropriate(Object object) {
        return object instanceof Transformable;
    }

    /**
     * Sets the target of the Dragger. Generally this will be some kind of Transformable object
     * that the user has clicked on and the MouseController has extracted. This becomes
     * the active Target of the Dragger for the duration of the mouseDrag event.
     *
     * @param t the Transformable target
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void setTarget(Object t) {
        this.target = ((Transformable) t);
    }

    /**
     * When the MouseController detects that the drag operation is completed it notifies
     * the Dragger to remove its target as the user no longer is dragging.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void removeTarget() {
        this.target = null;
    }

    /**
     * Using the constraint constants dragging can be constrained in any direction.
     *
     * @param flag constraint flag
     * @since QTJava 6.1
     * @deprecated
     */
    public void setConstrained(int flag) {
        this.constrained = flag;
    }

    /**
     * @deprecated Returns the current constraint setting * @deprecated since QTJava 6.1
     */
    public int getConstrained() {
        return this.constrained;
    }

    /**
     * This method is called by the MouseController when the mouse is first pressed down on
     * a valid Transformable object within the space it is controlling.
     * For efficiency, it is assumed that the overall characteristics of the enclosing space
     * will not be changing during the duration of the drag event - ie. the Matrix
     * of the enclosing space is not changing during a mouse drag. However the Target's
     * matrix can and will change as a result of the drag operation.
     *
     * @param event the mouse down event that may begin the drag action
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mousePressed(MouseEvent event) {
        try {
            QDRect displayBounds = this.space.getDisplayBounds();
            this.invSpaceMatrix = new Matrix();
            Matrix matrix = this.space.getMatrix().copy();
            matrix.setTx(0.0F);
            matrix.setTy(0.0F);
            matrix.inverse(this.invSpaceMatrix);
            this.origPt = new QDPoint[1];
            this.origPt[0] = new QDPoint(event.getX(), event.getY());
            this.invSpaceMatrix.transformPoints(this.origPt);
            this.origTargetMatrix = this.target.getMatrix();
            this.spaceRect = displayBounds.copy();
            this.spaceRect.setX(0);
            this.spaceRect.setY(0);
            this.invSpaceMatrix.transformRect(this.spaceRect);
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this, "mousePressed");
        }
    }

    /**
     * This method will allow the user to drag the transformable object around.
     * This method also does constraints testing based on the current state
     * of the constraints.
     *
     * @param event the mouse drag event that triggered the drag action.
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        try {
            int i = 0;
            QDPoint[] qdPoints = new QDPoint[1];
            qdPoints[0] = new QDPoint(event.getX(), event.getY());
            this.invSpaceMatrix.transformPoints(qdPoints);
            int j = qdPoints[0].getX() - this.origPt[0].getX();
            int k = qdPoints[0].getY() - this.origPt[0].getY();
            Matrix matrix = this.origTargetMatrix.copy();
            matrix.translate(j, k);
            QDDimension originalSize = this.target.getOriginalSize();
            int m = originalSize.getWidth();
            int n = originalSize.getHeight();
            float f1 = matrix.getTx();
            float f2 = matrix.getTy();
            int i1 = this.spaceRect.getX();
            int i2 = this.spaceRect.getWidth() - m;
            int i3 = this.spaceRect.getY();
            int i4 = this.spaceRect.getHeight() - n;
            int i5 = matrix.getType();
            switch (i5) {
                case 0:
                case 1:
                    break;
                default:
                    QDRect rect = new QDRect(0, 0, m, n);
                    matrix.transformRect(rect);
                    m = rect.getWidth();
                    n = rect.getHeight();
                    i1 = (int) (matrix.getTx() - rect.getX() + 0.5F);
                    f1 -= i1;
                    i3 = (int) (matrix.getTy() - rect.getY() + 0.5F);
                    f2 -= i3;
                    i4 = this.spaceRect.getHeight() - n + i3;
                    i2 = this.spaceRect.getWidth() - m + i1;
            }
            if (f1 < this.spaceRect.getX()) {
                if ((this.constrained & 0x1) != 0) {
                    matrix.setTx(i1);
                    i = 1;
                } else if (f1 <= -m) {
                    matrix.setTx(-m + this.xBufferIfUnConstrained + i1);
                    i = 1;
                }
            } else if (f1 + m > this.spaceRect.getWidth()) {
                if ((this.constrained & 0x10) != 0) {
                    matrix.setTx(i2);
                    i = 1;
                } else if (f1 >= this.spaceRect.getWidth()) {
                    matrix.setTx(i2 + m - this.xBufferIfUnConstrained);
                    i = 1;
                }
            }
            if (f2 < this.spaceRect.getY()) {
                if ((this.constrained & 0x100) != 0) {
                    matrix.setTy(i3);
                    i = 1;
                } else if (f2 <= -n) {
                    matrix.setTy(-n + this.yBufferIfUnConstrained + i3);
                    i = 1;
                }
            } else if (f2 + n > this.spaceRect.getHeight()) {
                if ((this.constrained & 0x1000) != 0) {
                    matrix.setTy(i4);
                    i = 1;
                } else if (f2 >= this.spaceRect.getHeight()) {
                    matrix.setTy(i4 + n - this.yBufferIfUnConstrained);
                    i = 1;
                }
            }
            this.target.setMatrix(matrix);
            if ((i != 0) && (this.actor != null))
                this.actor.trigger();
        } catch (QTException e) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), this, "mouseDragged");
        }
    }
}
