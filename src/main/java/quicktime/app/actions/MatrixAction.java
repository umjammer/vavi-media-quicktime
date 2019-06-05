
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
 * @deprecated
 */
public class MatrixAction extends PeriodicAction implements Targetable {
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

    protected Transformable target;

    protected TransformMatrix transMatrix;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    protected MatrixAction(int paramInt1,
            int paramInt2,
            QTDrawable paramQTDrawable,
            Transformable paramTransformable) throws QTException {
        super(paramInt1, paramInt2);
        this.target = paramTransformable;
        this.space = paramQTDrawable;
    }

    /**
     * @deprecated
     */
    public MatrixAction(int paramInt1,
            int paramInt2,
            QTDisplaySpace paramQTDisplaySpace,
            Transformable paramTransformable,
            TransformMatrix paramTransformMatrix) throws QTException {
        super(paramInt1, paramInt2);
        this.target = paramTransformable;
        this.space = paramQTDisplaySpace;
        this.transMatrix = paramTransformMatrix;
        this.transMatrix.initialize(paramQTDisplaySpace, paramTransformable);
    }

    /**
     * @deprecated
     */
    protected boolean doConstraintBoundsTesting = false;

    private boolean forwards = true;

    private QTDrawable space;

    private boolean consReached = false;

    /**
     * @deprecated
     */
    public void setTarget(Object paramObject) {
        this.target = ((Transformable) paramObject);
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
    public QTDrawable getSpace() {
        return this.space;
    }

    /**
     * @deprecated
     */
    public void setSpace(QTDrawable paramQTDrawable) {
        this.space = paramQTDrawable;
    }

    /**
     * @deprecated
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

                switch (localMatrix1.getType()) {
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
     * @deprecated
     */
    protected void setTargetMatrix(Matrix paramMatrix) throws QTException {
        this.target.setMatrix(paramMatrix);
    }

    /**
     * @deprecated
     */
    protected boolean constraintReached() {
        return this.consReached;
    }

    public String toString() {
        return getClass().getName() + "[target=" + this.target + "]";
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    protected void rateDirectionChanged(boolean paramBoolean) throws QTException {
    }

    /**
     * @deprecated
     */
    protected void transformMatrix(Matrix paramMatrix) throws QTException {
    }

    /**
     * @deprecated
     */
    protected void boundsReached(int paramInt) {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/MatrixAction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
