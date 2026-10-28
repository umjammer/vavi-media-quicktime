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
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public TimeCodeMedia(Track itsTrack, int timeScale, DataRef dataRef) throws QTException {
        super(itsTrack, 1953325924, timeScale, dataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public TimeCodeMedia(Track itsTrack, int timeScale) throws QTException {
        this(itsTrack, timeScale, null);
    }

    TimeCodeMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new TimeCodeDescription();
    }

    @Override
    MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return TimeCoder.fromMedia(this);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaHandler()"
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     */
    public TimeCoder getTimeCodeHandler() throws StdQTException {
        return TimeCoder.fromMedia(this);
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaSampleDescription()"
     * @param index Specifies the index of the image description to retrieve.
     * @return an TimeCodeDescription object for the specified index.
     * @throws quicktime.QTException
     */
    public TimeCodeDescription getTimeCodeDescription(int index) throws QTException {
        TimeCodeDescription localTimeCodeDescription = new TimeCodeDescription();
        getNativeSampleDescription(index, localTimeCodeDescription);
        return localTimeCodeDescription;
    }
}
