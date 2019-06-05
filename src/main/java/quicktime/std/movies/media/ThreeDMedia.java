
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class ThreeDMedia extends Media {
    public ThreeDMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1902392164, paramInt, paramDataRef);
    }

    public ThreeDMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    ThreeDMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new ThreeDDescription(1902392164);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new ThreeDMediaHandler(paramInt, paramObject);
    }

    public ThreeDMediaHandler getThreeDHandler() throws StdQTException {
        return new ThreeDMediaHandler(getNativeMediaHandler(), this);
    }

    public ThreeDDescription getThreeDDescription(int paramInt) throws QTException {
        ThreeDDescription localThreeDDescription = new ThreeDDescription(1902392164);
        getNativeSampleDescription(paramInt, localThreeDDescription);
        return localThreeDDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/ThreeDMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
