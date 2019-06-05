
package quicktime.std.movies;

import quicktime.QTException;
import quicktime.QTObject;


public final class MovieEditState extends QTObject {
    private Movie m;

    MovieEditState(Movie paramMovie, int paramInt) throws QTException {
        super(paramInt);
        this.m = paramMovie;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/MovieEditState.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
