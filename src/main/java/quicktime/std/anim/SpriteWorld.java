package quicktime.std.anim;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDColor;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.Matrix;

/**
 * The SpriteWorld class implements the corresponding data structure of the Movie Toolbox.
 * Your application can use SpriteWorld's to make animations. A Sprite World handles the common tasks of
 * invalidating appropriate areas as sprite properties change, the composition of sprites and their background
 * on an offscreen buffer, and the blitting of the result to the screen or alternate destination.
 */
public class SpriteWorld extends QTObject implements QuickTimeLib {

    private static Object linkage;

    static {
        class PrivelegedAction {

            void establish() {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SpriteWorld.class);
                    }
                });
            }
        }
        new PrivelegedAction().establish();
    }

    private static int allocate(QDGraphics paramQDGraphics1, QDGraphics paramQDGraphics2, QDColor paramQDColor, QDGraphics paramQDGraphics3) throws StdQTException, QDException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            int i = NewSpriteWorld(arrayOfInt, QTObject.ID(paramQDGraphics1), QTObject.ID(paramQDGraphics2), paramQDColor == null ? null : paramQDColor.getRGBColor(), QTObject.ID(paramQDGraphics3));
            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    /**
     * This constructor creates a new sprite world.
     * QuickTime::NewSpriteWorld()
     * @param port the target destination of the SpriteWorld. This may NOT be null.
     * @param spriteLayer Specifies the mSpriteGWorld QDGraphics.
     * @param backgroundColor Specifies the background color.
     * @param background Specifies the background QDGraphics that can be used for better performance when
     *  you have background sprites
     */
    public SpriteWorld(QDGraphics paramQDGraphics1, QDGraphics paramQDGraphics2, QDColor paramQDColor, QDGraphics paramQDGraphics3) throws QTException {
        super(allocate(paramQDGraphics1, paramQDGraphics2, paramQDColor, paramQDGraphics3));
        setWidthHeight(paramQDGraphics2);
        setMatrix(this.mMatrix);
        this.destPort = QTObject.ID(paramQDGraphics1);
    }

    private Matrix mMatrix = new Matrix();

    private int mWidth;

    private int mHeight;

    private int destPort;

    private int[] savedPort = { 0 };

    private int[] savedDevice = { 0 };

    private void setWidthHeight(QDGraphics paramQDGraphics) {
        synchronized (QTNative.globalsLock) {
            QDRect localQDRect = paramQDGraphics.getPortRect();
            this.mWidth = localQDRect.getWidth();
            this.mHeight = localQDRect.getHeight();
        }
    }

    /**
     * Sets the bounds of the SpriteWorld by scaling its matrix.
     * @param bounds the new size of the SpriteWorld
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setBounds(QDRect paramQDRect) throws StdQTException {
        Matrix localMatrix = new Matrix();
        localMatrix.setTx(paramQDRect.getX());
        localMatrix.setTy(paramQDRect.getY());
        localMatrix.setSx(paramQDRect.getWidth() / this.mWidth);
        localMatrix.setSy(paramQDRect.getHeight() / this.mHeight);
        setMatrix(localMatrix);
    }

    /**
     * Returns the current bounds of the SpriteWorld.
     * @return the bounds of the SpriteWorld
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDRect getBounds() throws StdQTException {
        this.bounds.setX((int) this.mMatrix.getTx());
        this.bounds.setY((int) this.mMatrix.getTy());
        this.bounds.setWidth((int) (this.mMatrix.getSx() * this.mWidth));
        this.bounds.setHeight((int) (this.mMatrix.getSy() * this.mHeight));
        return this.bounds;
    }

    private QDRect bounds = new QDRect();

    /**
     * This method sets the sprite world's clip.
     * QuickTime::SetSpriteWorldClip()
     * @param clipRgn Specifies the clipping region.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setClip(Region paramRegion) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldClip(_ID(), QTObject.ID(paramRegion)));
        }
    }

    /**
     * This method sets the Graphic's mode of the SpriteWorld.
     * QuickTime::SetSpriteWorldGraphicsMode()
     * @param gMode the new graphic's mode for the SpriteWorld
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setGraphicsMode(GraphicsMode paramGraphicsMode) throws StdQTException {
        paramGraphicsMode = paramGraphicsMode == null ? new GraphicsMode() : paramGraphicsMode;
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldGraphicsMode(_ID(), paramGraphicsMode.getGraphicsMode(), paramGraphicsMode.getColor().getRGBColor()));
        }
    }

    /**
     * Returns the current transformational matrix used by the sprite world
     * @return the current matrix.
     */
    public final Matrix getMatrix() {
        return this.mMatrix;
    }

    /**
     * This method sets the sprite world's matrix.
     * QuickTime::SetSpriteWorldMatrix()
     * @param matrix Specifies the Matrix.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setMatrix(Matrix paramMatrix) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            this.mMatrix = (paramMatrix == null ? new Matrix() : paramMatrix);
            StdQTException.checkError(SetSpriteWorldMatrix(_ID(), this.mMatrix.getBytes()));
        }
    }

    /**
     * This method allows the sprite world to update and redraw the invalid portions of the sprite world.
     * QuickTime::SpriteWorldIdle()
     * @param flagsIn Contains flags describing what type of actions may take place during the idle,
     *  such as if drawing to the final destination should be pressed.
     * @return Flags describing what type of actions took place during the idle, such as if any drawing took place.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int idle(int paramInt) throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            int i = SpriteWorldIdle(_ID(), paramInt, arrayOfInt);
            StdQTException.checkError(i);
        }
        return arrayOfInt[0];
    }

    /**
     * This method allows a rectangular area of the sprite world to be invalidated,
     * which will force it to redraw the next time SpriteWorldIdle is called.
     * QuickTime::InvalidateSpriteWorld()
     * @param invalidArea A Rectangle which describes the invalid rectangle in sprite world source space.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void invalidate(QDRect paramQDRect) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(InvalidateSpriteWorld(_ID(), paramQDRect == null ? null : paramQDRect.getRect()));
        }
    }

    /**
     * This method is used to determine if a sprite exists at a certain location.
     * QuickTime::SpriteWorldHitTest()
     * @param flags This allows the hit testing operation to be controlled more precisely.
     * @param loc The point that you wish to test.
     * @return the Sprite object which received the hit, or null if no sprite at that location.
     * @return {@link quicktime.QTException}
     */
    public final Sprite hitTest(int paramInt, QDPoint paramQDPoint) throws QTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SpriteWorldHitTest(_ID(), paramInt, paramQDPoint.getPoint(), arrayOfInt));
        }
        return arrayOfInt[0] == 0 ? null : new Sprite(arrayOfInt[0], this);
    }

    /**
     * Set the flags for the sprite world.
     * QuickTime::SetSpriteWorldFlags()
     */
    public final void setFlags(int paramInt1, int paramInt2) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldFlags(_ID(), paramInt1, paramInt2));
        }
    }

    private static native short NewSpriteWorld(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3);

    private static native short SetSpriteWorldClip(int paramInt1, int paramInt2);

    private static native short SetSpriteWorldGraphicsMode(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native short SetSpriteWorldMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native short SpriteWorldIdle(int paramInt1, int paramInt2, int[] paramArrayOfInt);

    private static native short InvalidateSpriteWorld(int paramInt, byte[] paramArrayOfByte);

    private static native short SpriteWorldHitTest(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt);

    private static native short SetSpriteWorldFlags(int paramInt1, int paramInt2, int paramInt3);

    private static native void DisposeAllSprites(int paramInt);

    private static native short GetMoviesError();
}
