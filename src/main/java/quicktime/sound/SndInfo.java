package quicktime.sound;

import quicktime.QTObject;
import quicktime.internal.toolbox.SoundManager;
import quicktime.io.OpenFile;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTUtils;


/**
 * This class returns Sound Header information as parsed from an Snd resource
 * or an AIFF Sound file
 */
public final class SndInfo implements SoundLib, InterfaceLib {

    /**
     * The general characteristics of the sound data
     */
    public SoundComponentData sndData;

    /**
     * The number of frames in the sample
     */
    public int numFrames;

    /**
     * The offset to the sound data
     */
    public int dataOffset;

    /**
     * The SetupAIFFHeader function creates an AIFF or AIFF-C file header, depending on the parameters passed to it:
     * Uncompressed sounds of any type are stored in AIFF format (that is, the compressionType parameter is 'NONE').
     * Compressed sounds of any type are stored in AIFF-C format (that is, the compressionType parameter is different from 'NONE').
     *
     * @param file            the open file to parse
     * @param numChannels     The number of channels for the sound; one channel is equivalent to monaural sound and two channels are equivalent to stereo sound.
     * @param sampleRate      The rate at which the sound was recorded.
     * @param sampleSize      The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param numBytes        The number of bytes of audio data that are to be stored in the Common Chunk of the AIFF or AIFF-C file. Max value is a 32-bit unsigned int.
     * @param numFrames       The number of sample frames for the sample sound. If you are using a compression type defined by Apple, you can pass 0 in this field and the appropriate value for this field will be computed automatically. Max value is a 32-bit unsigned int.
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::SetupAIFFHeader"
     */
    public static void setupAIFFHeader(OpenFile file, int numChannels, float sampleRate, int sampleSize, int compressionType, long numBytes, long numFrames) throws SoundException {
        int i = SetupAIFFHeader((short) QTObject.ID(file), (short) numChannels, QTUtils.X2UFix(sampleRate), (short) sampleSize, compressionType, (int) numBytes, (int) numFrames);
        SoundException.checkError(i);
    }

    /**
     * You use this function to retrieve information about from a AIFF Sound File.
     *
     * @param file the open file to parse
     * @return a SndInfo object
     * @throws quicktime.sound.SoundException
     * @see "QuickTime::ParseAIFFHeader"
     */
    public static SndInfo parseAIFFHeader(OpenFile file) throws SoundException {
        int[] numFrames = {0};
        int[] dataOffset = {0};
        SoundComponentData sndData = new SoundComponentData();
        SoundException.checkError(ParseAIFFHeader((short) QTObject.ID(file), sndData.getBytes(), numFrames, dataOffset));
        return new SndInfo(sndData, numFrames[0], dataOffset[0]);
    }

    SndInfo(SoundComponentData sndData, int numFrames, int dataOffset) {
        this.sndData = sndData;
        this.numFrames = numFrames;
        this.dataOffset = dataOffset;
    }

    private static short ParseAIFFHeader(short fRefNum, byte[] scd, int[] numFrames, int[] dataOffset) {
        return SoundManager.ParseAIFFHeader(fRefNum, scd, numFrames, dataOffset);
    }

    private static short SetupAIFFHeader(short fRefNum, short numChannels, int sampleRate, short sampleSize, int compressionType, int numBytes, int numFrames) {
        return SoundManager.SetupAIFFHeader(fRefNum, numChannels, sampleRate, sampleSize, compressionType, numBytes, numFrames);
    }
}
