package quicktime.app.anim;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.RecordMovie;
import quicktime.app.actions.Invalidator;
import quicktime.app.actions.Notifier;
import quicktime.app.actions.TickleList;
import quicktime.app.display.DrawingListener;
import quicktime.app.display.DrawingNotifier;
import quicktime.app.display.Layerable;
import quicktime.app.display.QTDisplaySpace;
import quicktime.app.image.Compositable;
import quicktime.app.image.DynamicImage;
import quicktime.app.image.Transformable;
import quicktime.app.spaces.Protocol;
import quicktime.app.spaces.SimpleSpace;
import quicktime.app.time.Timer;
import quicktime.qd.QDColor;
import quicktime.qd.QDDimension;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.anim.Sprite;
import quicktime.std.anim.SpriteWorld;
import quicktime.std.image.GraphicsMode;
import quicktime.std.image.ImageDescription;
import quicktime.std.image.Matrix;
import quicktime.util.EncodedImage;
import quicktime.util.RawEncodedImage;

/**
 * Deprecated.
 * SWCompositor provides the capability to composit a complex image together of disparate
 * image sources and then treat the result as a single image which is presented to the user.
 *
 * It uses the QuickTime SpriteWorld internally to perform the compositing tasks. Thus all
 * of the actual drawing of the members of a SWCompositor is handled through the interaction
 * between the SpriteWorld and Sprite classes of quicktime.
 *
 * The SpriteWorld itself is wrapped by the SWCompositor class, and to represent the Sprite
 * class it uses the TwoDSprite. The TwoDSprite is a presenter - it presents image information
 * and the presenation of the image information within the context of the SWCompositor's SpriteWorld
 * is determined by the matrix, graphics mode, layer and visibility of the TwoDSprite's Sprite object.
 *
 * To create a Sprite you need a valid SpriteWorld. To create a SpriteWorld you need a valid QDGraphics
 * destination. Depending on whether a SWCompositor is visible or not you may or may not have a valid
 * destination QDGraphics. The interaction between the SWCompositor and its TwoDSprite presenters
 * handles the saving and creating of SpriteWorlds and their Sprites - the application does not need to
 * deal specifically with this issue.
 *
 * The SWCompositor has been designed without the requirement that its members be TwoDSprites. It maybe more
 * convenient for an application to add members of a Compositor that are a source of image data and
 * have the addMember method deal with the mechanics of creating the TwoDSprite presenter. Thus there are
 * also methods that must be defined by the subclass for getting a member's TwoDSprite, and getting a TwoDSprite's
 * member. This is important to understand for the following reason. Once a member
 * has been added to a SWCompositor, it is the presenter within the SWCompositor itself (the TwoDSprite) that must be
 * interacted with to effect the display characteristics of the member in the SWCompositor. Whilst a subclass
 * may provide the capability to create the TwoDSprite presenter with display characteristics like Matrix and
 * GraphicsMode, once the member is added altering the member's display characteristics directly may or may not effect
 * the display characteristics of the member in the SWCompositor. Thus an application is better to always interact
 * with the member's TwoDSprite presenter.
 *
 * After a member is added to the SWCompositor the subclass should call the postProcessAdd (and remove in the case of
 * a removeMember call). This call will look at the member that is being added and will perform two services. If the
 * new member is an instance of the Notifier class then the SWCompositor registers the Notifer with the TwoDSprite presenter
 * as the NotifyListener. That way a Sprite who presents image data that is not available at the time the member is added
 * can still prepare itself for displaying an image when its Notifier notifies the TwoDSprite that its image data is valid.
 * If the new member is an instance of DynamicImage, then this method will also call that member's addedToCompositor method
 * to allow this new member to establish the invalidation mechanism it uses. Explicit invalidation is required for the SpriteWorld
 * to redraw a Sprite that presents image data that is changing (such as a Movie) when nothing about the Sprite's presentation
 * has changed.
 *
 * The subclass defines completely the storage mechanism and protocols of its members.
 * @see {@link quicktime.std.anim.SpriteWorld},
 * {@link quicktime.std.anim.Sprite},
 * {@link quicktime.app.anim.TwoDSprite},
 * quicktime.app.display.Compositor
 */
