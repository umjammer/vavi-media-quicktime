
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.image.ImageDescription;
import quicktime.std.movies.Track;


public final class VideoMedia extends Media {
    public VideoMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1986618469, paramInt, paramDataRef);
    }

    public VideoMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    VideoMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new ImageDescription(0);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new VideoMediaHandler(paramInt, paramObject);
    }

    public VideoMediaHandler getVideoHandler() throws StdQTException {
        return new VideoMediaHandler(getNativeMediaHandler(), this);
    }

    public ImageDescription getImageDescription(int paramInt) throws QTException {
        ImageDescription localImageDescription = new ImageDescription(0);
        getNativeSampleDescription(paramInt, localImageDescription);
        return localImageDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/VideoMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
