
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
 * @deprecated
 */
public class ImageDrawer implements Drawable {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    private static ImageDrawer logo = null;

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

                    localObject = new PrivilegedGetLogoAction().getContent(); // TODO class
                }

                Image localImage = Toolkit.getDefaultToolkit().createImage((ImageProducer) localObject);
                logo = new ImageDrawer(new Dimension(162, 162), localImage);
            } catch (Exception e) {
            }
        }

        return logo;
    }

    /**
     * @deprecated
     */
    protected Image im;

    public ImageDrawer(Dimension paramDimension, Image paramImage) {
        this.bounds = new QDRect(paramDimension);
        this.im = paramImage;
    }

    private QDRect bounds = new QDRect();

    protected Component canv;

    /**
     * @deprecated
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
     * @deprecated
     */
    public void removedFrom(Object paramObject) {
        if ((paramObject instanceof Component))
            this.canv = null;
    }

    /**
     * @deprecated
     */
    public Image getImage() {
        return this.im;
    }

    /**
     * @deprecated
     */
    public void setDisplayBounds(QDRect paramQDRect) throws QTException {
        this.bounds = paramQDRect;
    }

    /**
     * @deprecated
     */
    public QDRect getDisplayBounds() throws QTException {
        return this.bounds;
    }

    /**
     * @deprecated
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
     * @deprecated
     */
    public ImageDrawer() {
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /ImageDrawer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
