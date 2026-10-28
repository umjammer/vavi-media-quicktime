package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.internal.toolbox.SequenceGrabberManager;
import quicktime.jdirect.QTNative;
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
 * @see "QuickTime Components::SGChannel"
 */
public final class SGSoundChannel extends AudioChannel implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SGSoundChannel.class));
            }
        }.establish();
    }

    SGSoundChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    /**
     * Creates a sequence grabber channel and assigns a channel component to the channel.
     * The channel component is responsible for providing digitized data to the
     * sequence grabber component.
     * @see "QuickTime::SGNewChannel"
     * @param owner specifies the sequence grabber component the channel is attached to.
     */
    public SGSoundChannel(SequenceGrabber owner) throws StdQTException {
        super(owner, 1936684398);
    }

    @Override
    final SampleDescription makeDescription() throws QTException {
        return new SoundDescription(0);
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     * @see "QuickTime::SGGetChannelSampleDescription()"
     * @return a SoundDescription object.
     * @throws quicktime.QTException
     */
    public final SoundDescription getSoundDescription() throws QTException {
        return (SoundDescription) getSampleDescription();
    }

    /**
     * Set some parameters that relate to sound recording.
     * @see "QuickTime::SGSetSoundInputParameters"
     * @param sampleSize Specifies the number of bits in each sound sample.
     *    Set this field to 8 for 8-bit sound; set it to 16 for 16-bit sound..
     * @param numChannels Specifies the number of sound channels used by the sound sample.
     *   Set this field to 1 for monaural sounds; set it to 2 for stereo sounds...
     * @param compressionType Specifies the number of bits in each sound sample.
     *   Set this field to 8 for 8-bit sound; set it to 16 for 16-bit sound..
     * @throws quicktime.std.StdQTException
     */
    public void setSoundInputParameters(int sampleSize, int numChannels, int compressionType) throws StdQTException {
        int i = SGSetSoundInputParameters(_ID(), (short) sampleSize, (short) numChannels, compressionType);
        StdQTException.checkError(i);
    }

    /**
     * Get some parameters that relate to the current sound recording.
     * @see "QuickTime::SGGetSoundInputParameters"
     */
    public SGChannelInfo getSoundInputParameters() throws StdQTException {
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];
        int[] arrayOfInt = new int[1];
        int i = SGGetSoundInputParameters(_ID(), arrayOfShort1, arrayOfShort2, arrayOfInt);
        StdQTException.checkError(i);
        return new SGChannelInfo(arrayOfShort1[0], arrayOfShort2[0], arrayOfInt[0]);
    }

    /**
     * Set the Sound Input Rate
     * @see "QuickTime::SGSetSoundInputRate"
     * @param rate Specifies the rate at which the sound channel is to acquire data.
     * @throws quicktime.std.StdQTException
     */
    public void setSoundInputRate(float rate) throws StdQTException {
        int i = SGSetSoundInputRate(_ID(), QTUtils.X2UFix(rate));
        StdQTException.checkError(i);
    }

    /**
     * Get the Sound Input Rate
     * @see "QuickTime::SGGetSoundInputRate"
     */
    public float getSoundInputRate() throws StdQTException {
        int i = SGGetSoundInputRate(_ID());
        return QTUtils.UFix2X(i);
    }

    /**
     * Get the Sound Input Driver.
     * @see "QuickTime::SGGetSoundInputDriver"
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     */
    public SPBDevice getInputDriver() throws StdQTException {
        return SPBDevice.fromSoundChannel(this);
    }

    /**
     * Set the buffer size for sound recording in seconds
     * @see "QuickTime::SGSetSoundRecordChunkSize"
     * @param seconds the buffer size for the channel to use.
     * @throws quicktime.std.StdQTException
     */
    public void setRecordChunkSize(int seconds) throws StdQTException {
        int i = SGSetSoundRecordChunkSize(_ID(), seconds);
        StdQTException.checkError(i);
    }

    /**
     * Get the buffer size (in seconds) of the record buffer
     * @see "QuickTime::SGGetSoundRecordChunkSize"
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     */
    public int getRecordChunkSize() throws StdQTException {
        int i = SGGetSoundRecordChunkSize(_ID());
        return i;
    }

    /**
     * Set the Sound Input Driver, Automatically calls SGSoundInputDriverChanged
     * @see "QuickTime::SGSetSoundInputDriver"
     * @see "QuickTime::SGSoundInputDriverChanged"
     * @return the Sound Input driver in use by this channel.
     * @throws quicktime.std.StdQTException
     */
    public void setInputDriver(String paramString) throws StdQTException {
        int i = SGSetSoundInputDriver(_ID(), QTUtils.String2PString(paramString, 255));
        StdQTException.checkError(i);
        i = SGSoundInputDriverChanged(_ID());
        StdQTException.checkError(i);
    }

    private static int SGSetSoundInputParameters(int paramInt1, short paramShort1, short paramShort2, int paramInt2) {
        return SequenceGrabberManager.SGSetSoundInputParameters(paramInt1, paramShort1, paramShort2, paramInt2);
    }

    private static int SGGetSoundInputParameters(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int[] paramArrayOfInt) {
        return SequenceGrabberManager.SGGetSoundInputParameters(paramInt, paramArrayOfShort1, paramArrayOfShort2, paramArrayOfInt);
    }

    private static int SGSetSoundInputRate(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGSetSoundInputRate(paramInt1, paramInt2);
    }

    private static int SGGetSoundInputRate(int paramInt) {
        return SequenceGrabberManager.SGGetSoundInputRate(paramInt);
    }

    private static int SGSetSoundInputDriver(int paramInt, byte[] paramArrayOfByte) {
        return SequenceGrabberManager.SGSetSoundInputDriver(paramInt, paramArrayOfByte);
    }

    private static int SGSoundInputDriverChanged(int paramInt) {
        return SequenceGrabberManager.SGSoundInputDriverChanged(paramInt);
    }

    private static int SGSetSoundRecordChunkSize(int paramInt1, int paramInt2) {
        return SequenceGrabberManager.SGSetSoundRecordChunkSize(paramInt1, paramInt2);
    }

    private static int SGGetSoundRecordChunkSize(int paramInt) {
        return SequenceGrabberManager.SGGetSoundRecordChunkSize(paramInt);
    }
}

