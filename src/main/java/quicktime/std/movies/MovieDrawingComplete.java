package quicktime.std.movies;

/**
 * Object that implement this interface can be installed to a given movie and notified
 * by the MovieToolbox whenever drawing is completed on that movie
 */
public abstract interface MovieDrawingComplete {

    /**
     * The execute method is called by the Movie toolbox when drawing is completed.
     * @param m the movie which drawing has just been completed upon
     * @return an error code or zero if no error
     * @see {@link quicktime.std.clocks.QTCallBack},
     * {@link quicktime.std.movies.Movie#setDrawingCompleteProc(int, quicktime.std.movies.MovieDrawingComplete)}
     */
    public abstract int execute(Movie paramMovie);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/MovieDrawingComplete.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
