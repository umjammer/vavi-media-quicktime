
package quicktime.std.movies.media;

import quicktime.QTException;


public abstract interface AudioMediaHandler {
    public abstract float getBalance() throws QTException;

    public abstract void setBalance(float paramFloat) throws QTException;

    public abstract boolean getSoundLevelMeteringEnabled() throws QTException;

    public abstract void setSoundLevelMeteringEnabled(boolean paramBoolean) throws QTException;

    public abstract LevelMeterInfo getSoundLevelMeterInfo() throws QTException;

    public abstract MediaEQSpectrumBands getSoundEqualizerBands(int paramInt) throws QTException;

    public abstract void setSoundEqualizerBands(MediaEQSpectrumBands paramMediaEQSpectrumBands) throws QTException;

    public abstract void setSoundBassAndTreble(int paramInt1, int paramInt2) throws QTException;

    public abstract int[] getSoundBassAndTreble() throws QTException;

    public abstract int[] getSoundEqualizerBandLevels(int paramInt) throws QTException;
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/AudioMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
