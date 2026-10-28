package quicktime.std.qtcomponents;

import quicktime.std.movies.Track;


/**
 * MovieImportInfo contains information for the movie import component.
 * The results returned from querying a movie import component using a movie, a data reference and time values.
 */
public final class MovieImportInfo {

    /**
     * Creates a MovieImportInfo object containing given data.
     * @param t The used track result from movie import withFile/withHandle method.
     * @param timeVal The time value duration result from movie import withFile/withHandle method.
     */
    public MovieImportInfo(Track t, int timeVal, int flags) {
        this.trackRef = t;
        this.duration = timeVal;
        this.flags = flags;
    }

    /**
     * returns the referenced track(usedTrack) from the movie import component withFile method
     */
    public Track track() {
        return this.trackRef;
    }

    /**
     * returns the addedDuration from the movie import component withFile method
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * returns the outflags from the movie import component withFile method
     */
    public int getFlags() {
        return this.flags;
    }

    private Track trackRef = null;

    private int duration = 0;

    private int flags = 0;
}
