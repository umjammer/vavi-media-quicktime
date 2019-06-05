
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


public final class QDGraphics extends QTPointerRef implements QuickTimeLib, QTStreamingLib {
    private static Object linkage;

    public static final int kNativeSize = 108;

    public static QDGraphics scratch;

    public static QDGraphics validScratch;

    public static final int kDefaultPixelFormat = (QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)) ? 32 : 1111970369;

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

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

    public static QDGraphics fromGraphicsExporter(GraphicsExporter paramGraphicsExporter) throws StdQTException {
        int[] arrayOfInt = new int[1];

        int i = GraphicsExportGetInputGWorld(QTObject.ID(paramGraphicsExporter), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramGraphicsExporter);
    }

    public static QDGraphics getPort() {
        int[] arrayOfInt = {
            0
        };
        synchronized (QTNative.globalsLock) {
            GetPort(arrayOfInt);
        }
        return new QDGraphics(arrayOfInt[0], new Object());
    }

    public static int getPixelSize(int paramInt) throws StdQTException {
        return QTGetPixelSize(paramInt);
    }

    public static QDGraphics fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];

        int i = GraphicsImportGetGWorld(QTObject.ID(paramGraphicsImporter), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], paramGraphicsImporter);
    }

    public static QDGraphics fromMovie(Movie paramMovie) throws StdQTException {
        int[] arrayOfInt = new int[1];

        synchronized (QTNative.globalsLock) {
            GetMovieGWorld(QTObject.ID(paramMovie), arrayOfInt, null);
            StdQTException.checkError(GetMoviesError());

            return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramMovie);
        }
    }

    public static QDGraphics fromPresentation(Presentation paramPresentation, Stream paramStream) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];

        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(QTSPresGetGWorld(QTObject.ID(paramPresentation),
                                                       QTObject.ID(paramStream),
                                                       arrayOfInt1,
                                                       arrayOfInt2));

            return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], paramPresentation);
        }
    }

    public static QDGraphics fromMovieController(MovieController paramMovieController) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = MCGetControllerPort(QTObject.ID(paramMovieController));
            StdQTException.checkError(GetMoviesError());
            return i == 0 ? scratch : new QDGraphics(i, paramMovieController);
        }
    }

    public static QDGraphics fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int i = SGGetGWorld(QTObject.ID(paramSequenceGrabber), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(i);
        return arrayOfInt1[0] == 0 ? scratch : new QDGraphics(arrayOfInt1[0], paramSequenceGrabber);
    }

    public static QDGraphics fromCSequence(CSequence paramCSequence) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetCSequencePrevBuffer(QTObject.ID(paramCSequence), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramCSequence);
    }

    public static QDGraphics fromDSequenceImage(DSequence paramDSequence) throws StdQTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(GetDSequenceImageBuffer(QTObject.ID(paramDSequence), arrayOfInt));
        return arrayOfInt[0] == 0 ? scratch : new QDGraphics(arrayOfInt[0], paramDSequence);
    }

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

    public QDGraphics(ImageCompressionDialog paramImageCompressionDialog, QDRect paramQDRect, int paramInt) throws QTException {
        this(allocate(paramImageCompressionDialog, paramQDRect, paramInt));
    }

    public QDGraphics(ImageDescription paramImageDescription, int paramInt) throws QTException {
        this(allocate(paramImageDescription, paramInt));
    }

    public QDGraphics(QDRect paramQDRect) throws QTException {
        this(kDefaultPixelFormat, paramQDRect, null, null, 4096);
    }

    public QDGraphics(int paramInt, QDRect paramQDRect) throws QTException {
        this(paramInt, paramQDRect, null, null, 4096);
    }

    public QDGraphics(int paramInt1, QDRect paramQDRect, int paramInt2) throws QTException {
        this(paramInt1, paramQDRect, null, null, paramInt2);
    }

    public QDGraphics(int paramInt1,
            QDRect paramQDRect,
            ColorTable paramColorTable,
            GDevice paramGDevice,
            int paramInt2) throws QTException {
        this(allocate(paramInt1, paramQDRect, paramColorTable, paramGDevice, paramInt2));
    }

    private static int allocate(int paramInt1,
                                QDRect paramQDRect,
                                ColorTable paramColorTable,
                                GDevice paramGDevice,
                                int paramInt2) throws QDException {
        int[] arrayOfInt = {
            0
        };
        int i = QTNewGWorld(arrayOfInt,
                            paramInt1,
                            paramQDRect.getRect(),
                            QTObject.ID(paramColorTable),
                            QTObject.ID(paramGDevice),
                            paramInt2);

        QDException.checkError(i);
        return arrayOfInt[0];
    }

    private static int allocate(ImageCompressionDialog paramImageCompressionDialog,
                                QDRect paramQDRect,
                                int paramInt) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(SCNewGWorld(QTObject.ID(paramImageCompressionDialog),
                                              arrayOfInt,
                                              paramQDRect != null ? paramQDRect.getRect() : null,
                                              paramInt));

        if (arrayOfInt[0] == 0)
            throw new StdQTException(-50);
        return arrayOfInt[0];
    }

    private static int allocate(ImageDescription paramImageDescription, int paramInt) throws QTException {
        int[] arrayOfInt = new int[1];
        StdQTException.checkError(NewImageGWorld(arrayOfInt, QTObject.ID(paramImageDescription), paramInt));
        return arrayOfInt[0];
    }

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

    public boolean isOffscreen() {
        if (QTSession.isCurrentOS(4)) {
            int i = IsPortOffscreen(_ID());
            return i == 1;
        }
        return (getShortFromPointer(_ID(), 6) & 0xC001) == 49153;
    }

    public PixMap getPixMap() {
        return PixMap.fromQDGraphics(this);
    }

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

    public Pict makeThumbnail(QDRect paramQDRect, int paramInt) throws QTException {
        return Pict.thumbnailFromQDGraphics(this, paramQDRect, paramInt);
    }

    public QDRect getBounds() {
        QDRect localQDRect = new QDRect();

        if (QTSession.isCurrentOS(4)) {
            GetPortBounds(_ID(), localQDRect.getBytes());

            return localQDRect;
        }

        return getPortRect();
    }

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

    public int getTextFont() {
        return getShortAt(68);
    }

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

    public int getTextFace() {
        return QTUtils.UByte2Int(getByteAt(70));
    }

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

    public int getTextMode() {
        return getShortAt(72);
    }

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

    public int getTextSize() {
        return getShortAt(74);
    }

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

    public int measureScaledText(int paramInt, String paramString, float paramFloat1, float paramFloat2) {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };

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

    private static native short QTNewGWorld(int[] paramArrayOfInt,
                                            int paramInt1,
                                            byte[] paramArrayOfByte,
                                            int paramInt2,
                                            int paramInt3,
                                            int paramInt4);

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

    private static native void SetRectRgn(int paramInt,
                                          short paramShort1,
                                          short paramShort2,
                                          short paramShort3,
                                          short paramShort4);

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

    private static native short StdTxMeas(short paramShort,
                                          byte[] paramArrayOfByte,
                                          int[] paramArrayOfInt1,
                                          int[] paramArrayOfInt2,
                                          short[] paramArrayOfShort);

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
