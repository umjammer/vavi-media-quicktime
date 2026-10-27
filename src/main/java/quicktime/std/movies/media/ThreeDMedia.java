package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The ThreeDMedia class repesents base media in QuickTime.
 */
public final class ThreeDMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public ThreeDMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1902392164, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public ThreeDMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    ThreeDMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    @Override
    final SampleDescription makeDescription() throws QTException {
        return new ThreeDDescription(1902392164);
    }

    @Override
    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new ThreeDMediaHandler(paramInt, paramObject);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaHandler()"
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     */
    public ThreeDMediaHandler getThreeDHandler() throws StdQTException {
        return new ThreeDMediaHandler(getNativeMediaHandler(), this);
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaSampleDescription()"
     * @param index Specifies the index of the image description to retrieve.
     * @return an ThreeDDescription object for the specified index.
     * @throws quicktime.QTException
     */
    public ThreeDDescription getThreeDDescription(int paramInt) throws QTException {
        ThreeDDescription localThreeDDescription = new ThreeDDescription(1902392164);
        getNativeSampleDescription(paramInt, localThreeDDescription);
        return localThreeDDescription;
    }
}

