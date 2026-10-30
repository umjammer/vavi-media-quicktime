package quicktime.sound;

import quicktime.QTException;
import quicktime.QTObject;
import quicktime.internal.toolbox.MemoryManager;
import quicktime.internal.toolbox.SoundManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTHandle;
import quicktime.util.QTPointerRef;
import quicktime.util.QTUtils;


public final class SndHandle extends QTHandle implements QuickTimeLib, SoundLib {

    private QTPointerRef buf;

    /**
     * Construct an empty handle that must be initialized by the setupHeader call.
     */
    public SndHandle() throws QTException {
        super(200, false);
    }

    /**
     * Construct an empty handle that must be initialized by the setupHeader call.
     *
     * @param size  how many bytes to allocate
     * @param clear if true the handle's memory is cleared to zeroes.
     */
    public SndHandle(int size, boolean clear) throws QTException {
        super(size, clear);
    }

    /**
     * Construct a sound resource for containing sampled sound that can be used to record and play sound.
     * You must append the actual sample data to the created SndHandle before it can be used to
     * record or play.
     *
     * @param numChannels     the number of channels for the sound
     * @param sampleRate      the rate at which the sound was recorded
     * @param sampleSize      The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @see "QuickTime::SetupSndHeader"
     */
    public SndHandle(int numChannels, float sampleRate, int sampleSize, int compressionType) throws QTException {
        this(numChannels, sampleRate, sampleSize, compressionType, 60);
    }

    /**
     * Construct a sound resource for containing sampled sound that can be used to record and play sound.
     * You must append the actual sample data to the created SndHandle before it can be used to
     * record or play.
     *
     * @param numChannels     the number of channels for the sound
     * @param sampleRate      the rate at which the sound was recorded
     * @param sampleSize      The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param baseNote        The base note for the sound, expressed as a MIDI note value.
     * @see "QuickTime::SetupSndHeader"
     */
    public SndHandle(int numChannels, float sampleRate, int sampleSize, int compressionType, int baseNote) throws QTException {
        super(100, false);
        int i = setupHeader(numChannels, sampleRate, sampleSize, compressionType, baseNote, 0);
        setSize(i);
    }

    /**
     * You can use the SetupSndHeader function to construct a sound resource containing sampled sound that can be passed to the SndPlay function.
     *
     * @param numChannels     the number of channels for the sound
     * @param sampleRate      the rate at which the sound was recorded
     * @param sampleSize      The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param numBytes        The number of bytes of audio data that are to be stored in the handle. (This value is not necessarily the same as the number of samples in the sound.)
     * @return the size (in bytes) of the 'snd ' resource header that is created
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SetupSndHeader"
     */
    public int setupHeader(int numChannels, float sampleRate, int sampleSize, int compressionType, int numBytes) throws SoundException {
        return setupHeader(numChannels, sampleRate, sampleSize, compressionType, 60, 0);
    }

    /**
     * You can use the SetupSndHeader function to construct a sound resource containing sampled sound that can be passed to the SndPlay function.
     *
     * @param numChannels     the number of channels for the sound
     * @param sampleRate      the rate at which the sound was recorded
     * @param sampleSize      The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param baseNote        The base note for the sound, expressed as a MIDI note value.
     * @param numBytes        The number of bytes of audio data that are to be stored in the handle. (This value is not necessarily the same as the number of samples in the sound.)
     * @return the size (in bytes) of the 'snd ' resource header that is created
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SetupSndHeader"
     */
    public int setupHeader(int numChannels, float sampleRate, int sampleSize, int compressionType, int baseNote, int numBytes) throws SoundException {
        short[] headerLen = {0};
        SoundException.checkError(SetupSndHeader(_ID(), (short) numChannels, QTUtils.X2UFix(sampleRate), (short) sampleSize, compressionType, (short) baseNote, numBytes, headerLen));
        return headerLen[0];
    }

    /**
     * The number of bytes from the beginning of the sound resource specified by the sndHdl parameter to the sound header that is contained within.
     *
     * @return the offset to the sound header
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::GetSoundHeaderOffset"
     */
    public int getSoundHeaderOffset() throws SoundException {
        int[] offset = {0};
        SoundException.checkError(GetSoundHeaderOffset(_ID(), offset));
        return offset[0];
    }

