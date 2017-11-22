
package quicktime.std.movies.media;

import quicktime.QTException;
import quicktime.std.StdQTException;
import quicktime.std.movies.Track;
import quicktime.std.qtcomponents.TimeCodeDescription;
import quicktime.std.qtcomponents.TimeCoder;


public final class TimeCodeMedia extends Media {
    public TimeCodeMedia(Track paramTrack, int paramInt, DataRef paramDataRef) throws QTException {
        super(paramTrack, 1953325924, paramInt, paramDataRef);
    }

    public TimeCodeMedia(Track paramTrack, int paramInt) throws QTException {
        this(paramTrack, paramInt, null);
    }

    TimeCodeMedia(int paramInt, Track paramTrack) throws QTException {
        super(paramTrack, paramInt);
    }

    final SampleDescription makeDescription() throws QTException {
        return new TimeCodeDescription();
    }

    final MediaHandler makeHandler(int paramInt, Object paramObject) throws StdQTException {
        return TimeCoder.fromMedia(this);
    }

    public TimeCoder getTimeCodeHandler() throws StdQTException {
        return TimeCoder.fromMedia(this);
    }

    public TimeCodeDescription getTimeCodeDescription(int paramInt) throws QTException {
        TimeCodeDescription localTimeCodeDescription = new TimeCodeDescription();
        getNativeSampleDescription(paramInt, localTimeCodeDescription);
        return localTimeCodeDescription;
    }
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/TimeCodeMedia.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
