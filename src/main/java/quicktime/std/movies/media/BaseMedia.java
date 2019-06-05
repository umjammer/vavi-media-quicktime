
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class BaseMedia extends Media {
    public BaseMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1735291491, paramInt, paramDataRef);
    }

    public BaseMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    BaseMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1735291491);
    }

    MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MediaHandler(paramInt, paramObject);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/BaseMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
