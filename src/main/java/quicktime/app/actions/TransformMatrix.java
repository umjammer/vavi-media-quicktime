
package quicktime.app.actions;

import quicktime.std.image.Matrix;


/**
 * @deprecated
 */
public abstract class TransformMatrix {
    /**
     * @deprecated
     */
    protected static final int kLeftSide = 1;

    /**
     * @deprecated
     */
    protected static final int kRightSide = 16;

    /**
     * @deprecated
     */
    protected static final int kTopSide = 256;

    /**
     * @deprecated
     */
    protected static final int kBottomSide = 4096;

    /**
     * @deprecated
     */
    public static final int kNoConstraint = 0;

    /**
     * @deprecated
     */
    public static final int kConstrainWithinLeft = 1;

    /**
     * @deprecated
     */
    public static final int kConstrainWithinRight = 16;

    /**
     * @deprecated
     */
    public static final int kConstrainWithinTop = 256;

    /**
     * @deprecated
     */
    public static final int kConstrainWithinBottom = 4096;

    /**
     * @deprecated
     */
    public static final int kConstrainWithinHorizontal = 17;

    /**
     * @deprecated
     */
    public static final int kConstrainWithinVertical = 4352;
    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public static final int kConstrainWithinAll = 4369;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedLeft = 65536;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedRight = 1048576;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedTop = 16777216;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedBottom = 268435456;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedHorizontal = 1114112;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedVertical = 285212672;

    /**
     * @deprecated
     */
    public static final int kConstraintSpecifiedAll = 286326784;

    /**
     * @deprecated
     */
    protected TransformMatrix() {
        this.newPt[0] = new quicktime.qd.QDPoint(0, 0);
    }

    private int constrained = 0;

    /**
     * @deprecated
     */
    protected quicktime.app.image.Transformable target;

    /**
     * @deprecated
     */
    public int specifiedRightConstraint = 1;

    /**
     * @deprecated
     */
    public int specifiedLeftConstraint = 1;

    /**
     * @deprecated
     */
    public int specifiedTopConstraint = 1;

    /**
     * @deprecated
     */
    public int specifiedBottomConstraint = 1;

    private quicktime.qd.QDPoint[] origPt;

    private quicktime.qd.QDRect spaceRect;

    private Matrix invSpaceMatrix;

    private Matrix origTargetMatrix;

    private quicktime.qd.QDPoint[] newPt = new quicktime.qd.QDPoint[1];

    private quicktime.qd.QDRect transformMatrixRect = new quicktime.qd.QDRect();

    private Matrix deltaMatrix;

    private int setXLeft;

    private int setXRight;

    private int setYTop;

    private int setYBottom;

    public String toString() {
        return getClass().getName() + "[target=" + this.target + "]";
    }

    /**
     * @deprecated
     */
    public boolean equals(Object paramObject) {
        if (paramObject == this)
            return true;
        if (paramObject == null)
            return false;
        if (!paramObject.getClass().getName().equals(getClass().getName()))
            return false;
        if ((paramObject instanceof TransformMatrix)) {
            TransformMatrix localTransformMatrix = (TransformMatrix) paramObject;
            if (this.target != null)
                return this.target.equals(localTransformMatrix.target);
        }
        return false;
    }

    /**
     * @deprecated
     */
    public quicktime.app.image.Transformable getTarget() {
        return this.target;
    }

    /**
     * @deprecated
     */
    public void setConstraint(int paramInt) {
        this.constrained = paramInt;
    }

    public int getConstraint() {
        return this.constrained;
    }

    /**
     * @deprecated
     */
    public void initialize(quicktime.app.display.QTDisplaySpace paramQTDisplaySpace,
                           quicktime.app.image.Transformable paramTransformable) throws quicktime.QTException {
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
     */
    public void initialize(quicktime.app.display.QTDisplaySpace paramQTDisplaySpace,
                           quicktime.app.image.Transformable paramTransformable,
                           int paramInt1,
                           int paramInt2) throws quicktime.QTException {
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
     */
    public boolean transformTarget() throws quicktime.QTException {
        this.deltaMatrix = this.target.getMatrix().copy();
        doTransformation(this.deltaMatrix);
        return completeTransformation();
    }

    /**
     * @deprecated
     */
    public boolean transformTarget(int paramInt1, int paramInt2) throws quicktime.QTException {
        this.newPt[0].setX(paramInt1);
        this.newPt[0].setY(paramInt2);
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
            switch (k) {
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
     */
    public void flipTransformationSettings() {
    }

    /**
     * @deprecated
     */
    protected abstract void doTransformation(Matrix paramMatrix, int paramInt1, int paramInt2) throws quicktime.QTException;

    /**
     * @deprecated
     */
    protected abstract void doTransformation(Matrix paramMatrix) throws quicktime.QTException;

    /**
     * @deprecated
     */
    protected void boundsReached(int paramInt) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/TransformMatrix.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
