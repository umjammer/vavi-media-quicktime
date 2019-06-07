package quicktime.std.movies;

/**
 * Object that implement this interface can be installed to a given movie and notified
 * by the MovieToolbox whenever the pre-preroll of a movie is completed.
 */
public abstract interface MoviePrePreroll {

    /**
     * The execute method is called by the Movie toolbox when pre-preroll is completed
     * @param m the Movie that has been pre-prerolled
     * @param err the moviePreRoll error code - or zero if no error in pre-preroll
     * @see {@link quicktime.std.clocks.QTCallBack}
     */
    public abstract void execute(Movie paramMovie, int paramInt);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/MoviePrePreroll.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
