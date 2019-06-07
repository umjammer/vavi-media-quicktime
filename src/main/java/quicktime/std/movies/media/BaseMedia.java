package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The BaseMedia class repesents base media in QuickTime.
 */
public final class BaseMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public BaseMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1735291491, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public BaseMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    BaseMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1735291491);
    }

    MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MediaHandler(paramInt, paramObject);
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/BaseMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
