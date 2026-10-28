package quicktime.std.movies;

/**
 * Object that implement this interface can be installed to a given movie and notified
 * by the MovieToolbox whenever the pre-preroll of a movie is completed.
 */
public interface MoviePrePreroll {

    /**
     * The execute method is called by the Movie toolbox when pre-preroll is completed
     * @param m the Movie that has been pre-prerolled
     * @param err the moviePreRoll error code - or zero if no error in pre-preroll
     * @see quicktime.std.clocks.QTCallBack
     */
    void execute(Movie m, int err);
}
