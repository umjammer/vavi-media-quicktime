package quicktime.std.movies;

/**
 * The StatusInfo class represents the results returned from querying the status of a movie.
 */
public final class StatusInfo {

    StatusInfo(int err, Track problemTrack) {
        this.problemTrack = problemTrack;
        this.err = err;
    }

    /**
     * This method returns the found problem track.
     * @return the found problem track.
     */
    public Track getProblemTrack() {
        return this.problemTrack;
    }

    /**
     * This method returns the error.
     * @return the error.
     */
    public int getErr() {
        return this.err;
    }

    private Track problemTrack = null;

    private int err = 0;
}
