package quicktime.app.anim;

import quicktime.std.image.ImageDescription;

/**
 * Deprecated.
 * This class can be used to retain the current sprite settings which can be used
 * at a later stage to reset the TwoDSprite to those settings. The EncodedImage and ImageDescription
 * of the TwoDSpriteInfo class are transient and thus will not be saved when this object is serialized.
 * Applications should use a QuickTime movie or other means of saving image data.
 */
public final class TwoDSpriteInfo implements quicktime.app.image.ImageSpec, java.io.Serializable {

    private static final long serialVersionUID = 3807095621519970448L;

    transient quicktime.util.EncodedImage image;

    transient ImageDescription desc;

    quicktime.std.image.Matrix matrix;

    int layer;

    boolean visible;

    quicktime.std.image.GraphicsMode graphicsMode;

    static {
        if ((quicktime.QTSession.isCurrentOS(4)) && (quicktime.QTSession.getJavaVersion() >= 65540)) {
            throw new quicktime.QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Contains the information that can be used to create a sprite.
     * @param im the image of the sprite
     * @param d the image description that describes the format of the im
     * @param m the display matrix applied to the sprite
     * @param v if true the sprite is/was visible
     * @param l the layer of the sprite in its sprite world
     */
    public TwoDSpriteInfo(quicktime.util.EncodedImage paramEncodedImage, ImageDescription paramImageDescription, quicktime.std.image.Matrix paramMatrix, boolean paramBoolean, int paramInt) {
        this.image = paramEncodedImage;
        this.desc = paramImageDescription;
        this.matrix = paramMatrix;
        this.layer = paramInt;
        this.visible = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Contains the information that can be used to create a sprite.
     * @param im the image of the sprite
     * @param d the image description that describes the format of the im
     * @param m the display matrix applied to the sprite
     * @param l the layer of the sprite in its sprite world
     * @param v if true the sprite is/was visible
     * @param gm the GraphicsMode (or null)
     */
    public TwoDSpriteInfo(quicktime.util.EncodedImage paramEncodedImage, ImageDescription paramImageDescription, quicktime.std.image.Matrix paramMatrix, boolean paramBoolean, int paramInt, quicktime.std.image.GraphicsMode paramGraphicsMode) {
        this.image = paramEncodedImage;
        this.desc = paramImageDescription;
        this.matrix = paramMatrix;
        this.layer = paramInt;
        this.visible = paramBoolean;
        this.graphicsMode = paramGraphicsMode;
    }

    /**
     * Deprecated.
     * This method returns the actual image data.
     * An Exception is throw if there was some problem retrieving the data.
     * @return The current image data of the sprite* @deprecated since QTJava 6.1
     */
    public quicktime.util.EncodedImage getImage() {
        return this.image;
    }

    /**
     * Deprecated.
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return The description that describes the image * @deprecated since QTJava 6.1
     */
    public ImageDescription getDescription() {
        return this.desc;
    }

    /**
     * Deprecated.
     * @return The display matrix of the Sprite * @deprecated since QTJava 6.1
     */
    public quicktime.std.image.Matrix getMatrix() {
        return this.matrix;
    }

    /**
     * Deprecated.
     * @return The layer of the sprite in its SpriteWorld * @deprecated since QTJava 6.1
     */
    public int getLayer() {
        return this.layer;
    }

    /**
     * Deprecated.
     * @return The visibility of the sprite * @deprecated since QTJava 6.1
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Deprecated.
     */
    public quicktime.std.image.GraphicsMode getGraphicsMode() {
        return this.graphicsMode;
    }

    /**
     * Deprecated.
     * @return a String representation of this object* @deprecated since QTJava 6.1
     */
    public String toString() {
        return getClass().getName() + "[image=" + this.image + ",desc=" + this.desc + ",matrix=" + this.matrix + ",layer=" + this.layer + ",visible=" + this.visible + ",graphicsMode=" + this.graphicsMode + "]";
    }
}

