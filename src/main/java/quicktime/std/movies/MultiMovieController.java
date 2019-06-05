
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

    public MultiMovieController(Movie paramMovie) throws QTException {
        this(paramMovie, 0);
    }

    public MultiMovieController(Movie paramMovie, int paramInt) throws QTException {
        super(paramMovie, paramInt);
    }

    public MultiMovieController(int paramInt,
            Movie paramMovie,
            QDGraphics paramQDGraphics,
            QDPoint paramQDPoint) throws QTException {
        super(paramInt, paramMovie, paramQDGraphics, paramQDPoint);
    }

    public final void movieChanged(Movie paramMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCMovieChanged(_ID(), QTObject.ID(paramMovie)));
        }
    }

    public final void setMovie(Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveMovie(_ID()));
            if (QTSession.isCurrentOS(4)) {
                StdQTException.checkError(MCSetMovie(_ID(), QTObject.ID(paramMovie), 0, paramQDPoint.getPoint()));

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

        Vector<Movie> x = mcHashtable.get(_ID());
        if (paramMovie != null && x != null) {
            x.addElement(paramMovie);
        }
    }

    public final void addMovie(Movie paramMovie, QDGraphics paramQDGraphics, QDPoint paramQDPoint) throws StdQTException {
        setMovie(paramMovie, paramQDGraphics, paramQDPoint);
    }

    public final Movie getIndMovie(int paramInt) throws QTException {
        int i = 0;
        synchronized (QTNative.globalsLock) {
            i = MCGetIndMovie(_ID(), (short) paramInt);
        }
        StdQTException.checkError(GetMoviesError());

        return new Movie(i, this);
    }

    public final void removeAllMovies() throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveAllMovies(_ID()));
        }

        Vector<Movie> x = mcHashtable.get(new Integer(_ID()));
        if (x != null) {
            x.removeAllElements();
        }
    }

    public final void removeAMovie(Movie paramMovie) throws StdQTException {
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MCRemoveAMovie(_ID(), QTObject.ID(paramMovie)));
        }

        Vector<Movie> x = mcHashtable.get(new Integer(_ID()));
        if ((paramMovie != null) && (x != null)) {
            x.removeElement(paramMovie);
        }
    }

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
