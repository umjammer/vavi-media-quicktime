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

    AudioChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
    }

    AudioChannel(SequenceGrabber owner, int channelType) throws StdQTException {
        super(owner, channelType);
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
        short[] volume = { 0 };
        StdQTException.checkError(SGGetChannelVolume(_ID(), volume));
        return QTUtils.ShortFix2X(volume[0]);
    }

    private static int SGSetChannelVolume(int ref, short volume) {
        return SequenceGrabberManager.SGSetChannelVolume(ref, volume);
    }

    private static int SGGetChannelVolume(int ref, short[] volume) {
        return SequenceGrabberManager.SGGetChannelVolume(ref, volume);
    }
}
