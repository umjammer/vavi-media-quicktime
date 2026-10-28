package quicktime.std.movies.media;

import quicktime.std.movies.TimeInfo;

/**
 * The FoundTextInfo class contains information returned from searching text in a track.
 */
public final class FoundTextInfo extends TimeInfo {

    /**
     * The offset of the found text in track.
     */
    public int offset;

    /**
     * This constructor creates a FoundTextInfo object from the information returned after a search.
     * @param foundTime Specifies the time of the found text in track.
     * @param foundDuration Specifies the duration of the found text in track.
     * @param offset Specifies the offset of the found text in track.
     */
    public FoundTextInfo(int foundTime, int foundDuration, int offset) {
        super(foundTime, foundDuration);
        this.offset = offset;
    }
}

