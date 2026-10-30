package quicktime.std.movies;

import java.io.IOException;
import java.util.Hashtable;

import com.apple.mrj.macos.carbon.CarbonAccess;
import com.apple.mrj.macos.carbon.CarbonEventClient;
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

    private static final int[] savedPort = { 0 };

    private static final int[] savedDevice = { 0 };

    /**
     * The moviesTask method services all of the currently active movies.
     * You should call moviesTask as often as possible.
     * @see "QuickTime::MoviesTask()"
     * @param maxMilliSecToUse Determines the maximum number of milliseconds that MoviesTask can work before returning.
     * @throws quicktime.QTException
     */
    public static void taskAll(int maxMilliSecToUse) throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            if (QTSession.isInitialized()) {
                MoviesTask(0, maxMilliSecToUse);
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
     * @see "QuickTime::CreateMovieFile()"
     * @param pathName specifies the complete pathName of the file that is to be created.
     * @param fCreator Specifies the creator of the file.
     * @param flags Specifies the behaviour when creating the file.
     * @return QTFile object that specifies the newly created movie file.
     * @throws quicktime.QTException
     */
    public static Movie createMovieFile(QTFile pathName, int fCreator, int flags) throws QTException {
        if ((flags & 0x80000000) != 0) {
            try {
                QTFile.checkSecurity(pathName.getCanonicalPath(), 1024);
            } catch (IOException e) {
                throw new QTIOException(e.getMessage());
            }
        }
        int[] newMovie = { 0 };
        if ((flags & 0x40000000) != 0) {
            flags ^= 0x40000000;
        }
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = CreateMovieFile(pathName.getFSSpec(false, 512), fCreator, (short) -1, flags | 0x20000000, null, newMovie);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        return new Movie(newMovie[0], null);
    }

    /**
     * Determines a reference to the movie that contains the data collected during a record operation.
     * You can use this movie identifier with Movie Toolbox functions.
     * @see "QuickTime::SGGetMovie"
     * @return The Movie or null if there is no movie.
     * @see quicktime.std.sg.SequenceGrabber#getMovie()
     */
    public static Movie fromSequenceGrabber(SequenceGrabber time) {
        int i = SGGetMovie(QTObject.ID(time));
        return new Movie(i, time);
    }

    /**
     * This method allows your application to create a movie from the contents of the scrap.
     * @see "QuickTime::NewMovieFromScrap()"
     * @param newMovieFlags Controls the operation of this method.
     * @throws quicktime.QTException
     */
    public static Movie fromScrap(int newMovieFlags) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromScrap(newMovieFlags);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        if (i == 0) {
            throw new StdQTException(63526);
        }
        Movie x = new Movie(i, null);
        x.init();
        return x;
    }

    /**
     * This method creates a movie in memory from a resource that is
     * stored in a movie file. Your application specifies the movie file with the
     * opened file represented by the OpenMovieFile object.
     * Once you have opened a movie file and loaded a movie, your application can
     * proceed to work with the movie.
     * @see "QuickTime::NewMovieFromFile()"
     * @param fileIn identifies the open movie file from which the movie is to be loaded.
     * @throws quicktime.QTException
     */
    public static Movie fromFile(OpenMovieFile fileIn) throws QTException {
        return fromFile(fileIn, 1, null);
    }

    /**
     * This method creates a movie in memory from a resource that is
     * stored in a movie file. Your application specifies the movie file with the
     * opened file represented by the OpenMovieFile object.
     * Once you have opened a movie file and loaded a movie, your application can
     * proceed to work with the movie.
     * @see "QuickTime::NewMovieFromFile()"
     * @param fileIn identifies the open movie file stream from which the movie is to be loaded.
     * @param flags controls the operation of this method.
     * @param info a MovieInfo object to receive state information about the movie.
     * @throws quicktime.QTException
     */
    public static Movie fromFile(OpenMovieFile fileIn, int flags, MovieInfo info) throws QTException {
        int[] movie = new int[1];
        short s = (short) QTObject.ID(fileIn);
        short[] resId = new short[1];
        byte[] resName = new byte[255];
        byte[] dataRefWasChanged = new byte[1];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromFile(movie, s, resId, resName, (short) flags, dataRefWasChanged);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(i);
        if (movie[0] == 0) {
            throw new StdQTException(63526);
        }
        if (info != null) {
            info.setMovieInfo(resId[0], dataRefWasChanged[0] == 1, new String(resName, 1, resName.length - 1));
        }
        Movie x = new Movie(movie[0], null);
        x.init();
        return x;
    }

    /**
     * This method creates a movie in memory from a movie resource or
     * a handle you obtained from the PutMovieIntoHandle function.
     * @see "QuickTime::NewMovieFromHandle()"
     * @param hand contains a handle to the movie resource from which the movie is to be loaded.
     * @throws quicktime.QTException
     */
    public static Movie fromHandle(QTHandle hand) throws QTException {
        return fromHandle(hand, 1, null);
    }

    /**
     * This method creates a movie in memory from a movie resource or
     * a handle you obtained from the PutMovieIntoHandle function.
     * @see "QuickTime::NewMovieFromHandle()"
     * @param hand contains a handle to the movie resource from which the movie is to be loaded.
     * @param flags controls the operation of this method.
     * @param info a MovieInfo object to receive state information about the movie.
     * @throws quicktime.QTException
     */
    public static Movie fromHandle(QTHandle hand, int flags, MovieInfo info) throws QTException {
        int[] movie = new int[1];
        int i = QTObject.ID(hand);
        byte[] dataRefWasChanged = new byte[1];
        int j = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            j = NewMovieFromHandle(movie, i, (short) flags, dataRefWasChanged);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(j);
        if (movie[0] == 0) {
            throw new StdQTException(63526);
        }
        if (info != null) {
            info.setMovieInfo(0, dataRefWasChanged[0] == 1, null);
        }
        Movie x = new Movie(movie[0], null);
        x.init();
        x.dataRef = hand;
        return x;
    }

    /**
     * This method enables you to retrieve a movie
     * that is stored anywhere in the data fork of a specified file.
     * @see "QuickTime::NewMovieFromDataFork()"
     * @param fileIn The open movie file stream.
     * @param fileOffset Specifies the starting file offset of the atom in the data
     *                     fork of the file specified by the fRefNum parameter.
     * @param flags controls the operation of this method.
     * @param info a MovieInfo object to receive state information about the movie.
     * @throws quicktime.QTException
     * @throws quicktime.qd.QDException
     */
    public static Movie fromDataFork(OpenFile fileIn, int fileOffset, int flags, MovieInfo info) throws QTException, QDException {
        int[] movie = { 0 };
        short s = (short) QTObject.ID(fileIn);
        byte[] dataRefWasChanged = new byte[1];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromDataFork(movie, s, fileOffset, (short) flags, dataRefWasChanged);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        if (movie[0] == 0) {
            throw new StdQTException(63526);
        }
        if (info != null) {
            info.setMovieInfo(0, dataRefWasChanged[0] == 1, "");
        }
        Movie x = new Movie(movie[0], null);
        x.init();
        return x;
    }

    /**
     * This method enables you to retrieve a movie from any data reference.
     * @see "QuickTime::NewMovieFromDataRef()"
     * @param defaultDataRef a DataRef object specifying the data reference this method will load the movie from.
     * @param flags Controls the operation of this method.
     *
     *  NOTE: it is a good idea to use the flag newMovieAsyncOK (defined in StdQTConstants4) to prevent the VM from
     *  blocking (in the case of no network connection, or a bad URL)
     * @throws quicktime.QTException
     */
    public static Movie fromDataRef(DataRef defaultDataRef, int flags) throws QTException {
        int[] movie = { 0 };
        short[] id = { 0 };
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieFromDataRef(movie, (short) flags, id, QTObject.ID(defaultDataRef), defaultDataRef == null ? 0 : defaultDataRef.getType());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        if (movie[0] == 0)
            throw new StdQTException(63524);
        StdQTException.checkError(i);
        Movie x = new Movie(movie[0], null);
        x.init();
        x.dataRef = defaultDataRef;
        return x;
    }

    Movie(int time, Object ref) {
        super(time, ref);
    }

    /**
     * This method creates a new movie in memory. The Movie Toolbox
     * initializes the data structures for the new movie, which contains no tracks.
     * It supplies default settings of the movie being inactive and will select enabled tracks from
     * track groups.
     * @see "QuickTime::NewMovie()"
     */
    public Movie() throws QTException {
        this(1);
    }

    /**
     * This method creates a new movie in memory. The Movie Toolbox
     * initializes the data structures for the new movie, which contains no tracks.
     * It supplies default settings of the movie being active and will select enabled tracks from
     * track groups.
     * @see "QuickTime::NewMovie()"
     * @param flags flags to control settings when creating the movie.
     */
    public Movie(int flags) throws QTException {
        super(allocate(flags));
        init();
    }

    private static int allocate(int time) throws StdQTException, QDException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovie(time);
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
        QDRect boxRect = getBounds();
        boxRect.setX(0);
        boxRect.setY(0);
        setBounds(boxRect);
    }

    void _setNR(int time) {
        setNR(time);
    }

    /**
     * This method adds a movie resource to a specified resource file.
     * @see "QuickTime::AddMovieResource()"
     * @param fileOut Identifies the open writable movie file stream to which the resource is to be added.
     * @param resID Contains a pointer to a field that contains the resource ID number for the new resource.
     *  If the field referred to by the resId parameter is set to 0, the Movie Toolbox assigns a
     *  unique resource ID number to the new resource.
     * @param resourceName a String object that contains the name of the movie resource.
     *  If you set the resourceName parameter to null, the toolbox creates an unnamed resource.
     * @throws quicktime.std.StdQTException
     */
    public int addResource(OpenMovieFile fileOut, int resID, String resourceName) throws StdQTException {
        short[] resId = { (short) resID };
        byte[] resName = resourceName != null ? QTUtils.String2PString(resourceName, 255) : null;
        int i = AddMovieResource(_ID(), (short) QTObject.ID(fileOut), resId, resName);
        StdQTException.checkError(i);
        return resId[0];
    }

    /**
     * This method replaces the contents of a movie resource in a specified movie file.
     * @see "QuickTime::UpdateMovieResource()"
     * @param fileOut Specifies the open writable movie file stream for the movie to be updated.
     * @param resID Specifies the resource number for the movie.
     * @param resourceName Points to a new name for the resource. If you do not want to change the resource's name,
     *  set this parameter to null.
     * @throws quicktime.std.StdQTException
     */
    public void updateResource(OpenMovieFile fileOut, int resID, String resourceName) throws StdQTException {
        byte[] resName = resourceName != null ? QTUtils.String2PString(resourceName, 255) : null;
        int i = UpdateMovieResource(_ID(), (short) QTObject.ID(fileOut), (short) resID, resName);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a movie resource from a specified movie file.
     * @see "QuickTime::RemoveMovieResource()"
     * @param fileOut Specifies the open writable movie file stream for the movie to be removed.
     * @param resID Specifies the resource number for the movie.
     * @throws quicktime.std.StdQTException
     */
    public void removeResource(OpenMovieFile fileOut, int resID) throws StdQTException {
        int i = RemoveMovieResource((short) QTObject.ID(fileOut), (short) resID);
        StdQTException.checkError(i);
    }

    /**
     * The moviesTask method services this movie.
     * You should call moviesTask as often as possible.
     * @see "QuickTime::MoviesTask()"
     * @param maxMilliSecToUse Determines the maximum number of milliseconds that MoviesTask can work before returning.
     * @throws quicktime.QTException
     */
    public void task(int maxMilliSecToUse) throws QTException {
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            MoviesTask(_ID(), maxMilliSecToUse);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
    }

    /**
     * This allows you to assign a drawing-complete method to a movie.
     * The Movie Toolbox calls this function based upon guidelines you establish when you assign the method to the movie.
     * @see "QuickTime::SetMovieDrawingCompleteProc()"
     * @param flags flags that control under what conditions the proc is called
     * @param proc the object that will be called when drawing is complete
     * @throws quicktime.std.StdQTException
     * @see quicktime.std.clocks.QTCallBack
     */
    public void setDrawingCompleteProc(int flags, MovieDrawingComplete proc) throws StdQTException {
        if (proc == null) {
            removeDrawingCompleteProc();
            return;
        }
        if (this.drawingUPP != null)
            removeDrawingCompleteProc();
        this.drawingUPP = new MoviesDispatcher(this, proc);
        SetMovieDrawingCompleteProc(_ID(), flags, this.drawingUPP.ID(), 0);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This allows you to remove a previously assigned drawing-complete method to a movie.
     * @see "QuickTime::SetMovieDrawingCompleteProc()"
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
     * @see "QuickTime::PrerollMovie()"
     * @param time Contains the starting time of the movie segment to play.
     * @param rate Specifies the rate at which you anticipate playing the movie.
     *  Positive values indicate forward rates and negative values indicate reverse rates.
     * @throws quicktime.std.StdQTException
     */
    public void preroll(int time, float rate) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                int j = PrerollMovie(_ID(), time, QTUtils.X2Fix(rate));
                StdQTException.checkError(j);
            }
        }
        int i = PrerollMovie(_ID(), time, QTUtils.X2Fix(rate));
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to prepare a portion of a movie for playback.
     * This call is used when using a remote movie to establish network connections.
     * @see "QuickTime::PrePrerollMovie()"
     * @param time Contains the starting time of the movie segment to play.
     * @param rate Specifies the rate at which you anticipate playing the movie.
     *  Positive values indicate forward rates and negative values indicate reverse rates.
     * @throws quicktime.std.StdQTException
     */
    public void prePreroll(int time, float rate) throws StdQTException {
        this.preUPP = null;
        prePreroll(time, rate, null);
    }

    /**
     * This method allows your application to prepare a portion of a movie for playback.
     * This call is used when using a remote movie to establish network connections.
     * @see "QuickTime::PrePrerollMovie()"
     * @param time Contains the starting time of the movie segment to play.
     * @param rate Specifies the rate at which you anticipate playing the movie.
     *  Positive values indicate forward rates and negative values indicate reverse rates.
     * @param proc the MoviePrePreroll object that will be invoked by this call. This can be null.
     * @throws quicktime.std.StdQTException
     */
    public void prePreroll(int time, float rate, MoviePrePreroll proc) throws StdQTException {
        if (QTSession.getQTMajorVersion() == 3) {
            if (proc != null)
                proc.execute(this, 0);
            return;
        }
        if (proc == null) {
            proc = (aMovie, aInt) -> {
            };
        }
        this.preUPP = new MoviesDispatcher(this, proc);
        synchronized (QTNative.globalsLock) {
            int i = PrePrerollMovie(_ID(), time, QTUtils.X2Fix(rate), this.preUPP.ID(), 0);
            StdQTException.checkError(i);
        }
    }

    /**
     * This method aborts.
     * This call is used when using a remote movie to establish network connections.
     * This version of the pre-preroll is Asynchronous and the Supplied MoviePrePreroll proc will
     * be called back when the movie is pre-preRolled
     * @see "QuickTime::AbortPrePrerollMovie()"
     */
    public void abortPrePreroll(int time) throws StdQTException {
        if (QTSession.getQTMajorVersion() == 3)
            return;
        AbortPrePrerollMovie(_ID(), (short) time);
        if (this.preUPP != null) {
            this.preUPP.cleanupMethodClosure();
            this.preUPP = null;
        }
    }

    /**
     * This method loads a movie's data into memory. If the movie does not fit, the method throws an error.
     * @see "QuickTime::LoadMovieIntoRam()"
     * @param time Allows you to specify a portion of the movie to load. The time parameter contains the starting time of the movie segment to load.
     * @param duration Allows you to specify a portion of the movie to load. The time parameter contains the starting time of the movie segment to load. The duration parameter specifies the length of the segment to load.
     * @param flags Gives you explicit control over what is loaded into memory and how long to keep it around.
     * @throws quicktime.std.StdQTException
     */
    public void loadIntoRam(int time, int duration, int flags) throws StdQTException {
        int i = LoadMovieIntoRam(_ID(), time, flags, duration);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to activate and deactivate a movie.
     * @see "QuickTime::SetMovieActive()"
     * @param active Activates or deactivates the movie. Set this parameter to true to activate the movie; set this parameter to false to deactivate the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setActive(boolean active) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                byte b2 = (byte) (active ? 1 : 0);
                SetMovieActive(_ID(), b2);
                StdQTException.checkError(GetMoviesError());
            }
        }
        byte b1 = (byte) (active ? 1 : 0);
        SetMovieActive(_ID(), b1);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a boolean value. The method returns true if the movie is active and false if the movie is not active.
     * @see "QuickTime::GetMovieActive()"
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
     * @see "QuickTime::StartMovie()"
     */
    public void start() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StartMovie(_ID());
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method stops the playback of a movie.
     * @see "QuickTime::StopMovie()"
     */
    public void stop() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StopMovie(_ID());
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method repositions a movie to play from its start.
     * @see "QuickTime::GoToBeginningOfMovie()"
     */
    public void goToBeginning() throws StdQTException {
        GoToBeginningOfMovie(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method repositions a movie to play from its end.
     * @see "QuickTime::GoToEndOfMovie()"
     */
    public void goToEnd() throws StdQTException {
        GoToEndOfMovie(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to determine if a particular movie has completely finished playing.
     * @see "QuickTime::IsMovieDone()"
     */
    public boolean isDone() throws StdQTException {
        int i = IsMovieDone(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to determine whether a movie is in preview mode.
     * If a movie is in preview mode, only the movie's preview can be displayed.
     * @see "QuickTime::GetMoviePreviewMode()"
     */
    public boolean getPreviewMode() throws StdQTException {
        int i = GetMoviePreviewMode(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to place a movie into and out of preview mode.
     * When a movie is in preview mode, only those tracks identified as preview tracks are serviced.
     * @see "QuickTime::SetMoviePreviewMode()"
     */
    public void setPreviewMode(boolean previewMode) throws StdQTException {
        byte b = (byte) (previewMode ? 1 : 0);
        SetMoviePreviewMode(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * You can use showPoster method to display a movie's poster. The movie poster
     * uses the movie's matrix and display clipping characteristics.
     * @see "QuickTime::ShowMoviePoster()"
     */
    public void showPoster() throws StdQTException {
        ShowMoviePoster(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method plays a movie's preview.
     * @see "QuickTime::PlayMoviePreview()"
     */
    public void playPreview() throws StdQTException {
        PlayMoviePreview(_ID(), 0, 0);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * Your application can determine a movie's graphics world by calling the getGWorld method.
     * @see "QuickTime::GetMovieGWorld()"
     * @return The current destination graphics port
     * @throws quicktime.std.StdQTException
     */
    public QDGraphics getGWorld() throws StdQTException {
        return QDGraphics.fromMovie(this);
    }

    /**
     * The setGWorld method allows your application to establish a movie's display
     * coordinate system by setting the graphics world for displaying a movie.
     * @see "QuickTime::SetMovieGWorld()"
     * @param port the new graphics port or null to use current port
     * @param gdh the graphics device or null to use the current device
     * @throws quicktime.std.StdQTException
     */
    public void setGWorld(QDGraphics port, GDevice gdh) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieGWorld(_ID(), QTObject.ID(port), QTObject.ID(gdh));
            StdQTException.checkError(GetMoviesError());
        }
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::GetMovieNaturalBoundsRect()"
     */
    public QDRect getNaturalBoundsRect() throws StdQTException {
        QDRect naturalBoundsRect = new QDRect();
        GetMovieNaturalBoundsRect(_ID(), naturalBoundsRect.getRect());
        StdQTException.checkError(GetMoviesError());
        return naturalBoundsRect;
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::GetNextTrackForCompositing()"
     */
    public Track getNextTrackForCompositing(Track flags) throws QTException {
        int i = GetNextTrackForCompositing(_ID(), QTObject.ID(flags));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this);
    }

    /**
     * No QT Documentation.
     * @see "QuickTime::GetPrevTrackForCompositing()"
     */
    public Track getPrevTrackForCompositing(Track flags) throws QTException {
        int i = GetPrevTrackForCompositing(_ID(), QTObject.ID(flags));
        StdQTException.checkError(GetMoviesError());
        return new Track(i, this);
    }

    /**
     * This method creates a picture from the specified movie at the specified time.
     * This function uses only those movie tracks that are currently enabled and would therefore
     * be used in playback. Your application may call this function even if the movie is inactive.
     * @see "QuickTime::GetMoviePict()"
     * @param time contains the time from which the image is taken.
     * @return a Pict
     * @throws quicktime.QTException
     */
    public Pict getPict(int time) throws QTException {
        return Pict.fromMovie(this, time);
    }

    /**
     * This method creates a picture that contains a movie's poster.
     * returns a PicHandle object for the image.
     * @see "QuickTime::GetMoviePosterPict()"
     * @return a Pict
     * @throws quicktime.QTException
     */
    public Pict getPosterPict() throws QTException {
        return Pict.fromMovie(this);
    }

    /**
     * This method allows your application to ensure that the Movie Toolbox properly
     * displays your movie after it has been uncovered.
     * @see "QuickTime::UpdateMovie()"
     */
    public void update() throws StdQTException {
        int i = UpdateMovie(_ID());
        StdQTException.checkError(i);
    }

    /**
     * This method invalidates part of the movie specified by the invalidRgn. The invalidated
     * part will be drawn next time MoviesTask is called.
     * @see "QuickTime::InvalidateMovieRegion()"
     * @param invalidRgn a Region object to specify the invalid part of the movie.
     * @throws quicktime.std.StdQTException
     */
    public void invalidateRegion(Region invalidRgn) throws StdQTException {
        int i = InvalidateMovieRegion(_ID(), QTObject.ID(invalidRgn));
        StdQTException.checkError(i);
    }

    /**
     * This method returns a movie's boundary rectangle, which is a rectangle that encompasses all
     * of the movie's enabled tracks. The movie box is in the coordinate system of the movie's
     * graphics world and defines the movie's boundaries over the entire duration of the movie.
     * The movie's boundary rectangle defines the size and shape of the movie before the
     * Movie Toolbox applies the display clipping region.
     * @see "QuickTime::GetMovieBox()"
     * @return The movie's boundary rectangle
     * @throws quicktime.std.StdQTException
     */
    public QDRect getBounds() throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                QDRect bounds = new QDRect();
                GetMovieBox(_ID(), bounds.getRect());
                StdQTException.checkError(GetMoviesError());
                return bounds;
            }
        }
        QDRect x = new QDRect();
        GetMovieBox(_ID(), x.getRect());
        StdQTException.checkError(GetMoviesError());
        return x;
    }

    /**
     * This method returns a movie's boundary rectangle, which is a rectangle that encompasses all
     * of the movie's enabled tracks. The movie box is in the coordinate system of the movie's
     * graphics world and defines the movie's boundaries over the entire duration of the movie.
     * The movie's boundary rectangle defines the size and shape of the movie before the
     * Movie Toolbox applies the display clipping region.
     * @see "QuickTime::GetMovieBox()"
     * @return The movie's boundary rectangle
     * @throws quicktime.std.StdQTException
     */
    public QDRect getBox() throws StdQTException {
        return getBounds();
    }

    /**
     * This method sets a movie's boundary rectangle, or movie box,
     * which is a rectangle that encompasses the spatial representation of all of
     * the movie's enabled tracks. The movie box is in the display coordinate system.
     * @see "QuickTime::SetMovieBox()"
     * @param boxRect the coordinates of the new boundary rectangle.
     * @throws quicktime.std.StdQTException
     */
    public void setBounds(QDRect boxRect) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                SetMovieBox(_ID(), boxRect.getRect());
                StdQTException.checkError(GetMoviesError());
            }
        }
        SetMovieBox(_ID(), boxRect.getRect());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets a movie's boundary rectangle, or movie box,
     * which is a rectangle that encompasses the spatial representation of all of
     * the movie's enabled tracks. The movie box is in the display coordinate system.
     * @see "QuickTime::SetMovieBox()"
     * @param boxRect the coordinates of the new boundary rectangle.
     * @throws quicktime.std.StdQTException
     */
    public void setBox(QDRect boxRect) throws StdQTException {
        setBounds(boxRect);
    }

    /**
     * This method allows your application to determine a movie's current display clipping region.
     * @see "QuickTime::GetMovieDisplayClipRgn()"
     * @return a Region object.
     * @throws quicktime.QTException
     */
    public Region getDisplayClipRgn() throws QTException {
        return Region.fromMovieDisplayClip(this);
    }

    /**
     * This method allows your application to establish a movie's current display clipping region.
     * @see "QuickTime::SetMovieDisplayClipRgn()"
     * @param theClip a Region object.
     * @throws quicktime.std.StdQTException
     */
    public void setDisplayClipRgn(Region theClip) throws StdQTException {
        SetMovieDisplayClipRgn(_ID(), QTObject.ID(theClip));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a movie's clipping region.
     * @see "QuickTime::GetMovieClipRgn()"
     * @return a Region object.
     * @throws quicktime.QTException
     */
    public Region getClipRgn() throws QTException {
        return Region.fromMovieClip(this);
    }

    /**
     * This method allows your application to establish a movie's clipping region.
     * @see "QuickTime::SetMovieClipRgn()"
     * @param theClip a Region object specifying the new clip region.
     * @throws quicktime.std.StdQTException
     */
    public void setClipRgn(Region theClip) throws StdQTException {
        SetMovieClipRgn(_ID(), QTObject.ID(theClip));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a movie's display boundary region.
     * The display boundary region encloses all of a movie's enabled tracks
     * after the track matrix, track clip, movie matrix, and movie clip have
     * been applied to all of the movie's tracks. This region is in the display
     * coordinate system of the movie's graphics world.
     * @see "QuickTime::GetMovieDisplayBoundsRgn()"
     * @return a Region object specifying the display boundary region.
     * @throws quicktime.QTException
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
     * @see "QuickTime::GetMovieBoundsRgn()"
     * @return a Region object specifying the movie boundary region.
     * @throws quicktime.QTException
     */
    public Region getBoundsRgn() throws QTException {
        return Region.fromMovieBounds(this);
    }

    /**
     * This method creates a new movie resource for you. You can use this handle to store a QuickTime movie
     * in a specialized storage format.
     * @see "QuickTime::PutMovieIntoHandle()"
     * @param intoThisHandle Specifies the handle where the movie will be placed into.
     * @throws quicktime.std.StdQTException
     */
    public void putIntoHandle(QTHandle intoThisHandle) throws StdQTException {
        int i = PutMovieIntoHandle(_ID(), QTObject.ID(intoThisHandle));
        StdQTException.checkError(i);
    }

    /**
     * This method allows you to store a movie in the data fork of a given file.
     * @see "QuickTime::PutMovieIntoDataFork()"
     * @param fileOut Specifies the open writable movie file stream where the movie will be placed into.
     * @param offset Indicates where the movie should be written.
     * @param maxSize Indicates the largest number of bytes that may be written.
     * @throws quicktime.std.StdQTException
     */
    public void putIntoDataFork(OpenMovieFile fileOut, int offset, int maxSize) throws StdQTException {
        short s = (short) QTObject.ID(fileOut);
        int i = PutMovieIntoDataFork(_ID(), s, offset, maxSize);
        StdQTException.checkError(i);
    }

    /**
     * This method returns a long integer that contains the movie's creation date and time information.
     * @see "QuickTime::GetMovieCreationTime()"
     * @return the creation time.
     * @throws quicktime.std.StdQTException
     */
    public int getCreationTime() throws StdQTException {
        int i = GetMovieCreationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a long integer that contains the movie's creation date and time information.
     * @see "QuickTime::GetMovieModificationTime()"
     * @return the modification time.
     * @throws quicktime.std.StdQTException
     */
    public int getModificationTime() throws StdQTException {
        int i = GetMovieModificationTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns the time scale of a movie.
     * @see "QuickTime::GetMovieTimeScale()"
     * @return the time scale.
     * @throws quicktime.std.StdQTException
     */
    public int getTimeScale() throws StdQTException {
        int i = GetMovieTimeScale(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method establishes a movie's time scale.
     * @see "QuickTime::SetMovieTimeScale()"
     * @param timeScale Specifies the movie's new time scale.
     * @throws quicktime.std.StdQTException
     */
    public void setTimeScale(int timeScale) throws StdQTException {
        SetMovieTimeScale(_ID(), timeScale);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the duration of a movie.
     * @see "QuickTime::GetMovieDuration()"
     * return the duration.
     */
    public int getDuration() throws StdQTException {
        int i = GetMovieDuration(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a movie's playback rate.
     * @see "QuickTime::GetMovieRate()"
     * @return the playback rate.
     * @throws quicktime.std.StdQTException
     */
    public float getRate() throws StdQTException {
        int i = GetMovieRate(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    /**
     * This method sets a movie's playback rate.
     * @see "QuickTime::SetMovieRate()"
     * @param rate Specifies the rate.
     * @throws quicktime.std.StdQTException
     */
    public void setRate(float rate) throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            synchronized (QTNative.globalsLock) {
                SetMovieRate(_ID(), QTUtils.X2Fix(rate));
                StdQTException.checkError(GetMoviesError());
            }
        }
        SetMovieRate(_ID(), QTUtils.X2Fix(rate));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a movie's default playback rate.
     * @see "QuickTime::GetMoviePreferredRate()"
     * @return the preferred rate.
     * @throws quicktime.std.StdQTException
     */
    public float getPreferredRate() throws StdQTException {
        int i = GetMoviePreferredRate(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.Fix2X(i);
    }

    /**
     * This method allows your application to specify a movie's default playback rate.
     * @see "QuickTime::SetMoviePreferredRate()"
     * @param rate Specifies the rate.
     * @throws quicktime.std.StdQTException
     */
    public void setPreferredRate(float rate) throws StdQTException {
        SetMoviePreferredRate(_ID(), QTUtils.X2Fix(rate));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns a movie's preferred volume setting.
     * @see "QuickTime::GetMoviePreferredVolume()"
     * @return the preferred volume.
     * @throws quicktime.std.StdQTException
     */
    public float getPreferredVolume() throws StdQTException {
        short s = GetMoviePreferredVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    /**
     * This method allows your application to set a movie's preferred volume setting.
     * @see "QuickTime::SetMoviePreferredVolume()"
     * @param volume Specifies the preferred volume setting of the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setPreferredVolume(float volume) throws StdQTException {
        SetMoviePreferredVolume(_ID(), QTUtils.X2ShortFix(volume));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns movie's current volume setting.
     * @see "QuickTime::GetMovieVolume()"
     * @return the volume.
     * @throws quicktime.std.StdQTException
     */
    public float getVolume() throws StdQTException {
        short s = GetMovieVolume(_ID());
        StdQTException.checkError(GetMoviesError());
        return QTUtils.ShortFix2X(s);
    }

    /**
     * This method allows your application to set a movie's current volume.
     * @see "QuickTime::SetMovieVolume()"
     * @param volume Specifies the current volume setting of the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setVolume(float volume) throws StdQTException {
        SetMovieVolume(_ID(), QTUtils.X2ShortFix(volume));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows allows your application to retrieve a movie's
     * transformation matrix.
     * @see "QuickTime::GetMovieMatrix()"
     * @return a Matrix object.
     * @throws quicktime.std.StdQTException
     */
    public Matrix getMatrix() throws StdQTException {
        Matrix matrix = new Matrix();
        GetMovieMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(GetMoviesError());
        return matrix;
    }

    /**
     * This method allows your application to set a movie's transformation matrix.
     * The Movie Toolbox uses a movie's matrix to map a movie from its display coordinate system to
     * its graphics world.
     * @see "QuickTime::SetMovieMatrix()"
     * @param matrix the matrix to set
     * @throws quicktime.std.StdQTException
     */
    public void setMatrix(Matrix matrix) throws StdQTException {
        SetMovieMatrix(_ID(), matrix.getBytes());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the starting time and duration of the movie's preview.
     * @see "QuickTime::GetMoviePreviewTime()"
     * @return a TimeInfo object.
     * @throws quicktime.std.StdQTException
     */
    public TimeInfo getPreviewTime() throws StdQTException {
        int[] time = new int[1];
        int[] duration = new int[1];
        GetMoviePreviewTime(_ID(), time, duration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(time[0], duration[0]);
    }

    /**
     * This method allows your application to define the starting time and duration of the movie's preview.
     * @see "QuickTime::SetMoviePreviewTime()"
     * @param previewTimeInfo a TimeInfo object.
     * @throws quicktime.std.StdQTException
     */
    public void setPreviewTime(TimeInfo previewTimeInfo) throws StdQTException {
        SetMoviePreviewTime(_ID(), previewTimeInfo.time, previewTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to define the starting time and duration of the movie's preview.
     * @see "QuickTime::SetMoviePreviewTime()"
     */
    public void setPreviewTime(int fCreator, int flags) throws StdQTException {
        SetMoviePreviewTime(_ID(), fCreator, flags);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns the poster's time in the movie.
     * @see "QuickTime::GetMoviePosterTime()"
     * @return the poster's time.
     * @throws quicktime.std.StdQTException
     */
    public int getPosterTime() throws StdQTException {
        int i = GetMoviePosterTime(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method sets the poster time for the movie.
     * @see "QuickTime::SetMoviePosterTime()"
     * @param posterTime the poster time.
     * @throws quicktime.std.StdQTException
     */
    public void setPosterTime(int posterTime) throws StdQTException {
        SetMoviePosterTime(_ID(), posterTime);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method returns information about a movie's current selection.
     * @see "QuickTime::GetMovieSelection()"
     * @return a TimeInfo object specifying the movie's selection.
     * @throws quicktime.std.StdQTException
     */
    public TimeInfo getSelection() throws StdQTException {
        int[] time = new int[1];
        int[] duration = new int[1];
        GetMovieSelection(_ID(), time, duration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(time[0], duration[0]);
    }

    /**
     * This method sets a movie's current selection.
     * @see "QuickTime::SetMovieSelection()"
     * @param selectionTimeInfo a TimeInfo object specifying the time information for movie's new selection.
     * @throws quicktime.std.StdQTException
     */
    public void setSelection(TimeInfo selectionTimeInfo) throws StdQTException {
        SetMovieSelection(_ID(), selectionTimeInfo.time, selectionTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets a movie's current selection.
     * @see "QuickTime::SetMovieSelection()"
     * @param selectionTime Contains a time value specifying the starting point of the current selection.
     * @param selectionDuration Contains a time value that specifies the duration of the current selection.
     * @throws quicktime.std.StdQTException
     */
    public void setSelection(int selectionTime, int selectionDuration) throws StdQTException {
        SetMovieSelection(_ID(), selectionTime, selectionDuration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to define a movie's active segment.
     * @see "QuickTime::SetMovieActiveSegment()"
     * @param activeSegmentTimeInfo a TimeInfo object specifying the time information of the active segment.
     * @throws quicktime.std.StdQTException
     */
    public void setActiveSegment(TimeInfo activeSegmentTimeInfo) throws StdQTException {
        SetMovieActiveSegment(_ID(), activeSegmentTimeInfo.time, activeSegmentTimeInfo.duration);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine what portion of a movie is currently active for playing.
     * @see "QuickTime::GetMovieActiveSegment()"
     * @return a TimeInfo object specifying the active segment of the movie.
     * @throws quicktime.std.StdQTException
     */
    public TimeInfo getActiveSegment() throws StdQTException {
        int[] start = new int[1];
        int[] duration = new int[1];
        GetMovieActiveSegment(_ID(), start, duration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(start[0], duration[0]);
    }

    /**
     * This method returns a movie's current time.
     * @see "QuickTime::GetMovieTime()"
     * @return the movie's current time.
     * @throws quicktime.std.StdQTException
     */
    public int getTime() throws StdQTException {
        int i = GetMovieTime(_ID(), 0);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method returns a movie's current time as a TimeRecord object.
     * @see "QuickTime::GetMovieTime()"
     * @return the movie's current time as a TimeRecord object.
     * @throws quicktime.QTException
     */
    public TimeRecord getTRTime() throws QTException {
        TimeRecord flags = new TimeRecord();
        int i = GetMovieTime(_ID(), QTObject.ID(flags));
        StdQTException.checkError(GetMoviesError());
        return flags;
    }

    /**
     * This method allows you to set a movie's current time.
     * @see "QuickTime::SetMovieTime()"
     * @param newtime A TimeRecord object specifying the new time for the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setTime(TimeRecord newtime) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieTime(_ID(), QTObject.ID(newtime));
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to set a movie's time value.
     * @see "QuickTime::SetMovieTimeValue()"
     */
    public void setTimeValue(int time) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            SetMovieTimeValue(_ID(), time);
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to obtain access to a movie's user data list.
     * @see "QuickTime::GetMovieUserData()"
     * @return a reference to the movie's user data list.
     * @throws quicktime.std.StdQTException
     */
    public UserData getUserData() throws StdQTException {
        return UserData.fromMovie(this);
    }

    /**
     * This method returns the number of tracks in a movie.
     * @see "QuickTime::GetMovieTrackCount()"
     * @return the number of tracks in a movie.
     * @throws quicktime.std.StdQTException
     */
    public int getTrackCount() throws StdQTException {
        int i = GetMovieTrackCount(_ID());
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine the track identifier of a track given the track's ID value.
     * @see "QuickTime::GetMovieTrack()"
     * @param trackID ID value of the track (from Track.getID())
     * @return a Track object for the trackID.
     * @throws quicktime.QTException
     */
    public Track getTrack(int trackID) throws QTException {
        int i = GetMovieTrack(_ID(), trackID);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * This method allows your application to determine the track identifier of a track given the track's index value.
     * @see "QuickTime::GetMovieIndTrack()"
     * @param index index value of the track in the movie (Moive.getTrackCount())
     * @return a Track object for the specified index.
     * @throws quicktime.QTException
     */
    public Track getIndTrack(int index) throws QTException {
        int i = GetMovieIndTrack(_ID(), index);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * This method allows your application to determine the track identifier of a track given a track type and an index value.
     * @see "QuickTime::GetMovieIndTrackType()"
     * @param index Specifies the index value of the track for this operation. This is not that same as the track's index value in the movie. Rather, this parameter is an index into the set of tracks that meet your other selection criteria.
     * @param trackType Contains either a media type or a media characteristic value.
     * @param flags Contains flags that control the search operation.
     * @return a Track object for the specified index with in the tracks with the specified track type.
     * @throws quicktime.QTException
     */
    public Track getIndTrackType(int index, int trackType, int flags) throws QTException {
        int i = GetMovieIndTrackType(_ID(), index, trackType, flags);
        StdQTException.checkError(GetMoviesError());
        return i == 0 ? null : new Track(i, this);
    }

    /**
     * Removes a track from a movie.
     * @see "QuickTime::DisposeMovieTrack()"
     * @param track the track to remove from this movie.
     * @throws quicktime.std.StdQTException
     */
    public void removeTrack(Track track) throws StdQTException {
        if (QTObject.ID(track) == 0) {
            throw new StdQTException(63527);
        }
        int i = GetTrackMovie(QTObject.ID(track));
        StdQTException.checkError(GetMoviesError());
        if (i != _ID()) {
            throw new StdQTException(63526);
        }
        DisposeMovieTrack(QTObject.ID(track));
        StdQTException.checkError(GetMoviesError());
        track.owner = null;
        track._setNR(0);
    }

    /**
     * This method allows your application to enable and disable automatic track selection by the Movie Toolbox.
     * @see "QuickTime::SetAutoTrackAlternatesEnabled()"
     * @param enable Controls automatic track selection.
     * @throws quicktime.std.StdQTException
     */
    public void setAutoTrackAlternatesEnabled(boolean enable) throws StdQTException {
        byte b = (byte) (enable ? 1 : 0);
        SetAutoTrackAlternatesEnabled(_ID(), b);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to instruct the Movie Toolbox to select appropriate tracks immediately.
     * @see "QuickTime::SelectMovieAlternates()"
     */
    public void selectAlternates() throws StdQTException {
        SelectMovieAlternates(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method searches for times of interest in a movie.
     * @see "QuickTime::GetMovieNextInterestingTime()"
     * @param interestingTimeFlags Specifies the search criteria.
     * @param mediaTypes Specifies an array of media types
     * @param time Specifies a time value that establishes the starting point for the search.
     * @param rate Contains the search direction.
     * @return a TimeInfo object specifying the interesting time.
     * @throws quicktime.std.StdQTException
     */
    public TimeInfo getNextInterestingTime(int interestingTimeFlags, int[] mediaTypes, int time, float rate) throws StdQTException {
        int[] interestingTime = new int[1];
        int[] interestingDuration = new int[1];
        short s = 0;
        if (mediaTypes != null) {
            s = (short) mediaTypes.length;
        }
        GetMovieNextInterestingTime(_ID(), (short) interestingTimeFlags, s, mediaTypes, time, QTUtils.X2Fix(rate), interestingTime, interestingDuration);
        StdQTException.checkError(GetMoviesError());
        return new TimeInfo(interestingTime[0], interestingDuration[0]);
    }

    /**
     * This method adds an empty segment to a movie.
     * @see "QuickTime::InsertEmptyMovieSegment()"
     */
    public void insertEmptySegment(int fCreator, int flags) throws StdQTException {
        int i = InsertEmptyMovieSegment(_ID(), fCreator, flags);
        StdQTException.checkError(i);
    }

    /**
     * The insertSegment method copies part of one movie to another.
     * @see "QuickTime::InsertMovieSegment()"
     * @param dstMovie Specifies the destination movie for this operation.
     *  Specifies the start of the segment in the source movie.
     *  Specifies the duration of the segment in the source movie.
     *  Contains a time value specifying where the segment is to be inserted.
     * @throws quicktime.std.StdQTException
     */
    public void insertSegment(Movie dstMovie, int fCreator, int flags, int duration) throws StdQTException {
        int i = InsertMovieSegment(_ID(), QTObject.ID(dstMovie), fCreator, flags, duration);
        StdQTException.checkError(i);
    }

    /**
     * This method removes a specified segment from a movie.
     * @see "QuickTime::DeleteMovieSegment()"
     * @param startTime Contains a time value specifying the starting point of the segment to be deleted.
     * @param duration Contains a time value that specifies the duration of the segment to be deleted.
     * @throws quicktime.std.StdQTException
     */
    public void deleteSegment(int startTime, int duration) throws StdQTException {
        int i = DeleteMovieSegment(_ID(), startTime, duration);
        StdQTException.checkError(i);
    }

    /**
     * This method changes the duration of a segment of a movie and then removes the current selection from the original movie.
     * @see "QuickTime::ScaleMovieSegment()"
     * @param startTime Specifies the start of the segment.
     * @param oldDuration Specifies the duration of the segment in the source movie.
     * @param newDuration Specifies the new duration of the segment.
     * @throws quicktime.std.StdQTException
     */
    public void scaleSegment(int startTime, int oldDuration, int newDuration) throws StdQTException {
        int i = ScaleMovieSegment(_ID(), startTime, oldDuration, newDuration);
        StdQTException.checkError(i);
    }

    /**
     * This method creates a new movie that contains the original movie's current selection.
     * @see "QuickTime::CutMovieSelection()"
     * @return a new Movie object that contains the original movie's current selection.
     * @throws quicktime.QTException
     */
    public Movie cutSelection() throws QTException {
        int i = CutMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    /**
     * This method creates a new movie that contains the original movie's current selection.
     * @see "QuickTime::CopyMovieSelection()"
     * @return a new Movie object that contains the original movie's current selection.
     * @throws quicktime.QTException
     */
    public Movie copySelection() throws QTException {
        int i = CopyMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, null);
    }

    /**
     * This method places the tracks from one movie into another movie.
     * @see "QuickTime::PasteMovieSelection()"
     * @param srcMovie Specifies the source movie for this operation.
     * @throws quicktime.std.StdQTException
     */
    public void pasteSelection(Movie srcMovie) throws StdQTException {
        PasteMovieSelection(_ID(), QTObject.ID(srcMovie));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method adds one or more tracks to a movie.
     * @see "QuickTime::AddMovieSelection()"
     * @param srcMovie Specifies the source movie for this operation.
     * @throws quicktime.std.StdQTException
     */
    public void addSelection(Movie srcMovie) throws StdQTException {
        AddMovieSelection(_ID(), QTObject.ID(srcMovie));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method removes the segment of the movie that is defined by the current selection.
     * @see "QuickTime::ClearMovieSelection()"
     */
    public void clearSelection() throws StdQTException {
        ClearMovieSelection(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method takes the contents of a specified handle, together with its type, and pastes it into a specified movie.
     * @see "QuickTime::PasteHandleIntoMovie()"
     * @param handle Specifies the handle to be pasted into the movie indicated by the handleType parameter.
     * @param handleType Indicates the data type of the handle specified in the h parameter.
     * @param flags Specifies a constant that further refines conditions of the paste operation.
     * @param userComp Specifies the component or an instance of the component that is to perform the conversion of the data into a QuickTime movie.
     * @throws quicktime.std.StdQTException
     */
    public void pasteHandle(QTHandle handle, int handleType, int flags, MovieImporter userComp) throws StdQTException {
        int i = PasteHandleIntoMovie(QTObject.ID(handle), handleType, _ID(), flags, QTObject.ID(userComp));
        StdQTException.checkError(i);
    }

    /**
     * This method takes a movie (or a single track from within that movie) and converts it into a handle of a specified type.
     * @see "QuickTime::PutMovieIntoTypedHandle()"
     * @param targetTrack Specifies the track to convert.
     * @param handleType Indicates the type of the new data.
     * @param start Specifies the start time of the segment of the movie or track to be converted.
     * @param dur Specifies the duration of the segment of the movie or track to be converted.
     * @param flags Indicates condition of the conversion. Set this parameter to 0.
     * @param userComp Indicates a component or component instance of the movie export component you
     *  want to perform the conversion. Otherwise, set this parameter to 0 for the Movie Toolbox to
     *  choose the appropriate component.
     * @return a QTHandle object.
     * @throws quicktime.QTException
     */
    public QTHandle putIntoTypedHandle(Track targetTrack, int handleType, int start, int dur, int flags, MovieExporter userComp) throws QTException {
        int i = 0;
        if (targetTrack != null) {
            i = QTObject.ID(targetTrack);
        }
        QTHandle time4 = new QTHandle(0, true);
        int j = PutMovieIntoTypedHandle(_ID(), i, handleType, QTObject.ID(time4), flags, start, dur, QTObject.ID(userComp));
        StdQTException.checkError(j);
        return time4;
    }

    /**
     * The copySettings method copies many settings from one movie to another, overwriting the destination settings in the process.
     * @see "QuickTime::CopyMovieSettings()"
     * @param dstMovie Specifies the destination movie for this operation.
     * @throws quicktime.std.StdQTException
     */
    public void copySettings(Movie dstMovie) throws StdQTException {
        int i = CopyMovieSettings(_ID(), QTObject.ID(dstMovie));
        StdQTException.checkError(i);
    }

    /**
     * This method adds an empty track to the movie. The type of the track and its data reference is specified
     * by the parameters.
     * @see "QuickTime::AddEmptyTrackToMovie()"
     * @param srcTrack Specifies the Track object that will be used to determine the type and properties of the empty track.
     * @param dataRef Specifies the default data reference for the empty track.
     * @return a Track object.
     * @throws quicktime.QTException
     */
    public Track addEmptyTrack(Track srcTrack, DataRef dataRef) throws QTException {
        int[] dstTrack = new int[1];
        int i = AddEmptyTrackToMovie(QTObject.ID(srcTrack), _ID(), QTObject.ID(dataRef), dataRef.getType(), dstTrack);
        StdQTException.checkError(i);
        return new Track(dstTrack[0], this);
    }

    /**
     * Create a track in a movie.
     * @see "QuickTime::NewMovieTrack()"
     * @param width Specifies the width of the track, in pixels.
     * @param height Specifies the height of the track, in pixels.
     * @param trackVolume Specifies the volume setting of the track.
     * @throws quicktime.QTException
     */
    public Track newTrack(float width, float height, float trackVolume) throws QTException {
        return addTrack(width, height, trackVolume);
    }

    /**
     * Create a track in a movie.
     * @see "QuickTime::NewMovieTrack()"
     * @param width Specifies the width of the track, in pixels.
     * @param height Specifies the height of the track, in pixels.
     * @param trackVolume Specifies the volume setting of the track.
     * @throws quicktime.QTException
     */
    public Track addTrack(float width, float height, float trackVolume) throws QTException {
        return new Track(this, width, height, trackVolume);
    }

    /**
     * You can create an edit state by calling the NewMovieEditState function.
     * @see "QuickTime::NewMovieEditState()"
     * @return a movie's current edit state
     * @throws quicktime.QTException
     */
    public MovieEditState newEditState() throws QTException {
        int i = NewMovieEditState(_ID());
        StdQTException.checkError(GetMoviesError());
        return new MovieEditState(this, i);
    }

    /**
     * Your application can use this method to return a movie to its condition according
     * to an edit state you created previously.
     * @see "QuickTime::UseMovieEditState()"
     * @param anEditState Specifies the edit state for this operation.
     * @throws quicktime.std.StdQTException
     */
    public void useEditState(MovieEditState anEditState) throws StdQTException {
        int i = UseMovieEditState(_ID(), QTObject.ID(anEditState));
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to determine the size, in bytes, of the sample data in a segment of a movie.
     * @see "QuickTime::GetMovieDataSize()"
     * @param startTime Contains a time value specifying the starting point of the segment.
     * @param duration Contains a time value that specifies the duration of the segment.
     * @return the data size.
     * @throws quicktime.std.StdQTException
     */
    public int getDataSize(int startTime, int duration) throws StdQTException {
        int i = GetMovieDataSize(_ID(), startTime, duration);
        StdQTException.checkError(GetMoviesError());
        return i;
    }

    /**
     * This method allows your application to determine whether a specified point lies in the region
     * defined by a movie's final display boundary region after it has been clipped by the movie's
     * display clipping region.
     * @see "QuickTime::PtInMovie()"
     * @param pt Specifies the point to be checked.
     * @return whether the point is in the movie or not.
     * @throws quicktime.std.StdQTException
     */
    public boolean pointInMovie(QDPoint pt) throws StdQTException {
        int i = PtInMovie(_ID(), pt.getPoint());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method allows your application to specify a movie's language.
     * @see "QuickTime::SetMovieLanguage()"
     * @param language Specifies the movie's language or region code.
     * @throws quicktime.std.StdQTException
     */
    public void setLanguage(int language) throws StdQTException {
        SetMovieLanguage(_ID(), language);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine whether a movie has changed and needs to be saved.
     * @see "QuickTime::HasMovieChanged()"
     * @return whether the movie has changed or not.
     * @throws quicktime.std.StdQTException
     */
    public boolean hasChanged() throws StdQTException {
        int i = HasMovieChanged(_ID());
        StdQTException.checkError(GetMoviesError());
        return i == 1;
    }

    /**
     * This method sets the movie changed flag to indicate that the movie has not been changed.
     * @see "QuickTime::ClearMovieChanged()"
     */
    public void clearChanged() throws StdQTException {
        ClearMovieChanged(_ID());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method sets the default data reference for the movie.
     * @see "QuickTime::SetMovieDefaultDataRef()"
     * @param newRef Specifies the default data reference object.
     * @throws quicktime.std.StdQTException
     */
    public void setDefaultDataRef(DataRef newRef) throws StdQTException {
        int i = SetMovieDefaultDataRef(_ID(), QTObject.ID(newRef), newRef.getType());
        StdQTException.checkError(i);
        this.dataRef = newRef;
    }

    /**
     * This method allows your application to get the default data reference for the movie.
     * @see "QuickTime::GetMovieDefaultDataRef()"
     * @return a DataRef object specifiying the default data reference for the movie.
     * @throws quicktime.QTException
     */
    public DataRef getDefaultDataRef() throws QTException {
        return DataRef.fromMovie(this);
    }

    /**
     * This method allows your application to assign a color table to the movie.
     * @see "QuickTime::SetMovieColorTable()"
     * @param ctab Specifies the new color table.
     * @throws quicktime.std.StdQTException
     */
    public void setColorTable(ColorTable ctab) throws StdQTException {
        int i = SetMovieColorTable(_ID(), QTObject.ID(ctab));
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to get the color table of the movie.
     * @see "QuickTime::GetMovieColorTable()"
     * @return a ColorTable object specifying the current color table.
     * @throws quicktime.QTException
     */
    public ColorTable getColorTable() throws QTException {
        return ColorTable.fromMovie(this);
    }

    /**
     * This method creates a new movie file containing a specified movie.
     * @see "QuickTime::FlattenMovie()"
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param fileOut Contains writable movie file for the movie to be created.
     * @param creator Specifies the creator value for the new file.
     * @param scriptTag Specifies the script in which the movie file should be created.
     * @param createQTFileFlags Controls file creation options.
     * @param resName A String with the name of the movie resource. If you set the resName parameter to null,
     *  the toolbox creates an unnamed resource.
     * @return the resource ID number of the new resource
     * @throws quicktime.QTException
     */
    public int flatten(int movieFlattenFlags, QTFile fileOut, int creator, int duration, int createQTFileFlags, int scriptTag, String resName) throws QTException {
        short[] resId = new short[1];
        resId[0] = ((short) movieFlattenFlags);
        byte[] name = null;
        if (resName != null) {
            name = QTUtils.String2PString(resName, 255);
        }
        FlattenMovie(_ID(), movieFlattenFlags, fileOut.getFSSpec(false, 512), creator, (short) scriptTag, createQTFileFlags, resId, name);
        StdQTException.checkError(GetMoviesError());
        return resId[0];
    }

    /**
     * This method creates a new movie file and creates a new movie that contains all of its movie data.
     * @see "QuickTime::FlattenMovieData()"
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param fileOut Contains writable movie file for the movie to be created.
     * @param creator Specifies the creator value for the new file.
     * @param scriptTag Specifies the script in which the movie file should be created.
     * @param createQTFileFlags Controls file creation options.
     * @return a Movie object for the new movie.
     * @throws quicktime.QTException
     */
    public Movie flattenData(int movieFlattenFlags, QTFile fileOut, int creator, int scriptTag, int createQTFileFlags) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            int[] port = { 0 };
            int[] gdh = { 0 };
            GetMovieGWorld(_ID(), port, gdh);
            SetGWorld(port[0], gdh[0]);
            i = FlattenMovieData(_ID(), creator, fileOut.getFSSpec(false, 512), createQTFileFlags, (short) scriptTag, movieFlattenFlags);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        return i != 0 ? new Movie(i, null) : null;
    }

    /**
     * This method creates a new movie that contains all of its movie data loaded into memory.
     * The handle must be the size of the movie's data (see getDataSize() method).
     * @see "QuickTime::FlattenMovieData()"
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param handle the handle that is used to contain the new movie's data for the new movie that is returned.
     * @return a Movie object for the new movie.
     * @throws quicktime.QTException
     */
    public Movie flattenData(int movieFlattenFlags, QTHandle handle) throws QTException {
        byte[] fsspec = new byte[8];
        setIntInArray(fsspec, 0, 1752065132);
        setIntInArray(fsspec, 4, QTObject.ID(handle));
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            int[] port = { 0 };
            int[] gdh = { 0 };
            GetMovieGWorld(_ID(), port, gdh);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = FlattenMovieData(_ID(), movieFlattenFlags | 0x10, fsspec, 0, (short) -1, 0);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(GetMoviesError());
        Movie x = null;
        if (i != 0) {
            x = new Movie(i, null);
            x.dataRef = handle;
            x.setActive(true);
        }
        return x;
    }

    /**
     * This method allows you to attach a progress function to a movie. Currently it
     * assigns the default progress proc to the movie.
     * @see "QuickTime::SetMovieProgressProc()"
     */
    public void setProgressProc() throws StdQTException {
        int i = 0;
        if (QTSession.isCurrentOS(4)) {
            i = (Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                final int res1 = 0;

                @Override
                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        Movie.SetMovieProgressProc(_ID(), -1, 0);
                    }
                    return this.res1;
                }
            });
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
     * @see "QuickTime::SetMovieProgressProc()"
     * @param mp the new MovieProgress object
     * @throws quicktime.std.StdQTException
     */
    public void setProgressProc(MovieProgress mp) throws StdQTException {
        int i = 0;
        if (mp == null) {
            setProgressProc();
            return;
        }
        if (this.progUPP != null)
            this.progUPP.cleanupMethodClosure();
        this.progUPP = new MoviesDispatcher(this, mp);
        synchronized (QTNative.globalsLock) {
            SetMovieProgressProc(_ID(), this.progUPP.ID(), 0);
        }
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to intercept executing wired actions of this movie.
     * @see "QuickTime::AddMovieExecuteWiredActionsProc()"
     * @param wa the new ExecutingWiredActions object
     * @throws quicktime.std.StdQTException
     */
    public void addExecuteWiredActionsProc(ExecutingWiredAction wa) throws StdQTException {
        if (wa == null) {
            throw new StdQTException(-50);
        }
        if (this.wiredUPP != null) {
            RemoveMovieExecuteWiredActionsProc(_ID(), this.wiredUPP.ID(), 0);
            this.wiredUPP.cleanupMethodClosure();
        }
        this.wiredUPP = new MoviesDispatcher(this, wa);
        StdQTException.checkError(AddMovieExecuteWiredActionsProc(_ID(), this.wiredUPP.ID(), 0));
    }

    /**
     * This method allows you to remove the existing executing wired actions of this movie.
     * @see "QuickTime::RemoveMovieExecuteWiredActionsProc()"
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
     * @see "QuickTime::MovieExecuteWiredActions()"
     * @param flags execution flags
     * @throws quicktime.std.StdQTException
     */
    public void executeWiredActions(int flags, AtomContainer duration) throws StdQTException {
        StdQTException.checkError(MovieExecuteWiredActions(_ID(), flags, QTObject.ID(duration)));
    }

    /**
     * This method allows you to search for text in a movie.
     * @see "QuickTime::MovieSearchText()"
     * @param text Specifies the text to be searched.
     * @param searchFlags controls the operation of this method.
     * @param searchTrack Specifes the track which you want to limit the search to that track.
     * @param searchTime Specifes the starting time for the search
     * @param searchOffset Specifes the offset in media samples. Search will begin from that offset.
     * @return a SearchResult object.
     * @throws quicktime.QTException
     */
    public SearchResult searchText(QTPointer text, int searchFlags, Track searchTrack, int searchTime, int searchOffset) throws QTException {
        int[] arrayOfInt1 = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        arrayOfInt1[0] = QTObject.ID(searchTrack);
        arrayOfInt2[0] = searchFlags;
        arrayOfInt3[0] = searchTime;
        int i = MovieSearchText(_ID(), QTObject.ID(text), text.getSize(), searchOffset, arrayOfInt1, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new SearchResult(new Track(arrayOfInt1[0], this), arrayOfInt2[0], arrayOfInt3[0]);
    }

    /**
     * This method allows you to search for text in a movie.
     * @see "QuickTime::MovieSearchText()"
     * @param text Specifies th etext to be searched.
     * @param searchFlags controls the operation of this method.
     * @param searchTime Specifes the starting time for the search
     * @param searchOffset Specifes the offset in media samples. Search will begin from that offset.
     * @return a SearchResult object.
     * @throws quicktime.QTException
     */
    public SearchResult searchText(QTPointer text, int searchFlags, int searchTime, int searchOffset) throws QTException {
        int[] searchTrack = new int[1];
        int[] arrayOfInt2 = new int[1];
        int[] arrayOfInt3 = new int[1];
        searchTrack[0] = 0;
        arrayOfInt2[0] = searchFlags;
        arrayOfInt3[0] = searchTime;
        int i = MovieSearchText(_ID(), QTObject.ID(text), text.getSize(), searchOffset, searchTrack, arrayOfInt2, arrayOfInt3);
        StdQTException.checkError(i);
        return new SearchResult(new Track(searchTrack[0], this), arrayOfInt2[0], arrayOfInt3[0]);
    }

    /**
     * This method allows you to obtain a poster's boundary rectangle.
     * @see "QuickTime::GetPosterBox()"
     * @return a Rectangle object specifying the poster box for the movie.
     * @throws quicktime.std.StdQTException
     */
    public QDRect getPosterBox() throws StdQTException {
        QDRect posterBox = new QDRect();
        GetPosterBox(_ID(), posterBox.getRect());
        StdQTException.checkError(GetMoviesError());
        return posterBox;
    }

    /**
     * This method allows you to set a poster's boundary rectangle.
     * @see "QuickTime::SetPosterBox()"
     * @param boxRect a Rectangle object specifying the poster box for the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setPosterBox(QDRect boxRect) throws StdQTException {
        SetPosterBox(_ID(), boxRect.getRect());
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows your application to determine a movie's display boundary region during a specified segment.
     * @see "QuickTime::GetMovieSegmentDisplayBoundsRgn()"
     * @param time Specifies the starting time of the movie segment to consider.
     * @param duration Specifies the length of the segment to consider.
     * @return a Region object.
     * @throws quicktime.QTException
     */
    public Region getSegmentDisplayBoundsRgn(int time, int duration) throws QTException {
        return Region.fromMovieSegment(this, time, duration);
    }

    /**
     * This method searches for errors in all the enabled tracks of the movie.
     * @see "QuickTime::GetMovieStatus()"
     * @return a StatusInfo object specifying the first found problem track. Nothing is returned
     *  if there is no problem track.
     * @throws quicktime.QTException
     */
    public StatusInfo getStatus() throws QTException {
        int[] firstProblemTrack = new int[1];
        int i = GetMovieStatus(_ID(), firstProblemTrack);
        if (i != 0) {
            return new StatusInfo(i, new Track(firstProblemTrack[0], this));
        }
        return null;
    }

    /**
     * This method displays a dialog giving information about the movie.
     * @see "QuickTime::ShowMovieInformation()"
     */
    public void showInformation() throws StdQTException {
        if (QTSession.isCurrentOS(4)) {
            CarbonAccess.invokeCarbonEventClient(() -> {
                synchronized (QTNative.globalsLock) {
                    Movie.ShowMovieInformation(_ID(), 0, 0);
                }
                return null;
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
     * @see "QuickTime::PutMovieOnScrap()"
     * @param movieScrapFlags Flags that control the operation.
     * @throws quicktime.std.StdQTException
     */
    public void putOnScrap(int movieScrapFlags) throws StdQTException {
        int i = PutMovieOnScrap(_ID(), movieScrapFlags);
        StdQTException.checkError(i);
    }

    /**
     * This method allows your application to provide information to the Movie Toolbox that can influence movie playback.
     * @see "QuickTime::SetMoviePlayHints()"
     * @param flags Specifies the optimizations that can be used with this movie.
     * @param flagsMask Indicates which flags in the flags parameter are to be considered in this operation.
     * @throws quicktime.std.StdQTException
     */
    public void setPlayHints(int flags, int flagsMask) throws StdQTException {
        SetMoviePlayHints(_ID(), flagsMask, flags);
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to get movie's TimeBase.
     * @see "QuickTime::GetMovieTimeBase()"
     * @return a TimeBase object for movie's timebase
     * @throws quicktime.std.StdQTException
     */
    public TimeBase getTimeBase() throws StdQTException {
        return TimeBase.fromMovie(this);
    }

    /**
     * This method allows you to assign a master time base to a movie.
     * @see "QuickTime::SetMovieMasterTimeBase()"
     * @param tb Specifies the master time base to be assigned to this movie.
     * @param slaveZero Specifies the time, in the time scale of the master time base, that corresponds to a 0 time value for the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setMasterTimeBase(TimeBase tb, TimeRecord slaveZero) throws StdQTException {
        SetMovieMasterTimeBase(_ID(), QTObject.ID(tb), QTObject.ID(slaveZero));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method allows you to assign a clock component to a movie.
     * @see "QuickTime::SetMovieMasterClock()"
     * @param slaveZero Contains the time, in the clock's time scale, that corresponds to a 0 time value for the movie.
     * @throws quicktime.std.StdQTException
     */
    public void setMasterClock(Clock slaveZero, TimeRecord duration) throws StdQTException {
        SetMovieMasterClock(_ID(), QTObject.ID(slaveZero), QTObject.ID(duration));
        StdQTException.checkError(GetMoviesError());
    }

    /**
     * This method converts this movie into a specified file and type.
     * @see "QuickTime::ConvertMovieToFile()"
     * @param onlyTrack Specifies the track within the source movie for this conversion operation. To specify all tracks, set the value of this parameter to null.
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @return the resource id of the movie created.
     * @throws quicktime.QTException
     */
    public int convertToFile(Track onlyTrack, QTFile outputFile, int fileType, int creator, int scriptTag) throws QTException {
        return convertToFile(onlyTrack, outputFile, fileType, creator, scriptTag, 0, null);
    }

    /**
     * This method converts this movie into a specified file and type.
     * @see "QuickTime::ConvertMovieToFile()"
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @return the resource id of the movie created.
     * @throws quicktime.QTException
     */
    public int convertToFile(QTFile outputFile, int fileType, int creator, int scriptTag) throws QTException {
        return convertToFile(null, outputFile, fileType, creator, scriptTag, 0, null);
    }

    /**
     * This method converts this movie into a specified file and type.
     * @see "QuickTime::ConvertMovieToFile()"
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @param flags the appropriate flags for this operation
     * @return the resource id of the movie created.
     * @throws quicktime.QTException
     */
    public int convertToFile(QTFile outputFile, int fileType, int creator, int scriptTag, int flags) throws QTException {
        return convertToFile(null, outputFile, fileType, creator, scriptTag, flags, null);
    }

    /**
     * This method converts this movie into a specified file and type.
     * @see "QuickTime::ConvertMovieToFile()"
     * @param onlyTrack Specifies the track within the source movie for this conversion operation. To specify all tracks, set the value of this parameter to null.
     * @param outputFile Specifies the destination file.
     * @param fileType Specifies the data type of the destination file for the movie.
     * @param creator Specifies the creator value for the output file if it is a new one.
     * @param scriptTag Specifies the script into which the movie should be converted if the output file is a new one.
     * @param flags the appropriate flags for this operation
     * @param userComp A ComponentIdentifier of type 'spit' that will be opened and used by this call to export the Movie
     * @return the resource id of the movie created.
     * @throws quicktime.QTException
     * @throws quicktime.std.StdQTException
     */
    public int convertToFile(Track onlyTrack, QTFile outputFile, int fileType, int creator, int scriptTag, int flags, MovieExporter userComp) throws QTException, StdQTException {
        short[] resID_native = new short[1];
        byte[] fsspec = outputFile.getFSSpec(false, 512);
        int i1;
        if (QTSession.isCurrentOS(4)) {
            i1 = (Integer) CarbonAccess.invokeCarbonEventClient(new CarbonEventClient() {

                int res1;

                @Override
                public Object invoke() {
                    synchronized (QTNative.globalsLock) {
                        this.res1 = Movie.ConvertMovieToFile(_ID(), QTObject.ID(onlyTrack), fsspec, fileType, creator, (short) scriptTag, resID_native, flags, QTObject.ID(userComp));
                    }
                    return this.res1;
                }
            });
            StdQTException.checkError(i1);
            return resID_native[0];
        }
        synchronized (QTNative.globalsLock) {
            i1 = ConvertMovieToFile(_ID(), QTObject.ID(onlyTrack), outputFile.getFSSpec(false, 512), fileType, creator, (short) scriptTag, resID_native, flags, QTObject.ID(userComp));
        }
        StdQTException.checkError(i1);
        return resID_native[0];
    }

    /**
     * The QTVRGetQTVRTrack function returns a track for the QTVR track
     * that has the index specified by the index parameter in the QuickTime movie specified by the
     * theMovie parameter. If there is no such track, QTVRGetQTVRTrack returns the value null.
     * @see "QuickTime::QTVRGetQTVRTrack()"
     * @param index this value indicates the nth VR Track to return. QT3.0 only supports one VR track so this
     *  value should be set to 1.
     * @throws quicktime.QTException
     */
    public Track getQTVRTrack(int index) throws QTException {
        int i = QTVRGetQTVRTrack(_ID(), index);
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
        } catch (StdQTException e) {
        }
        return super.toString() + "[Error getting movie information.]";
    }

    /**
     * Determine whether a movie is being progressively downloaded..
     * @see "QuickTime::QTMovieNeedsTimeTable()"
     * @return a boolean if movie is being progressively downloaded
     * @throws quicktime.std.StdQTException
     */
    public boolean needsTimeTable() throws StdQTException {
        byte[] needsTimeTable = new byte[1];
        int i = QTMovieNeedsTimeTable(_ID(), needsTimeTable);
        StdQTException.checkError(i);
        return needsTimeTable[0] != 0;
    }

    /**
     * Returns the duration of the part of a movie that has already been downloaded.
     * @see "QuickTime::GetMaxLoadedTimeInMovie()"
     */
    public int maxLoadedTimeInMovie() throws StdQTException {
        int[] time = new int[1];
        int i = GetMaxLoadedTimeInMovie(_ID(), time);
        StdQTException.checkError(i);
        return time[0];
    }

    /**
     * Copies the user data in the movie to the destination movie
     * @see "QuickTime::CopyMovieUserData()"
     * @param destMovie destination movie for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyUserDataToMovie(Movie destMovie, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(_ID(), QTObject.ID(destMovie), replace == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified movie into this movie
     * @see "QuickTime::CopyMovieUserData()"
     * @param srcMovie source movie for user data
     * @param replace true if the user data should be replaced; false if you want the data merged
     * @throws quicktime.std.StdQTException
     */
    public void copyUserDataFromMovie(Movie srcMovie, boolean replace) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(QTObject.ID(srcMovie), _ID(), replace == true ? 1919970403 : 1835361639));
    }

    /**
     * Copies the user data from the specified movie into the destination movie
     * @see "QuickTime::CopyMovieUserData()"
     * @param srcMovie source movie for user data
     * @param destMovie destination movie for user data
     * @param copyRule constant for merge or replace
     *     use StdQTConstants5_1.kQTCopyUserDataReplace or StdQTConstants5_1.kQTCopyUserDataMerge
     * @throws quicktime.std.StdQTException
     */
    public static void copyUserData(Movie srcMovie, Movie destMovie, int copyRule) throws StdQTException {
        StdQTException.checkError(CopyMovieUserData(QTObject.ID(srcMovie), QTObject.ID(destMovie), copyRule));
    }

    /**
     * Looks for the first media handler that has a custom clock and uses that as the Movie's
     * master timebase clock. This is useful to tie the movies master timebase to a sound clock
     * if there is a sound track.
     *
     * @see "QuickTime::ChooseMovieClock()"
     * @param flags the movie flags
     */
    public void chooseClock(int flags) {
        ChooseMovieClock(_ID(), flags);
    }

    /**
     * QuickTime can handle public movie handles with self contained data references.
     * (Requires QT 6)
     * @see "QuickTime::PutMovieForDataRefIntoHandle()"
     * @param ref the dataref
     * @param handle the handle
     * @throws quicktime.std.StdQTException
     */
    public void putDataRefIntoHandle(DataRef ref, QTHandle handle) throws StdQTException {
        StdQTException.checkError(PutMovieForDataRefIntoHandle(_ID(), QTObject.ID(ref), ref.getType(), QTObject.ID(handle)));
    }

    /**
     * QuickTime can handle public movie handles with self contained data references.
     * (Requires QT 6)
     * @see "QuickTime::NewMovieForDataRefFromHandle()"
     * @param dataRef the dataref of movie to be created
     * @param publicHandle the handle containing the movie
     * @param flags the movie creation flags
     * @param info movie info (this can be null)
     * @return movie
     * @throws quicktime.std.StdQTException
     */
    public Movie forDataRefFromHandle(DataRef dataRef, QTHandle publicHandle, int flags, MovieInfo info) throws StdQTException {
        int[] movie = new int[1];
        byte[] dataRefWasChanged = new byte[1];
        int i = 0;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                GetGWorld(savedPort, savedDevice);
            SetGWorld(QTObject.ID(QDGraphics.scratch), 0);
            i = NewMovieForDataRefFromHandle(movie, QTObject.ID(publicHandle), (short) flags, dataRefWasChanged, QTObject.ID(dataRef), dataRef.getType());
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                SetGWorld(savedPort[0], savedDevice[0]);
            }
        }
        StdQTException.checkError(i);
        if (movie[0] == 0) {
            throw new StdQTException(63526);
        }
        if (info != null) {
            info.setMovieInfo(0, dataRefWasChanged[0] == 1, null);
        }
        Movie x = new Movie(movie[0], null);
        x.init();
        x.dataRef = dataRef;
        return x;
    }

    /**
     * Allows the user to ask the QuickTime idle manager when idling routines such as movies.task()
     * need to be called. (Requires QT 6)
     * @see "QuickTime::QTGetTimeUntilNextTask()"
     * @param scale the time scale to be used for the return value
     * @return the amount of time that needs to elapse in the given time scale before
     *  QuickTime's idling mechanism needs to be called
     * @throws quicktime.std.StdQTException
     */
    public static int getTimeUntilNextTask(int scale) throws StdQTException {
        int[] duration = { 0 };
        StdQTException.checkError(QTGetTimeUntilNextTask(duration, scale));
        return duration[0];
    }

    /**
     * Allows the user to specify one or more callback routines that will be called when QuickTime
     * needs tasking. (Requires QT 6)
     * @see "QuickTime::QTInstallNextTaskNeededSoonerCallback()"
     */
    public static void setNextTaskNeededSoonerCallback(NextTaskNeededSooner tns, int fCreator, int flags) throws StdQTException {
        if (tns == null) {
            return;
        }
        IdleDispatcher idleDispatcher = new IdleDispatcher(tns);
        Object localObject = dispatchers.put(tns, idleDispatcher);
        StdQTException.checkError(QTInstallNextTaskNeededSoonerCallback(idleDispatcher.ID(), fCreator, flags, 0));
    }

    /**
     * Allows the user to remove a callback from the IdleManager that no longer needs servicing.
     * (Requires QT 6)
     * @see "QuickTime::QTUninstallNextTaskNeededSoonerCallback()"
     */
    public static void removeNextTaskNeededSoonerCallback(NextTaskNeededSooner nextTaskNeededSooner) throws StdQTException {
        if (nextTaskNeededSooner == null) {
            return;
        }
        IdleDispatcher idleDispatcher = (IdleDispatcher) dispatchers.get(nextTaskNeededSooner);
        idleDispatcher.cleanupMethodClosure();
        StdQTException.checkError(QTUninstallNextTaskNeededSoonerCallback(idleDispatcher.ID(), 0));
    }

    private static final Hashtable<Object, Object> dispatchers = new Hashtable<>();

    /**
     * This method creates a new movie file containing a specified movie.
     * (Requires QT 6)
     * @see "QuickTime::FlattenMovieDataToDataRef()"
     * @param dataRef Contains dataRef for the movie to be created.
     * @param movieFlattenFlags Controls the process of adding movie data to the new movie file.
     * @param creator Specifies the creator value for the new file.
     * @param scriptTag Specifies the script in which the movie file should be created.
     * @param createMovieFileFlags Controls movie creation options.
     * @return the new movie
     * @throws quicktime.QTException
     */
    public Movie flattenDataToDataRef(DataRef dataRef, int movieFlattenFlags, int creator, int scriptTag, int createMovieFileFlags) throws QTException {
        int i = 0;
        Movie movie = null;
        synchronized (QTNative.globalsLock) {
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4))) {
                GetGWorld(savedPort, savedDevice);
            }
            int[] port = { 0 };
            int[] gdh = { 0 };
            GetMovieGWorld(_ID(), port, gdh);
            SetGWorld(port[0], gdh[0]);
            i = FlattenMovieDataToDataRef(_ID(), creator, QTObject.ID(dataRef), dataRef.getType(), movieFlattenFlags, (short) scriptTag, createMovieFileFlags);
            if ((QTSession.isCurrentOS(1)) || (QTSession.isCurrentOS(4)))
                SetGWorld(savedPort[0], savedDevice[0]);
        }
        StdQTException.checkError(GetMoviesError());
        if (i != 0) {
            movie = new Movie(i, null);
            movie.init();
            movie.dataRef = dataRef;
        }
        return movie;
    }

    /**
     * To resolve relative data references, a method was needed to determine the original location from which a movie was opened or imported.
     * This routine sets the movie's anchor data reference. Pass null to reset the anchor data reference to nothing.
     * The routine copies the data reference.
     * @param dataRef the dataref to the movie
     * @throws quicktime.std.StdQTException
     */
    public void setMovieAnchorDataRef(DataRef dataRef) throws StdQTException {
        int i = 0;
        i = SetMovieAnchorDataRef(_ID(), QTObject.ID(dataRef), dataRef.getType());
        StdQTException.checkError(i);
    }

    private boolean isMovieProgressProc() {
        int[] proc = { 0 };
        int[] refCon = { 0 };
        GetMovieProgressProc(_ID(), proc, refCon);
        return proc[0] != 0;
    }

    /**
     * If there is no anchor data reference associated with the movie the method returns true.
     * @return the anchor flag (If there is no anchor data reference associated with the movie then kMovieAnchorDataRefIsDefault is returned)
     * @throws quicktime.std.StdQTException
     */
    public boolean isAnchorDefaultDataRef() throws StdQTException {
        int i = 0;
        int[] dataRef = { 0 };
        int[] dataRefType = { 0 };
        int[] outFlags = { 0 };
        i = GetMovieAnchorDataRef(_ID(), dataRef, dataRefType, outFlags);
        StdQTException.checkError(i);
        return outFlags[0] == 1;
    }

    private static void MoviesTask(int fCreator, int flags) {
        MovieToolbox.MoviesTask(fCreator, flags);
    }

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static short CreateMovieFile(byte[] fsspec, int fCreator, short scriptTag, int flags, short[] resRefNum, int[] newMovie) {
        return MovieToolbox.CreateMovieFile(fsspec, fCreator, scriptTag, flags, resRefNum, newMovie);
    }

    private static int SGGetMovie(int time) {
        return SequenceGrabberManager.SGGetMovie(time);
    }

    private static int NewMovieFromScrap(int time) {
        return MovieToolbox.NewMovieFromScrap(time);
    }

    private static short NewMovieFromFile(int[] movie, short fCreator, short[] resId, byte[] resName, short resID, byte[] dataRefWasChanged) {
        return MovieToolbox.NewMovieFromFile(movie, fCreator, resId, resName, resID, dataRefWasChanged);
    }

    private static short NewMovieFromHandle(int[] movie, int time, short flags, byte[] dataRefWasChanged) {
        return MovieToolbox.NewMovieFromHandle(movie, time, flags, dataRefWasChanged);
    }

    private static short NewMovieFromDataFork(int[] movie, short fCreator, int time, short resID, byte[] dataRefWasChanged) {
        return MovieToolbox.NewMovieFromDataFork(movie, fCreator, time, resID, dataRefWasChanged);
    }

    private static short NewMovieFromDataRef(int[] movie, short flags, short[] id, int dataRef, int dataRefType) {
        return MovieToolbox.NewMovieFromDataRef(movie, flags, id, dataRef, dataRefType);
    }

    private static int NewMovie(int time) {
        return MovieToolbox.NewMovie(time);
    }

    private static short AddMovieResource(int time, short resRefNum, short[] resId, byte[] resName) {
        return MovieToolbox.AddMovieResource(time, resRefNum, resId, resName);
    }

    private static short UpdateMovieResource(int time, short fCreator, short resID, byte[] resName) {
        return MovieToolbox.UpdateMovieResource(time, fCreator, resID, resName);
    }

    private static short RemoveMovieResource(short fCreator, short resID) {
        return MovieToolbox.RemoveMovieResource(fCreator, resID);
    }

    private static short PrerollMovie(int fCreator, int flags, int duration) {
        return MovieToolbox.PrerollMovie(fCreator, flags, duration);
    }

    private static short LoadMovieIntoRam(int fCreator, int flags, int duration, int time4) {
        return MovieToolbox.LoadMovieIntoRam(fCreator, flags, duration, time4);
    }

    private static void SetMovieActive(int time, byte active) {
        MovieToolbox.SetMovieActive(time, active);
    }

    private static byte GetMovieActive(int time) {
        return MovieToolbox.GetMovieActive(time);
    }

    private static void StartMovie(int time) {
        MovieToolbox.StartMovie(time);
    }

    private static void StopMovie(int time) {
        MovieToolbox.StopMovie(time);
    }

    private static void GoToBeginningOfMovie(int time) {
        MovieToolbox.GoToBeginningOfMovie(time);
    }

    private static void GoToEndOfMovie(int time) {
        MovieToolbox.GoToEndOfMovie(time);
    }

    private static byte IsMovieDone(int time) {
        return MovieToolbox.IsMovieDone(time);
    }

    private static byte GetMoviePreviewMode(int time) {
        return MovieToolbox.GetMoviePreviewMode(time);
    }

    private static void SetMoviePreviewMode(int time, byte usePreview) {
        MovieToolbox.SetMoviePreviewMode(time, usePreview);
    }

    private static void ShowMoviePoster(int time) {
        MovieToolbox.ShowMoviePoster(time);
    }

    private static void PlayMoviePreview(int fCreator, int flags, int duration) {
        MovieToolbox.PlayMoviePreview(fCreator, flags, duration);
    }

    private static void SetMovieGWorld(int fCreator, int flags, int duration) {
        MovieToolbox.SetMovieGWorld(fCreator, flags, duration);
    }

    private static void GetMovieNaturalBoundsRect(int time, byte[] rect) {
        MovieToolbox.GetMovieNaturalBoundsRect(time, rect);
    }

    private static int GetNextTrackForCompositing(int fCreator, int flags) {
        return MovieToolbox.GetNextTrackForCompositing(fCreator, flags);
    }

    private static int GetPrevTrackForCompositing(int fCreator, int flags) {
        return MovieToolbox.GetPrevTrackForCompositing(fCreator, flags);
    }

    private static short UpdateMovie(int time) {
        return MovieToolbox.UpdateMovie(time);
    }

    private static short InvalidateMovieRegion(int fCreator, int flags) {
        return MovieToolbox.InvalidateMovieRegion(fCreator, flags);
    }

    private static void GetMovieBox(int time, byte[] rect) {
        MovieToolbox.GetMovieBox(time, rect);
    }

    private static void SetMovieBox(int time, byte[] rect) {
        MovieToolbox.SetMovieBox(time, rect);
    }

    private static void SetMovieDisplayClipRgn(int fCreator, int flags) {
        MovieToolbox.SetMovieDisplayClipRgn(fCreator, flags);
    }

    private static void SetMovieClipRgn(int fCreator, int flags) {
        MovieToolbox.SetMovieClipRgn(fCreator, flags);
    }

    private static short PutMovieIntoHandle(int fCreator, int flags) {
        return MovieToolbox.PutMovieIntoHandle(fCreator, flags);
    }

    private static short PutMovieIntoDataFork(int fCreator, short fileRefNum, int flags, int duration) {
        return MovieToolbox.PutMovieIntoDataFork(fCreator, fileRefNum, flags, duration);
    }

    private static int GetMovieCreationTime(int time) {
        return MovieToolbox.GetMovieCreationTime(time);
    }

    private static int GetMovieModificationTime(int time) {
        return MovieToolbox.GetMovieModificationTime(time);
    }

    private static int GetMovieTimeScale(int time) {
        return MovieToolbox.GetMovieTimeScale(time);
    }

    private static void SetMovieTimeScale(int fCreator, int flags) {
        MovieToolbox.SetMovieTimeScale(fCreator, flags);
    }

    private static int GetMovieDuration(int time) {
        return MovieToolbox.GetMovieDuration(time);
    }

    private static int GetMovieRate(int time) {
        return MovieToolbox.GetMovieRate(time);
    }

    private static void SetMovieRate(int fCreator, int flags) {
        MovieToolbox.SetMovieRate(fCreator, flags);
    }

    private static int GetMoviePreferredRate(int time) {
        return MovieToolbox.GetMoviePreferredRate(time);
    }

    private static void SetMoviePreferredRate(int fCreator, int flags) {
        MovieToolbox.SetMoviePreferredRate(fCreator, flags);
    }

    private static short GetMoviePreferredVolume(int time) {
        return MovieToolbox.GetMoviePreferredVolume(time);
    }

    private static void SetMoviePreferredVolume(int time, short volume) {
        MovieToolbox.SetMoviePreferredVolume(time, volume);
    }

    private static short GetMovieVolume(int time) {
        return MovieToolbox.GetMovieVolume(time);
    }

    private static void SetMovieVolume(int time, short volume) {
        MovieToolbox.SetMovieVolume(time, volume);
    }

    private static void GetMovieMatrix(int time, byte[] matrix) {
        MovieToolbox.GetMovieMatrix(time, matrix);
    }

    private static void SetMovieMatrix(int time, byte[] matrix) {
        MovieToolbox.SetMovieMatrix(time, matrix);
    }

    private static void GetMoviePreviewTime(int movie, int[] time, int[] duration) {
        MovieToolbox.GetMoviePreviewTime(movie, time, duration);
    }

    private static void SetMoviePreviewTime(int fCreator, int flags, int duration) {
        MovieToolbox.SetMoviePreviewTime(fCreator, flags, duration);
    }

    private static int GetMoviePosterTime(int time) {
        return MovieToolbox.GetMoviePosterTime(time);
    }

    private static void SetMoviePosterTime(int fCreator, int flags) {
        MovieToolbox.SetMoviePosterTime(fCreator, flags);
    }

    private static void GetMovieSelection(int movie, int[] time, int[] duration) {
        MovieToolbox.GetMovieSelection(movie, time, duration);
    }

    private static void SetMovieSelection(int fCreator, int flags, int duration) {
        MovieToolbox.SetMovieSelection(fCreator, flags, duration);
    }

    private static void SetMovieActiveSegment(int fCreator, int flags, int duration) {
        MovieToolbox.SetMovieActiveSegment(fCreator, flags, duration);
    }

    private static void GetMovieActiveSegment(int time, int[] start, int[] duration) {
        MovieToolbox.GetMovieActiveSegment(time, start, duration);
    }

    private static int GetMovieTime(int fCreator, int flags) {
        return MovieToolbox.GetMovieTime(fCreator, flags);
    }

    private static void SetMovieTime(int fCreator, int flags) {
        MovieToolbox.SetMovieTime(fCreator, flags);
    }

    private static void SetMovieTimeValue(int fCreator, int flags) {
        MovieToolbox.SetMovieTimeValue(fCreator, flags);
    }

    private static int GetMovieTrackCount(int time) {
        return MovieToolbox.GetMovieTrackCount(time);
    }

    private static int GetMovieTrack(int fCreator, int flags) {
        return MovieToolbox.GetMovieTrack(fCreator, flags);
    }

    private static int GetMovieIndTrack(int fCreator, int flags) {
        return MovieToolbox.GetMovieIndTrack(fCreator, flags);
    }

    private static int GetMovieIndTrackType(int fCreator, int flags, int duration, int time4) {
        return MovieToolbox.GetMovieIndTrackType(fCreator, flags, duration, time4);
    }

    private static int GetTrackMovie(int time) {
        return MovieToolbox.GetTrackMovie(time);
    }

    private static void DisposeMovieTrack(int time) {
        MovieToolbox.DisposeMovieTrack(time);
    }

    private static void SetAutoTrackAlternatesEnabled(int time, byte enabled) {
        MovieToolbox.SetAutoTrackAlternatesEnabled(time, enabled);
    }

    private static void SelectMovieAlternates(int time) {
        MovieToolbox.SelectMovieAlternates(time);
    }

    private static void GetMovieNextInterestingTime(int interestingTimeFlags, short flags, short resID, int[] whichMediaTypes, int startTime, int duration, int[] interestingTime, int[] interestingDuration) {
        MovieToolbox.GetMovieNextInterestingTime(interestingTimeFlags, flags, resID, whichMediaTypes, flags, duration, interestingTime, interestingDuration);
    }

    private static short InsertEmptyMovieSegment(int fCreator, int flags, int duration) {
        return MovieToolbox.InsertEmptyMovieSegment(fCreator, flags, duration);
    }

    private static short InsertMovieSegment(int fCreator, int flags, int duration, int time4, int time5) {
        return MovieToolbox.InsertMovieSegment(fCreator, flags, duration, time4, time5);
    }

    private static short DeleteMovieSegment(int fCreator, int flags, int duration) {
        return MovieToolbox.DeleteMovieSegment(fCreator, flags, duration);
    }

    private static short ScaleMovieSegment(int fCreator, int flags, int duration, int time4) {
        return MovieToolbox.ScaleMovieSegment(fCreator, flags, duration, time4);
    }

    private static int CutMovieSelection(int time) {
        return MovieToolbox.CutMovieSelection(time);
    }

    private static int CopyMovieSelection(int time) {
        return MovieToolbox.CopyMovieSelection(time);
    }

    private static void PasteMovieSelection(int fCreator, int flags) {
        MovieToolbox.PasteMovieSelection(fCreator, flags);
    }

    private static void AddMovieSelection(int fCreator, int flags) {
        MovieToolbox.AddMovieSelection(fCreator, flags);
    }

    private static void ClearMovieSelection(int time) {
        MovieToolbox.ClearMovieSelection(time);
    }

    private static short PasteHandleIntoMovie(int fCreator, int flags, int duration, int time4, int time5) {
        return MovieToolbox.PasteHandleIntoMovie(fCreator, flags, duration, time4, time5);
    }

    private static short PutMovieIntoTypedHandle(int fCreator, int flags, int duration, int time4, int time5, int time6, int time7, int time8) {
        return MovieToolbox.PutMovieIntoTypedHandle(fCreator, flags, duration, time4, time5, time6, time7, time8);
    }

    private static short CopyMovieSettings(int fCreator, int flags) {
        return MovieToolbox.CopyMovieSettings(fCreator, flags);
    }

    private static short AddEmptyTrackToMovie(int fCreator, int flags, int duration, int time4, int[] dstTrack) {
        return MovieToolbox.AddEmptyTrackToMovie(fCreator, flags, duration, time4, dstTrack);
    }

    private static int NewMovieEditState(int time) {
        return MovieToolbox.NewMovieEditState(time);
    }

    private static short UseMovieEditState(int fCreator, int flags) {
        return MovieToolbox.UseMovieEditState(fCreator, flags);
    }

    private static int GetMovieDataSize(int fCreator, int flags, int duration) {
        return MovieToolbox.GetMovieDataSize(fCreator, flags, duration);
    }

    private static byte PtInMovie(int fCreator, int flags) {
        return MovieToolbox.PtInMovie(fCreator, flags);
    }

    private static void SetMovieLanguage(int fCreator, int flags) {
        MovieToolbox.SetMovieLanguage(fCreator, flags);
    }

    private static byte HasMovieChanged(int time) {
        return MovieToolbox.HasMovieChanged(time);
    }

    private static void ClearMovieChanged(int time) {
        MovieToolbox.ClearMovieChanged(time);
    }

    private static short SetMovieDefaultDataRef(int fCreator, int flags, int duration) {
        return MovieToolbox.SetMovieDefaultDataRef(fCreator, flags, duration);
    }

    private static short SetMovieColorTable(int fCreator, int flags) {
        return MovieToolbox.SetMovieColorTable(fCreator, flags);
    }

    private static void FlattenMovie(int fCreator, int flags, byte[] fsspec, int creator, short scriptTag, int createMovieFileFlags, short[] resId, byte[] resName) {
        MovieToolbox.FlattenMovie(fCreator, flags, fsspec, creator, scriptTag, createMovieFileFlags, resId, resName);
    }

    private static int FlattenMovieData(int fCreator, int flags, byte[] fsspec, int duration, short scriptTag, int time4) {
        return MovieToolbox.FlattenMovieData(fCreator, flags, fsspec, duration, scriptTag, time4);
    }

    private static void setIntInArray(byte[] array, int fCreator, int flags) {
        MemoryManager.setIntInArray(array, fCreator, flags);
    }

    private static void SetMovieProgressProc(int fCreator, int flags, int duration) {
        MovieToolbox.SetMovieProgressProc(fCreator, flags, duration);
    }

    private static void GetMovieProgressProc(int time, int[] proc, int[] refCon) {
        MovieToolbox.GetMovieProgressProc(time, proc, refCon);
    }

    private static short MovieSearchText(int fCreator, int flags, int duration, int time4, int[] searchTrack, int[] searchTime, int[] searchOffset) {
        return MovieToolbox.MovieSearchText(fCreator, flags, duration, time4, searchTrack, searchTime, searchOffset);
    }

    private static void GetPosterBox(int time, byte[] rect) {
        MovieToolbox.GetPosterBox(time, rect);
    }

    private static void SetPosterBox(int time, byte[] rect) {
        MovieToolbox.SetPosterBox(time, rect);
    }

    private static int GetMovieStatus(int time, int[] firstProblemTrack) {
        return MovieToolbox.GetMovieStatus(time, firstProblemTrack);
    }

    private static void ShowMovieInformation(int fCreator, int flags, int duration) {
        MovieToolbox.ShowMovieInformation(fCreator, flags, duration);
    }

    private static short PutMovieOnScrap(int fCreator, int flags) {
        return MovieToolbox.PutMovieOnScrap(fCreator, flags);
    }

    private static void SetMoviePlayHints(int fCreator, int flags, int duration) {
        MovieToolbox.SetMoviePlayHints(fCreator, flags, duration);
    }

    private static void SetMovieMasterTimeBase(int fCreator, int flags, int duration) {
        MovieToolbox.SetMovieMasterTimeBase(fCreator, flags, duration);
    }

    private static void SetMovieMasterClock(int fCreator, int flags, int duration) {
        MovieToolbox.SetMovieMasterClock(fCreator, flags, duration);
    }

    private static short ConvertMovieToFile(int fCreator, int flags, byte[] fsspec, int duration, int time4, short scriptTag, short[] resId, int time5, int time6) {
        return MovieToolbox.ConvertMovieToFile(fCreator, flags, fsspec, duration, time4, scriptTag, resId, time5, time6);
    }

    private static int QTVRGetQTVRTrack(int fCreator, int flags) {
        return VRManager.QTVRGetQTVRTrack(fCreator, flags);
    }

    private static short QTMovieNeedsTimeTable(int time, byte[] needsTimeTable) {
        return MovieToolbox.QTMovieNeedsTimeTable(time, needsTimeTable);
    }

    private static short GetMaxLoadedTimeInMovie(int movie, int[] time) {
        return MovieToolbox.GetMaxLoadedTimeInMovie(movie, time);
    }

    private static short PrePrerollMovie(int fCreator, int flags, int duration, int time4, int time5) {
        return MovieToolbox.PrePrerollMovie(fCreator, flags, duration, time4, time5);
    }

    private static void AbortPrePrerollMovie(int time, short err) {
        MovieToolbox.AbortPrePrerollMovie(time, err);
    }

    private static void SetMovieDrawingCompleteProc(int fCreator, int flags, int duration, int time4) {
        MovieToolbox.SetMovieDrawingCompleteProc(fCreator, flags, duration, time4);
    }

    private static short AddMovieExecuteWiredActionsProc(int fCreator, int flags, int duration) {
        return MovieToolbox.AddMovieExecuteWiredActionsProc(fCreator, flags, duration);
    }

    private static short RemoveMovieExecuteWiredActionsProc(int fCreator, int flags, int duration) {
        return MovieToolbox.RemoveMovieExecuteWiredActionsProc(fCreator, flags, duration);
    }

    private static short MovieExecuteWiredActions(int fCreator, int flags, int duration) {
        return MovieToolbox.MovieExecuteWiredActions(fCreator, flags, duration);
    }

    private static void SetGWorld(int fCreator, int flags) {
        QuickDrawManager.SetGWorld(fCreator, flags);
    }

    private static void GetGWorld(int[] port, int[] gdh) {
        QuickDrawManager.GetGWorld(port, gdh);
    }

    private static void GetMovieGWorld(int time, int[] port, int[] gdh) {
        MovieToolbox.GetMovieGWorld(time, port, gdh);
    }

    private static short CopyMovieUserData(int fCreator, int flags, int duration) {
        return MovieToolbox.CopyMovieUserData(fCreator, flags, duration);
    }

    private static void ChooseMovieClock(int fCreator, int flags) {
        MovieToolbox.ChooseMovieClock(fCreator, flags);
    }

    private static short NewMovieForDataRefFromHandle(int[] movie, int fCreator, short scriptTag, byte[] dataRefWasChanged, int flags, int duration) {
        return MovieToolbox.NewMovieForDataRefFromHandle(movie, fCreator, scriptTag, dataRefWasChanged, flags, duration);
    }

    private static short PutMovieForDataRefIntoHandle(int fCreator, int flags, int duration, int time4) {
        return MovieToolbox.PutMovieForDataRefIntoHandle(fCreator, flags, duration, time4);
    }

    private static int FlattenMovieDataToDataRef(int fCreator, int flags, int duration, int time4, int time5, short scriptTag, int time6) {
        return MovieToolbox.FlattenMovieDataToDataRef(fCreator, flags, duration, time4, time5, scriptTag, time6);
    }

    private static short QTGetTimeUntilNextTask(int[] duration, int time) {
        return MovieToolbox.QTGetTimeUntilNextTask(duration, time);
    }

    private static short QTInstallNextTaskNeededSoonerCallback(int fCreator, int flags, int duration, int time4) {
        return MovieToolbox.QTInstallNextTaskNeededSoonerCallback(fCreator, flags, duration, time4);
    }

    private static short QTUninstallNextTaskNeededSoonerCallback(int fCreator, int flags) {
        return MovieToolbox.QTUninstallNextTaskNeededSoonerCallback(fCreator, flags);
    }

    private static short SetMovieAnchorDataRef(int fCreator, int flags, int duration) {
        return MovieToolbox.SetMovieAnchorDataRef(fCreator, flags, duration);
    }

    private static short GetMovieAnchorDataRef(int time, int[] dataRef, int[] dataRefType, int[] outFlags) {
        return MovieToolbox.GetMovieAnchorDataRef(time, dataRef, dataRefType, outFlags);
    }
}
