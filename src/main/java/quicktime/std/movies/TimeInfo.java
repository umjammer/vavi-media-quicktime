
package quicktime.std.movies;

public class TimeInfo {
    public int time;

    public int duration;

    public TimeInfo(int paramInt1, int paramInt2) {
        this.time = paramInt1;
        this.duration = paramInt2;
    }

    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + "]";
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/TimeInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
