package quicktime.app.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Deprecated.
 * The Paintable interface allows for implementors of this interface to paint
 * in the QTCanvas (via the QTImageDrawer).
 *
 * Standard AWT paint calls can be made on the graphics object that is supplied in the paint
 * call, with the rendered image being drawn by QuickTime.
 * @see quicktime.app.display.QTImageDrawer
 */
public abstract interface Paintable {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The Container object (QTImageDrawer) of the Paintable tells the paintable object the size of its available
     * drawing surface. Any drawing done outside of these bounds (originating at 0,0) will
     * be clipped. You should not call this method directly - it will be called by the QTImageDrawer
     * object when its old pixel data is invalid and new pixel data is required.
     *
     * After this call is received by the Paintable it should return in the paint method
     * a rectangle that describes the complete size of the image.
     * @param drawer the QTImageDrawer that is notifiying the Paintable object that its size has changed.
     * @param d the new size of the offscreen image that the painter can paint into.
     */
    public abstract void newSizeNotified(QTImageDrawer paramQTImageDrawer, Dimension paramDimension);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Paint on the graphics. The image that is painted on this graphics is rendered by
     * QuickTime. You can supply an array of Rectangles that tell the parent which parts
     * of the image were changed by this call. Only the pixels that are within these
     * rectangles will be changed in the pixel data that is drawn by the QT image.
     * Typically your first call to paint after being notified of a new size will
     * return a Rectangle which covers the entire image with subsequent calls having
     * the option of returning rectangles that describe only those regions of the pixel
     * data that have changed. Returning null will indicate that no pixel data has changed
     * Intelligent usage of this can improve drawing performance.
     * @param g the graphics to paint on.
     */
    public abstract Rectangle[] paint(Graphics paramGraphics);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Paintable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
