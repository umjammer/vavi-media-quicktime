
package quicktime.std.movies.media;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.util.QTUtils;


public class MPEGMediaHandler extends VideoMediaHandler implements QuickTimeLib, AudioMediaHandler {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(MPEGMediaHandler.class);
                    }
                });
            }
        }.establish();
    }

    MPEGMediaHandler(int paramInt, Object paramObject) {
        super(paramInt, paramObject);
    }

    public final float getBalance() throws StdQTException {
        short[] arrayOfShort = new short[1];
        StdQTException.checkError(MediaGetSoundBalance(_ID(), arrayOfShort));
        return QTUtils.ShortFix2X(arrayOfShort[0]);
    }

    public final void setBalance(float paramFloat) throws StdQTException {
        StdQTException.checkError(MediaSetSoundBalance(_ID(), QTUtils.X2ShortFix(paramFloat)));
    }

    public final boolean getSoundLevelMeteringEnabled() throws StdQTException {
        byte[] arrayOfByte = {
            0
        };
        StdQTException.checkError(MediaGetSoundLevelMeteringEnabled(_ID(), arrayOfByte));
        return arrayOfByte[0] != 0;
    }

    public final void setSoundLevelMeteringEnabled(boolean paramBoolean) throws StdQTException {
        StdQTException.checkError(MediaSetSoundLevelMeteringEnabled(_ID(), (byte) (paramBoolean ? 1 : 0)));
    }

    public final LevelMeterInfo getSoundLevelMeterInfo() throws StdQTException {
        byte[] arrayOfByte = new byte[4];
        int i = MediaGetSoundLevelMeterInfo(_ID(), arrayOfByte);
        StdQTException.checkError(i);
        return new LevelMeterInfo(arrayOfByte);
    }

    public final MediaEQSpectrumBands getSoundEqualizerBands(int paramInt) throws StdQTException {
        MediaEQSpectrumBands localMediaEQSpectrumBands = new MediaEQSpectrumBands(paramInt);

        StdQTException.checkError(MediaGetSoundEqualizerBands(_ID(), localMediaEQSpectrumBands.getBytes()));
        return localMediaEQSpectrumBands;
    }

    public final void setSoundEqualizerBands(MediaEQSpectrumBands paramMediaEQSpectrumBands) throws StdQTException {
        StdQTException.checkError(MediaSetSoundEqualizerBands(_ID(), paramMediaEQSpectrumBands.getBytes()));
    }

    public final void setSoundBassAndTreble(int paramInt1, int paramInt2) throws StdQTException {
        StdQTException.checkError(MediaSetSoundBassAndTreble(_ID(), (short) paramInt1, (short) paramInt2));
    }

    public final int[] getSoundBassAndTreble() throws StdQTException {
        short[] arrayOfShort1 = {
            0
        };
        short[] arrayOfShort2 = {
            0
        };

        StdQTException.checkError(MediaGetSoundBassAndTreble(_ID(), arrayOfShort1, arrayOfShort2));
        int[] arrayOfInt = {
            arrayOfShort1[0], arrayOfShort2[0]
        };
        return arrayOfInt;
    }

    public final int[] getSoundEqualizerBandLevels(int paramInt) throws StdQTException {
        byte[] arrayOfByte = new byte[paramInt];
        StdQTException.checkError(MediaGetSoundEqualizerBandLevels(_ID(), arrayOfByte));

        int[] arrayOfInt = new int[paramInt];
        for (int i = 0; i < arrayOfInt.length; i++) {
            arrayOfInt[i] = QTUtils.UByte2Int(arrayOfByte[i]);
        }
        return arrayOfInt;
    }

    private static native int MediaGetSoundBalance(int paramInt, short[] paramArrayOfShort);

    private static native int MediaSetSoundBalance(int paramInt, short paramShort);

    private static native int MediaGetSoundLevelMeteringEnabled(int paramInt, byte[] paramArrayOfByte);

    private static native int MediaSetSoundLevelMeteringEnabled(int paramInt, byte paramByte);

    private static native int MediaGetSoundEqualizerBands(int paramInt, byte[] paramArrayOfByte);

    private static native int MediaSetSoundEqualizerBands(int paramInt, byte[] paramArrayOfByte);

    private static native int MediaSetSoundBassAndTreble(int paramInt, short paramShort1, short paramShort2);

    private static native int MediaGetSoundBassAndTreble(int paramInt, short[] paramArrayOfShort1, short[] paramArrayOfShort2);

    private static native int MediaGetSoundLevelMeterInfo(int paramInt, byte[] paramArrayOfByte);

    private static native int MediaGetSoundEqualizerBandLevels(int paramInt, byte[] paramArrayOfByte);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/
 * movies/media/MPEGMediaHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
