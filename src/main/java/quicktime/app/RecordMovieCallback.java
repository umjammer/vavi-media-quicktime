package quicktime.app;

import quicktime.std.movies.Movie;

/**
 * Deprecated.
 * An object that implements this interface can be given to the RecordMovie
 * class to notify the caller when recording is complete.
 * The movie that is returned in the parametre of the finish method is the movie
 * to which a video track was added.
 */
public abstract interface RecordMovieCallback {

    /**
     * Deprecated.
     * <I>since QTJava 6.1</I>
     * Called when the movie is completed recording.
     * @param m the movie to which the track was added
     */
    public abstract void finish(Movie paramMovie);
}

