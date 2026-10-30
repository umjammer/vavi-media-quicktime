package quicktime.std.movies;

import quicktime.QTException;
import quicktime.QTObject;

/**
 * The MovieEditState class implements the corresponding data structure of the Movie Toolbox.
 * An edit state contains information that completely defines a movie's content at the time
 * you create the edit state. It is, in essence, a checkpoint in the edit session.
 * You can manage a movie's edit states in order to implement an undo capability for
 * editing movies.
 * This structure specifies the movie edit state for an operation. Your application
 * obtains a movie edit state identifier when you create the edit state by calling the
 * NewMovieEditState function (described on page 2-255 of Inside Macintosh: QuickTime).
 */
public final class MovieEditState extends QTObject {

    private final Movie m;

    MovieEditState(Movie m, int nativeRep) throws QTException {
        super(nativeRep);
        this.m = m;
    }
}
