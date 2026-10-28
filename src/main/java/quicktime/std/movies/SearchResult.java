package quicktime.std.movies;

/**
 * The SearchResult class represents the results returned from searching for text in a movie.
 */
public final class SearchResult {

    SearchResult(Track paramTrack, int paramInt1, int paramInt2) {
        this.foundTrack = paramTrack;
        this.time = paramInt1;
        this.offset = paramInt2;
    }

    /**
     * This method returns the found track.
     *
     * @return the found track.
     */
    public Track getFoundTrack() {
        return this.foundTrack;
    }

    /**
     * This method returns the found time.
     *
     * @return the found time.
     */
    public int getTime() {
        return this.time;
    }

    /**
     * This method returns the found offset.
     *
     * @return the found offset.
     */
    public int getOffset() {
        return this.offset;
    }

    private Track foundTrack = null;

    private int time = 0;

    private int offset = 0;
}