    /**
     * Return a Pointer to the sound data that is contained within the SndHandle.
     * The SndHandle will be locked by this call and should remain locked for the
     * life of the returned data object.
     *
     * @return a QTPointer to the Sound data
     * @throws quicktime.QTException
     */
    public QTPointerRef getSoundData() throws QTException {
        if (this.buf != null) {
            return this.buf;
        }
        int[] numFrames = {0};
        int[] dataOffset = {0};
        SoundComponentData soundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), soundComponentData.getBytes(), numFrames, dataOffset));
        if (dataOffset[0] >= getSize())
            throw new QTException("No data in SoundHandle yet");
        lock();
        return toQTPointer(dataOffset[0], getSize() - dataOffset[0]);
    }

    /**
     * Appends the sound buffer to the end of this handle. This is required
     * before the SndHandle can be used for recording or playing sound.
     * If a pre-existing buffer has been appended this will be removed and
     * replace with the incoming buffer. This will copy the incoming buffer.
     *
     * @param buffer the buffer that contains the actual sound data.
     * @throws quicktime.QTException
     */
    public void appendSoundData(QTPointerRef buffer) throws QTException {
        int[] numFrames = {0};
        int[] dataOffset = {0};
        SoundComponentData soundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), soundComponentData.getBytes(), numFrames, dataOffset));
        if (buffer != null) {
            setSize(dataOffset[0] + buffer.getSize());
            BlockMove(QTObject.ID(buffer), lockAndDeref(dataOffset[0]), buffer.getSize());
        } else {
            setSize(getSoundHeaderOffset());
        }
        this.buf = null;
        setIntAt(getSoundHeaderOffset(), 0);
    }

    /**
     * Appends the specified number of bytes to the SoundHandle after
     * the header. The call resizes the SndHandle to the sound header offset + buffer size.
     *
     * @param bufferSize the number of bytes to append for the sound buffer
     * @throws quicktime.QTException
     */
    public void appendSoundBuffer(int bufferSize) throws QTException {
        int[] numFrames = {0};
        int[] dataOffset = {0};
        SoundComponentData soundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), soundComponentData.getBytes(), numFrames, dataOffset));
        setSize(dataOffset[0] + bufferSize);
        this.buf = null;
        setIntAt(getSoundHeaderOffset(), 0);
    }

    /**
     * Sets the internal buffer Pointer of this sound header to the incomding buffer.
     * This does NOT copy memory and is therefore the most efficient means of storing the data.
     *
     * @param buffer the buffer that contains the actual sound data.
     * @throws quicktime.QTException
     */
    public void setSoundData(QTPointerRef buffer) throws QTException {
        int i = getSoundHeaderOffset();
        setIntAt(i, QTObject.ID(buffer));
        this.buf = buffer;
        int[] numFrames = {0};
        int[] dataOffset = {0};
        SoundComponentData soundComponentData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), soundComponentData.getBytes(), numFrames, dataOffset));
        setSize(dataOffset[0]);
    }

    /**
     * You use this function to retrieve information about a SndHandle.
     *
     * @return a SndInfo object
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::ParseSndHeader"
     */
    public SndInfo parseSndHeader() throws SoundException {
        int[] numFrames = {0};
        int[] dataOffset = {0};
        SoundComponentData sndData = new SoundComponentData();
        SoundException.checkError(ParseSndHeader(_ID(), sndData.getBytes(), numFrames, dataOffset));
        return new SndInfo(sndData, numFrames[0], dataOffset[0]);
    }

    private static short SetupSndHeader(int sndHandle, short numChannels, int sampleRate, short sampleSize, int compressionType, short baseNote, int numBytes, short[] headerLen) {
        return SoundManager.SetupSndHeader(sndHandle, numChannels, sampleRate, sampleSize, compressionType, baseNote, numBytes, headerLen);
    }

    private static short GetSoundHeaderOffset(int sndHandle, int[] offset) {
        return SoundManager.GetSoundHeaderOffset(sndHandle, offset);
    }

    private static void BlockMove(int src, int dst, int length) {
        MemoryManager.BlockMove(src, dst, length);
    }

    private static short ParseSndHeader(int sndHandle, byte[] scd, int[] numFrames, int[] dataOffset) {
        return SoundManager.ParseSndHeader(sndHandle, scd, numFrames, dataOffset);
    }
}
