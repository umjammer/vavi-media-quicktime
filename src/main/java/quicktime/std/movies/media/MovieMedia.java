
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class MovieMedia extends Media {
    public MovieMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1836019574, paramInt, paramDataRef);
    }

    public MovieMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    MovieMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SampleDescription(0);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MovieMediaHandler(paramInt, paramObject);
    }

    public MovieMediaHandler getMovieHandler() throws StdQTException {
        return new MovieMediaHandler(getNativeMediaHandler(), this);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MovieMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
