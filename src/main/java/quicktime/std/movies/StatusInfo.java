package quicktime.std.movies;

/**
 * The StatusInfo class represents the results returned from querying the status of a movie.
 */
public final class StatusInfo {

    StatusInfo(int paramInt, Track paramTrack) {
        this.problemTrack = paramTrack;
        this.err = paramInt;
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
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/StatusInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
