package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;
import quicktime.internal.toolbox.MediaHandlerManager;
import quicktime.jdirect.PrimitivesLib;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;

public class SoundMediaHandler extends MediaHandler implements QuickTimeLib, AudioMediaHandler, PrimitivesLib {

    private static Object linkage;

    static {
        new Object() {

            void establish() {
                linkage = AccessController.doPrivileged((PrivilegedAction<Object>) () -> QTNative.linkNativeMethods(SoundMediaHandler.class));
            }
        }.establish();
    }

    SoundMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    /**
     * Returns the balance of a sound media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right).
     * @see "QuickTime::MediaGetSoundBalance()"
     * @return the balance value
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final float getBalance() throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(MediaGetSoundBalance(_ID(), arrayOfShort));
        return arrayOfShort[0] / 128;
    }

    /**
     * Sets the balance of a sound media. Range from -1.0 (Left) to 0 (Equal) to 1.0 (Right).
     * @see "QuickTime::MediaSetSoundBalance()"
     * @param balance the new balance value.
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final void setBalance(float balance) throws StdQTException {
        StdQTException.checkError(MediaSetSoundBalance(_ID(), (short) (int) (balance * 128.0F)));
    }

    /**
     * Gets Sound Level Meter state (Not Documented).
     * @see "QuickTime::MediaGetSoundLevelMeteringEnabled"
     * @return the balance value
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final boolean getSoundLevelMeteringEnabled() throws StdQTException {
        byte[] arrayOfByte = { 0 };
        StdQTException.checkError(MediaGetSoundLevelMeteringEnabled(_ID(), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    /**
     * Enables Sound Level Meter (Not Documented).
     * @see "QuickTime::MediaSetSoundLevelMeteringEnabled"
     * @param enabled true if enabled
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final void setSoundLevelMeteringEnabled(boolean enabled) throws StdQTException {
        StdQTException.checkError(MediaSetSoundLevelMeteringEnabled(_ID(), (byte) (enabled ? 1 : 0)));
    }

    /**
     * Gets Sound Level Meter state (Not Documented).
     * @see "QuickTime::MediaGetSoundLevelMeterInfo"
     * @return LevelMeterInfo object
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final LevelMeterInfo getSoundLevelMeterInfo() throws StdQTException {
        byte[] arrayOfByte = new byte[4];
        int i = MediaGetSoundLevelMeterInfo(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return new LevelMeterInfo(arrayOfByte);
    }

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with the freq. (Not Documented).
     * @see "QuickTime::MediaGetSoundEqualizerBands"
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final MediaEQSpectrumBands getSoundEqualizerBands(int paramInt) throws StdQTException {
        MediaEQSpectrumBands localMediaEQSpectrumBands = new MediaEQSpectrumBands(paramInt);
        StdQTException.checkError(MediaGetSoundEqualizerBands(_ID(), localMediaEQSpectrumBands.getBytes()));
        return localMediaEQSpectrumBands;
    }

    /**
     * Pass in the count of bands you expect to get back, it would return the the actual value of bands available
     * with array of freq. (Not Documented).
     * @see "QuickTime::MediaSetSoundEqualizerBands"
     * @return MediaEQSpectrumBandsRecord object
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final void setSoundEqualizerBands(MediaEQSpectrumBands paramMediaEQSpectrumBands) throws StdQTException {
        StdQTException.checkError(MediaSetSoundEqualizerBands(_ID(), paramMediaEQSpectrumBands.getBytes()));
    }

    /**
     * Sets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     * @see "QuickTime::MediaSetSoundBassAndTreble"
     * @param bass the sound bass
     * @param treble the sound treble
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final void setSoundBassAndTreble(int bass, int treble) throws StdQTException {
        StdQTException.checkError(MediaSetSoundBassAndTreble(_ID(), (short) bass, (short) treble));
    }

    /**
     * Gets  Sound Bass and Treble (Not Documented).
     * Range is -1 to 1 where 0 is unity gain, 256 is maximum boost, -256 is maximum attenuation.
     * @see "QuickTime::MediaGetSoundBassAndTreble"
     * @return an array on length 2, containing 1st element as bass and 2nd element as treble.
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final int[] getSoundBassAndTreble() throws StdQTException {
        short[] arrayOfShort1 = { 0 };
        short[] arrayOfShort2 = { 0 };
        StdQTException.checkError(MediaGetSoundBassAndTreble(_ID(), arrayOfShort1, arrayOfShort2));
        int[] arrayOfInt = { arrayOfShort1[0], arrayOfShort2[0] };
        return arrayOfInt;
    }

    /**
     * Gets SoundEqualizer BandLevels . the levels is equal to the bands count ( from the getSoundEqualizerBands)
     * you pass in the expected count of levels and it would return the array of levels of count length having
     * band levels.(Not Documented)
     * @see "QuickTime::MediaGetSoundEqualizerBandLevels"
     * @return bandLevels
     * @throws quicktime.std.StdQTException
     */
    @Override
    public final int[] getSoundEqualizerBandLevels(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[paramInt];
        synchronized (QTNative.globalsLock) {
            StdQTException.checkError(MediaGetSoundEqualizerBandLevels(_ID(), arrayOfByte));
        }
        int[] x = new int[paramInt];
        for (int i = 0; i < x.length; i++) {
            x[i] = QTUtils.UByte2Int(arrayOfByte[i]);
        }
        return x;
    }

    private static int MediaGetSoundBalance(int paramInt, short[] paramArrayOfShort) {
        return MediaHandlerManager.MediaGetSoundBalance(paramInt, paramArrayOfShort);
    }

    private static int MediaSetSoundBalance(int paramInt, short paramShort) {
        return MediaHandlerManager.MediaSetSoundBalance(paramInt, paramShort);
    }

    private static int MediaGetSoundLevelMeteringEnabled(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaGetSoundLevelMeteringEnabled(paramInt, paramArrayOfByte);
    }

    private static int MediaSetSoundLevelMeteringEnabled(int paramInt, byte paramByte) {
        return MediaHandlerManager.MediaSetSoundLevelMeteringEnabled(paramInt, paramByte);
    }

    private static int MediaGetSoundEqualizerBands(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaGetSoundEqualizerBands(paramInt, paramArrayOfByte);
    }

    private static int MediaSetSoundEqualizerBands(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaSetSoundEqualizerBands(paramInt, paramArrayOfByte);
    }

    private static int MediaSetSoundBassAndTreble(int paramInt, short paramShort1, short paramShort2) {
        return MediaHandlerManager.MediaSetSoundBassAndTreble(paramInt, paramShort1, paramShort2);
    }

    private static int MediaGetSoundBassAndTreble(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2) {
        return MediaHandlerManager.MediaGetSoundBassAndTreble(paramInt, paramArrayOfShort1, paramArrayOfShort2);
    }

    private static int MediaGetSoundLevelMeterInfo(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaGetSoundLevelMeterInfo(paramInt, paramArrayOfByte);
    }

    private static int MediaGetSoundEqualizerBandLevels(int paramInt, byte[] paramArrayOfByte) {
        return MediaHandlerManager.MediaGetSoundEqualizerBandLevels(paramInt, paramArrayOfByte);
    }
}