public abstract class SWCompositor extends SimpleSpace implements QTDisplaySpace, DynamicImage, DrawingNotifier {

    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Used by subclasses to construct a SWCompositor. The subclass must specify a background or a backgroundColor
     * but not both.
     * @param spriteGWorld this the buffer which the internal SpriteWorld uses to composit its image into.
     * @param background this is background that can be supplied to the SpriteWorld
     * @param backgroundColor this is background color that can be supplied to the SpriteWorld
     * @param scale the initial scale of the Space's timer
     * @param period the initial period of the Space's timer
     * @param defaultProtocol the minimal Protocol that members of the SWCompositor must meet
     */
    protected SWCompositor(QDGraphics paramQDGraphics1, QDColor paramQDColor, QDGraphics paramQDGraphics2, int paramInt1, int paramInt2, Protocol paramProtocol) throws QTException {
        super(paramProtocol, paramInt1, paramInt2);
        this.mSpriteGWorld = paramQDGraphics1;
        this.mBGColor = paramQDColor;
        this.mBGPict = paramQDGraphics2;
        this.bnds = paramQDGraphics1.getPortRect();
    }

    /**
     * Deprecated.
     * default hit test is only to test on the visible pixels of a sprite* @deprecated since QTJava 6.1
     */
    public int hitTestFlag = 10;

    private QDPoint hitTestPoint = new QDPoint(0, 0);

    private QDGraphics mBGPict;

    private QDGraphics mSpriteGWorld;

    private Region mClip = null;

    private QDColor mBGColor;

    private QDGraphics mCurrentGrafPort = QDGraphics.scratch;

    private QDRect bnds;

    private GraphicsMode gMode;

    private Matrix savedMatrix = new Matrix();

    private ASpriteWorld mSpriteWorld = null;

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This flag is used when the task method is called.
     * You can use any of the values from the idle in flags of the SpriteWorld
     */
    public int taskFlag = 0;

    private int lastDrawRequiresDrawing = 0;

    private TickleList invalList;

    private RecordMovie rm;

    private boolean recordMovie = false;

    TwoDSprite spritePresenter;

    private long startTime = 0L;

    private int frameCount = 0;

    private SWCompositor parent;

    private boolean autoRedraw = true;

    private Vector listenerList;

    final class ASpriteWorld extends SpriteWorld {

        SWCompositor theCompositor;

        ASpriteWorld(QDGraphics paramQDGraphics1, QDGraphics paramQDGraphics2, QDColor paramQDColor, QDGraphics paramQDGraphics3, SWCompositor paramSWCompositor) throws QTException {
            super(paramQDGraphics1, paramQDGraphics2, paramQDColor, paramQDGraphics3);
            this.theCompositor = paramSWCompositor;
        }

        void _idle(int paramInt) throws QTException {
            SWCompositor.this.lastDrawRequiresDrawing = idle(paramInt);
            if (SWCompositor.this.recordMovie) {
                SWCompositor.this.recordMovie = SWCompositor.this.rm.frameReady();
                if (!SWCompositor.this.recordMovie)
                    SWCompositor.this.rm = null;
            }
            if (SWCompositor.this.listenerList != null) {
                synchronized (SWCompositor.this) {
                    for (int i = 0; i < SWCompositor.this.listenerList.size(); i++) ((DrawingListener) SWCompositor.this.listenerList.elementAt(i)).drawingComplete(this.theCompositor);
                }
            }
        }

        void _cleanup() {
            try {
                finalize();
            } catch (Throwable localThrowable) {
                QTRuntimeException.handleOrThrow(new QTRuntimeException(localThrowable.getMessage()), this, "finalize");
            }
        }
    }

