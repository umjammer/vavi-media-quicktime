package quicktime.app.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import quicktime.QTRuntimeException;
import quicktime.QTSession;

/**
 * Deprecated.
 * This class implements the Paintable interface and is used in conjunction
 * with a QTImageDrawer to capture the resulting pixel data of the specified
 * java.awt.Image for QuickTime to render.
 * @see {@link quicktime.app.image.QTImageDrawer}
 */
public class JImagePainter implements Paintable {

    Image image;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A Utility class to render java.awt.Image objects where the
     * resulting pixel data will be drawn within a QuickTime context. This
     * class is generally used in conjunction with the QTImageDrawer.
     * @param image the java.awt.Image to draw.
     * {@link quicktime.app.image.QTImageDrawer}
     */
    public JImagePainter(Image paramImage) {
        this.image = paramImage;
    }

    private int width = -1;

    private int height = -1;

    private Rectangle[] r = new Rectangle[1];

    private boolean scaled = false;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This prepares the Image for drawing, ensuring that all of the image
     * data is available and present.
     * @param javaComponent the java.awt.Component
     */
    public void prepareImage(Component paramComponent) {
        paramComponent.prepareImage(this.image, this.width, this.height, paramComponent);
    }

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
    public void newSizeNotified(QTImageDrawer paramQTImageDrawer, Dimension paramDimension) {
        this.width = paramDimension.width;
        this.height = paramDimension.height;
        this.r[0] = new Rectangle(this.width, this.height);
    }

    /**
     * @deprecated
     */
    public Rectangle[] paint(Graphics paramGraphics) {
        paramGraphics.drawImage(this.image, 0, 0, this.width, this.height, null);
        return this.r;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /JImagePainter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
