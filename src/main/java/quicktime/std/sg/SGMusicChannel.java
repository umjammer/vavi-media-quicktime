package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.MusicDescription;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.music.ToneDescription;

/**
 * Assists in Creating a MIDI Channel.
 * @see "QuickTime Components::SGChannel"
 */
public final class SGMusicChannel extends AudioChannel implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SGMusicChannel.class));
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
     * @see "QuickTime::SGNewChannel"
     * @param owner specifies the sequence grabber component the channel is attached to.
     */
    public SGMusicChannel(SequenceGrabber owner) throws StdQTException {
        super(owner, 1836413801);
    }

    @Override
    final SampleDescription makeDescription() throws QTException {
        return new MusicDescription();
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     * @see "QuickTime::SGGetChannelSampleDescription()"
     * @return a MusicDescription object.
     * @throws quicktime.QTException
     */
    public final MusicDescription getMusicDescription() throws QTException {
        return (MusicDescription) getSampleDescription();
    }

    /**
     * Gets the Instrument associates with a particular channel.
     * @see "QuickTime::SGGetInstrument"
     */
    public ToneDescription getInstrument() throws StdQTException {
        ToneDescription localToneDescription = new ToneDescription();
        int i = SGGetInstrument(_ID(), localToneDescription.getBytes());
        StdQTException.checkError(i);
        return localToneDescription;
    }

    /**
     * Sets the Instrument associates with a particular channel.
     * @see "QuickTime::SGSetInstrument"
     * @param td specifies the instrument to set the channel to.
     * @throws quicktime.std.StdQTException
     */
    public void setInstrument(ToneDescription td) throws StdQTException {
        int i = SGSetInstrument(_ID(), td.getBytes());
        StdQTException.checkError(i);
    }

    private static int SGGetInstrument(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGGetInstrument(paramInt, paramArrayOfByte);
    }

    private static int SGSetInstrument(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetInstrument(paramInt, paramArrayOfByte);
    }
}
