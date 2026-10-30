package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;

/**
 * The GenericMedia class provides a base from which to construct media classes
 * that represent media types that are unknown to the standard QT distribution.
 * The subclasses would define a public constructors that call their collorary
 * superclass constructors, but internally passing on the media type, as by definition
 * the media type is encapsulated by the particular subclass and does not need to be
 * provided by the user.
 *
 * The fromType call in the Media class will return an appropriate object of a type
 * that corresponds with the mediaType if the application has made this association known
 * to the GenericMedia class. An application does this by calling the addMediaType method
 * with the mediaType and full class name of the associated class. If a media type is
 * encountered within a QT movie that is unknown at runtime then it will return an instance
 * of GenericMedia. This has no defined public calls (except the getMediaType call)
 * but enables general calls that can be done on any media type.
 * You can only subclass GenericMedia by defining your class to be in the same package
 * as the GenericMedia class.
 */
public class GenericMedia extends Media {

    private int mediaType;

    protected GenericMedia(Track track, int timeScale, DataRef dataRef, int mediaType) throws QTException {
        super(track, mediaType, timeScale, dataRef);
        this.mediaType = mediaType;
    }

    protected GenericMedia(Track track, int timeScale, int mediaType) throws QTException {
        this(track, timeScale, null, mediaType);
    }

    protected GenericMedia(Integer integer) throws QTException {
        this(integer.intValue());
    }

    GenericMedia(int nativeRep) throws QTException {
        super(null, nativeRep);
    }

    void setIDs(int mediaType, Track track) {
        this.track = track;
        this.mediaType = mediaType;
    }

    /**
     * Returns the type of media that this Media object represets.
     */
    public final int getMediaType() {
        return this.mediaType;
    }

    @Override
    protected SampleDescription makeDescription() throws QTException {
        return new SampleDescription(this.mediaType);
    }

    @Override
    protected MediaHandler makeHandler(int nativeRep, Object ref) throws StdQTException {
        return new MediaHandler(nativeRep, ref);
    }
}
