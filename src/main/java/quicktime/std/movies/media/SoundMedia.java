package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The SoundMedia class repesents sound media in QuickTime.
 */
public final class SoundMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public SoundMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1936684398, paramInt, paramDataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * QuickTime::NewTrackMedia()
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public SoundMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    SoundMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SoundDescription(0);
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new SoundMediaHandler(paramInt, paramObject);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     * QuickTime::GetMediaHandler()
     * @return a MediaHandler object for the media's media handler.
     * @return {@link quicktime.std.StdQTException}
     */
    public SoundMediaHandler getSoundHandler() throws StdQTException {
        return new SoundMediaHandler(getNativeMediaHandler(), this);
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     * QuickTime::GetMediaSampleDescription()
     * @param index Specifies the index of the description to retrieve.
     * @return a SoundDescription object for the specified index.
     * @return {@link quicktime.QTException}
     */
    public SoundDescription getSoundDescription(int paramInt) throws QTException {
        SoundDescription localSoundDescription = new SoundDescription(0);
        getNativeSampleDescription(paramInt, localSoundDescription);
        return localSoundDescription;
    }
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/SoundMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
