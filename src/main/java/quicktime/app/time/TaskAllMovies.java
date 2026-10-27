package quicktime.app.time;

import quicktime.QTException;
import quicktime.std.movies.Movie;

/**
 * QuickTime's MoviesTask call can be called in two ways.
 * It can be called on a specific movie - this is what the MoviePlayer.task method
 * does (and Movie.task)
 * It can be called to service all active TimeBases and Movies in a single call.
 *
 * This class creates a single Taskable object that will use this second alternative,
 * making the single Movie.taskAll call and allowing the MovieToolbox to task all of
 * the exisiting and active Movies and TimeBases in a single native call.
 *
 * Classes in the quicktime.app packages (notably MoviePlayer, Timer, MoviePresenter) default
 * behaviour is not to task themselves individually but to register with the TaskAllMovies
 * class that they have a requirement for tasking. The TaskAllMovies call then has a
 * single thread and taskable object (itself) that calls the Moive.taskAll to
 * let QuickTime's MovieTask call do the required tasks for any of these active Movies
 * and TimeBases.
 *
 * An application can retrieve the Tasking object and set its default thread to a
 * TaskThread object of its choice.
 * @see quicktime.app.players.MoviePlayer,
 * {@link quicktime.app.time.Timer},
 * {@link quicktime.app.players.MoviePresenter}
 */
public class TaskAllMovies extends Tasking {

    private static final TaskThread t = new TaskThread("TaskAllMovies Tasker", 50);

    private static final TaskAllMovies tm = new TaskAllMovies();

    private static int nMovies = 0;

    private TaskAllMovies() {
        setDefaultTasker(t);
    }

    /**
     * Returns the TaskAllMovies Tasking object that is used to call Movie.taskAll.
     * @return a TaskAllMovies object
     */
    public static TaskAllMovies get() {
        return tm;
    }

    /**
     * Informs the TaskAllMovies object that another Movie or TimeBase is active
     * and would require tasking. If this is the only active Movie or TimeBase the
     * TaskAllMovies' TaskThread will be started.
     */
    public static void addMovieAndStart() {
        if (nMovies == 0) {
            tm.startTasking();
        }
        nMovies += 1;
    }

    /**
     * Informs the TaskAllMovies object that a Movie or TimeBase that was active
     * is no longer. If this was the only active Movie or TimeBase the
     * TaskAllMovies' TaskThread will be stopped.
     */
    public static void removeMovie() {
        nMovies -= 1;
        if (nMovies <= 0) {
            nMovies = 0;
            tm.stopTasking();
        }
    }

    /**
     * Returns the current number of active Movies and/or TimeBases that the
     * TaskAllMovies object is registered to count.
     * @return an int
     */
    public static int currentMovieCount() {
        return nMovies;
    }

    /**
     * Calls Movie.taskAll to task all active Movies and TimeBases.
     */
    @Override
    public void task() throws QTException {
        Movie.taskAll(0);
    }
}

