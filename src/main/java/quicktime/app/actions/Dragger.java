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
 * Deprecated.
 * The Dragger is a particular type of MouseResponder that performs the action
 * of moving the Dragger's target (a Transformable object) according to the
 * drag movements of the mouse.
 *
 * Dragger's can be constrained to keep their target object completely within
 * their enclosing space, or allow the target to be dragged out of that space
 * to a default minimum view of 1 row or column of pixels of the target.
 *
 * When a constraining condition is reached the Dragger will invoke the
 * trigger method of an attached Actionable object.
 *
 * NOTE: This class is no longer used and has been replaced by the new event model
 * When converting your code, use a DragAction object in quicktime.app.event
 *
 * Example:
 *
 * Dragger dragger = new Dragger (MouseResponder.kAnyModifiersMask);
 * GroupController controller1 = new GroupController (dragger, true);
 * drawer.addController (controller1);
 *
 * This code is replaced with:
 *
 * QTMouseTargetController qtmc = new QTMouseTargetController(true);
 * qtmc.addQTMouseListener(new DragAction (new TranslateMatrix()));
 * drawer.addController (qtmc);
 *
 * @see {@link quicktime.app.event.QTMouseController}
 */
public class Dragger extends MouseResponder {

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainNone = 0;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainLeft = 1;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainRight = 16;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainTop = 256;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainBottom = 4096;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainHorizontal = 17;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainVertical = 4352;

    /**
     * Deprecated.
     * Use these as constraint flags to restrict dragging operations* @deprecated since QTJava 6.1
     */
    public static final int kConstrainAll = 4369;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a Dragger object
     * @param modifierKeyMask - if specified will determine which modifier keys must
     *  be depressed for the action to be invoked. The modifierTestConditions is set
     *  in this case to MouseResonder.kModifiersExactMatch
     */
    public Dragger(int paramInt) {
        this(paramInt, -5, 0);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a Dragger object with specified modifier keys and test conditions under
     * which those modifier keys are matched.
     * @param modifierKeyMask - if specified will determine which modifier keys must
     *  be depressed for the action to be invoked.
     * @param modifierTestConditions the test conditions under which the modifier mask is tested
     */
    public Dragger(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, 0);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set some parameters that will create DragActions.
     * @param modifierKeyMask - if specified will determine which modifier keys must
     *  be depressed for the action to be invoked.
     * @param modifierTestConditions the test conditions under which the modifier mask is tested
     * @param additionalEvents additional mouse events the responder will respond to
     */
    public Dragger(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, paramInt2, 0x6 | paramInt3);
    }

    private int constrained = 4369;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The actor that will be invoked (if set) when the dragger
     * reaches a constraining condition
     */
    protected Actionable actor;

    /**
     * Deprecated.
     * This is the Transformable target of the Dragger action * @deprecated since QTJava 6.1
     */
    protected Transformable target;

    /**
     * Deprecated.
     * This is the QTDrawable space within which the Dragger's target is enclosed and contained* @deprecated since QTJava 6.1
     */
    protected QTDrawable space;

    /**
     * Deprecated.
     * The number of pixels on the horizontal axis that an object can be dragged beyond the bounds
     * if the dragging action is unconstrained. The default is one * @deprecated since QTJava 6.1
     */
    protected int xBufferIfUnConstrained = 1;

    /**
     * Deprecated.
     * The number of pixels on the vertical axis that an object can be dragged beyond the bounds
     * if the dragging action is unconstrained. The default is one * @deprecated since QTJava 6.1
     */
    protected int yBufferIfUnConstrained = 1;

    private QDPoint[] origPt;

    private QDPoint[] lastPt;

    private QDRect spaceRect;

    private Matrix invSpaceMatrix;

