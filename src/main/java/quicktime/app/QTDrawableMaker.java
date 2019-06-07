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
 * Deprecated.
 * This class is used in the making of QTDrawable objects by the QTFactory.makeDrawable methods
 * when a GraphicsImporter or Movie is made from the user's input. Applications can
 * subclass this class and pass it in to the makeDrawable methods to overide the method
 * that is of particular concern to return an object that is more appropriate to their
 * use. For example an ImagePresenter object could be returned from the GraphicsImporter that
 * yields better drawing performance but uses more memory.
 *
 * class MyQTDrawableMaker extends QTDrawableMaker {
 * public QTDrawable fromGraphicsImporter (GraphicsImporter gi) throws QTException {
 * return ImagePresenter.fromGraphicsImporter (gi);
 * }
 * }
 *
 * And this class is used with the factory methods like this:
 *
 * QTDrawable d = QTFactory.makeDrawable (..., new MyQTDrawable());
 */
public class QTDrawableMaker {

    static {
        if ((QTSession.isCurrentOS(4)) && (QTSession.getJavaVersion() >= 65540)) {
            throw new QTRuntimeException("Unsupported on Mac OS X and Java 1.4 and higher.");
        }
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @param gi the GraphicsImporter from which to construct a QTDrawable object which is capable of
     *  presenting the image data imported by the GraphicsImporter.
     * @return an GraphicsImporterDrawer object from the supplied GraphicsImporter
     * @return {@link quicktime.QTException}
     */
    public QTDrawable fromGraphicsImporter(GraphicsImporter paramGraphicsImporter) throws QTException {
        return new GraphicsImporterDrawer(paramGraphicsImporter);
    }

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * @param m the Movie from which to construct a QTDrawable object which is capable of
     *  presenting the Movie.
     * @return a QTPlayer object from the supplied Movie
     * @return {@link quicktime.QTException}
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
