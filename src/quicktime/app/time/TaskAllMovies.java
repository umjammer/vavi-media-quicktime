
package quicktime.app.time;

import quicktime.QTException;
import quicktime.std.movies.Movie;


public class TaskAllMovies extends Tasking {
    private static final TaskThread t = new TaskThread("TaskAllMovies Tasker", 50);

    private static final TaskAllMovies tm = new TaskAllMovies();

    private static int nMovies = 0;

    private TaskAllMovies() {
        setDefaultTasker(t);
    }

    public static TaskAllMovies get() {
        return tm;
    }

    public static void addMovieAndStart() {
        if (nMovies == 0) {
            tm.startTasking();
        }

        nMovies += 1;
    }

    public static void removeMovie() {
        nMovies -= 1;
        if (nMovies <= 0) {
            nMovies = 0;
            tm.stopTasking();
        }
    }

    public static int currentMovieCount() {
        return nMovies;
    }

    public void task() throws QTException {
        Movie.taskAll(0);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/app/time/
 * TaskAllMovies.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
