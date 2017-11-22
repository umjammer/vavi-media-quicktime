
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.sound.SPBDevice;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.movies.media.SoundDescription;
import quicktime.util.QTUtils;


public final class SGSoundChannel extends AudioChannel implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SGSoundChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGSoundChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    public SGSoundChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1936684398);
    }

    final SampleDescription makeDescription() throws QTException {
        return new SoundDescription(0);
    }

    public final SoundDescription getSoundDescription() throws QTException {
        return (SoundDescription) getSampleDescription();
    }

    public void setSoundInputParameters(int paramInt1, int paramInt2, int paramInt3) throws StdQTException {
        int i = SGSetSoundInputParameters(_ID(), (short) paramInt1, (short) paramInt2, paramInt3);

        StdQTException.checkError(i);
    }

    public SGChannelInfo getSoundInputParameters() throws StdQTException {
        short[] arrayOfShort1 = new short[1];
        short[] arrayOfShort2 = new short[1];
        int[] arrayOfInt = new int[1];
        int i = SGGetSoundInputParameters(_ID(), arrayOfShort1, arrayOfShort2, arrayOfInt);

        StdQTException.checkError(i);

        return new SGChannelInfo(arrayOfShort1[0], arrayOfShort2[0], arrayOfInt[0]);
    }

    public void setSoundInputRate(float paramFloat) throws StdQTException {
        int i = SGSetSoundInputRate(_ID(), QTUtils.X2UFix(paramFloat));
        StdQTException.checkError(i);
    }

    public float getSoundInputRate() throws StdQTException {
        int i = SGGetSoundInputRate(_ID());
        return QTUtils.UFix2X(i);
    }

    public SPBDevice getInputDriver() throws StdQTException {
        return SPBDevice.fromSoundChannel(this);
    }

    public void setRecordChunkSize(int paramInt) throws StdQTException {
        int i = SGSetSoundRecordChunkSize(_ID(), paramInt);
        StdQTException.checkError(i);
    }

    public int getRecordChunkSize() throws StdQTException {
        int i = SGGetSoundRecordChunkSize(_ID());
        return i;
    }

    public void setInputDriver(String paramString) throws StdQTException {
        int i = SGSetSoundInputDriver(_ID(), QTUtils.String2PString(paramString, 255));
        StdQTException.checkError(i);

        i = SGSoundInputDriverChanged(_ID());
        StdQTException.checkError(i);
    }

    private static native int SGSetSoundInputParameters(int paramInt1, short paramShort1, short paramShort2, int paramInt2);

    private static native int SGGetSoundInputParameters(int paramInt,
                                                        short[] paramArrayOfShort1,
                                                        short[] paramArrayOfShort2,
                                                        int[] paramArrayOfInt);

    private static native int SGSetSoundInputRate(int paramInt1, int paramInt2);

    private static native int SGGetSoundInputRate(int paramInt);

    private static native int SGSetSoundInputDriver(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSoundInputDriverChanged(int paramInt);

    private static native int SGSetSoundRecordChunkSize(int paramInt1, int paramInt2);

    private static native int SGGetSoundRecordChunkSize(int paramInt);
}

/* Location:
 * /Users/nsano/src/win32/QTJava_6.1_Win_SDK/tmp/QTJava.zip!/quicktime/std/sg/
 * SGSoundChannel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1 */
