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
    public FoundTextInfo(int paramInt1, int paramInt2, int paramInt3) {
        super(paramInt1, paramInt2);
        this.offset = paramInt3;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/FoundTextInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
