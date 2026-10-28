package quicktime.app.actions;

import quicktime.std.image.Matrix;

/**
 * @deprecated
 * This class provides a standard mechanism for applying matrix manipulations to
 * a Transformable target and supporting constraint bounding of the target's location
 * after its matrix is altered.
 *
 * The class presents two modes of operations, depending on which initialize and transformMatrix
 * methods are applied and the class is generally used with this pairing. The notes for the initialize
 * and transformMatrix methods document these usages.
 *
 * A subclass defines the abstract methods to apply the particular transformation logic that
 * they want to apply. If the kNoConstraint flag is applied then no constraints testing is done
 * to keep the specified Transformable target constrained within the bounds of its enclosing space.
 */
public abstract class TransformMatrix {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Used to nofify subclasses that the applied transformations have
     * exceeded one of the sides of the enclosing space of the Transformable target.
     */
    protected static final int kLeftSide = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Used to nofify subclasses that the applied transformations have
     * exceeded one of the sides of the enclosing space of the Transformable target.
     */
    protected static final int kRightSide = 16;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Used to nofify subclasses that the applied transformations have
     * exceeded one of the sides of the enclosing space of the Transformable target.
     */
    protected static final int kTopSide = 256;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Used to nofify subclasses that the applied transformations have
     * exceeded one of the sides of the enclosing space of the Transformable target.
     */
    protected static final int kBottomSide = 4096;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kNoConstraint = 0;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinLeft = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinRight = 16;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinTop = 256;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinBottom = 4096;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinHorizontal = 17;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinVertical = 4352;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstrainWithinAll = 4369;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedLeft = 65536;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedRight = 1048576;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedTop = 16777216;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedBottom = 268435456;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedHorizontal = 1114112;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedVertical = 285212672;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use these as constraint flags to restrict transformations that relocated the
     * the target within its space. The client of this class can either completely
     * specify the number of pixels outside of the space that the target should be constrained
     * to, or have the target completely constrained within the enclosing space, or specify
     * that no constraints should be applied.
     */
    public static final int kConstraintSpecifiedAll = 286326784;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a TransformMatrix class
     */
    protected TransformMatrix() {
        this.newPt[0] = new quicktime.qd.QDPoint(0, 0);
    }

    private int constrained = 0;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This is the Transformable target (which MUST be a member of the space)
     * of the TransformMatrix action. It is this target
     * that has its matrix transformed by the subclass. It is set in the intialize method.
     */
    protected quicktime.app.image.Transformable target;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Specify the number of pixels that the target is constrained to when it is positioned
     * beyond the right hand side. Its default value is 1, which means that its position is constrained
     * to showing only 1 column (the left hand column) of the target's  pixels.
     */
    public int specifiedRightConstraint = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Specify the number of pixels that the target is constrained to when it is positioned
     * beyond the left hand side. Its default value is 1, which means that its position is constrained
     * to showing only 1 column (the right hand column) of the target's  pixels.
     */
    public int specifiedLeftConstraint = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Specify the number of pixels that the target is constrained to when it is positioned
     * beyond the top side. Its default value is 1, which means that its position is constrained
     * to showing only 1 row (the bottom row) of the target's pixels.
     */
    public int specifiedTopConstraint = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Specify the number of pixels that the target is constrained to when it is positioned
     * beyond the bottom side. Its default value is 1, which means that its position is constrained
     * to showing only 1 row (the top row) of the target's pixels.
     */
    public int specifiedBottomConstraint = 1;

    private quicktime.qd.QDPoint[] origPt;

    private quicktime.qd.QDRect spaceRect;

    private Matrix invSpaceMatrix;

    private Matrix origTargetMatrix;

    private final quicktime.qd.QDPoint[] newPt = new quicktime.qd.QDPoint[1];

    private final quicktime.qd.QDRect transformMatrixRect = new quicktime.qd.QDRect();

    private Matrix deltaMatrix;

    private int setXLeft;

    private int setXRight;

    private int setYTop;

    private int setYBottom;

    /**
     * @deprecated
     * Return a string representation of this class. * @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[target=" + this.target + "]";
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns true if the incoming object is a Matrix action with the same target.
     * @param obj the object to test
     * @return true if equal, false if not
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!obj.getClass().getName().equals(getClass().getName()))
            return false;
        if ((obj instanceof TransformMatrix localTransformMatrix)) {
            if (this.target != null)
                return this.target.equals(localTransformMatrix.target);
        }
        return false;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * @return the current target of this object.
     */
    public quicktime.app.image.Transformable getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Using the constrain constants transformation of the target's matrix can be constrained in any direction.
     * @param flag constraint flag
     */
    public void setConstraint(int flag) {
        this.constrained = flag;
    }

