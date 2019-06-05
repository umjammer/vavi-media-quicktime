
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public class GenericMedia extends Media {
    private int mediaType;

    protected GenericMedia(Track paramTrack, int paramInt1, DataRef paramDataRef, int paramInt2) throws QTException {
        super(paramTrack, paramInt2, paramInt1, paramDataRef);
        this.mediaType = paramInt2;
    }

    protected GenericMedia(Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        this(paramTrack, paramInt1, null, paramInt2);
    }

    protected GenericMedia(Integer paramInteger) throws QTException {
        this(paramInteger.intValue());
    }

    GenericMedia(int paramInt) throws QTException {
        super(null, paramInt);
    }

    void setIDs(int paramInt, Track paramTrack) {
        this.track = paramTrack;
        this.mediaType = paramInt;
    }

    public final int getMediaType() {
        return this.mediaType;
    }

    protected SampleDescription makeDescription() throws QTException {
        return new SampleDescription(this.mediaType);
    }

    protected MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MediaHandler(paramInt, paramObject);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/GenericMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
