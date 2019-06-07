package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;

/**
 * Assists in importing sound into a movie.
 * The SGChannel implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 * Refer to SGChannel in QuickTime Components
 */
public abstract class AudioChannel extends SGChannel implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(AudioChannel.class);
                    }
                });
            }
        }.establish();
    }

    AudioChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    AudioChannel(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        super(paramSequenceGrabber, paramInt);
    }

    /**
     * Allows you to set a channel's sound volume setting.
     * QuickTime::SGSetChannelVolume
     * @param volume the new volume setting
     * @param {@link quicktime.std.StdQTException}
     */
    public void setVolume(float paramFloat) throws StdQTException {
        StdQTException.checkError(SGSetChannelVolume(_ID(), QTUtils.X2ShortFix(paramFloat)));
    }

    /**
     * Allows you to determine a channel's sound volume setting.
     * QuickTime::SGGetChannelVolume
     * @return the current volume setting
     * @return {@link quicktime.std.StdQTException}
     */
    public float getVolume() throws StdQTException {
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(SGGetChannelVolume(_ID(), arrayOfShort));
        return QTUtils.ShortFix2X(arrayOfShort[0]);
    }

    private static native int SGSetChannelVolume(int paramInt, short paramShort);

    private static native int SGGetChannelVolume(int paramInt, short[] paramArrayOfShort);
}
