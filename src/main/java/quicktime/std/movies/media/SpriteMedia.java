package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;


/**
 * The SpriteMedia class repesents sprite media in QuickTime.
 */
public final class SpriteMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     *
     * @param itsTrack  Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef   a DataRef object specifying the default data reference for this media.
     * @see "QuickTime::NewTrackMedia()"
     */
    public SpriteMedia(Track itsTrack, int timeScale, DataRef dataRef) throws QTException {
        super(itsTrack, 1936749172, timeScale, dataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     *
     * @param itsTrack  Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @see "QuickTime::NewTrackMedia()"
     */
    public SpriteMedia(Track itsTrack, int timeScale) throws QTException {
        this(itsTrack, timeScale, null);
    }

    SpriteMedia(int nativeRep, Track track) throws QTException {
        super(track, nativeRep);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new SpriteDescription();
    }

    @Override
    MediaHandler makeHandler(int nativeRep, Object ref) throws StdQTException {
        return new SpriteMediaHandler(nativeRep, ref);
    }

    /**
     * This method allows you to obtain a reference to a media handler component.
     * This returns the handler that is the appropriate class for this particular media.
     *
     * @return a MediaHandler object for the media's media handler.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::GetMediaHandler()"
     */
    public SpriteMediaHandler getSpriteHandler() throws StdQTException {
        return new SpriteMediaHandler(getNativeMediaHandler(), this);
    }

    /**
     * @see #getSpriteDescription
     * @deprecated
     */
    public SpriteDescription getSoundDescription(int index) throws QTException {
        SpriteDescription soundDescription = new SpriteDescription();
        getNativeSampleDescription(index, soundDescription);
        return soundDescription;
    }

    /**
     * This method allows you to retrieve a sample description from a media.
     * This returns the description that is the appropriate class for this particular media.
     *
     * @param index Specifies the index of the description to retrieve.
     * @return a SpriteDescription object for the specified index.
     * @throws quicktime.QTException
     * @see "QuickTime::GetMediaSampleDescription()"
     */
    public SpriteDescription getSpriteDescription(int index) throws QTException {
        SpriteDescription spriteDescription = new SpriteDescription();
        getNativeSampleDescription(index, spriteDescription);
        return spriteDescription;
    }
}
