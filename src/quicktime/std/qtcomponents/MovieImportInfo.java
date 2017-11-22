
package quicktime.std.qtcomponents;

import quicktime.std.movies.Track;


public final class MovieImportInfo {
    public MovieImportInfo(Track paramTrack, int paramInt1, int paramInt2) {
        this.trackRef = paramTrack;
        this.duration = paramInt1;
        this.flags = paramInt2;
    }

    public Track track() {
        return this.trackRef;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getFlags() {
        return this.flags;
    }

    private Track trackRef = null;

    private int duration = 0;

    private int flags = 0;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * qtcomponents/MovieImportInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
