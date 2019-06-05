
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


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

    public void setVolume(float paramFloat) throws StdQTException {
        StdQTException.checkError(SGSetChannelVolume(_ID(), QTUtils.X2ShortFix(paramFloat)));
    }

    public float getVolume() throws StdQTException {
        short[] arrayOfShort = {
            0
        };
        StdQTException.checkError(SGGetChannelVolume(_ID(), arrayOfShort));

        return QTUtils.ShortFix2X(arrayOfShort[0]);
    }

    private static native int SGSetChannelVolume(int paramInt, short paramShort);

    private static native int SGGetChannelVolume(int paramInt, short[] paramArrayOfShort);
}
