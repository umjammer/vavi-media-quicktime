
package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.std.image.Matrix;


/**
 * @deprecated
 */
public class TranslateMatrix extends TransformMatrix {
    private float deltaX;

    private float deltaY;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public TranslateMatrix() {
        this(0.0F, 0.0F);
    }

    /**
     * @deprecated
     */
    public TranslateMatrix(float paramFloat1, float paramFloat2) {
        this.deltaX = paramFloat1;
        this.deltaY = paramFloat2;
        setConstraint(4369);
    }

    public float getDeltaX() {
        return this.deltaX;
    }

    public void setDeltaX(float paramFloat) {
        this.deltaX = paramFloat;
    }

    public float getDeltaY() {
        return this.deltaY;
    }

    public void setDeltaY(float paramFloat) {
        this.deltaY = paramFloat;
    }

    /**
     * @deprecated
     */
    protected void doTransformation(Matrix paramMatrix, int paramInt1, int paramInt2) throws QTException {
        paramMatrix.translate(paramInt1, paramInt2);
    }

    /**
     * @deprecated
     */
    protected void doTransformation(Matrix paramMatrix) throws QTException {
        paramMatrix.translate(this.deltaX, this.deltaY);
    }

    /**
     * @deprecated
     */
    protected void boundsReached(int paramInt) {
        if ((paramInt & 0x11) != 0) {
            this.deltaX = (-this.deltaX);
        } else {
            this.deltaY = (-this.deltaY);
        }
    }

    /**
     * @deprecated
     */
    public void flipTransformationSettings() {
        this.deltaX = (-this.deltaX);
        this.deltaY = (-this.deltaY);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * actions/TranslateMatrix.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