    SpriteWorld getSpriteWorld() {
        return this.mSpriteWorld;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Resets the internal counters that are used to calculate the frame rate of the Compositor.
     */
    public void resetStatistics() {
        this.startTime = System.currentTimeMillis();
        this.frameCount = 0;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * The compositor's frame rate is calculated by the number of times it is tickled from
     * the time an application calls resetStatistics and is only valid if the rate of
     * the Compositor's Timer is not zero. If this rate is zero this returns 0.
     * Thus an application uses these calls like:
     *
     * myCompositor.resetStatistics();
     * Thread.sleep (2000);	//sleep the stat's thread for 2 seconds to allow gathering of profile info
     * float fps = myCompositor.getStatistics();
     *
     * This call does not reset the internal counters, so consequent calls of getStatistics
     * will return frames rendered over a longer period of time.
     */
    public float getStatistics() {
        if ((getTimer().getRate() == 0.0F) || (this.mSpriteWorld == null))
            return 0.0F;
        if (this.frameCount == 0)
            return 0.0F;
        return 1000.0F / ((System.currentTimeMillis() - this.startTime) / this.frameCount);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the list of invalidator objects for the Compositor's sprites
     * that have a dynamic image source. If no members of the Compositor
     * are dynamic image sources this call returns null.
     * @return an Enumeration
     */
    public Enumeration getInvalidatorList() {
        return this.invalList == null ? null : this.invalList.members();
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the invalidator that is associated with the supplied
     * member if the member has an Invalidator. If the member doesn't
     * or the object is not a member of the Compositor this returns null.
     * @param member a member of the Compositor
     * @return an Invalidator
     */
    public synchronized Invalidator getMemberInvalidator(Object paramObject) {
        if (!hasMember(paramObject))
            return null;
        if (this.invalList == null)
            return null;
        Enumeration localEnumeration = this.invalList.members();
        while (localEnumeration.hasMoreElements()) {
            Invalidator localInvalidator = (Invalidator) localEnumeration.nextElement();
            if (localInvalidator.getTarget().equals(paramObject))
                return localInvalidator;
        }
        return null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method allows the application to register the intent to record a movie out of the
     * compositing output of the SWCompositor. After each composit cycle the SWCompositor will
     * call the frameReady of the RecordMovie object as long as the frameReady method returns true.
     * @param rm the RecordMovie object
     * @param {@link quicktime.QTException}
     */
    public void setRecordMovie(RecordMovie paramRecordMovie) throws QTException {
        this.rm = paramRecordMovie;
        paramRecordMovie.addedToSource(this.mSpriteGWorld);
        this.recordMovie = true;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns true if the SWCompositor has a RecordMovie object.
     * @return a boolean
     */
    public boolean hasRecordMovie() {
        return this.recordMovie;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should return an ImageDescription that describes
     * the image data that the class contains.
     * @return the ImageDescription of an Image
     * @return {@link quicktime.QTException}
     */
    public ImageDescription getDescription() throws QTException {
        return new ImageDescription(this.mSpriteGWorld.getPixMap());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the actual image data.
     * @return the image data
     * @return {@link quicktime.QTException}
     * @return java.lang.IndexOutOfBoundsException
     */
    public EncodedImage getImage() throws QTException, IndexOutOfBoundsException {
        return RawEncodedImage.fromPixMap(this.mSpriteGWorld.getPixMap());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the SWCompositor that this is the parent container of this SWCompositor.
     * This call may return null if the calling object is the top level containing space.
     * @return the parent SWCompositor or null
     */
    public SWCompositor getParent() {
        return this.parent;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the client's current display transformations
     *
     * @return the display transformations of the client
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        return this.mSpriteWorld != null ? this.mSpriteWorld.getMatrix() : this.savedMatrix;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the current matrix of the Transformable object to
     * the new matrix.
     */
    public void setMatrix(Matrix paramMatrix) throws QTException {
        if (this.mSpriteWorld != null) {
            this.mSpriteWorld.setMatrix(paramMatrix);
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 4.0</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @see ()
     */
    public Dimension getInitialSize() throws QTException {
        QDRect localQDRect = this.mSpriteGWorld.getPortRect();
        return new Dimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the original size before the transformations
     * specified in the returned matrix are applied.
     * @return the size of the offscreen buffer of the Compositor
     * @return {@link quicktime.QTException}
     */
    public QDDimension getOriginalSize() throws QTException {
        QDRect localQDRect = this.mSpriteGWorld.getPortRect();
        return new QDDimension(localQDRect.getWidth(), localQDRect.getHeight());
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to get the destination QDGraphics of its client.
     * @return a QDGraphics object or null
     */
    public QDGraphics getGWorld() {
        return this.mCurrentGrafPort;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method to give the client the QDGraphics object it should
     * use to do its drawing. If the incoming QDGraphics is the QDGraphics.scratch then
     * the drawable will not be visible and can disable itself.
     * If a client is unable to set the graphics world it should throw an exception.
     *
     * @param cgp a QDGraphics object
     * @param {@link quicktime.QTException}
     */
    public synchronized void setGWorld(QDGraphics paramQDGraphics) throws QTException {
        if (paramQDGraphics == null)
            paramQDGraphics = QDGraphics.scratch;
        if (paramQDGraphics.equals(this.mCurrentGrafPort)) {
            return;
        }
        getTimer().setActive(false);
        if ((this.mSpriteWorld == null) && (!QDGraphics.scratch.equals(paramQDGraphics))) {
            this.mSpriteWorld = new ASpriteWorld(paramQDGraphics, this.mSpriteGWorld, this.mBGColor, this.mBGPict, this);
            if (this.gMode != null)
                this.mSpriteWorld.setGraphicsMode(this.gMode);
            setMatrix(this.savedMatrix);
            createMembers();
        } else if (QDGraphics.scratch.equals(paramQDGraphics)) {
            saveMembers();
            this.savedMatrix = this.mSpriteWorld.getMatrix();
            this.mSpriteWorld._cleanup();
            this.mSpriteWorld = null;
        } else {
            saveMembers();
            this.savedMatrix = this.mSpriteWorld.getMatrix();
            this.mSpriteWorld._cleanup();
            this.mSpriteWorld = null;
            this.mSpriteWorld = new ASpriteWorld(paramQDGraphics, this.mSpriteGWorld, this.mBGColor, this.mBGPict, this);
            if (this.gMode != null)
                this.mSpriteWorld.setGraphicsMode(this.gMode);
            setMatrix(this.savedMatrix);
            createMembers();
        }
        this.mCurrentGrafPort = paramQDGraphics;
        getTimer().setActive(this.mSpriteWorld != null);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to tell the client to adjust
     * its location, width and/or height. All values are in pixels.
     *
     * @param bounds the new size and location.
     * @param {@link quicktime.std.StdQTException}
     */
    public synchronized void setDisplayBounds(QDRect paramQDRect) throws StdQTException {
        if (this.mSpriteWorld != null) {
            this.mSpriteWorld.setBounds(paramQDRect);
        } else {
            this.savedMatrix.setTx(paramQDRect.getX());
            this.savedMatrix.setTy(paramQDRect.getY());
            QDRect localQDRect = this.mSpriteGWorld.getPortRect();
            this.savedMatrix.setSx(paramQDRect.getWidth() / localQDRect.getWidth());
            this.savedMatrix.setSy(paramQDRect.getHeight() / localQDRect.getHeight());
        }
        this.bnds = paramQDRect;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * A convenience method to set the position of a QTDrawable object.
     * @param x the new x location
     * @param y the new y location
     * @param {@link quicktime.QTException}
     */
    public void setLocation(int paramInt1, int paramInt2) throws QTException {
        QDRect localQDRect = getDisplayBounds();
        localQDRect.setX(paramInt1);
        localQDRect.setY(paramInt2);
        setDisplayBounds(localQDRect);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when it needs to get the client's
     * location, width and/or height. All values are in pixels.
     * @return bounds the new size and location.
     * @return {@link quicktime.QTException}
     */
    public QDRect getDisplayBounds() throws QTException {
        if (this.mSpriteWorld != null)
            return this.mSpriteWorld.getBounds();
        return this.bnds;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * QTCanvas calls this method when the client should redraw itself.
     * If the canvas is able to discern that only a part of the client's
     * drawing area needs to be redrawn - then this area shall be passed in
     * using the invalidRgn. Otherwise this will be null in which case the
     * client should redraw itself entirely.
     * @param invalidRgn the invalidRgn that the client should redraw
     * @param {@link quicktime.QTException}
     */
    public final void redraw(Region paramRegion) throws QTException {
        if (this.mSpriteWorld != null) {
            QDRect localQDRect1 = null;
            if (paramRegion != null) {
                QDRect localQDRect2 = paramRegion.getRgnBBox();
                localQDRect1 = localQDRect2.union(getDisplayBounds());
                if (localQDRect2.equals(localQDRect1)) {
                    localQDRect1 = null;
                } else
                    localQDRect1 = localQDRect2.intersection(getDisplayBounds());
            }
            this.mSpriteWorld.invalidate(localQDRect1);
            idle();
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current state of the auto redrawing flag.
     * @return a boolean.
     * @see (boolean)
     */
    public boolean isAutoRedrawing() {
        return this.autoRedraw;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * If a SWCompositor's time rate is equal to zero, changing a property of a member's sprite
     * requires the SWCompositor to be redrawn to see this change. This is done automatically for
     * you by the TwoDSprite class. However, if an application wishes to change a number of properties of
     * a given sprite or sprites, redrawing the Compositor after each change can be an udesirable side-effect
     * of this automatic redrawing capability. The application can temporarily turn or or off this capability of
     * the SWCompositor, which is controlled by the TwoDSprite, by changing the setting of the auto redrawing flag.
     * If this auto redrawing flag is false, the TwoDSprite will not automatically redraw its parent SWCompositor,
     * if the flag is true then the TwoDSprite will redraw its parent.
     *
     * Typically an application will set this flag to false, make the changes to its sprites, then reset the flag
     * to true. The application will still have to redraw the SWCompositor after making the changes to its
     * member sprites.
     *
     * If the rate of the SWCompositor's TimeBase is not equal to zero, then this flag has no effect as the composite
     * cycle is being controlled by the time-based rendering cycle.
     *
     * Regardless of the value of this flag, the SWCompositor will still redraw itself when the redraw method
     * is called. This flag only controls whether the SWCompositor's member sprites will automatically redraw their parents.
     * @param flag controls the auto redrawing capability of a SWCompositor. By default the auto-redrawing is set to true.
     */
    public void setAutoRedrawing(boolean paramBoolean) {
        this.autoRedraw = paramBoolean;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method can be called to explicitly force the SWCompositor to
     * redraw itself. It does not ticke the Compositor - so no ticklish or
     * invalidate actions will be performed. It will just force the internal
     * QuickTime SpriteWorld to do a composit cycle and redraw those areas
     * of the SpriteWorld which are invalid.
     *
     * In the normal course of operations an application does not need to call this method
     * but it is particularly useful for profiling. It should only
     * be called if the SpriteWorld that is presenting the SWCompositor is NOT null - otherwise
     * a NullPointerException will be thrown.
     */
    protected void idle() throws QTException {
        this.mSpriteWorld._idle(this.taskFlag);
        if ((this.spritePresenter != null) && (this.lastDrawRequiresDrawing != 0)) {
            this.lastDrawRequiresDrawing = 0;
            this.spritePresenter.invalidate();
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return the frontmost object that is located at the specified coordinates.
     * Hit test coordinates will take into account the location of the space
     * thus if the space is at 50, 50 and an object is located in the top,left
     * of the space then the hit test coords for the first pixel of this object
     * is 50, 50.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return an Object at that coordinate or null if no object at specified coordinate
     * @return {@link quicktime.QTException}
     */
    public Object hitTest(int paramInt1, int paramInt2) throws QTException {
        if (this.mSpriteWorld != null) {
            this.hitTestPoint.setX(paramInt1);
            this.hitTestPoint.setY(paramInt2);
            if ((getClip() != null) && (!getClip().pointIn(this.hitTestPoint))) {
                return null;
            }
            Sprite localSprite = this.mSpriteWorld.hitTest(this.hitTestFlag, this.hitTestPoint);
            if (localSprite == null)
                return null;
            Enumeration localEnumeration = sprites();
            while (localEnumeration.hasMoreElements()) {
                TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                if (localSprite.equals(localTwoDSprite.getSprite())) {
                    return localTwoDSprite;
                }
            }
        }
        return null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method sets the sprite world's clip.
     * QuickTime::SetSpriteWorldClip()
     * @param clipRgn Specifies the clipping region.
     * @param {@link quicktime.QTException}
     */
    public void setClip(Region paramRegion) throws QTException {
        if (this.mSpriteWorld != null) {
            this.mSpriteWorld.setClip(paramRegion);
        }
        this.mClip = paramRegion;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns the current clipping region used by the sprite world
     * @return the current clip region.
     */
    public Region getClip() {
        return this.mClip;
    }

    /**
     * Deprecated.
     * The Timer invokes the tickle method when the invocation constraints of
     * the Timer are reached.
     * @param er the effective rate of the Timer when the time condition matches
     * @param time the time that the Timer invoked the tickle method.
     * @return true if the Ticklish object should receive a consequent tickle, or false
     *  if the Ticklish object should not be tickled again.
     * @return {@link quicktime.QTException}
     */
    public boolean tickle(float paramFloat, int paramInt) throws QTException {
        Timer localTimer = getTimer();
        if ((localTimer.isActive()) && (this.mSpriteWorld != null)) {
            super.tickle(paramFloat, paramInt);
            if (this.invalList != null)
                this.invalList.tickle(paramFloat, paramInt);
            idle();
            this.frameCount += 1;
            return this.mSpriteWorld != null;
        }
        return false;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * When a dynamic image class is removed from a SWCompositor this
     * method is called to allow the class to cleanup any of the invalidation
     * processes it had established in the addedToCompositor call.
     * @param c the SWCompositor it is being removed from
     * @param {@link quicktime.QTException}
     */
    public synchronized void removedFromCompositor(SWCompositor paramSWCompositor) throws QTException {
        this.spritePresenter.spriteIsCompositor = null;
        this.spritePresenter = null;
        this.parent = null;
        setGWorld(QDGraphics.scratch);
        this.taskFlag = 0;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Returns an object that invalidates a sprite. When a DynamicImage
     * is a member of a SWompositor and the DynamicImage presents a
     * changing image (for example a movie), the TwoDSprite that presents
     * that image in the SWCompositor must be invalidated so that it will
     * redraw the Sprites image in the next composit cycle. The Invalidator
     * object is used to perform this function by the SWCompositor if the object
     * itself cannot perform the invalidation.
     * Can return null if the DynamicImage does not require the SWCompositor
     * to invalidate it (or the object is in a state where its image won't change).
     *
     * The Invalidators provided with objects that implement the DynamicImage
     * interface will execute at the scale and period of the Compositor
     * when they are created.
     * @param c the Compositor in which the DynamicImage is a member
     * @param s the TwoDSprite that is presenting the DynamicImage's image in
     *  the Compositor. This is the sprite that must be invalidated.
     * @return an Invalidator or null if invalidation is not required
     * @return {@link quicktime.QTException}
     * @see quicktime.app.display.SWCompositor
     */
    public Invalidator addedToCompositor(SWCompositor paramSWCompositor, TwoDSprite paramTwoDSprite) throws QTException {
        if (this.spritePresenter != null)
            removedFromCompositor(null);
        setGWorld(QDGraphics.validScratch);
        this.taskFlag = 1;
        this.spritePresenter = paramTwoDSprite;
        this.spritePresenter.spriteIsCompositor = this;
        this.parent = paramSWCompositor;
        return null;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the graphics mode of the SpriteDisplay object - this will be restored
     * on each subsequent creation of the internal SpriteWorld. This may be null
     * if not specified
     * @return the current graphics mode or null
     */
    public GraphicsMode getGraphicsMode() {
        return this.gMode;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Sets the current graphics mode.
     * @param mode the new graphics mode
     * @param {@link quicktime.QTException}
     */
    public void setGraphicsMode(GraphicsMode paramGraphicsMode) throws QTException {
        if (this.mSpriteWorld != null)
            this.mSpriteWorld.setGraphicsMode(paramGraphicsMode);
        this.gMode = paramGraphicsMode;
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the TwoDSprite for the member as a Compositable object to enable
     * an application to get and set the graphics mode presentation of the member object.
     * @param member the member object
     * @return the member's TwoDSprite presenter
     */
    public Compositable getCompositable(Object paramObject) {
        return getMemberSprite(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the TwoDSprite for the member as a Layerable object to enable
     * an application to get and set the layer presentation of the member object.
     * @param member the member object
     * @return the member's TwoDSprite presenter
     */
    public Layerable getLayerable(Object paramObject) {
        return getMemberSprite(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This will return the TwoDSprite for the member as a Transformable object to enable
     * an application to get and set the display presentation (its location, scale, etc) of the member object.
     * @param member the member object
     * @return the member's TwoDSprite presenter
     */
    public Transformable getTransformable(Object paramObject) {
        return getMemberSprite(paramObject);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should be called by the subclass after the member has been added to the SWCompositor.
     * If the member is itself a TwoDSprite the member and s arguments should be the TwoDSprite object.
     * If the member is an instance of the DynamicImage interface then the addedToCompositor method
     * will be called on the member and if required the member can return an Invalidator object which is then
     * added to the SWCompositor's Invalidator list. This list is the last list run before the SpriteWorld's
     * composit cycle is run. If the member is an instance of Notifier then the TwoDSprite will be set as
     * the NotifyListener of the member and the member can then notify the TwoDSprite when its image data
     * is valid and the TwoDSprite can present it.
     * @param member the new member that has been added.
     * @param s the TwoDSprite that will present this member
     * @param {@link quicktime.QTException}
     */
    protected synchronized void postProcessAdd(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        doDynamicImageAdd(paramObject, paramTwoDSprite);
        paramTwoDSprite.setParent(this);
        if ((paramObject instanceof Notifier)) {
            Notifier localNotifier = (Notifier) paramObject;
            localNotifier.addNotifyListener(paramTwoDSprite);
        }
        memberAdded(paramObject);
        if (this.mSpriteWorld != null) {
            paramTwoDSprite.createSprite(this.mSpriteWorld);
            if (this.autoRedraw) {
                idle();
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method should be called by the subclass before the member has been removed from the SWCompositor.
     * If the member is itself a TwoDSprite the member and s arguments should be the TwoDSprite object.
     * If the member is an instance of the DynamicImage interface then the removedFromCompositor method
     * will be called on the member allowing the member to cleanup its invalidation requirements.
     * If the member had required invalidation then its Invalidator object will be removed from the
     * Invalidator list.
     * @param member the new member that has been removed.
     * @param s the TwoDSprite that presented this member
     * @param {@link quicktime.QTException}
     */
    protected synchronized void postProcessRemove(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        doDynamicImageRemove(paramObject, paramTwoDSprite);
        memberRemoved(paramObject);
        paramTwoDSprite.remove();
        paramTwoDSprite.setParent(null);
        if ((this.autoRedraw) && (this.mSpriteWorld != null)) {
            idle();
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called if a SpriteWorld has previously been created and the destination
     * QDGraphics or QDGraphics is about to go away (such as removing a SpriteWorld from a QTCanvas).
     * Any Spirte objects that were attached to the sprite world will be invalid
     * after this call so you should ensure that you remove any possibility of a method being called
     * on any of these sprites. If sprites and or an action list has been added to the Compositor
     * then those sprites will be saved and reinstantiated if a consequent createSprites method is received.
     */
    protected void saveMembers() throws QTException {
        boolean bool = this.autoRedraw;
        try {
            this.autoRedraw = false;
            for (Enumeration localEnumeration = sprites(); localEnumeration.hasMoreElements(); ) {
                TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                doDynamicImageRemove(getSpriteMember(localTwoDSprite), localTwoDSprite);
                localTwoDSprite.removeNotify();
            }
        } finally {
            Enumeration localEnumeration;
            this.autoRedraw = bool;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method is called after the Compositor has been added to a valid QDGraphics and the
     * sprites can be added to the spriteworld. Any sprites that the Compositor knows about
     * through an action list or sprites being explicitly added, will be recreated at this
     * point. If a subclass overrides this method to create sprites on an initial call and add
     * them to the Compositor then on subsequent calls the subclass should call super.createSprites
     * to ensure that the previous sprites are recreated.
     */
    protected void createMembers() throws QTException {
        boolean bool = this.autoRedraw;
        try {
            this.autoRedraw = false;
            for (Enumeration localEnumeration = sprites(); localEnumeration.hasMoreElements(); ) {
                TwoDSprite localTwoDSprite = (TwoDSprite) localEnumeration.nextElement();
                localTwoDSprite.createSprite(this.mSpriteWorld);
                doDynamicImageAdd(getSpriteMember(localTwoDSprite), localTwoDSprite);
            }
        } finally {
            Enumeration localEnumeration;
            this.autoRedraw = bool;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Return a string representation of this object.
     * @return a String
     */
    public String toString() {
        try {
            return getClass().getName() + "[sw=" + this.mSpriteWorld + ",timer=" + getTimer() + ",grafPort=" + getGWorld() + ",bounds=" + getDisplayBounds() + "]";
        } catch (QTException localQTException) {
        }
        return getClass().getName();
    }

    private void doDynamicImageAdd(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        if ((this.mSpriteWorld != null) && ((paramObject instanceof DynamicImage))) {
            Invalidator localInvalidator = ((DynamicImage) paramObject).addedToCompositor(this, paramTwoDSprite);
            if (localInvalidator != null) {
                if (this.invalList == null)
                    this.invalList = new TickleList();
                this.invalList.addMember(localInvalidator);
            }
        }
    }

    private void doDynamicImageRemove(Object paramObject, TwoDSprite paramTwoDSprite) throws QTException {
        if ((this.mSpriteWorld != null) && ((paramObject instanceof DynamicImage))) {
            ((DynamicImage) paramObject).removedFromCompositor(this);
            if (this.invalList != null) {
                Enumeration localEnumeration = this.invalList.members();
                while (localEnumeration.hasMoreElements()) {
                    Invalidator localInvalidator = (Invalidator) localEnumeration.nextElement();
                    if (localInvalidator.getTarget().equals(paramTwoDSprite)) {
                        this.invalList.removeMember(localInvalidator);
                    }
                }
                if (this.invalList.isEmpty()) {
                    this.invalList = null;
                }
            }
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Registers an object as a listener of the Compositor. The object will receive notification when the Compositor draws a frame
     */
    public synchronized void addDrawingListener(DrawingListener paramDrawingListener) {
        if (this.listenerList == null) {
            this.listenerList = new Vector();
        }
        if (this.listenerList.contains(paramDrawingListener)) {
            return;
        }
        this.listenerList.addElement(paramDrawingListener);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Removes an object that is a listener of the Compositor. The object will no longer receive notification when the Compositor draws a frame
     */
    public synchronized void removeDrawingListener(DrawingListener paramDrawingListener) {
        this.listenerList.removeElement(paramDrawingListener);
        if (this.listenerList.isEmpty()) {
            this.listenerList = null;
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the TwoDSprite presenter that is presenting the image data
     * of the given member. It returns null if the object is not a member.
     * @param member the member object
     * @return the member's TwoDSprite presenter
     */
    public abstract TwoDSprite getMemberSprite(Object paramObject);

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This returns an Enumeration that allows your application to iterate over the TwoDSprite
     * presenters that are responsible for presenting the image data within the Compositor.
     * @return an Enumeration;
     */
    public abstract Enumeration sprites();

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * This method returns the member that is being presented by the supplied TwoDSprite presenter.
     * It returns null if the TwoDSprite is not present in the Compositor.
     * @param sprite the TwoDSprite presenter
     * @return the member that is being presented by the supplied TwoDSprite presenter
     */
    public abstract Object getSpriteMember(TwoDSprite paramTwoDSprite);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/anim/
 * SWCompositor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
