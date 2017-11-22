
package quicktime.app.image;

import quicktime.QTException;
import quicktime.app.actions.Invalidator;
import quicktime.app.anim.SWCompositor;
import quicktime.app.anim.TwoDSprite;


/**
 * @deprecated
 */
public abstract interface DynamicImage extends Compositable {
    /**
     * @deprecated
     */
    public abstract Invalidator addedToCompositor(SWCompositor paramSWCompositor,
                                                  TwoDSprite paramTwoDSprite) throws QTException;

    /**
     * @deprecated
     */
    public abstract void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/image
 * /DynamicImage.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
