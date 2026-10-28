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
        try {
            scratch = new QDGraphics(kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
            validScratch = new QDGraphics(kDefaultPixelFormat, new QDRect(1, 1), null, GDevice.getMain(), 2);
        } catch (QTException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     * @see "QuickTime::GraphicsExportGetInputGWorld"
     * @param ge the GraphicsExporter
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsExporter#getInputGWorld()
     */
    public static QDGraphics fromGraphicsExporter(GraphicsExporter ge) throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GraphicsExportGetInputGWorld(QTObject.ID(ge), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], ge);
    }

    /**
     * Returns the current QDGraphics.
     * @see "QuickTime::GetPort"
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
     * @see "QuickTime::QTGetPixelSize"
     * @return pixel size
     * @throws quicktime.std.StdQTException
     */
    public static int getPixelSize(int paramInt) throws StdQTException {
        return QTGetPixelSize(paramInt);
    }

    /**
     * Call this method to obtain the QDGraphics of the object.
     * @see "QuickTime::GraphicsImportGetGWorld"
     * @param gi the GraphicsImporter
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.GraphicsImporter#getGWorld()
     */
    public static QDGraphics fromGraphicsImporter(GraphicsImporter gi) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = GraphicsImportGetGWorld(QTObject.ID(gi), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], gi);
    }

    /**
     * Your application can determine a movie's graphics world by calling the getGWorld method.
     * @see "QuickTime::GetMovieGWorld()"
     * @param m the Movie
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.movies.Movie#getGWorld()
     */
    public static QDGraphics fromMovie(Movie m) throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            GetMovieGWorld(QTObject.ID(m), arrayOfInt, null);
            StdQTException.checkError(GetMoviesError());
            return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], m);
        }
    }

    /**
     * Your application can retrieve a Presentation's graphics world by calling the getGWorld method.
     * @see "QuickTime::QTSPresGetGWorld()"
     * @param p the Presentation
     * @param s Stream
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     * @see quicktime.streaming.Presentation#getGWorld()
     */
    public static QDGraphics fromPresentation(Presentation p, Stream s) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetGWorld(QTObject.ID(p), QTObject.ID(s), arrayOfInt1, arrayOfInt2));
            return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], p);
        }
    }

    /**
     * This method returns a movie controller's color graphics port.
     * @see "QuickTime::MCGetControllerPort()"
     * @param mc the MovieController
     * @return A QDGraphics object representing the controller's graf port.
     * @throws quicktime.std.StdQTException
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
     * @see "QuickTime::SGGetGWorld"
     * @param sg the SequenceGrabber
     * @return The current graf port of the sequence grabber.
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.sg.SequenceGrabber#getGWorld()
     */
    public static QDGraphics fromSequenceGrabber(SequenceGrabber sg) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = SGGetGWorld(QTObject.ID(sg), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], sg);
    }

    /**
     * This method determines the location of the previous image buffer allocated
     * by the compressor.
     * @see "QuickTime::GetCSequencePrevBuffer"
     * @param c the CSequence
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.CSequence#prevBuffer()
     */
    public static QDGraphics fromCSequence(CSequence c) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequencePrevBuffer(QTObject.ID(c), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], c);
    }

    /**
     * This method helps you determine the location of the offscreen image buffer
     * allocated by the decompressor.
     * @see "QuickTime::GetDSequenceImageBuffer"
     * @param d the DSequence
     * @return a QDGraphics object which is the graphics world for the image buffer
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.DSequence#getImageBuffer()
     */
    public static QDGraphics fromDSequenceImage(DSequence d) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetDSequenceImageBuffer(QTObject.ID(d), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], d);
    }

    /**
     * This method enables you to determine the location of the offscreen buffer
     * allocated by the decompressor.
     * @see "QuickTime::GetDSequenceScreenBuffer"
     * @param d the DSequence
     * @return a QDGraphics object which is the graphics world for the screen buffer
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.image.DSequence#getScreenBuffer()
     */
    public static QDGraphics fromDSequenceScreen(DSequence d) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetDSequenceScreenBuffer(QTObject.ID(d), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], d);
    }

    /**
     * This will return a QDGraphics object from a native graphics object
     * @see "QuickTime::GetNativeWindowPort"
     */
    public static QDGraphics fromNativeGraphics(WinNativeGraphics paramWinNativeGraphics) throws NativeGraphicsException {
        if (paramWinNativeGraphics.hWnd == 0)
            throw new NativeGraphicsException("invalid HWnd");
        int i = GetNativeWindowPort(paramWinNativeGraphics.hWnd);
        if (i == 0)
            throw new NativeGraphicsException("invalid Port");
        return new QDGraphics(i, paramWinNativeGraphics);
    }

    /**
     * This will return a QDGraphics object from a native graphics object
     */
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
     * @see "QuickTime::SCNewGWorld()"
     * @param rp the boundaries of the graphics world.
     * @param flags flags as specified to NewGWorld.
     * {@link quicktime.std.qtcomponents.ImageCompressionDialog#newGWorld(quicktime.qd.QDRect, int)}
     */
    public QDGraphics(ImageCompressionDialog paramImageCompressionDialog, QDRect area, int paramInt) throws QTException {
        this(allocate(paramImageCompressionDialog, area, paramInt));
    }

    /**
     * Creates an offscreen QDGraphics object from the width, height, depth, and color table
     * of a specified image description structure.
     * @see "QuickTime::NewImageGWorld"
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
     * @see "QuickTime::QTNewGWorld"
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     */
    public QDGraphics(QDRect bounds) throws QTException {
        this(kDefaultPixelFormat, bounds, null, null, 4096);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     * @see "QuickTime::QTNewGWorld"
     * @param pixelFormat the format of the pixels.
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     */
    public QDGraphics(int pixelFormat, QDRect bounds) throws QTException {
        this(pixelFormat, bounds, null, null, 4096);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     * @see "QuickTime::QTNewGWorld"
     * @param pixelFormat the format of the pixels.
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @param flags options available to your application. See GWorld flags above
     */
    public QDGraphics(int pixelFormat, QDRect bounds, int flags) throws QTException {
        this(pixelFormat, bounds, null, null, flags);
    }

    /**
     * An offscreen graphics world in Color QuickDraw contains a GWorld - and
     * its handles to associated PixMap and ColorTables - that describe an offscreen
     * graphics port. By default the pixels are locked.
     * @see "QuickTime::QTNewGWorld"
     * @param pixelFormat the format of the pixels.
     * @param bounds the boundary rectangle and port rectangle for the offscreen
     *                    pixel map
     * @param cTable a ColorTable
     * @param aGDevice a GDevice that should only be supplied if you set the noNewDevice
     *  flag, otherwise it is just ignored and can be passed null.
     * @param flags options available to your application. See GWorld flags above
     */
    public QDGraphics(int pixelFormat, QDRect bounds, ColorTable cTable, GDevice aGDevice, int flags) throws QTException {
        this(allocate(pixelFormat, bounds, cTable, aGDevice, flags));
    }

    private static int allocate(int startAngle, QDRect area, ColorTable paramColorTable, GDevice paramGDevice, int arcAngle) throws QDException {
        int[] arrayOfInt = { 0 };
        int i = QTNewGWorld(arrayOfInt, startAngle, area.getRect(), QTObject.ID(paramColorTable), QTObject.ID(paramGDevice), arcAngle);
        QDException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(ImageCompressionDialog paramImageCompressionDialog, QDRect area, int paramInt) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(SCNewGWorld(QTObject.ID(paramImageCompressionDialog), arrayOfInt, area != null ? area.getRect() : null, paramInt));
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
            QDRect localObject = new QDRect();
            GetPortBounds(_ID(), localObject.getBytes());
            return localObject;
        }
        Object localObject = new byte[8];
        getBytesAt(16, 8, (byte[]) localObject, 0);
        return QDRect.fromArray((byte[]) localObject, 8);
    }

    /**
     * Creates an 80-by-80 pixel thumbnail picture from the pixmap associated with this QDGraphics.
     * @see "QuickTime::MakeThumbnailFromPixMap()"
     * @param src a QDRect object defining the portion of the image to use for the thumbnail
     * @param colorDepth specifies the depth at which the image is likely to be viewed
     * @return a Pict object representing the thumbnail
     * @throws quicktime.QTException
     */
    public Pict makeThumbnail(QDRect src, int colorDepth) throws QTException {
        return Pict.thumbnailFromQDGraphics(this, src, colorDepth);
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
     * @throws quicktime.QTException
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
     * @see "QuickDraw::GetClip()"
     * @return Region object which defines the clip region
     * @throws quicktime.QTException
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
     * @see "QuickDraw::SetClip()"
     * @param rgn Region object which defines the new clip region
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
     * @see "QuickDraw::GetBackColor()"
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
     * @see "QuickDraw::RGBBackColor()"
     * @param bColor QDColor
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
     * @see "QuickDraw::FrameArc()"
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::PaintArc()"
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::EraseArc()"
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::InvertArc()"
     * @param area The rectangle that defines an ovalÕs boundaries.
     * @param startAngle The angle indicating the start of the arc.
     * @param arcAngle The angle indicating the arcÕs extent.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::FrameRoundRect()"
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::PaintRoundRect()"
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::EraseRoundRect()"
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::InvertRoundRect()"
     * @param area The rectangle that defines the rounded rectangleÕs boundaries.
     * @param ovalWidth The width of the oval defining the rounded corner.
     * @param ovalHeight The height of the oval defining the rounded corner.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::FrameOval()"
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::PaintOval()"
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::EraseOval()"
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::InvertOval()"
     * @param area The rectangle that defines the ovalÕs boundary.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::FramePoly()"
     * @param poly Polygon to frame.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::PaintPoly()"
     * @param poly Polygon to paint.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::ErasePoly()"
     * @param poly Polygon to erase.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::InvertPoly()"
     * @param poly Polygon to erase.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::FrameRect()"
     * @param area the portion of the QDGraphic port to frame
     *  or null for entire bounds
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::EraseRect()"
     * @param area the portion of the QDGraphic port to erase
     *  or null for entire bounds
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::EraseRgn()"
     * @param area the portion of the QDGraphic to erase
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::GetCPixel()"
     * @param h The horizontal coordinate of the point at the upper-left corner of the pixel.
     * @param v The vertical coordinate of the point at the upper-left corner of the pixel.
     * @return The QDColor for the pixel.
     * @throws quicktime.qd.QDException
     */
    public QDColor getCPixel(int h, int v) throws QDException {
        byte[] arrayOfByte = new byte[6];
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            GetCPixel((short) h, (short) v, arrayOfByte);
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
     * @see "QuickDraw::SetCPixel()"
     * @param h The horizontal coordinate of the point at the upper-left corner of the pixel.
     * @param v The vertical coordinate of the point at the upper-left corner of the pixel.
     * @throws quicktime.qd.QDException
     */
    public void setCPixel(int startAngle, int arcAngle, QDColor paramQDColor) throws QDException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(_ID(), 0);
            SetCPixel((short) startAngle, (short) arcAngle, paramQDColor.getRGBColor());
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
     * @see "QuickDraw::PenSize()"
     * @param width The pen width, as an integer from 0 to 32,767. If you set the value to 0, the pen does not draw. Values less than 0 are undefined.
     * @param height The pen height, as an integer from 0 to 32,767. If you set the value to 0, the pen does not draw. Values less than 0 are undefined.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::GetForeColor()"
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
     * @see "QuickDraw::RGBForeColor()"
     * @param fColor QDColor
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
     * @see "QuickDraw::PaintRect()"
     * @param area the portion of the QDGraphic port to paint
     *  or null for entire bounds
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::LineTo()"
     * @see "QuickDraw::MacLineTo()"
     * @param h The horizontal coordinate of the graphics penÕs new location.
     * @param v The vertical coordinate of the graphics penÕs new location.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::Line()"
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::MoveTo()"
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::Move()"
     * @param h The horizontal distance of the graphics penÕs movement.
     * @param v The vertical distance of the graphics penÕs movement.
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::PaintRgn()"
     * @see "QuickDraw::MacPaintRgn()"
     * @param area the portion of the QDGraphic port to paint
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::FrameRgn()"
     * @see "QuickDraw::MacFrameRgn()"
     * @param area the portion of the QDGraphic port to paint
     * @throws quicktime.qd.QDException
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
     * @see "QuickDraw::TextFont()"
     * @param fontNumber The font family ID (obtained from an getFNum call).
     * @see getFNum
     */
    public final void textFont(int fontNumber) {
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
     * @see "QuickDraw::TextFont()"
     * @return The font family ID of the current font.
     * @see getFNum
     */
    public int getTextFont() {
        return getShortAt(68);
    }

    /**
     * The TextFace procedure sets the style of the font in which the text is to be drawn in this QDGraphics port.
     * @see "QuickDraw::TextFace()"
     * @param face The style for text to be drawn
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
     * @see "QuickDraw::TextFace()"
     * @return The style of the current font.
     */
    public int getTextFace() {
        return QTUtils.UByte2Int(getByteAt(70));
    }

    /**
     * The TextMode procedure sets the transfer mode for drawing text in this QDGraphics port.
     * @see "QuickDraw::TextMode()"
     * @param mode The transfer mode to be used in drawing text.  Possible values are: srcCopy, srcOr, srcXOr, srcBic, etc.
     * @see quicktime.QTConstants#srcCopy
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
     * @see "QuickDraw::TextMode()"
     * @return The transfer mode used in drawing text.
     */
    public int getTextMode() {
        return getShortAt(72);
    }

    /**
     * The TextSize procedure sets the font size for text drawn in this QDGraphics port.
     * @see "QuickDraw::TextSize()"
     * @param size The font size in points.  If you specify 0, the system font size is used.
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
     * @see "QuickDraw::TextSize()"
     * @return The font size in points.
     */
    public int getTextSize() {
        return getShortAt(74);
    }

    /**
     * The DrawChar procedure draws the glyph for the specified character at the current pen location in this QDGraphics port.
     * @see "QuickDraw::DrawChar()"
     * @param character The character code whose glyph is to be drawn.
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
     * @see "QuickDraw::DrawText()"
     * @param text String containing the text to be drawn.
     * @param firstByte An offset from the start of the String (text) to the first byte of the text to be drawn.
     * @param byteCount The number of bytes of text to be drawn.
     */
    public void drawText(String text, int firstByte, int byteCount) {
        byte[] arrayOfByte = text.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            if (i != 0) {
                DrawText(arrayOfByte, (short) firstByte, (short) byteCount);
                SetGWorld(savedPort[0], savedDevice[0]);
            } else {
                MacDrawText(arrayOfByte, (short) firstByte, (short) byteCount);
            }
        }
    }

    /**
     * The CharWidth function returns the width in pixels of the specified character in this QDGraphics port.
     * @see "QuickDraw::CharWidth()"
     * @param character The character code whose width is to be measured.
     * @return The width of the specified character in pixels.
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
     * @see "QuickDraw::TextWidth()"
     * @param text String containing the text to be measured.
     * @param firstByte An offset from the start of the String (text) to the first byte of the text to be measured.
     * @param byteCount The number of bytes of text to be measured.
     * @return The width of the specified text in pixels.
     */
    public int textWidth(String text, int firstByte, int byteCount) {
        int j = 0;
        byte[] arrayOfByte = text.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            j = TextWidth(arrayOfByte, (short) firstByte, (short) byteCount);
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        return j;
    }

    /**
     * The StdText procedure is a QuickDraw bottleneck routine that can be called directly to obtain scaled (or unscaled) text..
     * @see "QuickDraw::StdText()"
     * @param count The number of bytes of text to be drawn.
     * @param text String containing the text to be drawn.
     * @param xScale Represents the scaling factor in the X dimension.
     * @param yScale Represents the scaling factor in the Y dimension.
     */
    public void drawTextScaled(int count, String text, float xScale, float yScale) {
        QDPoint localQDPoint1 = new QDPoint((int) (xScale * 100.0F), (int) (yScale * 100.0F));
        QDPoint localQDPoint2 = new QDPoint(100, 100);
        byte[] arrayOfByte = text.getBytes();
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            StdText((short) count, arrayOfByte, localQDPoint1.getPoint(), localQDPoint2.getPoint());
            if (i != 0) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * The measureScaledText method (based on the QuickDraw function StdTxMeas) measures the width of scaled or unscaled text.
     * @see "QuickDraw::StdTxMeas()"
     * @param byteCount The number of bytes of text to be measured.
     * @param text String containing the text to be measured.
     * @param xScale Represents the scaling factor in the X dimension.
     * @param yScale Represents the scaling factor in the Y dimension.
     * @return The width of the specified text in pixels.
     */
    public int measureScaledText(int byteCount, String text, float xScale, float yScale) {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        QDPoint localQDPoint1 = new QDPoint((int) (xScale * 100.0F), (int) (yScale * 100.0F));
        arrayOfInt1[0] = localQDPoint1.getPoint();
        QDPoint localQDPoint2 = new QDPoint(100, 100);
        arrayOfInt2[0] = localQDPoint2.getPoint();
        byte[] arrayOfByte = text.getBytes();
        short[] arrayOfShort = new short[4];
        int j = 0;
        int i = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 1 : 0;
        synchronized (QTNative.globalsLock) {
            if (i != 0) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(_ID(), 0);
            GetFontInfo(arrayOfShort);
            j = StdTxMeas((short) byteCount, arrayOfByte, arrayOfInt1, arrayOfInt2, arrayOfShort);
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

    private static int GraphicsExportGetInputGWorld(int paramInt, int[] paramArrayOfInt) {
        return GraphicsExportManager.GraphicsExportGetInputGWorld(paramInt, paramArrayOfInt);
    }

    private static short QTGetPixelSize(int paramInt) {
        return ImageCompressionManager.QTGetPixelSize(paramInt);
    }

    private static int GraphicsImportGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return GraphicsImportManager.GraphicsImportGetGWorld(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void GetMovieGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMovieGWorld(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int MCGetControllerPort(int paramInt) {
        return MovieControllerManager.MCGetControllerPort(paramInt);
    }

    private static int SGGetGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return SequenceGrabberManager.SGGetGWorld(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short GetCSequencePrevBuffer(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetCSequencePrevBuffer(paramInt, paramArrayOfInt);
    }

    private static short GetDSequenceImageBuffer(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetDSequenceImageBuffer(paramInt, paramArrayOfInt);
    }

    private static short GetDSequenceScreenBuffer(int paramInt, int[] paramArrayOfInt) {
        return ImageCompressionManager.GetDSequenceScreenBuffer(paramInt, paramArrayOfInt);
    }

    private static int GetNativeWindowPort(int paramInt) {
        return PlatformManager.GetNativeWindowPort(paramInt);
    }

    private static int GetGWorldPixMap(int paramInt) {
        return QuickDrawManager.GetGWorldPixMap(paramInt);
    }

    private static byte LockPixels(int paramInt) {
        return QuickDrawManager.LockPixels(paramInt);
    }

    private static short QTNewGWorld(int[] paramArrayOfInt, int startAngle, byte[] paramArrayOfByte, int arcAngle, int paramInt3, int paramInt4) {
        return QuickDrawManager.QTNewGWorld(paramArrayOfInt, startAngle, paramArrayOfByte, arcAngle, paramInt3, paramInt4);
    }

    private static int SCNewGWorld(int startAngle, int[] paramArrayOfInt, byte[] paramArrayOfByte, int arcAngle) {
        return ImageCompressionManager.SCNewGWorld(startAngle, paramArrayOfInt, paramArrayOfByte, arcAngle);
    }

    private static short NewImageGWorld(int[] paramArrayOfInt, int startAngle, int arcAngle) {
        return QuickDrawManager.NewImageGWorld(paramArrayOfInt, startAngle, arcAngle);
    }

    private static void GetBackColor(byte[] paramArrayOfByte) {
        QuickDrawManager.GetBackColor(paramArrayOfByte);
    }

    private static void RGBBackColor(byte[] paramArrayOfByte) {
        QuickDrawManager.RGBBackColor(paramArrayOfByte);
    }

    private static void EraseRect(byte[] paramArrayOfByte) {
        QuickDrawManager.EraseRect(paramArrayOfByte);
    }

    private static void FrameRect(byte[] paramArrayOfByte) {
        QuickDrawManager.FrameRect(paramArrayOfByte);
    }

    private static void MacFrameRect(byte[] paramArrayOfByte) {
        QuickDrawManager.MacFrameRect(paramArrayOfByte);
    }

    private static void EraseRgn(int paramInt) {
        QuickDrawManager.EraseRgn(paramInt);
    }

    private static void GetForeColor(byte[] paramArrayOfByte) {
        QuickDrawManager.GetForeColor(paramArrayOfByte);
    }

    private static void RGBForeColor(byte[] paramArrayOfByte) {
        QuickDrawManager.RGBForeColor(paramArrayOfByte);
    }

    private static void PaintRect(byte[] paramArrayOfByte) {
        QuickDrawManager.PaintRect(paramArrayOfByte);
    }

    private static void MacPaintRgn(int paramInt) {
        QuickDrawManager.MacPaintRgn(paramInt);
    }

    private static void MacFrameRgn(int paramInt) {
        QuickDrawManager.MacFrameRgn(paramInt);
    }

    private static void GetPort(int[] paramArrayOfInt) {
        QuickDrawManager.GetPort(paramArrayOfInt);
    }

    private static void SetGWorld(int startAngle, int arcAngle) {
        QuickDrawManager.SetGWorld(startAngle, arcAngle);
    }

    private static void PaintRgn(int paramInt) {
        QuickDrawManager.PaintRgn(paramInt);
    }

    private static void FrameRgn(int paramInt) {
        QuickDrawManager.FrameRgn(paramInt);
    }

    private static void CopyRgn(int startAngle, int arcAngle) {
        QuickDrawManager.CopyRgn(startAngle, arcAngle);
    }

    private static void SetRectRgn(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
        QuickDrawManager.SetRectRgn(paramInt, paramShort1, paramShort2, paramShort3, paramShort4);
    }

    private static void OffsetRgn(int paramInt, short paramShort1, short paramShort2) {
        QuickDrawManager.OffsetRgn(paramInt, paramShort1, paramShort2);
    }

    private static void UnionRgn(int startAngle, int arcAngle, int paramInt3) {
        QuickDrawManager.UnionRgn(startAngle, arcAngle, paramInt3);
    }

    private static void XorRgn(int startAngle, int arcAngle, int paramInt3) {
        QuickDrawManager.XorRgn(startAngle, arcAngle, paramInt3);
    }

    private static byte EqualRgn(int startAngle, int arcAngle) {
        return QuickDrawManager.EqualRgn(startAngle, arcAngle);
    }

    private static void SectRgn(int startAngle, int arcAngle, int paramInt3) {
        QuickDrawManager.SectRgn(startAngle, arcAngle, paramInt3);
    }

    private static void GetClip(int paramInt) {
        QuickDrawManager.GetClip(paramInt);
    }

    private static void SetClip(int paramInt) {
        QuickDrawManager.SetClip(paramInt);
    }

    private static void ClipRect(byte[] paramArrayOfByte) {
        QuickDrawManager.ClipRect(paramArrayOfByte);
    }

    private static void ClipRect(int paramInt) {
        QuickDrawManager.ClipRect(paramInt);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void Move(short paramShort1, short paramShort2) {
        QuickDrawManager.Move(paramShort1, paramShort2);
    }

    private static void MoveTo(short paramShort1, short paramShort2) {
        QuickDrawManager.MoveTo(paramShort1, paramShort2);
    }

    private static void MacLineTo(short paramShort1, short paramShort2) {
        QuickDrawManager.MacLineTo(paramShort1, paramShort2);
    }

    private static void LineTo(short paramShort1, short paramShort2) {
        QuickDrawManager.LineTo(paramShort1, paramShort2);
    }

    private static void Line(short paramShort1, short paramShort2) {
        QuickDrawManager.Line(paramShort1, paramShort2);
    }

    private static void SetCPixel(short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        QuickDrawManager.SetCPixel(paramShort1, paramShort2, paramArrayOfByte);
    }

    private static void GetCPixel(short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        QuickDrawManager.GetCPixel(paramShort1, paramShort2, paramArrayOfByte);
    }

    private static void PenSize(short paramShort1, short paramShort2) {
        QuickDrawManager.PenSize(paramShort1, paramShort2);
    }

    private static void PenNormal() {
        QuickDrawManager.PenNormal();
    }

    private static void FrameArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.FrameArc(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void PaintArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.PaintArc(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void EraseArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.EraseArc(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void InvertArc(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.InvertArc(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void FrameRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.FrameRoundRect(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void PaintRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.PaintRoundRect(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void EraseRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.EraseRoundRect(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void InvertRoundRect(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.InvertRoundRect(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void FrameOval(byte[] paramArrayOfByte) {
        QuickDrawManager.FrameOval(paramArrayOfByte);
    }

    private static void PaintOval(byte[] paramArrayOfByte) {
        QuickDrawManager.PaintOval(paramArrayOfByte);
    }

    private static void EraseOval(byte[] paramArrayOfByte) {
        QuickDrawManager.EraseOval(paramArrayOfByte);
    }

    private static void InvertOval(byte[] paramArrayOfByte) {
        QuickDrawManager.InvertOval(paramArrayOfByte);
    }

    private static void FramePoly(int paramInt) {
        QuickDrawManager.FramePoly(paramInt);
    }

    private static void PaintPoly(int paramInt) {
        QuickDrawManager.PaintPoly(paramInt);
    }

    private static void ErasePoly(int paramInt) {
        QuickDrawManager.ErasePoly(paramInt);
    }

    private static void InvertPoly(int paramInt) {
        QuickDrawManager.InvertPoly(paramInt);
    }

    private static short getShortFromPointer(int startAngle, int arcAngle) {
        return MemoryManager.getShortFromPointer(startAngle, arcAngle);
    }

    private static void setIntInPointer(int startAngle, int arcAngle, int paramInt3) {
        MemoryManager.setIntInPointer(startAngle, arcAngle, paramInt3);
    }

    private static int getIntFromPointer(int startAngle, int arcAngle) {
        return MemoryManager.getIntFromPointer(startAngle, arcAngle);
    }

    private static void TextFont(short paramShort) {
        QuickDrawManager.TextFont(paramShort);
    }

    private static void TextFace(short paramShort) {
        QuickDrawManager.TextFace(paramShort);
    }

    private static void TextMode(short paramShort) {
        QuickDrawManager.TextMode(paramShort);
    }

    private static void TextSize(short paramShort) {
        QuickDrawManager.TextSize(paramShort);
    }

    private static void DrawChar(short paramShort) {
        QuickDrawManager.DrawChar(paramShort);
    }

    private static void MacDrawText(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.MacDrawText(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void DrawText(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        QuickDrawManager.DrawText(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static short CharWidth(short paramShort) {
        return QuickDrawManager.CharWidth(paramShort);
    }

    private static short TextWidth(byte[] paramArrayOfByte, short paramShort1, short paramShort2) {
        return QuickDrawManager.TextWidth(paramArrayOfByte, paramShort1, paramShort2);
    }

    private static void MeasureText(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt) {
        QuickDrawManager.MeasureText(paramShort, paramArrayOfByte, paramArrayOfInt);
    }

    private static void StdText(short paramShort, byte[] paramArrayOfByte, int startAngle, int arcAngle) {
        QuickDrawManager.StdText(paramShort, paramArrayOfByte, startAngle, arcAngle);
    }

    private static short StdTxMeas(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2, short[] paramArrayOfShort) {
        return QuickDrawManager.StdTxMeas(paramShort, paramArrayOfByte, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfShort);
    }

    private static void GetFontInfo(short[] paramArrayOfShort) {
        QuickDrawManager.GetFontInfo(paramArrayOfShort);
    }

    private static byte IsPortOffscreen(int paramInt) {
        return QuickDrawManager.IsPortOffscreen(paramInt);
    }

    private static int GetPortBounds(int paramInt, byte[] paramArrayOfByte) {
        return QuickDrawManager.GetPortBounds(paramInt, paramArrayOfByte);
    }

    private static int GetPortClipRegion(int startAngle, int arcAngle) {
        return QuickDrawManager.GetPortClipRegion(startAngle, arcAngle);
    }

    private static int GetPortVisibleRegion(int startAngle, int arcAngle) {
        return QuickDrawManager.GetPortVisibleRegion(startAngle, arcAngle);
    }

    private static short QTSPresGetGWorld(int startAngle, int arcAngle, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return StreamingManager.QTSPresGetGWorld(startAngle, arcAngle, paramArrayOfInt1, paramArrayOfInt2);
    }
}