    private Matrix origTargetMatrix;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the containing Space of a target of a MouseResponder.
     * For a Dragger this space must be a QTDrawable.
     * @param o the containing space of the potential targets of the Dragger.
     */
    protected void setTargetSpace(Object paramObject) {
        this.space = ((QTDrawable) paramObject);
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
     * This method retreives the current actor that is attached to the action
     */
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Dragger knows how to drag objects of type Transformable around.
     * Thus this method returns true if the incoming object implements the Transformable interface.
     * @param object the object to test
     * @return a boolean
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Transformable;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the target of the Dragger. Generally this will be some kind of Transformable object
     * that the user has clicked on and the MouseController has extracted. This becomes
     * the active Target of the Dragger for the duration of the mouseDrag event.
     * @param t the Transformable target
     */
    protected void setTarget(Object paramObject) {
        this.target = ((Transformable) paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When the MouseController detects that the drag operation is completed it notifies
     * the Dragger to remove its target as the user no longer is dragging.
     */
    protected void removeTarget() {
        this.target = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Using the constrain constants dragging can be constrained in any direction.
     * @param flag constraint flag
     */
    public void setConstrained(int paramInt) {
        this.constrained = paramInt;
    }

    /**
     * Deprecated.
     * Returns the current constraint setting * @deprecated since QTJava 6.1
     */
    public int getConstrained() {
        return this.constrained;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the MouseController when the mouse is first pressed down on
     * a valid Transformable object within the space it is controlling.
     * For efficiency it is assumed that the overall characteristics of the enclosing space
     * will not be changing during the duration of the drag event - ie. the Matrix
     * of the enclosing space is not changing during a mouse drag. However the Target's
     * matrix can and will change as a result of the drag operation.
     * @param event the mouse down event that may begin the drag action
     */
    public void mousePressed(MouseEvent paramMouseEvent) {
        try {
            QDRect localQDRect = this.space.getDisplayBounds();
            this.invSpaceMatrix = new Matrix();
            Matrix localMatrix = this.space.getMatrix().copy();
            localMatrix.setTx(0.0F);
            localMatrix.setTy(0.0F);
            localMatrix.inverse(this.invSpaceMatrix);
            this.origPt = new QDPoint[1];
            this.origPt[0] = new QDPoint(paramMouseEvent.getX(), paramMouseEvent.getY());
            this.invSpaceMatrix.transformPoints(this.origPt);
            this.origTargetMatrix = this.target.getMatrix();
            this.spaceRect = localQDRect.copy();
            this.spaceRect.setX(0);
            this.spaceRect.setY(0);
            this.invSpaceMatrix.transformRect(this.spaceRect);
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mousePressed");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method will allow the user to drag the transformable object around.
     * This method also does constraints testing based on the current state
     * of the constraints.
     * @param event the mouse drag event that triggered the drag action.
     */
    public void mouseDragged(MouseEvent paramMouseEvent) {
        try {
            int i = 0;
            QDPoint[] arrayOfQDPoint = new QDPoint[1];
            arrayOfQDPoint[0] = new QDPoint(paramMouseEvent.getX(), paramMouseEvent.getY());
            this.invSpaceMatrix.transformPoints(arrayOfQDPoint);
            int j = arrayOfQDPoint[0].getX() - this.origPt[0].getX();
            int k = arrayOfQDPoint[0].getY() - this.origPt[0].getY();
            Matrix localMatrix = this.origTargetMatrix.copy();
            localMatrix.translate(j, k);
            QDDimension localQDDimension = this.target.getOriginalSize();
            int m = localQDDimension.getWidth();
            int n = localQDDimension.getHeight();
            float f1 = localMatrix.getTx();
            float f2 = localMatrix.getTy();
            int i1 = this.spaceRect.getX();
            int i2 = this.spaceRect.getWidth() - m;
            int i3 = this.spaceRect.getY();
            int i4 = this.spaceRect.getHeight() - n;
            int i5 = localMatrix.getType();
            switch(i5) {
                case 0:
                case 1:
                    break;
                default:
                    QDRect localQDRect = new QDRect(0, 0, m, n);
                    localMatrix.transformRect(localQDRect);
                    m = localQDRect.getWidth();
                    n = localQDRect.getHeight();
                    i1 = (int) (localMatrix.getTx() - localQDRect.getX() + 0.5F);
                    f1 -= i1;
                    i3 = (int) (localMatrix.getTy() - localQDRect.getY() + 0.5F);
                    f2 -= i3;
                    i4 = this.spaceRect.getHeight() - n + i3;
                    i2 = this.spaceRect.getWidth() - m + i1;
            }
            if (f1 < this.spaceRect.getX()) {
                if ((this.constrained & 0x1) != 0) {
                    localMatrix.setTx(i1);
                    i = 1;
                } else if (f1 <= -m) {
                    localMatrix.setTx(-m + this.xBufferIfUnConstrained + i1);
                    i = 1;
                }
            } else if (f1 + m > this.spaceRect.getWidth()) {
                if ((this.constrained & 0x10) != 0) {
                    localMatrix.setTx(i2);
                    i = 1;
                } else if (f1 >= this.spaceRect.getWidth()) {
                    localMatrix.setTx(i2 + m - this.xBufferIfUnConstrained);
                    i = 1;
                }
            }
            if (f2 < this.spaceRect.getY()) {
                if ((this.constrained & 0x100) != 0) {
                    localMatrix.setTy(i3);
                    i = 1;
                } else if (f2 <= -n) {
                    localMatrix.setTy(-n + this.yBufferIfUnConstrained + i3);
                    i = 1;
                }
            } else if (f2 + n > this.spaceRect.getHeight()) {
                if ((this.constrained & 0x1000) != 0) {
                    localMatrix.setTy(i4);
                    i = 1;
                } else if (f2 >= this.spaceRect.getHeight()) {
                    localMatrix.setTy(i4 + n - this.yBufferIfUnConstrained);
                    i = 1;
                }
            }
            this.target.setMatrix(localMatrix);
            if ((i != 0) && (this.actor != null))
                this.actor.trigger();
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this, "mouseDragged");
        }
    }
}

