
package quicktime.std.movies;

public final class SearchResult {
    SearchResult(Track paramTrack, int paramInt1, int paramInt2) {
        this.foundTrack = paramTrack;
        this.time = paramInt1;
        this.offset = paramInt2;
    }

    public Track getFoundTrack() {
        return this.foundTrack;
    }

    public int getTime() {
        return this.time;
    }

    public int getOffset() {
        return this.offset;
    }

    private Track foundTrack = null;

    private int time = 0;

    private int offset = 0;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/SearchResult.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
