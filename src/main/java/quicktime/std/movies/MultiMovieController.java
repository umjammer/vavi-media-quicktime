package quicktime.std.movies;

import java.util.Vector;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
import quicktime.internal.toolbox.MovieControllerManager;
import quicktime.internal.toolbox.MovieToolbox;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.qd.QDGraphics;
import quicktime.qd.QDPoint;
import quicktime.std.StdQTException;


/**
 * The MultiMovieController class allows for multiple movies to be associated
 * with a single controller. It is the developer's responsibility to appropriately
 * manage the resources consumed by multiple movies.
 */
public final class MultiMovieController extends MovieController implements QuickTimeLib {

    /**
     * This constructor locates a movie controller component for you and assigns a movie to
     * that controller. This constructor always creates a controller that is visible and
     * attached to a movie.
     *
     * @param itsMovie Identifies the movie to be associated with the movie controller.
     * @see "QuickTime::NewMovieController()"
     */
    public MultiMovieController(Movie itsMovie) throws QTException {
        this(itsMovie, 0);
    }

    /**
     * This constructor locates a movie controller component for you and assigns a movie to
     * that controller. This constructor always creates a controller that is attached to
     * a movie.
     *
     * @param itsMovie  Identifies the movie to be associated with the movie controller.
     * @param someFlags Contains flags that control the operation.
     * @see "QuickTime::NewMovieController()"
     */
    public MultiMovieController(Movie itsMovie, int someFlags) throws QTException {
        super(itsMovie, someFlags);
    }

    /**
     * This constructor creates a MovieController object for a given subType. It attaches the
     * controller to the specifed movie in the specifed window.
     *
     * @param subType  Specifies the controller component sub type.
     * @param theMovie Specifies the movie to be associated with the movie controller.
     * @param window   Identifies the window in which the movie is to be displayed. This may NOT be null.
     * @param location Specifies the upper-left corner of the movie within the window
     *                 specified by the w parameter.
     * @see "QuickTime::MCNewAttachedController()"
     */
    public MultiMovieController(int subType, Movie theMovie, QDGraphics window, QDPoint location) throws QTException {
        super(subType, theMovie, window, location);
    }

    /**
     * This method lets you inform a movie controller component that your application
     * has used the Movie Toolbox to change the characteristics of its associated movie.
     *
     * @param changedMovie Specifies the changed movie.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MCMovieChanged()"
     */
    public void movieChanged(Movie changedMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCMovieChanged(_ID(), QTObject.ID(changedMovie)));
        }
    }

    /**
     * This method adds a movie to the movie controller. This method is the same
     * as the addMovie method. But we need to keep setMovie because it is inherited
     * from MovieController.
     *
     * @param aMovie      Identifies the movie to be associated with the movie controller.
     * @param movieWindow Identifies the window in which the movie is to be displayed. This may NOT be null unless the movie is also null.
     * @param location    Specifies the upper-left corner of the movie within the window
     *                    specified by the movieWindow parameter.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MCSetMovie()"
     * @see quicktime.std.movies.MultiMovieController
     */
    @Override
    public void setMovie(Movie aMovie, QDGraphics movieWindow, QDPoint location) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveMovie(_ID()));
            if (QTSession.isCurrentOS(4)) {
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(aMovie), 0, location.getPoint()));
            } else {
                if ((aMovie != null) && (movieWindow == null)) {
                    throw new StdQTException(-50);
                }
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(aMovie), QTObject.ID(movieWindow), location.getPoint()));
            }
        }
        Vector<Movie> x = mcHashtable.get(_ID());
        if (aMovie != null && x != null) {
            x.addElement(aMovie);
        }
    }

    /**
     * This method adds a movie to the movie controller. This method is the same
     * as the setMovie method. But we need to keep setMovie because it is inherited
     * from MovieController.
     *
     * @param aMovie      Identifies the movie to be associated with the movie controller.
     * @param movieWindow Identifies the window in which the movie is to be displayed. This may NOT be null unless the movie is also null.
     * @param location    Specifies the upper-left corner of the movie within the window
     *                    specified by the movieWindow parameter.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MCSetMovie()"
     */
    public void addMovie(Movie aMovie, QDGraphics movieWindow, QDPoint location) throws StdQTException {
        setMovie(aMovie, movieWindow, location);
    }

    /**
     * This method allows you to get the movie at a specified index among the associated movies of a controller.
     *
     * @param index Specifies the index of the movie.
     * @return a Movie object.
     * @throws quicktime.QTException
     * @see "QuickTime::MCGetIndMovie()"
     */
    public Movie getIndMovie(int index) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetIndMovie(_ID(), (short) index);
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, this);
    }

    /**
     * This method allows you to remove all of the associated movies of a controller.
     *
     * @see "QuickTime::MCRemoveAllMovies()"
     */
    public void removeAllMovies() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveAllMovies(_ID()));
        }
        Vector<Movie> x = mcHashtable.get(_ID());
        if (x != null) {
            x.removeAllElements();
        }
    }

    /**
     * This method allows you to remove the specified movie from the controller.
     *
     * @param movie Specifies the movie to be removed.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MCRemoveAMovie()"
     */
    public void removeAMovie(Movie movie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveAMovie(_ID(), QTObject.ID(movie)));
        }
        Vector<Movie> x = mcHashtable.get(_ID());
        if ((movie != null) && (x != null)) {
            x.removeElement(movie);
        }
    }

    /**
     * This method allows you to remove the movie of the controller.
     *
     * @see "QuickTime::MCRemoveMovie()"
     */
    @Override
    public void removeMovie() throws StdQTException {
        super.removeMovie();
    }

    private static int MCMovieChanged(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCMovieChanged(paramInt1, paramInt2);
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

    private static short GetMoviesError() {
        return MovieToolbox.GetMoviesError();
    }

    private static int MCRemoveAllMovies(int paramInt) {
        return MovieControllerManager.MCRemoveAllMovies(paramInt);
    }

    private static int MCRemoveAMovie(int paramInt1, int paramInt2) {
        return MovieControllerManager.MCRemoveAMovie(paramInt1, paramInt2);
    }
}
