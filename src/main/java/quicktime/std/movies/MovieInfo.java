package quicktime.std.movies;

/**
 * The MovieInfo class holds various information returned by the Movie Toolbox about a movie.
 */
public final class MovieInfo {

    /**
     * The resource id.
     */
    public int resID;

    /**
     * If data reference was changed or not.
     */
    public boolean dataRefWasChanged;

    /**
     * The resource name.
     */
    public String resourceName;

    void setMovieInfo(int resID, boolean dataRefWasChanged, String resourceName) {
        this.resID = resID;
        this.dataRefWasChanged = dataRefWasChanged;
        this.resourceName = resourceName;
    }
}
