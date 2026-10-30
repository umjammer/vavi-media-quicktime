package quicktime.std.anim;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MatrixManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.SpriteManager;
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

    private static int allocate(QDGraphics destination, QDGraphics spriteLayer, QDColor color, QDGraphics background) throws StdQTException, QDException {
        int[] newSpriteWorld = new int[1];
        synchronized (QTNative.globalsLock) {
            int i = NewSpriteWorld(newSpriteWorld, QTObject.ID(destination), QTObject.ID(spriteLayer), color == null ? null : color.getRGBColor(), QTObject.ID(background));
            StdQTException.checkError(i);
        }
        return newSpriteWorld[0];
    }

    /**
     * This constructor creates a new sprite world.
     *
     * @param port            the target destination of the SpriteWorld. This may NOT be null.
     * @param spriteLayer     Specifies the mSpriteGWorld QDGraphics.
     * @param backgroundColor Specifies the background color.
     * @param background      Specifies the background QDGraphics that can be used for better performance when
     *                        you have background sprites
     * @see "QuickTime::NewSpriteWorld()"
     */
    public SpriteWorld(QDGraphics port, QDGraphics spriteLayer, QDColor backgroundColor, QDGraphics background) throws QTException {
        super(allocate(port, spriteLayer, backgroundColor, background));
        setWidthHeight(spriteLayer);
        setMatrix(this.mMatrix);
        this.destPort = QTObject.ID(port);
    }

    private Matrix mMatrix = new Matrix();

    private int mWidth;

    private int mHeight;

    private final int destPort;

    private final int[] savedPort = {0};

    private final int[] savedDevice = {0};

    private void setWidthHeight(QDGraphics widthHeight) {
        synchronized (QTNative.globalsLock) {
            QDRect portRect = widthHeight.getPortRect();
            this.mWidth = portRect.getWidth();
            this.mHeight = portRect.getHeight();
        }
    }

    /**
     * Sets the bounds of the SpriteWorld by scaling its matrix.
     *
     * @param bounds the new size of the SpriteWorld
     * @throws quicktime.std.StdQTException
     */
    public final void setBounds(QDRect bounds) throws StdQTException {
        Matrix matrix = new Matrix();
        matrix.setTx(bounds.getX());
        matrix.setTy(bounds.getY());
        matrix.setSx(bounds.getWidth() / this.mWidth);
        matrix.setSy(bounds.getHeight() / this.mHeight);
        setMatrix(matrix);
    }

    /**
     * Returns the current bounds of the SpriteWorld.
     *
     * @return the bounds of the SpriteWorld
     * @throws quicktime.std.StdQTException
     */
    public final QDRect getBounds() throws StdQTException {
        this.bounds.setX((int) this.mMatrix.getTx());
        this.bounds.setY((int) this.mMatrix.getTy());
        this.bounds.setWidth((int) (this.mMatrix.getSx() * this.mWidth));
        this.bounds.setHeight((int) (this.mMatrix.getSy() * this.mHeight));
        return this.bounds;
    }

    private final QDRect bounds = new QDRect();

    /**
     * This method sets the sprite world's clip.
     *
     * @param clipRgn Specifies the clipping region.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteWorldClip()"
     */
    public final void setClip(Region clipRgn) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldClip(_ID(), QTObject.ID(clipRgn)));
        }
    }

    /**
     * This method sets the Graphic's mode of the SpriteWorld.
     *
     * @param gMode the new graphic's mode for the SpriteWorld
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteWorldGraphicsMode()"
     */
    public final void setGraphicsMode(GraphicsMode gMode) throws StdQTException {
        gMode = gMode == null ? new GraphicsMode() : gMode;
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldGraphicsMode(_ID(), gMode.getGraphicsMode(), gMode.getColor().getRGBColor()));
        }
    }

    /**
     * Returns the current transformational matrix used by the sprite world
     *
     * @return the current matrix.
     */
    public final Matrix getMatrix() {
        return this.mMatrix;
    }

    /**
     * This method sets the sprite world's matrix.
     *
     * @param matrix Specifies the Matrix.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SetSpriteWorldMatrix()"
     */
    public final void setMatrix(Matrix matrix) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            this.mMatrix = (matrix == null ? new Matrix() : matrix);
            StdQTException.checkError(SetSpriteWorldMatrix(_ID(), this.mMatrix.getBytes()));
        }
    }

    /**
     * This method allows the sprite world to update and redraw the invalid portions of the sprite world.
     *
     * @param flagsIn Contains flags describing what type of actions may take place during the idle,
     *                such as if drawing to the final destination should be pressed.
     * @return Flags describing what type of actions took place during the idle, such as if any drawing took place.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SpriteWorldIdle()"
     */
    public final int idle(int flagsIn) throws StdQTException {
        int[] flagsOut = new int[1];
        synchronized (QTNative.globalsLock) {
            int i = SpriteWorldIdle(_ID(), flagsIn, flagsOut);
            StdQTException.checkError(i);
        }
        return flagsOut[0];
    }

    /**
     * This method allows a rectangular area of the sprite world to be invalidated,
     * which will force it to redraw the next time SpriteWorldIdle is called.
     *
     * @param invalidArea A Rectangle which describes the invalid rectangle in sprite world source space.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::InvalidateSpriteWorld()"
     */
    public final void invalidate(QDRect invalidArea) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(InvalidateSpriteWorld(_ID(), invalidArea == null ? null : invalidArea.getRect()));
        }
    }

    /**
     * This method is used to determine if a sprite exists at a certain location.
     *
     * @param flags This allows the hit testing operation to be controlled more precisely.
     * @param loc   The point that you wish to test.
     * @return the Sprite object which received the hit, or null if no sprite at that location.
     * @throws quicktime.QTException
     * @see "QuickTime::SpriteWorldHitTest()"
     */
    public final Sprite hitTest(int flags, QDPoint loc) throws QTException {
        int[] spriteHit = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SpriteWorldHitTest(_ID(), flags, loc.getPoint(), spriteHit));
        }
        return spriteHit[0] == 0 ? null : new Sprite(spriteHit[0], this);
    }

    /**
     * Set the flags for the sprite world.
     *
     * @see "QuickTime::SetSpriteWorldFlags()"
     */
    public final void setFlags(int flags, int flagsMask) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(SetSpriteWorldFlags(_ID(), flags, flagsMask));
        }
    }

    private static short NewSpriteWorld(int[] newSpriteWorld, int destination, int spriteLayer, byte[] backgroundColor, int background) {
        return SpriteManager.NewSpriteWorld(newSpriteWorld, destination, spriteLayer, backgroundColor, background);
    }

    private static short SetSpriteWorldClip(int spriteWorld, int clipRgn) {
        return SpriteManager.SetSpriteWorldClip(spriteWorld, clipRgn);
    }

    private static short SetSpriteWorldGraphicsMode(int spriteWorld, int mode, byte[] opColor) {
        return SpriteManager.SetSpriteWorldGraphicsMode(spriteWorld, mode, opColor);
    }

    private static short SetSpriteWorldMatrix(int spriteWorld, byte[] matrix) {
        return MatrixManager.SetSpriteWorldMatrix(spriteWorld, matrix);
    }

    private static short SpriteWorldIdle(int spriteWorld, int flagsIn, int[] flagsOut) {
        return SpriteManager.SpriteWorldIdle(spriteWorld, flagsIn, flagsOut);
    }

    private static short InvalidateSpriteWorld(int spriteWorld, byte[] invalidArea) {
        return SpriteManager.InvalidateSpriteWorld(spriteWorld, invalidArea);
    }

    private static short SpriteWorldHitTest(int spriteWorld, int flags, int loc, int[] spriteHit) {
        return SpriteManager.SpriteWorldHitTest(spriteWorld, flags, loc, spriteHit);
    }

    private static short SetSpriteWorldFlags(int spriteWorld, int flags, int flagsMask) {
        return SpriteManager.SetSpriteWorldFlags(spriteWorld, flags, flagsMask);
    }

    private static void DisposeAllSprites(int spriteWorld) {
        SpriteManager.DisposeAllSprites(spriteWorld);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }
}
