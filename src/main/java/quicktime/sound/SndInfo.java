package quicktime.sound;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.QTObject;
import quicktime.io.OpenFile;
import quicktime.jdirect.InterfaceLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.SoundLib;
import quicktime.util.QTUtils;

/**
 * This class returns Sound Header information as parsed from an Snd resource
 * or an AIFF Sound file
 */
public final class SndInfo implements SoundLib, InterfaceLib {

    private static Object linkage;

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

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                    public Object run() {
                        return QTNative.linkNativeMethods(SndInfo.class);
                    }
                });
            }
        }.establish();
    }

    /**
     * The SetupAIFFHeader function creates an AIFF or AIFF-C file header, depending on the parameters passed to it:
     * Uncompressed sounds of any type are stored in AIFF format (that is, the compressionType parameter is 'NONE').
     * Compressed sounds of any type are stored in AIFF-C format (that is, the compressionType parameter is different from 'NONE').
     * QuickTime::SetupAIFFHeader
     * @param file the open file to parse
     * @param numChannels The number of channels for the sound; one channel is equivalent to monaural sound and two channels are equivalent to stereo sound.
     * @param sampleRate The rate at which the sound was recorded.
     * @param sampleSize The sample size for the original sound (that is, bits per sample).
     * @param compressionType The compression type for the sound ('NONE', 'MAC3', 'MAC6', or other third-party types).
     * @param numBytes The number of bytes of audio data that are to be stored in the Common Chunk of the AIFF or AIFF-C file. Max value is a 32-bit unsigned int.
     * @param numFrames The number of sample frames for the sample sound. If you are using a compression type defined by Apple, you can pass 0 in this field and the appropriate value for this field will be computed automatically. Max value is a 32-bit unsigned int.
     * @param {@link quicktime.sound.SoundException}
     */
    public static void setupAIFFHeader(OpenFile paramOpenFile, int paramInt1, float paramFloat, int paramInt2, int paramInt3, long paramLong1, long paramLong2) throws SoundException {
        int i = SetupAIFFHeader((short) QTObject.ID(paramOpenFile), (short) paramInt1, QTUtils.X2UFix(paramFloat), (short) paramInt2, paramInt3, (int) paramLong1, (int) paramLong2);
        SoundException.checkError(i);
    }

    /**
     * You use this function to retrieve information about from a AIFF Sound File.
     * QuickTime::ParseAIFFHeader
     * @param file the open file to parse
     * @return a SndInfo object
     * @return {@link quicktime.sound.SoundException}
     */
    public static SndInfo parseAIFFHeader(OpenFile paramOpenFile) throws SoundException {
        int[] arrayOfInt1 = { 0 };
        int[] arrayOfInt2 = { 0 };
        SoundComponentData localSoundComponentData = new SoundComponentData();
        SoundException.checkError(ParseAIFFHeader((short) QTObject.ID(paramOpenFile), localSoundComponentData.getBytes(), arrayOfInt1, arrayOfInt2));
        return new SndInfo(localSoundComponentData, arrayOfInt1[0], arrayOfInt2[0]);
    }

    SndInfo(SoundComponentData paramSoundComponentData, int paramInt1, int paramInt2) {
        this.sndData = paramSoundComponentData;
        this.numFrames = paramInt1;
        this.dataOffset = paramInt2;
    }

    private static native short ParseAIFFHeader(short paramShort, byte[] paramArrayOfByte, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

    private static native short SetupAIFFHeader(short paramShort1, short paramShort2, int paramInt1, short paramShort3, int paramInt2, int paramInt3, int paramInt4);
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/sound/
 * SndInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
