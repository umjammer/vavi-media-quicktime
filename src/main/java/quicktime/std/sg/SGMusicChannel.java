
package quicktime.std.sg;

import java.security.AccessController;
import java.security.PrivilegedAction;

import quicktime.QTException;
import quicktime.jdirect.QTNative;
import quicktime.jdirect.QuickTimeLib;
import quicktime.std.StdQTException;
import quicktime.std.movies.media.MusicDescription;
import quicktime.std.movies.media.SampleDescription;
import quicktime.std.music.ToneDescription;


public final class SGMusicChannel extends AudioChannel implements QuickTimeLib {
    private static Object linkage;

    static {
        new Object() {
            void establish() {
                linkage = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        return QTNative.linkNativeMethods(SGMusicChannel.class);
                    }
                });
            }
        }.establish();
    }

    SGMusicChannel(int paramInt, SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramInt, paramSequenceGrabber);
    }

    public SGMusicChannel(SequenceGrabber paramSequenceGrabber) throws StdQTException {
        super(paramSequenceGrabber, 1836413801);
    }

    final SampleDescription makeDescription() throws QTException {
        return new MusicDescription();
    }

    public final MusicDescription getMusicDescription() throws QTException {
        return (MusicDescription) getSampleDescription();
    }

    public ToneDescription getInstrument() throws StdQTException {
        ToneDescription localToneDescription = new ToneDescription();
        int i = SGGetInstrument(_ID(), localToneDescription.getBytes());
        StdQTException.checkError(i);
        return localToneDescription;
    }

    public void setInstrument(ToneDescription paramToneDescription) throws StdQTException {
        int i = SGSetInstrument(_ID(), paramToneDescription.getBytes());
        StdQTException.checkError(i);
    }

    private static native int SGGetInstrument(int paramInt, byte[] paramArrayOfByte);

    private static native int SGSetInstrument(int paramInt, byte[] paramArrayOfByte);
}
