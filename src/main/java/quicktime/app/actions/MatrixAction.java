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
 * This class provides the means for a basic Matrix actions. Each time
 * the tickle method is called, it will retrieve the matrix from the target and
 * invoke the transformMatrix method, which the subclass must implement. The subclass
 * defines the transformMatrix method to apply a transformation to the matrix of the
 * target of the action.
 * <p>
 * The MatrixAction will also notify the subclass of any changes of rate direction
 * and if the subclass is interested in constraint testing will notify the subclass
 * which side(s) a targets matrix has collided with after the transformation has
 * been applied. These two states are notified with the rateDirectionChanged and
 * boundsReached methods respectively.
 * <p>
 * Though the Space is generally specified it is only used in the bounds testing.
 *
 * @deprecated
 */
public class MatrixAction extends PeriodicAction implements Targetable {

    /**
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected static final int kLeftSide = 1;

    /**
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected static final int kRightSide = 16;

    /**
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected static final int kTopSide = 256;

    /**
     * These constants are used in the boundsReached call to indicate to the
     * subclass which edge the target has collided with. Horizontal and Vertical
     * sides are provided as a means for testing for both left and right,
     * up and down respectively.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected static final int kBottomSide = 4096;

    /**
     * @since QTJava 6.1
     * @deprecated The target of the matrix transformations
     */
    protected Transformable target;

    /**
     * @since QTJava 6.1
     * @deprecated This is the class that will do the transformations on the target's matrix
     */
    protected TransformMatrix transMatrix;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Create a PeriodicAction that will act upon a Matrix.
     *
     * @param scale  the amount with which a second is divided into at a rate of one
     * @param period the number of scale ticks that elapse between invocations of the action.
     * @param space  the QTDrawable object that is the enclosing space of the Transformable target.
     * @param t      a transformable object that will have its display matrix altered by the deltaMatrix
     *               {@link quicktime.app.actions.TransformMatrix}
     * @since QTJava 4
     * @deprecated
     */
    protected MatrixAction(int scale, int period, QTDrawable space, Transformable t) throws QTException {
        super(scale, period);
        this.target = t;
        this.space = space;
    }

    /**
     * Create a MatrixAction that will act upon a Matrix. It delegates the
     * matrix transformation to the TransformMatrix object that is supplied in the constructor.
     *
     * @param scale                 the amount with which a second is divided into at a rate of one
     * @param period                the number of scale ticks that elapse between invocations of the action.
     * @param space                 the QTDrawable object that is the enclosing space of the Transformable target.
     * @param t                     a transformable object that will have its display matrix altered by the deltaMatrix
     * @param transformMatrixAction the transformation that will be applied to the target's matrix every time this action is invoked.
     * @since QTJava 6.1
     * @deprecated
     */
    public MatrixAction(int scale, int period, QTDisplaySpace space, Transformable t, TransformMatrix transformMatrixAction) throws QTException {
        super(scale, period);
        this.target = t;
        this.space = space;
        this.transMatrix = transformMatrixAction;
        this.transMatrix.initialize(space, t);
    }

    /**
     * Set this boolean to true or false to perform bounds testing of the
     * target within the bounds of its enclosing Space. Default is false.
     * If true the enclosing Space must be specified.
     *
     * @since QTJava 6.1
     * @deprecated
     */
    protected final boolean doConstraintBoundsTesting = false;

    private boolean forwards = true;

    private QTDrawable space;

    private boolean consReached = false;

