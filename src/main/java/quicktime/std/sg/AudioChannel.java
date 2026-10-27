package quicktime.std.sg;

import quicktime.internal.toolbox.SequenceGrabberManager;
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

    AudioChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    AudioChannel(SequenceGrabber paramSequenceGrabber, int paramInt) throws StdQTException {
        super(paramSequenceGrabber, paramInt);
    }

    /**
     * Allows you to set a channel's sound volume setting.
     * @see "QuickTime::SGSetChannelVolume"
     * @param volume the new volume setting
     * @throws quicktime.std.StdQTException
     */
    public void setVolume(float volume) throws StdQTException {
        StdQTException.checkError(SGSetChannelVolume(_ID(), QTUtils.X2ShortFix(volume)));
    }

    /**
     * Allows you to determine a channel's sound volume setting.
     * @see "QuickTime::SGGetChannelVolume"
     * @return the current volume setting
     * @throws quicktime.std.StdQTException
     */
    public float getVolume() throws StdQTException {
        short[] arrayOfShort = { 0 };
        StdQTException.checkError(SGGetChannelVolume(_ID(), arrayOfShort));
        return QTUtils.ShortFix2X(arrayOfShort[0]);
    }

    private static int SGSetChannelVolume(int paramInt, short paramShort) {
        return SequenceGrabberManager.SGSetChannelVolume(paramInt, paramShort);
    }

    private static int SGGetChannelVolume(int paramInt, short[] paramArrayOfShort) {
        return SequenceGrabberManager.SGGetChannelVolume(paramInt, paramArrayOfShort);
    }
}
