package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The TextMedia class repesents text media in QuickTime.
 */
public final class TextMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public TextMedia(Track itsTrack, int timeScale, DataRef dataRef) throws QTException {
        super(itsTrack, 1952807028, timeScale, dataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public TextMedia(Track itsTrack, int timeScale) throws QTException {
        this(itsTrack, timeScale, null);
    }

    TextMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    @Override
    final SampleDescription makeDescription() throws QTException {
        return new TextDescription();
    }

    @Override
    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new TextMediaHandler(paramInt, paramObject);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaHandler()"
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     */
    public TextMediaHandler getTextHandler() throws StdQTException {
        return new TextMediaHandler(getNativeMediaHandler(), this);
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaSampleDescription()"
     * @param index Specifies the index of the description to retrieve.
     * @return an TextDescription object for the specified index.
     * @throws quicktime.QTException
     */
    public TextDescription getTextDescription(int index) throws QTException {
        TextDescription localTextDescription = new TextDescription();
        getNativeSampleDescription(index, localTextDescription);
        return localTextDescription;
    }
}

