package quicktime.app.display;

import java.awt.AWTEventMulticaster;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.QTUnknownOSException;
import quicktime.app.image.ImageDrawer;
import quicktime.app.players.QTPlayer;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.qd.NativeGraphics;
import quicktime.qd.NativeGraphicsException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.util.QTUtils;
import quicktime.util.UtilException;

/**
 * @deprecated
 * A specialized canvas which supplies access to the native graphics environment
 * and offers expanded functionality to control its viewing or display size.
 * The QTCanvas object can display any object which implements the Drawable interface.
 * This Drawable object is set as the drawing client of the QTCanvas object.
 *
 * The resize and alignment flags determine how the QTCanvas object acts when it
 * has its setSize or setBounds method called. These flags allow the application to
 * control or restrict the viewing size of a QTCanvas - generally desirable as the
 * QuickTime client of a QTCanvas is constructed to be viewed in a particular
 * best size or react in a desired manner when resized. The resize flags comments
 * discuss the typically appropriate usage. If the kAnySize flag is used the QTCanvas
 * will be resized as any other java.awt.Component.
 *
 * The size of the QTCanvas and its Drawable client are kept synchronized. The
 * actual size of the QTCanvas is determined by the size of its Drawable
 * client when it is set (which becomes its "best" viewing size),
 * the resize and alignement flag settings, and the size of the QTCanvas'
 * java.awt.Container parent. The client, the resize and alignment flags determine
 * how a QTCanvas reacts to a setBounds or setSize call - any resize flag
 * other than the kAnySize flag will result in the QTCanvas' actual size
 * being arbitrated with the best viewing size of its client. Thus in many cases the calls
 * to setSize and setBounds on a QTCanvas object are requests and may not result in the
 * QTCanvas actually being resized to that requested size.
 *
 * When a QTCanvas' client is set the size of the client's current size is used by the QTCanvas to
 * indicate that this is that client's best viewing size. By default (and an application can change this)
 * this will also become the preferred size of the QTCanvas. This size is cached
 * by the QTCanvas and used when it is responding to resize requests from its parent
 * Container (for instance, the user resizes the window the QTCanvas is in).
 *
 * If an application changes the size of the client after it has been set - for instance
 * a Movie client is edited and the viewing area of the movie is now different - then
 * the application must inform the QTCanvas that the client's best size is changed, before resizing the
 * QTCanvas itself. This ensures that the QTCanvas will resize and redraw the client correctly, avoiding
 * unwanted scaling of the client:
 *
 * // application changes the actual display size of a Movie through
 * // adding a video track to a movie that only had a sound track
 * // this means that the movie client has now a best size that
 * // that is different when it was originally set as the client of this QTCanvas
 * myQTCanvas.clientChanged (r.getWidth(), r.getHeight());
 * myQTCanvas.invalidate();
 *
 * //resize the QTCanvas' parent container so that the changed viewing conditions of the
 * //client will be seen.
 * //pack() is appropriate, as is getParent().setSize(), or getParent().setBounds()
 * //The application could also just validate the parent if the parent should not change size
 *
 * Once the application has notified the canvas that the client's best viewing size has changed
 * the QTCanvas will correctly respond to the any new sizes that are given to it by it parent's
 * layout manager or resizing actions.
 *
 * In most interactions between a QTCanvas and its client this explicit resizing of the client is
 * not required as all of the Drawable clients are able to scale themselves appropriately to
 * a different display size. It is only required when the client's size itself has changed.
 */
public class QTCanvas extends Canvas {

    private static boolean MRJWorkaroundFlag;

