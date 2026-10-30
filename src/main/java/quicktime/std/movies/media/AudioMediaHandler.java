package quicktime.std.movies.media;

import quicktime.QTException;

/**
 * Media Handler classes that implement this interface provide support
 * for getting and setting balance in their sound output.
 */
public interface AudioMediaHandler {

    /**
     * Returns the balance of audio media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right)
     * @see "QuickTime::MediaGetSoundBalance()"
     * @return the balance value
     * @throws quicktime.QTException
     */
    float getBalance() throws QTException;

    /**
     * Sets the balance of audio media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right)
     * @see "QuickTime::MediaSetSoundBalance()"
     * @param balance the new balance value.
     * @throws quicktime.QTException
     */
    void setBalance(float balance) throws QTException;

    /**
     * Gets Sound Level Meter state (Not Documented).
     * @see "QuickTime::MediaGetSoundLevelMeteringEnabled"
     * @return the balance value
     * @throws quicktime.QTException
     */
    boolean getSoundLevelMeteringEnabled() throws QTException;

    /**
     * Enables Sound Level Meter (Not Documented).
     * @see "QuickTime::MediaSetSoundLevelMeteringEnabled"
     * @param enabled true if enabled
     * @throws quicktime.QTException
     */
    void setSoundLevelMeteringEnabled(boolean enabled) throws QTException;

    /**
     * Gets Sound Level Meter state (Not Documented).
     * @see "QuickTime::MediaGetSoundLevelMeterInfo"
     * @return LevelMeterInfo object
     * @throws quicktime.QTException
     */
    LevelMeterInfo getSoundLevelMeterInfo() throws QTException;

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with the freq. (Not Documented).
     * @see "QuickTime::MediaGetSoundEqualizerBands"
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.QTException
     */
    MediaEQSpectrumBands getSoundEqualizerBands(int count) throws QTException;

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with array of freq. (Not Documented).
     * @see "QuickTime::MediaSetSoundEqualizerBands"
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.QTException
     */
    void setSoundEqualizerBands(MediaEQSpectrumBands soundEqualizerBands) throws QTException;

    /**
     * Sets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     * @see "QuickTime::MediaSetSoundBassAndTreble"
     * @param bass the sound bass
     * @param treble the sound treble
     * @throws quicktime.QTException
     */
    void setSoundBassAndTreble(int bass, int treble) throws QTException;

    /**
     * Gets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     * @see "QuickTime::MediaGetSoundBassAndTreble"
     * @return an array on length 2, containing 1st element as bass and 2nd element as treble.
     * @throws quicktime.QTException
     */
    int[] getSoundBassAndTreble() throws QTException;

    /**
     * Gets SoundEqualizer BandLevels . the levels is equal to the bands count ( from the getSoundEqualizerBands)
     * you pass in the expected count of levels and it would return the array of levels of count length having
     * band levels.(Not Documented)
     * @see "QuickTime::MediaGetSoundEqualizerBandLevels"
     * @return bandLevels
     * @throws quicktime.QTException
     */
    int[] getSoundEqualizerBandLevels(int numBands) throws QTException;
}