    /**
     * Sets the target of the action. The target must be some kind of Transformable object.
     *
     * @param target a Transformable object
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    @Override
    public void setTarget(Object target) {
        this.target = ((Transformable) target);
    }

    /**
     * Returns the current target of the action.
     *
     * @return the current target
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    @Override
    public Object getTarget() {
        return this.target;
    }

    /**
     * The space which is the space that the transformable
     * object is a member of.
     *
     * @return the enclosing space.
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    public QTDrawable getSpace() {
        return this.space;
    }

    /**
     * Set the space which is the space that the transformable
     * target is a member of.
     *
     * @param space the enclosing space.
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    public void setSpace(QTDrawable space) {
        this.space = space;
    }

    /**
     * When called it performs three primary tasks:
     * <p>
     * (1) Checks to see if rate direction has changed - if so calls the rateDirectionChanged method
     * (2) Calls the transformMatrix method which the subclass defines
     * (3) If doConstraintBoundsTesting is true does bounds testing of the target within
     * the enclosing space and notifies of any collisions of the target with the space's sides.
     * <p>
     * Once these tasks are performed the target's matrix is set to the transformed matrix
     * through calling the setTargetMatrix with the transformed matrix.
     *
     * @param er the rate at which the action was invoked.
     * @param t  the time at which the action was invoked.
     * @throws quicktime.QTException
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected void doAction(float er, int t) throws QTException {
        this.consReached = false;
        if (((er > 0.0F) && (!this.forwards)) || ((er < 0.0F) && (this.forwards))) {
            this.forwards = (er > 0.0F);
            if (this.transMatrix != null) {
                this.transMatrix.flipTransformationSettings();
            } else {
                rateDirectionChanged(this.forwards);
            }
        }
        if (this.transMatrix != null) {
            this.consReached = this.transMatrix.transformTarget();
        } else {
            Matrix theMatrix = this.target.getMatrix().copy();
            transformMatrix(theMatrix);
            if (this.doConstraintBoundsTesting) {
                Matrix mr = new Matrix();
                this.space.getMatrix().inverse(mr);
                QDRect rect1 = this.space.getDisplayBounds().copy();
                mr.transformRect(rect1);
                QDDimension originalSize = this.target.getOriginalSize();
                float f1 = theMatrix.getTx();
                float f2 = theMatrix.getTy();
                int i = rect1.getX();
                int j = rect1.getWidth() - originalSize.getWidth();
                int k = rect1.getY();
                int m = rect1.getHeight() - originalSize.getHeight();
                switch (theMatrix.getType()) {
                    case 0:
                    case 1:
                        break;
                    default:
                        QDRect rect2 = new QDRect(0, 0, originalSize.getWidth(), originalSize.getHeight());
                        theMatrix.transformRect(rect2);
                        originalSize.setWidth(rect2.getWidth());
                        originalSize.setHeight(rect2.getHeight());
                        i = (int) (theMatrix.getTx() - rect2.getX() + 0.5F);
                        f1 -= i;
                        k = (int) (theMatrix.getTy() - rect2.getY() + 0.5F);
                        f2 -= k;
                        m = rect1.getHeight() - originalSize.getHeight() + k;
                        j = rect1.getWidth() - originalSize.getWidth() + i;
                }
                if (f1 < rect1.getX()) {
                    theMatrix.setTx(i);
                    boundsReached(1);
                    this.consReached = true;
                } else if (f1 + originalSize.getWidth() > rect1.getWidth()) {
                    theMatrix.setTx(j);
                    boundsReached(16);
                    this.consReached = true;
                }
                if (f2 < rect1.getY()) {
                    theMatrix.setTy(k);
                    boundsReached(256);
                    this.consReached = true;
                } else if (f2 + originalSize.getHeight() > rect1.getHeight()) {
                    theMatrix.setTy(m);
                    boundsReached(4096);
                    this.consReached = true;
                }
            }
            setTargetMatrix(theMatrix);
        }
    }

    /**
     * This method is invoked by the doAction method once the matrix has been
     * transformed. The default behaviour of this method is to just call the target's
     * setMatrix method with the transformed matrix.
     *
     * @param transformedMatrix the new matrix for the target
     * @throws quicktime.QTException
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    protected void setTargetMatrix(Matrix transformedMatrix) throws QTException {
        this.target.setMatrix(transformedMatrix);
    }

    /**
     * Subclasses should return true if after an action is triggered (the doAction
     * method is called) the action has reached one of its specified constraint conditions
     * and the actionable object should be triggered. How this is defined is determined
     * totally by the subclass. This method will only be called by the tickle method
     * if the Action has an Actionable actor.
     *
     * @return true if the actionable object (if set) should be triggered
     * @since QTJava 6.1
     * @deprecated
     */
    @Override
    protected boolean constraintReached() {
        return this.consReached;
    }

    /**
     * Return a string representation of this class.
     *
     * @return a String
     * @since QTJava 6.1
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "[target=" + this.target + "]";
    }

    /**
     * Returns true if the incoming object is a Matrix action with the same target.
     *
     * @param obj the object to test
     * @return true if equal, false if not
     * @since QTJava 6.1
     * @deprecated
     */
    public boolean equals(Object obj) {
        if ((obj instanceof MatrixAction matrixAction)) {
            if (this.transMatrix != null) {
                if (!this.transMatrix.equals(matrixAction.transMatrix)) {
                    return false;
                }
                return super.equals(obj);
            }
            if (!this.target.equals(matrixAction.target)) {
                return false;
            }
            return super.equals(obj);
        }
        return false;
    }

    /**
     * The Matrix action notifies subclasses when the rate at which an action is tickled changes
     * from positive to negative or vice versa.
     *
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    protected void rateDirectionChanged(boolean forward) throws QTException {
    }

    /**
     * The Matrix action calls the transformMatrix method with a Matrix that the subclass
     * should transform according to the particular transformation of the subclass.
     *
     * @param theMatrix the matrix to transform.
     * @throws quicktime.QTException
     * @see quicktime.app.actions.TransformMatrix
     * @since QTJava 4
     * @deprecated
     */
    protected void transformMatrix(Matrix theMatrix) throws QTException {
    }

    /**
     * If the Matrix action is doing constraint bounds testing and a bounding
     * condition is reached the Matrix action calls this method with one of four
     * flags to indicate which side of the enclosing space the target collided with.
     *
     * @param whichSide a flag that indicates which side
     * @see quicktime.app.actions.TransformMatrix
     * @deprecated <I>since QTJava 4</I>
     */
    protected void boundsReached(int whichSide) {
    }
}
