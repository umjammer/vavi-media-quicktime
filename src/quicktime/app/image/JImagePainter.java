
package quicktime.app.image;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import quicktime.QTRuntimeException;
import quicktime.QTSession;


/**
 * @deprecated
 */
public class JImagePainter implements Paintable {
    Image image;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public JImagePainter(Image paramImage) {
        this.image = paramImage;
    }

    private int width = -1;

    private int height = -1;

    private Rectangle[] r = new Rectangle[1];

    private boolean scaled = false;

    /**
     * @deprecated
     */
    public void prepareImage(Component paramComponent) {
        paramComponent.prepareImage(this.image, this.width, this.height, paramComponent);
    }

    /**
     * @deprecated
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
