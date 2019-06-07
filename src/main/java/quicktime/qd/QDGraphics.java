package quicktime.qd;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QTStreamingLib;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.image.CSequence;
import quicktime.std.image.DSequence;
import quicktime.std.image.GraphicsExporter;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.qtcomponents.ImageCompressionDialog;
import quicktime.std.sg.SequenceGrabber;
import quicktime.streaming.Presentation;
import quicktime.streaming.Stream;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;
import sun.awt.DrawingSurfaceInfo;
import sun.awt.MacDrawingSurface;

/**
 * A color graphics port defines a complete drawing environment that determines where
 * and how color graphics operations take place.
 *
 * To create an offscreen QDGraphics (in traditional QuickDraw parlance a GWorld)
 * any one of the various constructors can be used.
 * The QDGraphics from... calls will return the existing destination graphics of the
 * first supplied object. For the fromNativeGraphics this will always be an on screen
 * graphics (in traditional QuickDraw parlance a CGrafPort). For the other calls the
 * type of the QDGraphics returned is dependant on the destination graphics of the object
 * that the application has established.
 */
public final class QDGraphics extends QTPointerRef implements QuickTimeLib, QTStreamingLib {

    private static Object linkage;

    /**
     * This is the size (the number of bytes) that are required for this class
     */
    public static final int kNativeSize = 108;

    /**
     * This holds a reference to the scratchGWorld that you can use manually in
     * calls such as Movie.setGWorld. It is used in the Presentable interfaces
     * to signify that the Presentable is invisible (for instance when the QTJavaComponent
     * that hosts the Presentable is hidden). It should not be reset by an application.
     */
    public static QDGraphics scratch;

    /**
     * This holds a reference to the scratchGWorld that you can use
     * as a temporary destination graphics and still be considered as a valid
     * destination graphics by Presentable objects.
     */
    public static QDGraphics validScratch;

