package quicktime.app.time;

import quicktime.QTException;
import quicktime.std.clocks.TimeBase;

/**
 * This interface defines a protocol for dealing with time lines. The
 * Timer, MoviePlayer objects all have a TimeBase as the
 * foundation upon which their sense of time is founded. This interface
 * provids a way to get this TimeBase and to set and get its current rate.
 *
 * There is a close relationship between Timeable objects and Taskable objects,
 * A Timeable object's TimeBase
 * will tick of its own accord - time marches on regardless. However Timeable objects
 * generally have tasks that need to be performed at certain times. For instance,
 * a movie needs to draw its next frame at time x, etc. Whilst some of the tasks that
 * these objects perform are done at interrupt time, they also need to be given task
 * time, time from the application when the tasks and schedules that they maintain can
 * be performed outside of the constraints or system load of interrupt (or highest priority thread)
 * time.
 *
 * Thus these objects have a requirement to have some kind of task method called,
 * in the case of a Movie it is MoviesTask, of a MovieController - MCIdle
 *
 * The tasking requirements of these classes maybe performed directly by the class itself
 * or it maybe delegated to another class (as in the case
 * of MoviePlayer).
 *
 * @see Taskable,
 * {@link quicktime.app.time.Timer},
 * {@link quicktime.app.players.MoviePlayer},
 * {@link quicktime.std.clocks.TimeBase}
 */
public abstract interface Timeable {

    /**
     * Returns the current time base that provides the time foundation
     * of this object.
     * @return a TimeBase
     * @return {@link quicktime.QTException}
     */
    public abstract TimeBase getTimeBase() throws QTException;

    /**
     * Sets the Timeable's playback rate. Typically a rate of zero will
     * mean that the player is stopped, a positive value that the player
     * is playing forwards, a negative value that the time values are decreasing.
     * @param rate the new playback rate.
     * @param {@link quicktime.QTException}
     */
    public abstract void setRate(float paramFloat) throws QTException;

    /**
     * Returns the current rate of the Timeable object.
     * @return the playback rate
     * @return {@link quicktime.QTException}
     */
    public abstract float getRate() throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * Timeable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
