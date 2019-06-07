package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.MusicDescription;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.music.ToneDescription;

/**
 * Assists in Creating a MIDI Channel.
 * QuickTime Components::SGChannel
 */
public final class SGMusicChannel extends AudioChannel implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SGMusicChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGMusicChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    /**
     * Creates a sequence grabber music channel and assigns a channel component to the channel.
     * The channel component is responsible for providing music data to the
     * sequence grabber component.
     * QuickTime::SGNewChannel
     * @param owner specifies the sequence grabber component the channel is attached to.
     */
    public SGMusicChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1836413801);
    }

    final SampleDescription makeDescription() throws QTException {
        return new MusicDescription();
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     * QuickTime::SGGetChannelSampleDescription()
     * @return a MusicDescription object.
     * @return {@link quicktime.QTException}
     */
    public final MusicDescription getMusicDescription() throws QTException {
        return (MusicDescription) getSampleDescription();
    }

    /**
     * Gets the Instrument associates with a particular channel.
     * QuickTime::SGGetInstrument
     */
    public ToneDescription getInstrument() throws StdQTException {
        ToneDescription localToneDescription = new ToneDescription();
        int i = SGGetInstrument(_ID(), localToneDescription.getBytes());
        StdQTException.checkError(i);
        return localToneDescription;
    }

    /**
     * Sets the Instrument associates with a particular channel.
     * QuickTime::SGSetInstrument
     * @param td specifies the instrument to set the channel to.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setInstrument(ToneDescription paramToneDescription) throws StdQTException {
        int i = SGSetInstrument(_ID(), paramToneDescription.getBytes());
        StdQTException.checkError(i);
    }

    private static native int SGGetInstrument(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetInstrument(int paramInt, byte[] paramArrayOfByte);
}
