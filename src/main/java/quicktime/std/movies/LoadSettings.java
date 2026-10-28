package quicktime.std.movies;

/**
 * The LoadSettings class implements the load settings for movie tracks.
 */
public final class LoadSettings extends TimeInfo {

    /**
     * The flags for preloading.
     */
    public final int preloadFlags;

    /**
     * The default hints for preloading.
     */
    public final int defaultHints;

    /**
     * This constructor allows you to make a LoadSettings object.
     * @param preloadTime Specifies the starting time of preloading.
     * @param preloadDuration Specifies the duration of preloading.
     * @param preloadFlags Specifies some flags for preloading.
     * @param defaultHints Specifies the default hints for preloading.
     */
    public LoadSettings(int preloadTime, int preloadDuration, int preloadFlags, int defaultHints) {
        super(preloadTime, preloadDuration);
        this.preloadFlags = preloadFlags;
        this.defaultHints = defaultHints;
    }
}
