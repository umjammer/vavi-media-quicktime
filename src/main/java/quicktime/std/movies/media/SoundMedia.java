
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class SoundMedia extends Media {
    public SoundMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1936684398, paramInt, paramDataRef);
    }

    public SoundMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    SoundMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SoundDescription(0);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new SoundMediaHandler(paramInt, paramObject);
    }

    public SoundMediaHandler getSoundHandler() throws StdQTException {
        return new SoundMediaHandler(getNativeMediaHandler(), this);
    }

    public SoundDescription getSoundDescription(int paramInt) throws QTException {
        SoundDescription localSoundDescription = new SoundDescription(0);
        getNativeSampleDescription(paramInt, localSoundDescription);
        return localSoundDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SoundMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
