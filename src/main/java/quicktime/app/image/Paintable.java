
package quicktime.app.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;


/**
 * @deprecated
 */
public abstract interface Paintable {
    /**
     * @deprecated
     */
    public abstract void newSizeNotified(QTImageDrawer paramQTImageDrawer, Dimension paramDimension);

    /**
     * @deprecated
     */
    public abstract Rectangle[] paint(Graphics paramGraphics);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /Paintable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
