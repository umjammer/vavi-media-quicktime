package quicktime.std.sg;

import quicktime.QTException;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.MusicDescription;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.music.ToneDescription;


/**
 * Assists in Creating a MIDI Channel.
 *
 * @see "QuickTime Components::SGChannel"
 */
public final class SGMusicChannel extends AudioChannel implements QuickTimeLib {

    SGMusicChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
    }

    /**
     * Creates a sequence grabber music channel and assigns a channel component to the channel.
     * The channel component is responsible for providing music data to the
     * sequence grabber component.
     *
     * @param owner specifies the sequence grabber component the channel is attached to.
     * @see "QuickTime::SGNewChannel"
     */
    public SGMusicChannel(SequenceGrabber owner) throws StdQTException {
        super(owner, 1836413801);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new MusicDescription();
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     *
     * @return a MusicDescription object.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetChannelSampleDescription()"
     */
    public MusicDescription getMusicDescription() throws QTException {
        return (MusicDescription) getSampleDescription();
    }

    /**
     * Gets the Instrument associates with a particular channel.
     *
     * @see "QuickTime::SGGetInstrument"
     */
    public ToneDescription getInstrument() throws StdQTException {
        ToneDescription instrument = new ToneDescription();
        int i = SGGetInstrument(_ID(), instrument.getBytes());
        StdQTException.checkError(i);
        return instrument;
    }

    /**
     * Sets the Instrument associates with a particular channel.
     *
     * @param td specifies the instrument to set the channel to.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetInstrument"
     */
    public void setInstrument(ToneDescription td) throws StdQTException {
        int i = SGSetInstrument(_ID(), td.getBytes());
        StdQTException.checkError(i);
    }

    private static int SGGetInstrument(int ref, byte[] tone) {
        return SequenceGrabberManager.SGGetInstrument(ref, tone);
    }

    private static int SGSetInstrument(int ref, byte[] tone) {
        return SequenceGrabberManager.SGSetInstrument(ref, tone);
    }
}
