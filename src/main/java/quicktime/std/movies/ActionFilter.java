
package quicktime.std.movies;

import quicktime.qd.QDPoint;
import quicktime.qd.QDRect;
import quicktime.std.clocks.TimeRecord;
import quicktime.util.StringHandle;


public abstract class ActionFilter {
    public boolean execute(MovieController paramMovieController, int paramInt) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, float paramFloat) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, float[] paramArrayOfFloat) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, boolean[] paramArrayOfBoolean) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, int[] paramArrayOfInt) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, boolean paramBoolean) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, QDRect paramQDRect) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt1, int paramInt2) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, TimeRecord paramTimeRecord) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, ResolvedQTEventSpec paramResolvedQTEventSpec) {
        return false;
    }

    public boolean execute(MovieController paramMovieController,
                           int paramInt,
                           AtomContainer paramAtomContainer,
                           Atom paramAtom) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, StringHandle paramStringHandle) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt1, int paramInt2, StringHandle paramStringHandle) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, QDPoint paramQDPoint) {
        return false;
    }

    public boolean execute(MovieController paramMovieController,
                           int paramInt,
                           QTGetExternalMovieRecord paramQTGetExternalMovieRecord) {
        return false;
    }

    public boolean execute(MovieController paramMovieController, int paramInt, TimeRecord paramTimeRecord, String paramString) {
        return false;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/ActionFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
