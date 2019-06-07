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

    public abstract int[] getSoundBassAndTreble() throws QTException;

    public abstract int[] getSoundEqualizerBandLevels(int paramInt) throws QTException;
}
/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/AudioMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
