package quicktime.std.movies;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.QTSession;
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

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(MultiMovieController.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * This constructor locates a movie controller component for you and assigns a movie to
     * that controller. This constructor always creates a controller that is visible and
     * attached to a movie.
     * QuickTime::NewMovieController()
     * @param itsMovie Identifies the movie to be associated with the movie controller.
     */
    public MultiMovieController(Movie paramMovie) throws QTException {
        this(paramMovie, 0);
    }

    /**
     * This constructor locates a movie controller component for you and assigns a movie to
     * that controller. This constructor always creates a controller that is attached to
     * a movie.
     * QuickTime::NewMovieController()
     * @param itsMovie Identifies the movie to be associated with the movie controller.
     * @param someFlags Contains flags that control the operation.
     */
    public MultiMovieController(Movie paramMovie, int paramInt) throws QTException {
        super(paramMovie, paramInt);
    }

    /**
     * This constructor creates a MovieController object for a given subType. It attaches the
     * controller to the specifed movie in the specifed window.
     * QuickTime::MCNewAttachedController()
     * @param subType Specifies the controller component sub type.
     * @param theMovie Specifies the movie to be associated with the movie controller.
     * @param window Identifies the window in which the movie is to be displayed. This may NOT be null.
     * @param location Specifies the upper-left corner of the movie within the window
     *                  specified by the w parameter.
     */
    public MultiMovieController(int paramInt, Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws QTException {
        super(paramInt, paramMovie, paramQDGraphics, paramQDPoint);
    }

    /**
     * This method lets you inform a movie controller component that your application
     * has used the Movie Toolbox to change the characteristics of its associated movie.
     * QuickTime::MCMovieChanged()
     * @param changedMovie Specifies the changed movie.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void movieChanged(Movie paramMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCMovieChanged(_ID(), QTObject.ID(paramMovie)));
        }
    }

    /**
     * This method adds a movie to the movie controller. This method is the same
     * as the addMovie method. But we need to keep setMovie because it is inherited
     * from MovieController.
     * QuickTime::MCSetMovie()
     * @param aMovie Identifies the movie to be associated with the movie controller.
     * @param movieWindow Identifies the window in which the movie is to be displayed. This may NOT be null unless the movie is also null.
     * @param location Specifies the upper-left corner of the movie within the window
     *                     specified by the movieWindow parameter.
     * @param {@link quicktime.std.StdQTException}
     * @see {@link quicktime.std.movies.MultiMovieController}
     */
    public final void setMovie(Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveMovie(_ID()));
            if (QTSession.isCurrentOS(4)) {
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(paramMovie), 0, paramQDPoint.getPoint()));
            } else {
                if ((paramMovie != null) && (paramQDGraphics == null)) {
                    throw new StdQTException(-50);
                }
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(paramMovie), QTObject.ID(paramQDGraphics), paramQDPoint.getPoint()));
            }
        }
        Vector<Movie> x = mcHashtable.get(_ID());
        if (paramMovie != null && x != null) {
            x.addElement(paramMovie);
        }
    }

    /**
     * This method adds a movie to the movie controller. This method is the same
     * as the setMovie method. But we need to keep setMovie because it is inherited
     * from MovieController.
     * QuickTime::MCSetMovie()
     * @param aMovie Identifies the movie to be associated with the movie controller.
     * @param movieWindow Identifies the window in which the movie is to be displayed. This may NOT be null unless the movie is also null.
     * @param location Specifies the upper-left corner of the movie within the window
     *                     specified by the movieWindow parameter.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void addMovie(Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws StdQTException {
        setMovie(paramMovie, paramQDGraphics, paramQDPoint);
    }

    /**
     * This method allows you to get the movie at a specified index among the associated movies of a controller.
     * QuickTime::MCGetIndMovie()
     * @param index Specifies the index of the movie.
     * @return a Movie object.
     * @return {@link quicktime.QTException}
     */
    public final Movie getIndMovie(int paramInt) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetIndMovie(_ID(), (short) paramInt);
        }
        StdQTException.checkError(GetMoviesError());
        return new Movie(i, this);
    }

    /**
     * This method allows you to remove all of the associated movies of a controller.
     * QuickTime::MCRemoveAllMovies()
     */
    public final void removeAllMovies() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveAllMovies(_ID()));
        }
        Vector<Movie> x = mcHashtable.get(new Integer(_ID()));
        if (x != null) {
            x.removeAllElements();
        }
    }

    /**
     * This method allows you to remove the specified movie from the controller.
     * QuickTime::MCRemoveAMovie()
     * @param movie Specifies the movie to be removed.
     * @param {@link quicktime.std.StdQTException}
     */
    public final void removeAMovie(Movie paramMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveAMovie(_ID(), QTObject.ID(paramMovie)));
        }
        Vector<Movie> x = mcHashtable.get(new Integer(_ID()));
        if ((paramMovie != null) && (x != null)) {
            x.removeElement(paramMovie);
        }
    }

    /**
     * This method allows you to remove the movie of the controller.
     * QuickTime::MCRemoveMovie()
     */
    public final void removeMovie() throws StdQTException {
        super.removeMovie();
    }

    private static native int MCMovieChanged(int paramInt1, int paramInt2);

    private static native int MCRemoveMovie(int paramInt);

    private static native int MCSetMovie(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    private static native int MCGetIndMovie(int paramInt, short paramShort);

    private static native short GetMoviesError();

    private static native int MCRemoveAllMovies(int paramInt);

    private static native int MCRemoveAMovie(int paramInt1, int paramInt2);
}