    /**
     * @deprecated
     * Returns the current constraint setting * @deprecated since QTJava 6.1
     */
    public int getConstraint() {
        return this.constrained;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called to initialize the TransformMatrix operation. Typically this is
     * used with the transformMatrix (Matrix) call. This version of the initialize call is used
     * used with the MatrixAction class that applies some kind of transformation in the matrix
     * each time that action is tickled.
     *
     * The TransformMatrix operation need only be initialized
     * when the space itself changes its size.
     *
     * For a Compositor the initialization with this usage need only be done once as the class
     * will convert all constraints testing to the coordinate space of the Compositor's back buffer
     * For a DirectGroup each time the group changes its size the TransformMatrix must be reinitialized
     * if constraint testing is performed.
     *
     * @param space the space which the Transformable object is a member of.
     * @throws quicktime.QTException
     */
    public void initialize(quicktime.app.display.QTDisplaySpace paramQTDisplaySpace, quicktime.app.image.Transformable paramTransformable) throws quicktime.QTException {
        this.target = paramTransformable;
        quicktime.qd.QDRect localQDRect = paramQTDisplaySpace.getDisplayBounds();
        Matrix localMatrix1 = paramQTDisplaySpace.getMatrix();
        this.invSpaceMatrix = new Matrix();
        Matrix localMatrix2 = localMatrix1.copy();
        localMatrix2.setTx(0.0F);
        localMatrix2.setTy(0.0F);
        localMatrix2.inverse(this.invSpaceMatrix);
        this.origPt = new quicktime.qd.QDPoint[1];
        this.origPt[0] = new quicktime.qd.QDPoint(0, 0);
        this.origTargetMatrix = this.target.getMatrix();
        this.spaceRect = localQDRect.copy();
        this.spaceRect.setX(0);
        this.spaceRect.setY(0);
        this.invSpaceMatrix.transformRect(this.spaceRect);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called to initialize the TransformMatrix operation. Typically this is
     * used with the transformMatrix (Matrix, int deltaX, int deltaY) call. This version of the initialize call is used
     * used with the DragAction where each time the user selects an object to drag the TransformMatrix is reinitialized.
     * @param space the space which the Transformable object is a member of.
     * @param x the x-origin about which the transformation is applied. In consequent calls to the
     *  transformMatrix (Matrix, int deltaX, int deltaY) method, the deltaX value is a delta from this specified origin.
     * @param y the y-origin about which the transformation is applied. In consequent calls to the
     *  transformMatrix (Matrix, int deltaX, int deltaY) method, the deltaY value is a delta from this specified origin.
     * @throws quicktime.QTException
     */
    public void initialize(quicktime.app.display.QTDisplaySpace paramQTDisplaySpace, quicktime.app.image.Transformable paramTransformable, int paramInt1, int paramInt2) throws quicktime.QTException {
        this.target = paramTransformable;
        quicktime.qd.QDRect localQDRect = paramQTDisplaySpace.getDisplayBounds();
        Matrix localMatrix1 = paramQTDisplaySpace.getMatrix();
        this.invSpaceMatrix = new Matrix();
        Matrix localMatrix2 = localMatrix1.copy();
        localMatrix2.setTx(0.0F);
        localMatrix2.setTy(0.0F);
        localMatrix2.inverse(this.invSpaceMatrix);
        this.origPt = new quicktime.qd.QDPoint[1];
        this.origPt[0] = new quicktime.qd.QDPoint(paramInt1, paramInt2);
        this.invSpaceMatrix.transformPoints(this.origPt);
        this.origTargetMatrix = this.target.getMatrix();
        this.spaceRect = localQDRect.copy();
        this.spaceRect.setX(0);
        this.spaceRect.setY(0);
        this.invSpaceMatrix.transformRect(this.spaceRect);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will apply the transformation matrix to the target.
     * It will call the doTransformation (Matrix) method.
     * @return true if the this transformation reached a constraining condition
     * @throws quicktime.QTException
     */
    public boolean transformTarget() throws quicktime.QTException {
        this.deltaMatrix = this.target.getMatrix().copy();
        doTransformation(this.deltaMatrix);
        return completeTransformation();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will apply the transformation matrix to the target.
     * It will call the doTransformation (Matrix, int deltaX, int deltaY) method.
     * @param newX the new x position expressed in the local coordinates of the space.
     * @param newY the new y position expressed in the local coordinates of the space.
     * @return true if the this transformation reached a constraining condition
     * @throws quicktime.QTException
     */
    public boolean transformTarget(int newX, int newY) throws quicktime.QTException {
        this.newPt[0].setX(newX);
        this.newPt[0].setY(newY);
        this.invSpaceMatrix.transformPoints(this.newPt);
        int i = this.newPt[0].getX() - this.origPt[0].getX();
        int j = this.newPt[0].getY() - this.origPt[0].getY();
        this.deltaMatrix = this.origTargetMatrix.copy();
        doTransformation(this.deltaMatrix, i, j);
        return completeTransformation();
    }

    private boolean completeTransformation() throws quicktime.QTException {
        boolean bool = false;
        if (this.constrained != 0) {
            quicktime.qd.QDDimension localQDDimension = this.target.getOriginalSize();
            int i = localQDDimension.getWidth();
            int j = localQDDimension.getHeight();
            float f1 = this.deltaMatrix.getTx();
            float f2 = this.deltaMatrix.getTy();
            this.setXLeft = this.spaceRect.getX();
            this.setXRight = (this.spaceRect.getWidth() - i);
            this.setYTop = this.spaceRect.getY();
            this.setYBottom = (this.spaceRect.getHeight() - j);
            int k = this.deltaMatrix.getType();
            switch(k) {
                case 0:
                case 1:
                    break;
                default:
                    this.transformMatrixRect.setWidth(i);
                    this.transformMatrixRect.setHeight(j);
                    this.transformMatrixRect.setX(0);
                    this.transformMatrixRect.setY(0);
                    this.deltaMatrix.transformRect(this.transformMatrixRect);
                    i = this.transformMatrixRect.getWidth();
                    j = this.transformMatrixRect.getHeight();
                    this.setXLeft = ((int) (this.deltaMatrix.getTx() - this.transformMatrixRect.getX() + 0.5F));
                    f1 -= this.setXLeft;
                    this.setYTop = ((int) (this.deltaMatrix.getTy() - this.transformMatrixRect.getY() + 0.5F));
                    f2 -= this.setYTop;
                    this.setYBottom = (this.spaceRect.getHeight() - j + this.setYTop);
                    this.setXRight = (this.spaceRect.getWidth() - j + this.setXLeft);
            }
            bool = doConstraintsTesting(f1, f2, i, j);
        }
        this.target.setMatrix(this.deltaMatrix);
        return bool;
    }

    private boolean doConstraintsTesting(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
        boolean bool = false;
        if (paramFloat1 < this.spaceRect.getX()) {
            if ((this.constrained & 0x1) != 0) {
                this.deltaMatrix.setTx(this.setXLeft);
                boundsReached(1);
                bool = true;
            } else if (((this.constrained & 0x10000) != 0) && (paramFloat1 <= -paramInt1)) {
                this.deltaMatrix.setTx(-paramInt1 + this.specifiedLeftConstraint + this.setXLeft);
                boundsReached(1);
                bool = true;
            }
        } else if (paramFloat1 + paramInt1 > this.spaceRect.getWidth()) {
            if ((this.constrained & 0x10) != 0) {
                this.deltaMatrix.setTx(this.setXRight);
                boundsReached(16);
                bool = true;
            } else if (((this.constrained & 0x100000) != 0) && (paramFloat1 >= this.spaceRect.getWidth())) {
                this.deltaMatrix.setTx(this.setXRight + paramInt1 - this.specifiedRightConstraint);
                boundsReached(16);
                bool = true;
            }
        }
        if (paramFloat2 < this.spaceRect.getY()) {
            if ((this.constrained & 0x100) != 0) {
                this.deltaMatrix.setTy(this.setYTop);
                boundsReached(256);
                bool = true;
            } else if (((this.constrained & 0x1000000) != 0) && (paramFloat2 <= -paramInt2)) {
                this.deltaMatrix.setTy(-paramInt2 + this.specifiedTopConstraint + this.setYTop);
                boundsReached(256);
                bool = true;
            }
        } else if (paramFloat2 + paramInt2 > this.spaceRect.getHeight()) {
            if ((this.constrained & 0x1000) != 0) {
                this.deltaMatrix.setTy(this.setYBottom);
                boundsReached(4096);
                bool = true;
            } else if (((this.constrained & 0x10000000) != 0) && (paramFloat2 >= this.spaceRect.getHeight())) {
                this.deltaMatrix.setTy(this.setYBottom + paramInt2 - this.specifiedBottomConstraint);
                boundsReached(4096);
                bool = true;
            }
        }
        return bool;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * If a subclass is applying specified transformations to a matrix, then it should
     * negate the value of these pre-defined transformation values. This is used in conjunction
     * with the transformMatrix usage.
     */
    public void flipTransformationSettings() {
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Applies the transformation of the Matrix.
     * @param theMatrix The matrix handed in is the original matrix of the target when the TransformMatrix object was initialized.
     * @param deltaX the difference between the original x point when the TransformMatrix object was initialized and the current location.
     * @param deltaY the difference between the original y point when the TransformMatrix object was initialized and the current location.
     * @throws quicktime.QTException
     */
    protected abstract void doTransformation(Matrix theMatrix, int deltaX, int deltaY) throws quicktime.QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Applies the transformation of the Matrix.
     * @param theMatrix The matrix handed in is the current matrix of the target.
     * @throws quicktime.QTException
     */
    protected abstract void doTransformation(Matrix theMatrix) throws quicktime.QTException;

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is called whenever a constraints test hits one of the sides of the enclosing space.
     * @param whichSide indicates which side the constraints test hit.
     */
    protected void boundsReached(int whichSide) {
    }
}