    /**
     * This is the default pixel format that is used to create offscreen graphics contexts when
     * no format is used. It is the same format as Java image classes use internally
     * ie. ARGB at 32bits per pixel.
     */
    public static final int kDefaultPixelFormat = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 32 : 1111970369;

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    private static final int kFontInfoSz = 4;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(QDGraphics.class);
                    }
                });
            }
        }.establish();
        try {
            scratch = new QDGraphics(kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
            validScratch = new QDGraphics(kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
        } catch (QTException localQTException) {
        }
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     * QuickTime::GraphicsExportGetInputGWorld
     * @param ge the GraphicsExporter
     * @return The current destination graphics port
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsExporter#getInputGWorld()}
     */
    public static QDGraphics fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsExportGetInputGWorld(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramGraphicsExporter);
    }

    /**
     * Returns the current QDGraphics.
     * QuickTime::GetPort
     * @return a QDGraphics
     */
    public static QDGraphics getPort() {
        int[] arrayOfInt = { 0 };
        synchronized (QTNative.globalsLock) {
            GetPort(arrayOfInt);
        }
        return new QDGraphics(arrayOfInt[0], new Object());
    }

    /**
     * Returns the pixel size for a given pixel format.
     * QuickTime::QTGetPixelSize
     * @return pixel size
     * @return {@link quicktime.std.StdQTException}
     */
    public static int getPixelSize(int paramInt) throws StdQTException {
        return QTGetPixelSize(paramInt);
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     * QuickTime::GraphicsImportGetGWorld
     * @param gi the GraphicsImporter
     * @return The current destination graphics port
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.GraphicsImporter#getGWorld()}
     */
    public static QDGraphics fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetGWorld(QTObject.ID(paramGraphicsImporter), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], paramGraphicsImporter);
    }

    /**
     * Your application can determine a movie's graphics world by calling the getGWorld method.
     * QuickTime::GetMovieGWorld()
     * @param m the Movie
     * @return The current destination graphics port
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.Movie#getGWorld()}
     */
    public static QDGraphics fromMovie(Movie paramMovie) throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            GetMovieGWorld(QTObject.ID(paramMovie), arrayOfInt, null);
            StdQTException.checkError(GetMoviesError());
            return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramMovie);
        }
    }

    /**
     * Your application can retrieve a Presentation's graphics world by calling the getGWorld method.
     * QuickTime::QTSPresGetGWorld()
     * @param p the Presentation
     * @param s Stream
     * @return The current destination graphics port
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.streaming.Presentation#getGWorld()}
     */
    public static QDGraphics fromPresentation(Presentation paramPresentation, Stream paramStream) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetGWorld(QTObject.ID(paramPresentation), QTObject.ID(paramStream), arrayOfInt1, arrayOfInt2));
            return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], paramPresentation);
        }
    }

    /**
     * This method returns a movie controller's color graphics port.
     * QuickTime::MCGetControllerPort()
     * @param mc the MovieController
     * @return A QDGraphics object representing the controller's graf port.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.MovieController#getPort()}
     */
    public static QDGraphics fromMovieController(MovieController paramMovieController) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = MCGetControllerPort(QTObject.ID(paramMovieController));
            StdQTException.checkError(GetMoviesError());
            return i == 0 ? scratch : new QDGraphics(i, paramMovieController);
        }
    }

    /**
     * Returns the QDGraphic context of the sequence grabber.
     * QuickTime::SGGetGWorld
     * @param sg the SequenceGrabber
     * @return The current graf port of the sequence grabber.
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.sg.SequenceGrabber#getGWorld()}
     */
    public static QDGraphics fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = SGGetGWorld(QTObject.ID(paramSequenceGrabber), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], paramSequenceGrabber);
    }

    /**
     * This method determines the location of the previous image buffer allocated
     * by the compressor.
     * QuickTime::GetCSequencePrevBuffer
     * @param c the CSequence
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.CSequence#prevBuffer()}
     */
    public static QDGraphics fromCSequence(CSequence paramCSequence) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequencePrevBuffer(QTObject.ID(paramCSequence), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramCSequence);
    }

    /**
     * This method helps you determine the location of the offscreen image buffer
     * allocated by the decompressor.
     * QuickTime::GetDSequenceImageBuffer
     * @param d the DSequence
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.DSequence#getImageBuffer()}
     */
    public static QDGraphics fromDSequenceImage(DSequence paramDSequence) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetDSequenceImageBuffer(QTObject.ID(paramDSequence), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramDSequence);
    }

    /**
     * This method enables you to determine the location of the offscreen buffer
     * allocated by the decompressor.
     * QuickTime::GetDSequenceScreenBuffer
     * @param d the DSequence
     * @return a QDGraphics object which is the graphics world for the screen buffer
     * @return {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.image.DSequence#getScreenBuffer()}
     */
    public static QDGraphics fromDSequenceScreen(DSequence paramDSequence) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetDSequenceScreenBuffer(QTObject.ID(paramDSequence), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramDSequence);
    }

    public static QDGraphics fromNativeGraphics(WinNativeGraphics paramWinNativeGraphics) throws NativeGraphicsException {
        if (paramWinNativeGraphics.hWnd == 0)
            throw new NativeGraphicsException("invalid HWnd");
        int i = GetNativeWindowPort(paramWinNativeGraphics.hWnd);
        if (i == 0)
            throw new NativeGraphicsException("invalid Port");
        return new QDGraphics(i, paramWinNativeGraphics);
    }

    public static QDGraphics fromNativeGraphics(MacNativeGraphics paramMacNativeGraphics) throws NativeGraphicsException {
        synchronized (QTNative.globalsLock) {
            try {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                paramMacNativeGraphics.dsi.lock();
                int i = paramMacNativeGraphics.nd.getPort();
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
                if (i == 0)
                    throw new NativeGraphicsException("invalid Port");
                QDGraphics localQDGraphics = new QDGraphics(i, paramMacNativeGraphics);
                paramMacNativeGraphics.dsi.unlock();
                return localQDGraphics;
            } finally {
                paramMacNativeGraphics.dsi.unlock();
            }
        }
    }

    private QDGraphics(int paramInt) throws QTException {
        super(paramInt, 108, null);
        if (isOffscreen()) {
            LockPixels(GetGWorldPixMap(_ID()));
        }
    }

    private QDGraphics(int paramInt, Object paramObject) {
        super(paramInt, 108, paramObject);
        if (isOffscreen()) {
            LockPixels(GetGWorldPixMap(_ID()));
        }
    }

    /**
     * Creates an offscreen QDGraphics based on the current compression settings.
     * QuickTime::SCNewGWorld()
     * @param rp the boundaries of the graphics world.
     * @param flags flags as specified to NewGWorld.
     * {@link quicktime.std.qtcomponents.ImageCompressionDialog#newGWorld(quicktime.qd.QDRect, int)}
     */
    public QDGraphics(ImageCompressionDialog paramImageCompressionDialog, QDRect paramQDRect, int paramInt) throws QTException {
        this(allocate(paramImageCompressionDialog, paramQDRect, paramInt));
    }

    /**
     * Creates an offscreen QDGraphics object from the width, height, depth, and color table
     * of a specified image description structure.
     * QuickTime::NewImageGWorld
     * @param flags holding graphics world flags.
     * quicktime.std.image.ImageDescription#newGWorld()
     */
    public QDGraphics(ImageDescription paramImageDescription, int paramInt) throws QTException {
        this(allocate(paramImageDescription, paramInt));
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. Uses a pixel depth of 32 bits in ARGB format
     * and no special flags settings. By default the pixels are locked.
     * QuickTime::QTNewGWorld
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     */
    public QDGraphics(QDRect paramQDRect) throws QTException {
        this(kDefaultPixelFormat, paramQDRect, null, null, 4096);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     * QuickTime::QTNewGWorld
     * @param pixelFormat the format of the pixels.
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     */
    public QDGraphics(int paramInt, QDRect paramQDRect) throws QTException {
        this(paramInt, paramQDRect, null, null, 4096);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     * QuickTime::QTNewGWorld
     * @param pixelFormat the format of the pixels.
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @param flags options available to your application. See GWorld flags above
     */
    public QDGraphics(int paramInt1, QDRect paramQDRect, int paramInt2) throws QTException {
        this(paramInt1, paramQDRect, null, null, paramInt2);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     * QuickTime::QTNewGWorld
     * @param pixelFormat the format of the pixels.
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @param cTable a ColorTable
     * @param aGDevice a GDevice that should only be supplied if you set the noNewDevice
     *  flag, otherwise it is just ignored and can be passed null.
     * @param flags options available to your application. See GWorld flags above
     */
    public QDGraphics(int paramInt1, QDRect paramQDRect, ColorTable paramColorTable, GDevice paramGDevice, int paramInt2) throws QTException {
        this(allocate(paramInt1, paramQDRect, paramColorTable, paramGDevice, paramInt2));
    }

    private static int allocate(int paramInt1, QDRect paramQDRect, ColorTable paramColorTable, GDevice paramGDevice, int paramInt2) throws QDException {
        int[] arrayOfInt = { 0 };
        int i = QTNewGWorld(arrayOfInt, paramInt1, paramQDRect.getRect(), QTObject.ID(paramColorTable), QTObject.ID(paramGDevice), paramInt2);
        QDException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(ImageCompressionDialog paramImageCompressionDialog, QDRect paramQDRect, int paramInt) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(SCNewGWorld(QTObject.ID(paramImageCompressionDialog), arrayOfInt, paramQDRect != null ? paramQDRect.getRect() : null, paramInt));
        if (arrayOfInt[0] == 0)
            throw new StdQTException(-50);
        return arrayOfInt[0];
    }

    private static int allocate(ImageDescription paramImageDescription, int paramInt) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NewImageGWorld(arrayOfInt, QTObject.ID(paramImageDescription), paramInt));
        return arrayOfInt[0];
    }

    /**
     * This method should be used when a number of drawing methods need to
     * be invoked on this QDGraphics object. It will establish the required
     * global state for these methods to be called efficiently.
     * @param d the QDDrawer object that will do the drawing
     * @param {@link quicktime.QTException}
     */
    public void beginDraw(QDDrawer paramQDDrawer) throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            paramQDDrawer.draw(this);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * This returns true if the QDGraphics is an offscreen GWorld or an onscreen
     * CGrafPort.
     * @return true if offscreen, else false
     */
    public boolean isOffscreen() {
        if (QTSession.isCurrentOS(4)) {
            int i = IsPortOffscreen(_ID());
            return i == 1;
        }
        return (getShortFromPointer(_ID(), 6) & 0xC001) == 49153;
    }

    /**
     * A PixMap object which describes the pixels in this QDGraphics.
     * @return a PixMap object
     * @see quicktime.std.qd.PixMap
     */
    public PixMap getPixMap() {
        return PixMap.fromQDGraphics(this);
    }

    /**
     * The port rectangle that defines a subset of the pixel map to be used
     * for drawing.
     * @return aRect object which defines the boundaries of the port rectangle
     */
    public QDRect getPortRect() {
        if (QTSession.isCurrentOS(4)) {
            Object localObject = new QDRect();
            GetPortBounds(_ID(), ((QDRect) localObject).getBytes());
            return (QDRect) localObject;
        }
        Object localObject = new byte[8];
        getBytesAt(16, 8, (byte[]) localObject, 0);
        return QDRect.fromArray((byte[]) localObject, 8);
    }

    /**
     * Creates an 80-by-80 pixel thumbnail picture from the pixmap associated with this QDGraphics.
     * QuickTime::MakeThumbnailFromPixMap()
     * @param src a QDRect object defining the portion of the image to use for the thumbnail
     * @param colorDepth specifies the depth at which the image is likely to be viewed
     * @return a Pict object representing the thumbnail
     * @return {@link quicktime.QTException}
     */
    public Pict makeThumbnail(QDRect paramQDRect, int paramInt) throws QTException {
        return Pict.thumbnailFromQDGraphics(this, paramQDRect, paramInt);
    }

    /**
     * The port rectangle that defines a subset of the pixel map to be used
     * for drawing.
     * @return aRect object which defines the boundaries of the port rectangle
     */
    public QDRect getBounds() {
        QDRect localQDRect = new QDRect();
        if (QTSession.isCurrentOS(4)) {
            GetPortBounds(_ID(), localQDRect.getBytes());
            return localQDRect;
        }
        return getPortRect();
    }

    /**
     * This method returns intersection of visRgn and clipRgn of the port.
     * It is implemented on the low level of native calls to avoid extra
     * load on garbage collector
     * @return Region object which defines the intersection of visible and clipping regions
     * @return {@link quicktime.QTException}
     */
    public Region getVisClipRgn() throws QTException {
        Region localRegion1 = new Region();
        if (QTSession.isCurrentOS(4)) {
            Region localRegion2 = new Region();
            Region localRegion3 = new Region();
            GetPortClipRegion(_ID(), QTObject.ID(localRegion2));
            GetPortVisibleRegion(_ID(), QTObject.ID(localRegion3));
            SectRgn(QTObject.ID(localRegion2), QTObject.ID(localRegion3), QTObject.ID(localRegion1));
        } else {
            int i = getIntFromPointer(_ID(), 28);
            int j = getIntFromPointer(_ID(), 24);
            SectRgn(i, j, QTObject.ID(localRegion1));
        }
        return localRegion1;
    }

    /**
     * This method returns the clip Region of the port.
     * QuickDraw::GetClip()
     * @return Region object which defines the clip region
     * @return {@link quicktime.QTException}
     */
    public Region getClip() throws QTException {
        Region localRegion = new Region();
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            GetClip(QTObject.ID(localRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return localRegion;
    }

    /**
     * This method sets the clip Region of the port.
     * QuickDraw::SetClip()
     * @param rgn Region object which defines the new clip region
     */
    public void setClip(Region paramRegion) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            SetClip(QTObject.ID(paramRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * To change the clipping region of this QDGraphics, use the ClipRect procedure.
     * This method will set the clip to the current port rect of the QDGraphics
     * QuickDraw::ClipRect()
     */
    public void clipRect() {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            ClipRect(_ID() + 16);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * To change the clipping region of this QDGraphics, use the ClipRect procedure.
     * @param r the rect that will become the new clip rgn
     */
    public void clipRect(QDRect paramQDRect) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            ClipRect(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Returns the current background color of the QDGraphic.
     * QuickDraw::GetBackColor()
     * @return QDColor
     */
    public QDColor getBackColor() {
        byte[] arrayOfByte = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetBackColor(arrayOfByte);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return QDColor.fromArray(arrayOfByte, 6);
    }

    /**
     * Sets the current background color of the
     * QDGraphic.
     * QuickDraw::RGBBackColor()
     * @param bColor QDColor
     */
    public void setBackColor(QDColor paramQDColor) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            RGBBackColor(paramQDColor.getRGBColor());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using Pen size of the graphics pen for the current graphics port,
     * FrameArc draws an arc of the oval bounded by the rectangle that you
     * specify in the area parameter. Use the startAngle parameter to specify
     * where the arc begins as modulo 360.
     * Use the arcAngle parameter to specify how many degrees the arc covers.
     * Specify whether the angles are in positive or negative degrees;
     * a positive angle goes clockwise, while a negative angle goes counterclockwise.
     * Zero degrees is at 12 oÕclock high, 90¡ (or Ð270¡) is at 3 oÕclock, 180¡ (or Ð180¡)
     * is at 6 oÕclock, and 270¡ (or Ð90¡) is at 9 oÕclock. Measure other angles relative
     * to the bounding rectangle.
     * A line from the center of the rectangle through its upper-right corner is at 45¡,
     * even if the rectangle isnÕt square; a line through the lower-right corner is at 135¡,
     * and so on.
     * The arc is as wide as the pen width and as tall as the pen height.
     * The pen location does not change.
     * QuickDraw::FrameArc()
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @param {@link quicktime.qd.QDException}
     */
    public void frameArc(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FrameArc(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen mode of the current graphics port, PaintArc
     * draws a wedge of the oval bounded by the rectangle that you specify in the area parameter.
     * As in the FrameArc procedure described on page 3-72 of Inside Macintosh: Imaging With QuickDraw
     * and illustrated in Figure 3-21(Im), use the startAngle and arcAngle parameters to define the arc of the wedge.
     * The pen location does not change.
     * QuickDraw::PaintArc()
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @param {@link quicktime.qd.QDException}
     */
    public void paintArc(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintArc(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * EraseArc draws a wedge of the oval bounded by the rectangle that you specify in the area parameter with the
     * background pattern for the current graphics port. As in the FrameArc method on page 3-72 of Inside Macintosh:
     * Imaging With QuickDraw and as illustrated in Figure 3-21(Im), use the startAngle and arcAngle parameters to
     * define the arc of the wedge.
     * This method leaves the location of the graphics pen unchanged.
     * QuickDraw::EraseArc()
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @param {@link quicktime.qd.QDException}
     */
    public void eraseArc(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseArc(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * InvertArc inverts the pixels enclosed by a wedge of the oval bounded by the rectangle
     * that you specify in the area parameter. Every white pixel becomes black and every black pixel becomes white.
     * As in the FrameArc procedure described on page 3-72 of Inside Macintosh: Imaging With QuickDraw and as
     * illustrated in Figure 3-21(Im), use the startAngle and arcAngle parameters to define the arc of the wedge.
     * This procedure leaves the location of the graphics pen unchanged.
     * QuickDraw::InvertArc()
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @param {@link quicktime.qd.QDException}
     */
    public void invertArc(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertArc(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the Pen size of the graphics pen for the current graphics port, FrameRoundRect
     * draws an outline just inside the rounded rectangle bounded by the rectangle that you specify
     * in the area parameter. The outline is as wide as the pen width and as tall as the pen height.
     * The pen location does not change. Use the ovalWidth and ovalHeight parameters to specify the diameters
     * of curvature for the corners of the rounded rectangle.
     * If a region is open and being formed, the outside outline of the new rounded rectangle is mathematically
     * added to the regionÕs boundary.
     * QuickDraw::FrameRoundRect()
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @param {@link quicktime.qd.QDException}
     */
    public void frameRoundRect(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FrameRoundRect(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using Pen mode of the graphics pen for the current graphics port, the PaintRoundRect procedure draws
     * the interior of the rounded rectangle bounded by the rectangle that you specify in the area parameter.
     * Use the ovalWidth and ovalHeight parameters to specify the diameters of curvature for the corners of the
     * rounded rectangle. The pen location does not change..
     * QuickDraw::PaintRoundRect()
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @param {@link quicktime.qd.QDException}
     */
    public void paintRoundRect(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintRoundRect(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * EraseRoundRect draws the interior of the rounded rectangle bounded by the rectangle that you specify
     * in the area parameter with the background settings of the current graphics port.
     * This effectively erases the rounded rectangle. Use the ovalWidth and ovalHeight
     * parameters to specify the diameters of curvature for the corners of the rounded rectangle.
     * This procedure leaves the location of the graphics pen unchanged.
     * QuickDraw::EraseRoundRect()
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @param {@link quicktime.qd.QDException}
     */
    public void eraseRoundRect(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseRoundRect(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The InvertRoundRect procedure inverts the pixels enclosed by the rounded rectangle bounded by the rectangle
     * that you specify in the area parameter. Every white pixel becomes black and every black pixel becomes white.
     * The ovalWidth and ovalHeight parameters specify the diameters of curvature for the corners.
     * The pen location does not change.
     * QuickDraw::InvertRoundRect()
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @param {@link quicktime.qd.QDException}
     */
    public void invertRoundRect(QDRect paramQDRect, int paramInt1, int paramInt2) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertRoundRect(paramQDRect.getRect(), (short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the Pen mode, and size of the graphics pen for the current graphics port,
     * the FrameOval procedure draws an outline just inside the oval with the bounding rectangle that you
     * specify in the area parameter. The outline is as wide as the pen width and as tall as the pen height.
     * The pen location does not change.
     * If a region is open and being formed, the outside outline of the new oval is mathematically added
     * to the regionÕs boundary.
     * QuickDraw::FrameOval()
     * @param area The rectangle that defines the ovalÕs boundary.
     * @param {@link quicktime.qd.QDException}
     */
    public void frameOval(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FrameOval(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen mode for the current graphics port,
     * the PaintOval procedure draws the interior of an oval just inside the bounding rectangle
     * that you specify in the area parameter. The pen location does not change.
     * QuickDraw::PaintOval()
     * @param area The rectangle that defines the ovalÕs boundary.
     * @param {@link quicktime.qd.QDException}
     */
    public void paintOval(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintOval(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the background pattern for the current graphics port and the patCopy pattern mode,
     * the EraseOval procedure draws the interior of an oval just inside the bounding rectangle that
     * you specify in the area parameter. This effectively erases the oval bounded by the specified rectangle.
     * This procedure leaves the location of the graphics pen unchanged.
     * QuickDraw::EraseOval()
     * @param area The rectangle that defines the ovalÕs boundary.
     * @param {@link quicktime.qd.QDException}
     */
    public void eraseOval(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseOval(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The InvertOval procedure inverts the pixels enclosed by an oval just inside
     * the bounding rectangle that you specify in the area parameter.
     * Every white pixel becomes black and every black pixel becomes white.
     * The pen location does not change.
     * QuickDraw::InvertOval()
     * @param area The rectangle that defines the ovalÕs boundary.
     * @param {@link quicktime.qd.QDException}
     */
    public void invertOval(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertOval(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the current graphics portÕs pen size,
     * FramePoly plays back the line-drawing commands that define
     * the polygon passed in the poly parameter.
     * The graphics pen hangs below and to the right of each point on the boundary of the polygon.
     * Thus, the drawn polygon extends beyond the right and bottom edges of the polygonÕs bounding rectangle
     * (which is stored in the polyBBox field of the Polygon record) by the pen width and pen height,
     * respectively. All other graphics operations, such as painting a polygon with the PaintPoly procedure,
     * occur strictly within the boundary of the polygon, as illustrated in Figure 3-22(Im).
     * QuickDraw::FramePoly()
     * @param poly Polygon to frame.
     * @param {@link quicktime.qd.QDException}
     */
    public void framePoly(Polygon paramPolygon) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FramePoly(QTObject.ID(paramPolygon));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen pattern and pattern mode for the current graphics port,
     * PaintPoly draws the interior of a polygon
     * passed in the poly parameter. The pen location does not change.
     * QuickDraw::PaintPoly()
     * @param poly Polygon to paint.
     * @param {@link quicktime.qd.QDException}
     */
    public void paintPoly(Polygon paramPolygon) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintPoly(QTObject.ID(paramPolygon));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the patCopy pattern mode, ErasePoly draws the
     * interior of the polygon passed in the poly parameter
     * with the background pattern for the current graphics port.
     * QuickDraw::ErasePoly()
     * @param poly Polygon to erase.
     * @param {@link quicktime.qd.QDException}
     */
    public void erasePoly(Polygon paramPolygon) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            ErasePoly(QTObject.ID(paramPolygon));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * invertPoly inverts the pixels enclosed by the polygon passed in the poly parameter.
     * Every white pixel becomes black and every black pixel becomes white.
     * This procedure leaves the location of the graphics pen unchanged.
     * QuickDraw::InvertPoly()
     * @param poly Polygon to erase.
     * @param {@link quicktime.qd.QDException}
     */
    public void invertPoly(Polygon paramPolygon) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertPoly(QTObject.ID(paramPolygon));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen size of the graphics pen for the current graphics port,
     * the FrameRect procedure draws an outline just inside the rectangle that you specify in the area parameter.
     * The outline is as wide as the pen width and as tall as the pen height. The pen location does not change.
     * QuickDraw::FrameRect()
     * @param area the portion of the QDGraphic port to frame
     *  or null for entire bounds
     * @param {@link quicktime.qd.QDException}
     */
    public void frameRect(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null)
            paramQDRect = getPortRect();
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                FrameRect(paramQDRect.getRect());
            } else
                MacFrameRect(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Erases the specified rect with the current back color.
     * QuickDraw::EraseRect()
     * @param area the portion of the QDGraphic port to erase
     *  or null for entire bounds
     * @param {@link quicktime.qd.QDException}
     */
    public void eraseRect(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseRect(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Erases the specified region with the current back color.
     * QuickDraw::EraseRgn()
     * @param area the portion of the QDGraphic to erase
     * @param {@link quicktime.qd.QDException}
     */
    public void eraseRgn(Region paramRegion) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseRgn(QTObject.ID(paramRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Returns the RGB color for the pixel at the location you
     * specify in the h and v parameters.
     * QuickDraw::GetCPixel()
     * @param h The horizontal coordinate of the point at the upper-left corner of the pixel.
     * @param v The vertical coordinate of the point at the upper-left corner of the pixel.
     * @return The QDColor for the pixel.
     * @return {@link quicktime.qd.QDException}
     */
    public QDColor getCPixel(int paramInt1, int paramInt2) throws QDException {
        byte[] arrayOfByte = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetCPixel((short) paramInt1, (short) paramInt2, arrayOfByte);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return QDColor.fromArray(arrayOfByte, 6);
    }

    /**
     * For the pixel at the location you specify in the h and v parameters,
     * the setCPixel procedure sets a pixel value that most closely matches
     * the RGB color that you specify in the cPix parameter.
     * On an indexed color system, the SetCPixel procedure sets the pixel
     * value to the index of the best-matching color in the current deviceÕs CLUT.
     * In a direct environment, the SetCPixel procedure sets the pixel value to a
     * 16-bit or 32-bit direct pixel value.
     * QuickDraw::SetCPixel()
     * @param h The horizontal coordinate of the point at the upper-left corner of the pixel.
     * @param v The vertical coordinate of the point at the upper-left corner of the pixel.
     * @param {@link quicktime.qd.QDException}
     */
    public void setCPixel(int paramInt1, int paramInt2, QDColor paramQDColor) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            SetCPixel((short) paramInt1, (short) paramInt2, paramQDColor.getRGBColor());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The PenSize procedure sets the width that you specify in
     * the width parameter and the height that you specify in
     * the height parameter for the graphics pen in the current graphics port.
     * All subsequent calls to the Line and LineTo procedures and to the
     * procedures that draw framed shapes in the current graphics port
     * use the new pen dimensions.
     * QuickDraw::PenSize()
     * @param width The pen width, as an integer from 0 to 32,767. If you set the value to 0, the pen does not draw. Values less than 0 are undefined.
     * @param height The pen height, as an integer from 0 to 32,767. If you set the value to 0, the pen does not draw. Values less than 0 are undefined.
     * @param {@link quicktime.qd.QDException}
     */
    public void penSize(int paramInt1, int paramInt2) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PenSize((short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The PenNormal procedure restores the size, of the graphics pen
     * in the current graphics port to their initial values:
     * a size of 1 pixel by 1 pixel. The pen location does not change.
     * QuickDraw::PenNormal()
     */
    public void penNormal() throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PenNormal();
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Returns the current foreground color of the
     * QDGraphic.
     * QuickDraw::GetForeColor()
     * @return QDColor
     */
    public QDColor getForeColor() {
        byte[] arrayOfByte = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetForeColor(arrayOfByte);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return QDColor.fromArray(arrayOfByte, 6);
    }

    /**
     * Sets the current foreground color of the QDGraphics.
     * QuickDraw::RGBForeColor()
     * @param fColor QDColor
     */
    public void setForeColor(QDColor paramQDColor) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            RGBForeColor(paramQDColor.getRGBColor());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Paints the specified rect with the current foreground color.
     * QuickDraw::PaintRect()
     * @param area the portion of the QDGraphic port to paint
     *  or null for entire bounds
     * @param {@link quicktime.qd.QDException}
     */
    public void paintRect(QDRect paramQDRect) throws QDException {
        if (paramQDRect == null) {
            paramQDRect = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintRect(paramQDRect.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The LineTo procedure draws a line from the graphics penÕs current location
     * in the current graphics port to the new location (h,v), which you specify in the
     * local coordinates of the current graphics port. If you are using LineTo to
     * draw a region or polygon, its outline is infinitely thin and is not affected
     * by the values of the pnSize, pnMode, or pnPat field of the graphics port.
     * QuickDraw::LineTo()
     * QuickDraw::MacLineTo()
     * @param h The horizontal coordinate of the graphics penÕs new location.
     * @param v The vertical coordinate of the graphics penÕs new location.
     * @param {@link quicktime.qd.QDException}
     */
    public void lineTo(int paramInt1, int paramInt2) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                LineTo((short) paramInt1, (short) paramInt2);
            } else
                MacLineTo((short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Starting at the current location of the graphics pen, the Line procedure
     * draws a line the horizontal distance that you specify in the h
     * parameter and the vertical distance that you specify in the v parameter.
     * QuickDraw::Line()
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @param {@link quicktime.qd.QDException}
     */
    public void line(int paramInt1, int paramInt2) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            Line((short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The MoveTo procedure changes the graphics penÕs current location to
     * the new horizontal coordinate you specify in the h parameter and
     * the new vertical coordinate you specify in the v parameter.
     * Specify the new location in the local coordinates of the current graphics port.
     * The MoveTo procedure performs no drawing.
     * QuickDraw::MoveTo()
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @param {@link quicktime.qd.QDException}
     */
    public void moveTo(int paramInt1, int paramInt2) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            MoveTo((short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The Move procedure moves the graphics pen from its current location
     * in the current graphics port a horizontal distance that you specify
     * in the h parameter and a vertical distance that you specify in the
     * v parameter.
     * QuickDraw::Move()
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @param {@link quicktime.qd.QDException}
     */
    public void move(int paramInt1, int paramInt2) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            Move((short) paramInt1, (short) paramInt2);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Paints the specified region with the current foreground color.
     * QuickDraw::PaintRgn()
     * QuickDraw::MacPaintRgn()
     * @param area the portion of the QDGraphic port to paint
     * @param {@link quicktime.qd.QDException}
     */
    public void paintRgn(Region paramRegion) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                PaintRgn(QTObject.ID(paramRegion));
            } else
                MacPaintRgn(QTObject.ID(paramRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Draws an outline just inside the region you pass in the area parameter.
     * The outline never goes outside the region boundary. The pen location does not change.
     * QuickDraw::FrameRgn()
     * QuickDraw::MacFrameRgn()
     * @param area the portion of the QDGraphic port to paint
     * @param {@link quicktime.qd.QDException}
     */
    public void frameRgn(Region paramRegion) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                FrameRgn(QTObject.ID(paramRegion));
            } else
                MacFrameRgn(QTObject.ID(paramRegion));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The TextFont procedure sets the font of the QDGraphics port in which the text is to be rendered.
     * QuickDraw::TextFont()
     * @param fontNumber The font family ID (obtained from an getFNum call).
     * @see getFNum
     */
    public final void textFont(int paramInt) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextFont((short) paramInt);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextFont method returns the font family ID of the current font in this QDGraphics port.
     * QuickDraw::TextFont()
     * @return The font family ID of the current font.
     * @see getFNum
     */
    public int getTextFont() {
        return getShortAt(68);
    }

    /**
     * The TextFace procedure sets the style of the font in which the text is to be drawn in this QDGraphics port.
     * QuickDraw::TextFace()
     * @param face The style for text to be drawn
     */
    public void textFace(int paramInt) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextFace((short) paramInt);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextFace method returns the style of the current font in this QDGraphics port.
     * QuickDraw::TextFace()
     * @return The style of the current font.
     */
    public int getTextFace() {
        return QTUtils.UByte2Int(getByteAt(70));
    }

    /**
     * The TextMode procedure sets the transfer mode for drawing text in this QDGraphics port.
     * QuickDraw::TextMode()
     * @param mode The transfer mode to be used in drawing text.  Possible values are: srcCopy, srcOr, srcXOr, srcBic, etc.
     * @see quicktime.QTConstants#srcCopy
     */
    public void textMode(int paramInt) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextMode((short) paramInt);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextMode method returns the current transfer mode for drawing text in this QDGraphics port.
     * QuickDraw::TextMode()
     * @return The transfer mode used in drawing text.
     */
    public int getTextMode() {
        return getShortAt(72);
    }

    /**
     * The TextSize procedure sets the font size for text drawn in this QDGraphics port.
     * QuickDraw::TextSize()
     * @param size The font size in points.  If you specify 0, the system font size is used.
     */
    public void textSize(int paramInt) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextSize((short) paramInt);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextSize method returns the current font size for text drawn in this QDGraphics port.
     * QuickDraw::TextSize()
     * @return The font size in points.
     */
    public int getTextSize() {
        return getShortAt(74);
    }

    /**
     * The DrawChar procedure draws the glyph for the specified character at the current pen location in this QDGraphics port.
     * QuickDraw::DrawChar()
     * @param character The character code whose glyph is to be drawn.
     */
    public void drawChar(int paramInt) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            DrawChar((short) paramInt);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The DrawText procedure draws the specified text at the current pen location in this QDGraphics port.
     * QuickDraw::DrawText()
     * @param text String containing the text to be drawn.
     * @param firstByte An offset from the start of the String (text) to the first byte of the text to be drawn.
     * @param byteCount The number of bytes of text to be drawn.
     */
    public void drawText(String paramString, int paramInt1, int paramInt2) {
        byte[] arrayOfByte = paramString.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            if (i != 0) {
                DrawText(arrayOfByte, (short) paramInt1, (short) paramInt2);
                SetGWorld(savedPort[0], savedDevice[0]);
            } else {
                MacDrawText(arrayOfByte, (short) paramInt1, (short) paramInt2);
            }
        }
    }

    /**
     * The CharWidth function returns the width in pixels of the specified character in this QDGraphics port.
     * QuickDraw::CharWidth()
     * @param character The character code whose width is to be measured.
     * @return The width of the specified character in pixels.
     */
    public int charWidth(int paramInt) {
        int j = 0;
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            j = CharWidth((short) paramInt);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * The TextWidth function returns the length in pixels of the specified text in this QDGraphics port.
     * QuickDraw::TextWidth()
     * @param text String containing the text to be measured.
     * @param firstByte An offset from the start of the String (text) to the first byte of the text to be measured.
     * @param byteCount The number of bytes of text to be measured.
     * @return The width of the specified text in pixels.
     */
    public int textWidth(String paramString, int paramInt1, int paramInt2) {
        int j = 0;
        byte[] arrayOfByte = paramString.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            j = TextWidth(arrayOfByte, (short) paramInt1, (short) paramInt2);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * The StdText procedure is a QuickDraw bottleneck routine that can be called directly to obtain scaled (or unscaled) text..
     * QuickDraw::StdText()
     * @param count The number of bytes of text to be drawn.
     * @param text String containing the text to be drawn.
     * @param xScale Represents the scaling factor in the X dimension.
     * @param yScale Represents the scaling factor in the Y dimension.
     */
    public void drawTextScaled(int paramInt, String paramString, float paramFloat1, float paramFloat2) {
        QDPoint localQDPoint1 = new QDPoint((int) (paramFloat1 * 100.0F), (int) (paramFloat2 * 100.0F));
        QDPoint localQDPoint2 = new QDPoint(100, 100);
        byte[] arrayOfByte = paramString.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            StdText((short) paramInt, arrayOfByte, localQDPoint1.getPoint(), localQDPoint2.getPoint());
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The measureScaledText method (based on the QuickDraw function StdTxMeas) measures the width of scaled or unscaled text.
     * QuickDraw::StdTxMeas()
     * @param byteCount The number of bytes of text to be measured.
     * @param text String containing the text to be measured.
     * @param xScale Represents the scaling factor in the X dimension.
     * @param yScale Represents the scaling factor in the Y dimension.
     * @return The width of the specified text in pixels.
     */
    public int measureScaledText(int paramInt, String paramString, float paramFloat1, float paramFloat2) {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QDPoint localQDPoint1 = new QDPoint((int) (paramFloat1 * 100.0F), (int) (paramFloat2 * 100.0F));
        arrayOfInt1[0] = localQDPoint1.getPoint();
        QDPoint localQDPoint2 = new QDPoint(100, 100);
        arrayOfInt2[0] = localQDPoint2.getPoint();
        byte[] arrayOfByte = paramString.getBytes();
        short[] arrayOfShort = new short[4];
        int j = 0;
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            GetFontInfo(arrayOfShort);
            j = StdTxMeas((short) paramInt, arrayOfByte, arrayOfInt1, arrayOfInt2, arrayOfShort);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * Print information about this object.
     * @return a string representing this QDGraphics object
     */
    public String toString() {
        return super.toString() + "[PortRect=" + getPortRect() + ",isOffscreen=" + isOffscreen() + "]";
    }

    private static native int GraphicsExportGetInputGWorld(int paramInt, int[] paramArrayOfInt);

    private static native short QTGetPixelSize(int paramInt);

    private static native int GraphicsImportGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void GetMovieGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short GetMoviesError();

    private static native int MCGetControllerPort(int paramInt);

    private static native int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short GetCSequencePrevBuffer(int paramInt, int[] paramArrayOfInt);

    private static native short GetDSequenceImageBuffer(int paramInt, int[] paramArrayOfInt);

    private static native short GetDSequenceScreenBuffer(int paramInt, int[] paramArrayOfInt);

    private static native int GetNativeWindowPort(int paramInt);

    private static native int GetGWorldPixMap(int paramInt);

    private static native byte LockPixels(int paramInt);

    private static native short QTNewGWorld(int[] paramArrayOfInt, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4);

    private static native int SCNewGWorld(int paramInt1, int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt2);

    private static native short NewImageGWorld(int[] paramArrayOfInt, int paramInt1, int paramInt2);

    private static native void GetBackColor(byte[] paramArrayOfByte);

    private static native void RGBBackColor(byte[] paramArrayOfByte);

    private static native void EraseRect(byte[] paramArrayOfByte);

    private static native void FrameRect(byte[] paramArrayOfByte);

    private static native void MacFrameRect(byte[] paramArrayOfByte);

    private static native void EraseRgn(int paramInt);

    private static native void GetForeColor(byte[] paramArrayOfByte);

    private static native void RGBForeColor(byte[] paramArrayOfByte);

    private static native void PaintRect(byte[] paramArrayOfByte);

    private static native void MacPaintRgn(int paramInt);

    private static native void MacFrameRgn(int paramInt);

    private static native void GetPort(int[] paramArrayOfInt);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void PaintRgn(int paramInt);

    private static native void FrameRgn(int paramInt);

    private static native void CopyRgn(int paramInt1, int paramInt2);

    private static native void SetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4);

    private static native void OffsetRgn(int paramInt, short paramShort1, short paramShort2);

    private static native void UnionRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native void XorRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native byte EqualRgn(int paramInt1, int paramInt2);

    private static native void SectRgn(int paramInt1, int paramInt2, int paramInt3);

    private static native void GetClip(int paramInt);

    private static native void SetClip(int paramInt);

    private static native void ClipRect(byte[] paramArrayOfByte);

    private static native void ClipRect(int paramInt);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void Move(short paramShort1, short paramShort2);

    private static native void MoveTo(short paramShort1, short paramShort2);

    private static native void MacLineTo(short paramShort1, short paramShort2);

    private static native void LineTo(short paramShort1, short paramShort2);

    private static native void Line(short paramShort1, short paramShort2);

    private static native void SetCPixel(short paramShort1, short paramShort2, byte[] paramArrayOfByte);

    private static native void GetCPixel(short paramShort1, short paramShort2, byte[] paramArrayOfByte);

    private static native void PenSize(short paramShort1, short paramShort2);

    private static native void PenNormal();

    private static native void FrameArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void PaintArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void EraseArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void InvertArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void FrameRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void PaintRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void EraseRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void InvertRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void FrameOval(byte[] paramArrayOfByte);

    private static native void PaintOval(byte[] paramArrayOfByte);

    private static native void EraseOval(byte[] paramArrayOfByte);

    private static native void InvertOval(byte[] paramArrayOfByte);

    private static native void FramePoly(int paramInt);

    private static native void PaintPoly(int paramInt);

    private static native void ErasePoly(int paramInt);

    private static native void InvertPoly(int paramInt);

    private static native short getShortFromPointer(int paramInt1, int paramInt2);

    private static native void setIntInPointer(int paramInt1, int paramInt2, int paramInt3);

    private static native int getIntFromPointer(int paramInt1, int paramInt2);

    private static native void TextFont(short paramShort);

    private static native void TextFace(short paramShort);

    private static native void TextMode(short paramShort);

    private static native void TextSize(short paramShort);

    private static native void DrawChar(short paramShort);

    private static native void MacDrawText(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void DrawText(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native short CharWidth(short paramShort);

    private static native short TextWidth(byte[] paramArrayOfByte, short paramShort1, short paramShort2);

    private static native void MeasureText(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt);

    private static native void StdText(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native short StdTxMeas(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, short[] paramArrayOfShort);

    private static native void GetFontInfo(short[] paramArrayOfShort);

    private static native byte IsPortOffscreen(int paramInt);

    private static native int GetPortBounds(int paramInt, byte[] paramArrayOfByte);

    private static native int GetPortClipRegion(int paramInt1, int paramInt2);

    private static native int GetPortVisibleRegion(int paramInt1, int paramInt2);

    private static native short QTSPresGetGWorld(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/qd/
 * QDGraphics.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
