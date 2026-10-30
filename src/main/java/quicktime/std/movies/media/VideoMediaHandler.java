package quicktime.std.movies.media;

import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


public class VideoMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    protected VideoMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * The GetVideoMediaStatistics returns the average frame rate since the last time
     * ResetVideoMediaStatistics was called. Because of sampling errors, the values
     * returned from GetVideoMediaStatistics are accurate only after waiting at least
     * one second after calling ResetVideoMediaStatistics.
     * @see "QuickTime::VideoMediaGetStatistics"
     * @return the movieÕs play-back frame rate in frames per second.
     */
    public float getStatistics() {
        return QTUtils.Fix2X(VideoMediaGetStatistics(_ID()));
    }

    /**
     * Use the ResetVideoMediaStatistics function to reset the video media handlerÕs
     * counters before using GetVideoMediaStatistics to determine the frame rate of a
     * movie. This call can only be used on video or MPEG media handlers.
     * @see "QuickTime::VideoMediaResetStatistics"
     */
    public void resetStatistics() throws StdQTException {
        StdQTException.checkError(VideoMediaResetStatistics(_ID()));
    }

    /**
     * No QTDocumentation.
     * @see "QuickTime::VideoMediaGetStallCount"
     * @return the stall count.
     * @throws quicktime.std.StdQTException
     */
    public int getStallCount() throws StdQTException {
        int[] stalls = { 0 };
        StdQTException.checkError(VideoMediaGetStallCount(_ID(), stalls));
        return stalls[0];
    }

    private static int VideoMediaResetStatistics(int mh) {
        return MediaHandlerManager.VideoMediaResetStatistics(mh);
    }

    private static int VideoMediaGetStatistics(int mh) {
        return MediaHandlerManager.VideoMediaGetStatistics(mh);
    }

    private static int VideoMediaGetStallCount(int mh, int[] stalls) {
        return MediaHandlerManager.VideoMediaGetStallCount(mh, stalls);
    }
}
