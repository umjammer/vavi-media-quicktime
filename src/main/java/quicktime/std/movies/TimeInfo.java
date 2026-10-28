package quicktime.std.movies;

/**
 * The TimeInfo class represents time information returned by the Movie Toolbox
 * and general usage to pass in information regarding timing.
 */
public class TimeInfo {

    /**
     * Corresponds to some time value - generally a start time.
     */
    public final int time;

    /**
     * Corresponds to a duration value.
     */
    public final int duration;

    /**
     * This constructor creates a TimeInfo object.
     * @param time Specifies the a time value.
     * @param duration Specifies a duration value.
     */
    public TimeInfo(int time, int duration) {
        this.time = time;
        this.duration = duration;
    }

    /**
     * Returns a string representation of this class.
     * @return a String
     */
    public String toString() {
        return getClass().getName() + "[time=" + this.time + ",duration=" + this.duration + "]";
    }
}
