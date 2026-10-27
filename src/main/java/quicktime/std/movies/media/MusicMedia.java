package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The MusicMedia class repesents music media in QuickTime.
 */
public final class MusicMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public MusicMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1836413801, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public MusicMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    MusicMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    @Override
    final SampleDescription makeDescription() throws QTException {
        return new MusicDescription();
    }

    @Override
    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MusicMediaHandler(paramInt, paramObject);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaHandler()"
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     */
    public MusicMediaHandler getMusicHandler() throws StdQTException {
        return new MusicMediaHandler(getNativeMediaHandler(), this);
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     * @see "QuickTime::GetMediaSampleDescription()"
     * @param index Specifies the index of the description to retrieve.
     * @return a MusicDescription object for the specified index.
     * @throws quicktime.QTException
     */
    public MusicDescription getMusicDescription(int paramInt) throws QTException {
        MusicDescription localMusicDescription = new MusicDescription();
        getNativeSampleDescription(paramInt, localMusicDescription);
        return localMusicDescription;
    }
}

