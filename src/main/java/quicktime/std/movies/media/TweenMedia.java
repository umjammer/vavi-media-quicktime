
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class TweenMedia extends Media {
    public TweenMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1953981806, paramInt, paramDataRef);
    }

    public TweenMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    TweenMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1953981806);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MediaHandler(paramInt, paramObject);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/TweenMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
