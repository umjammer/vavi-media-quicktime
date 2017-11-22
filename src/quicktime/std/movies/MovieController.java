
package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Vector;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.ColorTable;
import quicktime.qd.QDDimension;
import quicktime.qd.QDException;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.comp.Component;
import quicktime.util.QTUtils;
import quicktime.util.StringHandle;


public class MovieController extends Component implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(MovieController.class);
                    }
                });
            }
        }.establish();
    }

    private static final boolean isMacOS = QTSession.isCurrentOS(1);

    public MovieController(Movie paramMovie) throws QTException {
        this(paramMovie, 0);
    }

    public MovieController(Movie paramMovie, int paramInt) throws QTException {
        super(allocate(paramMovie, paramInt), null);
        if (QTSession.isCurrentOS(4))
            setDragEnabled(false);
        this.currentPort = QTObject.ID(QDGraphics.scratch);
        SetGWorld(this.currentPort, 0);
        initControllerSize();
        this.movieRef = paramMovie;
        if (currentSessionID != this.sessionAllocated) {
            mcHashtable.clear();
            currentSessionID = this.sessionAllocated;
        }
        Vector<Movie> localVector = new Vector<>();
        localVector.addElement(this.movieRef);
        mcHashtable.put(_ID(), localVector);
    }

    public MovieController(int paramInt,
            Movie paramMovie,
            QDGraphics paramQDGraphics,
            QDPoint paramQDPoint) throws QTException {
        super(1886151033, paramInt);

        int i = 0;
        if ((!QTSession.isCurrentOS(4)) && (paramQDGraphics == null))
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            this.currentPort = QTObject.ID(paramQDGraphics);
            SetGWorld(this.currentPort, 0);

            if (QTSession.isCurrentOS(4)) {
                i = MCNewAttachedController(_ID(), QTObject.ID(paramMovie), 0, paramQDPoint.getPoint());

            } else {
                i = MCNewAttachedController(_ID(),
                                            QTObject.ID(paramMovie),
                                            QTObject.ID(paramQDGraphics),
                                            paramQDPoint.getPoint());
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        initControllerSize();
        if (QTSession.isCurrentOS(4))
            setDragEnabled(false);
        this.movieRef = paramMovie;
        if (currentSessionID != this.sessionAllocated) {
            mcHashtable.clear();
            currentSessionID = this.sessionAllocated;
        }
        Vector<Movie> x = new Vector<>();
        x.addElement(this.movieRef);
        mcHashtable.put(new Integer(_ID()), x);
    }

    private static final int[] savedPort = {
        0
    };

    private static final int[] savedDevice = {
        0
    };

    private int currentPort = 0;

    private static int allocate(Movie paramMovie, int paramInt) throws StdQTException, QDException {
        QDRect localQDRect = paramMovie.getBounds();
        int i = 0;

        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            i = NewMovieController(QTObject.ID(paramMovie), localQDRect.getRect(), paramInt | 0x1);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(GetMoviesError());

        return i;
    }

    MovieController(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
        if (paramObject == null) {
            throw new QTRuntimeException("Wrong use of Constructor");
        }
        try {
            if (QTSession.isCurrentOS(4))
                setDragEnabled(false);
        } catch (QTException localQTException) {
            throw new QTRuntimeException(localQTException.getMessage());
        }
    }

    protected static final Object mm(int paramInt, Object paramObject) {
        return new Movie(paramInt, paramObject);
    }

    static Hashtable<Integer, Vector<Movie>> mcHashtable = new Hashtable<>();

    private static int currentSessionID;

    private Movie movieRef = null;

    private MoviesDispatcher uppRef = null;

    private int cWidth;

    private int cHeight;

    private long lastFlush;

    protected void _dispose() throws QTException {
        Vector<Movie> localObject = mcHashtable.remove(_ID());
        if (localObject != null)
            localObject.removeAllElements();
    }

    private void initControllerSize() throws StdQTException {
        QDRect localQDRect1 = getMovie().getBounds();
        QDRect localQDRect2 = getBounds();
        this.cWidth = (localQDRect2.getWidth() - localQDRect1.getWidth());
        this.cHeight = (localQDRect2.getHeight() - localQDRect1.getHeight());
    }

    public final QDDimension getRequiredSize() {
        return new QDDimension(this.cWidth, this.cHeight);
    }

    public final void idle() throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if (QTSession.isInitialized()) {
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                    GetGWorld(savedPort, savedDevice);
                }
                SetGWorld(this.currentPort, 0);

                QDGraphics localQDGraphics = getPort();
                Region localRegion = localQDGraphics.getVisClipRgn();

                i = MCIdle(_ID());

                if ((QTSession.getJavaVersion() == 65539) && (QTSession.isCurrentOS(4))) {
                    long l = System.currentTimeMillis();
                    if (l - this.lastFlush > this.deltaTime) {
                        QDFlushPortBuffer(this.currentPort, QTObject.ID(localRegion));
                        this.lastFlush = System.currentTimeMillis();
                    }
                }
                if (this.debug) {
                    System.out.println("idle:" + (System.currentTimeMillis() - this.msecs) + "msecs");
                    this.msecs = System.currentTimeMillis();
                }
                if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                    SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        if (i < 0) {
            StdQTException.checkError(GetMoviesError());
        }
    }

    private long deltaTime = 50L;

    private long msecs = 0L;

    private boolean debug = false;

    public final void setActionFilter(ActionFilter paramActionFilter) throws StdQTException {
        setActionFilter(paramActionFilter, false);
    }

    public final void setActionFilter(ActionFilter paramActionFilter, boolean paramBoolean) throws StdQTException {
        if (paramActionFilter == null) {
            removeActionFilter();
            return;
        }
        if (this.uppRef != null) {
            removeActionFilter();
        }
        this.uppRef = new MoviesDispatcher(this, paramActionFilter, paramBoolean);
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetActionFilterWithRefCon(_ID(), this.uppRef.ID(), 0));
        }
    }

    public final void removeActionFilter() throws StdQTException {
        if (this.uppRef != null)
            this.uppRef.cleanupMethodClosure();
        this.uppRef = null;
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetActionFilterWithRefCon(_ID(), 0, 0));
        }
    }

    public void setMovie(Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws StdQTException {
        StdQTException.checkError(MCRemoveMovie(_ID()));
        synchronized (QTNative.globalsLock) {
            if (QTSession.isCurrentOS(4)) {
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(paramMovie), 0, paramQDPoint.getPoint()));

                setPort(paramQDGraphics);
            } else {
                if ((paramMovie != null) && (paramQDGraphics == null)) {
                    throw new StdQTException(-50);
                }
                StdQTException
                        .checkError(MCSetMovie(_ID(),
                                               QTObject.ID(paramMovie),
                                               QTObject.ID(paramQDGraphics),
                                               paramQDPoint.getPoint()));
            }
        }

        Vector<Movie> x = mcHashtable.get(new Integer(_ID()));
        if (this.movieRef != null && x != null) {
            x.removeElement(this.movieRef);
        }
        this.movieRef = paramMovie;
        if (this.movieRef != null && x != null) {
            x.addElement(this.movieRef);
        }
    }

    public final Movie getMovie() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetIndMovie(_ID(), (short) 0);
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, this);
    }

    public void removeMovie() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveMovie(_ID()));
        }
        this.movieRef = null;
        Vector<Movie> x = mcHashtable.get(new Integer(_ID()));
        if ((this.movieRef != null) && (x != null)) {
            x.removeElement(this.movieRef);
        }
    }

    public final void draw() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(this.currentPort, 0);

            if (QTSession.isCurrentOS(4)) {
                i = MCDraw(_ID(), 0);
            } else
                i = MCDraw(_ID(), this.currentPort);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
    }

    public final boolean click(QDPoint paramQDPoint, int paramInt) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
                SetGWorld(MCGetControllerPort(_ID()), 0);
            }

            if (QTSession.isCurrentOS(4)) {
                i = MCClick(_ID(), 0, paramQDPoint.getPoint(), TickCount(), QTUtils.convertEventModifier(paramInt));

            } else {

                i = MCClick(_ID(),
                            this.currentPort,
                            paramQDPoint.getPoint(),
                            TickCount(),
                            QTUtils.convertEventModifier(paramInt));
            }

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    public final boolean key(int paramInt1, int paramInt2) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(this.currentPort, 0);
            i = MCKey(_ID(), (byte) QTUtils.convertKeyValue(paramInt1), QTUtils.convertEventModifier(paramInt2));

            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    private final boolean doAction(short paramShort, int[] paramArrayOfInt) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCDoAction(_ID(), paramShort, paramArrayOfInt);
        }
        StdQTException.checkError(i);
        return i != 0;
    }

    private final boolean doAction(short paramShort, int paramInt) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCDoAction(_ID(), paramShort, paramInt);
        }
        StdQTException.checkError(i);
        return i != 0;
    }

    private final boolean doAction(short paramShort, short[] paramArrayOfShort) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCDoAction(_ID(), paramShort, paramArrayOfShort);
        }
        StdQTException.checkError(i);
        return i != 0;
    }

    private final boolean doAction(short paramShort1, short paramShort2) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCDoAction(_ID(), paramShort1, paramShort2);
        }
        StdQTException.checkError(i);
        return i != 0;
    }

    private final boolean doAction(short paramShort, byte[] paramArrayOfByte) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCDoAction(_ID(), paramShort, paramArrayOfByte);
        }
        StdQTException.checkError(i);
        return i != 0;
    }

    private final boolean doAction(short paramShort, byte paramByte) throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCDoAction(_ID(), paramShort, paramByte);
        }
        StdQTException.checkError(i);
        return i != 0;
    }

    public final void activate() throws StdQTException {
        doAction((short) 3, 0);
    }

    public final void deactivate() throws StdQTException {
        doAction((short) 4, 0);
    }

    public final void play(float paramFloat) throws StdQTException {
        doAction((short) 8, QTUtils.X2Fix(paramFloat));
    }

    public final void goToTime(TimeRecord paramTimeRecord) throws StdQTException {
        doAction((short) 12, QTObject.ID(paramTimeRecord));
    }

    public final void setVolume(float paramFloat) throws StdQTException {
        doAction((short) 14, QTUtils.X2ShortFix(paramFloat));
    }

    public final float getVolume() throws StdQTException {
        short[] arrayOfShort = new short[1];
        doAction((short) 15, arrayOfShort);
        return QTUtils.ShortFix2X(arrayOfShort[0]);
    }

    public final void step(int paramInt) throws StdQTException {
        doAction((short) 18, (short) paramInt);
    }

    public final void setLooping(boolean paramBoolean) throws StdQTException {
        doAction((short) 21, (byte) (paramBoolean ? 1 : 0));
    }

    public final boolean getLooping() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 22, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final void setLoopIsPalindrome(boolean paramBoolean) throws StdQTException {
        doAction((short) 23, (byte) (paramBoolean ? 1 : 0));
    }

    public final boolean getLoopIsPalindrome() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 22, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final void setGrowBoxBounds(QDRect paramQDRect) throws StdQTException {
        doAction((short) 25, paramQDRect.getRect());
    }

    public final void setSelectionBegin(TimeRecord paramTimeRecord) throws StdQTException {
        doAction((short) 29, QTObject.ID(paramTimeRecord));
    }

    public final void setSelectionDuration(TimeRecord paramTimeRecord) throws StdQTException {
        doAction((short) 30, QTObject.ID(paramTimeRecord));
    }

    public final void setKeysEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 32, (byte) (paramBoolean ? 1 : 0));
    }

    public final boolean getKeysEnabled() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 33, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final void setPlaySelection(boolean paramBoolean) throws StdQTException {
        doAction((short) 34, (byte) (paramBoolean ? 1 : 0));
    }

    public final boolean getPlaySelection() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 35, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final void setUseBadge(boolean paramBoolean) throws StdQTException {
        doAction((short) 36, (byte) (paramBoolean ? 1 : 0));
    }

    public final boolean getUseBadge() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 37, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final void setFlags(int paramInt) throws StdQTException {
        doAction((short) 38, paramInt);
    }

    public final int getFlags() throws StdQTException {
        int[] arrayOfInt = new int[1];
        doAction((short) 39, arrayOfInt);
        return arrayOfInt[0];
    }

    public final void setPlayEveryFrame(boolean paramBoolean) throws StdQTException {
        doAction((short) 40, (byte) (paramBoolean ? 1 : 0));
    }

    public final boolean getPlayEveryFrame() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 41, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final float getPlayRate() throws StdQTException {
        int[] arrayOfInt = new int[1];
        doAction((short) 42, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    public final void suspend() throws StdQTException {
        doAction((short) 46, 0);
    }

    public final void resume() throws StdQTException {
        doAction((short) 47, 0);
    }

    public final void setControllerKeysEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 48, (byte) (paramBoolean ? 1 : 0));
    }

    public final QDRect getTimeSliderRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        doAction((short) 49, localQDRect.getRect());
        return localQDRect;
    }

    public final boolean getDragEnabled() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 51, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    public final void setDragEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 52, (byte) (paramBoolean ? 1 : 0));
    }

    public final TimeRecord getSelectionBegin() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        doAction((short) 53, QTObject.ID(localTimeRecord));
        return localTimeRecord;
    }

    public final TimeRecord getSelectionDuration() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        doAction((short) 54, QTObject.ID(localTimeRecord));
        return localTimeRecord;
    }

    public final void prerollAndPlay(float paramFloat) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            SetGWorld(this.currentPort, 0);
            doAction((short) 55, QTUtils.X2Fix(paramFloat));
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    public final boolean getCursorSettingEnabled() throws StdQTException {
        int[] arrayOfInt = new int[1];
        doAction((short) 56, arrayOfInt);
        return arrayOfInt[0] == 1;
    }

    public final void setCursorSettingEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 57, (byte) (paramBoolean ? 1 : 0));
    }

    public final void setColorTable(ColorTable paramColorTable) throws StdQTException {
        doAction((short) 58, QTObject.ID(paramColorTable));
    }

    public final void controllerSizeChanged() throws StdQTException {
        doAction((short) 26, 0);
    }

    public final void badgeClick(boolean paramBoolean) throws StdQTException {
        doAction((short) 44, (byte) (paramBoolean ? 1 : 0));
    }

    public final void movieEdited() throws StdQTException {
        doAction((short) 50, 0);
    }

    public final void forceTimeTableUpdate() throws StdQTException {
        doAction((short) 61, 0);
    }

    public final void linkToURL(String paramString) throws QTException {
        if (paramString.startsWith("file:")) {
            Object localObject = System.getSecurityManager();
            if (localObject != null) {
                ((SecurityManager) localObject).checkRead(paramString);
            }
        }
        Object localObject = new StringHandle(paramString, 1);
        doAction((short) 59, QTObject.ID((QTObject) localObject));
    }

    public final boolean clickAndHoldPoint(QDPoint paramQDPoint) throws StdQTException {
        return doAction((short) 67, paramQDPoint.getPoint());
    }

    public final void setAttached(boolean paramBoolean) throws QTException {
        synchronized (QTNative.globalsLock) {
            int i = MCSetControllerAttached(_ID(), (byte) (paramBoolean ? 1 : 0));
            StdQTException.checkError(i);
        }
    }

    public final boolean isAttached() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCIsControllerAttached(_ID());
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    public final void setBounds(QDRect paramQDRect) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetControllerBoundsRect(_ID(), paramQDRect.getRect()));
        }
    }

    public final QDRect getBounds() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            QDRect localQDRect = new QDRect();
            StdQTException.checkError(MCGetControllerBoundsRect(_ID(), localQDRect.getRect()));
            return localQDRect;
        }
    }

    public final void setPort(QDGraphics paramQDGraphics) throws StdQTException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetControllerPort(_ID(), QTObject.ID(paramQDGraphics)));
        }

        this.currentPort = QTObject.ID(paramQDGraphics);
    }

    public final void setVisible(boolean paramBoolean) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetVisible(_ID(), (byte) (paramBoolean ? 1 : 0)));
        }
    }

    public final boolean getVisible() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetVisible(_ID());
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    public final void movieChanged() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = MCGetIndMovie(_ID(), (short) 0);
            StdQTException.checkError(MCMovieChanged(_ID(), i));
        }
    }

    public final void setDuration(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetDuration(_ID(), paramInt));
        }
    }

    public final int getCurrentTime() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = 0;
        int j = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetCurrentTime(_ID(), arrayOfInt);
        }
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    public final int getTimeScale() throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            MCGetCurrentTime(_ID(), arrayOfInt);
        }
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    public final void activate(QDGraphics paramQDGraphics, boolean paramBoolean) throws StdQTException, QDException {
        if ((!QTSession.isCurrentOS(4)) && (paramQDGraphics == null))
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            int i = MCActivate(_ID(), QTObject.ID(paramQDGraphics), (byte) (paramBoolean ? 1 : 0));
            StdQTException.checkError(i);
        }
    }

    public final void enableEditing(boolean paramBoolean) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCEnableEditing(_ID(), (byte) (paramBoolean ? 1 : 0)));
        }
    }

    public final boolean isEditingEnabled() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCIsEditingEnabled(_ID());
        }
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    public final Movie copy() throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCCopy(_ID());
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    public final Movie cut() throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCCut(_ID());
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    public final void paste() throws StdQTException {
        paste(null);
    }

    public final void paste(Movie paramMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPaste(_ID(), QTObject.ID(paramMovie)));
        }
    }

    public final void clear() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCClear(_ID()));
        }
    }

    public final void undo() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCUndo(_ID()));
        }
    }

    public final void position(QDRect paramQDRect1, QDRect paramQDRect2, int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPositionController(_ID(), paramQDRect1.getRect(), paramQDRect2.getRect(), paramInt));
        }
    }

    public final void position(QDRect paramQDRect, int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPositionController(_ID(), paramQDRect.getRect(), null, paramInt));
        }
    }

    public final int getControllerInfo() throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCGetControllerInfo(_ID(), arrayOfInt));
        }
        return arrayOfInt[0];
    }

    public final void setClip(Region paramRegion) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetClip(_ID(), QTObject.ID(paramRegion), 0));
        }
    }

    public final void setMovieClip(Region paramRegion) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetClip(_ID(), 0, QTObject.ID(paramRegion)));
        }
    }

    public final boolean inController(QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPtInController(_ID(), paramQDPoint.getPoint(), arrayOfByte));
        }
        return arrayOfByte[0] == 1;
    }

    public final void invalidate(QDGraphics paramQDGraphics, Region paramRegion) throws StdQTException, QDException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            int i = MCInvalidate(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramRegion));
            StdQTException.checkError(i);
        }
    }

    /* Error */
    public final QDGraphics getPort() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            return QDGraphics.fromMovieController(this);
        }
    }

    /* Error */
    public final Region getBoundsRgn() throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerBounds(this);
        }
    }

    /* Error */
    public final Region getWindowRgn(QDGraphics paramQDGraphics) throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerWindow(this, paramQDGraphics);
        }
    }

    /* Error */
    public final Region getClip() throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerClip(this);
        }
    }

    /* Error */
    public final Region drawBadge(Region region, boolean flag) throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerBadge(this, region, flag);
        }
    }

    private static native void QDFlushPortBuffer(int paramInt1, int paramInt2);

    private static native int MCNewAttachedController(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int NewMovieController(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

    private static native short GetMoviesError();

    private static native int MCIdle(int paramInt);

    private static native int MCSetActionFilterWithRefCon(int paramInt1, int paramInt2, int paramInt3);

    private static native int MCRemoveMovie(int paramInt);

    private static native int MCSetMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int MCGetIndMovie(int paramInt, short paramShort);

    private static native int MCGetControllerPort(int paramInt);

    private static native int MCClick(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    private static native int TickCount();

    private static native int MCKey(int paramInt1, byte paramByte, int paramInt2);

    private static native int MCDoAction(int paramInt, short paramShort, int[] paramArrayOfInt);

    private static native int MCDoAction(int paramInt1, short paramShort, int paramInt2);

    private static native int MCDoAction(int paramInt, short paramShort, short[] paramArrayOfShort);

    private static native int MCDoAction(int paramInt, short paramShort1, short paramShort2);

    private static native int MCDoAction(int paramInt, short paramShort, byte paramByte);

    private static native int MCDoAction(int paramInt, short paramShort, byte[] paramArrayOfByte);

    private static native int MCSetControllerAttached(int paramInt, byte paramByte);

    private static native int MCIsControllerAttached(int paramInt);

    private static native int MCSetControllerBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int MCGetControllerBoundsRect(int paramInt, byte[] paramArrayOfByte);

    private static native int MCSetControllerPort(int paramInt1, int paramInt2);

    private static native int MCSetVisible(int paramInt, byte paramByte);

    private static native int MCGetVisible(int paramInt);

    private static native int MCMovieChanged(int paramInt1, int paramInt2);

    private static native int MCSetDuration(int paramInt1, int paramInt2);

    private static native int MCGetCurrentTime(int paramInt, int[] paramArrayOfInt);

    private static native int MCActivate(int paramInt1, int paramInt2, byte paramByte);

    private static native int MCEnableEditing(int paramInt, byte paramByte);

    private static native int MCIsEditingEnabled(int paramInt);

    private static native int MCCopy(int paramInt);

    private static native int MCCut(int paramInt);

    private static native int MCPaste(int paramInt1, int paramInt2);

    private static native int MCClear(int paramInt);

    private static native int MCUndo(int paramInt);

    private static native int MCPositionController(int paramInt1,
                                                   byte[] paramArrayOfByte1,
                                                   byte[] paramArrayOfByte2,
                                                   int paramInt2);

    private static native int MCGetControllerInfo(int paramInt, int[] paramArrayOfInt);

    private static native int MCSetClip(int paramInt1, int paramInt2, int paramInt3);

    private static native int MCPtInController(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

    private static native int MCInvalidate(int paramInt1, int paramInt2, int paramInt3);

    private static native int MCDraw(int paramInt1, int paramInt2);

    private static native void SetGWorld(int paramInt1, int paramInt2);

    private static native void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/MovieController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
