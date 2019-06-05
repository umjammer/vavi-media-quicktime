
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

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

    public float getStatistics() {
        return QTUtils.Fix2X(VideoMediaGetStatistics(_ID()));
    }

    public void resetStatistics() throws StdQTException {
        StdQTException.checkError(VideoMediaResetStatistics(_ID()));
    }

    public int getStallCount() throws StdQTException {
        int[] arrayOfInt = {
            0
        };
        StdQTException.checkError(VideoMediaGetStallCount(_ID(), arrayOfInt));

        return arrayOfInt[0];
    }

    private static native int VideoMediaResetStatistics(int paramInt);

    private static native int VideoMediaGetStatistics(int paramInt);

    private static native int VideoMediaGetStallCount(int paramInt, int[] paramArrayOfInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/VideoMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
