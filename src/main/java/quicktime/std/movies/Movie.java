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
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.internal.toolbox.QuickDrawManager;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.internal.toolbox.VRManager;
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

/**
 * The Movie class implements methods for the corresponding QuickTime structure.
 * A QuickTime movie can consist of data in sequences from different forms, such as analog video and CD-ROM.
 * The movie is not the medium; it is the organizing principle.
 *
 * A QuickTime movie may contain several Tracks. Each track refers to a single media type that contains references to the
 * movie data, which may be stored as images or sounds on various kinds of storage media.
 *
 * Movie objects can be made from various sources that contain existing QuickTime movie data, such as memory, files, urls. To make
 * a movie from an existing movie source you use the from... class calls. If you wish to make a new movie that you will construct yourself
 * you use the movie constructor.
 *
 * If an application installs either a MovieProgress or MovieDrawingComplete callback, then it must remove these callbacks
 * when it has finished with them to allow the native structures used in these callbacks to be properly disposed.
 */
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

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * The moviesTask method services all of the currently active movies.
     * You should call moviesTask as often as possible.
     * QuickTime::MoviesTask()
     * @param maxMilliSecToUse Determines the maximum number of milliseconds that MoviesTask can work before returning.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * Creates a Movie object and the file as specified by the QTFile.
     * This call will not open the created movie file.
     * QuickTime::CreateMovieFile()
     * @param pathName specifies the complete pathName of the file that is to be created.
     * @param fCreator Specifies the creator of the file.
     * @param flags Specifies the behaviour when creating the file.
     * @return QTFile object that specifies the newly created movie file.
     * @return {@link quicktime.QTException}
     */
    public static Movie createMovieFile(QTFile paramQTFile, int paramInt1, int paramInt2) throws QTException {
        if ((paramInt2 & 0x80000000) != 0) {
            try {
                QTFile.checkSecurity(paramQTFile.getCanonicalPath(), 1024);
            } catch (IOException localIOException) {
                throw new QTIOException(localIOException.getMessage());
            }
        }
        int[] arrayOfInt = { 0 };
        if ((paramInt2 & 0x40000000) != 0) {
            paramInt2 ^= 0x40000000;
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = CreateMovieFile(paramQTFile.getFSSpec(false, 512), paramInt1, (short) -1, paramInt2 | 0x20000000, null, arrayOfInt);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        return new Movie(arrayOfInt[0], null);
    }

    /**
     * Determines a reference to the movie that contains the data collected during a record operation.
     * You can use this movie identifier with Movie Toolbox functions.
     * QuickTime::SGGetMovie
     * @return The Movie or null if there is no movie.
     * @see {@link quicktime.std.sg.SequenceGrabber#getMovie()}
     */
    public static Movie fromSequenceGrabber(SequenceGrabber paramSequenceGrabber) {
        int i = SGGetMovie(QTObject.ID(paramSequenceGrabber));
        return new Movie(i, paramSequenceGrabber);
    }

    /**
     * This method allows your application to create a movie from the contents of the scrap.
     * QuickTime::NewMovieFromScrap()
     * @param newMovieFlags Controls the operation of this method.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * This method creates a movie in memory from a resource that is
     * stored in a movie file. Your application specifies the movie file with the
     * opened file represented by the OpenMovieFile object.
     * Once you have opened a movie file and loaded a movie, your application can
     * proceed to work with the movie.
     * QuickTime::NewMovieFromFile()
     * @param fileIn identifies the open movie file from which the movie is to be loaded.
     * @param {@link quicktime.QTException}
     */
    public static Movie fromFile(OpenMovieFile paramOpenMovieFile) throws QTException {
        return fromFile(paramOpenMovieFile, 1, null);
    }

    /**
     * This method creates a movie in memory from a resource that is
     * stored in a movie file. Your application specifies the movie file with the
     * opened file represented by the OpenMovieFile object.
     * Once you have opened a movie file and loaded a movie, your application can
     * proceed to work with the movie.
     * QuickTime::NewMovieFromFile()
     * @param fileIn identifies the open movie file stream from which the movie is to be loaded.
     * @param flags controls the operation of this method.
     * @param info a MovieInfo object to receive state information about the movie.
     * @param {@link quicktime.QTException}
     */
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
            paramMovieInfo.setMovieInfo(arrayOfShort[0], arrayOfByte2[0] == 1, new String(arrayOfByte1, 1, arrayOfByte1.length - 1));
        }
        Object x = new Movie(arrayOfInt[0], null);
        ((Movie) x).init();
        return (Movie) x;
    }

    /**
     * This method creates a movie in memory from a movie resource or
     * a handle you obtained from the PutMovieIntoHandle function.
     * QuickTime::NewMovieFromHandle()
     * @param hand contains a handle to the movie resource from which the movie is to be loaded.
     * @param {@link quicktime.QTException}
     */
    public static Movie fromHandle(QTHandle paramQTHandle) throws QTException {
        return fromHandle(paramQTHandle, 1, null);
    }

    /**
     * This method creates a movie in memory from a movie resource or
     * a handle you obtained from the PutMovieIntoHandle function.
     * QuickTime::NewMovieFromHandle()
     * @param hand contains a handle to the movie resource from which the movie is to be loaded.
     * @param flags controls the operation of this method.
     * @param info a MovieInfo object to receive state information about the movie.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * This method enables you to retrieve a movie
     * that is stored anywhere in the data fork of a specified file.
     * QuickTime::NewMovieFromDataFork()
     * @param fileIn The open movie file stream.
     * @param fileOffset Specifies the starting file offset of the atom in the data
     *                     fork of the file specified by the fRefNum parameter.
     * @param flags controls the operation of this method.
     * @param info a MovieInfo object to receive state information about the movie.
     * @param {@link quicktime.QTException}
     * @param {@link quicktime.qd.QDException}
     */
    public static Movie fromDataFork(OpenFile paramOpenFile, int paramInt1, int paramInt2, MovieInfo paramMovieInfo) throws QTException, QDException {
        int[] arrayOfInt = { 0 };
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

    /**
     * This method enables you to retrieve a movie from any data reference.
     * QuickTime::NewMovieFromDataRef()
     * @param defaultDataRef a DataRef object specifying the data reference this method will load the movie from.
     * @param flags Controls the operation of this method.
     *
     *  NOTE: it is a good idea to use the flag newMovieAsyncOK (defined in StdQTConstants4) to prevent the VM from
     *  blocking (in the case of no network connection, or a bad URL)
     * @param {@link quicktime.QTException}
     */
    public static Movie fromDataRef(DataRef paramDataRef, int paramInt) throws QTException {
        int[] arrayOfInt = { 0 };
        short[] arrayOfShort = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromDataRef(arrayOfInt, (short) paramInt, arrayOfShort, QTObject.ID(paramDataRef), paramDataRef == null ? 0 : paramDataRef.getType());
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

    /**
     * This method creates a new movie in memory. The Movie Toolbox
     * initializes the data structures for the new movie, which contains no tracks.
     * It supplies default settings of the movie being inactive and will select enabled tracks from
     * track groups.
     * QuickTime::NewMovie()
     */
    public Movie() throws QTException {
        this(1);
    }

    /**
     * This method creates a new movie in memory. The Movie Toolbox
     * initializes the data structures for the new movie, which contains no tracks.
     * It supplies default settings of the movie being active and will select enabled tracks from
     * track groups.
     * QuickTime::NewMovie()
     * @param flags flags to control settings when creating the movie.
     */
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

    /**
     * This method adds a movie resource to a specified resource file.
     * QuickTime::AddMovieResource()
     * @param fileOut Identifies the open writable movie file stream to which the resource is to be added.
     * @param resID Contains a pointer to a field that contains the resource ID number for the new resource.
     *  If the field referred to by the resId parameter is set to 0, the Movie Toolbox assigns a
     *  unique resource ID number to the new resource.
     * @param resourceName a String object that contains the name of the movie resource.
     *  If you set the resourceName parameter to null, the toolbox creates an unnamed resource.
     * @param {@link quicktime.std.StdQTException}
     */
    public int addResource(OpenMovieFile paramOpenMovieFile, int paramInt, String paramString) throws StdQTException {
        short[] arrayOfShort = { (short) paramInt };
        byte[] arrayOfByte = paramString != null ? QTUtils.String2PString(paramString, 255) : null;
        int i = AddMovieResource(_ID(), (short) QTObject.ID(paramOpenMovieFile), arrayOfShort, arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfShort[0];
    }

    /**
     * This method replaces the contents of a movie resource in a specified movie file.
     * QuickTime::UpdateMovieResource()
     * @param fileOut Specifies the open writable movie file stream for the movie to be updated.
     * @param resID Specifies the resource number for the movie.
     * @param resourceName Points to a new name for the resource. If you do not want to change the resource's name,
     *  set this parameter to null.
     * @param {@link quicktime.std.StdQTException}
     */
    public void updateResource(OpenMovieFile paramOpenMovieFile, int paramInt, String paramString) throws StdQTException {
        byte[] arrayOfByte = paramString != null ? QTUtils.String2PString(paramString, 255) : null;
        int i = UpdateMovieResource(_ID(), (short) QTObject.ID(paramOpenMovieFile), (short) paramInt, arrayOfByte);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a movie resource from a specified movie file.
     * QuickTime::RemoveMovieResource()
     * @param fileOut Specifies the open writable movie file stream for the movie to be removed.
     * @param resID Specifies the resource number for the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void removeResource(OpenMovieFile paramOpenMovieFile, int paramInt) throws StdQTException {
        int i = RemoveMovieResource((short) QTObject.ID(paramOpenMovieFile), (short) paramInt);
        StdQTException.checkError(i);
    }

    /**
     * The moviesTask method services this movie.
     * You should call moviesTask as often as possible.
     * QuickTime::MoviesTask()
     * @param maxMilliSecToUse Determines the maximum number of milliseconds that MoviesTask can work before returning.
     * @param {@link quicktime.QTException}
     */
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

    /**
     * This allows you to assign a drawing-complete method to a movie.
     * The Movie Toolbox calls this function based upon guidelines you establish when you assign the method to the movie.
     * QuickTime::SetMovieDrawingCompleteProc()
     * @param flags flags that control under what conditions the proc is called
     * @param proc the object that will be called when drawing is complete
     * @param {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
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

    /**
     * This allows you to remove a previously assigned drawing-complete method to a movie.
     * QuickTime::SetMovieDrawingCompleteProc()
     */
    public void removeDrawingCompleteProc() throws StdQTException {
        SetMovieDrawingCompleteProc(_ID(), 0, 0, 0);
        if (this.drawingUPP != null)
            this.drawingUPP.cleanupMethodClosure();
        this.drawingUPP = null;
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to prepare a portion of a movie for playback.
     * QuickTime::PrerollMovie()
     * @param time Contains the starting time of the movie segment to play.
     * @param rate Specifies the rate at which you anticipate playing the movie.
     *  Positive values indicate forward rates and negative values indicate reverse rates.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method allows your application to prepare a portion of a movie for playback.
     * This call is used when using a remote movie to establish network connections.
     * QuickTime::PrePrerollMovie()
     * @param time Contains the starting time of the movie segment to play.
     * @param rate Specifies the rate at which you anticipate playing the movie.
     *  Positive values indicate forward rates and negative values indicate reverse rates.
     * @param {@link quicktime.std.StdQTException}
     */
    public void prePreroll(int paramInt, float paramFloat) throws StdQTException {
        this.preUPP = null;
        prePreroll(paramInt, paramFloat, null);
    }

    /**
     * This method allows your application to prepare a portion of a movie for playback.
     * This call is used when using a remote movie to establish network connections.
     * QuickTime::PrePrerollMovie()
     * @param time Contains the starting time of the movie segment to play.
     * @param rate Specifies the rate at which you anticipate playing the movie.
     *  Positive values indicate forward rates and negative values indicate reverse rates.
     * @param proc the MoviePrePreroll object that will be invoked by this call. This can be null.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method aborts.
     * This call is used when using a remote movie to establish network connections.
     * This version of the pre-preroll is Asynchronous and the Supplied MoviePrePreroll proc will
     * be called back when the movie is pre-preRolled
     * QuickTime::AbortPrePrerollMovie()
     */
    public void abortPrePreroll(int paramInt) throws StdQTException {
        if (QTSession.getQTMajorVersion() == 3)
            return;
        AbortPrePrerollMovie(_ID(), (short) paramInt);
        if (this.preUPP != null) {
            this.preUPP.cleanupMethodClosure();
            this.preUPP = null;
        }
    }

    /**
     * This method loads a movie's data into memory. If the movie does not fit, the method throws an error.
     * QuickTime::LoadMovieIntoRam()
     * @param time Allows you to specify a portion of the movie to load. The time parameter contains the starting time of the movie segment to load.
     * @param duration Allows you to specify a portion of the movie to load. The time parameter contains the starting time of the movie segment to load. The duration parameter specifies the length of the segment to load.
     * @param flags Gives you explicit control over what is loaded into memory and how long to keep it around.
     * @param {@link quicktime.std.StdQTException}
     */
    public void loadIntoRam(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = LoadMovieIntoRam(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to activate and deactivate a movie.
     * QuickTime::SetMovieActive()
     * @param active Activates or deactivates the movie. Set this parameter to true to activate the movie; set this parameter to false to deactivate the movie.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method returns a boolean value. The method returns true if the movie is active and false if the movie is not active.
     * QuickTime::GetMovieActive()
     */
    public boolean getActive() throws StdQTException {
        int i = GetMovieActive(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method starts the movie playing from the current movie time,
     * which is where the movie last stopped playing. Before playing the movie,
     * the Movie Toolbox makes the movie active, prerolls the movie, and sets the movie to
     * its preferred playback rate.
     * QuickTime::StartMovie()
     */
    public void start() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StartMovie(_ID());
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method stops the playback of a movie.
     * QuickTime::StopMovie()
     */
    public void stop() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StopMovie(_ID());
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method repositions a movie to play from its start.
     * QuickTime::GoToBeginningOfMovie()
     */
    public void goToBeginning() throws StdQTException {
        GoToBeginningOfMovie(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method repositions a movie to play from its end.
     * QuickTime::GoToEndOfMovie()
     */
    public void goToEnd() throws StdQTException {
        GoToEndOfMovie(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to determine if a particular movie has completely finished playing.
     * QuickTime::IsMovieDone()
     */
    public boolean isDone() throws StdQTException {
        int i = IsMovieDone(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to determine whether a movie is in preview mode.
     * If a movie is in preview mode, only the movie's preview can be displayed.
     * QuickTime::GetMoviePreviewMode()
     */
    public boolean getPreviewMode() throws StdQTException {
        int i = GetMoviePreviewMode(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to place a movie into and out of preview mode.
     * When a movie is in preview mode, only those tracks identified as preview tracks are serviced.
     * QuickTime::SetMoviePreviewMode()
     */
    public void setPreviewMode(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        SetMoviePreviewMode(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * You can use showPoster method to display a movie's poster. The movie poster
     * uses the movie's matrix and display clipping characteristics.
     * QuickTime::ShowMoviePoster()
     */
    public void showPoster() throws StdQTException {
        ShowMoviePoster(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method plays a movie's preview.
     * QuickTime::PlayMoviePreview()
     */
    public void playPreview() throws StdQTException {
        PlayMoviePreview(_ID(), 0, 0);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * Your application can determine a movie's graphics world by calling the getGWorld method.
     * QuickTime::GetMovieGWorld()
     * @return The current destination graphics port
     * @return {@link quicktime.std.StdQTException}
     */
    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromMovie(this);
    }

    /**
     * The setGWorld method allows your application to establish a movie's display
     * coordinate system by setting the graphics world for displaying a movie.
     * QuickTime::SetMovieGWorld()
     * @param port the new graphics port or null to use current port
     * @param gdh the graphics device or null to use the current device
     * @param {@link quicktime.std.StdQTException}
     */
    public void setGWorld(QDGraphics paramQDGraphics, GDevice paramGDevice) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieGWorld(_ID(), QTObject.ID(paramQDGraphics), QTObject.ID(paramGDevice));
            StdQTException.checkError(GetMoviesError());
        }
    }

    /**
     * No QT Documentation.
     * QuickTime::GetMovieNaturalBoundsRect()
     */
    public QDRect getNaturalBoundsRect() throws StdQTException {
        QDRect localQDRect = new QDRect();
        GetMovieNaturalBoundsRect(_ID(), localQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
        return localQDRect;
    }

    /**
     * No QT Documentation.
     * QuickTime::GetNextTrackForCompositing()
     */
    public Track getNextTrackForCompositing(Track paramTrack) throws QTException {
        int i = GetNextTrackForCompositing(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this);
    }

    /**
     * No QT Documentation.
     * QuickTime::GetPrevTrackForCompositing()
     */
    public Track getPrevTrackForCompositing(Track paramTrack) throws QTException {
        int i = GetPrevTrackForCompositing(_ID(), QTObject.ID(paramTrack));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this);
    }

    /**
     * This method creates a picture from the specified movie at the specified time.
     * This function uses only those movie tracks that are currently enabled and would therefore
     * be used in playback. Your application may call this function even if the movie is inactive.
     * QuickTime::GetMoviePict()
     * @param time contains the time from which the image is taken.
     * @return a Pict
     * @return {@link quicktime.QTException}
     */
    public Pict getPict(int paramInt) throws QTException {
        return Pict.fromMovie(this, paramInt);
    }

    /**
     * This method creates a picture that contains a movie's poster.
     * returns a PicHandle object for the image.
     * QuickTime::GetMoviePosterPict()
     * @return a Pict
     * @return {@link quicktime.QTException}
     */
    public Pict getPosterPict() throws QTException {
        return Pict.fromMovie(this);
    }

    /**
     * This method allows your application to ensure that the Movie Toolbox properly
     * displays your movie after it has been uncovered.
     * QuickTime::UpdateMovie()
     */
    public void update() throws StdQTException {
        int i = UpdateMovie(_ID());
        StdQTException.checkError(i);
    }

    /**
     * This method invalidates part of the movie specified by the invalidRgn. The invalidated
     * part will be drawn next time MoviesTask is called.
     * QuickTime::InvalidateMovieRegion()
     * @param invalidRgn a Region object to specify the invalid part of the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void invalidateRegion(Region paramRegion) throws StdQTException {
        int i = InvalidateMovieRegion(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(i);
    }

    /**
     * This method returns a movie's boundary rectangle, which is a rectangle that encompasses all
     * of the movie's enabled tracks. The movie box is in the coordinate system of the movie's
     * graphics world and defines the movie's boundaries over the entire duration of the movie.
     * The movie's boundary rectangle defines the size and shape of the movie before the
     * Movie Toolbox applies the display clipping region.
     * QuickTime::GetMovieBox()
     * @return The movie's boundary rectangle
     * @return {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method returns a movie's boundary rectangle, which is a rectangle that encompasses all
     * of the movie's enabled tracks. The movie box is in the coordinate system of the movie's
     * graphics world and defines the movie's boundaries over the entire duration of the movie.
     * The movie's boundary rectangle defines the size and shape of the movie before the
     * Movie Toolbox applies the display clipping region.
     * QuickTime::GetMovieBox()
     * @return The movie's boundary rectangle
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getBox() throws StdQTException {
        return getBounds();
    }

    /**
     * This method sets a movie's boundary rectangle, or movie box,
     * which is a rectangle that encompasses the spatial representation of all of
     * the movie's enabled tracks. The movie box is in the display coordinate system.
     * QuickTime::SetMovieBox()
     * @param boxRect the coordinates of the new boundary rectangle.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method sets a movie's boundary rectangle, or movie box,
     * which is a rectangle that encompasses the spatial representation of all of
     * the movie's enabled tracks. The movie box is in the display coordinate system.
     * QuickTime::SetMovieBox()
     * @param boxRect the coordinates of the new boundary rectangle.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setBox(QDRect paramQDRect) throws StdQTException {
        setBounds(paramQDRect);
    }

    /**
     * This method allows your application to determine a movie's current display clipping region.
     * QuickTime::GetMovieDisplayClipRgn()
     * @return a Region object.
     * @return {@link quicktime.QTException}
     */
    public Region getDisplayClipRgn() throws QTException {
        return Region.fromMovieDisplayClip(this);
    }

    /**
     * This method allows your application to establish a movie's current display clipping region.
     * QuickTime::SetMovieDisplayClipRgn()
     * @param theClip a Region object.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDisplayClipRgn(Region paramRegion) throws StdQTException {
        SetMovieDisplayClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a movie's clipping region.
     * QuickTime::GetMovieClipRgn()
     * @return a Region object.
     * @return {@link quicktime.QTException}
     */
    public Region getClipRgn() throws QTException {
        return Region.fromMovieClip(this);
    }

    /**
     * This method allows your application to establish a movie's clipping region.
     * QuickTime::SetMovieClipRgn()
     * @param theClip a Region object specifying the new clip region.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setClipRgn(Region paramRegion) throws StdQTException {
        SetMovieClipRgn(_ID(), QTObject.ID(paramRegion));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a movie's display boundary region.
     * The display boundary region encloses all of a movie's enabled tracks
     * after the track matrix, track clip, movie matrix, and movie clip have
     * been applied to all of the movie's tracks. This region is in the display
     * coordinate system of the movie's graphics world.
     * QuickTime::GetMovieDisplayBoundsRgn()
     * @return a Region object specifying the display boundary region.
     * @return {@link quicktime.QTException}
     */
    public Region getDisplayBoundsRgn() throws QTException {
        return Region.fromMovieDisplayBounds(this);
    }

    /**
     * This method allows your application to determine a movie's boundary region.
     * This region is in the movie's display coordinate system.
     * The movie boundary region encloses all of a movie's tracks after the union of
     * the track clip and the track matrix has been applied to all the movie's
     * tracks (but not to the movie itself).
     * QuickTime::GetMovieBoundsRgn()
     * @return a Region object specifying the movie boundary region.
     * @return {@link quicktime.QTException}
     */
    public Region getBoundsRgn() throws QTException {
        return Region.fromMovieBounds(this);
    }

    /**
     * This method creates a new movie resource for you. You can use this handle to store a QuickTime movie
     * in a specialized storage format.
     * QuickTime::PutMovieIntoHandle()
     * @param intoThisHandle Specifies the handle where the movie will be placed into.
     * @param {@link quicktime.std.StdQTException}
     */
    public void putIntoHandle(QTHandle paramQTHandle) throws StdQTException {
        int i = PutMovieIntoHandle(_ID(), QTObject.ID(paramQTHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to store a movie in the data fork of a given file.
     * QuickTime::PutMovieIntoDataFork()
     * @param fileOut Specifies the open writable movie file stream where the movie will be placed into.
     * @param offset Indicates where the movie should be written.
     * @param maxSize Indicates the largest number of bytes that may be written.
     * @param {@link quicktime.std.StdQTException}
     */
    public void putIntoDataFork(OpenMovieFile paramOpenMovieFile, int paramInt1, int paramInt2) throws StdQTException {
        short s = (short) QTObject.ID(paramOpenMovieFile);
        int i = PutMovieIntoDataFork(_ID(), s, paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method returns a long integer that contains the movie's creation date and time information.
     * QuickTime::GetMovieCreationTime()
     * @return the creation time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getCreationTime() throws StdQTException {
        int i = GetMovieCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a long integer that contains the movie's creation date and time information.
     * QuickTime::GetMovieModificationTime()
     * @return the modification time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getModificationTime() throws StdQTException {
        int i = GetMovieModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns the time scale of a movie.
     * QuickTime::GetMovieTimeScale()
     * @return the time scale.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTimeScale() throws StdQTException {
        int i = GetMovieTimeScale(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method establishes a movie's time scale.
     * QuickTime::SetMovieTimeScale()
     * @param timeScale Specifies the movie's new time scale.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTimeScale(int paramInt) throws StdQTException {
        SetMovieTimeScale(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the duration of a movie.
     * QuickTime::GetMovieDuration()
     * return the duration.
     */
    public int getDuration() throws StdQTException {
        int i = GetMovieDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a movie's playback rate.
     * QuickTime::GetMovieRate()
     * @return the playback rate.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getRate() throws StdQTException {
        int i = GetMovieRate(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    /**
     * This method sets a movie's playback rate.
     * QuickTime::SetMovieRate()
     * @param rate Specifies the rate.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method returns a movie's default playback rate.
     * QuickTime::GetMoviePreferredRate()
     * @return the preferred rate.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getPreferredRate() throws StdQTException {
        int i = GetMoviePreferredRate(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    /**
     * This method allows your application to specify a movie's default playback rate.
     * QuickTime::SetMoviePreferredRate()
     * @param rate Specifies the rate.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPreferredRate(float paramFloat) throws StdQTException {
        SetMoviePreferredRate(_ID(), QTUtils.X2Fix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a movie's preferred volume setting.
     * QuickTime::GetMoviePreferredVolume()
     * @return the preferred volume.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getPreferredVolume() throws StdQTException {
        short s = GetMoviePreferredVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    /**
     * This method allows your application to set a movie's preferred volume setting.
     * QuickTime::SetMoviePreferredVolume()
     * @param volume Specifies the preferred volume setting of the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPreferredVolume(float paramFloat) throws StdQTException {
        SetMoviePreferredVolume(_ID(), QTUtils.X2ShortFix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns movie's current volume setting.
     * QuickTime::GetMovieVolume()
     * @return the volume.
     * @return {@link quicktime.std.StdQTException}
     */
    public float getVolume() throws StdQTException {
        short s = GetMovieVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    /**
     * This method allows your application to set a movie's current volume.
     * QuickTime::SetMovieVolume()
     * @param volume Specifies the current volume setting of the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVolume(float paramFloat) throws StdQTException {
        SetMovieVolume(_ID(), QTUtils.X2ShortFix(paramFloat));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows allows your application to retrieve a movie's
     * transformation matrix.
     * QuickTime::GetMovieMatrix()
     * @return a Matrix object.
     * @return {@link quicktime.std.StdQTException}
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix localMatrix = new Matrix();
        GetMovieMatrix(_ID(), localMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
        return localMatrix;
    }

    /**
     * This method allows your application to set a movie's transformation matrix.
     * The Movie Toolbox uses a movie's matrix to map a movie from its display coordinate system to
     * its graphics world.
     * QuickTime::SetMovieMatrix()
     * @param matrix the matrix to set
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMatrix(Matrix paramMatrix) throws StdQTException {
        SetMovieMatrix(_ID(), paramMatrix.getBytes());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the starting time and duration of the movie's preview.
     * QuickTime::GetMoviePreviewTime()
     * @return a TimeInfo object.
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeInfo getPreviewTime() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMoviePreviewTime(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method allows your application to define the starting time and duration of the movie's preview.
     * QuickTime::SetMoviePreviewTime()
     * @param previewTimeInfo a TimeInfo object.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPreviewTime(TimeInfo paramTimeInfo) throws StdQTException {
        SetMoviePreviewTime(_ID(), paramTimeInfo.time, paramTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to define the starting time and duration of the movie's preview.
     * QuickTime::SetMoviePreviewTime()
     */
    public void setPreviewTime(int paramInt1, int paramInt2) throws StdQTException {
        SetMoviePreviewTime(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the poster's time in the movie.
     * QuickTime::GetMoviePosterTime()
     * @return the poster's time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getPosterTime() throws StdQTException {
        int i = GetMoviePosterTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method sets the poster time for the movie.
     * QuickTime::SetMoviePosterTime()
     * @param posterTime the poster time.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPosterTime(int paramInt) throws StdQTException {
        SetMoviePosterTime(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns information about a movie's current selection.
     * QuickTime::GetMovieSelection()
     * @return a TimeInfo object specifying the movie's selection.
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeInfo getSelection() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMovieSelection(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method sets a movie's current selection.
     * QuickTime::SetMovieSelection()
     * @param selectionTimeInfo a TimeInfo object specifying the time information for movie's new selection.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSelection(TimeInfo paramTimeInfo) throws StdQTException {
        SetMovieSelection(_ID(), paramTimeInfo.time, paramTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets a movie's current selection.
     * QuickTime::SetMovieSelection()
     * @param selectionTime Contains a time value specifying the starting point of the current selection.
     * @param selectionDuration Contains a time value that specifies the duration of the current selection.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSelection(int paramInt1, int paramInt2) throws StdQTException {
        SetMovieSelection(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to define a movie's active segment.
     * QuickTime::SetMovieActiveSegment()
     * @param activeSegmentTimeInfo a TimeInfo object specifying the time information of the active segment.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setActiveSegment(TimeInfo paramTimeInfo) throws StdQTException {
        SetMovieActiveSegment(_ID(), paramTimeInfo.time, paramTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine what portion of a movie is currently active for playing.
     * QuickTime::GetMovieActiveSegment()
     * @return a TimeInfo object specifying the active segment of the movie.
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeInfo getActiveSegment() throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        GetMovieActiveSegment(_ID(), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method returns a movie's current time.
     * QuickTime::GetMovieTime()
     * @return the movie's current time.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTime() throws StdQTException {
        int i = GetMovieTime(_ID(), 0);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a movie's current time as a TimeRecord object.
     * QuickTime::GetMovieTime()
     * @return the movie's current time as a TimeRecord object.
     * @return {@link quicktime.QTException}
     */
    public TimeRecord getTRTime() throws QTException {
        TimeRecord localTimeRecord = new TimeRecord();
        int i = GetMovieTime(_ID(), QTObject.ID(localTimeRecord));
        StdQTException.checkError(GetMoviesError());
        return localTimeRecord;
    }

    /**
     * This method allows you to set a movie's current time.
     * QuickTime::SetMovieTime()
     * @param newtime A TimeRecord object specifying the new time for the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setTime(TimeRecord paramTimeRecord) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieTime(_ID(), QTObject.ID(paramTimeRecord));
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to set a movie's time value.
     * QuickTime::SetMovieTimeValue()
     */
    public void setTimeValue(int paramInt) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieTimeValue(_ID(), paramInt);
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to obtain access to a movie's user data list.
     * QuickTime::GetMovieUserData()
     * @return a reference to the movie's user data list.
     * @return {@link quicktime.std.StdQTException}
     */
    public UserData getUserData() throws StdQTException {
        return UserData.fromMovie(this);
    }

    /**
     * This method returns the number of tracks in a movie.
     * QuickTime::GetMovieTrackCount()
     * @return the number of tracks in a movie.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getTrackCount() throws StdQTException {
        int i = GetMovieTrackCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine the track identifier of a track given the track's ID value.
     * QuickTime::GetMovieTrack()
     * @param trackID ID value of the track (from Track.getID())
     * @return a Track object for the trackID.
     * @return {@link quicktime.QTException}
     */
    public Track getTrack(int paramInt) throws QTException {
        int i = GetMovieTrack(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * This method allows your application to determine the track identifier of a track given the track's index value.
     * QuickTime::GetMovieIndTrack()
     * @param index index value of the track in the movie (Moive.getTrackCount())
     * @return a Track object for the specified index.
     * @return {@link quicktime.QTException}
     */
    public Track getIndTrack(int paramInt) throws QTException {
        int i = GetMovieIndTrack(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * This method allows your application to determine the track identifier of a track given a track type and an index value.
     * QuickTime::GetMovieIndTrackType()
     * @param index Specifies the index value of the track for this operation. This is not that same as the track's index value in the movie. Rather, this parameter is an index into the set of tracks that meet your other selection criteria.
     * @param trackType Contains either a media type or a media characteristic value.
     * @param flags Contains flags that control the search operation.
     * @return a Track object for the specified index with in the tracks with the specified track type.
     * @return {@link quicktime.QTException}
     */
    public Track getIndTrackType(int paramInt1, int paramInt2, int paramInt3) throws QTException {
        int i = GetMovieIndTrackType(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * Removes a track from a movie.
     * QuickTime::DisposeMovieTrack()
     * @param track the track to remove from this movie.
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method allows your application to enable and disable automatic track selection by the Movie Toolbox.
     * QuickTime::SetAutoTrackAlternatesEnabled()
     * @param enable Controls automatic track selection.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setAutoTrackAlternatesEnabled(boolean paramBoolean) throws StdQTException {
        byte b = (byte) (paramBoolean ? 1 : 0);
        SetAutoTrackAlternatesEnabled(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to instruct the Movie Toolbox to select appropriate tracks immediately.
     * QuickTime::SelectMovieAlternates()
     */
    public void selectAlternates() throws StdQTException {
        SelectMovieAlternates(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method searches for times of interest in a movie.
     * QuickTime::GetMovieNextInterestingTime()
     * @param interestingTimeFlags Specifies the search criteria.
     * @param mediaTypes Specifies an array of media types
     * @param time Specifies a time value that establishes the starting point for the search.
     * @param rate Contains the search direction.
     * @return a TimeInfo object specifying the interesting time.
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeInfo getNextInterestingTime(int paramInt1, int[] paramArrayOfInt, int paramInt2, float paramFloat) throws StdQTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        short s = 0;
        if (paramArrayOfInt != null) {
            s = (short) paramArrayOfInt.length;
        }
        GetMovieNextInterestingTime(_ID(), (short) paramInt1, s, paramArrayOfInt, paramInt2, QTUtils.X2Fix(paramFloat), arrayOfInt1, arrayOfInt2);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(arrayOfInt1[0], arrayOfInt2[0]);
    }

    /**
     * This method adds an empty segment to a movie.
     * QuickTime::InsertEmptyMovieSegment()
     */
    public void insertEmptySegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = InsertEmptyMovieSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * The insertSegment method copies part of one movie to another.
     * QuickTime::InsertMovieSegment()
     * @param dstMovie Specifies the destination movie for this operation.
     *  Specifies the start of the segment in the source movie.
     *  Specifies the duration of the segment in the source movie.
     *  Contains a time value specifying where the segment is to be inserted.
     * @param {@link quicktime.std.StdQTException}
     */
    public void insertSegment(Movie paramMovie, int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = InsertMovieSegment(_ID(), QTObject.ID(paramMovie), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a specified segment from a movie.
     * QuickTime::DeleteMovieSegment()
     * @param startTime Contains a time value specifying the starting point of the segment to be deleted.
     * @param duration Contains a time value that specifies the duration of the segment to be deleted.
     * @param {@link quicktime.std.StdQTException}
     */
    public void deleteSegment(int paramInt1, int paramInt2) throws StdQTException {
        int i = DeleteMovieSegment(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(i);
    }

    /**
     * This method changes the duration of a segment of a movie and then removes the current selection from the original movie.
     * QuickTime::ScaleMovieSegment()
     * @param startTime Specifies the start of the segment.
     * @param oldDuration Specifies the duration of the segment in the source movie.
     * @param newDuration Specifies the new duration of the segment.
     * @param {@link quicktime.std.StdQTException}
     */
    public void scaleSegment(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = ScaleMovieSegment(_ID(), paramInt1, paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * This method creates a new movie that contains the original movie's current selection.
     * QuickTime::CutMovieSelection()
     * @return a new Movie object that contains the original movie's current selection.
     * @return {@link quicktime.QTException}
     */
    public Movie cutSelection() throws QTException {
        int i = CutMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    /**
     * This method creates a new movie that contains the original movie's current selection.
     * QuickTime::CopyMovieSelection()
     * @return a new Movie object that contains the original movie's current selection.
     * @return {@link quicktime.QTException}
     */
    public Movie copySelection() throws QTException {
        int i = CopyMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    /**
     * This method places the tracks from one movie into another movie.
     * QuickTime::PasteMovieSelection()
     * @param srcMovie Specifies the source movie for this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void pasteSelection(Movie paramMovie) throws StdQTException {
        PasteMovieSelection(_ID(), QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method adds one or more tracks to a movie.
     * QuickTime::AddMovieSelection()
     * @param srcMovie Specifies the source movie for this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void addSelection(Movie paramMovie) throws StdQTException {
        AddMovieSelection(_ID(), QTObject.ID(paramMovie));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method removes the segment of the movie that is defined by the current selection.
     * QuickTime::ClearMovieSelection()
     */
    public void clearSelection() throws StdQTException {
        ClearMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method takes the contents of a specified handle, together with its type, and pastes it into a specified movie.
     * QuickTime::PasteHandleIntoMovie()
     * @param handle Specifies the handle to be pasted into the movie indicated by the handleType parameter.
     * @param handleType Indicates the data type of the handle specified in the h parameter.
     * @param flags Specifies a constant that further refines conditions of the paste operation.
     * @param userComp Specifies the component or an instance of the component that is to perform the conversion of the data into a QuickTime movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void pasteHandle(QTHandle paramQTHandle, int paramInt1, int paramInt2, MovieImporter paramMovieImporter) throws StdQTException {
        int i = PasteHandleIntoMovie(QTObject.ID(paramQTHandle), paramInt1, _ID(), paramInt2, QTObject.ID(paramMovieImporter));
        StdQTException.checkError(i);
    }

    /**
     * This method takes a movie (or a single track from within that movie) and converts it into a handle of a specified type.
     * QuickTime::PutMovieIntoTypedHandle()
     * @param targetTrack Specifies the track to convert.
     * @param handleType Indicates the type of the new data.
     * @param start Specifies the start time of the segment of the movie or track to be converted.
     * @param dur Specifies the duration of the segment of the movie or track to be converted.
     * @param flags Indicates condition of the conversion. Set this parameter to 0.
     * @param userComp Indicates a component or component instance of the movie export component you
     *  want to perform the conversion. Otherwise, set this parameter to 0 for the Movie Toolbox to
     *  choose the appropriate component.
     * @return a QTHandle object.
     * @return {@link quicktime.QTException}
     */
    public QTHandle putIntoTypedHandle(Track paramTrack, int paramInt1, int paramInt2, int paramInt3, int paramInt4, MovieExporter paramMovieExporter) throws QTException {
        int i = 0;
        if (paramTrack != null) {
            i = QTObject.ID(paramTrack);
        }
        QTHandle localQTHandle = new QTHandle(0, true);
        int j = PutMovieIntoTypedHandle(_ID(), i, paramInt1, QTObject.ID(localQTHandle), paramInt2, paramInt3, paramInt4, QTObject.ID(paramMovieExporter));
        StdQTException.checkError(j);
        return localQTHandle;
    }

    /**
     * The copySettings method copies many settings from one movie to another, overwriting the destination settings in the process.
     * QuickTime::CopyMovieSettings()
     * @param dstMovie Specifies the destination movie for this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void copySettings(Movie paramMovie) throws StdQTException {
        int i = CopyMovieSettings(_ID(), QTObject.ID(paramMovie));
        StdQTException.checkError(i);
    }

    /**
     * This method adds an empty track to the movie. The type of the track and its data reference is specified
     * by the parameters.
     * QuickTime::AddEmptyTrackToMovie()
     * @param srcTrack Specifies the Track object that will be used to determine the type and properties of the empty track.
     * @param dataRef Specifies the default data reference for the empty track.
     * @return a Track object.
     * @return {@link quicktime.QTException}
     */
    public Track addEmptyTrack(Track paramTrack, DataRef paramDataRef) throws QTException {
        int[] arrayOfInt = new int[1];
        int i = AddEmptyTrackToMovie(QTObject.ID(paramTrack), _ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), arrayOfInt);
        StdQTException.checkError(i);
        return new Track(arrayOfInt[0], this);
    }

    /**
     * Create a track in a movie.
     * QuickTime::NewMovieTrack()
     * @param width Specifies the width of the track, in pixels.
     * @param height Specifies the height of the track, in pixels.
     * @param trackVolume Specifies the volume setting of the track.
     * @param {@link quicktime.QTException}
     */
    public Track newTrack(float paramFloat1, float paramFloat2, float paramFloat3) throws QTException {
        return addTrack(paramFloat1, paramFloat2, paramFloat3);
    }

    /**
     * Create a track in a movie.
     * QuickTime::NewMovieTrack()
     * @param width Specifies the width of the track, in pixels.
     * @param height Specifies the height of the track, in pixels.
     * @param trackVolume Specifies the volume setting of the track.
     * @param {@link quicktime.QTException}
     */
    public Track addTrack(float paramFloat1, float paramFloat2, float paramFloat3) throws QTException {
        return new Track(this, paramFloat1, paramFloat2, paramFloat3);
    }

    /**
     * You can create an edit state by calling the NewMovieEditState function.
     * QuickTime::NewMovieEditState()
     * @return a movie's current edit state
     * @return {@link quicktime.QTException}
     */
    public MovieEditState newEditState() throws QTException {
        int i = NewMovieEditState(_ID());
        StdQTException.checkError(GetMoviesError());
        return new MovieEditState(this, i);
    }

    /**
     * Your application can use this method to return a movie to its condition according
     * to an edit state you created previously.
     * QuickTime::UseMovieEditState()
     * @param anEditState Specifies the edit state for this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void useEditState(MovieEditState paramMovieEditState) throws StdQTException {
        int i = UseMovieEditState(_ID(), QTObject.ID(paramMovieEditState));
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to determine the size, in bytes, of the sample data in a segment of a movie.
     * QuickTime::GetMovieDataSize()
     * @param startTime Contains a time value specifying the starting point of the segment.
     * @param duration Contains a time value that specifies the duration of the segment.
     * @return the data size.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getDataSize(int paramInt1, int paramInt2) throws StdQTException {
        int i = GetMovieDataSize(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine whether a specified point lies in the region
     * defined by a movie's final display boundary region after it has been clipped by the movie's
     * display clipping region.
     * QuickTime::PtInMovie()
     * @param pt Specifies the point to be checked.
     * @return whether the point is in the movie or not.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean pointInMovie(QDPoint paramQDPoint) throws StdQTException {
        int i = PtInMovie(_ID(), paramQDPoint.getPoint());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to specify a movie's language.
     * QuickTime::SetMovieLanguage()
     * @param language Specifies the movie's language or region code.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setLanguage(int paramInt) throws StdQTException {
        SetMovieLanguage(_ID(), paramInt);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine whether a movie has changed and needs to be saved.
     * QuickTime::HasMovieChanged()
     * @return whether the movie has changed or not.
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean hasChanged() throws StdQTException {
        int i = HasMovieChanged(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method sets the movie changed flag to indicate that the movie has not been changed.
     * QuickTime::ClearMovieChanged()
     */
    public void clearChanged() throws StdQTException {
        ClearMovieChanged(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets the default data reference for the movie.
     * QuickTime::SetMovieDefaultDataRef()
     * @param newRef Specifies the default data reference object.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setDefaultDataRef(DataRef paramDataRef) throws StdQTException {
        int i = SetMovieDefaultDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(i);
        this.dataRef = paramDataRef;
    }

    /**
     * This method allows your application to get the default data reference for the movie.
     * QuickTime::GetMovieDefaultDataRef()
     * @return a DataRef object specifiying the default data reference for the movie.
     * @return {@link quicktime.QTException}
     */
    public DataRef getDefaultDataRef() throws QTException {
        return DataRef.fromMovie(this);
    }

    /**
     * This method allows your application to assign a color table to the movie.
     * QuickTime::SetMovieColorTable()
     * @param ctab Specifies the new color table.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setColorTable(ColorTable paramColorTable) throws StdQTException {
        int i = SetMovieColorTable(_ID(), QTObject.ID(paramColorTable));
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to get the color table of the movie.
     * QuickTime::GetMovieColorTable()
     * @return a ColorTable object specifiying the current color table.
     * @return {@link quicktime.QTException}
     */
    public ColorTable getColorTable() throws QTException {
        return ColorTable.fromMovie(this);
    }

    /**
     * This method creates a new movie file containing a specified movie.
     * QuickTime::FlattenMovie()
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param fileOut Contains writable movie file for the movie to be created.
     * @param creator Specifies the creator value for the new file.
     * @param scriptTag Specifies the script in which the movie file should be created.
     * @param createQTFileFlags Controls file creation options.
     * @param resName A String with the name of the movie resource. If you set the resName parameter to null,
     *  the toolbox creates an unnamed resource.
     * @return the resource ID number of the new resource
     * @return {@link quicktime.QTException}
     */
    public int flatten(int paramInt1, QTFile paramQTFile, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString) throws QTException {
        short[] arrayOfShort = new short[1];
        arrayOfShort[0] = ((short) paramInt5);
        byte[] arrayOfByte = null;
        if (paramString != null) {
            arrayOfByte = QTUtils.String2PString(paramString, 255);
        }
        FlattenMovie(_ID(), paramInt1, paramQTFile.getFSSpec(false, 512), paramInt2, (short) paramInt3, paramInt4, arrayOfShort, arrayOfByte);
        StdQTException.checkError(GetMoviesError());
        return arrayOfShort[0];
    }

    /**
     * This method creates a new movie file and creates a new movie that contains all of its movie data.
     * QuickTime::FlattenMovieData()
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param fileOut Contains writable movie file for the movie to be created.
     * @param creator Specifies the creator value for the new file.
     * @param scriptTag Specifies the script in which the movie file should be created.
     * @param createQTFileFlags Controls file creation options.
     * @return a Movie object for the new movie.
     * @return {@link quicktime.QTException}
     */
    public Movie flattenData(int paramInt1, QTFile paramQTFile, int paramInt2, int paramInt3, int paramInt4) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            int[] arrayOfInt1 = { 0 };
            int[] arrayOfInt2 = { 0 };
            GetMovieGWorld(_ID(), arrayOfInt1, arrayOfInt2);
            SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            i = FlattenMovieData(_ID(), paramInt1, paramQTFile.getFSSpec(false, 512), paramInt2, (short) paramInt3, paramInt4);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Movie(i, null) : null;
    }

    /**
     * This method creates a new movie that contains all of its movie data loaded into memory.
     * The handle must be the size of the movie's data (see getDataSize() method).
     * QuickTime::FlattenMovieData()
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param handle the handle that is used to contain the new movie's data for the new movie that is returned.
     * @return a Movie object for the new movie.
     * @return {@link quicktime.QTException}
     */
    public Movie flattenData(int paramInt, QTHandle paramQTHandle) throws QTException {
        byte[] arrayOfByte = new byte[8];
        setIntInArray(arrayOfByte, 0, 1752065132);
        setIntInArray(arrayOfByte, 4, QTObject.ID(paramQTHandle));
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            int[] arrayOfInt1 = { 0 };
            int[] arrayOfInt2 = { 0 };
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

    /**
     * This method allows you to attach a progress function to a movie. Currently it
     * assigns the default progress proc to the movie.
     * QuickTime::SetMovieProgressProc()
     */
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

    /**
     * This method allows you to attach a progress function to a movie.
     * QuickTime::SetMovieProgressProc()
     * @param mp the new MovieProgress object
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method allows you to intercept executing wired actions of this movie.
     * QuickTime::AddMovieExecuteWiredActionsProc()
     * @param wa the new ExecutingWiredActions object
     * @param {@link quicktime.std.StdQTException}
     */
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

    /**
     * This method allows you to remove the existing executing wired actions of this movie.
     * QuickTime::RemoveMovieExecuteWiredActionsProc()
     */
    public void removeExecuteWiredActionsProc() throws StdQTException {
        if (this.wiredUPP != null) {
            int i = RemoveMovieExecuteWiredActionsProc(_ID(), this.wiredUPP.ID(), 0);
            this.wiredUPP.cleanupMethodClosure();
            this.wiredUPP = null;
            StdQTException.checkError(i);
        }
    }

    /**
     * This method executes specified wired actions associated with the movie
     * QuickTime::MovieExecuteWiredActions()
     * @param flags execution flags
     * @param {@link quicktime.std.StdQTException}
     */
    public void executeWiredActions(int paramInt, AtomContainer paramAtomContainer) throws StdQTException {
        StdQTException.checkError(MovieExecuteWiredActions(_ID(), paramInt, QTObject.ID(paramAtomContainer)));
    }

    /**
     * This method allows you to search for text in a movie.
     * QuickTime::MovieSearchText()
     * @param text Specifies the text to be searched.
     * @param searchFlags controls the operation of this method.
     * @param searchTrack Specifes the track which you want to limit the search to that track.
     * @param searchTime Specifes the starting time for the search
     * @param searchOffset Specifes the offset in media samples. Search will begin from that offset.
     * @return a SearchResult object.
     * @return {@link quicktime.QTException}
     */
    public SearchResult searchText(QTPointer paramQTPointer, int paramInt1, Track paramTrack, int paramInt2, int paramInt3) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        arrayOfInt1[0] = QTObject.ID(paramTrack);
        arrayOfInt2[0] = paramInt2;
        arrayOfInt3[0] = paramInt3;
        int i = MovieSearchText(_ID(), QTObject.ID(paramQTPointer), paramQTPointer.getSize(), paramInt1, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new SearchResult(new Track(arrayOfInt1[0], this), arrayOfInt2[0], arrayOfInt3[0]);
    }

    /**
     * This method allows you to search for text in a movie.
     * QuickTime::MovieSearchText()
     * @param text Specifies th etext to be searched.
     * @param searchFlags controls the operation of this method.
     * @param searchTime Specifes the starting time for the search
     * @param searchOffset Specifes the offset in media samples. Search will begin from that offset.
     * @return a SearchResult object.
     * @return {@link quicktime.QTException}
     */
    public SearchResult searchText(QTPointer paramQTPointer, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        arrayOfInt1[0] = 0;
        arrayOfInt2[0] = paramInt2;
        arrayOfInt3[0] = paramInt3;
        int i = MovieSearchText(_ID(), QTObject.ID(paramQTPointer), paramQTPointer.getSize(), paramInt1, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new SearchResult(new Track(arrayOfInt1[0], this), arrayOfInt2[0], arrayOfInt3[0]);
    }

    /**
     * This method allows you to obtain a poster's boundary rectangle.
     * QuickTime::GetPosterBox()
     * @return a Rectangle object specifying the poster box for the movie.
     * @return {@link quicktime.std.StdQTException}
     */
    public QDRect getPosterBox() throws StdQTException {
        QDRect localQDRect = new QDRect();
        GetPosterBox(_ID(), localQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
        return localQDRect;
    }

    /**
     * This method allows you to set a poster's boundary rectangle.
     * QuickTime::SetPosterBox()
     * @param boxRect a Rectangle object specifying the poster box for the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPosterBox(QDRect paramQDRect) throws StdQTException {
        SetPosterBox(_ID(), paramQDRect.getRect());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a movie's display boundary region during a specified segment.
     * QuickTime::GetMovieSegmentDisplayBoundsRgn()
     * @param time Specifies the starting time of the movie segment to consider.
     * @param duration Specifies the length of the segment to consider.
     * @return a Region object.
     * @return {@link quicktime.QTException}
     */
    public Region getSegmentDisplayBoundsRgn(int paramInt1, int paramInt2) throws QTException {
        return Region.fromMovieSegment(this, paramInt1, paramInt2);
    }

    /**
     * This method searches for errors in all the enabled tracks of the movie.
     * QuickTime::GetMovieStatus()
     * @return a StatusInfo object specifying the first found problem track. Nothing is returned
     *  if there is no problem track.
     * @return {@link quicktime.QTException}
     */
    public StatusInfo getStatus() throws QTException {
        int[] arrayOfInt = new int[1];
        int i = GetMovieStatus(_ID(), arrayOfInt);
        if (i != 0) {
            return new StatusInfo(i, new Track(arrayOfInt[0], this));
        }
        return null;
    }

    /**
     * This method displays a dialog giving information about the movie.
     * QuickTime::ShowMovieInformation()
     */
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

    /**
     * This method allows your application to place a movie onto the scrap.
     * QuickTime::PutMovieOnScrap()
     * @param movieScrapFlags Flags that control the operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void putOnScrap(int paramInt) throws StdQTException {
        int i = PutMovieOnScrap(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to provide information to the Movie Toolbox that can influence movie playback.
     * QuickTime::SetMoviePlayHints()
     * @param flags Specifies the optimizations that can be used with this movie.
     * @param flagsMask Indicates which flags in the flags parameter are to be considered in this operation.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setPlayHints(int paramInt1, int paramInt2) throws StdQTException {
        SetMoviePlayHints(_ID(), paramInt1, paramInt2);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to get movie's TimeBase.
     * QuickTime::GetMovieTimeBase()
     * @return a TimeBase object for movie's timebase
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromMovie(this);
    }

    /**
     * This method allows you to assign a master time base to a movie.
     * QuickTime::SetMovieMasterTimeBase()
     * @param tb Specifies the master time base to be assigned to this movie.
     * @param slaveZero Specifies the time, in the time scale of the master time base, that corresponds to a 0 time value for the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMasterTimeBase(TimeBase paramTimeBase, TimeRecord paramTimeRecord) throws StdQTException {
        SetMovieMasterTimeBase(_ID(), QTObject.ID(paramTimeBase), QTObject.ID(paramTimeRecord));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to assign a clock component to a movie.
     * QuickTime::SetMovieMasterClock()
     * @param slaveZero Contains the time, in the clock's time scale, that corresponds to a 0 time value for the movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMasterClock(Clock paramClock, TimeRecord paramTimeRecord) throws StdQTException {
        SetMovieMasterClock(_ID(), QTObject.ID(paramClock), QTObject.ID(paramTimeRecord));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method converts this movie into a specified file and type.
     * QuickTime::ConvertMovieToFile()
     * @param onlyTrack Specifies the track within the source movie for this conversion operation. To specify all tracks, set the value of this parameter to null.
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @return the resource id of the movie created.
     * @return {@link quicktime.QTException}
     */
    public int convertToFile(Track paramTrack, QTFile paramQTFile, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        return convertToFile(paramTrack, paramQTFile, paramInt1, paramInt2, paramInt3, 0, null);
    }

    /**
     * This method converts this movie into a specified file and type.
     * QuickTime::ConvertMovieToFile()
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @return the resource id of the movie created.
     * @return {@link quicktime.QTException}
     */
    public int convertToFile(QTFile paramQTFile, int paramInt1, int paramInt2, int paramInt3) throws QTException {
        return convertToFile(null, paramQTFile, paramInt1, paramInt2, paramInt3, 0, null);
    }

    /**
     * This method converts this movie into a specified file and type.
     * QuickTime::ConvertMovieToFile()
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @param flags the appropriate flags for this operation
     * @return the resource id of the movie created.
     * @return {@link quicktime.QTException}
     */
    public int convertToFile(QTFile paramQTFile, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws QTException {
        return convertToFile(null, paramQTFile, paramInt1, paramInt2, paramInt3, paramInt4, null);
    }

    /**
     * This method converts this movie into a specified file and type.
     * QuickTime::ConvertMovieToFile()
     * @param onlyTrack Specifies the track within the source movie for this conversion operation. To specify all tracks, set the value of this parameter to null.
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @param flags the appropriate flags for this operation
     * @param userComp A ComponentIdentifier of type 'spit' that will be opened and used by this call to export the Movie
     * @return the resource id of the movie created.
     * @return {@link quicktime.QTException}
     * @return {@link quicktime.std.StdQTException}
     */
    public int convertToFile(final Track onlyTrack, QTFile paramQTFile, final int fileType, final int creator, final int scriptTag, final int flags, final MovieExporter userComp) throws QTException, StdQTException {
        final short[] resID_native = new short[1];
        final byte[] fsspec = paramQTFile.getFSSpec(false, 512);
        int i1;
        if (QTSession.isCurrentOS(4)) {
            i1 = ((Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                int res1;

                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        this.res1 = Movie.ConvertMovieToFile(_ID(), QTObject.ID(onlyTrack), fsspec, fileType, creator, (short) scriptTag, resID_native, flags, QTObject.ID(userComp));
                    }
                    return this.res1;
                }
            })).intValue();
            StdQTException.checkError(i1);
            return resID_native[0];
        }
        synchronized (QTNative.globalsLock) {
            i1 = ConvertMovieToFile(_ID(), QTObject.ID(onlyTrack), paramQTFile.getFSSpec(false, 512), fileType, creator, (short) scriptTag, resID_native, flags, QTObject.ID(userComp));
        }
        StdQTException.checkError(i1);
        return resID_native[0];
    }

    /**
     * The QTVRGetQTVRTrack function returns a track for the QTVR track
     * that has the index specified by the index parameter in the QuickTime movie specified by the
     * theMovie parameter. If there is no such track, QTVRGetQTVRTrack returns the value null.
     * QuickTime::QTVRGetQTVRTrack()
     * @param index this value indicates the nth VR Track to return. QT3.0 only supports one VR track so this
     *  value should be set to 1.
     * @param {@link quicktime.QTException}
     */
    public Track getQTVRTrack(int paramInt) throws QTException {
        int i = QTVRGetQTVRTrack(_ID(), paramInt);
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * Print information about this object.
     *
     * @return a string representing this Movie object
     */
    public String toString() {
        try {
            return super.toString() + "[bounds=" + getBounds() + "]";
        } catch (StdQTException localStdQTException) {
        }
        return super.toString() + "[Error getting movie information.]";
    }

    /**
     * Determine whether a movie is being progressively downloaded..
     * QuickTime::QTMovieNeedsTimeTable()
     * @return a boolean if movie is being progressively downloaded
     * @return {@link quicktime.std.StdQTException}
     */
    public final boolean needsTimeTable() throws StdQTException {
        byte[] arrayOfByte = new byte[1];
        int i = QTMovieNeedsTimeTable(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return arrayOfByte[0] != 0;
    }

    /**
     * Returns the duration of the part of a movie that has already been downloaded.
     * QuickTime::GetMaxLoadedTimeInMovie()
     */
    public final int maxLoadedTimeInMovie() throws StdQTException {
        int[] arrayOfInt = new int[1];
        int i = GetMaxLoadedTimeInMovie(_ID(), arrayOfInt);
        StdQTException.checkError(i);
        return arrayOfInt[0];
    }

    /**
     * Copies the user data in the movie to the destination movie
     * QuickTime::CopyMovieUserData()
     * @param destMovie destination movie for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyUserDataToMovie(Movie paramMovie, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(_ID(), QTObject.ID(paramMovie), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified movie into this movie
     * QuickTime::CopyMovieUserData()
     * @param srcMovie source movie for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @param {@link quicktime.std.StdQTException}
     */
    public void copyUserDataFromMovie(Movie paramMovie, boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(QTObject.ID(paramMovie), _ID(), paramBoolean == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified movie into the destination movie
     * QuickTime::CopyMovieUserData()
     * @param srcMovie source movie for user data
     * @param destMovie destination movie for user data
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @param {@link quicktime.std.StdQTException}
     */
    public static void copyUserData(Movie paramMovie1, Movie paramMovie2, int paramInt) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(QTObject.ID(paramMovie1), QTObject.ID(paramMovie2), paramInt));
    }

    /**
     * Looks for the first media handler that has a custom clock and uses that as the Movie's
     * master timebase clock. This is useful to tie the movies master timebase to a sound clock
     * if there is a sound track.
     *
     * QuickTime::ChooseMovieClock()
     * @param flags the movie flags
     */
    public void chooseClock(int paramInt) {
        ChooseMovieClock(_ID(), paramInt);
    }

    /**
     * QuickTime can handle public movie handles with self contained data references.
     * (Requires QT 6)
     * QuickTime::PutMovieForDataRefIntoHandle()
     * @param ref the dataref
     * @param handle the handle
     * @param {@link quicktime.std.StdQTException}
     */
    public void putDataRefIntoHandle(DataRef paramDataRef, QTHandle paramQTHandle) throws StdQTException {
        StdQTException.checkError(PutMovieForDataRefIntoHandle(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType(), QTObject.ID(paramQTHandle)));
    }

    /**
     * QuickTime can handle public movie handles with self contained data references.
     * (Requires QT 6)
     * QuickTime::NewMovieForDataRefFromHandle()
     * @param dataRef the dataref of movie to be created
     * @param publicHandle the handle containing the movie
     * @param flags the movie creation flags
     * @param info movie info (this can be null)
     * @return movie
     * @return {@link quicktime.std.StdQTException}
     */
    public Movie forDataRefFromHandle(DataRef paramDataRef, QTHandle paramQTHandle, int paramInt, MovieInfo paramMovieInfo) throws StdQTException {
        int[] arrayOfInt = new int[1];
        byte[] arrayOfByte = new byte[1];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieForDataRefFromHandle(arrayOfInt, QTObject.ID(paramQTHandle), (short) paramInt, arrayOfByte, QTObject.ID(paramDataRef), paramDataRef.getType());
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

    /**
     * Allows the user to ask the QuickTime idle manager when idling routines such as movies.task()
     * need to be called. (Requires QT 6)
     * QuickTime::QTGetTimeUntilNextTask()
     * @param scale the time scale to be used for the return value
     * @return the amount of time that needs to elapse in the given time scale before
     *  QuickTime's idling mechanism needs to be called
     * @return {@link quicktime.std.StdQTException}
     */
    public static int getTimeUntilNextTask(int paramInt) throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(QTGetTimeUntilNextTask(arrayOfInt, paramInt));
        return arrayOfInt[0];
    }

    /**
     * Allows the user to specify one or more callback routines that will be called when QuickTime
     * needs tasking. (Requires QT 6)
     * QuickTime::QTInstallNextTaskNeededSoonerCallback()
     */
    public static void setNextTaskNeededSoonerCallback(NextTaskNeededSooner paramNextTaskNeededSooner, int paramInt1, int paramInt2) throws StdQTException {
        if (paramNextTaskNeededSooner == null) {
            return;
        }
        IdleDispatcher localIdleDispatcher = new IdleDispatcher(paramNextTaskNeededSooner);
        Object localObject = dispatchers.put(paramNextTaskNeededSooner, localIdleDispatcher);
        StdQTException.checkError(QTInstallNextTaskNeededSoonerCallback(localIdleDispatcher.ID(), paramInt1, paramInt2, 0));
    }

    /**
     * Allows the user to remove a callback from the IdleManager that no longer needs servicing.
     * (Requires QT 6)
     * QuickTime::QTUninstallNextTaskNeededSoonerCallback()
     */
    public static void removeNextTaskNeededSoonerCallback(NextTaskNeededSooner paramNextTaskNeededSooner) throws StdQTException {
        if (paramNextTaskNeededSooner == null) {
            return;
        }
        IdleDispatcher localIdleDispatcher = (IdleDispatcher) dispatchers.get(paramNextTaskNeededSooner);
        localIdleDispatcher.cleanupMethodClosure();
        StdQTException.checkError(QTUninstallNextTaskNeededSoonerCallback(localIdleDispatcher.ID(), 0));
    }

    private static Hashtable dispatchers = new Hashtable<>();

    /**
     * This method creates a new movie file containing a specified movie.
     * (Requires QT 6)
     * QuickTime::FlattenMovieDataToDataRef()
     * @param dataRef Contains dataRef for the movie to be created.
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param creator Specifies the creator value for the new file.
     * @param scriptTag Specifies the script in which the movie file should be created.
     * @param createMovieFileFlags Controls movie creation options.
     * @return the new movie
     * @return {@link quicktime.QTException}
     */
    public Movie flattenDataToDataRef(DataRef paramDataRef, int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws QTException {
        int i = 0;
        Movie localMovie = null;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            int[] arrayOfInt1 = { 0 };
            int[] arrayOfInt2 = { 0 };
            GetMovieGWorld(_ID(), arrayOfInt1, arrayOfInt2);
            SetGWorld(arrayOfInt1[0], arrayOfInt2[0]);
            i = FlattenMovieDataToDataRef(_ID(), paramInt1, QTObject.ID(paramDataRef), paramDataRef.getType(), paramInt2, (short) paramInt3, paramInt4);
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

    /**
     * To resolve relative data references, a method was needed to determine the original location from which a movie was opened or imported.
     * This routine sets the movie's anchor data reference. Pass null to reset the anchor data reference to nothing.
     * The routine copies the data reference.
     * @param dataRef the dataref to the movie
     * @param {@link quicktime.std.StdQTException}
     */
    public void setMovieAnchorDataRef(DataRef paramDataRef) throws StdQTException {
        int i = 0;
        i = SetMovieAnchorDataRef(_ID(), QTObject.ID(paramDataRef), paramDataRef.getType());
        StdQTException.checkError(i);
    }

    private boolean isMovieProgressProc() {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        GetMovieProgressProc(_ID(), arrayOfInt1, arrayOfInt2);
        return arrayOfInt1[0] != 0;
    }

    /**
     * If there is no anchor data reference associated with the movie the method returns true.
     * @return the anchor flag (If there is no anchor data reference associated with the movie then kMovieAnchorDataRefIsDefault is returned)
     * @return {@link quicktime.std.StdQTException}
     */
    public boolean isAnchorDefaultDataRef() throws StdQTException {
        int i = 0;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        int[] arrayOfInt3 = { 0 };
        i = GetMovieAnchorDataRef(_ID(), arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return arrayOfInt3[0] == 1;
    }

    private static void MoviesTask(int paramInt1, int paramInt2) {
        MovieToolbox.MoviesTask(paramInt1, paramInt2);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static short CreateMovieFile(byte[] paramArrayOfByte, int paramInt1, short paramShort, int paramInt2, short[] paramArrayOfShort, int[] paramArrayOfInt) {
        return MovieToolbox.CreateMovieFile(paramArrayOfByte, paramInt1, paramShort, paramInt2, paramArrayOfShort, paramArrayOfInt);
    }

    private static int SGGetMovie(int paramInt) {
        return SequenceGrabberManager.SGGetMovie(paramInt);
    }

    private static int NewMovieFromScrap(int paramInt) {
        return MovieToolbox.NewMovieFromScrap(paramInt);
    }

    private static short NewMovieFromFile(int[] paramArrayOfInt, short paramShort1, short[] paramArrayOfShort, byte[] paramArrayOfByte1, short paramShort2, byte[] paramArrayOfByte2) {
        return MovieToolbox.NewMovieFromFile(paramArrayOfInt, paramShort1, paramArrayOfShort, paramArrayOfByte1, paramShort2, paramArrayOfByte2);
    }

    private static short NewMovieFromHandle(int[] paramArrayOfInt, int paramInt, short paramShort, byte[] paramArrayOfByte) {
        return MovieToolbox.NewMovieFromHandle(paramArrayOfInt, paramInt, paramShort, paramArrayOfByte);
    }

    private static short NewMovieFromDataFork(int[] paramArrayOfInt, short paramShort1, int paramInt, short paramShort2, byte[] paramArrayOfByte) {
        return MovieToolbox.NewMovieFromDataFork(paramArrayOfInt, paramShort1, paramInt, paramShort2, paramArrayOfByte);
    }

    private static short NewMovieFromDataRef(int[] paramArrayOfInt, short paramShort, short[] paramArrayOfShort, int paramInt1, int paramInt2) {
        return MovieToolbox.NewMovieFromDataRef(paramArrayOfInt, paramShort, paramArrayOfShort, paramInt1, paramInt2);
    }

    private static int NewMovie(int paramInt) {
        return MovieToolbox.NewMovie(paramInt);
    }

    private static short AddMovieResource(int paramInt, short paramShort, short[] paramArrayOfShort, byte[] paramArrayOfByte) {
        return MovieToolbox.AddMovieResource(paramInt, paramShort, paramArrayOfShort, paramArrayOfByte);
    }

    private static short UpdateMovieResource(int paramInt, short paramShort1, short paramShort2, byte[] paramArrayOfByte) {
        return MovieToolbox.UpdateMovieResource(paramInt, paramShort1, paramShort2, paramArrayOfByte);
    }

    private static short RemoveMovieResource(short paramShort1, short paramShort2) {
        return MovieToolbox.RemoveMovieResource(paramShort1, paramShort2);
    }

    private static short PrerollMovie(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.PrerollMovie(paramInt1, paramInt2, paramInt3);
    }

    private static short LoadMovieIntoRam(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.LoadMovieIntoRam(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static void SetMovieActive(int paramInt, byte paramByte) {
        MovieToolbox.SetMovieActive(paramInt, paramByte);
    }

    private static byte GetMovieActive(int paramInt) {
        return MovieToolbox.GetMovieActive(paramInt);
    }

    private static void StartMovie(int paramInt) {
        MovieToolbox.StartMovie(paramInt);
    }

    private static void StopMovie(int paramInt) {
        MovieToolbox.StopMovie(paramInt);
    }

    private static void GoToBeginningOfMovie(int paramInt) {
        MovieToolbox.GoToBeginningOfMovie(paramInt);
    }

    private static void GoToEndOfMovie(int paramInt) {
        MovieToolbox.GoToEndOfMovie(paramInt);
    }

    private static byte IsMovieDone(int paramInt) {
        return MovieToolbox.IsMovieDone(paramInt);
    }

    private static byte GetMoviePreviewMode(int paramInt) {
        return MovieToolbox.GetMoviePreviewMode(paramInt);
    }

    private static void SetMoviePreviewMode(int paramInt, byte paramByte) {
        MovieToolbox.SetMoviePreviewMode(paramInt, paramByte);
    }

    private static void ShowMoviePoster(int paramInt) {
        MovieToolbox.ShowMoviePoster(paramInt);
    }

    private static void PlayMoviePreview(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.PlayMoviePreview(paramInt1, paramInt2, paramInt3);
    }

    private static void SetMovieGWorld(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMovieGWorld(paramInt1, paramInt2, paramInt3);
    }

    private static void GetMovieNaturalBoundsRect(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.GetMovieNaturalBoundsRect(paramInt, paramArrayOfByte);
    }

    private static int GetNextTrackForCompositing(int paramInt1, int paramInt2) {
        return MovieToolbox.GetNextTrackForCompositing(paramInt1, paramInt2);
    }

    private static int GetPrevTrackForCompositing(int paramInt1, int paramInt2) {
        return MovieToolbox.GetPrevTrackForCompositing(paramInt1, paramInt2);
    }

    private static short UpdateMovie(int paramInt) {
        return MovieToolbox.UpdateMovie(paramInt);
    }

    private static short InvalidateMovieRegion(int paramInt1, int paramInt2) {
        return MovieToolbox.InvalidateMovieRegion(paramInt1, paramInt2);
    }

    private static void GetMovieBox(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.GetMovieBox(paramInt, paramArrayOfByte);
    }

    private static void SetMovieBox(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.SetMovieBox(paramInt, paramArrayOfByte);
    }

    private static void SetMovieDisplayClipRgn(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieDisplayClipRgn(paramInt1, paramInt2);
    }

    private static void SetMovieClipRgn(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieClipRgn(paramInt1, paramInt2);
    }

    private static short PutMovieIntoHandle(int paramInt1, int paramInt2) {
        return MovieToolbox.PutMovieIntoHandle(paramInt1, paramInt2);
    }

    private static short PutMovieIntoDataFork(int paramInt1, short paramShort, int paramInt2, int paramInt3) {
        return MovieToolbox.PutMovieIntoDataFork(paramInt1, paramShort, paramInt2, paramInt3);
    }

    private static int GetMovieCreationTime(int paramInt) {
        return MovieToolbox.GetMovieCreationTime(paramInt);
    }

    private static int GetMovieModificationTime(int paramInt) {
        return MovieToolbox.GetMovieModificationTime(paramInt);
    }

    private static int GetMovieTimeScale(int paramInt) {
        return MovieToolbox.GetMovieTimeScale(paramInt);
    }

    private static void SetMovieTimeScale(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieTimeScale(paramInt1, paramInt2);
    }

    private static int GetMovieDuration(int paramInt) {
        return MovieToolbox.GetMovieDuration(paramInt);
    }

    private static int GetMovieRate(int paramInt) {
        return MovieToolbox.GetMovieRate(paramInt);
    }

    private static void SetMovieRate(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieRate(paramInt1, paramInt2);
    }

    private static int GetMoviePreferredRate(int paramInt) {
        return MovieToolbox.GetMoviePreferredRate(paramInt);
    }

    private static void SetMoviePreferredRate(int paramInt1, int paramInt2) {
        MovieToolbox.SetMoviePreferredRate(paramInt1, paramInt2);
    }

    private static short GetMoviePreferredVolume(int paramInt) {
        return MovieToolbox.GetMoviePreferredVolume(paramInt);
    }

    private static void SetMoviePreferredVolume(int paramInt, short paramShort) {
        MovieToolbox.SetMoviePreferredVolume(paramInt, paramShort);
    }

    private static short GetMovieVolume(int paramInt) {
        return MovieToolbox.GetMovieVolume(paramInt);
    }

    private static void SetMovieVolume(int paramInt, short paramShort) {
        MovieToolbox.SetMovieVolume(paramInt, paramShort);
    }

    private static void GetMovieMatrix(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.GetMovieMatrix(paramInt, paramArrayOfByte);
    }

    private static void SetMovieMatrix(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.SetMovieMatrix(paramInt, paramArrayOfByte);
    }

    private static void GetMoviePreviewTime(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMoviePreviewTime(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void SetMoviePreviewTime(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMoviePreviewTime(paramInt1, paramInt2, paramInt3);
    }

    private static int GetMoviePosterTime(int paramInt) {
        return MovieToolbox.GetMoviePosterTime(paramInt);
    }

    private static void SetMoviePosterTime(int paramInt1, int paramInt2) {
        MovieToolbox.SetMoviePosterTime(paramInt1, paramInt2);
    }

    private static void GetMovieSelection(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMovieSelection(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void SetMovieSelection(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMovieSelection(paramInt1, paramInt2, paramInt3);
    }

    private static void SetMovieActiveSegment(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMovieActiveSegment(paramInt1, paramInt2, paramInt3);
    }

    private static void GetMovieActiveSegment(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMovieActiveSegment(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static int GetMovieTime(int paramInt1, int paramInt2) {
        return MovieToolbox.GetMovieTime(paramInt1, paramInt2);
    }

    private static void SetMovieTime(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieTime(paramInt1, paramInt2);
    }

    private static void SetMovieTimeValue(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieTimeValue(paramInt1, paramInt2);
    }

    private static int GetMovieTrackCount(int paramInt) {
        return MovieToolbox.GetMovieTrackCount(paramInt);
    }

    private static int GetMovieTrack(int paramInt1, int paramInt2) {
        return MovieToolbox.GetMovieTrack(paramInt1, paramInt2);
    }

    private static int GetMovieIndTrack(int paramInt1, int paramInt2) {
        return MovieToolbox.GetMovieIndTrack(paramInt1, paramInt2);
    }

    private static int GetMovieIndTrackType(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.GetMovieIndTrackType(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int GetTrackMovie(int paramInt) {
        return MovieToolbox.GetTrackMovie(paramInt);
    }

    private static void DisposeMovieTrack(int paramInt) {
        MovieToolbox.DisposeMovieTrack(paramInt);
    }

    private static void SetAutoTrackAlternatesEnabled(int paramInt, byte paramByte) {
        MovieToolbox.SetAutoTrackAlternatesEnabled(paramInt, paramByte);
    }

    private static void SelectMovieAlternates(int paramInt) {
        MovieToolbox.SelectMovieAlternates(paramInt);
    }

    private static void GetMovieNextInterestingTime(int paramInt1, short paramShort1, short paramShort2, int[] paramArrayOfInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        MovieToolbox.GetMovieNextInterestingTime(paramInt1, paramShort1, paramShort2, paramArrayOfInt1, paramInt2, paramInt3, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static short InsertEmptyMovieSegment(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.InsertEmptyMovieSegment(paramInt1, paramInt2, paramInt3);
    }

    private static short InsertMovieSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.InsertMovieSegment(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short DeleteMovieSegment(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.DeleteMovieSegment(paramInt1, paramInt2, paramInt3);
    }

    private static short ScaleMovieSegment(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.ScaleMovieSegment(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int CutMovieSelection(int paramInt) {
        return MovieToolbox.CutMovieSelection(paramInt);
    }

    private static int CopyMovieSelection(int paramInt) {
        return MovieToolbox.CopyMovieSelection(paramInt);
    }

    private static void PasteMovieSelection(int paramInt1, int paramInt2) {
        MovieToolbox.PasteMovieSelection(paramInt1, paramInt2);
    }

    private static void AddMovieSelection(int paramInt1, int paramInt2) {
        MovieToolbox.AddMovieSelection(paramInt1, paramInt2);
    }

    private static void ClearMovieSelection(int paramInt) {
        MovieToolbox.ClearMovieSelection(paramInt);
    }

    private static short PasteHandleIntoMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.PasteHandleIntoMovie(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static short PutMovieIntoTypedHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
        return MovieToolbox.PutMovieIntoTypedHandle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
    }

    private static short CopyMovieSettings(int paramInt1, int paramInt2) {
        return MovieToolbox.CopyMovieSettings(paramInt1, paramInt2);
    }

    private static short AddEmptyTrackToMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
        return MovieToolbox.AddEmptyTrackToMovie(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    private static int NewMovieEditState(int paramInt) {
        return MovieToolbox.NewMovieEditState(paramInt);
    }

    private static short UseMovieEditState(int paramInt1, int paramInt2) {
        return MovieToolbox.UseMovieEditState(paramInt1, paramInt2);
    }

    private static int GetMovieDataSize(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.GetMovieDataSize(paramInt1, paramInt2, paramInt3);
    }

    private static byte PtInMovie(int paramInt1, int paramInt2) {
        return MovieToolbox.PtInMovie(paramInt1, paramInt2);
    }

    private static void SetMovieLanguage(int paramInt1, int paramInt2) {
        MovieToolbox.SetMovieLanguage(paramInt1, paramInt2);
    }

    private static byte HasMovieChanged(int paramInt) {
        return MovieToolbox.HasMovieChanged(paramInt);
    }

    private static void ClearMovieChanged(int paramInt) {
        MovieToolbox.ClearMovieChanged(paramInt);
    }

    private static short SetMovieDefaultDataRef(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.SetMovieDefaultDataRef(paramInt1, paramInt2, paramInt3);
    }

    private static short SetMovieColorTable(int paramInt1, int paramInt2) {
        return MovieToolbox.SetMovieColorTable(paramInt1, paramInt2);
    }

    private static void FlattenMovie(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, short paramShort, int paramInt4, short[] paramArrayOfShort, byte[] paramArrayOfByte2) {
        MovieToolbox.FlattenMovie(paramInt1, paramInt2, paramArrayOfByte1, paramInt3, paramShort, paramInt4, paramArrayOfShort, paramArrayOfByte2);
    }

    private static int FlattenMovieData(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, short paramShort, int paramInt4) {
        return MovieToolbox.FlattenMovieData(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramShort, paramInt4);
    }

    private static void setIntInArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        MemoryManager.setIntInArray(paramArrayOfByte, paramInt1, paramInt2);
    }

    private static void SetMovieProgressProc(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMovieProgressProc(paramInt1, paramInt2, paramInt3);
    }

    private static void GetMovieProgressProc(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMovieProgressProc(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short MovieSearchText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return MovieToolbox.MovieSearchText(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }

    private static void GetPosterBox(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.GetPosterBox(paramInt, paramArrayOfByte);
    }

    private static void SetPosterBox(int paramInt, byte[] paramArrayOfByte) {
        MovieToolbox.SetPosterBox(paramInt, paramArrayOfByte);
    }

    private static int GetMovieStatus(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.GetMovieStatus(paramInt, paramArrayOfInt);
    }

    private static void ShowMovieInformation(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.ShowMovieInformation(paramInt1, paramInt2, paramInt3);
    }

    private static short PutMovieOnScrap(int paramInt1, int paramInt2) {
        return MovieToolbox.PutMovieOnScrap(paramInt1, paramInt2);
    }

    private static void SetMoviePlayHints(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMoviePlayHints(paramInt1, paramInt2, paramInt3);
    }

    private static void SetMovieMasterTimeBase(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMovieMasterTimeBase(paramInt1, paramInt2, paramInt3);
    }

    private static void SetMovieMasterClock(int paramInt1, int paramInt2, int paramInt3) {
        MovieToolbox.SetMovieMasterClock(paramInt1, paramInt2, paramInt3);
    }

    private static short ConvertMovieToFile(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, short paramShort, short[] paramArrayOfShort, int paramInt5, int paramInt6) {
        return MovieToolbox.ConvertMovieToFile(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramInt4, paramShort, paramArrayOfShort, paramInt5, paramInt6);
    }

    private static int QTVRGetQTVRTrack(int paramInt1, int paramInt2) {
        return VRManager.QTVRGetQTVRTrack(paramInt1, paramInt2);
    }

    private static short QTMovieNeedsTimeTable(int paramInt, byte[] paramArrayOfByte) {
        return MovieToolbox.QTMovieNeedsTimeTable(paramInt, paramArrayOfByte);
    }

    private static short GetMaxLoadedTimeInMovie(int paramInt, int[] paramArrayOfInt) {
        return MovieToolbox.GetMaxLoadedTimeInMovie(paramInt, paramArrayOfInt);
    }

    private static short PrePrerollMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        return MovieToolbox.PrePrerollMovie(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    private static void AbortPrePrerollMovie(int paramInt, short paramShort) {
        MovieToolbox.AbortPrePrerollMovie(paramInt, paramShort);
    }

    private static void SetMovieDrawingCompleteProc(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        MovieToolbox.SetMovieDrawingCompleteProc(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short AddMovieExecuteWiredActionsProc(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.AddMovieExecuteWiredActionsProc(paramInt1, paramInt2, paramInt3);
    }

    private static short RemoveMovieExecuteWiredActionsProc(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.RemoveMovieExecuteWiredActionsProc(paramInt1, paramInt2, paramInt3);
    }

    private static short MovieExecuteWiredActions(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.MovieExecuteWiredActions(paramInt1, paramInt2, paramInt3);
    }

    private static void SetGWorld(int paramInt1, int paramInt2) {
        QuickDrawManager.SetGWorld(paramInt1, paramInt2);
    }

    private static void GetGWorld(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        QuickDrawManager.GetGWorld(paramArrayOfInt1, paramArrayOfInt2);
    }

    private static void GetMovieGWorld(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        MovieToolbox.GetMovieGWorld(paramInt, paramArrayOfInt1, paramArrayOfInt2);
    }

    private static short CopyMovieUserData(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.CopyMovieUserData(paramInt1, paramInt2, paramInt3);
    }

    private static void ChooseMovieClock(int paramInt1, int paramInt2) {
        MovieToolbox.ChooseMovieClock(paramInt1, paramInt2);
    }

    private static short NewMovieForDataRefFromHandle(int[] paramArrayOfInt, int paramInt1, short paramShort, byte[] paramArrayOfByte, int paramInt2, int paramInt3) {
        return MovieToolbox.NewMovieForDataRefFromHandle(paramArrayOfInt, paramInt1, paramShort, paramArrayOfByte, paramInt2, paramInt3);
    }

    private static short PutMovieForDataRefIntoHandle(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.PutMovieForDataRefIntoHandle(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static int FlattenMovieDataToDataRef(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short paramShort, int paramInt6) {
        return MovieToolbox.FlattenMovieDataToDataRef(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramShort, paramInt6);
    }

    private static short QTGetTimeUntilNextTask(int[] paramArrayOfInt, int paramInt) {
        return MovieToolbox.QTGetTimeUntilNextTask(paramArrayOfInt, paramInt);
    }

    private static short QTInstallNextTaskNeededSoonerCallback(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return MovieToolbox.QTInstallNextTaskNeededSoonerCallback(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    private static short QTUninstallNextTaskNeededSoonerCallback(int paramInt1, int paramInt2) {
        return MovieToolbox.QTUninstallNextTaskNeededSoonerCallback(paramInt1, paramInt2);
    }

    private static short SetMovieAnchorDataRef(int paramInt1, int paramInt2, int paramInt3) {
        return MovieToolbox.SetMovieAnchorDataRef(paramInt1, paramInt2, paramInt3);
    }

    private static short GetMovieAnchorDataRef(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3) {
        return MovieToolbox.GetMovieAnchorDataRef(paramInt, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    }
}

