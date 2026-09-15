package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
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
 * QuickTime Components::SGChannel
 */
public final class SGSoundChannel extends AudioChannel implements QuickTimeLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SGSoundChannel.class);
                    }
                });
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
     * QuickTime::SGNewChannel
     * @param owner specifies the sequence grabber component the channel is attached to.
     */
    public SGSoundChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1936684398);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SoundDescription(0);
    }

    /**
     * This method allows you to retrieve a music sample description from the SGChannel.
     * QuickTime::SGGetChannelSampleDescription()
     * @return a SoundDescription object.
     * @return {@link quicktime.QTException}
     */
    public final SoundDescription getSoundDescription() throws QTException {
        return (SoundDescription) getSampleDescription();
    }

    /**
     * Set some parameters that relate to sound recording.
     * QuickTime::SGSetSoundInputParameters
     * @param sampleSize Specifies the number of bits in each sound sample.
     *  		Set this field to 8 for 8-bit sound; set it to 16 for 16-bit sound..
     * @param numChannels Specifies the number of sound channels used by the sound sample.
     * 		Set this field to 1 for monaural sounds; set it to 2 for stereo sounds...
     * @param compressionType Specifies the number of bits in each sound sample.
     * 		Set this field to 8 for 8-bit sound; set it to 16 for 16-bit sound..
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSoundInputParameters(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = SGSetSoundInputParameters(_ID(), (short) paramInt1, (short) paramInt2, paramInt3);
        StdQTException.checkError(i);
    }

    /**
     * Get some parameters that relate to the current sound recording.
     * QuickTime::SGGetSoundInputParameters
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
     * QuickTime::SGSetSoundInputRate
     * @param rate Specifies the rate at which the sound channel is to acquire data.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setSoundInputRate(float paramFloat) throws StdQTException {
        int i = SGSetSoundInputRate(_ID(), QTUtils.X2UFix(paramFloat));
        StdQTException.checkError(i);
    }

    /**
     * Get the Sound Input Rate
     * QuickTime::SGGetSoundInputRate
     */
    public float getSoundInputRate() throws StdQTException {
        int i = SGGetSoundInputRate(_ID());
        return QTUtils.UFix2X(i);
    }

    /**
     * Get the Sound Input Driver.
     * QuickTime::SGGetSoundInputDriver
     * @return the Sound Input driver in use by this channel.
     * @return {@link quicktime.std.StdQTException}
     */
    public SPBDevice getInputDriver() throws StdQTException {
        return SPBDevice.fromSoundChannel(this);
    }

    /**
     * Set the buffer size for sound recording in seconds
     * QuickTime::SGSetSoundRecordChunkSize
     * @param seconds the buffer size for the channel to use.
     * @param {@link quicktime.std.StdQTException}
     */
    public void setRecordChunkSize(int paramInt) throws StdQTException {
        int i = SGSetSoundRecordChunkSize(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    /**
     * Get the buffer size (in seconds) of the record buffer
     * QuickTime::SGGetSoundRecordChunkSize
     * @return the Sound Input driver in use by this channel.
     * @return {@link quicktime.std.StdQTException}
     */
    public int getRecordChunkSize() throws StdQTException {
        int i = SGGetSoundRecordChunkSize(_ID());
        return i;
    }

    /**
     * Set the Sound Input Driver, Automatically calls SGSoundInputDriverChanged
     * QuickTime::SGSetSoundInputDriver
     * QuickTime::SGSoundInputDriverChanged
     * @return the Sound Input driver in use by this channel.
     * @return {@link quicktime.std.StdQTException}
     */
    public void setInputDriver(String paramString) throws StdQTException {
        int i = SGSetSoundInputDriver(_ID(), QTUtils.String2PString(paramString, 255));
        StdQTException.checkError(i);
        i = SGSoundInputDriverChanged(_ID());
        StdQTException.checkError(i);
    }

    private static native int SGSetSoundInputParameters(int paramInt1, short paramShort1, short paramShort2, int paramInt2);

    private static native int SGGetSoundInputParameters(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2, int[] paramArrayOfInt);

    private static native int SGSetSoundInputRate(int paramInt1, int paramInt2);

    private static native int SGGetSoundInputRate(int paramInt);

    private static native int SGSetSoundInputDriver(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSoundInputDriverChanged(int paramInt);

    private static native int SGSetSoundRecordChunkSize(int paramInt1, int paramInt2);

    private static native int SGGetSoundRecordChunkSize(int paramInt);
}

