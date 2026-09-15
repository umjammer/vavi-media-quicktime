package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The MPEGMedia class repesents base media in QuickTime.
 */
public final class MPEGMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public MPEGMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1297106247, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public MPEGMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    MPEGMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1297106247);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MPEGMediaHandler(paramInt, paramObject);
    }
}

