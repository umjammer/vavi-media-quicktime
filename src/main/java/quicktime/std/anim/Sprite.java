package quicktime.std.anim;

import java.awt.Dimension;
import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDDimension;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.ByteEncodedImage;
import quicktime.util.EncodedImage;
import quicktime.util.IntEncodedImage;
import quicktime.util.RawEncodedImage;

/**
 * The Sprite class implements the corresponding data structure of the Movie Toolbox.
 */
public final class Sprite extends QTObject implements QuickTimeLib, quicktime.jdirect.PrimitivesLib {

    private static Object linkage;

    private SpriteWorld sw;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(Sprite.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    Sprite(int paramInt, SpriteWorld paramSpriteWorld) throws QTException {
        super(paramInt);
        this.sw = paramSpriteWorld;
    }

    /**
     * This constructor creates a new Sprite object for a specified sprite world.
     * QuickTime::NewSprite()
     * @param itsSpriteWorld Specifies the sprite world.
     * @param idh an ImageDescription object that describes the image.
     * @param imageData Contains the sprite's image data.
     * @param matrix A Matrix object describing the sprite's matrix.
     * @param visible True if the sprite is visible.
     * @param layer The sprite's layer.
     */
    public Sprite(SpriteWorld paramSpriteWorld, ImageDescription paramImageDescription, EncodedImage paramEncodedImage, Matrix paramMatrix, boolean paramBoolean, int paramInt) throws QTException {
        super(allocate(paramSpriteWorld, paramImageDescription, paramEncodedImage, paramMatrix, paramBoolean, paramInt));
        this.sw = paramSpriteWorld;
    }

    private static int allocate(SpriteWorld paramSpriteWorld, ImageDescription paramImageDescription, EncodedImage paramEncodedImage, Matrix paramMatrix, boolean paramBoolean, int paramInt) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        int[] arrayOfInt = new int[1];
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = NewSprite(arrayOfInt, QTObject.ID(paramSpriteWorld), QTObject.ID(paramImageDescription), ((ByteEncodedImage) paramEncodedImage).getBytes(), paramMatrix.getBytes(), b, (short) paramInt);
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = NewSprite(arrayOfInt, QTObject.ID(paramSpriteWorld), QTObject.ID(paramImageDescription), ((IntEncodedImage) paramEncodedImage).getInts(), paramMatrix.getBytes(), b, (short) paramInt);
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = NewSprite(arrayOfInt, QTObject.ID(paramSpriteWorld), QTObject.ID(paramImageDescription), QTObject.ID((RawEncodedImage) paramEncodedImage), paramMatrix.getBytes(), b, (short) paramInt);
            }
            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    /**
     * Return the sprite world that this sprite belongs too
     */
    public SpriteWorld getSpriteWorld() {
        return this.sw;
    }

    /**
     * This method invalidates the portion of the sprite's sprite world which is occupied
     * by the sprite.
     * QuickTime::InvalidateSprite ()
     */
    public void invalidate() {
        synchronized (QTNative.globalsLock) {
            InvalidateSprite(_ID());
        }
    }

