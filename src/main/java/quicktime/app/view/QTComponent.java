
package quicktime.app.view;

import java.awt.Component;

import quicktime.QTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;


public abstract interface QTComponent {
    public abstract void setImage(GraphicsImporter paramGraphicsImporter) throws QTException;

    public abstract void setMovie(Movie paramMovie) throws QTException;

    public abstract void setMovieController(MovieController paramMovieController) throws QTException;

    public abstract Movie getMovie();

    public abstract MovieController getMovieController();

    public abstract GraphicsImporter getImage();

    public abstract Component asComponent();
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/view/
 * QTComponent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
