
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class SpriteMedia extends Media {
    public SpriteMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1936749172, paramInt, paramDataRef);
    }

    public SpriteMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    SpriteMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SpriteDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new SpriteMediaHandler(paramInt, paramObject);
    }

    public SpriteMediaHandler getSpriteHandler() throws StdQTException {
        return new SpriteMediaHandler(getNativeMediaHandler(), this);
    }

    /**
     * @deprecated
     */
    public SpriteDescription getSoundDescription(int paramInt) throws QTException {
        SpriteDescription localSpriteDescription = new SpriteDescription();
        getNativeSampleDescription(paramInt, localSpriteDescription);
        return localSpriteDescription;
    }

    public SpriteDescription getSpriteDescription(int paramInt) throws QTException {
        SpriteDescription localSpriteDescription = new SpriteDescription();
        getNativeSampleDescription(paramInt, localSpriteDescription);
        return localSpriteDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SpriteMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
