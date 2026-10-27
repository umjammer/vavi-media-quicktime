package quicktime.std.movies;

import quicktime.QTException;
import quicktime.QTObject;

/**
 * The TrackEditState class implements the corresponding data structure of the Movie Toolbox.
 * The Movie Toolbox provides functions that allow you to capture and restore the edit state
 * of a track. As with the functions that manipulate a movie's edit state, you can manage a
 * track's edit states in order to implement an undo capability for track editing.
 * For example, you can capture a track's edit state before performing an editing operation,
 * such as a cut, and later restore the old state.
 * This structure specifies the track edit state for an operation.
 * Your application obtains a track edit state identifier when you create the edit state by
 * calling the NewTrackEditState function (described on page 2-269 of
 * Inside Macintosh: QuickTime).
 */
public final class TrackEditState extends QTObject {

    private final Track t;

    TrackEditState(Track paramTrack, int paramInt) throws QTException {
        super(paramInt);
        this.t = paramTrack;
    }
}
