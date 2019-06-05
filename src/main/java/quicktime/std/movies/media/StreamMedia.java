
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class StreamMedia extends Media {
    public StreamMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1937011309, paramInt, paramDataRef);
    }

    public StreamMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    StreamMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new StreamDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new StreamMediaHandler(paramInt, paramObject);
    }

    public StreamMediaHandler getStreamHandler() throws StdQTException {
        return new StreamMediaHandler(getNativeMediaHandler(), this);
    }

    public StreamDescription getStreamDescription(int paramInt) throws QTException {
        StreamDescription localStreamDescription = new StreamDescription();
        getNativeSampleDescription(paramInt, localStreamDescription);
        return localStreamDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/StreamMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
