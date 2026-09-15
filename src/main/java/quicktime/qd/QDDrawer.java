package quicktime.qd;

import quicktime.QTException;

/**
 * This interface is used in conjunction with the QDGraphics.beginDraw method
 * to perform a collection of drawing operations in a single call. This is important
 * for efficient drawing as global thread state that is required for each of the
 * rendering methods on the QDGraphics class can be established once in the beginDraw
 * method. Thus when this interface's draw method is called, this global state does not
 * have to be reestablished before each drawing operation is performed.
 */
public abstract interface QDDrawer {

    /**
     * A class can draw efficiently on the supplied QDGraphics when this method
     * is called on the QDDrawer object by the QDGraphics.beginDraw method.
     * @param g the QDGraphics that is currently set up to draw upon.
     * @param {@link quicktime.QTException}
     */
    public abstract void draw(QDGraphics paramQDGraphics) throws QTException;
}

