package quicktime.app.view;

import java.awt.Component;
import quicktime.QTException;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;

/**
 * This interface is implemented by the underlying QTJava Components . These are convenience methods that are invoked by applications to
 * set and get the current movie or image displayed. To remove pass a null parameter to the set methods .
 */
public abstract interface QTComponent {

    /**
     * Sets the current image for this QTComponent to the specified image represented by the GraphicsImporter object.
     * @param img the GraphicsImporter object representing the image that this component displays. If the parameter is null the
     *  current image is removed.
     * @param {@link quicktime.QTException}
     */
    public abstract void setImage(GraphicsImporter paramGraphicsImporter) throws QTException;

    /**
     * Sets the current movie for this QTComponent to the specified movie represented by the Movie object.
     * @param mov the Movie object representing the movie that this component displays. If the parameter is null the
     *  current mov is removed.
     * @param {@link quicktime.QTException}
     */
    public abstract void setMovie(Movie paramMovie) throws QTException;

    /**
     * Sets the movie for this QTComponent to the specified movie represented by the MovieController object.
     * The movie and the controller both will be displayed in the component.
     * @param movieController the MovieController object representing the movie that this component displays. If the parameter is null the
     *  current Movie &amp; its controller is removed.
     * @param {@link quicktime.QTException}
     */
    public abstract void setMovieController(MovieController paramMovieController) throws QTException;

    /**
     * Gets the currently displayed movie of this component.
     * @return the current movie or null if no movie is displayed.
     */
    public abstract Movie getMovie();

    /**
     * Gets the currently displayed movie's controller object of this component.
     * @return the current MovieController or null if component has no controller.
     */
    public abstract MovieController getMovieController();

    /**
     * Gets the currently displayed image of this component.
     * @return the GraphicsImporter object or null if no image is displayed.
     */
    public abstract GraphicsImporter getImage();

    /**
     * Returns the AWT component representation of the underlying object.
     * @return the Component
     */
    public abstract Component asComponent();
}

