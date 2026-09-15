package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;
import quicktime.std.qtcomponents.TimeCodeDescription;
import quicktime.std.qtcomponents.TimeCoder;

/**
 * The TimeCodeMedia class repesents time code media in QuickTime.
 */
public final class TimeCodeMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public TimeCodeMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1953325924, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public TimeCodeMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    TimeCodeMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new TimeCodeDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return TimeCoder.fromMedia(this);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * QuickTime::GetMediaHandler()
     * @return a MediaHandler object for the media's media handler.
     * @return {@link quicktime.std.StdQTException}
     */
    public TimeCoder getTimeCodeHandler() throws StdQTException {
        return TimeCoder.fromMedia(this);
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     * QuickTime::GetMediaSampleDescription()
     * @param index Specifies the index of the image description to retrieve.
     * @return an TimeCodeDescription object for the specified index.
     * @return {@link quicktime.QTException}
     */
    public TimeCodeDescription getTimeCodeDescription(int paramInt) throws QTException {
        TimeCodeDescription localTimeCodeDescription = new TimeCodeDescription();
        getNativeSampleDescription(paramInt, localTimeCodeDescription);
        return localTimeCodeDescription;
    }
}

