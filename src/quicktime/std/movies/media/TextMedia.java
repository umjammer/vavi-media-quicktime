
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class TextMedia extends Media {
    public TextMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1952807028, paramInt, paramDataRef);
    }

    public TextMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    TextMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new TextDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new TextMediaHandler(paramInt, paramObject);
    }

    public TextMediaHandler getTextHandler() throws StdQTException {
        return new TextMediaHandler(getNativeMediaHandler(), this);
    }

    public TextDescription getTextDescription(int paramInt) throws QTException {
        TextDescription localTextDescription = new TextDescription();
        getNativeSampleDescription(paramInt, localTextDescription);
        return localTextDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/TextMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
