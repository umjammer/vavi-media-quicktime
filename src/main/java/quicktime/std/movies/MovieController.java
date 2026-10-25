package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.internal.toolbox.ClockManager;
import quicktime.internal.toolbox.MovieControllerManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.QuickDrawManager;
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

/**
 * The MovieController class implements the corresponding data structure of the
 * Movie Toolbox. MovieController allows your application to play and control
 * movies with a standard interface. You obtain this identifier from the Component
 * Manager's OpenComponent or OpenDefaultComponent function, or from the
 * NewMovieController function (described on page 2-29 of Inside Macintosh:
 * QuickTime Components).
 *
 * If an application installs an ActionFilter callback, then it must remove this callback
 * when it has finished with it to allow the native structures used in this callback to be properly disposed.
 */
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

    /**
     * This method locates a movie controller component for you and assigns a movie to
     * that controller. This function always creates a controller that is visible and attached to
     * a movie.
     * QuickTime::NewMovieController()
     * @param itsMovie Identifies the movie to be associated with the movie controller.
     */
    public MovieController(Movie paramMovie) throws QTException {
        this(paramMovie, 0);
    }

    /**
     * This method locates a movie controller component for you and assigns a movie to
     * that controller. This function always creates a controller that is attached to
     * a movie.
     * QuickTime::NewMovieController()
     * @param itsMovie Identifies the movie to be associated with the movie controller.
     * @param someFlags Contains flags that control the operation.
     */
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

    /**
     * This method constructs a MovieController object for a given subType. It attaches the controller
     * to the specifed movie in the specifed window.
     * QuickTime::MCNewAttachedController()
     * @param subType the sub type of the MovieController.kType component that you wish to open
     * @param theMovie Specifies the movie to be associated with the movie controller.
     * @param window Identifies the window in which the movie is to be displayed. This may NOT be null.
     * @param location Specifies the upper-left corner of the movie within the window specified by the w parameter.
     */
    public MovieController(int paramInt, Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws QTException {
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
                i = MCNewAttachedController(_ID(), QTObject.ID(paramMovie), QTObject.ID(paramQDGraphics), paramQDPoint.getPoint());
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

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

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

    /**
     * This returns the actual size of the controller itself - how many pixels high and wide that the
     * controller takes up. This size is independent of the actual display size of the movie and the
     * controller and represents the default number of pixels that are required in horizontal and
     * vertical dimensions to display the actual controller.
     * @return the minimum size for the controller
     */
    public final QDDimension getRequiredSize() {
        return new QDDimension(this.cWidth, this.cHeight);
    }

    /**
     * This method performs idle processing for a movie controller.
     * This idle processing includes calling the Movie Toolbox's MoviesTask
     * function for each movie that is associated with the controller.
     * QuickTime::MCIdle()
     */
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

    /**
     * Registers a class that can provide a filter function for any action the MovieController takes.
     * By default the filter will not be called with idle actions. If the filter is null the
     * current idleFilter will be removed.
     * QuickTime::MCSetActionFilterWithRefCon()
     * @param filter the filter that you use
     * @param {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
    public final void setActionFilter(ActionFilter paramActionFilter) throws StdQTException {
        setActionFilter(paramActionFilter, false);
    }

    /**
     * Registers a class that can provide a filter function for any action the MovieController takes.
     * If the filter is null the current idleFilter will be removed.
     * QuickTime::MCSetActionFilterWithRefCon()
     * @param filter the filter that you use
     * @param {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * Removes the current action filter from the MovieController.
     * QuickTime::MCSetActionFilterWithRefCon()
     */
    public final void removeActionFilter() throws StdQTException {
        if (this.uppRef != null)
            this.uppRef.cleanupMethodClosure();
        this.uppRef = null;
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetActionFilterWithRefCon(_ID(), 0, 0));
        }
    }

    /**
     * This method replaces the current movie controlled by the movie controller with a
     * new movie. When calling this method, you must supply a valid QDGraphics object for
     * the movieWindow parameter. To remove a movie, call removeMovie. To control multiple
     * movies, use the MultiMovieController class.
     * QuickTime::MCSetMovie()
     * @param aMovie Identifies the movie to be associated with the movie controller.
     * @param movieWindow Identifies the window in which the movie is to be displayed. This may NOT be null unless the movie is also null.
     * @param location Specifies the upper-left corner of the movie within the window
     *                     specified by the movieWindow parameter.
     * @param {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.MultiMovieController}
     */
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
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(paramMovie), QTObject.ID(paramQDGraphics), paramQDPoint.getPoint()));
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

    /**
     * This method allows you to get the movie associated with controller.
     * QuickTime::MCGetIndMovie()
     * @return a Movie object.
     * @return {@link quicktime.std.StdQTException}
     */
    public final Movie getMovie() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetIndMovie(_ID(), (short) 0);
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, this);
    }

    /**
     * This method allows you to remove the movie of the controller.
     * QuickTime::MCRemoveMovie()
     */
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

    /**
     * Your application should call the draw method in response to an update event
     * The movie controller component updates the movie controller if the controller is in the same window
     * with the Canvas object. The controller component updates the movie associated with
     * the controller only if the movie is contained in the frame of the Canvas object.
     * QuickTime::MCDraw()
     */
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

    /**
     * Your application should call the method when the user clicks in a movie controller window.
     * QuickTime::MCClick()
     * @param where a QDPoint indicating the location of the click
     * @param modifiers specifies java-defined modifier flags for the event
     * @param {@link quicktime.std.StdQTException}
     */
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
                i = MCClick(_ID(), this.currentPort, paramQDPoint.getPoint(), TickCount(), QTUtils.convertEventModifier(paramInt));
            }
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    /**
     * This method handles keyboard events for a movie controller. You can call this method
     * only if you have enabled keystroke processing in the controller. By default, keystroke
     * processing is turned off when you create a movie controller. You can enable and disable
     * keystroke processing using the setKeysEnabled method.
     * QuickTime::MCKey()
     * @param key specifies the java-defined keystroke
     * @param modifiers specifies java-defined modifier flags for the event
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method activates the movie controller.
     * QuickTime::MCDoAction()
     */
    public final void activate() throws StdQTException {
        doAction((short) 3, 0);
    }

    /**
     * This method deactivates the movie controller.
     * QuickTime::MCDoAction()
     */
    public final void deactivate() throws StdQTException {
        doAction((short) 4, 0);
    }

    /**
     * Your application can use this action to start or stop playing a movie.
     * QuickTime::MCDoAction()
     * @param rate Is a fixed value that indicates the rate of play.
     *  Values greater than 0 correspond to forward rates;
     *  values less than 0 play the movie backward. A value of 0 stops the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void play(float paramFloat) throws StdQTException {
        doAction((short) 8, QTUtils.X2Fix(paramFloat));
    }

    /**
     * Your application can use this action to goto a specific time.
     * QuickTime::MCDoAction()
     * @param time Specifies the time to go.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void goToTime(TimeRecord paramTimeRecord) throws StdQTException {
        doAction((short) 12, QTObject.ID(paramTimeRecord));
    }

    /**
     * Your application can use this action to set a movie's volume.
     * QuickTime::MCDoAction()
     */
    public final void setVolume(float paramFloat) throws StdQTException {
        doAction((short) 14, QTUtils.X2ShortFix(paramFloat));
    }

    /**
     * Your application can use this action to determine a movie's volume.
     * QuickTime::MCDoAction()
     * @return a 16-bit, fixed-point number that indicates the relative volume
     *  of the movie. Volume values range from Ð1.0 to 1.0. Negative values play
     *  no sound but preserve the absolute value of the volume setting.
     * @return {@link quicktime.std.StdQTException}
     */
    public final float getVolume() throws StdQTException {
        short[] arrayOfShort = new short[1];
        doAction((short) 15, arrayOfShort);
        return QTUtils.ShortFix2X(arrayOfShort[0]);
    }

    /**
     * Your application can use this action to play a movie while
     * skipping a specified number of frames at a time.
     * QuickTime::MCDoAction()
     * @param numberOfSteps value that specifies the number of steps
     *  (that is, the frames and the play direction). Positive values
     *  step the movie forward the specified number of frames; negative values
     *  step the movie backward. A value of 0 steps the movie forward one frame.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void step(int paramInt) throws StdQTException {
        doAction((short) 18, (short) paramInt);
    }

    /**
     * Your application can use this action to enable or disable looping for a movie.
     * QuickTime::MCDoAction()
     * @param looping a value of true indicates that looping is to be enabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setLooping(boolean paramBoolean) throws StdQTException {
        doAction((short) 21, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this action to determine whether a movie is looping.
     * QuickTime::MCDoAction()
     * @return true if looping is enabled for the movie that is assigned to this controller.
     *  Otherwise, it is false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getLooping() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 22, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this method to enable palindrome looping.
     * Palindrome looping causes a movie to play alternately forward and backward.
     * Looping must also be enabled for palindrome looping to take effect.
     * QuickTime::MCDoAction()
     * @param looping The parameter data must contain a Boolean valueÑa value
     *  of true indicates that palindrome looping is to be enabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setLoopIsPalindrome(boolean paramBoolean) throws StdQTException {
        doAction((short) 23, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this action to determine whether palindrome
     * looping is enabled for a movie. Looping must also be enabled for palindrome
     * looping to take effect.
     * QuickTime::MCDoAction()
     * @return true if palindrome looping is enabled for the movie
     *  that is assigned to this controller. Otherwise, it is false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getLoopIsPalindrome() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 22, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this action to set the limits for grow box.
     * QuickTime::MCDoAction()
     * @param limits Specifies the new limits.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setGrowBoxBounds(QDRect paramQDRect) throws StdQTException {
        doAction((short) 25, paramQDRect.getRect());
    }

    /**
     * Your application can use this action to set the start time of a selection.
     * QuickTime::MCDoAction()
     * @param time Specifies the start time.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setSelectionBegin(TimeRecord paramTimeRecord) throws StdQTException {
        doAction((short) 29, QTObject.ID(paramTimeRecord));
    }

    /**
     * Your application can use this action to set the duration of a selection.
     * QuickTime::MCDoAction()
     * @param time Specifies the start time.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setSelectionDuration(TimeRecord paramTimeRecord) throws StdQTException {
        doAction((short) 30, QTObject.ID(paramTimeRecord));
    }

    /**
     * Your application can use this action to enable or disable keystrokes for a movie.
     * By default, keystroke processing is disabled.
     * QuickTime::MCDoAction()
     * @param enabled a value of true indicates that keystrokes are to be enabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setKeysEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 32, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this action to determine whether keystrokes
     * are enabled for a movie controller.
     * QuickTime::MCDoAction()
     * @return true if keystrokes are enabled for the movie that is assigned to
     *  this controller. Otherwise, it is false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getKeysEnabled() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 33, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this action to constrain playing to the current selection.
     * QuickTime::MCDoAction()
     * @param play a value of true indicates that playing within the current
     *  selection is to be enabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setPlaySelection(boolean paramBoolean) throws StdQTException {
        doAction((short) 34, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this action to determine whether a movie
     * has been constrained to playing within its selection.
     * QuickTime::MCDoAction()
     * @return true if playing is constrained to the current selection.
     *  Otherwise, it sets the value to false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getPlaySelection() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 35, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this action to enable or disable a movie's
     * playback badge. If a controller's badge is enabled, then the badge is
     * displayed whenever the controller is not visible. When the controller
     * is visible, the badge is not displayed. If the badge is disabled,
     * the badge is never displayed.
     * QuickTime::MCDoAction()
     * @param useBadge a value of true indicates that the playback badge is to be enabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setUseBadge(boolean paramBoolean) throws StdQTException {
        doAction((short) 36, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this action to determine whether a controller
     * is using a badge. If a controller's badge is enabled, then the badge is
     * displayed whenever the controller is not visible. When the controller
     * is visible, the badge is not displayed. If the badge is disabled, the badge
     * is never displayed.
     * QuickTime::MCDoAction()
     * @return true if the controller is using a badge. Otherwise, it is false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getUseBadge() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 37, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this action to set a movie's control flags.
     * QuickTime::MCDoAction()
     * @param flags an integer that contains the new control flag values.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setFlags(int paramInt) throws StdQTException {
        doAction((short) 38, paramInt);
    }

    /**
     * Your application can use this action to retrieve a movie's control flags.
     * QuickTime::MCDoAction()
     * @return movie's control flags into.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getFlags() throws StdQTException {
        int[] arrayOfInt = new int[1];
        doAction((short) 39, arrayOfInt);
        return arrayOfInt[0];
    }

    /**
     * Your application can use this action to instruct the movie controller to
     * play every frame in a movie. In this case, the movie controller may play
     * the movie at a slower rate than you specify with the play() method.
     * However, the controller does not play the movie faster than the movie rate.
     * In addition, the controller does not play the movie's sound tracks.
     * QuickTime::MCDoAction()
     * @param play a value of true instructs the controller to play every frame
     *  in the movie, even if that means playing the movie at a slower rate than
     *  you previously specified.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setPlayEveryFrame(boolean paramBoolean) throws StdQTException {
        doAction((short) 40, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this action to determine whether the movie controller
     * has been instructed to play every frame in a movie. You tell the controller to
     * play every frame by using the setPlayEveryFrame method, which is
     * described earlier in this section.
     * QuickTime::MCDoAction()
     * @return true if the controller has been instructed to play every frame in the movie.
     *  Otherwise, the controller sets the value to false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getPlayEveryFrame() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 41, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this action to determine a movie's playback rate.
     * You set the playback rate when you start a movie playing by using the
     * play() method.
     * QuickTime::MCDoAction()
     * @return The movie controller returns the movie's playback rate. Values greater
     *          than 0 correspond to forward rates; values less than 0 play the movie
     *          backward. A value of 0 indicates that the movie is stopped.
     * @return {@link quicktime.std.StdQTException}
     */
    public final float getPlayRate() throws StdQTException {
        int[] arrayOfInt = new int[1];
        doAction((short) 42, arrayOfInt);
        return QTUtils.Fix2X(arrayOfInt[0]);
    }

    /**
     * Indicates that a suspend event has been received. There is no parameter.
     * QuickTime::MCDoAction()
     */
    public final void suspend() throws StdQTException {
        doAction((short) 46, 0);
    }

    /**
     * Indicates that a resume event has been received. There is no parameter.
     * QuickTime::MCDoAction()
     */
    public final void resume() throws StdQTException {
        doAction((short) 47, 0);
    }

    /**
     * This method allows your application to enable or disable responding to keys.
     * QuickTime::MCDoAction()
     */
    public final void setControllerKeysEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 48, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this method to receive the QDRect specifying
     * the location and size of the time slider.
     * QuickTime::MCDoAction()
     * @return A QDRect object specifying the location and size of the time slider.
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDRect getTimeSliderRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        doAction((short) 49, localQDRect.getRect());
        return localQDRect;
    }

    /**
     * Your application can use this method to determine whether the movie controller
     * has been instructed to enable drag & drop behavior.
     * QuickTime::MCDoAction()
     * @return true if the controller has been instructed to enable drag &amp; drop.
     *  Otherwise, it is false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getDragEnabled() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        doAction((short) 51, arrayOfByte);
        return arrayOfByte[0] == 1;
    }

    /**
     * Your application can use this method to enable drag & drop behavior.
     * QuickTime::MCDoAction()
     * @param enabled a value of true indicates that the drag &amp; drop behavior is to be enabled.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDragEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 52, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this method to receive the start time of  a selection.
     * QuickTime::MCDoAction()
     * @return A TimeRecord object specifying the start time of a selection.
     * @return {@link quicktime.QTException}
     */
    public final TimeRecord getSelectionBegin() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        doAction((short) 53, QTObject.ID(localTimeRecord));
        return localTimeRecord;
    }

    /**
     * Your application can use this method to receive the duration of  a selection.
     * QuickTime::MCDoAction()
     * @return A TimeRecord object specifying the duration of a selection.
     * @return {@link quicktime.QTException}
     */
    public final TimeRecord getSelectionDuration() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        doAction((short) 54, QTObject.ID(localTimeRecord));
        return localTimeRecord;
    }

    /**
     * Your application can use this method to start a movie.
     * this method makes sure that movie is prerolled before it starts playing.
     * QuickTime::MCDoAction()
     * @param rate a value indicating the rate of the movie playback.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * Your application can use this method to determine whether or not the movie controller
     * is allowed to change the cursor.
     * QuickTime::MCDoAction()
     * @return true if the controller is allowed to set the cursor.
     *  Otherwise, it is false.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getCursorSettingEnabled() throws StdQTException {
        int[] arrayOfInt = new int[1];
        doAction((short) 56, arrayOfInt);
        return arrayOfInt[0] == 1;
    }

    /**
     * Your application can use this method to specify whether or not the movie controller
     * is allowed to change the cursor.
     * QuickTime::MCDoAction()
     * @param enabled a value of true indicates that the movie controller is allowed to
     *                 change the cursor.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setCursorSettingEnabled(boolean paramBoolean) throws StdQTException {
        doAction((short) 57, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this method to assign a new color table to the controller
     * QuickTime::MCDoAction()
     * @param newCTable Specifies the new color table
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setColorTable(ColorTable paramColorTable) throws StdQTException {
        doAction((short) 58, QTObject.ID(paramColorTable));
    }

    /**
     * Your application can use this method to indicate that the controller size has changed.
     * QuickTime::MCDoAction()
     */
    public final void controllerSizeChanged() throws StdQTException {
        doAction((short) 26, 0);
    }

    /**
     * Your application can use this method to switch badge clicking (false to ignore, true to process).
     * QuickTime::MCDoAction()
     * @param flag true to process (the default), false to ignore.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void badgeClick(boolean paramBoolean) throws StdQTException {
        doAction((short) 44, (byte) (paramBoolean ? 1 : 0));
    }

    /**
     * Your application can use this method to indicate that the movie has been edited.
     * QuickTime::MCDoAction()
     */
    public final void movieEdited() throws StdQTException {
        doAction((short) 50, 0);
    }

    /**
     * Your application can use this method to force the update of the time table.
     * QuickTime::MCDoAction()
     */
    public final void forceTimeTableUpdate() throws StdQTException {
        doAction((short) 61, 0);
    }

    /**
     * Your application can use this method to set a URL.
     * QuickTime::MCDoAction()
     * @param url the URL that is used to create a DataReference.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * Your application can use this method to test if point has click and hold action.
     * QuickTime::MCDoAction()
     * @param point (local coordinates).
     * @return true if point has click &amp; hold action (e.g., VR object movie autorotate spot)
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean clickAndHoldPoint(QDPoint paramQDPoint) throws StdQTException {
        return doAction((short) 67, paramQDPoint.getPoint());
    }

    /**
     * Your application can use this method to make the controller attached or detached.
     * QuickTime::MCSetControllerAttached()
     * @param attached Specifies the new state. true if attached.
     * @param {@link quicktime.QTException}
     */
    public final void setAttached(boolean paramBoolean) throws QTException {
        synchronized (QTNative.globalsLock) {
            int i = MCSetControllerAttached(_ID(), (byte) (paramBoolean ? 1 : 0));
            StdQTException.checkError(i);
        }
    }

    /**
     * Your application can use this method to receive information whether the controller
     * is attached or not.
     * QuickTime::MCIsControllerAttached()
     * @return true if the controller is attached.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean isAttached() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCIsControllerAttached(_ID());
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    /**
     * This method lets you change the position and size of a movie controller.
     * A controller's boundary rectangle encloses the control portion of the controller.
     * In addition, in cases where the movie is attached to the this.component, the
     * boundary rectangle also encloses the movie. Note that changing the size of the
     * boundary rectangle may result in the movie being resized as well, if the movie
     * is attached to the controller.
     * QuickTime::MCSetControllerBoundsRect()
     * @param newBoundsRect Identifies the new boundary rectangle.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setBounds(QDRect paramQDRect) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetControllerBoundsRect(_ID(), paramQDRect.getRect()));
        }
    }

    /**
     * This method returns a movie controller's boundary rectangle.
     * This rectangle reflects the size and location of the controller even
     * if the controller is currently hidden. If the controller is detached
     * from its movie, the rectangle encompasses only the this.component, not the movie.
     * If the controller is attached to its movie, the rectangle encompasses
     * both the controller and the movie.
     * QuickTime::MCGetControllerBoundsRect()
     * @return a QDRect object specifying the boundary rectangle.
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDRect getBounds() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            QDRect localQDRect = new QDRect();
            StdQTException.checkError(MCGetControllerBoundsRect(_ID(), localQDRect.getRect()));
            return localQDRect;
        }
    }

    /**
     * This method allows your application to set the graphics port
     * for a movie controller. You can use this function to place a movie and its associated
     * movie controller in different graphics ports. If you are using an attached this.component,
     * both the controller and the movie's graphics ports are changed. If you are using
     * a detached this.component, this function changes only the graphics port of the control
     * portion of the controller. You must use the Movie Toolbox's SetMovieGWorld function
     * followed by the MCMovieChanged function to change other portions.
     * QuickTime::MCSetControllerPort()
     * @param grafPtr Points to the new graphics port for the movie controller. This may NOT be null.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setPort(QDGraphics paramQDGraphics) throws StdQTException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetControllerPort(_ID(), QTObject.ID(paramQDGraphics)));
        }
        this.currentPort = QTObject.ID(paramQDGraphics);
    }

    /**
     * This method allows your application to control the visibility of a movie controller.
     * QuickTime::MCSetVisible()
     * @param visible Specifies the action for this function. Set the visible parameter to
     *                 true to cause the controller to be visible. Set this parameter to false
     *                 to make the controller invisible.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setVisible(boolean paramBoolean) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetVisible(_ID(), (byte) (paramBoolean ? 1 : 0)));
        }
    }

    /**
     * This method returns a value that indicates whether a movie controller is visible.
     * QuickTime::MCGetVisible()
     * @return whether the controller visible or not.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean getVisible() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetVisible(_ID());
        }
        if (i < 0)
            StdQTException.checkError(i);
        return i == 1;
    }

    /**
     * This method lets you inform a movie controller component that your application
     * has used the Movie Toolbox to change the characteristics of its associated movie.
     * QuickTime::MCMovieChanged()
     */
    public final void movieChanged() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            int i = MCGetIndMovie(_ID(), (short) 0);
            StdQTException.checkError(MCMovieChanged(_ID(), i));
        }
    }

    /**
     * This method allows your application to set a controller's duration in the case
     * where a controller does not have a movie associated with it.
     * QuickTime::MCSetDuration()
     * @param duration Specifies the duration for the controller.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setDuration(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetDuration(_ID(), paramInt));
        }
    }

    /**
     * This method returns the time value represented by the indicator on the movie
     * controller's slider.
     * QuickTime::MCGetCurrentTime()
     * @return the time value that corresponds to the current setting of the indicator
     *          on the movie controller's slider.
     * @return {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method returns the time scale of the controller.
     * QuickTime::MCGetCurrentTime()
     * @return the time scale for the controller.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getTimeScale() throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            MCGetCurrentTime(_ID(), arrayOfInt);
        }
        StdQTException.checkError(GetMoviesError());
        return arrayOfInt[0];
    }

    /**
     * This method can be used in response to activate, deactivate, suspend, and resume events.
     * QuickTime::MCActivate()
     * @param window Specifies the window in which the event has occurred. This may NOT be null.
     * @param activate Indicates the nature of the event. Set this parameter to true for
     *                  activate and resume events. Set it to false for deactivate and
     *                  suspend events.
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.qd.QDException}
     */
    public final void activate(QDGraphics paramQDGraphics, boolean paramBoolean) throws StdQTException, QDException {
        if ((!QTSession.isCurrentOS(4)) && (paramQDGraphics == null))
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            int i = MCActivate(_ID(), QTObject.ID(paramQDGraphics), (byte) (paramBoolean ? 1 : 0));
            StdQTException.checkError(i);
        }
    }

    /**
     * This method allows your application to enable and disable editing for a movie
     * controller.
     * QuickTime::MCEnableEditing()
     * @param enabled Specifies whether to enable or disable editing for the controller.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void enableEditing(boolean paramBoolean) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCEnableEditing(_ID(), (byte) (paramBoolean ? 1 : 0)));
        }
    }

    /**
     * This method allows your application to determine whether editing is currently
     * enabled for a movie controller.
     * QuickTime::MCIsEditingEnabled()
     * @return a value indicating the current edit state of the controller.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean isEditingEnabled() throws StdQTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCIsEditingEnabled(_ID());
        }
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method returns a copy of the current movie selection from the movie associated
     * with a specified controller.
     * QuickTime::MCCopy()
     * @return a copy of the current movie selection from the movie associated with a
     *          specified controller.
     * @return {@link quicktime.QTException}
     */
    public final Movie copy() throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCCopy(_ID());
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    /**
     * This method returns a copy of the current movie selection from the movie associated
     * with a specified controller and then removes the current movie selection from the
     * source movie.
     * QuickTime::MCCut()
     * @return a copy of the current movie selection.
     * @return {@link quicktime.QTException}
     */
    public final Movie cut() throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCCut(_ID());
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    /**
     * This method inserts a specified movie at the current movie time in the movie
     * associated with a specified controller. This will paste the data that is found
     * currently on the scrap
     * QuickTime::MCPaste()
     */
    public final void paste() throws StdQTException {
        paste(null);
    }

    /**
     * This method inserts a specified movie at the current movie time in the movie
     * associated with a specified controller.
     * QuickTime::MCPaste()
     * @param srcMovie Specifies the movie to be inserted into the current selection
     *                  in the movie associated with the movie controller.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void paste(Movie paramMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPaste(_ID(), QTObject.ID(paramMovie)));
        }
    }

    /**
     * This method removes the current movie selection from the movie associated with a
     * specified controller.
     * QuickTime::MCClear()
     */
    public final void clear() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCClear(_ID()));
        }
    }

    /**
     * This method allows your application to discard the effects of the most recent edit operation.
     * QuickTime::MCUndo()
     */
    public final void undo() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCUndo(_ID()));
        }
    }

    /**
     * This method allows you to control the position of a movie and its controller on
     * the computer display.
     * QuickTime::MCPositionController()
     * @param movieRect Specifies the coordinates of the movie's boundary rectangle.
     * @param controllerRect Specifies the coordinates of the controller's boundary rectangle.
     * @param someFlags If you set these flags to 0, the movie controller component centers the
     *                    movie in the rectangle specified by movieRect and scales the movie to
     *                    fit in that rectangle. You may control how the movie is drawn by setting
     *                    one or more of the flags to 1.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void position(QDRect paramQDRect1, QDRect paramQDRect2, int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPositionController(_ID(), paramQDRect1.getRect(), paramQDRect2.getRect(), paramInt));
        }
    }

    /**
     * This method allows you to control the position of a movie and its controller on the computer display.
     * QuickTime::MCPositionController()
     * @param movieRect Specifies the coordinates of the movie's boundary rectangle.
     * @param someFlags If you set these flags to 0, the movie controller component centers the movie
     *  in the rectangle specified by movieRect and scales the movie to fit in that rectangle.
     *  You may control how the movie is drawn by setting one or more of the following flags to 1.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void position(QDRect paramQDRect, int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPositionController(_ID(), paramQDRect.getRect(), null, paramInt));
        }
    }

    /**
     * This method returns the current status of a movie controller and its associated movie.
     * QuickTime::MCGetControllerInfo()
     * @return the current status and capabilities of the controller.
     * @return {@link quicktime.std.StdQTException}
     */
    public final int getControllerInfo() throws StdQTException {
        int[] arrayOfInt = new int[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCGetControllerInfo(_ID(), arrayOfInt));
        }
        return arrayOfInt[0];
    }

    /**
     * This method allows you to set a movie controller's clipping region.
     * QuickTime::MCSetClip()
     * @param theClip A Region that defines the controller's clipping region.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setClip(Region paramRegion) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetClip(_ID(), QTObject.ID(paramRegion), 0));
        }
    }

    /**
     * This method allows you to set a movie's clipping region.
     * QuickTime::MCSetClip()
     * @param theClip A Region that defines the movie's clipping region.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void setMovieClip(Region paramRegion) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCSetClip(_ID(), 0, QTObject.ID(paramRegion)));
        }
    }

    /**
     * This method allows your application to determine if a point is within the controller's control area.
     * QuickTime::MCPtInController()
     * @return true if the point is in the controller's control area.
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean inController(QDPoint paramQDPoint) throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCPtInController(_ID(), paramQDPoint.getPoint(), arrayOfByte));
        }
        return arrayOfByte[0] == 1;
    }

    /**
     * This method allows your application to invalidate portions of a movie controller.
     * QuickTime::MCInvalidate()
     * @param window Specifies the window where the invalid region is in. This may NOT be null.
     * @param invalidRgn Specifies the invalid region.
     * @param {@link quicktime.std.StdQTException}
     * @param {@link quicktime.qd.QDException}
     */
    public final void invalidate(QDGraphics paramQDGraphics, Region paramRegion) throws StdQTException, QDException {
        if (paramQDGraphics == null)
            throw new StdQTException(-50);
        synchronized (QTNative.globalsLock) {
            int i = MCInvalidate(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramRegion));
            StdQTException.checkError(i);
        }
    }

    /**
     * This method returns a movie controller's color graphics port.
     * QuickTime::MCGetControllerPort()
     * @return A QDGraphics object representing the controller's graf port.
     * @return {@link quicktime.std.StdQTException}
     */
    public final QDGraphics getPort() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            return QDGraphics.fromMovieController(this);
        }
    }

    /**
     * This method returns the actual region occupied by the controller and its movie,
     * if the movie is attached to the controller.
     * QuickTime::MCGetControllerBoundsRgn()
     * @return a Region object specifying the boundary region for this controller.
     * @return {@link quicktime.QTException}
     */
    public final Region getBoundsRgn() throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerBounds(this);
        }
    }

    /**
     * This method  allows your application to determine the window region that is
     * actually in use by a controller and its movie.
     * QuickTime::MCGetWindowRgn()
     * @param window Specifies the window. This may NOT be null.
     * @return a Region object specifying the window region.
     * @return {@link quicktime.QTException}
     */
    public final Region getWindowRgn(QDGraphics paramQDGraphics) throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerWindow(this, paramQDGraphics);
        }
    }

    /**
     * This method allows you to obtain information describing a movie controller's clipping regions.
     * QuickTime::MCGetClip()
     * @return the clipping region of the entire movie controller
     * @return {@link quicktime.QTException}
     */
    public final Region getClip() throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerClip(this);
        }
    }

    /**
     * This method allows you to display a controller's badge.
     * QuickTime::MCDrawBadge()
     * @param movieRgn Specifies the boundary region of the controller's movie.
     * @return A Region that gives information about the location of the badge.
     * @return {@link quicktime.QTException}
     */
    public final Region drawBadge(Region region, boolean flag) throws QTException {
        synchronized (QTNative.globalsLock) {
            return Region.fromMovieControllerBadge(this, region, flag);
        }
    }

    private static void QDFlushPortBuffer(int paramInt1, int paramInt2) {
        QuickDrawManager.QDFlushPortBuffer(paramInt1, paramInt2);
    }

    private static int MCNewAttachedController(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieControllerManager.MCNewAttachedController(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int NewMovieController(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
        return MovieControllerManager.NewMovieController(paramInt1, paramArrayOfByte, paramInt2);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int MCIdle(int paramInt) {
        return MovieControllerManager.MCIdle(paramInt);
    }

    private static int MCSetActionFilterWithRefCon(int paramInt1, int paramInt2, int paramInt3) {
        return MovieControllerManager.MCSetActionFilterWithRefCon(paramInt1, paramInt2, paramInt3);
    }

    private static int MCRemoveMovie(int paramInt) {
        return MovieControllerManager.MCRemoveMovie(paramInt);
    }

    private static int MCSetMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieControllerManager.MCSetMovie(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int MCGetIndMovie(int paramInt, short paramShort) {
        return MovieControllerManager.MCGetIndMovie(paramInt, paramShort);
    }

    private static int MCGetControllerPort(int paramInt) {
        return MovieControllerManager.MCGetControllerPort(paramInt);
    }

    private static int MCClick(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieControllerManager.MCClick(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static int TickCount() {
        return ClockManager.TickCount();
    }

    private static int MCKey(int paramInt1, byte paramByte, int paramInt2) {
        return MovieControllerManager.MCKey(paramInt1, paramByte, paramInt2);
    }

    private static int MCDoAction(int paramInt, short paramShort, int[] paramArrayOfInt) {
        return MovieControllerManager.MCDoAction(paramInt, paramShort, paramArrayOfInt);
    }

    private static int MCDoAction(int paramInt1, short paramShort, int paramInt2) {
        return MovieControllerManager.MCDoAction(paramInt1, paramShort, paramInt2);
    }

    private static int MCDoAction(int paramInt, short paramShort, short[] paramArrayOfShort) {
        return MovieControllerManager.MCDoAction(paramInt, paramShort, paramArrayOfShort);
    }

    private static int MCDoAction(int paramInt, short paramShort1, short paramShort2) {
        return MovieControllerManager.MCDoAction(paramInt, paramShort1, paramShort2);
    }

    private static int MCDoAction(int paramInt, short paramShort, byte paramByte) {
        return MovieControllerManager.MCDoAction(paramInt, paramShort, paramByte);
    }

    private static int MCDoAction(int paramInt, short paramShort, byte[] paramArrayOfByte) {
        return MovieControllerManager.MCDoAction(paramInt, paramShort, paramArrayOfByte);
    }

    private static int MCSetControllerAttached(int paramInt, byte paramByte) {
        return MovieControllerManager.MCSetControllerAttached(paramInt, paramByte);
    }

    private static int MCIsControllerAttached(int paramInt) {
        return MovieControllerManager.MCIsControllerAttached(paramInt);
    }

    private static int MCSetControllerBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        return MovieControllerManager.MCSetControllerBoundsRect(paramInt, paramArrayOfByte);
    }

    private static int MCGetControllerBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        return MovieControllerManager.MCGetControllerBoundsRect(paramInt, paramArrayOfByte);
    }

    private static int MCSetControllerPort(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCSetControllerPort(paramInt1, paramInt2);
    }

    private static int MCSetVisible(int paramInt, byte paramByte) {
        return MovieControllerManager.MCSetVisible(paramInt, paramByte);
    }

    private static int MCGetVisible(int paramInt) {
        return MovieControllerManager.MCGetVisible(paramInt);
    }

    private static int MCMovieChanged(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCMovieChanged(paramInt1, paramInt2);
    }

    private static int MCSetDuration(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCSetDuration(paramInt1, paramInt2);
    }

    private static int MCGetCurrentTime(int paramInt, int[] paramArrayOfInt) {
        return MovieControllerManager.MCGetCurrentTime(paramInt, paramArrayOfInt);
    }

    private static int MCActivate(int paramInt1, int paramInt2, byte paramByte) {
        return MovieControllerManager.MCActivate(paramInt1, paramInt2, paramByte);
    }

    private static int MCEnableEditing(int paramInt, byte paramByte) {
        return MovieControllerManager.MCEnableEditing(paramInt, paramByte);
    }

    private static int MCIsEditingEnabled(int paramInt) {
        return MovieControllerManager.MCIsEditingEnabled(paramInt);
    }

    private static int MCCopy(int paramInt) {
        return MovieControllerManager.MCCopy(paramInt);
    }

    private static int MCCut(int paramInt) {
        return MovieControllerManager.MCCut(paramInt);
    }

    private static int MCPaste(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCPaste(paramInt1, paramInt2);
    }

    private static int MCClear(int paramInt) {
        return MovieControllerManager.MCClear(paramInt);
    }

    private static int MCUndo(int paramInt) {
        return MovieControllerManager.MCUndo(paramInt);
    }

    private static int MCPositionController(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2) {
        return MovieControllerManager.MCPositionController(paramInt1, paramArrayOfByte1, paramArrayOfByte2, paramInt2);
    }

    private static int MCGetControllerInfo(int paramInt, int[] paramArrayOfInt) {
        return MovieControllerManager.MCGetControllerInfo(paramInt, paramArrayOfInt);
    }

    private static int MCSetClip(int paramInt1, int paramInt2, int paramInt3) {
        return MovieControllerManager.MCSetClip(paramInt1, paramInt2, paramInt3);
    }

    private static int MCPtInController(int paramInt1, int paramInt2, byte[] paramArrayOfByte) {
        return MovieControllerManager.MCPtInController(paramInt1, paramInt2, paramArrayOfByte);
    }

    private static int MCInvalidate(int paramInt1, int paramInt2, int paramInt3) {
        return MovieControllerManager.MCInvalidate(paramInt1, paramInt2, paramInt3);
    }

    private static int MCDraw(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCDraw(paramInt1, paramInt2);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }
}

