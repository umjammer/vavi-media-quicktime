package quicktime.std.anim;

import java.awt.Dimension;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SpriteManager;
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

    private SpriteWorld sw;

    Sprite(int nativeRep, SpriteWorld sw) throws QTException {
        super(nativeRep);
        this.sw = sw;
    }

    /**
     * This constructor creates a new Sprite object for a specified sprite world.
     *
     * @param itsSpriteWorld Specifies the sprite world.
     * @param idh            an ImageDescription object that describes the image.
     * @param imageData      Contains the sprite's image data.
     * @param matrix         A Matrix object describing the sprite's matrix.
     * @param visible        True if the sprite is visible.
     * @param layer          The sprite's layer.
     * @see "QuickTime::NewSprite()"
     */
    public Sprite(SpriteWorld itsSpriteWorld, ImageDescription idh, EncodedImage imageData, Matrix matrix, boolean visible, int layer) throws QTException {
        super(allocate(itsSpriteWorld, idh, imageData, matrix, visible, layer));
        this.sw = itsSpriteWorld;
    }

    private static int allocate(SpriteWorld spriteWorld, ImageDescription idh, EncodedImage imageDataPtr, Matrix matrix, boolean visible, int layer) throws StdQTException {
        byte b = (byte) (visible ? 1 : 0);
        int[] newSprite = new int[1];
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((imageDataPtr instanceof ByteEncodedImage)) {
                i = NewSprite(newSprite, QTObject.ID(spriteWorld), QTObject.ID(idh), ((ByteEncodedImage) imageDataPtr).getBytes(), matrix.getBytes(), b, (short) layer);
            } else if ((imageDataPtr instanceof IntEncodedImage)) {
                i = NewSprite(newSprite, QTObject.ID(spriteWorld), QTObject.ID(idh), ((IntEncodedImage) imageDataPtr).getInts(), matrix.getBytes(), b, (short) layer);
            } else if ((imageDataPtr instanceof RawEncodedImage)) {
                i = NewSprite(newSprite, QTObject.ID(spriteWorld), QTObject.ID(idh), QTObject.ID((RawEncodedImage) imageDataPtr), matrix.getBytes(), b, (short) layer);
            }
            StdQTException.checkError(i);
        }
        return newSprite[0];
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
     *
     * @see "QuickTime::InvalidateSprite ()"
     */
    public void invalidate() {
        synchronized (QTNative.globalsLock) {
            InvalidateSprite(_ID());
        }
    }

    /**
     * This method tests if a point is inside a sprite.
     *
     * @param flags Specifies the control flags.
     * @param loc   Specifies the test point.
     * @return true if the point is inside the sprite, false if not.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteHitTest ()"
     */
    public boolean hitTest(int flags, QDPoint loc) throws StdQTException {
        byte[] wasHit = {0};
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SpriteHitTest(_ID(), flags, loc.getPoint(), wasHit));
        }
        return wasHit[0] != 0;
    }

    /**
     * This method gets the matrix property of this sprite.
     *
     * @return the matrix.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix matrix = new Matrix();
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 1, matrix.getBytes());
            StdQTException.checkError(i);
        }
        return matrix;
    }

    /**
     * This method sets the matrix property of this sprite.
     *
     * @param matrix Specifies the matrix.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setMatrix(Matrix matrix) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 1, matrix.getBytes());
            StdQTException.checkError(i);
        }
    }

    /**
     * This method gets the image description property of this sprite.
     *
     * @return the image description.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public ImageDescription getImageDescription() throws StdQTException {
        return ImageDescription.fromSprite(this);
    }

    /**
     * @return a Dimension
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     * @deprecated <I>since QTJava 4.0</I>
     * This method returns the original width and height of the image currently being
     * presented by the sprite.
     */
    public Dimension getInitialSize() throws StdQTException {
        int[] propertyValue = {0};
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, propertyValue);
            StdQTException.checkError(i);
        }
        return new Dimension(getShortFromHandle(propertyValue[0], 32), getShortFromHandle(propertyValue[0], 34));
    }

    /**
     * This method returns the original width and height of the image currently being
     * presented by the sprite.
     *
     * @return a QDDimension
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public QDDimension getOriginalSize() throws StdQTException {
        int[] propertyValue = {0};
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, propertyValue);
            StdQTException.checkError(i);
        }
        return new QDDimension(getShortFromHandle(propertyValue[0], 32), getShortFromHandle(propertyValue[0], 34));
    }

    /**
     * This method sets the image description property of this sprite.
     *
     * @param idh Specifies the image description.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setImageDescription(ImageDescription idh) throws StdQTException {
        int[] propertyValue = {0};
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 2, propertyValue);
            StdQTException.checkError(i);
            if (propertyValue[0] == QTObject.ID(idh))
                return;
            i = SetSpriteProperty(_ID(), 2, QTObject.ID(idh));
            StdQTException.checkError(i);
        }
    }

    /**
     * This method gets the image data property of this sprite.
     *
     * @return the image data.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public RawEncodedImage getImageData() throws StdQTException {
        return RawEncodedImage.fromSprite(this);
    }

    /**
     * This method sets the image data property of this sprite.
     * It will also set the size of the image data to the size of the pointer. If the
     * actual size of the image data is smaller than the handle you should set the
     * imageDataSize after this call to the desired size.
     *
     * @param data Specifies the image data.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setImageData(EncodedImage data) throws StdQTException {
        int i = -50;
        synchronized (QTNative.globalsLock) {
            if ((data instanceof ByteEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((ByteEncodedImage) data).getBytes());
            } else if ((data instanceof IntEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((IntEncodedImage) data).getInts());
            } else if ((data instanceof RawEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, QTObject.ID((RawEncodedImage) data));
            }
            StdQTException.checkError(i);
        }
        setImageDataSize(data.getSize());
    }

    /**
     * This method sets the image data property of this sprite.
     * It will also set the size of the image data to the size of the pointer. If the
     * actual size of the image data is smaller than the handle you should set the
     * imageDataSize after this call to the desired size.
     * <p>
     * This method can be useful to use if the SpriteWorld the Sprite is a member of
     * should be redrawn after changing the image data of a Sprite.
     *
     * @param data Specifies the image data.
     * @return a boolean which indicates if the incoming image data is different (true)
     * than the previous data the sprite was presented.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public boolean setImageDataIfDifferent(EncodedImage data) throws StdQTException {
        int[] propertyValue = {0};
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = GetSpriteProperty(_ID(), 3, propertyValue);
            StdQTException.checkError(i);
        }
        i = -50;
        synchronized (QTNative.globalsLock) {
            if ((data instanceof ByteEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((ByteEncodedImage) data).getBytes());
            } else if ((data instanceof IntEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, ((IntEncodedImage) data).getInts());
            } else if ((data instanceof RawEncodedImage)) {
                i = SetSpriteProperty(_ID(), 3, QTObject.ID((RawEncodedImage) data));
            }
            StdQTException.checkError(i);
        }
        int[] ai1 = new int[] {0};
        GetSpriteProperty(_ID(), 3, ai1);
        if (ai1[0] != propertyValue[0]) {
            setImageDataSize(data.getSize());
            return true;
        }
        return false;
    }

    /**
     * This method gets the visibility property of this sprite.
     *
     * @return the visibility.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public boolean getVisible() throws StdQTException {
        byte[] propertyValue = {0};
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 4, propertyValue);
            StdQTException.checkError(i);
        }
        return propertyValue[0] != 0;
    }

    /**
     * This method sets the visibility property of this sprite.
     *
     * @param visible Specifies the visibility.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setVisible(boolean visible) throws StdQTException {
        int i = (byte) (visible ? 1 : 0);
        synchronized (QTNative.globalsLock) {
            int j = SetSpriteProperty(_ID(), 4, (short) i);
            StdQTException.checkError(j);
        }
    }

    /**
     * This method gets the layer property of this sprite.
     *
     * @return the layer.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public int getLayer() throws StdQTException {
        short[] propertyValue = {0};
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 5, propertyValue);
            StdQTException.checkError(i);
        }
        return propertyValue[0];
    }

    /**
     * This method sets the layer property of this sprite.
     *
     * @param layer Specifies the layer.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setLayer(int layer) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 5, (short) layer);
            StdQTException.checkError(i);
        }
    }

    /**
     * This method gets the graphics mode property of this sprite.
     *
     * @return the graphics mode.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public GraphicsMode getGraphicsMode() throws StdQTException {
        GraphicsMode graphicsMode = new GraphicsMode();
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 6, graphicsMode.getBytes());
            StdQTException.checkError(i);
        }
        return graphicsMode;
    }

    /**
     * This method sets the graphics mode property of this sprite.
     *
     * @param gMode Specifies the graphics mode.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setGraphicsMode(GraphicsMode gMode) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            if (gMode != null) {
                int i = SetSpriteProperty(_ID(), 6, gMode.getBytes());
                StdQTException.checkError(i);
            }
        }
    }

    /**
     * This method gets the size of the image data that a sprite is currently displaying.
     *
     * @return the size of the image data
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetSpriteProperty ()"
     */
    public int getImageDataSize() throws StdQTException {
        int[] propertyValue = {0};
        synchronized (QTNative.globalsLock) {
            int i = GetSpriteProperty(_ID(), 7, propertyValue);
            StdQTException.checkError(i);
        }
        return propertyValue[0];
    }

    /**
     * This method sets the size of the image data that the sprite is currently displaying.
     * If you know the size of the image data it is recommended that you set this.
     *
     * @param size the size of the image data
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteProperty ()"
     */
    public void setImageDataSize(int size) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = SetSpriteProperty(_ID(), 7, size);
            StdQTException.checkError(i);
        }
    }

    /**
     * This will remove the sprite from the SpriteWorld it is contained in and destroy its
     * internal structures. The Sprite is not usuable after this call.
     * If you have any other java objects that refer to this sprite, they will also be unusable
     * - so use this call with care.
     *
     * @see "QuickTime::DisposeSprite ()"
     */
    public void remove() {
        synchronized (QTNative.globalsLock) {
            DisposeSprite(_ID());
        }
        this.sw = null;
        try {
            _dispose();
        } catch (Throwable _) {
        }
    }

    private static short NewSprite(int[] newSprite, int spriteWorld, int idh, byte[] imageDataPtr, byte[] matrix, byte visible, short layer) {
        return SpriteManager.NewSprite(newSprite, spriteWorld, idh, imageDataPtr, matrix, visible, layer);
    }

    private static short NewSprite(int[] newSprite, int spriteWorld, int idh, int[] imageDataPtr, byte[] matrix, byte visible, short layer) {
        return SpriteManager.NewSprite(newSprite, spriteWorld, idh, imageDataPtr, matrix, visible, layer);
    }

    private static short NewSprite(int[] newSprite, int spriteWorld, int idh, int imageDataPtr, byte[] matrix, byte visible, short layer) {
        return SpriteManager.NewSprite(newSprite, spriteWorld, idh, imageDataPtr, matrix, visible, layer);
    }

    private static void InvalidateSprite(int sprite) {
        SpriteManager.InvalidateSprite(sprite);
    }

    private static short SpriteHitTest(int sprite, int flags, int loc, byte[] wasHit) {
        return SpriteManager.SpriteHitTest(sprite, flags, loc, wasHit);
    }

    private static short GetSpriteProperty(int sprite, int propertyType, short[] propertyValue) {
        return SpriteManager.GetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short GetSpriteProperty(int sprite, int propertyType, int[] propertyValue) {
        return SpriteManager.GetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short GetSpriteProperty(int sprite, int propertyType, byte[] propertyValue) {
        return SpriteManager.GetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short SetSpriteProperty(int sprite, int propertyType, short[] propertyValue) {
        return SpriteManager.SetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short SetSpriteProperty(int sprite, int propertyType, short propertyValue) {
        return SpriteManager.SetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short SetSpriteProperty(int sprite, int propertyType, int[] propertyValue) {
        return SpriteManager.SetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short SetSpriteProperty(int sprite, int propertyType, int propertyValue) {
        return SpriteManager.SetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static short SetSpriteProperty(int sprite, int propertyType, byte[] propertyValue) {
        return SpriteManager.SetSpriteProperty(sprite, propertyType, propertyValue);
    }

    private static void DisposeSprite(int sprite) {
        SpriteManager.DisposeSprite(sprite);
    }

    private static short getShortFromHandle(int h, int offset) {
        return MemoryManager.getShortFromHandle(h, offset);
    }
}