    /**
     * This method tests if a point is inside a sprite.
     * QuickTime::SpriteHitTest ()
     * @param flags Specifies the control flags.
     * @param loc Specifies the test point.
     * @return true if the point is inside the sprite, false if not.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean hitTest(int paramInt, QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = { 0 };
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SpriteHitTest(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfByte));
        }
        return arrayOfByte[0] != 0;
    }

    /**
     * This method gets the matrix property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the matrix.
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 1, localMatrix.getBytes());
            StdQTException.checkError(i);
        }
        return localMatrix;
    }

    /**
     * This method sets the matrix property of this sprite.
     * QuickTime::SetSpriteProperty ()
     * @param matrix Specifies the matrix.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 1, paramMatrix.getBytes());
            StdQTException.checkError(i);
        }
    }

    /**
     * This method gets the image description property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the image description.
     * @return {@link quicktime.std.StdQTException}
     */
    public ImageDescription getImageDescription() throws StdQTException {
        return ImageDescription.fromSprite(this);
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original width and height of the image currently being
     * presented by the sprite.
     * QuickTime::GetSpriteProperty ()
     * @return a Dimension
     * @return {@link quicktime.std.StdQTException}
     */
    public Dimension getInitialSize() throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, arrayOfInt);
            StdQTException.checkError(i);
        }
        return new Dimension(getShortFromHandle(arrayOfInt[0], 32), getShortFromHandle(arrayOfInt[0], 34));
    }

    /**
     * This method returns the original width and height of the image currently being
     * presented by the sprite.
     * QuickTime::GetSpriteProperty ()
     * @return a QDDimension
     * @return {@link quicktime.std.StdQTException}
     */
    public QDDimension getOriginalSize() throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, arrayOfInt);
            StdQTException.checkError(i);
        }
        return new QDDimension(getShortFromHandle(arrayOfInt[0], 32), getShortFromHandle(arrayOfInt[0], 34));
    }

    /**
     * This method sets the image description property of this sprite.
     * QuickTime::SetSpriteProperty ()
     * @param idh Specifies the image description.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setImageDescription(ImageDescription paramImageDescription) throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, arrayOfInt);
            StdQTException.checkError(i);
            if (arrayOfInt[0] == QTObject.ID(paramImageDescription))
                return;
            i = SetSpriteProperty(_ID(), 2, QTObject.ID(paramImageDescription));
            StdQTException.checkError(i);
        }
    }

    /**
     * This method gets the image data property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the image data.
     * @return {@link quicktime.std.StdQTException}
     */
    public RawEncodedImage getImageData() throws StdQTException {
        return RawEncodedImage.fromSprite(this);
    }

    /**
     * This method sets the image data property of this sprite.
     * It will also set the size of the image data to the size of the pointer. If the
     * actual size of the image data is smaller than the handle you should set the
     * imageDataSize after this call to the desired size.
     * QuickTime::SetSpriteProperty ()
     * @param data Specifies the image data.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setImageData(EncodedImage paramEncodedImage) throws StdQTException {
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((ByteEncodedImage) paramEncodedImage).getBytes());
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((IntEncodedImage) paramEncodedImage).getInts());
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, QTObject.ID((RawEncodedImage) paramEncodedImage));
            }
            StdQTException.checkError(i);
        }
        setImageDataSize(paramEncodedImage.getSize());
    }

    /**
     * This method sets the image data property of this sprite.
     * It will also set the size of the image data to the size of the pointer. If the
     * actual size of the image data is smaller than the handle you should set the
     * imageDataSize after this call to the desired size.
     *
     * This method can be useful to use if the SpriteWorld the Sprite is a member of
     * should be redrawn after changing the image data of a Sprite.
     * QuickTime::SetSpriteProperty ()
     * @param data Specifies the image data.
     * @return a boolean which indicates if the incoming image data is different (true)
     *  than the previous data the sprite was presented.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean setImageDataIfDifferent(EncodedImage paramEncodedImage) throws StdQTException {
        int[] arrayOfInt = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetSpriteProperty(_ID(), 3, arrayOfInt);
            StdQTException.checkError(i);
        }
        i = -50;
        synchronized (QTNative.globalsLock) {
            if ((paramEncodedImage instanceof ByteEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((ByteEncodedImage) paramEncodedImage).getBytes());
            } else if ((paramEncodedImage instanceof IntEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((IntEncodedImage) paramEncodedImage).getInts());
            } else if ((paramEncodedImage instanceof RawEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, QTObject.ID((RawEncodedImage) paramEncodedImage));
            }
            StdQTException.checkError(i);
        }
        int[] ai1 = new int[] { 0 };
        GetSpriteProperty(_ID(), 3, ai1);
        if (ai1[0] != arrayOfInt[0]) {
            setImageDataSize(paramEncodedImage.getSize());
            return true;
        }
        return false;
    }

    /**
     * This method gets the visibility property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the visibility.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean getVisible() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 4, arrayOfByte);
            StdQTException.checkError(i);
        }
        return arrayOfByte[0] != 0;
    }

    /**
     * This method sets the visibility property of this sprite.
     * QuickTime::SetSpriteProperty ()
     * @param visible Specifies the visibility.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVisible(boolean paramBoolean) throws StdQTException {
        int i = (byte) (paramBoolean ? 1 : 0);
        synchronized (QTNative.globalsLock) {
            int j = SetSpriteProperty(_ID(), 4, (short) i);
            StdQTException.checkError(j);
        }
    }

    /**
     * This method gets the layer property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the layer.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getLayer() throws StdQTException {
        short[] arrayOfShort = { 0 };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 5, arrayOfShort);
            StdQTException.checkError(i);
        }
        return arrayOfShort[0];
    }

    /**
     * This method sets the layer property of this sprite.
     * QuickTime::SetSpriteProperty ()
     * @param layer Specifies the layer.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setLayer(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 5, (short) paramInt);
            StdQTException.checkError(i);
        }
    }

    /**
     * This method gets the graphics mode property of this sprite.
     * QuickTime::GetSpriteProperty ()
     * @return the graphics mode.
     * @return {@link quicktime.std.StdQTException}
     */
    public GraphicsMode getGraphicsMode() throws StdQTException {
        GraphicsMode localGraphicsMode = new GraphicsMode();
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 6, localGraphicsMode.getBytes());
            StdQTException.checkError(i);
        }
        return localGraphicsMode;
    }

    /**
     * This method sets the graphics mode property of this sprite.
     * QuickTime::SetSpriteProperty ()
     * @param gMode Specifies the graphics mode.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            if (paramGraphicsMode != null) {
                int i = SetSpriteProperty(_ID(), 6, paramGraphicsMode.getBytes());
                StdQTException.checkError(i);
            }
        }
    }

    /**
     * This method gets the size of the image data that a sprite is currently displaying.
     * QuickTime::GetSpriteProperty ()
     * @return the size of the image data
     * @return {@link quicktime.std.StdQTException}
     */
    public int getImageDataSize() throws StdQTException {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 7, arrayOfInt);
            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    /**
     * This method sets the size of the image data that the sprite is currently displaying.
     * If you know the size of the image data it is recommended that you set this.
     * QuickTime::SetSpriteProperty ()
     * @param size the size of the image data
     * @param {@link quicktime.std.StdQTException}
     */
    public void setImageDataSize(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 7, paramInt);
            StdQTException.checkError(i);
        }
    }

    /**
     * This will remove the sprite from the SpriteWorld it is contained in and destroy its
     * internal structures. The Sprite is not usuable after this call.
     * If you have any other java objects that refer to this sprite, they will also be unusable
     * - so use this call with care.
     * QuickTime::DisposeSprite ()
     */
    public void remove() {
        synchronized (QTNative.globalsLock) {
            DisposeSprite(_ID());
        }
        this.sw = null;
        try {
            finalize();
        } catch (Throwable localThrowable) {
        }
    }

    private static native short NewSprite(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte paramByte, short paramShort);

    private static native short NewSprite(int[] paramArrayOfInt1, int paramInt1, int paramInt2, int[] paramArrayOfInt2, byte[] paramArrayOfByte, byte paramByte, short paramShort);

    private static native short NewSprite(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, byte paramByte, short paramShort);

    private static native void InvalidateSprite(int paramInt);

    private static native short SpriteHitTest(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short GetSpriteProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, short[] paramArrayOfShort);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, short paramShort);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetSpriteProperty(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native void DisposeSprite(int paramInt);

    private static native short getShortFromHandle(int paramInt1, int paramInt2);
}
