package quicktime.std.sg;

import quicktime.QTException;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.sound.SPBDevice;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.SoundDescription;
import quicktime.util.QTUtils;


/**
 * Assists in importing sound into a movie.
 * The SGChannel implements methods for a corresponding QuickTime
 * ComponentInstance structure.
 *
 * @see "QuickTime Components::SGChannel"
 */
public final class SGSoundChannel extends AudioChannel implements QuickTimeLib {

    SGSoundChannel(int type, SequenceGrabber owner) throws StdQTException {
        super(type, owner);
    }

    /**
     * Creates a sequence grabber channel and assigns a channel component to the channel.
     * The channel component is responsible for providing digitized data to the
     * sequence grabber component.
     *
     * @param owner specifies the sequence grabber component the channel is attached to.
     * @see "QuickTime::SGNewChannel"
     */
    public SGSoundChannel(SequenceGrabber owner) throws StdQTException {
        super(owner, 1936684398);
    }

    @Override
    SampleDescription makeDescription() throws QTException {
        return new SoundDescription(0);
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     *
     * @return a SoundDescription object.
     * @throws quicktime.QTException
     * @see "QuickTime::SGGetChannelSampleDescription()"
     */
    public SoundDescription getSoundDescription() throws QTException {
        return (SoundDescription) getSampleDescription();
    }

    /**
     * Set some parameters that relate to sound recording.
     *
     * @param sampleSize      Specifies the number of bits in each sound sample.
     *                        Set this field to 8 for 8-bit sound; set it to 16 for 16-bit sound..
     * @param numChannels     Specifies the number of sound channels used by the sound sample.
     *                        Set this field to 1 for monaural sounds; set it to 2 for stereo sounds...
     * @param compressionType Specifies the number of bits in each sound sample.
     *                        Set this field to 8 for 8-bit sound; set it to 16 for 16-bit sound..
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetSoundInputParameters"
     */
    public void setSoundInputParameters(int sampleSize, int numChannels, int compressionType) throws StdQTException {
        int i = SGSetSoundInputParameters(_ID(), (short) sampleSize, (short) numChannels, compressionType);
        StdQTException.checkError(i);
    }

    /**
     * Get some parameters that relate to the current sound recording.
     *
     * @see "QuickTime::SGGetSoundInputParameters"
     */
    public SGChannelInfo getSoundInputParameters() throws StdQTException {
        short[] sampleSize = new short[1];
        short[] numChannels = new short[1];
        int[] compressionType = new int[1];
        int i = SGGetSoundInputParameters(_ID(), sampleSize, numChannels, compressionType);
        StdQTException.checkError(i);
        return new SGChannelInfo(sampleSize[0], numChannels[0], compressionType[0]);
    }

    /**
     * Set the Sound Input Rate
     *
     * @param rate Specifies the rate at which the sound channel is to acquire data.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetSoundInputRate"
     */
    public void setSoundInputRate(float rate) throws StdQTException {
        int i = SGSetSoundInputRate(_ID(), QTUtils.X2UFix(rate));
        StdQTException.checkError(i);
    }

    /**
     * Get the Sound Input Rate
     *
     * @see "QuickTime::SGGetSoundInputRate"
     */
    public float getSoundInputRate() throws StdQTException {
        int i = SGGetSoundInputRate(_ID());
        return QTUtils.UFix2X(i);
    }

    /**
     * Get the Sound Input Driver.
     *
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetSoundInputDriver"
     */
    public SPBDevice getInputDriver() throws StdQTException {
        return SPBDevice.fromSoundChannel(this);
    }

    /**
     * Set the buffer size for sound recording in seconds
     *
     * @param seconds the buffer size for the channel to use.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetSoundRecordChunkSize"
     */
    public void setRecordChunkSize(int seconds) throws StdQTException {
        int i = SGSetSoundRecordChunkSize(_ID(), seconds);
        StdQTException.checkError(i);
    }

    /**
     * Get the buffer size (in seconds) of the record buffer
     *
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGGetSoundRecordChunkSize"
     */
    public int getRecordChunkSize() throws StdQTException {
        int i = SGGetSoundRecordChunkSize(_ID());
        return i;
    }

    /**
     * Set the Sound Input Driver, Automatically calls SGSoundInputDriverChanged
     *
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::SGSetSoundInputDriver"
     * @see "QuickTime::SGSoundInputDriverChanged"
     */
    public void setInputDriver(String inputDriver) throws StdQTException {
        int i = SGSetSoundInputDriver(_ID(), QTUtils.String2PString(inputDriver, 255));
        StdQTException.checkError(i);
        i = SGSoundInputDriverChanged(_ID());
        StdQTException.checkError(i);
    }

    private static int SGSetSoundInputParameters(int ref, short sampleSize, short numChannels, int compressionType) {
        return SequenceGrabberManager.SGSetSoundInputParameters(ref, sampleSize, numChannels, compressionType);
    }

    private static int SGGetSoundInputParameters(int ref, short[] sampleSize, short[] numChannels, int[] compressionType) {
        return SequenceGrabberManager.SGGetSoundInputParameters(ref, sampleSize, numChannels, compressionType);
    }

    private static int SGSetSoundInputRate(int ref, int rate) {
        return SequenceGrabberManager.SGSetSoundInputRate(ref, rate);
    }

    private static int SGGetSoundInputRate(int ref) {
        return SequenceGrabberManager.SGGetSoundInputRate(ref);
    }

    private static int SGSetSoundInputDriver(int ref, byte[] name) {
        return SequenceGrabberManager.SGSetSoundInputDriver(ref, name);
    }

    private static int SGSoundInputDriverChanged(int ref) {
        return SequenceGrabberManager.SGSoundInputDriverChanged(ref);
    }

    private static int SGSetSoundRecordChunkSize(int ref, int seconds) {
        return SequenceGrabberManager.SGSetSoundRecordChunkSize(ref, seconds);
    }

    private static int SGGetSoundRecordChunkSize(int ref) {
        return SequenceGrabberManager.SGGetSoundRecordChunkSize(ref);
    }
}
