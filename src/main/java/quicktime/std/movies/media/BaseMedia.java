package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The BaseMedia class represents base media in QuickTime.
 */
public final class BaseMedia extends Media {

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public BaseMedia(Track itsTrack, int timeScale, DataRef dataRef) throws QTException {
        super(itsTrack, 1735291491, timeScale, dataRef);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public BaseMedia(Track itsTrack, int timeScale) throws QTException {
        this(itsTrack, timeScale, null);
    }

    BaseMedia(int nativeRep, Track track) throws QTException {
        super(track, nativeRep);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new SampleDescription(1735291491);
    }

    @Override
    MediaHandler makeHandler(int nativeRep, Object ref) throws StdQTException {
        return new MediaHandler(nativeRep, ref);
    }
}
