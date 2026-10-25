package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;

public class VideoMediaHandler extends VisualMediaHandler implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(VideoMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    protected VideoMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * The GetVideoMediaStatistics returns the average frame rate since the last time
     * ResetVideoMediaStatistics was called. Because of sampling errors, the values
     * returned from GetVideoMediaStatistics are accurate only after waiting at least
     * one second after calling ResetVideoMediaStatistics.
     * QuickTime::VideoMediaGetStatistics
     * @return the movieÕs play-back frame rate in frames per second.
     */
    public float getStatistics() {
        return QTUtils.Fix2X(VideoMediaGetStatistics(_ID()));
    }

    /**
     * Use the ResetVideoMediaStatistics function to reset the video media handlerÕs
     * counters before using GetVideoMediaStatistics to determine the frame rate of a
     * movie. This call can only be used on video or MPEG media handlers.
     * QuickTime::VideoMediaResetStatistics
     */
    public void resetStatistics() throws StdQTException {
        StdQTException.checkError(VideoMediaResetStatistics(_ID()));
    }

    /**
     * No QTDocumentation.
     * QuickTime::VideoMediaGetStallCount
     * @return the stall count.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getStallCount() throws StdQTException {
        int[] arrayOfInt = { 0 };
        StdQTException.checkError(VideoMediaGetStallCount(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    private static int VideoMediaResetStatistics(int paramInt) {
        return MediaHandlerManager.VideoMediaResetStatistics(paramInt);
    }

    private static int VideoMediaGetStatistics(int paramInt) {
        return MediaHandlerManager.VideoMediaGetStatistics(paramInt);
    }

    private static int VideoMediaGetStallCount(int paramInt, int[] paramArrayOfInt) {
        return MediaHandlerManager.VideoMediaGetStallCount(paramInt, paramArrayOfInt);
    }
}

