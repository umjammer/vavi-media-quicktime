
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class FlashMedia extends Media {
    public FlashMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1718383464, paramInt, paramDataRef);
    }

    public FlashMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    FlashMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new FlashDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new FlashMediaHandler(paramInt, paramObject);
    }

    public FlashMediaHandler getFlashHandler() throws StdQTException {
        return new FlashMediaHandler(getNativeMediaHandler(), this);
    }

    public FlashDescription getFlashDescription(int paramInt) throws QTException {
        FlashDescription localFlashDescription = new FlashDescription();
        getNativeSampleDescription(paramInt, localFlashDescription);
        return localFlashDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/FlashMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
