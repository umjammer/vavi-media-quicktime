package quicktime.std.movies.media;

import quicktime.QTException;

/**
 * Media Handler classes that implement this interface provide support
 * for getting and setting balance in their sound output.
 */
public abstract interface AudioMediaHandler {

    /**
     * Returns the balance of audio media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right)
     * QuickTime::MediaGetSoundBalance()
     * @return the balance value
     * @return {@link quicktime.QTException}
     */
    public abstract float getBalance() throws QTException;

    /**
     * Sets the balance of audio media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right)
     * QuickTime::MediaSetSoundBalance()
     * @param balance the new balance value.
     * @param {@link quicktime.QTException}
     */
    public abstract void setBalance(float paramFloat) throws QTException;

    /**
     * Gets Sound Level Meter state (Not Documented).
     * QuickTime::MediaGetSoundLevelMeteringEnabled
     * @return the balance value
     * @return {@link quicktime.QTException}
     */
    public abstract boolean getSoundLevelMeteringEnabled() throws QTException;

    /**
     * Enables Sound Level Meter (Not Documented).
     * QuickTime::MediaSetSoundLevelMeteringEnabled
     * @param enabled true if enabled
     * @param {@link quicktime.QTException}
     */
    public abstract void setSoundLevelMeteringEnabled(boolean paramBoolean) throws QTException;

    /**
     * Gets Sound Level Meter state (Not Documented).
     * QuickTime::MediaGetSoundLevelMeterInfo
     * @return LevelMeterInfo object
     * @return {@link quicktime.QTException}
     */
    public abstract LevelMeterInfo getSoundLevelMeterInfo() throws QTException;

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with the freq. (Not Documented).
     * QuickTime::MediaGetSoundEqualizerBands
     * @return MediaEQSpectrumBandsRecord object
     * @return {@link quicktime.QTException}
     */
    public abstract MediaEQSpectrumBands getSoundEqualizerBands(int paramInt) throws QTException;

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with array of freq. (Not Documented).
     * QuickTime::MediaSetSoundEqualizerBands
     * @return MediaEQSpectrumBandsRecord object
     * @return {@link quicktime.QTException}
     */
    public abstract void setSoundEqualizerBands(MediaEQSpectrumBands paramMediaEQSpectrumBands) throws QTException;

    /**
     * Sets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     * QuickTime::MediaSetSoundBassAndTreble
     * @param bass the sound bass
     * @param treble the sound treble
     * @param {@link quicktime.QTException}
     */
    public abstract void setSoundBassAndTreble(int paramInt1, int paramInt2) throws QTException;

    /**
     * Gets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     * QuickTime::MediaGetSoundBassAndTreble
     * @return an array on length 2, containing 1st element as bass and 2nd element as treble.
     * @return {@link quicktime.QTException}
     */
    public abstract int[] getSoundBassAndTreble() throws QTException;

    /**
     * Gets SoundEqualizer BandLevels . the levels is equal to the bands count ( from the getSoundEqualizerBands)
     * you pass in the expected count of levels and it would return the array of levels of count length having
     * band levels.(Not Documented)
     * QuickTime::MediaGetSoundEqualizerBandLevels
     * @return bandLevels
     * @return {@link quicktime.QTException}
     */
    public abstract int[] getSoundEqualizerBandLevels(int paramInt) throws QTException;
}

