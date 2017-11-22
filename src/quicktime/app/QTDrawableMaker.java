
package quicktime.app;

import quicktime.QTException;
import quicktime.QTRuntimeException;
import quicktime.QTSession;
import quicktime.app.display.QTDrawable;
import quicktime.app.image.GraphicsImporterDrawer;
import quicktime.app.players.QTPlayer;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;


/**
 * @deprecated
 */
public class QTDrawableMaker {
    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * @deprecated
     */
    public QTDrawable fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        return new GraphicsImporterDrawer(paramGraphicsImporter);
    }

    /**
     * @deprecated
     */
    public QTDrawable fromMovie(Movie paramMovie) throws QTException {
        MovieController localMovieController = new MovieController(paramMovie);
        localMovieController.setKeysEnabled(true);
        return new QTPlayer(localMovieController);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/
 * QTDrawableMaker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
