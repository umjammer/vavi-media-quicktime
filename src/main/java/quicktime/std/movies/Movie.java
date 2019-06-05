
package quicktime.std.movies;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.io.OpenFile;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.io.QTIOException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.QuickTimeVRLib;
import quicktime.qd.ColorTable;
import quicktime.qd.GDevice;
import quicktime.qd.Pict;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.Clock;
import quicktime.std.clocks.TimeBase;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.image.Matrix;
import quicktime.std.movies.media.DataRef;
import quicktime.std.movies.media.UserData;
import quicktime.std.qtcomponents.MovieExporter;
import quicktime.std.qtcomponents.MovieImporter;
import quicktime.std.sg.SequenceGrabber;
import quicktime.util.QTHandle;
import quicktime.util.QTPointer;
import quicktime.util.QTUtils;


public final class Movie extends QTObject implements QuickTimeLib, QuickTimeVRLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(Movie.class);
                    }
                });
            }
        }.establish();
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    public static void taskAll(int paramInt) throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            if (QTSession.isInitialized()) {
                MoviesTask(0, paramInt);
            } else
                throw new QTException("QuickTime is NOT initialized");
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    public static Movie createMovieFile(QTFile paramQTFile, int paramInt1, int paramInt2) throws QTException {
        if ((paramInt2 & 0x80000000) != 0) {
            try {
                QTFile.checkSecurity(paramQTFile.getCanonicalPath(), 1024);
            } catch (IOException localIOException) {
                throw new QTIOException(localIOException.getMessage());
            }
        }

        int[] arrayOfInt = {
            0
        };

        if ((paramInt2 & 0x40000000) != 0) {
            paramInt2 ^= 0x40000000;
        }
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);

            i = CreateMovieFile(paramQTFile.getFSSpec(false, 512),
                                paramInt1,
                                (short) -1,
                                paramInt2 | 0x20000000,
                                null,
                                arrayOfInt);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        return new Movie(arrayOfInt[0], null);
    }

    public static Movie fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) {
        int i = SGGetMovie(QTObject.ID(paramSequenceGrabber));
        return new Movie(i, paramSequenceGrabber);
    }

    public static Movie fromScrap(int paramInt) throws QTException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromScrap(paramInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        if (i == 0) {
            throw new StdQTException(63526);
        }
        Object x = new Movie(i, null);
        ((Movie) x).init();
        return (Movie) x;
    }

    public static Movie fromFile(OpenMovieFile paramOpenMovieFile) throws QTException {
        return fromFile(paramOpenMovieFile, 1, null);
    }

    public static Movie fromFile(OpenMovieFile paramOpenMovieFile, int paramInt, MovieInfo paramMovieInfo) throws QTException {
        int[] arrayOfInt = new int[1];
        short s = (short) QTObject.ID(paramOpenMovieFile);
        short[] arrayOfShort = new short[1];
        byte[] arrayOfByte1 = new byte[255];

        byte[] arrayOfByte2 = new byte[1];

        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromFile(arrayOfInt, s, arrayOfShort, arrayOfByte1, (short) paramInt, arrayOfByte2);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0) {
            throw new StdQTException(63526);
        }
        if (paramMovieInfo != null) {
            paramMovieInfo.setMovieInfo(arrayOfShort[0],
                                        arrayOfByte2[0] == 1,
                                        new String(arrayOfByte1, 1, arrayOfByte1.length - 1));
        }

        Object x = new Movie(arrayOfInt[0], null);
        ((Movie) x).init();
        return (Movie) x;
    }

    public static Movie fromHandle(QTHandle paramQTHandle) throws QTException {
        return fromHandle(paramQTHandle, 1, null);
    }

    public static Movie fromHandle(QTHandle paramQTHandle, int paramInt, MovieInfo paramMovieInfo) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = QTObject.ID(paramQTHandle);
        byte[] arrayOfByte = new byte[1];

        int j = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            j = NewMovieFromHandle(arrayOfInt, i, (short) paramInt, arrayOfByte);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(j);
        if (arrayOfInt[0] == 0) {
            throw new StdQTException(63526);
        }
        if (paramMovieInfo != null) {
            paramMovieInfo.setMovieInfo(0, arrayOfByte[0] == 1, null);
        }

        Object x = new Movie(arrayOfInt[0], null);
        ((Movie) x).init();
        ((Movie) x).dataRef = paramQTHandle;
        return (Movie) x;
    }

    public static Movie fromDataFork(OpenFile paramOpenFile,
                                     int paramInt1,
                                     int paramInt2,
                                     MovieInfo paramMovieInfo) throws QTException, QDException {
        int[] arrayOfInt = {
            0
        };
        short s = (short) QTObject.ID(paramOpenFile);
        byte[] arrayOfByte = new byte[1];

        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromDataFork(arrayOfInt, s, paramInt1, (short) paramInt2, arrayOfByte);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0) {
            throw new StdQTException(63526);
        }
        if (paramMovieInfo != null) {
            paramMovieInfo.setMovieInfo(0, arrayOfByte[0] == 1, "");
        }

        Object x = new Movie(arrayOfInt[0], null);
        ((Movie) x).init();
        return (Movie) x;
    }

    public static Movie fromDataRef(DataRef paramDataRef, int paramInt) throws QTException {
        int[] arrayOfInt = {
            0
        };
        short[] arrayOfShort = {
            0
        };

        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromDataRef(arrayOfInt,
                                    (short) paramInt,
                                    arrayOfShort,
                                    QTObject.ID(paramDataRef),
                                    paramDataRef == null ? 0 : paramDataRef.getType());

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        if (arrayOfInt[0] == 0)
            throw new StdQTException(63524);
        StdQTException.checkError(i);

        Object x = new Movie(arrayOfInt[0], null);
        ((Movie) x).init();
        ((Movie) x).dataRef = paramDataRef;
        return (Movie) x;
    }

    Movie(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public Movie() throws QTException {
        this(1);
    }

    public Movie(int paramInt) throws QTException {
        super(allocate(paramInt));
        init();
    }

    private static int allocate(int paramInt) throws StdQTException, QDException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovie(paramInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(GetMoviesError());
        if (i == 0)
            throw new StdQTException(63526);
        return i;
    }

    private Object dataRef = null;

    private MoviesDispatcher drawingUPP;

    private MoviesDispatcher preUPP;

    private MoviesDispatcher progUPP;

    private MoviesDispatcher wiredUPP;

    private void init() throws StdQTException {
        QDRect localQDRect = getBounds();
        localQDRect.setX(0);
        localQDRect.setY(0);
        setBounds(localQDRect);
    }

    void _setNR(int paramInt) {
        setNR(paramInt);
    }

    public int addResource(OpenMovieFile paramOpenMovieFile, int paramInt, String paramString) throws StdQTException {
        short[] arrayOfShort = {
            (short) paramInt
        };
        byte[] arrayOfByte = paramString != null ? QTUtils.String2PString(paramString, 255) : null;
        int i = AddMovieResource(_ID(), (short) QTObject.ID(paramOpenMovieFile), arrayOfShort, arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    public void updateResource(OpenMovieFile paramOpenMovieFile, int paramInt, String paramString) throws StdQTException {
        byte[] arrayOfByte = paramString != null ? QTUtils.String2PString(paramString, 255) : null;
        int i = UpdateMovieResource(_ID(), (short) QTObject.ID(paramOpenMovieFile), (short) paramInt, arrayOfByte);
        StdQTException.checkError(i);
    }

    public void removeResource(OpenMovieFile paramOpenMovieFile, int paramInt) throws StdQTException {
        int i = RemoveMovieResource((short) QTObject.ID(paramOpenMovieFile), (short) paramInt);
        StdQTException.checkError(i);
    }

    public void task(int paramInt) throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            MoviesTask(_ID(), paramInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    public void setDrawingCompleteProc(int paramInt, MovieDrawingComplete paramMovieDrawingComplete) throws StdQTException {
        if (paramMovieDrawingComplete == null) {
            removeDrawingCompleteProc();
            return;
        }

        if (this.drawingUPP != null)
            removeDrawingCompleteProc();
        this.drawingUPP = new MoviesDispatcher(this, paramMovieDrawingComplete);
        SetMovieDrawingCompleteProc(_ID(), paramInt, this.drawingUPP.ID(), 0);
        StdQTException.checkError(GetMoviesError());
    }

    public void removeDrawingCompleteProc() throws StdQTException {
        SetMovieDrawingCompleteProc(_ID(), 0, 0, 0);
        if (this.drawingUPP != null)
            this.drawingUPP.cleanupMethodClosure();
        this.drawingUPP = null;
        StdQTException.checkError(GetMoviesError());
    }

    public void preroll(int paramInt, float paramFloat) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                int j = PrerollMovie(_ID(), paramInt, QTUtils.X2Fix(paramFloat));
                StdQTException.checkError(j);
            }
        }
        int i = PrerollMovie(_ID(), paramInt, QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(i);
    }

    public void prePreroll(int paramInt, float paramFloat) throws StdQTException {
        this.preUPP = null;
        prePreroll(paramInt, paramFloat, null);
    }

    public void prePreroll(int paramInt, float paramFloat, MoviePrePreroll paramMoviePrePreroll) throws StdQTException {
        if (QTSession.getQTMajorVersion() == 3) {
            if (paramMoviePrePreroll != null)
                paramMoviePrePreroll.execute(this, 0);
            return;
        }

        if (paramMoviePrePreroll == null) {
            paramMoviePrePreroll = new MoviePrePreroll() {
                public void execute(Movie paramAnonymousMovie, int paramAnonymousInt) {
                }
            };
        }
        this.preUPP = new MoviesDispatcher(this, paramMoviePrePreroll);

        synchronized (QTNative.globalsLock) {
            int i = PrePrerollMovie(_ID(), paramInt, QTUtils.X2Fix(paramFloat), this.preUPP.ID(), 0);
            StdQTException.checkError(i);
        }
    }

    public void abortPrePreroll(int paramInt) throws StdQTException {
        if (QTSession.getQTMajorVersion() == 3)
            return;
        AbortPrePrerollMovie(_ID(), (short) paramInt);
        if (this.preUPP != null) {
            this.preUPP.cleanupMethodClosure();
            this.preUPP = null;
        }
    }

    public void loadIntoRam(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = LoadMovieIntoRam(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    public void setActive(boolean paramBoolean) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                byte b2 = (byte) (paramBoolean ? 1 : 0);
                SetMovieActive(_ID(), b2);
                StdQTException.checkError(GetMoviesError());
            }
        }
        byte b1 = (byte) (paramBoolean ? 1 : 0);
        SetMovieActive(_ID(), b1);
        StdQTException.checkError(GetMoviesError());
    }

    public boolean getActive() throws StdQTException {
        int i = GetMovieActive(_ID());
        StdQTException.checkError(GetMoviesError());

        return i == 1;
    }

    public void start() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StartMovie(_ID());
        }
        StdQTException.checkError(GetMoviesError());
    }

    public void stop() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StopMovie(_ID());
        }
        StdQTException.checkError(GetMoviesError());
    }

    public void goToBeginning() throws StdQTException {
        GoToBeginningOfMovie(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    public void goToEnd() throws StdQTException {
        GoToEndOfMovie(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    public boolean isDone() throws StdQTException {
        int i = IsMovieDone(_ID());
        StdQTException.checkError(GetMoviesError());

        return i == 1;
    }

    public boolean getPreviewMode() throws StdQTException {
        int i = GetMoviePreviewMode(_ID());
        StdQTException.checkError(GetMoviesError());

        return i == 1;
    }

    public void setPreviewMode(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        SetMoviePreviewMode(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    public void showPoster() throws StdQTException {
        ShowMoviePoster(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    public void playPreview() throws StdQTException {
        PlayMoviePreview(_ID(), 0, 0);
        StdQTException.checkError(GetMoviesError());
    }

    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromMovie(this);
    }

    public void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice));

            StdQTException.checkError(GetMoviesError());
        }
    }

    public QDRect getNaturalBoundsRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        GetMovieNaturalBoundsRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
        return localQDRect;
    }

    public Track getNextTrackForCompositing(Track paramTrack) throws QTException {
        int i = GetNextTrackForCompositing(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this);
    }

    public Track getPrevTrackForCompositing(Track paramTrack) throws QTException {
        int i = GetPrevTrackForCompositing(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this);
    }

    public Pict getPict(int paramInt) throws QTException {
        return Pict.fromMovie(this, paramInt);
    }

    public Pict getPosterPict() throws QTException {
        return Pict.fromMovie(this);
    }

    public void update() throws StdQTException {
        int i = UpdateMovie(_ID());
        StdQTException.checkError(i);
    }

    public void invalidateRegion(Region paramRegion) throws StdQTException {
        int i = InvalidateMovieRegion(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    public QDRect getBounds() throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                QDRect localQDRect = new QDRect();
                GetMovieBox(_ID(), localQDRect.getRect());
                StdQTException.checkError(GetMoviesError());
                return localQDRect;
            }
        }
        Object x = new QDRect();
        GetMovieBox(_ID(), ((QDRect) x).getRect());
        StdQTException.checkError(GetMoviesError());
        return (QDRect) x;
    }

    public QDRect getBox() throws StdQTException {
        return getBounds();
    }

    public void setBounds(QDRect paramQDRect) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                SetMovieBox(_ID(), paramQDRect.getRect());
                StdQTException.checkError(GetMoviesError());
            }
        }
        SetMovieBox(_ID(), paramQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
    }

    public void setBox(QDRect paramQDRect) throws StdQTException {
        setBounds(paramQDRect);
    }

    public Region getDisplayClipRgn() throws QTException {
        return Region.fromMovieDisplayClip(this);
    }

    public void setDisplayClipRgn(Region paramRegion) throws StdQTException {
        SetMovieDisplayClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(GetMoviesError());
    }

    public Region getClipRgn() throws QTException {
        return Region.fromMovieClip(this);
    }

    public void setClipRgn(Region paramRegion) throws StdQTException {
        SetMovieClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(GetMoviesError());
    }

    public Region getDisplayBoundsRgn() throws QTException {
        return Region.fromMovieDisplayBounds(this);
    }

    public Region getBoundsRgn() throws QTException {
        return Region.fromMovieBounds(this);
    }

    public void putIntoHandle(QTHandle paramQTHandle) throws StdQTException {
        int i = PutMovieIntoHandle(_ID(), QTObject.ID(paramQTHandle));
        StdQTException.checkError(i);
    }

    public void putIntoDataFork(OpenMovieFile paramOpenMovieFile, int paramInt1, int paramInt2) throws StdQTException {
        short s = (short) QTObject.ID(paramOpenMovieFile);
        int i = PutMovieIntoDataFork(_ID(), s, paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public int getCreationTime() throws StdQTException {
        int i = GetMovieCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int getModificationTime() throws StdQTException {
        int i = GetMovieModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public int getTimeScale() throws StdQTException {
        int i = GetMovieTimeScale(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public void setTimeScale(int paramInt) throws StdQTException {
        SetMovieTimeScale(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public int getDuration() throws StdQTException {
        int i = GetMovieDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public float getRate() throws StdQTException {
        int i = GetMovieRate(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    public void setRate(float paramFloat) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                SetMovieRate(_ID(), QTUtils.X2Fix(paramFloat));

                StdQTException.checkError(GetMoviesError());
            }
        }
        SetMovieRate(_ID(), QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    public float getPreferredRate() throws StdQTException {
        int i = GetMoviePreferredRate(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    public void setPreferredRate(float paramFloat) throws StdQTException {
        SetMoviePreferredRate(_ID(), QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    public float getPreferredVolume() throws StdQTException {
        short s = GetMoviePreferredVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    public void setPreferredVolume(float paramFloat) throws StdQTException {
        SetMoviePreferredVolume(_ID(), QTUtils.X2ShortFix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    public float getVolume() throws StdQTException {
        short s = GetMovieVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    public void setVolume(float paramFloat) throws StdQTException {
        SetMovieVolume(_ID(), QTUtils.X2ShortFix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        GetMovieMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
        return localMatrix;
    }

    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        SetMovieMatrix(_ID(), paramMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
    }

    public TimeInfo getPreviewTime() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMoviePreviewTime(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public void setPreviewTime(TimeInfo paramTimeInfo) throws StdQTException {
        SetMoviePreviewTime(_ID(), paramTimeInfo.time, paramTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    public void setPreviewTime(int paramInt1, int paramInt2) throws StdQTException {
        SetMoviePreviewTime(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    public int getPosterTime() throws StdQTException {
        int i = GetMoviePosterTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public void setPosterTime(int paramInt) throws StdQTException {
        SetMoviePosterTime(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public TimeInfo getSelection() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMovieSelection(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public void setSelection(TimeInfo paramTimeInfo) throws StdQTException {
        SetMovieSelection(_ID(), paramTimeInfo.time, paramTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    public void setSelection(int paramInt1, int paramInt2) throws StdQTException {
        SetMovieSelection(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    public void setActiveSegment(TimeInfo paramTimeInfo) throws StdQTException {
        SetMovieActiveSegment(_ID(), paramTimeInfo.time, paramTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    public TimeInfo getActiveSegment() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMovieActiveSegment(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public int getTime() throws StdQTException {
        int i = GetMovieTime(_ID(), 0);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public TimeRecord getTRTime() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        int i = GetMovieTime(_ID(), QTObject.ID(localTimeRecord));
        StdQTException.checkError(GetMoviesError());

        return localTimeRecord;
    }

    public void setTime(TimeRecord paramTimeRecord) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieTime(_ID(), QTObject.ID(paramTimeRecord));
        }
        StdQTException.checkError(GetMoviesError());
    }

    public void setTimeValue(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieTimeValue(_ID(), paramInt);
        }
        StdQTException.checkError(GetMoviesError());
    }

    public UserData getUserData() throws StdQTException {
        return UserData.fromMovie(this);
    }

    public int getTrackCount() throws StdQTException {
        int i = GetMovieTrackCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public Track getTrack(int paramInt) throws QTException {
        int i = GetMovieTrack(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    public Track getIndTrack(int paramInt) throws QTException {
        int i = GetMovieIndTrack(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    public Track getIndTrackType(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        int i = GetMovieIndTrackType(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    public void removeTrack(Track paramTrack) throws StdQTException {
        if (QTObject.ID(paramTrack) == 0) {
            throw new StdQTException(63527);
        }
        int i = GetTrackMovie(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        if (i != _ID()) {
            throw new StdQTException(63526);
        }
        DisposeMovieTrack(QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        paramTrack.owner = null;
        paramTrack._setNR(0);
    }

    public void setAutoTrackAlternatesEnabled(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        SetAutoTrackAlternatesEnabled(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    public void selectAlternates() throws StdQTException {
        SelectMovieAlternates(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    public TimeInfo getNextInterestingTime(int paramInt1,
                                           int[] paramArrayOfInt,
                                           int paramInt2,
                                           float paramFloat) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        short s = 0;

        if (paramArrayOfInt != null) {
            s = (short) paramArrayOfInt.length;
        }
        GetMovieNextInterestingTime(_ID(),
                                    (short) paramInt1,
                                    s,
                                    paramArrayOfInt,
                                    paramInt2,
                                    QTUtils.X2Fix(paramFloat),
                                    arrayOfInt1,
                                    arrayOfInt2);
        StdQTException.checkError(GetMoviesError());

        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    public void insertEmptySegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = InsertEmptyMovieSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public void insertSegment(Movie paramMovie, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = InsertMovieSegment(_ID(), QTObject.ID(paramMovie), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    public void deleteSegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = DeleteMovieSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    public void scaleSegment(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = ScaleMovieSegment(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    public Movie cutSelection() throws QTException {
        int i = CutMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());

        return new Movie(i, null);
    }

    public Movie copySelection() throws QTException {
        int i = CopyMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());

        return new Movie(i, null);
    }

    public void pasteSelection(Movie paramMovie) throws StdQTException {
        PasteMovieSelection(_ID(), QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
    }

    public void addSelection(Movie paramMovie) throws StdQTException {
        AddMovieSelection(_ID(), QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
    }

    public void clearSelection() throws StdQTException {
        ClearMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    public void pasteHandle(QTHandle paramQTHandle,
                            int paramInt1,
                            int paramInt2,
                            MovieImporter paramMovieImporter) throws StdQTException {
        int i = PasteHandleIntoMovie(QTObject.ID(paramQTHandle), paramInt1, _ID(), paramInt2, QTObject.ID(paramMovieImporter));
        StdQTException.checkError(i);
    }

    public QTHandle putIntoTypedHandle(Track paramTrack,
                                       int paramInt1,
                                       int paramInt2,
                                       int paramInt3,
                                       int paramInt4,
                                       MovieExporter paramMovieExporter) throws QTException {
        int i = 0;
        if (paramTrack != null) {
            i = QTObject.ID(paramTrack);
        }
        QTHandle localQTHandle = new QTHandle(0, true);
        int j = PutMovieIntoTypedHandle(_ID(),
                                        i,
                                        paramInt1,
                                        QTObject.ID(localQTHandle),
                                        paramInt2,
                                        paramInt3,
                                        paramInt4,
                                        QTObject.ID(paramMovieExporter));

        StdQTException.checkError(j);

        return localQTHandle;
    }

    public void copySettings(Movie paramMovie) throws StdQTException {
        int i = CopyMovieSettings(_ID(), QTObject.ID(paramMovie));
        StdQTException.checkError(i);
    }

    public Track addEmptyTrack(Track paramTrack, DataRef paramDataRef) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = AddEmptyTrackToMovie(QTObject.ID(paramTrack),
                                     _ID(),
                                     QTObject.ID(paramDataRef),
                                     paramDataRef.getType(),
                                     arrayOfInt);

        StdQTException.checkError(i);

        return new Track(arrayOfInt[0], this);
    }

    public Track newTrack(float paramFloat1, float paramFloat2, float paramFloat3) throws QTException {
        return addTrack(paramFloat1, paramFloat2, paramFloat3);
    }

    public Track addTrack(float paramFloat1, float paramFloat2, float paramFloat3) throws QTException {
        return new Track(this, paramFloat1, paramFloat2, paramFloat3);
    }

    public MovieEditState newEditState() throws QTException {
        int i = NewMovieEditState(_ID());
        StdQTException.checkError(GetMoviesError());
        return new MovieEditState(this, i);
    }

    public void useEditState(MovieEditState paramMovieEditState) throws StdQTException {
        int i = UseMovieEditState(_ID(), QTObject.ID(paramMovieEditState));
        StdQTException.checkError(i);
    }

    public int getDataSize(int paramInt1, int paramInt2) throws StdQTException {
        int i = GetMovieDataSize(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public boolean pointInMovie(QDPoint paramQDPoint) throws StdQTException {
        int i = PtInMovie(_ID(), paramQDPoint.getPoint());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    public void setLanguage(int paramInt) throws StdQTException {
        SetMovieLanguage(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    public boolean hasChanged() throws StdQTException {
        int i = HasMovieChanged(_ID());
        StdQTException.checkError(GetMoviesError());

        return i == 1;
    }

    public void clearChanged() throws StdQTException {
        ClearMovieChanged(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    public void setDefaultDataRef(DataRef paramDataRef) throws StdQTException {
        int i = SetMovieDefaultDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(i);
        this.dataRef = paramDataRef;
    }

    public DataRef getDefaultDataRef() throws QTException {
        return DataRef.fromMovie(this);
    }

    public void setColorTable(ColorTable paramColorTable) throws StdQTException {
        int i = SetMovieColorTable(_ID(), QTObject.ID(paramColorTable));
        StdQTException.checkError(i);
    }

    public ColorTable getColorTable() throws QTException {
        return ColorTable.fromMovie(this);
    }

    public int flatten(int paramInt1,
                       QTFile paramQTFile,
                       int paramInt2,
                       int paramInt3,
                       int paramInt4,
                       int paramInt5,
                       String paramString) throws QTException {
        short[] arrayOfShort = new short[1];
        arrayOfShort[0] = ((short) paramInt5);
        byte[] arrayOfByte = null;

        if (paramString != null) {
            arrayOfByte = QTUtils.String2PString(paramString, 255);
        }
        FlattenMovie(_ID(),
                     paramInt1,
                     paramQTFile.getFSSpec(false, 512),
                     paramInt2,
                     (short) paramInt3,
                     paramInt4,
                     arrayOfShort,
                     arrayOfByte);

        StdQTException.checkError(GetMoviesError());

        return arrayOfShort[0];
    }

    public Movie flattenData(int paramInt1,
                             QTFile paramQTFile,
                             int paramInt2,
                             int paramInt3,
                             int paramInt4) throws QTException {
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            int[] arrayOfInt1 = {
                0
            };
            int[] arrayOfInt2 = {
                0
            };
            GetMovieGWorld(_ID(), arrayOfInt1, arrayOfInt2);

            SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            i = FlattenMovieData(_ID(), paramInt1, paramQTFile.getFSSpec(false, 512), paramInt2, (short) paramInt3, paramInt4);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());

        return i != 0 ? new Movie(i, null) : null;
    }

    public Movie flattenData(int paramInt, QTHandle paramQTHandle) throws QTException {
        byte[] arrayOfByte = new byte[8];
        setIntInArray(arrayOfByte, 0, 1752065132);
        setIntInArray(arrayOfByte, 4, QTObject.ID(paramQTHandle));
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            int[] arrayOfInt1 = {
                0
            };
            int[] arrayOfInt2 = {
                0
            };
            GetMovieGWorld(_ID(), arrayOfInt1, arrayOfInt2);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = FlattenMovieData(_ID(), paramInt | 0x10, arrayOfByte, 0, (short) -1, 0);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(GetMoviesError());

        Object x = null;
        if (i != 0) {
            x = new Movie(i, null);
            ((Movie) x).dataRef = paramQTHandle;
            ((Movie) x).setActive(true);
        }
        return (Movie) x;
    }

    public void setProgressProc() throws StdQTException {
        int i = 0;
        if (QTSession.isCurrentOS(4)) {
            i = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                int res1 = 0;

                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        Movie.SetMovieProgressProc(_ID(), -1, 0);
                    }
                    return new Integer(this.res1);
                }
            })).intValue();
            StdQTException.checkError(i);
        } else {
            synchronized (QTNative.globalsLock) {
                SetMovieProgressProc(_ID(), -1, 0);
            }
        }
        if (this.progUPP != null) {
            this.progUPP.cleanupMethodClosure();
            this.progUPP = null;
        }
        StdQTException.checkError(GetMoviesError());
    }

    public void setProgressProc(MovieProgress paramMovieProgress) throws StdQTException {
        int i = 0;
        if (paramMovieProgress == null) {
            setProgressProc();
            return;
        }

        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        this.progUPP = new MoviesDispatcher(this, paramMovieProgress);
        synchronized (QTNative.globalsLock) {
            SetMovieProgressProc(_ID(), this.progUPP.ID(), 0);
        }
        StdQTException.checkError(GetMoviesError());
    }

    public void addExecuteWiredActionsProc(ExecutingWiredAction paramExecutingWiredAction) throws StdQTException {
        if (paramExecutingWiredAction == null) {
            throw new StdQTException(-50);
        }

        if (this.wiredUPP != null) {
            RemoveMovieExecuteWiredActionsProc(_ID(), this.wiredUPP.ID(), 0);
            this.wiredUPP.cleanupMethodClosure();
        }

        this.wiredUPP = new MoviesDispatcher(this, paramExecutingWiredAction);
        StdQTException.checkError(AddMovieExecuteWiredActionsProc(_ID(), this.wiredUPP.ID(), 0));
    }

    public void removeExecuteWiredActionsProc() throws StdQTException {
        if (this.wiredUPP != null) {
            int i = RemoveMovieExecuteWiredActionsProc(_ID(), this.wiredUPP.ID(), 0);
            this.wiredUPP.cleanupMethodClosure();
            this.wiredUPP = null;
            StdQTException.checkError(i);
        }
    }

    public void executeWiredActions(int paramInt, AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(MovieExecuteWiredActions(_ID(), paramInt, QTObject.ID(paramAtomContainer)));
    }

    public SearchResult searchText(QTPointer paramQTPointer,
                                   int paramInt1,
                                   Track paramTrack,
                                   int paramInt2,
                                   int paramInt3) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];

        arrayOfInt1[0] = QTObject.ID(paramTrack);

        arrayOfInt2[0] = paramInt2;
        arrayOfInt3[0] = paramInt3;

        int i = MovieSearchText(_ID(),
                                QTObject.ID(paramQTPointer),
                                paramQTPointer.getSize(),
                                paramInt1,
                                arrayOfInt1,
                                arrayOfInt2,
                                arrayOfInt3);

        StdQTException.checkError(i);
        return new SearchResult(new Track(arrayOfInt1[0], this), arrayOfInt2[0], arrayOfInt3[0]);
    }

    public SearchResult searchText(QTPointer paramQTPointer, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];

        arrayOfInt1[0] = 0;
        arrayOfInt2[0] = paramInt2;
        arrayOfInt3[0] = paramInt3;

        int i = MovieSearchText(_ID(),
                                QTObject.ID(paramQTPointer),
                                paramQTPointer.getSize(),
                                paramInt1,
                                arrayOfInt1,
                                arrayOfInt2,
                                arrayOfInt3);

        StdQTException.checkError(i);
        return new SearchResult(new Track(arrayOfInt1[0], this), arrayOfInt2[0], arrayOfInt3[0]);
    }

    public QDRect getPosterBox() throws StdQTException {
        QDRect localQDRect = new QDRect();
        GetPosterBox(_ID(), localQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
        return localQDRect;
    }

    public void setPosterBox(QDRect paramQDRect) throws StdQTException {
        SetPosterBox(_ID(), paramQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
    }

    public Region getSegmentDisplayBoundsRgn(int paramInt1, int paramInt2) throws QTException {
        return Region.fromMovieSegment(this, paramInt1, paramInt2);
    }

    public StatusInfo getStatus() throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GetMovieStatus(_ID(), arrayOfInt);
        if (i != 0) {
            return new StatusInfo(i, new Track(arrayOfInt[0], this));
        }
        return null;
    }

    public void showInformation() throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        Movie.ShowMovieInformation(_ID(), 0, 0);
                    }
                    return null;
                }
            });
            StdQTException.checkError(GetMoviesError());
        } else {
            synchronized (QTNative.globalsLock) {
                ShowMovieInformation(_ID(), 0, 0);
                StdQTException.checkError(GetMoviesError());
            }
        }
    }

    public void putOnScrap(int paramInt) throws StdQTException {
        int i = PutMovieOnScrap(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public void setPlayHints(int paramInt1, int paramInt2) throws StdQTException {
        SetMoviePlayHints(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromMovie(this);
    }

    public void setMasterTimeBase(TimeBase paramTimeBase, TimeRecord paramTimeRecord) throws StdQTException {
        SetMovieMasterTimeBase(_ID(), QTObject.ID(paramTimeBase), QTObject.ID(paramTimeRecord));
        StdQTException.checkError(GetMoviesError());
    }

    public void setMasterClock(Clock paramClock, TimeRecord paramTimeRecord) throws StdQTException {
        SetMovieMasterClock(_ID(), QTObject.ID(paramClock), QTObject.ID(paramTimeRecord));
        StdQTException.checkError(GetMoviesError());
    }

    public int convertToFile(Track paramTrack,
                             QTFile paramQTFile,
                             int paramInt1,
                             int paramInt2,
                             int paramInt3) throws QTException {
        return convertToFile(paramTrack, paramQTFile, paramInt1, paramInt2, paramInt3, 0, null);
    }

    public int convertToFile(QTFile paramQTFile, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        return convertToFile(null, paramQTFile, paramInt1, paramInt2, paramInt3, 0, null);
    }

    public int convertToFile(QTFile paramQTFile,
                             int paramInt1,
                             int paramInt2,
                             int paramInt3,
                             int paramInt4) throws QTException {
        return convertToFile(null, paramQTFile, paramInt1, paramInt2, paramInt3, paramInt4, null);
    }

    public int convertToFile(final Track onlyTrack,
                             QTFile paramQTFile,
                             final int fileType,
                             final int creator,
                             final int scriptTag,
                             final int flags,
                             final MovieExporter userComp) throws QTException, StdQTException {
        final short[] resID_native = new short[1];
        final byte[] fsspec = paramQTFile.getFSSpec(false, 512);

        int i1;
        if (QTSession.isCurrentOS(4)) {
            i1 = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {
                int res1;

                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        this.res1 = Movie.ConvertMovieToFile(_ID(),
                                                             QTObject.ID(onlyTrack),
                                                             fsspec,
                                                             fileType,
                                                             creator,
                                                             (short) scriptTag,
                                                             resID_native,
                                                             flags,
                                                             QTObject.ID(userComp));
                    }

                    return this.res1;
                }
            })).intValue();
            StdQTException.checkError(i1);
            return resID_native[0];
        }
        synchronized (QTNative.globalsLock) {
            i1 = ConvertMovieToFile(_ID(),
                                    QTObject.ID(onlyTrack),
                                    paramQTFile.getFSSpec(false, 512),
                                    fileType,
                                    creator,
                                    (short) scriptTag,
                                    resID_native,
                                    flags,
                                    QTObject.ID(userComp));
        }

        StdQTException.checkError(i1);
        return resID_native[0];
    }

    public Track getQTVRTrack(int paramInt) throws QTException {
        int i = QTVRGetQTVRTrack(_ID(), paramInt);
        return i == 0 ? null : new Track(i, this);
    }

    public String toString() {
        try {
            return super.toString() + "[bounds=" + getBounds() + "]";
        } catch (StdQTException localStdQTException) {
        }
        return super.toString() + "[Error getting movie information.]";
    }

    public final boolean needsTimeTable() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = QTMovieNeedsTimeTable(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    public final int maxLoadedTimeInMovie() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GetMaxLoadedTimeInMovie(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    public void copyUserDataToMovie(Movie paramMovie, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyMovieUserData(_ID(), QTObject.ID(paramMovie), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public void copyUserDataFromMovie(Movie paramMovie, boolean paramBoolean) throws StdQTException {
        StdQTException
                .checkError(CopyMovieUserData(QTObject.ID(paramMovie), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    public static void copyUserData(Movie paramMovie1, Movie paramMovie2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(QTObject.ID(paramMovie1), QTObject.ID(paramMovie2), paramInt));
    }

    public void chooseClock(int paramInt) {
        ChooseMovieClock(_ID(), paramInt);
    }

    public void putDataRefIntoHandle(DataRef paramDataRef, QTHandle paramQTHandle) throws StdQTException {
        StdQTException.checkError(PutMovieForDataRefIntoHandle(_ID(),
                                                               QTObject.ID(paramDataRef),
                                                               paramDataRef.getType(),
                                                               QTObject.ID(paramQTHandle)));
    }

    public Movie forDataRefFromHandle(DataRef paramDataRef,
                                      QTHandle paramQTHandle,
                                      int paramInt,
                                      MovieInfo paramMovieInfo) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];

        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieForDataRefFromHandle(arrayOfInt,
                                             QTObject.ID(paramQTHandle),
                                             (short) paramInt,
                                             arrayOfByte,
                                             QTObject.ID(paramDataRef),
                                             paramDataRef.getType());

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        if (arrayOfInt[0] == 0) {
            throw new StdQTException(63526);
        }
        if (paramMovieInfo != null) {
            paramMovieInfo.setMovieInfo(0, arrayOfByte[0] == 1, null);
        }

        Object x = new Movie(arrayOfInt[0], null);
        ((Movie) x).init();
        ((Movie) x).dataRef = paramDataRef;
        return (Movie) x;
    }

    public static int getTimeUntilNextTask(int paramInt) throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(QTGetTimeUntilNextTask(arrayOfInt, paramInt));

        return arrayOfInt[0];
    }

    public static void setNextTaskNeededSoonerCallback(NextTaskNeededSooner paramNextTaskNeededSooner,
                                                       int paramInt1,
                                                       int paramInt2) throws StdQTException {
        if (paramNextTaskNeededSooner == null) {
            return;
        }
        IdleDispatcher localIdleDispatcher = new IdleDispatcher(paramNextTaskNeededSooner);

        Object localObject = dispatchers.put(paramNextTaskNeededSooner, localIdleDispatcher);
        StdQTException.checkError(QTInstallNextTaskNeededSoonerCallback(localIdleDispatcher.ID(), paramInt1, paramInt2, 0));
    }

    public static void removeNextTaskNeededSoonerCallback(NextTaskNeededSooner paramNextTaskNeededSooner) throws StdQTException {
        if (paramNextTaskNeededSooner == null) {
            return;
        }
        IdleDispatcher localIdleDispatcher = (IdleDispatcher) dispatchers.get(paramNextTaskNeededSooner);
        localIdleDispatcher.cleanupMethodClosure();
        StdQTException.checkError(QTUninstallNextTaskNeededSoonerCallback(localIdleDispatcher.ID(), 0));
    }

    private static Hashtable dispatchers = new Hashtable<>();

    public Movie flattenDataToDataRef(DataRef paramDataRef,
                                      int paramInt1,
                                      int paramInt2,
                                      int paramInt3,
                                      int paramInt4) throws QTException {
        int i = 0;
        Movie localMovie = null;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            int[] arrayOfInt1 = {
                0
            };
            int[] arrayOfInt2 = {
                0
            };
            GetMovieGWorld(_ID(), arrayOfInt1, arrayOfInt2);

            SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            i = FlattenMovieDataToDataRef(_ID(),
                                          paramInt1,
                                          QTObject.ID(paramDataRef),
                                          paramDataRef.getType(),
                                          paramInt2,
                                          (short) paramInt3,
                                          paramInt4);

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        if (i != 0) {
            localMovie = new Movie(i, null);
            localMovie.init();
            localMovie.dataRef = paramDataRef;
        }
        return localMovie;
    }

    public void setMovieAnchorDataRef(DataRef paramDataRef) throws StdQTException {
        int i = 0;
        i = SetMovieAnchorDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(i);
    }

    private boolean isMovieProgressProc() {
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        GetMovieProgressProc(_ID(), arrayOfInt1, arrayOfInt2);

        return arrayOfInt1[0] != 0;
    }

    public boolean isAnchorDefaultDataRef() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = {
            0
        };
        int[] arrayOfInt2 = {
            0
        };
        int[] arrayOfInt3 = {
            0
        };
        i = GetMovieAnchorDataRef(_ID(), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return arrayOfInt3[0] == 1;
    }

    private static native void MoviesTask(int paramInt1, int paramInt2);

    private static native short GetMoviesError();

    private static native short CreateMovieFile(byte[] paramArrayOfByte,
                                                int paramInt1,
                                                short paramShort,
                                                int paramInt2,
                                                short[] paramArrayOfShort,
                                                int[] paramArrayOfInt);

    private static native int SGGetMovie(int paramInt);

    private static native int NewMovieFromScrap(int paramInt);

    private static native short NewMovieFromFile(int[] paramArrayOfInt,
                                                 short paramShort1,
                                                 short[] paramArrayOfShort,
                                                 byte[] paramArrayOfByte1,
                                                 short paramShort2,
                                                 byte[] paramArrayOfByte2);

    private static native short NewMovieFromHandle(int[] paramArrayOfInt,
                                                   int paramInt,
                                                   short paramShort,
                                                   byte[] paramArrayOfByte);

    private static native short NewMovieFromDataFork(int[] paramArrayOfInt,
                                                     short paramShort1,
                                                     int paramInt,
                                                     short paramShort2,
                                                     byte[] paramArrayOfByte);

    private static native short NewMovieFromDataRef(int[] paramArrayOfInt,
                                                    short paramShort,
                                                    short[] paramArrayOfShort,
                                                    int paramInt1,
                                                    int paramInt2);

    private static native int NewMovie(int paramInt);

    private static native short AddMovieResource(int paramInt,
                                                 short paramShort,
                                                 short[] paramArrayOfShort,
                                                 byte[] paramArrayOfByte);

    private static native short UpdateMovieResource(int paramInt,
                                                    short paramShort1,
                                                    short paramShort2,
                                                    byte[] paramArrayOfByte);

    private static native short RemoveMovieResource(short paramShort1, short paramShort2);

    private static native short PrerollMovie(int paramInt1, int paramInt2, int paramInt3);

    private static native short LoadMovieIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native void SetMovieActive(int paramInt, byte paramByte);

    private static native byte GetMovieActive(int paramInt);

    private static native void StartMovie(int paramInt);

    private static native void StopMovie(int paramInt);

    private static native void GoToBeginningOfMovie(int paramInt);

    private static native void GoToEndOfMovie(int paramInt);

    private static native byte IsMovieDone(int paramInt);

    private static native byte GetMoviePreviewMode(int paramInt);

    private static native void SetMoviePreviewMode(int paramInt, byte paramByte);

    private static native void ShowMoviePoster(int paramInt);

    private static native void PlayMoviePreview(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetMovieGWorld(int paramInt1, int paramInt2, int paramInt3);

    private static native void GetMovieNaturalBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int GetNextTrackForCompositing(int paramInt1, int paramInt2);

    private static native int GetPrevTrackForCompositing(int paramInt1, int paramInt2);

    private static native short UpdateMovie(int paramInt);

    private static native short InvalidateMovieRegion(int paramInt1, int paramInt2);

    private static native void GetMovieBox(int paramInt, byte[] paramArrayOfByte);

    private static native void SetMovieBox(int paramInt, byte[] paramArrayOfByte);

    private static native void SetMovieDisplayClipRgn(int paramInt1, int paramInt2);

    private static native void SetMovieClipRgn(int paramInt1, int paramInt2);

    private static native short PutMovieIntoHandle(int paramInt1, int paramInt2);

    private static native short PutMovieIntoDataFork(int paramInt1, short paramShort, int paramInt2, int paramInt3);

    private static native int GetMovieCreationTime(int paramInt);

    private static native int GetMovieModificationTime(int paramInt);

    private static native int GetMovieTimeScale(int paramInt);

    private static native void SetMovieTimeScale(int paramInt1, int paramInt2);

    private static native int GetMovieDuration(int paramInt);

    private static native int GetMovieRate(int paramInt);

    private static native void SetMovieRate(int paramInt1, int paramInt2);

    private static native int GetMoviePreferredRate(int paramInt);

    private static native void SetMoviePreferredRate(int paramInt1, int paramInt2);

    private static native short GetMoviePreferredVolume(int paramInt);

    private static native void SetMoviePreferredVolume(int paramInt, short paramShort);

    private static native short GetMovieVolume(int paramInt);

    private static native void SetMovieVolume(int paramInt, short paramShort);

    private static native void GetMovieMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native void SetMovieMatrix(int paramInt, byte[] paramArrayOfByte);

    private static native void GetMoviePreviewTime(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetMoviePreviewTime(int paramInt1, int paramInt2, int paramInt3);

    private static native int GetMoviePosterTime(int paramInt);

    private static native void SetMoviePosterTime(int paramInt1, int paramInt2);

    private static native void GetMovieSelection(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void SetMovieSelection(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetMovieActiveSegment(int paramInt1, int paramInt2, int paramInt3);

    private static native void GetMovieActiveSegment(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native int GetMovieTime(int paramInt1, int paramInt2);

    private static native void SetMovieTime(int paramInt1, int paramInt2);

    private static native void SetMovieTimeValue(int paramInt1, int paramInt2);

    private static native int GetMovieTrackCount(int paramInt);

    private static native int GetMovieTrack(int paramInt1, int paramInt2);

    private static native int GetMovieIndTrack(int paramInt1, int paramInt2);

    private static native int GetMovieIndTrackType(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int GetTrackMovie(int paramInt);

    private static native void DisposeMovieTrack(int paramInt);

    private static native void SetAutoTrackAlternatesEnabled(int paramInt, byte paramByte);

    private static native void SelectMovieAlternates(int paramInt);

    private static native void GetMovieNextInterestingTime(int paramInt1,
                                                           short paramShort1,
                                                           short paramShort2,
                                                           int[] paramArrayOfInt1,
                                                           int paramInt2,
                                                           int paramInt3,
                                                           int[] paramArrayOfInt2,
                                                           int[] paramArrayOfInt3);

    private static native short InsertEmptyMovieSegment(int paramInt1, int paramInt2, int paramInt3);

    private static native short InsertMovieSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short DeleteMovieSegment(int paramInt1, int paramInt2, int paramInt3);

    private static native short ScaleMovieSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int CutMovieSelection(int paramInt);

    private static native int CopyMovieSelection(int paramInt);

    private static native void PasteMovieSelection(int paramInt1, int paramInt2);

    private static native void AddMovieSelection(int paramInt1, int paramInt2);

    private static native void ClearMovieSelection(int paramInt);

    private static native short PasteHandleIntoMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native short PutMovieIntoTypedHandle(int paramInt1,
                                                        int paramInt2,
                                                        int paramInt3,
                                                        int paramInt4,
                                                        int paramInt5,
                                                        int paramInt6,
                                                        int paramInt7,
                                                        int paramInt8);

    private static native short CopyMovieSettings(int paramInt1, int paramInt2);

    private static native short AddEmptyTrackToMovie(int paramInt1,
                                                     int paramInt2,
                                                     int paramInt3,
                                                     int paramInt4,
                                                     int[] paramArrayOfInt);

    private static native int NewMovieEditState(int paramInt);

    private static native short UseMovieEditState(int paramInt1, int paramInt2);

    private static native int GetMovieDataSize(int paramInt1, int paramInt2, int paramInt3);

    private static native byte PtInMovie(int paramInt1, int paramInt2);

    private static native void SetMovieLanguage(int paramInt1, int paramInt2);

    private static native byte HasMovieChanged(int paramInt);

    private static native void ClearMovieChanged(int paramInt);

    private static native short SetMovieDefaultDataRef(int paramInt1, int paramInt2, int paramInt3);

    private static native short SetMovieColorTable(int paramInt1, int paramInt2);

    private static native void FlattenMovie(int paramInt1,
                                            int paramInt2,
                                            byte[] paramArrayOfByte1,
                                            int paramInt3,
                                            short paramShort,
                                            int paramInt4,
                                            short[] paramArrayOfShort,
                                            byte[] paramArrayOfByte2);

    private static native int FlattenMovieData(int paramInt1,
                                               int paramInt2,
                                               byte[] paramArrayOfByte,
                                               int paramInt3,
                                               short paramShort,
                                               int paramInt4);

    private static native void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    private static native void SetMovieProgressProc(int paramInt1, int paramInt2, int paramInt3);

    private static native void GetMovieProgressProc(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short MovieSearchText(int paramInt1,
                                                int paramInt2,
                                                int paramInt3,
                                                int paramInt4,
                                                int[] paramArrayOfInt1,
                                                int[] paramArrayOfInt2,
                                                int[] paramArrayOfInt3);

    private static native void GetPosterBox(int paramInt, byte[] paramArrayOfByte);

    private static native void SetPosterBox(int paramInt, byte[] paramArrayOfByte);

    private static native int GetMovieStatus(int paramInt, int[] paramArrayOfInt);

    private static native void ShowMovieInformation(int paramInt1, int paramInt2, int paramInt3);

    private static native short PutMovieOnScrap(int paramInt1, int paramInt2);

    private static native void SetMoviePlayHints(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetMovieMasterTimeBase(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetMovieMasterClock(int paramInt1, int paramInt2, int paramInt3);

    private static native short ConvertMovieToFile(int paramInt1,
                                                   int paramInt2,
                                                   byte[] paramArrayOfByte,
                                                   int paramInt3,
                                                   int paramInt4,
                                                   short paramShort,
                                                   short[] paramArrayOfShort,
                                                   int paramInt5,
                                                   int paramInt6);

    private static native int QTVRGetQTVRTrack(int paramInt1, int paramInt2);

    private static native short QTMovieNeedsTimeTable(int paramInt, byte[] paramArrayOfByte);

    private static native short GetMaxLoadedTimeInMovie(int paramInt, int[] paramArrayOfInt);

    private static native short PrePrerollMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native void AbortPrePrerollMovie(int paramInt, short paramShort);

    private static native void SetMovieDrawingCompleteProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native short AddMovieExecuteWiredActionsProc(int paramInt1, int paramInt2, int paramInt3);

    private static native short RemoveMovieExecuteWiredActionsProc(int paramInt1, int paramInt2, int paramInt3);

    private static native short MovieExecuteWiredActions(int paramInt1, int paramInt2, int paramInt3);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native void GetMovieGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short CopyMovieUserData(int paramInt1, int paramInt2, int paramInt3);

    private static native void ChooseMovieClock(int paramInt1, int paramInt2);

    private static native short NewMovieForDataRefFromHandle(int[] paramArrayOfInt,
                                                             int paramInt1,
                                                             short paramShort,
                                                             byte[] paramArrayOfByte,
                                                             int paramInt2,
                                                             int paramInt3);

    private static native short PutMovieForDataRefIntoHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int FlattenMovieDataToDataRef(int paramInt1,
                                                        int paramInt2,
                                                        int paramInt3,
                                                        int paramInt4,
                                                        int paramInt5,
                                                        short paramShort,
                                                        int paramInt6);

    private static native short QTGetTimeUntilNextTask(int[] paramArrayOfInt, int paramInt);

    private static native short QTInstallNextTaskNeededSoonerCallback(int paramInt1,
                                                                      int paramInt2,
                                                                      int paramInt3,
                                                                      int paramInt4);

    private static native short QTUninstallNextTaskNeededSoonerCallback(int paramInt1, int paramInt2);

    private static native short SetMovieAnchorDataRef(int paramInt1, int paramInt2, int paramInt3);

    private static native short GetMovieAnchorDataRef(int paramInt,
                                                      int[] paramArrayOfInt1,
                                                      int[] paramArrayOfInt2,
                                                      int[] paramArrayOfInt3);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/Movie.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
