
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class MPEGMedia extends Media {
    public MPEGMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1297106247, paramInt, paramDataRef);
    }

    public MPEGMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    MPEGMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1297106247);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MPEGMediaHandler(paramInt, paramObject);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MPEGMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
