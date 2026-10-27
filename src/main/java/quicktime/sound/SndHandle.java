package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTHandle;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;

public final class SndHandle extends QTHandle implements QuickTimeLib, SoundLib {

    private static Object linkage;

    private QTPointerRef buf;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SndHandle.class));
            }
        }.establish();
    }

    /**
     * Construct an empty handle that must be initialized by the setupHeader call.
     */
    public SndHandle() throws QTException {
        super(200, false);
    }

    /**
     * Construct an empty handle that must be initialized by the setupHeader call.
     * @param size how many bytes to allocate
     * @param clear if true the handle's memory is cleared to zeroes.
     */
    public SndHandle(int paramInt, boolean paramBoolean) throws QTException {
        super(paramInt, paramBoolean);
    }

    /**
     * Construct a sound resource for containing sampled sound that can be used to record and play sound.
     * You must append the actual sample data to the created SndHandle before it can be used to
     * record or play.
     * @see "QuickTime::SetupSndHeader"
     * @param numChannels the number of channels for the sound
     * @param sampleRate the rate at which the sound was recorded
     * @param sampleSize The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     */
    public SndHandle(int paramInt1, float paramFloat, int paramInt2, int paramInt3) throws QTException {
        this(paramInt1, paramFloat, paramInt2, paramInt3, 60);
    }

    /**
     * Construct a sound resource for containing sampled sound that can be used to record and play sound.
     * You must append the actual sample data to the created SndHandle before it can be used to
     * record or play.
     * @see "QuickTime::SetupSndHeader"
     * @param numChannels the number of channels for the sound
     * @param sampleRate the rate at which the sound was recorded
     * @param sampleSize The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param baseNote The base note for the sound, expressed as a MIDI note value.
     */
    public SndHandle(int paramInt1, float paramFloat, int paramInt2, int paramInt3, int paramInt4) throws QTException {
        super(100, false);
        int i = setupHeader(paramInt1, paramFloat, paramInt2, paramInt3, paramInt4, 0);
        setSize(i);
    }

    /**
     * You can use the SetupSndHeader function to construct a sound resource containing sampled sound that can be passed to the SndPlay function.
     * @see "QuickTime::SetupSndHeader"
     * @param numChannels the number of channels for the sound
     * @param sampleRate the rate at which the sound was recorded
     * @param sampleSize The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param numBytes The number of bytes of audio data that are to be stored in the handle. (This value is not necessarily the same as the number of samples in the sound.)
     * @return the size (in bytes) of the 'snd ' resource header that is created
     * @throws quicktime.sound.SoundException
     */
    public int setupHeader(int paramInt1, float paramFloat, int paramInt2, int paramInt3, int paramInt4) throws SoundException {
        return setupHeader(paramInt1, paramFloat, paramInt2, paramInt3, 60, paramInt4);
    }

    /**
     * You can use the SetupSndHeader function to construct a sound resource containing sampled sound that can be passed to the SndPlay function.
     * @see "QuickTime::SetupSndHeader"
     * @param numChannels the number of channels for the sound
     * @param sampleRate the rate at which the sound was recorded
     * @param sampleSize The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param baseNote The base note for the sound, expressed as a MIDI note value.
     * @param numBytes The number of bytes of audio data that are to be stored in the handle. (This value is not necessarily the same as the number of samples in the sound.)
     * @return the size (in bytes) of the 'snd ' resource header that is created
     * @throws quicktime.sound.SoundException
     */
    public int setupHeader(int paramInt1, float paramFloat, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws SoundException {
        short[] arrayOfShort = { 0 };
        SoundException.checkError(SetupSndHeader(_ID(), (short) paramInt1, QTUtils.X2UFix(paramFloat), (short) paramInt2, paramInt3, (short) paramInt4, paramInt5, arrayOfShort));
        return arrayOfShort[0];
    }

    /**
     * The number of bytes from the beginning of the sound resource specified by the sndHdl parameter to the sound header that is contained within.
     * @see "QuickTime::GetSoundHeaderOffset"
     * @return the offset to the sound header
     * @throws quicktime.sound.SoundException
     */
    public final int getSoundHeaderOffset() throws SoundException {
        int[] arrayOfInt = { 0 };
        SoundException.checkError(GetSoundHeaderOffset(_ID(), arrayOfInt));
        return arrayOfInt[0];
    }

    /**
     * Return a Pointer to the sound data that is contained within the SndHandle.
     * The SndHandle will be locked by this call and should remain locked for the
     * life of the returned data object.
     * @return a QTPointer to the Sound data
     * @throws quicktime.QTException
     */
    public QTPointerRef getSoundData() throws QTException {
        if (this.buf != null) {
            return this.buf;
        }
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        if (arrayOfInt2[0] >= getSize())
            throw new QTException("No data in SoundHandle yet");
        lock();
        return toQTPointer(arrayOfInt2[0], getSize() - arrayOfInt2[0]);
    }

    /**
     * Appends the sound buffer to the end of this handle. This is required
     * before the SndHandle can be used for recording or playing sound.
     * If a pre-existing buffer has been appended this will be removed and
     * replace with the incoming buffer. This will copy the incoming buffer.
     * @param buffer the buffer that contains the actual sound data.
     * @throws quicktime.QTException
     */
    public void appendSoundData(QTPointerRef paramQTPointerRef) throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        if (paramQTPointerRef != null) {
            setSize(arrayOfInt2[0] + paramQTPointerRef.getSize());
            BlockMove(QTObject.ID(paramQTPointerRef), lockAndDeref(arrayOfInt2[0]), paramQTPointerRef.getSize());
        } else {
            setSize(getSoundHeaderOffset());
        }
        this.buf = null;
        setIntAt(getSoundHeaderOffset(), 0);
    }

    /**
     * Appends the specified number of bytes to the SoundHandle after
     * the header. The call resizes the SndHandle to the sound header offset + buffer size.
     * @param bufferSize the number of bytes to append for the sound buffer
     * @throws quicktime.QTException
     */
    public void appendSoundBuffer(int paramInt) throws QTException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        setSize(arrayOfInt2[0] + paramInt);
        this.buf = null;
        setIntAt(getSoundHeaderOffset(), 0);
    }

    /**
     * Sets the internal buffer Pointer of this sound header to the incomding buffer.
     * This does NOT copy memory and is therefore the most efficient means of storing the data.
     * @param buffer the buffer that contains the actual sound data.
     * @throws quicktime.QTException
     */
    public void setSoundData(QTPointerRef paramQTPointerRef) throws QTException {
        int i = getSoundHeaderOffset();
        setIntAt(i, QTObject.ID(paramQTPointerRef));
        this.buf = paramQTPointerRef;
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        setSize(arrayOfInt2[0]);
    }

    /**
     * You use this function to retrieve information about a SndHandle.
     * @see "QuickTime::ParseSndHeader"
     * @return a SndInfo object
     * @throws quicktime.sound.SoundException
     */
    public final SndInfo parseSndHeader() throws SoundException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        return new SndInfo(localSoundComponentData, arrayOfInt1[0], arrayOfInt2[0]);
    }

    private static short SetupSndHeader(int paramInt1, short paramShort1, int paramInt2, short paramShort2, int paramInt3, short paramShort3, int paramInt4, short[] paramArrayOfShort) {
        return SoundManager.SetupSndHeader(paramInt1, paramShort1, paramInt2, paramShort2, paramInt3, paramShort3, paramInt4, paramArrayOfShort);
    }

    private static short GetSoundHeaderOffset(int paramInt, int[] paramArrayOfInt) {
        return SoundManager.GetSoundHeaderOffset(paramInt, paramArrayOfInt);
    }

    private static void BlockMove(int paramInt1, int paramInt2, int paramInt3) {
        MemoryManager.BlockMove(paramInt1, paramInt2, paramInt3);
    }

    private static short ParseSndHeader(int paramInt, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        return SoundManager.ParseSndHeader(paramInt, paramArrayOfByte, paramArrayOfInt1, paramArrayOfInt2);
    }
}

