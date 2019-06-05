
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
 * @deprecated
 */
public class Dragger extends MouseResponder {
    public static final int kConstrainNone = 0;

    public static final int kConstrainLeft = 1;

    public static final int kConstrainRight = 16;

    public static final int kConstrainTop = 256;

    public static final int kConstrainBottom = 4096;

    public static final int kConstrainHorizontal = 17;

    public static final int kConstrainVertical = 4352;

    public static final int kConstrainAll = 4369;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public Dragger(int paramInt) {
        this(paramInt, -5, 0);
    }

    /**
     * @deprecated
     */
    public Dragger(int paramInt1, int paramInt2) {
        this(paramInt1, paramInt2, 0);
    }

    /**
     * @deprecated
     */
    public Dragger(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, paramInt2, 0x6 | paramInt3);
    }

    private int constrained = 4369;

    /**
     * @deprecated
     */
    protected Actionable actor;

    protected Transformable target;

    protected QTDrawable space;

    protected int xBufferIfUnConstrained = 1;

    protected int yBufferIfUnConstrained = 1;

    private QDPoint[] origPt;

    private QDPoint[] lastPt;

    private QDRect spaceRect;

    private Matrix invSpaceMatrix;

    private Matrix origTargetMatrix;

    /**
     * @deprecated
     */
    protected void setTargetSpace(Object paramObject) {
        this.space = ((QTDrawable) paramObject);
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
    public Actionable getActionable() {
        return this.actor;
    }

    /**
     * @deprecated
     */
    public boolean isAppropriate(Object paramObject) {
        return paramObject instanceof Transformable;
    }

    /**
     * @deprecated
     */
    protected void setTarget(Object paramObject) {
        this.target = ((Transformable) paramObject);
    }

    /**
     * @deprecated
     */
    protected void removeTarget() {
        this.target = null;
    }

    /**
     * @deprecated
     */
    public void setConstrained(int paramInt) {
        this.constrained = paramInt;
    }

    public int getConstrained() {
        return this.constrained;
    }

    /**
     * @deprecated
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
     * @deprecated
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
            switch (i5) {
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

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/Dragger.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
