
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class MusicMedia extends Media {
    public MusicMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1836413801, paramInt, paramDataRef);
    }

    public MusicMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    MusicMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new MusicDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MusicMediaHandler(paramInt, paramObject);
    }

    public MusicMediaHandler getMusicHandler() throws StdQTException {
        return new MusicMediaHandler(getNativeMediaHandler(), this);
    }

    public MusicDescription getMusicDescription(int paramInt) throws QTException {
        MusicDescription localMusicDescription = new MusicDescription();
        getNativeSampleDescription(paramInt, localMusicDescription);
        return localMusicDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MusicMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
