package quicktime.std.movies;

/**
 * The LoadSettings class implements the load settings for movie tracks.
 */
public final class LoadSettings extends TimeInfo {

    /**
     * The flags for preloading.
     */
    public int preloadFlags;

    /**
     * The default hints for preloading.
     */
    public int defaultHints;

    /**
     * This constructor allows you to make a LoadSettings object.
     * @param preloadTime Specifies the starting time of preloading.
     * @param preloadDuration Specifies the duration of preloading.
     * @param preloadFlags Specifies some flags for preloading.
     * @param defaultHints Specifies the default hints for preloading.
     */
    public LoadSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super(paramInt1, paramInt2);
        this.preloadFlags = paramInt3;
        this.defaultHints = paramInt4;
    }
}

