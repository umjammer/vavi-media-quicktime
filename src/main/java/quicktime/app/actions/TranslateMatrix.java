package quicktime.app.actions;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.std.image.Matrix;

/**
 * @deprecated
 * This class applies a matrix translation on the Transformable target.
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
     * @since QTJava 6.1
     * Create a TranslateMatrix object. This constructor is used when
     * this class is used to translate the matrix of the target in reponse
     * to specific positioning relative to some origin. Typically used in a Drag operation.
     */
    public TranslateMatrix() {
        this(0.0F, 0.0F);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Create a TranslateMatrix object. This constructor is used when
     * this class is used to translate the matrix of the target by some
     * specified delta each time the transformMatrix method is called.
     * Typically used with the MatrixAction class.
     * @param deltaX the amount of pixels on the x axis the target will be translated.
     * @param deltaY the amount of pixels on the y axis the target will be translated.
     */
    public TranslateMatrix(float deltaX, float deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        setConstraint(4369);
    }

    /**
     * @deprecated
     * @return the current deltaX value* @deprecated since QTJava 6.1
     */
    public float getDeltaX() {
        return this.deltaX;
    }

    /**
     * @deprecated
     * @param x the new delta x to apply when the doTransformation(Matrix) method is called. * @deprecated since QTJava 6.1
     */
    public void setDeltaX(float x) {
        this.deltaX = x;
    }

    /**
     * @deprecated
     * @return the current deltaY value* @deprecated since QTJava 6.1
     */
    public float getDeltaY() {
        return this.deltaY;
    }

    /**
     * @deprecated
     * @param y the new delta y to apply when the doTransformation(Matrix) method is called. * @deprecated since QTJava 6.1
     */
    public void setDeltaY(float y) {
        this.deltaY = y;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Applies the specified delta values to the incoming matrix.
     * @param theMatrix The incoming matrix is the original matrix of the target when the TransformMatrix
     *  was last initialized.
     * @param deltaX the delta on the x-axis from the x value when the TransformMatrix
     *  was last initialized.
     * @param deltaY the delta on the y-axis from the x value when the TransformMatrix
     *  was last initialized.
     * @throws quicktime.QTException
     */
    @Override
    protected void doTransformation(Matrix theMatrix, int deltaX, int deltaY) throws QTException {
        theMatrix.translate(deltaX, deltaY);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Applies the preset delta values to the incoming matrix.
     * @param theMatrix The incoming matrix is the current matrix of the target.
     * @throws quicktime.QTException
     */
    @Override
    protected void doTransformation(Matrix theMatrix) throws QTException {
        theMatrix.translate(this.deltaX, this.deltaY);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Flips the delta values to achieve a "Bounce" type action when a constraining condition is reached.
     * @param whichSide specifies which side of the enclosing bounds the target hit.
     */
    @Override
    protected void boundsReached(int whichSide) {
        if ((whichSide & 0x11) != 0) {
            this.deltaX = (-this.deltaX);
        } else {
            this.deltaY = (-this.deltaY);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Flips the delta values to ensure that a MatrixAction usage of this class will
     * translate correctly when the rate at which the matrix action is invoked changes
     * direction.
     */
    @Override
    public void flipTransformationSettings() {
        this.deltaX = (-this.deltaX);
        this.deltaY = (-this.deltaY);
    }
}
