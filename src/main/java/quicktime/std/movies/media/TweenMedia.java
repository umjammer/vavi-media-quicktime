package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The TweenMedia class repesents base media in QuickTime.
 */
public final class TweenMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public TweenMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1953981806, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public TweenMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    TweenMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    @Override
    final SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1953981806);
    }

    @Override
    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MediaHandler(paramInt, paramObject);
    }
}

