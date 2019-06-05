
package quicktime.std.movies;

public final class StatusInfo {
    StatusInfo(int paramInt, Track paramTrack) {
        this.problemTrack = paramTrack;
        this.err = paramInt;
    }

    public Track getProblemTrack() {
        return this.problemTrack;
    }

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
