package quicktime.app;

import quicktime.std.movies.Movie;

/**
 * @deprecated
 * An object that implements this interface can be given to the RecordMovie
 * class to notify the caller when recording is complete.
 * The movie that is returned in the parametre of the finish method is the movie
 * to which a video track was added.
 */
public interface RecordMovieCallback {

    /**
     * @deprecated
     * @since QTJava 6.1
     * Called when the movie is completed recording.
     * @param m the movie to which the track was added
     */
    void finish(Movie m);
}

