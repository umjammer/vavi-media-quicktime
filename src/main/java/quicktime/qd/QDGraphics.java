package quicktime.qd;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.GraphicsExportManager;
import quicktime.internal.toolbox.GraphicsImportManager;
import quicktime.internal.toolbox.ImageCompressionManager;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieControllerManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.PlatformManager;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.StreamingManager;
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


/**
 * A color graphics port defines a complete drawing environment that determines where
 * and how color graphics operations take place.
 * <p>
 * To create an offscreen QDGraphics (in traditional QuickDraw parlance a GWorld)
 * any one of the various constructors can be used.
 * The QDGraphics from... calls will return the existing destination graphics of the
 * first supplied object. For the fromNativeGraphics this will always be an on screen
 * graphics (in traditional QuickDraw parlance a CGrafPort). For the other calls the
 * type of the QDGraphics returned is dependant on the destination graphics of the object
 * that the application has established.
 */
public final class QDGraphics extends QTPointerRef implements QuickTimeLib, QTStreamingLib {

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

    private static final int[] savedPort = {0};

    private static final int[] savedDevice = {0};

    private static final int kFontInfoSz = 4;

    static {
        try {
            scratch = new QDGraphics(kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
            validScratch = new QDGraphics(kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
        } catch (QTException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     *
     * @param ge the GraphicsExporter
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GraphicsExportGetInputGWorld"
     * @see quicktime.std.image.GraphicsExporter#getInputGWorld()
     */
    public static QDGraphics fromGraphicsExporter(GraphicsExporter ge) throws StdQTException {
        int[] gworld = new int[1];
        int i = GraphicsExportGetInputGWorld(QTObject.ID(ge), gworld);
        StdQTException.checkError(i);
        return gworld[0] == 0 ? scratch : new QDGraphics(gworld[0], ge);
    }

    /**
     * Returns the current QDGraphics.
     *
     * @return a QDGraphics
     * @see "QuickTime::GetPort"
     */
    public static QDGraphics getPort() {
        int[] port = {0};
        synchronized (QTNative.globalsLock) {
            GetPort(port);
        }
        return new QDGraphics(port[0], new Object());
    }

    /**
     * Returns the pixel size for a given pixel format.
     *
     * @return pixel size
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTGetPixelSize"
     */
    public static int getPixelSize(int pixelFormat) throws StdQTException {
        return QTGetPixelSize(pixelFormat);
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     *
     * @param gi the GraphicsImporter
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GraphicsImportGetGWorld"
     * @see quicktime.std.image.GraphicsImporter#getGWorld()
     */
    public static QDGraphics fromGraphicsImporter(GraphicsImporter gi) throws StdQTException {
        int[] port = new int[1];
        int[] gdh = new int[1];
        int i = GraphicsImportGetGWorld(QTObject.ID(gi), port, gdh);
        StdQTException.checkError(i);
        return port[0] == 0 ? scratch : new QDGraphics(port[0], gi);
    }

    /**
     * Your application can determine a movie's graphics world by calling the getGWorld method.
     *
     * @param m the Movie
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetMovieGWorld()"
     * @see quicktime.std.movies.Movie#getGWorld()
     */
    public static QDGraphics fromMovie(Movie m) throws StdQTException {
        int[] port = new int[1];
        synchronized (QTNative.globalsLock) {
            GetMovieGWorld(QTObject.ID(m), port, null);
            StdQTException.checkError(GetMoviesError());
            return port[0] == 0 ? scratch : new QDGraphics(port[0], m);
        }
    }

    /**
     * Your application can retrieve a Presentation's graphics world by calling the getGWorld method.
     *
     * @param p the Presentation
     * @param s Stream
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::QTSPresGetGWorld()"
     * @see quicktime.streaming.Presentation#getGWorld()
     */
    public static QDGraphics fromPresentation(Presentation p, Stream s) throws StdQTException {
        int[] gWorld = new int[1];
        int[] gdHandle = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetGWorld(QTObject.ID(p), QTObject.ID(s), gWorld, gdHandle));
            return gWorld[0] == 0 ? scratch : new QDGraphics(gWorld[0], p);
        }
    }

    /**
     * This method returns a movie controller's color graphics port.
     *
     * @param mc the MovieController
     * @return A QDGraphics object representing the controller's graf port.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MCGetControllerPort()"
     * @see quicktime.std.movies.MovieController#getPort()
     */
    public static QDGraphics fromMovieController(MovieController mc) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = MCGetControllerPort(QTObject.ID(mc));
            StdQTException.checkError(GetMoviesError());
            return i == 0 ? scratch : new QDGraphics(i, mc);
        }
    }

    /**
     * Returns the QDGraphic context of the sequence grabber.
     *
     * @param sg the SequenceGrabber
     * @return The current graf port of the sequence grabber.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetGWorld"
     * @see quicktime.std.sg.SequenceGrabber#getGWorld()
     */
    public static QDGraphics fromSequenceGrabber(SequenceGrabber sg) throws StdQTException {
        int[] port = new int[1];
        int[] gdh = new int[1];
        int i = SGGetGWorld(QTObject.ID(sg), port, gdh);
        StdQTException.checkError(i);
        return port[0] == 0 ? scratch : new QDGraphics(port[0], sg);
    }

    /**
     * This method determines the location of the previous image buffer allocated
     * by the compressor.
     *
     * @param c the CSequence
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetCSequencePrevBuffer"
     * @see quicktime.std.image.CSequence#prevBuffer()
     */
    public static QDGraphics fromCSequence(CSequence c) throws StdQTException {
        int[] gworld = new int[1];
        StdQTException.checkError(GetCSequencePrevBuffer(QTObject.ID(c), gworld));
        return gworld[0] == 0 ? scratch : new QDGraphics(gworld[0], c);
    }

    /**
     * This method helps you determine the location of the offscreen image buffer
     * allocated by the decompressor.
     *
     * @param d the DSequence
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetDSequenceImageBuffer"
     * @see quicktime.std.image.DSequence#getImageBuffer()
     */
    public static QDGraphics fromDSequenceImage(DSequence d) throws StdQTException {
        int[] gworld = new int[1];
        StdQTException.checkError(GetDSequenceImageBuffer(QTObject.ID(d), gworld));
        return gworld[0] == 0 ? scratch : new QDGraphics(gworld[0], d);
    }

    /**
     * This method enables you to determine the location of the offscreen buffer
     * allocated by the decompressor.
     *
     * @param d the DSequence
     * @return a QDGraphics object which is the graphics world for the screen buffer
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetDSequenceScreenBuffer"
     * @see quicktime.std.image.DSequence#getScreenBuffer()
     */
    public static QDGraphics fromDSequenceScreen(DSequence d) throws StdQTException {
        int[] gworld = new int[1];
        StdQTException.checkError(GetDSequenceScreenBuffer(QTObject.ID(d), gworld));
        return gworld[0] == 0 ? scratch : new QDGraphics(gworld[0], d);
    }

    /**
     * This will return a QDGraphics object from a native graphics object
     *
     * @see "QuickTime::GetNativeWindowPort"
     */
    public static QDGraphics fromNativeGraphics(WinNativeGraphics winNativeGraphics) throws NativeGraphicsException {
        if (winNativeGraphics.hWnd == 0)
            throw new NativeGraphicsException("invalid HWnd");
        int i = GetNativeWindowPort(winNativeGraphics.hWnd);
        if (i == 0)
            throw new NativeGraphicsException("invalid Port");
        return new QDGraphics(i, winNativeGraphics);
    }

    /**
     * This will return a QDGraphics object from a native graphics object
     */
    public static QDGraphics fromNativeGraphics(MacNativeGraphics macNativeGraphics) throws NativeGraphicsException {
        synchronized (QTNative.globalsLock) {
            try {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    GetGWorld(savedPort, savedDevice);
                macNativeGraphics.dsi.lock();
                int i = macNativeGraphics.nd.getPort();
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
                if (i == 0)
                    throw new NativeGraphicsException("invalid Port");
                QDGraphics qdGraphics = new QDGraphics(i, macNativeGraphics);
                macNativeGraphics.dsi.unlock();
                return qdGraphics;
            } finally {
                macNativeGraphics.dsi.unlock();
            }
        }
    }

    private QDGraphics(int nativeRep) throws QTException {
        super(nativeRep, 108, null);
        if (isOffscreen()) {
            LockPixels(GetGWorldPixMap(_ID()));
        }
    }

    private QDGraphics(int nativeRep, Object ref) {
        super(nativeRep, 108, ref);
        if (isOffscreen()) {
            LockPixels(GetGWorldPixMap(_ID()));
        }
    }

    /**
     * Creates an offscreen QDGraphics based on the current compression settings.
     *
     * @param rp    the boundaries of the graphics world.
     * @param flags flags as specified to NewGWorld.
     *              {@link quicktime.std.qtcomponents.ImageCompressionDialog#newGWorld(quicktime.qd.QDRect, int)}
     * @see "QuickTime::SCNewGWorld()"
     */
    public QDGraphics(ImageCompressionDialog dialog, QDRect rp, int flags) throws QTException {
        this(allocate(dialog, rp, flags));
    }

    /**
     * Creates an offscreen QDGraphics object from the width, height, depth, and color table
     * of a specified image description structure.
     *
     * @param flags holding graphics world flags.
     *              quicktime.std.image.ImageDescription#newGWorld()
     * @see "QuickTime::NewImageGWorld"
     */
    public QDGraphics(ImageDescription imageDescription, int flags) throws QTException {
        this(allocate(imageDescription, flags));
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. Uses a pixel depth of 32 bits in ARGB format
     * and no special flags settings. By default the pixels are locked.
     *
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *               pixel map
     * @see "QuickTime::QTNewGWorld"
     */
    public QDGraphics(QDRect bounds) throws QTException {
        this(kDefaultPixelFormat, bounds, null, null, 4096);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     *
     * @param pixelFormat the format of the pixels.
     * @param bounds      the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @see "QuickTime::QTNewGWorld"
     */
    public QDGraphics(int pixelFormat, QDRect bounds) throws QTException {
        this(pixelFormat, bounds, null, null, 4096);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     *
     * @param pixelFormat the format of the pixels.
     * @param bounds      the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @param flags       options available to your application. See GWorld flags above
     * @see "QuickTime::QTNewGWorld"
     */
    public QDGraphics(int pixelFormat, QDRect bounds, int flags) throws QTException {
        this(pixelFormat, bounds, null, null, flags);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     *
     * @param pixelFormat the format of the pixels.
     * @param bounds      the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @param cTable      a ColorTable
     * @param aGDevice    a GDevice that should only be supplied if you set the noNewDevice
     *                    flag, otherwise it is just ignored and can be passed null.
     * @param flags       options available to your application. See GWorld flags above
     * @see "QuickTime::QTNewGWorld"
     */
    public QDGraphics(int pixelFormat, QDRect bounds, ColorTable cTable, GDevice aGDevice, int flags) throws QTException {
        this(allocate(pixelFormat, bounds, cTable, aGDevice, flags));
    }

    private static int allocate(int startAngle, QDRect area, ColorTable colorTable, GDevice gd, int arcAngle) throws QDException {
        int[] gworld = {0};
        int i = QTNewGWorld(gworld, startAngle, area.getRect(), QTObject.ID(colorTable), QTObject.ID(gd), arcAngle);
        QDException.checkError(i);
        return gworld[0];
    }

    private static int allocate(ImageCompressionDialog imageCompressionDialog, QDRect area, int flags) throws QTException {
        int[] gworld = new int[1];
        StdQTException.checkError(SCNewGWorld(QTObject.ID(imageCompressionDialog), gworld, area != null ? area.getRect() : null, flags));
        if (gworld[0] == 0)
            throw new StdQTException(-50);
        return gworld[0];
    }

    private static int allocate(ImageDescription imageDescription, int flags) throws QTException {
        int[] gworld = new int[1];
        StdQTException.checkError(NewImageGWorld(gworld, QTObject.ID(imageDescription), flags));
        return gworld[0];
    }

    /**
     * This method should be used when a number of drawing methods need to
     * be invoked on this QDGraphics object. It will establish the required
     * global state for these methods to be called efficiently.
     *
     * @param d the QDDrawer object that will do the drawing
     * @throws quicktime.QTException
     */
    public void beginDraw(QDDrawer d) throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            d.draw(this);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * This returns true if the QDGraphics is an offscreen GWorld or an onscreen
     * CGrafPort.
     *
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
     *
     * @return a PixMap object
     * @see "quicktime.std.qd.PixMap"
     */
    public PixMap getPixMap() {
        return PixMap.fromQDGraphics(this);
    }

    /**
     * The port rectangle that defines a subset of the pixel map to be used
     * for drawing.
     *
     * @return aRect object which defines the boundaries of the port rectangle
     */
    public QDRect getPortRect() {
        if (QTSession.isCurrentOS(4)) {
            QDRect portRect = new QDRect();
            GetPortBounds(_ID(), portRect.getBytes());
            return portRect;
        }
        Object rectBytes = new byte[8];
        getBytesAt(16, 8, (byte[]) rectBytes, 0);
        return QDRect.fromArray((byte[]) rectBytes, 8);
    }

    /**
     * Creates an 80-by-80 pixel thumbnail picture from the pixmap associated with this QDGraphics.
     *
     * @param src        a QDRect object defining the portion of the image to use for the thumbnail
     * @param colorDepth specifies the depth at which the image is likely to be viewed
     * @return a Pict object representing the thumbnail
     * @throws quicktime.QTException
     * @see "QuickTime::MakeThumbnailFromPixMap()"
     */
    public Pict makeThumbnail(QDRect src, int colorDepth) throws QTException {
        return Pict.thumbnailFromQDGraphics(this, src, colorDepth);
    }

    /**
     * The port rectangle that defines a subset of the pixel map to be used
     * for drawing.
     *
     * @return aRect object which defines the boundaries of the port rectangle
     */
    public QDRect getBounds() {
        QDRect bounds = new QDRect();
        if (QTSession.isCurrentOS(4)) {
            GetPortBounds(_ID(), bounds.getBytes());
            return bounds;
        }
        return getPortRect();
    }

    /**
     * This method returns intersection of visRgn and clipRgn of the port.
     * It is implemented on the low level of native calls to avoid extra
     * load on garbage collector
     *
     * @return Region object which defines the intersection of visible and clipping regions
     * @throws quicktime.QTException
     */
    public Region getVisClipRgn() throws QTException {
        Region dst = new Region();
        if (QTSession.isCurrentOS(4)) {
            Region arcAngle = new Region();
            Region region3 = new Region();
            GetPortClipRegion(_ID(), QTObject.ID(arcAngle));
            GetPortVisibleRegion(_ID(), QTObject.ID(region3));
            SectRgn(QTObject.ID(arcAngle), QTObject.ID(region3), QTObject.ID(dst));
        } else {
            int i = getIntFromPointer(_ID(), 28);
            int j = getIntFromPointer(_ID(), 24);
            SectRgn(i, j, QTObject.ID(dst));
        }
        return dst;
    }

    /**
     * This method returns the clip Region of the port.
     *
     * @return Region object which defines the clip region
     * @throws quicktime.QTException
     * @see "QuickDraw::GetClip()"
     */
    public Region getClip() throws QTException {
        Region rgn = new Region();
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            GetClip(QTObject.ID(rgn));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return rgn;
    }

    /**
     * This method sets the clip Region of the port.
     *
     * @param rgn Region object which defines the new clip region
     * @see "QuickDraw::SetClip()"
     */
    public void setClip(Region rgn) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            SetClip(QTObject.ID(rgn));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * To change the clipping region of this QDGraphics, use the ClipRect procedure.
     * This method will set the clip to the current port rect of the QDGraphics
     *
     * @see "QuickDraw::ClipRect()"
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
     *
     * @param r the rect that will become the new clip rgn
     */
    public void clipRect(QDRect r) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            ClipRect(r.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Returns the current background color of the QDGraphic.
     *
     * @return QDColor
     * @see "QuickDraw::GetBackColor()"
     */
    public QDColor getBackColor() {
        byte[] rgb = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetBackColor(rgb);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return QDColor.fromArray(rgb, 6);
    }

    /**
     * Sets the current background color of the
     * QDGraphic.
     *
     * @param bColor QDColor
     * @see "QuickDraw::RGBBackColor()"
     */
    public void setBackColor(QDColor bColor) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            RGBBackColor(bColor.getRGBColor());
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
     *
     * @param area       The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle   The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::FrameArc()"
     */
    public void frameArc(QDRect area, int startAngle, int arcAngle) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FrameArc(area.getRect(), (short) startAngle, (short) arcAngle);
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
     *
     * @param area       The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle   The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PaintArc()"
     */
    public void paintArc(QDRect area, int startAngle, int arcAngle) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintArc(area.getRect(), (short) startAngle, (short) arcAngle);
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
     *
     * @param area       The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle   The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::EraseArc()"
     */
    public void eraseArc(QDRect area, int startAngle, int arcAngle) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseArc(area.getRect(), (short) startAngle, (short) arcAngle);
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
     *
     * @param area       The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle   The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::InvertArc()"
     */
    public void invertArc(QDRect area, int startAngle, int arcAngle) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertArc(area.getRect(), (short) startAngle, (short) arcAngle);
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
     *
     * @param area       The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth  The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::FrameRoundRect()"
     */
    public void frameRoundRect(QDRect area, int ovalWidth, int ovalHeight) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FrameRoundRect(area.getRect(), (short) ovalWidth, (short) ovalHeight);
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
     *
     * @param area       The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth  The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PaintRoundRect()"
     */
    public void paintRoundRect(QDRect area, int ovalWidth, int ovalHeight) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintRoundRect(area.getRect(), (short) ovalWidth, (short) ovalHeight);
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
     *
     * @param area       The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth  The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::EraseRoundRect()"
     */
    public void eraseRoundRect(QDRect area, int ovalWidth, int ovalHeight) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseRoundRect(area.getRect(), (short) ovalWidth, (short) ovalHeight);
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
     *
     * @param area       The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth  The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::InvertRoundRect()"
     */
    public void invertRoundRect(QDRect area, int ovalWidth, int ovalHeight) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertRoundRect(area.getRect(), (short) ovalWidth, (short) ovalHeight);
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
     *
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::FrameOval()"
     */
    public void frameOval(QDRect area) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FrameOval(area.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen mode for the current graphics port,
     * the PaintOval procedure draws the interior of an oval just inside the bounding rectangle
     * that you specify in the area parameter. The pen location does not change.
     *
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PaintOval()"
     */
    public void paintOval(QDRect area) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintOval(area.getRect());
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
     *
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::EraseOval()"
     */
    public void eraseOval(QDRect area) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseOval(area.getRect());
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
     *
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::InvertOval()"
     */
    public void invertOval(QDRect area) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertOval(area.getRect());
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
     *
     * @param poly Polygon to frame.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::FramePoly()"
     */
    public void framePoly(Polygon poly) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            FramePoly(QTObject.ID(poly));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen pattern and pattern mode for the current graphics port,
     * PaintPoly draws the interior of a polygon
     * passed in the poly parameter. The pen location does not change.
     *
     * @param poly Polygon to paint.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PaintPoly()"
     */
    public void paintPoly(Polygon poly) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintPoly(QTObject.ID(poly));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the patCopy pattern mode, ErasePoly draws the
     * interior of the polygon passed in the poly parameter
     * with the background pattern for the current graphics port.
     *
     * @param poly Polygon to erase.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::ErasePoly()"
     */
    public void erasePoly(Polygon poly) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            ErasePoly(QTObject.ID(poly));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * invertPoly inverts the pixels enclosed by the polygon passed in the poly parameter.
     * Every white pixel becomes black and every black pixel becomes white.
     * This procedure leaves the location of the graphics pen unchanged.
     *
     * @param poly Polygon to erase.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::InvertPoly()"
     */
    public void invertPoly(Polygon poly) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            InvertPoly(QTObject.ID(poly));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Using the pen size of the graphics pen for the current graphics port,
     * the FrameRect procedure draws an outline just inside the rectangle that you specify in the area parameter.
     * The outline is as wide as the pen width and as tall as the pen height. The pen location does not change.
     *
     * @param area the portion of the QDGraphic port to frame
     *             or null for entire bounds
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::FrameRect()"
     */
    public void frameRect(QDRect area) throws QDException {
        if (area == null)
            area = getPortRect();
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                FrameRect(area.getRect());
            } else
                MacFrameRect(area.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Erases the specified rect with the current back color.
     *
     * @param area the portion of the QDGraphic port to erase
     *             or null for entire bounds
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::EraseRect()"
     */
    public void eraseRect(QDRect area) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseRect(area.getRect());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Erases the specified region with the current back color.
     *
     * @param area the portion of the QDGraphic to erase
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::EraseRgn()"
     */
    public void eraseRgn(Region area) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            EraseRgn(QTObject.ID(area));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Returns the RGB color for the pixel at the location you
     * specify in the h and v parameters.
     *
     * @param h The horizontal coordinate of the point at the upper-left corner of the pixel.
     * @param v The vertical coordinate of the point at the upper-left corner of the pixel.
     * @return The QDColor for the pixel.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::GetCPixel()"
     */
    public QDColor getCPixel(int h, int v) throws QDException {
        byte[] rgb = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetCPixel((short) h, (short) v, rgb);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return QDColor.fromArray(rgb, 6);
    }

    /**
     * For the pixel at the location you specify in the h and v parameters,
     * the setCPixel procedure sets a pixel value that most closely matches
     * the RGB color that you specify in the cPix parameter.
     * On an indexed color system, the SetCPixel procedure sets the pixel
     * value to the index of the best-matching color in the current deviceÕs CLUT.
     * In a direct environment, the SetCPixel procedure sets the pixel value to a
     * 16-bit or 32-bit direct pixel value.
     *
     * @param h The horizontal coordinate of the point at the upper-left corner of the pixel.
     * @param v The vertical coordinate of the point at the upper-left corner of the pixel.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::SetCPixel()"
     */
    public void setCPixel(int h, int v, QDColor color) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            SetCPixel((short) h, (short) v, color.getRGBColor());
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
     *
     * @param width  The pen width, as an integer from 0 to 32,767. If you set the value to 0, the pen does not draw. Values less than 0 are undefined.
     * @param height The pen height, as an integer from 0 to 32,767. If you set the value to 0, the pen does not draw. Values less than 0 are undefined.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PenSize()"
     */
    public void penSize(int width, int height) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PenSize((short) width, (short) height);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The PenNormal procedure restores the size, of the graphics pen
     * in the current graphics port to their initial values:
     * a size of 1 pixel by 1 pixel. The pen location does not change.
     *
     * @see "QuickDraw::PenNormal()"
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
     *
     * @return QDColor
     * @see "QuickDraw::GetForeColor()"
     */
    public QDColor getForeColor() {
        byte[] rgb = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetForeColor(rgb);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        return QDColor.fromArray(rgb, 6);
    }

    /**
     * Sets the current foreground color of the QDGraphics.
     *
     * @param fColor QDColor
     * @see "QuickDraw::RGBForeColor()"
     */
    public void setForeColor(QDColor fColor) {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            RGBForeColor(fColor.getRGBColor());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Paints the specified rect with the current foreground color.
     *
     * @param area the portion of the QDGraphic port to paint
     *             or null for entire bounds
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PaintRect()"
     */
    public void paintRect(QDRect area) throws QDException {
        if (area == null) {
            area = getPortRect();
        }
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            PaintRect(area.getRect());
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
     *
     * @param h The horizontal coordinate of the graphics penÕs new location.
     * @param v The vertical coordinate of the graphics penÕs new location.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::LineTo()"
     * @see "QuickDraw::MacLineTo()"
     */
    public void lineTo(int h, int v) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                LineTo((short) h, (short) v);
            } else
                MacLineTo((short) h, (short) v);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Starting at the current location of the graphics pen, the Line procedure
     * draws a line the horizontal distance that you specify in the h
     * parameter and the vertical distance that you specify in the v parameter.
     *
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::Line()"
     */
    public void line(int h, int v) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            Line((short) h, (short) v);
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
     *
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::MoveTo()"
     */
    public void moveTo(int h, int v) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            MoveTo((short) h, (short) v);
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
     *
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::Move()"
     */
    public void move(int h, int v) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            Move((short) h, (short) v);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Paints the specified region with the current foreground color.
     *
     * @param area the portion of the QDGraphic port to paint
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::PaintRgn()"
     * @see "QuickDraw::MacPaintRgn()"
     */
    public void paintRgn(Region area) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                PaintRgn(QTObject.ID(area));
            } else
                MacPaintRgn(QTObject.ID(area));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * Draws an outline just inside the region you pass in the area parameter.
     * The outline never goes outside the region boundary. The pen location does not change.
     *
     * @param area the portion of the QDGraphic port to paint
     * @throws quicktime.qd.QDException
     * @see "QuickDraw::FrameRgn()"
     * @see "QuickDraw::MacFrameRgn()"
     */
    public void frameRgn(Region area) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                FrameRgn(QTObject.ID(area));
            } else
                MacFrameRgn(QTObject.ID(area));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The TextFont procedure sets the font of the QDGraphics port in which the text is to be rendered.
     *
     * @param fontNumber The font family ID (obtained from an getFNum call).
     * @see "QuickDraw::TextFont()"
     * @see "getFNum"
     */
    public void textFont(int fontNumber) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextFont((short) fontNumber);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextFont method returns the font family ID of the current font in this QDGraphics port.
     *
     * @return The font family ID of the current font.
     * @see "QuickDraw::TextFont()"
     * @see "getFNum"
     */
    public int getTextFont() {
        return getShortAt(68);
    }

    /**
     * The TextFace procedure sets the style of the font in which the text is to be drawn in this QDGraphics port.
     *
     * @param face The style for text to be drawn
     * @see "QuickDraw::TextFace()"
     */
    public void textFace(int face) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextFace((short) face);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextFace method returns the style of the current font in this QDGraphics port.
     *
     * @return The style of the current font.
     * @see "QuickDraw::TextFace()"
     */
    public int getTextFace() {
        return QTUtils.UByte2Int(getByteAt(70));
    }

    /**
     * The TextMode procedure sets the transfer mode for drawing text in this QDGraphics port.
     *
     * @param mode The transfer mode to be used in drawing text.  Possible values are: srcCopy, srcOr, srcXOr, srcBic, etc.
     * @see "QuickDraw::TextMode()"
     * @see "quicktime.QTConstants#srcCopy"
     */
    public void textMode(int mode) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextMode((short) mode);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextMode method returns the current transfer mode for drawing text in this QDGraphics port.
     *
     * @return The transfer mode used in drawing text.
     * @see "QuickDraw::TextMode()"
     */
    public int getTextMode() {
        return getShortAt(72);
    }

    /**
     * The TextSize procedure sets the font size for text drawn in this QDGraphics port.
     *
     * @param size The font size in points.  If you specify 0, the system font size is used.
     * @see "QuickDraw::TextSize()"
     */
    public void textSize(int size) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            TextSize((short) size);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The getTextSize method returns the current font size for text drawn in this QDGraphics port.
     *
     * @return The font size in points.
     * @see "QuickDraw::TextSize()"
     */
    public int getTextSize() {
        return getShortAt(74);
    }

    /**
     * The DrawChar procedure draws the glyph for the specified character at the current pen location in this QDGraphics port.
     *
     * @param character The character code whose glyph is to be drawn.
     * @see "QuickDraw::DrawChar()"
     */
    public void drawChar(int character) {
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            DrawChar((short) character);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The DrawText procedure draws the specified text at the current pen location in this QDGraphics port.
     *
     * @param text      String containing the text to be drawn.
     * @param firstByte An offset from the start of the String (text) to the first byte of the text to be drawn.
     * @param byteCount The number of bytes of text to be drawn.
     * @see "QuickDraw::DrawText()"
     */
    public void drawText(String text, int firstByte, int byteCount) {
        byte[] bytes = text.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            if (i != 0) {
                DrawText(bytes, (short) firstByte, (short) byteCount);
                SetGWorld(savedPort[0], savedDevice[0]);
            } else {
                MacDrawText(bytes, (short) firstByte, (short) byteCount);
            }
        }
    }

    /**
     * The CharWidth function returns the width in pixels of the specified character in this QDGraphics port.
     *
     * @param character The character code whose width is to be measured.
     * @return The width of the specified character in pixels.
     * @see "QuickDraw::CharWidth()"
     */
    public int charWidth(int character) {
        int j = 0;
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            j = CharWidth((short) character);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * The TextWidth function returns the length in pixels of the specified text in this QDGraphics port.
     *
     * @param text      String containing the text to be measured.
     * @param firstByte An offset from the start of the String (text) to the first byte of the text to be measured.
     * @param byteCount The number of bytes of text to be measured.
     * @return The width of the specified text in pixels.
     * @see "QuickDraw::TextWidth()"
     */
    public int textWidth(String text, int firstByte, int byteCount) {
        int j = 0;
        byte[] bytes = text.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            j = TextWidth(bytes, (short) firstByte, (short) byteCount);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * The StdText procedure is a QuickDraw bottleneck routine that can be called directly to obtain scaled (or unscaled) text..
     *
     * @param count  The number of bytes of text to be drawn.
     * @param text   String containing the text to be drawn.
     * @param xScale Represents the scaling factor in the X dimension.
     * @param yScale Represents the scaling factor in the Y dimension.
     * @see "QuickDraw::StdText()"
     */
    public void drawTextScaled(int count, String text, float xScale, float yScale) {
        QDPoint point1 = new QDPoint((int) (xScale * 100.0F), (int) (yScale * 100.0F));
        QDPoint point2 = new QDPoint(100, 100);
        byte[] bytes = text.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            StdText((short) count, bytes, point1.getPoint(), point2.getPoint());
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The measureScaledText method (based on the QuickDraw function StdTxMeas) measures the width of scaled or unscaled text.
     *
     * @param byteCount The number of bytes of text to be measured.
     * @param text      String containing the text to be measured.
     * @param xScale    Represents the scaling factor in the X dimension.
     * @param yScale    Represents the scaling factor in the Y dimension.
     * @return The width of the specified text in pixels.
     * @see "QuickDraw::StdTxMeas()"
     */
    public int measureScaledText(int byteCount, String text, float xScale, float yScale) {
        int[] numer = {0};
        int[] denom = {0};
        QDPoint point1 = new QDPoint((int) (xScale * 100.0F), (int) (yScale * 100.0F));
        numer[0] = point1.getPoint();
        QDPoint point2 = new QDPoint(100, 100);
        denom[0] = point2.getPoint();
        byte[] bytes = text.getBytes();
        short[] info = new short[4];
        int j = 0;
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            GetFontInfo(info);
            j = StdTxMeas((short) byteCount, bytes, numer, denom, info);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * Print information about this object.
     *
     * @return a string representing this QDGraphics object
     */
    public String toString() {
        return super.toString() + "[PortRect=" + getPortRect() + ",isOffscreen=" + isOffscreen() + "]";
    }

    private static int GraphicsExportGetInputGWorld(int ge, int[] gworld) {
        return GraphicsExportManager.GraphicsExportGetInputGWorld(ge, gworld);
    }

    private static short QTGetPixelSize(int pixelFormat) {
        return ImageCompressionManager.QTGetPixelSize(pixelFormat);
    }

    private static int GraphicsImportGetGWorld(int gi, int[] port, int[] gdh) {
        return GraphicsImportManager.GraphicsImportGetGWorld(gi, port, gdh);
    }

    private static void GetMovieGWorld(int movie, int[] port, int[] gdh) {
        MovieToolbox.GetMovieGWorld(movie, port, gdh);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int MCGetControllerPort(int mc) {
        return MovieControllerManager.MCGetControllerPort(mc);
    }

    private static int SGGetGWorld(int sg, int[] port, int[] gdh) {
        return SequenceGrabberManager.SGGetGWorld(sg, port, gdh);
    }

    private static short GetCSequencePrevBuffer(int seqID, int[] gworld) {
        return ImageCompressionManager.GetCSequencePrevBuffer(seqID, gworld);
    }

    private static short GetDSequenceImageBuffer(int seqID, int[] gworld) {
        return ImageCompressionManager.GetDSequenceImageBuffer(seqID, gworld);
    }

    private static short GetDSequenceScreenBuffer(int seqID, int[] gworld) {
        return ImageCompressionManager.GetDSequenceScreenBuffer(seqID, gworld);
    }

    private static int GetNativeWindowPort(int nativeWindow) {
        return PlatformManager.GetNativeWindowPort(nativeWindow);
    }

    private static int GetGWorldPixMap(int gworld) {
        return QuickDrawManager.GetGWorldPixMap(gworld);
    }

    private static byte LockPixels(int pixMap) {
        return QuickDrawManager.LockPixels(pixMap);
    }

    private static short QTNewGWorld(int[] gworld, int startAngle, byte[] rect, int arcAngle, int gd, int flags) {
        return QuickDrawManager.QTNewGWorld(gworld, startAngle, rect, arcAngle, gd, flags);
    }

    private static int SCNewGWorld(int startAngle, int[] gworld, byte[] rect, int arcAngle) {
        return ImageCompressionManager.SCNewGWorld(startAngle, gworld, rect, arcAngle);
    }

    private static short NewImageGWorld(int[] gworld, int startAngle, int arcAngle) {
        return QuickDrawManager.NewImageGWorld(gworld, startAngle, arcAngle);
    }

    private static void GetBackColor(byte[] rgb) {
        QuickDrawManager.GetBackColor(rgb);
    }

    private static void RGBBackColor(byte[] rgb) {
        QuickDrawManager.RGBBackColor(rgb);
    }

    private static void EraseRect(byte[] r) {
        QuickDrawManager.EraseRect(r);
    }

    private static void FrameRect(byte[] r) {
        QuickDrawManager.FrameRect(r);
    }

    private static void MacFrameRect(byte[] r) {
        QuickDrawManager.MacFrameRect(r);
    }

    private static void EraseRgn(int rgn) {
        QuickDrawManager.EraseRgn(rgn);
    }

    private static void GetForeColor(byte[] rgb) {
        QuickDrawManager.GetForeColor(rgb);
    }

    private static void RGBForeColor(byte[] rgb) {
        QuickDrawManager.RGBForeColor(rgb);
    }

    private static void PaintRect(byte[] r) {
        QuickDrawManager.PaintRect(r);
    }

    private static void MacPaintRgn(int rgn) {
        QuickDrawManager.MacPaintRgn(rgn);
    }

    private static void MacFrameRgn(int rgn) {
        QuickDrawManager.MacFrameRgn(rgn);
    }

    private static void GetPort(int[] port) {
        QuickDrawManager.GetPort(port);
    }

    private static void SetGWorld(int startAngle, int arcAngle) {
        QuickDrawManager.SetGWorld(startAngle, arcAngle);
    }

    private static void PaintRgn(int rgn) {
        QuickDrawManager.PaintRgn(rgn);
    }

    private static void FrameRgn(int rgn) {
        QuickDrawManager.FrameRgn(rgn);
    }

    private static void CopyRgn(int startAngle, int arcAngle) {
        QuickDrawManager.CopyRgn(startAngle, arcAngle);
    }

    private static void SetRectRgn(int rgn, short left, short top, short right, short bottom) {
        QuickDrawManager.SetRectRgn(rgn, left, top, right, bottom);
    }

    private static void OffsetRgn(int rgn, short dh, short dv) {
        QuickDrawManager.OffsetRgn(rgn, dh, dv);
    }

    private static void UnionRgn(int startAngle, int arcAngle, int dst) {
        QuickDrawManager.UnionRgn(startAngle, arcAngle, dst);
    }

    private static void XorRgn(int startAngle, int arcAngle, int dst) {
        QuickDrawManager.XorRgn(startAngle, arcAngle, dst);
    }

    private static byte EqualRgn(int startAngle, int arcAngle) {
        return QuickDrawManager.EqualRgn(startAngle, arcAngle);
    }

    private static void SectRgn(int startAngle, int arcAngle, int dst) {
        QuickDrawManager.SectRgn(startAngle, arcAngle, dst);
    }

    private static void GetClip(int rgn) {
        QuickDrawManager.GetClip(rgn);
    }

    private static void SetClip(int rgn) {
        QuickDrawManager.SetClip(rgn);
    }

    private static void ClipRect(byte[] r) {
        QuickDrawManager.ClipRect(r);
    }

    private static void ClipRect(int rectPtr) {
        QuickDrawManager.ClipRect(rectPtr);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }

    private static void Move(short dh, short dv) {
        QuickDrawManager.Move(dh, dv);
    }

    private static void MoveTo(short h, short v) {
        QuickDrawManager.MoveTo(h, v);
    }

    private static void MacLineTo(short h, short v) {
        QuickDrawManager.MacLineTo(h, v);
    }

    private static void LineTo(short h, short v) {
        QuickDrawManager.LineTo(h, v);
    }

    private static void Line(short dh, short dv) {
        QuickDrawManager.Line(dh, dv);
    }

    private static void SetCPixel(short h, short v, byte[] rgb) {
        QuickDrawManager.SetCPixel(h, v, rgb);
    }

    private static void GetCPixel(short h, short v, byte[] rgb) {
        QuickDrawManager.GetCPixel(h, v, rgb);
    }

    private static void PenSize(short width, short height) {
        QuickDrawManager.PenSize(width, height);
    }

    private static void PenNormal() {
        QuickDrawManager.PenNormal();
    }

    private static void FrameArc(byte[] r, short start, short angle) {
        QuickDrawManager.FrameArc(r, start, angle);
    }

    private static void PaintArc(byte[] r, short start, short angle) {
        QuickDrawManager.PaintArc(r, start, angle);
    }

    private static void EraseArc(byte[] r, short start, short angle) {
        QuickDrawManager.EraseArc(r, start, angle);
    }

    private static void InvertArc(byte[] r, short start, short angle) {
        QuickDrawManager.InvertArc(r, start, angle);
    }

    private static void FrameRoundRect(byte[] r, short ow, short oh) {
        QuickDrawManager.FrameRoundRect(r, ow, oh);
    }

    private static void PaintRoundRect(byte[] r, short ow, short oh) {
        QuickDrawManager.PaintRoundRect(r, ow, oh);
    }

    private static void EraseRoundRect(byte[] r, short ow, short oh) {
        QuickDrawManager.EraseRoundRect(r, ow, oh);
    }

    private static void InvertRoundRect(byte[] r, short ow, short oh) {
        QuickDrawManager.InvertRoundRect(r, ow, oh);
    }

    private static void FrameOval(byte[] r) {
        QuickDrawManager.FrameOval(r);
    }

    private static void PaintOval(byte[] r) {
        QuickDrawManager.PaintOval(r);
    }

    private static void EraseOval(byte[] r) {
        QuickDrawManager.EraseOval(r);
    }

    private static void InvertOval(byte[] r) {
        QuickDrawManager.InvertOval(r);
    }

    private static void FramePoly(int poly) {
        QuickDrawManager.FramePoly(poly);
    }

    private static void PaintPoly(int poly) {
        QuickDrawManager.PaintPoly(poly);
    }

    private static void ErasePoly(int poly) {
        QuickDrawManager.ErasePoly(poly);
    }

    private static void InvertPoly(int poly) {
        QuickDrawManager.InvertPoly(poly);
    }

    private static short getShortFromPointer(int startAngle, int arcAngle) {
        return MemoryManager.getShortFromPointer(startAngle, arcAngle);
    }

    private static void setIntInPointer(int startAngle, int arcAngle, int value) {
        MemoryManager.setIntInPointer(startAngle, arcAngle, value);
    }

    private static int getIntFromPointer(int startAngle, int arcAngle) {
        return MemoryManager.getIntFromPointer(startAngle, arcAngle);
    }

    private static void TextFont(short font) {
        QuickDrawManager.TextFont(font);
    }

    private static void TextFace(short face) {
        QuickDrawManager.TextFace(face);
    }

    private static void TextMode(short mode) {
        QuickDrawManager.TextMode(mode);
    }

    private static void TextSize(short size) {
        QuickDrawManager.TextSize(size);
    }

    private static void DrawChar(short ch) {
        QuickDrawManager.DrawChar(ch);
    }

    private static void MacDrawText(byte[] text, short firstByte, short byteCount) {
        QuickDrawManager.MacDrawText(text, firstByte, byteCount);
    }

    private static void DrawText(byte[] text, short firstByte, short byteCount) {
        QuickDrawManager.DrawText(text, firstByte, byteCount);
    }

    private static short CharWidth(short ch) {
        return QuickDrawManager.CharWidth(ch);
    }

    private static short TextWidth(byte[] text, short firstByte, short byteCount) {
        return QuickDrawManager.TextWidth(text, firstByte, byteCount);
    }

    private static void MeasureText(short count, byte[] text, int[] locs) {
        QuickDrawManager.MeasureText(count, text, locs);
    }

    private static void StdText(short count, byte[] text, int startAngle, int arcAngle) {
        QuickDrawManager.StdText(count, text, startAngle, arcAngle);
    }

    private static short StdTxMeas(short count, byte[] text, int[] numer, int[] denom, short[] info) {
        return QuickDrawManager.StdTxMeas(count, text, numer, denom, info);
    }

    private static void GetFontInfo(short[] info) {
        QuickDrawManager.GetFontInfo(info);
    }

    private static byte IsPortOffscreen(int port) {
        return QuickDrawManager.IsPortOffscreen(port);
    }

    private static int GetPortBounds(int port, byte[] rect) {
        return QuickDrawManager.GetPortBounds(port, rect);
    }

    private static int GetPortClipRegion(int startAngle, int arcAngle) {
        return QuickDrawManager.GetPortClipRegion(startAngle, arcAngle);
    }

    private static int GetPortVisibleRegion(int startAngle, int arcAngle) {
        return QuickDrawManager.GetPortVisibleRegion(startAngle, arcAngle);
    }

    private static short QTSPresGetGWorld(int startAngle, int arcAngle, int[] gWorld, int[] gdHandle) {
        return StreamingManager.QTSPresGetGWorld(startAngle, arcAngle, gWorld, gdHandle);
    }
}
