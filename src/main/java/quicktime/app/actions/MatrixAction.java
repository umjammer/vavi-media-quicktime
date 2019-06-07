package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.display.QTDrawable;
import quicktime.app.image.Transformable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDRect;
import quicktime.std.image.Matrix;

/**
 * Deprecated.
 * This class provides the means for a basic Matrix actions. Each time
 * the tickle method is called, it will retrieve the matrix from the target and
 * invoke the transformMatrix method, which the subclass must implement. The subclass
 * defines the transformMatrix method to apply a transformation to the matrix of the
 * target of the action.
 *
 * The MatrixAction will also notify the subclass of any changes of rate direction
 * and if the subclass is interested in constraint testing will notify the subclass
 * which side(s) a targets matrix has collided with after the transformation has
 * been applied. These two states are notified with the rateDirectionChanged and
 * boundsReached methods respectively.
 *
 * Though the Space is generally specified it is only used in the bounds testing.
 */
public class MatrixAction extends PeriodicAction implements Targetable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     */
    protected static final int kLeftSide = 1;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     */
    protected static final int kRightSide = 16;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     */
    protected static final int kTopSide = 256;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     */
    protected static final int kBottomSide = 4096;

    /**
     * Deprecated.
     * The target of the matrix transformations* @deprecated since QTJava 6.1
     */
    protected Transformable target;

    /**
     * Deprecated.
     * This is the class that will do the transformations on the target's matrix * @deprecated since QTJava 6.1
     */
    protected TransformMatrix transMatrix;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * Create a PeriodicAction that will act upon a Matrix.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @param space the QTDrawable object that is the enclosing space of the Transformable target.
     * @param t a transformable object that will have its display matrix altered by the deltaMatrix
     * {@link quicktime.app.actions.TransformMatrix}
     */
    protected MatrixAction(int paramInt1, int paramInt2, QTDrawable paramQTDrawable, Transformable paramTransformable) throws QTException {
        super(paramInt1, paramInt2);
        this.target = paramTransformable;
        this.space = paramQTDrawable;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Create a MatrixAction that will act upon a Matrix. It delegates the
     * matrix transformation to the TransformMatrix object that is supplied in the constructor.
     * @param scale the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @param space the QTDrawable object that is the enclosing space of the Transformable target.
     * @param t a transformable object that will have its display matrix altered by the deltaMatrix
     * @param transformMatrixAction the transformation that will be applied to the target's matrix every time this action is invoked.
     */
    public MatrixAction(int paramInt1, int paramInt2, QTDisplaySpace paramQTDisplaySpace, Transformable paramTransformable, TransformMatrix paramTransformMatrix) throws QTException {
        super(paramInt1, paramInt2);
        this.target = paramTransformable;
        this.space = paramQTDisplaySpace;
        this.transMatrix = paramTransformMatrix;
        this.transMatrix.initialize(paramQTDisplaySpace, paramTransformable);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Set this boolean to true or false to perform bounds testing of the
     * target within the bounds of its enclosing Space. Default is false.
     * If true the enclosing Space must be specified.
     */
    protected boolean doConstraintBoundsTesting = false;

    private boolean forwards = true;

    private QTDrawable space;

    private boolean consReached = false;

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * Sets the target of the action. The target must be some kind of Transformable object.
     * @param target a Transformable object
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    public void setTarget(Object paramObject) {
        this.target = ((Transformable) paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * Returns the current target of the action.
     * @return the current target
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * The space which is the space that the transformable
     * object is a member of.
     * @return the enclosing space.
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    public QTDrawable getSpace() {
        return this.space;
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * Set the space which is the space that the transformable
     * target is a member of.
     * @param space the enclosing space.
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    public void setSpace(QTDrawable paramQTDrawable) {
        this.space = paramQTDrawable;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When called it performs three primary tasks:
     *
     * (1) Checks to see if rate direction has changed - if so calls the rateDirectionChanged method
     * (2) Calls the transformMatrix method which the subclass defines
     * (3) If doConstraintBoundsTesting is true does bounds testing of the target within
     * the enclosing space and notifies of any collisions of the target with the space's sides.
     *
     * Once these tasks are performed the target's matrix is set to the transformed matrix
     * through calling the setTargetMatrix with the transformed matrix.
     * @param er the rate at which the action was invoked.
     * @param t the time at which the action was invoked.
     * @param {@link quicktime.QTException}
     */
    protected void doAction(float paramFloat, int paramInt) throws QTException {
        this.consReached = false;
        if (((paramFloat > 0.0F) && (!this.forwards)) || ((paramFloat < 0.0F) && (this.forwards))) {
            this.forwards = (paramFloat > 0.0F);
            if (this.transMatrix != null) {
                this.transMatrix.flipTransformationSettings();
            } else {
                rateDirectionChanged(this.forwards);
            }
        }
        if (this.transMatrix != null) {
            this.consReached = this.transMatrix.transformTarget();
        } else {
            Matrix localMatrix1 = this.target.getMatrix().copy();
            transformMatrix(localMatrix1);
            if (this.doConstraintBoundsTesting) {
                Matrix localMatrix2 = new Matrix();
                this.space.getMatrix().inverse(localMatrix2);
                QDRect localQDRect1 = this.space.getDisplayBounds().copy();
                localMatrix2.transformRect(localQDRect1);
                QDDimension localQDDimension = this.target.getOriginalSize();
                float f1 = localMatrix1.getTx();
                float f2 = localMatrix1.getTy();
                int i = localQDRect1.getX();
                int j = localQDRect1.getWidth() - localQDDimension.getWidth();
                int k = localQDRect1.getY();
                int m = localQDRect1.getHeight() - localQDDimension.getHeight();
                switch(localMatrix1.getType()) {
                    case 0:
                    case 1:
                        break;
                    default:
                        QDRect localQDRect2 = new QDRect(0, 0, localQDDimension.getWidth(), localQDDimension.getHeight());
                        localMatrix1.transformRect(localQDRect2);
                        localQDDimension.setWidth(localQDRect2.getWidth());
                        localQDDimension.setHeight(localQDRect2.getHeight());
                        i = (int) (localMatrix1.getTx() - localQDRect2.getX() + 0.5F);
                        f1 -= i;
                        k = (int) (localMatrix1.getTy() - localQDRect2.getY() + 0.5F);
                        f2 -= k;
                        m = localQDRect1.getHeight() - localQDDimension.getHeight() + k;
                        j = localQDRect1.getWidth() - localQDDimension.getWidth() + i;
                }
                if (f1 < localQDRect1.getX()) {
                    localMatrix1.setTx(i);
                    boundsReached(1);
                    this.consReached = true;
                } else if (f1 + localQDDimension.getWidth() > localQDRect1.getWidth()) {
                    localMatrix1.setTx(j);
                    boundsReached(16);
                    this.consReached = true;
                }
                if (f2 < localQDRect1.getY()) {
                    localMatrix1.setTy(k);
                    boundsReached(256);
                    this.consReached = true;
                } else if (f2 + localQDDimension.getHeight() > localQDRect1.getHeight()) {
                    localMatrix1.setTy(m);
                    boundsReached(4096);
                    this.consReached = true;
                }
            }
            setTargetMatrix(localMatrix1);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * This method is invoked by the doAction method once the matrix has been
     * transformed. The default behaviour of this method is to just call the target's
     * setMatrix method with the transformed matrix.
     * @param transformedMatrix the new matrix for the target
     * @param {@link quicktime.QTException}
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    protected void setTargetMatrix(Matrix paramMatrix) throws QTException {
        this.target.setMatrix(paramMatrix);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Subclasses should return true if after an action is triggered (the doAction
     * method is called) the action has reached one of its specified constraint conditions
     * and the actionable object should be triggered. How this is defined is determined
     * totally by the subclass. This method will only be called by the tickle method
     * if the Action has an Actionable actor.
     * @return true if the actionable object (if set) should be triggered
     */
    protected boolean constraintReached() {
        return this.consReached;
    }

    /**
     * Deprecated.
     * Return a string representation of this class. * @deprecated since QTJava 6.1
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[target=" + this.target + "]";
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the incoming object is a Matrix action with the same target.
     * @param obj the object to test
     * @return true if equal, false if not
     */
    public boolean equals(Object paramObject) {
        if ((paramObject instanceof MatrixAction)) {
            MatrixAction localMatrixAction = (MatrixAction) paramObject;
            if (this.transMatrix != null) {
                if (!this.transMatrix.equals(localMatrixAction.transMatrix)) {
                    return false;
                }
                return super.equals(paramObject);
            }
            if (!this.target.equals(localMatrixAction.target)) {
                return false;
            }
            return super.equals(paramObject);
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * The Matrix action notifies subclasses when the rate at which an action is tickled changes
     * from positive to negative or vice versa.
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    protected void rateDirectionChanged(boolean paramBoolean) throws QTException {
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * The Matrix action calls the transformMatrix method with a Matrix that the subclass
     * should transform according to the particular transformation of the subclass.
     * @param theMatrix the matrix to transform.
     * @param {@link quicktime.QTException}
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    protected void transformMatrix(Matrix paramMatrix) throws QTException {
    }

    /**
     * Deprecated.
     * <I>since QTJava 4</I>
     * If the Matrix action is doing constraint bounds testing and a bounding
     * condition is reached the Matrix action calls this method with one of four
     * flags to indicate which side of the enclosing space the target collided with.
     * @param whichSide a flag that indicates which side
     * @see {@link quicktime.app.actions.TransformMatrix}
     */
    protected void boundsReached(int paramInt) {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/MatrixAction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
