package quicktime.std.movies.media;

import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


public class MPEGMediaHandler extends VideoMediaHandler implements QuickTimeLib, AudioMediaHandler {

    MPEGMediaHandler(int nativeRep, Object ref) {
        super(nativeRep, ref);
    }

    /**
     * Returns the balance of a sound media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right)
     *
     * @return the balance value
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaGetSoundBalance()"
     */
    @Override
    public final float getBalance() throws StdQTException {
        short[] balance = new short[1];
        StdQTException.checkError(MediaGetSoundBalance(_ID(), balance));
        return QTUtils.ShortFix2X(balance[0]);
    }

    /**
     * Sets the balance of a sound media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right)
     *
     * @param balance the new balance value.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaSetSoundBalance()"
     */
    @Override
    public final void setBalance(float balance) throws StdQTException {
        StdQTException.checkError(MediaSetSoundBalance(_ID(), QTUtils.X2ShortFix(balance)));
    }

    /**
     * Gets Sound Level Meter state (Not Documented).
     *
     * @return the balance value
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaGetSoundLevelMeteringEnabled"
     */
    @Override
    public final boolean getSoundLevelMeteringEnabled() throws StdQTException {
        byte[] enabled = {0};
        StdQTException.checkError(MediaGetSoundLevelMeteringEnabled(_ID(), enabled));
        return enabled[0] != 0;
    }

    /**
     * Enables Sound Level Meter (Not Documented).
     *
     * @param enabled true if enabled
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaSetSoundLevelMeteringEnabled"
     */
    @Override
    public final void setSoundLevelMeteringEnabled(boolean enabled) throws StdQTException {
        StdQTException.checkError(MediaSetSoundLevelMeteringEnabled(_ID(), (byte) (enabled ? 1 : 0)));
    }

    /**
     * Gets Sound Level Meter state (Not Documented).
     *
     * @return LevelMeterInfo object
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaGetSoundLevelMeterInfo"
     */
    @Override
    public final LevelMeterInfo getSoundLevelMeterInfo() throws StdQTException {
        byte[] info = new byte[4];
        int i = MediaGetSoundLevelMeterInfo(_ID(), info);
        StdQTException.checkError(i);
        return new LevelMeterInfo(info);
    }

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with the freq. (Not Documented).
     *
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaGetSoundEqualizerBands"
     */
    @Override
    public final MediaEQSpectrumBands getSoundEqualizerBands(int count) throws StdQTException {
        MediaEQSpectrumBands soundEqualizerBands = new MediaEQSpectrumBands(count);
        StdQTException.checkError(MediaGetSoundEqualizerBands(_ID(), soundEqualizerBands.getBytes()));
        return soundEqualizerBands;
    }

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with array of freq. (Not Documented).
     *
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaSetSoundEqualizerBands"
     */
    @Override
    public final void setSoundEqualizerBands(MediaEQSpectrumBands soundEqualizerBands) throws StdQTException {
        StdQTException.checkError(MediaSetSoundEqualizerBands(_ID(), soundEqualizerBands.getBytes()));
    }

    /**
     * Sets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     *
     * @param bass   the sound bass
     * @param treble the sound treble
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaSetSoundBassAndTreble"
     */
    @Override
    public final void setSoundBassAndTreble(int bass, int treble) throws StdQTException {
        StdQTException.checkError(MediaSetSoundBassAndTreble(_ID(), (short) bass, (short) treble));
    }

    /**
     * Gets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     *
     * @return an array on length 2, containing 1st element as bass and 2nd element as treble.
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaGetSoundBassAndTreble"
     */
    @Override
    public final int[] getSoundBassAndTreble() throws StdQTException {
        short[] bass = {0};
        short[] treble = {0};
        StdQTException.checkError(MediaGetSoundBassAndTreble(_ID(), bass, treble));
        int[] soundBassAndTreble = {bass[0], treble[0]};
        return soundBassAndTreble;
    }

    /**
     * Gets SoundEqualizer BandLevels . the levels is equal to the bands count ( from the getSoundEqualizerBands)
     * you pass in the expected count of levels and it would return the array of levels of count length having
     * band levels.(Not Documented)
     *
     * @return bandLevels
     * @throws quicktime.std.StdQTException
     * @see "QuickTime::MediaGetSoundEqualizerBandLevels"
     */
    @Override
    public final int[] getSoundEqualizerBandLevels(int numBands) throws StdQTException {
        byte[] levels = new byte[numBands];
        StdQTException.checkError(MediaGetSoundEqualizerBandLevels(_ID(), levels));
        int[] soundEqualizerBandLevels = new int[numBands];
        for (int i = 0; i < soundEqualizerBandLevels.length; i++) {
            soundEqualizerBandLevels[i] = QTUtils.UByte2Int(levels[i]);
        }
        return soundEqualizerBandLevels;
    }

    private static int MediaGetSoundBalance(int mh, short[] balance) {
        return MediaHandlerManager.MediaGetSoundBalance(mh, balance);
    }

    private static int MediaSetSoundBalance(int mh, short balance) {
        return MediaHandlerManager.MediaSetSoundBalance(mh, balance);
    }

    private static int MediaGetSoundLevelMeteringEnabled(int mh, byte[] enabled) {
        return MediaHandlerManager.MediaGetSoundLevelMeteringEnabled(mh, enabled);
    }

    private static int MediaSetSoundLevelMeteringEnabled(int mh, byte enabled) {
        return MediaHandlerManager.MediaSetSoundLevelMeteringEnabled(mh, enabled);
    }

    private static int MediaGetSoundEqualizerBands(int mh, byte[] bands) {
        return MediaHandlerManager.MediaGetSoundEqualizerBands(mh, bands);
    }

    private static int MediaSetSoundEqualizerBands(int mh, byte[] bands) {
        return MediaHandlerManager.MediaSetSoundEqualizerBands(mh, bands);
    }

    private static int MediaSetSoundBassAndTreble(int mh, short bass, short treble) {
        return MediaHandlerManager.MediaSetSoundBassAndTreble(mh, bass, treble);
    }

    private static int MediaGetSoundBassAndTreble(int mh, short[] bass, short[] treble) {
        return MediaHandlerManager.MediaGetSoundBassAndTreble(mh, bass, treble);
    }

    private static int MediaGetSoundLevelMeterInfo(int mh, byte[] info) {
        return MediaHandlerManager.MediaGetSoundLevelMeterInfo(mh, info);
    }

    private static int MediaGetSoundEqualizerBandLevels(int mh, byte[] levels) {
        return MediaHandlerManager.MediaGetSoundEqualizerBandLevels(mh, levels);
    }
}
