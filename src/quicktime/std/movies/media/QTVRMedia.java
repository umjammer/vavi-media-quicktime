
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


public final class QTVRMedia extends GenericMedia {
    public QTVRMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, paramInt, paramDataRef, 1903457906);
    }

    public QTVRMedia(Track paramTrack, int paramInt) throws QTException {
        super(paramTrack, paramInt, 1903457906);
    }

    public static void registerQTVRMedia() {
        GenericMedia.addMediaType(1903457906, "quicktime.std.movies.media.QTVRMedia");
    }

    protected SampleDescription makeDescription() throws QTException {
        return new QTVRSampleDescription();
    }

    protected MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new QTVRMediaHandler(paramInt, paramObject);
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/QTVRMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
