package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * Represents VR Media - this is an example of how to subclass GenericMedia
 * to return a Media object of a subclass that corresponds to the Media Type.
 */
public final class QTVRMedia extends GenericMedia {

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     * @param dataRef a DataRef object specifying the default data reference for this media.
     */
    public QTVRMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, paramInt, paramDataRef, 1903457906);
    }

    /**
     * This constructor creates a media struct for the specified Track object.
     * @see "QuickTime::NewTrackMedia()"
     * @param itsTrack Specifies the Track object this media belongs to.
     * @param timeScale Specifies the time scale of the new media.
     */
    public QTVRMedia(Track paramTrack, int paramInt) throws QTException {
        super(paramTrack, paramInt, 1903457906);
    }

    public static void registerQTVRMedia() {
        GenericMedia.addMediaType(1903457906, "quicktime.std.movies.media.QTVRMedia");
    }

    @Override
    protected SampleDescription makeDescription() throws QTException {
        return new QTVRSampleDescription();
    }

    @Override
    protected MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new QTVRMediaHandler(paramInt, paramObject);
    }
}

