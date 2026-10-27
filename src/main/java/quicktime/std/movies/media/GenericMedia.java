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

    protected GenericMedia(Track paramTrack, int paramInt1, DataRef paramDataRef, int paramInt2) throws QTException {
        super(paramTrack, paramInt2, paramInt1, paramDataRef);
        this.mediaType = paramInt2;
    }

    protected GenericMedia(Track paramTrack, int paramInt1, int paramInt2) throws QTException {
        this(paramTrack, paramInt1, null, paramInt2);
    }

    protected GenericMedia(Integer paramInteger) throws QTException {
        this(paramInteger.intValue());
    }

    GenericMedia(int paramInt) throws QTException {
        super(null, paramInt);
    }

    void setIDs(int paramInt, Track paramTrack) {
        this.track = paramTrack;
        this.mediaType = paramInt;
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
    protected MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return new MediaHandler(paramInt, paramObject);
    }
}

