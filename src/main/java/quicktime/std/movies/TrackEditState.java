
package quicktime.std.movies;

import quicktime.QTException;
import quicktime.QTObject;


public final class TrackEditState extends QTObject {
    private Track t;

    TrackEditState(Track paramTrack, int paramInt) throws QTException {
        super(paramInt);
        this.t = paramTrack;
    }
}