    private static final boolean debug = false;

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
        if (QTSession.isCurrentOS(2)) {
            MRJWorkaroundFlag = false;
        } else if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
            String str = System.getProperty("java.version");
            int i = Character.getNumericValue(str.charAt(0));
            int j = 0;
            int k = 0;
            int m = 0;
            for (int n = 1; n < str.length(); n++) {
                char c = str.charAt(n);
                if (Character.isDigit(c)) {
                    if (m == 0) {
                        j = Character.getNumericValue(c);
                        m = 1;
                    } else {
                        k = Character.getNumericValue(c);
                    }
                }
            }
            if ((i == 1) && (j == 1) && (k < 8)) {
                MRJWorkaroundFlag = true;
            } else {
                MRJWorkaroundFlag = false;
            }
        }
    }

    private static int canvasCount = 0;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client should maintain the aspect size of its original dimensions
     * but grow bigger by integral increments (2x, 3x, 4x, etc.)
     */
    public static final int kIntegralResize = 1;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client should revert to its initially specified size. The client
     * can be made smaller (using an aspect setting) but can be no larger than its initial size. The QTCanvas
     * takes this initial size as the best viewing size for its client.
     */
    public static final int kInitialSize = 2;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client can be resized to any size >= the minimum and <=
     * the maximum.
     */
    public static final int kFreeResize = 3;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client can be resized to any size which preserves the
     * initial aspect ratio.
     */
    public static final int kAspectResize = 4;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag if the client can be resized to any size which is a factor of
     * 2 of the initial size.
     */
    public static final int kPerformanceResize = 5;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag to allow the client's horizontal aspect to be any size, but the vertical
     * axis will be the intial size of smaller. This is a good setting for QT VR content.
     */
    public static final int kHorizontalResize = 6;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag to allow the client's vertical aspect to be any size, but the horizontal
     * axis will be the intial size of smaller.
     */
    public static final int kVerticalResize = 7;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Use this flag specify that the client cannot be resized
     */
    public static final int kInitialSizeNoResize = 8;

    transient int numCanvas;

    private transient Vector<?> mControllers;

    private final QTCanvasHelper qtcHelper;

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructor for creating a client-less QTCanvas. Default settings are:
     */
    public QTCanvas() {
        this(3, 0.0F, 0.0F);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Constructor for creating a client-less QTCanvas.
     * @param resizeFlag the resize control setting
     * @param xAlignment a value between 0 and 1 that sets the position of the drawable client within the
     *  space that AWT will give to the canvas when layed out. 0 means left, 1 means right, 0.5 means centre.
     * @param yAlignment a value between 0 and 1 that sets the position of the drawable client within the
     *  space that AWT will give to the canvas when layed out. 0 means top, 1 means bottom, 0.5 means centre.
     */
    public QTCanvas(int paramInt, float paramFloat1, float paramFloat2) {
        this.resizeFlag = paramInt;
        this.initialWidth = 1;
        this.initialHeight = 1;
        setAlignmentValues(paramFloat1, paramFloat2);
        this.minWidth = 1;
        this.minHeight = 1;
        this.maxWidth = Integer.MAX_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        this.numCanvas = (canvasCount++);
        this.qtcHelper = new QTCanvasHelper(this);
    }

    private boolean doRedrawForQTPlayer = false;

    private float xAlignment;

    private float yAlignment;

    private int resizeFlag;

    transient int initialWidth;

    transient int initialHeight;

    int currentX;

    int currentY;

    private int minWidth;

    private int minHeight;

    private int maxWidth;

    private int maxHeight;

    private transient int prefWidth;

    private transient int prefHeight;

    int currentWidth;

    int currentHeight;

    transient NativeGraphics ng = null;

    private final transient long msecs = 0L;

    transient boolean deferResize = false;

    private transient boolean appHasSetPreferredSize = false;

    boolean isWin = QTSession.isCurrentOS(2);

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method will return a QTJava object, QDGraphics, which is a reference to
     * the native graphics drawing environment being used by this QTCanvas object.
     * If the canvas is invisible it will return an offscreen QDGraphics that will never
     * be seen.
     * @return a QDGraphics object
     * @throws quicktime.QTUnknownOSException if the current operating system is unknown to QuickTime
     * @throws quicktime.NativeGraphicsException if there was a problem initialising the NativeGraphics environment
     * @throws quicktime.qd.NativeGraphicsException
     */
    public final QDGraphics getPort() throws QTUnknownOSException, NativeGraphicsException {
        QDGraphics localQDGraphics = null;
        if (isVisible()) {
            localQDGraphics = this.ng != null ? this.ng.getGWorld() : null;
        }
        return localQDGraphics == null ? QDGraphics.scratch : localQDGraphics;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the coordinates of a component relative to the NativeGrapics
     * window it is currently placed within.
     * @return a Point object representing the location of the QTCanvas in the native graphics environment
     * @throws quicktime.QTUnknownOSException
     * @throws quicktime.qd.NativeGraphicsException
     */
    public final Point getNGLocation() throws QTUnknownOSException, NativeGraphicsException {
        if ((this.qtcHelper.mClient instanceof QTDrawable)) {
            return this.ng != null ? this.ng.getLocation() : new Point(0, 0);
        }
        return getLocation();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the NativeGrapics object that represents the Graphics Port that corresponds
     * to the display space taken up by the QTCanvas. It may return null if there is currently
     * no native graphics environment
     * @return a NativeGraphics object.
     */
    public final NativeGraphics getNativeGraphics() {
        return this.ng;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * An incoming point (typically from some kind of MouseEvent) is in coordinates that have their origin
     * set at the local coordinate system of the canvas. This method converts those coordinates into
     * the native graphics location that is the coordinate system of the canvas' client.
     * @param x the x location
     * @param y the y location
     * @return a QDPoint that translates the x and y into the NativeGraphics coordinate system.
     * @throws quicktime.QTUnknownOSException
     * @throws quicktime.qd.NativeGraphicsException
     */
    public final QDPoint translatePoint(int paramInt1, int paramInt2) throws QTUnknownOSException, NativeGraphicsException {
        Point localPoint = getNGLocation();
        return new QDPoint(paramInt1 + localPoint.x, paramInt2 + localPoint.y);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Removes the client from the canvas.
     */
    public final synchronized void removeClient() {
        this.qtcHelper.doRemoveClient();
        repaint();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Associates a new client, a Drawable object, with this QTCanvas.
     * The flag determines if awt will perform a layout and how the client will be integrated
     * with the canvas. If the flag is false then the new client will take on the current size and
     * position of the canvas. If the flag is true, then awt will layout the canvas again, using
     * the initial size of the client and the resize flags to resize the canvas and its client.
     *
     * The size of the client when this method is called is used to ascertain the best size
     * for viewing this client's media.
     *
     * This method can throw a number of exceptions:
     * QTUnknownOSException the application is trying to run on a platform that is not supported by QuickTime
     * NativeGraphicsException the native graphics environment is not initialised
     * QTException or subclass - there was a problem within QuickTime itself in dealing with this client
     * @param client a Drawable object - if null the method will remove the client
     * @param layoutFlag if true java.awt will re-layout the canvas, if false the client will fit
     *  into the existing state (display size, etc) of the canvas, disregarding any resize that might
     *  have occured to honour the layout flag setting with respect to the different size of the new client.
     * @throws quicktime.QTException
     */
    public synchronized void setClient(Drawable paramDrawable, boolean paramBoolean) throws QTException {
        this.qtcHelper.doSetClient(paramDrawable, paramDrawable != null ? paramDrawable.getDisplayBounds() : null, getMinimumSize(), getMaximumSize(), paramBoolean);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Associates a new client, a Drawable object, with this QTCanvas. It will use existing
     * alignment and resize restrictions.
     *
     * This method can throw a number of exceptions:
     * QTUnknownOSException the application is trying to run on a platform that is not supported by QuickTime
     * NativeGraphicsException the native graphics environment is not initialised
     * QTException or subclass - there was a problem within QuickTime itself in dealing with this client
     * @param client a Drawable object - if null the method will remove the client
     * @param initialBounds the best size of the client
     * @throws quicktime.QTException
     */
    public synchronized void setClient(Drawable paramDrawable, QDRect paramQDRect) throws QTException {
        this.qtcHelper.doSetClient(paramDrawable, paramQDRect, getMinimumSize(), getMaximumSize(), true);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the Drawable object currently associated with this QTCanvas.
     * @return a Drawable object
     */
    public final Drawable getClient() {
        return this.qtcHelper.mClient;
    }

    boolean deferRedraw = false;

    private boolean moved;

    void setInitialWidthAndHeight(QDRect paramQDRect, Dimension paramDimension1, Dimension paramDimension2) {
        this.initialWidth = paramQDRect.getWidth();
        this.initialHeight = paramQDRect.getHeight();
        this.minWidth = Math.max(paramDimension1.width, 0);
        this.minHeight = Math.max(paramDimension1.height, 0);
        this.maxWidth = Math.max(paramDimension2.width, this.minWidth);
        this.maxHeight = Math.max(paramDimension2.height, this.minHeight);
        if (this.initialWidth < this.minWidth) {
            this.initialWidth = this.minWidth;
        } else if (this.initialWidth > this.maxWidth) {
            this.initialWidth = this.maxWidth;
        }
        if (this.initialHeight < this.minHeight) {
            this.initialHeight = this.minHeight;
        } else if (this.initialHeight > this.maxHeight) {
            this.initialHeight = this.maxHeight;
        }
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }
    }

    final void doParentLayout() {
        Container localContainer = getParent();
        if (localContainer != null) {
            invalidate();
            localContainer.validate();
            return;
        }
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the alignment position values. The alignment values must be between 0.0
     * and 1.0. Values out of this range will be reset to either 0.0 or 1.0, whichever
     * is closest to the given argument.
     *
     * Alignment Positioning works like this:
     *
     * @x=0.0 y=0.0 is equivalent to a top-left position
     * @x=1.0 y=1.0 is equivalent to a bottom-right position
     * @x=0.5 y=0.5 is equivalent to a centered position
     * @x=0.25 y=0.75 is equivalent to 1/4 in from the the left and 3/4 down from the top
     * Position alignments can be varied to any degree, limited by the pixel resolution
     * of the display surface.
     * @param xAlignment a float between 0.0 and 1.0 representing the x position alignment
     * @param yAlignment a float between 0.0 and 1.0 representing the y position alignment
     */
    public void setAlignment(float paramFloat1, float paramFloat2) {
        Rectangle localRectangle = getBounds();
        setAlignmentValues(paramFloat1, paramFloat2);
        doParentLayout();
        this.doRedrawForQTPlayer = this.qtcHelper.shouldRedrawQTPlayer(localRectangle);
    }

    /**
     * @deprecated
     * @see * @deprecated since QTJava 6.1
     */
    public final void setAlignmentX(float paramFloat) {
        setAlignment(paramFloat, getAlignmentY());
    }

    /**
     * @deprecated
     * @see * @deprecated since QTJava 6.1
     */
    public final void setAlignmentY(float paramFloat) {
        setAlignment(getAlignmentX(), paramFloat);
    }

    private void setAlignmentValues(float paramFloat1, float paramFloat2) {
        if (paramFloat1 > 1.0F) {
            this.xAlignment = 1.0F;
        } else this.xAlignment = Math.max(paramFloat1, 0.0F);
        if (paramFloat2 > 1.0F) {
            this.yAlignment = 1.0F;
        } else this.yAlignment = Math.max(paramFloat2, 0.0F);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current setting of the x position alignment being used.
     */
    @Override
    public float getAlignmentX() {
        return this.xAlignment;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current setting of the y position alignment being used.
     */
    @Override
    public float getAlignmentY() {
        return this.yAlignment;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Reset the resize flag and re-layout the component.
     * @param rFlag the new value for the resizeFlag
     */
    public void setResizeFlag(int paramInt) {
        this.resizeFlag = paramInt;
        Rectangle localRectangle = getBounds();
        doParentLayout();
        this.doRedrawForQTPlayer = this.qtcHelper.shouldRedrawQTPlayer(localRectangle);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the current setting of the resize flag being used.
     * @return the resize flag value
     */
    public int getResizeFlag() {
        return this.resizeFlag;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the minimum size of the QTCanvas. If the QTCanvas is currently smaller
     * than the minimum size passed in, it will be adjusted to meet the new minimum
     * size requirements, in accordance with the setting of the resizeFlag.
     * @param minSize a Dimension object
     */
    @Override
    public void setMinimumSize(Dimension paramDimension) {
        if (paramDimension == null) {
            setMinimumSize(0, 0);
        } else {
            setMinimumSize(paramDimension.width, paramDimension.height);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the minimum size of the canvas.
     * @param width the minimum width
     * @param height the minimum height
     */
    public void setMinimumSize(int paramInt1, int paramInt2) {
        this.minWidth = Math.max(paramInt1, 0);
        this.minHeight = Math.max(paramInt2, 0);
        if ((this.currentWidth < this.minWidth) || (this.currentHeight < this.minHeight)) {
            doParentLayout();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the minimum acceptable size of this QTCanvas object.
     * @return a Dimension object which defines the minimum size
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(this.minWidth, this.minHeight);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the best size of the client, the size of the client when it was set to this QTCanvas, or
     * the size if the appliction notified the QTCanvas that the clien't best size was changed.
     * This may not be the size of the display space of the
     * QTCanvas depending on java layout instructions and current display space available to the canvas.
     * @return a Dimension object which details the client's known initial size
     * @see clientChanged
     */
    public Dimension getBestSize() {
        return new Dimension(this.initialWidth, this.initialHeight);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is used by an appliction to notify a QTCanvas that the best size of its' current
     * client has changed. This is typically the initial or actual size of the client before
     * any matrix transforamations have been applied to the client.
     *
     * An application only needs to call this if the display characteristics of the client have
     * been changed. For instance, the client is a movie and some of the visual tracks of this movie
     * have been disabled, or a new video track was added.
     * Thus the movie itself has changed its best size and the application must
     * use this method to notify the QTCanvas that the client's best size has changed.
     *
     * This will not effect the display area of the QTCanvas, the application should invalidate the
     * QTCanvas and re-layout the parent container to have this change take effect. getBestSize() returns
     * this newly notified size.
     *
     * In the normal operation of a QTCanvas and its client there is no need for an application to use this method.
     * @param bestSize the client's new best width and height
     */
    public void clientChanged(Dimension paramDimension) {
        clientChanged(paramDimension.width, paramDimension.height);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * This method is used by an appliction to notify a QTCanvas that the best size of its' current
     * client has changed. This is typically the initial or actual size of the client before
     * any matrix transforamations have been applied to the client.
     *
     * An application only needs to call this if the display characteristics of the client have
     * been changed. For instance, the client is a movie and some of the visual tracks of this movie
     * have been disabled, or a new video track was added.
     * Thus the movie itself has changed its best size and the application must
     * use this method to notify the QTCanvas that the client's best size has changed.
     *
     * This will not effect the display area of the QTCanvas, the application should invalidate the
     * QTCanvas and re-layout the parent container to have this change take effect.  getBestSize() returns
     * this newly notified size.
     *
     * In the normal operation of a QTCanvas and its client there is no need for an application to use this method.
     * @param bestWidth the client's new best width
     * @param bestHeight the client's new actual height
     */
    public void clientChanged(int paramInt1, int paramInt2) {
        Rectangle localRectangle = new Rectangle(getLocation(), new Dimension(this.initialWidth, this.initialHeight));
        this.initialWidth = Math.max(paramInt1, 0);
        this.initialHeight = Math.max(paramInt2, 0);
        if (!this.appHasSetPreferredSize) {
            this.prefWidth = this.initialWidth;
            this.prefHeight = this.initialHeight;
        }
        this.doRedrawForQTPlayer = this.qtcHelper.shouldRedrawQTPlayer(localRectangle);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the preferred size of the QTCanvas object. If you haven't explicitly set the preferred
     * size of the canvas this will be the initial size of the canvas' client.
     * @return a Dimension object which defines the preferred size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.prefWidth, this.prefHeight);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the preferred size of the QTCanvas object.
     * @return a Dimension object which defines the preferred size
     */
    @Override
    public void setPreferredSize(Dimension paramDimension) {
        if (paramDimension == null) {
            setPreferredSize(this.initialWidth, this.initialHeight);
        } else {
            setPreferredSize(paramDimension.width, paramDimension.height);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the preferred size of the QTCanvas object.
     * Passing in values of less than zero for width and height will reset the behaviour of the QTCanvas
     * to retrieve the preferred size as the initial size of the current or new client
     * of the QTCanvas.
     * @param width the minimum width
     * @param height the minimum height
     */
    public void setPreferredSize(int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            this.prefWidth = this.initialWidth;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefWidth = paramInt1;
            this.appHasSetPreferredSize = true;
        }
        if (paramInt2 < 0) {
            this.prefHeight = this.initialHeight;
            this.appHasSetPreferredSize = false;
        } else {
            this.prefHeight = paramInt2;
            this.appHasSetPreferredSize = true;
        }
        if ((this.currentWidth != this.prefWidth) || (this.currentHeight != this.prefHeight)) {
            doParentLayout();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * We override this method so that QTCanvas can gain the focus immediately
     * @return true, this component is focus-traversable
     */
    @Override
    public boolean isFocusTraversable() {
        return true;
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the minimum size of the QTCanvas. If the QTCanvas is currently larger
     * than the maximum size passed in, it will be adjusted to meet the new maximum
     * size requirements, in accordance with the setting of the resizeFlag.
     * If null is passed in, maximum size will be the size of the screen
     * @param maxSize a Dimension object
     */
    @Override
    public void setMaximumSize(Dimension paramDimension) {
        if (paramDimension == null) {
            Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
            setMaximumSize(localDimension.width, localDimension.height);
        } else {
            setMaximumSize(paramDimension.width, paramDimension.height);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Set the maximum size of the canvas.
     * @param width the minimum width
     * @param height the minimum height
     */
    public void setMaximumSize(int paramInt1, int paramInt2) {
        this.maxWidth = Math.max(paramInt1, 0);
        this.maxHeight = Math.max(paramInt2, 0);
        if ((this.currentWidth > this.maxWidth) || (this.currentHeight > this.maxHeight)) {
            doParentLayout();
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Returns the maximum acceptable size of this QTCanvas object.
     * @return a Dimension object which defines the maximum size
     */
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(this.maxWidth, this.maxHeight);
    }

    private Dimension getAspectSize(int paramInt1, int paramInt2) {
        if (paramInt1 < this.minWidth) {
            paramInt1 = this.minWidth;
        } else if (paramInt1 > this.maxWidth)
            paramInt1 = this.maxWidth;
        if (paramInt2 < this.minHeight) {
            paramInt2 = this.minHeight;
        } else if (paramInt2 > this.maxHeight) {
            paramInt2 = this.maxHeight;
        }
        float f1 = paramInt1 / (Math.max(this.initialWidth, 1));
        float f2 = paramInt2 / (Math.max(this.initialHeight, 1));
        int i;
        int j;
        if (f1 < f2) {
            i = (int) (this.initialWidth * f1);
            j = (int) (this.initialHeight * f1);
        } else {
            i = (int) (this.initialWidth * f2);
            j = (int) (this.initialHeight * f2);
        }
        return new Dimension(i, j);
    }

    private void setCurrentWidthHeight(int width, int height) {
        switch(getResizeFlag()) {
            case 2:
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;
                if (this.currentWidth < this.minWidth) {
                    this.currentWidth = this.minWidth;
                } else if (this.currentWidth > this.maxWidth)
                    this.currentWidth = this.maxWidth;
                if (this.currentHeight < this.minHeight) {
                    this.currentHeight = this.minHeight;
                } else if (this.currentHeight > this.maxHeight) {
                    this.currentHeight = this.maxHeight;
                }
                if ((this.currentWidth > width) || (this.currentHeight > height)) {
                    Dimension localDimension = getAspectSize(width, height);
                    this.currentWidth = localDimension.width;
                    this.currentHeight = localDimension.height;
                }
                break;
            case 4:
                Dimension localDimension = getAspectSize(width, height);
                this.currentWidth = localDimension.width;
                this.currentHeight = localDimension.height;
                break;
            case 6:
                this.currentWidth = width;
                this.currentHeight = this.initialHeight;
                if (this.currentWidth < this.minWidth) {
                    this.currentWidth = this.minWidth;
                } else if (this.currentWidth > this.maxWidth)
                    this.currentWidth = this.maxWidth;
                if (this.currentHeight < this.minHeight) {
                    this.currentHeight = this.minHeight;
                } else if (this.currentHeight > this.maxHeight) {
                    this.currentHeight = this.maxHeight;
                }
                if (this.currentHeight > height) {
                    Dimension localDimension2 = getAspectSize(width, height);
                    this.currentHeight = localDimension2.height;
                }
                break;
            case 7:
                this.currentWidth = this.initialWidth;
                this.currentHeight = height;
                if (this.currentWidth < this.minWidth) {
                    this.currentWidth = this.minWidth;
                } else if (this.currentWidth > this.maxWidth)
                    this.currentWidth = this.maxWidth;
                if (this.currentHeight < this.minHeight) {
                    this.currentHeight = this.minHeight;
                } else if (this.currentHeight > this.maxHeight) {
                    this.currentHeight = this.maxHeight;
                }
                if (this.currentWidth > width) {
                    Dimension localDimension2 = getAspectSize(width, height);
                    this.currentWidth = localDimension2.width;
                }
                break;
            case 5:
                if (width < this.minWidth) {
                    width = this.minWidth;
                } else if (width > this.maxWidth)
                    width = this.maxWidth;
                if (height < this.minHeight) {
                    height = this.minHeight;
                } else if (height > this.maxHeight) {
                    height = this.maxHeight;
                }
                boolean flag = false;
                int scale = 2;
                while (!flag) {
                    boolean flag2 = false;
                    if ((width < this.initialWidth) || (height < this.initialHeight)) {
                        int w = this.initialWidth / scale;
                        int h = this.initialHeight / scale;
                        if ((w <= width) && (h <= height)) {
                            flag = true;
                            flag2 = false;
                            this.currentWidth = w;
                            this.currentHeight = h;
                            while (flag2 != false) {
                                if ((this.currentWidth >= this.minWidth) && (this.currentHeight >= this.minHeight)) {
                                    flag2 = true;
                                }
                            }
                        }
                    } else {
                        this.currentWidth *= 2;
                        this.currentHeight *= 2;
                        int w = this.initialWidth * scale;
                        int h = this.initialHeight * scale;
                        if ((w >= width) || (h >= height)) {
                            flag = true;
                            if ((w > width) || (h > height)) {
                                w /= 2;
                                h /= 2;
                            }
                            flag2 = false;
                            this.currentWidth = w;
                            this.currentHeight = h;
                            while (flag2 != false) {
                                if ((this.currentWidth <= this.maxWidth) && (this.currentHeight <= this.maxHeight)) {
                                    flag2 = true;
                                }
                                this.currentWidth /= 2;
                                this.currentHeight /= 2;
                            }
                        }
                    }
                    scale *= 2;
                }
                break;
            case 1:
                if (width < this.minWidth) {
                    width = this.minWidth;
                } else if (width > this.maxWidth)
                    width = this.maxWidth;
                if (height < this.minHeight) {
                    height = this.minHeight;
                } else if (height > this.maxHeight) {
                    height = this.maxHeight;
                }
                int w = this.initialWidth == 0 ? 0 : width / this.initialWidth;
                int h = this.initialHeight == 0 ? 0 : height / this.initialHeight;
                int l = Math.min(w, h);
                if (l < 1) {
                    float w2 = this.initialWidth == 0 ? 0.0F : width / this.initialWidth;
                    float h2 = this.initialHeight == 0 ? 0.0F : height / this.initialHeight;
                    l = 0;
                    if (w2 <= h2) {
                        l = this.initialWidth / width + 1;
                    } else
                        l = this.initialHeight / height + 1;
                    this.currentWidth = ((int) (this.initialWidth * 1.0F / l));
                    this.currentHeight = ((int) (this.initialHeight * 1.0F / l));
                } else {
                    this.currentWidth = (this.initialWidth * l);
                    this.currentHeight = (this.initialHeight * l);
                }
                break;
            case 8:
                this.currentWidth = this.initialWidth;
                this.currentHeight = this.initialHeight;
                if (this.currentWidth < this.minWidth) {
                    this.currentWidth = this.minWidth;
                } else if (this.currentWidth > this.maxWidth)
                    this.currentWidth = this.maxWidth;
                if (this.currentHeight < this.minHeight) {
                    this.currentHeight = this.minHeight;
                } else if (this.currentHeight > this.maxHeight) {
                    this.currentHeight = this.maxHeight;
                }
                break;
            case 3:
            default:
                if (width < this.minWidth) {
                    width = this.minWidth;
                } else if (width > this.maxWidth)
                    width = this.maxWidth;
                if (height < this.minHeight) {
                    height = this.minHeight;
                } else if (height > this.maxHeight) {
                    height = this.maxHeight;
                }
                this.currentWidth = width;
                this.currentHeight = height;
                break;
        }
    }

    private void setCurrentXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = paramInt3 - this.currentWidth;
        if (i < 0) {
            i = 0;
        }
        int j = paramInt4 - this.currentHeight;
        if (j < 0) {
            j = 0;
        }
        this.currentX = (paramInt1 + (int) (this.xAlignment * i));
        this.currentY = (paramInt2 + (int) (this.yAlignment * j));
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Resizes the QTCanvas object to the specified parameters.
     * @param dim a Dimension object with the new width and height
     */
    @Override
    @Deprecated
    public void setSize(Dimension paramDimension) {
        setSize(paramDimension.width, paramDimension.height);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Resizes the QTCanvas object, as well as the associated Drawable object.
     * @param width the width, in pixels, to make this component
     * @param height the height, in pixels, to make this component
     */
    @Override
    public void setSize(int paramInt1, int paramInt2) {
        setBounds(this.currentX, this.currentY, paramInt1, paramInt2);
    }

    /**
     * @deprecated
     * <I>As of JDK version 1.1,
     * replaced by setBounds(int, int, int, int).</I>
     */
    @Override
    public final void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Resizes and/or moves the QTCanvas object as well as the associated Drawable
     * object to the parameters specified.
     * @param x the x coordinate, in pixels, relative to the parent of this
     *                component
     * @param y the y coordinate, in pixels, relative to the parent of this
     *                component
     * @param width the width, in pixels, to make this component
     * @param height the height, in pixels, to make this component
     */
    @Override
    public final synchronized void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        boolean bool = isValid();
        int i = this.currentWidth;
        int j = this.currentHeight;
        setCurrentWidthHeight(paramInt3, paramInt4);
        int k = (i != this.currentWidth) || (j != this.currentHeight) ? 1 : 0;
        int m = this.currentX;
        int n = this.currentY;
        setCurrentXY(paramInt1, paramInt2, paramInt3, paramInt4);
        super.reshape(this.currentX, this.currentY, this.currentWidth, this.currentHeight);
        if (this.qtcHelper.mClient != null) {
            this.deferResize = ((this.deferResize) || (k != 0));
            if (this.deferResize) {
                this.doRedrawForQTPlayer = ((!this.isWin) && (!bool) && (((this.qtcHelper.mClient instanceof QTPlayer)) || ((this.qtcHelper.mClient instanceof GroupDrawable)) || ((this.qtcHelper.mClient instanceof ImageDrawer))) && (isShowing()));
            }
            this.moved = ((m != this.currentX) || (n != this.currentY) || (this.moved));
        }
    }

    private int countRegionErr = 0;

    ComponentListener clientListener;

    /**
     * @deprecated
     * @since QTJava 6.1
     * We override the update method to call our paint method as the background colour repaint is
     * not valid for a QTCanvas object. However, subclasses can override the update method to
     * do whatever is appropriate for their particular situation.
     */
    @Override
    public void update(Graphics paramGraphics) {
        if (this.qtcHelper.mClient == null) {
            super.update(paramGraphics);
        } else if (QTSession.isCurrentOS(4)) {
            paint(paramGraphics);
        } else if ((!(this.qtcHelper.mClient instanceof QTDrawable)) || (this.deferRedraw)) {
            paint(paramGraphics);
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Sets the visible state of the Canvas.
     * @param b if true canvas is visible, if false then its not
     */
    @Override
    public synchronized void setVisible(boolean paramBoolean) {
        super.setVisible(paramBoolean);
        try {
            if (paramBoolean) {
                this.qtcHelper.doSetGWorld(true);
                doParentLayout();
            } else {
                this.qtcHelper.doSetGWorld(false);
                Container localContainer = getParent();
                if (localContainer != null) {
                    localContainer.repaint();
                }
            }
        } catch (QTException localQTException) {
            QTRuntimeException.handleOrThrow(new QTRuntimeException(localQTException), this.qtcHelper.mClient, "setGWorld");
        }
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * A ComponentListener and ComponentEvent are used to listen to these types
     * of events that occur to the QTCanvas' client - ie. when the QTCanvas' client
     * is resized, moved, shown or hidden. These events generally occur in conjunction
     * with the Component events they mirror - with some exceptions:
     * ComponentShown and ComponentHidden are generated when the QTCanvas is shown
     * or hidden and when the QTDrawable client is set or removed.
     * ComonentMoved and ComponentResized are generated when the QTCanvas is
     * moved or resized but the event is actually generated when the QTCanvas is repainted
     * as this is when the QTDrawable client is itself resized or moved.
     * @param e the ComponentListener
     * <HR/>
     * <A/>
     * <H3>
     * removeQTClientListener</H3>
     *
     * public void removeQTClientListener(java.awt.event.ComponentListener
     * e)
     * @deprecated
     * @since QTJava 6.1
     * Remove a previously registered ComponentListener that is listening
     * to events that occur with the QTCanvas' QTDrawable client.
     * @param e the ComponentListener
     * @see addQTClientListener
     * <HR/>
     * <A/>
     * <H3>
     * addNotify</H3>
     *
     * public void addNotify()
     * @deprecated
     * @since QTJava 6.1
     * We use this method to notify an existing client that its NativeGraphics
     * environment may have changed and it should recheck its status
     * <HR/>
     * <A/>
     * <H3>
     * removeNotify</H3>
     *
     * public void removeNotify()
     * @deprecated
     * @since QTJava 6.1
     * Overridden to notify the client associated with this QTCanvas that it no
     * longer has a grafport to draw to and should stop all graphics operations.
     * <HR/>
     * <A/>
     * <H3>
     * toString</H3>
     *
     * public java.lang.String toString()
     * @deprecated
     * @since QTJava 6.1
     * Print information about this object.
     * @return a string representing this QTCanvas object
     * <HR/>
     * <A/>
     * <H3>
     * finalize</H3>
     *
     * protected void finalize()
     * throws java.lang.Throwable
     * @deprecated
     * java.lang.Throwable
     * <HR/>
     * <A/>
     * <TABLE>
     *
     * <TR>
     * <A/>
     * <TABLE>
     *
     * <TR>    <A href="../../../overview-summary.html">Overview</A> 
     * <TD>    <A href="package-summary.html">Package</A> </TD>
     * <TD>  Class </TD>
     * <TD>    <A href="package-tree.html">Tree</A> </TD>
     * <TD>    <A href="../../../deprecated-list.html">Deprecated</A> </TD>
     * <TD>    <A href="../../../index-all.html">Index</A> </TD>
     * <TD>    <A href="../../../help-doc.html">Help</A> </TD>
     * </TR>
     * </TABLE>
     *
     * <TD>
     *
     * </TD>
     * </TR>
     *
     * <TR>
     * <TD>
     *  <A href="../../../quicktime/app/display/JQTCanvas.html">PREV CLASS</A> 
     *  NEXT CLASS</TD>
     * <TD>
     * <A href="../../../index.html" target="_top">FRAMES</A>   
     *  <A href="QTCanvas.html" target="_top">NO FRAMES</A>   
     *  
     *
     * <A href="../../../allclasses-noframe.html" target="">All Classes</A>
     *
     * </TD>
     * </TR>
     * <TR>
     * <TD>
     * SUMMARY: NESTED | <A href="#field_summary">FIELD</A> | <A href="#constructor_summary">CONSTR</A> | <A href="#method_summary">METHOD</A></TD>
     * <TD>
     * DETAIL: <A href="#field_detail">FIELD</A> | <A href="#constructor_detail">CONSTR</A> | <A href="#method_detail">METHOD</A></TD>
     * </TR>
     * </TABLE>
     * <HR/>
     */
    public void addQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.add(this.clientListener, paramComponentListener);
    }

    /**
     * @deprecated
     */
    public void removeQTClientListener(ComponentListener paramComponentListener) {
        this.clientListener = AWTEventMulticaster.remove(this.clientListener, paramComponentListener);
    }

    /**
     * @deprecated
     */
    @Override
    public synchronized void addNotify() {
        super.addNotify();
        if (!this.addDone) {
            try {
                this.ng = NativeGraphics.getContext(this);
                if (this.qtcHelper.mClient != null) {
                    this.qtcHelper.doSetGWorld(isVisible());
                    this.qtcHelper.mClient.addedTo(this);
                    if (isVisible()) {
                        if (!this.isWin) {
                            this.deferRedraw = true;
                        } else
                            repaint();
                    }
                }
                QTSession.addActiveCanvas(this);
            } catch (QTException e) {
                this.qtcHelper.doRemoveClient();
                throw new QTRuntimeException(e);
            }
            this.addDone = true;
        }
    }

    private boolean addDone = false;

    /**
     * @deprecated
     */
    @Override
    public synchronized void removeNotify() {
        synchronized (QTSession.terminationLock()) {
            if ((this.addDone) && (QTSession.isInitialized())) {
                try {
                    if (this.qtcHelper.mClient != null) {
                        this.qtcHelper.doSetGWorld(false);
                        this.qtcHelper.mClient.removedFrom(this);
                    }
                    QTSession.removeActiveCanvas(this);
                    this.addDone = false;
                } catch (QTException e) {
                    this.qtcHelper.doRemoveClient();
                    throw new QTRuntimeException(e);
                } finally {
                    if ((QTSession.isCurrentOS(2)) && (this.ng != null)) {
                        RemoveContext(this.ng);
                    }
                    this.ng = null;
                    super.removeNotify();
                }
            } else {
                super.removeNotify();
            }
        }
    }

    /**
     * @deprecated
     */
    public String toString() {
        return getClass().getName() + "_" + this.numCanvas + "_" + "[Location=" + getLocation() + ",Size=" + getSize() + "]";
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            removeNotify();
        } finally {
            this.qtcHelper.mClient = null;
        }
        super.finalize();
    }

    /**
     * @deprecated
     * @since QTJava 6.1
     * Override the standard paint method.
     */
    @Override
    @Deprecated
    public void paint(Graphics g) {
        if (qtcHelper.mClient != null) {
            if (isValid()) {
                synchronized (this) {
                    QDGraphics qdgraphics;
                    quicktime.qd.Region region;
                    qdgraphics = null;
                    region = null;
                    try {
                        if (QTSession.isCurrentOS(1) || QTSession.isCurrentOS(4)) {
                            qdgraphics = getPort();
                            region = qdgraphics.getClip();
                            qdgraphics.clipRect();
                        }
                        synchronized (qtcHelper.mClient) {
                            if (deferResize)
                                qtcHelper.doClientSetBounds();
                            if (moved) {
                                moved = false;
                                if (clientListener != null)
                                    clientListener.componentMoved(new ComponentEvent(this, 100));
                            }
                            if (deferResize) {
                                deferResize = false;
                                if (clientListener != null)
                                    clientListener.componentResized(new ComponentEvent(this, 101));
                                if (doRedrawForQTPlayer) {
                                    qtcHelper.mClient.redraw(((quicktime.qd.Region) (null)));
                                    doRedrawForQTPlayer = false;
                                }
                                if (!(!MRJWorkaroundFlag || deferRedraw || isWin))
                                    return;
                            }
                        }
                        if (qtcHelper.mClient instanceof QTDrawable) {
                            if (!((QTDrawable) qtcHelper.mClient).getGWorld().equals(QDGraphics.scratch)) {
                                QTUtils.checkFreeMemory();
                                qtcHelper.mClient.redraw(((quicktime.qd.Region) null));
                            }
                        } else if (isShowing()) {
                            QTUtils.checkFreeMemory();
                            qtcHelper.mClient.redraw(((quicktime.qd.Region) null));
                        }
                        deferRedraw = false;
                    } catch (QTException e) {
                        if (e.errorCode() == -500 || e.errorCode() == -108) {
                            if (e.errorCode() == -108)
                                try {
                                    QTUtils.checkFreeMemory();
                                } catch (UtilException f) {
                                    QTRuntimeException.handleOrThrow(new QTRuntimeException(f), qtcHelper.mClient, "redraw");
                                }
                            if (countRegionErr < 3) {
                                if (deferResize)
                                    doRedrawForQTPlayer = true;
                                countRegionErr++;
                                QTUtils.reclaimMemory();
                                repaint();
                                return;
                            } else {
                                countRegionErr = 0;
                                QTRuntimeException.handleOrThrow(new QTRuntimeException(e), qtcHelper.mClient, "setDisplayBounds");
                            }
                        } else {
                            QTRuntimeException.handleOrThrow(new QTRuntimeException(e), qtcHelper.mClient, "setDisplayBounds");
                        }
                    } finally {
                        if (qdgraphics != null && region != null)
                            qdgraphics.setClip(region);
                    }
                }
                countRegionErr = 0;
            }
            doParentLayout();
            repaint();
        }
    }

    private static void RemoveContext(Object paramObject) {
        PlatformManager.RemoveContext(paramObject);
    }
}
