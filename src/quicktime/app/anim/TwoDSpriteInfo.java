
package quicktime.app.anim;

import quicktime.std.image.ImageDescription;


/**
 * @deprecated
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
     * @deprecated
     */
    public TwoDSpriteInfo(quicktime.util.EncodedImage paramEncodedImage,
            ImageDescription paramImageDescription,
            quicktime.std.image.Matrix paramMatrix,
            boolean paramBoolean,
            int paramInt) {
        this.image = paramEncodedImage;
        this.desc = paramImageDescription;
        this.matrix = paramMatrix;
        this.layer = paramInt;
        this.visible = paramBoolean;
    }

    /**
     * @deprecated
     */
    public TwoDSpriteInfo(quicktime.util.EncodedImage paramEncodedImage,
            ImageDescription paramImageDescription,
            quicktime.std.image.Matrix paramMatrix,
            boolean paramBoolean,
            int paramInt,
            quicktime.std.image.GraphicsMode paramGraphicsMode) {
        this.image = paramEncodedImage;
        this.desc = paramImageDescription;
        this.matrix = paramMatrix;
        this.layer = paramInt;
        this.visible = paramBoolean;
        this.graphicsMode = paramGraphicsMode;
    }

    public quicktime.util.EncodedImage getImage() {
        return this.image;
    }

    public ImageDescription getDescription() {
        return this.desc;
    }

    public quicktime.std.image.Matrix getMatrix() {
        return this.matrix;
    }

    public int getLayer() {
        return this.layer;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public quicktime.std.image.GraphicsMode getGraphicsMode() {
        return this.graphicsMode;
    }

    public String toString() {
        return getClass().getName() + "[image=" + this.image + ",desc=" + this.desc + ",matrix=" + this.matrix + ",layer="
               + this.layer + ",visible=" + this.visible + ",graphicsMode=" + this.graphicsMode + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * TwoDSpriteInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
