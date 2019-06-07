package quicktime.app.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.Drawable;
import quicktime.qd.QDRect;
import quicktime.qd.Region;

/**
 * Deprecated.
 * This class will present a normal java.awt.Image object as a client of the QTCanvas.
 */
public class ImageDrawer implements Drawable {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static ImageDrawer logo = null;

    /**
     * Deprecated.
     * This returns an ImageDrawer object that presents the standard QuickTime logo * @deprecated since QTJava 6.1
     */
    public static final ImageDrawer getQTLogo() {
        if (logo == null) {
            try {
                Class<?> localClass = Class.forName("quicktime.app.image.ImageDrawer");
                Object localObject = null;
                if (QTSession.getJavaVersion() == 65537) {
                    localObject = localClass.getResource("/qt3logo.jpg").getContent();
                } else {
                    class PrivilegedGetLogoAction {

                        Object getContent() {
                            return AccessController.doPrivileged(new PrivilegedAction<Object>() {

                                public Object run() {
                                    Object localObject = null;
                                    try {
                                        URL localURL = ImageDrawer.class.getResource("/qt3logo.jpg");
                                        localObject = localURL.getContent();
                                    } catch (Exception e) {
                                    }
                                    return localObject;
                                }
                            });
                        }
                    }
                    // TODO class
                    localObject = new PrivilegedGetLogoAction().getContent();
                }
                Image localImage = Toolkit.getDefaultToolkit().createImage((ImageProducer) localObject);
                logo = new ImageDrawer(new Dimension(162, 162), localImage);
            } catch (Exception e) {
            }
        }
        return logo;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This is the image that the redraw method will draw to the destination Canvas
     */
    protected Image im;

    /**
     * Deprecated.
     */
    public ImageDrawer(Dimension paramDimension, Image paramImage) {
        this.bounds = new QDRect(paramDimension);
        this.im = paramImage;
    }

    private QDRect bounds = new QDRect();

    /**
     * Deprecated.
     */
    protected Component canv;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is added to the object
     * that is the source of the interest. The ImageDrawer expect this object
     * to be some kind of java.awt.Component, in which case it will use that
     * Components java.awt.Graphics object to draw the java.awt.Image that it
     * presents in the redraw call.
     * @param interest the object that is to be the source of interest for the
     *  the object that implements this interface.
     */
    public void addedTo(Object paramObject) {
        if ((paramObject instanceof Component)) {
            this.canv = ((Component) paramObject);
            if (this.im != null) {
                this.canv.prepareImage(this.im, this.canv);
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called by the specified object when the instance of
     * the class that implements this interface is removed from the object
     * that is the source of the interest. The ImageDrawer expect this object
     * to be some kind of java.awt.Component, in which case it will remove its
     * capability to redraw its java.awt.Image in its redraw call.
     * @param interest the object that was the source of interest for the
     *  the object that implements this interface.
     */
    public void removedFrom(Object paramObject) {
        if ((paramObject instanceof Component))
            this.canv = null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the current image that this object is presenting
     */
    public Image getImage() {
        return this.im;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to tell the client to adjust its
     * location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @param {@link quicktime.QTException}
     */
    public void setDisplayBounds(QDRect paramQDRect) throws QTException {
        this.bounds = paramQDRect;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to ask the client for its boundary.
     * All values are in pixels.
     *
     * @return the display boundary of the client
     * @return {@link quicktime.QTException}
     */
    public QDRect getDisplayBounds() throws QTException {
        return this.bounds;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @param {@link quicktime.QTException}
     */
    public void redraw(Region paramRegion) throws QTException {
        if ((this.canv != null) && (this.im != null)) {
            Graphics localGraphics = this.canv.getGraphics();
            QDRect localQDRect = this.bounds;
            if (localQDRect == null)
                localQDRect = new QDRect(this.canv.getSize());
            localGraphics.drawImage(this.im, 0, 0, localQDRect.getWidth(), localQDRect.getHeight(), this.canv);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Makes a default ImageDrawer that will draw nothing as it has no image to present
     * nor component to present in
     */
    public ImageDrawer() {
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
